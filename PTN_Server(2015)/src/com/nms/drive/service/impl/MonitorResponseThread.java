﻿package com.nms.drive.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.nms.drive.network.UDPNetworkServer;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.ExceptionManage;


public class MonitorResponseThread extends Thread {

	
	private PtnDirveService ptnDirveService = null;
	
	// 已收到响应结束的命令
	private boolean pd = true;
	private UDPNetworkServer udpNetworkServer;
	private int sleepTime;
	
	public MonitorResponseThread(int sleepTime,UDPNetworkServer udpNetworkServer,String name)
	{
		this.setName(name);
		this.sleepTime = sleepTime;
		this.udpNetworkServer = udpNetworkServer;
	}
	@Override
	public void run() {
		while (pd) {
			try {
				monitorResponse();// 获得命令
				Thread.sleep(sleepTime);
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
	 * 监控是否有命令返回
	 * 
	 * @throws Exception
	 */
	private void monitorResponse() throws Exception {
		try {
			// System.out.println("【" + new Date() + "】-【接收命令列表:" +
			// responseCommandMap.size() + "】-【命令合层后的列表:" +
			// commmandListMap.size() + "】");
			byte[] commands = udpNetworkServer.get(1440);
			String sourceIp = udpNetworkServer.getDatagramPacket().getAddress().toString();//设备ip
			if (commands != null && commands.length > 0 && !sourceIp.equals("/"+ConstantUtil.serviceIp)) {
				Map<String,byte[]> map = new HashMap<String, byte[]>();
				map.put(sourceIp, commands);
				ConstantUtil.monitorResponseLinkedQueue.add(map);
				
			}
		} catch (Exception e) {
			throw e;
		}
	}

	

	/**
	 * 如果到达一定条件还没有收齐所有分片那么就重传命令
	 */
	private void retransmission() {
		// 暂时没有实现
	}

	/**
	 * 通过PDU UUID删除响应完成的命令对象
	 * 
	 * @param pduUUID
	 */
	public void removeDriveUtilObject(String pduUUID) {
		ConstantUtil.recive_commmandListMap.remove(pduUUID);
	}



	public void setPtnDirveService(PtnDirveService ptnDirveService) {
		this.ptnDirveService = ptnDirveService;
	}
	
}
