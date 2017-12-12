package com.nms.snmp.ninteface.impl.alarm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import com.nms.drive.service.impl.CoderUtils;
import com.nms.snmp.ninteface.framework.SnmpConfig;
import com.nms.ui.manager.ExceptionManage;

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
				e.printStackTrace();
			}
			
		}
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
		String s = "reqLoginAlarm;user=yiy;key=qw#$@;type=msg";
		try {
			byte[] b = s.getBytes("utf-8");
			byte[] head = new byte[9];
			for (int i = 0; i < head.length; i++) {
				head[i] = 0;
			}
			head[8] = (byte) b.length;
			AlarmTcpTracpRun alarmTcpTracpRun = new AlarmTcpTracpRun(null, null);
			System.out.println(alarmTcpTracpRun.reqBoby(CoderUtils.arraycopy(head, b)));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
