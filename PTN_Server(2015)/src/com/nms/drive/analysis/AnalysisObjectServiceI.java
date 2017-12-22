package com.nms.drive.analysis;

import java.util.List;

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
import com.nms.drive.service.bean.status.InsertLinkOamObject;
import com.nms.drive.service.bean.status.OamLinkStatusInfoObject;
import com.nms.drive.service.bean.status.OamMepInfoObject;
import com.nms.drive.service.bean.status.OamPingFrameObject;
import com.nms.drive.service.bean.status.OamTraceHopsObject;
import com.nms.drive.service.bean.status.PwStatusObject;
import com.nms.drive.service.bean.status.TunnelStatusObject;
import com.nms.drive.service.bean.status.VpwsStatusObject;
import com.nms.service.bean.ActionObject;

public interface AnalysisObjectServiceI {

	/**
	 
	 * 解析Tunnel生成带tunnelObject属性的byte[]	 
	 * @param neObject:网元信息
	 * @param tunnelObjectList:隧道对象
	 * @return 数据体命令
	 * @throws Exception
	 */
	 
	public byte[] AnalysisTunnalToCommand(NEObject neObject, List<TunnelObject> tunnelObjectList) throws Exception;

	/**
	 * 解析命令生成Tunnel对象
	 * 
	 * @param dataCommand数据体命令
	 * @return tunnelObject隧道对象
	 * @throws Exception
	 */
	public List<TunnelObject> AnalysisCommandToTunnal(byte[] dataCommand) throws Exception;

	/**
	 * 解析ELine生成命令
	 * 
	 * @param neObject网元信息
	 * @param ELineObject隧道对象
	 * @return 数据体命令
	 * 
	 * @throws Exception
	 */
	public byte[] AnalysisElineToCommand(NEObject neObject, List<ELineObject> elineObjectList) throws Exception;

	/**
	 * 解析命令生成ELine对象
	 * 
	 * @param dataCommand数据体命令
	 * @return ELineObject隧道对象
	 * @throws Exception
	 */
	public List<ELineObject> AnalysisCommandToELine(byte[] dataCommand) throws Exception;

	/**
	 * 解析Pw生成命令
	 * 
	 * @param neObject网元信息
	 * @param PwObject隧道对象
	 * @return 数据体命令
	 * @throws Exception
	 * 
	 * @throws Exception
	 */
	public byte[] AnalysisPwToCommand(NEObject neObject, List<PwObject> pwObjectList) throws Exception;

	/**
	 * 解析命令生成Pw对象
	 * 
	 * @param dataCommand数据体命令
	 * @return PwObject隧道对象
	 * @throws Exception
	 */
	public List<PwObject> AnalysisCommandToPw(byte[] dataCommand) throws Exception;


	/**
	 * 解析ELan生成命令
	 * 
	 * @param neObject
	 * @param proObjectList
	 * @return
	 * @throws Exception
	 */
	public byte[] AnalysisELanToCommand(NEObject neObject, List<ELanObject> elanObjectList) throws Exception;

	/**
	 * 解析命令生成ETree对象
	 * 
	 * @param commands
	 * @return
	 * @throws Exception
	 */
	public byte[] AnalysisETreeToCommand(NEObject neObject, List<ETreeObject> treeObjectList) throws Exception;

	/**
	 * 解析命令生成ETree对象
	 * 
	 * @param commands
	 * @return
	 * @throws Exception
	 */
	
	public List<ETreeObject> analysisCommandToETree(byte[] commands) throws Exception;

	/**
	 * 解析ethOAM生成命令
	 * 
	 * @param neObject
	 * @param ethOAM
	 * @return
	 * @throws Exception
	 */
	public byte[] AnalysisETHOAMToCommand(NEObject neObject, ETHOAM ethOAM) throws Exception;

	/**
	 * 解析命令生成ethOAM对象
	 * 
	 * @param commands
	 * @return
	 * @throws Exception
	 */
	public ETHOAM analysisCommandToETHOAM(byte[] commands) throws Exception;

	/**
	 * 根据e1Object生成命令
	 * 
	 * @param neObject
	 * @param e1Object
	 * @return
	 * @throws Exception
	 */
	public byte[] AnalysisE1ToCommand(NEObject neObject, E1Object e1Object) throws Exception;

	/**
	 * 根据命令生成E1Object对象
	 * 
	 * @param commands
	 * @return
	 * @throws Exception
	 */
	public E1Object AnalysisCommandsToE1Object(byte[] commands) throws Exception;

