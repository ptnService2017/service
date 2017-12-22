﻿package com.nms.drive.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.nms.db.bean.equipment.shelf.SiteInst;
import com.nms.db.bean.system.Field;
import com.nms.drive.analysis.AnalysisObjectService;
import com.nms.drive.analysis.AnalysisObjectServiceI;
import com.nms.drive.service.bean.ClockObject;
import com.nms.drive.service.bean.LoopProRotateObject;
import com.nms.drive.service.bean.NEObject;
import com.nms.drive.service.bean.OSPFinfoWhObeject;
import com.nms.drive.service.bean.ProtectRorateObject;
import com.nms.drive.service.bean.TdmLoopObject;
import com.nms.drive.service.bean.WorkSpace;
import com.nms.drive.service.bean.status.PingOrderControllerObject;
import com.nms.drive.service.impl.CoderUtils;
import com.nms.drive.service.impl.PtnDirveService;
import com.nms.drive.service.impl.bean.DriveUtilObject;
import com.nms.model.equipment.shlef.SiteService_MB;
import com.nms.model.system.FieldService_MB;
import com.nms.model.util.Services;
import com.nms.service.bean.ActionObject;
import com.nms.service.bean.OperationObject;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.UiUtil;

public class DriveService extends PtnDirveService implements DriveServiceI {

	private int initStates = 0;

	/**
	 * 初始化驱动服务
	 * 
	 * @throws Exception
	 */
	public void init() throws Exception {
		if (initStates == 0) {
			super.init();
			initStates = 1;
		} else {
			throw new Exception("已经初始化过！只允许初始化一次！");
		}
	}

	/**
	 * 销毁
	 */
	public void destroy() {
		super.destroy();
		initStates = 0;
	}

	/**
	 * 更新隧道
	 * 
	 * @param OperationObject回调对象
	 * @param actionObject事件对象
	 * @throws Exception
	 * 
	 */
	public void updataTunnal(OperationObject operationObject, ActionObject actionObject) throws Exception {
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getTunnelObjectList());// 赋值对象
			driveUtilObject.setPtnService(PtnDriveCommand.CG0103);// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP

			//根据tunnelObjectList生成带固定格式的byte[]
			AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();
			byte[] dataCommand = analysisObjectServiceI.AnalysisTunnalToCommand(actionObject.getNeObject(), actionObject.getTunnelObjectList());

