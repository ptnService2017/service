package com.nms.db.bean.ptn.path;

import java.util.ArrayList;
import java.util.List;

import com.nms.db.bean.ptn.path.pw.PwInfo;
import com.nms.db.bean.ptn.path.pw.PwNniInfo;
import com.nms.db.bean.ptn.port.AcPortInfo;
import com.nms.ui.frame.ViewDataObj;

public class ServiceInfo extends ViewDataObj{

	private static final long serialVersionUID = -3713404901786745006L;
	private int id;
	private int serviceId;
	private int serviceType;
	private int activeStatus;
	
	//对于CES业务，aAC,zAC存的是两端的时隙或是e1口
	private int aAcId;//在Etree中代表 根节点端ac主键ID
	private int zAcId;//
	
	//存放pw对应的NNI端口
	private int aportId;
	private int zportId;
	
	private String name;
	private String createTime;
	private String createUser;
	private String ASiteName;
	private String ZSiteName;
	
	private int aSiteId;
	private int zSiteId;
	
	private int isSingle;	//是否为单网元配置
	private boolean node = false;

	private int cestype;
	private String jobStatus;
	private int clientId;	
	
	private int action=0;	//修改时此条数据的动作  0=没有改变pw、AC  1=改变了pw、ac  2=新增数据   3=删除数据
	private PwInfo beforePw=null;	//修改之前的pw对象
	
//	private AcPortInfo beforeAAc=null;	//修改之前的a端AC对象
	private List<AcPortInfo> beforeAAcList = null;
	private List<AcPortInfo> beforeZAcList = null;
//	private AcPortInfo beforeZAc=null;	//修改之前的z端AC对象
	
	//corba新建业务时要创建的AC对象。
	private AcPortInfo createAc_a=null;
	private AcPortInfo createAc_z=null;
	
	private int pwId;//pw 主键ID
	
	private int beforeActiveStatus;	//修改之前的激活状态
	
	private List<PwNniInfo> pwNniList = new ArrayList<PwNniInfo>();//一致性检测需要用到
	private List<AcPortInfo> acPortList = new ArrayList<AcPortInfo>();//一致性检测需要用到
	
	private String amostAcId;//在创建Elan和etree选择多AC是的ac ID 值
	private String zmostAcId;//在创建Elan和etree选择多AC是的ac ID 值
	
	private int siteId = 0;//在elan删除一个节点的网元ID
	
	//对于双规
	private int branchMainSite =0;
	private int branchProtectSite=0;
	private String activatingTime;// 激活时间
	
	public String getActivatingTime() {
		return activatingTime;
	}

	public void setActivatingTime(String activatingTime) {
		this.activatingTime = activatingTime;
	}
	
