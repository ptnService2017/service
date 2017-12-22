package com.nms.service.impl.wh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nms.db.bean.equipment.port.PortInst;
import com.nms.db.bean.ptn.CccInfo;
import com.nms.db.bean.ptn.port.AcPortInfo;
import com.nms.db.bean.ptn.port.Acbuffer;
import com.nms.db.bean.ptn.port.PortLagInfo;
import com.nms.db.bean.ptn.qos.QosInfo;
import com.nms.db.enums.EActiveStatus;
import com.nms.db.enums.EServiceType;
import com.nms.drive.service.bean.CccObject;
import com.nms.drive.service.bean.CccUNIObject;
import com.nms.drive.service.bean.NEObject;
import com.nms.drive.service.bean.VpwsBuffer;
import com.nms.model.equipment.port.PortService_MB;
import com.nms.model.ptn.CccService_MB;
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

public class CccWHServiceImpl extends WHOperationBase implements OperationServiceI {

	private Map<Integer, Integer> acIdAndLanPortNumberMap = null;

	@Override
	public String excutionDelete(List objectList) throws Exception {
		List<CccInfo> cccInfoList = null;
		List<Integer> siteIdList = null;
		OperationObject operationObjectAfter = null;
		OperationObject operationObjectResult = null;
		try {
			cccInfoList = objectList;
			siteIdList = new ArrayList<Integer>();
			siteIdList.add(cccInfoList.get(0).getaSiteId());
			if (siteIdList.size() > 0) {
				if (super.isLockBySiteIdList(siteIdList)) {
					return ResourceUtil.srcStr(StringKeysTip.TIP_SITE_LOCK);
				}		
				super.lockSite(siteIdList, SiteLockTypeUtil.CCC_DELETE);
				SiteUtil siteUtil=new SiteUtil();
				for (Integer siteId : siteIdList) {
					try {
						if ("0".equals(siteUtil.SiteTypeUtil(siteId) + "")) {
							operationObjectAfter = this.getOperationObject(siteId);
							super.sendAction(operationObjectAfter);
							operationObjectResult = super.verification(operationObjectAfter);
							if (!operationObjectResult.isSuccess()) {
								return super.getErrorMessage(operationObjectResult);
							}
						}
					} catch (Exception e) {
						return ResultString.CONFIG_FAILED;
					}
				}
			}
			super.clearLock(siteIdList);
			return ResultString.CONFIG_SUCCESS;
		} catch (Exception e) {
			throw e;
		} finally {
			super.clearLock(siteIdList);
			cccInfoList = null;
			siteIdList = null;
			operationObjectAfter = null;
			operationObjectResult = null;
		}

	}

	@Override
	public String excutionInsert(Object object) throws Exception {
		List<CccInfo> cccInfoList = new ArrayList<CccInfo>();
		CccInfo cccInfo = null;
		List<Integer> siteIdList = null;
		OperationObject operationObjectAfter = null;
		OperationObject operationObjectResult = null;
		try {
			cccInfo = (CccInfo)object;
			cccInfoList.add(cccInfo);
			siteIdList = new ArrayList<Integer>();
			siteIdList.add(cccInfoList.get(0).getaSiteId());
			if (siteIdList.size() > 0) {
				if (super.isLockBySiteIdList(siteIdList)) {
					return ResourceUtil.srcStr(StringKeysTip.TIP_SITE_LOCK);
				}				
				super.lockSite(siteIdList, SiteLockTypeUtil.CCC_INSERT);
				SiteUtil siteUtil=new SiteUtil();
				for (Integer siteId : siteIdList) {
					try {
						if ("0".equals(siteUtil.SiteTypeUtil(siteId) + "")) {
							operationObjectAfter = this.getOperationObject(siteId);
							super.sendAction(operationObjectAfter);
							operationObjectResult = super.verification(operationObjectAfter);
							if (!operationObjectResult.isSuccess()) {
								return super.getErrorMessage(operationObjectResult);
							}
						}
					} catch (Exception e) {
						return ResultString.CONFIG_FAILED;
					}
				}
			}
				return ResultString.CONFIG_SUCCESS;
			} catch (Exception e1) {
				throw e1;
			} finally {
				super.clearLock(siteIdList);
				cccInfoList = null;
				siteIdList = null;
				operationObjectAfter = null;
				operationObjectResult = null;

			}
		}
			
		
			