			super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			super.dividePage(driveUtilObject);// 将整体命令分片
			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠，防止拥塞

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 更新PW
	 * 
	 * @param operationObject回调对象
	 * @param actionObject事件对象
	 * @throws Exception
	 */
	public void updataPW(OperationObject operationObject, ActionObject actionObject) throws Exception {
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getPwObjectList());// 赋值对象
			driveUtilObject.setPtnService("CG0103");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			// 临时调用
			AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();
			byte[] dataCommand = analysisObjectServiceI.AnalysisPwToCommand(actionObject.getNeObject(), actionObject.getPwObjectList());
			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片

			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞

		} catch (Exception e) {
			throw e;
		}

	}

	/**
	 * 更新ELine
	 * 
	 * @param operationObject回调对象
	 * @param actionObject事件对象
	 * @throws Exception
	 */
	public void updataEline(OperationObject operationObject, ActionObject actionObject) throws Exception {
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getElineObjectList());// 赋值对象
			driveUtilObject.setPtnService("CG0103");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			// 临时调用
			AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();
			byte[] dataCommand = analysisObjectServiceI.AnalysisElineToCommand(actionObject.getNeObject(), actionObject.getElineObjectList());

			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片

			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞

		} catch (Exception e) {
			throw e;
		}

	}

	/**
	 * 更新ETree
	 * 
	 * @param operationObject
	 * @param actionObject
	 * @throws Exception
	 */
	public void updataETree(OperationObject operationObject, ActionObject actionObject) throws Exception {
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getEtreeObjectList());// 赋值对象
			driveUtilObject.setPtnService("CG0103");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			// 临时调用
			AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();
			byte[] dataCommand = analysisObjectServiceI.AnalysisETreeToCommand(actionObject.getNeObject(), actionObject.getEtreeObjectList());
			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片

			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞

		} catch (Exception e) {
			throw e;
		}

	}
	/**
	 * 更新ETree
	 * 
	 * @param operationObject
	 * @param actionObject
	 * @throws Exception
	 */
	public void updataELan(OperationObject operationObject, ActionObject actionObject) throws Exception {
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getElanObejctList());// 赋值对象
			driveUtilObject.setPtnService("CG0103");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			// 临时调用
			AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();
			byte[] dataCommand = analysisObjectServiceI.AnalysisELanToCommand(actionObject.getNeObject(), actionObject.getElanObejctList());
			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片

			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞

		} catch (Exception e) {
			throw e;
		}

	}

	/**
	 * 更新ETHOAM
	 * 
	 * @param operationObject回调对象
	 * @param actionObject事件对象
	 * @throws Exception
	 */
	public void updataETHOAM(OperationObject operationObject, ActionObject actionObject) throws Exception {
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getEthOAM());// 赋值对象
			driveUtilObject.setPtnService("CG0103");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			// 临时调用
			AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();
			byte[] dataCommand = analysisObjectServiceI.AnalysisETHOAMToCommand(actionObject.getNeObject(), actionObject.getEthOAM());
			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片

			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 更新E1
	 * 
	 * @param operationObject
	 * @param actionObject
	 * @throws Exception
	 */
	public void updataE1(OperationObject operationObject, ActionObject actionObject) throws Exception {
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getE1Object());// 赋值对象
			driveUtilObject.setPtnService("CG0103");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();
			byte[] dataCommand = analysisObjectServiceI.AnalysisE1ToCommand(actionObject.getNeObject(), actionObject.getE1Object());
			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片

			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 更新LSP 保护
	 * 
	 * @param operationObject回调对象
	 * @param actionObject事件对象
	 * @throws Exception
	 */
	public void updataLSPProtection(OperationObject operationObject, ActionObject actionObject) throws Exception {
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getLSPProtectionList());// 赋值对象
			driveUtilObject.setPtnService("CG0103");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID((actionObject.getActionId()) + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			// 临时调用
			AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();
			byte[] dataCommand = analysisObjectServiceI.AnalysisLSPProtectionToCommand(actionObject.getNeObject(), actionObject.getLSPProtectionList());
			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞

		} catch (Exception e) {
			throw e;
		}

	}

	/**
	 * 更新PW层队列调度及缓存管理策略
	 * 
	 * @param operationObject回调对象
	 * @param actionObject事件对象
	 * @throws Exception
	 */
	public void updataPwQueueAndBufferManage(OperationObject operationObject, ActionObject actionObject) throws Exception {

		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getLSPProtectionList());// 赋值对象
			driveUtilObject.setPtnService("CG0103");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			// 临时调用
			AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();

			byte[] dataCommand = analysisObjectServiceI.AnalysisPwQueueAndBufferManageToCommand(actionObject.getNeObject(), actionObject.getPwQueueAndBufferManage());

			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片

			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * WS向M查询某NE所有盘告警即时状态
	 * 
	 * @param operationObject
	 * @param actionObject
	 * @throws Exception
	 */
	public void queryAllAlarmObject(OperationObject operationObject, ActionObject actionObject) throws Exception {
		// M S 05 02
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			// driveUtilObject.setPtnDataObject();// 赋值对象
			driveUtilObject.setPtnService("AS0201");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			byte[] dataCommand = new byte[2];

			// NE地址
			byte[] ne = CoderUtils.intToBytes(actionObject.getNeObject().getNeAddress());
			dataCommand[0] = ne[2];
			dataCommand[1] = ne[3];

			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * WS向M查询某NE某盘告警即时状态
	 * 
	 * @param operationObject
	 * @param actionObject
	 * @throws Exception
	 */
	public void queryAllAlarmObjectByCard(OperationObject operationObject, ActionObject actionObject) throws Exception {
		// M S 05 03
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			// driveUtilObject.setPtnDataObject();// 赋值对象
			driveUtilObject.setPtnService("AS0202");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			byte[] dataCommand = new byte[5];

			// NE地址
			byte[] neaddress = CoderUtils.intToBytes(actionObject.getNeObject().getNeAddress());
			dataCommand[0] = neaddress[2];
			dataCommand[1] = neaddress[3];
			// 盘类型
			byte[] platetype = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelType());
			dataCommand[2] = platetype[3];
			//盘组号
			byte[] PanelGroupId = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelGroupId());
			dataCommand[3] = PanelGroupId[3];
			// 盘地址
			byte[] plateaddress = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelAddress());
			dataCommand[4] = plateaddress[3];

			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片

			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞

		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * WS向M查询某NE某盘告警即时状态
	 * 
	 * @param operationObject
	 * @param actionObject
	 * @throws Exception
	 */
	public void queryHisAlarmObjectByCard(OperationObject operationObject, ActionObject actionObject) throws Exception {
		// M S 05 03
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			// driveUtilObject.setPtnDataObject();// 赋值对象
			driveUtilObject.setPtnService("MS0504");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			byte[] dataCommand = new byte[5];

			// NE地址
			byte[] neaddress = CoderUtils.intToBytes(actionObject.getNeObject().getNeAddress());
			dataCommand[0] = neaddress[2];
			dataCommand[1] = neaddress[3];
			// 盘类型
			byte[] platetype = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelType());
			dataCommand[2] = platetype[3];
			// 盘地址
			byte[] plateaddress = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelAddress());
			byte[] plateGroup = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelGroupId());
			dataCommand[3] = plateGroup[3];
			dataCommand[4] = plateaddress[3];

			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片

			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 更新TMP OAM故障管理配置 保护
	 * 
	 * @param operationObject回调对象
	 * @param actionObject事件对象
	 * @throws Exception
	 */
	public void updataTMPOAMBugControl(OperationObject operationObject, ActionObject actionObject) throws Exception {
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getLSPProtectionList());// 赋值对象
			driveUtilObject.setPtnService("CG0103");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			// 临时调用
			AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();

			byte[] dataCommand = analysisObjectServiceI.AnalysisTMPOAMBugControlToCommand(actionObject.getNeObject(), actionObject.getTmpOAMBugControlObject());
			
			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片

			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞

		} catch (Exception e) {
			throw e;
		}

	}

	/**
	 * WS向M查询某NE所有盘性能
	 * 
	 * @param operationObject
	 * @param actionObject
	 * @throws Exception
	 */
	public void queryAllPerformanceObject(OperationObject operationObject, ActionObject actionObject) throws Exception {
		// P S 05 01
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			// driveUtilObject.setPtnDataObject();// 赋值对象
			driveUtilObject.setPtnService("PM0101");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			long l = actionObject.getPerformanceBeginDataTime();
			Date date = new Date(l);
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			String year = calendar.get(calendar.YEAR) + "";
			
			int year_1 = Integer.parseInt(year)/256;
			int year_2 = Integer.parseInt(year)%256;
			byte[] yearByte_1 = CoderUtils.intToBytes(year_1);
			byte[] yearByte_2 = CoderUtils.intToBytes(year_2);
			byte[] monthByte = CoderUtils.intToBytes(calendar.get(calendar.MONTH) + 1);
			byte[] dayByte = CoderUtils.intToBytes(calendar.get(calendar.DAY_OF_MONTH));
			byte[] hourByte = CoderUtils.intToBytes(calendar.get(calendar.HOUR_OF_DAY));
			byte[] minByte = CoderUtils.intToBytes(calendar.get(calendar.MINUTE));
			byte[] secByte = CoderUtils.intToBytes(calendar.get(calendar.SECOND));

			// 历史性能个数
			byte[] performanceCountByte = CoderUtils.intToBytes(actionObject.getPerformanceCount());// 历史性能个数
//			if(actionObject.getPerformanceCount() == 0 || actionObject.getPerformanceType() != 0 || actionObject.getPerformanceCount() == 1){
				yearByte_1 = CoderUtils.intToBytes(255);
				yearByte_2 = CoderUtils.intToBytes(255);
				monthByte = CoderUtils.intToBytes(0);
				dayByte = CoderUtils.intToBytes(0);
				hourByte = CoderUtils.intToBytes(0);
				minByte = CoderUtils.intToBytes(0);
				secByte = CoderUtils.intToBytes(0);
//			}
			// 历史性能起始数
			byte[] performanceBeginCountByte = CoderUtils.intToBytes(actionObject.getPerformanceBeginCount());// 历史性能起始数
			// 性能数据类型（00，20，21，30）
			byte[] performanceTypeByte = CoderUtils.intToBytes(actionObject.getPerformanceType());// 性能数据类型（00，20，21，30）
			// NE地址
			byte[] neAddressByte = CoderUtils.intToBytes(actionObject.getNeObject().getNeAddress());

			byte[] dataCommand = new byte[12];
			dataCommand[0] = neAddressByte[2];
			dataCommand[1] = neAddressByte[3];
			dataCommand[2] = yearByte_1[yearByte_1.length - 1];
			dataCommand[3] = yearByte_2[yearByte_2.length - 1];
			dataCommand[4] = monthByte[monthByte.length - 1];
			dataCommand[5] = dayByte[dayByte.length - 1];
			dataCommand[6] = hourByte[hourByte.length - 1];
			dataCommand[7] = minByte[minByte.length - 1];
			dataCommand[8] = secByte[secByte.length - 1];
			dataCommand[9] = performanceCountByte[performanceCountByte.length - 1];
			dataCommand[10] = performanceBeginCountByte[performanceBeginCountByte.length - 1];
			dataCommand[11] = performanceTypeByte[performanceTypeByte.length - 1];

			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
			
			CoderUtils.print16String(dataCommand);
			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * WS向M查询某NE某盘性能
	 * 
	 * @param operationObject
	 * @param actionObject
	 * @throws Exception
	 */

	@SuppressWarnings("static-access")
	public void queryPerformanceObjectByCard(OperationObject operationObject, ActionObject actionObject) throws Exception {
		// P S 05 02
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			// driveUtilObject.setPtnDataObject();// 赋值对象
			driveUtilObject.setPtnService("PM0102");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP

			long l = actionObject.getPerformanceBeginDataTime();
			Date date = new Date(l);
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			String year = calendar.get(calendar.YEAR) + "";
			
			int year_1 = Integer.parseInt(year)/256;
			int year_2 = Integer.parseInt(year)%256;
			byte[] yearByte_1 = CoderUtils.intToBytes(year_1);
			byte[] yearByte_2 = CoderUtils.intToBytes(year_2);
			byte[] monthByte = CoderUtils.intToBytes(calendar.get(calendar.MONTH) + 1);
			byte[] dayByte = CoderUtils.intToBytes(calendar.get(calendar.DAY_OF_MONTH));
			byte[] hourByte = CoderUtils.intToBytes(calendar.get(calendar.HOUR_OF_DAY));
			byte[] minByte = CoderUtils.intToBytes(calendar.get(calendar.MINUTE));
			byte[] secByte = CoderUtils.intToBytes(calendar.get(calendar.SECOND));
			// 历史性能个数
			byte[] performanceCountByte = CoderUtils.intToBytes(actionObject.getPerformanceCount());// 历史性能个数
			if(actionObject.getPerformanceCount() == 0 || actionObject.getPerformanceType() != 0){
				yearByte_1 = CoderUtils.intToBytes(255);
				yearByte_2 = CoderUtils.intToBytes(255);
				monthByte = CoderUtils.intToBytes(0);
				dayByte = CoderUtils.intToBytes(0);
				hourByte = CoderUtils.intToBytes(0);
				minByte = CoderUtils.intToBytes(0);
				secByte = CoderUtils.intToBytes(0);
			}
			// 历史性能起始数
			byte[] performanceBeginCountByte = CoderUtils.intToBytes(actionObject.getPerformanceBeginCount());// 历史性能起始数
			// 性能数据类型（00，20，21，30）
			byte[] performanceTypeByte = CoderUtils.intToBytes(actionObject.getPerformanceType());// 性能数据类型（00，20，21，30）
			// NE地址
			byte[] neAddressByte = CoderUtils.intToBytes(actionObject.getNeObject().getNeAddress());
			// 盘类型1（基本盘类型）
			byte[] masterCardByte = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelType());
			// 盘地址
			byte[] masterCardAddressByte = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelAddress());
			//盘组号
			byte[] masterCardGroup = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelGroupId());
			
			byte[] dataCommand = new byte[15];
			dataCommand[0] = neAddressByte[2];
			dataCommand[1] = neAddressByte[3];
			dataCommand[2] = masterCardByte[masterCardByte.length - 1];
			dataCommand[3] = masterCardGroup[masterCardGroup.length-1];
			dataCommand[4] = masterCardAddressByte[masterCardAddressByte.length-1];
			dataCommand[5] = yearByte_1[yearByte_1.length - 1];
			dataCommand[6] = yearByte_2[yearByte_2.length - 1];
			dataCommand[7] = monthByte[monthByte.length - 1];
			dataCommand[8] = dayByte[dayByte.length - 1];
			dataCommand[9] = hourByte[hourByte.length - 1];
			dataCommand[10] = minByte[minByte.length - 1];
			dataCommand[11] = secByte[secByte.length - 1];
			dataCommand[12] = performanceCountByte[performanceCountByte.length - 1];
			dataCommand[13] = performanceBeginCountByte[performanceBeginCountByte.length - 1];
			dataCommand[14] = performanceTypeByte[performanceTypeByte.length - 1];
			
			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
			
			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 更新TMC OAM故障管理配置 保护
	 * 
	 * @param operationObject回调对象
	 * @param actionObject事件对象
	 * @throws Exception
	 */
	public void updataTMCOAMBugControl(OperationObject operationObject, ActionObject actionObject) throws Exception {
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getLSPProtectionList());// 赋值对象
			driveUtilObject.setPtnService("CG0103");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			// 临时调用
			AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();

			byte[] dataCommand = analysisObjectServiceI.AnalysisTMCOAMBugControlToCommand(actionObject.getNeObject(), actionObject.getTmcOAMBugControlObject());

			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片

			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞

		} catch (Exception e) {
			throw e;
		}

	}

	/**
	 * 更新Port LAG 端口聚合
	 */
	public void updatePortLAG(OperationObject operationObject, ActionObject actionObject) throws Exception {
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getElineObjectList());// 赋值对象
			driveUtilObject.setPtnService("CG0103");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			// 临时调用
			AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();
			byte[] dataCommand = analysisObjectServiceI.AnalysisPortLAGToCommand(actionObject.getNeObject(), actionObject.getPortLAGList());

			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片

			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 更新tmc通道保护
	 * 
	 * @param operationObject
	 * @param actionObject
	 * @throws Exception
	 */
	public void updateTMCTunnelProtect(OperationObject operationObject, ActionObject actionObject) throws Exception {

		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getElineObjectList());// 赋值对象
			driveUtilObject.setPtnService("CG0103");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			// 临时调用
			AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();
			byte[] dataCommand = analysisObjectServiceI.AnalysisTMCTunnelProtectTocommands(actionObject.getNeObject(), actionObject.getTmcTunnelProectList());

			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片

			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 更新时间同步
	 * 
	 * @param operationObject
	 * @param actionObject
	 * @throws Exception
	 */
	public void updataTimeSynchronize(OperationObject operationObject, ActionObject actionObject) throws Exception {
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getElineObjectList());// 赋值对象
			driveUtilObject.setPtnService("CG0103");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			// 临时调用
			AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();
			byte[] dataCommand = analysisObjectServiceI.AnalysisTimeSynchronizeObjectToCommands(actionObject.getNeObject(), actionObject.getTimeSynchronizeObject());

			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片

			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 更新TMS OAM配置块
	 * 
	 * @param operationObject
	 * @param actionObject
	 * @throws Exception
	 */
	public void updataTmsOamObject(OperationObject operationObject, ActionObject actionObject) throws Exception {

		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getElineObjectList());// 赋值对象
			driveUtilObject.setPtnService("CG0103");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			// 临时调用
			AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();
			byte[] dataCommand = analysisObjectServiceI.AnalysisTMSOAMObjectToCommands(actionObject.getNeObject(), actionObject.getTmsOamObject());

			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片

			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 更新 PHB到TMC/TMP EXP映射表(09H)
	 */
	public void updatePHBToEXP(OperationObject operationObject, ActionObject actionObject) throws Exception {
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getElineObjectList());// 赋值对象
			driveUtilObject.setPtnService("CG0103");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			// 临时调用
			AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();
			byte[] dataCommand = analysisObjectServiceI.analysisPHBToCommand(actionObject.getNeObject(), actionObject.getPhbToEXPObject());

			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片

			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞

		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 更新TMS OAM故障管理配置 保护
	 * 
	 * @param operationObject回调对象
	 * @param actionObject事件对象
	 * @throws Exception
	 */
	public void updataTMSOAMBugControl(OperationObject operationObject, ActionObject actionObject) throws Exception {
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getLSPProtectionList());// 赋值对象
			driveUtilObject.setPtnService("CG0103");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			// 临时调用
			AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();

			byte[] dataCommand = analysisObjectServiceI.AnalysisTMSOAMBugControlToCommand(actionObject.getNeObject(), actionObject.getTmsOAMBugControlObject());

			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片

			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞

		} catch (Exception e) {
			throw e;
		}

	}
	
	/**
	 * 更新端口高级配置
	 * 
	 * @param operationObject回调对象
	 * @param actionObject事件对象
	 * @throws Exception
	 */
	public void updataPortSeniorConfig(OperationObject operationObject, ActionObject actionObject) throws Exception{
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getLSPProtectionList());// 赋值对象
			driveUtilObject.setPtnService("CG0103");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			// 临时调用
			AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();

			byte[] dataCommand = analysisObjectServiceI.AnalysisPortSeniorConfigCommandToCommands(actionObject.getNeObject(), actionObject.getSeniorPortConfigObjectlist());

			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片

			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞

		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 *  更新EXP to PHB 
	 */
	public void updateEXPToPHB(OperationObject operationObject,ActionObject actionObject)throws Exception{
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getLSPProtectionList());// 赋值对象
			driveUtilObject.setPtnService("CG0103");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			// 临时调用
			AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();

			byte[] dataCommand = analysisObjectServiceI.analysisEXPToCommand(actionObject.getNeObject(), actionObject.getExpToPHBObject());

			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片

			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞

		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 更新以太网链路 OAM
	 */
	public void updateETHLinkOAM(OperationObject operationObject,ActionObject actionObject)throws Exception{
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getLSPProtectionList());// 赋值对象
			driveUtilObject.setPtnService("CG0103");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			// 临时调用
			AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();

			byte[] dataCommand = analysisObjectServiceI.analysisETHLinkOAMToCommand(actionObject.getNeObject(), actionObject.getEthLinkOAMObject());

			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
			
			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 更新 环网保护(04H) 
	 */
	public void updateRoundProtection(OperationObject operationObject,ActionObject actionObject)throws Exception{
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getLSPProtectionList());// 赋值对象
			driveUtilObject.setPtnService("CG0103");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			// 临时调用
			AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();
			byte[] dataCommand = analysisObjectServiceI.analysisRoundPToCommand(actionObject.getNeObject(), actionObject.getRoundProtectionObject());

			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片

			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 更新IGMP SNOOPING配置(17H)
	 */
	public void updateIGMPSNOOPing(OperationObject operationObject,ActionObject actionObject)throws Exception{
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getLSPProtectionList());// 赋值对象
			driveUtilObject.setPtnService("CG0103");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			// 临时调用
			AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();
			byte[] dataCommand = analysisObjectServiceI.analysisIGMPToCommand(actionObject.getNeObject(), actionObject.getIgmpsnooping());

			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片

			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 更新 全局配置块(03H)
	 */
	public void updateGloble(OperationObject operationObject,ActionObject actionObject)throws Exception{
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getLSPProtectionList());// 赋值对象
			driveUtilObject.setPtnService("CG0103");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			// 临时调用
			AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();
			byte[] dataCommand = analysisObjectServiceI.analysisGlpbalToCommand(actionObject.getNeObject(), actionObject.getGlobalObject());

			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片

			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 更新 时间同步管理(1CH)
	 */
	public void updateTimeSync(OperationObject operationObject,ActionObject actionObject)throws Exception{
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getLSPProtectionList());// 赋值对象
			driveUtilObject.setPtnService("CG0103");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			// 临时调用
			AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();
			byte[] dataCommand = analysisObjectServiceI.analysisTimeSynctoCommand(actionObject.getNeObject(), actionObject.getTimesyncobject());

			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片

			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 更新 静态组播 
	 */
	public void updateGroupSpread(OperationObject operationObject,ActionObject actionObject)throws Exception{
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getLSPProtectionList());// 赋值对象
			driveUtilObject.setPtnService("CG0103");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			// 临时调用
			AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();
			byte[] dataCommand = analysisObjectServiceI.analysisGroupSpreadToCommand(actionObject.getNeObject(), actionObject.getGroupSpreadObject());

			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片

			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 更新 单卡基本信息
	 */
	public void updateSingleObject(OperationObject operationObject,ActionObject actionObject)throws Exception{
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getLSPProtectionList());// 赋值对象
			driveUtilObject.setPtnService("CG0103");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			// 临时调用
			AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();
			byte[] dataCommand = analysisObjectServiceI.analysisSingleToCommand(actionObject.getNeObject(),actionObject.getSingleObject());

			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片

			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 更新 静态单播(0BH)
	 */
	public void updateStaticUnicast(OperationObject operationObject,ActionObject actionObject)throws Exception{
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getLSPProtectionList());// 赋值对象
			driveUtilObject.setPtnService("CG0103");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			// 临时调用
			AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();
			byte[] dataCommand = analysisObjectServiceI.analysisStaticUToCommand(actionObject.getNeObject(), actionObject.getStaticUnicast());

			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片

			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 更新Clockobject
	 * 
	 * @param operationObject
	 * @param actionObject
	 * @throws Exception
	 */
	public void updataClockObject(OperationObject operationObject, ActionObject actionObject) throws Exception {
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getLSPProtectionList());// 赋值对象
			driveUtilObject.setPtnService("CG0103");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			// // 临时调用
			AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();
			//
			byte[] dataCommand = analysisObjectServiceI.AnalysisClockObjectToCommands(actionObject.getNeObject(), actionObject.getClockObject());

			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片

			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞

		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 更新管理配置
	 * @param operationObject回调对象
	 * @param actionObject事件对象
	 * @throws Exception
	 */
	public void updateManagement(OperationObject operationObject, ActionObject actionObject) throws Exception {
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getManagementObject());// 赋值对象
			driveUtilObject.setPtnService("CG0101");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			// 临时调用
			AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();
			byte[] dataCommand = analysisObjectServiceI.AnalysisManagementObjectToCommands(actionObject.getNeObject(), actionObject.getManagementObject());
