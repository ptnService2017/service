﻿package com.nms.service.impl.base;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.nms.drive.service.PtnServiceEnum;
import com.nms.drive.service.bean.ClockObject;
import com.nms.drive.service.bean.E1Object;
import com.nms.drive.service.bean.EthLoopObject;
import com.nms.drive.service.bean.GlobalObject;
import com.nms.drive.service.bean.NEObject;
import com.nms.drive.service.bean.TimeSyncObject;
import com.nms.drive.service.bean.status.PingOrderControllerObject;
import com.nms.model.system.SiteLockService_MB;
import com.nms.model.util.Services;
import com.nms.service.bean.ActionObject;
import com.nms.service.bean.OperationObject;
import com.nms.service.impl.util.WhImplUtil;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.UiUtil;

public class WHOperationBase extends OperationBase {
	
	/**执行时间*/
	public Date date;
	
	/**
	 * 验证是否成功
	 * @param operationObject
	 * @throws InterruptedException 
	 */
	public OperationObject verification(OperationObject operationObject) throws InterruptedException{
		while(true){
			if(!super.isTimeOut(this.date)){
				boolean flag=true;
				for(ActionObject actionObject : operationObject.getActionObjectList()){
					if(null==actionObject.getStatus() || actionObject.getStatus().equals("")){
						//System.out.println("------设备超时");
						flag=false;
					}
				}
				operationObject.setErrorLabel(0);
				if(flag){
					System.out.println("成功了");
					operationObject.setSuccess(this.isAllSuccess(operationObject));
					return operationObject;
				}
			}else{
				//System.out.println("------自动超时");
				System.out.println("超時了");
				operationObject.setSuccess(false);
				operationObject.setErrorLabel(1);
				return operationObject;
			}
			Thread.sleep(1000);
		}
	}
	
	/**
	 * 设置超时时间
	 * @param operationObject
	 * @param time
	 * @return
	 * @throws InterruptedException
	 */
	public OperationObject verification(OperationObject operationObject,int time) throws InterruptedException{
		while(true){
			if(!super.isTimeOut(this.date,time)){
				boolean flag=true;
				for(ActionObject actionObject : operationObject.getActionObjectList()){
					if(null==actionObject.getStatus() || actionObject.getStatus().equals("")){
						//System.out.println("------设备超时");
						flag=false;
					}
				}
				operationObject.setErrorLabel(0);
				if(flag){
					System.out.println("成功了");
					operationObject.setSuccess(this.isAllSuccess(operationObject));
					return operationObject;
				}
			}else{
				//System.out.println("------自动超时");
				System.out.println("超時了");
				operationObject.setSuccess(false);
				operationObject.setErrorLabel(1);
				return operationObject;
			}
			Thread.sleep(1000);
		}
	}
	
	/**
	 * 验证是否全是成功的
	 * @return
	 */
	private boolean isAllSuccess(OperationObject operationObject){
		
		boolean flag=true;
		for(ActionObject actionObject : operationObject.getActionObjectList()){
			if(!actionObject.getStatus().equals("配置成功")){
				flag=false;
				break;
			}
		}
		return flag;
	}
	
	
	
	
	/**
	 * 获取actionid
	 * @param actionObjectList
	 * @return
	 */
	public int getActionId(List<ActionObject> actionObjectList){
		
		Random random = new Random();
        int number = Math.abs(random.nextInt())%1000;
		
		if(actionObjectList != null && actionObjectList.size()>0){
			boolean flag=true;
			for(ActionObject actionObject : actionObjectList){
				if(actionObject.getActionId() == number){
					flag=false;
					break;
				}
			}
			if(flag){
				return number;
			}else{
				return this.getActionId(actionObjectList);
			}
			
		}else{
			return number;
		}
	}
	
