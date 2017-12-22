﻿package com.nms.service.impl.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.nms.db.bean.alarm.CurrentAlarmInfo;
import com.nms.db.bean.alarm.HisAlarmInfo;
import com.nms.db.bean.equipment.card.CardInst;
import com.nms.db.bean.equipment.port.PortInst;
import com.nms.db.bean.equipment.shelf.SiteInst;
import com.nms.db.bean.perform.HisPerformanceInfo;
import com.nms.db.bean.ptn.AclInfo;
import com.nms.db.bean.ptn.BlackAndwhiteMacInfo;
import com.nms.db.bean.ptn.CccInfo;
import com.nms.db.bean.ptn.EthLoopInfo;
import com.nms.db.bean.ptn.EthServiceInfo;
import com.nms.db.bean.ptn.L2cpInfo;
import com.nms.db.bean.ptn.SmartFan;
import com.nms.db.bean.ptn.path.eth.ElanInfo;
import com.nms.db.bean.ptn.path.eth.EtreeInfo;
import com.nms.db.bean.ptn.port.AcPortInfo;
import com.nms.db.bean.ptn.port.PortLagInfo;
import com.nms.db.bean.system.Field;
import com.nms.db.enums.EObjectType;
import com.nms.drive.service.bean.CccUNIObject;
import com.nms.drive.service.bean.ELineObject;
import com.nms.drive.service.bean.ETreeUNIObject;
import com.nms.drive.service.bean.NEObject;
import com.nms.model.alarm.CurAlarmService_MB;
import com.nms.model.alarm.HisAlarmService_MB;
import com.nms.model.equipment.card.CardService_MB;
import com.nms.model.equipment.port.PortService_MB;
import com.nms.model.equipment.shlef.SiteService_MB;
import com.nms.model.perform.HisPerformanceService_Mb;
import com.nms.model.ptn.port.PortLagService_MB;
import com.nms.model.system.FieldService_MB;
import com.nms.model.util.Services;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.ResourceUtil;
import com.nms.ui.manager.UiUtil;
import com.nms.ui.manager.keys.StringKeysTip;

public class WhImplUtil{
	/**
	 * 通过网元id获取NEObject
	 * @param siteId
	 * @return
	 */
	public NEObject siteIdToNeObject(Integer siteId) throws Exception{
		
		SiteService_MB siteService = null;
		CardService_MB cardService = null;
		NEObject neObject = null;
		List<CardInst> cardInsts = null;
		try {
			siteService=(SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE);
			neObject = getNEObject(siteId,siteService);
			String siteType = siteService.getSiteType(siteId);
			cardService = (CardService_MB) ConstantUtil.serviceFactory.newService_MB(Services.CARD);
			if("710B".equals(siteType)){
				neObject.setControlPanelType(118492006);
			}else if("710A".equals(siteType)||"710".equals(siteType)||"ETN-5000".equals(siteType)){
				 cardInsts = cardService.selectBySiteId(siteId);
				for(CardInst cardInst : cardInsts){
					if("XCTO1".equals(cardInst.getCardName())){
						neObject.setControlPanelType(118491749);
						break;  
					}
					if("XCTS1".equals(cardInst.getCardName())){
						neObject.setControlPanelType(118491767);
						break;
					}
					if("ESMC".equals(cardInst.getCardName())){
						neObject.setControlPanelType(118491767);
						break;
					}
				}
				
			}else if("ZXWT CTN280".equals(siteType) || "PAS 100/200".equals(siteType)){
				neObject.setControlPanelType(118491749);
			}else if("703C".equals(siteType) || "703-1A".equals(siteType) || "703-2A".equals(siteType)
					|| "ZXCTN 605-FE224".equals(siteType) || "ZXCTN 605-FE124".equals(siteType)
					||"703-1".equals(siteType)||"703-2".equals(siteType)||"703-3".equals(siteType)
					||"703-4".equals(siteType)||"703-5".equals(siteType)||"703-6".equals(siteType)
					||"703-7".equals(siteType)||"CSG T2000".equals(siteType)
			){
				neObject.setControlPanelType(117637232);
			}else if("703B".equals(siteType) || "IPA500".equals(siteType)){
				neObject.setControlPanelType(117637224);
			}else if("703-4A".equals(siteType)|| "ZXWT CTN180".equals(siteType) ||"703-6A".equals(siteType)
					|| "ZXCTN 605-GF222".endsWith(siteType) || "ZXCTN 605-F12".endsWith(siteType)|| "ZXCTN 605-G12".endsWith(siteType)
					|| "ZXCTN 605-F24".endsWith(siteType) || "ZXCTN 605-F22".endsWith(siteType)|| "ZXCTN 605-G22".endsWith(siteType)){
				neObject.setControlPanelType(117637234);
			}else if (("703-2C".equals(siteType)) || ("703-2B".equals(siteType)) || ("703-2A".equals(siteType))
					|| ("ETN-200-204E".equals(siteType))|| ("ETN-200-204".equals(siteType))) {
				 neObject.setControlPanelType(117637232);
			 }
		}finally{
			UiUtil.closeService_MB(siteService);
			UiUtil.closeService_MB(cardService);
			cardInsts = null;
		}
		return neObject;
	}
	