//			CoderUtils.print16String(dataCommand);
			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
			
			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 软件升级配置 
	 * @param operationObject回调对象
	 * @param actionObject事件对象
	 * @throws Exception
	 */
	public void updateSoftwareUpdate(OperationObject operationObject, ActionObject actionObject) throws Exception {
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getSoftwareUpdate());// 赋值对象
			driveUtilObject.setPtnService("UD0101");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			// 临时调用
			AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();
			byte[] dataCommand = analysisObjectServiceI.AnalysisSoftwareUpdateObjectToCommands(actionObject.getNeObject(), actionObject.getSoftwareUpdate());

			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
			
			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 更新回答单盘信息
	 * @param operationObject 回调对象
	 * @param actionObject 事件对象
	 * @throws Exception
	 */
	public void updateResponsePan(OperationObject operationObject,ActionObject actionObject) throws Exception {
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
//			driveUtilObject.setPtnDataObject(actionObject.getResponsePan());// 赋值对象
			driveUtilObject.setPtnService("MS0703");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			// 临时调用
			AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();
//			byte[] dataCommand = analysisObjectServiceI.AnalysisResponsePanTable(actionObject.getNeObject(), actionObject.getResponsePan());
			
			byte[] dataCommand = new byte[8];
			// NE地址
			byte[] neaddress = CoderUtils.intToBytes(actionObject.getNeObject().getNeAddress());
			dataCommand[0] = neaddress[2];
			dataCommand[1] = neaddress[3];
			// 盘类型
			byte[] platetype = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelType());
			dataCommand[2] = platetype[0];
			dataCommand[3] = platetype[1];
			dataCommand[4] = platetype[2];
			dataCommand[5] = platetype[3];
			// 盘地址
			byte[] plateaddress = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelAddress());
			dataCommand[6] = plateaddress[2];
			dataCommand[7] = plateaddress[3];
			
			
			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞
		} catch (Exception e){
			throw e;
		}
	} 
	/**
	 * WS向M查询某NE某盘某配置块信息
	 * 
	 * @param operationObject
	 * @param actionObject
	 * @throws Exception
	 */
	public void queryBusinessObjectByCard(OperationObject operationObject, ActionObject actionObject,int number) throws Exception {
		// M S 05 03
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			// driveUtilObject.setPtnDataObject();// 赋值对象
			driveUtilObject.setPtnService("CG0201");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			byte[] dataCommand = new byte[6];

			// NE地址
			byte[] neaddress = CoderUtils.intToBytes(actionObject.getNeObject().getNeAddress());
			dataCommand[0] = neaddress[2];
			dataCommand[1] = neaddress[3];
			// 盘类型
			byte[] platetype = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelType());
			dataCommand[2] = platetype[3];
			//盘组号
			byte[] controlPanelGroupId = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelGroupId());
			dataCommand[3] = controlPanelGroupId[3];
			
			// 盘地址
			byte[] plateaddress = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelAddress());	
			dataCommand[4] = plateaddress[3];

			//配置块
			byte[] config = CoderUtils.intToBytes(number);
			dataCommand[5] = config[3];
			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
			CoderUtils.print16String(dataCommand);
			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 端口配置
	 */
	@Override
	public void updatePortConfig(OperationObject operationObject, ActionObject actionObject) throws Exception {
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getProtObjectList());// 赋值对象
			driveUtilObject.setPtnService("CG0103");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			// 临时调用
			AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();
			byte[] dataCommand = analysisObjectServiceI.AnalysisPortConfigToCommands(actionObject.getNeObject(), actionObject.getProtObjectList().get(0));

			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
			
			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中

			Thread.sleep(200);// 休眠防止拥塞
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 回复确认帧
	 * @param operationObject
	 * @param actionObject
	 * @throws Exception
	 */
	public void confirmAlarmFrame(OperationObject operationObject, ActionObject actionObject,int uddp,String ip) throws Exception {
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getProtObjectList());// 赋值对象
			driveUtilObject.setPtnService("AS0101");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			// 临时调用
			byte[] dataCommand = {0};

			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Confirm-PDU",uddp);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
			driveUtilObject.setDestinationIP(ip);//设置目的IP
			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 回复确认帧
	 * @param operationObject
	 * @param actionObject
	 * @throws Exception
	 */
//	public void confirmAlarmFrame(OperationObject operationObject, ActionObject actionObject,int uddp,String ip, byte[] dataCmd) throws Exception {
//		try {
//			DriveUtilObject driveUtilObject = new DriveUtilObject();
//			driveUtilObject.setPtnDataObject(actionObject.getProtObjectList());// 赋值对象
//			driveUtilObject.setPtnService("AS0101");// 赋值业务类型
//			driveUtilObject.setDirection(0);// WS向PTN发送
//			driveUtilObject.setStates(0);// 待发送状态
//			driveUtilObject.setSendDate(new Date());// 赋值发送时间
//			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
//			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
//			// 临时调用
//			byte[] dataCommand = new byte[dataCmd.length+1];
//			System.arraycopy(dataCmd, 0, dataCommand, 0, dataCmd.length);
//			dataCommand[dataCommand.length-1] = 1;
//			ExceptionManage.dispose(new Exception(CoderUtils.print16String(dataCommand)), this.getClass());
//			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Confirm-PDU",uddp);// 解析对象为命令
//			CoderUtils.print16String(driveUtilObject.getSendCommands());
//			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
//			driveUtilObject.setDestinationIP(ip);//设置目的IP
//			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
//			Thread.sleep(200);// 休眠防止拥塞
//		} catch (Exception e) {
//			throw e;
//		}
//	}
	
	/**
	 * 强制倒换命令转换
	 */
	@Override
	public void updateProtectRorate(OperationObject operationObject, ActionObject actionObject) throws Exception {
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getProtectRorateObject());// 赋值对象
			driveUtilObject.setPtnService("CM0101");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			byte[] dataCommand = new byte[12];
			// NE地址
			byte[] neaddress = CoderUtils.intToBytes(actionObject.getNeObject().getNeAddress());
			dataCommand[0] = neaddress[2];
			dataCommand[1] = neaddress[3];
			// 盘类型
			byte[] platetype = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelType());
			dataCommand[2] = platetype[3];
			//盘组号
			byte[] controlPanelGroupId = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelGroupId());
			dataCommand[3] = controlPanelGroupId[3];
			// 盘地址
			byte[] plateaddress = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelAddress());	
			dataCommand[4] = plateaddress[3];
			//控制代码
			dataCommand[5] = CoderUtils.intToBytes(254)[3];
			//命令类型
			dataCommand[6] = CoderUtils.intToBytes(2)[3];
			//指命令			
//			dataCommand[7] = CoderUtils.intToBytes(3)[3];
			//指令附加字节
			dataCommand[8] = CoderUtils.intToBytes(0)[3];
			//指命令、参数1,参数2
			this.getRorate(actionObject.getProtectRorateObject(), dataCommand);
			CoderUtils.print16String(dataCommand);
			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
			
			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞
		} catch (Exception e) {
			throw e;
		}
	}
	
	/** 
	 * 获得强制倒换参数
	 * @param protectRorateInfo
	 * @return
	 */
	private void getRorate(ProtectRorateObject protectRorateObject,byte[] dataCommand) {
		
		//强制倒换类型
		dataCommand[9] = CoderUtils.intToBytes(protectRorateObject.getRoate())[3];	
           if(protectRorateObject.getRoateType().equals("tunnel")){
   			//指命令			
   			dataCommand[7] = CoderUtils.intToBytes(3)[3];
   			if(protectRorateObject.getSiteRorate() ==1 ){//全网倒换
   				dataCommand[10] = CoderUtils.intToBytes(0)[3];
   				dataCommand[11] = CoderUtils.intToBytes(0)[3];
   				}else{
			        dataCommand[10] = CoderUtils.intToBytes(protectRorateObject.getTunnelbusinessid()/256)[3];
			        dataCommand[11] = CoderUtils.intToBytes(protectRorateObject.getTunnelbusinessid()%256)[3];
			    }
           }else if(protectRorateObject.getRoateType().equals("pw")){
        	 //指命令			
      		dataCommand[7] = CoderUtils.intToBytes(5)[3];
      		if(protectRorateObject.getSiteRorate() ==1 ){//全网倒换
   				dataCommand[10] = CoderUtils.intToBytes(0)[3];
   				dataCommand[11] = CoderUtils.intToBytes(0)[3];
   				}else{
   					dataCommand[10] = CoderUtils.intToBytes(protectRorateObject.getPwbussinessid()/256)[3];
   		   			dataCommand[11] = CoderUtils.intToBytes(protectRorateObject.getPwbussinessid()%256)[3]; 	
   				}           
		   }
	}
	
	/**
	 * 新建网元下发配置
	 * @param operationObject
	 * @param actionObject
	 * @throws Exception
	 */
	public void updateSite(OperationObject operationObject,ActionObject actionObject)throws Exception{
		WorkSpace workSp = null;
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnService("JC0423");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			byte[] dataCommand = new byte[7];
			// NE地址
			byte[] neaddress = CoderUtils.intToBytes(actionObject.getNeObject().getNeAddress());
			dataCommand[0] = neaddress[2];
			dataCommand[1] = neaddress[3];
			//工作站
			dataCommand[2] = CoderUtils.intToBytes(1)[3];
			workSp = actionObject.getManagementObject().getWorkSpaces().get(0); 
			String[] ips = workSp.getWorkSpaceIp().split("\\.");
			for (int i = 0; i < ips.length; i++) {
				dataCommand[3+i] = CoderUtils.intToBytes(Integer.valueOf(ips[i]))[3];
			}
			
			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
			
			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}  
	}
	
	/**
	 * 板卡复位
	 */
	@Override
	public void updateSlotReset(OperationObject operationObject, ActionObject actionObject) throws Exception {
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnService("CM0101");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			byte[] dataCommand = new byte[10];
			// NE地址
			byte[] neaddress = CoderUtils.intToBytes(actionObject.getNeObject().getNeAddress());
			dataCommand[0] = neaddress[2];
			dataCommand[1] = neaddress[3];
			// 盘类型
			byte[] platetype = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelType());
			dataCommand[2] = platetype[3];
			//盘组号
			byte[] controlPanelGroupId = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelGroupId());
			dataCommand[3] = controlPanelGroupId[3];
			// 盘地址
			byte[] plateaddress = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelAddress());	
			dataCommand[4] = plateaddress[3];
			//控制代码
			dataCommand[5] = CoderUtils.intToBytes(254)[3]; 
			//命令类型
			dataCommand[6] = CoderUtils.intToBytes(0)[3];
			//指命令
			if(actionObject.getNeObject().getControlPanelType()==118489105 ){
				//指命令
				dataCommand[7] = CoderUtils.intToBytes(187)[3];
				//指令附加字节
				dataCommand[8] = CoderUtils.intToBytes(187)[3];
				//参数1,参数2
				dataCommand[9] = CoderUtils.intToBytes(0)[3];
				
			}else{
				dataCommand[7] = CoderUtils.intToBytes(1)[3];
				//指令附加字节
				dataCommand[8] = CoderUtils.intToBytes(0)[3];
				dataCommand[9] = CoderUtils.intToBytes(1)[3];
			}
//			dataCommand[7] = CoderUtils.intToBytes(1)[3];
		
			//参数1,参数2
