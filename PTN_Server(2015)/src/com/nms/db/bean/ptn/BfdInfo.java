package com.nms.db.bean.ptn;

import com.nms.ui.frame.ViewDataObj;
import com.nms.ui.manager.ExceptionManage;


public class BfdInfo extends ViewDataObj {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1318487589378511002L;
	private int id;
	private int siteId;
	private int bfdId;
	private int bfdEnabel;//BFD会话使能 0/1=关/开
	private int testMode;//检测方式 0/1=单跳检测/多跳检测
	private int bfdMessageSendType;//BFD报文发帧类型：比特7+比特6:00/01=BFD for IP MPLS/ BFD for MPLS-TP比特0~比特5：0/1/2= IP/LSP/PW+LSP
	private int vlanPriority;//vlan优先级1-7
    private int vlanId;//vlanId 1-2-4095
    private int serviceType;//服务类型
    private String localIp;//本地IP地址
    private String peerIp;// 远端IP地址
    private int udpPort;//UDP源端口
    private int mySid;//本地回话标识符
    private int peerStudyEnabel;//对端会话标识符自学习使能：0/1=不使能/使能
    private int peerSid;//对端会话标识符
    private int dmti;//发送最小周期
    private int rmri;//接收最小周期
	private int dectMul;//检测倍数DECT_MUL：1-3-255
	private int pwBfdStyle;//BFD FOR PW时BFD封装方式：0/1/2/3=PW-ACH封装/IPv4-UDP封装/IPv6-UDP封装（暂不支持ipv6）/CCTYPE2封装（三层标签）
    private int pwTtl;//PW TTL值 0-255
    private int tLayelPortMark;//二层端口BFD标识：0表示不是二层端口BFD，1表示是二层端口BFD
	private int lspId;
	private int pwId;
	private String lspName;
	private String pwName;
	

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

	public int getBfdId() {
		return bfdId;
	}

	public void setBfdId(int bfdId) {
		this.bfdId = bfdId;
	}

	public String getPwName() {
		return pwName;
	}

	public void setPwName(String pwName) {
		this.pwName = pwName;
	}

	public String getLspName() {
		return lspName;
	}

	public void setLspName(String lspName) {
		this.lspName = lspName;
	}

	public int getBfdEnabel() {
		return bfdEnabel;
	}

	public void setBfdEnabel(int bfdEnabel) {
		this.bfdEnabel = bfdEnabel;
	}

	public int getTestMode() {
		return testMode;
	}

	public void setTestMode(int testMode) {
		this.testMode = testMode;
	}

	public int getBfdMessageSendType() {
		return bfdMessageSendType;
	}

	public void setBfdMessageSendType(int bfdMessageSendType) {
		this.bfdMessageSendType = bfdMessageSendType;
	}


	public int getVlanPriority() {
		return vlanPriority;
	}

	public void setVlanPriority(int vlanPriority) {
		this.vlanPriority = vlanPriority;
	}

	public int getVlanId() {
		return vlanId;
	}

	public void setVlanId(int vlanId) {
		this.vlanId = vlanId;
	}

	public int getServiceType() {
		return serviceType;
	}

	public void setServiceType(int serviceType) {
		this.serviceType = serviceType;
	}

	public String getLocalIp() {
		return localIp;
	}

	public void setLocalIp(String localIp) {
		this.localIp = localIp;
	}

	public String getPeerIp() {
		return peerIp;
	}

	public void setPeerIp(String peerIp) {
		this.peerIp = peerIp;
	}

	public int getUdpPort() {
		return udpPort;
	}

	public void setUdpPort(int udpPort) {
		this.udpPort = udpPort;
	}
	
	public int getMySid() {
		return mySid;
	}

	public void setMySid(int mySid) {
		this.mySid = mySid;
	}

	public int getPeerStudyEnabel() {
		return peerStudyEnabel;
	}

	public void setPeerStudyEnabel(int peerStudyEnabel) {
		this.peerStudyEnabel = peerStudyEnabel;
	}

	public int getPeerSid() {
		return peerSid;
	}

	public void setPeerSid(int peerSid) {
		this.peerSid = peerSid;
	}

	public int getDmti() {
		return dmti;
	}

	public void setDmti(int dmti) {
		this.dmti = dmti;
	}

	public int getRmri() {
		return rmri;
	}

	public void setRmri(int rmri) {
		this.rmri = rmri;
	}

	public int getDectMul() {
		return dectMul;
	}

	public void setDectMul(int dectMul) {
		this.dectMul = dectMul;
	}

	public int getPwBfdStyle() {
		return pwBfdStyle;
	}

	public void setPwBfdStyle(int pwBfdStyle) {
		this.pwBfdStyle = pwBfdStyle;
	}

	public int getPwTtl() {
		return pwTtl;
	}

	public void setPwTtl(int pwTtl) {
		this.pwTtl = pwTtl;
	}

	public int gettLayelPortMark() {
		return tLayelPortMark;
	}

	public void settLayelPortMark(int tLayelPortMark) {
		this.tLayelPortMark = tLayelPortMark;
	}
	
	public int getLspId() {
		return lspId;
	}
	
	public void setLspId(int lspId) {
		this.lspId = lspId;
	}
	
	public int getPwId() {
		return pwId;
	}

	public void setPwId(int pwId) {
		this.pwId = pwId;
	}

	
	

	@Override
	public void putObjectProperty() {
		// TODO Auto-generated method stub
		try {
//			this.putClientProperty("id", this.getId());
//			this.putClientProperty("macId", this.getMacId());
//			this.putClientProperty("vlan",this.getVlan());
//			this.putClientProperty("portName",getPortName());
//			this.putClientProperty("macAddress", this.getMacAddress());
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
	}




//	private String getPortName() {
//		PortService portService = null;
//		try {
//			portService = (PortService) ConstantUtil.serviceFactory.newService(Services.PORT);
//			return portService.selectPortybyid(this.getPortId()).getPortName();
//		} catch (Exception e) {
//			ExceptionManage.dispose(e, this.getClass());
//		}
//		finally
//		{
//			UiUtil.closeService(portService);
//		}
//		return "";
//	}






	





	



}
