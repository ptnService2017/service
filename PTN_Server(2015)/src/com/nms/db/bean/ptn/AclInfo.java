package com.nms.db.bean.ptn;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.nms.db.bean.equipment.port.PortInst;
import com.nms.model.equipment.port.PortService_MB;
import com.nms.model.util.Services;
import com.nms.ui.frame.ViewDataObj;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.UiUtil;

/**
 * @author zk
 *ACl配置管理 对象
 */
public class AclInfo extends ViewDataObj implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -394168399212048397L;
	
	private int id ;
	private int siteId ; 
	private int act ; //动作：  0/1   = 允许：拒绝
	private int direction ; //入出方向： 0/1 = 入方向/出方向
	private int portNumber ; //端口号:0/1/2/…/6 =无/WAN1/WAN2/LAN1/LAN2/LAN3/LAN4
	private int vlanId; // vlanID：   1-4094
	private int isSourceMac  ; // 是否匹配源MAC地址
	private String sourceMac = "" ; //源MAC地址
	private int isPurposeMac ; //是否匹目的MAC地址:
	private String purposeMac = ""; //源MAC地址
	private int isSourceIp ; // 是否匹配源IP地址
	private String sourceIp = ""; //源ip地址
	private int isPurposeIp ; //是否匹目的IP地址:
	private String purposeIp= "" ; //源IP地址
	private int isMatchCos ; //是否匹配cos：        0/1=否/是
	private int cosValue ; //cos值 ：             0-7
	private int isMatchDSCP ; //是否匹配DSCP        0/1=否/是
	private int dscpValue ; //DSCP                0-63
	private int isMatchTOS ; //是否匹配TOS         0/1=否/是
	private int tosValue ; //TOS                 0-7
	private int isSourcePort ; // 是否匹配源端口：     0/1=否/是
	private int sourcePort ; //源端口号            1-65535
	private int isPurposePort ; //是否匹配目的端口：   0/1=否/是
	private int  purposePort ; //目的端口号          1-65535
	private int ruleObject ; //字节43	规则应用对象：0/1/2=端口/vlan/端口+vlan
	private int treatyType ; //传输层协议类型 0/1/2=无/TCP/UDP
	
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
	public int getAct() {
		return act;
	}
	public void setAct(int act) {
		this.act = act;
	}
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	public int getPortNumber() {
		return portNumber;
	}
	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}
	public int getVlanId() {
		return vlanId;
	}
	public void setVlanId(int vlanId) {
		this.vlanId = vlanId;
	}
	public int getIsSourceMac() {
		return isSourceMac;
	}
	public void setIsSourceMac(int isSourceMac) {
		this.isSourceMac = isSourceMac;
	}
	public String getSourceMac() {
		return sourceMac;
	}
	public void setSourceMac(String sourceMac) {
		this.sourceMac = sourceMac;
	}
	public int getIsPurposeMac() {
		return isPurposeMac;
	}
	public void setIsPurposeMac(int isPurposeMac) {
		this.isPurposeMac = isPurposeMac;
	}
	public String getPurposeMac() {
		return purposeMac;
	}
	public void setPurposeMac(String purposeMac) {
		this.purposeMac = purposeMac;
	}
	public int getIsSourceIp() {
		return isSourceIp;
	}
	public void setIsSourceIp(int isSourceIp) {
		this.isSourceIp = isSourceIp;
	}
	public String getSourceIp() {
		return sourceIp;
	}
	public void setSourceIp(String sourceIp) {
		this.sourceIp = sourceIp;
	}
	public int getIsPurposeIp() {
		return isPurposeIp;
	}
	public void setIsPurposeIp(int isPurposeIp) {
		this.isPurposeIp = isPurposeIp;
	}
	public String getPurposeIp() {
		return purposeIp;
	}
	public void setPurposeIp(String purposeIp) {
		this.purposeIp = purposeIp;
	}
	public int getIsMatchCos() {
		return isMatchCos;
	}
	public void setIsMatchCos(int isMatchCos) {
		this.isMatchCos = isMatchCos;
	}
	public int getCosValue() {
		return cosValue;
	}
	public void setCosValue(int cosValue) {
		this.cosValue = cosValue;
	}
	public int getIsMatchDSCP() {
		return isMatchDSCP;
	}
	public void setIsMatchDSCP(int isMatchDSCP) {
		this.isMatchDSCP = isMatchDSCP;
	}
	public int getDscpValue() {
		return dscpValue;
	}
	public void setDscpValue(int dscpValue) {
		this.dscpValue = dscpValue;
	}
	public int getIsMatchTOS() {
		return isMatchTOS;
	}
	public void setIsMatchTOS(int isMatchTOS) {
		this.isMatchTOS = isMatchTOS;
	}
	public int getTosValue() {
		return tosValue;
	}
	public void setTosValue(int tosValue) {
		this.tosValue = tosValue;
	}
	public int getIsSourcePort() {
		return isSourcePort;
	}
	public void setIsSourcePort(int isSourcePort) {
		this.isSourcePort = isSourcePort;
	}
	public int getSourcePort() {
		return sourcePort;
	}
	public void setSourcePort(int sourcePort) {
		this.sourcePort = sourcePort;
	}
	public int getIsPurposePort() {
		return isPurposePort;
	}
	public void setIsPurposePort(int isPurposePort) {
		this.isPurposePort = isPurposePort;
	}
	public int getPurposePort() {
		return purposePort;
	}
	public void setPurposePort(int purposePort) {
		this.purposePort = purposePort;
	}
	public int getRuleObject() {
		return ruleObject;
	}
	public void setRuleObject(int ruleObject) {
		this.ruleObject = ruleObject;
	}
	
	public int getTreatyType() {
		return treatyType;
	}
	public void setTreatyType(int treatyType) {
		this.treatyType = treatyType;
	}
	@Override
	public void putObjectProperty() {
		try {
			this.putClientProperty("portName", this.selectProt(this.getPortNumber()+"", siteId, 0));
			this.putClientProperty("vlanID", getVlanId()+"");
			this.putClientProperty("ruleObject", UiUtil.getCodeByValue("ACLRULEOBJECT", this.getRuleObject()+"").getCodeName());
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
	}
	
	private String selectProt(String number,int siteId,int portId) {
		PortService_MB portService = null;
		PortInst portInst = null;
		List<PortInst> allportInstList = null;
		String portName = null;
		try {
			allportInstList=new ArrayList<PortInst>();
			portService = (PortService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORT);
			portInst = new PortInst();
			portInst.setSiteId(siteId);
			portInst.setNumber(Integer.parseInt(number));
			portInst.setPortId(portId);
			allportInstList = portService.select(portInst);
			if(allportInstList!=null&&allportInstList.size()>0){
				 portName = allportInstList.get(0).getPortName();
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			UiUtil.closeService_MB(portService);
			portInst = null;
			allportInstList = null;
		}
		return portName;
	}
}