	/**
	 * 根据命令生成PerformanceObj对象
	 * 
	 * @param commands
	 * @return
	 * @throws Exception
	 */
	public PerformanceObject AnalysisCommadsToPerformanceObjectBySite(byte[] commands) throws Exception;

	/**
	 * 所有告警 根据命令生成AlarmObject对象
	 */
	public AlarmObject AnalysisCommadsToAllAlarm(byte[] commands) throws Exception;

	/**
	 * 某盘告警 根据命令生成AlarmObject对象
	 */
	public AlarmObject AnalysisCommadsToPlateAlarm(byte[] commands) throws Exception;

	/**
	 * 根据LSP保护对象生成命令
	 * 
	 * @param neObject
	 * @param tunnelProtectionList
	 * @return
	 * @throws Exception
	 */
	public byte[] AnalysisLSPProtectionToCommand(NEObject neObject, List<TunnelProtection> tunnelProtectionList) throws Exception;

	/**
	 * 根据命令生成LSP保护对象
	 * 
	 * @param commands
	 * @return
	 * @throws Exception
	 */
	public List<TunnelProtection> analysisCommandToLSPProtection(byte[] commands) throws Exception;

	/**
	 * 解析TMPOAMBugControl生成命令
	 * 
	 * @param neObject
	 * @param tmpoam
	 * @return
	 * @throws Exception
	 */
	public byte[] AnalysisTMPOAMBugControlToCommand(NEObject neObject, TMPOAMBugControlObject tmpoam) throws Exception;

	/**
	 * 解析命令生成TMPOAMBugControl
	 * 
	 * @param commands
	 * @return
	 * @throws Exception
	 */
	public TMPOAMBugControlObject analysisCommandToTMPOAMBugControl(byte[] commands) throws Exception;

	/**
	 * 解析PwQueueAndBufferManage生成命令
	 * 
	 * @param neObject
	 * @param pwManage
	 * @return
	 * @throws Exception
	 */
	public byte[] AnalysisPwQueueAndBufferManageToCommand(NEObject neObject, PwQueueAndBufferManage pwManage) throws Exception;

	/**
	 * 解析命令生成PwQueueAndBufferManage
	 * 
	 * @param commands
	 * @return
	 * @throws Exception
	 */
	public PwQueueAndBufferManage AnalysisCommandToPwQueueAndBufferManage(byte[] commands) throws Exception;

	/**
	 * 解析TMCOAMBugControl生成命令
	 * 
	 * @param neObject
	 * @param tmpoam
	 * @return
	 * @throws Exception
	 */
	public byte[] AnalysisTMCOAMBugControlToCommand(NEObject neObject, TMCOAMBugControlObject tmcoam) throws Exception;

	/**
	 * 解析命令生成TMCOAMBugControl
	 * 
	 * @param commands
	 * @return
	 * @throws Exception
	 */
	public TMCOAMBugControlObject analysisCommandToTMCOAMBugControl(byte[] commands) throws Exception;

	/**
	 * 解析端口聚合 TMCOAMBugControl 对象生成命令
	 * 
	 * @param neObject
	 * @param portLAG
	 * @return
	 * @throws Exception
	 */
	public byte[] AnalysisPortLAGToCommand(NEObject neObject, List<PortLAGObject> portLAG) throws Exception;

	/**
	 * 解析命令生成端口聚合 PortLAG 对象
	 * 
	 * @param commans
	 * @return
	 * @throws Exception
	 */
	public List<PortLAGObject> AnalysisCommandToPortLAG(byte[] commans) throws Exception;

	/**
	 * 解析List<TMCTunnelProtectObject>集合为命令
	 * 
	 * @param neObject
	 * @param tmcTunnelProtectObjList
	 * @return
	 * @throws Exception
	 */
	public byte[] AnalysisTMCTunnelProtectTocommands(NEObject neObject, List<TMCTunnelProtectObject> tmcTunnelProtectObjList) throws Exception;

	/**
	 * 解析命令为List<TMCTunnelProtectObject>集合
	 * 
	 * @param neObject
	 * @param tmcTunnelProtectObjList
	 * @return
	 * @throws Exception
	 */
	public List<TMCTunnelProtectObject> AnalysisCommandsToTMCTunnelProtectObject(byte[] commands) throws Exception;

