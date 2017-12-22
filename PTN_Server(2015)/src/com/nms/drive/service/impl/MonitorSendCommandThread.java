﻿package com.nms.drive.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.nms.drive.network.UDPNetworkServer;
import com.nms.drive.service.impl.bean.DriveUtilObject;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.ExceptionManage;

public class MonitorSendCommandThread extends Thread {

	private static Byte commmandListLock = 0;// 发送同步锁
	private PtnDirveService ptnDirveService = null;
	private List<DriveUtilObject> commmandList = new ArrayList<DriveUtilObject>();// 待发送
	private boolean pd = true;
	private int sleepTime;
	private UDPNetworkServer networkServer;
	public MonitorSendCommandThread(int sleepTime,UDPNetworkServer networkServer,String name)
	{
		this.setName(name);
		this.sleepTime = sleepTime;
		this.networkServer = networkServer;
	}
	@Override
	public void run() {
		while (pd) {
			try {
				monitorSendCommand();
			} catch (Exception e) {
				ExceptionManage.dispose(e,this.getClass());
			}
			try {
				Thread.sleep(sleepTime);// 休眠防止拥塞
			} catch (Exception e) {
				ExceptionManage.dispose(e,this.getClass());
			}
		}
	} 

	@Override
	public void destroy() {
		pd = false;// 停止线程
	}

	/**
	 * 监控是否有要发送的命令
	 */
	private void monitorSendCommand() {
		synchronized (commmandListLock) {
			DriveUtilObject driveUtilObject = null;
			byte[] commands = null;
			// System.out.println("【" + new Date() + "】-【待发送命令列表:" +
			// commmandList.size() + "】-【已发送命令列表:" + commmandListMap.size() +
			// "】");

			for (int i = 0; i < commmandList.size(); i++) {
				driveUtilObject = commmandList.get(i);
				if (driveUtilObject.getStates() == 0) {// 待发送
					List<byte[]> sendCommandList = driveUtilObject.getSendCommandList();// 分页后的命令请求命令
					for (int j = 0; j < sendCommandList.size(); j++) {
						commands = sendCommandList.get(j);
						// System.out.println(j);
						networkServer.send(commands,driveUtilObject.getDestinationIP());
//						 CoderUtils.print16String(commands);// 打印发送命令
						// 标记为已发送
						driveUtilObject.setStates(1);
						// !!!!!
						// 移动到已发送列表中
						putDriveUtilObject(driveUtilObject.getUuid() + "", driveUtilObject);
						try {
							Thread.sleep(5);// 休眠防止拥塞
						} catch (Exception e) {
							ExceptionManage.dispose(e,this.getClass());
						}
					}
					
				}
			}
			commmandList.clear();
		}
	}

	/**
	 * 追加要发送的命令
	 * 
	 * @param driveUtilObject
	 */
	public void addDriveUtilObject(DriveUtilObject driveUtilObject) {
		synchronized (commmandListLock) {
			commmandList.add(driveUtilObject);
		}
	}


	/**
	 * 添加已发送 DriveUtilObject
	 * 
	 * @param uuid
	 * @param driveUtilObject
	 */
	private void putDriveUtilObject(String uuid, DriveUtilObject driveUtilObject) {
		ConstantUtil.send_commmandListMap.put(uuid, driveUtilObject);
	}

	public void setPtnDirveService(PtnDirveService ptnDirveService) {
		this.ptnDirveService = ptnDirveService;
	}
}
