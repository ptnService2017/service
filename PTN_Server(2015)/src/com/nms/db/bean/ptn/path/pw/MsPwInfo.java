package com.nms.db.bean.ptn.path.pw;

import java.util.List;

import com.nms.db.bean.ptn.oam.OamInfo;
import com.nms.db.bean.ptn.qos.QosInfo;
import com.nms.model.equipment.shlef.SiteService_MB;
import com.nms.model.util.Services;
import com.nms.ui.frame.ViewDataObj;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.UiUtil;

public class MsPwInfo extends ViewDataObj implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -501033766877726724L;
	private int id;
	private int siteId;
	private int pwId;
	private int frontTunnelId;
	private int backTunnelId;
	private int frontInlabel;
	private int frontOutlabel;
	private int backInlabel;
	private int backOutlabel;
	private List<OamInfo> oamInfos;
	private List<QosInfo> qosInfos;
	private int mipId;
	public int getSiteId() {
		return siteId;
	}
	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}
	public int getPwId() {
		return pwId;
	}
	public void setPwId(int pwId) {
		this.pwId = pwId;
	}
	public int getFrontTunnelId() {
		return frontTunnelId;
	}
	public void setFrontTunnelId(int frontTunnelId) {
		this.frontTunnelId = frontTunnelId;
	}
	public int getBackTunnelId() {
		return backTunnelId;
	}
	public void setBackTunnelId(int backTunnelId) {
		this.backTunnelId = backTunnelId;
	}
	public int getFrontInlabel() {
		return frontInlabel;
	}
	public void setFrontInlabel(int frontInlabel) {
		this.frontInlabel = frontInlabel;
	}
	public int getFrontOutlabel() {
		return frontOutlabel;
	}
	public void setFrontOutlabel(int frontOutlabel) {
		this.frontOutlabel = frontOutlabel;
	}
	public int getBackInlabel() {
		return backInlabel;
	}
	public void setBackInlabel(int backInlabel) {
		this.backInlabel = backInlabel;
	}
	public int getBackOutlabel() {
		return backOutlabel;
	}
	public void setBackOutlabel(int backOutlabel) {
		this.backOutlabel = backOutlabel;
	}
	public List<OamInfo> getOamInfos() {
		return oamInfos;
	}
	public void setOamInfos(List<OamInfo> oamInfos) {
		this.oamInfos = oamInfos;
	}
	public List<QosInfo> getQosInfos() {
		return qosInfos;
	}
	public void setQosInfos(List<QosInfo> qosInfos) {
		this.qosInfos = qosInfos;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Override
	public void putObjectProperty() {
		SiteService_MB siteService = null;
		try {
			siteService=(SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE);
			this.putClientProperty("siteName", siteService.getSiteName(this.getSiteId()));
			this.putClientProperty("frontInlabel", this.getFrontInlabel());
			this.putClientProperty("frontOutlabel", this.getBackInlabel());
			this.putClientProperty("backInlabel", this.getFrontOutlabel());
			this.putClientProperty("backOutlabel", this.getBackOutlabel());
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			UiUtil.closeService_MB(siteService);
		}
	}
	public int getMipId() {
		return mipId;
	}
	public void setMipId(int mipId) {
		this.mipId = mipId;
	}
}