	public List<PwNniInfo> getPwNniList() {
		return pwNniList;
	}
	/**
	 * @return the branchProtectSite
	 */
	public int getBranchProtectSite() {
		return branchProtectSite;
	}
	/**
	 * @param branchProtectSite the branchProtectSite to set
	 */
	public void setBranchProtectSite(int branchProtectSite) {
		this.branchProtectSite = branchProtectSite;
	}
	/**
	 * @return the branchMainSite
	 */
	public int getBranchMainSite() {
		return branchMainSite;
	}
	/**
	 * @param branchMainSite the branchMainSite to set
	 */
	public void setBranchMainSite(int branchMainSite) {
		this.branchMainSite = branchMainSite;
	}
	public void setPwNniList(List<PwNniInfo> pwNniList) {
		this.pwNniList = pwNniList;
	}
	public List<AcPortInfo> getAcPortList() {
		return acPortList;
	}
	public void setAcPortList(List<AcPortInfo> acPortList) {
		this.acPortList = acPortList;
	}
	public int getCestype() {
		return cestype;
	}
	public void setCestype(int cestype) {
		this.cestype = cestype;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getServiceId() {
		return serviceId;
	}
	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}		
	@Override
	public String getName() {
		return name;
	}
	@Override
	public void setName(String name) {
		this.name = name;
	}
	public int getServiceType() {
		return serviceType;
	}
	public void setServiceType(int serviceType) {
		this.serviceType = serviceType;
	}
	public int getActiveStatus() {
		return activeStatus;
	}
	public void setActiveStatus(int activeStatus) {
		this.activeStatus = activeStatus;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public int getaAcId() {
		return aAcId;
	}
	public void setaAcId(int aAcId) {
		this.aAcId = aAcId;
	}
	public int getzAcId() {
		return zAcId;
	}
	public void setzAcId(int zAcId) {
		this.zAcId = zAcId;
	}
	public int getAportId() {
		return aportId;
	}
	public void setAportId(int aportId) {
		this.aportId = aportId;
	}
	public int getZportId() {
		return zportId;
	}
	public void setZportId(int zportId) {
		this.zportId = zportId;
	}
	public String getASiteName() {
		return ASiteName;
	}
	public void setASiteName(String aSiteName) {
		ASiteName = aSiteName;
	}
	public String getZSiteName() {
		return ZSiteName;
	}
	public void setZSiteName(String zSiteName) {
		ZSiteName = zSiteName;
	}
	public int getaSiteId() {
		return aSiteId;
	}
	public void setaSiteId(int aSiteId) {
		this.aSiteId = aSiteId;
	}
	public int getzSiteId() {
		return zSiteId;
	}
	public void setzSiteId(int zSiteId) {
		this.zSiteId = zSiteId;
	}
	public int getIsSingle() {
		return isSingle;
	}
	public void setIsSingle(int isSingle) {
		this.isSingle = isSingle;
	}
	public boolean isNode() {
		return node;
	}
	public void setNode(boolean node) {
		this.node = node;
	}
	public String getJobStatus() {
		return jobStatus;
	}
	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}
	public int getClientId() {
		return clientId;
	}
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	public int getAction() {
		return action;
	}
	public void setAction(int action) {
		this.action = action;
	}
	public PwInfo getBeforePw() {
		return beforePw;
	}
	public void setBeforePw(PwInfo beforePw) {
		this.beforePw = beforePw;
	}
//	public AcPortInfo getBeforeZAc() {
//		return beforeZAc;
//	}
//	public void setBeforeZAc(AcPortInfo beforeZAc) {
//		this.beforeZAc = beforeZAc;
//	}
//	public AcPortInfo getBeforeAAc() {
//		return beforeAAc;
//	}
//	public void setBeforeAAc(AcPortInfo beforeAAc) {
//		this.beforeAAc = beforeAAc;
//	}
	
	public AcPortInfo getCreateAc_a() {
		return createAc_a;
	}
	public List<AcPortInfo> getBeforeAAcList() {
		return beforeAAcList;
	}
	public void setBeforeAAcList(List<AcPortInfo> beforeAAcList) {
		this.beforeAAcList = beforeAAcList;
	}
	public List<AcPortInfo> getBeforeZAcList() {
		return beforeZAcList;
	}
	public void setBeforeZAcList(List<AcPortInfo> beforeZAcList) {
		this.beforeZAcList = beforeZAcList;
	}
	public void setCreateAc_a(AcPortInfo createAc_a) {
		this.createAc_a = createAc_a;
	}
	public AcPortInfo getCreateAc_z() {
		return createAc_z;
	}
	public void setCreateAc_z(AcPortInfo createAc_z) {
		this.createAc_z = createAc_z;
	}
	@Override
	public void putObjectProperty() {
		
	}
	public int getPwId() {
		return pwId;
	}
	public void setPwId(int pwId) {
		this.pwId = pwId;
	}
	public int getBeforeActiveStatus() {
		return beforeActiveStatus;
	}
	public void setBeforeActiveStatus(int beforeActiveStatus) {
		this.beforeActiveStatus = beforeActiveStatus;
	}
	public String getAmostAcId() {
		return amostAcId;
	}
	public void setAmostAcId(String amostAcId) {
		this.amostAcId = amostAcId;
	}
	public String getZmostAcId() {
		return zmostAcId;
	}
	public void setZmostAcId(String zmostAcId) {
		this.zmostAcId = zmostAcId;
	}
	public int getSiteId() {
		return siteId;
	}
	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}
}
