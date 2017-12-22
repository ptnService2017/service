﻿package com.nms.drive.analysis;

import java.util.ArrayList;
import java.util.List;

import com.nms.drive.analysis.datablock.AnalysisARPTable;
import com.nms.drive.analysis.datablock.AnalysisAclTable;
import com.nms.drive.analysis.datablock.AnalysisAlarmReportTable;
import com.nms.drive.analysis.datablock.AnalysisAlarmShieldTable;
import com.nms.drive.analysis.datablock.AnalysisAlarmTable;
import com.nms.drive.analysis.datablock.AnalysisAllBusinessTable;
import com.nms.drive.analysis.datablock.AnalysisBfdTable;
import com.nms.drive.analysis.datablock.AnalysisBlackWhiteMacTable;
import com.nms.drive.analysis.datablock.AnalysisCccTable;
import com.nms.drive.analysis.datablock.AnalysisCircleSiteConnectTable;
import com.nms.drive.analysis.datablock.AnalysisClockTable;
import com.nms.drive.analysis.datablock.AnalysisE1Table;
import com.nms.drive.analysis.datablock.AnalysisELineTable;
import com.nms.drive.analysis.datablock.AnalysisETHLinkTable;
import com.nms.drive.analysis.datablock.AnalysisETHOAMTable;
import com.nms.drive.analysis.datablock.AnalysisETHOamConfig;
import com.nms.drive.analysis.datablock.AnalysisETreeTable;
import com.nms.drive.analysis.datablock.AnalysisEXPToPHBTable;
import com.nms.drive.analysis.datablock.AnalysisEthLoopTable;
import com.nms.drive.analysis.datablock.AnalysisEthServciceTable;
import com.nms.drive.analysis.datablock.AnalysisGlobalTable;
import com.nms.drive.analysis.datablock.AnalysisGroupSpread;
import com.nms.drive.analysis.datablock.AnalysisIGMPSNOOPINGTable;
import com.nms.drive.analysis.datablock.AnalysisL2CPServciceTable;
import com.nms.drive.analysis.datablock.AnalysisLSPProtectionTable;
import com.nms.drive.analysis.datablock.AnalysisMSPWTable;
import com.nms.drive.analysis.datablock.AnalysisMacLearningTable;
import com.nms.drive.analysis.datablock.AnalysisMacTable;
import com.nms.drive.analysis.datablock.AnalysisManagementTable;
import com.nms.drive.analysis.datablock.AnalysisNeObjectSNTable;
import com.nms.drive.analysis.datablock.AnalysisOamLinkStatusTable;
import com.nms.drive.analysis.datablock.AnalysisOamMepInfoObjectTable;
import com.nms.drive.analysis.datablock.AnalysisPHBToEXPTable;
import com.nms.drive.analysis.datablock.AnalysisPWTable;
import com.nms.drive.analysis.datablock.AnalysisPerformanceTable;
import com.nms.drive.analysis.datablock.AnalysisPmLimiteTable;
import com.nms.drive.analysis.datablock.AnalysisPort2LayerAttrTable;
import com.nms.drive.analysis.datablock.AnalysisPortConfig;
import com.nms.drive.analysis.datablock.AnalysisPortDiscardTable;
import com.nms.drive.analysis.datablock.AnalysisPortLAGTable;
import com.nms.drive.analysis.datablock.AnalysisPortPriTable;
import com.nms.drive.analysis.datablock.AnalysisPortSeniorConfigTable;
import com.nms.drive.analysis.datablock.AnalysisPwManageTable;
import com.nms.drive.analysis.datablock.AnalysisQinQTable;
import com.nms.drive.analysis.datablock.AnalysisResponsePanTable;
import com.nms.drive.analysis.datablock.AnalysisResponseTable;
import com.nms.drive.analysis.datablock.AnalysisRoundProtectionTable;
import com.nms.drive.analysis.datablock.AnalysisSecondMacTable;
import com.nms.drive.analysis.datablock.AnalysisSingleCardBasicInformation;
import com.nms.drive.analysis.datablock.AnalysisSinglePanAutoTable;
import com.nms.drive.analysis.datablock.AnalysisSiteAttributeTable;
import com.nms.drive.analysis.datablock.AnalysisSiteConnectTable;
import com.nms.drive.analysis.datablock.AnalysisSiteStatusTable;
import com.nms.drive.analysis.datablock.AnalysisSmartFanTable;
import com.nms.drive.analysis.datablock.AnalysisSoftwareUpdateTable;
import com.nms.drive.analysis.datablock.AnalysisStaticUnicast;
import com.nms.drive.analysis.datablock.AnalysisTMCOAMBugControlTable;
import com.nms.drive.analysis.datablock.AnalysisTMCTunnelProtectTable;
import com.nms.drive.analysis.datablock.AnalysisTMPOAMBugControlTable;
import com.nms.drive.analysis.datablock.AnalysisTMSOAMBugControlTable;
import com.nms.drive.analysis.datablock.AnalysisTMSOAMTable;
import com.nms.drive.analysis.datablock.AnalysisTUNNELTable;
import com.nms.drive.analysis.datablock.AnalysisTimeSyncTable;
import com.nms.drive.analysis.datablock.AnalysisTimeSynchronizeTable;
import com.nms.drive.analysis.datablock.AnalysisUpgradeManageObject;
import com.nms.drive.analysis.datablock.AnalysisV35PortTable;
import com.nms.drive.analysis.datablock.status.AnalysisARPStatus;
import com.nms.drive.analysis.datablock.status.AnalysisAllOamStatus;
import com.nms.drive.analysis.datablock.status.AnalysisBfdStatus;
import com.nms.drive.analysis.datablock.status.AnalysisE1LegStatus;
import com.nms.drive.analysis.datablock.status.AnalysisEthOAMPingQueryTable;
import com.nms.drive.analysis.datablock.status.AnalysisEthOAMTraceQueryTable;
import com.nms.drive.analysis.datablock.status.AnalysisInsertLinkOamTable;
import com.nms.drive.analysis.datablock.status.AnalysisLagStatus;
import com.nms.drive.analysis.datablock.status.AnalysisLspProetctStatus;
import com.nms.drive.analysis.datablock.status.AnalysisPortStatus;
import com.nms.drive.analysis.datablock.status.AnalysisPtpBasicStatus;
import com.nms.drive.analysis.datablock.status.AnalysisPtpPortStatus;
import com.nms.drive.analysis.datablock.status.AnalysisPwProtectStatus;
import com.nms.drive.analysis.datablock.status.AnalysisPwStatus;
import com.nms.drive.analysis.datablock.status.AnalysisTunnelStatus;
import com.nms.drive.analysis.datablock.status.AnalysisVPWSStatus;
import com.nms.drive.analysis.datablock.status.AnalysisWrappingStatus;
import com.nms.drive.service.bean.ARPObject;
import com.nms.drive.service.bean.AclObject;
import com.nms.drive.service.bean.AlarmObject;
import com.nms.drive.service.bean.AlarmShieldObject;
import com.nms.drive.service.bean.BfdObject;
import com.nms.drive.service.bean.BlackWhiteMacObject;
import com.nms.drive.service.bean.CccObject;
import com.nms.drive.service.bean.ClockObject;
import com.nms.drive.service.bean.E1Object;
import com.nms.drive.service.bean.ELanObject;
import com.nms.drive.service.bean.ELineObject;
import com.nms.drive.service.bean.ETHLinkOAMObject;
import com.nms.drive.service.bean.ETHOAM;
import com.nms.drive.service.bean.ETHOAMAllObject;
import com.nms.drive.service.bean.ETHOAMObject;
import com.nms.drive.service.bean.ETreeObject;
import com.nms.drive.service.bean.EXPToPHBObject;
import com.nms.drive.service.bean.EthLoopObject;
import com.nms.drive.service.bean.EthServiceInfoObject;
import com.nms.drive.service.bean.EthServiceObject;
import com.nms.drive.service.bean.GlobalObject;
import com.nms.drive.service.bean.GroupSpreadObject;
import com.nms.drive.service.bean.IGMPSNOOPINGObject;
import com.nms.drive.service.bean.L2CPinfoObject;
import com.nms.drive.service.bean.MacLearningObject;
import com.nms.drive.service.bean.MacManageObject;
import com.nms.drive.service.bean.ManagementObject;
import com.nms.drive.service.bean.MsPwObject;
import com.nms.drive.service.bean.NEObject;
import com.nms.drive.service.bean.PHBToEXPObject;
import com.nms.drive.service.bean.PerformanceObject;
import com.nms.drive.service.bean.PmValueLimiteObject;
import com.nms.drive.service.bean.Port2LayerObject;
import com.nms.drive.service.bean.PortLAGObject;
import com.nms.drive.service.bean.PortObject;
import com.nms.drive.service.bean.PortSeniorConfig;
import com.nms.drive.service.bean.PwObject;
import com.nms.drive.service.bean.PwQueueAndBufferManage;
import com.nms.drive.service.bean.QinQObject;
import com.nms.drive.service.bean.ResponseObject;
import com.nms.drive.service.bean.ResponsePan;
import com.nms.drive.service.bean.RoundProtectionObject;
import com.nms.drive.service.bean.SecondMacStudyObject;
import com.nms.drive.service.bean.SingleObject;
import com.nms.drive.service.bean.SinglePan;
import com.nms.drive.service.bean.SmartFanObject;
import com.nms.drive.service.bean.SoftwareUpdate;
import com.nms.drive.service.bean.StaticUnicastObject;
import com.nms.drive.service.bean.TMCOAMBugControlObject;
import com.nms.drive.service.bean.TMCTunnelProtectObject;
import com.nms.drive.service.bean.TMPOAMBugControlObject;
import com.nms.drive.service.bean.TMSOAMBugControlObject;
import com.nms.drive.service.bean.TMSOAMObject;
import com.nms.drive.service.bean.TimeSyncObject;
import com.nms.drive.service.bean.TimeSynchronizeObject;
import com.nms.drive.service.bean.TunnelObject;
import com.nms.drive.service.bean.TunnelProtection;
import com.nms.drive.service.bean.UpgradeManageObject;
import com.nms.drive.service.bean.V35PortObject;
import com.nms.drive.service.bean.status.BfdStatusObject;
import com.nms.drive.service.bean.status.E1LegStatusObject;
import com.nms.drive.service.bean.status.InsertLinkOamObject;
import com.nms.drive.service.bean.status.LagStatusObject;
import com.nms.drive.service.bean.status.OamLinkStatusInfoObject;
import com.nms.drive.service.bean.status.OamMepInfoObject;
import com.nms.drive.service.bean.status.OamPingFrameObject;
import com.nms.drive.service.bean.status.OamTraceHopsObject;
import com.nms.drive.service.bean.status.PortStatusObject;
import com.nms.drive.service.bean.status.ProtectStatusObject;
import com.nms.drive.service.bean.status.PtpBasicStatusObject;
import com.nms.drive.service.bean.status.PtpPortStatusObject;
import com.nms.drive.service.bean.status.PwProtectStatusObject;
import com.nms.drive.service.bean.status.PwStatusObject;
import com.nms.drive.service.bean.status.TunnelStatusObject;
import com.nms.drive.service.bean.status.VpwsStatusObject;
import com.nms.drive.service.bean.status.WrappingStatusObject;
import com.nms.drive.service.impl.CoderUtils;
import com.nms.service.bean.ActionObject;
public class AnalysisObjectService implements AnalysisObjectServiceI {

	/**
	 * 解析tunnelObject生成带tunnelObject属性的byte[]
	 * 
	 * @param neObject网元信息
	 * @param tunnelObject隧道对象F
	 * @return 数据体命令
	 * 
	 * @throws Exception
	 */
	public byte[] AnalysisTunnalToCommand(NEObject neObject, List<TunnelObject> tunnelObjectList) throws Exception {
		byte[] tunnalToCommand = new byte[0];

		AnalysisTUNNELTable analysisTUNNELTable = new AnalysisTUNNELTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/TUNNEL表(06H).xml";
		byte[] dataCommand = analysisTUNNELTable.analysisObjectToCommand(tunnelObjectList, configXml);// tunnelObject对象数据

		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);// 获得盘头信息
		byte[] dataBlock = dataBlockHead(6, dataCommand.length, 0);// 获得配置块头信息

		tunnalToCommand = CoderUtils.arraycopy(tunnalToCommand, neHand);
		tunnalToCommand = CoderUtils.arraycopy(tunnalToCommand, controlPanel);
		tunnalToCommand = CoderUtils.arraycopy(tunnalToCommand, dataBlock);
		tunnalToCommand = CoderUtils.arraycopy(tunnalToCommand, dataCommand);

