package com.nms.db.bean.equipment.port;

import com.nms.ui.frame.ViewDataObj;

public class Port2LayerAttr extends ViewDataObj {
	private static final long serialVersionUID = 7885730994765067850L;
	private int id;//主键id
	private int siteId;//网元id
	private int portId;//端口id
	private int portNumber;//端口号
	private int macEnable = 1;//MAC地址学习使能:0/1= 禁止/使能
	private int macCount;//MAC地址学习条目数:1-512-30000
	private int tpIdType;//入匹配TPID类型:0/1/2/3 = 8100/88a8/9100/9200
	private int vlanCount;//所加入vlan的数目(目前写0,以后会扩展)
	private int portType;//端口类型
	private int pvid;//pvid
	private int qinqEnable;//qinq使能
	private int qinqModel;//qinq模式
	private String vlans;//vlan值
	private String qinqs;//qinq配置
	private int qinqCount;//qinq条目数
	
	@Override
	public void putObjectProperty() {
		
	}
	
	public int getSiteId() {
		return siteId;
	}

	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPortId() {
		return portId;
	}

	public void setPortId(int portId) {
		this.portId = portId;
	}

	public int getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}

	public int getMacEnable() {
		return macEnable;
	}

	public void setMacEnable(int macEnable) {
		this.macEnable = macEnable;
	}

	public int getMacCount() {
		return macCount;
	}

	public void setMacCount(int macCount) {
		this.macCount = macCount;
	}

	public int getTpIdType() {
		return tpIdType;
	}

	public void setTpIdType(int tpIdType) {
		this.tpIdType = tpIdType;
	}

	public int getVlanCount() {
		return vlanCount;
	}

	public void setVlanCount(int vlanCount) {
		this.vlanCount = vlanCount;
	}

	public int getPortType() {
		return portType;
	}

	public void setPortType(int portType) {
		this.portType = portType;
	}

	public int getPvid() {
		return pvid;
	}

	public void setPvid(int pvid) {
		this.pvid = pvid;
	}

	public int getQinqEnable() {
		return qinqEnable;
	}

	public void setQinqEnable(int qinqEnable) {
		this.qinqEnable = qinqEnable;
	}

	public int getQinqModel() {
		return qinqModel;
	}

	public void setQinqModel(int qinqModel) {
		this.qinqModel = qinqModel;
	}

	public String getVlans() {
		return vlans;
	}

	public void setVlans(String vlans) {
		this.vlans = vlans;
	}

	public String getQinqs() {
		return qinqs;
	}

	public void setQinqs(String qinqs) {
		this.qinqs = qinqs;
	}

	public int getQinqCount() {
		return qinqCount;
	}

	public void setQinqCount(int qinqCount) {
		this.qinqCount = qinqCount;
	}

	
}