	/**
	 * 发送
	 * @param operationObject
	 * @throws Exception 
	 */
	public void sendAction(OperationObject operationObject) throws Exception{
		this.date=new Date();
		for(ActionObject actionObject : operationObject.getActionObjectList()){
			//下发配置
			if(actionObject.getType() == PtnServiceEnum.TUNNEL.toString()){
				ConstantUtil.driveService.updataTunnal(operationObject, actionObject);
			}else if(actionObject.getType() == PtnServiceEnum.LSP_PROTECT.toString()){
				ConstantUtil.driveService.updataLSPProtection(operationObject, actionObject);
			}else if(actionObject.getType().equals("port")){
				ConstantUtil.driveService.updatePortConfig(operationObject, actionObject);
			}else if(actionObject.getType().equals("pw")){
				ConstantUtil.driveService.updataPW(operationObject, actionObject);
			}else if(actionObject.getType().equals("eline")){
				ConstantUtil.driveService.updataEline(operationObject, actionObject);
			}else if(actionObject.getType().equals("etree")){
				ConstantUtil.driveService.updataETree(operationObject, actionObject);
			}else if(actionObject.getType().equals("elan")){
				ConstantUtil.driveService.updataELan(operationObject, actionObject);
			}else if(actionObject.getType().equals("queryPerforBySite")){
				ConstantUtil.driveService.queryAllPerformanceObject(operationObject, actionObject);
			}else if(actionObject.getType().equals("hisPerformanceByCard")){
				ConstantUtil.driveService.queryPerformanceObjectByCard(operationObject, actionObject);
			}else if(actionObject.getType().equals("hisPerformanceBySite")){
				ConstantUtil.driveService.queryAllPerformanceObject(operationObject, actionObject);
			}else if(actionObject.getType().equals("AdministrateConfig")){
				ConstantUtil.driveService.updateManagement(operationObject, actionObject);
			}else if(actionObject.getType().equals("queryCurrAlarmBySlot")){
				ConstantUtil.driveService.queryAllAlarmObjectByCard(operationObject, actionObject);
			}else if(actionObject.getType().equals("queryCurrAlarmBySite")){
				ConstantUtil.driveService.queryAllAlarmObject(operationObject, actionObject);
			}else if(actionObject.getType().equals("allConfig")){
				ConstantUtil.driveService.updateGloble(operationObject, actionObject);
			}else if(actionObject.getType().equals("segmentOam")){
				ConstantUtil.driveService.updataTmsOamObject(operationObject, actionObject);
			}else if(actionObject.getType().equals("portLag")){
				ConstantUtil.driveService.updatePortLAG(operationObject, actionObject);
			}else if(actionObject.getType().equals("e1")){
				ConstantUtil.driveService.updataE1(operationObject, actionObject);
			}else if(actionObject.getType().equals("protectRorate")){
				ConstantUtil.driveService.updateProtectRorate(operationObject, actionObject);
			}else if(actionObject.getType().equals("site")){
				ConstantUtil.driveService.updateSite(operationObject, actionObject);
			}else if(actionObject.getType().equals("softwareUpdate")){
				ConstantUtil.driveService.updateSoftwareUpdate(operationObject, actionObject);
			}else if(actionObject.getType().equals("slotreset")){
				ConstantUtil.driveService.updateSlotReset(operationObject, actionObject);
			}else if(actionObject.getType().equals("tdmloopback")){
				ConstantUtil.driveService.updateTdmLoopBack(operationObject, actionObject);
			}else if(actionObject.getType().equals("currecttime")){
				ConstantUtil.driveService.updateCurrectTime(operationObject, actionObject);
			}else if(actionObject.getType().equals("wrapping")){
				ConstantUtil.driveService.updateRoundProtection(operationObject, actionObject);
			}else if(actionObject.getType().equals("tmpOAMConfig")){
				ConstantUtil.driveService.updataTMPOAMBugControl(operationObject, actionObject);
			}else if(actionObject.getType().equals("tmsOAMConfig")){
				ConstantUtil.driveService.updataTMSOAMBugControl(operationObject, actionObject);
			}else if(actionObject.getType().equals("siteAttribute")){
				ConstantUtil.driveService.querySiteAttribute(operationObject, actionObject);
			}else if(actionObject.getType().equals("phbToexp")){
				ConstantUtil.driveService.updatePHBToEXP(operationObject, actionObject);
			}else if(actionObject.getType().equals("expTophb")){
				ConstantUtil.driveService.updateEXPToPHB(operationObject, actionObject);
			}else if(actionObject.getType().equals("clockFreque")){
				ConstantUtil.driveService.updataClockObject(operationObject, actionObject);
			}else if(actionObject.getType().equals("clearSite")){
				ConstantUtil.driveService.updateClearSite(operationObject, actionObject);
			}else if(actionObject.getType().equals("tmcOAMConfig")){
				ConstantUtil.driveService.updataTMCOAMBugControl(operationObject, actionObject);
			}else if(actionObject.getType().equals("uploadConfig")){
				ConstantUtil.driveService.uploadSite(operationObject, actionObject);
			}else if(actionObject.getType().equals("clockRorateFreque")){
				ConstantUtil.driveService.clockRorate(operationObject, actionObject);
			}else if(actionObject.getType().equals("Static")){
				ConstantUtil.driveService.updateStaticUnicast(operationObject, actionObject);
			}else if(actionObject.getType().equals("group")){
				ConstantUtil.driveService.updateGroupSpread(operationObject, actionObject);
			}else if(actionObject.getType().equals("downloadConfig")){
				ConstantUtil.driveService.downloadSite(operationObject, actionObject);
			}else if(actionObject.getType().equals("siteStatus")){
				ConstantUtil.driveService.querySiteStatus(operationObject, actionObject,actionObject.getNeObject().getStatusMark());
			}else if(actionObject.getType().equals("allStatus")){
				ConstantUtil.driveService.querySiteStatus(operationObject, actionObject,actionObject.getNeObject().getStatusMark());
			}else if(actionObject.getType().equals("ethOAMConfig")){
				ConstantUtil.driveService.updateETHoamConfig(operationObject, actionObject);
			}else if(actionObject.getType().equals("cleanPerforBySite")){
				ConstantUtil.driveService.cleanCurrPerformanceBySite(operationObject,actionObject);
			}else if(actionObject.getType().equals("qinq")){
				ConstantUtil.driveService.updataQinQ(operationObject,actionObject);
			}else if(actionObject.getType().equals("pmValueLimite")){
				ConstantUtil.driveService.updataPmLimite(operationObject,actionObject);
			}else if(actionObject.getType().equals("ethLinkOAMConfig")){
				ConstantUtil.driveService.updateETHLinkOAM(operationObject, actionObject);
			}else if(actionObject.getType().equals("blackListMac")){
				ConstantUtil.driveService.updateMacManage(operationObject, actionObject);
			}else if(actionObject.getType().equals("oamStatus")){
				ConstantUtil.driveService.queryOamStatus(operationObject, actionObject);
			}else if(actionObject.getType().equals("ACLConfig")){
				ConstantUtil.driveService.updateAcl(operationObject, actionObject);
			}else if(actionObject.getType().equals("macLearningLimit")){
				ConstantUtil.driveService.updateMacLearning(operationObject, actionObject);
			}else if(actionObject.getType().equals("discardFlow")){
				ConstantUtil.driveService.updateCreateOrDeleteDiscardFlow(operationObject, actionObject);
			}else if(actionObject.getType().equals("querySoftwareSummary")){
				ConstantUtil.driveService.querySoftwareSummary(operationObject, actionObject);
			}else if(actionObject.getType().equals("v35port")){
				ConstantUtil.driveService.updateV35Port(operationObject, actionObject);
			}else if(actionObject.getType().equals("loopProRorate")){
				ConstantUtil.driveService.updateLoopProRorate(operationObject, actionObject);
			}else if(actionObject.getType().equals("smartFan")){
				ConstantUtil.driveService.updateSmartFan(operationObject, actionObject);
			}else if(actionObject.getType().equals("cancelSoftWare")){
				ConstantUtil.driveService.cancelSoftware(operationObject, actionObject);
			}else if(actionObject.getType().equals("queryLocaltionSn")){
				ConstantUtil.driveService.querySn(operationObject, actionObject,1);
			}else if(actionObject.getType().equals("queryAdjoinSn")){
				ConstantUtil.driveService.querySn(operationObject, actionObject,2);
			}else if(actionObject.getType().equals("setremoteIP")){
				ConstantUtil.driveService.setIP(operationObject, actionObject,2);
			}else if(actionObject.getType().equals("setIP")){
				ConstantUtil.driveService.setIP(operationObject, actionObject,1);
			}else if(actionObject.getType().equals("setSN")){
				ConstantUtil.driveService.setSN(operationObject, actionObject,3);
			}else if(actionObject.getType().equals("setRemoteSN")){
				ConstantUtil.driveService.setSN(operationObject, actionObject,4);
			}else if(actionObject.getType().equals("ETHSERVICE")){
				ConstantUtil.driveService.updateEthService(operationObject, actionObject);
			}else if(actionObject.getType().equals("queryShieldAlarm")){
				ConstantUtil.driveService.updateShieldAlarm(operationObject, actionObject);
			}else if(actionObject.getType().equals("L2CPConfig")){
				ConstantUtil.driveService.updateL2CP(operationObject, actionObject);
			}else if(actionObject.getType().equals("port_pri")){
				ConstantUtil.driveService.updatePortPri(operationObject, actionObject);
			}else if(actionObject.getType().equals("portDiscard")){
				ConstantUtil.driveService.updatePortDiscord(operationObject, actionObject);
			}else if(actionObject.getType().equals("portDiscardFlow")){
				ConstantUtil.driveService.updatePortVlan(operationObject, actionObject);
			}else if(actionObject.getType().equals("secondMacStudy")){
				ConstantUtil.driveService.updateSecondMacStudy(operationObject, actionObject);
			}else if(actionObject.getType().equals("port2layer")){
				ConstantUtil.driveService.updatePort2layer(operationObject, actionObject);
			}else if(actionObject.getType().equals("taskCurrecttime")){
				ConstantUtil.driveService.updateRestartTime(operationObject, actionObject);
			}else if(actionObject.getType().equals("queryRemoteSn")){
				ConstantUtil.driveService.querySn(operationObject, actionObject,3);
			}else if(actionObject.getType().equals("ccc")){
				ConstantUtil.driveService.updataCcc(operationObject, actionObject);
			}else if(actionObject.getType().equals("bfdManagement")){
				ConstantUtil.driveService.updataBfd(operationObject, actionObject);
			}else if(actionObject.getType().equals("arpManagement")){
				ConstantUtil.driveService.updateARP(operationObject, actionObject);
			}else if(actionObject.getType().equals("routeIn")){
				ConstantUtil.driveService.updateRoute(operationObject, actionObject);
			}else if(actionObject.getType().equals("mspw")){
				ConstantUtil.driveService.updateMsPW(operationObject, actionObject);
			}
			
			//同步配置
			else if(actionObject.getType().equals("tunnelSynchro")){
				ConstantUtil.driveService.queryBusinessObjectByCard(operationObject, actionObject,6);
			}else if(actionObject.getType().equals("elanSynchro")){
				ConstantUtil.driveService.queryBusinessObjectByCard(operationObject, actionObject,8);
			}else if(actionObject.getType().equals("pwSynchro")){
				ConstantUtil.driveService.queryBusinessObjectByCard(operationObject, actionObject,19);
			}else if(actionObject.getType().equals("elineSynchro")){
				ConstantUtil.driveService.queryBusinessObjectByCard(operationObject, actionObject,7);
			}else if(actionObject.getType().equals("etreeSynchro")){
				ConstantUtil.driveService.queryBusinessObjectByCard(operationObject, actionObject,8);
			}else if(actionObject.getType().equals("tunnelProtectSynchro")){
				ConstantUtil.driveService.queryBusinessObjectByCard(operationObject, actionObject,5);
			}else if(actionObject.getType().equals("e1Synchro")){
				ConstantUtil.driveService.queryBusinessObjectByCard(operationObject, actionObject,24);
			}else if(actionObject.getType().equals("portLagSynchro")){
				ConstantUtil.driveService.queryBusinessObjectByCard(operationObject, actionObject,20);
			}else if(actionObject.getType().equals("portSynchro")){
				ConstantUtil.driveService.queryBusinessObjectByCard(operationObject, actionObject,21);
			}else if(actionObject.getType().equals("tmsOamSynchro")){
				ConstantUtil.driveService.queryBusinessObjectByCard(operationObject, actionObject,26);
			}else if(actionObject.getType().equals("ethlinkOamSynchro")){
				ConstantUtil.driveService.queryBusinessObjectByCard(operationObject, actionObject,27);
			}else if(actionObject.getType().equals("ethOamSynchro")){
				ConstantUtil.driveService.queryBusinessObjectByCard(operationObject, actionObject,31);
			}else if(actionObject.getType().equals("v35portSynchro")){
				ConstantUtil.driveService.queryBusinessObjectByCard(operationObject, actionObject,38);
			}else if(actionObject.getType().equals("smartFanSynchro")){
				ConstantUtil.driveService.queryBusinessObjectByCard(operationObject, actionObject,56);
			}else if(actionObject.getType().equals("ethServiceSynchro")){
				ConstantUtil.driveService.queryBusinessObjectByCard(operationObject, actionObject,57);
			}else if(actionObject.getType().equals("portPriSynchro")){
				ConstantUtil.driveService.queryBusinessObjectByCard(operationObject, actionObject,60);
			}else if(actionObject.getType().equals("portDiscardSynchro")){
				ConstantUtil.driveService.queryBusinessObjectByCard(operationObject, actionObject,61);
			}else if(actionObject.getType().equals("mspwSynchro")){
			ConstantUtil.driveService.queryBusinessObjectByCard(operationObject, actionObject,30);
			}else if(actionObject.getType().equals("staticUniSynchro")){
				ConstantUtil.driveService.queryBusinessObjectByCard(operationObject, actionObject,11);
			}else if(actionObject.getType().equals("allConfigSynchro")){
				ConstantUtil.driveService.queryBusinessObjectByCard(operationObject, actionObject,3);
			}else if(actionObject.getType().equals("dualSynchro")){
				ConstantUtil.driveService.queryBusinessObjectByCard(operationObject, actionObject,14);
			}else if(actionObject.getType().equals("clockFrequSynchro")){
				ConstantUtil.driveService.queryBusinessObjectByCard(operationObject, actionObject,29);
			}else if(actionObject.getType().equals("port2LayerSynchro")){
				ConstantUtil.driveService.queryBusinessObjectByCard(operationObject, actionObject,62);
			}else if(actionObject.getType().equals("phbMexpSynchro")){
				ConstantUtil.driveService.queryBusinessObjectByCard(operationObject, actionObject,9);
			}else if(actionObject.getType().equals("exp2phbSynchro")){
				ConstantUtil.driveService.queryBusinessObjectByCard(operationObject, actionObject,10);
			}else if(actionObject.getType().equals("pmlimiteSynchro")){
				ConstantUtil.driveService.queryBusinessObjectByCard(operationObject, actionObject,32);
			}else if(actionObject.getType().equals("aclSynchro")){
				ConstantUtil.driveService.queryBusinessObjectByCard(operationObject, actionObject,37);
			}else if(actionObject.getType().equals("secondMacStudySynchro")){
				ConstantUtil.driveService.queryBusinessObjectByCard(operationObject, actionObject,63);
			}else if(actionObject.getType().equals("loopProtectSynchro")){
				ConstantUtil.driveService.queryBusinessObjectByCard(operationObject, actionObject,4);
			}else if(actionObject.getType().equals("timeSynchro")){
				ConstantUtil.driveService.queryBusinessObjectByCard(operationObject, actionObject,28);
			}else if(actionObject.getType().equals("l2cpSynchro")){
				ConstantUtil.driveService.queryBusinessObjectByCard(operationObject, actionObject,59);
			}else if(actionObject.getType().equals("blackmaclistSynchro")){
				ConstantUtil.driveService.queryBusinessObjectByCard(operationObject, actionObject,35);
			}else if(actionObject.getType().equals("blackwhitemacSynchro")){
				ConstantUtil.driveService.queryBusinessObjectByCard(operationObject, actionObject,36);
			}else if(actionObject.getType().equals("cccSynchro")){
				ConstantUtil.driveService.queryBusinessObjectByCard(operationObject, actionObject,64);
			}else if(actionObject.getType().equals("bfdSynchro")){
				ConstantUtil.driveService.queryBusinessObjectByCard(operationObject, actionObject,65);
			}else if(actionObject.getType().equals("arpSynchro")){
				ConstantUtil.driveService.queryBusinessObjectByCard(operationObject, actionObject,66);
			}else if(actionObject.getType().equals("ospfSynchro")){
				ConstantUtil.driveService.ospfSyn(operationObject, actionObject);
			}else if(actionObject.getType().equals("macvlan")){
				ConstantUtil.driveService.macVlan(operationObject, actionObject);
			}
			
		}
	}
	