//			dataCommand[9] = CoderUtils.intToBytes(1)[3];
												
			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
			
			CoderUtils.print16String(dataCommand);
			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * tdm环回
	 */
	@Override
	public void updateTdmLoopBack(OperationObject operationObject, ActionObject actionObject) throws Exception {
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			
			driveUtilObject.setPtnService("CM0101");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			byte[] dataCommand = new byte[10];
			// NE地址
			byte[] neaddress = CoderUtils.intToBytes(actionObject.getNeObject().getNeAddress());
			dataCommand[0] = neaddress[2];
			dataCommand[1] = neaddress[3];
			// 盘类型
			byte[] platetype = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelType());
			dataCommand[2] = platetype[3];
			//盘组号
			byte[] controlPanelGroupId = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelGroupId());
			dataCommand[3] = controlPanelGroupId[3];
			// 盘地址
			byte[] plateaddress = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelAddress());	
			dataCommand[4] = plateaddress[3];
			//控制代码
			dataCommand[5] = CoderUtils.intToBytes(254)[3];
			//命令类型 
			dataCommand[6] = CoderUtils.intToBytes(3)[3];
			//指命令
			TdmLoopObject tdm = actionObject.getTdmLoopObject();
			if(tdm.getLoopType() == 0){
				//线路环回
				dataCommand[7] = CoderUtils.intToBytes(58)[3];
			}else{
				//设备环回
				dataCommand[7] = CoderUtils.intToBytes(54)[3];
			}
			//指令附加字节
			dataCommand[8] = CoderUtils.intToBytes(tdm.getSwitchStatus())[3];
			dataCommand[9] = CoderUtils.intToByte(tdm.getLegId())[3];
			CoderUtils.print16String(dataCommand);
			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
			
			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 网元属性
	 * @param operationObject
	 * @param actionObject
	 */
	public void querySiteAttribute(OperationObject operationObject, ActionObject actionObject) {
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			
			driveUtilObject.setPtnService("AS0301");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			// NE地址
			byte[] neAddressByte = CoderUtils.intToBytes(actionObject.getNeObject().getNeAddress());
			byte[] dataCommand = new byte[2];
			dataCommand[0] = neAddressByte[2];
			dataCommand[1] = neAddressByte[3];
			
			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
			
			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
	}
	
	/**
	 * 网元校时
	 * @param operationObject
	 * @param actionObject
	 */
	public void updateCurrectTime(OperationObject operationObject, ActionObject actionObject) {
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			
			driveUtilObject.setPtnService("CM0102");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			//long l = System.currentTimeMillis();
			long l=actionObject.getNeObject().getL();		
			Date date = new Date(l);
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			String year = calendar.get(calendar.YEAR) + "";
			
			int year_1 = Integer.parseInt(year)/256;
			int year_2 = Integer.parseInt(year)%256;
			byte[] yearByte_1 = CoderUtils.intToBytes(year_1);
			byte[] yearByte_2 = CoderUtils.intToBytes(year_2);
			byte[] monthByte = CoderUtils.intToBytes(calendar.get(calendar.MONTH) + 1);
			byte[] dayByte = CoderUtils.intToBytes(calendar.get(calendar.DAY_OF_MONTH));
			byte[] hourByte = CoderUtils.intToBytes(calendar.get(calendar.HOUR_OF_DAY));
			byte[] minByte = CoderUtils.intToBytes(calendar.get(calendar.MINUTE));
			byte[] secByte = CoderUtils.intToBytes(calendar.get(calendar.SECOND));
			// NE地址
			byte[] neAddressByte = CoderUtils.intToBytes(actionObject.getNeObject().getNeAddress());
			byte[] dataCommand = new byte[9];
			dataCommand[0] = yearByte_1[yearByte_1.length - 1];
			dataCommand[1] = yearByte_2[yearByte_2.length - 1];
			dataCommand[2] = monthByte[monthByte.length - 1];
			dataCommand[3] = dayByte[dayByte.length - 1];
			dataCommand[4] = hourByte[hourByte.length - 1];
			dataCommand[5] = minByte[minByte.length - 1];
			dataCommand[6] = secByte[secByte.length - 1];
			dataCommand[7] = neAddressByte[2];
			dataCommand[8] = neAddressByte[3];
			
			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
			
			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
	}
	
	/**
	 * 清除设备数据
	 * @param operationObject
	 * @param actionObject
	 */
	public void updateClearSite(OperationObject operationObject, ActionObject actionObject) {
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			
			driveUtilObject.setPtnService("CG0301");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
		
			// NE地址
			byte[] neAddressByte = CoderUtils.intToBytes(actionObject.getNeObject().getNeAddress());
			// 盘类型1（基本盘类型）
			byte[] masterCardByte = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelType());
			// 盘地址
			byte[] masterCardAddressByte = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelAddress());
			//盘组号
			byte[] masterCardGroup = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelGroupId());
			
			byte[] dataCommand = new byte[6];
			dataCommand[0] = neAddressByte[2];
			dataCommand[1] = neAddressByte[3];
			dataCommand[2] = masterCardByte[masterCardByte.length - 1];
			dataCommand[3] = masterCardGroup[masterCardGroup.length-1];
			dataCommand[4] = masterCardAddressByte[masterCardAddressByte.length-1];
			dataCommand[5] = CoderUtils.intToBytes(50)[3];
			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
			
			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
	}
	
	/**
	 * 网元上载数据
	 * @param operationObject
	 * @param actionObject
	 */
	public void uploadSite(OperationObject operationObject, ActionObject actionObject) {
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			
			driveUtilObject.setPtnService("CG0202");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
		
			// NE地址
			byte[] neAddressByte = CoderUtils.intToBytes(actionObject.getNeObject().getNeAddress());
			byte[] dataCommand = new byte[3];
			dataCommand[0] = neAddressByte[2];
			dataCommand[1] = neAddressByte[3];
			dataCommand[2] = CoderUtils.intToByte(1)[3];
			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
	}
	/**
	 * 时钟命令转换
	 */
	@Override
	public void clockRorate(OperationObject operationObject, ActionObject actionObject) throws Exception {
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			
			driveUtilObject.setPtnService("CM0101");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			ClockObject clockObject = actionObject.getClockObject();
			byte[] dataCommand = null;
			if(clockObject.getClockRorate() == 30){
				//指命令
				dataCommand = new byte[9];
				dataCommand[7] = CoderUtils.intToBytes(2)[3];
				dataCommand[8] = CoderUtils.intToBytes(0)[3];
			}else{
				//指命令
				dataCommand = new byte[10];
				dataCommand[7] = CoderUtils.intToBytes(1)[3];
				dataCommand[8] = CoderUtils.intToBytes(0)[3];
				dataCommand[9] = CoderUtils.intToBytes(clockObject.getClockRorate())[3];
			}
			// NE地址
			byte[] neaddress = CoderUtils.intToBytes(actionObject.getNeObject().getNeAddress());
			dataCommand[0] = neaddress[2];
			dataCommand[1] = neaddress[3];
			// 盘类型
			byte[] platetype = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelType());
			dataCommand[2] = platetype[3];
			//盘组号
			byte[] controlPanelGroupId = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelGroupId());
			dataCommand[3] = controlPanelGroupId[3];
			// 盘地址
			byte[] plateaddress = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelAddress());	
			dataCommand[4] = plateaddress[3];
			//控制代码
			dataCommand[5] = CoderUtils.intToBytes(254)[3];
			//命令类型
			dataCommand[6] = CoderUtils.intToBytes(1)[3];
			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
			CoderUtils.print16String(dataCommand);
			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 网元下载数据
	 * @param operationObject
	 * @param actionObject
	 */
	public void downloadSite(OperationObject operationObject, ActionObject actionObject) {
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			
			driveUtilObject.setPtnService("CG0203");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
		
			// NE地址
			byte[] neAddressByte = CoderUtils.intToBytes(actionObject.getNeObject().getNeAddress());
			byte[] dataCommand = getBytes(actionObject.getNeObject().getFile());
			dataCommand[0] = neAddressByte[2];
			dataCommand[1] = neAddressByte[3];
			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
			
			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
	}
	
	 /** 
     * 获得指定文件的byte数组 
     */  
    public static byte[] getBytes( File file){  
        byte[] buffer = null;  
        try {   
            FileInputStream fis = new FileInputStream(file);  
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);  
            byte[] b = new byte[1000];  
            int n;  
            while ((n = fis.read(b)) != -1) {  
                bos.write(b, 0, n);  
            }  
            fis.close();  
            bos.close();  
            buffer = bos.toByteArray();  
        } catch (FileNotFoundException e) {  
            ExceptionManage.dispose(e,DriveService.class);  
        } catch (IOException e) {  
            ExceptionManage.dispose(e,DriveService.class);  
        }  
        return buffer;  
    }  
    
    /**
	 * WS向M查询某NE某盘某配置块信息
	 * 
	 * @param operationObject
	 * @param actionObject
	 * @throws Exception
	 */
	public void querySiteStatus(OperationObject operationObject, ActionObject actionObject,int number) throws Exception {
		// PM 04 01
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			// driveUtilObject.setPtnDataObject();// 赋值对象
			driveUtilObject.setPtnService("PM0401");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			byte[] dataCommand = new byte[10];

			// NE地址
			byte[] neaddress = CoderUtils.intToBytes(actionObject.getNeObject().getNeAddress());
			dataCommand[0] = neaddress[2];
			dataCommand[1] = neaddress[3];
			// 盘类型
			byte[] platetype = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelType());
			dataCommand[2] = platetype[3];
			//盘组号
			byte[] controlPanelGroupId = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelGroupId());
			dataCommand[3] = controlPanelGroupId[3];
			
			// 盘地址
			byte[] plateaddress = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelAddress());	
			dataCommand[4] = plateaddress[3];

			//状态扩展类型
			byte[] config = CoderUtils.intToBytes(2);
			dataCommand[5] = config[3];
			
			//状态分块总数
			byte[] pagesMaxCount = CoderUtils.intToBytes(1);
			dataCommand[6] = pagesMaxCount[3];
			
			dataCommand[7] = (byte) CoderUtils.char2ASCII('D');
			dataCommand[8] = (byte) CoderUtils.char2ASCII('A');
			
