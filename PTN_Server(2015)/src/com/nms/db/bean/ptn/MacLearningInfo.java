package com.nms.db.bean.ptn;

import java.io.Serializable;
import java.util.List;

import com.nms.db.bean.equipment.port.PortInst;
import com.nms.model.equipment.port.PortService_MB;
import com.nms.model.util.Services;
import com.nms.ui.frame.ViewDataObj;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.ResourceUtil;
import com.nms.ui.manager.UiUtil;
import com.nms.ui.manager.keys.StringKeysLbl;
import com.nms.ui.manager.keys.StringKeysTip;

public class MacLearningInfo extends ViewDataObj implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2637445145868291127L;
	
	private int id;
	private int siteId;
	private int portId;
	private int macModel;//MAC学习限制模式：0/1/2=无限制/基于端口 /基于端口+VLAN(默认0)
	private int vlanId;//Vlan值：1~4094（默认1）
	private int macCount;//MAC地址学习限制数目：（0~255）（默认0）
	
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
	public int getMacModel() {
		return macModel;
	}
	public void setMacModel(int macModel) {
		this.macModel = macModel;
	}
	public int getVlanId() {
		return vlanId;
	}
	public void setVlanId(int vlanId) {
		this.vlanId = vlanId;
	}
	public int getMacCount() {
		return macCount;
	}
	public void setMacCount(int macCount) {
		this.macCount = macCount;
	}
	@Override
	public void putObjectProperty() {
		this.putClientProperty("id", getId());
		this.putClientProperty("portId", this.getportName());
		this.putClientProperty("macModel", this.getModel());
		this.putClientProperty("vlanId", getVlanId());
		this.putClientProperty("macCount", getMacCount());
	}
	
	private String getportName() {
		List<PortInst> portList = null;
		PortService_MB service = null;
		PortInst port = null;
		try {
			if(portList == null){
				port = new PortInst();
				port.setSiteId(ConstantUtil.siteId);
				service = (PortService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORT);
				portList = service.select(port);
			}
			
			for (PortInst portInst : portList) {
				if(portInst.getPortId() == this.getPortId()){
					return portInst.getPortName();
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			UiUtil.closeService_MB(service);
		}
		return null;
	}
	
	private String getModel() {
		if(this.getMacModel() == 0){
			return ResourceUtil.srcStr(StringKeysLbl.LBL_PORT_EXPORT_SPEED_NO_LIMIT);
		}
		if(this.getMacModel() == 1){
			return ResourceUtil.srcStr(StringKeysTip.TIP_BASE_PORT);
		}
		if(this.getMacModel() == 2){
			return ResourceUtil.srcStr(StringKeysTip.TIP_BASE_PORT_VLAN);
		}
		return "";
	}
	
}
