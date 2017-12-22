﻿package com.nms.service.impl.wh;

import java.util.ArrayList;
import java.util.List;

import com.nms.db.bean.ptn.oam.OamInfo;
import com.nms.db.bean.ptn.oam.OamMepInfo;
import com.nms.db.bean.ptn.path.pw.MsPwInfo;
import com.nms.db.bean.ptn.path.pw.PwInfo;
import com.nms.db.bean.ptn.path.tunnel.Lsp;
import com.nms.db.bean.ptn.path.tunnel.Tunnel;
import com.nms.db.bean.ptn.qos.QosInfo;
import com.nms.db.enums.EActiveStatus;
import com.nms.db.enums.EManufacturer;
import com.nms.db.enums.EPwType;
import com.nms.db.enums.EQosDirection;
import com.nms.db.enums.OamTypeEnum;
import com.nms.db.enums.QosTemplateTypeEnum;
import com.nms.drive.service.bean.MsPwObject;
import com.nms.drive.service.bean.NEObject;
import com.nms.drive.service.bean.PwObject;
import com.nms.model.ptn.path.pw.MsPwInfoService_MB;
import com.nms.model.ptn.path.pw.PwInfoService_MB;
import com.nms.model.ptn.path.tunnel.LspInfoService_MB;
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
import com.nms.ui.manager.UiUtil;

public class PwWHServiceImpl extends WHOperationBase implements OperationServiceI {

	@SuppressWarnings("unchecked")
	@Override
	public String excutionInsert(Object object) throws Exception {
		List<PwInfo> pwInfos = null;
		String result = "";
		List<Integer> siteIdList = null;
		OperationObject operationObjectAfter = null;
		OperationObject operationObjectMsPwAfter = null;
		OperationObject operationObjectResult = null;
		List<PwInfo> pwList = new ArrayList<PwInfo>();
		NEObject neObject = null;
		List<PwObject> pwObjects = null;
		List<MsPwObject> msPwObjects = null;
		try {
			pwInfos = (List<PwInfo>) object;
			pwList.add(pwInfos.get(0));
			/** 获取此pw的所有网元ID */
			siteIdList = this.getSiteIds(pwList);
			SiteUtil siteUtil = new SiteUtil();
			WhImplUtil whImplUtil = new WhImplUtil();
			operationObjectAfter = new OperationObject();
			operationObjectMsPwAfter = new OperationObject();
			if(siteIdList.size() > 0){
				for (Integer siteId : siteIdList) {
					if ("0".equals(siteUtil.SiteTypeUtil(siteId) + "")) {
						neObject = whImplUtil.siteIdToNeObject(siteId);
						// pw配置
						pwObjects = this.getPwInfoObject(siteId);
						ActionObject actionObj = this.getActionObject(pwObjects, neObject);
						operationObjectAfter.getActionObjectList().add(actionObj);
						if(pwInfos.get(0).getMsPwInfos() != null && pwInfos.get(0).getMsPwInfos().size() > 0){
							// 多段pw配置块
							msPwObjects = this.getMsPwObject(siteId);
							ActionObject actionMsPwObj = this.getActionMsPwObject(msPwObjects, neObject);
							operationObjectAfter.getActionObjectList().add(actionMsPwObj);
						}
					}
				}
				super.sendAction(operationObjectAfter);
				operationObjectResult = super.verification(operationObjectAfter);// 获取设备返回信息
				if (!(operationObjectResult.isSuccess())) {// 失败
					return super.getErrorMessage(operationObjectResult);// 设备返回信息有需要也可以扩展为key=siteid，value=返回信息,的map结构
				}
				if(pwInfos.get(0).getMsPwInfos() != null && pwInfos.get(0).getMsPwInfos().size() > 0){
					super.sendAction(operationObjectMsPwAfter);
					operationObjectResult = super.verification(operationObjectMsPwAfter);// 获取设备返回信息
					if (!(operationObjectResult.isSuccess())) {// 失败
						return super.getErrorMessage(operationObjectResult);// 设备返回信息有需要也可以扩展为key=siteid，value=返回信息,的map结构
					}
				}
			}
			return ResultString.CONFIG_SUCCESS;
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			super.clearLock(siteIdList);
		}
		return result;
	}