   /**
     * 获取operationobject对象
	 * 
	 * @param siteIdList
	 *            网元ID
	 * @return
	 * @throws Exception
	 */
				
     private OperationObject getOperationObject(int siteId) throws Exception {
		  OperationObject operationObject = null;
		  ActionObject actionObject = null;
		  NEObject neObject = null;
		  try {
			   operationObject = new OperationObject();
			   WhImplUtil whImplUtil = new WhImplUtil();
			   actionObject = new ActionObject();
			   neObject = whImplUtil.siteIdToNeObject(siteId);
			   actionObject.setActionId(super.getActionId(operationObject.getActionObjectList()));
			   actionObject.setNeObject(neObject);
			   actionObject.setType("ccc");
			   actionObject.setCccObjectList(this.getCccInfoObject(siteId));
			   operationObject.getActionObjectList().add(actionObject);
		   } catch (Exception e) {
				ExceptionManage.dispose(e,this.getClass());
		   } finally {
				actionObject = null;
				neObject = null;
			}
			return operationObject;
								
	}
			
			
			
			
	/**
	 * 根据网元ID查询CccObject
	 * 
	 * @param siteId
	 * @return
	 * @throws Exception
	 */
	private List<CccObject> getCccInfoObject(int siteId) throws Exception {
		CccService_MB cccService = null;
		List<CccInfo> cccInfoList = null;
		CccObject cccObject = null;
		CccInfo cccInfo = null;
		List<CccObject> cccObjectList = null;
		List<CccInfo> activeList = null;				
		try {
			cccService = (CccService_MB) ConstantUtil.serviceFactory.newService_MB(Services.CCCMANAGEMENT);								
			// 查询所有激活的ccc
			cccInfo = new CccInfo();
			cccInfo.setActiveStatus(EActiveStatus.ACTIVITY.getValue());
			cccInfoList = cccService.selectNodeBySite(siteId);
			cccObjectList = new ArrayList<CccObject>();
			activeList = new ArrayList<CccInfo>();
			for (CccInfo active : cccInfoList) {// 获取所有激活的ccc
				if (active.getActiveStatus() == EActiveStatus.ACTIVITY.getValue())
					activeList.add(active);
			}
			
			
			// 设置每一条业务的nni端口的仿真lanportId（1-64）和uni端口的lanportId（1-10）
			configLanPortIdForCccService(activeList, siteId);
			for (CccInfo ccc : activeList) {						
				cccObject = new CccObject();
				cccObject.setVplsId(ccc.getaXcId());
				configCccUNIObject(cccObject,ccc.getAmostAcId());
				cccObjectList.add(cccObject);
				} 
			
			acIdAndLanPortNumberMap = null;
		} catch (Exception e) {
			throw e;
		} finally {
			UiUtil.closeService_MB(cccService);
		}
		return cccObjectList;
	}


	@Override
	public String excutionUpdate(Object object) throws Exception {
		CccInfo cccInfo = null;
		List<Integer> siteIdList = null;
		OperationObject operationObjectAfter = null;
		OperationObject operationObjectResult = null;
		try {
			cccInfo = (CccInfo)object;
			siteIdList = new ArrayList<Integer>();
			siteIdList.add(cccInfo.getaSiteId());
			if (siteIdList.size() > 0) {
				if (super.isLockBySiteIdList(siteIdList)) {
					return ResourceUtil.srcStr(StringKeysTip.TIP_SITE_LOCK);
				}
				super.lockSite(siteIdList, SiteLockTypeUtil.CCC_DELETE);
				SiteUtil siteUtil=new SiteUtil();		
				for(Integer siteId : siteIdList){
					if("0".equals(siteUtil.SiteTypeUtil(siteId)+"")){////非失连网元、非虚拟网元下发设备
						operationObjectAfter = this.getOperationObject(siteId);
						super.sendAction(operationObjectAfter);
						operationObjectResult = super.verification(operationObjectAfter);
						if (!operationObjectResult.isSuccess()) {
							return super.getErrorMessage(operationObjectResult);
						}
					}
				}
			}
			super.clearLock(siteIdList);
			return ResultString.CONFIG_SUCCESS;
		} catch (Exception e) {
			throw e;
		} finally {
			super.clearLock(siteIdList);			
			siteIdList = null;
			operationObjectAfter = null;
			operationObjectResult = null;
		}
	}




