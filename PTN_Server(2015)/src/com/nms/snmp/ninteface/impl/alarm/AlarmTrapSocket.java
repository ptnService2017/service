package com.nms.snmp.ninteface.impl.alarm;

import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.nms.corba.ninterface.util.DateTimeUtil;
import com.nms.drive.service.impl.CoderUtils;
import com.nms.model.alarm.CurAlarmService_MB;
import com.nms.model.alarm.HisAlarmService_MB;
import com.nms.model.util.Services;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.DateUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.UiUtil;

public class AlarmTrapSocket extends Thread{
	private static Integer alarmSeq = 0;
	private AlarmTcpTracpMainThread alarmTcpTracpMainThread;
	
	public AlarmTrapSocket(AlarmTcpTracpMainThread alarmTcpTracpMainThread) {
		this.alarmTcpTracpMainThread = alarmTcpTracpMainThread;
	}
	
	@Override
	public void run() {
		CurAlarmService_MB curAlarmService_MB = null;
		try {
			while(true){
				curAlarmService_MB = (CurAlarmService_MB) ConstantUtil.serviceFactory.newService_MB(Services.CurrentAlarm);
				List<Map<String, Object>> list = curAlarmService_MB.queryNorthRun(alarmSeq, alarmSeq>0?"":DateUtil.getDate(new Date(System.currentTimeMillis()-15000), DateUtil.FULLTIME));
				System.out.println(System.currentTimeMillis()+"alarmSeq::"+alarmSeq+"实时告警：："+list.size());
				for (Map<String,Object> map:list) {
					alarmSeq = Integer.parseInt(map.get("alarmId").toString());
					Iterator<String> is = alarmTcpTracpMainThread.getAlarmTrap().keySet().iterator();
					StringBuilder stringBuilder = new StringBuilder();
					stringBuilder.append("{");
					stringBuilder.append("\"alarmSeq\":\""+map.get("alarmId")+"\",");
					stringBuilder.append("\"alarmTitle\":\""+map.get("alarmTitle")+"\",");
					Integer status = Integer.parseInt(map.get("alarmStatus").toString())==0?1:0;
					stringBuilder.append("\"alarmStatus\":"+status+",");
					stringBuilder.append("\"alarmType\":\""+"通信告警"+"\",");
					stringBuilder.append("\"origSeverity\":"+getLevel(map.get("origSeverity").toString())+",");
					stringBuilder.append("\"eventTime\":\""+(status ==1?map.get("happenedtime"):map.get("clearedtime"))+"\",");
					stringBuilder.append("\"alarmId\":\""+map.get("alarmId")+"\",");
					stringBuilder.append("\"specificProblemID\":\""+map.get("specificProblemID")+"\",");
					stringBuilder.append("\"specificProblem\":\""+map.get("specificProblem")+"\",");
					Integer i = (Integer) map.get("objectType");
					String str = "";
					if(i==null ||i == 8|| i== 5 || i==0|| i==10|| i==7){
						str = "NEL";
					}else if(i == 0x3){
						str = "PRT";
					}else if(i == 0x40){
						str = "PW";
					}else if(i == 0x30){
						str = "TNL";
					}else if(i == 0x50 || i==0x60){
						str = "ETH";
					}else if(i == 0x70){
						str = "TDM";
					}
					
					if(map.get("neUID").toString().equals("0") && map.get("neName") == null){
						stringBuilder.append("\"neUID\":\"\",");
						stringBuilder.append("\"neName\":\"\",");
						stringBuilder.append("\"neType\":\"PTN\",");
						stringBuilder.append("\"objectUID\":\"3301EBCS1\",");
						stringBuilder.append("\"objectName\":\"EMS服务器\",");
						stringBuilder.append("\"objectType\":\"OMC\",");
						stringBuilder.append("\"locationInfo\":\"OMC\",");
						stringBuilder.append("\"addInfo\":\"\",");
						stringBuilder.append("\"holderType\":\"\",");
						stringBuilder.append("\"alarmCheck\":\"\",");
						stringBuilder.append("\"layer\":");
					}else{
						stringBuilder.append("\"neUID\":\"3301EBCS1NEL"+map.get("neUID")+"\",");
						stringBuilder.append("\"neName\":\""+map.get("neName")+"\",");
						stringBuilder.append("\"neType\":\"PTN\",");
						stringBuilder.append("\"objectUID\":\"3301EBCS1"+str+map.get("objectUID")+"\",");
						stringBuilder.append("\"objectName\":\""+map.get("objectName")+"\",");
						stringBuilder.append("\"objectType\":\""+str+"\",");
						stringBuilder.append("\"locationInfo\":\""+"slot:槽位1;card:主控"+"\",");
						stringBuilder.append("\"addInfo\":\""+map.get("addInfo")+"\",");
						stringBuilder.append("\"holderType\":\""+map.get("neType")+"\",");
						stringBuilder.append("\"alarmCheck\":\"\",");
						stringBuilder.append("\"layer\":2");
					}
					
					stringBuilder.append("}");
					while(is.hasNext()){
						Socket s = alarmTcpTracpMainThread.getAlarmTrap().get(is.next());
						AlarmTcpTracpRun alarmTcpTracpRun = alarmTcpTracpMainThread.getAlarmTcpTracpRuns().get(s.toString());
						if(alarmTcpTracpRun.getIsTrap() && alarmTcpTracpRun.getHasLogin() && "msg".equals(alarmTcpTracpRun.getType())){
							s.getOutputStream().write(res(0, stringBuilder.toString()));
							s.getOutputStream().flush();
							NorthLogUtils.alarmrealtimeLog("clientIp:"+alarmTcpTracpRun.getClientIp()+","+"userName:"+alarmTcpTracpRun.getUserName()+",alarmSeq:"+map.get("alarmId")+",sendTime:"+DateUtil.getDate(DateUtil.FULLTIME));
						}
						
					}
					NorthLogUtils.alarmrunLog(stringBuilder.toString());
					
				}
				System.out.println(System.currentTimeMillis());
				UiUtil.closeService_MB(curAlarmService_MB);
				
				//验证180秒的心跳
				Iterator<String> is = alarmTcpTracpMainThread.getAlarmTrap().keySet().iterator();
				while(is.hasNext()){
					Socket s = alarmTcpTracpMainThread.getAlarmTrap().get(is.next());
					AlarmTcpTracpRun alarmTcpTracpRun = alarmTcpTracpMainThread.getAlarmTcpTracpRuns().get(s.toString());
					System.out.println(alarmTcpTracpRun.getHeartTime()+"====="+System.currentTimeMillis());
					if(System.currentTimeMillis()-alarmTcpTracpRun.getHeartTime()>70*60*1000){
						System.out.println("心跳没有");
						alarmTcpTracpRun.closeScket();
						NorthLogUtils.northClientLog("clientIp:"+alarmTcpTracpRun.getClientIp()+","+"userName:"+alarmTcpTracpRun.getUserName()+",loginTime:"+DateUtil.getDate(new Date(alarmTcpTracpRun.getLoginTime()), DateUtil.FULLTIME)+",loginOutTime"+DateUtil.getDate(DateUtil.FULLTIME)+",continuousTime:"+DateTimeUtil.datepoor(alarmTcpTracpRun.getLoginTime(), System.currentTimeMillis()));
					}
					
				}
				System.out.println(System.currentTimeMillis());
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			UiUtil.closeService_MB(curAlarmService_MB);
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
	
	private void getTime(byte[] bs){
		String s = Long.toHexString(System.currentTimeMillis()/1000);
		for (int i = 3; i < 7; i++) {
			bs[i] = (byte) Integer.parseInt(s.substring((i-3)*2, (i-2)*2), 16);
		}
	}
}