	/**
	 * 解析timeSynchronizeObject为命令
	 * 
	 * @param neObject
	 * @param timeSynchronizeObject
	 * @return
	 * @throws Exception
	 */
	public byte[] AnalysisTimeSynchronizeObjectToCommands(NEObject neObject, TimeSynchronizeObject timeSynchronizeObject) throws Exception;

	/**
	 * 解析命令为timeSynchronizeObject
	 * 
	 * @param neObject
	 * @param timeSynchronizeObject
	 * @return
	 * @throws Exception
	 */
	public TimeSynchronizeObject AnalysisCommandsToTimeSynchronizeObject(byte[] commands) throws Exception;

	/**
	 * 解析TMSOAMObject对象为命令
	 * 
	 * @param neObject
	 * @param tmsOamObject
	 * @return
	 * @throws Exception
	 */
	public byte[] AnalysisTMSOAMObjectToCommands(NEObject neObject, TMSOAMObject tmsOamObject) throws Exception;

	/**
	 * 解析命令为TMSOAMObject对象
	 * 
	 * @param commands
	 * @return
	 * @throws Exception
	 */
	public TMSOAMObject AnalysisCommandsToTMSOAMObject(byte[] commands) throws Exception;

	
	/**
	 * 解析PHBToEXP对象为命令
	 */
	public byte[] analysisPHBToCommand(NEObject neObject,List<PHBToEXPObject> phbToEXP)throws Exception;
	
	/**
	 * 解析命令为PHBToEXP对象
	 */
	public List<PHBToEXPObject> analysisCommandToPHB(byte[] commans)throws Exception;

	/**
	 * 解析命令生成TMSOAMBugControl
	 * 
	 * @param commands
	 * @return
	 * @throws Exception
	 */
	public byte[] AnalysisTMSOAMBugControlToCommand(NEObject neObject, TMSOAMBugControlObject tmsoam) throws Exception;

	/**
	 * 解析TMSOAMBugControl 对象生成命令
	 * 
	 * @param neObject
	 * @param portLAG
	 * @return
	 * @throws Exception
	 */
	public TMSOAMBugControlObject analysisCommandToTMSOAMBugControl(byte[] commands) throws Exception;
	
	/**
	 * 解析EXPToPHB 对象生成命令
	 */
	public byte[] analysisEXPToCommand(NEObject neObject,List<EXPToPHBObject> expToPHBObject)throws Exception;
	
	/**
	 * 解析命令生成对象 EXPToPHB
	 */
	public List<EXPToPHBObject> analysisCommandToEXPObject(byte[] commands)throws Exception;
	
	/**
	 * 解析命令生成seniorPortConfigObjectlist
	 * 
	 * @param commands
	 * @return
	 * @throws Exception
	 */
	public byte[] AnalysisPortSeniorConfigCommandToCommands(NEObject neObject, List<PortSeniorConfig> seniorPortConfigObjectlist) throws Exception;

	/**
	 * 解析PortSeniorConfig 对象生成命令
	 * 
	 * @param neObject
	 * @param SeniorPortConfig
	 * @return
	 * 
	 */
	public PortSeniorConfig AnalysisCommandsToPortSeniorConfig(byte[] commands) throws Exception;
	
	/**
	 * 解析 ETHLinkOAM 对象 生成命令
	 */
	public byte[] analysisETHLinkOAMToCommand(NEObject neObject,List<ETHLinkOAMObject> ethLinkOAMList)throws Exception;
	/**
	 * 解析命令生成 ETHLinkOAM 对象
	 */
	public List<ETHLinkOAMObject> analysisCommandToETHLinkOAM(byte[] command)throws Exception;
	
	/**
	 * 解析环网保护 生成命令
	 */
	public byte[] analysisRoundPToCommand(NEObject neObject,List<RoundProtectionObject> roundP_List)throws Exception;
	/**
	 * 解析命令生成环网保护对象
	 */
	public List<RoundProtectionObject> analysisCommandToRoundP(byte[] command)throws Exception;

