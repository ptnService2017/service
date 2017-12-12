package com.nms.snmp.ninteface.impl.alarm;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AlarmTcpTracpMainThread extends Thread{
	private ServerSocket serverSocket;
	private boolean isRun = true;
	private ConcurrentHashMap<String,Long> alarmTrapHeart = new ConcurrentHashMap<String,Long>();
	private ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(10);
	private ConcurrentHashMap<String,Boolean> alarmTrapRun = new ConcurrentHashMap<String,Boolean>();
	
	@Override
	public void run() {
		while(isRun){
			try {
				serverSocket = new ServerSocket(31232);
				Socket socket = serverSocket.accept();
				alarmTrapRun.put(socket.toString(), true);
				alarmTrapHeart.put(socket.toString(), System.currentTimeMillis());
				AlarmTcpTracpRun alarmTcpTracpRun = new AlarmTcpTracpRun(socket,this);
				threadPoolExecutor.execute(alarmTcpTracpRun);
			} catch (IOException e) {
				e.printStackTrace();
			}
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
	
	
	
}
