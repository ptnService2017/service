﻿package com.nms.service.impl.dispatch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nms.db.bean.equipment.manager.UpgradeManage;
import com.nms.db.bean.equipment.shelf.SiteInst;
import com.nms.db.bean.path.Segment;
import com.nms.db.bean.ptn.SiteStatusInfo;
import com.nms.db.bean.ptn.oamStatus.OamStatusInfo;
import com.nms.db.bean.system.code.Code;
import com.nms.db.enums.EManufacturer;
import com.nms.model.equipment.shlef.SiteService_MB;
import com.nms.model.util.Services;
import com.nms.service.OperationServiceI;
import com.nms.service.impl.base.DispatchBase;
import com.nms.service.impl.cx.SiteCXServiceImpl;
import com.nms.service.impl.dispatch.rmi.SiteDispatchI;
import com.nms.service.impl.util.DataDownLoad;
import com.nms.service.impl.util.ResultString;
import com.nms.service.impl.util.SiteUtil;
import com.nms.service.impl.util.TypeAndActionUtil;
import com.nms.service.impl.wh.SiteWHServiceImpl;
import com.nms.service.notify.Message.MessageType;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.ResourceUtil;
import com.nms.ui.manager.UiUtil;
import com.nms.ui.manager.keys.StringKeysLbl;
import com.nms.ui.manager.keys.StringKeysTip;

public class SiteDispatch extends DispatchBase implements SiteDispatchI {