	/**
	 * IGMP SNOOPING配置 生成命令
	 */
	public byte[] analysisIGMPToCommand(NEObject neObject,List<IGMPSNOOPINGObject> igmpS_List)throws Exception;
	/**
	 * 命令生成 IGMP SNOOPING配置 对象
	 */
	public List<IGMPSNOOPINGObject> analysisCommandToIGMP(byte[] command)throws Exception;

	
	/**
	 * 静态组播(0CH) 对象 生成 命令
	 */
	public byte[] analysisGroupSpreadToCommand(NEObject neObject,List<GroupSpreadObject> groupSpreadObject)throws Exception;
	/**
	 * 命令 生成 静态组播(0CH) 对象
	 */
	public List<GroupSpreadObject> analysisCommandToGroupSpread(byte[] command)throws Exception;
	/**
	 * 全局配置块(03H) 对象 生成 命令
	 */
	public byte[] analysisGlpbalToCommand(NEObject neObject,GlobalObject globalObject)throws Exception;
	/**
	 * 命令 生成 全局配置块(03H) 对象
	 */
	public GlobalObject analysisCommandToObject(byte[] command)throws Exception;

	/**
	 * 单卡基本信息 对象 生成 命令
	 */
	public byte[] analysisSingleToCommand(NEObject neObject,SingleObject singleObject)throws Exception;
	
	/**
	 * 命令 生成 单卡基本信息 对象 
	 */
	public SingleObject analysisCommandToSingleObject(byte[] command)throws Exception;
	
	/**
	 * 静态单播(0BH) 对象 生成命令
	 */
	public byte[] analysisStaticUToCommand(NEObject neObject,List<StaticUnicastObject> staticUnicastObject)throws Exception;
	/**
	 * 命令 生成 静态单播(0BH) 对象
	 */
	public List<StaticUnicastObject> analysisCommandToStaticU(byte[] command)throws Exception;




	/**
	 * 解析ClockObject对象为命令
	 * 
	 * @param neObject
	 * @param clockObject
	 * @return
	 * @throws Exception
	 */
	public byte[] AnalysisClockObjectToCommands(NEObject neObject, ClockObject clockObject) throws Exception;

	/**
	 * 解析命令为ClockObject对象
	 * 
	 * @param commands
	 * @return
	 * @throws Exception
	 */
	public ClockObject AnalysisCommandsToClockObject(byte[] commands) throws Exception;

	/**
	 * 解析MangementObject对象为命令
	 * @param managementObject
	 * @return commands
	 * @throws Exception
	 */
	public byte[] AnalysisManagementObjectToCommands(NEObject neObject,ManagementObject managementObject)throws Exception;

	/**
	 * 解析命令为Response对象
	 * @param commands
	 * @return ResponseObject
	 * @throws Exception
	 */
	public ResponseObject AnalysisCommandsToResponseObject(byte[] commands)throws Exception;
	
	/**
	 * 根据字节数组生成单盘信息对象
	 * @param command
	 * @return SinglePan
	 * @throws Exception
	 */
	public SinglePan AnalysisSinglePanAutoTable(byte[] commands)throws Exception;
	/**
	 * 根据对象生成字节数组
	 * @param responsePan
	 * @param configXml
	 * @return
	 * @throws Exception
	 */
	public byte[] AnalysisResponsePanTable(NEObject neObject,ResponsePan responsePan)throws Exception;
	/**
	 * 所有报告上来的告警 根据命令生成AlarmObject对象
	 */
	public AlarmObject AnalysisCommadsToAllReportAlarm(byte[] commands) throws Exception;
	/**
	 * 解析SoftwareUpdate生成命令
	 */
	public byte[] AnalysisSoftwareUpdateObjectToCommands(NEObject neObject,SoftwareUpdate softwareUpdate) throws Exception;
	/**
	 * 查询所有业务生成Object对象
	 */
	public ActionObject AnalysisCommadsToAllBusiness(byte[] commands) throws Exception;
	
	public byte[] AnalysisPortConfigToCommands(NEObject neObject,PortObject protObject)throws Exception;
	/**
	 * 查询所有业务生成Object对象
	 */
	public NEObject AnalysisCommadsToSiteAttribute(byte[] commands) throws Exception;
	/**
	 *解析命令生成ProtObject
	 * @param commands
	 * @return
	 * @throws Exception
	 */
	public PortObject analysisPortConfigObjectToCommands(byte[] commands) throws Exception;
	/**
	 * 查询状态
	 */
	public ActionObject AnalysisSiteStatusTable(byte[] commands) throws Exception;
	
/**
 *以太网OAM 
 */
	public byte[] AnalysisEObjectToCommands(NEObject neObject, ETHOAMAllObject ethoamAllObject) throws Exception;
	
