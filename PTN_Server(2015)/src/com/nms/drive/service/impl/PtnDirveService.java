﻿package com.nms.drive.service.impl;

import java.io.InputStream;
import java.util.Properties;

import com.nms.drive.network.UDPNetworkServer;
import com.nms.drive.service.impl.bean.DriveUtilObject;
import com.nms.service.bean.ActionObject;
import com.nms.service.bean.AlarmObjectService;
import com.nms.service.bean.OperationObject;
import com.nms.service.bean.PerformanceObjectService;

public class PtnDirveService {

	protected int PDUHandUUID = 999;
	protected int CommandHandUUID = 0;
	protected int DividePageUUID = 0;

	protected int commandHandByteCount = 21;// 命令头字节数
	protected int dividePageHandByteCount = 12;// 适配层分片头字节数
	protected int pduHandByteCount = 16;// PDU头字节数
	protected int maxCommandByteCount = 1450;// 单条命令最大字节数
	// 分页头12个字节，PDU头16的字节，命令头21个字节，全部扣除剩下的是命令体的最大长度。
	protected final int maxDataCommandByteCount = maxCommandByteCount - dividePageHandByteCount;

	protected MonitorSendCommandThread monitorSendCommandThread = null;
	protected MonitorSendCommandThread monitorSendCommandThread_copy1 = null;
	protected MonitorResponseThread monitorResponseThread = null;
	protected MonitorResponseThread monitorResponseThread_copy1 = null;
	protected MonitorRetransmissionThread monitorRetransmissionThread = null;
	protected MonitorCallbackThread monitorCallbackThread = null;
	protected MonitroMergerPagesThread monitroMergerPagesThread = null;
	protected String ptnaddress = "";
	protected int ptnPort = 0;
	protected int localPort = 0;

	protected UDPNetworkServer uDPNetworkServer = null;
	protected UDPNetworkServer uDPNetworkServer_copy1 = null;

	public AlarmObjectService alarmObjectService = null;
	public PerformanceObjectService performanceObjectService = null;
	
	public void setAlarmObjectService(AlarmObjectService alarmObjectService) {
		this.alarmObjectService = alarmObjectService;
	}
	
	
	public void setPerformanceObjectService(PerformanceObjectService performanceObjectService) {
		this.performanceObjectService = performanceObjectService;
	}