		return tunnalToCommand;
	}

	/**
	 * 解析命令生成Tunnel对象
	 * 
	 * @param dataCommand数据体命令
	 * @return tunnelObject隧道对象
	 * @throws Exception
	 */
	public List<TunnelObject> AnalysisCommandToTunnal(byte[] dataCommand) throws Exception {
		AnalysisTUNNELTable analysisTUNNELTable = new AnalysisTUNNELTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/TUNNEL表(06H).xml";
		List<TunnelObject> tunnelObjectList = analysisTUNNELTable.analysisCommandToObject(dataCommand, configXml);
		return tunnelObjectList;
	}

	/**
	 * 解析ELine生成命令
	 * 
	 * @param neObject网元信息
	 * @param ELineObject隧道对象
	 * @return 数据体命令
	 * 
	 * @throws Exception
	 */
	public byte[] AnalysisElineToCommand(NEObject neObject, List<ELineObject> elineObjectList) throws Exception {
		byte[] elineToCommand = new byte[0];

		AnalysisELineTable analysisELINETable = new AnalysisELineTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/VPWS业务(07H).xml";
		byte[] dataCommand = analysisELINETable.analysisObjectToCommand(elineObjectList, configXml);
		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(7, dataCommand.length, 0);
		elineToCommand = CoderUtils.arraycopy(elineToCommand, neHand);
		elineToCommand = CoderUtils.arraycopy(elineToCommand, controlPanel);
		elineToCommand = CoderUtils.arraycopy(elineToCommand, dataBlock);
		elineToCommand = CoderUtils.arraycopy(elineToCommand, dataCommand);

		return elineToCommand;
	}

	/**
	 * 解析命令生成ELine对象
	 * 
	 * @param dataCommand数据体命令
	 * @return ELineObject隧道对象
	 * @throws Exception
	 */
	public List<ELineObject> AnalysisCommandToELine(byte[] dataCommand) throws Exception {
		AnalysisELineTable analysisELINETable = new AnalysisELineTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/VPWS业务(07H).xml";
		List<ELineObject> elineObjectList = analysisELINETable.analysisCommandToObject(dataCommand, configXml);
		return elineObjectList;
	}

	/**
	 * 解析Pw生成命令
	 * 
	 * @param neObject网元信息
	 * @param PwObject隧道对象
	 * @return 数据体命令
	 * @throws Exception
	 * @throws Exception
	 */
	public byte[] AnalysisPwToCommand(NEObject neObject, List<PwObject> pwObjectList) throws Exception {
		byte[] pwToCommand = new byte[0];

		AnalysisPWTable analysisPWTable = new AnalysisPWTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/PW配置块(13H).xml";
		byte[] dataCommand = analysisPWTable.analysisObjectToCommand(pwObjectList, configXml);

		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(19, dataCommand.length, 0);

		pwToCommand = CoderUtils.arraycopy(pwToCommand, neHand);
		pwToCommand = CoderUtils.arraycopy(pwToCommand, controlPanel);
		pwToCommand = CoderUtils.arraycopy(pwToCommand, dataBlock);
		pwToCommand = CoderUtils.arraycopy(pwToCommand, dataCommand);

		return pwToCommand;
	}

	/**
	 * 解析命令生成Pw对象
	 * 
	 * @param dataCommand数据体命令
	 * @return PwObject隧道对象
	 * @throws Exception
	 */
	public List<PwObject> AnalysisCommandToPw(byte[] dataCommand) throws Exception {
		AnalysisPWTable analysisPWTable = new AnalysisPWTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/PW配置块(13H).xml";
		List<PwObject> pwObjectList = analysisPWTable.analysisCommandToObject(dataCommand, configXml);
		return pwObjectList;
	}

	/**
	 * 解析ETree生成命令
	 */
	public byte[] AnalysisETreeToCommand(NEObject neObject, List<ETreeObject> treeObjectList) throws Exception {
		byte[] eTreeCommand = new byte[0];

		AnalysisETreeTable analysisETreeTable = new AnalysisETreeTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/VPLS-VS(08H).xml";
		byte[] dataCommand = analysisETreeTable.analysisETreeToCommand(treeObjectList, configXml);

		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(8, dataCommand.length, 0);

		eTreeCommand = CoderUtils.arraycopy(eTreeCommand, neHand);
		eTreeCommand = CoderUtils.arraycopy(eTreeCommand, controlPanel);
		eTreeCommand = CoderUtils.arraycopy(eTreeCommand, dataBlock);
		eTreeCommand = CoderUtils.arraycopy(eTreeCommand, dataCommand);
		return eTreeCommand;
	}

	/**
	 * 解析命令生成ETree对象
	 */
	public List<ETreeObject> analysisCommandToETree(byte[] commands) throws Exception {
		AnalysisETreeTable analysisETreeTable = new AnalysisETreeTable();
		String configXMl = "com/nms/drive/analysis/xmltool/file/VPLS-VS(08H).xml";
		List<ETreeObject> eTreeObjListList = analysisETreeTable.analysisCommandsToETreeObject(commands, configXMl);

		return eTreeObjListList;
	}

	/**
	 * 解析ELan生成命令
	 */
	public byte[] AnalysisELanToCommand(NEObject neObject, List<ELanObject> elanList) throws Exception {
		byte[] eTreeCommand = new byte[0];

		AnalysisETreeTable analysisETreeTable = new AnalysisETreeTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/VPLS-VS(08H).xml";
		List<ETreeObject> eTreeObjectList = new ArrayList<ETreeObject>();
		for (ETreeObject eTreeObject : elanList) {
			eTreeObjectList.add(eTreeObject);
		}
		byte[] dataCommand = analysisETreeTable.analysisETreeToCommand(eTreeObjectList, configXml);

		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(8, dataCommand.length, 0);

		eTreeCommand = CoderUtils.arraycopy(eTreeCommand, neHand);
		eTreeCommand = CoderUtils.arraycopy(eTreeCommand, controlPanel);
		eTreeCommand = CoderUtils.arraycopy(eTreeCommand, dataBlock);
		eTreeCommand = CoderUtils.arraycopy(eTreeCommand, dataCommand);
		return eTreeCommand;
	}

	/**
	 * 解析命令生成ETree对象
	 */
	public List<ETreeObject> analysisCommandToELan(byte[] commands) throws Exception {
		AnalysisETreeTable analysisETreeTable = new AnalysisETreeTable();
		String configXMl = "com/nms/drive/analysis/xmltool/file/VPLS-VS(08H).xml";
		List<ETreeObject> eTreeObjListList = analysisETreeTable.analysisCommandsToETreeObject(commands, configXMl);

		return eTreeObjListList;
	}

	/**
	 * 解析LSP保护 对象 生成命令
	 */
	public byte[] AnalysisLSPProtectionToCommand(NEObject neObject, List<TunnelProtection> tunnelProtectionList) throws Exception {
		byte[] lspProtectionCommand = new byte[0];

		AnalysisLSPProtectionTable analysisLSPProtectionTable = new AnalysisLSPProtectionTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/LSP保护(05H).xml";
		byte[] dataCommand = analysisLSPProtectionTable.analysisObjectToCommand(tunnelProtectionList, configXml);

		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(5, dataCommand.length, 0);

		lspProtectionCommand = CoderUtils.arraycopy(lspProtectionCommand, neHand);
		lspProtectionCommand = CoderUtils.arraycopy(lspProtectionCommand, controlPanel);
		lspProtectionCommand = CoderUtils.arraycopy(lspProtectionCommand, dataBlock);
		lspProtectionCommand = CoderUtils.arraycopy(lspProtectionCommand, dataCommand);

		return lspProtectionCommand;
	}

	/**
	 * 解析命令生成LSP保护对象
	 */
	public List<TunnelProtection> analysisCommandToLSPProtection(byte[] commands) throws Exception {
		AnalysisLSPProtectionTable analysisLSPProtectionTable = new AnalysisLSPProtectionTable();
		String configXMl = "com/nms/drive/analysis/xmltool/file/LSP保护(05H).xml";
		List<TunnelProtection> lspProtectionObject = analysisLSPProtectionTable.analysisCommandToObject(commands, configXMl);
		return lspProtectionObject;
	}

	/**
	 * 解析ETHOAM生成命令
	 */
	public byte[] AnalysisETHOAMToCommand(NEObject neObject, ETHOAM ethOAM) throws Exception {
		byte[] ethOAMCommand = new byte[0];

		AnalysisETHOAMTable analysisETHOAMTable = new AnalysisETHOAMTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/以太网OAM配置(0DH).xml";
		byte[] dataCommand = analysisETHOAMTable.analysisETHOAMInfoToCommand(ethOAM, configXml);

		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(13, dataCommand.length, 0);

		ethOAMCommand = CoderUtils.arraycopy(ethOAMCommand, neHand);
		ethOAMCommand = CoderUtils.arraycopy(ethOAMCommand, controlPanel);
		ethOAMCommand = CoderUtils.arraycopy(ethOAMCommand, dataBlock);
		ethOAMCommand = CoderUtils.arraycopy(ethOAMCommand, dataCommand);

		return ethOAMCommand;
	}

	/**
	 * 解析命令生成ETHOAM对象
	 */
	public ETHOAM analysisCommandToETHOAM(byte[] commands) throws Exception {

		AnalysisETHOAMTable analysisETHOAMTable = new AnalysisETHOAMTable();
		String configXMl = "com/nms/drive/analysis/xmltool/file/以太网OAM配置(0DH).xml";
		ETHOAM ethOAM = analysisETHOAMTable.analysisCommandInfoToETHOAM(commands, configXMl);

		return ethOAM;
	}

	/**
	 * 根据E1Object生成命令
	 */
	public byte[] AnalysisE1ToCommand(NEObject neObject, E1Object e1Object) throws Exception {
		byte[] e1ToCommand = new byte[0];

		AnalysisE1Table analysisE1Table = new AnalysisE1Table();
		String configXml = "com/nms/drive/analysis/xmltool/file/E1仿真配置(18H).xml";
		byte[] dataCommand = analysisE1Table.analysisE1ObjectToCommands(e1Object, configXml);

		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(24, dataCommand.length, 0);

		e1ToCommand = CoderUtils.arraycopy(e1ToCommand, neHand);
		e1ToCommand = CoderUtils.arraycopy(e1ToCommand, controlPanel);
		e1ToCommand = CoderUtils.arraycopy(e1ToCommand, dataBlock);
		e1ToCommand = CoderUtils.arraycopy(e1ToCommand, dataCommand);
		return e1ToCommand;
	}

	/**
	 * 根据命令生成E1Object对象
	 */
	public E1Object AnalysisCommandsToE1Object(byte[] commands) throws Exception {
		AnalysisE1Table analysisE1Table = new AnalysisE1Table();
		String configXMl = "com/nms/drive/analysis/xmltool/file/E1仿真配置 （18H）.xml";
		E1Object e1Object = analysisE1Table.analysisCommandsToE1Object(commands, configXMl);
		return e1Object;
	}

	/**
	 * 根据命令生成PerformanceObj对象
	 */
	public PerformanceObject AnalysisCommadsToPerformanceObjectBySite(byte[] commands) throws Exception {
		AnalysisPerformanceTable performanceTable = new AnalysisPerformanceTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/性能NE下所有盘配置.xml";
		PerformanceObject performanceObject = performanceTable.analysiscomandToObjectBySite(commands, configXml);
		return performanceObject;
	}

	/**
	 * 根据命令生成PerformanceObj对象
	 */
	public PerformanceObject AnalysisCommadsToPerformanceObjectByCard(byte[] commands) throws Exception {
		AnalysisPerformanceTable performanceTable = new AnalysisPerformanceTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/性能某盘配置.xml";
		PerformanceObject performanceObject = performanceTable.analysiscomandToObjectByCard(commands, configXml);
		return performanceObject;
	}

	/**
	 * 所有报告上来的告警 根据命令生成AlarmObject对象
	 */
	public AlarmObject AnalysisCommadsToAllReportAlarm(byte[] commands) throws Exception {
		AlarmObject alarmObject = new AlarmObject();
		if (commands.length > 5) {
			AnalysisAlarmReportTable analysisAlarmReportTable = new AnalysisAlarmReportTable();
			alarmObject = analysisAlarmReportTable.analysisCommandToAllAlarm(commands);
			return alarmObject;
		}
		return alarmObject;
	}

	/**
	 * 所有告警 根据命令生成AlarmObject对象
	 */
	public AlarmObject AnalysisCommadsToAllAlarm(byte[] commands) throws Exception {
		AnalysisAlarmTable analysisAlarmTable = new AnalysisAlarmTable();
		AlarmObject alarmObject = analysisAlarmTable.analysisCommandToAllAlarm(commands);
		return alarmObject;
	}

	/**
	 * 某盘告警 根据命令生成AlarmObject对象
	 */
	public AlarmObject AnalysisCommadsToPlateAlarm(byte[] commands) throws Exception {
		AnalysisAlarmTable analysisAlarmTable = new AnalysisAlarmTable();
		AlarmObject alarmObject = analysisAlarmTable.analysisCommandToplateAlarm(commands);
		return alarmObject;
	}

	/**
	 * 解析TMPOAMBugControl生成命令
	 */
	public byte[] AnalysisTMPOAMBugControlToCommand(NEObject neObject, TMPOAMBugControlObject tmpoam) throws Exception {
		byte[] tmpOAMCommand = new byte[0];

		AnalysisTMPOAMBugControlTable analysisTMPOAMBugControlTable = new AnalysisTMPOAMBugControlTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/TMP OAM故障管理配置(0FH).xml";
		byte[] dataCommand = analysisTMPOAMBugControlTable.analysisTMPOAMBugControlToCommand(tmpoam, configXml);

		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(15, dataCommand.length, 0);

		tmpOAMCommand = CoderUtils.arraycopy(tmpOAMCommand, neHand);
		tmpOAMCommand = CoderUtils.arraycopy(tmpOAMCommand, controlPanel);
		tmpOAMCommand = CoderUtils.arraycopy(tmpOAMCommand, dataBlock);
		tmpOAMCommand = CoderUtils.arraycopy(tmpOAMCommand, dataCommand);

		return tmpOAMCommand;
	}

	/**
	 * 解析命令生成TMPOAMBugControl对象
	 */
	public TMPOAMBugControlObject analysisCommandToTMPOAMBugControl(byte[] commands) throws Exception {

		AnalysisTMPOAMBugControlTable analysisTMPOAMBugControlTable = new AnalysisTMPOAMBugControlTable();
		String configXMl = "com/nms/drive/analysis/xmltool/file/TMP OAM故障管理配置(0FH).xml";
		TMPOAMBugControlObject tmpoam = analysisTMPOAMBugControlTable.analysisCommandInfoToTMPOAMBugControlObject(commands, configXMl);

		return tmpoam;
	}

	/**
	 * 解析PwQueueAndBufferManage生成命令
	 * 
	 * @param neObject
	 * @param pwManage
	 * @return
	 * @throws Exception
	 */
	public byte[] AnalysisPwQueueAndBufferManageToCommand(NEObject neObject, PwQueueAndBufferManage pwManage) throws Exception {
		byte[] pwManageToCommand = new byte[0];

		AnalysisPwManageTable pwManageTable = new AnalysisPwManageTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/PW层队列调度及缓存管理策略(12).xml";
		byte[] dataCommand = pwManageTable.analysisPwQueueAndBufferManageToCommands(pwManage, configXml);

		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(18, dataCommand.length, 0);

		pwManageToCommand = CoderUtils.arraycopy(pwManageToCommand, neHand);
		pwManageToCommand = CoderUtils.arraycopy(pwManageToCommand, controlPanel);
		pwManageToCommand = CoderUtils.arraycopy(pwManageToCommand, dataBlock);
		pwManageToCommand = CoderUtils.arraycopy(pwManageToCommand, dataCommand);

		return pwManageToCommand;

	}

	/**
	 * 解析命令生成PwQueueAndBufferManage
	 * 
	 * @param commands
	 * @return
	 * @throws Exception
	 */
	public PwQueueAndBufferManage AnalysisCommandToPwQueueAndBufferManage(byte[] commands) throws Exception {
		AnalysisPwManageTable analysisPwManageTable = new AnalysisPwManageTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/PW层队列调度及缓存管理策略(12).xml";
		PwQueueAndBufferManage pwManage = analysisPwManageTable.analysisCommandsToPwManage(commands, configXml);
		return pwManage;
	}

	/**
	 * 解析TMCOAMBugControl生成命令
	 */
	public byte[] AnalysisTMCOAMBugControlToCommand(NEObject neObject, TMCOAMBugControlObject tmpoam) throws Exception {
		byte[] tmcOAMCommand = new byte[0];

		AnalysisTMCOAMBugControlTable analysisTMCOAMBugControlTable = new AnalysisTMCOAMBugControlTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/TMC OAM故障管理配置(10H).xml";
		byte[] dataCommand = analysisTMCOAMBugControlTable.analysisTMCOAMBugControlToCommand(tmpoam, configXml);

		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(16, dataCommand.length, 0);

		tmcOAMCommand = CoderUtils.arraycopy(tmcOAMCommand, neHand);
		tmcOAMCommand = CoderUtils.arraycopy(tmcOAMCommand, controlPanel);
		tmcOAMCommand = CoderUtils.arraycopy(tmcOAMCommand, dataBlock);
		tmcOAMCommand = CoderUtils.arraycopy(tmcOAMCommand, dataCommand);

		return tmcOAMCommand;
	}

	/**
	 * 解析命令生成TMCOAMBugControl对象
	 */
	public TMCOAMBugControlObject analysisCommandToTMCOAMBugControl(byte[] commands) throws Exception {

		AnalysisTMCOAMBugControlTable analysisTMCOAMBugControlTable = new AnalysisTMCOAMBugControlTable();
		String configXMl = "com/nms/drive/analysis/xmltool/file/TMC OAM故障管理配置(10H).xml";
		TMCOAMBugControlObject tmcoam = analysisTMCOAMBugControlTable.analysisCommandInfoToTMCOAMBugControl(commands, configXMl);

		return tmcoam;
	}

	/**
	 * 解析命令生成端口聚合 PortLAG 对象
	 */
	public List<PortLAGObject> AnalysisCommandToPortLAG(byte[] commans) throws Exception {
		AnalysisPortLAGTable analysisPortLAGTable = new AnalysisPortLAGTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/端口聚合(14H).xml";
		List<PortLAGObject> portLAGObjectList = analysisPortLAGTable.analysisCommandToObject(commans, configXml);
		return portLAGObjectList;
	}

	/**
	 * 解析端口聚合 PortLAG 对象生成命令
	 */
	public byte[] AnalysisPortLAGToCommand(NEObject neObject, List<PortLAGObject> portLAGList) throws Exception {
		byte[] portLAGToCommand = new byte[0];

		AnalysisPortLAGTable analysisPortLAGTable = new AnalysisPortLAGTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/端口聚合(14H).xml";
		byte[] dataCommand = analysisPortLAGTable.analysisObjectToCommand(portLAGList, configXml);

		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(20, dataCommand.length, 0);

		portLAGToCommand = CoderUtils.arraycopy(portLAGToCommand, neHand);
		portLAGToCommand = CoderUtils.arraycopy(portLAGToCommand, controlPanel);
		portLAGToCommand = CoderUtils.arraycopy(portLAGToCommand, dataBlock);
		portLAGToCommand = CoderUtils.arraycopy(portLAGToCommand, dataCommand);

		return portLAGToCommand;
	}

	/**
	 * 解析List<TMCTunnelProtectObject>集合为命令
	 * 
	 * @param neObject
	 * @param tmcTunnelProtectObjList
	 * @return
	 * @throws Exception
	 */
	public byte[] AnalysisTMCTunnelProtectTocommands(NEObject neObject, List<TMCTunnelProtectObject> tmcTunnelProtectObjList) throws Exception {
		byte[] tmcTunnelProToCommand = new byte[0];

		AnalysisTMCTunnelProtectTable analysisTMCTunnelProtectTable = new AnalysisTMCTunnelProtectTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/TMC通道保护(0EH).xml";
		byte[] dataCommand = analysisTMCTunnelProtectTable.analysisTMCTunnelProtectObjToCommand(tmcTunnelProtectObjList, configXml);

		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(14, dataCommand.length, 0);

		tmcTunnelProToCommand = CoderUtils.arraycopy(tmcTunnelProToCommand, neHand);
		tmcTunnelProToCommand = CoderUtils.arraycopy(tmcTunnelProToCommand, controlPanel);
		tmcTunnelProToCommand = CoderUtils.arraycopy(tmcTunnelProToCommand, dataBlock);
		tmcTunnelProToCommand = CoderUtils.arraycopy(tmcTunnelProToCommand, dataCommand);

		return tmcTunnelProToCommand;
	}

	/**
	 * 解析命令为List<TMCTunnelProtectObject>集合
	 * 
	 * @param neObject
	 * @param tmcTunnelProtectObjList
	 * @return
	 * @throws Exception
	 */
	public List<TMCTunnelProtectObject> AnalysisCommandsToTMCTunnelProtectObject(byte[] commands) throws Exception {
		AnalysisTMCTunnelProtectTable analysisTMCTunnelProtectTable = new AnalysisTMCTunnelProtectTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/TMC通道保护(0EH).xml";
		List<TMCTunnelProtectObject> tmcTunnelProtectObjList = analysisTMCTunnelProtectTable.analysisCommandToTMCTunnelProtectObj(commands, configXml);
		return tmcTunnelProtectObjList;
	}

	/**
	 * 解析timeSynchronizeObject为命令
	 * 
	 * @param neObject
	 * @param timeSynchronizeObject
	 * @return
	 * @throws Exception
	 */
	public byte[] AnalysisTimeSynchronizeObjectToCommands(NEObject neObject, TimeSynchronizeObject timeSynchronizeObject) throws Exception {
		byte[] timeSynchronizeObjToCommand = new byte[0];

		AnalysisTimeSynchronizeTable timeSynchronizeTable = new AnalysisTimeSynchronizeTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/时间同步配置块(1CH).xml";
		byte[] dataCommand = timeSynchronizeTable.analysisTimeSynchronizeObjectToCommands(timeSynchronizeObject, configXml);

		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(28, dataCommand.length, 0);

		timeSynchronizeObjToCommand = CoderUtils.arraycopy(timeSynchronizeObjToCommand, neHand);
		timeSynchronizeObjToCommand = CoderUtils.arraycopy(timeSynchronizeObjToCommand, controlPanel);
		timeSynchronizeObjToCommand = CoderUtils.arraycopy(timeSynchronizeObjToCommand, dataBlock);
		timeSynchronizeObjToCommand = CoderUtils.arraycopy(timeSynchronizeObjToCommand, dataCommand);

		return timeSynchronizeObjToCommand;
	}

	/**
	 * 解析命令为timeSynchronizeObject
	 * 
	 * @param neObject
	 * @param timeSynchronizeObject
	 * @return
	 * @throws Exception
	 */
	public TimeSynchronizeObject AnalysisCommandsToTimeSynchronizeObject(byte[] commands) throws Exception {
		AnalysisTimeSynchronizeTable timeSynchronizeTable = new AnalysisTimeSynchronizeTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/时间同步配置块(1CH).xml";
		TimeSynchronizeObject timeSynchronizeObject = timeSynchronizeTable.analysisCommandsToTimeSynchronizeObject(commands, configXml);
		return timeSynchronizeObject;
	}

	/**
	 * 解析命令为PHBToEXP对象
	 */
	public List<PHBToEXPObject> analysisCommandToPHB(byte[] commands) throws Exception {
		AnalysisPHBToEXPTable analysisPHBToEXPTable = new AnalysisPHBToEXPTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/PHB到TMCTMP EXP映射表(09H).xml";
		List<PHBToEXPObject> phbToexpObject = analysisPHBToEXPTable.analysisCommandToObject(commands, configXml);
		return phbToexpObject;
	}

	/**
	 * 解析PHBToEXP对象为命令
	 */
	public byte[] analysisPHBToCommand(NEObject neObject, List<PHBToEXPObject> phbToEXP) throws Exception {
		byte[] PHBToCommand = new byte[0];
		AnalysisPHBToEXPTable analysisPHBToEXPTable = new AnalysisPHBToEXPTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/PHB到TMCTMP EXP映射表(09H).xml";

		byte[] dataCommand = analysisPHBToEXPTable.analysisObjectToCommand(phbToEXP, configXml);

		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(9, dataCommand.length, 0);

		PHBToCommand = CoderUtils.arraycopy(PHBToCommand, neHand);
		PHBToCommand = CoderUtils.arraycopy(PHBToCommand, controlPanel);
		PHBToCommand = CoderUtils.arraycopy(PHBToCommand, dataBlock);
		PHBToCommand = CoderUtils.arraycopy(PHBToCommand, dataCommand);

		return PHBToCommand;
	}

	/**
	 * 解析TMSOAMBugControl生成命令
	 */
	public byte[] AnalysisTMSOAMBugControlToCommand(NEObject neObject, TMSOAMBugControlObject tmpoam) throws Exception {
		byte[] tmcOAMCommand = new byte[0];

		AnalysisTMSOAMBugControlTable analysisTMSOAMBugControlTable = new AnalysisTMSOAMBugControlTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/TMS OAM故障管理配置(11H).xml";
		byte[] dataCommand = analysisTMSOAMBugControlTable.analysisTMSOAMBugControlToCommand(tmpoam, configXml);

		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(17, dataCommand.length, 0);

		tmcOAMCommand = CoderUtils.arraycopy(tmcOAMCommand, neHand);
		tmcOAMCommand = CoderUtils.arraycopy(tmcOAMCommand, controlPanel);
		tmcOAMCommand = CoderUtils.arraycopy(tmcOAMCommand, dataBlock);
		tmcOAMCommand = CoderUtils.arraycopy(tmcOAMCommand, dataCommand);

		return tmcOAMCommand;
	}

	/**
	 * 解析命令生成TMSOAMBugControl对象
	 */
	public TMSOAMBugControlObject analysisCommandToTMSOAMBugControl(byte[] commands) throws Exception {

		AnalysisTMSOAMBugControlTable analysisTMSOAMBugControlTable = new AnalysisTMSOAMBugControlTable();
		String configXMl = "com/nms/drive/analysis/xmltool/file/TMS OAM故障管理配置(11H).xml";
		TMSOAMBugControlObject tmsoam = analysisTMSOAMBugControlTable.analysisCommandInfoToTMSOAMBugControlObject(commands, configXMl);

		return tmsoam;
	}

	/**
	 * 解析TMSOAMObject对象为命令
	 * 
	 * @param neObject
	 * @param tmsOamObject
	 * @return
	 * @throws Exception
	 */
	public byte[] AnalysisTMSOAMObjectToCommands(NEObject neObject, TMSOAMObject tmsOamObject) throws Exception {

		byte[] tmsOamObjToCommand = new byte[0];

		AnalysisTMSOAMTable analysisTMSOAMTable = new AnalysisTMSOAMTable();
		String TMSOAMInfoXml = "com/nms/drive/analysis/xmltool/file/TMS OAM配置块(1AH).xml";
		byte[] dataCommand = analysisTMSOAMTable.analysisTMSOAMObjectToCommands(tmsOamObject, TMSOAMInfoXml);

		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(26, dataCommand.length, 0);

		tmsOamObjToCommand = CoderUtils.arraycopy(tmsOamObjToCommand, neHand);
		tmsOamObjToCommand = CoderUtils.arraycopy(tmsOamObjToCommand, controlPanel);
		tmsOamObjToCommand = CoderUtils.arraycopy(tmsOamObjToCommand, dataBlock);
		tmsOamObjToCommand = CoderUtils.arraycopy(tmsOamObjToCommand, dataCommand);

		return tmsOamObjToCommand;
	}

	/**
	 * 解析命令为TMSOAMObject对象
	 * 
	 * @param commands
	 * @return
	 * @throws Exception
	 */
	public TMSOAMObject AnalysisCommandsToTMSOAMObject(byte[] commands) throws Exception {
		AnalysisTMSOAMTable analysisTMSOAMTable = new AnalysisTMSOAMTable();
		String TMSOAMInfoXml = "com/nms/drive/analysis/xmltool/file/TMS OAM配置块(1AH)_SUB.xml";
		TMSOAMObject tmsOamObject = analysisTMSOAMTable.analysisCommandsToTMSOAMObject(commands, TMSOAMInfoXml);
		return tmsOamObject;
	}

	/**
	 * 解析seniorPortConfigObjectlist对象为命令
	 * 
	 * @param neObject
	 * @param SeniorPortConfig
	 * @return
	 * @throws Exception
	 */
	public byte[] AnalysisPortSeniorConfigCommandToCommands(NEObject neObject, List<PortSeniorConfig> portSeniorConfigObjectlist) throws Exception {

		byte[] portSeniorConfigCommand = new byte[0];

		AnalysisPortSeniorConfigTable analysisPortSeniorConfigConfigTable = new AnalysisPortSeniorConfigTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/端口高级配置(19H).xml";
		byte[] dataCommand = analysisPortSeniorConfigConfigTable.analysisObjectToCommand(portSeniorConfigObjectlist, configXml);

		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(25, dataCommand.length, 0);

		portSeniorConfigCommand = CoderUtils.arraycopy(portSeniorConfigCommand, neHand);
		portSeniorConfigCommand = CoderUtils.arraycopy(portSeniorConfigCommand, controlPanel);
		portSeniorConfigCommand = CoderUtils.arraycopy(portSeniorConfigCommand, dataBlock);
		portSeniorConfigCommand = CoderUtils.arraycopy(portSeniorConfigCommand, dataCommand);

		return portSeniorConfigCommand;
	}

	/**
	 * 解析命令为seniorPortConfigObjectlist对象
	 * 
	 * @param commands
	 * @return
	 * @throws Exception
	 */
	public PortSeniorConfig AnalysisCommandsToPortSeniorConfig(byte[] commands) throws Exception {
		AnalysisPortSeniorConfigTable analysisPortSeniorConfigTable = new AnalysisPortSeniorConfigTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/端口高级配置(19H)_SUB.xml";
		PortSeniorConfig portSeniorConfigObject = analysisPortSeniorConfigTable.analysisCommandToObject(commands, configXml);
		return portSeniorConfigObject;
	}

	/**
	 * 解析EXPToPHB 对象生成命令
	 */
	public byte[] analysisEXPToCommand(NEObject neObject, List<EXPToPHBObject> expToPHBObject) throws Exception {
		byte[] expToPHBCommand = new byte[0];

		String configXml = "com/nms/drive/analysis/xmltool/file/TMP EXP到PHB映射表(0AH).xml";
		byte[] dataCommand = new AnalysisEXPToPHBTable().analysisObjectToCommand(expToPHBObject, configXml);

		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(10, dataCommand.length, 0);

		expToPHBCommand = CoderUtils.arraycopy(expToPHBCommand, neHand);
		expToPHBCommand = CoderUtils.arraycopy(expToPHBCommand, controlPanel);
		expToPHBCommand = CoderUtils.arraycopy(expToPHBCommand, dataBlock);
		expToPHBCommand = CoderUtils.arraycopy(expToPHBCommand, dataCommand);

		return expToPHBCommand;
	}

	/**
	 * 解析命令生成对象 EXPToPHB
	 */
	public List<EXPToPHBObject> analysisCommandToEXPObject(byte[] commands) throws Exception {
		String configXml = "com/nms/drive/analysis/xmltool/file/TMP EXP到PHB映射表(0AH).xml";
		List<EXPToPHBObject> expToPHBObject = new AnalysisEXPToPHBTable().analysisCommandToObject(commands, configXml);
		return expToPHBObject;
	}

	/**
	 * 解析ClockObject对象为命令
	 * 
	 * @param neObject
	 * @param clockObject
	 * @return
	 * @throws Exception
	 */
	public byte[] AnalysisClockObjectToCommands(NEObject neObject, ClockObject clockObject) throws Exception {
		byte[] clockToCommand = new byte[0];

		String configXml = "com/nms/drive/analysis/xmltool/file/时钟配置块(01H).xml";
		AnalysisClockTable analysisClockTable = new AnalysisClockTable();
		byte[] dataCommand = analysisClockTable.analysisClockObjectToCommands(clockObject, configXml);

		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(29, dataCommand.length, 0);

		clockToCommand = CoderUtils.arraycopy(clockToCommand, neHand);
		clockToCommand = CoderUtils.arraycopy(clockToCommand, controlPanel);
		clockToCommand = CoderUtils.arraycopy(clockToCommand, dataBlock);
		clockToCommand = CoderUtils.arraycopy(clockToCommand, dataCommand);

		return clockToCommand;
	}

	/**
	 * 解析命令为ClockObject对象
	 * 
	 * @param commands
	 * @return
	 * @throws Exception
	 */
	public ClockObject AnalysisCommandsToClockObject(byte[] commands) throws Exception {
		AnalysisClockTable analysisClockTable = new AnalysisClockTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/时钟配置块(01H).xml";
		ClockObject clockObject = analysisClockTable.analysisCommandsToClockObject(commands, configXml);

		return clockObject;
	}

	/**
	 * 解析 以太网链路OAM 对象 生成命令
	 */
	public byte[] analysisETHLinkOAMToCommand(NEObject neObject, List<ETHLinkOAMObject> ethLinkOAMList) throws Exception {
		byte[] ethLinkCommand = new byte[0];

		String configXml = "com/nms/drive/analysis/xmltool/file/以太网链路OAM(1BH).xml";
		byte[] dataCommand = new AnalysisETHLinkTable().analysisObjectToCommand(ethLinkOAMList, configXml);

		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(27, dataCommand.length, 0);

		ethLinkCommand = CoderUtils.arraycopy(ethLinkCommand, neHand);
		ethLinkCommand = CoderUtils.arraycopy(ethLinkCommand, controlPanel);
		ethLinkCommand = CoderUtils.arraycopy(ethLinkCommand, dataBlock);
		ethLinkCommand = CoderUtils.arraycopy(ethLinkCommand, dataCommand);

		return ethLinkCommand;
	}

	/**
	 * 解析命令生成 以太网链路OAM 对象
	 */
	public List<ETHLinkOAMObject> analysisCommandToETHLinkOAM(byte[] command) throws Exception {
		String configXml = "com/nms/drive/analysis/xmltool/file/以太网链路OAM(1BH).xml";
		List<ETHLinkOAMObject> ethLink = new AnalysisETHLinkTable().analysisCommandToObject(command, configXml);
		return ethLink;
	}

	/**
	 * 解析环网保护 生成命令
	 */
	public byte[] analysisRoundPToCommand(NEObject neObject, List<RoundProtectionObject> roundP_List) throws Exception {
		byte[] command = new byte[0];

		String configXml = "com/nms/drive/analysis/xmltool/file/环网保护(04H).xml";
		byte[] dataCommand = new AnalysisRoundProtectionTable().analysisRoundProtectionObjcetToCommands(roundP_List, configXml);

		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(4, dataCommand.length, 0);

		command = CoderUtils.arraycopy(command, neHand);
		command = CoderUtils.arraycopy(command, controlPanel);
		command = CoderUtils.arraycopy(command, dataBlock);
		command = CoderUtils.arraycopy(command, dataCommand);

		return command;
	}

	/**
	 * 解析命令生成环网保护对象
	 */
	public List<RoundProtectionObject> analysisCommandToRoundP(byte[] command) throws Exception {
		String configXml = "com/nms/drive/analysis/xmltool/file/环网保护(04H).xml";
		List<RoundProtectionObject> roundP = new AnalysisRoundProtectionTable().analysisCommandToRoundProtectionObject(command, configXml);
		return roundP;
	}

	/**
	 * IGMP SNOOPING配置 生成命令
	 */
	public byte[] analysisIGMPToCommand(NEObject neObject, List<IGMPSNOOPINGObject> igmpS_List) throws Exception {
		byte[] command = new byte[0];

		String configXml = "com/nms/drive/analysis/xmltool/file/IGMP SNOOPING配置(17H).xml";
		byte[] dataCommand = new AnalysisIGMPSNOOPINGTable().analysisIGMPSNOOPINGObjectToCommands(igmpS_List, configXml);

		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(23, dataCommand.length, 0);

		command = CoderUtils.arraycopy(command, neHand);
		command = CoderUtils.arraycopy(command, controlPanel);
		command = CoderUtils.arraycopy(command, dataBlock);
		command = CoderUtils.arraycopy(command, dataCommand);

		return command;
	}

	/**
	 * 命令生成 IGMP SNOOPING配置 对象
	 */
	public List<IGMPSNOOPINGObject> analysisCommandToIGMP(byte[] command) throws Exception {
		String configXml = "com/nms/drive/analysis/xmltool/file/IGMP SNOOPING配置(17H).xml";
		List<IGMPSNOOPINGObject> igmp = new AnalysisIGMPSNOOPINGTable().analysisCommandsToIGMPSNOOPINGObject(command, configXml);
		return igmp;
	}

	/**
	 * 静态组播(0CH) 对象 生成 命令
	 */
	public byte[] analysisGroupSpreadToCommand(NEObject neObject, List<GroupSpreadObject> groupSpreadObject) throws Exception {
		byte[] command = new byte[0];

		String configXml = "com/nms/drive/analysis/xmltool/file/静态组播(0CH).xml";
		byte[] dataCommand = new AnalysisGroupSpread().analysisObjectToCommand(groupSpreadObject, configXml);

		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(12, dataCommand.length, 0);

		command = CoderUtils.arraycopy(command, neHand);
		command = CoderUtils.arraycopy(command, controlPanel);
		command = CoderUtils.arraycopy(command, dataBlock);
		command = CoderUtils.arraycopy(command, dataCommand);

		return command;
	}

	/**
	 * 命令 生成 静态组播(0CH) 对象
	 */
	public List<GroupSpreadObject> analysisCommandToGroupSpread(byte[] command) throws Exception {
		String configXml = "com/nms/drive/analysis/xmltool/file/静态组播(0CH).xml";
		List<GroupSpreadObject> gs = new AnalysisGroupSpread().analysisCommandToObject(command, configXml);
		return gs;
	}

	/**
	 * 全局配置块(03H) 对象 生成 命令
	 */
	public byte[] analysisGlpbalToCommand(NEObject neObject, GlobalObject globalObject) throws Exception {
		byte[] command = new byte[0];

		String configXml = "com/nms/drive/analysis/xmltool/file/全局配置块(03H).xml";
		byte[] dataCommand = new AnalysisGlobalTable().analysisGlobalObjectToCommands(globalObject, configXml);

		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(3, dataCommand.length, 0);

		command = CoderUtils.arraycopy(command, neHand);
		command = CoderUtils.arraycopy(command, controlPanel);
		command = CoderUtils.arraycopy(command, dataBlock);
		command = CoderUtils.arraycopy(command, dataCommand);

		return command;
	}

	/**
	 * 命令 生成 全局配置块(03H) 对象
	 */
	public GlobalObject analysisCommandToObject(byte[] command) throws Exception {
		String configXml = "com/nms/drive/analysis/xmltool/file/全局配置块(03H).xml";
		GlobalObject globalObject = new AnalysisGlobalTable().analysisCommandsToGlobalObject(command, configXml);
		return globalObject;
	}

	/**
	 * 单卡基本信息 对象 生成 命令
	 */
	public byte[] analysisSingleToCommand(NEObject neObject, SingleObject singleObject) throws Exception {
		byte[] command = new byte[0];

		String configXml = "com/nms/drive/analysis/xmltool/file/单卡基本信息.xml";
		byte[] dataCommand = new AnalysisSingleCardBasicInformation().analysisObjectToCommand(singleObject, configXml);

		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(1, dataCommand.length, 0);

		command = CoderUtils.arraycopy(command, neHand);
		command = CoderUtils.arraycopy(command, controlPanel);
		command = CoderUtils.arraycopy(command, dataBlock);
		command = CoderUtils.arraycopy(command, dataCommand);

		return command;
	}

	/**
	 * 命令 生成 单卡基本信息 对象
	 */
	public SingleObject analysisCommandToSingleObject(byte[] command) throws Exception {
		String configXml = "com/nms/drive/analysis/xmltool/file/单卡基本信息.xml";
		SingleObject singleObject = new AnalysisSingleCardBasicInformation().analysisCommandToObject(command, configXml);
		return singleObject;
	}

	/**
	 * 静态单播(0BH) 对象 生成命令
	 */
	public byte[] analysisStaticUToCommand(NEObject neObject, List<StaticUnicastObject> staticUnicastObject) throws Exception {
		byte[] command = new byte[0];

		String configXml = "com/nms/drive/analysis/xmltool/file/静态单播(0BH).xml";
		byte[] dataCommand = new AnalysisStaticUnicast().analysisObjectToCommand(staticUnicastObject, configXml);

		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(11, dataCommand.length, 0);

		command = CoderUtils.arraycopy(command, neHand);
		command = CoderUtils.arraycopy(command, controlPanel);
		command = CoderUtils.arraycopy(command, dataBlock);
		command = CoderUtils.arraycopy(command, dataCommand);

		return command;
	}

	/**
	 * 命令 生成 静态单播(0BH) 对象
	 */
	public List<StaticUnicastObject> analysisCommandToStaticU(byte[] command) throws Exception {
		String configXml = "com/nms/drive/analysis/xmltool/file/静态单播(0BH).xml";
		List<StaticUnicastObject> a = new AnalysisStaticUnicast().analysisCommandToObject(command, configXml);
		return a;
	}

	@Override
	public byte[] AnalysisPortConfigToCommands(NEObject neObject, PortObject protObject) throws Exception {
		byte[] command = new byte[0];

		String configXml = "com/nms/drive/analysis/xmltool/file/端口配置块.xml";
		byte[] dataCommand = new AnalysisPortConfig().analysisPortConfigObjectToCommands(protObject, configXml);

		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(21, dataCommand.length, 0);

		command = CoderUtils.arraycopy(command, neHand);
		command = CoderUtils.arraycopy(command, controlPanel);
		command = CoderUtils.arraycopy(command, dataBlock);
		command = CoderUtils.arraycopy(command, dataCommand);

		return command;
	}

	/**
	 * 命令 生成 单卡基本信息 对象
	 */
	public PortObject analysisPortConfigObjectToCommands(byte[] command) throws Exception {
		String configXml = "com/nms/drive/analysis/xmltool/file/端口配置块_SUB.xml";
		PortObject protObject = new AnalysisPortConfig().analysisCommandsToPortConfigObject(command, configXml);
		return protObject;
	}

	/**
	 *性能门限配置块 命令
	 */
	public byte[] AnalysisPmLimiteObjectToCommands(NEObject neObject, PmValueLimiteObject pmValueLimiteObject) throws Exception {
		String configXml = "com/nms/drive/analysis/xmltool/file/性能门限配置块(20H).xml";
		byte[] command = new byte[0];
		byte[] dataCommand = new AnalysisPmLimiteTable().analysisObjectToCommand(pmValueLimiteObject, configXml);
		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(32, dataCommand.length, 0);

		command = CoderUtils.arraycopy(command, neHand);
		command = CoderUtils.arraycopy(command, controlPanel);
		command = CoderUtils.arraycopy(command, dataBlock);
		command = CoderUtils.arraycopy(command, dataCommand);

		return command;
	}
	public PmValueLimiteObject analysisCommandToPmLimiteObject(byte[] command) throws Exception {
		String configXml = "com/nms/drive/analysis/xmltool/file/性能门限配置块(20H).xml";
		PmValueLimiteObject obj = new AnalysisPmLimiteTable().analysisCommandsToGlobalObject(command, configXml);
		return obj;
	}
	
	public AclObject analysisCommandToAclObject(byte[] command) throws Exception {
		String configXml = "com/nms/drive/analysis/xmltool/file/ACL配置块(22H).xml";
		AclObject aclObj = new AnalysisAclTable().analysisCommandsToAclObject(command, configXml);
		return aclObj;
	}

	public List<SecondMacStudyObject> analysisCommandToSecondMacObject(byte[] command) throws Exception {
		AnalysisSecondMacTable analysisSecondMacTable = new AnalysisSecondMacTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/二层静态MAC地址学习配置块(3fH).xml";
		List<SecondMacStudyObject> objList =analysisSecondMacTable.analysisToCommandsToSecondMacStudyObject(command, configXml);
		return objList;
	}
	
	public List<RoundProtectionObject> analysisCommandToLoopProRotateObject(byte[] command) throws Exception {
		String configXml = "com/nms/drive/analysis/xmltool/file/环网保护(04H).xml";
		List<RoundProtectionObject> loopObj = new AnalysisRoundProtectionTable().analysisCommandsToLoopProRotateObject(command, configXml);
		return loopObj;
	}
	
	public TimeSyncObject analysisCommandsToTimeSynchrObject(byte[] command) throws Exception {
		String configXml = "com/nms/drive/analysis/xmltool/file/时间同步配置块(1CH).xml";
		TimeSyncObject timeSyncObj = new AnalysisTimeSyncTable().analysisCommandsToTimesyncObject(command, configXml);
		return timeSyncObj;
	}
    //l2CP同步
	public L2CPinfoObject analysisCommandToL2cpObject(byte[] command) throws Exception {
		String configXml = "com/nms/drive/analysis/xmltool/file/L2CP功能(59).xml";
		L2CPinfoObject l2cpObj = new AnalysisL2CPServciceTable().analysisCommandsToL2cpInfoObject(command, configXml);
		return l2cpObj;
	}
	//MAC黑名单
	public List<MacManageObject> analysisCommandToMacManageObject(byte[] command) throws Exception {
		AnalysisMacTable analysisMacTable = new AnalysisMacTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/MAC管理(23H).xml";
		List<MacManageObject> objList =analysisMacTable.analysisToCommandsToMacManageObject(command, configXml);
		return objList;
	}
	
	//MAC黑白名单
		public List<BlackWhiteMacObject> analysisCommandToBlackWhiteMacObject(byte[] command) throws Exception {
			AnalysisBlackWhiteMacTable analysisBlackWhiteMacTable = new AnalysisBlackWhiteMacTable();
			String configXml = "com/nms/drive/analysis/xmltool/file/MAC地址的黑白名单(24H).xml";
			List<BlackWhiteMacObject> objList =analysisBlackWhiteMacTable.analysisToCommandsToBlackWhiteMacManageObject(command, configXml);
			return objList;
		}
			

	/**
	 * 获得NE头信息
	 * 
	 * @param neObject
	 * @return
	 */
	private byte[] neHand(NEObject neObject) {
		byte[] neHand = new byte[5];

		// NE地址
		byte[] neAddress = CoderUtils.intToBytes(neObject.getNeAddress());
		neHand[0] = neAddress[2];// 0x01
		neHand[1] = neAddress[3];// 0x01

		// 盘类型
		byte[] controlPanelType = CoderUtils.intToBytes(neObject.getControlPanelType());
		neHand[2] = controlPanelType[3]; // 0xa0

		// 盘组号
		byte[] controlPanelGroupId = CoderUtils.intToBytes(neObject.getControlPanelGroupId());
		neHand[3] = controlPanelGroupId[3];// 0x01

		// 盘地址
		byte[] controlPanelAddress = CoderUtils.intToBytes(neObject.getControlPanelAddress());
		neHand[4] = controlPanelAddress[3];// 0x02

		return neHand;
	}

	/**
	 * 获得盘头信息
	 * 
	 * @param blockCount
	 * @param neObject
	 * @return
	 */
	private byte[] controlPanelHead(int blockCount, NEObject neObject) {
		byte[] controlPanelHead = new byte[101];
		// 盘类型扩展码（0xFE）
		controlPanelHead[0] = (byte) 0xFE;

		// 固定填充字段
		for (int i = 1; i < 86; i++) {
			controlPanelHead[i] = 0x00;
		}

		// 盘类型
		byte[] controlPanelType = CoderUtils.intToBytes(neObject.getControlPanelType());
		controlPanelHead[19] = controlPanelType[0];// 0x03
		controlPanelHead[20] = controlPanelType[1];// 0x00
		controlPanelHead[21] = controlPanelType[2];// 0x02
		controlPanelHead[22] = controlPanelType[3];// 0xa0

		// 配置块扩展标志（BE，CF，FC，EB）
		controlPanelHead[86] = (byte) 0xBE;
		controlPanelHead[87] = (byte) 0xCF;
		controlPanelHead[88] = (byte) 0xFC;
		controlPanelHead[89] = (byte) 0xEB;

		// 配置扩展类型（01=ASON扩展格式）
		controlPanelHead[90] = 0x01;

		// 配置块条目表偏移
		controlPanelHead[91] = 0x00;
		controlPanelHead[92] = 0x06;

		// 盘类型
		controlPanelHead[93] = controlPanelType[0];// 0x03
		controlPanelHead[94] = controlPanelType[1];// 0x00
		controlPanelHead[95] = controlPanelType[2];// 0x02
		controlPanelHead[96] = controlPanelType[3];// 0xa0

		// 盘组号
		byte[] controlPanelGroupId = CoderUtils.intToBytes(neObject.getControlPanelGroupId());
		controlPanelHead[97] = controlPanelGroupId[3];// 0x01

		// 盘地址
		byte[] controlPanelAddress = CoderUtils.intToBytes(neObject.getControlPanelAddress());
		controlPanelHead[98] = controlPanelAddress[3];// 0x02

		// 配置扩展类型
		controlPanelHead[99] = 0x01;

		// 分块配置块个数
		byte[] blockCountByte = CoderUtils.intToBytes(blockCount);
		controlPanelHead[100] = blockCountByte[3];

		return controlPanelHead;
	}

	/**
	 * 管理配置生成命令
	 * 
	 * @param neObject网元信息
	 * @return 数据体命令
	 * 
	 * @throws Exception
	 */
	public byte[] AnalysisManagementObjectToCommands(NEObject neObject, ManagementObject managementObject) throws Exception {
		AnalysisManagementTable analysisManagementTable = new AnalysisManagementTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/管理配置.xml";
		byte[] dataCommand = analysisManagementTable.AnalysisObjectToCommand(managementObject, configXml);

		return dataCommand;
	}

	/**
	 * 软件升级
	 * 
	 * @param neObject
	 * @param softwareUpdate
	 * @return
	 * @throws Exception
	 */
	public byte[] AnalysisSoftwareUpdateObjectToCommands(NEObject neObject, SoftwareUpdate softwareUpdate) throws Exception {
		AnalysisSoftwareUpdateTable analysisSoftwareUpdateTable = new AnalysisSoftwareUpdateTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/WS向某NE某盘下载软件配置.xml";
		byte[] dataCommand = analysisSoftwareUpdateTable.AnalysisObjectToCommand(softwareUpdate, configXml);
		return dataCommand;
	}

	/**
	 * 解析命令生成ResponseObject对象
	 * 
	 * @param byte[] commands
	 * @return responseObject
	 * @throws Exception
	 */
	public ResponseObject AnalysisCommandsToResponseObject(byte[] commands) throws Exception {
		String configXml = "com/nms/drive/analysis/xmltool/file/回答配置.xml";
		ResponseObject responseObject = new AnalysisResponseTable().analysisCommandToObject(commands, configXml);
		return responseObject;
	}

	/**
	 * 根据字节数组生成单盘信息对象
	 * 
	 * @param command
	 * @return SinglePan
	 * @throws Exception
	 */
	public SinglePan AnalysisSinglePanAutoTable(byte[] commands) throws Exception {
		String configXml = "com/nms/drive/analysis/xmltool/file/单盘自动发现信息.xml";
		SinglePan singlePan = new AnalysisSinglePanAutoTable().analysisCommandToObject(commands, configXml);
		return singlePan;
	}

	/**
	 * 根据对象生成字节数组
	 * 
	 * @param responsePan
	 * @param configXml
	 * @return
	 * @throws Exception
	 */
	public byte[] AnalysisResponsePanTable(NEObject neObject, ResponsePan responsePan) throws Exception {
		byte[] command = new byte[0];

		String configXml = "com/nms/drive/analysis/xmltool/file/回答单盘信息配置.xml";
		byte[] dataCommand = new AnalysisResponsePanTable().analysisObjectToCommand(responsePan, configXml);

		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(11, dataCommand.length, 0);

		command = CoderUtils.arraycopy(command, neHand);
		command = CoderUtils.arraycopy(command, controlPanel);
		command = CoderUtils.arraycopy(command, dataBlock);
		command = CoderUtils.arraycopy(command, dataCommand);

		return command;
	}

	/**
	 * 查询所有业务生成Object对象
	 */
	public ActionObject AnalysisCommadsToAllBusiness(byte[] commands) throws Exception {
		AnalysisAllBusinessTable analysisAllBusinessTable = new AnalysisAllBusinessTable();
		ActionObject actionObject = analysisAllBusinessTable.analysisCommandToObject(commands);
		return actionObject;
	}

	/**
	 * 查询所有业务生成Object对象
	 */
	public NEObject AnalysisCommadsToSiteAttribute(byte[] commands) throws Exception {
		String configXml = "com/nms/drive/analysis/xmltool/file/查询网元属性.xml";
		AnalysisSiteAttributeTable analysisAllBusinessTable = new AnalysisSiteAttributeTable();
		NEObject neObject = analysisAllBusinessTable.analysisCommandToSite(commands, configXml);
		return neObject;
	}

	/**
	 * 获取网元连接状态
	 */
	public List<NEObject> AnalysisCommadsToSiteObject(byte[] commands) throws Exception {
		String configXml = "com/nms/drive/analysis/xmltool/file/网元连接状态_SUB.xml";
		AnalysisSiteConnectTable analysisSiteConnectTable = new AnalysisSiteConnectTable();
		List<NEObject> neObjects = analysisSiteConnectTable.analysisCommandsToSiteObject(commands, configXml);
		return neObjects;
	}

	/**
	 * 获取设备轮询上报网元连接状态
	 */
	public List<NEObject> AnalysisCommadsToCircleSiteObject(byte[] commands) throws Exception {
		String configXml = "com/nms/drive/analysis/xmltool/file/网元连接状态_SUB(2).xml";
		AnalysisCircleSiteConnectTable analysisCircleSiteConnectTable = new AnalysisCircleSiteConnectTable();
		List<NEObject> neObjects = analysisCircleSiteConnectTable.analysisCommandsToSiteObject(commands, configXml);
		return neObjects;
	}

	/**
	 * 端口状态信息
	 */
	public List<PortStatusObject> AnalysisPortStatus(byte[] commands) throws Exception {
		String configXml = "com/nms/drive/analysis/xmltool/file/status/端口状态配置块.xml";
		AnalysisPortStatus analysisPortStatus = new AnalysisPortStatus();
		List<PortStatusObject> portStatuss = analysisPortStatus.analysisCommandsToPortConfigObject(commands, configXml);
		return portStatuss;
	}

	/**
	 * LSP保护状态信息
	 */
	public List<ProtectStatusObject> AnalysisLspProetctStatus(byte[] commands) throws Exception {
		String configXml = "com/nms/drive/analysis/xmltool/file/status/LSP保护状态配置块.xml";
		AnalysisLspProetctStatus analysisLspProetctStatus = new AnalysisLspProetctStatus();
		List<ProtectStatusObject> protectStatuss = analysisLspProetctStatus.analysisCommandsToPortConfigObject(commands, configXml);
		return protectStatuss;
	}

	/**
	 * 网元所以状态信息
	 */
	public ActionObject AnalysisSiteStatusTable(byte[] commands) throws Exception {
		AnalysisSiteStatusTable analysisSiteStatusTable = new AnalysisSiteStatusTable();
		ActionObject actionObject = analysisSiteStatusTable.analysisCommandToObject(commands);
		return actionObject;
	}

	/**
	 * 解析msPw生成命令
	 * 
	 * @param neObject网元信息
	 * @param PwObject隧道对象
	 * @return 数据体命令
	 * @throws Exception
	 * @throws Exception
	 */
	public byte[] AnalysisMsPwToCommand(NEObject neObject, List<MsPwObject> msPwObjects) throws Exception {
		byte[] pwToCommand = new byte[0];

		AnalysisMSPWTable analysisPWTable = new AnalysisMSPWTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/多段pw配置块.xml";
		byte[] dataCommand = analysisPWTable.analysisObjectToCommand(msPwObjects, configXml);
		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(30, dataCommand.length, 0);

		pwToCommand = CoderUtils.arraycopy(pwToCommand, neHand);
		pwToCommand = CoderUtils.arraycopy(pwToCommand, controlPanel);
		pwToCommand = CoderUtils.arraycopy(pwToCommand, dataBlock);
		pwToCommand = CoderUtils.arraycopy(pwToCommand, dataCommand);

		return pwToCommand;
	}

	/**
	 * 查询MSPW生成Object对象
	 */
	public List<MsPwObject> AnalysisCommadsToMspw(byte[] commands) throws Exception {
		AnalysisMSPWTable analysisMSPWTable = new AnalysisMSPWTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/多段pw配置块.xml";
		List<MsPwObject> msPwObjects = analysisMSPWTable.analysisCommandToObject(commands, configXml);
		return msPwObjects;
	}

	/**
	 * PW状态信息
	 */
	public List<PwStatusObject> AnalysisPwStatus(byte[] commands) throws Exception {
		String configXml = "com/nms/drive/analysis/xmltool/file/status/PW状态配置块.xml";
		AnalysisPwStatus analysisPwStatus = new AnalysisPwStatus();
		List<PwStatusObject> pwStatus = analysisPwStatus.analysisCommandsToPortConfigObject(commands, configXml);
		return pwStatus;
	}

	/**
	 * Tunnel状态信息
	 */
	public List<TunnelStatusObject> AnalysisTunnelStatus(byte[] commands) throws Exception {
		String configXml = "com/nms/drive/analysis/xmltool/file/status/TUNNEL状态配置块.xml";
		AnalysisTunnelStatus analysisTunnelStatus = new AnalysisTunnelStatus();
		List<TunnelStatusObject> tunnelStatus = analysisTunnelStatus.analysisCommandsToPortConfigObject(commands, configXml);
		return tunnelStatus;
	}

	/**
	 * 业务状态信息
	 */
	public List<VpwsStatusObject> AnalysisVpwsStatus(byte[] commands) throws Exception {
		String configXml = "com/nms/drive/analysis/xmltool/file/status/业务状态配置块.xml";
		AnalysisVPWSStatus analysisVPWSStatus = new AnalysisVPWSStatus();
		List<VpwsStatusObject> vpwsStatusList = analysisVPWSStatus.analysisCommandsToPortConfigObject(commands, configXml);
		return vpwsStatusList;
	}

	/**
	 * 环网保护状态信息
	 */
	public List<WrappingStatusObject> AnalysisWrappingStatus(byte[] commands) throws Exception {
		String configXml = "com/nms/drive/analysis/xmltool/file/status/环网保护状态配置块.xml";
		AnalysisWrappingStatus analysisWrappingStatus = new AnalysisWrappingStatus();
		List<WrappingStatusObject> wrappingStatusObjects = analysisWrappingStatus.analysisCommandsToPortConfigObject(commands, configXml);
		return wrappingStatusObjects;
	}
	
	/**
	 * pw保护状态信息
	 */
	public List<PwProtectStatusObject> AnalysisPwProtectStatus(byte[] commands) throws Exception {
		String configXml = "com/nms/drive/analysis/xmltool/file/status/PW保护状态配置块.xml";
		AnalysisPwProtectStatus analysisPwProtectStatus = new AnalysisPwProtectStatus();
		List<PwProtectStatusObject> pwProtectStatuss = analysisPwProtectStatus.analysisCommandsToPortConfigObject(commands, configXml);
		return pwProtectStatuss;
	}

	/**
	 * LAG端口聚合状态信息
	 */
	public List<LagStatusObject> AnalysisLagStatus(byte[] commands) throws Exception {
		String lagXml = "com/nms/drive/analysis/xmltool/file/status/Lag端口状态配置块.xml";
		String portXml = "com/nms/drive/analysis/xmltool/file/status/Lag端口状态配置块_SUB2.xml";
		AnalysisLagStatus analysisLagStatus = new AnalysisLagStatus();
		List<LagStatusObject> lagStatuss = analysisLagStatus.analysisCommandsToLagObject(commands, lagXml,portXml);
		return lagStatuss;
	}
	/**
	 *获得配置块头信息
	 * 
	 * @param identifier
	 * @param dataLength
	 * @param offset
	 * @return
	 */
	private byte[] dataBlockHead(int identifier, int dataLength, int offset) {
		byte[] dataBlockHead = new byte[25];

		// FH(ASCII传送,界面按ASCII显示)
		dataBlockHead[0] = (byte) CoderUtils.char2ASCII('D');
		dataBlockHead[1] = (byte) CoderUtils.char2ASCII('A');

		// 配置数据块1标识码
		byte[] identifierByte = CoderUtils.intToBytes(identifier);
		dataBlockHead[2] = identifierByte[3];

		// 配置数据块数据长度
		byte[] dataLengthByte = CoderUtils.intToBytes(dataLength);
		dataBlockHead[3] = dataLengthByte[0];
		dataBlockHead[4] = dataLengthByte[1];
		dataBlockHead[5] = dataLengthByte[2];
		dataBlockHead[6] = dataLengthByte[3];

		// 配置数据块数据偏移量
		byte[] offsetByte = CoderUtils.intToBytes(offset);
		dataBlockHead[7] = offsetByte[0];
		dataBlockHead[8] = offsetByte[1];
		dataBlockHead[9] = offsetByte[2];
		dataBlockHead[10] = offsetByte[3];

		// 压缩类型(00/01-FFH=不压缩/压缩类型)
		dataBlockHead[11] = 0x00;
		// 是否强制执行(00/01=非强制/强制
		dataBlockHead[12] = 0x00;

		// CRC
		dataBlockHead[13] = 0x00;
		dataBlockHead[14] = 0x00;
		dataBlockHead[15] = 0x00;
		dataBlockHead[16] = 0x00;

		// 备用
		dataBlockHead[17] = 0x00;
		dataBlockHead[18] = 0x00;
		dataBlockHead[19] = 0x00;
		dataBlockHead[20] = 0x00;
		dataBlockHead[21] = 0x00;
		dataBlockHead[22] = 0x00;
		dataBlockHead[23] = 0x00;
		dataBlockHead[24] = 0x00;
		return dataBlockHead;
	}

	/**
	 * 将以太网OAM对象转换成命令
	 */
	@Override
	public byte[] AnalysisEObjectToCommands(NEObject neObject, ETHOAMAllObject ObjectList) throws Exception {
		byte[] tunnalToCommand = new byte[0];

		AnalysisETHOamConfig analysisTUNNELTable = new AnalysisETHOamConfig();
		String configXml = "com/nms/drive/analysis/xmltool/file/以太网OAM(13H).xml";
		byte[] dataCommand = analysisTUNNELTable.analysisGlobalObjectToCommands(ObjectList, configXml);

		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(31, dataCommand.length, 0);

		tunnalToCommand = CoderUtils.arraycopy(tunnalToCommand, neHand);
		tunnalToCommand = CoderUtils.arraycopy(tunnalToCommand, controlPanel);
		tunnalToCommand = CoderUtils.arraycopy(tunnalToCommand, dataBlock);
		tunnalToCommand = CoderUtils.arraycopy(tunnalToCommand, dataCommand);

		return tunnalToCommand;
	}

	/**
	 * 解析命令生成QinQ对象
	 */
	public List<QinQObject> AnalysisCommandToQinQ(byte[] dataCommand) throws Exception {
		AnalysisQinQTable analysisQinQTable = new AnalysisQinQTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/QinQ表(21H).xml";
		List<QinQObject> qinqObjectList = analysisQinQTable.analysisCommandToObject(dataCommand, configXml);
		return qinqObjectList;
	}
	
	/**
	 * 解析qinq对象生命命令
	 */
	public byte[] AnalysisQinQToCommand(NEObject neObject, List<QinQObject> qinqObjectList) throws Exception {
		byte[] qinqToCommand = new byte[0];

		AnalysisQinQTable analysisQinQTable = new AnalysisQinQTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/QinQ表(21H).xml";
		byte[] dataCommand = analysisQinQTable.analysisObjectToCommand(qinqObjectList, configXml);

		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(33, dataCommand.length, 0);

		qinqToCommand = CoderUtils.arraycopy(qinqToCommand, neHand);
		qinqToCommand = CoderUtils.arraycopy(qinqToCommand, controlPanel);
		qinqToCommand = CoderUtils.arraycopy(qinqToCommand, dataBlock);
		qinqToCommand = CoderUtils.arraycopy(qinqToCommand, dataCommand);

		return qinqToCommand;
	}

	/**
	 * 解析命令生成以太网OAM对
	 * 
	 * @param dataCommand数据体命令
	 * @return
	 * @throws Exception
	 */
	public List<ETHOAMObject> AnalysisCommandToEthOam(byte[] dataCommand) throws Exception {
		AnalysisETHOamConfig analysisTUNNELTable = new AnalysisETHOamConfig();
		String configXml = "com/nms/drive/analysis/xmltool/file/以太网OAM(13H).xml";
		List<ETHOAMObject> pwObjectList = analysisTUNNELTable.analysisCommandToObject(dataCommand, configXml);
		return pwObjectList;
	}

	/**
	 * 解析黑名单mac对象生命命令
	 */
	public byte[] AnalysisMacManageToCommands(NEObject neObject, List<MacManageObject> macObjectList) throws Exception {
		byte[] macToCommand = new byte[0];

		AnalysisMacTable analysisMacTable = new AnalysisMacTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/MAC管理(23H).xml";
		byte[] dataCommand = analysisMacTable.analysisObjectToCommand(macObjectList, configXml);

		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(35, dataCommand.length, 0);

		macToCommand = CoderUtils.arraycopy(macToCommand, neHand);
		macToCommand = CoderUtils.arraycopy(macToCommand, controlPanel);
		macToCommand = CoderUtils.arraycopy(macToCommand, dataBlock);
		macToCommand = CoderUtils.arraycopy(macToCommand, dataCommand);

		return macToCommand;
	}

	/**
	 * 解析命令生成OamTraceHops对象
	 */
	public List<OamTraceHopsObject> AnalysisCommandToOamTraceHops(byte[] dataCommand) throws Exception {
		AnalysisEthOAMTraceQueryTable analysisEthOAMTraceQueryTable = new AnalysisEthOAMTraceQueryTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/以太网OAM Trace结果查询(31).xml";
		List<OamTraceHopsObject> oamTraceHopsList = analysisEthOAMTraceQueryTable.analysisCommandToObject(dataCommand, configXml);
		return oamTraceHopsList;
	}

	/**
	 * 解析命令生成OamPingFrame对象
	 */
	public List<OamPingFrameObject> AnalysisCommandToOamPingFrame(byte[] dataCommand) throws Exception {
		AnalysisEthOAMPingQueryTable analysisEthOAMPingQueryTable = new AnalysisEthOAMPingQueryTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/以太网OAM Ping结果查询(3E).xml";
		List<OamPingFrameObject> oamPingFrameList = analysisEthOAMPingQueryTable.analysisCommandToObject(dataCommand, configXml);
		return oamPingFrameList;
	}

	/**
	 * 解析命令生成接入以太网OAM对象
	 * 
	 * @param dataCommand数据体命令
	 * @return
	 * @throws Exception
	 */
	@Override
	public InsertLinkOamObject AnalysisCommandToInsertLinkOam(byte[] dataCommand) throws Exception {
		AnalysisInsertLinkOamTable analysisInsertLinkOamTable = null;
		String configXml = null;
		InsertLinkOamObject insertLinkOamInfo = null;
		try {
			insertLinkOamInfo = new InsertLinkOamObject();
			analysisInsertLinkOamTable = new AnalysisInsertLinkOamTable();
			configXml = "com/nms/drive/analysis/xmltool/file/接入链路以太网OAM.xml";
			insertLinkOamInfo = analysisInsertLinkOamTable.analysisCommandToObject(dataCommand, configXml);
		} catch (Exception e) {
			throw e;
		} finally {
			analysisInsertLinkOamTable = null;
			configXml = null;
		}
		return insertLinkOamInfo;
	}

	/**
	 * 网元所以状态信息
	 */
	public ActionObject AnalysisOamStatusTable(byte[] commands) throws Exception {
		AnalysisAllOamStatus analysisAllOamStatus = new AnalysisAllOamStatus();
		ActionObject actionObject = analysisAllOamStatus.analysisCommandToObject(commands);
		return actionObject;
	}

	/**
	 * 以太网环回
	 */
	@Override
	public byte[] AnalysisEthLoopObjectToCommands(NEObject neObject, EthLoopObject ethLoopObject) throws Exception {

		byte[] command = new byte[0];
		String configXml = "com/nms/drive/analysis/xmltool/file/以太网环回(23H).xml";
		byte[] dataCommand = new AnalysisEthLoopTable().analysisEthLoopObjectToCommands(ethLoopObject, configXml);
		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(38, dataCommand.length, 0);
		command = CoderUtils.arraycopy(command, neHand);
		command = CoderUtils.arraycopy(command, controlPanel);
		command = CoderUtils.arraycopy(command, dataBlock);
		command = CoderUtils.arraycopy(command, dataCommand);

		return command;
	}

	/**
	 *ACL配置管理
	 */
	@Override
	public byte[] AnalysisAclObjectToCommands(NEObject neObject, AclObject aclObject) throws Exception {

		byte[] command = new byte[0];
		String configXml = "com/nms/drive/analysis/xmltool/file/ACL配置块(22H).xml";
		//数据信息块
		byte[] dataCommand = new AnalysisAclTable().analysisAclToCommands(aclObject, configXml);
		// 获得NE头信息  5个字节
		byte[] neHand = neHand(neObject);
		//盘头信息 101个字节
		byte[] controlPanel = controlPanelHead(1, neObject);
		//获得配置块头信息 25个字节
		byte[] dataBlock = dataBlockHead(37, dataCommand.length, 0);
		
		command = CoderUtils.arraycopy(command, neHand);
		command = CoderUtils.arraycopy(command, controlPanel);
		command = CoderUtils.arraycopy(command, dataBlock);
		command = CoderUtils.arraycopy(command, dataCommand);
		
		return command;
	}

	/**
	 * 解析mac学习数目限制对象生成命令
	 */
	@Override
	public byte[] AnalysisMacLearningToCommands(NEObject neObject, List<MacLearningObject> macLearningObjectList) throws Exception {
		byte[] macToCommand = new byte[0];

		AnalysisMacLearningTable analysisMacTable = new AnalysisMacLearningTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/MAC学习数目限制管理(22H).xml";
		byte[] dataCommand = analysisMacTable.analysisObjectToCommand(macLearningObjectList, configXml);

		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(34, dataCommand.length, 0);

		macToCommand = CoderUtils.arraycopy(macToCommand, neHand);
		macToCommand = CoderUtils.arraycopy(macToCommand, controlPanel);
		macToCommand = CoderUtils.arraycopy(macToCommand, dataBlock);
		macToCommand = CoderUtils.arraycopy(macToCommand, dataCommand);

		return macToCommand;
	}

	@Override
	public byte[] AnalysisBlackWhiteMacToCommands(NEObject neObject, List<BlackWhiteMacObject> blackWhiteMacObjectList) throws Exception {

		byte[] command = new byte[0];
		String configXml = "com/nms/drive/analysis/xmltool/file/MAC地址的黑白名单(24H).xml";
		byte[] dataCommand = new AnalysisBlackWhiteMacTable().analysisObjectToCommand(blackWhiteMacObjectList, configXml);
		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(36, dataCommand.length, 0);

		command = CoderUtils.arraycopy(command, neHand);
		command = CoderUtils.arraycopy(command, controlPanel);
		command = CoderUtils.arraycopy(command, dataBlock);
		command = CoderUtils.arraycopy(command, dataCommand);

		return command;
	}

	/**
	 * 将命令码转换为相应的对象
	 * 
	 * @param dataCommand
	 *            dataCommand数据体命令
	 * @return OamLinkStatusInfo 对象
	 * @throws Exception
	 */
	@Override
	public OamLinkStatusInfoObject AnalysisCommandToOamLinkStatusInfo(byte[] dataCommand) throws Exception {

		AnalysisOamLinkStatusTable analysisOamLinkStatusTable = null;
		OamLinkStatusInfoObject oamLinkStatusInfoObject = null;
		String configXml = null;

		try {
			configXml = "com/nms/drive/analysis/xmltool/file/接入链路以太网OAM状态查询.xml";
			analysisOamLinkStatusTable = new AnalysisOamLinkStatusTable();
			oamLinkStatusInfoObject = analysisOamLinkStatusTable.analysisCommandToObject(dataCommand, configXml);

		} catch (Exception e) {
			throw e;
		} finally {
			analysisOamLinkStatusTable = null;
			configXml = null;
		}

		return oamLinkStatusInfoObject;
	}

	public OamMepInfoObject AnalysisCommandToOamMepInfoObject(byte[] dataCommand) throws Exception {

		AnalysisOamMepInfoObjectTable analysisOamMepInfoObjectTable = null;
		OamMepInfoObject oamMepInfoObject = null;
		String configXml = null;

		try {
			configXml = "com/nms/drive/analysis/xmltool/file/接入链路以太网oam对端MEP信息(57).xml";
			analysisOamMepInfoObjectTable = new AnalysisOamMepInfoObjectTable();
			oamMepInfoObject = analysisOamMepInfoObjectTable.analysisCommonToObject(dataCommand, configXml);

		} catch (Exception e) {
			throw e;
		} finally {
			analysisOamMepInfoObjectTable = null;
			configXml = null;
		}
		return oamMepInfoObject;
	}

	/**
	 * 查询软件摘要
	 * 
	 * @param dataCommand
	 * @return
	 * @throws Exception
	 */
	public List<UpgradeManageObject> AnalysisUpgradeManageObject(byte[] dataCommand) throws Exception {
		AnalysisUpgradeManageObject analysisOamMepInfoObjectTable = new AnalysisUpgradeManageObject();
		String configXml = "com/nms/drive/analysis/xmltool/file/软件摘要.xml";
		List<UpgradeManageObject> upgradeManageObjects = null;
		if (dataCommand.length > 100) {
			upgradeManageObjects = analysisOamMepInfoObjectTable.analysisUpgradeManager(dataCommand, configXml);
		}
		return upgradeManageObjects;
	}

	/**
	 * V35接口配置
	 * 
	 * @param neObject
	 * @param v35PortObject
	 * @return
	 * @throws Exception
	 */
	public byte[] AnalysisV35PortObjectToCommand(NEObject neObject, V35PortObject v35PortObject) throws Exception {
		byte[] elineToCommand = new byte[0];

		AnalysisV35PortTable analysisV35PortTable = new AnalysisV35PortTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/V35接口配置(26H).xml";
		byte[] dataCommand = analysisV35PortTable.analysisObjectToCommands(v35PortObject, configXml);
		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(39, dataCommand.length, 0);
		elineToCommand = CoderUtils.arraycopy(elineToCommand, neHand);
		elineToCommand = CoderUtils.arraycopy(elineToCommand, controlPanel);
		elineToCommand = CoderUtils.arraycopy(elineToCommand, dataBlock);
		elineToCommand = CoderUtils.arraycopy(elineToCommand, dataCommand);

		return elineToCommand;
	}

	/**
	 * V35接口配置
	 * 
	 * @param dataCommand
	 * @return
	 * @throws Exception
	 */
	public V35PortObject AnalysisCommandToV35PortObject(byte[] dataCommand) throws Exception {
		AnalysisV35PortTable analysisV35PortTable = new AnalysisV35PortTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/V35接口配置(26H).xml";
		V35PortObject v35PortObject = analysisV35PortTable.analysisCommandsToObject(dataCommand, configXml);
		return v35PortObject;
	}

	/**
	 * 解析智能风扇生成命令
	 * 
	 * @param neObject网元信息
	 * @return 数据体命令
	 * 
	 * @throws Exception
	 */
	@Override
	public byte[] AnalysisSmartFanToCommand(NEObject neObject, List<SmartFanObject> fanObjectList) throws Exception {
		byte[] fanCommand = new byte[0];
		AnalysisSmartFanTable analysisSmartFanTable = new AnalysisSmartFanTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/智能风扇配置(38H).xml";
		byte[] dataCommand = analysisSmartFanTable.analysisObjectToCommands(fanObjectList, configXml);
		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(56, dataCommand.length, 0);
		fanCommand = CoderUtils.arraycopy(fanCommand, neHand);
		fanCommand = CoderUtils.arraycopy(fanCommand, controlPanel);
		fanCommand = CoderUtils.arraycopy(fanCommand, dataBlock);
		fanCommand = CoderUtils.arraycopy(fanCommand, dataCommand);
		return fanCommand;
	}

	/**
	 * E1仿真状态信息
	 */
	public List<E1LegStatusObject> AnalysisE1LegStatus(byte[] commands) throws Exception {
		String configXml = "com/nms/drive/analysis/xmltool/file/status/E1仿真状态配置块.xml";
		AnalysisE1LegStatus analysisE1LegStatus = new AnalysisE1LegStatus();
		List<E1LegStatusObject> e1LegStatusList = analysisE1LegStatus.analysisCommandsToPortConfigObject(commands, configXml);
		return e1LegStatusList;
	}

	/**
	 * 智能风扇
	 * 
	 * @param dataCommand
	 * @return
	 * @throws Exception
	 */
	public List<SmartFanObject> AnalysisCommandToSmartFanObject(byte[] dataCommand) throws Exception {
		AnalysisSmartFanTable analysisSmartFanTable = new AnalysisSmartFanTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/智能风扇配置(38H).xml";
		List<SmartFanObject> fanObjList = analysisSmartFanTable.analysisCommandToObject(dataCommand, configXml);
		return fanObjList;
	}

	/**
	 * 查询相邻网元SN
	 * 
	 * @param dataCommand
	 * @return
	 * @throws Exception
	 */
	public List<NEObject> AnalysisCommandToSNObject(byte[] dataCommand) throws Exception {
		AnalysisNeObjectSNTable analysisNeObjectSNTable = new AnalysisNeObjectSNTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/相邻网元SN.xml";
		List<NEObject> neObjects = analysisNeObjectSNTable.analysisCommandInfoToNEObjectS(dataCommand, configXml);
		return neObjects;
	}

	/**
	 * 
	 * 以太网业务配置块
	 */
	public byte[] AnalysisEthServiceToCommand(NEObject neObject, EthServiceObject ethServiceObject) throws Exception {
		byte[] command = new byte[0];
		String configXml = "com/nms/drive/analysis/xmltool/file/以太网配置块(39H).xml";
		byte[] dataCommand = new AnalysisEthServciceTable().analysisAclToCommands(ethServiceObject, configXml);
		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(57, dataCommand.length, 0);
		command = CoderUtils.arraycopy(command, neHand);
		command = CoderUtils.arraycopy(command, controlPanel);
		command = CoderUtils.arraycopy(command, dataBlock);
		command = CoderUtils.arraycopy(command, dataCommand);
		return command;
	}

	@Override
	public List<EthServiceInfoObject> AnalysisCommandToEthService(byte[] dataCommand) throws Exception {
		AnalysisEthServciceTable analysisEthServciceTable = new AnalysisEthServciceTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/以太网配置块(39H).xml";
		List<EthServiceInfoObject> ethServiceObjList = analysisEthServciceTable.analysisCommandToObject(dataCommand, configXml);
		return ethServiceObjList;
	}

	@Override
	public byte[] AnalysisAlarmShieldObjectToCommand(NEObject neObject, AlarmShieldObject alarmShieldObject) throws Exception {
		byte[] command = new byte[0];
		byte[] dataCommand = null;
		int configVlaue = 0;// 协议对象的配置块值
		String configXml = "";
		// String configXml = "com/nms/drive/analysis/xmltool/file/告警屏蔽.xml";
		// String configXmlAlarmCode =
		// "com/nms/drive/analysis/xmltool/file/告警屏蔽_1.xml";
		// String configXmlLine =
		// "com/nms/drive/analysis/xmltool/file/告警线路屏蔽.xml";

		// 告警屏蔽 oxE1
		if (alarmShieldObject.getLineOrAlarmCode() == 1) {
			// 判断是通过线路确定告警码来处理 还是通过告警码来确认告警线路来屏蔽告警
			configVlaue = 225;
			if (alarmShieldObject.getShieldType() == 1) {
				configXml = "com/nms/drive/analysis/xmltool/file/告警屏蔽.xml";
			} else {
				configXml = "com/nms/drive/analysis/xmltool/file/告警屏蔽_1.xml";
				// dataCommand = new
				// AnalysisAlarmShieldTable().analysisAclToCommands(alarmShieldObject,
				// configXmlAlarmCode);
			}
		}// 线路屏蔽 0xE3
		else if (alarmShieldObject.getLineOrAlarmCode() == 2) {
			configVlaue = 227;
			configXml = "com/nms/drive/analysis/xmltool/file/告警线路屏蔽.xml";
			// dataCommand = new
			// AnalysisAlarmShieldTable().analysisAclToCommands(alarmShieldObject,
			// configXmlLine);
		}

		dataCommand = new AnalysisAlarmShieldTable().analysisAclToCommands(alarmShieldObject, configXml);

		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(configVlaue, dataCommand.length, 0);
		command = CoderUtils.arraycopy(command, neHand);
		command = CoderUtils.arraycopy(command, controlPanel);
		command = CoderUtils.arraycopy(command, dataBlock);
		command = CoderUtils.arraycopy(command, dataCommand);
		return command;

	}

	@Override
	public byte[] AnalysisL2cpObjectToCommand(NEObject neObject, L2CPinfoObject L2CPinfoObject) throws Exception {
		byte[] command = new byte[0];
		String configXml = "com/nms/drive/analysis/xmltool/file/L2CP功能(59).xml";
		byte[] dataCommand = new AnalysisL2CPServciceTable().analysisAclToCommands(L2CPinfoObject, configXml);
		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(59, dataCommand.length, 0);
		command = CoderUtils.arraycopy(command, neHand);
		command = CoderUtils.arraycopy(command, controlPanel);
		command = CoderUtils.arraycopy(command, dataBlock);
		command = CoderUtils.arraycopy(command, dataCommand);
		return command;
	}

	/**
	 * 端口qos配置
	 */
	@Override
	public byte[] AnalysisPortPriToCommand(NEObject neObject, PortObject portObject) throws Exception {
		byte[] command = new byte[0];
		String configXml = "com/nms/drive/analysis/xmltool/file/端口QOS配置(60).xml";
		byte[] dataCommand = new AnalysisPortPriTable().analysisPortPriObjectToCommands(portObject, configXml);
		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(60, dataCommand.length, 0);
		command = CoderUtils.arraycopy(command, neHand);
		command = CoderUtils.arraycopy(command, controlPanel);
		command = CoderUtils.arraycopy(command, dataBlock);
		command = CoderUtils.arraycopy(command, dataCommand);
		return command;
	}

	@Override
	public PortObject analysisCommandToPortPri(byte[] command) throws Exception {
		String configXml = "com/nms/drive/analysis/xmltool/file/端口QOS配置(60)_SUB.xml";
		PortObject portObject = new AnalysisPortPriTable().analysisCommandToObject(command, configXml);
		return portObject;
	}

	@Override
	public EthServiceObject AnalysisCommandToPortDiscard(byte[] dataCommand) throws Exception {
		AnalysisPortDiscardTable analysisPortDiscardTable = new AnalysisPortDiscardTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/端口丢弃流(3dH).xml";
		EthServiceObject ethServiceObj = analysisPortDiscardTable.analysisCommandToObject(dataCommand, configXml);
		return ethServiceObj;

	}

	@Override
	public byte[] AnalysisPortDiscardToCommand(NEObject neObject, EthServiceObject ethServiceObject) throws Exception {
		byte[] command = new byte[0];
		String configXml = "com/nms/drive/analysis/xmltool/file/端口丢弃流(3dH).xml";
		byte[] dataCommand = new AnalysisPortDiscardTable().analysisObjectToCommands(ethServiceObject, configXml);
		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(61, dataCommand.length, 0);
		command = CoderUtils.arraycopy(command, neHand);
		command = CoderUtils.arraycopy(command, controlPanel);
		command = CoderUtils.arraycopy(command, dataBlock);
		command = CoderUtils.arraycopy(command, dataCommand);
		return command;
	}
	/**
	 * 解析SecondMacStudyObject为命令
	 * 
	 * @param neObject
	 * @param timeSynchronizeObject
	 * @return
	 * @throws Exception
	 */
	public byte[] AnalysisSecondMacManageToCommands(NEObject neObject, List<SecondMacStudyObject> secondMacStudyObjectList) throws Exception {

		byte[] command = new byte[0];
		String configXml = "com/nms/drive/analysis/xmltool/file/二层静态MAC地址学习配置块(3fH).xml";
		byte[] dataCommand = new AnalysisSecondMacTable().analysisSecondMacStudyObjectToCommands(secondMacStudyObjectList, configXml);
		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(63, dataCommand.length, 0);
		command = CoderUtils.arraycopy(command, neHand);
		command = CoderUtils.arraycopy(command, controlPanel);
		command = CoderUtils.arraycopy(command, dataBlock);
		command = CoderUtils.arraycopy(command, dataCommand);
		return command;
	}

	@Override
	public byte[] AnalysisPort2layerToCommand(NEObject neObject, List<Port2LayerObject> port2LayerObjectList) {
		byte[] command = new byte[0];
		String configXml = "com/nms/drive/analysis/xmltool/file/端口二层属性(3eH).xml";
		byte[] dataCommand = new AnalysisPort2LayerAttrTable().analysisObjectToCommands(port2LayerObjectList, configXml);
		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(62, dataCommand.length, 0);
		command = CoderUtils.arraycopy(command, neHand);
		command = CoderUtils.arraycopy(command, controlPanel);
		command = CoderUtils.arraycopy(command, dataBlock);
		command = CoderUtils.arraycopy(command, dataCommand);
		return command;
	}

	@Override
	public List<Port2LayerObject> analysisCommandToPort2LayerObj(byte[] command) throws Exception {
		AnalysisPort2LayerAttrTable analysisPortTable = new AnalysisPort2LayerAttrTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/端口二层属性(3eH).xml";
		List<Port2LayerObject> objList = analysisPortTable.analysisCommandToObject(command, configXml);
		return objList;
	}

	@Override
	public byte[] analysisTimeSynctoCommand(NEObject neObject,TimeSyncObject timesyncobj) throws Exception {
		byte[] command = new byte[0];
		String configXml = "com/nms/drive/analysis/xmltool/file/时间同步配置块(1CH).xml";
		byte[] dataCommand = new AnalysisTimeSyncTable().analysisTimeSyncObjectToCommands(timesyncobj, configXml);

		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(28, dataCommand.length, 0);

		command = CoderUtils.arraycopy(command, neHand);
		command = CoderUtils.arraycopy(command, controlPanel);
		command = CoderUtils.arraycopy(command, dataBlock);
		command = CoderUtils.arraycopy(command, dataCommand);

		return command;
	}

	/**
	 * 解析Ccc生成命令
	 * 
	 * @param neObject网元信息
	 * @param CccObject隧道对象
	 * @return 数据体命令
	 * 
	 * @throws Exception
	 */
	public byte[] AnalysisCccToCommand(NEObject neObject, List<CccObject> cccObjectList) throws Exception {
		byte[] cccToCommand = new byte[0];

		AnalysisCccTable analysisCccTable = new AnalysisCccTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/CCC(40H).xml";
		byte[] dataCommand = analysisCccTable.analysisCccToCommand(cccObjectList, configXml);
		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(64, dataCommand.length, 0);
		cccToCommand = CoderUtils.arraycopy(cccToCommand, neHand);
		cccToCommand = CoderUtils.arraycopy(cccToCommand, controlPanel);
		cccToCommand = CoderUtils.arraycopy(cccToCommand, dataBlock);
		cccToCommand = CoderUtils.arraycopy(cccToCommand, dataCommand);

		return cccToCommand;
	}

	

	/**
	 * 解析命令生成Ccc对象
	 * 
	 * @param dataCommand数据体命令
	 * @return ELineObject隧道对象
	 * @throws Exception
	 */
	public List<CccObject> AnalysisCommandToCcc(byte[] dataCommand) throws Exception {
		AnalysisCccTable analysisCccTable = new AnalysisCccTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/CCC(40H).xml";
		List<CccObject> cccObjectList = analysisCccTable.analysisCommandsToCccObject(dataCommand, configXml);
		return cccObjectList;
	}
	/**
	 * ptp端口状态信息
	 */
	public List<PtpPortStatusObject> AnalysisPtpPortStatus(byte[] commands) throws Exception {
		String configXml = "com/nms/drive/analysis/xmltool/file/status/PTP端口状态配置块.xml";
		AnalysisPtpPortStatus analysisPtpPortStatus = new AnalysisPtpPortStatus();
		List<PtpPortStatusObject> ptpPortStatuss = analysisPtpPortStatus.analysisCommandsToPtpPortConfigObject(commands, configXml);
		return ptpPortStatuss;
	}
	/**
	 * ptp基本信息
	 */
	public List<PtpBasicStatusObject> AnalysisPtpBasicStatus(byte[] commands) throws Exception {
		String configXml = "com/nms/drive/analysis/xmltool/file/status/PTP基本状态配置块.xml";
		AnalysisPtpBasicStatus analysisPtpBasicStatus = new AnalysisPtpBasicStatus();
		List<PtpBasicStatusObject> ptpBasicStatuss = analysisPtpBasicStatus.analysisCommandsToPtpBasicConfigObject(commands, configXml);
		return ptpBasicStatuss;
	}
	
	
	/**
	 * 解析bfd生成命令
	 * 
	 * @param neObject网元信息
	 * @param bfdObject隧道对象
	 * @return 数据体命令
	 * 
	 * @throws Exception
	 */
	public byte[] AnalysisBfdToCommand(NEObject neObject, List<BfdObject> bfdObjectList) throws Exception {
		byte[] bfdToCommand = new byte[0];

		AnalysisBfdTable analysisBfdTable = new AnalysisBfdTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/BFD配置块(41H).xml";
		byte[] dataCommand = analysisBfdTable.analysisBfdObjectToCommands(bfdObjectList, configXml);
		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(65, dataCommand.length, 0);
		bfdToCommand = CoderUtils.arraycopy(bfdToCommand, neHand);
		bfdToCommand = CoderUtils.arraycopy(bfdToCommand, controlPanel);
		bfdToCommand = CoderUtils.arraycopy(bfdToCommand, dataBlock);
		bfdToCommand = CoderUtils.arraycopy(bfdToCommand, dataCommand);

		return bfdToCommand;
	}

	
	/**
	 * 解析命令生成bfd对象
	 * 
	 * @param dataCommand数据体命令
	 * @return ELineObject隧道对象
	 * @throws Exception
	 */
	public List<BfdObject> AnalysisCommandToBfd(byte[] dataCommand) throws Exception {
		AnalysisBfdTable analysisBfdTable = new AnalysisBfdTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/BFD配置块(41H)_sub.xml";
		List<BfdObject> bfdObjectList = analysisBfdTable.analysisCommandsToBfdObject(dataCommand, configXml);
		return bfdObjectList;
	}
	
	
	/**
	 * bfd状态基本信息
	 */
	public List<BfdStatusObject> AnalysisBfdStatus(byte[] commands) throws Exception {
		String configXml = "com/nms/drive/analysis/xmltool/file/status/BFD状态配置块.xml";
		AnalysisBfdStatus analysisbfdStatus = new AnalysisBfdStatus();
		List<BfdStatusObject> bfdStatuss = analysisbfdStatus.analysisCommandsToBfdConfigObject(commands, configXml);
		return bfdStatuss;
	}

	/**
	 * arp配置
	 * 解析对象为命令
	 */
	@Override
	public byte[] AnalysisARPToCommand(NEObject neObject, List<ARPObject> arpObjectList) throws Exception {
		byte[] command = new byte[0];
		AnalysisARPTable analysisBfdTable = new AnalysisARPTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/ARP配置块(42H).xml";
		byte[] dataCommand = analysisBfdTable.analysisARPObjectToCommands(arpObjectList, configXml);
		byte[] neHand = neHand(neObject);
		byte[] controlPanel = controlPanelHead(1, neObject);
		byte[] dataBlock = dataBlockHead(66, dataCommand.length, 0);
		command = CoderUtils.arraycopy(command, neHand);
		command = CoderUtils.arraycopy(command, controlPanel);
		command = CoderUtils.arraycopy(command, dataBlock);
		command = CoderUtils.arraycopy(command, dataCommand);

		return command;
	}
	
	/**
	 * 解析命令生成对象
	 * @param dataCommand数据体命令
	 * @throws Exception
	 */
	public List<ARPObject> analysisCommandsToARPObject(byte[] dataCommand) throws Exception {
		AnalysisARPTable analysisARPTable = new AnalysisARPTable();
		String configXml = "com/nms/drive/analysis/xmltool/file/ARP配置块(42H)_sub.xml";
		List<ARPObject> aRPObjectList = analysisARPTable.analysisCommandsToARPObject(dataCommand, configXml);
		return aRPObjectList;
	}
	
	/**
	 * arp状态基本信息
	 */
	public List<ARPObject> AnalysisArpStatus(byte[] dataCommand) throws Exception {
		String configXml = "com/nms/drive/analysis/xmltool/file/ARP配置块(42H)_sub.xml";
		AnalysisARPStatus analysisARPTable = new AnalysisARPStatus();
		List<ARPObject> aRPObjectList = analysisARPTable.analysisCommandsToConfigObject(dataCommand, configXml);
		return aRPObjectList;
	}

}