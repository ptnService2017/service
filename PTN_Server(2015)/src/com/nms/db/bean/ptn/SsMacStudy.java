﻿package com.nms.db.bean.ptn;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.nms.model.equipment.port.PortService_MB;
import com.nms.model.util.Services;
import com.nms.ui.frame.ViewDataObj;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.UiUtil;

public class SsMacStudy extends ViewDataObj implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1318487589378511002L;
	private int id;
	private int num;
	private int vlan;
	private int portId;
	private int macCount;
	private String macAddress;
	private List<String> macAddressList = new ArrayList<String>();
	private List<CommonBean> commonList = new ArrayList<CommonBean>();
	private int siteId;//设备id
	private String portNa;
	private int lanid;
	private int elanid;
	
	public List<CommonBean> getCommonList() {
		return commonList;
	}

	public void setCommonList(List<CommonBean> commonList) {
		this.commonList = commonList;
	}

	public String getPortNa() {
		return portNa;
	}

	public void setPortNa(String portNa) {
		this.portNa = portNa;
	}

	@Override
	public void putObjectProperty() {
		try {
			
			this.putClientProperty("id", getId());
			this.putClientProperty("num", getNum());
			this.putClientProperty("vlan", getVlan());		
			this.putClientProperty("countNumber", getMacCount());
			this.putClientProperty("portName",getPortName());			
			
			this.putClientProperty("macAddress",getMacAddress());
		
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}

	}
	

	private String getPortName() {
		PortService_MB portService = null;
		try {
			portService = (PortService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORT);
			return portService.selectPortybyid(this.getPortId()).getPortName();
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		finally
		{
			UiUtil.closeService_MB(portService);
		}
		return "";
	}



	public int getNum() {
		return num;
	}






	public void setNum(int num) {
		this.num = num;
	}






	public int getVlan() {
		return vlan;
	}






	public void setVlan(int vlan) {
		this.vlan = vlan;
	}






	public int getMacCount() {
		return macCount;
	}






	public void setMacCount(int macCount) {
		this.macCount = macCount;
	}






	public String getMacAddress() {
		return macAddress;
	}






	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}






	public int getSiteId() {
		return siteId;
	}






	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}






	public int getPortId() {
		return portId;
	}


	public void setPortId(int portId) {
		this.portId = portId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<String> getMacAddressList() {
		return macAddressList;
	}

	public void setMacAddressList(List<String> macAddressList) {
		this.macAddressList = macAddressList;
	}

	public int getLanid() {
		return lanid;
	}

	public void setLanid(int lanid) {
		this.lanid = lanid;
	}

	public int getElanid() {
		return elanid;
	}

	public void setElanid(int elanid) {
		this.elanid = elanid;
	}



}
