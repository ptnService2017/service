﻿package com.nms.service.impl.wh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nms.db.bean.equipment.port.PortInst;
import com.nms.db.bean.ptn.path.eth.ElanInfo;
import com.nms.db.bean.ptn.path.eth.EtreeInfo;
import com.nms.db.bean.ptn.path.pw.PwInfo;
import com.nms.db.bean.ptn.path.pw.PwNniInfo;
import com.nms.db.bean.ptn.path.tunnel.Tunnel;
import com.nms.db.bean.ptn.port.AcPortInfo;
import com.nms.db.bean.ptn.port.Acbuffer;
import com.nms.db.bean.ptn.port.PortLagInfo;
import com.nms.db.bean.ptn.qos.QosInfo;
import com.nms.db.enums.EActiveStatus;
import com.nms.db.enums.EManufacturer;
import com.nms.db.enums.EServiceType;
import com.nms.drive.service.bean.ELanObject;
import com.nms.drive.service.bean.ETreeNNIObject;
import com.nms.drive.service.bean.ETreeObject;
import com.nms.drive.service.bean.ETreeUNIObject;
import com.nms.drive.service.bean.NEObject;
import com.nms.drive.service.bean.VpwsBuffer;
import com.nms.model.equipment.port.PortService_MB;
import com.nms.model.ptn.path.eth.ElanInfoService_MB;
import com.nms.model.ptn.path.eth.EtreeInfoService_MB;
import com.nms.model.ptn.path.pw.PwInfoService_MB;
import com.nms.model.ptn.path.pw.PwNniInfoService_MB;
import com.nms.model.ptn.port.AcBufferService_MB;
import com.nms.model.ptn.port.AcPortInfoService_MB;
import com.nms.model.ptn.port.PortLagService_MB;
import com.nms.model.ptn.qos.QosInfoService_MB;
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

public class ElanWHServiceImpl extends WHOperationBase implements OperationServiceI {
	@SuppressWarnings("unchecked")
	@Override
	public String excutionDelete(List objectList) throws Exception {
		List<ElanInfo> elanInfoList = null;
		List<Integer> siteIdList = null;
		OperationObject operationObjectAfter = null;
		OperationObject operationObjectResult = null;
		List<ELanObject> eLanObjectList = null;
		NEObject neObject = null;
		EtreeInfoService_MB etreeService = null;
		ElanInfoService_MB elanInfoService = null;
		PwNniInfoService_MB pwNNIService = null;
		try {
			elanInfoList = objectList;
			siteIdList = this.getSiteIds(elanInfoList);		
			if (siteIdList.size() > 0) {
				if (super.isLockBySiteIdList(siteIdList)) {
					return ResourceUtil.srcStr(StringKeysTip.TIP_SITE_LOCK);
				}
				etreeService = (EtreeInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.EtreeInfo);
				elanInfoService = (ElanInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.ElanInfo);
				pwNNIService = (PwNniInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PwNniBuffer);
				super.lockSite(siteIdList, SiteLockTypeUtil.ELAN_DELETE);
				SiteUtil siteUtil=new SiteUtil();
				WhImplUtil whImplUtil = new WhImplUtil();
				operationObjectAfter = new OperationObject();
				for(Integer siteId : siteIdList){
					if("0".equals(siteUtil.SiteTypeUtil(siteId)+"")){////非失连网元、非虚拟网元下发设备
						neObject = whImplUtil.siteIdToNeObject(siteId);
						eLanObjectList = this.getElanObject(etreeService,elanInfoService,pwNNIService,siteId);//获取下发elan对象
						ActionObject actionObj = this.getActionObject(eLanObjectList, neObject);
						operationObjectAfter.getActionObjectList().add(actionObj);
					}
				}
				super.sendAction(operationObjectAfter);
				operationObjectResult = super.verification(operationObjectAfter);//设备返回信息
				if (!(operationObjectResult.isSuccess())) {//有一次失败，直接返回配置失败
					super.clearLock(siteIdList);
					return super.getErrorMessage(operationObjectResult);
				}
			}
			return ResultString.CONFIG_SUCCESS;
		} catch (Exception e) {
			throw e;
		} finally {
			super.clearLock(siteIdList);
			UiUtil.closeService_MB(elanInfoService);
			UiUtil.closeService_MB(etreeService);
			UiUtil.closeService_MB(pwNNIService);
		}
	}

