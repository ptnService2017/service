package com.nms.ui.ptn.alarm;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import twaver.AlarmSeverity;
import twaver.table.TTableColumn;

import com.nms.db.bean.alarm.AlarmInfo;
import com.nms.db.bean.alarm.CurrentAlarmInfo;
import com.nms.db.bean.alarm.HisAlarmInfo;
import com.nms.db.bean.alarm.WarningLevel;
import com.nms.db.bean.equipment.port.E1Info;
import com.nms.db.bean.equipment.port.PortInst;
import com.nms.db.bean.equipment.shelf.SiteInst;
import com.nms.db.bean.equipment.slot.SlotInst;
import com.nms.db.bean.path.Segment;
import com.nms.db.bean.ptn.Businessid;
import com.nms.db.bean.ptn.path.ServiceInfo;
import com.nms.db.bean.ptn.path.ces.CesInfo;
import com.nms.db.bean.ptn.path.eth.DualInfo;
import com.nms.db.bean.ptn.path.eth.ElanInfo;
import com.nms.db.bean.ptn.path.eth.ElineInfo;
import com.nms.db.bean.ptn.path.eth.EtreeInfo;
import com.nms.db.bean.ptn.path.protect.PwProtect;
import com.nms.db.bean.ptn.path.pw.PwInfo;
import com.nms.db.bean.ptn.path.tunnel.Lsp;
import com.nms.db.bean.ptn.path.tunnel.Tunnel;
import com.nms.db.bean.system.user.UserInst;
import com.nms.db.enums.EObjectType;
import com.nms.db.enums.EServiceType;
import com.nms.drive.service.bean.AlarmInfoObject;
import com.nms.drive.service.bean.AlarmLineObject;
import com.nms.drive.service.bean.AlarmMasterCardObject;
import com.nms.drive.service.bean.AlarmObject;
import com.nms.drive.service.impl.CoderUtils;
import com.nms.jms.common.OpviewMessage;
import com.nms.jms.jmsMeanager.JmsUtil;
import com.nms.model.alarm.CurAlarmService_MB;
import com.nms.model.alarm.HisAlarmService_MB;
import com.nms.model.alarm.WarningLevelService_MB;
import com.nms.model.equipment.port.E1InfoService_MB;
import com.nms.model.equipment.port.PortService_MB;
import com.nms.model.equipment.shlef.SiteService_MB;
import com.nms.model.equipment.slot.SlotService_MB;
import com.nms.model.ptn.BusinessidService_MB;
import com.nms.model.ptn.path.eth.DualInfoService_MB;
import com.nms.model.ptn.path.protect.PwProtectService_MB;
import com.nms.model.ptn.path.pw.PwInfoService_MB;
import com.nms.model.ptn.path.tunnel.TunnelService_MB;
import com.nms.model.util.CodeConfigItem;
import com.nms.model.util.Services;
import com.nms.service.bean.ActionObject;
import com.nms.service.bean.OperationObject;
import com.nms.ui.frame.RowIDRenderer;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.DateUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.ResourceUtil;
import com.nms.ui.manager.UiUtil;
import com.nms.ui.manager.keys.StringKeysLbl;
import com.nms.ui.manager.keys.StringKeysObj;
import com.nms.ui.manager.keys.StringKeysTab;
import com.nms.ui.manager.keys.StringKeysTip;

/**
 * 告警信息的工具类
 * 
 * @author lp
 * 
 */
public class AlarmTools {
	private int criticalColorRGB = -1564897;//默认紧急告警:红色
	private int majorColorRGB = -26368;//默认主要告警:橙色
	private int minorColorRGB = -1447651;//默认次要告警:黄色
	private int warningColorRGB = -7368720;//默认提示告警:紫色
	private Map<Integer, String> protectTunnelNameMap = new HashMap<Integer, String>();
	private Map<Integer, String> portNameMap = new HashMap<Integer, String>();
//	private Map<Integer, String> segmentNameMap = new HashMap<Integer, String>();
	private Map<Integer, String> tunnelNameMap = new HashMap<Integer, String>();
	private Map<Integer, String> serviceIdPwNameMap = new HashMap<Integer, String>();
	private Map<Integer,String>  dualNameMap = new HashMap<Integer, String>();
	
	
	public AlarmTools(){}
	
	/**
	 * 在没有必要加载://需要加载数据库里面的告警颜色时 可以通过这个来初始化 AlarmTools 对象
	 * @param label
	 */
	public AlarmTools(int label){}
	
	/**
	 * 初始化告警等级
	 */
	private void initAlarmSeverity(){
		AlarmSeverity.WARNING.setDisplayName(ResourceUtil.srcStr(StringKeysObj.ALARMSEVERITY_WARNING));
		AlarmSeverity.CRITICAL.setColor(new Color(this.criticalColorRGB));			
		AlarmSeverity.MAJOR.setColor(new Color(this.majorColorRGB));
		AlarmSeverity.MINOR.setColor(new Color(this.minorColorRGB));
		AlarmSeverity.WARNING.setColor(new Color(this.warningColorRGB));
		AlarmSeverity.CLEARED.setColor(Color.green);
	}
	
	/**
	 * 告警table表头
	 */
	public TTableColumn[] createDefaultColumns() {
		TTableColumn objColumn = createColumn("obj", ResourceUtil.srcStr(StringKeysObj.ORDER_NUM), 0, false);
		TTableColumn indexColumn = createColumn("index", ResourceUtil.srcStr(StringKeysObj.ORDER_NUM), 40, true);
		indexColumn.setRenderer(new RowIDRenderer());

		return new TTableColumn[] { objColumn, indexColumn,
		        // 告警级别
				createColumn("alarmSeverity", ResourceUtil.srcStr(StringKeysObj.ALARM_LEVEL), 100, true),
				// 网元编号
				createColumn("siteName", ResourceUtil.srcStr(StringKeysObj.STRING_SITE_NAME), 80, true), 
				createColumn("alarmSource", ResourceUtil.srcStr(StringKeysObj.STRING_ALARM_SOURCE), 80, true), 
				// 告警名称
				createColumn("warningNotes", ResourceUtil.srcStr(StringKeysObj.STRING_ALARM_DETAIL), 120, true),
				// 告警描述
				createColumn("alarmDesc", ResourceUtil.srcStr(StringKeysTab.TAB_ALARM), 120, true), 
				// 告警类型
				createColumn("warningTypes", ResourceUtil.srcStr(StringKeysObj.STRING_ALARM_TYPE), 80, true),
				
				// createColumn(Alarm.PROPERTY_ALARMTYPE, 60, true),
				createColumn("ackUser", ResourceUtil.srcStr(StringKeysObj.STRING_CONFIRM_USER), 80, true),
				// 发生时间
				createColumn("raisedTime", ResourceUtil.srcStr(StringKeysObj.HAPPENED_TIME), 120, true),
				// 确认时间
				createColumn("ackTime", ResourceUtil.srcStr(StringKeysObj.CONFIRM_TIME), 120, true),
				//是否清除
				createColumn("isCleared", ResourceUtil.srcStr(StringKeysObj.IS_CLEARED), 120, true),
				// 清除时间
				createColumn("clearedTime", ResourceUtil.srcStr(StringKeysObj.CLEAR_TIME), 120, true),
				//备注
				createColumn("remarks", ResourceUtil.srcStr(StringKeysLbl.LBL_ALARM_REMARK), 120, true),
			};
	}

