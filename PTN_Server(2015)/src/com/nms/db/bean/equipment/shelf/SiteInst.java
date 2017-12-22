﻿package com.nms.db.bean.equipment.shelf;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.nms.db.bean.system.Field;
import com.nms.db.bean.system.SiteLock;
import com.nms.model.system.FieldService_MB;
import com.nms.model.system.SiteLockService_MB;
import com.nms.model.util.Services;
import com.nms.service.impl.util.SiteUtil;
import com.nms.ui.frame.ViewDataObj;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.ResourceUtil;
import com.nms.ui.manager.UiUtil;
import com.nms.ui.manager.keys.StringKeysObj;

public class SiteInst extends ViewDataObj {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9200948812416613288L;
	private int Site_Inst_Id;
	private String Site_Hum_Id;		//网元ID 武汉用
	private String CellDescribe;	//网元IP
	private String CellId;	//网元名称
	private String CellType;	//网元类型
	private String CellEditon;	//网元厂商 700系列或者700+系列。  关联code	
	private String CellIcccode;	//
	private String CellTPoam;
	private String CellTimeZone;
	private String CellTime;
	private String CellTimeServer;
	private String CellIdentifier;
	private int FieldID;
	private int SiteX;
	private int SiteY;
	private EquipInst equipInst;
	private int type;
	private String swich;
	private String username;
	private String userpwd;
	private int loginstatus;
	private String versions;
	private int isGateway;//是否为网关网元
	private String totalTime;//上电累计时间
	private String coverIP;//掩码
	private String complieTime;//编译时间
	private String softEdition;//软件版本
	private String hardEdition;//硬件版本
	private int currectTime;//校时状态
	private int manageSatus;//管理配置状态
	private int equiplmentStatus;//设备配置状态
	private int siteType;
	private File file;//网元上载/下载时，界面获取的file
	private int manufacturer;//厂商信息  0/1=武汉/晨晓
	private int statusMark;//网元某状态标志
	private String bootTime;//boot编译时间
	private String fpgaTime;//fpag编译时间
	private String plateNumber;//盘号
	private String cardNumber;//卡号
	private String createPlateNumber;//制盘时间
	private String programmeTime;//编程时间
	private int isDeleteTopo=0;	//主拓扑刷新时，验证是否为删除动作  0=不是删除  1=删除     kk
	private String createTime;	//创建时间
	private int parameter;//条目数
	private int isCreateDiscardFlow ;//是否创建了丢弃流 0:默认是创建可丢弃流 /1：是删除了丢弃流
	private byte[] bs;//软件摘要
	private String createUser="";//创建者标识
	private String siteLocation; //网元位置
	private String sn;//SN码
	private String rootIP;//父节点IP
	private String linkPort;//邻居端口信息
    private int alarmReversalModel = 0;//0:不反转；1:人工恢复模式  2:自动恢复模式
	private int neCount;//网元总数量
	private int cardCount;//板卡总数量
	private long l;//系统时间
	private int isAlarmReversal;//是否设置告警反转
	private int isDelayAlarmTrap = 0;//是否设置告警延迟上报
	private String delayTime;//延迟上报的时间
	private int rack;//机架
	private int shelf;//机框
	private String sitePercent;//网元数量占所有网元的百分比
	private String controlType;//控制命令类型
	
	
	//升级属性
	private String result;//升级结果
	private String fileName;//当前升级文件
	private String allFileName;//需升级所有文件
	private int schedule;//升级进度
	private String status;//升级状态
	private String rbootTime;//重启时间 
	private String neMAC;//设备MAC地址
	private boolean isLocationNe;//是否是本地网元

	public String getNeMAC() {
		return neMAC;
	}

	public void setNeMAC(String neMAC) {
		this.neMAC = neMAC;
	}

	public String getControlType() {
		return controlType;
	}

	public void setControlType(String controlType) {
		this.controlType = controlType;
	}


	public String getSitePercent() {
		return sitePercent;
	}


	public void setSitePercent(String sitePercent) {
		this.sitePercent = sitePercent;
	}


	public long getL() {
		return l;
	}


	public void setL(long l) {
		this.l = l;
	}


