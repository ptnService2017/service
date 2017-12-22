package com.nms.db.bean.ptn.clock;

import java.io.Serializable;

import com.nms.model.equipment.port.PortService_MB;
import com.nms.model.util.Services;
import com.nms.ui.frame.ViewDataObj;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.ResourceUtil;
import com.nms.ui.manager.UiUtil;
import com.nms.ui.manager.keys.StringKeysObj;

@SuppressWarnings("serial")
public class PortConfigInfo extends ViewDataObj implements Serializable{
	private int id;// id
	private int siteId;// 关联网元表的主键ID
	private int port;// 端口
	private int portEnable;// 端口使能
	private String clockModel;// 时钟模式
	private int delayMechanism;// 延时机制
	private String vlanID;// Vlan ID
	private int operationMode;// 操作模式
	private String portStatus;// 端口状态
	private int timeStampMode;// 时间戳模式
	private int AnncPacketsInterval;// Annc报文间隔
	private String AnncTimeoutSetting;// Annc超时设置
	private int SyncPacketsInterval;// Sync报文间隔
	private int Delay_ReqPacketsInterval;// Delay_Req报文间隔
	private int Pdel_ReqPacketsInterval;// Pdel_Req报文间隔
	private String lineDelayCompensation;// 线路延时补偿
	private String delayCompensationMeasure;// 透延时补偿测量值
	private String portMapping;// 端口Map表
	private int interfaceType;//接口类型


	@Override
	public void putObjectProperty() {
		PortService_MB portService = null;
		try {
			portService = (PortService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORT);
			this.putClientProperty("id", getId());
			this.putClientProperty("port", portService.getPortname(getPort()));
//			this.putClientProperty("port", UiUtil.getCodeById(this.getPort()).getCodeName());
			this.putClientProperty("portEnable", this.getPortEnable()==0?ResourceUtil.srcStr(StringKeysObj.ALLCONFIG_FID_ENABLED_NO) : ResourceUtil.srcStr(StringKeysObj.ALLCONFIG_FID_ENABLED));
			this.putClientProperty("clockModel",UiUtil.getCodeByValue("interfaceType",this.getInterfaceType()+"").getCodeName());
			//timeStampMode
			this.putClientProperty("delayMechanism", UiUtil.getCodeById(this.getDelayMechanism()).getCodeName());
			this.putClientProperty("vlanID", getVlanID());
			if(this.getOperationMode()>0){
				this.putClientProperty("operationMode", UiUtil.getCodeById(this.getOperationMode()).getCodeName());
			}		
			this.putClientProperty("portStatus", this.getPortStatus());
			this.putClientProperty("timeStampMode",UiUtil.getCodeByValue("timeStampMode",this.getTimeStampMode()+"").getCodeName());
			this.putClientProperty("AnncPacketsInterval", UiUtil.getCodeByValue("PTPTimePort",getAnncPacketsInterval()+"").getCodeName());
			//this.putClientProperty("AnncPacketsInterval", UiUtil.getCodeById(this.getAnncPacketsInterval()).getCodeName());
			//UiUtil.getCodeByValue("delayMechanism",ptpPort.getDelaymechanism()).getId()
			this.putClientProperty("AnncTimeoutSetting", this.getAnncTimeoutSetting());
			this.putClientProperty("SyncPacketsInterval",  UiUtil.getCodeByValue("SyncPtpPort",this.getSyncPacketsInterval()+"").getCodeName());
			this.putClientProperty("Delay_ReqPacketsInterval" ,UiUtil.getCodeByValue("PTPTimePort",this.getDelay_ReqPacketsInterval()+"").getCodeName());
			this.putClientProperty("Pdel_ReqPacketsInterval", UiUtil.getCodeByValue("PTPTimePort",this.getPdel_ReqPacketsInterval()+"").getCodeName());
			this.putClientProperty("lineDelayCompensation", getLineDelayCompensation());
			this.putClientProperty("delayCompensationMeasure", getDelayCompensationMeasure());
			this.putClientProperty("portMapping", getPortMapping());
			if(0!=this.getInterfaceType()){
				this.putClientProperty("interfaceType", UiUtil.getCodeByValue("interfaceType",this.getInterfaceType()+"").getCodeName());
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			UiUtil.closeService_MB(portService);
		}
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getSiteId() {
		return siteId;
	}


	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}


	public int getPort() {
		return port;
	}


	public void setPort(int port) {
		this.port = port;
	}


	public int getPortEnable() {
		return portEnable;
	}


	public void setPortEnable(int portEnable) {
		this.portEnable = portEnable;
	}


	public String getClockModel() {
		return clockModel;
	}


	public void setClockModel(String clockModel) {
		this.clockModel = clockModel;
	}


	public int getDelayMechanism() {
		return delayMechanism;
	}


	public void setDelayMechanism(int delayMechanism) {
		this.delayMechanism = delayMechanism;
	}


	public String getVlanID() {
		return vlanID;
	}


	public void setVlanID(String vlanID) {
		this.vlanID = vlanID;
	}


	public int getOperationMode() {
		return operationMode;
	}


	public void setOperationMode(int operationMode) {
		this.operationMode = operationMode;
	}


	public String getPortStatus() {
		return portStatus;
	}


	public void setPortStatus(String portStatus) {
		this.portStatus = portStatus;
	}


	public int getTimeStampMode() {
		return timeStampMode;
	}


	public void setTimeStampMode(int timeStampMode) {
		this.timeStampMode = timeStampMode;
	}


	public int getAnncPacketsInterval() {
		return AnncPacketsInterval;
	}


	public void setAnncPacketsInterval(int anncPacketsInterval) {
		AnncPacketsInterval = anncPacketsInterval;
	}


	public String getAnncTimeoutSetting() {
		return AnncTimeoutSetting;
	}


	public void setAnncTimeoutSetting(String anncTimeoutSetting) {
		AnncTimeoutSetting = anncTimeoutSetting;
	}


	public int getSyncPacketsInterval() {
		return SyncPacketsInterval;
	}


	public void setSyncPacketsInterval(int syncPacketsInterval) {
		SyncPacketsInterval = syncPacketsInterval;
	}


	public int getDelay_ReqPacketsInterval() {
		return Delay_ReqPacketsInterval;
	}


	public void setDelay_ReqPacketsInterval(int delayReqPacketsInterval) {
		Delay_ReqPacketsInterval = delayReqPacketsInterval;
	}


	public int getPdel_ReqPacketsInterval() {
		return Pdel_ReqPacketsInterval;
	}


	public void setPdel_ReqPacketsInterval(int pdelReqPacketsInterval) {
		Pdel_ReqPacketsInterval = pdelReqPacketsInterval;
	}


	public String getLineDelayCompensation() {
		return lineDelayCompensation;
	}


	public void setLineDelayCompensation(String lineDelayCompensation) {
		this.lineDelayCompensation = lineDelayCompensation;
	}


	public String getDelayCompensationMeasure() {
		return delayCompensationMeasure;
	}


	public void setDelayCompensationMeasure(String delayCompensationMeasure) {
		this.delayCompensationMeasure = delayCompensationMeasure;
	}


	public String getPortMapping() {
		return portMapping;
	}


	public void setPortMapping(String portMapping) {
		this.portMapping = portMapping;
	}


	public int getInterfaceType() {
		return interfaceType;
	}


	public void setInterfaceType(int interfaceType) {
		this.interfaceType = interfaceType;
	}

	
}
