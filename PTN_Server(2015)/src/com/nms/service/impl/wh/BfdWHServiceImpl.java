package com.nms.service.impl.wh;

import java.util.ArrayList;
import java.util.List;

import com.nms.db.bean.ptn.BfdInfo;
import com.nms.db.enums.EManufacturer;
import com.nms.drive.service.bean.BfdObject;
import com.nms.drive.service.bean.NEObject;
import com.nms.model.ptn.BfdInfoService_MB;
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

public class BfdWHServiceImpl extends WHOperationBase implements OperationServiceI {

	@Override
	public String excutionDelete(List objectList) throws Exception {
		List<BfdInfo> bfdInfoList = null;
		List<Integer> siteIdList = null;
		OperationObject operationObject = null;
		OperationObject operationObjectResult = null;
		BfdInfoService_MB bfdInfoService = null;
		try {
			bfdInfoList = objectList;
			siteIdList = new ArrayList<Integer>();
			siteIdList.add(bfdInfoList.get(0).getSiteId());
			if (super.isLockBySiteIdList(siteIdList)) {
				return ResourceUtil.srcStr(StringKeysTip.TIP_SITE_LOCK);
			}
			super.lockSite(siteIdList, SiteLockTypeUtil.BFD_DELETE);//锁住网元
			siteIdList = new ArrayList<Integer>();
			bfdInfoService = (BfdInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.BFDMANAGEMENT);		
			bfdInfoService.delete(bfdInfoList);
			// 下发设备
			if(super.getManufacturer(bfdInfoList.get(0).getSiteId()) == EManufacturer.WUHAN.getValue()){
				operationObject = this.getOperationObject(bfdInfoList.get(0).getSiteId(),"bfdManagement");
				super.sendAction(operationObject);
				operationObjectResult = super.verification(operationObject);
				if (operationObjectResult.isSuccess()) {
					return operationObjectResult.getActionObjectList().get(0).getStatus();
				} else {
					// 如果失败 将还原删除的数据
					for(BfdInfo rollBackAclInfo:bfdInfoList){
						bfdInfoService.insert(rollBackAclInfo);
					}
					return super.getErrorMessage(operationObjectResult);
				}
			}
			
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			super.clearLock(siteIdList);
			siteIdList=null;
			bfdInfoList = null;
			operationObjectResult=null;
			operationObject=null;
			UiUtil.closeService_MB(bfdInfoService);
		}
		return ResultString.CONFIG_SUCCESS;			

	}

	@Override
	public String excutionInsert(Object object) throws Exception {
		BfdInfo  bfdInfo = null;
		List<Integer> siteIdList = null;
		OperationObject operationObjectAfter = null;
		OperationObject operationObjectResult = null;
		BfdInfoService_MB bfdInfoService = null;
		int inserResule = 0;		
		try {
			bfdInfo=(BfdInfo) object;
			bfdInfoService =(BfdInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.BFDMANAGEMENT);			
			siteIdList = new ArrayList<Integer>();
			siteIdList.add(bfdInfo.getSiteId());
			if(super.getManufacturer(bfdInfo.getSiteId()) == EManufacturer.WUHAN.getValue()){
				super.lockSite(siteIdList, SiteLockTypeUtil.BFD_INSERT);
				inserResule = bfdInfoService.insert(bfdInfo);
				// 下设备
				operationObjectAfter = this.getOperationObject(bfdInfo.getSiteId(),"bfdManagement");
				super.sendAction(operationObjectAfter);
				operationObjectResult = super.verification(operationObjectAfter);	
				if (operationObjectResult.isSuccess()) {						
					return operationObjectResult.getActionObjectList().get(0).getStatus();
				} else {
					// 下设备失败时删除新建的数据
			
						BfdInfo bfdInfoOther = new BfdInfo();
						bfdInfoOther.setSiteId(bfdInfo.getSiteId());
						bfdInfoOther.setId(inserResule);
				        List<BfdInfo> bfdList=new ArrayList<BfdInfo>();
				        bfdList.add(bfdInfoOther);
						bfdInfoService.delete(bfdList);					
					return super.getErrorMessage(operationObjectResult);
				}
			}else{
				return ResultString.CONFIG_SUCCESS;
			}
			
		} catch (Exception e) {
			throw e;
		} finally {
			super.clearLock(siteIdList);
			UiUtil.closeService_MB(bfdInfoService);
			operationObjectAfter = null;
			operationObjectResult = null;
			siteIdList=null;
			bfdInfo=null;
		}
		}
			
		
			