	private void configLanPortIdForCccService(List<CccInfo> cccInfoList, int siteId) throws Exception {
		Map<Integer, Integer> acIdAndLanPortIdNumberMap = new HashMap<Integer, Integer>();	
		Set<Integer> acIdSet = new HashSet<Integer>();	
		UiUtil uiUtil = null;
		try {
			uiUtil = new UiUtil();
			int count = 1;
			for (CccInfo cccInfo : cccInfoList) {
				acIdSet.addAll(uiUtil.getAcIdSets(cccInfo.getAmostAcId()));				
			}
			// uni
			for (Integer acId : acIdSet) {
				acIdAndLanPortIdNumberMap.put(acId, count);
				count++;
			}				
			this.acIdAndLanPortNumberMap = acIdAndLanPortIdNumberMap;			
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			acIdAndLanPortIdNumberMap = null;			
			acIdSet = null;
		}

	}


	private void configCccUNIObject(CccObject cccObject, String acIds) {
		List<CccUNIObject> cccUniObjList = new ArrayList<CccUNIObject>();
		CccUNIObject uniObj = null;
		AcPortInfoService_MB acSerivce = null;
		AcBufferService_MB uniBufService = null;
		QosInfoService_MB qosInfoService = null;
		List<Integer> acIdList = null;
//		AcPortInfo acPortInfo = null;
		PortService_MB portService = null;
		PortInst portInst = null;
		List<Acbuffer> bufList = null;
		List<VpwsBuffer> vpwsBufList = null;
		QosInfo qosInfo = null;
		List<QosInfo> qosInfoList = null;
		List<AcPortInfo> acPortInfoList = null;
		try {
			qosInfoList = new ArrayList<QosInfo>();
			acSerivce = (AcPortInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.AcInfo);
			uniBufService = (AcBufferService_MB) ConstantUtil.serviceFactory.newService_MB(Services.UniBuffer);
			qosInfoService = (QosInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.QosInfo);
			portService = (PortService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORT);

			acIdList = new ArrayList<Integer>();
			  if(null != acIds && !"".equals(acIds))
	             {
	            	 for(String acId : acIds.split(","))
	            	 {
	            		 acIdList.add(Integer.parseInt(acId.trim())); 
	            	 }
	             }
//			acPortInfo = acSerivce.select(acIdList).get(0);
			 acPortInfoList = acSerivce.select(acIdList);
			WhImplUtil whImplUtil = new WhImplUtil();
			
			if(acPortInfoList != null && acPortInfoList.size()>0)
			{
				for(AcPortInfo acPortInst: acPortInfoList)
				{
					uniObj = new CccUNIObject();
					// 端口
					if (acPortInst.getLagId() > 0) {
						whImplUtil.lagId(acPortInst, uniObj);
						uniObj.setSlot(20);
					} else {
						portInst = whImplUtil.getportId(acPortInst.getPortId(), acPortInst.getSiteId());
						uniObj.setPort(portInst.getNumber());
						uniObj.setSlot(portInst.getSlotNumber());
					}
					// 简单qos
					qosInfoList = qosInfoService.getQosByObj(EServiceType.ACPORT.toString(), acPortInst.getId());
					if (null != qosInfoList && !qosInfoList.isEmpty()) {
						qosInfo = qosInfoList.get(0);
					}
					// 细分流
					bufList = new ArrayList<Acbuffer>();
					bufList = uniBufService.select(acPortInst.getId());
					vpwsBufList = getVpwsBufferList(bufList);
					
					uniObj.setLanPortId(this.acIdAndLanPortNumberMap.get(acPortInst.getId()));
					if (null != qosInfo) {
						uniObj.setCbs(qosInfo.getCbs());
						uniObj.setCir(qosInfo.getCir()/1000);
						uniObj.setCm(qosInfo.getColorSence());
						uniObj.setPbs(qosInfo.getPbs());
						uniObj.setPir(qosInfo.getPir()/1000);
					}
					cccObject.setMacCount(acPortInst.getMacCount());
					uniObj.setDownTagBehavior(Integer.parseInt(UiUtil.getCodeById(acPortInst.getExitRule()).getCodeValue()));
					uniObj.setDownTagValnId(Integer.parseInt(acPortInst.getVlanId()));
					uniObj.setDownTagValnPri(Integer.parseInt(acPortInst.getVlanpri()));
					uniObj.setMacAddresslearn(Integer.parseInt(UiUtil.getCodeById(acPortInst.getMacAddressLearn()).getCodeValue()));
					uniObj.setModel(Integer.parseInt(UiUtil.getCodeById(acPortInst.getModel()).getCodeValue()));
					uniObj.setPortSplitHorizon(Integer.parseInt(UiUtil.getCodeById(acPortInst.getHorizontalDivision()).getCodeValue()));
					uniObj.setUpTagRecognition(Integer.parseInt(UiUtil.getCodeById(acPortInst.getTagAction()).getCodeValue()));
					uniObj.setVCTrafficPolicing(Integer.parseInt(UiUtil.getCodeById(acPortInst.getManagerEnable()).getCodeValue()));
					uniObj.setVpwsBufferCount(vpwsBufList.size());
					uniObj.setVpwsBufferList(vpwsBufList);
					cccUniObjList.add(uniObj);
				}
			}
			cccObject.setCccUNIObjectList(cccUniObjList);
			cccObject.setCccUNIObjCount(cccUniObjList.size());
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			UiUtil.closeService_MB(acSerivce);
			UiUtil.closeService_MB(portService);
			UiUtil.closeService_MB(uniBufService);
			UiUtil.closeService_MB(qosInfoService);
			uniObj = null;
			acIdList = null;
			portInst = null;
			bufList = null;
			vpwsBufList = null;
			qosInfo = null;
			qosInfoList = null;
			acPortInfoList = null;
		}
	}
	
