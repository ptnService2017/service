package com.nms.snmp.ninteface.impl.alarm;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.nms.model.util.ServiceFactory;
import com.nms.snmp.ninteface.framework.SnmpConfig;
import com.nms.snmp.ninteface.util.NorthConfig;
import com.nms.ui.manager.ConstantUtil;
import com.nms.util.Mybatis_DBManager;


public class AlarmTcpTracpMainThread extends Thread{
	private ServerSocket serverSocket;
	private boolean isRun = true;
	private ConcurrentHashMap<String,Long> alarmTrapHeart = new ConcurrentHashMap<String,Long>();
	private ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(20);
	private ConcurrentHashMap<String,Boolean> alarmTrapRun = new ConcurrentHashMap<String,Boolean>();
	
	@Override
	public void run() {
		
		try {
			serverSocket = new ServerSocket(NorthConfig.northAlarmPort);
		while(isRun){
				Socket socket = serverSocket.accept();
				alarmTrapRun.put(socket.toString(), true);
				alarmTrapHeart.put(socket.toString(), System.currentTimeMillis());
				AlarmTcpTracpRun alarmTcpTracpRun = new AlarmTcpTracpRun(socket,this);
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
		 Mybatis_DBManager.init("127.0.0.1");
		 ConstantUtil.serviceFactory = new ServiceFactory();
		 SnmpConfig.getInstanse().init();
		AlarmTcpTracpMainThread alarmTcpTracpMainThread = new AlarmTcpTracpMainThread();
		new Thread(alarmTcpTracpMainThread).start();
	}
	
}