	private TTableColumn createColumn(String name, String display, int width, boolean visible) {
		return new TTableColumn(name, display, width).setVisible(visible);
	}

	/**
	 * 告警级别
	 */
	public AlarmSeverity getAlarmSeverity(int value) {
		AlarmSeverity type = null;
		switch (value) {
		case 0:
			type = AlarmSeverity.CLEARED;
			break;
		case 1:
			type = AlarmSeverity.INDETERMINATE;
			break;
		case 2:
			type = AlarmSeverity.WARNING;
			break;
		case 3:
			type = AlarmSeverity.MINOR;
			break;
		case 4:
			type = AlarmSeverity.MAJOR;
			break;
		case 5:
			type = AlarmSeverity.CRITICAL;
			break;
		default:
			type = null;
			break;
		}
		return type;
	}

	/**
	 * 告警类型
	 */
	public String getAlarmType(int value) {
		String str = null;
		switch (value) {
		case 1:
			str = ResourceUtil.srcStr(StringKeysObj.STRING_COMMUNICATION_ALARM);//通讯告警
			break;
		case 2:
			str = ResourceUtil.srcStr(StringKeysObj.STRING_SERVICE_QUALITY_ALARM);//服务质量告警
			break;
		case 3:
			str = ResourceUtil.srcStr(StringKeysObj.STRING_EQUIPMENT_ALARM);//设备告警
			break;
		case 4:
			str = ResourceUtil.srcStr(StringKeysObj.STRING_DO_ERROR_ALARM);//处理错误告警
			break;
		case 5:
			str = ResourceUtil.srcStr(StringKeysObj.STRING_ENVIRONMENT_ALARM);//环境告警
			break;
		case 6:
			str = ResourceUtil.srcStr(StringKeysObj.STRING_EQUIPPOWER_ALARM);//设备电源告警
			break;
		default:
			str = ResourceUtil.srcStr(StringKeysObj.STRING_COMMUNICATION_ALARM);
			break;
		}
		return str;
	}

	/**
	 * 解析设备线路，产生告警业务id，业务类型、业务名称等信息
	 * 
	 * @param lineObject
	 * @param info
	 */
	private void setIdAndType(AlarmLineObject lineObject, AlarmInfo info, int slotNumber,int alarmCode)
	{
		String lineCode = Integer.toHexString(lineObject.getLine());
		while (lineCode.length() < 4) {//28672
			lineCode = 0 + lineCode;
		}
		int codeType = Integer.parseInt(lineCode.substring(0, 2),16);
		String codeType2 = lineCode.substring(2, 4);
		try {
			if("8".equals(codeType2)){//设备温度
				info.setObjectType(EObjectType.CI_TEMP);
				info.setObjectName("CiTemperature");
			}else if ("5".equals(codeType2)) {// 电源
				info.setObjectType(EObjectType.POWER);
				info.setObjectName("Voltage");
			}else if (codeType == 0) {// 电源
				info.setObjectType(EObjectType.POWER);
				info.setObjectName("POWERALM");
				//风扇
				if("10".equals(codeType2)){
					info.setObjectType(EObjectType.FAN1);
				}else if("11".equals(codeType2)){
					info.setObjectType(EObjectType.FAN2);
				}else if("12".equals(codeType2)){
					info.setObjectType(EObjectType.FAN3);
				}
			} else if (codeType == 0x1) {// 时钟
				info.setObjectType(EObjectType.CLOCK);
				info.setObjectName(ResourceUtil.srcStr(StringKeysObj.CLOCK));
			} else if (codeType == 0x2) {// 端口
				info.setObjectType(EObjectType.PORT);
				info.setObjectId(lineObject.getLine()%256 + 1);//端口号
				String name = this.portNameMap.get(info.getObjectId());
				if(name != null){
					info.setObjectName(name);
					if(alarmCode == 35){
						info.setObjectName(name+" DYING_GASP");
					}
				}else{
					info.setObjectName("未知端口号:"+(lineObject.getLine()%256 + 1));
				}
			} else if (codeType == 0x3) {// 段层(TMS)
				info.setObjectType(EObjectType.TMS_OAM);
				info.setObjectId(lineObject.getLine()%256 + 1);
				String name = this.portNameMap.get(info.getObjectId());
				if(name != null){
					info.setObjectName(name);
				}
			} else if (codeType == 0x4) {// 时间同步
				info.setObjectType(null);
				if (lineCode.substring(2).equals("00")) {
					info.setObjectName(ResourceUtil.srcStr(StringKeysObj.PTP_MODULE));
				} else if (lineCode.substring(2).equals("01")) {
					info.setObjectName(ResourceUtil.srcStr(StringKeysObj.TOD_PORT));
				} else {
					int portNum = Integer.parseInt(lineCode.substring(2));
					info.setObjectName(ResourceUtil.srcStr(StringKeysObj.PTP_PORT) + (portNum - 1));
				}
			} else if (codeType == 0x5) {// Wrapping
				info.setObjectType(null);
				info.setObjectId(lineObject.getLine()%256 + 1);
			} else if (codeType == 0x10) {// LSP1+1保护
			
			} else if (codeType == 0x20) {// LSP1:1保护
				info.setObjectType(EObjectType.LSP);
				info.setObjectId(lineObject.getLine()%256 + 1);
				String objName = this.protectTunnelNameMap.get(info.getObjectId());
				if(objName != null){
					info.setObjectName(objName);
				}
			} else if (codeType == 0x30) {// TMP通道
				info.setObjectType(EObjectType.TUNNEL);
				int tmsNum = lineObject.getLine()%256 + 1;
				info.setObjectId(tmsNum);
				String objName = this.tunnelNameMap.get(tmsNum);
				if(objName != null){
					info.setObjectName(objName);
				}
			} else if (codeType == 0x40) {// TMC通路
				info.setObjectType(EObjectType.PW);
				info.setObjectId(lineObject.getLine()%256 + 1);
				String objName = this.serviceIdPwNameMap.get(info.getObjectId());
				if(objName != null){
					info.setObjectName(objName);
				}
			} else if (codeType == 0x50) {// VPWS业务实例
				info.setObjectType(EObjectType.VPWS);
				info.setObjectId(lineObject.getLine()%256 + 1);
			} else if (codeType == 0x60) {// VPLS业务实例
				info.setObjectType(EObjectType.VPLS);
				info.setObjectId(lineObject.getLine()%256 + 1);
			} else if (codeType == 0x70) {// 1731线路(ETHOAM)
				info.setObjectType(EObjectType.ETHOAM);
				info.setObjectId(Integer.parseInt(lineCode.substring(0, 1)) + 1);
				info.setObjectName("ETHOAM("+(lineObject.getLine()%256 + 1)+")");
			} else if (codeType == 0x80) {// E1
				info.setObjectType(EObjectType.E1);
				info.setObjectName("E1线路" + (lineObject.getLine()%256 + 1));
			}else if (codeType == 0xa0) {// dual
				ExceptionManage.infor("双归线路号"+(lineObject.getLine()%256 + 1), AlarmTools.class);
				info.setObjectType(EObjectType.DUAL);
				info.setObjectId(lineObject.getLine()%256 + 1);
				info.setObjectName(this.dualNameMap.get(info.getObjectId()));
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,AlarmTools.class);
		}
	}