	/**
	 * 多段PW
	 * @param neObject
	 * @param msPwObjects
	 * @return
	 * @throws Exception
	 */
	public byte[] AnalysisMsPwToCommand(NEObject neObject, List<MsPwObject> msPwObjects) throws Exception ;
	/**
	 * PW状态信息
	 */
	public List<PwStatusObject> AnalysisPwStatus(byte[] commands) throws Exception;
	/**
	 * Tunnel状态信息
	 */
	public List<TunnelStatusObject> AnalysisTunnelStatus(byte[] commands) throws Exception;
	/**
	 * 业务状态信息
	 */
	public List<VpwsStatusObject> AnalysisVpwsStatus(byte[] commands) throws Exception;
	
	/**
	 * 解析Qinq生成命令
	 * 
	 * @param neObject网元信息
	 * @param tunnelObject隧道对象
	 * @return 数据体命令
	 * 
	 * @throws Exception
	 */
	public byte[] AnalysisQinQToCommand(NEObject neObject, List<QinQObject> qinqObjectList) throws Exception;

	/**
	 * 解析命令生成Qinq对象
	 * 
	 * @param dataCommand数据体命令
	 * @return tunnelObject隧道对象
	 * @throws Exception
	 */
	public List<QinQObject> AnalysisCommandToQinQ(byte[] dataCommand) throws Exception;

	/**
	 *性能门限配置块 命令
	 */
	public byte[] AnalysisPmLimiteObjectToCommands(NEObject neObject,PmValueLimiteObject pmValueLimiteObject)throws Exception;
	
	/**
	 * 解析黑名单mac对象生成命令
	 * 
	 * @param macManageObjectList
	 * @return dataCommand数据体命令
	 * @throws Exception
	 */
	public byte[] AnalysisMacManageToCommands(NEObject neObject, List<MacManageObject> macManageObjectList) throws Exception;

	/**
	 * 解析命令生成OamTraceHops对象
	 */
	public List<OamTraceHopsObject> AnalysisCommandToOamTraceHops(byte[] dataCommand)throws Exception;
	
	/**
	 * 解析命令生成OamPingFrame对象
	 */
	public List<OamPingFrameObject> AnalysisCommandToOamPingFrame(byte[] dataCommand)throws Exception;
	/**
	 * 接入链路以太网oam查询
	 */
	public InsertLinkOamObject AnalysisCommandToInsertLinkOam(byte[] dataCommand) throws Exception;
	
	/**
	 * 
	 * 以太网环回(对象 生成 命令
	 */
	public byte[] AnalysisEthLoopObjectToCommands(NEObject neObject,EthLoopObject ethLoopObject) throws Exception;
	
	/**
	 * 
	 * ACL配置管理(对象 生成 命令
	 */
	public byte[] AnalysisAclObjectToCommands(NEObject neObject,AclObject aclObject) throws Exception;
	
	/**
	 * 解析mac学习数目限制对象生成命令
	 * 
	 * @param macLearningObjectList
	 * @return dataCommand数据体命令
	 * @throws Exception
	 */
	public byte[] AnalysisMacLearningToCommands(NEObject neObject, List<MacLearningObject> macLearningObjectList) throws Exception;
	

	/**
	 *将黑白mac名单对象生成命令
	 * 
	 * @param blackWhiteMacObject
	 * @return dataCommand数据体命令
	 * @throws Exception
	 */
	public byte[] AnalysisBlackWhiteMacToCommands(NEObject neObject,List<BlackWhiteMacObject> blackWhiteMacObject) throws Exception;
	
	/**
	 * 将命令码转换为相应的对象
	 * @param dataCommand 
	 *         dataCommand数据体命令
	 * @return  OamLinkStatusInfo 对象
	 * @throws Exception
	 */
	public OamLinkStatusInfoObject AnalysisCommandToOamLinkStatusInfo(byte[] dataCommand) throws Exception;
	