	private ActionObject getActionObject(List<ELanObject> eLanObjectList, NEObject neObject) {
		ActionObject actionObject = null;
		try {
			actionObject = new ActionObject();
			List<ActionObject> actionIdList = new ArrayList<ActionObject>();
			actionIdList.add(actionObject);
			actionObject.setActionId(super.getActionId(actionIdList));
			actionObject.setNeObject(neObject);
			actionObject.setType("elan");
			actionObject.setElanObejctList(eLanObjectList);
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return actionObject;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String excutionInsert(Object object) throws Exception {
		List<ElanInfo> elanInfoList = null;
		List<Integer> siteIdList = null;
		OperationObject operationObjectAfter = null;
		OperationObject operationObjectResult = null;
		List<ELanObject> eLanObjectList = null;
		NEObject neObject = null;
		EtreeInfoService_MB etreeService = null;
		ElanInfoService_MB elanInfoService = null;
		PwNniInfoService_MB pwNNIService = null;
		try {
			elanInfoList = (List<ElanInfo>)object;
			siteIdList = this.getSiteIds(elanInfoList);
			
			if (siteIdList.size() > 0) {
				if (super.isLockBySiteIdList(siteIdList)) {
					return ResourceUtil.srcStr(StringKeysTip.TIP_SITE_LOCK);
				}
				etreeService = (EtreeInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.EtreeInfo);
				elanInfoService = (ElanInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.ElanInfo);
				pwNNIService = (PwNniInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PwNniBuffer);
				super.lockSite(siteIdList, SiteLockTypeUtil.ELAN_DELETE);
				SiteUtil siteUtil=new SiteUtil();
				WhImplUtil whImplUtil = new WhImplUtil();
				operationObjectAfter = new OperationObject();
				for(Integer siteId : siteIdList){
					if("0".equals(siteUtil.SiteTypeUtil(siteId)+"")){////非失连网元、非虚拟网元下发设备
						neObject = whImplUtil.siteIdToNeObject(siteId);
						eLanObjectList = this.getElanObject(etreeService,elanInfoService,pwNNIService,siteId);//获取下发elan对象
						ActionObject actionObj = this.getActionObject(eLanObjectList, neObject);
						operationObjectAfter.getActionObjectList().add(actionObj);
					}
				}
				super.sendAction(operationObjectAfter);
				operationObjectResult = super.verification(operationObjectAfter);//设备返回信息
				if (!(operationObjectResult.isSuccess())) {//有一次失败，直接返回配置失败
					super.clearLock(siteIdList);
					return super.getErrorMessage(operationObjectResult);
				}
			}
			return ResultString.CONFIG_SUCCESS;
		} catch (Exception e) {
			throw e;
		} finally {
			super.clearLock(siteIdList);
			UiUtil.closeService_MB(elanInfoService);
			UiUtil.closeService_MB(etreeService);
			UiUtil.closeService_MB(pwNNIService);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String excutionUpdate(Object object) throws Exception {
		List<ElanInfo> elanInfoList = null;
		List<Integer> siteIdList = null;
		OperationObject operationObjectAfter = null;
		OperationObject operationObjectResult = null;
		List<ELanObject> eLanObjectList = null;
		NEObject neObject = null;
		EtreeInfoService_MB etreeService = null;
		ElanInfoService_MB elanInfoService = null;
		PwNniInfoService_MB pwNNIService = null;
		try {
			elanInfoList = (List<ElanInfo>)object;
			siteIdList = this.getSiteIds(elanInfoList);
			if (siteIdList.size() > 0) {
				if (super.isLockBySiteIdList(siteIdList)) {
					return ResourceUtil.srcStr(StringKeysTip.TIP_SITE_LOCK);
				}
				etreeService = (EtreeInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.EtreeInfo);
				elanInfoService = (ElanInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.ElanInfo);
				pwNNIService = (PwNniInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PwNniBuffer);
				super.lockSite(siteIdList, SiteLockTypeUtil.ELAN_DELETE);
				SiteUtil siteUtil=new SiteUtil();
				WhImplUtil whImplUtil = new WhImplUtil();
				operationObjectAfter = new OperationObject();
				for(Integer siteId : siteIdList){
					if("0".equals(siteUtil.SiteTypeUtil(siteId)+"")){////非失连网元、非虚拟网元下发设备
						neObject = whImplUtil.siteIdToNeObject(siteId);
						eLanObjectList = this.getElanObject(etreeService,elanInfoService,pwNNIService,siteId);//获取下发elan对象
						ActionObject actionObj = this.getActionObject(eLanObjectList, neObject);
						operationObjectAfter.getActionObjectList().add(actionObj);
					}
				}
				super.sendAction(operationObjectAfter);
				operationObjectResult = super.verification(operationObjectAfter);//设备返回信息
				if (!(operationObjectResult.isSuccess())) {//有一次失败，直接返回配置失败
					super.clearLock(siteIdList);
					return super.getErrorMessage(operationObjectResult);
				}
			}
			return ResultString.CONFIG_SUCCESS;
		} catch (Exception e) {
			throw e;
		} finally {
			super.clearLock(siteIdList);
			UiUtil.closeService_MB(elanInfoService);
			UiUtil.closeService_MB(etreeService);
			UiUtil.closeService_MB(pwNNIService);
		}
	}

	@SuppressWarnings("unused")
	private boolean beUsedByActivePw(Tunnel beforeTunnel) throws Exception {

		PwInfoService_MB pwInfoService = null;
		List<PwInfo> pwList = null;
		List<Integer> tunnelIds = null;
		try {
			pwInfoService = (PwInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PwInfo);

			tunnelIds = new ArrayList<Integer>();
			tunnelIds.add(beforeTunnel.getTunnelId());
			pwList = pwInfoService.selectPwInfoByTunnelId(tunnelIds);
			for (PwInfo pw : pwList) {
				if (pw.getPwStatus() == EActiveStatus.ACTIVITY.getValue()) {
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			throw e;
		} finally {
			UiUtil.closeService_MB(pwInfoService);
			pwList = null;
			tunnelIds = null;
		}

	}


	/**
	 * 根据网元id查询网元下的elan
	 * 
	 * @param siteId
	 *            网元id
	 * @return
	 * @throws Exception
	 */
	private List<ELanObject> getElanObject(EtreeInfoService_MB etreeService,ElanInfoService_MB elanInfoService,PwNniInfoService_MB pwNNIService ,int siteId) throws Exception {
		List<ELanObject> elanObjectList = null;
		Map<String, List<ElanInfo>> serviceIdAndElanListMap = null;
		Map<String, List<EtreeInfo>> ServiceIdAndEtreeListMap = null;
		List<String> removeServiceIdList = null;
		ELanObject elanObject = null;
		List<Integer> pwIdList = null;
		List<EtreeInfo> etreeInfos = null;
		ElanInfo elanInfo = null;
		 int vplsId = 0;//VSid
	     String acIds ="" ;
	     int type =0;//类型
	     int role = 0;//角色
		try {
			// 查询所有激活的elanInfo
			removeServiceIdList = new ArrayList<String>();
			serviceIdAndElanListMap = elanInfoService.selectSiteNodeBy(siteId);
			ServiceIdAndEtreeListMap = etreeService.selectNodeBySite(siteId);
			for(String str : ServiceIdAndEtreeListMap.keySet()){
				etreeInfos = ServiceIdAndEtreeListMap.get(str);
				List<ElanInfo> elanInfos = new ArrayList<ElanInfo>();
				for(EtreeInfo etreeInfo : etreeInfos){
					elanInfo = new ElanInfo();
					elanInfo.setId(etreeInfo.getId());
					if(etreeInfo.getRootSite() == siteId){
						elanInfo.setAxcId(etreeInfo.getaXcId());
						elanInfo.setZxcId(etreeInfo.getzXcId());
						elanInfo.setActiveStatus(etreeInfo.getActiveStatus());
						elanInfo.setaAcId(etreeInfo.getaAcId());
						elanInfo.setzAcId(etreeInfo.getzAcId());
						elanInfo.setAmostAcId(etreeInfo.getAmostAcId());
						elanInfo.setZmostAcId(etreeInfo.getZmostAcId());
						elanInfo.setaSiteId(etreeInfo.getRootSite());
						elanInfo.setzSiteId(etreeInfo.getBranchSite());
						elanInfo.setRole(1);
					}else{
						elanInfo.setAxcId(etreeInfo.getzXcId());
						elanInfo.setZxcId(etreeInfo.getaXcId());
						elanInfo.setActiveStatus(etreeInfo.getActiveStatus());
						elanInfo.setaAcId(etreeInfo.getzAcId());
						elanInfo.setzAcId(etreeInfo.getaAcId());
						elanInfo.setAmostAcId(etreeInfo.getZmostAcId());
						elanInfo.setZmostAcId(etreeInfo.getAmostAcId());
						elanInfo.setaSiteId(etreeInfo.getBranchSite());
						elanInfo.setzSiteId(etreeInfo.getRootSite());
					}
					elanInfo.setServiceType(etreeInfo.getServiceType());
					elanInfo.setServiceId(etreeInfo.getServiceId());
					elanInfo.setPwId(etreeInfo.getPwId());
					elanInfo.setServiceType(etreeInfo.getServiceType());
					elanInfo.setName(etreeInfo.getName());
					
					elanInfo.setIsSingle(etreeInfo.getIsSingle());
					elanInfo.setJobStatus(etreeInfo.getJobStatus());
					
					elanInfos.add(elanInfo);
				}
				serviceIdAndElanListMap.put(elanInfos.get(0).getServiceId()+"/"+elanInfos.get(0).getServiceType(), elanInfos);
			}
			for (List<ElanInfo> info : serviceIdAndElanListMap.values()) {
				for(ElanInfo elanInfo2 : info){
					if (elanInfo2.getActiveStatus() != EActiveStatus.ACTIVITY.getValue()) {
						removeServiceIdList.add(elanInfo2.getServiceId()+"/"+elanInfo2.getServiceType());
					}
				}
			}
			for (String serviceId : removeServiceIdList) {
				serviceIdAndElanListMap.remove(serviceId);
			}
			elanObjectList = new ArrayList<ELanObject>();
			
			for (String str : serviceIdAndElanListMap.keySet()) {
				List<ElanInfo> elanInfoList = serviceIdAndElanListMap.get(str);
				pwIdList = new ArrayList<Integer>();
				// 网元对应的pwIDList
				for (ElanInfo elan : elanInfoList) {
					if (elan.getaSiteId() == siteId || elan.getzSiteId() == siteId) {
						pwIdList.add(elan.getPwId());
					}
				}
				// 设置每一条业务的nni端口的仿真lanportId（1-64）和uni端口的lanportId（1-10）
//				configLanPortIdForEtreeService(pwNNIService,elanInfoList,siteId);
                if(elanInfoList.get(0).getaSiteId() == siteId){
                	vplsId = elanInfoList.get(0).getAxcId();
                	acIds = elanInfoList.get(0).getAmostAcId();
                }else if(elanInfoList.get(0).getzSiteId() == siteId){
                	vplsId = elanInfoList.get(0).getZxcId();
                	acIds = elanInfoList.get(0).getZmostAcId();
                }
                type = elanInfoList.get(0).getServiceType();
                role = elanInfoList.get(0).getRole();
            	elanObject = new ELanObject();
				elanObject.setVplsId(vplsId);
				configELANUNIObject(elanObject, acIds);
				configElanNNIObject(pwNNIService,elanObject, pwIdList, siteId);
				elanObject.setType(type);
				/*********************2015-2-11 张坤修改**************************************/
				elanObject.setRole(role);
				elanObjectList.add(elanObject);
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			serviceIdAndElanListMap = null;
			removeServiceIdList = null;
			elanObject = null;
			pwIdList = null;
		}
		return elanObjectList;
	}

	// 待修改
	private void configELANUNIObject(ELanObject elanObject, String acIds) {
		List<ETreeUNIObject> etreeUniObjList = new ArrayList<ETreeUNIObject>();
		ETreeUNIObject uniObj = null;
		AcPortInfoService_MB acSerivce = null;
		AcBufferService_MB uniBufService = null;
		QosInfoService_MB qosInfoService = null;
		List<Integer> acIdList = null;
		List<AcPortInfo> acPortInfoList = null;
		PortInst portInst = null;
		List<Acbuffer> bufList = null;
		List<VpwsBuffer> vpwsBufList = null;
		QosInfo qosInfo = null;
		List<QosInfo> qosInfoList = null;
		try {
			qosInfoList = new ArrayList<QosInfo>();
			acSerivce = (AcPortInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.AcInfo);
			uniBufService = (AcBufferService_MB) ConstantUtil.serviceFactory.newService_MB(Services.UniBuffer);
			qosInfoService = (QosInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.QosInfo);

			acIdList = new ArrayList<Integer>();
			
             if(null != acIds && !"".equals(acIds))
             {
            	 for(String acId : acIds.split(","))
            	 {
            		 acIdList.add(Integer.parseInt(acId.trim())); 
            	 }
             }
             acPortInfoList = acSerivce.select(acIdList);
			// 端口
			WhImplUtil whImplUtil = new WhImplUtil();
			if(acPortInfoList != null && acPortInfoList.size() >0)
			{
				for(AcPortInfo acPortInfo : acPortInfoList)
				{
					uniObj = new ETreeUNIObject();
					if (acPortInfo.getLagId() > 0) {
						whImplUtil.lagId(acPortInfo, uniObj);
						uniObj.setSlot(20);
					} else {
						portInst = whImplUtil.getportId(acPortInfo.getPortId(), acPortInfo.getSiteId());
						uniObj.setPort(portInst.getNumber());
						uniObj.setSlot(portInst.getSlotNumber());
					}

					// 简单qos
					qosInfoList = qosInfoService.getQosByObj(EServiceType.ACPORT.toString(),acPortInfo.getId());
					if (null != qosInfoList && !qosInfoList.isEmpty()) {
						qosInfo = qosInfoList.get(0);
					}
					// 细分流
					bufList = new ArrayList<Acbuffer>();
					bufList = uniBufService.select(acPortInfo.getId());
					vpwsBufList = getVpwsBufferList(bufList);
					elanObject.setMacCount(acPortInfo.getMacCount());
					uniObj.setLanPortId(acPortInfo.getLanId());
					if (null != qosInfo) {
						uniObj.setCbs(qosInfo.getCbs());
						uniObj.setCir(qosInfo.getCir()/1000);
						uniObj.setCm(qosInfo.getColorSence());
						uniObj.setPbs(qosInfo.getPbs());
						uniObj.setPir(qosInfo.getPir()/1000);
					}
					//给驱动的对象赋值，下拉列表存储code表主键。 所以通过主键查询value 	kk
					uniObj.setDownTpidUni(Integer.parseInt(UiUtil.getCodeById(acPortInfo.getDownTpid()).getCodeValue()));
					uniObj.setDownTagBehavior(Integer.parseInt(UiUtil.getCodeById(acPortInfo.getExitRule()).getCodeValue()));
					uniObj.setDownTagValnId(Integer.parseInt(acPortInfo.getVlanId()));
					uniObj.setDownTagValnPri(Integer.parseInt(acPortInfo.getVlanpri()));
					uniObj.setMacAddresslearn(Integer.parseInt(UiUtil.getCodeById(acPortInfo.getMacAddressLearn()).getCodeValue()));
					uniObj.setModel(Integer.parseInt(UiUtil.getCodeById(acPortInfo.getModel()).getCodeValue()));
					uniObj.setPortSplitHorizon(Integer.parseInt(UiUtil.getCodeById(acPortInfo.getHorizontalDivision()).getCodeValue()));
					uniObj.setUpTagRecognition(Integer.parseInt(UiUtil.getCodeById(acPortInfo.getTagAction()).getCodeValue()));
					uniObj.setVCTrafficPolicing(Integer.parseInt(UiUtil.getCodeById(acPortInfo.getManagerEnable()).getCodeValue()));
					uniObj.setVpwsBufferCount(vpwsBufList.size());
					uniObj.setVpwsBufferList(vpwsBufList);
					etreeUniObjList.add(uniObj);
				}
			}
			elanObject.setETreeUNIObjectList(etreeUniObjList);
			elanObject.setETreeUNIObjCount(etreeUniObjList.size());
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			UiUtil.closeService_MB(acSerivce);
			UiUtil.closeService_MB(uniBufService);
			UiUtil.closeService_MB(qosInfoService);
			etreeUniObjList = null;
			uniObj = null;
			acIdList = null;
			acPortInfoList = null;
			portInst = null;
			bufList = null;
			qosInfo = null;
			qosInfoList = null;
		}
	}


	// 待修改
	private void configElanNNIObject(PwNniInfoService_MB pwNniService,ELanObject eLanObject, List<Integer> pwIdList, int siteId) {
		List<ETreeNNIObject> eTreeNNIObjectList = new ArrayList<ETreeNNIObject>();
		ETreeNNIObject eTreeNNIObject = null;
		PwNniInfo pwnniInfo = null;
		List<PwNniInfo> pwNniInfoList = null;

		PwInfoService_MB pwInfoService = null;
		PwInfo pwInfo = null;
		try {
			pwInfoService = (PwInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PwInfo);

			pwNniInfoList = new ArrayList<PwNniInfo>();
			pwnniInfo = new PwNniInfo();
			for (Integer pwId : pwIdList) {
				pwnniInfo.setPwId(pwId);
				pwnniInfo.setSiteId(siteId);
				pwNniInfoList.addAll(pwNniService.select(pwnniInfo));
			}
			for (PwNniInfo nniInfo : pwNniInfoList) {
				pwInfo = new PwInfo();
				pwInfo.setPwId(nniInfo.getPwId());
				pwInfo = pwInfoService.selectBypwid_notjoin(pwInfo);
				eTreeNNIObject = new ETreeNNIObject();
				eTreeNNIObject.setDownTagRecognition(Integer.parseInt(UiUtil.getCodeById(nniInfo.getTagAction()).getCodeValue()));
				eTreeNNIObject.setLanPortId(nniInfo.getLanId()-10);
				eTreeNNIObject.setMacAddresslearn(Integer.parseInt(UiUtil.getCodeById(nniInfo.getMacAddressLearn()).getCodeValue()));
				eTreeNNIObject.setPortSplitHorizon(Integer.parseInt(UiUtil.getCodeById(nniInfo.getHorizontalDivision()).getCodeValue()));
				eTreeNNIObject.setControlEnabl(Integer.parseInt(UiUtil.getCodeById(nniInfo.getControlEnable()).getCodeValue()));
				if (siteId == pwInfo.getASiteId()) {
					eTreeNNIObject.setPwID(pwInfo.getApwServiceId());
				} else if (siteId == pwInfo.getZSiteId()) {
					eTreeNNIObject.setPwID(pwInfo.getZpwServiceId());
				}
				eTreeNNIObject.setDownTpidNni(Integer.parseInt(UiUtil.getCodeById(nniInfo.getTpid()).getCodeValue()));
				eTreeNNIObject.setUpTagBehavior(Integer.parseInt(UiUtil.getCodeById(nniInfo.getExitRule()).getCodeValue()));
				eTreeNNIObject.setUpTagValnId(Integer.parseInt(nniInfo.getSvlan()));
				eTreeNNIObject.setUpTagValnPri(Integer.parseInt(nniInfo.getVlanpri()));
				eTreeNNIObjectList.add(eTreeNNIObject);
			}
			eLanObject.setETreeNNIObjectList(eTreeNNIObjectList);
			eLanObject.setETreeNNIObjCount(eTreeNNIObjectList.size());

		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			eTreeNNIObject = null;
			UiUtil.closeService_MB(pwInfoService);
			pwnniInfo = null;
			pwNniInfoList = null;
		}
	}

	private void configLanPortIdForEtreeService(PwNniInfoService_MB pwNNIService,List<ElanInfo> elanInfoList,int siteId) throws Exception {
		Map<Integer, Integer> acIdAndLanPortIdNumberMap = new HashMap<Integer, Integer>();
		Map<Integer, Integer> pwNNIandLanPortIdNumberMap = new HashMap<Integer, Integer>();
		Set<Integer> acIdSet = new HashSet<Integer>();
		List<Integer> pwIdList = new ArrayList<Integer>();
		PwNniInfo pwNNIInfo = null;
		List<PwNniInfo> pwNNIInfoList = null;
		UiUtil uiUtil = null;
		try {
			int count = 1;
			uiUtil = new UiUtil();
			for (ElanInfo elanInfo : elanInfoList) {
				
				acIdSet.addAll(uiUtil.getAcIdSets(elanInfo.getAmostAcId()));
				acIdSet.addAll(uiUtil.getAcIdSets(elanInfo.getZmostAcId()));
				pwIdList.add(elanInfo.getPwId());
			}
			// uni
			for (Integer acId : acIdSet) {
				acIdAndLanPortIdNumberMap.put(acId, count);
				count++;
			}
			// nni
			pwNNIInfoList = new ArrayList<PwNniInfo>();
			for (Integer pwId : pwIdList) {
				pwNNIInfo = new PwNniInfo();
				pwNNIInfo.setPwId(pwId);
				pwNNIInfo.setSiteId(siteId);
				pwNNIInfoList.addAll(pwNNIService.select(pwNNIInfo));
			}
			count = 1;
			for (PwNniInfo pwNNI : pwNNIInfoList) {
				pwNNIandLanPortIdNumberMap.put(pwNNI.getId(), count);
				count++;
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			acIdAndLanPortIdNumberMap = null;
			pwNNIandLanPortIdNumberMap = null;
			acIdSet = null;
			pwIdList = new ArrayList<Integer>();
			pwNNIInfo = null;
			pwNNIInfoList = null;
			uiUtil = null;
		}

	}

	private List<VpwsBuffer> getVpwsBufferList(List<Acbuffer> bufList) {
		List<VpwsBuffer> vpwsBufferList = new ArrayList<VpwsBuffer>();
		VpwsBuffer vpwsbuf = null;
		int i = 0;
		try {
			for (Acbuffer buf : bufList) {
				vpwsbuf = new VpwsBuffer();
				i++;
				vpwsbuf.set_802_1P(buf.getEightIp());
				vpwsbuf.setBufferEnable(buf.getBufferEnable());
				vpwsbuf.setCbs(buf.getCbs());
				vpwsbuf.setCir(buf.getCir()/1000);
				vpwsbuf.setCm(buf.getCm());
				vpwsbuf.setIpDscp(buf.getIpDscp());
				vpwsbuf.setModel(buf.getModel());
				vpwsbuf.setPbs(buf.getPbs());
				vpwsbuf.setPhb(Integer.parseInt(UiUtil.getCodeById(buf.getPhb()).getCodeValue()));
				vpwsbuf.setPir(buf.getPir()/1000);
				vpwsbuf.setPwLable(20);
				vpwsbuf.setSourceIP(buf.getSourceIp());
				vpwsbuf.setSourceMac(buf.getSourceMac());
				vpwsbuf.setStrategy(buf.getStrategy());
				vpwsbuf.setTargetIP(buf.getTargetIp());
				vpwsbuf.setTargetMac(buf.getTargetMac());
				vpwsbuf.setVlanId(buf.getVlanId());
				vpwsbuf.setVpwsBufferID(i);
				vpwsbuf.setSourceTcpPortId(buf.getSourceTcpPortId());
				vpwsbuf.setEndTcpPortId(buf.getEndTcpPortId());
				vpwsbuf.setIPTOS(buf.getIPTOS());
				vpwsbuf.setPortType(buf.getPortType());
				vpwsBufferList.add(vpwsbuf);
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		
		return vpwsBufferList;
	}

	/**
	 * 获取port对象
	 * 
	 * @param portId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
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
	 * @param elanInfoList
	 *            elan对象
	 * @return siteID集合
	 * @throws Exception
	 */
	private List<Integer> getSiteIds(List<ElanInfo> elanInfoList) throws Exception {
		List<Integer> siteIds = null;
		try {
			siteIds = new ArrayList<Integer>();
			for (ElanInfo elan : elanInfoList) {
				if (elan.getaSiteId()!=0 && !siteIds.contains(elan.getaSiteId()) 
						&& super.getManufacturer(elan.getaSiteId()) == EManufacturer.WUHAN.getValue()
						) {
					siteIds.add(elan.getaSiteId());
				}
				if (elan.getzSiteId() !=0 && !siteIds.contains(elan.getzSiteId()) 
						&& super.getManufacturer(elan.getzSiteId()) == EManufacturer.WUHAN.getValue()
						) {
					siteIds.add(elan.getzSiteId());
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return siteIds;
	}
	
	
	/**
	 * 与设备同步elan业务,同时同步elan所占用的ac端口信息,以及pw信息
	 * 
	 * @author guoqc
	 * @param siteId
	 * @return
	 * @exception
	 */
	public Object synchro(int siteId) throws Exception {
		OperationObject operaObj = new OperationObject();
		ElanInfoService_MB elanInfoService = null;
		try {
			operaObj = this.getSynchroOperationObject(siteId);// 封装下发数据
			super.sendAction(operaObj);// 下发数据
			super.verification(operaObj);// 验证是否下发成功
			if (operaObj.isSuccess()) {
				/* 成功，则与数据库进行同步 */
				elanInfoService = (ElanInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.ElanInfo);
				for (ActionObject actionObject : operaObj.getActionObjectList()) {
					elanInfoService.updateStatusActivate(siteId, EActiveStatus.UNACTIVITY.getValue());
					this.synchro_db(elanInfoService,actionObject.getEtreeObjectList(), siteId);
				}
				
				return ResultString.CONFIG_SUCCESS;
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}finally{
			UiUtil.closeService_MB(elanInfoService);
		}
		return "超时";
	}

	/**
	 * 封装下发数据
	 * 
	 * @param siteId
	 * @return operationObject
	 * @throws Exception
	 */
	private OperationObject getSynchroOperationObject(int siteId) throws Exception {
		OperationObject operationObject = null;
		ActionObject actionObject = null;
		NEObject neObject = null;
		try {
			operationObject = new OperationObject();
			WhImplUtil whImplUtil = new WhImplUtil();
			neObject = whImplUtil.siteIdToNeObject(siteId);
			actionObject = new ActionObject();
			actionObject.setActionId(super.getActionId(operationObject.getActionObjectList()));
			actionObject.setNeObject(neObject);
			actionObject.setType("elanSynchro");
			operationObject.getActionObjectList().add(actionObject);
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			actionObject = null;
		}
		return operationObject;
	}

	/**
	 * 
	 * @param elanObejctList
	 * @param siteId
	 * @throws Exception
	 */
	private void synchro_db(ElanInfoService_MB elanInfoService,List<ETreeObject> elanObjectList, int siteId) throws Exception {
		List<ElanInfo> elanInfoList = null;
		List<AcPortInfo> acPortInfos = null;
		List<PwNniInfo> infos = null;
		int serviceId = 1;
		List<Integer> acIdList = null;
		AcPortInfoService_MB acInfoService = null;
		try {
			acInfoService = (AcPortInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.AcInfo);
			SynchroUtil synchroUtil = new SynchroUtil();
			for(ETreeObject eLanObject : elanObjectList){
				acIdList = new ArrayList<Integer>();
				try {
					if(eLanObject.getType() == 2){
						try {
							acPortInfos = this.getAcPortInfo(eLanObject, siteId);
							for(AcPortInfo acPortInfo : acPortInfos){
								int  acId = synchroUtil.acPortInfoSynchro(acPortInfo, siteId);
								if(acId > 0 && !acIdList.contains(acId)){
									acIdList.add(acId);
								}
							}
						} catch (Exception e) {
							ExceptionManage.dispose(e, getClass());
							continue;
						}
						//业务
						try {
							elanInfoList = this.getElanInfo(eLanObject, siteId,serviceId,acIdList);
							synchroUtil.elanSynchro(elanInfoService,elanInfoList, siteId);
						} catch (Exception e) {
							ExceptionManage.dispose(e, getClass());
//							acInfoService.updateState(acIdList);
							continue;
						}
						infos = this.getPwNniInfo(eLanObject, siteId);
						for(PwNniInfo pwNniInfo : infos){
							synchroUtil.pwNniBufferInfoSynchro(pwNniInfo, siteId, true);
						}
						serviceId++;
					}	
				} catch (Exception e) {
					ExceptionManage.dispose(e, getClass());
				}
			
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}finally{
			UiUtil.closeService_MB(acInfoService);
		}

	}

	/**
	 * 
	 * @param elanObjectList
	 * @param siteId
	 * @return
	 * @throws Exception
	 */
	private List<ElanInfo> getElanInfo(ETreeObject elanObject, int siteId,int serviceId,List<Integer> acIdList) throws Exception {
		List<ElanInfo> elanInfoList = new ArrayList<ElanInfo>();
		PwInfoService_MB pwInfoService = null;
		PwInfo pwinfo = null;
		String acIds = "";
		try {
			
			pwInfoService = (PwInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PwInfo);
			acIds = acIdList.toString();
			for (ETreeNNIObject nniObject : elanObject.getETreeNNIObjectList()) {
				ElanInfo elanInfo = new ElanInfo();
				elanInfo.setAxcId(elanObject.getVplsId());
				elanInfo.setaSiteId(siteId);
				elanInfo.setServiceId(serviceId);
//				elanInfo.setaAcId(acId);
				elanInfo.setAmostAcId(acIds.subSequence(1, acIds.length() -1).toString());
				pwinfo = pwInfoService.select(siteId, nniObject.getPwID());	
				elanInfo.setPwId(pwinfo.getPwId());
				elanInfo.setServiceType(EServiceType.ELAN.getValue());
				elanInfo.setName("elan"+super.getNowMS());
				elanInfo.setActiveStatus(EActiveStatus.ACTIVITY.getValue());
				elanInfo.setIsSingle(1);
				elanInfoList.add(elanInfo);
			}
		} catch (Exception e) {
			throw e;
		}finally{
			UiUtil.closeService_MB(pwInfoService);
		}
		return elanInfoList;
	}

	/**
	 * 
	 * @param elanObject
	 * @param siteId
	 * @return
	 * @throws Exception
	 */
	private List<PwNniInfo> getPwNniInfo(ETreeObject eLanObject, int siteId) throws Exception {
		List<PwNniInfo> pwNniInfoList = new ArrayList<PwNniInfo>();
		PwInfoService_MB pwInfoService = null;
		PwInfo pwinfo = null;
		
		try {
			pwInfoService = (PwInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PwInfo);
			for (ETreeNNIObject nniObj : eLanObject.getETreeNNIObjectList()) {
				PwNniInfo pwNniInfo = new PwNniInfo();
				int lanId=nniObj.getLanPortId()+10;
				pwNniInfo.setLanId(lanId);
				pwNniInfo.setSiteId(siteId);
				pwinfo = pwInfoService.select(siteId, nniObj.getPwID());
				pwNniInfo.setPwId(pwinfo.getPwId());
				pwNniInfo.setTagAction(UiUtil.getCodeByValue("TAGRECOGNITION", nniObj.getDownTagRecognition()+"").getId());
				pwNniInfo.setExitRule(UiUtil.getCodeByValue("PORTTAGBEHAVIOR", nniObj.getUpTagBehavior()+"").getId());
				pwNniInfo.setSvlan(nniObj.getUpTagValnId()+"");
				pwNniInfo.setVlanpri(nniObj.getUpTagValnPri()+"");
				pwNniInfo.setMacAddressLearn(UiUtil.getCodeByValue("MACLEARN", nniObj.getMacAddresslearn()+"").getId());
				pwNniInfo.setHorizontalDivision(UiUtil.getCodeByValue("VCTRAFFICPOLICING", nniObj.getPortSplitHorizon()+"").getId());
				pwNniInfo.setControlEnable(UiUtil.getCodeByValue("ENABLEDSTATUE", nniObj.getControlEnabl() + "").getId());
				pwNniInfoList.add(pwNniInfo);
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			UiUtil.closeService_MB(pwInfoService);
		}

		return pwNniInfoList;
	}

	/**
	 * 
	 * @param elanObject
	 * @param siteId
	 * @return
	 * @throws Exception
	 */
	private List<AcPortInfo> getAcPortInfo(ETreeObject eTreeObject, int siteId) throws Exception {
		List<AcPortInfo> acPortInfoList = new ArrayList<AcPortInfo>();
		AcPortInfo acPortInfo = null;
		PortInst portInst = null;
		PortService_MB portService = null;
		PortLagService_MB portLagService = null;
		QosInfo simpleQos = null;
		try {
			portService = (PortService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORT);
			portLagService = (PortLagService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORTLAG);
			for (int i = 0; i < eTreeObject.getETreeUNIObjectList().size(); i++) {
				ETreeUNIObject uniObj = eTreeObject.getETreeUNIObjectList().get(i);
				int portModel = 0;
				acPortInfo = new AcPortInfo();
				if (uniObj.getPort() < 100) {
					
					portInst = new PortInst();
					portInst.setNumber(uniObj.getPort());
					portInst.setSiteId(siteId);
					portInst = portService.select(portInst).get(0);
					acPortInfo.setPortId(portInst.getPortId());
					portModel = portInst.getPortAttr().getPortUniAttr().getVlanRelevance() == 51?1:0;
				} else if (uniObj.getPort() != 201) {
					
					PortLagInfo portLagInfo = new PortLagInfo();
					portLagInfo.setSiteId(siteId);
					portLagInfo.setLagID(uniObj.getPort() - 100);
					portLagInfo = portLagService.selectLAGByCondition(portLagInfo).get(0);
					acPortInfo.setLagId(portLagInfo.getId());
				}
				acPortInfo.setLanId(uniObj.getLanPortId());
				acPortInfo.setMacCount(eTreeObject.getMacCount());
				acPortInfo.setSiteId(siteId);
				acPortInfo.setModel(UiUtil.getCodeByValue("UNIPORTMODE", uniObj.getModel()+"").getId());
				simpleQos = new QosInfo();
				simpleQos.setPir(uniObj.getPir()*1000);
				simpleQos.setCir(uniObj.getCir()*1000);
				simpleQos.setCbs(uniObj.getCbs());
				simpleQos.setPbs(uniObj.getPbs());
				simpleQos.setCos(5);
				simpleQos.setQosType("L2");
				acPortInfo.setTagAction(UiUtil.getCodeByValue("TAGRECOGNITION", uniObj.getUpTagRecognition()+"").getId());
				acPortInfo.setExitRule(UiUtil.getCodeByValue("PORTTAGBEHAVIOR", uniObj.getDownTagBehavior()+"").getId());
				acPortInfo.setVlanId(uniObj.getDownTagValnId()+"");
				acPortInfo.setVlanpri(uniObj.getDownTagValnPri()+"");
				acPortInfo.setManagerEnable(UiUtil.getCodeByValue("VCTRAFFICPOLICING", uniObj.getVCTrafficPolicing()+"").getId());
				acPortInfo.setHorizontalDivision(UiUtil.getCodeByValue("VCTRAFFICPOLICING", uniObj.getPortSplitHorizon() + "").getId());
				acPortInfo.setMacAddressLearn(UiUtil.getCodeByValue("MACLEARN", uniObj.getMacAddresslearn()+"").getId());
				acPortInfo.setName("ac_elan" +(i+1)+"-"+eTreeObject.getVplsId()+"_"+super.getNowMS());
//					 acPortInfo.setBufType(uniObj);
				acPortInfo.setBufferList(getacbuffer(uniObj, uniObj.getPort(), siteId));
				acPortInfo.setModel(UiUtil.getCodeByValue("MODEL", uniObj.getModel() + "").getId());
				acPortInfo.setPortModel(UiUtil.getCodeByValue("UNIPORTMODE", portModel+"").getId());
				acPortInfo.setSimpleQos(simpleQos);
				acPortInfo.setAcStatus(EActiveStatus.ACTIVITY.getValue());
				acPortInfo.setIsUser(1);
				acPortInfoList.add(acPortInfo);
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			UiUtil.closeService_MB(portService);
			UiUtil.closeService_MB(portLagService);
		}
		return acPortInfoList;
	}
	/**
	 * 
	 * @param uniObj
	 * @param portId
	 * @return
	 * @throws Exception
	 */
	private List<Acbuffer> getacbuffer(ETreeUNIObject uniObj, int portNumber, int siteId) throws Exception {
		List<Acbuffer> acbufferList = new ArrayList<Acbuffer>();
		Acbuffer acbuffer = new Acbuffer();
		for (VpwsBuffer VpwsBuffer : uniObj.getVpwsBufferList()) {
			try {
				acbuffer.setBufferEnable(VpwsBuffer.getBufferEnable());
				acbuffer.setVlanId(VpwsBuffer.getVlanId());
				acbuffer.setSourceMac(VpwsBuffer.getSourceMac());
				acbuffer.setTargetMac(VpwsBuffer.getTargetMac());
				acbuffer.setEightIp(VpwsBuffer.get_802_1P());
				acbuffer.setSourceIp(VpwsBuffer.getSourceIP());
				acbuffer.setTargetIp(VpwsBuffer.getTargetIP());
				acbuffer.setIpDscp(VpwsBuffer.getIpDscp());
				acbuffer.setModel(VpwsBuffer.getModel());
				acbuffer.setCir(VpwsBuffer.getCir()*1000);
				acbuffer.setPir(VpwsBuffer.getPir()*1000);
				acbuffer.setCm(VpwsBuffer.getCm());
				acbuffer.setCbs(VpwsBuffer.getCbs());
				acbuffer.setPbs(VpwsBuffer.getPbs());
				acbuffer.setStrategy(VpwsBuffer.getStrategy());
				acbuffer.setPhb(UiUtil.getCodeByValue("CONRIRMPHB", VpwsBuffer.getPhb() + "").getId());
				acbuffer.setSourceTcpPortId(VpwsBuffer.getSourceTcpPortId());
				acbuffer.setEndTcpPortId(VpwsBuffer.getEndTcpPortId());
				acbuffer.setIPTOS(VpwsBuffer.getIPTOS());
				acbuffer.setPortType(VpwsBuffer.getPortType());
				acbufferList.add(acbuffer);
			} catch (Exception e) {
				ExceptionManage.dispose(e,this.getClass());
			}
		}
		return acbufferList;
	}

	public List<ElanInfo> consistence(int siteId) {
		List<ElanInfo> elanList = new ArrayList<ElanInfo>();
		try {
			OperationObject operaObj = this.getSynchroOperationObject(siteId);// 封装下发数据
			super.sendAction(operaObj);// 下发数据
			super.verification(operaObj);// 验证是否下发成功
			if (operaObj.isSuccess()) {
				for (ActionObject actionObject : operaObj.getActionObjectList()) {
					elanList = this.getELanInfoList(actionObject.getEtreeObjectList(), siteId);
					return elanList;
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return elanList;
	}

	private List<ElanInfo> getELanInfoList(List<ETreeObject> elanObjectList, int siteId) {
		List<ElanInfo> elanList = new ArrayList<ElanInfo>();
		int serviceId = 1;
		List<Integer> acIds = new ArrayList<Integer>();
		for (ETreeObject elanObject : elanObjectList) {
			try {
				if (elanObject.getType() == 2) {
					List<AcPortInfo> acList = this.getAcPortInfo(elanObject, siteId);
					List<ElanInfo> elanInfoList = this.getElanInfo(elanObject, siteId, serviceId,acIds);
					List<PwNniInfo> pwNniList = this.getPwNniInfo(elanObject, siteId);
					ElanInfo elanInfo = elanInfoList.get(0);
					elanInfo.getAcPortList().addAll(acList);
					elanInfo.getPwNniList().addAll(pwNniList);
					elanList.add(elanInfo);
				}
			} catch (Exception e) {
				ExceptionManage.dispose(e, this.getClass());
			}
			serviceId++;
		}
		return elanList;
	}
}