	/**
	 * 将查询的设备告警信息，转化成List<HisAlarmInfo>历史告警对象(保存数据库)
	 * 
	 * @param operationObject
	 *            查询设备的操作对象
	 * @return
	 */
	public List<HisAlarmInfo> convertObject2HisInfo(OperationObject operationObject) throws Exception {
		List<HisAlarmInfo> objs = null;
		SiteService_MB siteService = null;
		SiteInst siteInst = null;
		SlotService_MB slotService = null;
		SlotInst slotInst = null;
		List<SlotInst> slotList = null;
		try {
			objs = new ArrayList<HisAlarmInfo>();
			siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE);
			slotService = (SlotService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SLOT);
			siteInst = new SiteInst();
			for (ActionObject obj : operationObject.getActionObjectList()) {
				siteInst = new SiteInst();
				if (obj.getAlarmObject() != null) {
					AlarmObject alarmObject = obj.getAlarmObject();
					// 封装网元信息
					siteInst.setCellId(String.valueOf(obj.getNeObject().getNeAddress() % 256));
					siteInst = siteService.select(siteInst).get(0);
					for (AlarmMasterCardObject alarmCardObject : alarmObject.getAlarmMasterCardObjectList()) {
						slotInst = new SlotInst();
						slotInst.setNumber(alarmCardObject.getMasterCardAddress());
						slotInst.setSiteId(siteInst.getSite_Inst_Id());
						slotList = slotService.select(slotInst);
						if (obj.getType().equals("queryAlarmBySite")) {
							for (SlotInst slot : slotList) {
								objs.add(wrappingHisAlarmInfo(obj, alarmCardObject, siteInst, slot));
							}
						} else {
							slotInst = slotService.select(slotInst).get(0);
							objs.add(wrappingHisAlarmInfo(obj, alarmCardObject, siteInst, slotInst));
						}
					}
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,AlarmTools.class);
		} finally {
			UiUtil.closeService_MB(slotService);
			UiUtil.closeService_MB(siteService);
		    siteInst = null;
		    slotInst = null;
			slotList = null;
		}
		return objs;
	}

