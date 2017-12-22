package com.nms.db.bean.ptn.oam;

import java.util.ArrayList;
import java.util.List;

import com.nms.db.bean.equipment.port.PortInst;
import com.nms.db.bean.equipment.shelf.SiteInst;
import com.nms.db.bean.ptn.path.eth.ElineInfo;
import com.nms.db.bean.ptn.path.pw.PwInfo;
import com.nms.db.bean.ptn.path.tunnel.Tunnel;
import com.nms.db.enums.EServiceType;
import com.nms.db.enums.OamTypeEnum;
import com.nms.model.equipment.port.PortService_MB;
import com.nms.model.equipment.shlef.SiteService_MB;
import com.nms.model.ptn.path.eth.ElineInfoService_MB;
import com.nms.model.ptn.path.pw.PwInfoService_MB;
import com.nms.model.ptn.path.tunnel.TunnelService_MB;
import com.nms.model.util.Services;
import com.nms.ui.frame.ViewDataObj;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.UiUtil;

public class OamInfo extends ViewDataObj implements java.io.Serializable{

	private static final long serialVersionUID = -8162535813108634378L;

	private int id;

	private OamMepInfo oamMep;

	private OamMipInfo oamMip;

	private OamTypeEnum oamType;

	private OamLinkInfo oamLinkInfo;

	private OamEthernetInfo oamEthernetInfo;

	private String oamObjType = "";

	private String oamPath = "";
	
	private String neDirect; //A Z 端OAM区分 a 为a端   ，z为z端
	public OamEthernetInfo getOamEthernetInfo() {
		return oamEthernetInfo;
	}

	public void setOamEthernetInfo(OamEthernetInfo oamEthernetInfo) {
		this.oamEthernetInfo = oamEthernetInfo;
	}

	public String getOamPath() {
		return oamPath;
	}

	public void setOamPath(String oamPath) {
		this.oamPath = oamPath;
	}

	public String getOamObjType() {
		return oamObjType;
	}