	/**
	 * 登陆所有网元
	 * 
	 * @throws Exception
	 */
	public void siteLogin() throws Exception {

		SiteService_MB siteService = null;
		List<SiteInst> siteInstList = null;
		try {
			siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE);
			siteInstList = siteService.select();
			this.siteLogin(siteInstList);
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			UiUtil.closeService_MB(siteService);
		}
	}

	/**
	 * 登陆指定网元
	 * 
	 * @param siteIdList
	 * @throws Exception
	 */
	public void siteLogin(List<SiteInst> siteInstList) throws Exception {
		List<SiteInst> siteInst_cx = new ArrayList<SiteInst>();
		List<SiteInst> siteInst_wh = new ArrayList<SiteInst>();
		String manufacturer = null;
		SiteCXServiceImpl siteCXServiceImpl = null;
		try {
			List<SiteInst> siteInstLoginList = new ArrayList<SiteInst>() ;
			for(SiteInst siteInst:siteInstList)
			if("0".equals(UiUtil.getCodeById(siteInst.getSiteType()).getCodeValue())){
				siteInstLoginList.add(siteInst);
			}
			siteCXServiceImpl = new SiteCXServiceImpl();
			for (SiteInst siteInst : siteInstLoginList) {
				manufacturer = UiUtil.getCodeById(Integer.parseInt(siteInst.getCellEditon())).getCodeName();
//				if ("700+系列".equals(manufacturer) || "700+Series".equals(manufacturer)) {
					siteInst_wh.add(siteInst);
//				} else {
//					siteInst_cx.add(siteInst);
//				}
			}

			if (siteInst_cx.size() > 0) {
				siteCXServiceImpl.siteLogin(siteInst_cx);
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			siteInst_cx = null;
			siteInst_wh = null;
			manufacturer = null;
			siteCXServiceImpl = null;
		}
	}

	/**
	 * 登出所有网元
	 * 
	 * @throws Exception
	 */
	public void siteLogout() throws Exception {
		SiteService_MB siteService = null;
		List<SiteInst> siteInstList = null;
		List<SiteInst> siteInst_cx = new ArrayList<SiteInst>();
		List<SiteInst> siteInst_wh = new ArrayList<SiteInst>();
		String manufacturer = null;
		SiteCXServiceImpl siteCXServiceImpl = new SiteCXServiceImpl();
		try {
			siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE);
			siteInstList = siteService.select();
			List<SiteInst> siteInstLoginList = new ArrayList<SiteInst>() ;
			for(SiteInst siteInst:siteInstList)
				if("0".equals(UiUtil.getCodeById(siteInst.getSiteType()).getCodeValue())){
					siteInstLoginList.add(siteInst);
				}
			if(null!=siteInstLoginList){
				for (SiteInst siteInst : siteInstLoginList) {
//					manufacturer = UiUtil.getCodeById(Integer.parseInt(siteInst.getCellEditon())).getCodeName();
//					if ("700+系列".equals(manufacturer) || "700+series".equals(manufacturer)) {
						siteInst_wh.add(siteInst);
//					} else {
//						siteInst_cx.add(siteInst);
//					}
				}

				if (siteInst_cx.size() > 0) {
					siteCXServiceImpl.siteLogout(siteInst_cx);
				}
			}
			

		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			UiUtil.closeService_MB(siteService);
		}
	}

	/**
	 * 创建网元
	 * 
	 * @param siteInst
	 * @return
	 * @throws Exception
	 */
	public String excuteInsert(Object object) throws Exception {
		OperationServiceI operationServiceI = null;
		Code code= null;
		String result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_FAIL);
		int msgType = 0 ;
		SiteInst instBefore = null;
		SiteInst siteInst=null;
		try {
			if (object == null) {
				throw new Exception("acPortInfo is null");
			}
			siteInst=(SiteInst) object;
			msgType = siteInst.getSite_Inst_Id();
			//虚拟网元操作
			SiteUtil siteUtil=new SiteUtil();
			instBefore =(SiteInst) siteUtil.selectSiteAction(null,siteInst.getSite_Inst_Id());
			code = UiUtil.getCodeById(Integer.parseInt(siteInst.getCellEditon()));

			if ("0".equals(code.getCodeValue())) {
				operationServiceI=new SiteWHServiceImpl();
			} else {
				String siteCheck =(String) siteUtil.irtualSiteAction(TypeAndActionUtil.ACTION_SAVEANDUPDATE,siteInst);
				if(null!=siteCheck){
					if (msgType > 0 && null !=instBefore) {
						Map<String, String> attributes = new HashMap<String, String>();
						if (!siteInst.getCellId().equals(instBefore.getCellId())) {
							attributes.put("userLabel", siteInst.getCellId());
						}
						if (!siteInst.getCreateUser().equals(instBefore.getCreateUser())) {
							attributes.put("createUser", siteInst.getCreateUser());
						}
						if (siteInst.getIsGateway() != instBefore.getIsGateway()) {
							attributes.put("isGateway", siteInst.getIsGateway()+"");
						}
						attributes.put("id", siteInst.getSite_Inst_Id()+"");
						super.notifyCorba("managedElement", MessageType.ATTRIBUTECHG, attributes,"",ResultString.CONFIG_SUCCESS);
					}else {
						super.notifyCorba("managedElement", MessageType.CREATION, siteInst,"",ResultString.CONFIG_SUCCESS);
					}
					return siteCheck;
				}
				operationServiceI = new SiteCXServiceImpl();
				if (msgType > 0) {
					SiteCXServiceImpl siteCXServiceImpl = new SiteCXServiceImpl();
					instBefore = siteCXServiceImpl.select(siteInst.getSite_Inst_Id());
				}
			}
			result = operationServiceI.excutionInsert(siteInst);
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			code = null;
			operationServiceI = null;
		}
		if (ResultString.CONFIG_SUCCESS.equals(result)) {
			if (msgType > 0 && null !=instBefore) {
				Map<String, String> attributes = new HashMap<String, String>();
				if (!siteInst.getCellId().equals(instBefore.getCellId())) {
					attributes.put("userLabel", siteInst.getCellId());
				}
				if (!siteInst.getCreateUser().equals(instBefore.getCreateUser())) {
					attributes.put("createUser", siteInst.getCreateUser());
				}
				if (siteInst.getIsGateway() != instBefore.getIsGateway()) {
					attributes.put("isGateway", siteInst.getIsGateway()+"");
				}
				attributes.put("id", siteInst.getSite_Inst_Id()+"");
				super.notifyCorba("managedElement", MessageType.ATTRIBUTECHG, attributes,"",ResultString.CONFIG_SUCCESS);
			}else {
				super.notifyCorba("managedElement", MessageType.CREATION, siteInst,"",ResultString.CONFIG_SUCCESS);
			}
			return ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
		} else {
			return result;
		}
	}

	/**
	 * 查询网元信息
	 * 
	 * @param siteId
	 * @return
	 * @throws Exception
	 */
	public SiteInst selectSite(int siteId) throws Exception {
		int manufacturer = 0;
		SiteCXServiceImpl siteCXServiceImpl = null;
		SiteInst siteInst = null;
		SiteWHServiceImpl siteWHServiceImpl = null;
		try {
			
			manufacturer = super.getManufacturer(siteId);

			if (manufacturer==EManufacturer.WUHAN.getValue()) {
				siteWHServiceImpl = new SiteWHServiceImpl();
				siteInst = siteWHServiceImpl.select(siteId);
			} else {
				siteCXServiceImpl = new SiteCXServiceImpl();
				siteInst = siteCXServiceImpl.select(siteId);
			}

		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			siteCXServiceImpl = null;
		}
		return siteInst;
	}

	/**
	 * 修改网元信息
	 * 
	 * @param siteInst
	 * @return
	 * @throws Exception
	 */
	public String excuteUpdate(Object object) throws Exception {

		Code code = null;
		OperationServiceI operationServiceI = null;
		String result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_FAIL);
		SiteInst instBefore = null;

		SiteInst siteInst=null;
		try {
			if (object == null) {
				throw new Exception("acPortInfo is null");
			}
			siteInst=(SiteInst) object;
			//虚拟网元操作
			SiteUtil siteUtil=new SiteUtil();
			instBefore =(SiteInst) siteUtil.selectSiteAction(null,siteInst.getSite_Inst_Id());
			
			code = UiUtil.getCodeById(Integer.parseInt(siteInst.getCellEditon()));
			if ("0".equals(code.getCodeValue())) {
				operationServiceI = new SiteWHServiceImpl();
			} else {
				String siteCheck =(String) siteUtil.irtualSiteAction(TypeAndActionUtil.ACTION_UPDATE,siteInst);
				if(null!=siteCheck){
					Map<String, String> attributes = new HashMap<String, String>();
					if (!siteInst.getCellId().equals(instBefore.getCellId())) {
						attributes.put("userLabel", siteInst.getName());
					}
					if (!siteInst.getCreateUser().equals(instBefore.getCreateUser())) {
						attributes.put("createUser", siteInst.getCreateUser());
					}
					if (siteInst.getIsGateway() != instBefore.getIsGateway()) {
						attributes.put("isGateway", siteInst.getIsGateway()+"");
					}
					attributes.put("id", siteInst.getSite_Inst_Id()+"");
					super.notifyCorba("managedElement", MessageType.ATTRIBUTECHG, attributes,"",ResultString.CONFIG_SUCCESS);
					return siteCheck;
				}
				operationServiceI = new SiteCXServiceImpl();
				SiteCXServiceImpl siteCXServiceImpl = new SiteCXServiceImpl();
				instBefore = siteCXServiceImpl.select(siteInst.getSite_Inst_Id());
			}
			result = operationServiceI.excutionUpdate(siteInst);
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			code = null;
			operationServiceI = null;
		}
		if (ResultString.CONFIG_SUCCESS.equals(result)) {
			Map<String, String> attributes = new HashMap<String, String>();
			if (!siteInst.getCellId().equals(instBefore.getCellId())) {
				attributes.put("userLabel", siteInst.getCellId());
			}
			if (!siteInst.getCreateUser().equals(instBefore.getCreateUser())) {
				attributes.put("createUser", siteInst.getCreateUser());
			}
			if (siteInst.getIsGateway() != instBefore.getIsGateway()) {
				attributes.put("createUser", siteInst.getCreateUser());
			}
			attributes.put("id", siteInst.getSite_Inst_Id()+"");
			super.notifyCorba("managedElement", MessageType.ATTRIBUTECHG, attributes,"",ResultString.CONFIG_SUCCESS);
			return ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
		} else {
			return result;
		}
	}

	/**
	 * 校时
	 * @param siteInst
	 * @return
	 * @throws Exception
	 */
	public String currectTime(SiteInst siteInst) throws Exception{
		SiteWHServiceImpl operationServiceI = null;
		String result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_FAIL);

		try {
			if (siteInst == null) {
				throw new Exception("siteInst is null");
			}
			if(super.getManufacturer(siteInst.getSite_Inst_Id()) == EManufacturer.WUHAN.getValue()){
				operationServiceI = new SiteWHServiceImpl();
				result = operationServiceI.currectTime(siteInst);
			}

		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			operationServiceI = null;
		}
		if (ResultString.CONFIG_SUCCESS.equals(result)) {
			return ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
		} else {
			return result;
		}
	}
	
	/**
	 * 网元初始化
	 * @param siteInst
	 * @return
	 * @throws Exception
	 */
	public String clearSite(SiteInst siteInst) throws Exception{
		String manufacturer = null;
		SiteWHServiceImpl operationServiceI = null;
		SiteCXServiceImpl siteCXServiceImpl=null;
		String result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_FAIL);
		
		try {
			if (siteInst == null) {
				throw new Exception("siteInst is null");
			}
			//虚拟网元操作
			SiteUtil siteUtil = new SiteUtil();
			String siteCheck =(String) siteUtil.irtualSiteAction(TypeAndActionUtil.ACTION_DELETE,siteInst);
			if(null!=siteCheck){
				return siteCheck;
			}
			
			manufacturer = UiUtil.getCodeById(Integer.parseInt(siteInst.getCellEditon())).getCodeName();
//			if (manufacturer.equals("700+系列")) {
				operationServiceI = new SiteWHServiceImpl();
				result = operationServiceI.clearSite(siteInst);
//			} else {
//				siteCXServiceImpl=new SiteCXServiceImpl();
//				result = siteCXServiceImpl.initialize(siteInst);
//			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			manufacturer = null;
			operationServiceI = null;
		}
		if (ResultString.CONFIG_SUCCESS.equals(result)) {
			return ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
		} else {
			return result;
		}
	}
	
	
	/**
	 * 网元上载
	 * @param siteInst
	 * @return
	 */
	public byte[] uploadConfig(SiteInst siteInst){
		int manufacturer = 0;
		SiteCXServiceImpl siteCXServiceImpl = null;
		SiteWHServiceImpl siteWHServiceImpl = null;
		byte[] result = null;
		try {
			manufacturer = super.getManufacturer(siteInst.getSite_Inst_Id());

			if (manufacturer==EManufacturer.WUHAN.getValue()) {
				siteWHServiceImpl = new SiteWHServiceImpl();
				result = siteWHServiceImpl.uploadConfig(siteInst);
			} else {
				siteCXServiceImpl = new SiteCXServiceImpl();
//				siteInst = siteCXServiceImpl.select(siteId);
			}

		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			siteCXServiceImpl = null;
		}
		return result;
	}
	
	/**
	 * 网元下载
	 * @param siteInst
	 * @return
	 */
	public String downloadConfig(SiteInst siteInst){
		int manufacturer = 0;
		SiteCXServiceImpl siteCXServiceImpl = null;
		SiteWHServiceImpl siteWHServiceImpl = null;
		String result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_FAIL);
		try {
			manufacturer = super.getManufacturer(siteInst.getSite_Inst_Id());

			if (manufacturer==EManufacturer.WUHAN.getValue()) {
				siteWHServiceImpl = new SiteWHServiceImpl();
				result = siteWHServiceImpl.downloadConfig(siteInst);
			} else {
				siteCXServiceImpl = new SiteCXServiceImpl();
//				siteInst = siteCXServiceImpl.select(siteId);
			}

		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			siteCXServiceImpl = null;
		}
		return result;
	}
	
	/**
	 * 网元搜索
	 */
	public List<SiteInst> siteSearch(String ip,int manufacturer){
		List<SiteInst> siteInstList = new ArrayList<SiteInst>() ;
		SiteCXServiceImpl siteCXServiceImpl = null;
		SiteWHServiceImpl siteWHServiceImpl = null;
		try {
			if (manufacturer==EManufacturer.WUHAN.getValue()) {
				siteWHServiceImpl = new SiteWHServiceImpl();
	//			siteInstList = siteWHServiceImpl.siteSearch(ip);
			} else {
				siteCXServiceImpl = new SiteCXServiceImpl();
				siteInstList = siteCXServiceImpl.siteSearch(ip);
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			siteCXServiceImpl = null;
		}
		return siteInstList;
	}
	
	/**
	 * 查询网元某状态
	 * @param siteInst
	 * @return
	 */
	public SiteStatusInfo siteStatus(SiteInst siteInst){
		int manufacturer = 0;
		SiteCXServiceImpl siteCXServiceImpl = null;
		SiteWHServiceImpl siteWHServiceImpl = null;
		SiteStatusInfo result = null;
		try {
			manufacturer = super.getManufacturer(siteInst.getSite_Inst_Id());

			if (manufacturer==EManufacturer.WUHAN.getValue()) {
				siteWHServiceImpl = new SiteWHServiceImpl();
				result = siteWHServiceImpl.siteStatus(siteInst);
			} else {
				siteCXServiceImpl = new SiteCXServiceImpl();
//				siteInst = siteCXServiceImpl.select(siteId);
			}

		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			siteCXServiceImpl = null;
		}
		return result;
	}
	/**
	 * 数据下载
	 * @param a  要下载数据的类型
	 * @param siteInstList 网元集合
	 * @throws Exception 
	 */
	public String dataDownLoadActionPerformed(List<SiteInst> siteInstList, int[] downLoadSelected) throws Exception {
		List<SiteInst> actionList = new ArrayList<SiteInst>();
		for(SiteInst siteInst:siteInstList){
			if(siteInst.getLoginstatus()==1){
				actionList.add(siteInst);
			}
		}
		if(actionList.isEmpty()){
			return ResourceUtil.srcStr(StringKeysTip.TIP_UP_DOWN);
		}else{
			DataDownLoad dataDownLoad = new DataDownLoad();
			return dataDownLoad.dataDownLoadAction(actionList, downLoadSelected);
		}
	}

	/**
	 * 删除网元操作
	 */
	public String excuteDelete(Object object) throws RemoteException, Exception {
		
		SiteService_MB siteService = null;
		String resultStr = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_FAIL);;
		
		try {
			
			siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE);
			SiteInst siteInst = (SiteInst)object;
			int result=siteService.delete(siteInst);
			if (result >0 ) {
				resultStr = ResultString.CONFIG_SUCCESS ;
			}
			super.notifyCorba("managedElement", MessageType.DELETION, siteInst,"",resultStr);
			
		} catch (Exception e) {
			throw e;
		}finally{
			UiUtil.closeService_MB(siteService);
		}
		return resultStr;
	}

	@Override
	public String synchro(int siteId) throws RemoteException, Exception {
		return null;
	}

	/**
	 * 查询网元某状态
	 * @param siteInst
	 * @return
	 */
	public OamStatusInfo oamStatus(SiteInst siteInst){
		int manufacturer = 0;
		SiteCXServiceImpl siteCXServiceImpl = null;
		SiteWHServiceImpl siteWHServiceImpl = null;
		OamStatusInfo result = null;
		try {
			siteWHServiceImpl = new SiteWHServiceImpl();
			result = siteWHServiceImpl.oamStatus(siteInst);

		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			siteCXServiceImpl = null;
		}
		return result;
	}
	/**
	 * function:用于创建或删除丢弃流
	 * @param siteInst
	 * @return
	 */
	public String createOrDeleteDiscardFlow(SiteInst siteInst){
		SiteWHServiceImpl siteWHServiceImpl  = new SiteWHServiceImpl();
		SiteService_MB siteService = null;
		String result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_FAIL);
		SiteInst siteInfo = null;
		try {
			siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE);
			siteInfo = siteService.select(siteInst.getSite_Inst_Id());
			siteService.saveOrUpdate(siteInst);
			result = siteWHServiceImpl.createOrDeleteDiscardFlow(siteInst);
			if(!result.equals(ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS))){
				siteService.saveOrUpdate(siteInfo);
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			UiUtil.closeService_MB(siteService);
		}
		if (ResultString.CONFIG_SUCCESS.equals(result)) {
			String str = "";
			if(siteInst != null && siteInst.getLoginstatus() == 0){
				str = siteInst.getCellId();
			}
			if(str.equals("")){
				return  ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}else{
				return  ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS)+","+str+ResultString.NOT_ONLINE_SUCCESS;
			}
		}else{
			return result;
		}
	}
	
	/**
	 * 查询或
	 * @param siteInst
	 * @return
	 */
	public List<UpgradeManage> softwareSummary(SiteInst siteInst){
		SiteWHServiceImpl siteWHServiceImpl = null;
		List<UpgradeManage> result = null;
		try {
			siteWHServiceImpl = new SiteWHServiceImpl();
			result = siteWHServiceImpl.querySummary(siteInst);
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
		}
		return result;
	}

	/**
	 * 下发软件摘要
	 * @param siteInst
	 * @return
	 */
	public String sendSummary(SiteInst siteInst){
		SiteWHServiceImpl siteWHServiceImpl = null;
		String result = null;
		try {
			siteWHServiceImpl = new SiteWHServiceImpl();
			result = siteWHServiceImpl.sendSummary(siteInst);
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
		}
		return result;
	}
	
	/**
	 * 用于取消软件升级
	 * @param siteInst 网元对象
	 * 
	 */
	public String cancelSlftWare(SiteInst siteInst) throws RemoteException, Exception {
		SiteWHServiceImpl siteWHService = null;
		String result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_FAIL);
		try {
			siteWHService = new SiteWHServiceImpl();
			result = siteWHService.cancelSoftware(siteInst);
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return result;
	}
	
	/**
	 * 查询SN(包含本地或相邻)
	 * @param siteInst
	 * @return
	 */
	public List<SiteInst> querySn(SiteInst siteInst,int isLocaltion){
		SiteWHServiceImpl siteWHService = null;
		List<SiteInst> siteInsts = null;
		try {
			siteWHService = new SiteWHServiceImpl();
			siteInsts = siteWHService.querySn(siteInst,isLocaltion);
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return siteInsts;
	}
	
	/**
	 * 设置网元IP
	 * @param siteInst
	 * @return
	 */
	public String setSiteIP(SiteInst siteInst){
		String result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_FAIL);
		try {
			SiteWHServiceImpl siteWHService = new SiteWHServiceImpl();
			result = siteWHService.setSiteIP(siteInst);
		} catch (Exception e) {
			ExceptionManage.dispose(e, getClass());
		}
		return result;
	}

	/**
	 * 拓扑自动发现
	 */