	public HisAlarmInfo wrappingHisAlarmInfo(ActionObject obj, AlarmMasterCardObject alarmCardObject, SiteInst siteInst, SlotInst slotInst) 
	{
		SimpleDateFormat sdf = null;
		try {
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			this.getObjectNameMap(siteInst);
			for (AlarmLineObject lineObject : alarmCardObject.getAlarmLineObjectList()) {
				for (AlarmInfoObject infoObject : lineObject.getAlarmInfoObjectList()) {
					HisAlarmInfo info = new HisAlarmInfo();
					info.setSiteId(siteInst.getSite_Inst_Id());
					info.setRaisedTime(sdf.parse(infoObject.getAlarmDate()));
					info.setAlarmTime(sdf.format(info.getRaisedTime()));
					info.setSiteAddress(obj.getNeObject().getNeAddress());
					info.setSlotId(slotInst.getId());
					info.setSlotNumber(alarmCardObject.getMasterCardAddress());
					info.setAlarmCode(infoObject.getAlarmCode());
					info.setAlarmLevel(infoObject.getAlarmStatus());
					setIdAndType(lineObject, info,alarmCardObject.getMasterCardAddress(),infoObject.getAlarmCode());
					return info;
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,AlarmTools.class);
		} finally {
			sdf = null;
		}
		return null;
	}

	/**
	 * 获取设备主动上报的告警，并把相应的历史告警和当前告警进行入库
	 * 
	 * @param alarmObjectList
	 * @throws Exception
	 */
	public List<CurrentAlarmInfo> convertAlarm(List<AlarmObject> alarmObjectList) throws Exception {
		SiteService_MB siteService = null;
		SiteInst siteInst = null;
		SlotInst slotInst = null;
		SimpleDateFormat sdf = null;
		CurrentAlarmInfo info = null;
		HisAlarmInfo hisAlarmInfo = null;
		CurAlarmService_MB curAlarmService = null;
		HisAlarmService_MB hisAlarmService = null;
		List<CurrentAlarmInfo> currentAlarmInfoList = null;
		List<CurrentAlarmInfo> currentAlarmInfoListToCorba = null;//將设备报上来的数据转给COrBA
		SlotService_MB slotService = null;
		long delayTime = 0l;
		Map<String, WarningLevel> levelMap = null;
		try {
			levelMap = getWarningLevelMap();
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE);
			curAlarmService = (CurAlarmService_MB) ConstantUtil.serviceFactory.newService_MB(Services.CurrentAlarm);
			hisAlarmService = (HisAlarmService_MB) ConstantUtil.serviceFactory.newService_MB(Services.HisAlarm);
			slotService = (SlotService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SLOT);
			currentAlarmInfoListToCorba = new ArrayList<CurrentAlarmInfo>();
			siteInst = new SiteInst();
			for (AlarmObject alarmObject : alarmObjectList) {
				siteInst = new SiteInst();
				if (alarmObject != null) {
					// 封装网元信息
					siteInst.setSite_Hum_Id(String.valueOf(alarmObject.getNeAddress() % 256));
					siteInst.setFieldID(alarmObject.getNeAddress() / 256);
					siteInst = siteService.selectByNeAddress(siteInst);
					if(siteInst.getDelayTime()!= null&&siteInst.getDelayTime().contains("-")){
					  String[] delayTimeString = siteInst.getDelayTime().split("-");
					  delayTime = Long.parseLong(delayTimeString[0])+Long.parseLong(delayTimeString[1]);
					}
					if(System.currentTimeMillis() > delayTime){
					if(siteInst != null){
						this.getObjectNameMap(siteInst);
						for (AlarmMasterCardObject alarmCardObject : alarmObject.getAlarmMasterCardObjectList()) {
							slotInst = new SlotInst();
							slotInst.setSiteId(siteInst.getSite_Inst_Id());
							slotInst.setNumber(alarmCardObject.getMasterCardAddress()%256);
							List<SlotInst> slotInsts = slotService.select(slotInst);
							if(slotInsts.size()>0){
								slotInst = slotInsts.get(0);
							}
							for (AlarmLineObject lineObject : alarmCardObject.getAlarmLineObjectList()) {
								for (AlarmInfoObject infoObject : lineObject.getAlarmInfoObjectList()) {
									WarningLevel warningLevel = new WarningLevel();
									warningLevel.setWarningcode(infoObject.getAlarmCode());
									warningLevel.setWarninglevel(getAlarmLevel(infoObject.getAlarmStatus()));
									HisAlarmInfo northAlarm = new HisAlarmInfo();
									northAlarm.setRaisedTime(sdf.parse(infoObject.getAlarmDate()));
									northAlarm.setClearedTime(sdf.parse(infoObject.getAlarmDate()));
									northAlarm.setConfirmtime(sdf.format(System.currentTimeMillis()));
									northAlarm.setSiteId(siteInst.getSite_Inst_Id());
									northAlarm.setSiteName(siteInst.getCellId() + "");
									northAlarm.setSlotId(slotInst.getId());
									northAlarm.setSlotNumber(slotInst.getNumber());
									northAlarm.setSiteAddress(alarmObject.getNeAddress());
									northAlarm.setAlarmCode(infoObject.getAlarmCode());
									northAlarm.setAlarmLevel(warningLevel.getWarninglevel());// 确定告警等级
									northAlarm.setWarningLevel(warningLevel);
									northAlarm.setAlarmTime(infoObject.getAlarmDate());
									northAlarm.setWarningLevel(warningLevel);
									setIdAndType(lineObject, northAlarm,slotInst.getNumber(),infoObject.getAlarmCode());// 确定告警类型
									if (getAlarmStatus(infoObject.getAlarmStatus()) == 1) {// 判断是历史告警还是当前告警
										info = new CurrentAlarmInfo();
										info.setRaisedTime(sdf.parse(infoObject.getAlarmDate()));
										info.setSiteId(siteInst.getSite_Inst_Id());
										info.setSiteName(siteInst.getCellId() + "");
										info.setSlotId(slotInst.getId());
										info.setSlotNumber(slotInst.getNumber());
										info.setLabelAlarmStatus(1);//表示告警上报
										setIdAndType(lineObject, info,slotInst.getNumber(),infoObject.getAlarmCode());// 确定告警类型
										info.setSiteAddress(alarmObject.getNeAddress());
										info.setAlarmCode(infoObject.getAlarmCode());
										info.setAlarmLevel(getAlarmLevel(infoObject.getAlarmStatus()));// 确定告警等级
										this.updateCode(info, warningLevel);
										info.setAlarmSeverity(this.getAlarmSeverity(warningLevel.getWarninglevel()));
										info.setWarningLevel(warningLevel);
										info.setIsCleared(ResourceUtil.srcStr(StringKeysTip.TIP_UNCLEARED));
										info.setWarningLevel_temp(warningLevel.getWarninglevel());
										currentAlarmInfoList = curAlarmService.select(info);
										if (currentAlarmInfoList != null && currentAlarmInfoList.size() > 0) {// 判断当前告警是否存在
											this.filterByAlarmBlocking(currentAlarmInfoList, levelMap);
											curAlarmService.delete(currentAlarmInfoList.get(0).getId());
											currentAlarmInfoList.get(0).setAlarmTime(infoObject.getAlarmDate());
											currentAlarmInfoList.get(0).setAlarmLevel(info.getAlarmLevel());
											currentAlarmInfoList.get(0).setId(0);
											curAlarmService.saveOrUpdate(currentAlarmInfoList.get(0));// 存在，先删除，再入库
											currentAlarmInfoListToCorba.add(currentAlarmInfoList.get(0));
										} else {
											List<CurrentAlarmInfo> list = new ArrayList<CurrentAlarmInfo>();
											list.add(info);
											this.filterByAlarmBlocking(list, levelMap);
											for(CurrentAlarmInfo cInfo : list){
												curAlarmService.saveOrUpdate(cInfo);// 不存在，直接入库
												currentAlarmInfoListToCorba.add(cInfo);
											}
										}
										northAlarm.setIsClear(0);
									} else {
										hisAlarmInfo = new HisAlarmInfo();
										
										hisAlarmInfo.setRaisedTime(sdf.parse(infoObject.getAlarmDate()));
										hisAlarmInfo.setClearedTime(sdf.parse(infoObject.getAlarmDate()));
										hisAlarmInfo.setConfirmtime(sdf.format(System.currentTimeMillis()));
//										hisAlarmInfo.setAckUser(ConstantUtil.user.getUser_Name());
										hisAlarmInfo.setSiteId(siteInst.getSite_Inst_Id());
										hisAlarmInfo.setSiteName(siteInst.getType() + "");
										hisAlarmInfo.setSlotId(slotInst.getId());
										hisAlarmInfo.setSlotNumber(slotInst.getNumber());
										hisAlarmInfo.setSiteAddress(alarmObject.getNeAddress());
										setIdAndType(lineObject, hisAlarmInfo,slotInst.getNumber(),infoObject.getAlarmCode());// 确定告警类型
										hisAlarmInfo.setAlarmCode(infoObject.getAlarmCode());
										hisAlarmInfo.setAlarmLevel(getAlarmLevel(infoObject.getAlarmStatus()));// 确定告警等级
										this.updateCode(hisAlarmInfo, warningLevel);
										hisAlarmInfo.setAlarmSeverity(this.getAlarmSeverity(warningLevel.getWarninglevel()));
										hisAlarmInfo.setWarningLevel(warningLevel);
										
										CurrentAlarmInfo currentAlarmInfo = new CurrentAlarmInfo();
										currentAlarmInfo.setSiteId(siteInst.getSite_Inst_Id());
										currentAlarmInfo.setSiteName(siteInst.getCellId() + "");
										currentAlarmInfo.setLabelAlarmStatus(0);//表示告警消失
										currentAlarmInfo.setSlotId(slotInst.getId());
										currentAlarmInfo.setSlotNumber(slotInst.getNumber());
										setIdAndType(lineObject, currentAlarmInfo,slotInst.getNumber(),infoObject.getAlarmCode());// 确定告警类型
										currentAlarmInfo.setSiteAddress(alarmObject.getNeAddress());
										currentAlarmInfo.setAlarmCode(infoObject.getAlarmCode());
										currentAlarmInfo.setAlarmLevel(getAlarmLevel(infoObject.getAlarmStatus()));// 确定告警等级
										warningLevel.setWarninglevel(getAlarmLevel(infoObject.getAlarmStatus()));
										currentAlarmInfo.setAlarmSeverity(this.getAlarmSeverity(warningLevel.getWarninglevel()));
										currentAlarmInfo.setWarningLevel(warningLevel);
										currentAlarmInfo.setObjectId(hisAlarmInfo.getObjectId());
										currentAlarmInfo.setRaisedTime(hisAlarmInfo.getRaisedTime());
										List<CurrentAlarmInfo> currentAlarmInfo_beforeList = curAlarmService.select(currentAlarmInfo);
										curAlarmService.deleteCurrentAlarmInfo(currentAlarmInfo);
										if(currentAlarmInfo_beforeList != null && currentAlarmInfo_beforeList.size() > 0){
											//将当前告警的备注封装到历史告警中
											hisAlarmInfo.setAckTime(currentAlarmInfo_beforeList.get(0).getAckTime());
											hisAlarmInfo.setRaisedTime(currentAlarmInfo_beforeList.get(0).getRaisedTime());
											hisAlarmInfo.setCommonts(currentAlarmInfo_beforeList.get(0).getAlarmComments());
											hisAlarmInfo.setAlarmId(currentAlarmInfo_beforeList.get(0).getId());//北向中告警产生，消失唯一标识
										}
										hisAlarmService.saveOrUpdate(hisAlarmInfo);// 不存在，直接入库
										List<HisAlarmInfo> northHis = hisAlarmService.queryHisNorth(northAlarm);
										if(northHis.size()>0){
											northAlarm.setAlarmId(northHis.get(0).getId());
											northAlarm.setIsClear(1);
										}
									}
									if(CodeConfigItem.getInstance().getSnmpStartOrClose() == 0){
										hisAlarmService.saveNorth(northAlarm);//北向告警流水
										OpviewMessage opviewMessage = new OpviewMessage();
										opviewMessage.setOccuredTime(System.currentTimeMillis());
										opviewMessage.setMessageSource("alarmNorth");
										opviewMessage.setObject(northAlarm);
										JmsUtil.sendNorthAlarm(opviewMessage);
									}
								}
							}
						}
					}
				 }
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,AlarmTools.class);
			throw e;
		} finally {
			UiUtil.closeService_MB(siteService);
			UiUtil.closeService_MB(curAlarmService);
			UiUtil.closeService_MB(hisAlarmService);
			UiUtil.closeService_MB(slotService);
			siteInst = null;
			slotInst = null;
			sdf = null;
			info = null;
			hisAlarmInfo = null;
			currentAlarmInfoList = null;
		}
		return currentAlarmInfoListToCorba;
	}
	
	/**
	 * 处理当前告警
	 * @param alarmObjectList
	 * @return
	 * @throws Exception
	 */
	public void operateCurrentAlarm(List<AlarmObject> alarmObjectList) throws Exception {
		SiteService_MB siteService = null;
		SiteInst siteInst = null;
		SlotInst slotInst = null;
		SimpleDateFormat sdf = null;
		CurrentAlarmInfo info = null;
		CurAlarmService_MB curAlarmService = null;
		List<CurrentAlarmInfo> currentAlarmInfoList = null;
		long delayTime =0l;
		SlotService_MB slotService = null;
		Map<String, WarningLevel> levelMap = null;
		try {
			levelMap = getWarningLevelMap();
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE);
			curAlarmService = (CurAlarmService_MB) ConstantUtil.serviceFactory.newService_MB(Services.CurrentAlarm);
			slotService = (SlotService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SLOT);
			siteInst = new SiteInst();
			for (AlarmObject alarmObject : alarmObjectList) {
				siteInst = new SiteInst();
				currentAlarmInfoList = new ArrayList<CurrentAlarmInfo>();
				if (alarmObject != null) {
					// 封装网元信息
					siteInst.setSite_Hum_Id(String.valueOf(alarmObject.getNeAddress() % 256));
					siteInst.setFieldID(alarmObject.getNeAddress() / 256);
					siteInst = siteService.selectByNeAddress(siteInst);
					if(siteInst != null){
						this.getObjectNameMap(siteInst);
				    	if(siteInst.getDelayTime() != null && siteInst.getDelayTime().contains("-")){
						  String[] delayTimeString = siteInst.getDelayTime().split("-");
						  delayTime = Long.parseLong(delayTimeString[0])+Long.parseLong(delayTimeString[1]);
						}
				    	if(System.currentTimeMillis() > delayTime){
						 for (AlarmMasterCardObject alarmCardObject : alarmObject.getAlarmMasterCardObjectList()) {
							slotInst = new SlotInst();
							slotInst.setSiteId(siteInst.getSite_Inst_Id());
							slotInst.setNumber(alarmCardObject.getMasterCardAddress()%256);
							List<SlotInst> slotInsts = slotService.select(slotInst);
							if(slotInsts.size()>0){
								slotInst = slotInsts.get(0);
							}
							for (AlarmLineObject lineObject : alarmCardObject.getAlarmLineObjectList()) {
								for (AlarmInfoObject infoObject : lineObject.getAlarmInfoObjectList()) {
									if (getAlarmStatus(infoObject.getAlarmStatus()) == 1) {// 判断是历史告警还是当前告警
										info = new CurrentAlarmInfo();
										info.setRaisedTime(sdf.parse(infoObject.getAlarmDate()));
										info.setSiteId(siteInst.getSite_Inst_Id());
										info.setSiteName(siteInst.getCellId() + "");
										info.setSlotId(slotInst.getId());
										info.setSlotNumber(slotInst.getNumber());
										setIdAndType(lineObject, info,slotInst.getNumber(),infoObject.getAlarmCode());// 确定告警类型
										//05
										info.setSiteAddress(alarmObject.getNeAddress());
										info.setAlarmCode(infoObject.getAlarmCode());
										info.setAlarmLevel(getAlarmLevel(infoObject.getAlarmStatus()));// 确定告警等级
										WarningLevel warningLevel = new WarningLevel();
										warningLevel.setWarningcode(infoObject.getAlarmCode());
										warningLevel.setWarninglevel(getAlarmLevel(infoObject.getAlarmStatus()));
										this.updateCode(info, warningLevel);
										info.setAlarmSeverity(this.getAlarmSeverity(warningLevel.getWarninglevel()));
										info.setWarningLevel(warningLevel);
										info.setWarningLevel_temp(warningLevel.getWarninglevel());
										currentAlarmInfoList.add(info);
									}
								}
							}
						}
						this.filterByAlarmBlocking(currentAlarmInfoList, levelMap);
						curAlarmService.insertList(currentAlarmInfoList, siteInst.getSite_Inst_Id());
					 }
					}
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,AlarmTools.class);
			throw e;
		} finally {
			UiUtil.closeService_MB(slotService);
			UiUtil.closeService_MB(siteService);
			UiUtil.closeService_MB(curAlarmService);
			siteInst = null;
			slotInst = null;
			sdf = null;
			info = null;
			currentAlarmInfoList = null;
		}
	}
	
	private Map<String, WarningLevel> getWarningLevelMap(){
		WarningLevelService_MB warningLevelService = null;
		Map<String, WarningLevel> map = null;
		try {
			warningLevelService = (WarningLevelService_MB) ConstantUtil.serviceFactory.newService_MB(Services.WarningLevel);
			//获取告警级别map
			List<WarningLevel> warningLevels = warningLevelService.select();
			map = new HashMap<String, WarningLevel>();
			for(WarningLevel level:warningLevels){
				map.put(level.getManufacturer()+":"+level.getWarningcode()+":"+level.getWarninglevel(), level);
			}
		}catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			UiUtil.closeService_MB(warningLevelService);
		}
		return map;
	}
	
