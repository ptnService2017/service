﻿package com.nms.service.impl.wh;

import java.util.ArrayList;
import java.util.List;

import com.nms.db.bean.equipment.shelf.SiteInst;
import com.nms.db.bean.ptn.ecn.OSPFinfo_wh;
import com.nms.drive.service.PtnServiceEnum;
import com.nms.drive.service.bean.NEObject;
import com.nms.drive.service.bean.OSPFinfoWhObeject;
import com.nms.model.equipment.shlef.SiteService_MB;
import com.nms.model.ptn.ecn.Ospf_whService_MB;
import com.nms.model.util.Services;
import com.nms.service.OperationServiceI;
import com.nms.service.bean.ActionObject;
import com.nms.service.bean.OperationObject;
import com.nms.service.impl.base.WHOperationBase;
import com.nms.service.impl.util.ResultString;
import com.nms.service.impl.util.SiteUtil;
import com.nms.service.impl.util.SynchroUtil;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.UiUtil;

public class OSPFinfoWHServiceImpl extends WHOperationBase implements OperationServiceI{

	@Override
	public String excutionDelete(List objectList) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String excutionInsert(Object object) throws Exception {
		
		
		Ospf_whService_MB ospfWhServiceMB = null;
		OperationObject operationObjectAfter = null;
		OperationObject operationObjectResult = null;
		NEObject neObject = null;
		String reslut = ResultString.CONFIG_TIMEOUT;
		List<OSPFinfoWhObeject> ospFinfoWhObejects = null;
		String ip = "";
		SiteService_MB siteServiceMB = null;
		try {
			ospfWhServiceMB = (Ospf_whService_MB) ConstantUtil.serviceFactory.newService_MB(Services.OSPF_WH);
			List<OSPFinfo_wh> ospFinfoWhs = (List<OSPFinfo_wh>) object;
			int siteId = ospFinfoWhs.get(0).getSiteId();
			neObject = this.getNeObject(siteId);
			operationObjectAfter = new OperationObject();
			ospFinfoWhObejects = this.getOSPFinfoWhObeject(ospFinfoWhs);
			super.sendAction(operationObjectAfter, neObject, ospFinfoWhObejects, PtnServiceEnum.OSPFWH);//下发全局配置块
			operationObjectResult = super.verification(operationObjectAfter,12000);//获取设备返回信息
			if (operationObjectResult.isSuccess()) {
				ospfWhServiceMB.insert(ospFinfoWhs);
				reslut = operationObjectResult.getActionObjectList().get(0).getStatus();
				for (int i = 0; i < ospFinfoWhs.size(); i++) {
					if(ospFinfoWhs.get(i).getPortType() == 1 && ospFinfoWhs.get(i).getOspfType()==11){
						ip = ospFinfoWhs.get(i).getIp();
						siteServiceMB = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE);
						SiteInst siteInst = siteServiceMB.selectById(siteId);
						siteInst.setCellDescribe(ip);
						siteInst.setSwich((Integer.parseInt(ip.split("\\.")[2])*256+Integer.parseInt(ip.split("\\.")[3]))+"");
						siteServiceMB.updateSite(siteInst);
					}
				}
			} else {//失败
				reslut = super.getErrorMessage(operationObjectResult);
			}
			if(ospFinfoWhs.size() == 0 && reslut.equals(ResultString.CONFIG_SUCCESS)){
				ospfWhServiceMB.deleteBySiteId(siteId);
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			UiUtil.closeService_MB(ospfWhServiceMB);
			UiUtil.closeService_MB(siteServiceMB);
		}
		return reslut;
	
	}

	private List<OSPFinfoWhObeject> getOSPFinfoWhObeject(List<OSPFinfo_wh> ospFinfoWhs){
		List<OSPFinfoWhObeject> OSPFinfoWhObejects = new ArrayList<OSPFinfoWhObeject>();
		if(ospFinfoWhs.get(0).isHas()){
			for (int i = 0; i < ospFinfoWhs.size(); i++) {
				OSPFinfoWhObeject ospFinfoWhObeject = new OSPFinfoWhObeject();
				ospFinfoWhObeject.setEnable(ospFinfoWhs.get(i).getEnable());
				ospFinfoWhObeject.setFolw(ospFinfoWhs.get(i).getFolw());
				ospFinfoWhObeject.setIp(ospFinfoWhs.get(i).getIp());
				ospFinfoWhObeject.setMask(ospFinfoWhs.get(i).getMask());
				ospFinfoWhObeject.setOspfType(ospFinfoWhs.get(i).getOspfType());
				ospFinfoWhObeject.setPortModel(ospFinfoWhs.get(i).getPortModel());
				ospFinfoWhObeject.setPortType(ospFinfoWhs.get(i).getPortType());
				ospFinfoWhObeject.setVlanValues(ospFinfoWhs.get(i).getVlanValues());
				OSPFinfoWhObejects.add(ospFinfoWhObeject);
			}
		}	
		return OSPFinfoWhObejects;
	}
	
