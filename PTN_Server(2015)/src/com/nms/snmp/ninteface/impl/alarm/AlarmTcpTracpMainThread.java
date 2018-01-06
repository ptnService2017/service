package com.nms.snmp.ninteface.impl.alarm;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.nms.db.bean.alarm.HisAlarmInfo;
import com.nms.db.bean.alarm.WarningLevel;
import com.nms.model.alarm.HisAlarmService_MB;
import com.nms.model.util.ServiceFactory;
import com.nms.model.util.Services;
import com.nms.snmp.ninteface.framework.SnmpConfig;
import com.nms.snmp.ninteface.util.NorthConfig;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.DateUtil;
import com.nms.util.Mybatis_DBManager;


public class AlarmTcpTracpMainThread extends Thread{
	private ServerSocket serverSocket;
	private boolean isRun = true;
	private ConcurrentHashMap<String,Long> alarmTrapHeart = new ConcurrentHashMap<String,Long>();
	private ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(20);
	private ConcurrentHashMap<String,Boolean> alarmTrapRun = new ConcurrentHashMap<String,Boolean>();
	private ConcurrentHashMap<String, Socket> alarmTrap = new ConcurrentHashMap<String, Socket>();
	private ConcurrentHashMap<String, AlarmTcpTracpRun> alarmTcpTracpRuns = new ConcurrentHashMap<String, AlarmTcpTracpRun>();
	
	
	@Override
	public void run() {

		try {
			serverSocket = new ServerSocket(NorthConfig.northAlarmPort);
			AlarmTrapSocket alarmTrapSocket = new AlarmTrapSocket(this);
			new Thread(alarmTrapSocket).start();
			while(isRun){
				Socket socket = serverSocket.accept();
				alarmTrapRun.put(socket.toString(), true);
				alarmTrapHeart.put(socket.toString(), System.currentTimeMillis());
				alarmTrap.put(socket.toString(), socket);
				AlarmTcpTracpRun alarmTcpTracpRun = new AlarmTcpTracpRun(socket,this);
				alarmTcpTracpRuns.put(socket.toString(), alarmTcpTracpRun);
				threadPoolExecutor.execute(alarmTcpTracpRun);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public ServerSocket getServerSocket() {
		return serverSocket;
	}


	public void setServerSocket(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}


	public boolean isRun() {
		return isRun;
	}


	public void setRun(boolean isRun) {
		this.isRun = isRun;
	}


	public ConcurrentHashMap<String, Long> getAlarmTrapHeart() {
		return alarmTrapHeart;
	}


	public void setAlarmTrapHeart(ConcurrentHashMap<String, Long> alarmTrapHeart) {
		this.alarmTrapHeart = alarmTrapHeart;
	}


	public ExecutorService getThreadPoolExecutor() {
		return threadPoolExecutor;
	}


	public void setThreadPoolExecutor(ExecutorService threadPoolExecutor) {
		this.threadPoolExecutor = threadPoolExecutor;
	}


	public ConcurrentHashMap<String, Boolean> getAlarmTrapRun() {
		return alarmTrapRun;
	}


	public void setAlarmTrapRun(ConcurrentHashMap<String, Boolean> alarmTrapRun) {
		this.alarmTrapRun = alarmTrapRun;
	}
	
	public static void main(String[] args) {
		 Mybatis_DBManager.init("199.199.13.198");
		 ConstantUtil.serviceFactory = new ServiceFactory();
		 SnmpConfig.getInstanse().init();
		AlarmTcpTracpMainThread alarmTcpTracpMainThread = new AlarmTcpTracpMainThread();
		new Thread(alarmTcpTracpMainThread).start();
		
		HisAlarmService_MB hisAlarmService_MB = null;
		try {
			hisAlarmService_MB = (HisAlarmService_MB) ConstantUtil.serviceFactory.newService_MB(Services.HisAlarm);
//			for (int i = 0; i < 15*60; i++) {
//				List<HisAlarmInfo> list = new ArrayList<HisAlarmInfo>();
//				for (int j = 0; j <3; j++) {
//					HisAlarmInfo northAlarm = new HisAlarmInfo();
//					northAlarm.setRaisedTime(new Date());
//					northAlarm.setClearedTime(new Date());
//					northAlarm.setConfirmtime(DateUtil.getDate(DateUtil.FULLTIME));
//					northAlarm.setAlarmTime(DateUtil.getDate(DateUtil.FULLTIME));
//					northAlarm.setHappenedtime(DateUtil.getDate(DateUtil.FULLTIME));
//					northAlarm.setSiteId(13);
//					WarningLevel warningLevel = new WarningLevel();
//					warningLevel.setWarninglevel(4);
//					warningLevel.setWarningcode(17);
//					northAlarm.setWarningLevel(warningLevel);
//					northAlarm.setAlarmLevel(4);
//					northAlarm.setAlarmCode(17);
//					list.add(northAlarm);
//					hisAlarmService_MB.insertNorthBatch(list);
//				}
//				Thread.sleep(50);
//			}
			System.out.println("charu wan");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}


	public ConcurrentHashMap<String, Socket> getAlarmTrap() {
		return alarmTrap;
	}


	public void setAlarmTrap(ConcurrentHashMap<String, Socket> alarmTrap) {
		this.alarmTrap = alarmTrap;
	}


	public ConcurrentHashMap<String, AlarmTcpTracpRun> getAlarmTcpTracpRuns() {
		return alarmTcpTracpRuns;
	}


	public void setAlarmTcpTracpRuns(ConcurrentHashMap<String, AlarmTcpTracpRun> alarmTcpTracpRuns) {
		this.alarmTcpTracpRuns = alarmTcpTracpRuns;
	}
	
	
}
