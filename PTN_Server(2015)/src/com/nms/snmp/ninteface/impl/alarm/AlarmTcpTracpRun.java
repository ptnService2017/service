package com.nms.snmp.ninteface.impl.alarm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nms.drive.service.bean.TunnelObject;
import com.nms.drive.service.impl.CoderUtils;
import com.nms.model.alarm.HisAlarmService_MB;
import com.nms.model.util.Services;
import com.nms.snmp.ninteface.framework.SnmpConfig;
import com.nms.snmp.ninteface.util.FileTools;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.DateUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.UiUtil;

public class AlarmTcpTracpRun implements Runnable{
	private Socket socket;
	private AlarmTcpTracpMainThread alarmTcpTracpMainThread;
	
	public AlarmTcpTracpRun(Socket socket,AlarmTcpTracpMainThread alarmTcpTracpMainThread) {
		this.socket = socket;
		this.alarmTcpTracpMainThread = alarmTcpTracpMainThread;
	}
	
	
	@Override
	public void run() {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();
			while (alarmTcpTracpMainThread.getAlarmTrapRun().get(socket.toString())) {
				byte[] tempBytes = new byte[1024];
				try {
					inputStream.read(tempBytes);
					if(tempBytes[2] ==1){//登陆
						reqLoginAlarm(outputStream,tempBytes);
					}else if(tempBytes[2] ==3){//请求同步告警
						ackSyncAlarmMsg(outputStream,tempBytes);
					}else if(tempBytes[2] ==5){//请求批量同步告警
						ackSyncAlarmFile(outputStream,tempBytes);
					}else if(tempBytes[2] ==8){//心跳请求
						ackHeartBeat(outputStream,tempBytes);
					}else if(tempBytes[2] ==10){//关闭连接通知
						alarmTcpTracpMainThread.getAlarmTrapRun().put(socket.toString(), false);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				inputStream.close();
				outputStream.close();
				socket.close();
			} catch (IOException e) {
				ExceptionManage.dispose(e, this.getClass());
			}
			
		}
	}

	private void ackSyncAlarmFile(OutputStream outputStream, byte[] tempBytes) {
		String[] reqboby = this.reqBoby(tempBytes);
		HisAlarmService_MB hisAlarmService_MB = null;
		Map<String,String> param = new HashMap<String,String>();
		List<Map<String,String>> list = null;
		String re = "";
		try {
			hisAlarmService_MB = (HisAlarmService_MB) ConstantUtil.serviceFactory.newService_MB(Services.HisAlarm);
			if(reqboby[2].contains("alarmSeq")){
				param.put("alarmSeq", reqboby[2].split("=")[1]);
			}else{
				String startTime = "";
				String endTime = "";
				if(reqboby[2].split("=").length>1){
					startTime = reqboby[2].split("=")[1];
				}
				if(reqboby[3].split("=").length>1){
					endTime = reqboby[3].split("=")[1];
				}
				param.put("startTime", startTime);
				param.put("endTime", endTime);
				param.put("syncSource", reqboby[3].split("=")[1]);
			}
			list = hisAlarmService_MB.sysNorthAlarm(param);
			if(list.size() == 0){
				re = "reqSyncAlarmFile;reqId="+reqboby[1].split("=")[1]+";result=success;resDesc=null";
				outputStream.write(res(6, re));
			}else{
				String fileName = "snmpData\\ZJ\\CS\\EB\\OMC\\FM\\"+DateUtil.getDate("yyyyMMdd")+"\\FM-OMC-1A-V1.1.0-"+DateUtil.getDate("yyyyMMdd-HHmm")+".txt";
				this.createFile(fileName, list);
				re = "ackSyncAlarmFileResult;reqId="+reqboby[1].split("=")[1]+";filename="+fileName+";result=success;resDesc=null";
				outputStream.write(res(7, re));
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}finally{
			UiUtil.closeService_MB(hisAlarmService_MB);
		}
	}

	private void createFile(String fileName,List<Map<String,String>> list){
		StringBuilder stringBuilder = new StringBuilder();
		for(Map<String,String> map : list){
			stringBuilder.append("{");
			stringBuilder.append("\"alarmTitle\":\""+map.get("alarmTitle")+"\",");
			stringBuilder.append("\"alarmStatus\":"+map.get("alarmStatus")+",");
			stringBuilder.append("\"alarmType\":\""+map.get("alarmType")+"\",");
			stringBuilder.append("\"origSeverity\":"+getLevel(map.get("origSeverity").toString())+",");
			stringBuilder.append("\"eventTime\":\""+map.get("eventTime")+"\",");
			stringBuilder.append("\"alarmId\":\""+map.get("alarmId")+"\",");
			stringBuilder.append("\"specificProblemID\":\""+map.get("specificProblemID")+"\",");
			stringBuilder.append("\"specificProblem\":\""+map.get("specificProblem")+"\",");
			stringBuilder.append("\"neUID\":\"3301EBCS1NEL"+map.get("neUID")+"\",");
			stringBuilder.append("\"neName\":\""+map.get("neName")+"\",");
			stringBuilder.append("\"neType\":\""+map.get("neType")+"\",");
			stringBuilder.append("\"objectUID\":\""+map.get("objectUID")+"\",");
			stringBuilder.append("\"objectName\":\""+map.get("objectName")+"\",");
			stringBuilder.append("\"objectType\":\""+map.get("objectType")+"\",");
			stringBuilder.append("\"locationInfo\":\""+map.get("locationInfo")+"\",");
			stringBuilder.append("\"addInfo\":\""+map.get("addInfo")+"\",");
			stringBuilder.append("\"holderType\":\""+map.get("holderType")+"\"");
			stringBuilder.append("}\r\n");
		}
		OutputStreamWriter  output = null;
		try {
			File f = new File(fileName);
			if(!f.exists()){
				f.createNewFile();// 不存在则创建
			}
			output = new OutputStreamWriter (new FileOutputStream(fileName),"UTF-8");
			output.write(stringBuilder.toString());
			output.flush();
			
			FileTools fileTools = new FileTools();
			fileTools.zipFile(fileName, fileName+".zip");
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private int getLevel(String origSeverity){
		int l = 1;
		if(origSeverity.equals("4")){
			l=2;
		}if(origSeverity.equals("3")){
			l=3;
		}if(origSeverity.equals("2")){
			l=4;
		}
		return l;
	}
	
	/**
	 * 请求同步告警答复
	 * @param outputStream
	 * @param tempBytes
	 */
	private void ackSyncAlarmMsg(OutputStream outputStream, byte[] tempBytes) {
		String[] reqboby = this.reqBoby(tempBytes);
		String re = "";
		try {
			if(reqboby.length<1){
				re = "ackSyncAlarmMsg;reqId="+reqboby[1].split("=")[1]+";result=fail;resDesc=null";
				outputStream.write(res(9, re));
			}else{
				re = "ackSyncAlarmMsg;reqId="+reqboby[1].split("=")[1]+";result=succ;resDesc=null";
				outputStream.write(res(9, re));
			}
			
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
	}


	/**
	 * 心跳答复
	 * @param outputStream
	 * @param tempBytes
	 */
	private void ackHeartBeat(OutputStream outputStream,byte[] tempBytes) {
		String[] reqboby = this.reqBoby(tempBytes);
		String re = "";
		try {
			if(reqboby.length<1){
				outputStream.write(res(9, re));
			}else{
				re = "reqHeartBeat;reqId="+reqboby[1].split("=")[1];
				outputStream.write(res(9, re));
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		
	}


	/**
	 * 请求登陆
	 * @param outputStream
	 * @param tempBytes
	 */
	private void reqLoginAlarm(OutputStream outputStream,byte[] tempBytes){
		String[] reqboby = this.reqBoby(tempBytes);
		String re = "";
		String userName = reqboby[1].split("=")[1];
		String passWord = reqboby[2].split("=")[1];
		if(reqboby.length<4){
			re = "ackLoginAlarm;result=fail;resDesc=param-error";
		}else if(!userName.equals(SnmpConfig.getInstanse().getValue("alarm.userName"))){
			re = "ackLoginAlarm;result=fail;resDesc=username-error";
		}else if(!passWord.equals(SnmpConfig.getInstanse().getValue("alarm.passWord"))){
			re = "ackLoginAlarm;result=fail;resDesc=password-error";
		}else{
			re = "ackLoginAlarm;result=succ;resDesc=null";
		}
		try {
			outputStream.write(res(1, re));
		} catch (IOException e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		
	}
	
	
	/**
	 * 返回消息体
	 * @param type
	 * @param bobys
	 * @return
	 */
	private byte[] res(int type,String bobys){
		byte[] res = null;
		try {
			byte[] boby = bobys.getBytes("utf-8");
			byte[] head = new byte[9];
			head[0] = (byte) 0xff;
			head[1] = (byte) 0xff;
			head[2] = (byte) type;
			this.getTime(head);
			byte[] length = CoderUtils.intToBytes(boby.length);
			head[7] = length[2];
			head[8] = length[3];
			res = CoderUtils.arraycopy(head, boby);
		} catch (UnsupportedEncodingException e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return res;
	}
	
	
	/**
	 * 请求消息体
	 * @param tempBytes
	 * @return
	 */
	private String[] reqBoby(byte[] tempBytes){
		byte[] lengthby = new byte[4];
		lengthby[0] = 0;
		lengthby[1] = 0;
		lengthby[2] = tempBytes[7];
		lengthby[3] = tempBytes[8];
		int length = CoderUtils.bytesToInt(lengthby);
		byte[] reboby = new byte[length];
		System.arraycopy(tempBytes, 9, reboby, 0, reboby.length);
		String reboby_s = null;
		String[] ss = null;
		try {
			reboby_s = new String(reboby, "utf-8");
			System.out.println(reboby_s);
			ss = reboby_s.split(";");
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return ss;
	}
	
	private void getTime(byte[] bs){
		String s = Long.toHexString(System.currentTimeMillis()/1000);
		for (int i = 3; i < 7; i++) {
			bs[i] = (byte) Integer.parseInt(s.substring((i-3)*2, (i-2)*2), 16);
		}
	}
	
	public static void main(String[] args) {
			String s = "{\"reqLoginAlarm\":\"user\",\"key\":\"qw#$@;type=msg\"}";
			TunnelObject object = new TunnelObject();
			object.setTunnelId(1);
			TunnelObject object2 = new TunnelObject();
			object2.setTunnelId(2);
			List<TunnelObject> list = new ArrayList<TunnelObject>();
			list.add(object);
			list.add(object2);
			String str = "aaa";
//			JsonMapper.toJsonString(list).replace("}", "}\r\n");
			System.out.println(DateUtil.getDate("yyyyMMdd"));
//			AlarmTcpTracpRun.contentToTxt("C:\\Users\\peng\\test22.txt", str);
		}
}