	/**
	 * 回滚
	 * @param operationObjectResult 验证后的结果。 从中选择已经成功的进行回滚
	 * @param operationObjectBefore 下发之前的配置
	 * @return
	 * @throws Exception
	 */
	public boolean rollback(OperationObject operationObjectResult , Map<Integer, ActionObject> operationObjectBefore ) throws Exception{
		
		OperationObject operationObjectRollback=new OperationObject();
		for(ActionObject actionObject : operationObjectResult.getActionObjectList()){
			if(actionObject.getStatus()==null || actionObject.getStatus().equals("配置成功")){
				operationObjectRollback.getActionObjectList().add(operationObjectBefore.get(actionObject.getNeObject().getNeAddress()));
			}
		}
		
		this.sendAction(operationObjectRollback);
		operationObjectResult=this.verification(operationObjectRollback);
		return operationObjectResult.isSuccess();
	}
	
	/**
	 * 锁网元
	 * @param siteIdList
	 * @throws Exception 
	 */
	public void lockSite(List<Integer> siteIdList,String lockType) throws Exception{
		SiteLockService_MB siteLockService=null;
		try {
			siteLockService=(SiteLockService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITELOCK);
			siteLockService.save(siteIdList, lockType);
		} catch (Exception e) {
			throw e;
		}finally{
			UiUtil.closeService_MB(siteLockService);
		}
	}
	
