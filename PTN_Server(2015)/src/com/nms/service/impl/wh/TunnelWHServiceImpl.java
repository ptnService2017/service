﻿package com.nms.service.impl.wh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.nms.db.bean.equipment.port.PortInst;
import com.nms.db.bean.ptn.oam.OamInfo;
import com.nms.db.bean.ptn.oam.OamMepInfo;
import com.nms.db.bean.ptn.oam.OamMipInfo;
import com.nms.db.bean.ptn.path.tunnel.Lsp;
import com.nms.db.bean.ptn.path.tunnel.Tunnel;
import com.nms.db.bean.ptn.qos.QosInfo;
import com.nms.db.enums.EActionType;
import com.nms.db.enums.EActiveStatus;
import com.nms.db.enums.EManufacturer;
import com.nms.db.enums.EQosDirection;
import com.nms.db.enums.OamTypeEnum;
import com.nms.db.enums.QosTemplateTypeEnum;
import com.nms.drive.service.PtnServiceEnum;
import com.nms.drive.service.bean.LSPObject;
import com.nms.drive.service.bean.NEObject;
import com.nms.drive.service.bean.TunnelObject;
import com.nms.drive.service.bean.TunnelProtection;
import com.nms.model.equipment.port.PortService_MB;
import com.nms.model.ptn.path.tunnel.TunnelService_MB;
import com.nms.model.util.Services;
import com.nms.service.OperationServiceI;
import com.nms.service.bean.ActionObject;
import com.nms.service.bean.OperationObject;
import com.nms.service.impl.base.WHOperationBase;
import com.nms.service.impl.util.ResultString;
import com.nms.service.impl.util.SiteUtil;
import com.nms.service.impl.util.SynchroUtil;
import com.nms.service.impl.util.WhImplUtil;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.ResourceUtil;
import com.nms.ui.manager.SiteLockTypeUtil;
import com.nms.ui.manager.UiUtil;
import com.nms.ui.manager.keys.StringKeysTip;

public class TunnelWHServiceImpl extends WHOperationBase implements OperationServiceI {

	@SuppressWarnings("unchecked")
	@Override
	public String excutionDelete(List objectList) throws Exception {
		List<Tunnel> tunnelList = null;
		List<Integer> siteIdList = null;
		OperationObject operationObjectAfter = null;
		OperationObject operationObjectProtectAfter = null;
		OperationObject operationObjectResult = null;
		NEObject neObject = null;
		List<TunnelObject> tunnelObjectList = null;
		List<TunnelProtection> tunnelProtectionList = null;
		try {
			tunnelList = objectList;
			siteIdList = this.getSiteIds(tunnelList);
			if (siteIdList != null && siteIdList.size() > 0) {
				if (super.isLockBySiteIdList(siteIdList)) {
					return ResourceUtil.srcStr(StringKeysTip.TIP_SITE_LOCK);
				}
				super.lockSite(siteIdList, SiteLockTypeUtil.TUNNEL_DELETE);// 锁住网元
				WhImplUtil whImplUtil = new WhImplUtil();
				operationObjectAfter = new OperationObject();
				operationObjectProtectAfter = new OperationObject();
				SiteUtil siteUtil = new SiteUtil();
				for (Integer siteId : siteIdList) {
					if ("0".equals(siteUtil.SiteTypeUtil(siteId) + "")) {
						neObject = whImplUtil.siteIdToNeObject(siteId);
						tunnelProtectionList = new ArrayList<TunnelProtection>();
						tunnelObjectList = this.getTunnelObject(siteId, tunnelProtectionList, tunnelList.get(0).getTunnelType(), EActionType.DELETE);
						ActionObject actionObj = this.getActionObject(tunnelObjectList, neObject);
						operationObjectAfter.getActionObjectList().add(actionObj);
						
						ActionObject actionProtectObj = this.getActionProtectObject(tunnelProtectionList, neObject);
						operationObjectProtectAfter.getActionObjectList().add(actionProtectObj);
					}
				}
				super.sendAction(operationObjectAfter);
				operationObjectResult = super.verification(operationObjectAfter);// 获取设备返回信息
				if (!(operationObjectResult.isSuccess())) {// 失败
					return super.getErrorMessage(operationObjectResult);// 设备返回信息有需要也可以扩展为key=siteid，value=返回信息,的map结构
				}
				super.sendAction(operationObjectProtectAfter);// 下发1:1保护配置
				operationObjectResult = super.verification(operationObjectProtectAfter);// 获取设备返回信息
				if (!(operationObjectResult.isSuccess())) {// 失败
					return super.getErrorMessage(operationObjectResult);
				}
			}
			return ResultString.CONFIG_SUCCESS;
		} catch (Exception e) {
			throw e;
		} finally {
			super.clearLock(siteIdList);
		}
	}

