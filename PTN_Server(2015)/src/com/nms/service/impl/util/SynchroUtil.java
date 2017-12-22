﻿/**   
 * 文件名：SynchroUtil.java   
 * 创建人：kk   
 * 创建时间：2013-5-13 下午01:25:15 
 *   
 */
package com.nms.service.impl.util;

import java.util.ArrayList;
import java.util.List;

import com.nms.db.bean.equipment.port.E1Info;
import com.nms.db.bean.equipment.port.PortInst;
import com.nms.db.bean.equipment.port.PortStm;
import com.nms.db.bean.equipment.port.PortStmTimeslot;
import com.nms.db.bean.equipment.port.V35PortInfo;
import com.nms.db.bean.path.Segment;
import com.nms.db.bean.perform.PmValueLimiteInfo;
import com.nms.db.bean.ptn.ARPInfo;
import com.nms.db.bean.ptn.AclInfo;
import com.nms.db.bean.ptn.AllConfigInfo;
import com.nms.db.bean.ptn.BfdInfo;
import com.nms.db.bean.ptn.BlackAndwhiteMacInfo;
import com.nms.db.bean.ptn.CccInfo;
import com.nms.db.bean.ptn.EthServiceInfo;
import com.nms.db.bean.ptn.L2cpInfo;
import com.nms.db.bean.ptn.MacManagementInfo;
import com.nms.db.bean.ptn.SiteRoate;
import com.nms.db.bean.ptn.SmartFan;
import com.nms.db.bean.ptn.SsMacStudy;
import com.nms.db.bean.ptn.clock.ClockSource;
import com.nms.db.bean.ptn.clock.ExternalClockInterface;
import com.nms.db.bean.ptn.clock.FrequencyInfo;
import com.nms.db.bean.ptn.clock.LineClockInterface;
import com.nms.db.bean.ptn.clock.PortConfigInfo;
import com.nms.db.bean.ptn.clock.TimeManageInfo;
import com.nms.db.bean.ptn.clock.TimeSyncInfo;
import com.nms.db.bean.ptn.ecn.CCN;
import com.nms.db.bean.ptn.ecn.MCN;
import com.nms.db.bean.ptn.ecn.OSPFAREAInfo;
import com.nms.db.bean.ptn.ecn.OSPFInfo;
import com.nms.db.bean.ptn.ecn.OSPFInterface;
import com.nms.db.bean.ptn.ecn.OSPFinfo_wh;
import com.nms.db.bean.ptn.ecn.OspfRedistribute;
import com.nms.db.bean.ptn.oam.OamEthernetInfo;
import com.nms.db.bean.ptn.oam.OamInfo;
import com.nms.db.bean.ptn.oam.OamLinkInfo;
import com.nms.db.bean.ptn.oam.OamMepInfo;
import com.nms.db.bean.ptn.oam.OamMipInfo;
import com.nms.db.bean.ptn.path.GroupSpreadInfo;
import com.nms.db.bean.ptn.path.StaticUnicastInfo;
import com.nms.db.bean.ptn.path.ces.CesInfo;
import com.nms.db.bean.ptn.path.eth.DualInfo;
import com.nms.db.bean.ptn.path.eth.ElanInfo;
import com.nms.db.bean.ptn.path.eth.ElineInfo;
import com.nms.db.bean.ptn.path.eth.EtreeInfo;
import com.nms.db.bean.ptn.path.protect.DualProtect;
import com.nms.db.bean.ptn.path.protect.LoopProtectInfo;
import com.nms.db.bean.ptn.path.protect.MspProtect;
import com.nms.db.bean.ptn.path.pw.PwInfo;
import com.nms.db.bean.ptn.path.pw.PwNniInfo;
import com.nms.db.bean.ptn.path.tunnel.Lsp;
import com.nms.db.bean.ptn.path.tunnel.Tunnel;
import com.nms.db.bean.ptn.port.AcPortInfo;
import com.nms.db.bean.ptn.port.Acbuffer;
import com.nms.db.bean.ptn.port.PortLagInfo;
import com.nms.db.bean.ptn.qos.QosInfo;
import com.nms.db.bean.ptn.qos.QosMappingAttr;
import com.nms.db.bean.ptn.qos.QosMappingMode;
import com.nms.db.bean.ptn.qos.QosQueue;
import com.nms.db.enums.EActiveStatus;
import com.nms.db.enums.EManufacturer;
import com.nms.db.enums.OamTypeEnum;
import com.nms.drive.service.bean.TunnelProtection;
import com.nms.drive.service.impl.CoderUtils;
import com.nms.model.equipment.port.E1InfoService_MB;
import com.nms.model.equipment.port.PortService_MB;
import com.nms.model.equipment.port.PortStmService_MB;
import com.nms.model.equipment.port.PortStmTimeslotService_MB;
import com.nms.model.equipment.port.V35PortService_MB;
import com.nms.model.equipment.shlef.SiteService_MB;
import com.nms.model.path.SegmentService_MB;
import com.nms.model.perform.PmLimiteService_MB;
import com.nms.model.ptn.ARPInfoService_MB;
import com.nms.model.ptn.AclService_MB;
import com.nms.model.ptn.AllConfigService_MB;
import com.nms.model.ptn.BfdInfoService_MB;
import com.nms.model.ptn.BlackWhiteMacService_MB;
import com.nms.model.ptn.CccService_MB;
import com.nms.model.ptn.EthService_MB;
import com.nms.model.ptn.L2CPService_MB;
import com.nms.model.ptn.MacManageService_MB;
import com.nms.model.ptn.SecondMacStudyService_MB;
import com.nms.model.ptn.SmartFanService_MB;
import com.nms.model.ptn.TimeSyncService_MB;
import com.nms.model.ptn.clock.ClockFrequService_MB;
import com.nms.model.ptn.clock.ExternalClockInterfaceService_MB;
import com.nms.model.ptn.clock.FrequencyClockManageService_MB;
import com.nms.model.ptn.clock.PortDispositionInfoService_MB;
import com.nms.model.ptn.clock.TimeLineClockInterfaceService_MB;
import com.nms.model.ptn.clock.TimeManageInfoService_MB;
import com.nms.model.ptn.ecn.CCNService_MB;
import com.nms.model.ptn.ecn.MCNService_MB;
import com.nms.model.ptn.ecn.OSPFAREAService_MB;
import com.nms.model.ptn.ecn.OSPFInfoService_MB;
import com.nms.model.ptn.ecn.OSPFInterfaceService_MB;
import com.nms.model.ptn.ecn.Ospf_whService_MB;
import com.nms.model.ptn.ecn.RedistributeService_MB;
import com.nms.model.ptn.oam.OamEthreNetService_MB;
import com.nms.model.ptn.oam.OamInfoService_MB;
import com.nms.model.ptn.path.GroupSpreadService_MB;
import com.nms.model.ptn.path.SingleSpreadService_MB;
import com.nms.model.ptn.path.ces.CesInfoService_MB;
import com.nms.model.ptn.path.eth.DualInfoService_MB;
import com.nms.model.ptn.path.eth.ElanInfoService_MB;
import com.nms.model.ptn.path.eth.ElineInfoService_MB;
import com.nms.model.ptn.path.eth.EtreeInfoService_MB;
import com.nms.model.ptn.path.protect.MspProtectService_MB;
import com.nms.model.ptn.path.protect.WrappingProtectService_MB;
import com.nms.model.ptn.path.pw.PwInfoService_MB;
import com.nms.model.ptn.path.pw.PwNniInfoService_MB;
import com.nms.model.ptn.path.tunnel.LspInfoService_MB;
import com.nms.model.ptn.path.tunnel.TunnelService_MB;
import com.nms.model.ptn.port.AcBufferService_MB;
import com.nms.model.ptn.port.AcPortInfoService_MB;
import com.nms.model.ptn.port.DualProtectService_MB;
import com.nms.model.ptn.port.PortLagService_MB;
import com.nms.model.ptn.qos.QosInfoService_MB;
import com.nms.model.ptn.qos.QosMappingModeService_MB;
import com.nms.model.util.Services;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.DateUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.UiUtil;

/**
 * 
 * 项目名称：WuHanPTN2012 类名称：SynchroUtil 类描述：同步帮助类 创建人：kk 创建时间：2013-5-13 下午01:25:15 修改人：kk 修改时间：2013-5-13 下午01:25:15 修改备注：
 * 
 * @version
 * 
 */
public class SynchroUtil {

	/**
	 * tunnel对象与数据库同步
	 * 
	 * @author kk
	 * 
	 * @param tunnel
	 *            设备上查询出的tunnel对象
	 * @param role
	 *            类型。 "ingress"=A端 "egress"=z端 "xc"=中间节点 ""=武汉不确定是a还是z 所以同时比较az端
	 * @param siteId
	 *            网元ID
	 * @param siteRoate
	 *            设备上查出的倒换对象        
	 * @return
	 * @throws Exception
	 * 
	 * @Exception 异常对象
	 */
	public void tunnelSynchro(Tunnel tunnel, String role, int siteId) throws Exception {

		LspInfoService_MB lspService = null;
		List<Lsp> lspList = null;
		TunnelService_MB tunnelService = null;
		SiteRoate siteRoate=null;
		OamInfoService_MB oamInfoService = null;
		SiteService_MB siteService = null;
		try {
			siteService=(SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE);
			tunnelService = (TunnelService_MB) ConstantUtil.serviceFactory.newService_MB(Services.Tunnel);
			lspService = (LspInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.LSPINFO);
			oamInfoService = (OamInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.OamInfo);
			lspList = lspService.select_synchro(role, siteId, tunnel);

//			if (lspList.size() == 0) {
//				throw new Exception("同步tunnel时 查询LSP出错");
//			}

			switch (lspList.size()) {
			case 0:
				// 插入tunnel
				tunnelService.save(tunnel);
//				/**
//				 * 插入tunnel 时，判断是否为保护，
//				 * 保护则添加  site_roate表 （倒换
//				 */
//				if("2".equals(UiUtil.getCodeById(Integer.parseInt(tunnel.getTunnelType())).getCodeValue())){
//					siteRoate.setTypeName(tunnel.getTunnelName());
//					siteRoateService.insert(siteRoate);
//				}
				
				break;
			case 1:
				if (siteService.getManufacturer(siteId) == EManufacturer.valueOf("CHENXIAO").getValue()) {
					// 判断是不是1:1保护，如果是。修改1:1保护
					if ("2".equals(UiUtil.getCodeById(Integer.parseInt(tunnel.getTunnelType())).getCodeValue())) {
						int projectId = updateProtectTunnel(tunnel.getProtectTunnel(), tunnelService, lspList, lspService, role);
						if (projectId != 0) {
							tunnel.setProtectTunnelId(projectId);
						}
					}
				}else{//武汉设备需同步oam
					OamInfo oamInfo = new OamInfo();
					if(tunnel.getaSiteId() == siteId){
						oamInfo.setOamMep(tunnel.getOamList().get(0).getOamMep());
						oamInfo = oamInfoService.queryByCondition(oamInfo, OamTypeEnum.AMEP);
					}else if(tunnel.getzSiteId() == siteId){
						oamInfo.setOamMep(tunnel.getOamList().get(0).getOamMep());
						oamInfo = oamInfoService.queryByCondition(oamInfo, OamTypeEnum.ZMEP);
					}else{
						oamInfo.setOamMip(tunnel.getOamList().get(0).getOamMip());
						oamInfo = oamInfoService.queryByCondition(oamInfo, OamTypeEnum.MIP);
					}
					if(oamInfo.getOamMep() != null && oamInfo.getOamMep().getId()>0){
						tunnel.getOamList().get(0).getOamMep().setId(oamInfo.getOamMep().getId());
					}
					if(oamInfo.getOamMip() != null && oamInfo.getOamMip().getId()>0){
						tunnel.getOamList().get(0).getOamMip().setId(oamInfo.getOamMip().getId());
					}
				}

				// 修改A Z端
				updateTunnel(lspList, tunnel, lspService,tunnelService);
				/**
				 * 更新tunnel 时，判断是否为保护，
				 * 保护则更新  site_roate表 （倒换
				 */
				if(!tunnel.getTunnelType().equals("0") && "2".equals(UiUtil.getCodeById(Integer.parseInt(tunnel.getTunnelType())).getCodeValue())){
					siteRoate=new SiteRoate();
					siteRoate.setTypeId(tunnel.getTunnelId());
					siteRoate.setType("tunnel");
					siteRoate.setSiteId(siteId);
				//	siteRoate.
				//	siteRoateService.update(siteRoate);
				}
				break;
			case 2:
				// 修改xc
				if (siteService.getManufacturer(siteId) == EManufacturer.valueOf("WUHAN").getValue()) {
					OamInfo oamInfo = new OamInfo();
					OamMipInfo oamMipInfo = new OamMipInfo();
					oamMipInfo.setSiteId(siteId);
					oamMipInfo.setServiceId(lspList.get(0).getTunnelId());
					oamMipInfo.setObjType("TUNNEL");
					oamInfo.setOamMip(oamMipInfo);
					oamInfo = oamInfoService.queryByCondition(oamInfo, OamTypeEnum.MIP);
					if(oamInfo.getOamMip() != null && oamInfo.getOamMip().getId()>0){
						tunnel.getOamList().get(0).getOamMip().setId(oamInfo.getOamMip().getId());
						
					}
				}
				updateTunnel(lspList, tunnel, lspService,tunnelService);
				break;
			default:
				throw new Exception("同步tunnel时 查询LSP出错");
			}

		} catch (Exception e) {
			throw e;
		} finally {
			UiUtil.closeService_MB(oamInfoService);
			UiUtil.closeService_MB(lspService);
			UiUtil.closeService_MB(tunnelService);
			UiUtil.closeService_MB(siteService);
		}
	}