	private NEObject getNeObject(int siteID){
		NEObject neObject = new NEObject();
		SiteService_MB siteServiceMB = null;
		try {
			siteServiceMB = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE);
			SiteInst siteInst = siteServiceMB.selectById(siteID);
			if(siteInst.getRootIP() != null && !siteInst.getRootIP().equals("")){
				neObject.setManageIP(siteInst.getRootIP());
			}else{
				neObject.setManageIP("255.255.255.255");
			}
			neObject.setSn(siteInst.getSn());
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}finally{
			UiUtil.closeService_MB(siteServiceMB);
		}
		return neObject;
	}
	
	@Override
	public String excutionUpdate(Object object) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object synchro(int siteId) throws Exception {
		OperationObject operationObjectAfter = null;
		OperationObject operationObjectResult = null;
		String reslut = ResultString.CONFIG_TIMEOUT;
		try {
			operationObjectAfter = getSynchroOperationObject(siteId, "ospfSynchro");
			super.sendAction(operationObjectAfter);
			operationObjectResult = super.verification(operationObjectAfter);//获取设备返回信息
			if (operationObjectResult.isSuccess()) {
				SynchroUtil synchroUtil = new SynchroUtil();
				for (ActionObject actionObject : operationObjectResult.getActionObjectList()) {
					synchroUtil.ospfSynchro(this.getOSPFinfo_wh(actionObject.getOspFinfoWhObejects(),siteId),siteId);
				}
				reslut = ResultString.CONFIG_SUCCESS;
			}else{
				reslut = operationObjectResult.getActionObjectList().get(0).getStatus();
			}	
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
		}
		return reslut;
	}

	/**
	 * 封装同步下发数据
	 * @param siteId
	 * @return operationObject
	 * @throws Exception
	 */
	protected OperationObject getSynchroOperationObject(int siteId,String type) throws Exception {
		OperationObject operationObject = null;
		ActionObject actionObject = null;
		NEObject neObject = null;
		try {
			operationObject = new OperationObject();
			neObject = this.getNeObject(siteId);
			actionObject = new ActionObject();
			actionObject.setActionId(this.getActionId(operationObject.getActionObjectList()));
			actionObject.setNeObject(neObject);
			actionObject.setType(type);
			operationObject.getActionObjectList().add(actionObject);
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			actionObject = null;
			actionObject = null;
			neObject = null;
		}
		return operationObject;
	}
	
	private List<OSPFinfo_wh> getOSPFinfo_wh(List<OSPFinfoWhObeject> ospFinfoWhObjects,int siteId){
		List<OSPFinfo_wh> OSPFinfoWhs = new ArrayList<OSPFinfo_wh>();
		for (int i = 0; i < ospFinfoWhObjects.size(); i++) {
			OSPFinfo_wh ospFinfoWh = new OSPFinfo_wh();
			ospFinfoWh.setSiteId(siteId);
			ospFinfoWh.setEnable(ospFinfoWhObjects.get(i).getEnable());
			ospFinfoWh.setFolw(ospFinfoWhObjects.get(i).getFolw());
			ospFinfoWh.setIp(ospFinfoWhObjects.get(i).getIp());
			ospFinfoWh.setMask(ospFinfoWhObjects.get(i).getMask());
			ospFinfoWh.setOspfType(ospFinfoWhObjects.get(i).getOspfType());
			ospFinfoWh.setPortModel(ospFinfoWhObjects.get(i).getPortModel());
			ospFinfoWh.setPortType(ospFinfoWhObjects.get(i).getPortType());
			ospFinfoWh.setVlanValues(ospFinfoWhObjects.get(i).getVlanValues());
			OSPFinfoWhs.add(ospFinfoWh);
		}
		return OSPFinfoWhs;
	}
}