	public void setOamObjType(String oamObjType) {
		this.oamObjType = oamObjType;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public OamMepInfo getOamMep() {
		return oamMep;
	}

	public void setOamMep(OamMepInfo oamMep) {
		this.oamMep = oamMep;
	}

	public OamMipInfo getOamMip() {
		return oamMip;
	}

	public void setOamMip(OamMipInfo oamMip) {
		this.oamMip = oamMip;
	}

	public OamTypeEnum getOamType() {
		return oamType;
	}

	public void setOamType(OamTypeEnum oamType) {
		this.oamType = oamType;
	}

	public OamLinkInfo getOamLinkInfo() {
		return oamLinkInfo;
	}

	public void setOamLinkInfo(OamLinkInfo oamLinkInfo) {
		this.oamLinkInfo = oamLinkInfo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void putObjectProperty() {
		SiteService_MB siteService = null;
		SiteInst siteInst;
		try {
			siteService =  (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE);
			siteInst = new SiteInst();
			if (getOamMep() != null) {
				if(getOamMep().getSiteId()>0){
					siteInst = siteService.select(getOamMep().getSiteId());
				}				
				getClientProperties().put("id", getId());
				getClientProperties().put("megIcc", getOamMep().getMegIcc());
				getClientProperties().put("megUmc", getOamMep().getMegUmc());
				getClientProperties().put("localMepId", getOamMep().getLocalMepId());
				getClientProperties().put("remoteMepId", getOamMep().getRemoteMepId());
				getClientProperties().put("lpbOutTime", getOamMep().getLpbOutTime());
				getClientProperties().put("mel", getOamMep().getMel());
				getClientProperties().put("cv", getOamMep().isCv() ? Boolean.TRUE : Boolean.FALSE);
				if (getOamMep().isCv() && getOamMep().getCvCycle() > 0) {
					getClientProperties().put("cvCycle", UiUtil.getCodeById(getOamMep().getCvCycle()).getCodeName());
				} else {
					getClientProperties().put("cvCycle", 0);
				}
				getClientProperties().put("lck", getOamMep().isLck() ? Boolean.TRUE : Boolean.FALSE);
				getClientProperties().put("aps", getOamMep().isAps());
				getClientProperties().put("ssm", getOamMep().isSsm());
				getClientProperties().put("scc", getOamMep().isSccTest());
				getClientProperties().put("mip", "");
				if(null!=siteInst)
					getClientProperties().put("siteId", siteInst.getCellId());
				getClientProperties().put("ringTimer", getOamMip());
				getClientProperties().put("lm", getOamMep().isLm() ? Boolean.TRUE : Boolean.FALSE);
				getClientProperties().put("dm", getOamMep().isDm() ? Boolean.TRUE : Boolean.FALSE);
				getClientProperties().put("objectType", getOamMep().getObjType());
				
				getClientProperties().put("oamName", this.getoamName(this.getOamMep().getObjType(), this.getOamMep().getObjId()));
				getClientProperties().put("objectName",
						this.getObjectName(this.getOamMep().getObjType(), this.getOamMep().getObjId(), this.getOamMep().getServiceId()));
				getClientProperties().put("mip", Boolean.FALSE);
			}
			if (getOamMip() != null) {
				siteInst = siteService.select(getOamMip().getSiteId());
				getClientProperties().put("id", getId());
				getClientProperties().put("mel", 0);
				getClientProperties().put("lck", Boolean.FALSE);
				getClientProperties().put("objectType", "XC");
				getClientProperties().put("objectName",
						this.getObjectName(this.getOamMip().getObjType(), this.getOamMip().getObjId(), this.getOamMip().getServiceId()));
				getClientProperties().put("mip", Boolean.TRUE);
				if(null!=siteInst)
					getClientProperties().put("siteId", siteInst.getCellId());
			}
			if (oamLinkInfo != null) {
				siteInst = siteService.select(getOamLinkInfo().getSiteId());
				getClientProperties().put("objectType", getOamLinkInfo().getObjType());
				getClientProperties().put("mel", null);
				getClientProperties().put("lck", Boolean.FALSE);
				getClientProperties().put("objectName", selectProt("0",this.getOamLinkInfo().getSiteId(),getOamLinkInfo().getObjId()));
				if(null!=siteInst)
					getClientProperties().put("siteId", siteInst.getCellId());
			}
			if (oamEthernetInfo != null) {
				siteInst = siteService.select(getOamEthernetInfo().getSiteId());
				getClientProperties().put("id", getOamEthernetInfo().getId());
				getClientProperties().put("objectName", selectProt(getOamEthernetInfo().getPort(),this.getOamEthernetInfo().getSiteId(),0));
				getClientProperties().put("objectType", EServiceType.ETHERNET.toString());
				getClientProperties().put("mip",UiUtil.getCodeByValue("MPattribute",getOamEthernetInfo().getMpLable()+"").getCodeName().equals("MIP") ? Boolean.TRUE : Boolean.FALSE);
				if(null!=siteInst)
					getClientProperties().put("siteId", siteInst.getCellId());
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			UiUtil.closeService_MB(siteService);
		}
	}

	private String selectProt(String number,int siteId,int portId) {
		PortService_MB portService = null;
		PortInst portInst = null;
		List<PortInst> allportInstList = null;
		try {
			allportInstList=new ArrayList<PortInst>();
			portService = (PortService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORT);
			portInst = new PortInst();
			portInst.setSiteId(siteId);
			portInst.setNumber(Integer.parseInt(number));
			portInst.setPortId(portId);
			allportInstList = portService.select(portInst);
			if(allportInstList!=null&&allportInstList.size()>0){
				return allportInstList.get(0).getPortName();
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			UiUtil.closeService_MB(portService);
			portInst = null;
		}
		return "";
	}

//	private List<PortInst> getPortInstbyPortType(List<PortInst> portInstList) {
//		List<PortInst> infoList = new ArrayList<PortInst>();
//		for (PortInst info : portInstList) {
//			if (info.getPortType().equalsIgnoreCase("nni") || info.getPortType().equalsIgnoreCase("uni")
//					|| info.getPortType().equalsIgnoreCase("NONE")) {
//				infoList.add(info);
//			}
//		}
//		return infoList;
//	}

	private Object getoamName(String objectType, int objectId) throws Exception {
		PortService_MB portService = null;
		TunnelService_MB tunnelService = null;
		PwInfoService_MB pwInfoService = null;
		try {
			if ((EServiceType.SECTION.toString()+"_TEST").equals(objectType)) { 	//	段的  返回端口名
				portService = (PortService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORT);
				return portService.getPortname(objectId);
			} else if ((EServiceType.TUNNEL.toString()+"_TEST").equals(objectType)) {	//	tunnel  根据tunnelid去tunnel表中查询
				tunnelService = (TunnelService_MB) ConstantUtil.serviceFactory.newService_MB(Services.Tunnel);
				Tunnel tunnel = new Tunnel();
				tunnel.setTunnelId(objectId);
				List<Tunnel> tunnelList = tunnelService.select_nojoin(tunnel);
				if (null != tunnelList && tunnelList.size() == 1) {
					if("0".equals(tunnelList.get(0).getTunnelType())){
						return tunnelList.get(0).getTunnelName();
					}else{
						if("2".equals(UiUtil.getCodeById(Integer.parseInt(tunnelList.get(0).getTunnelType())).getCodeValue())){
							return tunnelList.get(0).getTunnelName()+"_work";
						}else{
							return tunnelList.get(0).getTunnelName();
						}
					}
				} else {
					return "";
				}
			} else if ((EServiceType.PW.toString()+"_TEST").equals(objectType)) { //	pw  根据pwid去pwinfo表中查询
				pwInfoService = (PwInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PwInfo);
				PwInfo pwInfo = new PwInfo();
				pwInfo.setPwId(objectId);
				pwInfo = pwInfoService.selectBypwid_notjoin(pwInfo);
				if (null != pwInfo) {
					return pwInfo.getPwName();
				} else {
					return "";
				}
			} else{
				return "";
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			UiUtil.closeService_MB(portService);
			UiUtil.closeService_MB(tunnelService);
			UiUtil.closeService_MB(pwInfoService);
		}
		return "";
	}

	/**
	 * 获取OAM对应的业务名称
	 * 
	 * @author kk
	 * 
	 * @param
	 * 
	 * @return
	 * 
	 * @Exception 异常对象
	 */
	private String getObjectName(String objectType, int objectId, int serviceId) throws Exception {
		PortService_MB portService = null;
		TunnelService_MB tunnelService = null;
		ElineInfoService_MB elineService = null;
		PwInfoService_MB pwInfoService = null;
		try {
			if ((EServiceType.SECTION.toString()).equals(objectType)) { // 段的 返回端口名
				portService = (PortService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORT);
				return portService.getPortname(objectId);
			} else if ((EServiceType.TUNNEL.toString()).equals(objectType)) { // tunnel
				tunnelService = (TunnelService_MB) ConstantUtil.serviceFactory.newService_MB(Services.Tunnel);
				Tunnel tunnel = new Tunnel();
				tunnel.setTunnelId(serviceId);
				List<Tunnel> tunnelList = tunnelService.select_nojoin(tunnel);
				if (null != tunnelList && tunnelList.size() == 1) {
					if ("0".equals(tunnelList.get(0).getTunnelType())) {
						return tunnelList.get(0).getTunnelName();
					} else {
						if ("2".equals(UiUtil.getCodeById(Integer.parseInt(tunnelList.get(0).getTunnelType())).getCodeValue())) {
							return tunnelList.get(0).getTunnelName() + "_work";
						} else {
							return tunnelList.get(0).getTunnelName();
						}
					}
				} else {
					return "";
				}
			} else if ((EServiceType.PW.toString()).equals(objectType)) { // pw
				pwInfoService = (PwInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PwInfo);
				PwInfo pwInfo = new PwInfo();
				pwInfo.setPwId(serviceId);
				pwInfo = pwInfoService.selectBypwid_notjoin(pwInfo);
				if (null != pwInfo) {
					return pwInfo.getPwName();
				} else {
					return "";
				}
			} else if ((EServiceType.ELINE.toString()).equals(objectType)) { // eline
				elineService = (ElineInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.Eline);
				ElineInfo elineinfo = new ElineInfo();
				elineinfo.setId(serviceId);
				List<ElineInfo> elineInfoList = elineService.selectByCondition_nojoin(elineinfo);
				if (null != elineInfoList && elineInfoList.size() == 1) {
					return elineInfoList.get(0).getName();
				} else {
					return "";
				}
			} else {
				return "";
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			UiUtil.closeService_MB(portService);
			UiUtil.closeService_MB(tunnelService);
			UiUtil.closeService_MB(pwInfoService);
			UiUtil.closeService_MB(elineService);
		}
		return "";
	}

	public String getNeDirect() {
		return neDirect;
	}

	public void setNeDirect(String neDirect) {
		this.neDirect = neDirect;
	}
}