	/**
	 * 修改LSP数据库
	 * 
	 * updateDB(这里用一句话描述这个方法的作用)
	 * 
	 * @author kk
	 * 
	 * @param lspList
	 *            从数据库查询出来的lsp结果
	 * @param tunnel
	 *            要修改的tunnel对象
	 * @param lspService
	 *            lspservice对象
	 * 
	 * @return
	 * 
	 * @Exception 异常对象
	 */
	private void updateTunnel(List<Lsp> lspList, Tunnel tunnel, LspInfoService_MB lspService,TunnelService_MB tunnelService) throws Exception {

		OamInfoService_MB oamInfoService = null;
		SiteService_MB siteService = null;
		try {
			siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE);
			oamInfoService = (OamInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.OamInfo);
			if (tunnel.getLspParticularList().get(0).getASiteId() != 0 && siteService.getManufacturer(tunnel.getLspParticularList().get(0).getASiteId()) == EManufacturer.WUHAN.getValue()) {
				for (int i = 0; i < lspList.size(); i++) {
					// 判断是哪A端还是Z端 取不同的端口做修改
					if (lspList.get(i).getASiteId() == tunnel.getLspParticularList().get(i).getASiteId() && lspList.get(i).getAtunnelbusinessid() == tunnel.getLspParticularList().get(i).getAtunnelbusinessid()) {
						lspList.get(i).setAPortId(tunnel.getLspParticularList().get(i).getAPortId());
						// 把设备读上来的标签给要修改的对象
						lspList.get(i).setFrontLabelValue(tunnel.getLspParticularList().get(i).getFrontLabelValue());
						lspList.get(i).setBackLabelValue(tunnel.getLspParticularList().get(i).getBackLabelValue());
					} else {
						lspList.get(i).setZPortId(tunnel.getLspParticularList().get(i).getAPortId());
						// 把设备读上来的标签给要修改的对象
						lspList.get(i).setFrontLabelValue(tunnel.getLspParticularList().get(i).getBackLabelValue());
						lspList.get(i).setBackLabelValue(tunnel.getLspParticularList().get(i).getFrontLabelValue());
					}
					lspList.get(i).setSourceMac(tunnel.getLspParticularList().get(i).getSourceMac());
					lspList.get(i).setTargetMac(tunnel.getLspParticularList().get(i).getTargetMac());
					// 修改
					lspService.saveOrUpdate(lspList.get(i));
					tunnel.setTunnelId(lspList.get(i).getTunnelId());
				}
			} else {
				for (int i = 0; i < lspList.size(); i++) {
					// 把设备读上来的标签给要修改的对象
					lspList.get(i).setFrontLabelValue(tunnel.getLspParticularList().get(i).getFrontLabelValue());
					lspList.get(i).setBackLabelValue(tunnel.getLspParticularList().get(i).getBackLabelValue());

					lspList.get(i).setAoppositeId(tunnel.getLspParticularList().get(i).getAoppositeId());
					lspList.get(i).setZoppositeId(tunnel.getLspParticularList().get(i).getZoppositeId());

					// 判断是哪A端还是Z端 取不同的端口做修改
					if (lspList.get(i).getASiteId() == tunnel.getLspParticularList().get(i).getASiteId() && lspList.get(i).getAtunnelbusinessid() == tunnel.getLspParticularList().get(i).getAtunnelbusinessid()) {
						lspList.get(i).setAPortId(tunnel.getLspParticularList().get(i).getAPortId());
					} else {
						lspList.get(i).setZPortId(tunnel.getLspParticularList().get(i).getZPortId());
					}
					// 修改
					lspService.saveOrUpdate(lspList.get(i));
					tunnel.setTunnelId(lspList.get(i).getTunnelId());
				}
			}
			tunnelService.update_synchro(tunnel);

			if (tunnel.getOamList().size() > 0) {
				for (OamInfo oamInfo : tunnel.getOamList()) {
					if (oamInfo.getOamType() == OamTypeEnum.AMEP) {
						oamInfo.getOamMep().setServiceId(tunnel.getTunnelId());
						oamInfo.getOamMep().setObjId(tunnel.getLspParticularList().get(0).getAtunnelbusinessid());
						oamInfo.setOamType(OamTypeEnum.AMEP);
					} else if (oamInfo.getOamType() == OamTypeEnum.ZMEP) {
						oamInfo.getOamMep().setServiceId(tunnel.getTunnelId());
						oamInfo.getOamMep().setObjId(tunnel.getLspParticularList().get(0).getZtunnelbusinessid());
						oamInfo.setOamType(OamTypeEnum.ZMEP);
					} else if (oamInfo.getOamType() == OamTypeEnum.MEP) {
						oamInfo.getOamMep().setServiceId(tunnel.getTunnelId());
					} else if (oamInfo.getOamType() == OamTypeEnum.MIP) {
						oamInfo.getOamMip().setServiceId(tunnel.getTunnelId());
					}
					oamInfoService.saveOrUpdate(oamInfo);
				}
			}

			// updateOam(tunnel.getOamList());
		} catch (Exception e) {
			throw e;
		} finally {
			UiUtil.closeService_MB(oamInfoService);
			UiUtil.closeService_MB(siteService);
		}
	}

	/**
	 * 
	 * 同步保护tunnel
	 * 
	 * @author kk
	 * 
	 * @param
	 * 
	 * @return
	 * 
	 * @Exception 异常对象
	 */
	private int updateProtectTunnel(Tunnel protectTunnel, TunnelService_MB tunnelService, List<Lsp> lspList, LspInfoService_MB lspService, String role) throws Exception {

		int tunnelId = 0;
		int result = 0;
		Tunnel tunnel = null;
		List<Lsp> lspList_protect = new ArrayList<Lsp>();
		try {

			tunnelId = lspList.get(0).getTunnelId();
			tunnel = new Tunnel();
			tunnel.setTunnelId(tunnelId);
			tunnel = tunnelService.select_nojoin(tunnel).get(0);

			if (null == tunnel.getProtectTunnel()) {
				result = tunnelService.savaProtect(protectTunnel);
			} else {
				result = tunnel.getProtectTunnel().getTunnelId();

				for (Lsp lsp : tunnel.getProtectTunnel().getLspParticularList()) {
					if ("ingress".equals(role)) {
						if (lsp.getASiteId() == tunnel.getProtectTunnel().getASiteId()) {
							lspList_protect.add(lsp);
							break;
						}
					} else if ("egress".equals(role)) {
						if (lsp.getZSiteId() == tunnel.getProtectTunnel().getZSiteId()) {
							lspList_protect.add(lsp);
							break;
						}
					}
				}

				updateTunnel(lspList_protect, protectTunnel, lspService,tunnelService);
			}

		} catch (Exception e) {
			throw e;
		}

		return result;
	}

	/**
	 * 修改OAM
	 * 
	 * @author kk
	 * 
	 * @param
	 * 
	 * @throws Exception
	 * 
	 * @Exception 异常对象
	 */
	public void updateOam(List<OamInfo> oamInfoList) throws Exception {
		OamInfoService_MB oamInfoService = null;
		try {
			oamInfoService = (OamInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.OamInfo);

			for (OamInfo oamInfo : oamInfoList) {
				oamInfoService.saveOrUpdate(oamInfo);
			}

		} catch (Exception e) {
			throw e;
		} finally {
			UiUtil.closeService_MB(oamInfoService);
		}
	}

	/**
	 * 同步以太网OAM
	 * @author kk
	 * 
	 * @param
	 * 
	 * @throws Exception
	 * 
	 * @Exception 异常对象
	 */
	public static void ethOamSynchro(OamEthernetInfo oamInfo) throws Exception {
		OamEthreNetService_MB oamInfoService = null;
		try {
			oamInfoService = (OamEthreNetService_MB) ConstantUtil.serviceFactory.newService_MB(Services.OAMETHERNET);
			oamInfoService.saveOrUpdate(oamInfo);

		} catch (Exception e) {
			throw e;
		} finally {
			UiUtil.closeService_MB(oamInfoService);
		}
	}
	/**
	 * pw 对象与数据库信息同步
	 * 
	 * @author kk
	 * 
	 * @param pwinfo
	 *            数据库pw对象
	 * @param siteid
	 *            网元id
	 * 
	 * @return
	 * @throws Exception
	 * 
	 * @Exception 异常对象
	 */
	public void pwInfoSynchro(PwInfo pwinfo, int siteid) throws Exception {
		PwInfoService_MB pwInfoService = null;
		List<PwInfo> pwInfoList = null;
		int serviceid = 0;
		try {
			pwInfoService = (PwInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PwInfo);
			if (pwinfo.getApwServiceId() == 0) {
				serviceid = pwinfo.getZpwServiceId();
			} else {
				serviceid = pwinfo.getApwServiceId();
			}
			pwInfoList = pwInfoService.select_synchro(siteid, serviceid, pwinfo.getType().getValue());

			if (null == pwInfoList) {
				throw new Exception("同步pw时 查询pw出错");
			}

			switch (pwInfoList.size()) {
			case 0:
				// 插入pw
				pwInfoService.save(pwinfo);
				break;
			case 1:
				// 修改PW
				if(pwInfoList.get(0).getTunnelId() > 0){
					updatePwinfo(pwInfoList, pwinfo, pwInfoService);
				}else{
					List<Integer> pwIdList = new ArrayList<Integer>();
					pwIdList.add(pwInfoList.get(0).getPwId());
					pwInfoService.updateActiveStatus(pwIdList, 1);
				};
				break;

			default:
				throw new Exception("同步pw时 查询pw出错");
			}

		} catch (Exception e) {
			throw e;
		} finally {
			UiUtil.closeService_MB(pwInfoService);
		}
	}

	/**
	 * 修改pw数据库
	 * 
	 * @author kk
	 * 
	 * @param pwInfoList
	 *            数据库中的pw对象
	 * @param pwinfo
	 *            设备中的pw对象
	 * @return
	 * @throws Exception
	 * 
	 * @Exception 异常对象
	 */
	private void updatePwinfo(List<PwInfo> pwInfoList, PwInfo pwinfo, PwInfoService_MB pwInfoService) throws Exception {

		if (null == pwInfoList || pwInfoList.size() != 1) {
			throw new Exception("pwInfoList is null");
		}
		if (null == pwinfo) {
			throw new Exception("pwinfo is null");
		}
		if (null == pwInfoService) {
			throw new Exception("pwInfoService is null");
		}

		PwInfo pwInfo_db = null;
		SiteService_MB siteService = null;
		OamInfoService_MB oamInfoService = null;
		try {
			siteService=(SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE);
			pwInfo_db = pwInfoList.get(0);
			if (pwinfo.getASiteId() != 0 && siteService.getManufacturer(pwinfo.getASiteId()) == EManufacturer.WUHAN.getValue()) {
				pwInfo_db.setPwStatus(EActiveStatus.ACTIVITY.getValue());
				pwInfo_db.setTunnelId(pwinfo.getTunnelId());
				if (pwInfo_db.getASiteId() == pwinfo.getASiteId()) {
					pwInfo_db.setInlabelValue(pwinfo.getInlabelValue());
					pwInfo_db.setOutlabelValue(pwinfo.getOutlabelValue());
				} else {
					pwInfo_db.setInlabelValue(pwinfo.getOutlabelValue());
					pwInfo_db.setOutlabelValue(pwinfo.getInlabelValue());
				}
				pwInfo_db.setJobStatus(pwinfo.getJobStatus());
				pwInfo_db.setaOutVlanValue(pwinfo.getaOutVlanValue());
				pwInfo_db.setaVlanEnable(pwinfo.getaVlanEnable());
				pwInfo_db.setaSourceMac(pwinfo.getaSourceMac());
				pwInfo_db.setAtargetMac(pwinfo.getAtargetMac());
			} else {
				pwInfo_db.setPwStatus(EActiveStatus.ACTIVITY.getValue());
				pwInfo_db.setTunnelId(pwinfo.getTunnelId());
				pwInfo_db.setInlabelValue(pwinfo.getInlabelValue());
				pwInfo_db.setOutlabelValue(pwinfo.getOutlabelValue());
				pwInfo_db.setQosList(pwinfo.getQosList());
				pwInfo_db.setAoppositeId(pwinfo.getAoppositeId());
				pwInfo_db.setZoppositeId(pwinfo.getZoppositeId());
				pwInfo_db.setJobStatus(pwinfo.getJobStatus());
				pwInfo_db.setzOutVlanValue(pwinfo.getaOutVlanValue());
				pwInfo_db.setzVlanEnable(pwinfo.getaVlanEnable());
				pwInfo_db.setzSourceMac(pwinfo.getaSourceMac());
				pwInfo_db.setZtargetMac(pwinfo.getAtargetMac());
			}

			// pwInfo_db.setPwName(pwinfo.getPwName());
			if(pwinfo.getOamList() != null && pwinfo.getOamList().size()>0){//武汉设备需同步pw的oam
				oamInfoService = (OamInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.OamInfo);
				for(OamInfo oamInfo : pwinfo.getOamList()){
					if(oamInfo.getOamMep() != null){
						List<OamMepInfo> oamMepInfos = oamInfoService.selectByOamMepInfo(oamInfo.getOamMep());
						if(oamMepInfos.size()>0){
							oamInfo.getOamMep().setId(oamMepInfos.get(0).getId());
						}
					}
				}
				pwInfo_db.setOamList(pwinfo.getOamList());
			}
			if(pwinfo.getQosList() != null && pwinfo.getQosList().size()>0){
				
				pwInfo_db.setQosList(pwinfo.getQosList());
			}
			pwInfoService.update(pwInfo_db);

			// updateQos(tunnel.getQosList());

		} catch (Exception e) {
			throw e;
		} finally {
			UiUtil.closeService_MB(siteService);
			UiUtil.closeService_MB(oamInfoService);
		}
	}

	/**
	 * eline 对象与数据库信息同步
	 * 
	 * @author kk
	 * 
	 * @param eline
	 *            数据库eline对象
	 * @param siteid
	 *            网元id
	 * 
	 * @return
	 * @throws Exception
	 * 
	 * @Exception 异常对象
	 */
	public void elineSynchro(ElineInfo elineInfo, int siteid) throws Exception {
		ElineInfoService_MB elineService = null;
		List<ElineInfo> elineInfoList = null;
		try {
			elineService = (ElineInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.Eline);
			if(elineInfo.getaSiteId() == siteid){
				elineInfoList = elineService.select_synchro(siteid, elineInfo.getaXcId());
			}else if(elineInfo.getzSiteId() == siteid){
				elineInfoList = elineService.select_synchro(siteid, elineInfo.getzXcId());
			}
			if (null == elineInfoList) {
				throw new Exception("同步eline时 查询eline出错");
			}

			switch (elineInfoList.size()) {
			case 0:
				// 插入eline
				elineInfo.setCreateTime(DateUtil.getDate(DateUtil.FULLTIME));
				elineService.save(elineInfo);
				break;
			case 1:
				// 修改eline
				updateEline(elineInfoList, elineInfo, elineService);
				break;

			default:
				throw new Exception("同步eline时 查询eline出错");
			}

		} catch (Exception e) {
			throw e;
		} finally {
			UiUtil.closeService_MB(elineService);
		}
	}
	/**
	 * 修改eline数据库
	 * 
	 * @author kk
	 * 
	 * @param elineInfoList
	 *            数据库查询出来的eline对象
	 * @param elineInfo
	 *            设备上的eline对象
	 * @return
	 * @throws Exception
	 * 
	 * @Exception 异常对象
	 */
	private void updateEline(List<ElineInfo> elineInfoList, ElineInfo elineInfo, ElineInfoService_MB elineService) throws Exception {

		if (null == elineInfoList || elineInfoList.size() != 1) {
			throw new Exception("elineInfoList is null");
		}
		if (null == elineInfo) {
			throw new Exception("elineInfo is null");
		}
		if (null == elineService) {
			throw new Exception("elineService is null");
		}

		ElineInfo elineInfo_db = null;
		try {
			elineInfo_db = elineInfoList.get(0);
			elineInfo_db.setActiveStatus(EActiveStatus.ACTIVITY.getValue());
			elineInfo_db.setPwId(elineInfo.getPwId());
			elineInfo_db.setJobStatus(elineInfo.getJobStatus());
			if (elineInfo_db.getaSiteId() == elineInfo.getaSiteId()) {
				elineInfo_db.setaAcId(elineInfo.getaAcId());
			} else {
				elineInfo_db.setzAcId(elineInfo.getzAcId());
			}

			elineService.update(elineInfo_db);

		} catch (Exception e) {
			throw e;
		} finally {
			elineInfo_db = null;
		}
	}

	/**
	 * dualInfo 对象与数据库信息同步
	 * 
	 * @author kk
	 * 
	 * @param dualInfo
	 *            数据库eline对象
	 * @param siteid
	 *            网元id
	 * 
	 * @return
	 * @throws Exception
	 * 
	 * @Exception 异常对象
	 */
	public void dualSynchro(DualInfo dualInfo, int siteId) throws Exception {
		List<DualInfo> dualInfoListDB = null;
		DualInfoService_MB dualInfoService = null;
		List<DualInfo> dualInfoList = null;
		DualInfo dualInfoDb = null;
		try {
			dualInfoService = (DualInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.DUALINFO);
			dualInfoListDB = new ArrayList<DualInfo>();
			if(dualInfo.getaXcId()>0){
				dualInfoDb = dualInfoService.selectBySiteIdAndBusinessId(siteId,dualInfo.getaXcId());
			}else if(dualInfo.getzXcId() >0){
				dualInfoDb = dualInfoService.selectBySiteIdAndBusinessId(siteId,dualInfo.getzXcId());
			}
			
			if(dualInfoDb != null)
			{
				dualInfoListDB.add(dualInfoDb);
			}
			if (null == dualInfoListDB) {
               throw new Exception("同步dual时 查询dual出错");
			}

			switch (dualInfoListDB.size()) {
			case 0:
				dualInfoList = new ArrayList<DualInfo>();
				dualInfoList.add(dualInfo);
				// 插入dual
				dualInfoService.insert(dualInfoList);
				break;
			case 1:
				// 修改eline
				updateDual(dualInfoListDB, dualInfo, dualInfoService);
				break;

			default:
				throw new Exception("同步dual时 查询dual出错");
			}

		} catch (Exception e) {
			throw e;
		} finally {
			UiUtil.closeService_MB(dualInfoService);
			dualInfoListDB = null;
			dualInfoList = null;
			dualInfoDb= null;
		}
	}
	
	private void updateDual(List<DualInfo> dualInfoListDB, DualInfo dualInfo,
			DualInfoService_MB dualInfoService) throws Exception{
		
		if (null == dualInfoListDB || dualInfoListDB.size() != 1) {
			throw new Exception("dualInfoListDB is null");
		}
		if (null == dualInfo) {
			throw new Exception("dualInfo is null");
		}
		if (null == dualInfoService) {
			throw new Exception("dualInfoService is null");
		}
		try 
		{
			dualInfoListDB.get(0).setActiveStatus(EActiveStatus.ACTIVITY.getValue());
			dualInfoListDB.get(0).setPwId(dualInfo.getPwId());
			if(dualInfo.getaSiteId()>0){
				dualInfoListDB.get(0).setaAcId(dualInfo.getaAcId());
			}else {
				dualInfoListDB.get(0).setzAcId(dualInfo.getzAcId());
			}
			

			dualInfoService.update(dualInfoListDB);
		} catch (Exception e) 
		{
			ExceptionManage.dispose(e, getClass());
		}
		
	}

	/**
	 * Ces 对象与数据库信息同步
	 * 
	 * @author kk
	 * 
	 * @param cesinfo
	 *            数据库ces对象
	 * @param siteid
	 *            网元id
	 * 
	 * @return
	 * @throws Exception
	 * @throws Exception
	 * 
	 * @Exception 异常对象
	 */
	public void CesSynchro(CesInfo cesInfo, int siteId) throws Exception {
		CesInfoService_MB cesInfoService = null;
		List<CesInfo> cesInfoList = null;
		try {
			cesInfoService = (CesInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.CesInfo);
			if(cesInfo.getaSiteId() == siteId){
				cesInfoList = cesInfoService.select_synchro(siteId, cesInfo.getAxcId());
			}else if(cesInfo.getzSiteId() == siteId){
				cesInfoList = cesInfoService.select_synchro(siteId, cesInfo.getZxcId());
			}
			if (null == cesInfoList) {
				throw new Exception("同步ces时查询ces出错");
			}

			switch (cesInfoList.size()) {
			case 0:
				// 插入eline
				cesInfo.setCreateTime(DateUtil.getDate(DateUtil.FULLTIME));
				cesInfoService.save(cesInfo);
				break;
			case 1:
				// 修改eline
				updateCes(cesInfoList, cesInfo, cesInfoService);
				break;

			default:
				throw new Exception("同步ces时查询ces出错");
			}

		} catch (Exception e) {
			throw e;
		} finally {
			UiUtil.closeService_MB(cesInfoService);
		}
	}

	/**
	 * 修改eline数据库
	 * 
	 * @author kk
	 * 
	 * @param elineInfoList
	 *            数据库查询出来的eline对象
	 * @param elineInfo
	 *            设备上的eline对象
	 * @return
	 * @throws Exception
	 * 
	 * @Exception 异常对象
	 */
	private void updateCes(List<CesInfo> cesInfoList, CesInfo cesInfo, CesInfoService_MB cesInfoService) throws Exception {

		if (null == cesInfoList || cesInfoList.size() != 1) {
			throw new Exception("cesInfoList is null");
		}
		if (null == cesInfo) {
			throw new Exception("cesInfo is null");
		}
		if (null == cesInfoService) {
			throw new Exception("cesInfoService is null");
		}

		try {
			cesInfoList.get(0).setActiveStatus(EActiveStatus.ACTIVITY.getValue());
			cesInfoList.get(0).setPwId(cesInfo.getPwId());
			if (cesInfoList.get(0).getaSiteId() == cesInfo.getaSiteId()) {
				cesInfoList.get(0).setaAcId(cesInfo.getaAcId());
			} else {
				cesInfoList.get(0).setzAcId(cesInfo.getzAcId());
			}

			cesInfoService.update(cesInfoList);

		} catch (Exception e) {
			throw e;
		} finally {
		}

	}

	/**
	 * ac 对象与数据库信息同步
	 * 
	 * @author wangwf
	 * 
	 * @param acPortInfo
	 *            数据库acPortInfo对象
	 * @param siteid
	 *            网元id
	 * 
	 * @throws Exception
	 * 
	 * @Exception 异常对象
	 * 
	 * @return acId AC的数据库主键ID
	 */
	public int acPortInfoSynchro(AcPortInfo acPortInfo, int siteid) throws Exception {
		AcPortInfoService_MB acInfoService = null;
		List<AcPortInfo> acPortInfoList = null;
		SiteService_MB siteService = null;
		int acId = 0;
		PortService_MB portService = null;
		PortLagService_MB lagService = null;
		try {
			/*by guoqc*****************************************************/
			int[] acRelevanceArr = new int[6];//ac关联规则的规则集合，元素为1表示关联，0表示不关联
			int vlanRelevance = 0;
			int eightIpRelevance = 0;
			int sourMacRelevance = 0;
			int endMacRelevance = 0;
			int sourIPRelevance = 0;
			int endIPRelevance = 0;
			if(acPortInfo.getPortId() > 0){
				portService = (PortService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORT);
				PortInst port = new PortInst();
				port.setPortId(acPortInfo.getPortId());
				List<PortInst> portList = portService.select(port);
				if(portList != null && !portList.isEmpty()){
					port = portList.get(0);
					vlanRelevance = Integer.parseInt(UiUtil.getCodeById(port.getPortAttr().getPortUniAttr().getVlanRelevance()).getCodeValue());
					eightIpRelevance = Integer.parseInt(UiUtil.getCodeById(port.getPortAttr().getPortUniAttr().getEightIpRelevance()).getCodeValue());
					sourMacRelevance = Integer.parseInt(UiUtil.getCodeById(port.getPortAttr().getPortUniAttr().getSourceMacRelevance()).getCodeValue());
					endMacRelevance = Integer.parseInt(UiUtil.getCodeById(port.getPortAttr().getPortUniAttr().getDestinationMacRelevance()).getCodeValue());
					sourIPRelevance = Integer.parseInt(UiUtil.getCodeById(port.getPortAttr().getPortUniAttr().getSourceIpRelevance()).getCodeValue());
					endIPRelevance = Integer.parseInt(UiUtil.getCodeById(port.getPortAttr().getPortUniAttr().getDestinationIpRelevance()).getCodeValue());
				}
			}else if(acPortInfo.getLagId() > 0){
				lagService = (PortLagService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORTLAG);
				PortLagInfo lag = new PortLagInfo();
				lag.setId(acPortInfo.getLagId());
				List<PortLagInfo> lagList = lagService.selectPortByCondition(lag);
				if(lagList != null && !lagList.isEmpty()){
					lag = lagList.get(0);
					vlanRelevance = lag.getVlanRelating();
				    eightIpRelevance = lag.getRelatingSet();
				    sourMacRelevance = lag.getFountainMAC();
				    endMacRelevance = lag.getAimMAC();
				    sourIPRelevance = lag.getFountainIP();
				    endIPRelevance = lag.getAimIP();
				}
			}
			acRelevanceArr[0] = vlanRelevance;
			acRelevanceArr[1] = eightIpRelevance;
			acRelevanceArr[2] = sourMacRelevance;
			acRelevanceArr[3] = endMacRelevance;
			acRelevanceArr[4] = sourIPRelevance;
			acRelevanceArr[5] = endIPRelevance;
			
			siteService=(SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE);
			acInfoService = (AcPortInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.AcInfo);
			if (siteService.getManufacturer(siteid) == EManufacturer.CHENXIAO.getValue()) {
				acPortInfoList = acInfoService.selectByCondition_synchro(acPortInfo);
			} else {
				acPortInfoList = acInfoService.select_vlan(acPortInfo, acRelevanceArr);
			}
			/*end**************************************************************/
			if (null == acPortInfoList) {
				throw new Exception("同步ac时 查询ac出错");
			}

			switch (acPortInfoList.size()) {
			case 0:
				// 插入ac
				acId = acInfoService.saveOrUpdate(acPortInfo.getBufferList(), acPortInfo);
				break;
			case 1:
				// 修改ac
				acId = updateAcPortInfo(acPortInfoList, acPortInfo, acInfoService);
				break;

			default:
				throw new Exception("同步ac时 查询ac出错");
			}
        
		} catch (Exception e) {
			System.err.print(e);
			//将插入的AC在数据库删除
			if(acPortInfoList.size() == 0 && acId >0){
				acInfoService.delete(acId);
			}else{
				//恢复原来的数据
				if(acPortInfoList != null && acPortInfoList.size() >0){
					acInfoService.update(acPortInfoList.get(0));
				}
			}
			throw e;
		} finally {
			UiUtil.closeService_MB(acInfoService);
			UiUtil.closeService_MB(siteService);
			UiUtil.closeService_MB(portService);
			UiUtil.closeService_MB(lagService);
		}
		return acId;
	}

	/**
	 * 修改ac数据库
	 * 
	 * @author wangwf
	 * 
	 * @param acPortInfoList
	 *            数据库查询出来的ac对象
	 * @param acPortInfo
	 *            设备上的acPortInfo对象
	 * @param acInfoService
	 *            acInfoService
	 * @throws Exception
	 * 
	 * @Exception 异常对象
	 * 
	 * @return result 返回的是ac的数据库主键ID
	 */
	private int  updateAcPortInfo(List<AcPortInfo> acPortInfoList, AcPortInfo acPortInfo, AcPortInfoService_MB acInfoService) throws Exception {

		int result = 0;
		if (null == acPortInfoList || acPortInfoList.size() != 1) {
			throw new Exception("acPortInfoList is null");
		}
		if (null == acPortInfo) {
			throw new Exception("acPortInfo is null");
		}
		if (null == acInfoService) {
			throw new Exception("acInfoService is null");
		}
		OamInfoService_MB oamInfoService = null;
		try {
			oamInfoService = (OamInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.OamInfo);
			AcPortInfo acPortInfoNew = acPortInfoList.get(0);
			acPortInfoNew.setAcBusinessId(acPortInfo.getAcBusinessId());
			acPortInfoNew.setVlanId(acPortInfo.getVlanId());
			acPortInfoNew.setPortModel(acPortInfo.getPortModel());
			acPortInfoNew.setSimpleQos(acPortInfo.getSimpleQos());
			acPortInfoNew.setBufType(acPortInfo.getBufType());
			acPortInfoNew.setJobStatus(acPortInfo.getJobStatus());
			acPortInfoNew.setManagerEnable(acPortInfo.getManagerEnable());
			if (acPortInfo.getExitRule() > 0) {
				acPortInfoNew.setExitRule(acPortInfo.getExitRule());
			}
			acPortInfoNew.setBufferList(acPortInfo.getBufferList());
			acPortInfoNew.setVlancri(acPortInfo.getVlancri());
			acPortInfoNew.setVlanpri(acPortInfo.getVlanpri());
			acPortInfoNew.setHorizontalDivision(acPortInfo.getHorizontalDivision());
			acPortInfoNew.setMacAddressLearn(acPortInfo.getMacAddressLearn());
			acPortInfoNew.setTagAction(acPortInfo.getTagAction());
//			acPortInfoNew.setIsUser(acPortInfo.getIsUser());
			acPortInfo.setId(acPortInfoNew.getId());
			acPortInfoNew.setAcStatus(acPortInfo.getAcStatus());
			acPortInfoNew.setLanId(acPortInfo.getLanId());
			acInfoService.update(acPortInfoNew);

			result = acPortInfoNew.getId();
			// if (acPortInfo.getOamList() != null && acPortInfo.getOamList().size() > 0) {
			// List<OamInfo> oamList = acPortInfo.getOamList();
			// for (OamInfo oamInfo : oamList) {
			// if (oamInfo.getOamType() == OamTypeEnum.AMEP || oamInfo.getOamType() == OamTypeEnum.ZMEP) {
			// oamInfo.getOamMep().setObjId(acPortInfo.getAcBusinessId());
			// } else if (oamInfo.getOamType() == OamTypeEnum.MEP) {
			//
			// } else if (oamInfo.getOamType() == OamTypeEnum.MIP) {
			//
			// }
			// oamInfoService.saveOrUpdate(oamInfo);
			// }
			// }
		} catch (Exception e) {
			throw e;
		} finally {
			UiUtil.closeService_MB(oamInfoService);
		}
		return result;

	}

	/**
	 * 
	 * etree与数据库同步
	 * 
	 * @author kk
	 * 
	 * @param
	 * 
	 * @return
	 * @throws Exception
	 * 
	 * @Exception 异常对象
	 */
	public List<Integer> etreeSynchro(EtreeInfoService_MB etreeService,List<EtreeInfo> etreeInfoList, int siteId) throws Exception {

		if (null == etreeInfoList || etreeInfoList.size() == 0) {
			throw new Exception("etreeInfoList is null");
		}
//		EtreeService etreeService = null;
		String role = null;
		List<EtreeInfo> etreeInfoList_db = null;
		List<Integer> etreeIdList = new ArrayList<Integer>();
		try {
			if (etreeInfoList.get(0).getRootSite() == 0) {
				role = TypeAndActionUtil.ACTION_BRANCH;
			} else {
				role = TypeAndActionUtil.ACTION_ROOT;
			}

//			etreeService = (EtreeService) ConstantUtil.serviceFactory.newService(Services.EtreeInfo);
			etreeInfoList_db = etreeService.select_synchro(etreeInfoList.get(0), role);

			switch (etreeInfoList_db.size()) {
			case 0:
				// 插入ac
				etreeIdList = etreeService.insert(etreeInfoList);
				break;
			default:
				updateEtree(etreeInfoList_db, etreeInfoList, etreeService, role);
				for (EtreeInfo etreeInfo : etreeInfoList_db) {
					etreeIdList.add(etreeInfo.getId());
				}
				break;
			}
		} catch (Exception e) {
			if(etreeInfoList_db.size() == 0){
				etreeService.deleteById(etreeIdList);
			}else{
				etreeService.update(etreeInfoList_db);
			}
			throw e;
		} finally {
//			UiUtil.closeService(etreeService);
			role = null;
			etreeInfoList_db = null;
		}
       return etreeIdList;
	}

	/**
	 * 
	 * 
	 * updateEtree(这里用一句话描述这个方法的作用)
	 * 
	 * @author kk
	 * 
	 * @param
	 * 
	 * @return
	 * @throws Exception
	 * 
	 * @Exception 异常对象
	 */
	private void updateEtree(List<EtreeInfo> etreeInfoList_db, List<EtreeInfo> etreeInfoList, EtreeInfoService_MB etreeService, String role) throws Exception {
		if (null == etreeInfoList_db) {
			throw new Exception("etreeInfoList_db is null");
		}
		if (null == etreeInfoList) {
			throw new Exception("etreeInfoList is null");
		}
		if (null == etreeService) {
			throw new Exception("etreeService is null");
		}

		try {
			if (etreeInfoList_db.size() != etreeInfoList.size()) {
				throw new Exception("etreeInfoList_db、etreeInfoList 元素数量错误");
			}

			for (int i = 0; i < etreeInfoList_db.size(); i++) {
				etreeInfoList_db.get(i).setActiveStatus(EActiveStatus.ACTIVITY.getValue());
				etreeInfoList_db.get(i).setPwId(etreeInfoList.get(i).getPwId());
				if (TypeAndActionUtil.ACTION_ROOT.equals(role)) {
					etreeInfoList_db.get(i).setaAcId(etreeInfoList.get(i).getaAcId());
				} else {
					etreeInfoList_db.get(i).setzAcId(etreeInfoList.get(i).getzAcId());
				}
			}
			etreeService.update(etreeInfoList_db);

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * elan设备与数据库同步
	 * 
	 * @author kk
	 * 
	 * @param
	 * 
	 * @return
	 * @throws Exception
	 * 
	 * @Exception 异常对象
	 */
	public void elanSynchro(ElanInfoService_MB elanInfoService,List<ElanInfo> elanInfoList, int siteId) throws Exception {
		if (null == elanInfoList || elanInfoList.size() == 0) {
			throw new Exception("elanInfoList is null");
		}
		List<ElanInfo> elanInfoList_db = null;
		try {
			elanInfoList_db = elanInfoService.select_synchro(elanInfoList.get(0));
			switch (elanInfoList_db.size()) {
			case 0:
				elanInfoService.insert(elanInfoList);
				break;
			default:
				updateElan(elanInfoList_db, elanInfoList, elanInfoService, siteId);
				break;
			}

		} catch (Exception e) {
			throw e;
		} finally {
			elanInfoList_db = null;
		}
	}

	/**
	 * 
	 * 修改elandb
	 * 
	 * @author kk
	 * 
	 * @param
	 * 
	 * @return
	 * @throws Exception
	 * 
	 * @Exception 异常对象
	 */
	private void updateElan(List<ElanInfo> elanInfoList_db, List<ElanInfo> elanInfoList, ElanInfoService_MB elanInfoService, int siteId) throws Exception {

		if (null == elanInfoList_db) {
			throw new Exception("elanInfoList_db is null");
		}
		if (null == elanInfoList) {
			throw new Exception("elanInfoList is null");
		}
		if (null == elanInfoService) {
			throw new Exception("elanInfoService is null");
		}
		try {
			if (elanInfoList_db.size() != elanInfoList.size()) {
				throw new Exception("elanInfoList_db、elanInfoList 元素数量错误");
			}

			for (int i = 0; i < elanInfoList_db.size(); i++) {
				elanInfoList_db.get(i).setActiveStatus(EActiveStatus.ACTIVITY.getValue());
				elanInfoList_db.get(i).setPwId(elanInfoList.get(i).getPwId());
				if (elanInfoList_db.get(i).getaSiteId() == elanInfoList.get(i).getaSiteId()) {
			    	elanInfoList_db.get(i).setaAcId(elanInfoList.get(i).getaAcId());
			   } else {
				    elanInfoList_db.get(i).setzAcId(elanInfoList.get(i).getaAcId());
			   }
			
			}

			elanInfoService.update(elanInfoList_db);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 修改数据库端口对象
	 * 
	 * @author kk
	 * 
	 * @param portInst
	 *            端口对象
	 * 
	 * @return
	 * @throws Exception
	 * 
	 * @Exception 异常对象
	 */
	public void updatePort(PortInst portInst) throws Exception {

		PortService_MB portService = null;
		PortInst portInst_db = null;
		List<PortInst> portInstList = null;
		SiteService_MB siteService = null;
		try {
			siteService=(SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE);
			portService = (PortService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORT);

			portInst_db = new PortInst();
			portInst_db.setSiteId(portInst.getSiteId());
			// 根据设备厂商不同，查询端口的条件不同
			if (siteService.getManufacturer(portInst.getSiteId()) == EManufacturer.WUHAN.getValue()) {
				portInst_db.setNumber(portInst.getNumber());
			} else {
				portInst_db.setPortName(portInst.getPortName());
			}

			portInstList = portService.select(portInst_db);

			if (null == portInstList || portInstList.size() != 1) {
				ExceptionManage.logDebug("同步ETHPORT时没有找到" + portInst_db.getPortName() + "端口",SynchroUtil.class);
				return;
			}
			portInst_db = portInstList.get(0);
			portInst_db.setPortType(portInst.getPortType());
			portInst_db.setIsEnabled_code(portInst.getIsEnabled_code());
			portInst_db.setIsEnabled_QinQ(portInst.getIsEnabled_QinQ());
			portInst_db.setIsEnabledLaser(portInst.getIsEnabledLaser());
			if(portInst_db.getQosQueues() != null && portInst_db.getQosQueues().size()>0){
				for (int i = 0; i < portInst_db.getQosQueues().size(); i++) {
					int id = portInst_db.getQosQueues().get(i).getId();
					CoderUtils.copy(portInst.getQosQueues().get(i),portInst_db.getQosQueues().get(i));
					portInst_db.getQosQueues().get(i).setId(id);
				}
			}
			// 从驱动对象取值，进行修改
			convertPort(portInst_db, portInst);
			
			portService.update_synchro(portInst_db);

		} catch (Exception e) {
			throw e;
		} finally {
			UiUtil.closeService_MB(portService);
			UiUtil.closeService_MB(siteService);
		}

	}

	/**
	 * 驱动数据与数据库数据同步
	 * 
	 * @author kk
	 * 
	 * @param
	 * 
	 * @return
	 * 
	 * @Exception 异常对象
	 */
	private void convertPort(PortInst portInst_db, PortInst portInst_drive) {

		if (isNull(portInst_drive.getJobStatus())) { // 工作状态
			portInst_db.setJobStatus(portInst_drive.getJobStatus());
		}
		if (null != portInst_drive.getOamInfo()) { // oam
			portInst_db.setOamInfo(portInst_drive.getOamInfo());
		}
		if (isNull(portInst_drive.getMacAddress())) { // mac地址
			portInst_db.setMacAddress(portInst_drive.getMacAddress());
		}
		if (null != portInst_drive.getPortAttr()) {
			if (portInst_drive.getPortAttr().getPortSpeed() > 0) { // 端口速率
				portInst_db.getPortAttr().setPortSpeed(portInst_drive.getPortAttr().getPortSpeed());
			}
			if (isNull(portInst_drive.getPortAttr().getActualSpeed())) { // 实际速率
				portInst_db.getPortAttr().setActualSpeed(portInst_drive.getPortAttr().getActualSpeed());
			}
			if (portInst_drive.getPortAttr().getWorkModel() > 0) { // 工作模式，对应code表主键
				portInst_db.getPortAttr().setWorkModel(portInst_drive.getPortAttr().getWorkModel());
			}
			if (isNull(portInst_drive.getPortAttr().getMaxFrameSize())) { // 最大帧长=武汉MTU
				portInst_db.getPortAttr().setMaxFrameSize(portInst_drive.getPortAttr().getMaxFrameSize());
			}
			if (portInst_drive.getPortAttr().getFluidControl() > 0) { // 流控=武汉802.3流控
				portInst_db.getPortAttr().setFluidControl(portInst_drive.getPortAttr().getFluidControl());
			}
			if (isNull(portInst_drive.getPortAttr().getExitLimit())) { // 出口限速
				portInst_db.getPortAttr().setMaxFrameSize(portInst_drive.getPortAttr().getMaxFrameSize());
			}
			if (isNull(portInst_drive.getPortAttr().getEntranceLimit())) { // 入口限速
				portInst_db.getPortAttr().setEntranceLimit(portInst_drive.getPortAttr().getEntranceLimit());
			}
			if (portInst_drive.getPortAttr().getSlowAgreement() == 0 || portInst_drive.getPortAttr().getSlowAgreement() == 1) { // 启动慢协议
				portInst_db.getPortAttr().setSlowAgreement(portInst_drive.getPortAttr().getSlowAgreement());
			}
			if (portInst_drive.getPortAttr().getTenWan() == 0 || portInst_drive.getPortAttr().getTenWan() == 1) { // 启动10G WAN
				portInst_db.getPortAttr().setTenWan(portInst_drive.getPortAttr().getTenWan());
			}
			if (portInst_drive.getPortAttr().getMessageLoopback() == 0 || portInst_drive.getPortAttr().getMessageLoopback() == 1) { // 是否启动报文环回
				portInst_db.getPortAttr().setMessageLoopback(portInst_drive.getPortAttr().getMessageLoopback());
			}
			if (portInst_drive.getPortAttr().getSfpExpectType() > 0) { // SFP期望类型
				portInst_db.getPortAttr().setSfpExpectType(portInst_drive.getPortAttr().getSfpExpectType());
			}
			if (isNull(portInst_drive.getPortAttr().getSfpActual())) { // SFP实际类型
				portInst_db.getPortAttr().setSfpActual(portInst_drive.getPortAttr().getSfpActual());
			}
			if (isNull(portInst_drive.getPortAttr().getWorkWavelength())) { // 工作波长
				portInst_db.getPortAttr().setWorkWavelength(portInst_drive.getPortAttr().getWorkWavelength());
			}
			if (isNull(portInst_drive.getPortAttr().getSfpVender())) { // sfp厂家信息
				portInst_db.getPortAttr().setSfpVender(portInst_drive.getPortAttr().getSfpVender());
			}

			// uni信息
			if (null != portInst_drive.getPortAttr().getPortUniAttr()) {
				if (portInst_drive.getPortAttr().getPortUniAttr().getEthernetPackaging() > 0) { // 以太网封装 对应code表主键
					portInst_db.getPortAttr().getPortUniAttr().setEthernetPackaging(portInst_drive.getPortAttr().getPortUniAttr().getEthernetPackaging());
				}
				if (portInst_drive.getPortAttr().getPortUniAttr().getVlanTpId() > 0) { // 运营商vlantpid 对应code表主键
					portInst_db.getPortAttr().getPortUniAttr().setVlanTpId(portInst_drive.getPortAttr().getPortUniAttr().getVlanTpId());
				}
				if (portInst_drive.getPortAttr().getPortUniAttr().getOuterVlanTpId() > 0) { // outer vlan tp id
					portInst_db.getPortAttr().getPortUniAttr().setOuterVlanTpId(portInst_drive.getPortAttr().getPortUniAttr().getOuterVlanTpId());
				}
				if (portInst_drive.getPortAttr().getPortUniAttr().getVlanMode() > 0) { // 以太网vlan模式
					portInst_db.getPortAttr().getPortUniAttr().setVlanMode(portInst_drive.getPortAttr().getPortUniAttr().getVlanMode());
				}
				if (portInst_drive.getPortAttr().getPortUniAttr().getIsBroadcast() > 0) { // 广播包抑制开关
					portInst_db.getPortAttr().getPortUniAttr().setIsBroadcast(portInst_drive.getPortAttr().getPortUniAttr().getIsBroadcast());
				}
				if (isNull(portInst_drive.getPortAttr().getPortUniAttr().getBroadcast())) { // 广播报文抑制=武汉 广播包抑制
					portInst_db.getPortAttr().getPortUniAttr().setBroadcast(portInst_drive.getPortAttr().getPortUniAttr().getBroadcast());
				}
				if (portInst_drive.getPortAttr().getPortUniAttr().getIsUnicast() > 0) { // 洪泛包抑制开关
					portInst_db.getPortAttr().getPortUniAttr().setIsUnicast(portInst_drive.getPortAttr().getPortUniAttr().getIsUnicast());
				}
				if (isNull(portInst_drive.getPortAttr().getPortUniAttr().getUnicast())) { // 单播报文抑制=洪泛包抑制
					portInst_db.getPortAttr().getPortUniAttr().setUnicast(portInst_drive.getPortAttr().getPortUniAttr().getUnicast());
				}
				if (portInst_drive.getPortAttr().getPortUniAttr().getIsMulticast() > 0) { // 组播包抑制开关
					portInst_db.getPortAttr().getPortUniAttr().setIsMulticast(portInst_drive.getPortAttr().getPortUniAttr().getIsMulticast());
				}
				if (isNull(portInst_drive.getPortAttr().getPortUniAttr().getMulticast())) { // 组播报文抑制=组播包抑制
					portInst_db.getPortAttr().getPortUniAttr().setMulticast(portInst_drive.getPortAttr().getPortUniAttr().getMulticast());
				}
				if (isNull(portInst_drive.getPortAttr().getPortUniAttr().getVlanId())) { // 缺省vlanid
					portInst_db.getPortAttr().getPortUniAttr().setVlanId(portInst_drive.getPortAttr().getPortUniAttr().getVlanId());
				}
				if (isNull(portInst_drive.getPortAttr().getPortUniAttr().getVlanPri())) { // 缺省vlan优先级
					portInst_db.getPortAttr().getPortUniAttr().setVlanPri(portInst_drive.getPortAttr().getPortUniAttr().getVlanPri());
				}
				if (portInst_drive.getPortAttr().getPortUniAttr().getVlanRelevance() > 0) { // VLAN关联设置
					portInst_db.getPortAttr().getPortUniAttr().setVlanRelevance(portInst_drive.getPortAttr().getPortUniAttr().getVlanRelevance());
				}
				if (portInst_drive.getPortAttr().getPortUniAttr().getEightIpRelevance() > 0) { // 802.iP关联设置
					portInst_db.getPortAttr().getPortUniAttr().setEightIpRelevance(portInst_drive.getPortAttr().getPortUniAttr().getEightIpRelevance());
				}
				if (portInst_drive.getPortAttr().getPortUniAttr().getSourceMacRelevance() > 0) { // 源MAC地址关联设置
					portInst_db.getPortAttr().getPortUniAttr().setSourceMacRelevance(portInst_drive.getPortAttr().getPortUniAttr().getSourceMacRelevance());
				}
				if (portInst_drive.getPortAttr().getPortUniAttr().getDestinationMacRelevance() > 0) { // 目的MAC地址关联设置
					portInst_db.getPortAttr().getPortUniAttr().setDestinationMacRelevance(portInst_drive.getPortAttr().getPortUniAttr().getDestinationMacRelevance());
				}
				if (portInst_drive.getPortAttr().getPortUniAttr().getSourceIpRelevance() > 0) { // 源IP地址关联设置
					portInst_db.getPortAttr().getPortUniAttr().setSourceIpRelevance(portInst_drive.getPortAttr().getPortUniAttr().getSourceIpRelevance());
				}
				if (portInst_drive.getPortAttr().getPortUniAttr().getDestinationIpRelevance() > 0) { // 目的IP地址关联设置
					portInst_db.getPortAttr().getPortUniAttr().setDestinationIpRelevance(portInst_drive.getPortAttr().getPortUniAttr().getDestinationIpRelevance());
				}
				if (portInst_drive.getPortAttr().getPortUniAttr().getPwRelevance() > 0) { // pw关联设置
					portInst_db.getPortAttr().getPortUniAttr().setPwRelevance(portInst_drive.getPortAttr().getPortUniAttr().getPwRelevance());
				}
				if (portInst_drive.getPortAttr().getPortUniAttr().getDscpRelevance() > 0) { // dscp关联设置
					portInst_db.getPortAttr().getPortUniAttr().setDscpRelevance(portInst_drive.getPortAttr().getPortUniAttr().getDscpRelevance());
				}
				if (portInst_drive.getPortAttr().getPortUniAttr().getMappingEnable() > 0) { // pri映射
					portInst_db.getPortAttr().getPortUniAttr().setMappingEnable(portInst_drive.getPortAttr().getPortUniAttr().getMappingEnable());
				}
				if (portInst_drive.getPortAttr().getPortUniAttr().getSourceTcpPortRelevance() > 0) { // 源tcp
					portInst_db.getPortAttr().getPortUniAttr().setSourceTcpPortRelevance(portInst_drive.getPortAttr().getPortUniAttr().getSourceTcpPortRelevance());
				}
				if (portInst_drive.getPortAttr().getPortUniAttr().getEndTcpPortRelevance() > 0) { // 目的tcp
					portInst_db.getPortAttr().getPortUniAttr().setEndTcpPortRelevance(portInst_drive.getPortAttr().getPortUniAttr().getEndTcpPortRelevance());
				}
				if (portInst_drive.getPortAttr().getPortUniAttr().getTosRelevance() > 0) { // tos关联设置
					portInst_db.getPortAttr().getPortUniAttr().setTosRelevance(portInst_drive.getPortAttr().getPortUniAttr().getTosRelevance());
				}
			}
			// nni信息
			if (null != portInst_drive.getPortAttr().getPortNniAttr()) {
				if (isNull(portInst_drive.getPortAttr().getPortNniAttr().getStaticMac())) { // 静态MAC地址
					portInst_db.getPortAttr().getPortNniAttr().setStaticMac(portInst_drive.getPortAttr().getPortNniAttr().getStaticMac());
				}
				if (isNull(portInst_drive.getPortAttr().getPortNniAttr().getNeighbourId())) { // 邻居网元ID
					portInst_db.getPortAttr().getPortNniAttr().setNeighbourId(portInst_drive.getPortAttr().getPortNniAttr().getNeighbourId());
				}
				if (isNull(portInst_drive.getPortAttr().getPortNniAttr().getOperMac())) { // 对端接口mac地址
					portInst_db.getPortAttr().getPortNniAttr().setOperMac(portInst_drive.getPortAttr().getPortNniAttr().getOperMac());
				}
				if (isNull(portInst_drive.getPortAttr().getPortNniAttr().getOperId())) { // 对端接口ID
					portInst_db.getPortAttr().getPortNniAttr().setOperId(portInst_drive.getPortAttr().getPortNniAttr().getOperId());
				}
				if (portInst_drive.getPortAttr().getPortNniAttr().getNeighbourFind() > 0) { // 邻居发现状态 对应code表主键
					portInst_db.getPortAttr().getPortNniAttr().setNeighbourFind(portInst_drive.getPortAttr().getPortNniAttr().getNeighbourFind());
				}
				if (portInst_drive.getPortAttr().getPortNniAttr().getCcnEnable() == 0 || portInst_drive.getPortAttr().getPortNniAttr().getCcnEnable() == 1) { // ccn承载使能 0=false 1=true
					portInst_db.getPortAttr().getPortNniAttr().setCcnEnable(portInst_drive.getPortAttr().getPortNniAttr().getCcnEnable());
				}
				if (isNull(portInst_drive.getPortAttr().getPortNniAttr().getNniVlanid())) { // 缺省vlanid
					portInst_db.getPortAttr().getPortNniAttr().setNniVlanid(portInst_drive.getPortAttr().getPortNniAttr().getNniVlanid());
				}
				if (isNull(portInst_drive.getPortAttr().getPortNniAttr().getNniVlanpri())) { // 缺省vlan优先级
					portInst_db.getPortAttr().getPortNniAttr().setNniVlanpri(portInst_drive.getPortAttr().getPortNniAttr().getNniVlanpri());
				}
				//NNI的是否连接段
				portInst_db.getPortAttr().getPortNniAttr().setStat(portInst_drive.getPortAttr().getPortNniAttr().getStat());
			}
		}

	}

	/**
	 * 判断字符串是否为null或者""
	 * 
	 * @author kk
	 * 
	 * @param
	 * 
	 * @return 如果不是返回true 否则false
	 * 
	 * @Exception 异常对象
	 */
	private boolean isNull(String text) {

		if (null != text && !"".equals(text)) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * ccn 对象与数据库信息同步
	 * 
	 * @param ccn
	 * @throws Exception
	 */
	public static void ccnSynchro(CCN ccn) throws Exception {
		CCNService_MB service = null;
		List<CCN> ccnList = null;
		CCN ccn_db = null;
		try {
			service = (CCNService_MB) ConstantUtil.serviceFactory.newService_MB(Services.CCN);
			ccnList = service.queryByNeInAndName(ccn.getCcnName(), Integer.valueOf(ccn.getNeId()));

			if (ccnList.size() == 0) {
				service.insert(ccn);
			} else {

				if (ccnList.size() > 1) {
					ExceptionManage.logDebug("查询CCN出现异常",SynchroUtil.class);
					return;
				}
				ccn_db = ccnList.get(0);
				ccn_db.setAdmin(ccn.getAdmin());
				ccn_db.setOper(ccn.getOper());
				ccn_db.setIp(ccn.getIp());
				ccn_db.setPeer(ccn.getPeer());
				ccn_db.setMtu(ccn.getMtu());
				ccn_db.setPortname(ccn.getPortname());
				ccn_db.setStatus(ccn.getStatus());

				service.update(ccn_db);
			}

		} catch (Exception e) {
			throw e;
		} finally {
			UiUtil.closeService_MB(service);
		}
	}

	/**
	 * OSPFAREA 对象与数据库信息同步
	 * 
	 * @author wangwf
	 * 
	 * @param ospfAREAInfo
	 *            数据库ospfAREAInfo对象
	 * @param siteid
	 *            网元id
	 * 
	 * @return
	 * @throws Exception
	 * 
	 * @Exception 异常对象
	 */
	public void ospfAreaSynchro(OSPFAREAInfo ospfAREAInfo, int siteid) throws Exception {
		OSPFAREAService_MB ospfAREAService = null;
		List<OSPFAREAInfo> ospfAREAInfoList = null;
		try {
			ospfAREAService = (OSPFAREAService_MB) ConstantUtil.serviceFactory.newService_MB(Services.OSPFAREA);
			ospfAREAInfoList = ospfAREAService.queryByNeIDAndAreaRange(siteid + "", ospfAREAInfo.getArea_range());

			switch (ospfAREAInfoList.size()) {
			case 0:
				ospfAREAService.insert(ospfAREAInfo);
				break;
			case 1:
				updateOSPFAREAInfo(ospfAREAInfoList, ospfAREAInfo, ospfAREAService);
				break;

			default:
				throw new Exception("同步OSPFAREAInfo时 查询OSPFAREAInfo出错");
			}

		} catch (Exception e) {
			throw e;
		} finally {
			UiUtil.closeService_MB(ospfAREAService);
		}
	}

	/**
	 * 修改OSPFAREAInfo数据库
	 * 
	 * @author wangwf
	 * 
	 * @param ospfAREAInfoList
	 *            数据库中的OSPFAREAInfo对象
	 * @param ospfAREAInfo
	 *            设备中的OSPFAREAInfo对象
	 * @return
	 * @throws Exception
	 * 
	 * @Exception 异常对象
	 */
	private void updateOSPFAREAInfo(List<OSPFAREAInfo> ospfAREAInfoList, OSPFAREAInfo ospfAREAInfo, OSPFAREAService_MB ospfAREAService) throws Exception {

		if (null == ospfAREAInfoList || ospfAREAInfoList.size() != 1) {
			throw new Exception("ospfAREAInfoList is null");
		}
		if (null == ospfAREAInfo) {
			throw new Exception("ospfAREAInfo is null");
		}
		if (null == ospfAREAService) {
			throw new Exception("ospfAREAService is null");
		}

		OSPFAREAInfo ospfAREAInfo_db = null;
		try {
			ospfAREAInfo_db = ospfAREAInfoList.get(0);
			ospfAREAInfo_db.setArea_range(ospfAREAInfo.getArea_range());
			ospfAREAInfo_db.setNeId(ospfAREAInfo.getNeId());
			ospfAREAInfo_db.setType(ospfAREAInfo.getType());
			ospfAREAInfo_db.setStatus(ospfAREAInfo.getStatus());
			ospfAREAInfo_db.setMetric(ospfAREAInfo.getMetric());
			ospfAREAInfo_db.setSummary(ospfAREAInfo.getSummary());
			ospfAREAService.update(ospfAREAInfo_db);

		} catch (Exception e) {
			throw e;
		} finally {
			ospfAREAInfo_db = null;
		}
	}

	/**
	 * OspfRedistribute 对象与数据库信息同步
	 * 
	 * @author wangwf
	 * 
	 * @param OspfRedistribute
	 *            数据库OspfRedistribute对象
	 * @param siteid
	 *            网元id
	 * 
	 * @return
	 * @throws Exception
	 * 
	 * @Exception 异常对象
	 */
	public void ospfRedistributeSynchro(OspfRedistribute ospfRedistribute, int siteid) throws Exception {
		RedistributeService_MB redistributeService = null;
		List<OspfRedistribute> ospfRedistributeList = null;
		try {
			redistributeService = (RedistributeService_MB) ConstantUtil.serviceFactory.newService_MB(Services.REDISTRIBUTE);
			ospfRedistributeList = redistributeService.queryByNeIdAndNameRedistributeType(ospfRedistribute.getNeId(), ospfRedistribute.getRedistribute_type());

			switch (ospfRedistributeList.size()) {
			case 0:
				redistributeService.insert(ospfRedistribute);
				break;
			case 1:
				updateOspfRedistribute(ospfRedistributeList, ospfRedistribute, redistributeService);
				break;

			default:
				throw new Exception("同步OspfRedistribute时 查询OspfRedistribute出错");
			}

		} catch (Exception e) {
			throw e;
		} finally {
			UiUtil.closeService_MB(redistributeService);
		}
	}

	/**
	 * 修改OspfRedistribute数据库
	 * 
	 * @author wangwf
	 * 
	 * @param ospfRedistributeList
	 *            数据库中的OspfRedistribute对象
	 * @param ospfRedistribute
	 *            设备中的OspfRedistribute对象
	 * @return
	 * @throws Exception
	 * 
	 * @Exception 异常对象
	 */
	private void updateOspfRedistribute(List<OspfRedistribute> ospfRedistributeList, OspfRedistribute ospfRedistribute, RedistributeService_MB redistributeService) throws Exception {

		if (null == ospfRedistributeList || ospfRedistributeList.size() != 1) {
			throw new Exception("ospfRedistributeList is null");
		}
		if (null == ospfRedistribute) {
			throw new Exception("ospfRedistribute is null");
		}
		if (null == redistributeService) {
			throw new Exception("redistributeService is null");
		}

		OspfRedistribute ospfRedistribute_db = null;
		try {
			ospfRedistribute_db = new OspfRedistribute();
			ospfRedistribute_db.setNeId(ospfRedistribute.getNeId());
			ospfRedistribute_db.setRedistribute_type(ospfRedistribute.getRedistribute_type());
			ospfRedistribute_db.setType(ospfRedistribute.getType());
			ospfRedistribute_db.setMetrictype(ospfRedistribute.getMetrictype());
			ospfRedistribute_db.setMetric(ospfRedistribute.getMetric());
			ospfRedistribute_db.setStatus(ospfRedistribute.getStatus());
			ospfRedistribute_db.setEnable(ospfRedistribute.getEnable());
			redistributeService.update(ospfRedistribute_db);

		} catch (Exception e) {
			throw e;
		} finally {
			ospfRedistribute_db = null;
		}
	}

	/**
	 * OSPFInfo 对象与数据库信息同步
	 * 
	 * @author wangwf
	 * 
	 * @param OSPFInfo
	 *            数据库OSPFInfo对象
	 * @param siteid
	 *            网元id
	 * 
	 * @return
	 * @throws Exception
	 * 
	 * @Exception 异常对象
	 */
	public void ospfInfoSynchro(OSPFInfo ospfInfo, int siteid) throws Exception {
		OSPFInfoService_MB ospfInfoService = null;
		OSPFInfo ospfInfo_db = null;
		try {
			ospfInfoService = (OSPFInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.OSPFInfo);
			ospfInfo_db = ospfInfoService.queryByNeID(siteid);

			if (null == ospfInfo_db) {
				ospfInfoService.insert(ospfInfo);
			} else {
				ospfInfo.setRt_id_area(ospfInfo.getRt_id_area());
				ospfInfo.setDefmetric(ospfInfo.getDefmetric());
				ospfInfo.setAbr(ospfInfo.getAbr());
				ospfInfo.setCompatiblerfc1583(ospfInfo.getCompatiblerfc1583());
				ospfInfo.setRefbandwidth(ospfInfo.getRefbandwidth());
				ospfInfo.setDistance(ospfInfo.getDistance());
				ospfInfo.setSpf_delay(ospfInfo.getSpf_delay());
				ospfInfo.setSpf_holdtime(ospfInfo.getSpf_holdtime());
				ospfInfo.setSpf_maxholdtime(ospfInfo.getSpf_maxholdtime());
				ospfInfo.setRefresh_time(ospfInfo.getRefresh_time());
				ospfInfo.setVersion(ospfInfo.getVersion());
				ospfInfoService.update(ospfInfo_db);
			}

		} catch (Exception e) {
			throw e;
		} finally {
			UiUtil.closeService_MB(ospfInfoService);
		}
	}

	/**
	 * e1口与数据库同步
	 * 
	 * @author kk
	 * 
	 * @param portInst
	 *            要修改的端口对象
	 * @param eInfo
	 *            要修改的e1对象
	 * 
	 * @return
	 * @throws Exception
	 * 
	 * @Exception 异常对象
	 */
	public void pdhportSynchro(PortInst portInst, E1Info eInfo) throws Exception {
		E1InfoService_MB infoService = null;
		List<E1Info> eInfoList = null;
		E1Info eInfo_db = null;
		PortService_MB portService = null;
		try {
			infoService = (E1InfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.E1Info);
			eInfoList = infoService.selectByCondition(eInfo);
			if (null == eInfoList || eInfoList.size() != 1) {
				ExceptionManage.logDebug("同步e1失败，没有查询到" + eInfo.getPortName() + "端口",SynchroUtil.class);
				return;
			}
			// 修改e1表
			eInfo_db = eInfoList.get(0);
			// eInfo_db.setModel(eInfo.getModel());
			eInfo_db.setLinecoding(eInfo.getLinecoding());
			eInfo_db.setImpedance(eInfo.getImpedance());
			infoService.update(eInfo_db);

			// 修改port表
			portService = (PortService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORT);
			portInst.setPortId(eInfo_db.getPortId());
			portService.update_synchro(portInst);

		} catch (Exception e) {
			throw e;
		} finally {
			UiUtil.closeService_MB(infoService);
			UiUtil.closeService_MB(portService);
		}
	}

	/**
	 * MCN 对象与数据库信息同步
	 * 
	 * @author wangwf
	 * 
	 * @param MCN
	 *            数据库MCN对象
	 * @param siteid
	 *            网元id
	 * 
	 * @return
	 * @throws Exception
	 * 
	 * @Exception 异常对象
	 */
	public void mcnSynchro(MCN mcn, int siteid) throws Exception {
		MCNService_MB mcnService = null;
		List<MCN> mcn_dbList = null;
		try {
			mcnService = (MCNService_MB) ConstantUtil.serviceFactory.newService_MB(Services.MCN);
			mcn_dbList = mcnService.queryBySiteId(siteid);

			if (mcn_dbList.size() > 0) {
				mcnService.update(mcn);
			} else {
				mcnService.insert(mcn);
			}

		} catch (Exception e) {
			throw e;
		} finally {
			UiUtil.closeService_MB(mcnService);
		}
	}

	/**
	 * OSPFINTERFACE 对象与数据库信息同步
	 * 
	 * @author wangwf
	 * 
	 * @param ospfInterface
	 *            数据库ospfInterface对象
	 * @param siteid
	 *            网元id
	 * 
	 * @return
	 * @throws Exception
	 * 
	 * @Exception 异常对象
	 */
	public void ospfInterfaceSynchro(OSPFInterface ospfInterface, int siteid) throws Exception {
		OSPFInterfaceService_MB ospfInterfaceService = null;
		List<OSPFInterface> ospfInterface_dbList = null;
		try {
			boolean isUpdate = false;
			ospfInterfaceService = (OSPFInterfaceService_MB) ConstantUtil.serviceFactory.newService_MB(Services.OSPFINTERFACE);
			ospfInterface_dbList = ospfInterfaceService.queryByNeID(siteid);
			String neId = ospfInterface.getNeId();
			String name = ospfInterface.getInterfaceName();

			for (OSPFInterface ospfInterfaces : ospfInterface_dbList) {
				if (neId.equals(ospfInterfaces.getNeId()) && name.equals(ospfInterfaces.getInterfaceName())) {
					isUpdate = true;
				}
			}

			if (!isUpdate) {
				ospfInterfaceService.insert(ospfInterface);
			} else {
				OSPFInterface ospfInterfacedb = ospfInterface_dbList.get(0);
				ospfInterfacedb.setInterfaceName(ospfInterface.getInterfaceName());
				ospfInterfacedb.setArea(ospfInterface.getArea());
				ospfInterfacedb.setType(ospfInterface.getType());
				ospfInterfacedb.setHello_interval(ospfInterface.getHello_interval());
				ospfInterfacedb.setDead_interval(ospfInterface.getDead_interval());
				ospfInterfacedb.setRetransmit_interval(ospfInterface.getRetransmit_interval());
				ospfInterfacedb.setTransmit_delay(ospfInterface.getTransmit_delay());
				ospfInterfacedb.setCost(ospfInterface.getCost());
				ospfInterfacedb.setPrioriy(ospfInterface.getPrioriy());
				ospfInterfacedb.setActiveStatus(ospfInterface.getActiveStatus());
				ospfInterfacedb.setNeId(ospfInterface.getNeId());
				ospfInterfaceService.updateStatus(ospfInterfacedb);
			}

		} catch (Exception e) {
			throw e;
		} finally {
			UiUtil.closeService_MB(ospfInterfaceService);
		}
	}

	/**
	 * sdh端口对象与数据库信息同步
	 * 
	 * @author wangwf
	 * 
	 * @param portStm
	 *            数据库portStm对象
	 * @param siteid
	 *            网元id
	 * 
	 * @return
	 * @throws Exception
	 * 
	 * @Exception 异常对象
	 */
	public void portStmSynchro(PortStm portStm, int siteid) throws Exception {

		PortStmService_MB portStmService = null;
		PortService_MB portService = null;

		List<PortStm> portStm_dbList = null;
		try {

			portStmService = (PortStmService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORTSTM);
			portService = (PortService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORT);

			PortInst portinst = new PortInst();
			portinst.setSiteId(siteid);
			portinst.setPortName(portStm.getName());
			List<PortInst> portList = portService.select(portinst);
			if (null != portList && portList.size() > 0) {
				portStm_dbList = portStmService.queryBySiteIdAndPortId(siteid, portList.get(0).getPortId());

				if (null == portStm_dbList || portStm_dbList.size() == 0) {
					portStmService.save(portStm);
				} else {
					PortStm portStmdb = portStm_dbList.get(0);
					portStmdb.setJobwavelength(portStm.getJobwavelength());
					portStmdb.setSfpexpect(portStm.getSfpexpect());
					portStmdb.setSfpreality(portStm.getSfpreality());
					portStmdb.setSfpvender(portStm.getSfpvender());
					portStmdb.setStatus(portStm.getStatus());
					portStmdb.setJobstatus(portStm.getJobstatus());
					portStmService.update(portStmdb);
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {

			UiUtil.closeService_MB(portStmService);
			UiUtil.closeService_MB(portService);

		}
	}

	/**
	 * sdh时隙对象与数据库信息同步
	 * 
	 * @author wangwf
	 * 
	 * @param portStmTimeslot
	 *            数据库portStmTimeslot对象
	 * @param siteid
	 *            网元id
	 * 
	 * @return
	 * @throws Exception
	 * 
	 * @Exception 异常对象
	 */
	public void portStmTimeslotSynchro(PortStmTimeslot portStmTimeslot, int siteid) throws Exception {
		PortStmTimeslotService_MB portStmTimeslotService = null;
		List<PortStmTimeslot> portStmTimeslot_dbList = null;
		try {
			portStmTimeslotService = (PortStmTimeslotService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORTSTMTIMESLOT);
			portStmTimeslot_dbList = portStmTimeslotService.selectBySiteIdAndNumberAndName(siteid, portStmTimeslot.getPortid(), portStmTimeslot.getTimeslotname());
			if (null == portStmTimeslot_dbList || portStmTimeslot_dbList.size() == 0) {
				portStmTimeslotService.insert(portStmTimeslot);
			} else {
				PortStmTimeslot portStmTimeslotdb = portStmTimeslot_dbList.get(0);
				portStmTimeslotdb.setManagerStatus(portStmTimeslot.getManagerStatus());
				portStmTimeslotdb.setJobstatus(portStmTimeslot.getJobstatus());
				portStmTimeslotdb.setExpectjtwo(portStmTimeslot.getExpectjtwo());
				portStmTimeslotdb.setSendjtwo(portStmTimeslot.getSendjtwo());
				portStmTimeslotdb.setRealityjtwo(portStmTimeslot.getRealityjtwo());
				portStmTimeslotdb.setExpectvfive(portStmTimeslot.getExpectvfive());
				portStmTimeslotdb.setSendvfive(portStmTimeslot.getSendvfive());
				portStmTimeslotdb.setRealityvfive(portStmTimeslot.getRealityvfive());
				portStmTimeslotdb.setCheckvfive(portStmTimeslot.getCheckvfive());
				portStmTimeslotdb.setJobstatus(portStmTimeslot.getJobstatus());
				portStmTimeslotService.update(portStmTimeslotdb);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			UiUtil.closeService_MB(portStmTimeslotService);
		}
	}

	public static void segmentSynchro(Segment segment, List<QosQueue> qosQueues, List<OamInfo> oamInfos) throws Exception {
		SegmentService_MB segmentService = null;
		try {
			segmentService = (SegmentService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SEGMENT);
			List<Segment> segmentList = segmentService.select_search(segment.getAPORTID(), segment.getZPORTID());
			List<Segment> segmentList1 = segmentService.select_search(segment.getZPORTID(), segment.getAPORTID());
			if ((segmentList!=null && segmentList.size() > 0 ) || (segmentList1!=null && segmentList1.size() > 0)) {

			} else {
				segment.setBANDWIDTH(0);
				segment.setTYPE(0);
				segmentService.saveOrUpdate(segment, qosQueues, oamInfos);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			UiUtil.closeService_MB(segmentService);
		}

	}

	/**
	 * ETH端口对象与数据库同步
	 * 
	 * @author guoqc
	 * @param portInst
	 *            数据库portInst对象
	 * @param siteId
	 *            网元ID
	 * @throws Exception
	 */
	public void portInstSynchro(PortInst portInst) throws Exception {
		PortService_MB portService = null;
		PortLagService_MB portLagService = null;
		List<PortInst> portInstList = null;
		PortInst portInstdb = null;
		try {
			portService = (PortService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORT);
			portLagService = (PortLagService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORTLAG);
			// 根据siteId，portType，number三个条件进行判断
			portInstdb = new PortInst();
			portInstdb.setSiteId(portInst.getSiteId());
			portInstdb.setPortType(portInst.getPortType());
			portInstdb.setNumber(portInst.getNumber());
			portInstList = portService.select(portInstdb);

			if (null == portInstList || 0 == portInstList.size()) {
				// 没有此条件对应的记录，就将此记录插入
				portService.saveOrUpdate(portInst);
				portLagService.saveOrUpdate(portInst.getLagInfo());
			} else if (1 == portInstList.size()) {
				// 有此条件对应的记录，就将此记录做更新操作
				int portId = portInstList.get(0).getPortId();
				portInst.setPortId(portId);
				portService.saveOrUpdate(portInst);
				portInst.getLagInfo().setPortId(portId);
				portLagService.saveOrUpdate(portInst.getLagInfo());
			} else {
				throw new Exception("同步ETH端口出错");
			}

		} catch (Exception e) {
			ExceptionManage.logDebug("同步ETH端口出错 ！！！！",SynchroUtil.class);
		} finally {
			UiUtil.closeService_MB(portService);
			UiUtil.closeService_MB(portLagService);
			portInstList = null;
		}

	}

	/**
	 * Lag配置对象与数据库同步
	 * 
	 * @author guoqc
	 * @param portLagInfo
	 * @throws Exception
	 */
	public void portLagInfoSynchro(PortLagInfo portLagInfo) throws Exception {
		PortLagService_MB portLagService = null;
		List<PortLagInfo> portLagInfoList = null;
		PortLagInfo portLagdb = null;
		try {
			portLagService = (PortLagService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORTLAG);
			// 根据siteId,lagId查询数据库
			portLagdb = new PortLagInfo();
			portLagdb.setSiteId(portLagInfo.getSiteId());
			portLagdb.setLagID(portLagInfo.getLagID());
			portLagInfoList = portLagService.selectByCondition(portLagdb);
			if (null == portLagInfoList || 0 == portLagInfoList.size()) {
				// 没有此条件对应的记录,做插入操作
				portLagService.saveOrUpdate(portLagInfo);
			} else if (1 == portLagInfoList.size()) {
				// 有此条件对应的记录,做更新操作
				portLagInfo.setId(portLagInfoList.get(0).getId());
				portLagService.saveOrUpdate(portLagInfo);
			} else {
				ExceptionManage.logDebug("同步Lag配置管理出错!!!!",SynchroUtil.class);
			}
		} catch (Exception e) {
			ExceptionManage.logDebug("同步Lag配置管理出错!!!!",SynchroUtil.class);
		} finally{
			UiUtil.closeService_MB(portLagService);
		}

	}

	/**
	 * e1对象与数据库同步
	 * 
	 * @author guoqc
	 * @param e1Info
	 * @throws Exception
	 */
	public void e1Synchro(E1Info e1Info,String e1) throws Exception {
		E1InfoService_MB e1InfoService = null;
		E1Info e1Infodb = null;
		List<E1Info> e1InfoList = null;
		List<E1Info> e1InfoList1 = null;
		PortService_MB portService = null;
		List<PortInst> portInsts = null;
		try {
			e1InfoService = (E1InfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.E1Info);
			portService = (PortService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORT);
			// 根据siteId,legId查询数据库
			e1Infodb = new E1Info();
			e1Infodb.setSiteId(e1Info.getSiteId());
			e1Infodb.setLegId(e1Info.getLegId());
			e1InfoList = e1InfoService.selectByCondition(e1Infodb);	
			if(null != e1InfoList && e1InfoList.size()!=0){
				e1InfoList1=new ArrayList<E1Info>();											
				for(int i=0;i< e1InfoList.size();i++){
					if(e1InfoList.get(i).getPortName().startsWith(e1)){
						e1InfoList1.add(e1InfoList.get(i));
					}
				}
			}
			if (null == e1InfoList1 || 0 == e1InfoList1.size()) {
				// 没有此条件对应的记录,做插入操作
				//e1InfoService.saveOrUpdate(e1Info);
			} else if (1 == e1InfoList1.size()) {
				// 有此条件对应的记录,做更新操作
				e1Info.setId(e1InfoList1.get(0).getId());
				e1Info.setIsAlarmRevesal(e1InfoList1.get(0).getIsAlarmRevesal());
				e1Info.setFzType(e1InfoList1.get(0).getFzType());
				e1Info.setFrameformat(e1InfoList1.get(0).getFrameformat());
				e1Info.setComplexFrame(e1InfoList1.get(0).getComplexFrame());	
				e1Info.setPortId(e1InfoList1.get(0).getPortId());
				e1Info.setPortName(e1InfoList1.get(0).getPortName());
				e1Info.setModel(e1InfoList1.get(0).getModel());
				e1Info.setLinecoding(e1InfoList1.get(0).getLinecoding());
				e1Info.setImpedance(e1InfoList1.get(0).getImpedance());
				e1Info.setCardId(e1InfoList1.get(0).getCardId());	
				//修改port表的属性
				PortInst portInst = new PortInst();
				portInst.setPortId(e1InfoList1.get(0).getPortId());				
				portInsts = portService.select(portInst);			
				if(portInsts != null && portInsts.size()>0){
					for(PortInst inst : portInsts){
						    inst.setIsEnabled_code(e1Info.getLegEnable());
						    portService.saveOrUpdate(inst);
						}
					}
				e1InfoService.saveOrUpdate(e1Info);
			} else {
				ExceptionManage.logDebug("同步PDH出错!!!!",SynchroUtil.class);
			}
		} catch (Exception e) {
			ExceptionManage.logDebug("同步PDH出错!!!!",SynchroUtil.class);
		} finally {
			UiUtil.closeService_MB(e1InfoService);
			UiUtil.closeService_MB(portService);
		}
					
	}

	/**
	 * 全局配置块对象与数据库同步
	 * 
	 * @author guoqc
	 * @param allConfigInfo
	 * @throws Exception
	 */
	public void allConfigSynchro(AllConfigInfo allConfigInfo) throws Exception {
		AllConfigService_MB allConfigService = null;
//		List<AllConfigInfo> allConfigInfoList = null;
		try {
			allConfigService = (AllConfigService_MB) ConstantUtil.serviceFactory.newService_MB(Services.ALLCONFIG);
			// 根据siteId查询数据库
//			allConfigInfoList = allConfigService.select(allConfigInfo.getSiteId());
//			if (null == allConfigInfoList || 0 == allConfigInfoList.size()) {
				// 没有此条件对应的记录,做插入操作
				allConfigService.save(allConfigInfo);
//			} else if (1 == allConfigInfoList.size()) {
				// 有此条件对应的记录,做更新操作
//				allConfigService.update(allConfigInfo);
//			} else {
//				throw new Exception("同步全局配置块出错!!!!");
//			}

		} catch (Exception e) {
			ExceptionManage.dispose(e,SynchroUtil.class);
		} finally {
			UiUtil.closeService_MB(allConfigService);
		}

	}
	
	/**
	 * PHB2EXP与数据库同步
	 * 
	 * @author dxh
	 * @param allConfigInfo
	 * @throws Exception
	 */
	public void phbAndexpSynchro(List<QosMappingMode>  qosMaplist, String Type) throws Exception {
		QosMappingModeService_MB modeService = null;
		QosMappingMode mode = null;
		List<QosMappingMode> modeList = new ArrayList<QosMappingMode>();
		try {
			modeService = (QosMappingModeService_MB) ConstantUtil.serviceFactory.newService_MB(Services.QosMappingModeService);
			
			mode = new QosMappingMode();
			mode.setSiteId(qosMaplist.get(0).getSiteId());
			mode.setTypeName(Type);
			// 根据siteId查询数据库
			modeList = modeService.queryByCondition(mode);
			if(Type.equals("EXP_PHB"))
			{
				for(int i=0; i<modeList.size();i++)
				{
					QosMappingMode qosmode = modeList.get(i);
					List<QosMappingAttr> qosMappingAttrList  = qosmode.getQosMappingAttrList();
					for(int j=0; j<qosMappingAttrList.size(); j++)
					{
						qosMappingAttrList.get(j).setGrade(qosMaplist.get(i).getQosMappingAttrList().get(j).getGrade());
					}
				}
			}else if(Type.equals("PHB_EXP"))
			{
				for(int i=0; i<modeList.size();i++)
				{
					QosMappingMode qosmode = modeList.get(i);
					List<QosMappingAttr> qosMappingAttrList  = qosmode.getQosMappingAttrList();
					for(int j=0; j<qosMappingAttrList.size(); j++)
					{
						qosMappingAttrList.get(j).setValue(qosMaplist.get(i).getQosMappingAttrList().get(j).getValue());
					}
				}
			}
			
			if (null == modeList || 0 == modeList.size()) {
				// 没有此条件对应的记录,做插入操作
				for(QosMappingMode QoSMode : modeList)
				{
					modeService.save(QoSMode);
				}
			} else if (0 < modeList.size()) {
				// 有此条件对应的记录,做更新操作
				modeService.Update(modeList);
			} else {
				throw new Exception("同步PHB到EXP出错!!!!");
			}

		} catch (Exception e) {
			ExceptionManage.dispose(e,SynchroUtil.class);
		} finally {
			UiUtil.closeService_MB(modeService);
		}
	}
	/**
	 * 时钟频率对象与数据库同步
	 * 
	 * @author dxh
	 * @param FrequencyInfo
	 * @throws Exception
	 */
	public void frequSynchro(FrequencyInfo frequInfo) throws Exception {
		ClockFrequService_MB clockFreqService = null;
		List<FrequencyInfo> frqInfoList = null;
		try {
			clockFreqService = (ClockFrequService_MB) ConstantUtil.serviceFactory.newService_MB(Services.ClockFrequ);
			// 根据siteId查询数据库
			frqInfoList = clockFreqService.query(frequInfo.getSiteId());
			if (null == frqInfoList || 0 == frqInfoList.size()) {
				// 没有此条件对应的记录,做插入操作
				clockFreqService.insert(frequInfo);
			} else if (1 == frqInfoList.size()) {
				// 有此条件对应的记录,做更新操作
				clockFreqService.update(frequInfo);
			} else {
				throw new Exception("同步时钟配置块出错!!!!");
			}

		} catch (Exception e) {
			ExceptionManage.dispose(e,SynchroUtil.class);
		} finally {
			UiUtil.closeService_MB(clockFreqService);
		}

	}

	/**
	 * 静态单播对象与数据库同步
	 * 
	 * @author guoqc
	 * @param unicastInfo
	 * @throws Exception
	 */
	public void singleSpreadSynchro(StaticUnicastInfo unicastInfo) throws Exception {
		SingleSpreadService_MB singleSpreadService = null;
		List<StaticUnicastInfo> staticUnicastInfoList = null;
		try {
			singleSpreadService = (SingleSpreadService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SINGELSPREAD);
			// 根据siteId查询数据库
			staticUnicastInfoList = singleSpreadService.querybyCondition(unicastInfo);
			if (null == staticUnicastInfoList || 0 == staticUnicastInfoList.size()) {
				// 没有此条件对应的记录,做插入操作
				singleSpreadService.insert(unicastInfo);
			} else if (1 == staticUnicastInfoList.size()) {
				// 有此条件对应的记录,做更新操作
				singleSpreadService.update(unicastInfo);
			} else {
				throw new Exception("同步静态单播出错!!!!!");
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,SynchroUtil.class);
		} finally {
			UiUtil.closeService_MB(singleSpreadService);
		}
	}

	/**
	 * 静态组播数据库同步
	 * 
	 * @author guoqc
	 * @param groupSpreadInfo
	 * @throws Exception
	 */
	public void groupSpreadSynchro(GroupSpreadInfo groupSpreadInfo) throws Exception {
		GroupSpreadService_MB groupSpreadService = null;
		List<GroupSpreadInfo> groupSpreadInfoList = null;
		GroupSpreadInfo groupSpreadInfodb = null;
		try {
			groupSpreadService = (GroupSpreadService_MB) ConstantUtil.serviceFactory.newService_MB(Services.GROUPSPREAD);
			groupSpreadInfodb = new GroupSpreadInfo();
			groupSpreadInfodb.setSiteId(groupSpreadInfo.getSiteId());
			groupSpreadInfoList = groupSpreadService.query(groupSpreadInfodb);
			if (null == groupSpreadInfoList || 1 == groupSpreadInfoList.size()) {
				groupSpreadService.insert(groupSpreadInfo);
			} else if (1 == groupSpreadInfoList.size()) {
				groupSpreadService.update(groupSpreadInfo);
			} else {
				throw new Exception("同步静态组播出错!!!!");
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,SynchroUtil.class);
		} finally {
			UiUtil.closeService_MB(groupSpreadService);
		}

	}

	/**
	 * LAG与数据库同步
	 * 
	 * @author kk
	 * 
	 * @param
	 * 
	 * @return
	 * @throws Exception
	 * 
	 * @Exception 异常对象
	 */
	public void portLagSynchro(PortLagInfo portLagInfo) throws Exception {
		PortLagService_MB portLagService = null;
		List<PortLagInfo> portLagInfoList = null;
		try {

			portLagService = (PortLagService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORTLAG);
			// 根据LAGID和SITEID查询lag表
			portLagInfoList = portLagService.selectByConditionForSynchro(portLagInfo);

			switch (portLagInfoList.size()) {
			case 0:
				// 新增
				portLagService.insertLag(portLagInfo);
				break;
			case 1:
				// 修改
				updatePortLag(portLagInfoList.get(0), portLagInfo, portLagService);
				break;
			default:
				throw new Exception("同步lag时 查询lag出错");
			}

		} catch (Exception e) {
			throw e;
		} finally {
			UiUtil.closeService_MB(portLagService);
		}
	}

	/**
	 * 给要修改的LAG对象赋值，并且修改数据库
	 * 
	 * @author kk
	 * 
	 * @param
	 * 
	 * @return
	 * 
	 * @Exception 异常对象
	 */
	private void updatePortLag(PortLagInfo portLagInfo_db, PortLagInfo portLagInfo, PortLagService_MB portLagService) throws Exception {

		portLagInfo_db.setAdmin(portLagInfo.getAdmin());
		portLagInfo_db.setQosQueueList(portLagInfo.getQosQueueList());
		portLagInfo_db.setPortList(portLagInfo.getPortList());
		portLagInfo_db.setLagMode(portLagInfo.getLagMode());
		portLagInfo_db.setPortLimitation(portLagInfo.getPortLimitation());
		portLagInfo_db.setBroadcastFlux(portLagInfo.getBroadcastFlux());
		portLagInfo_db.setGroupBroadcastFlux(portLagInfo.getGroupBroadcastFlux());
		portLagInfo_db.setFloodFlux(portLagInfo.getFloodFlux());
		portLagInfo_db.setMaxFrameLength(portLagInfo.getMaxFrameLength());
		portLagInfo_db.setVlanIC(portLagInfo.getVlanIC());
		portLagInfo_db.setVlanPriority(portLagInfo.getVlanPriority());
		portLagInfo_db.setMsgLoop(portLagInfo.getMsgLoop());
		portLagInfo_db.setResumeModel(portLagInfo.getResumeModel());
		portLagInfo_db.setInportLimitation(portLagInfo.getInportLimitation());
		portLagInfo_db.setMeangerStatus(portLagInfo.getMeangerStatus());
		portLagInfo_db.setsMac(portLagInfo.getsMac());
		portLagInfo_db.setLblNetWrap(portLagInfo.getLblNetWrap());
		portLagInfo_db.setLblVlanTpId(portLagInfo.getLblVlanTpId());
		portLagInfo_db.setLblouterTpid(portLagInfo.getLblouterTpid());
		portLagInfo_db.setLblNetVlanMode(portLagInfo.getLblNetVlanMode());
		portLagInfo_db.setLagStatus(portLagInfo.getLagStatus());
		portLagService.updateLag(portLagInfo_db);

	}

	/**
	 * 武汉同步保护 将主要tunnel和备用tunnel的关系入库
	 * 
	 * @param lspProtectionList
	 * @param siteId
	 */
	public void protectSynchro(List<TunnelProtection> lspProtectionList, int siteId) {
		Tunnel mainTunnel = null;
		Tunnel standTunnel = null;
		TunnelService_MB tunnelService = null;
		try {
			tunnelService = (TunnelService_MB) ConstantUtil.serviceFactory.newService_MB(Services.Tunnel);
			for (TunnelProtection tunnelProtection : lspProtectionList) {
				mainTunnel = tunnelService.selectBySiteIdAndServiceId(siteId, tunnelProtection.getMainLspID());
				standTunnel = tunnelService.selectBySiteIdAndServiceId(siteId, tunnelProtection.getStandbyLspID());
				mainTunnel.setProtectTunnelId(standTunnel.getTunnelId());
				mainTunnel.setTunnelType(186 + "");
				standTunnel.setTunnelType(0 + "");
				standTunnel.setTunnelStatus(EActiveStatus.ACTIVITY.getValue());
				if (standTunnel.getaSiteId() == siteId) {
					standTunnel.setAprotectId(tunnelProtection.getLspLogicId());
				} else if (standTunnel.getzSiteId() == siteId) {
					standTunnel.setZprotectId(tunnelProtection.getLspLogicId());
				}
				mainTunnel.setWaittime(tunnelProtection.getWaittime());
				mainTunnel.setDelaytime(tunnelProtection.getProtractedTime());
				tunnelService.update(mainTunnel);
				tunnelService.update(standTunnel);
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,SynchroUtil.class);
		} finally {
			UiUtil.closeService_MB(tunnelService);
			standTunnel = null;
			mainTunnel = null;
		}

	}

	/**
	 * 同步pwNniBuffer信息
	 * 
	 * @param pwNniInfo
	 * @param siteId
	 * @param isVPLS true/false
	 * 			     vpws业务不需要mac地址学习和水平分割两个属性
	 */
	public void pwNniBufferInfoSynchro(PwNniInfo pwNniInfo, int siteId, boolean isVPLS) {
		PwNniInfoService_MB pwNniBufferService = null;
		PwNniInfo info = null;
		List<PwNniInfo> pwNniInfos = null;
		try {
			pwNniBufferService = (PwNniInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PwNniBuffer);
			info = new PwNniInfo();
			info.setSiteId(siteId);
			info.setPwId(pwNniInfo.getPwId());
			pwNniInfos = pwNniBufferService.select(info);
			if (pwNniInfos != null && pwNniInfos.size() > 0) {
				PwNniInfo pwNniInfo2 = pwNniInfos.get(0);
				pwNniInfo2.setPwId(pwNniInfo.getPwId());
				pwNniInfo2.setTagAction(pwNniInfo.getTagAction());
				pwNniInfo2.setExitRule(pwNniInfo.getExitRule());
				pwNniInfo2.setSvlan(pwNniInfo.getSvlan());
				pwNniInfo2.setVlanpri(pwNniInfo.getVlanpri());
				pwNniInfo2.setLanId(pwNniInfo.getLanId());
				if(isVPLS){
					pwNniInfo2.setMacAddressLearn(pwNniInfo.getMacAddressLearn());
					pwNniInfo2.setHorizontalDivision(pwNniInfo.getHorizontalDivision());
				}
				pwNniInfo2.setControlEnable(pwNniInfo.getControlEnable());
				pwNniBufferService.saveOrUpdate(pwNniInfo2);
			} else {
				pwNniBufferService.saveOrUpdate(pwNniInfo);
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,SynchroUtil.class);
		} finally {
			UiUtil.closeService_MB(pwNniBufferService);
			info = null;
			pwNniInfos = null;
		}
	}

	/**
	 * 环保护设备与数据库同步
	 * 
	 * @author kk
	 * 
	 * @param
	 * 
	 * @return
	 * @throws Exception
	 * 
	 * @Exception 异常对象
	 */
	public void loopProtectSynchro(LoopProtectInfo loopProtect, int siteId) throws Exception {
		if (null == loopProtect) {
			throw new Exception("loopProtect is null");
		}
		WrappingProtectService_MB service = null;
		List<LoopProtectInfo> loopProtect_db = null;
		LoopProtectInfo loopProtectInfo;
		try {
			loopProtect_db = new ArrayList<LoopProtectInfo>();
			loopProtectInfo = new LoopProtectInfo();
			service = (WrappingProtectService_MB) ConstantUtil.serviceFactory.newService_MB(Services.WRAPPINGPROTECT);
			loopProtectInfo.setSiteId(siteId);
			loopProtectInfo.setLoopId(loopProtect.getLoopId());
			loopProtect_db = service.select(loopProtectInfo);
			switch (loopProtect_db.size()) {
			case 0:
				loopProtect_db.add(loopProtect);
				service.insert(loopProtect_db);
				break;
			case 1:
				updateLoopProtect(loopProtect_db, loopProtect, service, siteId);
				break;
			}

		} catch (Exception e) {
			throw e;
		} finally {
			UiUtil.closeService_MB(service);
			loopProtect_db = null;
			loopProtectInfo = null;
		}
	}

	/**
	 * 
	 * 修改elandb
	 * 
	 * @author kk
	 * 
	 * @param
	 * 
	 * @return
	 * @throws Exception
	 * 
	 * @Exception 异常对象
	 */
	private void updateLoopProtect(List<LoopProtectInfo> loopProtectList_db, LoopProtectInfo loopProtect, WrappingProtectService_MB service, int siteId) throws Exception {

		if (null == loopProtectList_db) {
			throw new Exception("loopProtectList_db is null");
		}
		if (null == loopProtect) {
			throw new Exception("loopProtect is null");
		}
		if (null == service) {
			throw new Exception("WrappingProtectService is null");
		}
		LoopProtectInfo updateLoopProtect;
		List<LoopProtectInfo> loopProtectList;
		try {
			loopProtectList = new ArrayList<LoopProtectInfo>();
			updateLoopProtect = loopProtectList_db.get(0);
		//	updateLoopProtect.setName(loopProtect.getName() + "");
			updateLoopProtect.setLoopId(loopProtect.getLoopId());
			updateLoopProtect.setNodeId(loopProtect.getNodeId());
			updateLoopProtect.setWaittime(loopProtect.getWaittime());
			updateLoopProtect.setDelaytime(loopProtect.getDelaytime());
			updateLoopProtect.setWestLspId(loopProtect.getWestLspId());
			updateLoopProtect.setEastLspId(loopProtect.getEastLspId());
			updateLoopProtect.setWestPort(loopProtect.getWestPort());
			updateLoopProtect.setEastPort(loopProtect.getEastPort());
			updateLoopProtect.setApsenable(loopProtect.getApsenable());
			updateLoopProtect.setProtectType(loopProtect.getProtectType());
//			updateLoopProtect.setWestNodeId(loopProtect.getWestNodeId());
//			updateLoopProtect.setEastNodeId(loopProtect.getEastNodeId());
			//updateLoopProtect.setIsSingle(loopProtect.getIsSingle());
			updateLoopProtect.setActiveStatus(loopProtect.getActiveStatus());
			loopProtectList.add(updateLoopProtect);
			service.update(loopProtectList);
		} catch (Exception e) {
			throw e;
		}finally{
			updateLoopProtect = null;
			loopProtectList = null;
		}

	}

	/**
	 * qos与数据库层同步
	 * 
	 * @param qosInfoList
	 *            驱动层转换后的对象
	 * @throws Exception
	 */
	public void updateQos(List<QosInfo> qosInfoList) throws Exception {

		if (null == qosInfoList || qosInfoList.size() == 0) {
			throw new Exception("qosInfoList is null");
		}
		AcBufferService_MB bufferService = null;
		QosInfoService_MB qosInfoService = null;
		QosInfo qos = null;
		Acbuffer acBufferSel;
		List<QosInfo> qosInfoList_select = null;
		List<Acbuffer> acbufferList_select = new ArrayList<Acbuffer>();;
		try {
			//根据qos类型、名称、网元查询qos
			bufferService = (AcBufferService_MB) ConstantUtil.serviceFactory.newService_MB(Services.UniBuffer);
			qosInfoService = (QosInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.QosInfo);
			qos = new QosInfo();
			qos.setSiteId(qosInfoList.get(0).getSiteId());
			qos.setQosname(qosInfoList.get(0).getQosname());
			qos.setQosType(qosInfoList.get(0).getQosType());
			qosInfoList_select = qosInfoService.queryByCondition(qos);
			//如果没查询到。执行新建方法，否则执行修改方法
			if(qosInfoList_select.size() == 0){
				qosInfoService.save(qosInfoList);
			}else{
				qosInfoService.updateQos(qosInfoList);
			}
			
			if(qosInfoList.get(0).getBufferList().size()>0){
				for(Acbuffer acbuffer:qosInfoList.get(0).getBufferList()){
					acbufferList_select.clear();
					acBufferSel = new Acbuffer();
					acBufferSel.setQosType(acbuffer.getQosType());
					acBufferSel.setQosName(acbuffer.getQosName());
					acbufferList_select = bufferService.selectForCondition(acBufferSel);
					if(acbufferList_select.size() == 0){
						acbufferList_select.add(acbuffer);
					}else{
						acbufferList_select.add(updateAcBuffer(acbufferList_select.get(0),acbuffer,bufferService));
					}
					bufferService.saveOrUpdate(acbufferList_select);
				}
			}
		} catch (Exception e) {
			throw e;
		} finally{
			UiUtil.closeService_MB(bufferService);
			UiUtil.closeService_MB(qosInfoService);
		}
	}
	private Acbuffer updateAcBuffer(Acbuffer acbufferSel, Acbuffer acbuffer,AcBufferService_MB bufferService) {
		
		acbufferSel.setSeq(acbuffer.getSeq());
		acbufferSel.setPhb(acbuffer.getPhb());
		acbufferSel.setCir(acbuffer.getCir());
		acbufferSel.setCbs(acbuffer.getCbs());
		acbufferSel.setEir(acbuffer.getEir());
		acbufferSel.setEbs(acbuffer.getEbs());
		acbufferSel.setCm(acbuffer.getCm());
		acbufferSel.setPir(acbuffer.getPir());
		acbufferSel.setOperatorVlanIdValue(acbuffer.getOperatorVlanIdValue());
		acbufferSel.setOperatorVlanIdMask(acbuffer.getOperatorVlanIdMask());
		acbufferSel.setClientVlanIdValue(acbuffer.getClientVlanIdValue());
		acbufferSel.setClientVlanIdMask(acbuffer.getClientVlanIdMask());
		acbufferSel.setOperatorVlanPriorityValue(acbuffer.getOperatorVlanPriorityValue());
		acbufferSel.setOperatorVlanPriorityMask(acbuffer.getOperatorVlanPriorityMask());
		acbufferSel.setClientVlanPriorityValue(acbuffer.getClientVlanPriorityValue());
		acbufferSel.setClientVlanPriorityMask(acbuffer.getClientVlanPriorityMask());
		acbufferSel.setiPTypeValue(acbuffer.getiPTypeValue());
		acbufferSel.setiPTypeMask(acbuffer.getiPTypeMask());
		acbufferSel.setTargetIp(acbuffer.getTargetIp());
		acbufferSel.setSinkIpLabelMask(acbuffer.getSinkIpLabelMask());
		acbufferSel.setSourceIp(acbuffer.getSourceIp());
		acbufferSel.setSourceIpLabelMask(acbuffer.getSourceIpLabelMask());
		acbufferSel.setTargetMac(acbuffer.getTargetMac());
		acbufferSel.setSinkMacLabelMask(acbuffer.getSinkMacLabelMask());
		acbufferSel.setSourceMac(acbuffer.getSourceMac());
		acbufferSel.setSourceMacLabelMask(acbuffer.getSourceMacLabelMask());
		acbufferSel.setEthernetTypeValue(acbuffer.getEthernetTypeValue());
		acbufferSel.setEthernetTypeMask(acbuffer.getEthernetTypeMask());
		acbufferSel.setQosType(acbuffer.getQosType());
		acbufferSel.setAppendBufferName(acbuffer.getAppendBufferName());
		return acbufferSel;
	}

	/**
	 * OSPFINTERFACE 对象与数据库信息同步
	 * 
	 * @author sy
	 * 
	 * @param ospfInterface
	 *            数据库ospfInterface对象
	 * @param siteid
	 *            网元id
	 * 
	 * @return
	 * @throws Exception
	 * 
	 * @Exception 异常对象
	 */
	public void PtpSiteSynchro(TimeManageInfo timeManageInfo, int siteId) throws Exception {
		if (null == timeManageInfo ) {
			throw new Exception("timeManageInfo is null");
		}
		TimeManageInfo timeInfo =null;
		TimeManageInfoService_MB timeManageInfoService = null;
		try {
			timeManageInfoService = (TimeManageInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.TimeManageInfoService);
			timeInfo=new TimeManageInfo();
			timeInfo.setSiteId(siteId);
			
			timeInfo = timeManageInfoService.select(siteId);
			 if(timeInfo!=null){
				 updatePtpSite(timeInfo,timeManageInfo,timeManageInfoService,siteId);
			 }else{
				 timeManageInfoService.insertTimeManageInfo(timeManageInfo);
			 }

		} catch (Exception e) {
			throw e;
		} finally {
			UiUtil.closeService_MB(timeManageInfoService);
			timeManageInfo = null;
		}
	}
	/**
	 * 
	 * 修改timeManageInfo
	 * 
	 * @author sy
	 * 
	 * @param
	 * 
	 * @return
	 * @throws Exception
	 * 
	 * @Exception 异常对象
	 */
	private void updatePtpSite(TimeManageInfo timeManageInfo, TimeManageInfo TimeManageInfo,TimeManageInfoService_MB timeManageInfoService, int siteId) throws Exception {
		
		if (null == timeManageInfo) {
			throw new Exception("timeManageInfo is null");
		}
		try {
			timeManageInfo.setSiteId(TimeManageInfo.getSiteId());
			timeManageInfo.setModel(TimeManageInfo.getModel());
			timeManageInfo.setPriorOne(TimeManageInfo.getPriorOne());//优先级  1  ， 2
			timeManageInfo.setPriorTwo(TimeManageInfo.getPriorTwo());
			timeManageInfo.setClockType(TimeManageInfo.getClockType());//时钟类型
			timeManageInfo.setClockVariance(TimeManageInfo.getClockVariance());//时钟方差
			timeManageInfo.setClockPrecision(TimeManageInfo.getClockPrecision());//时钟精度
			//timeManageInfo.setManufacturerOUI(Integer.parseInt());//OUI厂商
			timeManageInfo.setClockRegion(TimeManageInfo.getClockRegion());
			timeManageInfo.setClockRegionOne(TimeManageInfo.getClockRegionOne());
			timeManageInfo.setClockRegionTwo(TimeManageInfo.getClockRegionTwo());
			timeManageInfo.setClockRegionThree(TimeManageInfo.getClockRegionThree());
			timeManageInfo.setClockRegionFour(TimeManageInfo.getClockRegionFour());
			timeManageInfo.setClockRegionTwoJbox(TimeManageInfo.getClockRegionTwoJbox());				
			timeManageInfo.setClockRegionThreeJbox(TimeManageInfo.getClockRegionThreeJbox());		
			timeManageInfo.setClockRegionFourJbox(TimeManageInfo.getClockRegionFourJbox());
			timeManageInfo.setFollowModel(TimeManageInfo.getFollowModel());
			timeManageInfo.setClockRegionDelay(TimeManageInfo.getClockRegionDelay());
			timeManageInfo.setTodsendTime(TimeManageInfo.getTodsendTime());
			
			//状态
			timeManageInfo.setTimeID(TimeManageInfo.getTimeID());
			timeManageInfo.setTimeType(TimeManageInfo.getTimeType());
			timeManageInfo.setFtimeID(TimeManageInfo.getFtimeID());
			timeManageInfo.setFtimePort(TimeManageInfo.getFtimePort());
			timeManageInfo.setLeapNumber(TimeManageInfo.getLeapNumber());
			timeManageInfo.setSystemVarianceValue(TimeManageInfo.getSystemVarianceValue());
			timeManageInfo.setTodState(TimeManageInfo.getTodState());
			timeManageInfo.setzTimeID(TimeManageInfo.getzTimeID());
			timeManageInfo.setzTimeTpye(TimeManageInfo.getzTimeTpye());
			timeManageInfo.setzTimePrecision(TimeManageInfo.getzTimePrecision());
			timeManageInfo.setzTimeVariance(TimeManageInfo.getzTimeVariance());
			timeManageInfo.setzTimePriorOne(TimeManageInfo.getzTimePrecision());
			timeManageInfo.setzTimePriorTwo(TimeManageInfo.getzTimePriorTwo());
			timeManageInfoService.update(timeManageInfo);
		} catch (Exception e) {
			throw e;
		}

	}

	public void externalClockSynchro(ExternalClockInterface externalClockInterface, int siteId) throws Exception {
		if (null == externalClockInterface ) {
			throw new Exception("externalClockInterface is null");
		}
		ExternalClockInterfaceService_MB externalClockInterfaceService = null;
		List<ExternalClockInterface> externalClockInterface_db = null;
		ExternalClockInterface ExternalClockInterfaceInfo = null;
		try {
			ExternalClockInterfaceInfo = new ExternalClockInterface();
			externalClockInterface_db = new ArrayList<ExternalClockInterface>();
			externalClockInterfaceService=(ExternalClockInterfaceService_MB)ConstantUtil.serviceFactory.newService_MB(Services.ExternalClockInterfaceService);
			ExternalClockInterfaceInfo.setSiteId(siteId);
			ExternalClockInterfaceInfo.setInterfaceName(externalClockInterface.getInterfaceName());
			externalClockInterface_db = externalClockInterfaceService.select(ExternalClockInterfaceInfo);
			switch (externalClockInterface_db.size()) {
			case 0:
				externalClockInterfaceService.insertTimeManageInfo(externalClockInterface);
				break;
			case 1:
				updatEexternalClockInterface(externalClockInterface_db,externalClockInterface, externalClockInterfaceService, siteId);
				break;
			}

		} catch (Exception e) {
			throw e;
		} finally {
			UiUtil.closeService_MB(externalClockInterfaceService);
			externalClockInterface_db = null;
			ExternalClockInterfaceInfo = null;
		}
		
	}

	private void updatEexternalClockInterface(
			List<ExternalClockInterface> externalClockInterface_db,
			ExternalClockInterface externalClockInterface,
			ExternalClockInterfaceService_MB externalClockInterfaceService, int siteId) throws Exception {
		if (null == externalClockInterface_db) {
			throw new Exception("externalClockInterface_db is null");
		}
		if (null == externalClockInterface) {
			throw new Exception("externalClockInterface is null");
		}
		if (null == externalClockInterfaceService) {
			throw new Exception("externalClockInterfaceService is null");
		}
		ExternalClockInterface updateExternalClockInterface;
		try {
			updateExternalClockInterface = externalClockInterface_db.get(0);
			updateExternalClockInterface.setName(externalClockInterface.getName());
			updateExternalClockInterface.setSiteId(externalClockInterface.getSiteId());
			updateExternalClockInterface.setInterfaceName(externalClockInterface.getInterfaceName());
			updateExternalClockInterface.setManagingStatus(externalClockInterface.getManagingStatus());
			updateExternalClockInterface.setWorkingStatus(externalClockInterface.getWorkingStatus());
			updateExternalClockInterface.setInterfaceMode(externalClockInterface.getInterfaceMode());
			updateExternalClockInterface.setInputImpedance(externalClockInterface.getInputImpedance());
			updateExternalClockInterface.setSanBits(externalClockInterface.getSanBits());
			updateExternalClockInterface.setTimeOffsetCompensation(externalClockInterface.getTimeOffsetCompensation());
			updateExternalClockInterface.setActiveStatus(externalClockInterface.getActiveStatus());
			externalClockInterfaceService.update(updateExternalClockInterface);
		} catch (Exception e) {
			throw e;
		}
		
	}

	public void clockSourceSynchro(ClockSource clockSource, int siteId) throws Exception {
		if (null == clockSource ) {
			throw new Exception("clockSource is null");
		}
		ClockSource clockSourceInfo = null;
		List<ClockSource> clockSource_db = null;
		FrequencyClockManageService_MB frequencyClockManageService = null;
		try {
			clockSourceInfo = new ClockSource();
			clockSource_db = new ArrayList<ClockSource>();
			frequencyClockManageService=(FrequencyClockManageService_MB)ConstantUtil.serviceFactory.newService_MB(Services.FrequencyClockManageService);
			clockSourceInfo.setSiteId(siteId);
			clockSourceInfo.setPort(clockSource.getPort());
			clockSource_db = frequencyClockManageService.select(clockSourceInfo);
			switch (clockSource_db.size()) {
			case 0:
				frequencyClockManageService.insert(clockSource);
				break;
			case 1:
				updateClockSource(clockSource_db,clockSource, frequencyClockManageService, siteId);
				break;
			}

		} catch (Exception e) {
			throw e;
		} finally {
			UiUtil.closeService_MB(frequencyClockManageService);
			clockSource = null;
			clockSource_db = null;
			clockSourceInfo = null;
		}
	}
		/**  同步
		 *接口配置 添加
		 */
		public void portSynchro(PortConfigInfo portSource, int siteId) throws Exception {
			if (null == portSource ) {
				throw new Exception("portSource is null");
			}
			List<PortConfigInfo> clockSource_db = null;
			PortConfigInfo ptpPort=null;
			PortDispositionInfoService_MB portDispositionInfoService=null;
			try {
				clockSource_db = new ArrayList<PortConfigInfo>();
				portDispositionInfoService=(PortDispositionInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PortDispositionInfoService);
				ptpPort =new PortConfigInfo();
				ptpPort.setSiteId(siteId);
				clockSource_db = portDispositionInfoService.select(siteId);
				switch (clockSource_db.size()) {
				case 0:
					portDispositionInfoService.insertPortDispositionInfo(portSource);
					break;
				case 1:
					updatePtpPort(clockSource_db,portSource,portDispositionInfoService,siteId);
					break;
				}
			} catch (Exception e) {
				throw e;
			} finally {
				UiUtil.closeService_MB(portDispositionInfoService);
				portSource = null;
				clockSource_db = null;
			}
		}
		
		/**
		 * 端口配置  同步（2）
		 * @param clockSource_db
		 * @param clockSource
		 * @param frequencyClockManageService
		 * @param siteId
		 * @throws Exception
		 */
		private void updatePtpPort(List<PortConfigInfo> clockSource_db,
				PortConfigInfo ptpPort,PortDispositionInfoService_MB portDispositionInfoService, int siteId) throws Exception {
			if (null == clockSource_db) {
				throw new Exception("clockSource_db is null");
			}
			if (null == ptpPort) {
				throw new Exception("ptpPort is null");
			}
			if (null == portDispositionInfoService) {
				throw new Exception("portDispositionInfoService is null");
			}
			PortConfigInfo portConfigInfo ;
			try {
				portConfigInfo = clockSource_db.get(0);
				portConfigInfo.setSiteId(ptpPort.getSiteId());
				portConfigInfo.setPort(ptpPort.getPort());
				//端口使能
				
				portConfigInfo.setDelayMechanism(ptpPort.getDelayMechanism());//延时机制 (e2e) e2e=1端到端 , p2p=2 点到点
				portConfigInfo.setVlanID(ptpPort.getVlanID());//vlan号 (1) ; int [0,4094]
				portConfigInfo.setPortStatus(ptpPort.getPortStatus());//端口状态(init); init=1初始化 ,fault=2错误状态
				portConfigInfo.setOperationMode(ptpPort.getOperationMode());//操作模式 (auto) ; auto=0自动选源 ,mast
				portConfigInfo.setAnncPacketsInterval(ptpPort.getAnncPacketsInterval());//annc时间间隔 (1) int [-4,4]
				portConfigInfo.setAnncTimeoutSetting(ptpPort.getAnncTimeoutSetting());//annc接收超时(4) int [1,65535]
				portConfigInfo.setSyncPacketsInterval(ptpPort.getSyncPacketsInterval());//Sync时间间隔 (0) int [-8,1]
				portConfigInfo.setDelay_ReqPacketsInterval(ptpPort.getDelay_ReqPacketsInterval());//DelayReq时间间隔(0) int [-4,4]
				portConfigInfo.setPdel_ReqPacketsInterval(ptpPort.getPdel_ReqPacketsInterval());//pdelreq时间间隔(0) int [-4,4]
				portConfigInfo.setLineDelayCompensation(ptpPort.getLineDelayCompensation());//测出来的延时补偿(0); int  [-10000000,10000000]
				portConfigInfo.setInterfaceType(ptpPort.getInterfaceType());//接口类型
				portConfigInfo.setTimeStampMode(ptpPort.getTimeStampMode());
				portDispositionInfoService.update(portConfigInfo);
			} catch (Exception e) {
				throw e;
			}
			
		}

	private void updateClockSource(List<ClockSource> clockSource_db,
			ClockSource clockSource,FrequencyClockManageService_MB frequencyClockManageService, int siteId) throws Exception {
		if (null == clockSource_db) {
			throw new Exception("clockSource_db is null");
		}
		if (null == clockSource) {
			throw new Exception("clockSource is null");
		}
		if (null == frequencyClockManageService) {
			throw new Exception("frequencyClockManageService is null");
		}
		ClockSource clockSourceInfoForUpdate ;
		try {
			clockSourceInfoForUpdate = clockSource_db.get(0);
			clockSourceInfoForUpdate.setSiteId(clockSource.getSiteId());
			clockSourceInfoForUpdate.setPort(clockSource.getPort());
			clockSourceInfoForUpdate.setSystemPriorLevel(clockSource.getSystemPriorLevel());
			clockSourceInfoForUpdate.setRecoverModel(clockSource.getRecoverModel());
			clockSourceInfoForUpdate.setDNUGroup(clockSource.getDNUGroup());
			clockSourceInfoForUpdate.setExportPriorLevel(clockSource.getExportPriorLevel());
			clockSourceInfoForUpdate.setReceiveSSMValue(clockSource.getReceiveSSMValue());
			clockSourceInfoForUpdate.setSSMSend(clockSource.getSSMSend());
			clockSourceInfoForUpdate.setJobState(clockSource.getJobState());
			clockSourceInfoForUpdate.setManageState(clockSource.getManageState());
			clockSourceInfoForUpdate.setActiveStatus(clockSource.getActiveStatus());
			clockSourceInfoForUpdate.setReceiveSSMRealityValue(clockSource.getReceiveSSMRealityValue());
			clockSourceInfoForUpdate.setPhysicsState(clockSource.getPhysicsState());
			clockSourceInfoForUpdate.setLogicState(clockSource.getLogicState());
			frequencyClockManageService.update(clockSourceInfoForUpdate);
		} catch (Exception e) {
			throw e;
		}
		
	}
	public void lineClockInterfaceSynchro(LineClockInterface clockSource, int siteId) throws Exception {
		if (null == clockSource ) {
			throw new Exception("clockSource is null");
		}
		List<LineClockInterface> clockSource_db = null;
		TimeLineClockInterfaceService_MB timeLineClockInterfaceService=null;
		try {
			clockSource_db = new ArrayList<LineClockInterface>();
			timeLineClockInterfaceService=(TimeLineClockInterfaceService_MB)ConstantUtil.serviceFactory.newService_MB(Services.TimeLineClockInterfaceService);
			LineClockInterface clockInterface= new LineClockInterface();
			//clockInterface.setSiteId(siteId);
			clockInterface.setPortName(clockSource.getPortName());
			clockSource_db = timeLineClockInterfaceService.select(clockInterface);
			if(clockSource_db.size()>0) {
				for(LineClockInterface lineClockInterface:clockSource_db){
					clockSource.setPort(lineClockInterface.getPort());
					
					updateLineClockSource(clockSource_db,clockSource,timeLineClockInterfaceService,siteId);
				}			
			}else{				
					timeLineClockInterfaceService.insert(clockSource);						
			}

		} catch (Exception e) {
			throw e;
		} finally {
			UiUtil.closeService_MB(timeLineClockInterfaceService);
			clockSource = null;
			clockSource_db = null;
		}
	}
	/**
	 * 线路时钟接口    -同步
	 * @param clockSource_db
	 * @param clockSource
	 * @param frequencyClockManageService
	 * @param siteId
	 * @throws Exception
	 */
	private void updateLineClockSource(List<LineClockInterface> clockSource_db,
			LineClockInterface clockSource,TimeLineClockInterfaceService_MB timeLineClockInterfaceService, int siteId) throws Exception {
		if (null == clockSource_db) {
			throw new Exception("clockSource_db is null");
		}
		if (null == clockSource) {
			throw new Exception("clockSource is null");
		}
		if (null == timeLineClockInterfaceService) {
			throw new Exception("timeLineClockInterfaceService is null");
		}
		LineClockInterface lineClockInterface ;
		try {
			lineClockInterface = clockSource_db.get(0);
			lineClockInterface.setId(clockSource.getId());
			lineClockInterface.setPortName(clockSource.getPortName());
			lineClockInterface.setSiteId(clockSource.getSiteId());
			lineClockInterface.setPort(clockSource.getPort());
			lineClockInterface.setSsmSendingEnabled(clockSource.getSsmSendingEnabled());
			lineClockInterface.setRate(clockSource.getRate());
			lineClockInterface.setDnuGroup(clockSource.getDnuGroup());
			timeLineClockInterfaceService.update(lineClockInterface);
		} catch (Exception e) {
			throw e;
		}
		
	}
	public void mspProtectSynchro(MspProtect mspProtect, int siteId) throws Exception {
		if (null == mspProtect ) {
			throw new Exception("mspProtect is null");
		}
		List<MspProtect> mspProtect_db = null;
		MspProtectService_MB mspProtectService=null;
		MspProtect mspProtectSel = null;
		try {
			mspProtect_db = new ArrayList<MspProtect>();
			mspProtectService = (MspProtectService_MB) ConstantUtil.serviceFactory.newService_MB(Services.MSPPROTECT);
			mspProtectSel = new MspProtect();
			mspProtectSel.setSiteId(mspProtect.getSiteId());
			mspProtectSel.setBusinessId(mspProtect.getBusinessId());
			mspProtect_db = mspProtectService.select(mspProtectSel);
			if(mspProtect_db.size()>0) {
				mspProtect.setId(mspProtect_db.get(0).getId());
				mspProtectService.saveOrUpdate(mspProtect);
			}else{				
				mspProtectService.saveOrUpdate(mspProtect);						
			}
		} catch (Exception e) {
			throw e;
		} finally {
			UiUtil.closeService_MB(mspProtectService);
			mspProtectSel = null;
			mspProtect_db = null;
		}
	}

	public void oamLinkInfoSynchro(OamInfo oamInfo, int siteId) throws Exception {
		OamInfoService_MB oamInfoService=null;
		OamLinkInfo oamLinkInfoSel = null;
		OamInfo oamInfoSel;
		try {
			oamInfoService = (OamInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.OamInfo);
			oamLinkInfoSel = new OamLinkInfo();
			oamInfoSel = new OamInfo();
			oamLinkInfoSel.setSiteId(siteId);
			oamLinkInfoSel.setObjId(oamInfo.getOamLinkInfo().getObjId());
			oamLinkInfoSel.setObjType(OamTypeEnum.LINKOAM.toString());
			oamInfoSel.setOamLinkInfo(oamLinkInfoSel);
			oamInfoSel = oamInfoService.queryByCondition(oamInfoSel,  OamTypeEnum.LINKOAM);
			if(null!=oamInfoSel&&null!=oamInfoSel.getOamLinkInfo()){
				if(oamInfoSel.getOamLinkInfo().getId()>0) {
					oamInfo.getOamLinkInfo().setId(oamInfoSel.getOamLinkInfo().getId());
					oamInfoService.saveOrUpdate(oamInfo);
				}
			}else{
				oamInfoService.saveOrUpdate(oamInfo);	
			}
			
		} catch (Exception e) {
			throw e;
		} finally {
			UiUtil.closeService_MB(oamInfoService);
			oamLinkInfoSel = null;
			oamInfoSel = null;
		}
	}

	public void dualProtectSynchro(DualProtect dualProtect, int siteId) throws Exception {
		if (null == dualProtect ) {
			throw new Exception("dualProtect is null");
		}
		List<DualProtect> dualProtect_db = null;
		DualProtectService_MB dualProtectService=null;
		DualProtect dualProtectSel = null;
		try {
			dualProtect_db = new ArrayList<DualProtect>();
			dualProtectService = (DualProtectService_MB) ConstantUtil.serviceFactory.newService_MB(Services.DUALPROTECTSERVICE);
			dualProtectSel = new DualProtect();
			dualProtectSel.setSiteId(dualProtect.getSiteId());
			dualProtectSel.setBusinessId(dualProtect.getBusinessId());
			dualProtect_db = dualProtectService.queryByCondition(dualProtectSel);
			if(dualProtect_db.size()>0) {
				dualProtect.setId(dualProtect_db.get(0).getId());
				dualProtectService.saveOrUpdate(dualProtect);
			}else{				
				dualProtectService.saveOrUpdate(dualProtect);						
			}
		} catch (Exception e) {
			throw e;
		} finally {
			UiUtil.closeService_MB(dualProtectService);
		}
	}
	
	/**
	 * v35port与数据库比较
	 * @param v35PortInfo
	 */
	public void v35PortSynchro(V35PortInfo v35PortInfo){
		V35PortService_MB v35PortService = null;
		List<V35PortInfo> v35PortInfos = null;
		try {
			v35PortService = (V35PortService_MB) ConstantUtil.serviceFactory.newService_MB(Services.V35PORT);
			v35PortInfos = v35PortService.selectByCondition(v35PortInfo);
			if(v35PortInfos != null && v35PortInfos.size()>0){
				int id = v35PortInfos.get(0).getId();
				CoderUtils.copy(v35PortInfo, v35PortInfos.get(0));
				v35PortInfos.get(0).setId(id);
				v35PortService.saveOrUpdate(v35PortInfos.get(0));
			}else{
				v35PortService.saveOrUpdate(v35PortInfo);
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, SynchroUtil.class);
		} finally {
			UiUtil.closeService_MB(v35PortService);
		}
	}

	public void updateSmartFan(SmartFanService_MB service,List<SmartFan> fanList) throws Exception {
		try {
			if(fanList.size() > 0){
				List<SmartFan> fanListBefore = service.selectAll(fanList.get(0).getSiteId());
				if(fanListBefore.size() == 0){
					//说明数据库没有数据,直接插入
					for (SmartFan fan : fanList) {
						service.save(fan);
					}
				}else{
					for (int i = 0; i < fanListBefore.size(); i++) {
						SmartFan fanBefore = fanListBefore.get(i);
						int id = fanBefore.getId();
						CoderUtils.copy(fanList.get(i), fanBefore);
						fanBefore.setId(id);
						service.update(fanBefore);
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 
	 * @param fanList
	 * @throws Exception
	 */
	public void updateEthService(EthService_MB service,List<EthServiceInfo> ethServiceList) throws Exception {
		try {		
			if(ethServiceList!= null && ethServiceList.size()>0){
				//一切以设备上的数据为主，先将原来的数据删除完
				service.deleteBySiteId(ethServiceList.get(0).getSiteId());
				//说明数据库没有数据,直接插入
//				for (EthServiceInfo ethServiceInfo : ethServiceList) {
					service.save(ethServiceList);
//				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, SynchroUtil.class);
		}
	}
	
	public void pmlimiteSynchro(PmValueLimiteInfo pmValueLimiteInfo)
	{
		PmLimiteService_MB pmLimiteService = null;
		PmValueLimiteInfo pminfo = null;
		try {
			pmLimiteService = (PmLimiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PmLimiteService);
			// 根据条件查询数据库
			pminfo = pmLimiteService.select(pmValueLimiteInfo);
			if (null == pminfo) 
			{
				// 没有此条件对应的记录,做插入操作
				pmLimiteService.insert(pmValueLimiteInfo);
			} else if(null != pminfo) 
			{
				// 有此条件对应的记录,做更新操作
				pmLimiteService.update(pmValueLimiteInfo);
			} else {
				throw new Exception("同步性能门限出错!!!!!");
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,SynchroUtil.class);
		} finally {
			UiUtil.closeService_MB(pmLimiteService);
		}
	}
	
	public void aclConfigSynchro(List<AclInfo> aclInfoList)
	{
		if(aclInfoList.size() == 0)
		{
			return;
		}
		AclService_MB aclService = null;
		try {
//			aclService = (AclService) ConstantUtil.serviceFactory.newService(Services.ACLSERCVICE);
//			List<AclInfo>  acl_before = aclService.select(aclInfoList.get(0).getSiteId());
//			if(acl_before == null || acl_before.size()==0)
//			{
//				aclService.batchSave(aclInfoList);
//			}
//			else
//			{
//				for (int i = 0; i < acl_before.size(); i++) 
//				{
//					AclInfo aclBefore = acl_before.get(i);
//					int id = aclBefore.getId();
//					CoderUtils.copy(aclInfoList.get(i), aclBefore);
//					aclBefore.setId(id);
//					aclService.update(aclBefore);
//				}
//			}
			aclService = (AclService_MB) ConstantUtil.serviceFactory.newService_MB(Services.ACLSERCVICE);
			aclService.batchSave(aclInfoList);
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			UiUtil.closeService_MB(aclService);
		}
	}
	
	
	/**
	 * 二层静态MAC对象与数据库同步
	 * 
	 * @author taopan
	 * @param ssMacStudy
	 * @throws Exception
	 */
	public void secondMacSynchro(SsMacStudy ssMacStudy) throws Exception {
		if(ssMacStudy == null){
			return;
		}
		SecondMacStudyService_MB secondMacStudyService = null;
		try {
			secondMacStudyService = (SecondMacStudyService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SECONDMACSTUDY);			
			//将number转为portId存入数据库		
			int portId = secondMacStudyService.selectPortIdBySiteId(ssMacStudy.getSiteId(),ssMacStudy.getPortId());
			ssMacStudy.setPortId(portId);
			secondMacStudyService.save(ssMacStudy);
		} catch (Exception e) {
			ExceptionManage.dispose(e,SynchroUtil.class);
		} finally {
			UiUtil.closeService_MB(secondMacStudyService);
		}
	}

	
	/**
	 *环网保护  对象与数据库信息同步
	 * 
	 * 
	 * @param 环网
	 *            数据库环网对象
	 * @param siteid
	 *            网元id
	 * 
	 * @return
	 * @throws Exception
	 * 
	 * @Exception 异常对象
	 */
	public void loopsProtectSynchro(LoopProtectInfo loopProtect, int siteId) throws Exception {
		if (null == loopProtect) {
			return;
		}
		WrappingProtectService_MB service = null;
		List<LoopProtectInfo> loopProtect_db = null;
		LoopProtectInfo loopProtectInfo;
		try {
			loopProtect_db = new ArrayList<LoopProtectInfo>();
			loopProtectInfo = new LoopProtectInfo();
			service = (WrappingProtectService_MB) ConstantUtil.serviceFactory.newService_MB(Services.WRAPPINGPROTECT);
			loopProtectInfo.setSiteId(siteId);
			loopProtectInfo.setLoopId(loopProtect.getLoopId());
			loopProtect_db = service.select(loopProtectInfo);
			switch (loopProtect_db.size()) {
			case 0:
				loopProtect.setSiteId(siteId);
				loopProtect_db.add(loopProtect);
				service.insert(loopProtect_db);
				break;
			case 1:
				updateLoopProtect(loopProtect_db, loopProtect, service, siteId);
				break;
			}

		} catch (Exception e) {
			throw e;
		} finally {
			UiUtil.closeService_MB(service);
			loopProtect_db = null;
			loopProtectInfo = null;
		}
	}
	
	/**
	 * 时间同步
	 * 
	 * @author taopan
	 * @param ssMacStudy
	 * @throws Exception
	 */
	public void timeSyncInfoSynchro(TimeSyncInfo timeSyncInfo) throws Exception {
		if(timeSyncInfo == null ){
			return;
		}
		TimeSyncService_MB timeSyncService = null;
		try {		
			timeSyncService = (TimeSyncService_MB) ConstantUtil.serviceFactory.newService_MB(Services.TIME_SYNC_MANGE);
			timeSyncService.save(timeSyncInfo);		
		} catch (Exception e) {
			ExceptionManage.dispose(e,SynchroUtil.class);
		} finally {
		    UiUtil.closeService_MB(timeSyncService);
		}
	}
	
	/**
	 * l2cp对象与数据库同步
	 * 
	 * @author guoqc
	 * @param allConfigInfo
	 * @throws Exception
	 */
	public void l2cpSynchro(L2cpInfo l2cpInfo) throws Exception {	
		L2CPService_MB l2cpService = null;
		try {
			l2cpService = (L2CPService_MB) ConstantUtil.serviceFactory.newService_MB(Services.L2CPSERVICE);				
			// 做插入操作
			l2cpService.save(l2cpInfo);						
		} catch (Exception e) {
			ExceptionManage.dispose(e,SynchroUtil.class);
		} finally {
			UiUtil.closeService_MB(l2cpService);
		}
	}
	
	
	
	/**
	 * MAC黑名单
	 * 
	 * @author taopan
	 * @param MacManagementInfo
	 * @throws Exception
	 */
	public void macManageSynchro(MacManagementInfo macManagementInfo) throws Exception {
		if(macManagementInfo == null){
			return;
		}
		SecondMacStudyService_MB secondMacStudyService = null;
		MacManageService_MB macManageService = null;
		try {
			secondMacStudyService = (SecondMacStudyService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SECONDMACSTUDY);			
			macManageService = (MacManageService_MB) ConstantUtil.serviceFactory.newService_MB(Services.MACMANAGE);			
			//将number转为portId存入数据库		
			int portId = secondMacStudyService.selectPortIdBySiteId(macManagementInfo.getSiteId(),macManagementInfo.getPortId());
			macManagementInfo.setPortId(portId);
			macManageService.save(macManagementInfo);
		} catch (Exception e) {
			ExceptionManage.dispose(e,SynchroUtil.class);
		} finally {
			UiUtil.closeService_MB(secondMacStudyService);
			UiUtil.closeService_MB(macManageService);
		}
	}


	/**
	 * Mac黑白地址同步
	 * 
	 * @author taopan
	 * @param ssMacStudy
	 * @throws Exception
	 */
	public void blackwhiteSynchro(BlackAndwhiteMacInfo blackAndwhiteMacInfo) throws Exception {
		if(blackAndwhiteMacInfo == null){
			return;
		}
		BlackWhiteMacService_MB blackWhiteMacService = null;
		try {
			blackWhiteMacService = (BlackWhiteMacService_MB) ConstantUtil.serviceFactory.newService_MB(Services.BLACKWHITEMAC);			
			//查出elan的名称	
			String elanName = blackWhiteMacService.selectByVsAndSiteId(blackAndwhiteMacInfo.getElanBussinessId(),blackAndwhiteMacInfo.getSiteId());			
			blackAndwhiteMacInfo.setVplsServiceName(elanName);
			blackWhiteMacService.save(blackAndwhiteMacInfo);
		} catch (Exception e) {
			ExceptionManage.dispose(e,SynchroUtil.class);
		} finally {
			UiUtil.closeService_MB(blackWhiteMacService);
		}
	}

	/**
	 * 
	 * ccc与数据库同步
	 * 
	 * @author kk
	 * 
	 * @param
	 * 
	 * @return
	 * @throws Exception
	 * 
	 * @Exception 异常对象
	 */
	public void cccSynchro(CccService_MB cccService,CccInfo cccInfo, int siteId) throws Exception {

		List<CccInfo> cccInfoList = null;
		try {

			cccInfoList = cccService.select_synchro(siteId, cccInfo.getaXcId());
			
			if (null == cccInfoList) {
				throw new Exception("同步ccc时 查询ccc出错");
			}

			switch (cccInfoList.size()) {
			case 0:
				// 插入ccc
				cccInfo.setCreateTime(DateUtil.getDate(DateUtil.FULLTIME));
				cccService.insert(cccInfo);
				break;
			case 1:
				// 修改ccc
				updateCcc(cccInfoList, cccInfo, cccService);
				break;

			default:
				throw new Exception("同步ccc时 查询ccc出错");
			}

		} catch (Exception e) {
			throw e;
		} finally {
			
		}
	}
	
	
	
	
	/**
	 * 修改eline数据库
	 * 
	 * @author kk
	 * 
	 * @param elineInfoList
	 *            数据库查询出来的eline对象
	 * @param elineInfo
	 *            设备上的eline对象
	 * @return
	 * @throws Exception
	 * 
	 * @Exception 异常对象
	 */
	private void updateCcc(List<CccInfo> cccInfoList, CccInfo cccInfo, CccService_MB cccService) throws Exception {

		if (null == cccInfoList || cccInfoList.size() != 1) {
			throw new Exception("cccInfoList is null");
		}
		if (null == cccInfo) {
			throw new Exception("cccInfo is null");
		}
		if (null == cccService) {
			throw new Exception("cccService is null");
		}

		CccInfo cccInfo_db = null;
		try {
			cccInfo_db = cccInfoList.get(0);
			cccInfo_db.setActiveStatus(EActiveStatus.ACTIVITY.getValue());
			cccInfo_db.setaXcId(cccInfo.getaXcId());
			cccInfo_db.setAmostAcId(cccInfo.getAmostAcId());
			cccInfo_db.setAction(1);			
			cccService.update(cccInfo_db);
		} catch (Exception e) {
			throw e;
		} finally {
			cccInfo_db = null;
		}
	}
	/**
	 * bfd对象与数据库同步
	 * 
	 * @author taopan
	 * @param ssMacStudy
	 * @throws Exception
	 */
	public void bfdSynchro(BfdInfo bfdInfo) throws Exception {
		if(bfdInfo == null){
			return;
		}
		BfdInfoService_MB bfdService = null;
		try {
			bfdService = (BfdInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.BFDMANAGEMENT);				
			bfdService.insert(bfdInfo);
		} catch (Exception e) {
			ExceptionManage.dispose(e,SynchroUtil.class);
		} finally {
			UiUtil.closeService_MB(bfdService);
		}
	}

	public void arpSynchro(ARPInfo arpInfo_NE, ARPInfoService_MB service) {
		try {
			ARPInfo arpInfo = service.select_synchro(arpInfo_NE.getSiteId(), arpInfo_NE.getPwProtectId());
			if (null == arpInfo) {
				// 插入
				service.insert(arpInfo_NE);
			}else{
				// 修改
				arpInfo_NE.setId(arpInfo.getId());
				arpInfo_NE.setName(arpInfo.getName());
				service.update(arpInfo_NE);
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,SynchroUtil.class);
		}
	}
	
	public void ospfSynchro(List<OSPFinfo_wh> ospFinfoWhs,int siteId){
		Ospf_whService_MB ospfWhServiceMB = null;
		try {
			ospfWhServiceMB = (Ospf_whService_MB) ConstantUtil.serviceFactory.newService_MB(Services.OSPF_WH);
			ospfWhServiceMB.insert(ospFinfoWhs);
		} catch (Exception e) {
			ExceptionManage.dispose(e,SynchroUtil.class);
		}finally{
			UiUtil.closeService_MB(ospfWhServiceMB);
		}
	}
}