//			//当前状态分块数
//			byte[] pagesCount = CoderUtils.intToBytes(1);
//			dataCommand[9] = pagesCount[3];
			
			//标志码
			byte[] mark = CoderUtils.intToBytes(number);
			dataCommand[9] = mark[3];
			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片

			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞

		} catch (Exception e) {
			throw e;
		}
	}
	

	/**
	 * 更新msPW
	 * 
	 * @param operationObject回调对象
	 * @param actionObject事件对象
	 * @throws Exception
	 */
	public void updateMsPW(OperationObject operationObject, ActionObject actionObject) throws Exception {
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getPwObjectList());// 赋值对象
			driveUtilObject.setPtnService("CG0103");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			// 临时调用
			AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();
			byte[] dataCommand = analysisObjectServiceI.AnalysisMsPwToCommand(actionObject.getNeObject(), actionObject.getMsPwObjects());
			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片

			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞

		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 回复确认帧
	 * @param operationObject
	 * @param actionObject
	 * @throws Exception
	 */
	public void confirmFrame(OperationObject operationObject, ActionObject actionObject,int uddp) throws Exception {
		FieldService_MB fieldService = null;
		SiteService_MB siteService = null;
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getProtObjectList());// 赋值对象
			driveUtilObject.setPtnService("AS0101");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			int fielId = actionObject.getNeObject().getNeAddress()/256;
		    fieldService = (FieldService_MB) ConstantUtil.serviceFactory.newService_MB(Services.Field);
			Field field = new Field();
			field.setGroupId(fielId);
			if(fieldService.select(field) != null && fieldService.select(field).size()>0){
				field = fieldService.select(field).get(0);
				SiteInst siteInst = new SiteInst();
				siteInst.setFieldID(fielId);
				siteInst.setSite_Inst_Id(field.getmSiteId());
				siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE);
				if(siteService.select(siteInst) != null && siteService.select(siteInst).size()>0){
					siteInst = siteService.select(siteInst).get(0);
					ptnaddress = siteInst.getCellDescribe(); 
					uDPNetworkServer.set_address(ptnaddress);
					// 临时调用
					byte[] dataCommand = {0};

					driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Confirm-PDU",uddp);// 解析对象为命令
					driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
					super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
					Thread.sleep(200);// 休眠防止拥塞
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			UiUtil.closeService_MB(fieldService);
			UiUtil.closeService_MB(siteService);
		}
	}
	
	/**
	 * 以太网OAM
	 * @param neObject
	 * @throws Exception
	 */
	
	public void updateETHoamConfig(OperationObject operationObject, ActionObject actionObject) throws Exception{
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getProtObjectList());// 赋值对象
			driveUtilObject.setPtnService("CG0103");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			// 临时调用
			AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();
			byte[] dataCommand = analysisObjectServiceI.AnalysisEObjectToCommands(actionObject.getNeObject(), actionObject.getEthoamAllObject());

			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
			
            
			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 根据网元Id清除设备性能
	 * @param operationObject
	 * @param actionObject
	 * @throws Exception 
	 */
	public void cleanCurrPerformanceBySite(OperationObject operationObject,ActionObject actionObject) throws Exception {
		try {
			//CM0101
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			
			driveUtilObject.setPtnService("CM0101");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			byte[] dataCommand = new byte[9];
			// NE地址
			byte[] neaddress = CoderUtils.intToBytes(actionObject.getNeObject().getNeAddress());
			dataCommand[0] = neaddress[2];
			dataCommand[1] = neaddress[3];
			// 盘类型
			byte[] platetype = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelType());
			dataCommand[2] = platetype[3];
			//盘组号
			byte[] controlPanelGroupId = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelGroupId());
			dataCommand[3] = controlPanelGroupId[3];
			// 盘地址
			byte[] plateaddress = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelAddress());	
			dataCommand[4] = plateaddress[3];
			//控制代码
			dataCommand[5] = CoderUtils.intToBytes(254)[3];
			//命令类型
			dataCommand[6] = CoderUtils.intToBytes(0)[3];
			//指令码
			dataCommand[7] = CoderUtils.intToBytes(178)[3];
			//指令附加字节
			dataCommand[8] = CoderUtils.intToBytes(0)[3];
			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
			CoderUtils.print16String(dataCommand);
			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	/**
	 * 性能门限配置块
	 * 
	 * @param operationObject回调对象
	 * @param actionObject事件对象
	 * @throws Exception
	 */
	public void updataPmLimite(OperationObject operationObject, ActionObject actionObject) throws Exception {
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getPwObjectList());// 赋值对象
			driveUtilObject.setPtnService("CG0103");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			// 临时调用
			AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();
			byte[] dataCommand = analysisObjectServiceI.AnalysisPmLimiteObjectToCommands(actionObject.getNeObject(), actionObject.getPmValueLimiteObject());

			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞

		} catch (Exception e) {
			throw e;
		}

	}
	
	/**
	 * 更新QinQ
	 * 
	 * @param OperationObject回调对象
	 * @param actionObject事件对象
	 * @throws Exception
	 * 
	 */
	public void updataQinQ(OperationObject operationObject, ActionObject actionObject) throws Exception {
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getQinQObjects());// 赋值对象
			driveUtilObject.setPtnService("CG0103");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			// 临时调用
			AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();
			byte[] dataCommand = analysisObjectServiceI.AnalysisQinQToCommand(actionObject.getNeObject(), actionObject.getQinQObjects());
			CoderUtils.print16String(dataCommand);
			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 更新黑名单mac
	 * @param operationObject
	 * @param actionObject
	 * @throws Exception 
	 */
	public void updateMacManage(OperationObject operationObject,ActionObject actionObject) throws Exception {
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getMacManageObjectList());// 赋值对象
			driveUtilObject.setPtnService("CG0103");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			// 临时调用
			AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();
			byte[] dataCommand = analysisObjectServiceI.AnalysisMacManageToCommands(actionObject.getNeObject(), actionObject.getMacManageObjectList());
			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞

		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 更新二层静态mac
	 * @param operationObject
	 * @param actionObject 
	 * @throws Exception 
	 */
	public void updateSecondMacStudy(OperationObject operationObject,ActionObject actionObject) throws Exception {
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getMacManageObjectList());// 赋值对象
			driveUtilObject.setPtnService("CG0103");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			// 临时调用
			AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();
			byte[] dataCommand = analysisObjectServiceI.AnalysisSecondMacManageToCommands(actionObject.getNeObject(), actionObject.getsecondMacStudyObjectList());
			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞

		} catch (Exception e) {
			throw e;
		}
	}

	 /**
	 * WS向M查询某NE某盘某配置块信息
	 * 
	 * @param operationObject
	 * @param actionObject
	 * @throws Exception
	 */
	public void queryOamStatus(OperationObject operationObject, ActionObject actionObject) throws Exception {
		// PM 04 01
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			// driveUtilObject.setPtnDataObject();// 赋值对象
			driveUtilObject.setPtnService("TS0504");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			byte[] dataCommand;
			
			if(actionObject.getNeObject().getStatusMark() == 65){
				dataCommand = new byte[8];
				dataCommand[6] =(CoderUtils.intToBytes(actionObject.getNeObject().getNeStatus()))[2];
				dataCommand[7] =(CoderUtils.intToBytes(actionObject.getNeObject().getNeStatus()))[3];
			}else if(actionObject.getNeObject().getNeStatus()>0){
				dataCommand = new byte[7];
				dataCommand[6] =( CoderUtils.intToBytes(actionObject.getNeObject().getNeStatus()))[3];
			}else{
				dataCommand = new byte[6];
			}
			// NE地址
			byte[] neaddress = CoderUtils.intToBytes(actionObject.getNeObject().getNeAddress());
			dataCommand[0] = neaddress[2];
			dataCommand[1] = neaddress[3];
			// 盘类型
			byte[] platetype = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelType());
			dataCommand[2] = platetype[3];
			//盘组号
			byte[] controlPanelGroupId = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelGroupId());
			dataCommand[3] = controlPanelGroupId[3];
			
			// 盘地址
			byte[] plateaddress = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelAddress());	
			dataCommand[4] = plateaddress[3];

			//监视类型
			
			byte[] config = CoderUtils.intToBytes(actionObject.getNeObject().getStatusMark());
			dataCommand[5] = config[3];
			
			CoderUtils.print16String(dataCommand);
			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片

			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞

		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 强制倒换命令转换
	 */
	@Override
	public void updateOamPing(OperationObject operationObject, ActionObject actionObject) throws Exception {
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnService("CM0101");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			
			PingOrderControllerObject pingOrderControllerInfo = actionObject.getPingOrderControllerObject();
			byte[] dataCommand = new byte[19];
			// NE地址
			byte[] neaddress = CoderUtils.intToBytes(actionObject.getNeObject().getNeAddress());
			dataCommand[0] = neaddress[2];
			dataCommand[1] = neaddress[3];
			// 盘类型
			byte[] platetype = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelType());
			dataCommand[2] = platetype[3];
			//盘组号
			byte[] controlPanelGroupId = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelGroupId());
			dataCommand[3] = controlPanelGroupId[3];
			// 盘地址
			byte[] plateaddress = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelAddress());	
			dataCommand[4] = plateaddress[3];
			
			//控制代码
			dataCommand[5] = CoderUtils.intToBytes(254)[3];
			
			//命令类型
			dataCommand[6] = CoderUtils.intToBytes(4)[3];
			
			
			//命令码
			if(pingOrderControllerInfo.getType() == 0){
				dataCommand[7] = CoderUtils.intToBytes(48)[3];
			}else{
				dataCommand[7] = CoderUtils.intToBytes(49)[3];
			}
			dataCommand[8] = CoderUtils.intToBytes(0)[3];
			
			
			dataCommand[9] = CoderUtils.intToBytes(pingOrderControllerInfo.getId())[3];
			dataCommand[10] = CoderUtils.intToBytes(pingOrderControllerInfo.getMepId()/256)[3];
			dataCommand[11] = CoderUtils.intToBytes(pingOrderControllerInfo.getMepId()%256)[3];
			String mac = CoderUtils.transformMac(pingOrderControllerInfo.getFarMac());
			dataCommand[12] = CoderUtils.intToBytes(Integer.parseInt((mac.split("-"))[0]))[3];
			dataCommand[13] = CoderUtils.intToBytes(Integer.parseInt((mac.split("-"))[1]))[3];
			dataCommand[14] = CoderUtils.intToBytes(Integer.parseInt((mac.split("-"))[2]))[3];
			dataCommand[15] = CoderUtils.intToBytes(Integer.parseInt((mac.split("-"))[3]))[3];
			dataCommand[16] = CoderUtils.intToBytes(Integer.parseInt((mac.split("-"))[4]))[3];
			dataCommand[17] = CoderUtils.intToBytes(Integer.parseInt((mac.split("-"))[5]))[3];
			dataCommand[18] = CoderUtils.intToBytes(pingOrderControllerInfo.getPing())[3];
			CoderUtils.print16String(dataCommand);
			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
			
			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 跟新以太网环回
	 */
	public void updateEthLoop(OperationObject operationObject,ActionObject actionObject)throws Exception{
		AnalysisObjectServiceI analysisObjectServiceI = null;
		DriveUtilObject driveUtilObject = null;
		try {
			driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getEthLoopObject());// 赋值对象
			driveUtilObject.setPtnService("CG0103");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			// 临时调用
			analysisObjectServiceI = new AnalysisObjectService();
			byte[] dataCommand = analysisObjectServiceI.AnalysisEthLoopObjectToCommands(actionObject.getNeObject(), actionObject.getEthLoopObject());

			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
			
//			CoderUtils.print16String(dataCommand);
			
			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞
		} catch (Exception e) {
			throw e;
		}finally{
			 analysisObjectServiceI = null;
			 driveUtilObject = null;
		}
	}
	
	/**
	 * 跟新ACL配置管理
	 */
	public void updateAcl(OperationObject operationObject,ActionObject actionObject)throws Exception{
		AnalysisObjectServiceI analysisObjectServiceI = null;
		DriveUtilObject driveUtilObject = null;
		try {
			driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getAclObject());// 赋值对象
			driveUtilObject.setPtnService("CG0103");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			// 临时调用
			analysisObjectServiceI = new AnalysisObjectService();
			byte[] dataCommand = analysisObjectServiceI.AnalysisAclObjectToCommands(actionObject.getNeObject(), actionObject.getAclObject());

			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞
		} catch (Exception e) {
			throw e;
		}finally{
			 analysisObjectServiceI = null;
			 driveUtilObject = null;
		}
	}
	
	/**
	 * 更新mac学习数目限制
	 * @param operationObject
	 * @param actionObject
	 * @throws Exception 
	 */
	public void updateMacLearning(OperationObject operationObject,ActionObject actionObject) throws Exception {
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getMacLearningObjectList());// 赋值对象
			driveUtilObject.setPtnService("CG0103");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			// 临时调用
			AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();
			byte[] dataCommand = analysisObjectServiceI.AnalysisMacLearningToCommands(actionObject.getNeObject(), actionObject.getMacLearningObjectList());
			CoderUtils.print16String(dataCommand);
			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞

		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 更新mac学习数目限制
	 * @param operationObject
	 * @param actionObject
	 * @throws Exception 
	 */
	public void updateBlackWhiteMac(OperationObject operationObject,ActionObject actionObject) throws Exception {
		
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getMacLearningObjectList());// 赋值对象
			driveUtilObject.setPtnService("CG0103");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			// 临时调用
			AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();
			byte[] dataCommand = analysisObjectServiceI.AnalysisBlackWhiteMacToCommands(actionObject.getNeObject(), actionObject.getBlackWhiteMacObjectList());
			CoderUtils.print16String(dataCommand);
			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞

		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 取消软件升级
	 * 
	 */
	public void cancelSoftware(OperationObject operationObject, ActionObject actionObject) throws Exception {
		
		try {
			DriveUtilObject driveUtilObject = new  DriveUtilObject();
			driveUtilObject.setPtnService("UD0102");//赋值业务类型
			driveUtilObject.setDirection(0);//ws向PTN发送
			driveUtilObject.setStates(0);//待发送状态
			driveUtilObject.setSendDate(new Date());//赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId()+"");//操作ID
			driveUtilObject.setOperationObject(operationObject);//赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			
			//ne地址
			byte[] neAddressByte = CoderUtils.intToByte(actionObject.getNeObject().getNeAddress());
			//盘类型1
			byte[] masterCardByte = CoderUtils.intToByte(actionObject.getNeObject().getControlPanelType());
			//盘组号
			byte[] masterCardGroup = CoderUtils.intToByte(actionObject.getNeObject().getControlPanelGroupId());
			//盘地址
			byte[] masterCardAddress = CoderUtils.intToByte(actionObject.getNeObject().getControlPanelAddress());

			byte[] dataCommand = new byte[5];
			dataCommand[0] = neAddressByte[2]; 
			dataCommand[1] = neAddressByte[3]; 
			dataCommand[2] = masterCardByte[masterCardByte.length -1];
			dataCommand[3] = masterCardGroup[masterCardGroup.length -1];
			dataCommand[4] = masterCardAddress[masterCardAddress.length -1];
			
			CoderUtils.print16String(dataCommand);
			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);//解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);//将整体命令分片
			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);//放到代发命令列表中
			Thread.sleep(200);//休眠防止拥塞
			
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * function:创建或者删除丢弃流配置
	 * @param operationObject
	 * @param actionObject
	 * @throws Exception
	 */
	public void updateCreateOrDeleteDiscardFlow(OperationObject operationObject,ActionObject actionObject)throws Exception{
		
		try {
			DriveUtilObject driveUtilObject = new  DriveUtilObject();
			driveUtilObject.setPtnService("CM0101");//赋值业务类型
			driveUtilObject.setDirection(0);//ws向PTN发送
			driveUtilObject.setStates(0);//待发送状态
			driveUtilObject.setSendDate(new Date());//赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId()+"");//操作ID
			driveUtilObject.setOperationObject(operationObject);//赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			
			//ne地址
			byte[] neAddressByte = CoderUtils.intToBytes(actionObject.getNeObject().getNeAddress());
			//盘类型1（基本盘类型）
			byte[] masterCardByte = CoderUtils.intToByte(actionObject.getNeObject().getControlPanelType());
			//盘组号
			byte[] masterCardGroup = CoderUtils.intToByte(actionObject.getNeObject().getControlPanelGroupId());
			//盘地址
			byte[] masterCardAddressByte = CoderUtils.intToByte(actionObject.getNeObject().getControlPanelAddress());
			
			byte[] dataCommand = new byte[9];
			dataCommand[0] = neAddressByte[2];
			dataCommand[1] = neAddressByte[3];
			dataCommand[2] = masterCardByte[masterCardByte.length-1];
			dataCommand[3] = masterCardGroup[masterCardGroup.length-1];
			dataCommand[4] = masterCardAddressByte[masterCardAddressByte.length -1];
			//控制代码
			dataCommand[5] = CoderUtils.intToByte(254)[3];
			//命令类型
			dataCommand[6] = CoderUtils.intToByte(4)[3];
			//指命令
			if(actionObject.getNeObject().getIsCreateDiscardFlow() == 1 ){
				dataCommand[7] = CoderUtils.intToByte(1)[3];
			}else {
				dataCommand[7] = CoderUtils.intToByte(2)[3];
			}
			//指令附加字节
			dataCommand[8] = CoderUtils.intToBytes(0)[3];
			
			CoderUtils.print16String(dataCommand);
			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);//解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);//将整体命令分片
			CoderUtils.print16String(driveUtilObject.getSendCommands());
			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);//放到代发命令列表中
			Thread.sleep(200);//休眠防止拥塞
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * @param operationObject
	 * @param actionObject
	 * @throws Exception
	 */
	public void querySoftwareSummary(OperationObject operationObject,ActionObject actionObject)throws Exception{
		
		try {
			DriveUtilObject driveUtilObject = new  DriveUtilObject();
			driveUtilObject.setPtnService("UD0203");//赋值业务类型
			driveUtilObject.setDirection(0);//ws向PTN发送
			driveUtilObject.setStates(0);//待发送状态
			driveUtilObject.setSendDate(new Date());//赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId()+"");//操作ID
			driveUtilObject.setOperationObject(operationObject);//赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			//ne地址
			byte[] neAddressByte = CoderUtils.intToBytes(actionObject.getNeObject().getNeAddress());
			//盘类型1（基本盘类型）
			byte[] masterCardByte = CoderUtils.intToByte(actionObject.getNeObject().getControlPanelType());
			//盘组号
			byte[] masterCardGroup = CoderUtils.intToByte(actionObject.getNeObject().getControlPanelGroupId());
			//盘地址
			byte[] masterCardAddressByte = CoderUtils.intToByte(actionObject.getNeObject().getControlPanelAddress());
			
			byte[] dataCommand;
			if(actionObject.getBs() != null && actionObject.getBs().length>0){
				dataCommand = new byte[6+actionObject.getBs().length-24];
				dataCommand[5] = CoderUtils.intToByte(2)[3];
				System.arraycopy(actionObject.getBs(),24,dataCommand, 6, actionObject.getBs().length-24);
			}else{
				dataCommand = new byte[6];
				dataCommand[5] = CoderUtils.intToByte(1)[3];
			}
			
			dataCommand[0] = neAddressByte[2];
			dataCommand[1] = neAddressByte[3];
			dataCommand[2] = masterCardByte[masterCardByte.length-1];
			dataCommand[3] = masterCardGroup[masterCardGroup.length-1];
			dataCommand[4] = masterCardAddressByte[masterCardAddressByte.length -1];
			
			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);//解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);//将整体命令分片
			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);//放到代发命令列表中
			
			Thread.sleep(200);//休眠防止拥塞
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * V35接口管理
	 * 
	 * @param OperationObject回调对象
	 * @param actionObject事件对象
	 * @throws Exception
	 * 
	 */
	public void updateV35Port(OperationObject operationObject, ActionObject actionObject) throws Exception {
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getTunnelObjectList());// 赋值对象
			driveUtilObject.setPtnService(PtnDriveCommand.CG0103);// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP

			AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();
			byte[] dataCommand = analysisObjectServiceI.AnalysisV35PortObjectToCommand(actionObject.getNeObject(), actionObject.getV35PortObject());

			super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			super.dividePage(driveUtilObject);// 将整体命令分片
			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠，防止拥塞

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 环网保护倒换
	 * @throws Exception 
	 */
	public void updateLoopProRorate(OperationObject operationObject, ActionObject actionObject) throws Exception {
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getProtectRorateObject());// 赋值对象
			driveUtilObject.setPtnService("CM0101");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			byte[] dataCommand = new byte[12];
			// NE地址
			byte[] neaddress = CoderUtils.intToBytes(actionObject.getNeObject().getNeAddress());
			dataCommand[0] = neaddress[2];
			dataCommand[1] = neaddress[3];
			// 盘类型
			byte[] platetype = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelType());
			dataCommand[2] = platetype[3];
			//盘组号
			byte[] controlPanelGroupId = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelGroupId());
			dataCommand[3] = controlPanelGroupId[3];
			// 盘地址
			byte[] plateaddress = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelAddress());	
			dataCommand[4] = plateaddress[3];
			//控制代码
			dataCommand[5] = CoderUtils.intToBytes(254)[3];
			//命令类型
			dataCommand[6] = CoderUtils.intToBytes(2)[3];
			//指命令
			dataCommand[7] = CoderUtils.intToBytes(4)[3];
			//指令附加字节
			dataCommand[8] = CoderUtils.intToBytes(0)[3];
			//参数1,参数2,参数3
			this.getLoopProRotate(actionObject.getLoopProRotateObject(), dataCommand);
			CoderUtils.print16String(dataCommand);
			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞
		} catch (Exception e) {
			throw e;
		}
	}

	private void getLoopProRotate(LoopProRotateObject loopProRotateObject, byte[] dataCommand) {
		//倒换类型
		dataCommand[9] = CoderUtils.intToBytes(loopProRotateObject.getActionType())[3];
		//环网id
		dataCommand[10] = CoderUtils.intToBytes(loopProRotateObject.getRingId())[3];
		//方向
		dataCommand[11] = CoderUtils.intToBytes(loopProRotateObject.getDirection())[3];
	}

	/**
	 * 智能风扇配置
	 */
	public void updateSmartFan(OperationObject operationObject, ActionObject actionObject) throws Exception {
		DriveUtilObject driveUtilObject = new DriveUtilObject();
		driveUtilObject.setPtnDataObject(actionObject.getSmartFanObjectList());// 赋值对象
		driveUtilObject.setPtnService("CG0103");// 赋值业务类型
		driveUtilObject.setDirection(0);// WS向PTN发送
		driveUtilObject.setStates(0);// 待发送状态
		driveUtilObject.setSendDate(new Date());// 赋值发送时间
		driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
		driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
		driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
		// 临时调用
		AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();
		byte[] dataCommand = analysisObjectServiceI.AnalysisSmartFanToCommand(actionObject.getNeObject(), actionObject.getSmartFanObjectList());
		driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
		driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
		super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
		Thread.sleep(200);// 休眠防止拥塞
	}
	
	/**
	 * 查询SN
	 * @param operationObject
	 * @param actionObject
	 * @param isLocation
	 * @throws Exception
	 */
	public void querySn(OperationObject operationObject, ActionObject actionObject,int isLocation) throws Exception {
		DriveUtilObject driveUtilObject = new DriveUtilObject();
		driveUtilObject.setPtnDataObject(actionObject.getSmartFanObjectList());// 赋值对象
		driveUtilObject.setDirection(0);// WS向PTN发送
		driveUtilObject.setStates(0);// 待发送状态
		driveUtilObject.setSendDate(new Date());// 赋值发送时间
		driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
		driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
		driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
		if(isLocation == 1){
			driveUtilObject.setPtnService("NC0401");// 赋值业务类型
			byte[] dataCommand = {2,0,0};
			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject.setDestinationIP("255.255.255.255");//设置目的IP
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
			super.monitorSendCommandThread_copy1.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
		}else if(isLocation == 2){
			driveUtilObject.setPtnService("JC0501");// 赋值业务类型
			byte[] dataCommand = {0,0};
			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
		}else if(isLocation == 3){
			driveUtilObject.setPtnService("JC0503");// 赋值业务类型
			byte[] dataCommand = {0,0,0,0,0,2};
			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
		}
		Thread.sleep(200);// 休眠防止拥塞
	}
	
	public void setIP(OperationObject operationObject, ActionObject actionObject,int type) throws Exception {
		DriveUtilObject driveUtilObject = new DriveUtilObject();
		driveUtilObject.setPtnDataObject(actionObject.getSmartFanObjectList());// 赋值对象
		driveUtilObject.setDirection(0);// WS向PTN发送
		driveUtilObject.setPtnService("JC0502");// 赋值业务类型
		driveUtilObject.setStates(0);// 待发送状态
		driveUtilObject.setSendDate(new Date());// 赋值发送时间
		driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
		driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
		driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
		if(type == 2){
			byte[] dataCommand = new byte[58+actionObject.getNeObject().getSn().length()];
			setCommand(dataCommand, actionObject.getNeObject(),0);
			CoderUtils.print16String(dataCommand);
			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
		}else if(type == 1){
			driveUtilObject.setPtnService("NC0401");// 赋值业务类型
			driveUtilObject.setDestinationIP("255.255.255.255");//设置目的IP
			byte[] dataCommand = new byte[57+actionObject.getNeObject().getSn().length()];
			setCommand(dataCommand, actionObject.getNeObject(),1);
			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
			super.monitorSendCommandThread_copy1.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
		}
		
		Thread.sleep(200);// 休眠防止拥塞
	}
	
	private void setCommand(byte[] dataCommand,NEObject neObject,int count){
		for (int i = 0; i < dataCommand.length; i++) {
			dataCommand[i] = 0;
		}
		if(count>0){
			dataCommand[0] = 4;
		}
		dataCommand[3-count] = (byte) (dataCommand.length-4-count);//配置数据总长度
		dataCommand[4-count] = 1;//配置数据总包数
		dataCommand[5-count] = 1;//当前数据包序号
		dataCommand[7-count] = 1;//TTL
		dataCommand[8-count] = (byte) 252;//CRC1
		dataCommand[9-count] = 95;//CRC2
		dataCommand[10-count] = 96;//CRC3
		dataCommand[11-count] = 5;//CRC4
		dataCommand[15-count] = 1;//配置网元个数
		dataCommand[16-count] = (byte) neObject.getSn().length();//SN码长度
		for (int i = 0; i < neObject.getSn().length(); i++) {
			char c = neObject.getSn().charAt(i);
			int b = c;
			dataCommand[17-count+i] = (byte) b;
		}
		dataCommand[22-count+neObject.getSn().length()] = (byte) Integer.parseInt(neObject.getNeIP().split("\\.")[2]);//ID1
		dataCommand[23-count+neObject.getSn().length()] = (byte) Integer.parseInt(neObject.getNeIP().split("\\.")[3]);//ID2
		dataCommand[24-count+neObject.getSn().length()] = (byte) 128;//默认域标志
		dataCommand[25-count+neObject.getSn().length()] = 1;//ip数据类型
		dataCommand[27-count+neObject.getSn().length()] = 30;//ip数据长度
		dataCommand[28-count+neObject.getSn().length()] = (byte) Integer.parseInt(neObject.getNeIP().split("\\.")[0]);//ip[1]
		dataCommand[29-count+neObject.getSn().length()] = (byte) Integer.parseInt(neObject.getNeIP().split("\\.")[1]);//ip[2]
		dataCommand[30-count+neObject.getSn().length()] = (byte) Integer.parseInt(neObject.getNeIP().split("\\.")[2]);//ip[3]
		dataCommand[31-count+neObject.getSn().length()] = (byte) Integer.parseInt(neObject.getNeIP().split("\\.")[3]);//ip[4]
		dataCommand[32-count+neObject.getSn().length()] = (byte) 255;//子网掩码1
		dataCommand[33-count+neObject.getSn().length()] = (byte) 255;//子网掩码2
		dataCommand[34-count+neObject.getSn().length()] = (byte) 255;//子网掩码3
		dataCommand[44-count+neObject.getSn().length()] = (byte) 64;//IP标示
		
	}
	
	/**
	 * 
	 * @param operationObject
	 * @param actionObject
	 * @param type 本地或远程
	 * @throws Exception
	 */
	public void setSN(OperationObject operationObject, ActionObject actionObject,int type) throws Exception {
		DriveUtilObject driveUtilObject = new DriveUtilObject();
		driveUtilObject.setPtnDataObject(actionObject.getSmartFanObjectList());// 赋值对象
		driveUtilObject.setDirection(0);// WS向PTN发送
		driveUtilObject.setPtnService("JC0503");// 赋值业务类型
		driveUtilObject.setStates(0);// 待发送状态
		driveUtilObject.setSendDate(new Date());// 赋值发送时间
		driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
		driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
		driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
		if(type == 4){
			byte[] dataCommand = new byte[19];
			for (int i = 0; i < dataCommand.length; i++) {
				dataCommand[i] = 0;
			}
			dataCommand[5] =1;
			dataCommand[6] =12;
			for (int i = 0; i < 12; i++) {
				dataCommand[7+i] = (byte) actionObject.getNeObject().getSn().charAt(i);
			}
			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
		}else if(type == 3){
			driveUtilObject.setPtnService("NC0401");// 赋值业务类型
			driveUtilObject.setDestinationIP("255.255.255.255");//设置目的IP
			byte[] dataCommand = new byte[16];
			for (int i = 0; i < dataCommand.length; i++) {
				dataCommand[i] = 0;
			}
			dataCommand[0] =3;
			dataCommand[3] =12;
			for (int i = 0; i < 12; i++) {
				dataCommand[4+i] = (byte) actionObject.getNeObject().getSn().charAt(i);
			}
			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
			super.monitorSendCommandThread_copy1.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
		}
		
		Thread.sleep(200);// 休眠防止拥塞
	}
	/**
	 * 以太网业务配置块
	 */
	public void updateEthService(OperationObject operationObject, ActionObject actionObject) throws Exception {
		DriveUtilObject driveUtilObject = new DriveUtilObject();
		driveUtilObject.setPtnDataObject(actionObject.getEthServiceObject());// 赋值对象
		driveUtilObject.setPtnService("CG0103");// 赋值业务类型
		driveUtilObject.setDirection(0);// WS向PTN发送
		driveUtilObject.setStates(0);// 待发送状态
		driveUtilObject.setSendDate(new Date());// 赋值发送时间
		driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
		driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
		driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
		// 临时调用
		AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();
		byte[] dataCommand = analysisObjectServiceI.AnalysisEthServiceToCommand(actionObject.getNeObject(), actionObject.getEthServiceObject());
		driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
		driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
		super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
		Thread.sleep(200);// 休眠防止拥塞
	}

	/**
	 * 告警屏蔽功能
	 * 
	 */
	@Override
	public void updateShieldAlarm(OperationObject operationObject,ActionObject actionObject) throws Exception {
		
		DriveUtilObject driveUtilObject = new DriveUtilObject();
		driveUtilObject.setPtnDataObject(actionObject.getEthServiceObject());// 赋值对象
		driveUtilObject.setPtnService("CG0103");// 赋值业务类型
		driveUtilObject.setDirection(0);// WS向PTN发送
		driveUtilObject.setStates(0);// 待发送状态
		driveUtilObject.setSendDate(new Date());// 赋值发送时间
		driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
		driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
		driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
		// 临时调用
		AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();
		byte[] dataCommand = analysisObjectServiceI.AnalysisAlarmShieldObjectToCommand(actionObject.getNeObject(), actionObject.getAlarmShieldObject());
		driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
		driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
		super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
		Thread.sleep(200);// 休眠防止拥塞
	
	}

	@Override
	public void updateL2CP(OperationObject operationObject,ActionObject actionObject) throws Exception {
		
		DriveUtilObject driveUtilObject = new DriveUtilObject();
		driveUtilObject.setPtnDataObject(actionObject.getEthServiceObject());// 赋值对象
		driveUtilObject.setPtnService("CG0103");// 赋值业务类型
		driveUtilObject.setDirection(0);// WS向PTN发送
		driveUtilObject.setStates(0);// 待发送状态
		driveUtilObject.setSendDate(new Date());// 赋值发送时间
		driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
		driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
		driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
		// 临时调用
		AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();
		byte[] dataCommand = analysisObjectServiceI.AnalysisL2cpObjectToCommand(actionObject.getNeObject(),actionObject.getL2CPinfoObject());
		driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
		driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
		super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
		Thread.sleep(200);// 休眠防止拥塞
		
	}

	public void updatePortPri(OperationObject operationObject, ActionObject actionObject) throws Exception {
		DriveUtilObject driveUtilObject = new DriveUtilObject();
		driveUtilObject.setPtnDataObject(actionObject.getEthServiceObject());// 赋值对象
		driveUtilObject.setPtnService("CG0103");// 赋值业务类型
		driveUtilObject.setDirection(0);// WS向PTN发送
		driveUtilObject.setStates(0);// 待发送状态
		driveUtilObject.setSendDate(new Date());// 赋值发送时间
		driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
		driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
		driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
		// 临时调用
		AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();
		byte[] dataCommand = analysisObjectServiceI.AnalysisPortPriToCommand(actionObject.getNeObject(), actionObject.getProtObjectList().get(0));
		driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
		driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
		super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
		Thread.sleep(200);// 休眠防止拥塞
	}
	
	public void updatePortDiscord(OperationObject operationObject, ActionObject actionObject) throws Exception {
		DriveUtilObject driveUtilObject = new DriveUtilObject();
		driveUtilObject.setPtnDataObject(actionObject.getEthServiceObject());// 赋值对象
		driveUtilObject.setPtnService("CG0103");// 赋值业务类型
		driveUtilObject.setDirection(0);// WS向PTN发送
		driveUtilObject.setStates(0);// 待发送状态
		driveUtilObject.setSendDate(new Date());// 赋值发送时间
		driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
		driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
		driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
		// 临时调用
		AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();
		byte[] dataCommand = analysisObjectServiceI.AnalysisPortDiscardToCommand(actionObject.getNeObject(), actionObject.getEthServiceObject());
		driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
		driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
		super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
		Thread.sleep(200);// 休眠防止拥塞
	}
	
	/**
	 * function:创建或者删除丢弃流配置
	 * @param operationObject
	 * @param actionObject
	 * @throws Exception
	 */
	public void updatePortVlan(OperationObject operationObject,ActionObject actionObject)throws Exception{
		
		try {
			DriveUtilObject driveUtilObject = new  DriveUtilObject();
			driveUtilObject.setPtnService("CM0101");//赋值业务类型
			driveUtilObject.setDirection(0);//ws向PTN发送
			driveUtilObject.setStates(0);//待发送状态
			driveUtilObject.setSendDate(new Date());//赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId()+"");//操作ID
			driveUtilObject.setOperationObject(operationObject);//赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			
			//ne地址
			byte[] neAddressByte = CoderUtils.intToBytes(actionObject.getNeObject().getNeAddress());
			//盘类型1（基本盘类型）
			byte[] masterCardByte = CoderUtils.intToByte(actionObject.getNeObject().getControlPanelType());
			//盘组号
			byte[] masterCardGroup = CoderUtils.intToByte(actionObject.getNeObject().getControlPanelGroupId());
			//盘地址
			byte[] masterCardAddressByte = CoderUtils.intToByte(actionObject.getNeObject().getControlPanelAddress());
			
			byte[] dataCommand = new byte[10];
			dataCommand[0] = neAddressByte[2];
			dataCommand[1] = neAddressByte[3];
			dataCommand[2] = masterCardByte[masterCardByte.length-1];
			dataCommand[3] = masterCardGroup[masterCardGroup.length-1];
			dataCommand[4] = masterCardAddressByte[masterCardAddressByte.length -1];
			//控制代码
			dataCommand[5] = CoderUtils.intToByte(254)[3];
			//命令类型
			dataCommand[6] = CoderUtils.intToByte(4)[3];
			//指命令
			dataCommand[7] = CoderUtils.intToByte(50)[3];
			//指令附加字节
			dataCommand[8] = CoderUtils.intToBytes(0)[3];
			dataCommand[9] = CoderUtils.intToBytes(actionObject.getNeObject().getIsCreateDiscardFlow())[3];
			
			CoderUtils.print16String(dataCommand);
			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);//解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);//将整体命令分片
			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);//放到代发命令列表中
			Thread.sleep(200);//休眠防止拥塞
			
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 端口2层属性
	 * @throws Exception 
	 */
	public void updatePort2layer(OperationObject operationObject, ActionObject actionObject) throws Exception {
		DriveUtilObject driveUtilObject = new DriveUtilObject();
		driveUtilObject.setPtnDataObject(actionObject.getEthServiceObject());// 赋值对象
		driveUtilObject.setPtnService("CG0103");// 赋值业务类型
		driveUtilObject.setDirection(0);// WS向PTN发送
		driveUtilObject.setStates(0);// 待发送状态
		driveUtilObject.setSendDate(new Date());// 赋值发送时间
		driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
		driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
		driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
		// 临时调用
		AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();
		byte[] dataCommand = analysisObjectServiceI.AnalysisPort2layerToCommand(actionObject.getNeObject(), actionObject.getPort2LayerObjectList());
		driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
		driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
		super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
		Thread.sleep(200);// 休眠防止拥塞
	}
	
	/**
	 * 升级重启时间
	 * @param operationObject
	 * @param actionObject
	 * @throws Exception
	 */
	public void updateRestartTime(OperationObject operationObject,ActionObject actionObject) throws Exception{
		// TODO Auto-generated method stub
		try {
		DriveUtilObject driveUtilObject = new DriveUtilObject();
		driveUtilObject.setPtnDataObject(actionObject.getProtectRorateObject());// 赋值对象
		driveUtilObject.setPtnService("CM0101");// 赋值业务类型
		driveUtilObject.setDirection(0);// WS向PTN发送
		driveUtilObject.setStates(0);// 待发送状态
		driveUtilObject.setSendDate(new Date());// 赋值发送时间
		driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
		driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
		driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
		byte[] dataCommand = new byte[16];
		// NE地址
		byte[] neaddress = CoderUtils.intToBytes(actionObject.getNeObject().getNeAddress());
		dataCommand[0] = neaddress[2];
		dataCommand[1] = neaddress[3];
		// 盘类型
		byte[] platetype = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelType());
		dataCommand[2] = platetype[3];
		//盘组号
		byte[] controlPanelGroupId = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelGroupId());
		dataCommand[3] = controlPanelGroupId[3];
		// 盘地址
		byte[] plateaddress = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelAddress());	
		dataCommand[4] = plateaddress[3];
		//控制代码
		dataCommand[5] = CoderUtils.intToBytes(254)[3];
		//命令类型
		dataCommand[6] = CoderUtils.intToBytes(6)[3];
		//指命令
		dataCommand[7] = CoderUtils.intToBytes(2)[3];
		//指令附加字节
		dataCommand[8] = CoderUtils.intToBytes(1)[3];
		//参数1,参数2
		
		long l = actionObject.getNeObject().getL();
		Date date = new Date(l);
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		String year = calendar.get(calendar.YEAR) + "";
		
		int year_1 = Integer.parseInt(year)/256;
		int year_2 = Integer.parseInt(year)%256;
		byte[] yearByte_1 = CoderUtils.intToBytes(year_1);
		byte[] yearByte_2 = CoderUtils.intToBytes(year_2);
		byte[] monthByte = CoderUtils.intToBytes(calendar.get(calendar.MONTH) + 1);
		byte[] dayByte = CoderUtils.intToBytes(calendar.get(calendar.DAY_OF_MONTH));
		byte[] hourByte = CoderUtils.intToBytes(calendar.get(calendar.HOUR_OF_DAY));
		byte[] minByte = CoderUtils.intToBytes(calendar.get(calendar.MINUTE));
		byte[] secByte = CoderUtils.intToBytes(calendar.get(calendar.SECOND));
		dataCommand[9] = yearByte_1[yearByte_1.length-1];
		dataCommand[10] = yearByte_2[yearByte_2.length-1];
		dataCommand[11] = monthByte[monthByte.length-1];
		dataCommand[12] = dayByte[dayByte.length-1];
		dataCommand[13] = hourByte[hourByte.length-1];
		dataCommand[14] = minByte[minByte.length-1];
		dataCommand[15] = secByte[secByte.length-1];
		
		driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
		driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
		
		CoderUtils.print16String(dataCommand);
		super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
		
		Thread.sleep(200);// 休眠防止拥塞
	} catch (Exception e) {
		throw e;
	}
	}
	
	/**
	 * 更新Ccc
	 * 
	 * @param operationObject回调对象
	 * @param actionObject事件对象
	 * @throws Exception
	 */
	public void updataCcc(OperationObject operationObject, ActionObject actionObject) throws Exception {
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getElineObjectList());// 赋值对象
			driveUtilObject.setPtnService("CG0103");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			// 临时调用
			AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();
			byte[] dataCommand = analysisObjectServiceI.AnalysisCccToCommand(actionObject.getNeObject(), actionObject.getCccObjectList());

			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片

			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞

		} catch (Exception e) {
			throw e;
		}

	}

	/**
	 * 更新Bfd
	 * 
	 * @param operationObject回调对象
	 * @param actionObject事件对象
	 * @throws Exception
	 */
	public void updataBfd(OperationObject operationObject, ActionObject actionObject) throws Exception {
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getElineObjectList());// 赋值对象
			driveUtilObject.setPtnService("CG0103");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			// 临时调用
			AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();
			byte[] dataCommand = analysisObjectServiceI.AnalysisBfdToCommand(actionObject.getNeObject(), actionObject.getBfdObjectList());

			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片

			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞

		} catch (Exception e) {
			throw e;
		}

	}

	/**
	 * arp配置
	 * @param operationObject
	 * @param actionObject
	 * @throws Exception 
	 */
	public void updateARP(OperationObject operationObject, ActionObject actionObject) throws Exception {
		DriveUtilObject driveUtilObject = new DriveUtilObject();
		driveUtilObject.setPtnDataObject(actionObject.getElineObjectList());// 赋值对象
		driveUtilObject.setPtnService("CG0103");// 赋值业务类型
		driveUtilObject.setDirection(0);// WS向PTN发送
		driveUtilObject.setStates(0);// 待发送状态
		driveUtilObject.setSendDate(new Date());// 赋值发送时间
		driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
		driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
		driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
		// 临时调用
		AnalysisObjectServiceI analysisObjectServiceI = new AnalysisObjectService();
		byte[] dataCommand = analysisObjectServiceI.AnalysisARPToCommand(actionObject.getNeObject(), actionObject.getArpObjectList());

		driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
		driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片

		super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
		Thread.sleep(200);// 休眠防止拥塞
	}
	
	/**
	 * route配置
	 * @param operationObject
	 * @param actionObject
	 * @throws Exception 
	 */
	public void updateRoute(OperationObject operationObject, ActionObject actionObject) throws Exception {
		DriveUtilObject driveUtilObject = new DriveUtilObject();
		driveUtilObject.setPtnDataObject(actionObject.getElineObjectList());// 赋值对象
		driveUtilObject.setPtnService("NC0401");// 赋值业务类型
		driveUtilObject.setDirection(0);// WS向PTN发送
		driveUtilObject.setStates(0);// 待发送状态
		driveUtilObject.setSendDate(new Date());// 赋值发送时间
		driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
		driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
		driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
		// 临时调用
		byte[] dataCommand = {1};

		driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
		driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片

		super.monitorSendCommandThread_copy1.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
		Thread.sleep(200);// 休眠防止拥塞
	}
	
	public void setOSPF(OperationObject operationObject, ActionObject actionObject) throws Exception {
		DriveUtilObject driveUtilObject = new DriveUtilObject();
		driveUtilObject.setPtnDataObject(actionObject.getSmartFanObjectList());// 赋值对象
		driveUtilObject.setDirection(0);// WS向PTN发送
		driveUtilObject.setPtnService("NC0401");// 赋值业务类型
		driveUtilObject.setStates(0);// 待发送状态
		driveUtilObject.setSendDate(new Date());// 赋值发送时间
		driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
		driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
		
		driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
		
		byte[] ospfcommand = getOspfcommand(actionObject.getOspFinfoWhObejects());
		int index = 0;
		if(actionObject.getNeObject().getManageIP().equals("255.255.255.255")){
			index = 0;
		}else{
			driveUtilObject.setPtnService("JC0502");// 赋值业务类型
			index = 1;
		}
		byte[] dataCommand = new byte[12+index+actionObject.getNeObject().getSn().length()+ospfcommand.length];
		sethead(dataCommand, actionObject.getNeObject(), ospfcommand, index);
		CoderUtils.print16String(dataCommand);
		driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
		driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
		if(index == 0){
			super.monitorSendCommandThread_copy1.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
		}else{
			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
		}
		
		
		Thread.sleep(200);// 休眠防止拥塞
	}
	
	private void sethead(byte[] dataCommand,NEObject neObject,byte[] ospfcommand,int index){
		if(index ==0){
			dataCommand[0] = 4;
		}
		dataCommand[1+index] = CoderUtils.intToBytes(dataCommand.length-3-index)[2];
		dataCommand[2+index] = CoderUtils.intToBytes(dataCommand.length-3-index)[3];
		dataCommand[3+index] = (byte) neObject.getSn().length();
		for (int i = 0; i < 12; i++) {
			dataCommand[4+index+i] = (byte) neObject.getSn().charAt(i);
		}
		dataCommand[8+index+neObject.getSn().length()] = 0;
		dataCommand[9+index+neObject.getSn().length()] = 0;
		dataCommand[10+index+neObject.getSn().length()] = 2;
		dataCommand[11+index+neObject.getSn().length()] = (byte) 0x80;
		for (int i = 0; i < ospfcommand.length; i++) {
			dataCommand[12+index+neObject.getSn().length()+i] = ospfcommand[i];
		}
	}
	
 	private byte[] getOspfcommand(List<OSPFinfoWhObeject> OSPFinfoWhObejectS){
		List<byte[]> portcommands = new ArrayList<byte[]>();
		List<byte[]> fieldcommands = new ArrayList<byte[]>();
		for (int i = 0; i < OSPFinfoWhObejectS.size(); i++) {
			if(OSPFinfoWhObejectS.get(i).getOspfType() == 11){
				byte[] command = new byte[12];
				command[0] = (byte) Integer.parseInt(OSPFinfoWhObejectS.get(i).getIp().split("\\.")[0]);
				command[1] = (byte) Integer.parseInt(OSPFinfoWhObejectS.get(i).getIp().split("\\.")[1]);
				command[2] = (byte) Integer.parseInt(OSPFinfoWhObejectS.get(i).getIp().split("\\.")[2]);
				command[3] = (byte) Integer.parseInt(OSPFinfoWhObejectS.get(i).getIp().split("\\.")[3]);
				command[5] = (byte) Integer.parseInt(OSPFinfoWhObejectS.get(i).getMask());
				byte[] portName = CoderUtils.intToByte(OSPFinfoWhObejectS.get(i).getPortType());
				command[6] = portName[2];
				command[7] = portName[3];
				byte[] vlan = CoderUtils.intToByte(OSPFinfoWhObejectS.get(i).getVlanValues());
				command[8] = vlan[2];
				command[9] = vlan[3];
				command[10] = (byte) OSPFinfoWhObejectS.get(i).getFolw();
				command[11] = (byte) OSPFinfoWhObejectS.get(i).getPortModel();
				portcommands.add(command);
			}else if(OSPFinfoWhObejectS.get(i).getOspfType() == 23){
				byte[] command = new byte[8];
				command[0] = (byte) Integer.parseInt(OSPFinfoWhObejectS.get(i).getIp().split("\\.")[0]);
				command[1] = (byte) Integer.parseInt(OSPFinfoWhObejectS.get(i).getIp().split("\\.")[1]);
				command[2] = (byte) Integer.parseInt(OSPFinfoWhObejectS.get(i).getIp().split("\\.")[2]);
				command[3] = (byte) Integer.parseInt(OSPFinfoWhObejectS.get(i).getIp().split("\\.")[3]);
				byte[] portName = CoderUtils.intToByte(OSPFinfoWhObejectS.get(i).getPortType());
				command[4] = portName[2];
				command[5] = portName[3];
				byte[] enable = CoderUtils.intToByte(OSPFinfoWhObejectS.get(i).getEnable());
				command[6] = enable[2];
				command[7] = enable[3];
				fieldcommands.add(command);
			}
		}
		byte[] commands = new byte[8+portcommands.size()*12+fieldcommands.size()*8];
		commands[0] = 0;
		commands[1] = 13;
		commands[2] = 0;
		commands[3] = (byte) portcommands.size();
		for (int i = 0; i < portcommands.size(); i++) {
			System.arraycopy(portcommands.get(i), 0, commands, 4+i*12, 12);
		}
		commands[4+portcommands.size()*12] = 0;
		commands[4+portcommands.size()*12+1] = 23;
		commands[4+portcommands.size()*12+2] = 0;
		commands[4+portcommands.size()*12+3] = (byte) fieldcommands.size();
		for (int i = 0; i < fieldcommands.size(); i++) {
			System.arraycopy(fieldcommands.get(i), 0, commands, 8+portcommands.size()*12+i*8, 8);
		}
		return commands;
	}
 	
	public void ospfSyn(OperationObject operationObject, ActionObject actionObject) throws Exception {
		DriveUtilObject driveUtilObject = new DriveUtilObject();
		driveUtilObject.setPtnDataObject(actionObject.getSmartFanObjectList());// 赋值对象
		driveUtilObject.setDirection(0);// WS向PTN发送
		driveUtilObject.setPtnService("NC0401");// 赋值业务类型
		driveUtilObject.setStates(0);// 待发送状态
		driveUtilObject.setSendDate(new Date());// 赋值发送时间
		driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
		driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
		
		driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
		if(actionObject.getNeObject().getManageIP().equals("255.255.255.255")){
			byte[] dataCommand = {9}; 
			CoderUtils.print16String(dataCommand);
			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
			super.monitorSendCommandThread_copy1.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
		}else{
			driveUtilObject.setPtnService("JC0504");// 赋值业务类型
			byte[] dataCommand = new byte[5+actionObject.getNeObject().getSn().length()]; 
			dataCommand[0] = 0;
			dataCommand[1] = 0;
			dataCommand[2] = 0;
			dataCommand[3] = (byte) (1+actionObject.getNeObject().getSn().length());
			dataCommand[4] = (byte) (actionObject.getNeObject().getSn().length());
			byte[] sns = actionObject.getNeObject().getSn().getBytes();
			System.arraycopy(sns, 0, dataCommand, 5, sns.length);
			CoderUtils.print16String(dataCommand);
			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
		}
		
		Thread.sleep(200);// 休眠防止拥塞
	}

	public void macVlan(OperationObject operationObject, ActionObject actionObject) throws Exception{
		try {
			DriveUtilObject driveUtilObject = new DriveUtilObject();
			driveUtilObject.setPtnDataObject(actionObject.getProtectRorateObject());// 赋值对象
			driveUtilObject.setPtnService("CM0101");// 赋值业务类型
			driveUtilObject.setDirection(0);// WS向PTN发送
			driveUtilObject.setStates(0);// 待发送状态
			driveUtilObject.setSendDate(new Date());// 赋值发送时间
			driveUtilObject.setOperID(actionObject.getActionId() + "");// 操作ID
			driveUtilObject.setOperationObject(operationObject);// 赋值回调函数
			driveUtilObject.setDestinationIP(actionObject.getNeObject().getManageIP());//设置目的IP
			byte[] dataCommand = new byte[17];
			// NE地址
			byte[] neaddress = CoderUtils.intToBytes(actionObject.getNeObject().getNeAddress());
			dataCommand[0] = neaddress[2];
			dataCommand[1] = neaddress[3];
			// 盘类型
			byte[] platetype = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelType());
			dataCommand[2] = platetype[3];
			//盘组号
			byte[] controlPanelGroupId = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelGroupId());
			dataCommand[3] = controlPanelGroupId[3];
			// 盘地址
			byte[] plateaddress = CoderUtils.intToBytes(actionObject.getNeObject().getControlPanelAddress());	
			dataCommand[4] = plateaddress[3];
			//控制代码
			dataCommand[5] = CoderUtils.intToBytes(254)[3];
			//命令类型
			dataCommand[6] = CoderUtils.intToBytes(0x20)[3];
			//指命令			
			dataCommand[7] = CoderUtils.intToBytes(9)[3];
			//指令附加字节
			dataCommand[8] = CoderUtils.intToBytes(0)[3];
			dataCommand[9] = CoderUtils.intToBytes(0x43)[3];
			
			dataCommand[10] = CoderUtils.intToBytes(Integer.parseInt(actionObject.getNeObject().getVlanMac().get(0)))[2];
			dataCommand[11] = CoderUtils.intToBytes(Integer.parseInt(actionObject.getNeObject().getVlanMac().get(0)))[3];
			
			dataCommand[12] = CoderUtils.intToBytes(Integer.parseInt(actionObject.getNeObject().getVlanMac().get(1)))[2];
			dataCommand[13] = CoderUtils.intToBytes(Integer.parseInt(actionObject.getNeObject().getVlanMac().get(1)))[3];
			
			dataCommand[14] = CoderUtils.intToBytes(Integer.parseInt(actionObject.getNeObject().getVlanMac().get(2)))[3];
			dataCommand[15] = CoderUtils.intToBytes(Integer.parseInt(actionObject.getNeObject().getVlanMac().get(3)))[3];
			dataCommand[16] = CoderUtils.intToBytes(Integer.parseInt(actionObject.getNeObject().getVlanMac().get(4)))[3];
			
			CoderUtils.print16String(dataCommand);
			driveUtilObject = super.analysisData(dataCommand, driveUtilObject,"Request-PDU",0);// 解析对象为命令
			driveUtilObject = super.dividePage(driveUtilObject);// 将整体命令分片
			
			super.monitorSendCommandThread.addDriveUtilObject(driveUtilObject);// 放到代发命令列表中
			Thread.sleep(200);// 休眠防止拥塞
		} catch (Exception e) {
			throw e;
		}
		
	}

}