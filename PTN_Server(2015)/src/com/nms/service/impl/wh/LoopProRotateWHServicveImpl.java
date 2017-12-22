package com.nms.service.impl.wh;

import java.util.ArrayList;
import java.util.List;

import com.nms.db.bean.equipment.port.PortInst;
import com.nms.db.bean.ptn.path.protect.LoopProRotate;
import com.nms.db.bean.ptn.path.protect.LoopProtectInfo;
import com.nms.db.enums.EActiveStatus;
import com.nms.drive.service.bean.LoopProRotateObject;
import com.nms.drive.service.bean.NEObject;
import com.nms.drive.service.bean.RoundProtectionObject;
import com.nms.model.equipment.port.PortService_MB;
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

public class LoopProRotateWHServicveImpl extends WHOperationBase implements OperationServiceI {

	@Override
	public String excutionDelete(List objectList) throws Exception {
		return null;
	}

	@Override
	public String excutionInsert(Object object) throws Exception {
		return null;
	}

	@Override
	public String excutionUpdate(Object object) throws Exception {
		try {
			List<Integer> isSuccess = new ArrayList<Integer>();
			LoopProRotate rotate = (LoopProRotate)object;
			List<Integer> siteIdList = rotate.getSiteIdList();
			if(siteIdList.size()>0){
				super.lockSite(siteIdList, SiteLockTypeUtil.PORTLAG_INSERT);
				for (int i = 0; i < siteIdList.size(); i++) {
					OperationObject	operationObjectAfter = this.getOperationObject(rotate, siteIdList.get(i));
					super.sendAction(operationObjectAfter);
					OperationObject operationObjectResult = super.verification(operationObjectAfter);
					if(operationObjectResult.isSuccess()){
						super.clearLock(siteIdList);
						if(operationObjectResult.getActionObjectList().get(0).getStatus().equals(ResultString.CONFIG_SUCCESS)){
							isSuccess.add(i);
						}
					}else{
						super.clearLock(siteIdList);
					}
				}
				if(isSuccess.size() == siteIdList.size()){
					return ResultString.CONFIG_SUCCESS;
				}else{
					return ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_FAIL);			
				}
			}else{
				return ResultString.CONFIG_SUCCESS;
			}
		}catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return ResultString.CONFIG_SUCCESS;
	}

	private OperationObject getOperationObject(LoopProRotate rotate, int siteId) {
		OperationObject operationObject = new OperationObject();
		try {
			ActionObject actionObject = new ActionObject();
			SiteUtil siteUtil=new SiteUtil();
			if(0 == siteUtil.SiteTypeUtil(siteId)){
				WhImplUtil whImplUtil = new WhImplUtil();
				NEObject neObject = whImplUtil.siteIdToNeObject(siteId);
				actionObject.setActionId(super.getActionId(operationObject.getActionObjectList()));
				actionObject.setNeObject(neObject);
				actionObject.setType("loopProRorate");
	 			actionObject.setLoopProRotateObject(this.getLoopProRotateObject(rotate, siteId));
				operationObject.getActionObjectList().add(actionObject);
			}else{
				actionObject.setStatus(ResultString.CONFIG_SUCCESS);
				operationObject.getActionObjectList().add(actionObject);
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return operationObject;
	}

	private LoopProRotateObject getLoopProRotateObject(LoopProRotate rotate, int siteId) {
		LoopProRotateObject loopObj = new LoopProRotateObject();
		loopObj.setActionType(rotate.getActionType());
		loopObj.setDirection(rotate.getDirection());
		loopObj.setRingId(rotate.getRingId());
		loopObj.setSiteId(siteId);
		return loopObj;
	}

	@Override
	public Object synchro(int siteId) throws Exception {
		OperationObject operationObject = new OperationObject();
		try {
			operationObject = super.getSynchroOperationObject(siteId, "loopProtectSynchro");
			super.sendAction(operationObject);//下发数据
			OperationObject	operationObjectResult = super.verification(operationObject);
			if(operationObjectResult.isSuccess()){ 
				/*成功，则与数据库进行同步*/					
				for(ActionObject actionObject : operationObjectResult.getActionObjectList()){
					this.synchro_db(actionObject.getRoundProtectionObject(),siteId);
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return ResultString.CONFIG_SUCCESS;
	}
	/**
	 * 与数据库进行同步
	 * @param staticUnicast
	 * @param siteId
	 * @throws Exception
	 */
	private void synchro_db(List<RoundProtectionObject> roundProtectionObjectList, int siteId)throws Exception {
		List<LoopProtectInfo> loopProtectInfoList= null;
		try {
			loopProtectInfoList = this.getLoopProtectObject(roundProtectionObjectList,siteId);
			SynchroUtil synchroUtil = new SynchroUtil();
			for(LoopProtectInfo loopProtectInfo : loopProtectInfoList){
				synchroUtil.loopsProtectSynchro(loopProtectInfo,siteId);
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}finally{
			loopProtectInfoList = null;
		}
		
	}
	
	
	
	private List<LoopProtectInfo> getLoopProtectObject(List<RoundProtectionObject> roundProtectionObjectList2, int siteId) {		
		PortService_MB portService = null;
		List<LoopProtectInfo> loopProtectInfoList  = new ArrayList<LoopProtectInfo>();
		try {			
			LoopProtectInfo loopProtectInfo = null;
			portService = (PortService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORT);
			for(RoundProtectionObject loopObj : roundProtectionObjectList2){				
					loopProtectInfo = new LoopProtectInfo();					
					PortInst westportInst = new PortInst();
					PortInst eastPortInst = new PortInst();
					eastPortInst.setNumber(loopObj.getEastPort());
					eastPortInst.setSiteId(siteId);
					eastPortInst = portService.select(eastPortInst).get(0);
					loopProtectInfo.setEastPort(eastPortInst.getPortId());
					westportInst.setNumber(loopObj.getWestProt());
					westportInst.setSiteId(siteId);
					westportInst = portService.select(westportInst).get(0);
					loopProtectInfo.setWestPort(westportInst.getPortId());
					loopProtectInfo.setDelaytime(loopObj.getDelayTime());
					loopProtectInfo.setEastLspId(loopObj.getEastLspId());
					loopProtectInfo.setEastSlot(loopObj.getEastSlot());
					loopProtectInfo.setLogicId(loopObj.getLogicRoundID());
					loopProtectInfo.setLoopNodeNumber(loopObj.getNodeCount());
					loopProtectInfo.setNodeId(loopObj.getNodeID());
					loopProtectInfo.setProtectType(loopObj.getProtectType());
					loopProtectInfo.setBackType(loopObj.getReturnType());
					loopProtectInfo.setLoopId(loopObj.getRoundID());
					loopProtectInfo.setTargetNodeId(loopObj.getTargetNodeId());
					loopProtectInfo.setWestLspId(loopObj.getWestLspId());
					loopProtectInfo.setWestSlot(loopObj.getWestSlot());
					loopProtectInfo.setWaittime(loopObj.getWaitTime());
					loopProtectInfo.setIsSingle(1);
					loopProtectInfo.setActiveStatus(EActiveStatus.ACTIVITY.getValue());
					loopProtectInfoList.add(loopProtectInfo);
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}finally{
			UiUtil.closeService_MB(portService);
	    }
			return loopProtectInfoList;
	}
			
			
				
}
