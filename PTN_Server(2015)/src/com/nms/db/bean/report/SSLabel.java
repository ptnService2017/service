package com.nms.db.bean.report;

import com.nms.model.equipment.shlef.SiteService_MB;
import com.nms.model.util.Services;
import com.nms.ui.frame.ViewDataObj;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.UiUtil;

public class SSLabel extends ViewDataObj{

	/**
	 * ViewDataObj
	 */
	private static final long serialVersionUID = 4749880765070045305L;

	@Override
	public void putObjectProperty() {
		this.putClientProperty("id",this.getId());
		this.putClientProperty("product",this.getSiteProduct());
		this.putClientProperty("SiteName",this.getSiteName());
		this.putClientProperty("portName",this.getPortName());
		this.putClientProperty("labelType",this.getLabelType());
		this.putClientProperty("lspCount",this.getLspCount());
		this.putClientProperty("lspUsed",this.getLspUsed());
		this.putClientProperty("lspCanUsed",this.getLspCanUsed());
//		this.putClientProperty("pwCount",this.getPwCount());
//		this.putClientProperty("pwUsed",this.getPwUsed());
//		this.putClientProperty("pwCanUsed",this.getPwCanUsed());		
	}
	
	/**
	 * 获取产品类型
	 */
	private String getSiteProduct() {
		SiteService_MB siteService = null;
		try {
			siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE);
			int manufacturer = siteService.getManufacturer(this.getSiteId());
			//1表示是晨晓,0表示是武汉
			if(manufacturer == 1){
				this.product = "700系列";
				return "700系列";
			}else{
				this.product = "700+系列";
				return "700+系列";
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			UiUtil.closeService_MB(siteService);
		}
		return "";
	}

	private String id; // 序号
	private String SiteName; // 网元名称
	private String labelType; // 标签类型
	private String lspCount; // LSP标签总数
	private String lspUsed; // LSP已占用标签数
	private String lspCanUsed; // LSP可用标签数
//	private String pwCount; // PW标签总数
//	private String pwUsed; // PW已占用标签数
//	private String pwCanUsed; // PW可用标签数
	private int siteId;
	private String product;
	private String portName;
	private int portId;
	
	public int getPortId() {
		return portId;
	}

	public void setPortId(int portId) {
		this.portId = portId;
	}

	public String getPortName() {
		return portName;
	}

	public void setPortName(String portName) {
		this.portName = portName;
	}

	public String getProduct() {
		return product;
	}
	
	public void setProduct(String product) {
		this.product = product;
	}

	public int getSiteId() {
		return siteId;
	}
	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSiteName() {
		return SiteName;
	}
	public void setSiteName(String siteName) {
		SiteName = siteName;
	}
	public String getLspCount() {
		return lspCount;
	}
	public void setLspCount(String lspCount) {
		this.lspCount = lspCount;
	}
	public String getLspUsed() {
		return lspUsed;
	}
	public void setLspUsed(String lspUsed) {
		this.lspUsed = lspUsed;
	}
	public String getLspCanUsed() {
		return lspCanUsed;
	}
	public void setLspCanUsed(String lspCanUsed) {
		this.lspCanUsed = lspCanUsed;
	}
//	public String getPwCount() {
//		return pwCount;
//	}
//	public void setPwCount(String pwCount) {
//		this.pwCount = pwCount;
//	}
//	public String getPwUsed() {
//		return pwUsed;
//	}
//	public void setPwUsed(String pwUsed) {
//		this.pwUsed = pwUsed;
//	}
//	public String getPwCanUsed() {
//		return pwCanUsed;
//	}
//	public void setPwCanUsed(String pwCanUsed) {
//		this.pwCanUsed = pwCanUsed;
//	}
	public String getLabelType() {
		return labelType;
	}
	public void setLabelType(String labelType) {
		this.labelType = labelType;
	}
}