	/**
	 * 获得neobject对象
	 * @param siteInst 网元对象
	 * @return 
	 * @throws Exception 
	 */
	private NEObject getNEObject(int siteId,SiteService_MB siteService) throws Exception{
		
		NEObject neObject=null;
		SiteInst siteInst=null;
		FieldService_MB fieldService = null;
		List<Field> fields = null;
		SiteInst msiteInst = null;
		Field field = null;
		try {
			fieldService = (FieldService_MB) ConstantUtil.serviceFactory.newService_MB(Services.Field);
			siteInst = siteService.select(siteId);
			fields = fieldService.selectByFieldId(siteInst.getFieldID());
			neObject = new NEObject();
			if(fields.size()>0){
				if("field".equals(fields.get(0).getObjectType())){//对应域
					//获得真实ne地址
					neObject.setNeAddress(fields.get(0).getGroupId()*256+Integer.parseInt(siteInst.getSite_Hum_Id()));
					//获取M网元ip
					msiteInst = new SiteInst();
					msiteInst = siteService.select(fields.get(0).getmSiteId());
					if(msiteInst.getCellDescribe()!= null){
						neObject.setManageIP(msiteInst.getCellDescribe());
					}else{
						ExceptionManage.infor("该域中不存在网关网元所有命令发送失败", WhImplUtil.class);
					}
				}else if("subnet".equals(fields.get(0).getObjectType())){//对应子网
					
					//获取域id
					field = new Field();
					field.setId(fields.get(0).getParentId());
					fields = fieldService.select(field);
					if(fields.size()>0){
						if("field".equals(fields.get(0).getObjectType())){
							//获取M网元ip
							msiteInst = new SiteInst();
							msiteInst = siteService.select(fields.get(0).getmSiteId());
							neObject.setManageIP(msiteInst.getCellDescribe());
							//获得真实ne地址
							neObject.setNeAddress(fields.get(0).getGroupId()*256+Integer.parseInt(siteInst.getSite_Hum_Id()));
						}
					}
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, WhImplUtil.class);
		}finally{
			UiUtil.closeService_MB(fieldService);
			siteInst=null;
			fields = null;
		    msiteInst = null;
			field = null;
		}
		return neObject;
	}
	
	/**
	 * 同步meg
	 * @param meg
	 * @return
	 */
	public String synchroMeg(String meg) {
		StringBuffer stringBuffer = new StringBuffer();
		String[] strs = meg.split(",");
		for (String str : strs) {
			char c = (char) Integer.parseInt(str);
			stringBuffer.append(c);
		}
		return stringBuffer.toString();
	}
	
	/**
	 * 删除tunnel，pw之类的信息时，删除相关的当前告警，历史告警，历史性能
	 * @param objectType
	 * @param objectId
	 * @throws Exception 
	 */
	public void deleteAlarmAndPerformance(EObjectType objectType,int objectId,int siteId) {
		CurAlarmService_MB currentAlamService = null;
		HisAlarmService_MB hisAlarmService = null;
		HisPerformanceService_Mb hisPerformanceService = null;
		CurrentAlarmInfo currentAlarmInfo = null;
		HisAlarmInfo hisAlarmInfo = null;
		HisPerformanceInfo hisPerformanceInfo = null;
		try {
			//删除当前告警
			currentAlamService = (CurAlarmService_MB) ConstantUtil.serviceFactory.newService_MB(Services.CurrentAlarm);
			currentAlarmInfo = new CurrentAlarmInfo();
			currentAlarmInfo.setObjectType(objectType);
			currentAlarmInfo.setObjectId(objectId);
			currentAlarmInfo.setSiteId(siteId);
			currentAlamService.deleteCurrentAlarmInfo(currentAlarmInfo);
			
			//删除历史告警
			hisAlarmService = (HisAlarmService_MB) ConstantUtil.serviceFactory.newService_MB(Services.HisAlarm);
			hisAlarmInfo = new HisAlarmInfo();
			hisAlarmInfo.setObjectType(objectType);
			hisAlarmInfo.setObjectId(objectId);
			hisAlarmInfo.setSiteId(siteId);
			hisAlarmService.deleteHisAlarmInfo(hisAlarmInfo);
			
			//删除历史性能
//			hisPerformanceService = (HisPerformanceService) ConstantUtil.serviceFactory.newService(Services.HisPerformance);
//			hisPerformanceInfo = new HisPerformanceInfo();
//			hisPerformanceInfo.setObjectType(objectType);
//			hisPerformanceInfo.setObjectId(objectId);
//			hisPerformanceService.deleteHisPerformance(hisPerformanceInfo);
			
		} catch (Exception e) {
			ExceptionManage.dispose(e, WhImplUtil.class);
		} finally {
			UiUtil.closeService_MB(currentAlamService);
		//	UiUtil.closeService(hisPerformanceService);
			UiUtil.closeService_MB(hisAlarmService);
		}
	}
	
	public void lagId(AcPortInfo acPortInfo, Object object) throws Exception {
		PortLagService_MB portLagService = null;
		PortLagInfo portLagInfo = null;
		try {
			portLagService = (PortLagService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORTLAG);
			portLagInfo = new PortLagInfo();
			portLagInfo.setId(acPortInfo.getLagId());
			portLagInfo = portLagService.selectLAGByCondition(portLagInfo).get(0);
			if(object instanceof ELineObject){
				((ELineObject)object).setProtUNI(100+portLagInfo.getLagID());
			}else if(object instanceof ETreeUNIObject){
				((ETreeUNIObject)object).setPort(100+portLagInfo.getLagID());
			}else if(object instanceof CccUNIObject){
				((CccUNIObject)object).setPort(100+portLagInfo.getLagID());
			}
			portLagInfo.setStatusActive(0);
			portLagService.saveOrUpdate(portLagInfo);

		} catch (Exception e) {
			ExceptionManage.dispose(e,WhImplUtil.class);
		} finally {
			UiUtil.closeService_MB(portLagService);
		}

	}

	public PortInst getportId(int portId, int siteId) {
		PortInst port = null;
		PortService_MB portService = null;
		try {
			portService = (PortService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORT);
			port = new PortInst();
			port.setPortId(portId);
			port.setSiteId(siteId);
			List<PortInst> insts = portService.select(port);
			if (insts != null && insts.size() > 0) {
				port = portService.select(port).get(0);
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,WhImplUtil.class);
		} finally {
			UiUtil.closeService_MB(portService);
		}
		return port;
	}
	
	/**
	 * 通过网元ID来获取网元的IP名称
	 * @param siteList
	 * @return
	 */
	public String getNeNames(List<Integer> siteList){
		SiteService_MB siteService = null;
		String str = new String();
		try {
			if(siteList != null && siteList.size()>0){					
				siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE);
				HashSet<Integer> hashSet= new HashSet<Integer>(siteList);
				siteList.clear();
				siteList.addAll(hashSet);			
				for (int i = 0; i < siteList.size(); i++) {	
					if(siteList.get(i) > 0 ){
						SiteInst siteInst = siteService.select(siteList.get(i));
						if(siteInst != null && siteInst.getLoginstatus() == 0){
							if(i == siteList.size()-1){
								str += siteInst.getCellId();
							}else{
								str += siteInst.getCellId()+"、";
							}
						}
					}
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, getClass());
		}finally{
			UiUtil.closeService_MB(siteService);
		}
		//如果最后一顿号结尾，去掉
		if(str.endsWith("、"))
		{
			str = str.substring(0, str.length()-1);
		}
		return str;
	}
	
	/**
	 * 返回离线网元的信息
	 * @param objectList 对象的信息
	 * @return
	 */
	public String offLineResult(Object objectList){
//		OperationServiceOffLine OperationServiceOffLine = null;
		String restult = "";
		List<Integer> siteIdList = new ArrayList<Integer>();
		try {
			if(objectList instanceof List && ((List)objectList).size() >0){
				List list = (List) objectList;
				Object busiObject = list.get(0);
				if(busiObject instanceof EtreeInfo){
//					OperationServiceOffLine = new EtreeWHServiceImpl();
					siteIdList =getSiteIdList(objectList);
				}else if(busiObject instanceof ElanInfo){
//					OperationServiceOffLine = new ElanWHServiceImpl();
					siteIdList = getSiteIdList(objectList);
				}else if(busiObject instanceof AclInfo){
					//单站侧ACL 批量删除
					siteIdList.add(((AclInfo)busiObject).getSiteId());
				}else if(busiObject instanceof BlackAndwhiteMacInfo){
					//单站侧以太网黑白名单的  批量删除
					siteIdList.add(((BlackAndwhiteMacInfo)busiObject).getSiteId());
				}else if(busiObject instanceof EthServiceInfo){
					//单站侧以太网二层业务配置块的  批量删除
					siteIdList.add(((EthServiceInfo)busiObject).getSiteId());
				}else if(busiObject instanceof CccInfo){
					siteIdList.add(((CccInfo)busiObject).getaSiteId());
				}
			}else if(objectList instanceof EthLoopInfo){
				siteIdList.add(((EthLoopInfo)objectList).getSiteId());
			}else if(objectList instanceof AclInfo){
				//单站侧ACL 新建或修改
				siteIdList.add(((AclInfo)objectList).getSiteId());
			}else if(objectList instanceof BlackAndwhiteMacInfo){
				//单站侧以太网黑白名单的  新建或修改
				siteIdList.add(((BlackAndwhiteMacInfo)objectList).getSiteId());
			}else if(objectList instanceof EthServiceInfo){
				//单站侧以太网二层业务配置块  新建或修改
				siteIdList.add(((EthServiceInfo)objectList).getSiteId());
			}else if(objectList instanceof L2cpInfo){
			    //单站侧L2CP配置块  新建或修改
			   siteIdList.add(((L2cpInfo)objectList).getSiteId());
		    }else if(objectList instanceof SmartFan){
				//单站侧自能风扇配置块  修改
				siteIdList.add(((SmartFan)objectList).getSiteId());
			}else if(objectList instanceof CccInfo){
				//CccInfo插入
				siteIdList.add(((CccInfo)objectList).getaSiteId());
			}
			if(siteIdList != null &&  siteIdList.size()>0){
				String str = this.getNeNames(siteIdList);
				if(str.equals("")){
					restult = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
				}else{
					restult= ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS)+","+str+ResultString.NOT_ONLINE_SUCCESS;
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, getClass());
		}finally{
//			OperationServiceOffLine = null;
		}
		return restult;
	}
	
	public List<Integer> getSiteIdList(Object object){
		List<Integer> siteIdList = new ArrayList<Integer>();
		List<EtreeInfo> etreeList = new ArrayList<EtreeInfo>();
		List<ElanInfo> elanInfoList = new ArrayList<ElanInfo>();		
		try {
			if(object instanceof List){
				List list = (List) object;
				Object busiObject = list.get(0);
				if(busiObject instanceof EtreeInfo){
					etreeList = (List<EtreeInfo>) object;
					for (EtreeInfo etreeInfo : etreeList) {
						if (etreeInfo.getRootSite() > 0) {
							if (!siteIdList.contains(etreeInfo.getRootSite())) {
								siteIdList.add(etreeInfo.getRootSite());
							}
						}
						if (etreeInfo.getBranchSite() > 0) {
							if (!siteIdList.contains(etreeInfo.getBranchSite())) {
								siteIdList.add(etreeInfo.getBranchSite());
							}
						}
					}
				}else if(busiObject instanceof ElanInfo){
					elanInfoList = (List<ElanInfo>) object;
					for (ElanInfo elanInfo : elanInfoList) {
						if (elanInfo.getaSiteId() > 0) {
							if (!siteIdList.contains(elanInfo.getaSiteId())) {
								siteIdList.add(elanInfo.getaSiteId());
							}
						}
						if (elanInfo.getzSiteId() > 0) {
							if (!siteIdList.contains(elanInfo.getzSiteId())) {
								siteIdList.add(elanInfo.getzSiteId());
							}
						}
					}
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, getClass());
		}
		return siteIdList;
	}
	
	/**
	 * 通过网元ID来获取网元的IP名称
	 * @param siteList
	 * @return
	 */
	public String getNeName(List<Integer> siteList){
		SiteService_MB siteService = null;
		String str = new String();
		try {
			if(siteList != null && siteList.size()>0){					
				siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE);
				HashSet<Integer> hashSet= new HashSet<Integer>(siteList);
				siteList.clear();
				siteList.addAll(hashSet);			
				for (int i = 0; i < siteList.size(); i++) {	
					if(siteList.get(i) > 0 ){
						SiteInst siteInst = siteService.select(siteList.get(i));
						if(siteInst != null ){
							if(i == siteList.size()-1){
								str += siteInst.getCellId();
							}else{
								str += siteInst.getCellId()+"、";
							}
						}
					}
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, getClass());
		}finally{
			UiUtil.closeService_MB(siteService);
		}
		//如果最后一顿号结尾，去掉
		if(str.endsWith("、"))
		{
			str = str.substring(0, str.length()-1);
		}
		return str;
	}
	
}