	private void updateCode(AlarmInfo alarm, WarningLevel warningLevel){
		int code = alarm.getAlarmCode();
		if(code == 216){
			// port
			alarm.setObjectType(EObjectType.PORT);
			alarm.setAlarmLevel(5);
			warningLevel.setWarninglevel(5);
		}else if(code == 217){
			// tmp
			alarm.setObjectType(EObjectType.TUNNEL);
			alarm.setAlarmLevel(5);
			warningLevel.setWarninglevel(5);
		}else if(code == 218){
			// tmc
			alarm.setObjectType(EObjectType.PW);
			alarm.setAlarmLevel(5);
			warningLevel.setWarninglevel(5);
		}else if(code == 219){
			// vpws
			alarm.setObjectType(EObjectType.VPWS);
			alarm.setAlarmLevel(5);
			warningLevel.setWarninglevel(5);
		}else if(code == 220){
			// vpls
			alarm.setObjectType(EObjectType.VPLS);
			alarm.setAlarmLevel(5);
			warningLevel.setWarninglevel(5);
		}	
	}
	
	/**
	 * 根据告警屏蔽配置过滤当前告警
	 * @param alarmList
	 */
	private void filterByAlarmBlocking(List<CurrentAlarmInfo> curAlarmList, Map<String, WarningLevel> levelMap){
		if(curAlarmList != null && ConstantUtil.alarmBlock != null){
			if(!ConstantUtil.alarmBlock.isClose()){
				List<CurrentAlarmInfo> alarmList = new ArrayList<CurrentAlarmInfo>();
				List<SiteInst> siteList = ConstantUtil.alarmBlock.getSiteList();
				for(CurrentAlarmInfo alarm : curAlarmList){
					for(SiteInst site : siteList){
						int siteId = site.getSite_Inst_Id();
						if(alarm.getSiteId() == siteId && ConstantUtil.alarmBlock.getAlarmLevel().contains(alarm.getAlarmLevel())){
							WarningLevel level = levelMap.get("1:"+alarm.getAlarmCode()+":"+alarm.getAlarmLevel());
							if(level != null){
								if(ConstantUtil.alarmBlock.getWarningType() == 0){
									// 屏蔽所有类型的告警
									alarmList.add(alarm);
								}else{
									if(ConstantUtil.alarmBlock.getWarningType() == level.getWarningtype() && 
											ConstantUtil.alarmBlock.getAlarmLevel().contains(alarm.getAlarmLevel())){
										if(filterByAlarmSrc(alarm)){
											long alarmTime = DateUtil.updateTimeToLong(alarm.getHappenedtime(), DateUtil.FULLTIME);
											long startTime = DateUtil.updateTimeToLong(ConstantUtil.alarmBlock.getHappenTime(), DateUtil.FULLTIME);
											long endTime = DateUtil.updateTimeToLong(ConstantUtil.alarmBlock.getHappenEndTime(), DateUtil.FULLTIME);
											if(startTime != 0){
												if(alarmTime >= startTime && alarmTime <= endTime){
													alarmList.add(alarm);
												}
											}else{
												alarmList.add(alarm);
											}
										}
									}
								}
							}
						}
					}
				}
				if(alarmList.size() > 0){
					curAlarmList.removeAll(alarmList);
				}
			}
		}
	}
	