	/**
	 * 初始化驱动服务
	 * 
	 * @throws Exception
	 */
	protected void init() throws Exception {
		// 初始化各种监控线程
		try {
			Properties props = new Properties();
			InputStream propsIs = MonitorCallbackThread.class.getClassLoader().getResourceAsStream("com/nms/drive/service/impl/configDrive.properties");
			props.load(propsIs);
			ptnaddress = props.getProperty("ptnAddress");
			ptnPort = Integer.valueOf(props.getProperty("ptnPort"));
			localPort = Integer.valueOf(props.getProperty("localPort"));
		} catch (Exception e) {
			throw new Exception("configDrive.properties文件配置有问题!");
		}
		try {

			uDPNetworkServer = new UDPNetworkServer();
			uDPNetworkServer.connection(ptnaddress, ptnPort, localPort);
			uDPNetworkServer_copy1 = new UDPNetworkServer();
			uDPNetworkServer_copy1.connection(ptnaddress, ptnPort+1, localPort+1);
			
			monitorSendCommandThread = new MonitorSendCommandThread(50,uDPNetworkServer,"monitorSendCommandThread");
			monitorSendCommandThread.setPtnDirveService(this);
			monitorSendCommandThread.start();

			monitorSendCommandThread_copy1 = new MonitorSendCommandThread(1000, uDPNetworkServer_copy1,"monitorSendCommandThread_copy1");
			monitorSendCommandThread_copy1.setPtnDirveService(this);
			monitorSendCommandThread_copy1.start();
			
			
			monitorResponseThread = new MonitorResponseThread(1,uDPNetworkServer,"monitorResponseThread");
			monitorResponseThread.setPtnDirveService(this);
			monitorResponseThread.start();
			
			monitorResponseThread_copy1 = new MonitorResponseThread(1000, uDPNetworkServer_copy1, "monitorResponseThread_copy1");
			monitorResponseThread_copy1.setPtnDirveService(this);
			monitorResponseThread_copy1.start();
			
			monitorCallbackThread = new MonitorCallbackThread();
			monitorCallbackThread.setPtnDirveService(this);
			monitorCallbackThread.setAlarmObjectService(alarmObjectService);
			monitorCallbackThread.setPerformanceObjectService(performanceObjectService);
			monitorCallbackThread.start();
			monitorRetransmissionThread = new MonitorRetransmissionThread();
			monitorRetransmissionThread.setPtnDirveService(this);
			monitorRetransmissionThread.start();

			monitroMergerPagesThread = new MonitroMergerPagesThread();
			monitroMergerPagesThread.start();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 销毁
	 */
	@SuppressWarnings("deprecation")
	protected void destroy() {
		try {
			monitorSendCommandThread.destroy();
			monitorSendCommandThread.stop();
			monitorSendCommandThread = null;

			monitorResponseThread.destroy();
			monitorResponseThread.stop();
			monitorResponseThread = null;

			monitorRetransmissionThread.destroy();
			monitorRetransmissionThread.stop();
			monitorRetransmissionThread = null;

			monitorCallbackThread.destroy();
			monitorCallbackThread.stop();
			monitorCallbackThread = null;

			monitorSendCommandThread_copy1.destroy();
			monitorSendCommandThread_copy1.stop();
			monitorSendCommandThread_copy1 = null;
			
			monitorResponseThread_copy1.destroy();
			monitorResponseThread_copy1.stop();
			monitorResponseThread_copy1 = null;
			
			uDPNetworkServer_copy1.close();
			uDPNetworkServer.close();
		} catch (Exception e) {
		}
	}

	/**
	 * 解析对象并形成命令
	 * 
	 * @param dataCommand命令数据体部分
	 * @param driveUtilObject驱动对象
	 * @throws Exception
	 * @return 驱动对象
	 */
	protected DriveUtilObject analysisData(byte[] dataCommand, DriveUtilObject driveUtilObject,String str ,int uuid) throws Exception {
		try {
			byte[] sendCommands = new byte[0];
			//基本的PDU头信息 16个字节
			byte[] pduHand = getPDUHand(str, (dataCommand.length + commandHandByteCount),uuid);
			//相应的命令头
			byte[] commandHand = getCommandHand(driveUtilObject.getPtnService(), dataCommand.length);
			
			if("Confirm-PDU".equalsIgnoreCase(str)){
				//确认帧
				sendCommands = CoderUtils.arraycopy(sendCommands, pduHand);
				sendCommands = CoderUtils.arraycopy(sendCommands, dataCommand);
			}else{
				sendCommands = CoderUtils.arraycopy(sendCommands, pduHand);
				sendCommands = CoderUtils.arraycopy(sendCommands, commandHand);
				sendCommands = CoderUtils.arraycopy(sendCommands, dataCommand);
			}

			driveUtilObject.setUuid(PDUHandUUID);
			// 赋值命令
			driveUtilObject.setSendCommands(sendCommands);

		} catch (Exception e) {
			throw e;
		}
		return driveUtilObject;
	}

	/**
	 * 分页封装将一个过长的命令组装成若干个命令
	 * 
	 * @param driveUtilObject驱动对象
	 * @return 驱动对象
	 */
	protected  synchronized DriveUtilObject dividePage(DriveUtilObject driveUtilObject) {
		DividePageUUID++;// 为下次序号自动累加
		if (DividePageUUID >= 65535) {
			DividePageUUID = 1;// 恢复默认值
		}

		byte[] sendCommands = driveUtilObject.getSendCommands();
		byte[] sendCommand = null;
		double commLength = sendCommands.length;
		int pageCount = (int) Math.round((commLength / maxDataCommandByteCount) + 0.5d);// 分几页
		if (pageCount == 0) {
			pageCount = 1;
		}
		int startCount = 0;
		int byteArrayCount = 0;
		for (int i = 0; i < pageCount; i++) {

			if ((pageCount - 1) == i) {
				byteArrayCount = sendCommands.length - (i * (maxDataCommandByteCount));
			} else {
				byteArrayCount = maxDataCommandByteCount;
			}
			sendCommand = new byte[byteArrayCount];

			for (int j = 0; j < byteArrayCount; j++) {
				sendCommand[j] = sendCommands[startCount];
				startCount++;
			}

			byte[] pageHand = getPageHand(DividePageUUID, i, pageCount, sendCommand.length);

			byte[] a = pageHand;//12
			byte[] b = sendCommand;//数据
			byte[] c = new byte[a.length + b.length];
			System.arraycopy(a, 0, c, 0, a.length);// 两个数组合并
			System.arraycopy(b, 0, c, a.length, b.length);// 两个数组合并
			driveUtilObject.getSendCommandList().add(c);
		}

		return driveUtilObject;
	}

	/**
	 * 根据业务不同获得相应的命令头
	 * 
	 * @param ptnService
	 *            业务
	 * @param MaxCommandByteCount
	 *            最大命令数
	 * @return
	 */
	private byte[] getCommandHand(String ptnService, int MaxCommandByteCount) {
		/*
		 * 其中，“对象范围”、“对象标识号2”、“对象标识号3”、“状态2”和“备用1～4”字段现在没有使用。
		 */
		byte[] commandHand = new byte[commandHandByteCount];

		/*
		 * 【命令码】
		 */
		if ("CG0103".equals(ptnService)) {

			commandHand[0] = (byte) CoderUtils.char2ASCII('C');
			commandHand[1] = (byte) CoderUtils.char2ASCII('G');
			commandHand[2] = 0x01;
			commandHand[3] = 0x03;
		} else if ("AS0201".equals(ptnService)) {

			commandHand[0] = (byte) CoderUtils.char2ASCII('A');
			commandHand[1] = (byte) CoderUtils.char2ASCII('S');
			commandHand[2] = 0x02;
			commandHand[3] = 0x01;
		} else if ("AS0202".equals(ptnService)) {

			commandHand[0] = (byte) CoderUtils.char2ASCII('A');
			commandHand[1] = (byte) CoderUtils.char2ASCII('S');
			commandHand[2] = 0x02;
			commandHand[3] = 0x02;
		} else if ("PM0101".equals(ptnService)) {
			commandHand[0] = (byte) CoderUtils.char2ASCII('P');
			commandHand[1] = (byte) CoderUtils.char2ASCII('M');
			commandHand[2] = 0x01;
			commandHand[3] = 0x01;
		} else if ("PM0102".equals(ptnService)) {
			commandHand[0] = (byte) CoderUtils.char2ASCII('P');
			commandHand[1] = (byte) CoderUtils.char2ASCII('M');
			commandHand[2] = 0x01;
			commandHand[3] = 0x02;
		}else if ("CG0101".equals(ptnService)) {
			commandHand[0] = (byte) CoderUtils.char2ASCII('C');
			commandHand[1] = (byte) CoderUtils.char2ASCII('G');
			commandHand[2] = 0x01;
			commandHand[3] = 0x01;
		}else if ("UD0101".equals(ptnService)) {
			commandHand[0] = (byte) CoderUtils.char2ASCII('U');
			commandHand[1] = (byte) CoderUtils.char2ASCII('D');
			commandHand[2] = 0x01;
			commandHand[3] = 0x01;
		}else if ("CG0201".equals(ptnService)) {
			commandHand[0] = (byte) CoderUtils.char2ASCII('C');
			commandHand[1] = (byte) CoderUtils.char2ASCII('G');
			commandHand[2] = 0x02;
			commandHand[3] = 0x01;
		}else if ("CM0101".equals(ptnService)) {
			commandHand[0] = (byte) CoderUtils.char2ASCII('C');
			commandHand[1] = (byte) CoderUtils.char2ASCII('M');
			commandHand[2] = 0x01;
			commandHand[3] = 0x01;
		}else if ("CM0102".equals(ptnService)) {
			commandHand[0] = (byte) CoderUtils.char2ASCII('C');
			commandHand[1] = (byte) CoderUtils.char2ASCII('M');
			commandHand[2] = 0x01;
			commandHand[3] = 0x02;
		}else if ("AS0301".equals(ptnService)) {
			commandHand[0] = (byte) CoderUtils.char2ASCII('A');
			commandHand[1] = (byte) CoderUtils.char2ASCII('S');
			commandHand[2] = 0x03;
			commandHand[3] = 0x01;
		}else if ("CG0301".equals(ptnService)) {
			commandHand[0] = (byte) CoderUtils.char2ASCII('C');
			commandHand[1] = (byte) CoderUtils.char2ASCII('G');
			commandHand[2] = 0x03;
			commandHand[3] = 0x01;
		}else if ("CG0202".equals(ptnService)) {
			commandHand[0] = (byte) CoderUtils.char2ASCII('C');
			commandHand[1] = (byte) CoderUtils.char2ASCII('G');
			commandHand[2] = 0x02;
			commandHand[3] = 0x02;
		}else if ("CG0203".equals(ptnService)) {
			commandHand[0] = (byte) CoderUtils.char2ASCII('C');
			commandHand[1] = (byte) CoderUtils.char2ASCII('G');
			commandHand[2] = 0x02;
			commandHand[3] = 0x03;
		}else if ("PM0401".equals(ptnService)) {
			commandHand[0] = (byte) CoderUtils.char2ASCII('P');
			commandHand[1] = (byte) CoderUtils.char2ASCII('M');
			commandHand[2] = 0x04;
			commandHand[3] = 0x01;
		}else if ("CM0101".equals(ptnService)) {
			commandHand[0] = (byte) CoderUtils.char2ASCII('C');
			commandHand[1] = (byte) CoderUtils.char2ASCII('M');
			commandHand[2] = 0x01;
			commandHand[3] = 0x01;
		}else if ("TS0504".equals(ptnService)) {
			commandHand[0] = (byte) CoderUtils.char2ASCII('T');
			commandHand[1] = (byte) CoderUtils.char2ASCII('S');
			commandHand[2] = 0x05;
			commandHand[3] = 0x04;
		}else if ("UD0203".equals(ptnService)) {
			commandHand[0] = (byte) CoderUtils.char2ASCII('U');
			commandHand[1] = (byte) CoderUtils.char2ASCII('D');
			commandHand[2] = 0x02;
			commandHand[3] = 0x03;
		}else if ("UD0102".equals(ptnService)) {
			commandHand[0] = (byte) CoderUtils.char2ASCII('U');
			commandHand[1] = (byte) CoderUtils.char2ASCII('D');
			commandHand[2] = 0x01;
			commandHand[3] = 0x02;
		}else if ("NC0401".equals(ptnService)) {
			commandHand[0] = (byte) CoderUtils.char2ASCII('N');
			commandHand[1] = (byte) CoderUtils.char2ASCII('C');
			commandHand[2] = 0x04;
			commandHand[3] = 0x01;
		}else if ("JC0501".equals(ptnService)) {
			commandHand[0] = (byte) CoderUtils.char2ASCII('J');
			commandHand[1] = (byte) CoderUtils.char2ASCII('C');
			commandHand[2] = 0x05;
			commandHand[3] = 0x01;
		}else if ("JC0502".equals(ptnService)) {
			commandHand[0] = (byte) CoderUtils.char2ASCII('J');
			commandHand[1] = (byte) CoderUtils.char2ASCII('C');
			commandHand[2] = 0x05;
			commandHand[3] = 0x02;
		}else if ("JC0503".equals(ptnService)) {
			commandHand[0] = (byte) CoderUtils.char2ASCII('J');
			commandHand[1] = (byte) CoderUtils.char2ASCII('C');
			commandHand[2] = 0x05;
			commandHand[3] = 0x03;
		}else if ("JC0504".equals(ptnService)) {
			commandHand[0] = (byte) CoderUtils.char2ASCII('J');
			commandHand[1] = (byte) CoderUtils.char2ASCII('C');
			commandHand[2] = 0x05;
			commandHand[3] = 0x04;
		}
		

		/*
		 * 【方向】 “方向”字段表达的信息在PDU头的“通信类型”字段中有更明确的表达， 为保持协议的前后兼容性，在NMU（M）向WS发出的数据中改字段固定填“0x28” WS向NMU发“0x88”， NMU（M）接收的数据中不解释该字段。
		 */
		commandHand[4] = (byte) 0x88;// 未用
		/*
		 * 【对象范围】
		 */
		commandHand[5] = 0x00;// 未用
		/*
		 * 【对象标识号】
		 * 
		 * “对象标识号1”：D0（BIT0）：该帧是否支持压缩传送方式（0／l=不支持／支持） D1（BITl）：该帧长度之后的内容是否是压缩数据（0／1=非压缩／压缩） 因NMU肯定支持数据压缩传送方式，因此BIT0固定填1。
		 * 
		 * “对象标识4”字段说明使用的协议类型和协议数据类，其中： D7：说明使用的协议类型，确定后面数据解释的方式 D7＝0/1＝按现有WS－MAU协议解释/按ASON定义的WS－NMU协议解释 D0～D3说明数据类型，分为如下几类： 1——配置数据 2——告警数据 3——性能数据 A——透明帧数据
		 * 
		 * D7 ..... D0
		 */
		commandHand[6] = 0x00;// 未用
		commandHand[7] = (byte) CoderUtils.convertAlgorism(new char[] { '0', '0', '0', '0', '0', '0', '1', '1' });
		commandHand[8] = (byte) CoderUtils.convertAlgorism(new char[] { '0', '0', '0', '0', '0', '1', '1', '1' });
		commandHand[9] = 0x31;// 未用

		/*
		 * 【流水号】 字段填PDU头中“会晤系列号”字段的最后一个字节。
		 */
		CommandHandUUID++;// 为下次序号自动累加
		if (CommandHandUUID >= 255) {
			CommandHandUUID = 1;// 恢复默认值
		}
		commandHand[10] = (byte) CommandHandUUID;

		/*
		 * 【状态】 “状态1”字段在申请配置的“JC040E”命令时按如下方式解释： D5～D7未用， D4——地址参数申请位，“0”=地址对，“1”=地址错； D3——时间参数申请位，“0”=不申请，“1”=申请； D2——结构参数申请位，“0”=不申请，“1”=申请； D1——设备参数申请位，“0”=不申请，“1”=申请； D0——管理参数申请位，“0”=不申请，“1”=申请
		 */
		commandHand[11] = (byte) CoderUtils.convertAlgorism(new char[] { '0', '0', '0', '0', '1', '1', '1', '1' });
		commandHand[12] = 0x00;// 未用
		/*
		 * 【备用】
		 */
		commandHand[13] = 0x00;// 未用
		commandHand[14] = 0x00;// 未用
		commandHand[15] = 0x00;// 未用
		commandHand[16] = 0x00;// 未用

		/*
		 * 【数据长度】
		 */
		byte[] maxCommandByte = CoderUtils.intToBytes(MaxCommandByteCount);
		commandHand[17] = maxCommandByte[0];
		commandHand[18] = maxCommandByte[1];
		commandHand[19] = maxCommandByte[2];
		commandHand[20] = maxCommandByte[3];

		return commandHand;
	}

	/**
	 * 根据配置信息获得PDU头
	 * 
	 * @param pduTypeTab
	 *            PDU类型标签
	 * @param pudDataCount
	 *            PDU信息体的长度
	 * @return PDU命令头
	 */
	private byte[] getPDUHand(String pduTypeTab, int pudDataCount ,int uuId) {
		byte[] pduHand = new byte[pduHandByteCount];
		/*
		 * 【网管PDU标签】
		 */
		pduHand[0] = (byte) CoderUtils.char2ASCII('D');
		pduHand[1] = (byte) CoderUtils.char2ASCII('A');
		pduHand[2] = (byte) CoderUtils.char2ASCII('T');
		pduHand[3] = (byte) CoderUtils.char2ASCII('A');
		/*
		 * 【协议版本】
		 */
		pduHand[4] = 0x02;

		/*
		 * 【PDU类型标签】 Request-PDU = E1; Response-PDU = E2; Report-PDU = E3; Confirm-PDU = E4; Broadcast-PDU = E5;
		 */

		if ("Request-PDU".equalsIgnoreCase(pduTypeTab)) {
			pduHand[5] = (byte) 0xE1;
		} else if ("Response-PDU".equalsIgnoreCase(pduTypeTab)) {
			pduHand[5] = (byte) 0xE2;
		} else if ("Report-PDU".equalsIgnoreCase(pduTypeTab)) {
			pduHand[5] = (byte) 0xE3;
		} else if ("Confirm-PDU".equalsIgnoreCase(pduTypeTab)) {
			pduHand[5] = (byte) 0xE4;
		} else if ("Broadcast-PDU".equalsIgnoreCase(pduTypeTab)) {
			pduHand[5] = (byte) 0xE5;
		} else {
			pduHand[5] = (byte) 0xE1;// 默认请求
		}

		/*
		 * 【通信类型】 “通信类型”字段（1字节）表示管理实体间信息交互的方式和发起方，分配代码如下： WS－M：由WS发出的数据，类型代码为1； M－WS：由M发出的数据，类型代码为2； M－A：由M发出的数据，类型代码为3； A－M：由A发出的数据，类型代码为4； A－BMU：由A发出的数据，类型代码为5； BMU－A：由BMU发出的数据，类型代码为6； A—BMU广播：由A向所管辖BMU发起的广播型数据，类型代码为7； PC－A：由PC机发出的数据（用串口通信），代码类型为8； A－PC：由A向PC发出的数据（用串口通信），类型代码为9； PC－A：由PC机发出的数据（用以太网通信），类型代码为0xA； A－PC：由PC机发出的数据（用以太网通信），类型代码为0xB； ACU－BMU：由ACU向BMU发出的数据，类型代码为0xE； BMU－ACU：由BMU向ACU发出的数据，类型代码为0xD； ACU－NMU：由ACU向NMU发出的数据，类型代码为0xE； NMU－ACU：由NMU向ACU发出的数据，类型代码为0xF。
		 */
		pduHand[6] = 0x01;// WS默认是1

		/*
		 * 【下一个PDU头】
		 */
		pduHand[7] = 0x01;// 默认值

		/*
		 * 【会晤序列号】
		 */
		PDUHandUUID++;// 为下次序号自动累加
		if (PDUHandUUID >= 65535) {
			PDUHandUUID = 1;// 恢复默认值
		}
		byte[] uuid = new byte[4];
		if(uuId>0){
			uuid = CoderUtils.intToBytes(uuId);
			/*
			 * 【PDU信息体的长度】
			 */
			byte[] pudDataCountBytes = CoderUtils.intToBytes(1);
			pduHand[12] = pudDataCountBytes[0];
			pduHand[13] = pudDataCountBytes[1];
			pduHand[14] = pudDataCountBytes[2];
			pduHand[15] = pudDataCountBytes[3];
		}else{
			uuid = CoderUtils.intToBytes(PDUHandUUID);
			/*
			 * 【PDU信息体的长度】
			 */
			byte[] pudDataCountBytes = CoderUtils.intToBytes(pudDataCount);
			pduHand[12] = pudDataCountBytes[0];
			pduHand[13] = pudDataCountBytes[1];
			pduHand[14] = pudDataCountBytes[2];
			pduHand[15] = pudDataCountBytes[3];
		}
		pduHand[8] = uuid[0];
		pduHand[9] = uuid[1];
		pduHand[10] = uuid[2];
		pduHand[11] = uuid[3];
		

		

		return pduHand;
	}

	/**
	 * 根据分页情况获得分页头
	 * 
	 * @param uuid报文序列号
	 * @param pageCount第几片分页
	 * @param maxPageCount总共几个分页
	 * @param length数据长度
	 * @return 分片命令头
	 */
	private byte[] getPageHand(int uuid, int pageCount, int maxPageCount, int length) {
		byte[] pageHand = new byte[dividePageHandByteCount];

		// 【报文序列号】
		byte[] uuidBytes = CoderUtils.intToBytes(uuid);
		pageHand[0] = uuidBytes[0];
		pageHand[1] = uuidBytes[1];
		pageHand[2] = uuidBytes[2];
		pageHand[3] = uuidBytes[3];

		// 【分片号】
		byte[] pageCountsBytes = CoderUtils.intToBytes(pageCount + 1);// 外部传入0开始+1保证命令
		pageHand[4] = pageCountsBytes[2];
		pageHand[5] = pageCountsBytes[3];

		// 【总片数】
		byte[] maxPageCountBytes = CoderUtils.intToBytes(maxPageCount);
		pageHand[6] = maxPageCountBytes[2];
		pageHand[7] = maxPageCountBytes[3];

		/*
		 * 【控制字】 D7：报文类型 0：数据报文，传送PDU的报文。 1：控制报文，传送协议适配层内部的控制信息。规定控制报文一定不包含分片的数据，即控制报文的总片数为1，分片号也为1。 D6：数据等级 0：一般数据 1：重要数据。 D5～D0：备用（全0） 0：一般数据 1：重要数据。
		 */
		pageHand[8] = (byte) CoderUtils.convertAlgorism(new char[] { '0', '1', '0', '0', '0', '0', '0', '0' });

		/*
		 * 【备用】（全0）
		 */
		pageHand[9] = 0x00;

		/*
		 * 【数据长度】
		 */
		byte[] lengthBytes = CoderUtils.intToBytes(length);
		pageHand[10] = lengthBytes[2];
		pageHand[11] = lengthBytes[3];

		return pageHand;
	}

	/**
	 * 日志输出
	 * 
	 * @param classObj
	 *            类对象
	 * @param e
	 *            异常对象
	 * @param type
	 *            异常类型
	 */
	public void logOut(Class classObj, Exception e, String type) {
		logOut(classObj, e.toString(), type);
	}

	/**
	 * 日志输出
	 * 
	 * @param classObj
	 *            类对象
	 * @param logs
	 *            异常对象
	 * @param type
	 *            异常类型
	 */
	public void logOut(Class classObj, String logs, String type) {
		String typeStr = "调试信息：";
		if ("error".equalsIgnoreCase(type)) {
			typeStr = "异常信息:";
		}
		System.out.println(typeStr + "【" + classObj.getName() + "】 " + logs);
	}

	public UDPNetworkServer getUDPNetworkServer() {
		return uDPNetworkServer;
	}

}