	public OamMepInfoObject  AnalysisCommandToOamMepInfoObject(byte[] dataCommand) throws Exception;
	/**
	 * 查询软件摘要
	 * @param dataCommand
	 * @return
	 * @throws Exception
	 */
	public List<UpgradeManageObject> AnalysisUpgradeManageObject(byte[] dataCommand) throws Exception;
	/**
	 * V35接口配置
	 * @param neObject
	 * @param v35PortObject
	 * @return
	 * @throws Exception
	 */
	public byte[] AnalysisV35PortObjectToCommand(NEObject neObject, V35PortObject v35PortObject) throws Exception;
	/**
	 * V35接口配置
	 * @param dataCommand
	 * @return
	 * @throws Exception
	 */
	public V35PortObject AnalysisCommandToV35PortObject(byte[] dataCommand) throws Exception;
	/**
	 * 解析智能风扇生成命令
	 * 
	 * @param neObject网元信息
	 * @return 数据体命令
	 * 
	 * @throws Exception
	 */
	public byte[] AnalysisSmartFanToCommand(NEObject neObject, List<SmartFanObject> fanObjectList) throws Exception;
	/**
	 * 解析命令生成风扇对象
	 */
	public List<SmartFanObject> AnalysisCommandToSmartFanObject(byte[] dataCommand) throws Exception;
	/**
	 * 查询相邻网元SN
	 * @param dataCommand
	 * @return
	 * @throws Exception
	 */
	public List<NEObject> AnalysisCommandToSNObject(byte[] dataCommand) throws Exception ;
	
	/**
	 * 解析以太网业务配置块
	 * 
	 * @param neObject网元信息
	 * @return 数据体命令
	 * 
	 * @throws Exception
	 */
	public byte[] AnalysisEthServiceToCommand(NEObject neObject,EthServiceObject EthServiceObject) throws Exception;
	/**
	 * 解析以太网业务配置块
	 * 
	 * @return 数据体命令
	 * @throws Exception
	 */
	public List<EthServiceInfoObject> AnalysisCommandToEthService(byte[] dataCommand) throws Exception;
	
	/**
	 * 端口丢弃流
	 * @param neObject
	 * @param EthServiceObject
	 * @return
	 * @throws Exception
	 */
	public byte[] AnalysisPortDiscardToCommand(NEObject neObject,EthServiceObject EthServiceObject) throws Exception;
	public EthServiceObject AnalysisCommandToPortDiscard(byte[] dataCommand) throws Exception;
	
	/**
	 * 告警屏蔽 
	 * @param neObject网元信息
	 * @return 数据体命令
	 * 
	 * @throws Exception
	 */
	public byte[] AnalysisAlarmShieldObjectToCommand(NEObject neObject,AlarmShieldObject alarmShieldObject) throws Exception;
	/**
	 * L2CP配置块
	 * @param neObject网元信息
	 * @return 数据体命令
	 * @throws Exception
	 */
	public byte[] AnalysisL2cpObjectToCommand(NEObject neObject,L2CPinfoObject L2CPinfoObject) throws Exception;
	
	/**
	 * 端口pri映射
	 * @param neObject
	 * @param portObject
	 * @return
	 * @throws Exception
	 */
	public byte[] AnalysisPortPriToCommand(NEObject neObject,PortObject portObject) throws Exception;
	public PortObject analysisCommandToPortPri(byte[] command) throws Exception ;

	public byte[] AnalysisSecondMacManageToCommands(NEObject neObject,List<SecondMacStudyObject> list) throws Exception;

	/**
	 * 端口2层属性
	 */
	public byte[] AnalysisPort2layerToCommand(NEObject neObject, List<Port2LayerObject> port2LayerObjectList);
	
	public List<Port2LayerObject> analysisCommandToPort2LayerObj(byte[] command) throws Exception;
	
	public byte[] analysisTimeSynctoCommand(NEObject neObject, TimeSyncObject timesyncobj) throws Exception;
	
	public byte[] AnalysisCccToCommand(NEObject neObject, List<CccObject> cccobjList) throws Exception;
	public List<CccObject> AnalysisCommandToCcc(byte[] command) throws Exception;
	public byte[] AnalysisBfdToCommand(NEObject neObject, List<BfdObject> bfdobjList) throws Exception;
	public List<BfdObject> AnalysisCommandToBfd(byte[] command) throws Exception;
   
	/**
     * arp配置 解析对象为命令
     * @param neObject
     * @param arpObjectList
     * @return
     * @throws Exception
     */
	public byte[] AnalysisARPToCommand(NEObject neObject, List<ARPObject> arpObjectList) throws Exception;
	
	/**
	 * arp配置 解析命令为对象
	 */
	public List<ARPObject> analysisCommandsToARPObject(byte[] command) throws Exception;
	
	/**
	 * arp状态解析
	 */
	public List<ARPObject> AnalysisArpStatus(byte[] command)throws Exception;
}