	private boolean filterByAlarmSrc(CurrentAlarmInfo alarm){
		//根据告警源过滤
		if(ConstantUtil.alarmBlock.getAlarmSrc() != 0){
			int alarmSrc = ConstantUtil.alarmBlock.getAlarmSrc();
			Object alarmBusi = ConstantUtil.alarmBlock.getAlarmBusiness();
			E1InfoService_MB e1Service = null;
			PortService_MB portService = null;
			try {
				portService = (PortService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORT);
				e1Service = (E1InfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.E1Info);
				if(alarmSrc == 1){// 端口
					if((alarm.getObjectType().getValue() == EObjectType.PORT.getValue())){
						if(alarmBusi != null){
							PortInst port = (PortInst)alarmBusi;
							if(alarm.getObjectId() == port.getNumber()){
								return true;
							}
						}else{
							return true;
						}
					}else if(alarm.getObjectType().getValue() == EObjectType.E1.getValue()){
						if(alarmBusi != null){
							PortInst port = (PortInst)alarmBusi;
							E1Info e1Con = new E1Info();
							e1Con.setPortId(port.getPortId());
							try {
								e1Con = e1Service.selectByCondition(e1Con).get(0);
							} catch (Exception e) {
								ExceptionManage.dispose(e, this.getClass());
							}
							if(("E1线路"+e1Con.getLegId()).equals(alarm.getObjectName())){
								return true;
							}
						}else{
							return true;
						}
					}
				}else if(alarmSrc == 2){// 段
					if((alarm.getObjectType().getValue() == EObjectType.TMS_OAM.getValue())){
						if(alarmBusi != null){
							PortInst portCon = new PortInst();
							portCon.setSiteId(alarm.getSiteId());
							portCon.setNumber(alarm.getObjectId());
							List<PortInst> portList = portService.select(portCon);
							if(portList != null && portList.size() == 1){
								Segment segment = (Segment)alarmBusi;
								portCon = portList.get(0);
								if((segment.getASITEID() == portCon.getSiteId() && segment.getAPORTID() == portCon.getPortId()) || 
										segment.getZSITEID() == portCon.getSiteId() && segment.getZPORTID() == portCon.getPortId()){
									return true;
								}
							}
						}else{
							return true;
						}
					}
				}else if(alarmSrc == 3){// Tunnel
					if((alarm.getObjectType().getValue() == EObjectType.TUNNEL.getValue())){
						if(alarmBusi != null){
							Tunnel tunnel = (Tunnel) alarmBusi;
							for(Lsp lsp : tunnel.getLspParticularList()){
								if((lsp.getASiteId() == alarm.getSiteId() && lsp.getAtunnelbusinessid() == alarm.getObjectId()) ||
										(lsp.getZSiteId() == alarm.getSiteId() && lsp.getZtunnelbusinessid() == alarm.getObjectId())){
									return true;
								}
							}
						}else{
							return true;
						}
					}
					if(alarm.getObjectType().getValue() == EObjectType.LSP.getValue()){
						if(alarmBusi != null){
							Tunnel tunnel = (Tunnel) alarmBusi;
							if(tunnel.getProtectTunnelId() > 0){
								Tunnel pTunnel = tunnel.getProtectTunnel();
								if((pTunnel.getASiteId() == alarm.getSiteId() && pTunnel.getAprotectId() == alarm.getObjectId()) ||
										(pTunnel.getZSiteId() == alarm.getSiteId() && pTunnel.getZprotectId() == alarm.getObjectId())){
									return true;
								}
							}
						}else{
							return true;
						}
					}
				}else if(alarmSrc == 4){// Pw
					if((alarm.getObjectType().getValue() == EObjectType.PW.getValue())){
						if(alarmBusi != null){
							PwInfo pw = (PwInfo) alarmBusi;
							if((pw.getASiteId() == alarm.getSiteId() && pw.getApwServiceId() == alarm.getObjectId()) ||
									(pw.getZSiteId() == alarm.getSiteId() && pw.getZpwServiceId() == alarm.getObjectId())){
								return true;
							}
						}else{
							return true;
						}
					}
				}else if(alarmSrc == 5){// vpws业务
					if(alarm.getObjectType().getValue() == EObjectType.VPWS.getValue()){
						if(alarmBusi != null){
							if(alarmBusi instanceof ElineInfo){
								ElineInfo eline = (ElineInfo) alarmBusi;
								if((eline.getaSiteId() == alarm.getSiteId() && eline.getaXcId() == alarm.getObjectId()) ||
								(eline.getzSiteId() == alarm.getSiteId() && eline.getzXcId() == alarm.getObjectId())){
									return true;
								}
							}else if(alarmBusi instanceof CesInfo){
								CesInfo ces = (CesInfo) alarmBusi;
								if((ces.getaSiteId() == alarm.getSiteId() && ces.getAxcId() == alarm.getObjectId()) ||
								(ces.getzSiteId() == alarm.getSiteId() && ces.getZxcId() == alarm.getObjectId())){
									return true;
								}
							}
						}else{
							return true;
						}
					}
				}else if(alarmSrc == 6){// vpls业务
					if((alarm.getObjectType().getValue() == EObjectType.VPLS.getValue())){
						if(alarmBusi != null){
							List<ServiceInfo> serviceList = (List<ServiceInfo>) alarmBusi;
							for(ServiceInfo serviceInfo : serviceList){
								if(serviceInfo.getServiceType() == EServiceType.ETREE.getValue()){
									EtreeInfo etree = (EtreeInfo) serviceInfo;
									if((etree.getRootSite() == alarm.getSiteId() && etree.getaXcId() == alarm.getObjectId()) ||
											(etree.getBranchSite() == alarm.getSiteId() && etree.getzXcId() == alarm.getObjectId())){
										return true;
									}
								}else if(serviceInfo.getServiceType() == EServiceType.ELAN.getValue()){
									ElanInfo elan = (ElanInfo) serviceInfo;
									if((elan.getaSiteId() == alarm.getSiteId() && elan.getAxcId() == alarm.getObjectId()) ||
											(elan.getzSiteId() == alarm.getSiteId() && elan.getZxcId() == alarm.getObjectId())){
										return true;
									}
								}
							}
						}else{
							return true;
						}
					}
				}
			} catch (Exception e) {
				ExceptionManage.dispose(e, this.getClass());
			} finally {
				UiUtil.closeService_MB(e1Service);
				UiUtil.closeService_MB(portService);
			}
		}else{
			return true;
		}
		return false;
	}
	