	private ActionObject getActionProtectObject(List<TunnelProtection> tunnelProtectionList, NEObject neObject) {
		ActionObject actionObject = null;
		try {
			actionObject = new ActionObject();
			List<ActionObject> actionIdList = new ArrayList<ActionObject>();
			actionIdList.add(actionObject);
			actionObject.setActionId(super.getActionId(actionIdList));
			actionObject.setNeObject(neObject);
			actionObject.setType(PtnServiceEnum.LSP_PROTECT.toString());
			actionObject.setLSPProtectionList(tunnelProtectionList);
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return actionObject;
	}

	private ActionObject getActionObject(List<TunnelObject> tunnelObjectList, NEObject neObject) {
		ActionObject actionObject = null;
		try {
			actionObject = new ActionObject();
			List<ActionObject> actionIdList = new ArrayList<ActionObject>();
			actionIdList.add(actionObject);
			actionObject.setActionId(super.getActionId(actionIdList));
			actionObject.setNeObject(neObject);
			actionObject.setType(PtnServiceEnum.TUNNEL.toString());
			actionObject.setTunnelObjectList(tunnelObjectList);
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return actionObject;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String excutionInsert(Object object) throws Exception {
		List<Tunnel> tunnelList = null;
		List<Integer> siteIdList = null;
		OperationObject operationObjectAfter = null;
		OperationObject operationObjectProtectAfter = null;
		OperationObject operationObjectResult = null;
		NEObject neObject = null;
		List<TunnelObject> tunnelObjectList = null;
		List<TunnelProtection> tunnelProtectionList = null;
		try {
			tunnelList = (List<Tunnel>) object;
			/** 获取此tunnel的所有网元ID */
			siteIdList = this.getSiteIds(tunnelList);
			if (siteIdList != null && siteIdList.size() > 0) {
				/** 锁住此tunnel下的所有网元 */
				super.lockSite(siteIdList, SiteLockTypeUtil.TUNNEL_INSERT);
				operationObjectAfter = new OperationObject();
				operationObjectProtectAfter = new OperationObject();
				WhImplUtil whImplUtil = new WhImplUtil();
				SiteUtil siteUtil = new SiteUtil();
				for (Integer siteId : siteIdList) {
					if ("0".equals(siteUtil.SiteTypeUtil(siteId) + "")) {
						neObject = whImplUtil.siteIdToNeObject(siteId);
						tunnelProtectionList = new ArrayList<TunnelProtection>();
						tunnelObjectList = this.getTunnelObject(siteId, tunnelProtectionList, tunnelList.get(0).getTunnelType(), EActionType.INSERT);
						ActionObject actionObj = this.getActionObject(tunnelObjectList, neObject);
						operationObjectAfter.getActionObjectList().add(actionObj);
						
						ActionObject actionProtectObj = this.getActionProtectObject(tunnelProtectionList, neObject);
						operationObjectProtectAfter.getActionObjectList().add(actionProtectObj);
					}
				}
				super.sendAction(operationObjectAfter);
				operationObjectResult = super.verification(operationObjectAfter);// 获取设备返回信息
				if (!(operationObjectResult.isSuccess())) {// 失败
					return super.getErrorMessage(operationObjectResult);// 设备返回信息有需要也可以扩展为key=siteid，value=返回信息,的map结构
				}
				if ("186".equals(tunnelList.get(0).getTunnelType())) {
					super.sendAction(operationObjectProtectAfter);// 下发1:1保护配置
					operationObjectResult = super.verification(operationObjectProtectAfter);// 获取设备返回信息
					if (!(operationObjectResult.isSuccess())) {// 失败
						return super.getErrorMessage(operationObjectResult);
					}
				}
			}
			return ResultString.CONFIG_SUCCESS;
		} catch (Exception e) {
			throw e;
		} finally {
			super.clearLock(siteIdList);
		}
	}

	@Override
	public String excutionUpdate(Object object) throws Exception {
		Tunnel tunnel = null;
		List<Tunnel> tunnelList = new ArrayList<Tunnel>();
		List<Integer> siteIdList = null;
		OperationObject operationObjectAfter = null;
		OperationObject operationObjectProtectAfter = null;
		OperationObject operationObjectResult = null;
		NEObject neObject = null;
		List<TunnelObject> tunnelObjectList = null;
		List<TunnelProtection> tunnelProtectionList = null;
		try {
			tunnel = (Tunnel) object;
			tunnelList.add(tunnel);
			/** 获取此tunnel的所有网元ID */
			siteIdList = this.getSiteIds(tunnelList);
			if (siteIdList.size() > 0) {
				/** 锁住此tunnel下的所有网元 */
				super.lockSite(siteIdList, SiteLockTypeUtil.TUNNEL_UPDATE);
				WhImplUtil whImplUtil = new WhImplUtil();
				SiteUtil siteUtil = new SiteUtil();
				operationObjectAfter = new OperationObject();
				operationObjectProtectAfter = new OperationObject();
				for (Integer siteId : siteIdList) {
					if ("0".equals(siteUtil.SiteTypeUtil(siteId) + "")) {
						neObject = whImplUtil.siteIdToNeObject(siteId);
						tunnelProtectionList = new ArrayList<TunnelProtection>();
						tunnelObjectList = getTunnelObject(siteId, tunnelProtectionList, tunnelList.get(0).getTunnelType(), EActionType.UPDATE);
						ActionObject actionObj = this.getActionObject(tunnelObjectList, neObject);
						operationObjectAfter.getActionObjectList().add(actionObj);
						ActionObject actionProtectObj = this.getActionProtectObject(tunnelProtectionList, neObject);
						operationObjectProtectAfter.getActionObjectList().add(actionProtectObj);
					}
				}
				super.sendAction(operationObjectAfter);
				operationObjectResult = super.verification(operationObjectAfter);// 获取设备返回信息
				if (!(operationObjectResult.isSuccess())) {// 失败
					return super.getErrorMessage(operationObjectResult);// 设备返回信息有需要也可以扩展为key=siteid，value=返回信息,的map结构
				}
				if ("186".equals(tunnelList.get(0).getTunnelType())) {
					super.sendAction(operationObjectProtectAfter);// 下发1:1保护配置
					operationObjectResult = super.verification(operationObjectProtectAfter);// 获取设备返回信息
					if (!(operationObjectResult.isSuccess())) {// 失败
						return super.getErrorMessage(operationObjectResult);
					}
				}
				return ResultString.CONFIG_SUCCESS;
			} else {
				return ResultString.CONFIG_SUCCESS;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			super.clearLock(siteIdList);
		}
	}

	/**
	 * 一致性检测
	 * 
	 * @param siteId
	 * @return
	 */
	public List<Tunnel> consistence(int siteId) {
		OperationObject operationObject = null;
		OperationObject operationObjectResult = null;
		List<Tunnel> tunnels = new ArrayList<Tunnel>();
		HashMap<Integer, Tunnel> tunnelsHashMap = new HashMap<Integer, Tunnel>();
		OperationObject operationObjectProtect = null;
		OperationObject operationObjectResultProtect = null;
		List<TunnelProtection> tunnelProtections = new ArrayList<TunnelProtection>();
		try {
			operationObject = this.getSynchroOperationObject(siteId, "tunnelSynchro");// 封装下发数据
			super.sendAction(operationObject);
			operationObjectResult = super.verification(operationObject);
			if (operationObjectResult.isSuccess()) {// 返回成功
				for (ActionObject actionObject : operationObjectResult.getActionObjectList()) {
					tunnelsHashMap = this.getTunnelInfoMap(actionObject.getTunnelObjectList(), siteId);
				}

			}

			operationObjectProtect = this.getSynchroOperationObject(siteId, "tunnelProtectSynchro");// 封装下发数据
			super.sendAction(operationObjectProtect);
			operationObjectResultProtect = super.verification(operationObjectProtect);
			if (operationObjectResultProtect.isSuccess()) {// 返回成功
				tunnelProtections = operationObjectResultProtect.getActionObjectList().get(0).getLSPProtectionList();
			}

			for (TunnelProtection tunnelProtection : tunnelProtections) {
				Tunnel mainTunnel = tunnelsHashMap.get(tunnelProtection.getMainLspID());
				Tunnel standTunnel = tunnelsHashMap.get(tunnelProtection.getStandbyLspID());
				mainTunnel.setProtectTunnel(standTunnel);
				mainTunnel.setDelaytime(tunnelProtection.getProtractedTime());
				mainTunnel.setWaittime(tunnelProtection.getWaittime());
				mainTunnel.setProtectBack(tunnelProtection.getReturnType());
				if (standTunnel.getaSiteId() == siteId) {
					standTunnel.setAprotectId(tunnelProtection.getLspLogicId());
				} else if (standTunnel.getzSiteId() == siteId) {
					standTunnel.setZprotectId(tunnelProtection.getLspLogicId());
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		for (Integer integer : tunnelsHashMap.keySet()) {
			tunnels.add(tunnelsHashMap.get(integer));
		}
		return tunnels;
	}

	/**
	 * 下发设备流程 根据网元id查询网元下的tunnel
	 * 
	 * @param siteId
	 *            网元id
	 * @param actionType 
	 * @param string 
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */

	@SuppressWarnings("static-access")
	private List<TunnelObject> getTunnelObject(int siteId, List<TunnelProtection> protectTunnelObjectList, String tunnelType, EActionType actionType) throws Exception {
		List<TunnelObject> tunnelObjectList = null;
		TunnelService_MB tunnelService = null;
		List<Tunnel> activeList = null;
		List<Tunnel> tunnelList = null;
		TunnelObject tunnelObject = null;
		List<Lsp> lspParticularList = null;
		LSPObject lspObject = null;
		PortInst portInst_left = null;
		PortInst portInst_right = null;
		List<OamInfo> oamInfoList = null;
		Lsp lspParticular2 = null;
		List<Tunnel> tunnels = null;
		Tunnel mainTunnel = null;
		try {
			tunnelService = (TunnelService_MB) ConstantUtil.serviceFactory.newService_MB(Services.Tunnel);
			tunnelList = tunnelService.selectWHNodesBySiteId(siteId);
			activeList = new ArrayList<Tunnel>();

			for (Tunnel obj : tunnelList) {
				if (obj.getTunnelStatus() == EActiveStatus.ACTIVITY.getValue())
					activeList.add(obj);
			}
			tunnelObjectList = new ArrayList<TunnelObject>();
			for (int i = 0; i < activeList.size(); i++) {
				Tunnel tunnel = activeList.get(i);
				mainTunnel = new Tunnel();
				if (tunnel.getLspParticularList().size() == 1) {
					boolean flag = false;
					if(actionType != EActionType.DELETE){
						if ("186".equals(tunnelType)) {
							flag = true;
						}
					}else{	
						flag = true;
					}
					if (flag && "0".equals(tunnel.getTunnelType())) {// 封装保护tunnel和对应的保护配置块
						tunnels = new ArrayList<Tunnel>();
						TunnelProtection protectTunnelObject = new TunnelProtection();
						mainTunnel.setProtectTunnelId(tunnel.getTunnelId());
						tunnels.add(tunnel);// 保护tunnel
						mainTunnel = tunnelService.select_nojoin(mainTunnel).get(0);
						tunnels.add(mainTunnel);// 主用tunnel
						this.getProtectTunnelObject(siteId, tunnels, protectTunnelObject);
						protectTunnelObjectList.add(protectTunnelObject);
					}
				}
				tunnelObject = new TunnelObject();
				if (tunnel.getSncpIds() != null && tunnel.getSncpIds().length() > 0) {// 是否是SNCP保护类型tunnel
					tunnelObject.setServiceType(3);
				}
				tunnelObject.setBindingLsp(tunnel.getIsReverse());

				// lspParticularList =
				// lspParticularService.selectBySiteIdAndTunnelId(siteId,
				// tunnel.getTunnelId());
				lspParticularList = tunnel.getLspParticularList();
				if (lspParticularList.size() == 1) {// tunnel的A端或Z端
					lspParticular2 = lspParticularList.get(0);
					if (lspParticular2.getASiteId() == siteId) {
						portInst_left = this.getPortInst(lspParticular2.getAPortId());
						/** 前向lsp */
						lspObject = new LSPObject();
						lspObject.setVlanEnable(tunnel.getaVlanEnable());
						lspObject.setOutVlanValue(tunnel.getaOutVlanValue());
						lspObject.setTpId(tunnel.getaTp_id());
						lspObject.setSourceMac(transformMac(tunnel.getSourceMac()));
						lspObject.setTargetMac(transformMac(tunnel.getEndMac()));
						lspObject.setTunnelType(0);
						lspObject.setInLable(lspParticular2.getBackLabelValue());
						lspObject.setInTTL(lspParticular2.getFrontTtl());
						lspObject.setInProt(portInst_left.getNumber());
						lspObject.setInSlot(portInst_left.getSlotNumber());
//						lspObject.setOutprot(portInst_left.getNumber());
//						lspObject.setOutSlot(portInst_left.getSlotNumber());
						lspObject.setSourceIP(lspParticular2.getZoppositeId());
						lspObject.setTargetIP(lspParticular2.getAoppositeId());
						lspObject.setLspId(generateLspIdBytunnelId(lspParticular2.getAtunnelbusinessid(), "Fore"));
						if (tunnel.getQosList() != null && tunnel.getQosList().size() > 0) {// 转换qos信息
							lspObject.setPir(tunnel.getQosList().get(0).getPir() / 1000);
							lspObject.setCir(tunnel.getQosList().get(0).getCir() / 1000);
							lspObject.setPbs(tunnel.getQosList().get(0).getPbs());
							if (tunnel.getQosList().get(0).getCbs() < 0) {
								lspObject.setCbs(0);
							} else {
								lspObject.setCbs(tunnel.getQosList().get(0).getCbs());
							}
							lspObject.setCos(tunnel.getQosList().get(0).getCos());
						}
						lspObject.setBandwidthEnable(tunnel.getInBandwidthControl());
						tunnelObject.setForeLSP(lspObject);

						/** 后向lsp */
						lspObject = new LSPObject();
						lspObject.setVlanEnable(tunnel.getzVlanEnable());
						lspObject.setOutVlanValue(tunnel.getzOutVlanValue());
						lspObject.setTpId(tunnel.getzTp_id());
						lspObject.setSourceMac(transformMac(tunnel.getSourceMac()));
						lspObject.setTargetMac(transformMac(tunnel.getEndMac()));
						lspObject.setOutLable(lspParticular2.getFrontLabelValue());
						lspObject.setOutTTL(lspParticular2.getBackTtl());
//						lspObject.setInProt(portInst_left.getNumber());
						lspObject.setTunnelType(1);
						lspObject.setInSlot(portInst_left.getSlotNumber());
						lspObject.setOutprot(portInst_left.getNumber());
						lspObject.setOutSlot(portInst_left.getSlotNumber());
						lspObject.setLspId(generateLspIdBytunnelId(lspParticular2.getAtunnelbusinessid(), "After"));
						if (tunnel.getQosList() != null && tunnel.getQosList().size() > 0) {// 转换qos信息
							lspObject.setPir(tunnel.getQosList().get(1).getPir() / 1000);
							lspObject.setCir(tunnel.getQosList().get(1).getCir() / 1000);
							lspObject.setPbs(tunnel.getQosList().get(1).getPbs());
							lspObject.setCbs(tunnel.getQosList().get(1).getCbs());
							if (tunnel.getQosList().get(1).getCbs() < 0) {
								lspObject.setCbs(0);
							} else {
								lspObject.setCbs(tunnel.getQosList().get(1).getCbs());
							}
							lspObject.setCos(tunnel.getQosList().get(1).getCos());
						}
						lspObject.setBandwidthEnable(tunnel.getOutBandwidthControl());
						tunnelObject.setAfterLSP(lspObject);
						if (tunnel.getAprotectId() > 0) {
							tunnelObject.setSncpID(tunnel.getAprotectId());
						}
						tunnelObject.setTunnelId(lspParticular2.getAtunnelbusinessid());
						tunnelObject.setRole(0);
					} else if (lspParticular2.getZSiteId() == siteId) {
						portInst_right = this.getPortInst(lspParticular2.getZPortId());

						/** 前向lsp */
						lspObject = new LSPObject();
						lspObject.setVlanEnable(tunnel.getzVlanEnable());
						lspObject.setOutVlanValue(tunnel.getzOutVlanValue());
						lspObject.setTpId(tunnel.getzTp_id());
						lspObject.setSourceMac(transformMac(tunnel.getSourceMac()));
						lspObject.setTargetMac(transformMac(tunnel.getEndMac()));
						lspObject.setTunnelType(0);
						lspObject.setInLable(lspParticular2.getFrontLabelValue());
						lspObject.setInTTL(lspParticular2.getBackTtl());
						lspObject.setInProt(portInst_right.getNumber());
						lspObject.setInSlot(portInst_right.getSlotNumber());
						lspObject.setSourceIP(lspParticular2.getAoppositeId());
						lspObject.setTargetIP(lspParticular2.getZoppositeId());
						lspObject.setLspId(generateLspIdBytunnelId(lspParticular2.getZtunnelbusinessid(), "Fore"));
						if (tunnel.getQosList() != null && tunnel.getQosList().size() > 0) {// 转换qos信息
							lspObject.setPir(tunnel.getQosList().get(0).getPir() / 1000);
							lspObject.setCir(tunnel.getQosList().get(0).getCir() / 1000);
							lspObject.setPbs(tunnel.getQosList().get(0).getPbs());
							lspObject.setCbs(tunnel.getQosList().get(0).getCbs());
							lspObject.setCos(tunnel.getQosList().get(0).getCos());
						}
						lspObject.setBandwidthEnable(tunnel.getInBandwidthControl());
						tunnelObject.setForeLSP(lspObject);

						/** 后向lsp */
						lspObject = new LSPObject();
						lspObject.setVlanEnable(tunnel.getaVlanEnable());
						lspObject.setOutVlanValue(tunnel.getaOutVlanValue());
						lspObject.setTpId(tunnel.getaTp_id());
						lspObject.setSourceMac(transformMac(tunnel.getSourceMac()));
						lspObject.setTargetMac(transformMac(tunnel.getEndMac()));
						lspObject.setTunnelType(1);
						lspObject.setOutLable(lspParticular2.getBackLabelValue());
						lspObject.setOutTTL(lspParticular2.getFrontTtl());
						lspObject.setOutprot(portInst_right.getNumber());
						lspObject.setOutSlot(portInst_right.getSlotNumber());
						lspObject.setLspId(generateLspIdBytunnelId(lspParticular2.getZtunnelbusinessid(), "After"));
						if (tunnel.getQosList() != null && tunnel.getQosList().size() > 0) {// 转换qos信息
							lspObject.setPir(tunnel.getQosList().get(1).getPir() / 1000);
							lspObject.setCir(tunnel.getQosList().get(1).getCir() / 1000);
							lspObject.setPbs(tunnel.getQosList().get(1).getPbs());
							lspObject.setCbs(tunnel.getQosList().get(1).getCbs());
							lspObject.setCos(tunnel.getQosList().get(1).getCos());
						}
						lspObject.setBandwidthEnable(tunnel.getOutBandwidthControl());
						tunnelObject.setAfterLSP(lspObject);
						tunnelObject.setTunnelId(lspParticular2.getZtunnelbusinessid());
						tunnelObject.setRole(1);
					}
					if ("493".equals(tunnel.getTunnelType()) && mainTunnel.getTunnelId() > 0) {
						tunnelObject.getForeLSP().setTunnelType(3);
						tunnelObject.getAfterLSP().setTunnelType(3);
					}
				} else {// tunnel的中间站点
					//中间站点可能出现的情况:1-2,2-3或者2-3,1-2，所以需要去判断
					int firstASite = lspParticularList.get(0).getASiteId();
					int firstZSite = lspParticularList.get(0).getZSiteId();
					int endASite = lspParticularList.get(1).getASiteId();
					int endZSite = lspParticularList.get(1).getZSiteId();
					if(firstZSite == endASite){//1-2,2-3
						portInst_left = this.getPortInst(lspParticularList.get(0).getZPortId());
						portInst_right = this.getPortInst(lspParticularList.get(1).getAPortId());
					}else if(firstASite == endZSite){//2-3,1-2
						portInst_left = this.getPortInst(lspParticularList.get(1).getZPortId());
						portInst_right = this.getPortInst(lspParticularList.get(0).getAPortId());
						List<Lsp> lspList = new ArrayList<Lsp>();
						lspList.add(lspParticularList.get(1));
						lspList.add(lspParticularList.get(0));
						lspParticularList.clear();
						lspParticularList.addAll(lspList);
					}

					/** 前向lsp */
					lspObject = new LSPObject();
					lspObject.setVlanEnable(tunnel.getaVlanEnable());
					lspObject.setOutVlanValue(tunnel.getaOutVlanValue());
					lspObject.setTpId(tunnel.getaTp_id());
					if (tunnel.getIsSingle() == 1) {
						lspObject.setSourceMac(transformMac(lspParticularList.get(0).getSourceMac()));
						lspObject.setTargetMac(transformMac(lspParticularList.get(0).getTargetMac()));
					} else {
						lspObject.setSourceMac(transformMac(tunnel.getSourceMac()));
						lspObject.setTargetMac(transformMac(tunnel.getEndMac()));
					}
					lspObject.setTunnelType(2);
					lspObject.setInLable(lspParticularList.get(0).getFrontLabelValue());
					lspObject.setInProt(portInst_left.getNumber());
					lspObject.setInSlot(portInst_left.getSlotNumber());

					lspObject.setOutLable(lspParticularList.get(1).getFrontLabelValue());
					lspObject.setOutprot(portInst_right.getNumber());
					lspObject.setOutSlot(portInst_right.getSlotNumber());
					lspObject.setLspId(generateLspIdBytunnelId(lspParticularList.get(0).getZtunnelbusinessid(), "After"));
					lspObject.setSourceIP(lspParticularList.get(0).getAoppositeId());
					lspObject.setTargetIP(lspParticularList.get(0).getZoppositeId());
					lspObject.setPir(tunnel.getQosList().get(0).getPir() / 1000);
					lspObject.setCir(tunnel.getQosList().get(0).getCir() / 1000);
					lspObject.setPbs(tunnel.getQosList().get(0).getPbs());
					lspObject.setCbs(tunnel.getQosList().get(0).getCbs());
					lspObject.setCos(tunnel.getQosList().get(0).getCos());
					lspObject.setBandwidthEnable(tunnel.getInBandwidthControl());

					tunnelObject.setForeLSP(lspObject);

					/** 后向lsp */
					lspObject = new LSPObject();
					lspObject.setVlanEnable(tunnel.getzVlanEnable());
					lspObject.setOutVlanValue(tunnel.getzOutVlanValue());
					lspObject.setTpId(tunnel.getzTp_id());
					if (tunnel.getIsSingle() == 1) {
						lspObject.setSourceMac(transformMac(lspParticularList.get(1).getSourceMac()));
						lspObject.setTargetMac(transformMac(lspParticularList.get(1).getTargetMac()));
					} else {
						lspObject.setSourceMac(transformMac(tunnel.getSourceMac()));
						lspObject.setTargetMac(transformMac(tunnel.getEndMac()));
					}
					lspObject.setTunnelType(2);
					lspObject.setInLable(lspParticularList.get(1).getBackLabelValue());
					lspObject.setInProt(portInst_right.getNumber());
					lspObject.setInSlot(portInst_right.getSlotNumber());

					lspObject.setOutLable(lspParticularList.get(0).getBackLabelValue());
					lspObject.setOutprot(portInst_left.getNumber());
					lspObject.setOutSlot(portInst_left.getSlotNumber());
					lspObject.setLspId(generateLspIdBytunnelId(lspParticularList.get(1).getAtunnelbusinessid(), "Fore"));
					lspObject.setPir(tunnel.getQosList().get(1).getPir() / 1000);
					lspObject.setCir(tunnel.getQosList().get(1).getCir() / 1000);
					lspObject.setPbs(tunnel.getQosList().get(1).getPbs());
					lspObject.setCbs(tunnel.getQosList().get(1).getCbs());
					lspObject.setCos(tunnel.getQosList().get(1).getCos());
					lspObject.setBandwidthEnable(tunnel.getOutBandwidthControl());
					lspObject.setSourceIP(lspParticularList.get(1).getZoppositeId());
					lspObject.setTargetIP(lspParticularList.get(1).getAoppositeId());
					tunnelObject.setAfterLSP(lspObject);
					if (tunnel.getSncpIds() != null && tunnel.getSncpIds().length() > 0) {// 转换tunnel的SNCPid
						String[] strs = tunnel.getSncpIds().split("/");
						if (strs.length == 4) {
							if (Integer.parseInt(strs[0]) == siteId) {
								tunnelObject.setSncpID(Integer.parseInt(strs[1]));
							} else if (Integer.parseInt(strs[2]) == siteId) {
								tunnelObject.setSncpID(Integer.parseInt(strs[3]));
							}
						}

					}
					tunnelObject.setTunnelId(lspParticularList.get(0).getZtunnelbusinessid());
					tunnelObject.setRole(2);
				}

				oamInfoList = tunnel.getOamList();
				for (OamInfo oamInfo : oamInfoList) {// 转换oam信息，武汉tunnel的oam暂时只有A,Z端，中间网元没有oam
					if (oamInfo.getOamMep() != null && oamInfo.getOamMep().getSiteId() == siteId) {
						tunnelObject.setOam_resore("0");
						tunnelObject.setOam_tmp(oamInfo.getOamType().AMEP.getValue() + "");
						tunnelObject.setOam_mel(this.getMel(oamInfo.getOamMep().getMel()));
						tunnelObject.setOam_bao("00");
						tunnelObject.setSourceMEP(oamInfo.getOamMep().getLocalMepId());
						tunnelObject.setEquityMEP(oamInfo.getOamMep().getRemoteMepId());
						tunnelObject.setAspEnabl(oamInfo.getOamMep().isAps() ? 1 : 0);
						if (oamInfo.getOamMep().getLspTc() == 0) {
							tunnelObject.getAfterLSP().setInTC(0);
							tunnelObject.getAfterLSP().setOutTC(0);
							tunnelObject.getForeLSP().setInTC(0);
							tunnelObject.getForeLSP().setOutTC(0);
						}
						tunnelObject.getAfterLSP().setInTC(Integer.parseInt(UiUtil.getCodeById(oamInfo.getOamMep().getLspTc()).getCodeValue()));
						tunnelObject.getAfterLSP().setOutTC(Integer.parseInt(UiUtil.getCodeById(oamInfo.getOamMep().getLspTc()).getCodeValue()));
						tunnelObject.getForeLSP().setInTC(Integer.parseInt(UiUtil.getCodeById(oamInfo.getOamMep().getLspTc()).getCodeValue()));
						tunnelObject.getForeLSP().setOutTC(Integer.parseInt(UiUtil.getCodeById(oamInfo.getOamMep().getLspTc()).getCodeValue()));
						this.getMegIccAndMegUmc(tunnelObject, oamInfo.getOamMep().getMegIcc(), oamInfo.getOamMep().getMegUmc());
						tunnelObject.setCvEnabl(oamInfo.getOamMep().isCv() ? "1" : "0");
						tunnelObject.setCvCycle(getCycle(oamInfo.getOamMep().getCvCycle()));
						tunnelObject.setCvReserve("0000");
						tunnelObject.setLmEnabl(oamInfo.getOamMep().isLm() ? 1 : 0);
						tunnelObject.setOamEnable(oamInfo.getOamMep().isOamEnable() ? 1 : 0);
					} else if (oamInfo.getOamMip() != null && oamInfo.getOamMip().getSiteId() == siteId) {
						tunnelObject.setOam_resore("0");
						tunnelObject.setOam_tmp(0 + "");
						tunnelObject.setOam_mel(7 + "");
						tunnelObject.setOam_bao("00");
						tunnelObject.setSourceMEP(oamInfo.getOamMip().getMipId());
						tunnelObject.setEquityMEP(oamInfo.getOamMip().getMipId());
						tunnelObject.getAfterLSP().setInTC(oamInfo.getOamMip().getTc());
						tunnelObject.getAfterLSP().setOutTC(oamInfo.getOamMip().getTc());
						tunnelObject.getForeLSP().setInTC(oamInfo.getOamMip().getTc());
						tunnelObject.getForeLSP().setOutTC(oamInfo.getOamMip().getTc());
					}
				}
				tunnelObjectList.add(tunnelObject);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			UiUtil.closeService_MB(tunnelService);
			tunnelList = null;
			tunnelObject = null;
			lspParticularList = null;
			lspObject = null;
			portInst_left = null;
			portInst_right = null;
		}
		return tunnelObjectList;
	}

	/**
	 * 下发设备流程 封装保护配置块
	 * 
	 * @param siteId
	 * @param tunnelList
	 * @param protectTunnelObject
	 * @return
	 * @throws Exception
	 */
	private TunnelProtection getProtectTunnelObject(int siteId, List<Tunnel> tunnelList, TunnelProtection protectTunnelObject) throws Exception {
		PortService_MB portService = null;
		PortInst mainportInst = null;
		PortInst standportInst = null;
		Tunnel mainTunnel = tunnelList.get(1);
		Tunnel standTunnel = tunnelList.get(0);
		try {
			portService = (PortService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORT);
			if (mainTunnel.getASiteId() == siteId) {// A端
				mainportInst = new PortInst();
				mainportInst.setPortId(mainTunnel.getAPortId());
				mainportInst = portService.select(mainportInst).get(0);// 主用port

				standportInst = new PortInst();
				standportInst.setPortId(standTunnel.getAPortId());
				standportInst = portService.select(standportInst).get(0);// 备用port

				for (Lsp particular : mainTunnel.getLspParticularList()) {// 遍历主用tunnel
					if (particular.getASiteId() == siteId) {
						protectTunnelObject.setMainLspID(particular.getAtunnelbusinessid());
					}

				}

				for (Lsp particular : standTunnel.getLspParticularList()) {// 遍历保护tunnel
					if (particular.getASiteId() == siteId) {
						protectTunnelObject.setStandbyLspID(particular.getAtunnelbusinessid());
					}
				}
				protectTunnelObject.setLspLogicId(standTunnel.getAprotectId());
			}

			if (mainTunnel.getZSiteId() == siteId) {// Z端
				mainportInst = new PortInst();
				mainportInst.setPortId(mainTunnel.getZPortId());
				if (portService.select(mainportInst) != null && portService.select(mainportInst).size() > 0) {
					mainportInst = portService.select(mainportInst).get(0);// 主用port
				}

				standportInst = new PortInst();
				standportInst.setPortId(standTunnel.getZPortId());
				if (portService.select(standportInst) != null && portService.select(standportInst).size() > 0) {
					standportInst = portService.select(standportInst).get(0);// 备用port
				}

				for (Lsp particular : mainTunnel.getLspParticularList()) {// 遍历主用tunnel
					if (particular.getZSiteId() == siteId) {
						protectTunnelObject.setMainLspID(particular.getZtunnelbusinessid());
					}
				}

				for (Lsp particular : standTunnel.getLspParticularList()) {// 遍历保护tunnel
					if (particular.getZSiteId() == siteId) {
						protectTunnelObject.setStandbyLspID(particular.getZtunnelbusinessid());
					}
				}
				protectTunnelObject.setLspLogicId(standTunnel.getZprotectId());
			}
			if ("493".equals(mainTunnel.getTunnelType())) {// SNCP保护
				protectTunnelObject.setProtectionType(3);
			} else {// 普通1:1保护
				protectTunnelObject.setProtectionType(2);
			}
			protectTunnelObject.setId(mainTunnel.getTunnelId());
			protectTunnelObject.setMainPort(mainportInst.getNumber());
			protectTunnelObject.setObjProtectType(0 + "");
			protectTunnelObject.setMainSlot(10);
			protectTunnelObject.setProtractedTime(mainTunnel.getDelaytime());
			protectTunnelObject.setStandbySlot(10);
			protectTunnelObject.setStandbyPort(standportInst.getNumber());
			protectTunnelObject.setReturnType(mainTunnel.getProtectBack());
			protectTunnelObject.setOperationType(0);
			protectTunnelObject.setWaittime(mainTunnel.getWaittime());
		} catch (Exception e) {
			throw e;
		} finally {
			UiUtil.closeService_MB(portService);
		}
		return protectTunnelObject;
	}

	/**
	 * cycle的转换
	 * 
	 * @param cycle
	 * @return
	 */
	private String getCycle(double cycle) {
		String str = "";
		if (cycle == 87) {
			str = "001";
		} else if (cycle == 88) {
			str = "010";
		} else if (cycle == 89) {
			str = "011";
		} else if (cycle == 90) {
			str = "100";
		}
		return str;
	}

	/**
	 * mel的转换
	 * 
	 * @param mel
	 * @return
	 */
	private String getMel(int mel) {
		String str = "";
		str = Integer.toBinaryString(mel);
		for (int i = str.length(); i < 3; i++) {
			str = 0 + str;
		}
		return str;
	}

	/**
	 * 生成megumcID
	 * 
	 * @param tunnelObject
	 * @param megId
	 */
	private void getMegIccAndMegUmc(TunnelObject tunnelObject, String megICC, String megUCC) {
		tunnelObject.setMegIcc(getMeg(megICC));
		tunnelObject.setMegUmc(getMeg(megUCC));
	}

	/**
	 * 生成6位meg
	 * 
	 * @param meg
	 * @return
	 */
	private String getMeg(String meg) {
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < meg.length(); i++) {
			char c = meg.charAt(i);
			int b = c;

			if (i == 5) {
				str.append(b);
			} else {
				str.append(b + ",");
			}
		}
		for (int i = 0; i < 6-meg.length(); i++) {
			str.append("0,");
		}
		return str.toString();
	}

	/**
	 * 获取port对象
	 * 
	 * @param portId
	 * @return
	 * @throws Exception
	 */
	private PortInst getPortInst(int portId) throws Exception {
		PortService_MB portService = null;
		PortInst portinst = null;
		List<PortInst> portInstList = null;
		try {
			portService = (PortService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORT);
			portinst = new PortInst();
			portinst.setPortId(portId);
			portInstList = portService.select(portinst);
			if (portInstList == null || portInstList.size() != 1) {
				throw new Exception("查询port出错");
			}
			portinst = portInstList.get(0);

		} catch (Exception e) {
			throw e;
		} finally {
			UiUtil.closeService_MB(portService);
		}
		return portinst;
	}

	/**
	 * 获取siteID集合
	 * 
	 * @param tunnel
	 *            tunnel对象
	 * @return siteID集合
	 * @throws Exception
	 */
	private List<Integer> getSiteIds(List<Tunnel> tunnelList) throws Exception {
		List<Integer> siteIds = null;
		TunnelService_MB tunnelService = null;
		Tunnel protectTunnel = null;
		try {
			siteIds = new ArrayList<Integer>();
			tunnelService = (TunnelService_MB) ConstantUtil.serviceFactory.newService_MB(Services.Tunnel);
			for (Tunnel tunnel : tunnelList) {
				if (tunnel.getProtectTunnelId() > 0) {
					protectTunnel = new Tunnel();
					protectTunnel.setTunnelId(tunnel.getProtectTunnelId());
					protectTunnel = tunnelService.select_nojoin(protectTunnel).get(0);
					for (Lsp lspParticular : protectTunnel.getLspParticularList()) {
						if (lspParticular.getASiteId() > 0) {
							if (!siteIds.contains(lspParticular.getASiteId()) && super.getManufacturer(lspParticular.getASiteId()) == EManufacturer.WUHAN.getValue()) {
								siteIds.add(lspParticular.getASiteId());
							}
						}
						if (lspParticular.getZSiteId() > 0) {
							if (!siteIds.contains(lspParticular.getZSiteId()) && lspParticular.getZPortId() > 0 && super.getManufacturer(lspParticular.getZSiteId()) == EManufacturer.WUHAN.getValue()) {
								siteIds.add(lspParticular.getZSiteId());
							}
						}
					}

				}

				for (Lsp lspParticular : tunnel.getLspParticularList()) {
					if (lspParticular.getASiteId() > 0) {
						if (!siteIds.contains(lspParticular.getASiteId()) && super.getManufacturer(lspParticular.getASiteId()) == EManufacturer.WUHAN.getValue()) {
							siteIds.add(lspParticular.getASiteId());
						}
					}
					if (lspParticular.getZSiteId() > 0) {
						if (!siteIds.contains(lspParticular.getZSiteId()) && lspParticular.getZPortId() > 0 && super.getManufacturer(lspParticular.getZSiteId()) == EManufacturer.WUHAN.getValue()) {
							siteIds.add(lspParticular.getZSiteId());
						}
					}
				}
			}

		} catch (Exception e) {
			throw e;
		} finally {
			UiUtil.closeService_MB(tunnelService);
		}
		return siteIds;
	}

	/**
	 * TunnelID生成lspid
	 * 
	 * @param tunnelId
	 * @param str
	 * @return
	 */
	private int generateLspIdBytunnelId(int tunnelId, String str) {
		if ("Fore".equals(str)) {
			return (tunnelId - 1) * 2 + 1;
		} else if ("After".equals(str)) {
			return (tunnelId - 1) * 2 + 2;
		}
		return 0;
	}

	/**
	 * 与设备同步LSP
	 * 
	 * synchro_lsp(这里用一句话描述这个方法的作用)
	 * 
	 * 
	 * @param TunnelObject
	 *            设备上的tunnel名称
	 * 
	 * @return
	 * @throws Exception
	 * 
	 * @Exception 异常对象
	 */
	@Override
	public Object synchro(int siteId) {
		OperationObject operationObject = new OperationObject();
		OperationObject operationObjectResult = null;
		OperationObject operationObjectProtect = new OperationObject();
		OperationObject operationObjectResultProtect = null;
		TunnelService_MB tunnelService = null;
		try {
			tunnelService = (TunnelService_MB) ConstantUtil.serviceFactory.newService_MB(Services.Tunnel);
			operationObject = this.getSynchroOperationObject(siteId, "tunnelSynchro");// 封装下发数据
			super.sendAction(operationObject);
			operationObjectResult = super.verification(operationObject);
			if (operationObjectResult.isSuccess()) {// 返回成功
				for (ActionObject actionObject : operationObjectResult.getActionObjectList()) {
					// tunnelService = (TunnelService)
					// ConstantUtil.serviceFactory.newService(Services.Tunnel);
					tunnelService.update_activity(siteId, EActiveStatus.UNACTIVITY.getValue());
					this.synchroTunnel_db(actionObject, siteId);// tunnel,与数据库进行同步
				}

			}
			operationObjectProtect = this.getSynchroOperationObject(siteId, "tunnelProtectSynchro");// 封装下发数据
			super.sendAction(operationObjectProtect);
			operationObjectResultProtect = super.verification(operationObjectProtect);
			if (operationObjectResultProtect.isSuccess()) {// 返回成功
				SynchroUtil synchroUtil = new SynchroUtil();
				for (ActionObject actionObject : operationObjectResultProtect.getActionObjectList()) {
					synchroUtil.protectSynchro(actionObject.getLSPProtectionList(), siteId);// 保护与数据库进行同步
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			UiUtil.closeService_MB(tunnelService);
		}

		return ResultString.CONFIG_SUCCESS;
	}

	/**
	 * 与数据库进行同步
	 * 
	 * @param tunnelObjectList
	 * @param siteId
	 */
	private void synchroTunnel_db(ActionObject actionObject, int siteId) {
		List<Tunnel> tunnelList = null;
		tunnelList = this.getTunnelInfo(actionObject.getTunnelObjectList(), siteId);
		SynchroUtil synchroUtil = new SynchroUtil();
		for (Tunnel tunnel : tunnelList) {
			try {
				synchroUtil.tunnelSynchro(tunnel, "", siteId);
			} catch (Exception e) {
				ExceptionManage.dispose(e, this.getClass());
			}
		}
	}

//	/**
//	 * 同步设备流程 封装OperationObject
//	 * 
//	 * @param siteId
//	 * @param type
//	 * @return
//	 * @throws Exception
//	 */
//	private OperationObject getSynchroOperationObject(int siteId, String type) throws Exception {
//		OperationObject operationObject = null;
//		ActionObject actionObject = null;
//		NEObject neObject = null;
//
//		try {
//			operationObject = new OperationObject();
//			WhImplUtil whImplUtil = new WhImplUtil();
//			neObject = whImplUtil.siteIdToNeObject(siteId);
//			actionObject = new ActionObject();
//			actionObject.setActionId(super.getActionId(operationObject.getActionObjectList()));
//			actionObject.setNeObject(neObject);
//			actionObject.setType(type);
//			operationObject.getActionObjectList().add(actionObject);
//
//		} catch (Exception e) {
//			throw e;
//		} finally {
//			actionObject = null;
//			neObject = null;
//		}
//		return operationObject;
//	}

	/**
	 * 同步设备流程 封装Tunnel信息
	 * 
	 * @param tunnelObjectList
	 * @param siteId
	 * @return
	 */
	public List<Tunnel> getTunnelInfo(List<TunnelObject> tunnelObjectList, int siteId) {
		List<Tunnel> tunnelList = new ArrayList<Tunnel>();

		for (TunnelObject tunnelObject : tunnelObjectList) {
			Tunnel tunnel = new Tunnel();
			// tunnel.setTunnelId(tunnelObject.getTunnelId());
			tunnel.setTunnelStatus(EActiveStatus.ACTIVITY.getValue());
			tunnel.setTunnelType(185 + "");
			tunnel.setIsSingle(1);
			tunnel.setSourceMac(tunnelObject.getForeLSP().getSourceMac());
			tunnel.setEndMac(tunnelObject.getForeLSP().getTargetMac());
			tunnel.setTunnelName("tunnel_" + tunnelObject.getTunnelId());
			tunnel.setInBandwidthControl(tunnelObject.getForeLSP().getBandwidthEnable());
			tunnel.setOutBandwidthControl(tunnelObject.getAfterLSP().getBandwidthEnable());
			tunnel.setLspParticularList(this.getLspInfo(tunnelObject, siteId, tunnel));// tunnelObject转换成tunnel
			tunnel.setOamList(this.getTunnelOamInfo(tunnelObject, siteId));// oam数据转换
			tunnel.setQosList(this.getQosInfos(tunnelObject, siteId));// qos数据转换
			tunnelList.add(tunnel);
		}
		return tunnelList;
	}

	public HashMap<Integer, Tunnel> getTunnelInfoMap(List<TunnelObject> tunnelObjectList, int siteId) {
		HashMap<Integer, Tunnel> hashMap = new HashMap<Integer, Tunnel>();

		for (TunnelObject tunnelObject : tunnelObjectList) {
			Tunnel tunnel = new Tunnel();
			// tunnel.setTunnelId(tunnelObject.getTunnelId());
			tunnel.setTunnelStatus(EActiveStatus.ACTIVITY.getValue());
			tunnel.setTunnelType(185 + "");
			tunnel.setIsSingle(1);
			tunnel.setSourceMac(tunnelObject.getForeLSP().getSourceMac());
			tunnel.setEndMac(tunnelObject.getForeLSP().getTargetMac());
			tunnel.setTunnelName("tunnel_" + tunnelObject.getTunnelId());
			tunnel.setInBandwidthControl(tunnelObject.getForeLSP().getBandwidthEnable());
			tunnel.setOutBandwidthControl(tunnelObject.getAfterLSP().getBandwidthEnable());
			tunnel.setLspParticularList(this.getLspInfo(tunnelObject, siteId, tunnel));// tunnelObject转换成tunnel
			tunnel.setOamList(this.getTunnelOamInfo(tunnelObject, siteId));// oam数据转换
			tunnel.setQosList(this.getQosInfos(tunnelObject, siteId));// qos数据转换
			hashMap.put(tunnelObject.getTunnelId(), tunnel);
		}
		return hashMap;
	}

	/**
	 * 同步设备流程 封装Lsp信息
	 * 
	 * @param tunnelObject
	 * @param siteId
	 * @return
	 */
	private List<Lsp> getLspInfo(TunnelObject tunnelObject, int siteId, Tunnel tunnel) {
		List<Lsp> lspList = new ArrayList<Lsp>();
		Lsp afterLSP = new Lsp();
		Lsp foreLSP = new Lsp();
		PortService_MB portService = null;
		PortInst aPortInst = null;
		PortInst zPortInst = null;
		LSPObject foreLSPObject = null;
		LSPObject afterLSPObject = null;
		try {
			portService = (PortService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORT);
			if (tunnelObject.getAfterLSP().getTunnelType() == 2) {// 判断是否为中间网元
				afterLSPObject = tunnelObject.getAfterLSP();
				foreLSPObject = tunnelObject.getForeLSP();
				// 左端lsp
				zPortInst = new PortInst();
				zPortInst.setSiteId(siteId);
				zPortInst.setNumber(tunnelObject.getForeLSP().getInProt());
				zPortInst = portService.select(zPortInst).get(0);
				foreLSP.setBackLabelValue(afterLSPObject.getOutLable());
				foreLSP.setZPortId(zPortInst.getPortId());
				foreLSP.setZSiteId(siteId);
				foreLSP.setFrontLabelValue(foreLSPObject.getInLable());
				foreLSP.setZtunnelbusinessid(tunnelObject.getTunnelId());
				foreLSP.setAoppositeId(foreLSPObject.getSourceIP());
				foreLSP.setZoppositeId(foreLSPObject.getTargetIP());
				foreLSP.setSourceMac(foreLSPObject.getSourceMac());
				foreLSP.setTargetMac(foreLSPObject.getTargetMac());
				lspList.add(foreLSP);
				// 右端lsp
				aPortInst = new PortInst();
				aPortInst.setSiteId(siteId);
				aPortInst.setNumber(tunnelObject.getAfterLSP().getInProt());
				aPortInst = portService.select(aPortInst).get(0);
				afterLSP.setBackLabelValue(afterLSPObject.getInLable());
				afterLSP.setAPortId(aPortInst.getPortId());
				afterLSP.setASiteId(siteId);
				afterLSP.setFrontLabelValue(foreLSPObject.getOutLable());
				afterLSP.setAtunnelbusinessid(tunnelObject.getTunnelId());
				afterLSP.setZoppositeId(afterLSPObject.getSourceIP());
				afterLSP.setAoppositeId(afterLSPObject.getTargetIP());
				afterLSP.setSourceMac(afterLSPObject.getSourceMac());
				afterLSP.setTargetMac(afterLSPObject.getTargetMac());
				lspList.add(afterLSP);

				tunnel.setaVlanEnable(foreLSPObject.getVlanEnable());
				tunnel.setaOutVlanValue(foreLSPObject.getOutVlanValue());
				tunnel.setaTp_id(foreLSPObject.getTpId());
				tunnel.setzVlanEnable(afterLSPObject.getVlanEnable());
				tunnel.setzOutVlanValue(afterLSPObject.getOutVlanValue());
				tunnel.setzTp_id(afterLSPObject.getTpId());
			} else {// A,Z端
				aPortInst = new PortInst();
				aPortInst.setSiteId(siteId);
				aPortInst.setNumber(tunnelObject.getForeLSP().getInProt());
				aPortInst = portService.select(aPortInst).get(0);
				if (tunnelObject.getRole() == 0) {
					foreLSP.setBackLabelValue(tunnelObject.getForeLSP().getInLable());
					foreLSP.setBackTtl(tunnelObject.getForeLSP().getInTTL());
					foreLSP.setAPortId(aPortInst.getPortId());
					foreLSP.setASiteId(siteId);
					foreLSP.setFrontLabelValue(tunnelObject.getAfterLSP().getOutLable());
					foreLSP.setFrontTtl(tunnelObject.getAfterLSP().getOutTTL());
					foreLSP.setAtunnelbusinessid(tunnelObject.getTunnelId());
					foreLSP.setAoppositeId(tunnelObject.getForeLSP().getTargetIP());
					foreLSP.setZoppositeId(tunnelObject.getForeLSP().getSourceIP());
					foreLSP.setSourceMac(tunnelObject.getForeLSP().getSourceMac());
					foreLSP.setTargetMac(tunnelObject.getForeLSP().getTargetMac());
					lspList.add(foreLSP);
					tunnel.setASiteId(siteId);
					tunnel.setAPortId(aPortInst.getPortId());
				} else {
					foreLSP.setFrontLabelValue(tunnelObject.getForeLSP().getInLable());
					foreLSP.setBackTtl(tunnelObject.getForeLSP().getInTTL());
					foreLSP.setZPortId(aPortInst.getPortId());
					foreLSP.setZSiteId(siteId);
					foreLSP.setBackLabelValue(tunnelObject.getAfterLSP().getOutLable());
					foreLSP.setFrontTtl(tunnelObject.getAfterLSP().getOutTTL());
					foreLSP.setZtunnelbusinessid(tunnelObject.getTunnelId());
					foreLSP.setAoppositeId(tunnelObject.getForeLSP().getSourceIP());
					foreLSP.setZoppositeId(tunnelObject.getForeLSP().getTargetIP());
					foreLSP.setSourceMac(tunnelObject.getForeLSP().getSourceMac());
					foreLSP.setTargetMac(tunnelObject.getForeLSP().getTargetMac());
					lspList.add(foreLSP);
					tunnel.setZSiteId(siteId);
					tunnel.setZPortId(aPortInst.getPortId());
				}

				tunnel.setaVlanEnable(tunnelObject.getForeLSP().getVlanEnable());
				tunnel.setaOutVlanValue(tunnelObject.getForeLSP().getOutVlanValue());
				tunnel.setaTp_id(tunnelObject.getForeLSP().getTpId());
				tunnel.setzVlanEnable(tunnelObject.getAfterLSP().getVlanEnable());
				tunnel.setzOutVlanValue(tunnelObject.getAfterLSP().getOutVlanValue());
				tunnel.setzTp_id(tunnelObject.getAfterLSP().getTpId());
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			UiUtil.closeService_MB(portService);
			aPortInst = null;
			zPortInst = null;
		}
		return lspList;
	}

	/**
	 * 封装Oam信息
	 * 
	 * @param tunnelObject
	 * @param siteId
	 * @return
	 * @throws Exception
	 */
	private List<OamInfo> getTunnelOamInfo(TunnelObject tunnelObject, int siteId) {
		List<OamInfo> oamInfoList = new ArrayList<OamInfo>();
		OamInfo oam = new OamInfo();

		if (tunnelObject.getAfterLSP().getTunnelType() != 2) {
			try {
				OamMepInfo oamMep = new OamMepInfo();
				oamMep.setLspTc((UiUtil.getCodeByValue("TC", tunnelObject.getForeLSP().getInTC() + "")).getId());
				oamMep.setMel(Integer.parseInt(tunnelObject.getOam_mel(), 2));
				oamMep.setMegIcc(this.synchroMeg(tunnelObject.getMegIcc()));
				oamMep.setMegUmc(this.synchroMeg(tunnelObject.getMegUmc()));
				oamMep.setLocalMepId(tunnelObject.getSourceMEP());
				oamMep.setRemoteMepId(tunnelObject.getEquityMEP());
				oamMep.setAps(tunnelObject.getAspEnabl() == 1 ? true : false);
				oamMep.setCv("1".equals(tunnelObject.getCvEnabl()));
				oamMep.setCvCycle(getCycleCode(tunnelObject.getCvCycle()));
				oamMep.setCvReserve1(Integer.parseInt(tunnelObject.getCvReserve()));
				oamMep.setLm(tunnelObject.getLmEnabl() == 1);
				oamMep.setSiteId(siteId);
				oamMep.setObjId(tunnelObject.getTunnelId());
				oamMep.setObjType("TUNNEL");
				if (tunnelObject.getRole() == 0) {
					oam.setOamType(OamTypeEnum.AMEP);
				} else if (tunnelObject.getRole() == 1) {
					oam.setOamType(OamTypeEnum.ZMEP);
				}
				oamMep.setOamEnable(tunnelObject.getOamEnable() == 1?true:false);
				oam.setOamMep(oamMep);
			} catch (Exception e) {
				ExceptionManage.dispose(e, this.getClass());
			}

		} else {
			OamMipInfo oamMipInfo = new OamMipInfo();
			oamMipInfo.setMipId(tunnelObject.getSourceMEP());
			oamMipInfo.setSiteId(siteId);
			oamMipInfo.setObjId(tunnelObject.getTunnelId());
			oamMipInfo.setObjType("TUNNEL");
			oam.setOamType(OamTypeEnum.MIP);
			oam.setOamMip(oamMipInfo);
		}
		oamInfoList.add(oam);
		return oamInfoList;
	}

	/**
	 * 同步meg
	 * 
	 * @param meg
	 * @return
	 */
	private String synchroMeg(String meg) {
		StringBuffer stringBuffer = new StringBuffer();
		String[] strs = meg.split(",");
		for (String str : strs) {
			char c = (char) Integer.parseInt(str);
			stringBuffer.append(c);
		}
		return stringBuffer.toString();
	}

	/**
	 * 同步设备流程 转换qos
	 * 
	 * @param tunnelObject
	 * @param siteId
	 * @return
	 */
	private List<QosInfo> getQosInfos(TunnelObject tunnelObject, int siteId) {
		List<QosInfo> qosInfos = new ArrayList<QosInfo>();
		QosInfo qosInfo = new QosInfo();
		qosInfo.setSiteId(siteId);
		qosInfo.setQosType(QosTemplateTypeEnum.LLSP.toString());
		qosInfo.setCos(tunnelObject.getForeLSP().getCos());
		qosInfo.setDirection(EQosDirection.FORWARD.getValue() + "");
		qosInfo.setCir(tunnelObject.getForeLSP().getCir() * 1000);
		qosInfo.setCbs(tunnelObject.getForeLSP().getCbs());
		qosInfo.setPir(tunnelObject.getForeLSP().getPir() * 1000);
		qosInfo.setPbs(tunnelObject.getForeLSP().getPbs());
		qosInfo.setQosname("tunnel_" + tunnelObject.getTunnelId());
		qosInfo.setStatus(2);
		qosInfos.add(qosInfo);

		qosInfo = new QosInfo();
		qosInfo.setSiteId(siteId);
		qosInfo.setQosType(QosTemplateTypeEnum.LLSP.toString());
		qosInfo.setCos(tunnelObject.getAfterLSP().getCos());
		qosInfo.setDirection(EQosDirection.BACKWARD.getValue() + "");
		qosInfo.setCir(tunnelObject.getAfterLSP().getCir() * 1000);
		qosInfo.setCbs(tunnelObject.getAfterLSP().getCbs());
		qosInfo.setPir(tunnelObject.getAfterLSP().getPir() * 1000);
		qosInfo.setPbs(tunnelObject.getAfterLSP().getPbs());
		qosInfo.setQosname("tunnel_" + tunnelObject.getTunnelId());
		qosInfo.setStatus(2);
		qosInfos.add(qosInfo);
		return qosInfos;
	}

	/**
	 * 同步设备流程 同步cycle
	 * 
	 * @param cycle
	 * @return
	 */
	private int getCycleCode(String cycle) {
		int codeId = 0;
		if ("001".equals(cycle)) {
			codeId = 87;
		} else if ("010".equals(cycle)) {
			codeId = 88;
		} else if ("011".equals(cycle)) {
			codeId = 89;
		} else if ("100".equals(cycle)) {
			codeId = 90;
		}
		return codeId;
	}

	private String transformMac(String mac) {
		StringBuffer buffer = new StringBuffer();
		String[] strs = mac.split("-");
		for (int i = 0; i < strs.length; i++) {
			String str = strs[i];
			if (i == strs.length - 1) {
				buffer.append(transformInt(str));
			} else {
				buffer.append(transformInt(str) + "-");
			}
		}
		return buffer.toString();
	}

	private int transformInt(String str) {
		int first = 0;
		int last = 0;
		str.subSequence(0, 1);
		if ("0".equals(str.subSequence(0, 1))) {
			first = 0;
		}
		if ("0".equals(str.substring(1))) {
			last = 0;
		}
		if ("1".equals(str.subSequence(0, 1))) {
			first = 1;
		}
		if ("1".equals(str.substring(1))) {
			last = 1;
		}
		if ("2".equals(str.subSequence(0, 1))) {
			first = 2;
		}
		if ("2".equals(str.substring(1))) {
			last = 2;
		}
		if ("3".equals(str.subSequence(0, 1))) {
			first = 3;
		}
		if ("3".equals(str.substring(1))) {
			last = 3;
		}
		if ("4".equals(str.subSequence(0, 1))) {
			first = 4;
		}
		if ("4".equals(str.substring(1))) {
			last = 4;
		}
		if ("5".equals(str.subSequence(0, 1))) {
			first = 5;
		}
		if ("5".equals(str.substring(1))) {
			last = 5;
		}
		if ("6".equals(str.subSequence(0, 1))) {
			first = 6;
		}
		if ("6".equals(str.substring(1))) {
			last = 6;
		}
		if ("7".equals(str.subSequence(0, 1))) {
			first = 7;
		}
		if ("7".equals(str.substring(1))) {
			last = 7;
		}
		if ("8".equals(str.subSequence(0, 1))) {
			first = 8;
		}
		if ("8".equals(str.substring(1))) {
			last = 8;
		}
		if ("9".equals(str.subSequence(0, 1))) {
			first = 9;
		}
		if ("9".equals(str.substring(1))) {
			last = 9;
		}
		if ("A".equals(str.subSequence(0, 1))) {
			first = 10;
		}
		if ("A".equals(str.substring(1))) {
			last = 10;
		}
		if ("B".equals(str.subSequence(0, 1))) {
			first = 11;
		}
		if ("B".equals(str.substring(1))) {
			last = 11;
		}
		if ("C".equals(str.subSequence(0, 1))) {
			first = 12;
		}
		if ("C".equals(str.substring(1))) {
			last = 12;
		}
		if ("D".equals(str.subSequence(0, 1))) {
			first = 13;
		}
		if ("D".equals(str.substring(1))) {
			last = 13;
		}
		if ("E".equals(str.subSequence(0, 1))) {
			first = 14;
		}
		if ("E".equals(str.substring(1))) {
			last = 14;
		}
		if ("F".equals(str.subSequence(0, 1))) {
			first = 15;
		}
		if ("F".equals(str.substring(1))) {
			last = 15;
		}
		return first * 16 + last;
	}
}
