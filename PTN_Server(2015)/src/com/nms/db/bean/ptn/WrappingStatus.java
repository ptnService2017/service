package com.nms.db.bean.ptn;

import com.nms.db.bean.equipment.port.PortInst;
import com.nms.db.bean.ptn.path.protect.LoopProtectInfo;
import com.nms.model.equipment.port.PortService_MB;
import com.nms.model.ptn.path.protect.WrappingProtectService_MB;
import com.nms.model.util.Services;
import com.nms.ui.frame.ViewDataObj;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.UiUtil;

public class WrappingStatus extends ViewDataObj{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4056232019252094912L;
	private int wrappingId;//环id
	private int westPort;//西向端口
	private int eastPort;//东向端口
	private int protectType;//保护类型
	private int rorateStatus;//倒换状态
	private int totalNumber;//节点总数
	private int localNumber;//本站节点id
	private int westAlarm;//西向线路告警
	private int eastAlarm;//东向线路告警
	private int westReceiveAps;//西向收aps信息
	private int eastReceiveAps;//东向收aps信息
	private int westSendAps;//西向发aps信息
	private int eastSendAps;//东向发aps信息
	private int delayTime;//拖延时间
	private int backType;
	@Override
	public void putObjectProperty() {
		this.putClientProperty("name",this.getWrappingName());
		this.putClientProperty("westPort",getPortName(getWestPort()));
		this.putClientProperty("eastPort",getPortName(getEastPort()));
		this.putClientProperty("protectType",this.getgetProtType());
		this.putClientProperty("totalNumber",getTotalNumber());
		this.putClientProperty("localNumber",getLocalNumber());
		this.putClientProperty("westAlarm",this.getAlarmStatus(getWestAlarm()));
		this.putClientProperty("eastAlarm",this.getAlarmStatus(getEastAlarm()));
		this.putClientProperty("westReceiveAps",Integer.toHexString(getWestReceiveAps()).toUpperCase());
		this.putClientProperty("eastReceiveAps",Integer.toHexString(getEastReceiveAps()).toUpperCase());
		this.putClientProperty("westSendAps",Integer.toHexString(getWestSendAps()).toUpperCase());
		this.putClientProperty("eastSendAps",Integer.toHexString(getEastSendAps()).toUpperCase());
		this.putClientProperty("delayTime",getDelayTime());
		try {
			this.putClientProperty("rorateStatus",(UiUtil.getCodeByValue("WRAPPINGSTATUS", this.getRorateStatus()+"")).getCodeName());
			this.putClientProperty("backType", (UiUtil.getCodeByValue("BACKTYPE", this.getBackType()+"")).getCodeName());
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
	}
	
	private String getAlarmStatus(int alarm) {
		if(alarm == 0){
			return "无";
		}else{
			return "有";
		}
	}

	private String getWrappingName() {
		WrappingProtectService_MB protService=null;
		try {
			protService = (WrappingProtectService_MB) ConstantUtil.serviceFactory.newService_MB(Services.WRAPPINGPROTECT);	
			LoopProtectInfo prot = new LoopProtectInfo();
			prot.setLoopId(this.getWrappingId());
			return protService.select(prot).get(0).getName();
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}finally
		{
			UiUtil.closeService_MB(protService);
		}
		return "";
	}

	private String getgetProtType() {
		if(protectType == 0){
			return "无保护";
		}else if(protectType == 1){
			return "Wrapping_V1";
		}else if(protectType == 2){
			return "Wrapping_V2";
		}
		return "";
	}
	
	private String getPortName(int number){
		String portName = "";
		PortService_MB portService=null;
		try {
			PortInst portInst = new PortInst();
			portService = (PortService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORT);
			portInst.setNumber(number);
			portInst.setSiteId(ConstantUtil.siteId);
			portInst = portService.select(portInst).get(0);
			portName = portInst.getPortName();
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}finally
		{
			UiUtil.closeService_MB(portService);
		}
		return portName;
	}
	
	public int getWrappingId() {
		return wrappingId;
	}
	public void setWrappingId(int wrappingId) {
		this.wrappingId = wrappingId;
	}
	public int getWestPort() {
		return westPort;
	}
	public void setWestPort(int westPort) {
		this.westPort = westPort;
	}
	public int getEastPort() {
		return eastPort;
	}
	public void setEastPort(int eastPort) {
		this.eastPort = eastPort;
	}
	public int getProtectType() {
		return protectType;
	}
	public void setProtectType(int protectType) {
		this.protectType = protectType;
	}
	public int getRorateStatus() {
		return rorateStatus;
	}
	public void setRorateStatus(int rorateStatus) {
		this.rorateStatus = rorateStatus;
	}
	public int getTotalNumber() {
		return totalNumber;
	}
	public void setTotalNumber(int totalNumber) {
		this.totalNumber = totalNumber;
	}
	public int getLocalNumber() {
		return localNumber;
	}
	public void setLocalNumber(int localNumber) {
		this.localNumber = localNumber;
	}
	public int getWestAlarm() {
		return westAlarm;
	}
	public void setWestAlarm(int westAlarm) {
		this.westAlarm = westAlarm;
	}
	public int getEastAlarm() {
		return eastAlarm;
	}
	public void setEastAlarm(int eastAlarm) {
		this.eastAlarm = eastAlarm;
	}
	public int getWestReceiveAps() {
		return westReceiveAps;
	}
	public void setWestReceiveAps(int westReceiveAps) {
		this.westReceiveAps = westReceiveAps;
	}
	public int getEastReceiveAps() {
		return eastReceiveAps;
	}
	public void setEastReceiveAps(int eastReceiveAps) {
		this.eastReceiveAps = eastReceiveAps;
	}
	public int getWestSendAps() {
		return westSendAps;
	}
	public void setWestSendAps(int westSendAps) {
		this.westSendAps = westSendAps;
	}
	public int getEastSendAps() {
		return eastSendAps;
	}
	public void setEastSendAps(int eastSendAps) {
		this.eastSendAps = eastSendAps;
	}
	public int getDelayTime() {
		return delayTime;
	}
	public void setDelayTime(int delayTime) {
		this.delayTime = delayTime;
	}
	public int getBackType() {
		return backType;
	}
	public void setBackType(int backType) {
		this.backType = backType;
	}
	
}