	/**
	 * 根据告警状态解析告警等级
	 * 
	 * @param alarmStatus
	 * @return
	 */
	public int getAlarmLevel(int alarmStatus) {
		String status = Integer.toBinaryString(alarmStatus);

		while (status.length() < 8) {
			status = "0" + status;
		}
		char[] ch = new char[4];
		status.getChars(4, status.length(), ch, 0);
		int alarmLevel = 0;
		alarmLevel = CoderUtils.convertAlgorism(ch) + 1;
		if (alarmStatus == 19 || alarmStatus == 3 || alarmStatus == 2) {
			alarmLevel = 5;
		} else if (alarmStatus == 17 || alarmStatus == 1 || alarmStatus == 0) {
			alarmLevel = 4;
		} else if (alarmStatus == 21 || alarmStatus == 5 || alarmStatus == 4) {
			alarmLevel = 3;
		} else {
			alarmLevel = 2;
		}
		return alarmLevel;
	}

	public int getAlarmStatus(int alarmStatus) {
		String status = Integer.toBinaryString(alarmStatus);
		int c = Integer.parseInt(status.substring(status.length() - 1));
		return c;
	}
	
	/**
	 *function:用于产生用户登陆错误而产生相应的告警 
	 *   总共产生4条提示告警:1：用户被锁告警；2:3条用户密码输入错误告警;
	 * @param userInst
	 * @param label 1:表示用户名正确但是密码错误而产生的告警；2:表示用户名错误;3:表示产生用户被锁告警
	 * @param count 总产生告警的条目数
	 */
	public void produceAlarmToLogins(UserInst userInst,int label,int count){
		
		List<CurrentAlarmInfo> userAlarmList = new ArrayList<CurrentAlarmInfo>();
		CurAlarmService_MB curAlarmService = null;
		try {
			curAlarmService = (CurAlarmService_MB) ConstantUtil.serviceFactory.newService_MB(Services.CurrentAlarm);
			if(label ==1){
				//产生count条用户密码输入错误告警
				for(int i = 0; i< count; i++){
				 userAlarmList.add(userLockAlarm(1050,2,userInst.getUser_Name()));
				}
			}else if(label ==2){
				//产生count条用户名输入错误告警
				for(int i = 0; i< count; i++){
					userAlarmList.add(userLockAlarm(1051,2,userInst.getUser_Name()));
			   }
			}else if(label ==3){
				//产生count 用户被锁告警
				for(int i = 0; i< count; i++){
					userAlarmList.add(userLockAlarm(1000,2,userInst.getUser_Name()));
			   }
			}
			
			if(userAlarmList.size()>0 ){
				for(CurrentAlarmInfo currentInfo : userAlarmList){
					curAlarmService.saveOrUpdate(currentInfo);
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,AlarmTools.class);
		}finally{
			UiUtil.closeService_MB(curAlarmService);
		}
	}
	/**
	 * 产生一条告警
	 * @param userInst
	 * @return
	 */
	private CurrentAlarmInfo userLockAlarm(int alarmCode,int level,String objectInfo){
		CurrentAlarmInfo userLockAlarm = new CurrentAlarmInfo();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		try {
			userLockAlarm.setAlarmCode(alarmCode);
			userLockAlarm.setAlarmLevel(level);
			userLockAlarm.setObjectName(ResourceUtil.srcStr(StringKeysTip.TIP_LABEL_EMSSERVICE)+objectInfo);
			userLockAlarm.setSiteName(objectInfo);
			userLockAlarm.setRaisedTime(date);
			userLockAlarm.setAlarmTime(sdf.format(date));
			userLockAlarm.setWarningLevel_temp(level);
			userLockAlarm.setObjectType(EObjectType.EMSCLIENT);
			WarningLevel warningLevel = new WarningLevel();
			warningLevel.setWarningcode(userLockAlarm.getAlarmCode());
			warningLevel.setWarninglevel(userLockAlarm.getAlarmLevel());
			userLockAlarm.setAlarmSeverity(this.getAlarmSeverity(warningLevel.getWarninglevel()));
			userLockAlarm.setWarningLevel(warningLevel);
			
		} catch (Exception e) {
			ExceptionManage.dispose(e,AlarmTools.class);
		}finally
		{
			sdf = null;
			date = null;
		}
		return userLockAlarm;
	}
	
	/**
	 * 
	 * 
	 * @param userInst
	 * @param alarmCode 10052 代表数据库总内存对应的告警信息
	 *                  10053 代表每张表对应的告警
	 */
	public void createMointorClintAndService(int alarmCode,int level,String objectInfo,int label) throws Exception{
		CurAlarmService_MB curAlarmService = null;
		List<CurrentAlarmInfo> currentAlarmList = null;
		try {
			curAlarmService = (CurAlarmService_MB) ConstantUtil.serviceFactory.newService_MB(Services.CurrentAlarm);
			CurrentAlarmInfo currentAlarmInfo = userLockAlarm(alarmCode,level,objectInfo);
			if(label  == 0)
			{
				currentAlarmList = curAlarmService.queryClientAlarm(alarmCode, level);	
			}else
			{
				currentAlarmList = curAlarmService.queryClientDISCAlarm(alarmCode, level,currentAlarmInfo.getObjectName());	
			}
			if(currentAlarmList !=null && currentAlarmList.size()>0){
				currentAlarmInfo.setId(currentAlarmList.get(0).getId());
			}
			curAlarmService.saveOrUpdate(currentAlarmInfo);
		} finally{
			UiUtil.closeService_MB(curAlarmService);
			currentAlarmList = null;
		}
	}
	
	/**
	 * 
	 * 如果环境恢复正常,还存在之前上报的告警就将其删除
	 * @param userInst
	 * @param alarmCode
	 * @param level
	 */
	public void deleteMointorClintAndServiceAlarm(int alarmCode,int level,String objectInfo){
		CurAlarmService_MB curAlarmService = null;
		try {
			curAlarmService = (CurAlarmService_MB) ConstantUtil.serviceFactory.newService_MB(Services.CurrentAlarm);
			CurrentAlarmInfo currentAlarmInfo = userLockAlarm(alarmCode,level,objectInfo);
			curAlarmService.deleteCurrentAlarmInfo(currentAlarmInfo);
		} catch (Exception e) {
			ExceptionManage.dispose(e,AlarmTools.class);
		}finally{
			UiUtil.closeService_MB(curAlarmService);
		}
	}
	
	/**
	 * 获取对象名称map
	 */
	private void getObjectNameMap(SiteInst siteInst) {
		this.protectTunnelNameMap.clear();
		this.getProtectTunnelNameMap(siteInst);
		this.portNameMap.clear();
		this.getPortNameMap(siteInst);
//		this.segmentNameMap.clear();
//		this.getSegmentNameMap(siteInst);
		this.tunnelNameMap.clear();
		this.getTunnelNameMap(siteInst);
		this.serviceIdPwNameMap.clear();
		this.getPwNameMap(siteInst);
		this.dualNameMap.clear();
		this.getDualNameMap(siteInst);
	}
	
	private void getProtectTunnelNameMap(SiteInst siteInst) {
		TunnelService_MB tunnelService = null;
		try {
			int siteId = siteInst.getSite_Inst_Id();
			tunnelService = (TunnelService_MB) ConstantUtil.serviceFactory.newService_MB(Services.Tunnel);
			List<Tunnel> tunnelList = tunnelService.select(siteId);
			if(tunnelList != null && !tunnelList.isEmpty()){
				for (Tunnel t : tunnelList) {
					Tunnel protectTunnel = t.getProtectTunnel();
					if(protectTunnel != null && protectTunnel.getTunnelId() > 0){
						String tunnelName = protectTunnel.getTunnelName();
						if(siteId == protectTunnel.getASiteId()){
							this.protectTunnelNameMap.put(protectTunnel.getAprotectId(), tunnelName);
						}else if(siteId == protectTunnel.getZSiteId()){
							this.protectTunnelNameMap.put(protectTunnel.getZprotectId(), tunnelName);
						}
					}
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			UiUtil.closeService_MB(tunnelService);
		}
	}
	
	private void getPortNameMap(SiteInst siteInst){
		PortService_MB portService = null;
		try {
			portService = (PortService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORT);
			PortInst port = new PortInst();
			port.setSiteId(siteInst.getSite_Inst_Id());
			List<PortInst> portList = portService.select(port);
			if(portList != null){
				for (PortInst portInst : portList) {
					if(!portInst.getPortType().equals("system")){
						String name = portInst.getPortName();
						if(name.contains("e1")){
							this.portNameMap.put(portInst.getNumber(), name.substring(0,name.lastIndexOf(".")));
						}else{
							this.portNameMap.put(portInst.getNumber(), name);
						}
					}
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			UiUtil.closeService_MB(portService);
		}
	}
	
//	private void getSegmentNameMap(SiteInst siteInst){
//		PortService portService = null;
//		SegmentService segmentService = null;
//		try {
//			portService = (PortService) ConstantUtil.serviceFactory.newService(Services.PORT);
//			segmentService = (SegmentService) ConstantUtil.serviceFactory.newService(Services.SEGMENT);
//			PortInst port = new PortInst();
//			port.setSiteId(siteInst.getSite_Inst_Id());
//			List<PortInst> portList = portService.select(port);
//			if(portList != null){
//				for (PortInst portInst : portList) {
//					if(portInst.getPortType().contains("NNI")){
//						List<Segment> segmentList = segmentService.
//						selectBySideAndPort(siteInst.getSite_Inst_Id(), portInst.getPortId());
//						if(segmentList.size() > 0){
//							Segment s = segmentList.get(0);
//							this.segmentNameMap.put(portInst.getNumber(), 
//									s.getId()+"/"+s.getNAME());
//						}
//					}
//				}
//			}
//		} catch (Exception e) {
//			ExceptionManage.dispose(e, this.getClass());
//		} finally {
//			UiUtil.closeService(portService);
//			UiUtil.closeService(segmentService);
//		}
//	}
	
	private void getTunnelNameMap(SiteInst siteInst){
		TunnelService_MB tunnelService = null;
		try {
			tunnelService = (TunnelService_MB) ConstantUtil.serviceFactory.newService_MB(Services.Tunnel);
			this.tunnelNameMap = tunnelService.selectTunnelNameBySiteId(siteInst.getSite_Inst_Id());
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			UiUtil.closeService_MB(tunnelService);
		}
	}
	
	private void getPwNameMap(SiteInst siteInst){
		BusinessidService_MB busiService = null;
		PwInfoService_MB pwInfoService = null;
		try {
			busiService = (BusinessidService_MB) ConstantUtil.serviceFactory.newService_MB(Services.BUSINESSID);
			pwInfoService = (PwInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PwInfo);
			Businessid bId = new Businessid();
			bId.setIdStatus(1);
			bId.setSiteId(siteInst.getSite_Inst_Id());
			bId.setType("ethpw");
			List<Businessid> bIdList = busiService.queryByCondition(bId);
			for (Businessid businessid : bIdList) {
				List<PwInfo> pwList = pwInfoService.selectBysiteIdAndServiceId(siteInst.getSite_Inst_Id(),businessid.getIdValue());
				if(pwList != null && pwList.size() > 0){
					this.serviceIdPwNameMap.put(businessid.getIdValue(), pwList.get(0).getPwName());
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			UiUtil.closeService_MB(busiService);
			UiUtil.closeService_MB(pwInfoService);
		}
	}
	
	private void getDualNameMap(SiteInst siteInst){
		DualInfoService_MB dualInfoServiceMB = null;
		PwProtectService_MB pwProtectService_MB = null;
		try {
			dualInfoServiceMB = (DualInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.DUALINFO);
			pwProtectService_MB = (PwProtectService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PWPROTECT);
			PwProtect pwProtect = new PwProtect();
			pwProtect.setSiteId(siteInst.getSite_Inst_Id());
			List<PwProtect> pwProtects = pwProtectService_MB.select(pwProtect);
			for (int i = 0; i < pwProtects.size(); i++) {
				DualInfo dualInfo = dualInfoServiceMB.queryById(pwProtects.get(i).getServiceId());
				if(dualInfo != null && !dualInfo.getName().equals("")){
					this.dualNameMap.put(pwProtects.get(i).getBusinessId(), dualInfo.getName());
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}finally{
			UiUtil.closeService_MB(pwProtectService_MB);
			UiUtil.closeService_MB(dualInfoServiceMB);
		}
	}
}