//	public String topologicalLinkFound(List<SiteInst> siteInsts, int netWorkId,List<Segment> seg) throws RemoteException {
//		String result =ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_FAIL);		
//		try {
//			SiteWHServiceImpl siteWHService = new SiteWHServiceImpl();
//			result = siteWHService.topologicalLinkFound(siteInsts, netWorkId,seg);
//		} catch (Exception e) {
//			ExceptionManage.dispose(e, getClass());
//		}
//		return result;
//	}
	
	
	public List<Segment> topologicalLinkFound(List<SiteInst> siteInsts, int netWorkId,List<Segment> seg) throws RemoteException {
		String result =ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_FAIL);		
		try {
			SiteWHServiceImpl siteWHService = new SiteWHServiceImpl();
			seg = siteWHService.topologicalLinkFound(siteInsts, netWorkId,seg);
		} catch (Exception e) {
			ExceptionManage.dispose(e, getClass());
		}
		return seg;
	}
	
	@Override
	public Object consistence(int siteId) throws RemoteException, Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Object pingCMD(String neIP) throws RemoteException {
		String result = "";
		try {
			String command = "ping "+neIP;
			Process p = Runtime.getRuntime().exec(command);
			if(p != null){
				BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
	            String inline;
	            while ((inline = br.readLine()) != null) {
	            	if(!"".equals(inline)){
	            		result += inline+"/";
	            	}
	            }
	            br.close();
	            p.destroy();
			}
		} catch (IOException e) {
			return "";
		}
		return result;
	}

	/**
	 * 定时重启
	 */
	@Override
	public String taskRboot(List<SiteInst> siteInsts) throws RemoteException {
		String result ="";
		SiteWHServiceImpl siteWHServiceImpl = new SiteWHServiceImpl();
		SiteService_MB siteService = null;
		try {
			siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE);
			for(SiteInst siteInst : siteInsts){
				result = siteWHServiceImpl.taskRboot(siteInst);
			}
			siteService.updateBatch(siteInsts);
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}finally{
			UiUtil.closeService_MB(siteService);
		}
		return result;
	}
	
	
	   
	 /** 
     * 读取分析结果 
     * 
     * @param pattern 
     * @return 
     */  
	 public String readUntil(InputStream in,String pattern)  
	    {  
	    	 StringBuffer sb1 = new StringBuffer(); 
	        try  
	        {  	        		  
	            char lastChar = pattern.charAt(pattern.length() - 1);  
	            StringBuffer sb = new StringBuffer();  
	             char ch = (char)in.read();  
	            
	            while (true)  
	            {  
	                sb.append(ch);  
	                if (ch == lastChar)  
	                {  
	                    if (sb.toString().endsWith(pattern))  
	                    {  
	                       
	                    	return sb.toString();  
	                    }  
	                }  
	                ch = (char)in.read();  
	               sb1.append(ch);
	            } 

	        }catch (Exception e){  
	            e.printStackTrace();  
	        }
	       
	        return null;  
	    }

	@Override
	public String routeIn() throws RemoteException {
		String result ="";
		SiteWHServiceImpl siteWHServiceImpl = new SiteWHServiceImpl();
		try {
			result = siteWHServiceImpl.routeIn();
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return result;
	}

	public String vlanMac(SiteInst siteInst,List<String> value)throws RemoteException{
		String result ="";
		SiteWHServiceImpl siteWHServiceImpl = new SiteWHServiceImpl();
		try {
			result = siteWHServiceImpl.vlanMac(siteInst,value);
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return result;
	}
  
   
	@Override
	public String getVersion() throws RemoteException, Exception {
		String version=ResourceUtil.srcStr(StringKeysLbl.LBL_JLABTL2_VERSIONS);
		return version;
	}

}