	private OperationObject getOperationObject(int siteId,String type) {
		OperationObject operationObject = null;
		NEObject neObject = null;
		ActionObject actionObject = null;
		try {
			WhImplUtil whImplUtil = new WhImplUtil();
			SiteUtil siteUtil=new SiteUtil();			
			operationObject = new OperationObject();
			neObject = whImplUtil.siteIdToNeObject(siteId);
			actionObject = new ActionObject();
			if("0".equals(siteUtil.SiteTypeUtil(siteId)+"")){			
			actionObject.setActionId(super.getActionId(operationObject.getActionObjectList()));
			actionObject.setNeObject(neObject);
			actionObject.setType(type);
			actionObject.setBfdObjectList(this.setBfdInfoObjectContrlList(siteId));									
			operationObject.getActionObjectList().add(actionObject);
			}else{				
			actionObject.setStatus(ResultString.CONFIG_SUCCESS);
			operationObject.getActionObjectList().add(actionObject);			
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			neObject = null;
			actionObject = null;
		}
		return operationObject;
	}
			
	 /**
	  * 将数据库的对象转换成驱动对象
	  * @param siteId
	  * @return
	  */
		private List<BfdObject> setBfdInfoObjectContrlList(int siteId) {
			BfdInfoService_MB bfdInfoStudyService = null;
			List<BfdObject> bfdObjectList = null;	
			try {
				bfdObjectList=new ArrayList<BfdObject>();
				bfdInfoStudyService =(BfdInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.BFDMANAGEMENT);			
				bfdObjectList=bfdInfoStudyService.selectBySiteId(siteId);							     		
			} catch (Exception e) {
				ExceptionManage.dispose(e,this.getClass());
			}finally{
				UiUtil.closeService_MB(bfdInfoStudyService);
			}
			
			return bfdObjectList;
		}
				
			


	@Override
	public String excutionUpdate(Object object) throws Exception {
		BfdInfo  bfdInfo = null;
		List<Integer> siteIdList = null;
		OperationObject operationObjectAfter = null;
		OperationObject operationObjectResult = null;
		BfdInfoService_MB bfdInfoService = null;
		List<BfdInfo> bfd=null;
		try {
			bfdInfo=(BfdInfo) object;
			bfdInfoService =(BfdInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.BFDMANAGEMENT);			
			siteIdList = new ArrayList<Integer>();
			siteIdList.add(bfdInfo.getSiteId());
			if(super.getManufacturer(bfdInfo.getSiteId()) == EManufacturer.WUHAN.getValue()){
				super.lockSite(siteIdList, SiteLockTypeUtil.BFD_UPDATE);
				bfd = bfdInfoService.selectByCondition(bfdInfo);
				bfdInfoService.update(bfdInfo);
				// 下设备
				operationObjectAfter = this.getOperationObject(bfdInfo.getSiteId(),"bfdManagement");
				super.sendAction(operationObjectAfter);
				operationObjectResult = super.verification(operationObjectAfter);	
				if (operationObjectResult.isSuccess()) {						
					return operationObjectResult.getActionObjectList().get(0).getStatus();
				} else {
	
					// 下设备失败时把已经跟新的数据还原
					bfdInfoService.update(bfd.get(0));
					return super.getErrorMessage(operationObjectResult);
				}
			}else{
				return ResultString.CONFIG_SUCCESS;
			}
			
		} catch (Exception e) {
			throw e;
		} finally {
			super.clearLock(siteIdList);
			UiUtil.closeService_MB(bfdInfoService);
			operationObjectAfter = null;
			operationObjectResult = null;
			siteIdList=null;
		}
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
		BfdInfoService_MB bfdService = null;
		try {
			operaObj = this.getSynchroOperationObject(siteId);// 封装下发数据
			super.sendAction(operaObj);// 下发数据
			super.verification(operaObj);// 验证是否下发成功
			if (operaObj.isSuccess()) {
				/* 成功，则与数据库进行同步 */
				bfdService = (BfdInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.BFDMANAGEMENT);
				bfdService.deleteBySiteId(siteId);
				for (ActionObject actionObject : operaObj.getActionObjectList()) {
					
					this.synchro_db(actionObject.getBfdObjectList(), siteId);
				}
				
				return ResultString.CONFIG_SUCCESS;
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			UiUtil.closeService_MB(bfdService);
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
			actionObject.setType("bfdSynchro");
			operationObject.getActionObjectList().add(actionObject);
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			actionObject = null;
		}
		return operationObject;
	}

	/**
	 * 与数据库进行同步
	 * @param bfdInfo
	 * @param siteId
	 * @throws Exception
	 */
	private void synchro_db(List<BfdObject> bfdList, int siteId)throws Exception {
		List<BfdInfo> bfdInfoList= null;
		try {
			bfdInfoList = this.getBfdInfo(bfdList,siteId);
			SynchroUtil synchroUtil = new SynchroUtil();
			for(BfdInfo bfdInfo : bfdInfoList){
				synchroUtil.bfdSynchro(bfdInfo);
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}finally{
			bfdInfoList=null;
		}
		
	}
	
	/**
	 * 将设备信息封装到staticUnicastInfo表中
	 * @param staticUnicastList
	 * @param siteId
	 * @return List<StaticUnicastInfo>
	 * @throws Exception
	 */
	private List<BfdInfo> getBfdInfo(List<BfdObject> bfdList, int siteId) {
		List<BfdInfo> bfdInfoList = new ArrayList<BfdInfo>();
		BfdInfo info = null;
		for(BfdObject bfdObj : bfdList){
			try {
				info = new BfdInfo();
				info.setSiteId(siteId);
				info.setBfdId(bfdObj.getBfdId());
				info.setBfdEnabel(bfdObj.getBfdEnabel());
				info.setTestMode(bfdObj.getTestMode());	
				info.setBfdMessageSendType(bfdObj.getBfdMessageSendType());				
				info.setVlanPriority(bfdObj.getVlanPriority());
				info.setVlanId(bfdObj.getVlanId());
				info.setServiceType(bfdObj.getServiceType());
				info.setLocalIp(bfdObj.getLocalIp()+"");	
				info.setPeerIp(bfdObj.getPeerIp()+"");				
				info.setUdpPort(bfdObj.getUdpPort());
				info.setMySid(bfdObj.getMySid());
				info.setPeerStudyEnabel(bfdObj.getPeerStudyEnabel());
				info.setPeerSid(bfdObj.getPeerSid());	
				info.setDmti(bfdObj.getDmti());				
				info.setRmri(bfdObj.getRmri());
				info.setDectMul(bfdObj.getDectMul());
				info.setPwBfdStyle(bfdObj.getPwBfdStyle());
				info.setPwTtl(bfdObj.getPwTtl());	
				info.settLayelPortMark(bfdObj.gettLayelPortMark());
				info.setLspId(bfdObj.getLspId());
				info.setPwId(bfdObj.getPwId());
				bfdInfoList.add(info);
			} catch (Exception e) {
				ExceptionManage.dispose(e,this.getClass());
			}
		}
		return bfdInfoList;
	}

}