	public String toString() {
		return new StringBuffer()

		.append("Site_Inst_Id=").append(Site_Inst_Id)

		.append(";Site_Hum_Id=").append(Site_Hum_Id)

		.append(";CellDescribe=").append(CellDescribe)

		.append(";CellId=").append(CellId)

		.append(";CellType=").append(CellType)

		.append(";CellEditon=").append(CellEditon)

		.append(";CellIcccode=").append(CellIcccode)

		.append(";CellTPoam=").append(CellTPoam)

		.append(";CellTimeZone=").append(CellTimeZone)

		.append(";CellTime=").append(CellTime)

		.append(";CellTimeServer=").append(CellTimeServer)

		.append(";CellIdentifier=").append(CellIdentifier)

		.append(";FieldID=").append(FieldID)

		.append(";SiteX=").append(SiteX)

		.append(";SiteY=").append(SiteY)

		.append(";type=").append(type).

		append(";swich=").append(swich).toString();
	}

	
	public String getTotalTime() {
		return totalTime;
	}


	public String getCreateUser() {
		return createUser;
	}


	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}


	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}


	public String getCoverIP() {
		return coverIP;
	}


	public void setCoverIP(String coverIP) {
		this.coverIP = coverIP;
	}


	public String getComplieTime() {
		return complieTime;
	}


	public void setComplieTime(String complieTime) {
		this.complieTime = complieTime;
	}


	public String getSoftEdition() {
		return softEdition;
	}


	public void setSoftEdition(String softEdition) {
		this.softEdition = softEdition;
	}


	public String getHardEdition() {
		return hardEdition;
	}


	public void setHardEdition(String hardEdition) {
		this.hardEdition = hardEdition;
	}


	public int getCurrectTime() {
		return currectTime;
	}


	public void setCurrectTime(int currectTime) {
		this.currectTime = currectTime;
	}


	public int getManageSatus() {
		return manageSatus;
	}


	public void setManageSatus(int manageSatus) {
		this.manageSatus = manageSatus;
	}


	public int getEquiplmentStatus() {
		return equiplmentStatus;
	}


	public void setEquiplmentStatus(int equiplmentStatus) {
		this.equiplmentStatus = equiplmentStatus;
	}


	public int getSite_Inst_Id() {
		return Site_Inst_Id;
	}

	public void setSite_Inst_Id(int siteInstId) {
		Site_Inst_Id = siteInstId;
	}

	public String getSite_Hum_Id() {
		return Site_Hum_Id;
	}

	public void setSite_Hum_Id(String site_Hum_Id) {
		Site_Hum_Id = site_Hum_Id;
	}

	public String getCellDescribe() {
		return CellDescribe;
	}

	public void setCellDescribe(String cellDescribe) {
		CellDescribe = cellDescribe;
	}

	public String getCellId() {
		return CellId;
	}

	public void setCellId(String cellId) {
		CellId = cellId;
	}

	public String getCellType() {
		return CellType;
	}

	public void setCellType(String cellType) {
		CellType = cellType;
	}

	public String getCellEditon() {
		return CellEditon;
	}

	public void setCellEditon(String cellEditon) {
		CellEditon = cellEditon;
	}

	public String getCellIcccode() {
		return CellIcccode;
	}

	public void setCellIcccode(String cellIcccode) {
		CellIcccode = cellIcccode;
	}

	public String getCellTPoam() {
		return CellTPoam;
	}

	public void setCellTPoam(String cellTPoam) {
		CellTPoam = cellTPoam;
	}

	public String getCellTimeZone() {
		return CellTimeZone;
	}

	public void setCellTimeZone(String cellTimeZone) {
		CellTimeZone = cellTimeZone;
	}

	public String getCellTime() {
		return CellTime;
	}

	public void setCellTime(String cellTime) {
		CellTime = cellTime;
	}

	public String getCellTimeServer() {
		return CellTimeServer;
	}

	public void setCellTimeServer(String cellTimeServer) {
		CellTimeServer = cellTimeServer;
	}

	public String getCellIdentifier() {
		return CellIdentifier;
	}

	public void setCellIdentifier(String cellIdentifier) {
		CellIdentifier = cellIdentifier;
	}

	public int getFieldID() {
		return FieldID;
	}

	public void setFieldID(int fieldID) {
		FieldID = fieldID;
	}

	public int getSiteX() {
		return SiteX;
	}

	public void setSiteX(int siteX) {
		SiteX = siteX;
	}

	public int getSiteY() {
		return SiteY;
	}

	public void setSiteY(int siteY) {
		SiteY = siteY;
	}

	public EquipInst getEquipInst() {
		return equipInst;
	}

	public void setEquipInst(EquipInst equipInst) {
		this.equipInst = equipInst;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getSwich() {
		return swich;
	}

	public void setSwich(String swich) {
		this.swich = swich;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserpwd() {
		return userpwd;
	}

	public void setUserpwd(String userpwd) {
		this.userpwd = userpwd;
	}

	public int getLoginstatus() {
		return loginstatus;
	}

	public void setLoginstatus(int loginstatus) {
		this.loginstatus = loginstatus;
	}

	public String getVersions() {
		return versions;
	}

	public void setVersions(String versions) {
		this.versions = versions;
	}

	public int getIsGateway() {
		return isGateway;
	}

	public void setIsGateway(int isGateway) {
		this.isGateway = isGateway;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void putObjectProperty() {
		try {
			getClientProperties().put("id", getSite_Inst_Id());
			getClientProperties().put("neName", getCellId());
			getClientProperties().put("fieldName", this.getFieldName(getFieldID()));
			getClientProperties().put("neType", getCellType());
			SiteUtil siteUtil = new SiteUtil();
			if("0".equals(siteUtil.SiteTypeUtil(this.getSite_Inst_Id())+"")){
				getClientProperties().put("siteState", ResourceUtil.srcStr(StringKeysObj.STRING_ONLINE));
			}else{
				getClientProperties().put("siteState", ResourceUtil.srcStr(StringKeysObj.STRING_OFFLINE));
			}
			String yes = ResourceUtil.srcStr(StringKeysObj.OBJ_YES);
			String no = ResourceUtil.srcStr(StringKeysObj.OBJ_NO);
			getClientProperties().put("administratorType", getIsGateway() == 1 ?  yes : no);
			getClientProperties().put("isCreateFlow", getIsCreateDiscardFlow()== 0 ?  yes : no);
			getClientProperties().put("openCondition", getSwich());
			getClientProperties().put("neDescribe", getCellDescribe());
			getClientProperties().put("username", getUsername());
			getClientProperties().put("userpwd", getUserpwd());
			getClientProperties().put("clearLockEnabled", getSiteLock() == true? yes : no);
			getClientProperties().put("CellEditon", UiUtil.getCodeById(Integer.parseInt(getCellEditon())).getCodeName());
			getClientProperties().put("versions", getVersions());
			getClientProperties().put("CellDescribe", getCellDescribe());
			getClientProperties().put("CellType", getCellType());
			getClientProperties().put("sn", getSn());
			getClientProperties().put("CellID", getSite_Inst_Id());
			getClientProperties().put("allFileName", getAllFileName());
			getClientProperties().put("fileName", getFileName());
			getClientProperties().put("schedule", getSchedule());
			getClientProperties().put("result", getResult());
			getClientProperties().put("rebootTime", getRebootTime(getL()));
                        getClientProperties().put("isLocationNe", isLocationNe?ResourceUtil.srcStr(StringKeysObj.OBJ_YES):"");
			getClientProperties().put("rootSiteIp", getRootIP());
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}

	}
	
	/**
	 * 重启时间
	 * @param l
	 * @return
	 */
	private String getRebootTime(long l){
		String time = "";
		if(l >0){
			Date date = new Date(l);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			time = dateFormat.format(date);
		}
		return time;
		
	}

	private String getFieldName(int fieldID) {
		Field field = null;
		List<Field> list = null;
		FieldService_MB fieldService = null;
		try {
			field = new Field();
			field.setId(fieldID);
			list = new ArrayList<Field>();
		    fieldService = (FieldService_MB) ConstantUtil.serviceFactory.newService_MB(Services.Field);
			list = fieldService.selectByFieldId(fieldID);
			if (list != null && list.size() != 0) {
				return list.get(0).getFieldName();
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, UiUtil.class);
		} finally {
			UiUtil.closeService_MB(fieldService);
		}
		return "";
	}


	private boolean getSiteLock()throws Exception {
		SiteLockService_MB lockService = null;
		SiteInst siteInst = null;
		List<SiteLock> locks = null;
		boolean flag = true;
		try {
			int siteId = getSite_Inst_Id();
			siteInst = new SiteInst();
			siteInst.setSite_Inst_Id(siteId);
			lockService = (SiteLockService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITELOCK);
			locks = lockService.selectSiteLock(siteInst);
			if(1==locks.size()) {
				flag = true;
			}else{
				flag = false;
			}
		}catch(Exception e){
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			UiUtil.closeService_MB(lockService);
		}
		return flag;
	}


	public int getSiteType() {
		return siteType;
	}


	public void setSiteType(int siteType) {
		this.siteType = siteType;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public int getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(int manufacturer) {
		this.manufacturer = manufacturer;
	}


	public int getStatusMark() {
		return statusMark;
	}


	public void setStatusMark(int statusMark) {
		this.statusMark = statusMark;
	}


	public String getBootTime() {
		return bootTime;
	}


	public void setBootTime(String bootTime) {
		this.bootTime = bootTime;
	}


	public String getFpgaTime() {
		return fpgaTime;
	}


	public void setFpgaTime(String fpgaTime) {
		this.fpgaTime = fpgaTime;
	}
	public String getPlateNumber() {
		return plateNumber;
	}


	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}


	public String getCardNumber() {
		return cardNumber;
	}


	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}


	public String getCreatePlateNumber() {
		return createPlateNumber;
	}


	public void setCreatePlateNumber(String createPlateNumber) {
		this.createPlateNumber = createPlateNumber;
	}


	public String getProgrammeTime() {
		return programmeTime;
	}


	public void setProgrammeTime(String programmeTime) {
		this.programmeTime = programmeTime;
	}


	public int getIsDeleteTopo() {
		return isDeleteTopo;
	}


	public void setIsDeleteTopo(int isDeleteTopo) {
		this.isDeleteTopo = isDeleteTopo;
	}


	public String getCreateTime() {
		return createTime;
	}


	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}


	public int getParameter() {
		return parameter;
	}


	public void setParameter(int parameter) {
		this.parameter = parameter;
	}


	public int getIsCreateDiscardFlow() {
		return isCreateDiscardFlow;
	}


	public void setIsCreateDiscardFlow(int isCreateDiscardFlow) {
		this.isCreateDiscardFlow = isCreateDiscardFlow;
	}


	public byte[] getBs() {
		return bs;
	}


	public void setBs(byte[] bs) {
		this.bs = bs;
	}
	
	
	
	public String getSiteLocation()
	{
		return siteLocation;
	}


	public void setSiteLocation(String location)
	{
		this.siteLocation = location;
	}


	public String getSn() {
		return sn;
	}


	public void setSn(String sn) {
		this.sn = sn;
	}


	public String getRootIP() {
		return rootIP;
	}


	public void setRootIP(String rootIP) {
		this.rootIP = rootIP;
	}


	public String getLinkPort() {
		return linkPort;
	}


	public void setLinkPort(String linkPort) {
		this.linkPort = linkPort;
	}

	public void setNeCount(int neCount) {
		this.neCount = neCount;
	}


	public int getCardCount() { 
		return cardCount;
	}


	public void setCardCount(int cardCount) {
		this.cardCount = cardCount;
	}



	public int getAlarmReversalModel() {
		return alarmReversalModel;
	}


	public void setAlarmReversalModel(int alarmReversalModel) {
		this.alarmReversalModel = alarmReversalModel;
	}


	public int getNeCount() {
		return neCount;
	}


	public int getIsDelayAlarmTrap() {
		return isDelayAlarmTrap;
	}


	public void setIsDelayAlarmTrap(int isDelayAlarmTrap) {
		this.isDelayAlarmTrap = isDelayAlarmTrap;
	}


	public int getIsAlarmReversal() {
		return isAlarmReversal;
	}


	public void setIsAlarmReversal(int isAlarmReversal) {
		this.isAlarmReversal = isAlarmReversal;
	}


	public String getDelayTime() {
		return delayTime;
	}


	public void setDelayTime(String delayTime) {
		this.delayTime = delayTime;
	}


	public int getRack() {
		return rack;
	}


	public void setRack(int rack) {
		this.rack = rack;
	}


	public int getShelf() {
		return shelf;
	}


	public void setShelf(int shelf) {
		this.shelf = shelf;
	}


	public String getResult() {
		return result;
	}


	public void setResult(String result) {
		this.result = result;
	}


	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public String getAllFileName() {
		return allFileName;
	}


	public void setAllFileName(String allFileName) {
		this.allFileName = allFileName;
	}


	public int getSchedule() {
		return schedule;
	}


	public void setSchedule(int schedule) {
		this.schedule = schedule;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getRbootTime() {
		return rbootTime;
	}


	public void setRbootTime(String rbootTime) {
		this.rbootTime = rbootTime;
	}

	public boolean isLocationNe() {
		return isLocationNe;
	}


	public void setLocationNe(boolean isLocationNe) {
		this.isLocationNe = isLocationNe;
	}

	
}