	private ActionObject getActionObject(List<PwObject> pwObjects, NEObject neObject) {
		ActionObject actionObject = null;
		try {
			actionObject = new ActionObject();
			List<ActionObject> actionIdList = new ArrayList<ActionObject>();
			actionIdList.add(actionObject);
			actionObject.setActionId(super.getActionId(actionIdList));
			actionObject.setNeObject(neObject);
			actionObject.setType("pw");
			actionObject.setPwObjectList(pwObjects);
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return actionObject;
	}

	private ActionObject getActionMsPwObject(List<MsPwObject> msPwObjects, NEObject neObject) {
		ActionObject actionObject = null;
		try {
			actionObject = new ActionObject();
			List<ActionObject> actionIdList = new ArrayList<ActionObject>();
			actionIdList.add(actionObject);
			actionObject.setActionId(super.getActionId(actionIdList));
			actionObject.setNeObject(neObject);
			actionObject.setType("mspw");
			actionObject.setMsPwObjects(msPwObjects);
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return actionObject;
	}

	@Override
	public String excutionUpdate(Object object) throws Exception {
		PwInfo pwInfo = null;
		List<Integer> siteIdList = null;
		OperationObject operationObjectAfter = null;
		OperationObject operationObjectMsPwAfter = null;
		OperationObject operationObjectResult = null;
		List<PwInfo> pwList = new ArrayList<PwInfo>();
		NEObject neObject = null;
		List<PwObject> pwObjects = null;
		List<MsPwObject> msPwObjects = null;
		try {
			pwInfo = (PwInfo) object;
			pwList.add(pwInfo);
			/** 获取此tunnel的所有网元ID */
			siteIdList = this.getSiteIds(pwList);
			SiteUtil siteUtil = new SiteUtil();
			WhImplUtil whImplUtil = new WhImplUtil();
			operationObjectAfter = new OperationObject();
			operationObjectMsPwAfter = new OperationObject();
			if(siteIdList.size() > 0){
				for (Integer siteId : siteIdList) {
					if ("0".equals(siteUtil.SiteTypeUtil(siteId) + "")) {
						neObject = whImplUtil.siteIdToNeObject(siteId);
						// pw配置
						pwObjects = this.getPwInfoObject(siteId);
						ActionObject actionObj = this.getActionObject(pwObjects, neObject);
						operationObjectAfter.getActionObjectList().add(actionObj);
						if(pwInfo.getMsPwInfos() != null && pwInfo.getMsPwInfos().size() > 0){
							// 多段pw配置块
							msPwObjects = this.getMsPwObject(siteId);
							ActionObject actionMsPwObj = this.getActionMsPwObject(msPwObjects, neObject);
							operationObjectAfter.getActionObjectList().add(actionMsPwObj);
						}
					}
				}
				super.sendAction(operationObjectAfter);
				operationObjectResult = super.verification(operationObjectAfter);// 获取设备返回信息
				if (!(operationObjectResult.isSuccess())) {// 失败
					return super.getErrorMessage(operationObjectResult);// 设备返回信息有需要也可以扩展为key=siteid，value=返回信息,的map结构
				}
				if(pwInfo.getMsPwInfos() != null && pwInfo.getMsPwInfos().size() > 0){
					super.sendAction(operationObjectMsPwAfter);
					operationObjectResult = super.verification(operationObjectMsPwAfter);// 获取设备返回信息
					if (!(operationObjectResult.isSuccess())) {// 失败
						return super.getErrorMessage(operationObjectResult);// 设备返回信息有需要也可以扩展为key=siteid，value=返回信息,的map结构
					}
				}
			}
			return ResultString.CONFIG_SUCCESS;
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			super.clearLock(siteIdList);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String excutionDelete(List objectList) throws Exception {
		List<Integer> siteIdList = null;
		OperationObject operationObjectAfter = null;
		OperationObject operationObjectMsPwAfter = null;
		OperationObject operationObjectResult = null;
		List<PwInfo> pwList = new ArrayList<PwInfo>();
		NEObject neObject = null;
		List<PwObject> pwObjects = null;
		List<MsPwObject> msPwObjects = null;
		try {
			pwList = (List<PwInfo>) objectList;
			/** 获取此pw的所有网元ID */
			siteIdList = this.getSiteIds(pwList);
			SiteUtil siteUtil = new SiteUtil();
			WhImplUtil whImplUtil = new WhImplUtil();
			operationObjectAfter = new OperationObject();
			operationObjectMsPwAfter = new OperationObject();
			if(siteIdList.size() > 0){
				for (Integer siteId : siteIdList) {
					if ("0".equals(siteUtil.SiteTypeUtil(siteId) + "")) {
						neObject = whImplUtil.siteIdToNeObject(siteId);
						// pw配置
						pwObjects = this.getPwInfoObject(siteId);
						ActionObject actionObj = this.getActionObject(pwObjects, neObject);
						operationObjectAfter.getActionObjectList().add(actionObj);
						// 多段pw配置块
						msPwObjects = this.getMsPwObject(siteId);
						ActionObject actionMsPwObj = this.getActionMsPwObject(msPwObjects, neObject);
						operationObjectAfter.getActionObjectList().add(actionMsPwObj);
					}
				}
				super.sendAction(operationObjectAfter);
				operationObjectResult = super.verification(operationObjectAfter);// 获取设备返回信息
				if (!(operationObjectResult.isSuccess())) {// 失败
					return super.getErrorMessage(operationObjectResult);// 设备返回信息有需要也可以扩展为key=siteid，value=返回信息,的map结构
				}
				super.sendAction(operationObjectMsPwAfter);
				operationObjectResult = super.verification(operationObjectMsPwAfter);// 获取设备返回信息
				if (!(operationObjectResult.isSuccess())) {// 失败
					return super.getErrorMessage(operationObjectResult);// 设备返回信息有需要也可以扩展为key=siteid，value=返回信息,的map结构
				}
			}
			return ResultString.CONFIG_SUCCESS;
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			super.clearLock(siteIdList);
			operationObjectAfter = null;
			operationObjectResult = null;
			pwList = null;
		}
		return null;
	}

	/**
	 * 转换MSpw
	 * 
	 * @param siteId
	 * @return
	 */
	private List<MsPwObject> getMsPwObject(Integer siteId) {
		MsPwInfoService_MB msPwInfoService = null;
		TunnelService_MB tunnelService = null;
		List<MsPwInfo> msPwInfos = null;
		MsPwInfo msPwInfo = null;
		MsPwObject msPwObject = null;
		List<MsPwObject> msPwObjects = null;
		Tunnel tunnel = null;
		PwInfoService_MB pwInfoService = null;
		PwInfo pwInfo = null;
		try {
			msPwInfoService = (MsPwInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.MSPWSERVICE);
			tunnelService = (TunnelService_MB) ConstantUtil.serviceFactory.newService_MB(Services.Tunnel);
			pwInfoService = (PwInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PwInfo);
			msPwInfo = new MsPwInfo();
			msPwInfo.setSiteId(siteId);
			msPwObjects = new ArrayList<MsPwObject>();
			msPwInfos = msPwInfoService.select(msPwInfo);
			int id = 1;
			if (msPwInfos != null && msPwInfos.size() > 0) {
				for (MsPwInfo info : msPwInfos) {
//					pwInfo = pwInfoService.selectByPwId(info.getPwId());
					pwInfo = pwInfoService.selectByPwIdForMulti(info.getPwId(),0);
					if (pwInfo != null && pwInfo.getPwStatus() == EActiveStatus.ACTIVITY.getValue()) {
						msPwObject = new MsPwObject();
						msPwObject.setMspwId(id);
						msPwObject.setFrontInlabel(info.getFrontInlabel());
						msPwObject.setFrontOutlabel(info.getBackInlabel());
//						msPwObject.setFrontOutlabel(info.getBackOutlabel());
						msPwObject.setBackInlabel(info.getFrontOutlabel());
						msPwObject.setBackOutlabel(info.getBackOutlabel());
//						msPwObject.setBackOutlabel(info.getBackInlabel());
						msPwObject.setMipId(info.getMipId());
						tunnel = tunnelService.selectByTunnelIdAndSiteId(siteId, info.getFrontTunnelId());
						msPwObject.setFrontTunnelId(this.getTunnelId(siteId, tunnel));
						tunnel = tunnelService.selectByTunnelIdAndSiteId(siteId, info.getBackTunnelId());
						msPwObject.setBackTunnelId(this.getTunnelId(siteId, tunnel));
						msPwObjects.add(msPwObject);
					}
					id++;
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			UiUtil.closeService_MB(pwInfoService);
			UiUtil.closeService_MB(msPwInfoService);
			UiUtil.closeService_MB(tunnelService);
		}
		return msPwObjects;
	}

	private int getTunnelId(int siteId,Tunnel tunnel){
		int tunnelID = 0;
		for(Lsp lsp : tunnel.getLspParticularList()){
			if(lsp.getASiteId() == siteId && lsp.getAtunnelbusinessid() != 0){
				tunnelID = lsp.getAtunnelbusinessid();
			}else if(lsp.getZSiteId() == siteId && lsp.getZtunnelbusinessid() != 0){
				tunnelID = lsp.getZtunnelbusinessid();
			}
		}
		return tunnelID;
	}
	
	private List<PwObject> getPwInfoObject(int siteId) {

		List<PwObject> pwObjectsList = new ArrayList<PwObject>();
		PwInfoService_MB pwInfoService = null;
		List<PwInfo> pwInfosList = new ArrayList<PwInfo>();
		PwObject pwObject = null;
		LspInfoService_MB lspParticularService = null;
		List<Lsp> lspList = new ArrayList<Lsp>();

		List<OamInfo> oamInfoList = null;
		List<PwInfo> pwInfosActive = new ArrayList<PwInfo>();
		try {
			pwInfoService = (PwInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PwInfo);
			lspParticularService = (LspInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.LSPINFO);
			pwInfosList = pwInfoService.selectNodeBySiteid(siteId);
			oamInfoList = new ArrayList<OamInfo>();
			for (PwInfo pwInfo : pwInfosList) {
				if (pwInfo.getPwStatus() == EActiveStatus.ACTIVITY.getValue()) {
					pwInfosActive.add(pwInfo);
				}
			}
			if (pwInfosList != null && pwInfosActive.size() > 0) {
				for (PwInfo pwInfo : pwInfosList) {
					pwObject = new PwObject();
					oamInfoList = pwInfo.getOamList();
					
					for (OamInfo oamInfo : oamInfoList) {
						if (oamInfo.getOamMep() != null && oamInfo.getOamMep().getSiteId() == siteId) {
							pwObject.setSourceMEP(oamInfo.getOamMep().getLocalMepId());
							pwObject.setEquityMEP(oamInfo.getOamMep().getRemoteMepId());
							pwObject.setCsfEnabl(oamInfo.getOamMep().isCsfEnable() ? "1" : "0");
							
							pwObject.setCvEnabl(oamInfo.getOamMep().isCv() ? "1" : "0");
							pwObject.setCvCycle(getCycle(oamInfo.getOamMep().getCvCycle()));
							pwObject.setCvReserve("0000");
							
							pwObject.setMel(oamInfo.getOamMep().getMel());
							
							if (oamInfo.getOamMep().getLspTc() == 0) {
								pwObject.setLspTc(0);
							} else {
								pwObject.setLspTc(Integer.parseInt(UiUtil.getCodeById(oamInfo.getOamMep().getLspTc()).getCodeValue()));
							}
							if (oamInfo.getOamMep().getPwTc() == 0) {
								pwObject.setPwTc(0);
							} else {
								pwObject.setPwTc(Integer.parseInt(UiUtil.getCodeById(oamInfo.getOamMep().getPwTc()).getCodeValue()));
							}
							this.getMegIccAndMegUmc(pwObject, oamInfo.getOamMep().getMegIcc(), oamInfo.getOamMep().getMegUmc());
							pwObject.setOamEnable(oamInfo.getOamMep().isOamEnable() ? 1 : 0);
							pwObject.setLm(oamInfo.getOamMep().isLm()?1:0);
						} else if (oamInfo.getOamMip() != null && oamInfo.getOamMip().getSiteId() == siteId) {
							pwObject.setSourceMEP(oamInfo.getOamMep().getLocalMepId());
							pwObject.setEquityMEP(oamInfo.getOamMep().getRemoteMepId());

							pwObject.setCvEnabl(oamInfo.getOamMep().isCv() ? "1" : "0");
							pwObject.setCvCycle(getCycle(oamInfo.getOamMep().getCvCycle()));
							pwObject.setCvReserve("0000");
						}
						
					}

					if (pwInfo.getPwStatus() == EActiveStatus.ACTIVITY.getValue()) {
						// 多段pw
						if ("1".equals(pwInfo.getBusinessType())) {
							/*调试使用***************************************************/
//							System.out.println("下发多段PW");
//							ExceptionManage.dispose(new Exception("下发多段PW"), this.getClass());
							/*end**************************************************/
							if (pwInfo.getASiteId() == siteId) {
								lspList = lspParticularService.selectBytunnelId(pwInfo.getMsPwInfos().get(0).getFrontTunnelId());
								pwObject.setPwId(pwInfo.getApwServiceId());
								for (Lsp lsp : lspList) {
									if (lsp.getASiteId() == siteId) {
										pwObject.setTunnelId(lsp.getAtunnelbusinessid());
									} else if (lsp.getZSiteId() == siteId) {
										pwObject.setTunnelId(lsp.getZtunnelbusinessid());
									}
								}
								pwObject.setInLable(pwInfo.getOutlabelValue());
								pwObject.setOutLable(pwInfo.getInlabelValue());
								/*调试使用***************************************************/
//								System.out.println("A端网元ID:"+siteId);
//								ExceptionManage.dispose(new Exception("A端网元ID:"+siteId), this.getClass());
//								System.out.println("下发入标签/出标签:"+pwInfo.getOutlabelValue()+"/"+pwInfo.getInlabelValue());
//								ExceptionManage.dispose(new Exception("下发入标签/出标签:"+pwInfo.getOutlabelValue()+"/"+pwInfo.getInlabelValue()), this.getClass());
								/*end**************************************************/
								pwObject.setOutVlanValue(pwInfo.getaOutVlanValue());
								pwObject.setVlanEnable(pwInfo.getaVlanEnable());
								pwObject.setTp_id(pwInfo.getAtp_id());
//								pwObject.setSourceMac(CoderUtils.transformMac(pwInfo.getaSourceMac()));
//								pwObject.setTargetMac(CoderUtils.transformMac(pwInfo.getAtargetMac()));
								pwObject.setRole(0);
							} else if (pwInfo.getZSiteId() == siteId) {
								lspList = lspParticularService.selectBytunnelId(pwInfo.getMsPwInfos().get(pwInfo.getMsPwInfos().size() - 1).getBackTunnelId());
								pwObject.setPwId(pwInfo.getZpwServiceId());
								for (Lsp lsp : lspList) {
									if (lsp.getASiteId() == siteId) {
										pwObject.setTunnelId(lsp.getAtunnelbusinessid());
									} else if (lsp.getZSiteId() == siteId) {
										pwObject.setTunnelId(lsp.getZtunnelbusinessid());
									}
								}
								pwObject.setInLable(pwInfo.getBackInlabel());
								pwObject.setOutLable(pwInfo.getBackOutlabel());
								/*调试使用***************************************************/
//								System.out.println("Z端网元ID:"+siteId);
//								ExceptionManage.dispose(new Exception("Z端网元ID:"+siteId), this.getClass());
//								System.out.println("下发入标签/出标签:"+pwInfo.getBackInlabel()+"/"+pwInfo.getBackOutlabel());
//								ExceptionManage.dispose(new Exception("下发入标签/出标签:"+pwInfo.getBackInlabel()+"/"+pwInfo.getBackOutlabel()), this.getClass());
								/*end**************************************************/
								pwObject.setOutVlanValue(pwInfo.getzOutVlanValue());
								pwObject.setVlanEnable(pwInfo.getzVlanEnable());
								pwObject.setTp_id(pwInfo.getZtp_id());
//								pwObject.setSourceMac(CoderUtils.transformMac(pwInfo.getzSourceMac()));
//								pwObject.setTargetMac(CoderUtils.transformMac(pwInfo.getZtargetMac()));
								pwObject.setRole(1);
							}
						} else {
							// 普通pw
							/*调试使用***************************************************/
//							System.out.println("下发普通PW");
//							ExceptionManage.dispose(new Exception("下发普通PW"), this.getClass());
							/*end**************************************************/
							lspList = lspParticularService.selectBytunnelId(pwInfo.getTunnelId());
							if (pwInfo.getASiteId() == siteId) {
								pwObject.setPwId(pwInfo.getApwServiceId());
								for (Lsp lsp : lspList) {
									if (lsp.getASiteId() == siteId) {
										pwObject.setTunnelId(lsp.getAtunnelbusinessid());
									} else if (lsp.getZSiteId() == siteId) {
										pwObject.setTunnelId(lsp.getZtunnelbusinessid());
									}
								}
								pwObject.setInLable(pwInfo.getInlabelValue());
								pwObject.setOutLable(pwInfo.getOutlabelValue());
								/*调试使用***************************************************/
//								System.out.println("A端网元ID:"+siteId);
//								ExceptionManage.dispose(new Exception("A端网元ID:"+siteId), this.getClass());
//								System.out.println("下发入标签/出标签:"+pwInfo.getInlabelValue()+"/"+pwInfo.getOutlabelValue());
//								ExceptionManage.dispose(new Exception("下发入标签/出标签:"+pwInfo.getInlabelValue()+"/"+pwInfo.getOutlabelValue()), this.getClass());
								/*end**************************************************/
								pwObject.setOutVlanValue(pwInfo.getaOutVlanValue());
								pwObject.setTp_id(pwInfo.getAtp_id());
								pwObject.setVlanEnable(pwInfo.getaVlanEnable());
//								pwObject.setSourceMac(CoderUtils.transformMac(pwInfo.getaSourceMac()));
//								pwObject.setTargetMac(CoderUtils.transformMac(pwInfo.getAtargetMac()));
								pwObject.setRole(0);
							} else if (pwInfo.getZSiteId() == siteId) {
								pwObject.setPwId(pwInfo.getZpwServiceId());
								for (Lsp lsp : lspList) {
									if (lsp.getASiteId() == siteId) {
										pwObject.setTunnelId(lsp.getAtunnelbusinessid());
									} else if (lsp.getZSiteId() == siteId) {
										pwObject.setTunnelId(lsp.getZtunnelbusinessid());
									}
								}
								pwObject.setInLable(pwInfo.getOutlabelValue());
								pwObject.setOutLable(pwInfo.getInlabelValue());
								/*调试使用***************************************************/
//								System.out.println("Z端网元ID:"+siteId);
//								ExceptionManage.dispose(new Exception("Z端网元ID:"+siteId), this.getClass());
//								System.out.println("下发入标签/出标签:"+pwInfo.getOutlabelValue()+"/"+pwInfo.getInlabelValue());
//								ExceptionManage.dispose(new Exception("下发入标签/出标签:"+pwInfo.getOutlabelValue()+"/"+pwInfo.getInlabelValue()), this.getClass());
								/*end**************************************************/
								pwObject.setOutVlanValue(pwInfo.getzOutVlanValue());
								pwObject.setTp_id(pwInfo.getZtp_id());
								pwObject.setVlanEnable(pwInfo.getzVlanEnable());
//								pwObject.setSourceMac(CoderUtils.transformMac(pwInfo.getzSourceMac()));
//								pwObject.setTargetMac(CoderUtils.transformMac(pwInfo.getZtargetMac()));
								pwObject.setRole(1);
							}
						}
						// 赋值QOS的属性值
						if (pwInfo.getQosList() != null && pwInfo.getQosList().size() > 0) {
							QosInfo qosInst = pwInfo.getQosList().get(0);
							pwObject.setPbs(qosInst.getPbs());
							if (qosInst.getCbs() < 0) {
								pwObject.setCbs(0);
							} else {
								pwObject.setCbs(qosInst.getCbs());
							}
							pwObject.setCm(qosInst.getColorSence());
							pwObject.setPir(qosInst.getPir() / 1000);
							pwObject.setCir(qosInst.getCir() / 1000);	
						}
						pwObject.setModel(pwInfo.getQosModel());
						pwObject.setPhbtoexpstrategy(pwInfo.getExpStrategy());
						pwObject.setExp(pwInfo.getExpAssignment());
						pwObject.setExpid(pwInfo.getPhbToExpId());
						pwObject.setExptophbstrategy(pwInfo.getPhbStrategy());
						pwObject.setPhb(pwInfo.getPhbAssignment());
						pwObject.setPhbid(pwInfo.getExpTophbId());
						pwObject.setPwType(pwInfo.getType().getValue());
						pwObjectsList.add(pwObject);
					}
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			UiUtil.closeService_MB(pwInfoService);
			UiUtil.closeService_MB(lspParticularService);
		}
		return pwObjectsList;
	}

	public String getCycle(double cycle) {
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

	private List<Integer> getSiteIds(List<PwInfo> pwInfoActivate) throws Exception {
		List<Integer> siteIds = new ArrayList<Integer>();
		try {

			for (PwInfo pw : pwInfoActivate) {
				if (!siteIds.contains(pw.getASiteId()) && pw.getASiteId() > 0 && super.getManufacturer(pw.getASiteId()) == EManufacturer.WUHAN.getValue()) {
					siteIds.add(pw.getASiteId());
				}
				if (!siteIds.contains(pw.getZSiteId()) && pw.getZSiteId() > 0 && EManufacturer.WUHAN.getValue() == super.getManufacturer(pw.getZSiteId()))// 单点对象的Z端不用下发
					siteIds.add(pw.getZSiteId());
				if (pw.getMsPwInfos() != null && pw.getMsPwInfos().size() > 0) {
					for (MsPwInfo msPwInfo : pw.getMsPwInfos()) {
						if (!siteIds.contains(msPwInfo.getSiteId())) {
							siteIds.add(msPwInfo.getSiteId());
						}
					}
				}

			}
		} catch (Exception e) {
			throw e;
		}

		return siteIds;
	}

	@Override
	public Object synchro(int siteId) throws Exception {
		OperationObject operationObject = new OperationObject();
		OperationObject operationObjectResult = null;
		PwInfoService_MB pwInfoService = null;
		OperationObject operationObjectMspw = new OperationObject();
		OperationObject operationObjectResultMspw = null;
		String result = ResultString.CONFIG_TIMEOUT;
		String mspwResult = ResultString.CONFIG_TIMEOUT;
		try {
			operationObject = this.getSynchroOperationObject(siteId, "pwSynchro");// 封装下发数据
			super.sendAction(operationObject);
			operationObjectResult = super.verification(operationObject);
			if (operationObjectResult.isSuccess()) {// 返回成功
				pwInfoService = (PwInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PwInfo);
				for (ActionObject actionObject : operationObjectResult.getActionObjectList()) {
					pwInfoService.updateActiveStatus(siteId, EActiveStatus.UNACTIVITY.getValue());
					this.synchro_db(actionObject.getPwObjectList(), siteId);// 与数据库进行同步
				}
				result = ResultString.CONFIG_SUCCESS;
			}
			
			operationObjectMspw = this.getSynchroOperationObject(siteId, "mspwSynchro");// 封装下发数据
			super.sendAction(operationObjectMspw);
			operationObjectResultMspw = super.verification(operationObjectMspw);
			if (operationObjectResultMspw.isSuccess()) {// 返回成功
				for (ActionObject actionObject : operationObjectResultMspw.getActionObjectList()) {
					this.synchroMspw_db(actionObject.getMsPwObjects(), siteId);
				}
				mspwResult = ResultString.CONFIG_SUCCESS;
			}
			
			if(result.equals(ResultString.CONFIG_SUCCESS))
			{
			     return mspwResult;
			}
			
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			operationObjectResult = null;
			UiUtil.closeService_MB(pwInfoService);
		}
		return ResultString.QUERY_FAILED;
	}
	
	/**
	 * 检查一致性 同步设备的数据
	 * @param siteId
	 * @return
	 */
	public List<Object> consistence(int siteId){
		
		List<Object> pwInfoList = new ArrayList<Object>();
		OperationObject operationObject = new OperationObject();
		OperationObject operationObjectResult = null;
		OperationObject operationObjectMspw = new OperationObject();
		OperationObject operationObjectResultMspw = null;
		try {
			operationObject = this.getSynchroOperationObject(siteId, "pwSynchro");// 封装下发数据
			super.sendAction(operationObject);
			operationObjectResult = super.verification(operationObject);
			if (operationObjectResult.isSuccess()) {// 返回成功
				for (ActionObject actionObject : operationObjectResult.getActionObjectList()) {
					pwInfoList.addAll(this.getpwInfoList(actionObject.getPwObjectList(), siteId));
				}
			}
			operationObjectMspw = this.getSynchroOperationObject(siteId, "mspwSynchro");// 封装下发数据
			super.sendAction(operationObjectMspw);
			operationObjectResultMspw = super.verification(operationObjectMspw);
			if (operationObjectResultMspw.isSuccess()) {// 返回成功
				for (ActionObject actionObject : operationObjectResultMspw.getActionObjectList()) {
					pwInfoList.addAll(this.getMspwInfoList(actionObject.getMsPwObjects(), siteId));
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, getClass());
		}finally{
			 operationObject = null;
			 operationObjectResult = null;
			 operationObjectMspw = null;
			 operationObjectResultMspw = null;
		}
		return pwInfoList;
	}

	/**
	 * 将设备的多段OAM信息转为数据库对象信息
	 * @param msPwObjects
	 * @param siteId
	 * @return
	 * @throws Exception
	 */
	private List<PwInfo> getMspwInfoList(List<MsPwObject> msPwObjects, int siteId) throws Exception {
		List<MsPwInfo> msPwInfos = null;
		TunnelService_MB tunnelService = null;
		List<PwInfo> pwInfoList = new ArrayList<PwInfo>();
		PwInfo pwInfo  = null;
		try {
			tunnelService = (TunnelService_MB) ConstantUtil.serviceFactory.newService_MB(Services.Tunnel);
			for (MsPwObject msPwObject : msPwObjects) {
				msPwInfos = new ArrayList<MsPwInfo>();
				pwInfo = new PwInfo();
				MsPwInfo msPwInfo = new MsPwInfo();
				Tunnel frontTunnel = tunnelService.selectBySiteIdAndServiceId(siteId, msPwObject.getFrontTunnelId());
				msPwInfo.setFrontTunnelId(frontTunnel.getTunnelId());
				Tunnel backTunnel = tunnelService.selectBySiteIdAndServiceId(siteId, msPwObject.getBackTunnelId());
				msPwInfo.setId(msPwObject.getMspwId());
				msPwInfo.setBackTunnelId(backTunnel.getTunnelId());
				msPwInfo.setSiteId(siteId);
				msPwInfo.setFrontInlabel(msPwObject.getFrontInlabel());
				msPwInfo.setFrontOutlabel(msPwObject.getBackInlabel());
//				msPwInfo.setFrontOutlabel(msPwObject.getBackOutlabel());
				msPwInfo.setBackInlabel(msPwObject.getFrontOutlabel());
				msPwInfo.setBackOutlabel(msPwObject.getBackOutlabel());
//				msPwInfo.setBackOutlabel(msPwObject.getBackInlabel());
				/*调试使用***************************************************/
//				System.out.println("一致性检测多段PW");
//				ExceptionManage.dispose(new Exception("一致性检测多段PW"), this.getClass());
//				System.out.println("网元ID:"+siteId);
//				ExceptionManage.dispose(new Exception("网元ID:"+siteId), this.getClass());
//				System.out.println("一致性检测前向入标签/前向出标签:"+msPwObject.getFrontInlabel()+"/"+msPwObject.getBackInlabel());
//				ExceptionManage.dispose(new Exception("一致性检测前向入标签/前向出标签:"+msPwObject.getFrontInlabel()+"/"+msPwObject.getBackInlabel()), this.getClass());
//				System.out.println("一致性检测后向入标签/后向出标签:"+msPwObject.getFrontOutlabel()+"/"+msPwObject.getBackOutlabel());
//				ExceptionManage.dispose(new Exception("一致性检测后向入标签/后向出标签:"+msPwObject.getFrontOutlabel()+"/"+msPwObject.getBackOutlabel()), this.getClass());
				/*end**************************************************/
				msPwInfo.setMipId(msPwObject.getMipId());
				msPwInfos.add(msPwInfo);
				pwInfo.setBusinessType("1");
				pwInfo.setMsPwInfos(msPwInfos);
				pwInfoList.add(pwInfo);
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			UiUtil.closeService_MB(tunnelService);
			msPwInfos = null;
		}
		return pwInfoList;
	}
	
	
	private void synchroMspw_db(List<MsPwObject> msPwObjects, int siteId) {
		List<MsPwInfo> msPwInfos = new ArrayList<MsPwInfo>();
		TunnelService_MB tunnelService = null;
		MsPwInfoService_MB msPwInfoService = null;
		PwInfoService_MB pwInfoService = null;
		int id = 1;
		try {
			tunnelService = (TunnelService_MB) ConstantUtil.serviceFactory.newService_MB(Services.Tunnel);
			msPwInfoService = (MsPwInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.MSPWSERVICE);
			pwInfoService = (PwInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PwInfo);
			for (MsPwObject msPwObject : msPwObjects) {
				MsPwInfo msPwInfo = new MsPwInfo();
				Tunnel frontTunnel = tunnelService.selectBySiteIdAndServiceId(siteId, msPwObject.getFrontTunnelId());
				msPwInfo.setFrontTunnelId(frontTunnel.getTunnelId());
				Tunnel backTunnel = tunnelService.selectBySiteIdAndServiceId(siteId, msPwObject.getBackTunnelId());
				msPwInfo.setBackTunnelId(backTunnel.getTunnelId());
				msPwInfo.setSiteId(siteId);
				msPwInfo.setFrontInlabel(msPwObject.getFrontInlabel());
				msPwInfo.setFrontOutlabel(msPwObject.getBackInlabel());
//				msPwInfo.setFrontOutlabel(msPwObject.getBackOutlabel());
				msPwInfo.setBackInlabel(msPwObject.getFrontOutlabel());
				msPwInfo.setBackOutlabel(msPwObject.getBackOutlabel());
//				msPwInfo.setBackOutlabel(msPwObject.getBackInlabel());
				msPwInfo.setMipId(msPwObject.getMipId());
				/*调试使用***************************************************/
//				System.out.println("同步多段PW");
//				ExceptionManage.dispose(new Exception("同步多段PW"), this.getClass());
//				System.out.println("网元ID:"+siteId);
//				ExceptionManage.dispose(new Exception("网元ID:"+siteId), this.getClass());
//				System.out.println("同步前向入标签/前向出标签:"+msPwObject.getFrontInlabel()+"/"+msPwObject.getBackInlabel());
//				ExceptionManage.dispose(new Exception("同步前向入标签/前向出标签:"+msPwObject.getFrontInlabel()+"/"+msPwObject.getBackInlabel()), this.getClass());
//				System.out.println("同步后向入标签/后向出标签:"+msPwObject.getFrontOutlabel()+"/"+msPwObject.getBackOutlabel());
//				ExceptionManage.dispose(new Exception("同步后向入标签/后向出标签:"+msPwObject.getFrontOutlabel()+"/"+msPwObject.getBackOutlabel()), this.getClass());
				/*end**************************************************/
				
				msPwInfos = msPwInfoService.select(msPwInfo);
				if (msPwInfos.size() == 0)
				{
					/************同步时给相应的PWnfo表中*****张坤  2015-3-4*****/
					PwInfo pwInfo = new PwInfo();
					pwInfo.setInlabelValue(msPwInfo.getFrontInlabel());
					pwInfo.setOutlabelValue(msPwInfo.getFrontOutlabel());
//					pwInfo.setOutlabelValue(msPwInfo.getBackOutlabel());
					pwInfo.setBackInlabel(msPwInfo.getBackInlabel());
					pwInfo.setBackOutlabel(msPwInfo.getBackOutlabel());
//					pwInfo.setBackOutlabel(msPwInfo.getFrontOutlabel());
					/*调试使用***************************************************/
//					System.out.println("同步多段中的普通PW");
//					ExceptionManage.dispose(new Exception("同步多段中的普通PW"), this.getClass());
//					System.out.println("网元ID:"+siteId);
//					ExceptionManage.dispose(new Exception("网元ID:"+siteId), this.getClass());
//					System.out.println("同步backIn标签/backOut标签:"+msPwObject.getBackInlabel()+"/"+msPwObject.getBackOutlabel());
//					ExceptionManage.dispose(new Exception("同步backIn标签/backOut标签:"+msPwObject.getBackInlabel()+"/"+msPwObject.getBackOutlabel()), this.getClass());
					/*end**************************************************/
					
					PwInfo pwInst = pwInfoService.selectByLabels(pwInfo);
					if(pwInst.getPwId() > 0)
					{
						msPwInfo.setPwId(pwInst.getPwId());
					}
					else
					{
						pwInfo.setPwName("ms-"+id);
						pwInfo.setPwStatus(EActiveStatus.ACTIVITY.getValue());
						pwInfo.setType(EPwType.ETH);
						pwInfo.setIsSingle(1);
						pwInfo.setZoppositeId("0.0.0.0");
						pwInfo.setAoppositeId("0.0.0.0");
						pwInfo.setBusinessType("1");
						int pwId = pwInfoService.insertPwInfo(pwInfo);
						msPwInfo.setPwId(pwId);
					}
					/*****************张坤  2015-3-4**********/
					msPwInfoService.insert(msPwInfo);
				}
				id ++;
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			UiUtil.closeService_MB(tunnelService);
			UiUtil.closeService_MB(msPwInfoService);
			UiUtil.closeService_MB(pwInfoService);
		}
	}

	private void synchro_db(List<PwObject> pwObjectList, int siteId) {
		List<PwInfo> pwList = null;
		try {
			pwList = this.getpwInfoList(pwObjectList, siteId);
			SynchroUtil synchroUtil = new SynchroUtil();
			for (PwInfo pwInfo : pwList) {
				try {
					synchroUtil.pwInfoSynchro(pwInfo, siteId);
				} catch (Exception e) {
					ExceptionManage.dispose(e, this.getClass());
				}
			}
		} catch (Exception e1) {
			ExceptionManage.dispose(e1, this.getClass());
		}
	}

//	@Override
//	public 
	
	private List<PwInfo> getpwInfoList(List<PwObject> pwObjectList, int siteId) throws Exception {
		List<PwInfo> pwInfoList = new ArrayList<PwInfo>();
		Tunnel tunnel = null;
		TunnelService_MB tunnelService = null;
		try {
			tunnelService = (TunnelService_MB) ConstantUtil.serviceFactory.newService_MB(Services.Tunnel);
			/*调试使用***************************************************/
//			System.out.println("同步普通PW");
//			ExceptionManage.dispose(new Exception("同步普通PW"), this.getClass());
			/*end**************************************************/
			for (PwObject pwObject : pwObjectList) {
				PwInfo pwInfo = new PwInfo();
				tunnel = tunnelService.selectBySiteIdAndServiceId(siteId, pwObject.getTunnelId());
				pwInfo.setTunnelId(tunnel == null? 0:tunnel.getTunnelId());
				pwInfo.setPwStatus(EActiveStatus.ACTIVITY.getValue());
				if (pwObject.getRole() == 0) {
					pwInfo.setInlabelValue(pwObject.getInLable());
					pwInfo.setOutlabelValue(pwObject.getOutLable());
					pwInfo.setASiteId(siteId);
					pwInfo.setApwServiceId(pwObject.getPwId());
					pwInfo.setaSourceMac(pwObject.getSourceMac());
					pwInfo.setAtargetMac(pwObject.getTargetMac());
					pwInfo.setRole(0);
					/*调试使用***************************************************/
//					System.out.println("A端网元ID:"+siteId);
//					ExceptionManage.dispose(new Exception("A端网元ID:"+siteId), this.getClass());
//					System.out.println("同步入标签/出标签:"+pwObject.getInLable()+"/"+pwObject.getOutLable());
//					ExceptionManage.dispose(new Exception("同步入标签/出标签:"+pwObject.getInLable()+"/"+pwObject.getOutLable()), this.getClass());
					/*end**************************************************/
				} else {
					pwInfo.setInlabelValue(pwObject.getOutLable());
					pwInfo.setOutlabelValue(pwObject.getInLable());
					/*调试使用***************************************************/
//					System.out.println("Z端网元ID:"+siteId);
//					ExceptionManage.dispose(new Exception("Z端网元ID:"+siteId), this.getClass());
//					System.out.println("同步入标签/出标签:"+pwObject.getOutLable()+"/"+pwObject.getInLable());
//					ExceptionManage.dispose(new Exception("同步入标签/出标签:"+pwObject.getOutLable()+"/"+pwObject.getInLable()), this.getClass());
					/*end**************************************************/
					pwInfo.setZSiteId(siteId);
					pwInfo.setZpwServiceId(pwObject.getPwId());
					pwInfo.setzSourceMac(pwObject.getSourceMac());
					pwInfo.setZtargetMac(pwObject.getTargetMac());
					pwInfo.setRole(1);
				}
				pwInfo.setQosModel(pwObject.getModel());
				pwInfo.setZoppositeId("0.0.0.0");
				pwInfo.setAoppositeId("0.0.0.0");
				pwInfo.setExpStrategy(pwObject.getPhbtoexpstrategy());
				pwInfo.setExpAssignment(pwObject.getExp());
				pwInfo.setPhbToExpId(pwObject.getExpid());
				pwInfo.setPhbStrategy(pwObject.getExptophbstrategy());
				pwInfo.setPhbAssignment(pwObject.getPhb());
				pwInfo.setExpTophbId(pwObject.getPhbid());
				pwObject.setPhbid(pwInfo.getExpTophbId());
				pwInfo.setOamList(this.getPwOamInfo(pwObject, siteId));
				pwInfo.setType(pwObject.getPwType() == 1 ? EPwType.ETH : EPwType.PDH);
				pwInfo.setIsSingle(1);
				pwInfo.setQosList(this.getQosInfos(pwObject, siteId));
				pwInfo.setPwName("pw" + pwObject.getPwId());
				pwInfo.setaOutVlanValue(pwObject.getOutVlanValue());
				pwInfo.setaVlanEnable(pwObject.getVlanEnable());
				pwInfo.setaSourceMac(pwObject.getSourceMac());
				pwInfo.setAtargetMac(pwObject.getTargetMac());
				pwInfo.setAtp_id(pwObject.getTp_id());
				pwInfo.setBusinessType("0");
				if(tunnel != null){
					pwInfoList.add(pwInfo);
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			tunnel = null;
			UiUtil.closeService_MB(tunnelService);
		}
		return pwInfoList;
	}

	private List<OamInfo> getPwOamInfo(PwObject pwObject, int siteId) throws Exception {
		List<OamInfo> oamInfoList = new ArrayList<OamInfo>();
		OamInfo oam = new OamInfo();
		WhImplUtil whImplUtil = new WhImplUtil();
		OamMepInfo oamMep = new OamMepInfo();
		oamMep.setMel(pwObject.getMel());
		oamMep.setLocalMepId(pwObject.getSourceMEP());
		oamMep.setRemoteMepId(pwObject.getEquityMEP());
		oamMep.setCv(pwObject.getCvEnabl().equals("1")?true:false);
		oamMep.setCvCycle(this.getCycleCode(pwObject.getCvCycle()));
		oamMep.setCvReserve1(Integer.parseInt(pwObject.getCvReserve()));
		oamMep.setCsfEnable("1".equals(pwObject.getCsfEnabl()));
		oamMep.setMegIcc(whImplUtil.synchroMeg(pwObject.getMegIcc()));
		oamMep.setMegUmc(whImplUtil.synchroMeg(pwObject.getMegUmc()));
		oamMep.setLspTc((UiUtil.getCodeByValue("TC", pwObject.getLspTc() + "")).getId());
		oamMep.setPwTc((UiUtil.getCodeByValue("TC", pwObject.getPwTc() + "")).getId());
		oamMep.setSiteId(siteId);
		oamMep.setObjId(pwObject.getPwId());
		oamMep.setObjType("PW");
		oamMep.setLm(pwObject.getLm()==1?true:false);
		oamMep.setOamEnable(pwObject.getOamEnable()==1);
		if (pwObject.getRole() == 0) {
			oam.setOamType(OamTypeEnum.AMEP);
		} else {
			oam.setOamType(OamTypeEnum.ZMEP);
		}

		oam.setOamMep(oamMep);
		oamInfoList.add(oam);
		return oamInfoList;
	}

//	private OperationObject getSynchroOperationObject(int siteId, String type) throws Exception {
//		OperationObject operationObject = null;
//		ActionObject actionObject = null;
//		NEObject neObject = null;
//		try {
//			operationObject = new OperationObject();
//			WhImplUtil whImplUtil = new WhImplUtil();
//			neObject = whImplUtil.siteIdToNeObject(siteId);
//			actionObject = new ActionObject();
//			actionObject.setActionId(super.getActionId(operationObject.getActionObjectList()));
//			actionObject.setNeObject(neObject);
//			actionObject.setType(type);
//			operationObject.getActionObjectList().add(actionObject);
//		} catch (Exception e) {
//			throw e;
//		} finally {
//			actionObject = null;
//			neObject = null;
//		}
//		return operationObject;
//	}

	/**
	 * 生成megumcID
	 * 
	 * @param tunnelObject
	 * @param megId
	 */
	public void getMegIccAndMegUmc(PwObject pwObject, String megICC, String megUCC) {
		pwObject.setMegIcc(getMeg(megICC));
		pwObject.setMegUmc(getMeg(megUCC));
	}

	/**
	 * 生成6位meg
	 * 
	 * @param meg
	 * @return
	 */
	public String getMeg(String meg) {
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
	 * 同步连通性周期
	 * 
	 * @param cycle
	 * @return
	 */
	public int getCycleCode(String cycle) {
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

	public List<QosInfo> getQosInfos(PwObject pwObject, int siteId) {
		List<QosInfo> qosInfos = new ArrayList<QosInfo>();
		QosInfo qosInfo = new QosInfo();
		qosInfo.setSiteId(siteId);
		qosInfo.setQosType(QosTemplateTypeEnum.LLSP.toString());
		qosInfo.setDirection(EQosDirection.FORWARD.getValue() + "");
		qosInfo.setColorSence(pwObject.getCm());
		qosInfo.setCir(pwObject.getCir() * 1000);
		qosInfo.setCbs(pwObject.getCbs());
		qosInfo.setPir(pwObject.getPir() * 1000);
		qosInfo.setPbs(pwObject.getPbs());
		qosInfo.setCos(5);
		qosInfo.setQosname("pw_" + pwObject.getPwId());
		qosInfo.setStatus(2);
		qosInfos.add(qosInfo);
		QosInfo qosBackInfo = new QosInfo();
		qosBackInfo.setSiteId(siteId);
		qosBackInfo.setQosType(QosTemplateTypeEnum.LLSP.toString());
		qosBackInfo.setColorSence(pwObject.getCm());
		qosBackInfo.setCir(pwObject.getCir() * 1000);
		qosBackInfo.setCbs(pwObject.getCbs());
		qosBackInfo.setPir(pwObject.getPir() * 1000);
		qosBackInfo.setPbs(pwObject.getPbs());
		qosBackInfo.setCos(5);
		qosBackInfo.setQosname("pw_" + pwObject.getPwId());
		qosBackInfo.setStatus(2);
		qosBackInfo.setDirection(EQosDirection.BACKWARD.getValue() + "");
		qosInfos.add(qosBackInfo);
		return qosInfos;
	}
}