	/**
	 * 解锁
	 * @param siteIdList
	 * @throws Exception
	 */
	public void clearLock(List<Integer> siteIdList) throws Exception{
		SiteLockService_MB siteLockService=null;
		try {
			siteLockService=(SiteLockService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITELOCK);
			siteLockService.updateClear(siteIdList);
		} catch (Exception e) {
			throw e;
		}finally{
			UiUtil.closeService_MB(siteLockService);
		}
	}
	
	/**
	 * 暂时去掉网元被锁的功能，此功能待日后完善
	 * @param siteIdList
	 * @return
	 * @throws Exception 
	 */
	public boolean isLockBySiteIdList(List<Integer> siteIdList) throws Exception {
		SiteLockService_MB siteLockService = null;
		try {
			siteLockService = (SiteLockService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITELOCK);
			for(int siteId : siteIdList){
				if(siteLockService.isLock(siteId)){
					return false;
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			UiUtil.closeService_MB(siteLockService);
		}
		return false;		
	}
	
	/**
	 * 获取失败消息
	 * @return
	 */
	public String getErrorMessage(OperationObject operationObject){
		
		String result="";
		
		for(ActionObject actionObject : operationObject.getActionObjectList()){
			if(actionObject.getStatus()==null||actionObject.getStatus().equals("")){
				result="超时";
				break;
			}
			if(!actionObject.getStatus().equals("配置成功")){
				result = actionObject.getStatus();
				break;
			}
		}
		return result;
	}
	
	/**
	 * 获取当前毫秒数
	 * 
	 * @author kk
	 * 
	 * @return
	 * 
	 * @Exception 异常对象
	 */
	public String getNowMS() {

		Date date = new Date();
		return date.getTime() + "";

	}
	
	/**
	 * 下发设备
	 * @param operationObject:回调对象
	 * @param neObject:网元对象
	 * @param objectList:下发的对象集合
	 * @param type:下发的对象类型
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void sendAction(OperationObject operationObject,NEObject neObject,List objectList,PtnServiceEnum objectType){
		this.date=new Date();//获取下发时间
		ActionObject actionObject = new ActionObject();
		this.pottingOperationObecjt(operationObject, neObject, actionObject);//将驱动事件对象，放入回调对象中
		try {
			if(objectType == PtnServiceEnum.TUNNEL){//下发tunnel
				actionObject.setTunnelObjectList(objectList);
				ConstantUtil.driveService.updataTunnal(operationObject, actionObject);
			}else if (objectType == PtnServiceEnum.LSP_PROTECT){//下发lsp1:1保护
				actionObject.setLSPProtectionList(objectList);
				ConstantUtil.driveService.updataLSPProtection(operationObject, actionObject);
			}else if (objectType == PtnServiceEnum.CLOCK){//下发时钟配置
				ClockObject clockObject = (ClockObject) objectList.get(0);
				actionObject.setClockObject(clockObject);
				ConstantUtil.driveService.updataClockObject(operationObject, actionObject);
			}else if (objectType == PtnServiceEnum.ALLCONFIG){//下发全局配置块
				GlobalObject globalObject = (GlobalObject) objectList.get(0);
				actionObject.setGlobalObject(globalObject);
				ConstantUtil.driveService.updateGloble(operationObject, actionObject);
			}else if (objectType == PtnServiceEnum.TIMESYNC){//时钟同步管理配置块
				TimeSyncObject timeSyncObject = (TimeSyncObject) objectList.get(0);
				actionObject.setTimesyncobject(timeSyncObject);
				ConstantUtil.driveService.updateTimeSync(operationObject, actionObject);
			}else if (objectType == PtnServiceEnum.E1){//下发E1配置块
				E1Object e1Object = (E1Object) objectList.get(0);
				actionObject.setE1Object(e1Object);
				ConstantUtil.driveService.updataE1(operationObject, actionObject);
			}else if (objectType == PtnServiceEnum.ETREE){//下发ETREE配置块
				actionObject.setEtreeObjectList(objectList);
				ConstantUtil.driveService.updataETree(operationObject, actionObject);
			}else if(objectType == PtnServiceEnum.ELAN){//下发ELAN配置块
				actionObject.setElanObejctList(objectList);
				ConstantUtil.driveService.updataELan(operationObject, actionObject);
			}else if(objectType == PtnServiceEnum.ELINE){//下发ELINE配置块
				actionObject.setElineObjectList(objectList);
				ConstantUtil.driveService.updataEline(operationObject, actionObject);
			}else if(objectType == PtnServiceEnum.PWPROTECT){//下发TMC通道保护配置块
				actionObject.setTmcTunnelProectList(objectList);
				ConstantUtil.driveService.updateTMCTunnelProtect(operationObject, actionObject);
			}else if(objectType == PtnServiceEnum.OAMPING){//oam状态监视控制命令
				actionObject.setPingOrderControllerObject((PingOrderControllerObject) objectList.get(0));
				ConstantUtil.driveService.updateOamPing(operationObject, actionObject);
			}else if(objectType == PtnServiceEnum.ETHLOOP){//以太网环回
				EthLoopObject ethLoopObject = (EthLoopObject) objectList.get(0);
				actionObject.setEthLoopObject(ethLoopObject);
				ConstantUtil.driveService.updateEthLoop(operationObject, actionObject);
			}else if(objectType == PtnServiceEnum.BLACKWHITEMAC){//MAC地址的黑白名单
				actionObject.setBlackWhiteMacObjectList(objectList);
				ConstantUtil.driveService.updateBlackWhiteMac(operationObject, actionObject);
			}else if(objectType == PtnServiceEnum.PW){//PW
				actionObject.setPwObjectList(objectList);
				ConstantUtil.driveService.updataPW(operationObject, actionObject);
			}else if(objectType == PtnServiceEnum.MSPW){//MSPW
				actionObject.setMsPwObjects(objectList);
				ConstantUtil.driveService.updateMsPW(operationObject, actionObject);
			}else if(objectType == PtnServiceEnum.OSPFWH){//OSPFWH
				actionObject.setOspFinfoWhObejects(objectList);
				ConstantUtil.driveService.setOSPF(operationObject, actionObject);
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
	}
	
	/**
	 * 将驱动事件对象，放入回调对象中
	 * @param operationObject:回调对象
	 * @param neObject:网元对象
	 * @param actionObject:驱动事件对象
	 */
	private void pottingOperationObecjt(OperationObject operationObject,NEObject neObject,ActionObject actionObject){
		List<ActionObject> actionObjects = new ArrayList<ActionObject>();
		actionObjects.add(actionObject);
		actionObject.setActionId(this.getActionId(actionObjects));
		actionObject.setNeObject(neObject);
		operationObject.setActionObjectList(actionObjects);
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
		WhImplUtil whImplUtil = null;
		try {
		    whImplUtil = new WhImplUtil();
			operationObject = new OperationObject();
			neObject = whImplUtil.siteIdToNeObject(siteId);
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
			whImplUtil = null;
		}
		return operationObject;
	}
}