	private List<VpwsBuffer> getVpwsBufferList(List<Acbuffer> bufList) {
		List<VpwsBuffer> vpwsBufferList = new ArrayList<VpwsBuffer>();
		VpwsBuffer vpwsbuf = null;
		int i = 0;
		try {
			for (Acbuffer buf : bufList) {
				i++;
				vpwsbuf = new VpwsBuffer();
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
				vpwsbuf.setVpwsBufferID(i);
				vpwsbuf.setSourceIP(buf.getSourceIp());
				vpwsbuf.setSourceMac(buf.getSourceMac());
				vpwsbuf.setStrategy(buf.getStrategy());
				vpwsbuf.setTargetIP(buf.getTargetIp());
				vpwsbuf.setTargetMac(buf.getTargetMac());
				vpwsbuf.setVlanId(buf.getVlanId());
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
	 * 与设备同步ccc业务,同时同步ccc所占用的ac端口信息
	 * 
	 * @author guoqc
	 * @param siteId
	 * @return
	 * @exception
	 */
	public Object synchro(int siteId) throws Exception {
		OperationObject operaObj = new OperationObject();
		CccService_MB cccService = null;
		try {
			operaObj = this.getSynchroOperationObject(siteId);// 封装下发数据
			super.sendAction(operaObj);// 下发数据
			super.verification(operaObj);// 验证是否下发成功
			if (operaObj.isSuccess()) {
				/* 成功，则与数据库进行同步 */
				cccService = (CccService_MB) ConstantUtil.serviceFactory.newService_MB(Services.CCCMANAGEMENT);
				for (ActionObject actionObject : operaObj.getActionObjectList()) {
					cccService.updateStatusActivate(siteId, EActiveStatus.UNACTIVITY.getValue());
					this.synchro_db(cccService,actionObject.getCccObjectList(), siteId);
				}
				
				return ResultString.CONFIG_SUCCESS;
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			UiUtil.closeService_MB(cccService);
		}
		return ResultString.CONFIG_TIMEOUT;
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
			WhImplUtil whImplUtil = new WhImplUtil();
			operationObject = new OperationObject();
			neObject = whImplUtil.siteIdToNeObject(siteId);
			actionObject = new ActionObject();
			actionObject.setActionId(super.getActionId(operationObject.getActionObjectList()));
			actionObject.setNeObject(neObject);
			actionObject.setType("cccSynchro");
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
	 * @param elineObjList
	 * @param siteId
	 * @throws Exception
	 */
	private void synchro_db(CccService_MB cccService,List<CccObject> cccObjList, int siteId) throws Exception {
		CccInfo cccInfo = null;
		List<AcPortInfo> acPortInfos = null;		
		int serviceId = 1;
		List<Integer> acIdList = null;
		AcPortInfoService_MB acInfoService = null;
		try {
			acInfoService = (AcPortInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.AcInfo);
			SynchroUtil synchroUtil = new SynchroUtil();
			for (CccObject cccObject : cccObjList) {
				acIdList = new ArrayList<Integer>();
				try {
				
						//AC
						try {
							acPortInfos = this.getAcPortInfo(cccObject, siteId);
							for (AcPortInfo acPortInfo : acPortInfos) {
								int acId = synchroUtil.acPortInfoSynchro(acPortInfo, siteId);
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
							cccInfo = this.getCccInfo(cccObject, siteId, serviceId,acIdList);
							synchroUtil.cccSynchro(cccService,cccInfo, siteId);
						} catch (Exception e) {
							ExceptionManage.dispose(e, getClass());
							acInfoService.updateState(acIdList);
							continue;
						}
					
						serviceId++;
				} catch (Exception e) {
					ExceptionManage.dispose(e, getClass());
				}
			}

		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			UiUtil.closeService_MB(acInfoService);
			cccInfo = null;
			acPortInfos = null;
		
		}
	}

	/**
	 * 
	 * @param cccObjectList
	 * @param siteId
	 * @return
	 * @throws Exception
	 */
	private CccInfo getCccInfo(CccObject cccObject, int siteId, int serviceId, List<Integer> acIdList) throws Exception {
		CccInfo cccInfo = new CccInfo();		
		String acIds = "";
		try {
			acIds = acIdList.toString();						
					
			cccInfo.setServiceId(serviceId);
			cccInfo.setServiceType(EServiceType.CCC.getValue());
			cccInfo.setName("ccc" + cccObject.getVplsId());
			cccInfo.setActiveStatus(EActiveStatus.ACTIVITY.getValue());
			cccInfo.setAmostAcId(acIds.subSequence(1, acIds.length() -1).toString());
			cccInfo.setaXcId(cccObject.getVplsId());
			cccInfo.setaSiteId(siteId);			
			
		} catch (Exception e) {
			throw e;
		}finally{
			acIds = null;
		}
		return cccInfo;
	}


	/**
	 * 
	 * @param elanObject
	 * @param siteId
	 * @return
	 * @throws Exception
	 */
	private List<AcPortInfo> getAcPortInfo(CccObject cccObject, int siteId) throws Exception {
		List<AcPortInfo> acPortInfoList = new ArrayList<AcPortInfo>();
		AcPortInfo acPortInfo = null;
		PortInst portInst = null;
		PortService_MB portService = null;
		PortLagService_MB portLagService = null;
		QosInfo simpleQos = null;
		try {
			portService = (PortService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORT);
			portLagService = (PortLagService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORTLAG);
			int i=0;
			for (CccUNIObject uniObj : cccObject.getCccUNIObjectList()) {
				i++;
				acPortInfo = new AcPortInfo();
				int portModel = 0;
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
				acPortInfo.setMacCount(cccObject.getMacCount());
				acPortInfo.setSiteId(siteId);
				simpleQos = new QosInfo();
				simpleQos.setPir(uniObj.getPir()*1000);
				simpleQos.setCir(uniObj.getCir()*1000);
				simpleQos.setCbs(uniObj.getCbs());
				simpleQos.setPbs(uniObj.getPbs());
				simpleQos.setCos(5);
				simpleQos.setQosType("L2");
				acPortInfo.setTagAction(UiUtil.getCodeByValue("TAGRECOGNITION", uniObj.getUpTagRecognition() + "").getId());
				acPortInfo.setExitRule(UiUtil.getCodeByValue("PORTTAGBEHAVIOR", uniObj.getDownTagBehavior() + "").getId());
				acPortInfo.setVlanId(uniObj.getDownTagValnId() + "");
				acPortInfo.setVlanpri(uniObj.getDownTagValnPri() + "");
				acPortInfo.setManagerEnable(UiUtil.getCodeByValue("VCTRAFFICPOLICING", uniObj.getVCTrafficPolicing() + "").getId());
				acPortInfo.setHorizontalDivision(UiUtil.getCodeByValue("VCTRAFFICPOLICING", uniObj.getPortSplitHorizon() + "").getId());
				acPortInfo.setMacAddressLearn(UiUtil.getCodeByValue("MACLEARN", uniObj.getMacAddresslearn()+"").getId());
				//acPortInfo.setName("ac_ccc" + cccObject.getVplsId());
				acPortInfo.setName("ac_ccc" + cccObject.getVplsId()+"-"+i);
				acPortInfo.setAcStatus(EActiveStatus.ACTIVITY.getValue());
				acPortInfo.setModel(UiUtil.getCodeByValue("MODEL", uniObj.getModel() + "").getId());
				// acPortInfo.setBufType(uniObj);
				acPortInfo.setBufferList(getacbuffer(uniObj, uniObj.getPort(), siteId));
				acPortInfo.setPortModel(UiUtil.getCodeByValue("UNIPORTMODE", portModel+"").getId());
				acPortInfo.setSimpleQos(simpleQos);
				acPortInfo.setIsUser(1);
				acPortInfoList.add(acPortInfo);
			}
		} catch (Exception e) {
			throw e;
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
	private List<Acbuffer> getacbuffer(CccUNIObject uniObj, int portNumber, int siteId) throws Exception {
		List<Acbuffer> acbufferList = new ArrayList<Acbuffer>();
		Acbuffer acbuffer = new Acbuffer();
//		if (portNumber < 100) {
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
					acbuffer.setModel(VpwsBuffer.getModel());
					acbufferList.add(acbuffer);
				} catch (Exception e) {
					ExceptionManage.dispose(e,this.getClass());
				}
			}
//		}
		return acbufferList;
	}

	public List<CccInfo> consistence(int siteId) {
		List<CccInfo> cccList = new ArrayList<CccInfo>();
		try {
			OperationObject operaObj = this.getSynchroOperationObject(siteId);// 封装下发数据
			super.sendAction(operaObj);// 下发数据
			super.verification(operaObj);// 验证是否下发成功
			if (operaObj.isSuccess()) {
				for (ActionObject actionObject : operaObj.getActionObjectList()) {
					cccList = this.getCccInfoList(actionObject.getCccObjectList(), siteId);
					return cccList;
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return cccList;
	}

	private List<CccInfo> getCccInfoList(List<CccObject> cccObjectList, int siteId) {
		List<CccInfo> cccList = new ArrayList<CccInfo>();
		int serviceId = 1;
		List<Integer> acIds = new ArrayList<Integer>();
		for (CccObject cccObject : cccObjectList) {
			try {
					List<AcPortInfo> acList = this.getAcPortInfo(cccObject, siteId);
					CccInfo cccInfo = this.getCccInfo(cccObject, siteId, serviceId,acIds);				
					cccInfo.getAcPortList().addAll(acList);					
					cccList.add(cccInfo);	
			} catch (Exception e) {
				ExceptionManage.dispose(e, this.getClass());
			}
			serviceId++;
		}
		return cccList;
	}


}