﻿package com.nms.service.impl.util;

import java.util.ArrayList;
import java.util.List;

import com.nms.db.bean.equipment.card.CardInst;
import com.nms.db.bean.equipment.port.E1Info;
import com.nms.db.bean.equipment.port.Port2LayerAttr;
import com.nms.db.bean.equipment.port.PortInst;
import com.nms.db.bean.equipment.port.PortStm;
import com.nms.db.bean.equipment.port.PortStmTimeslot;
import com.nms.db.bean.equipment.port.V35PortInfo;
import com.nms.db.bean.equipment.shelf.SiteInst;
import com.nms.db.bean.equipment.slot.SlotInst;
import com.nms.db.bean.path.Segment;
import com.nms.db.bean.perform.PerformanceTaskInfo;
import com.nms.db.bean.perform.PmValueLimiteInfo;
import com.nms.db.bean.ptn.ARPInfo;
import com.nms.db.bean.ptn.AclInfo;
import com.nms.db.bean.ptn.BfdInfo;
import com.nms.db.bean.ptn.EthServiceInfo;
import com.nms.db.bean.ptn.MacLearningInfo;
import com.nms.db.bean.ptn.MacManagementInfo;
import com.nms.db.bean.ptn.SiteRoate;
import com.nms.db.bean.ptn.SmartFan;
import com.nms.db.bean.ptn.SsMacStudy;
import com.nms.db.bean.ptn.clock.ClockSource;
import com.nms.db.bean.ptn.clock.ExternalClockInterface;
import com.nms.db.bean.ptn.clock.FrequencyInfoNeClock;
import com.nms.db.bean.ptn.clock.LineClockInterface;
import com.nms.db.bean.ptn.clock.PortConfigInfo;
import com.nms.db.bean.ptn.clock.TimeManageInfo;
import com.nms.db.bean.ptn.ecn.MCN;
import com.nms.db.bean.ptn.ecn.OSPFInterface;
import com.nms.db.bean.ptn.oam.OamEthernetInfo;
import com.nms.db.bean.ptn.oam.OamInfo;
import com.nms.db.bean.ptn.oam.OamLinkInfo;
import com.nms.db.bean.ptn.oam.OamMepInfo;
import com.nms.db.bean.ptn.oam.OamMipInfo;
import com.nms.db.bean.ptn.path.StaticUnicastInfo;
import com.nms.db.bean.ptn.path.ces.CesInfo;
import com.nms.db.bean.ptn.path.eth.DualInfo;
import com.nms.db.bean.ptn.path.eth.ElanInfo;
import com.nms.db.bean.ptn.path.eth.ElineInfo;
import com.nms.db.bean.ptn.path.eth.EtreeInfo;
import com.nms.db.bean.ptn.path.protect.DualProtect;
import com.nms.db.bean.ptn.path.protect.LoopProtectInfo;
import com.nms.db.bean.ptn.path.protect.MspProtect;
import com.nms.db.bean.ptn.path.pw.PwInfo;
import com.nms.db.bean.ptn.path.tunnel.Lsp;
import com.nms.db.bean.ptn.path.tunnel.Tunnel;
import com.nms.db.bean.ptn.port.AcPortInfo;
import com.nms.db.bean.ptn.port.PortLagInfo;
import com.nms.db.bean.ptn.qos.QosInfo;
import com.nms.db.bean.ptn.qos.QosMappingMode;
import com.nms.db.bean.ptn.qos.QosQueue;
import com.nms.db.enums.EActiveStatus;
import com.nms.db.enums.EManufacturer;
import com.nms.model.equipment.card.CardService_MB;
import com.nms.model.equipment.port.E1InfoService_MB;
import com.nms.model.equipment.port.Port2LayerAttrService_MB;
import com.nms.model.equipment.port.PortService_MB;
import com.nms.model.equipment.port.PortStmService_MB;
import com.nms.model.equipment.port.PortStmTimeslotService_MB;
import com.nms.model.equipment.port.V35PortService_MB;
import com.nms.model.equipment.shlef.SiteService_MB;
import com.nms.model.equipment.slot.SlotService_MB;
import com.nms.model.perform.PmLimiteService_MB;
import com.nms.model.ptn.ARPInfoService_MB;
import com.nms.model.ptn.AclService_MB;
import com.nms.model.ptn.BfdInfoService_MB;
import com.nms.model.ptn.EthService_MB;
import com.nms.model.ptn.MacLearningService_MB;
import com.nms.model.ptn.MacManageService_MB;
import com.nms.model.ptn.SecondMacStudyService_MB;
import com.nms.model.ptn.SmartFanService_MB;
import com.nms.model.ptn.clock.ExternalClockInterfaceService_MB;
import com.nms.model.ptn.clock.FrequencyClockManageService_MB;
import com.nms.model.ptn.clock.FrequencyInfoNeClockService_MB;
import com.nms.model.ptn.clock.PortDispositionInfoService_MB;
import com.nms.model.ptn.clock.TimeLineClockInterfaceService_MB;
import com.nms.model.ptn.clock.TimeManageInfoService_MB;
import com.nms.model.ptn.ecn.MCNService_MB;
import com.nms.model.ptn.ecn.OSPFInterfaceService_MB;
import com.nms.model.ptn.oam.OamEthreNetService_MB;
import com.nms.model.ptn.oam.OamInfoService_MB;
import com.nms.model.ptn.path.SingleSpreadService_MB;
import com.nms.model.ptn.path.ces.CesInfoService_MB;
import com.nms.model.ptn.path.eth.DualInfoService_MB;
import com.nms.model.ptn.path.eth.ElanInfoService_MB;
import com.nms.model.ptn.path.eth.ElineInfoService_MB;
import com.nms.model.ptn.path.eth.EtreeInfoService_MB;
import com.nms.model.ptn.path.protect.MspProtectService_MB;
import com.nms.model.ptn.path.protect.WrappingProtectService_MB;
import com.nms.model.ptn.path.pw.PwInfoService_MB;
import com.nms.model.ptn.path.tunnel.TunnelService_MB;
import com.nms.model.ptn.port.AcPortInfoService_MB;
import com.nms.model.ptn.port.DualProtectService_MB;
import com.nms.model.ptn.port.PortLagService_MB;
import com.nms.model.ptn.qos.QosMappingModeService_MB;
import com.nms.model.ptn.qos.QosQueueService_MB;
import com.nms.model.ptn.qos.QosRelevanceService_MB;
import com.nms.model.util.Services;
import com.nms.service.impl.base.DispatchBase;
import com.nms.service.impl.cx.AcCXServiceImpl;
import com.nms.service.impl.dispatch.ElanDispatch;
import com.nms.service.impl.dispatch.EtreeDispatch;
import com.nms.service.impl.wh.AcWHServiceImpl;
import com.nms.ui.manager.BusinessIdException;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.ResourceUtil;
import com.nms.ui.manager.UiUtil;
import com.nms.ui.manager.keys.StringKeysTip;
import com.nms.ui.ptn.performance.model.CurrentPerformanceFilter;
	
/**
 * 虚拟网元实现的方法，不下发设备，只入库
 * @author dzy
 *
 */
public class SiteUtil {
	
	/**
	 * 验证网元类型
	 * 
	 * @param SiteId  网元ID
	 * 
	 * @return  0为真是网元 、1为虚拟网元或离线网元
	 * @throws Exception 
	 */
	public int SiteTypeUtil(int siteId) throws Exception {
		int flag =0;
		SiteService_MB siteService = null;
		SiteInst siteInst = null;
		try {
		    if (0 != siteId)
		    {
				siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE);
				siteInst = (SiteInst) siteService.select(siteId);
				if(null!=siteInst && "1".equals(UiUtil.getCodeById(siteInst.getSiteType()).getCodeValue())||0==siteInst.getLoginstatus()){
					flag =1;
				}
			}else
			{
				flag =1;
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,SiteUtil.class);
		} finally {
			UiUtil.closeService_MB(siteService);
		}
	
		return flag;
	}
	
	/**
	 * 虚拟网元方法
	 * @param action 操作方法
	 * @param object 操作对象
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Object irtualSiteAction(String action,Object object) throws Exception {
		
		String result = null;
		
		//参数为单体业务对象  
		if(object instanceof AcPortInfo){// AC端口 
			result = acAction(action,object);
		}else if(object instanceof CardInst){ //板卡
			result = cardAction(action,object);
		}else if(object instanceof PortInst){//ETH端口
			result = portAction(action,object);
		}else if(object instanceof PortLagInfo){	//Lag管理配置
			result = portLagAction(action,object);
		}else if(object instanceof PortStm){//SDH端口管理
			result = portStmAction(action,object);
		}else if(object instanceof PortStmTimeslot){//sdh时隙管理
			result = portStmTimeslotAction(action,object);
		}else if(object instanceof MCN){//MCN端口管理
			result = MCNAction(action,object);
		}else if(object instanceof  Tunnel){//Tunnel端口
			result = tunnelAction(action,object);
		}else if(object instanceof  Segment){//段
			result = segmentAction(action,object);
		}else if(object instanceof  PwInfo){//pw
			result = pwAction(action,object);
		}else if(object instanceof  ElineInfo){//eline
			result = eLineAction(action,object);
		}else if(object instanceof  OamInfo){//oam  OamEthernetInfo
			result = oamAction(action,object);
		}else if(object instanceof  OamEthernetInfo){//以太网OAM
			result = ethOamAction(action,object);
		}else if(object instanceof  SiteInst){//网元
			result = siteInstAction(action,object);
		}else if(object instanceof  E1Info){//PDH
			result = e1Action(action,object);
		}else if(object instanceof  OSPFInterface){//OSPF
			result = OSPFInstAction(action,object);
		}else if(object instanceof  SiteRoate){//保护倒换
			result = protectRorateAction(action,object);
		}else if(object instanceof  CurrentPerformanceFilter){	//当前性能
			CurrentPerformanceFilter resultFilter = (CurrentPerformanceFilter) currentPerformanceAction(action,object);
			return resultFilter;
		}else if(object instanceof  PerformanceTaskInfo){//长期定时任务
			result =  (String) performanceTaskAction(action,object);
		}else if(object instanceof  ClockSource){//时钟频率
			result =  (String) clockSourceAction(action,object);
		}else if(object instanceof  FrequencyInfoNeClock){//时钟状态
			result =  (String) frequencyInfoNeClockAction(action,object);
		}else if(object instanceof  TimeManageInfo){//PTP配置
			result =  (String) timeManageInfoAction(action,object);
		}else if(object instanceof  ExternalClockInterface){//外接时钟接口
			result =  (String) exClockInterfaceAction(action,object);
		}else if(object instanceof  PortConfigInfo){//端口配置
			result =  (String) portConfigInfoAction(action,object);
		}else if(object instanceof  LineClockInterface){//线路时钟接口
			result =  (String) lineClockInterfaceAction(action,object);
		}else if(object instanceof  CesInfo){                 //CES业务
			result =  (String) CESAction(action,object);
		}else if(object instanceof  PmValueLimiteInfo){//性能门限
			result =  (String) pmValueLimiteInfoAction(action,object);
		}else if(object instanceof  MspProtect){                 //MSP保护业务
			result =  (String) mspAction(action,object);
		}else if(object instanceof  DualProtect){                 //双规保护业务
			result =  (String) dualAction(action,object);
		}else if(object instanceof  StaticUnicastInfo){                 //静态单播
			result =  (String) staticUnicastAction(action,object); 
		}else if(object instanceof  SlotInst){                 //复位（WH）
			result =  (String) slotInstAction(action,object); 
		}else if(object instanceof  MacManagementInfo){//黑名单mac管理
			result =  (String) macManageAction(action,object); 
		}else if(object instanceof  MacLearningInfo){//mac学习数目限制管理
			result =  (String) macLearnAction(action,object); 
		}else if(object instanceof  V35PortInfo){
			result =  (String) v35PortInfo(action,object); 
//		}else if(object instanceof QinqInst){//QinQ
//			result = (String)qinQAction(action, object);
		}else if(object instanceof SmartFan){//智能风扇
			result = (String)smartFanAction(action, object);
		}
//		else if(object instanceof EthServiceInfo){//4.5.43	以太网二层业务配置块
//			result = (String)ethServiceAction(action, object);
//		}
		else if(object instanceof Port2LayerAttr){//端口2层属性
			result = (String)port2LayerAttrAction(action, object);
		}else if(object instanceof QosMappingMode){//phb映射
			result = (String)phbMappingExpAction(action, object);
		}else if(object instanceof BfdInfo){
			result = (String)bfdActionForList(action, object); //bfd
		}else if(object instanceof SsMacStudy){
			result = (String)secondMacActionForList(action, object); //二层MAC学习
		}
		
		//参数为List  action为 方法名称
		if(object instanceof List){
			List list = (List) object;
			Object busiObject = list.get(0);
			if(busiObject instanceof AcPortInfo){  //AC
				result = acPortInfoActionForList(action,list);
			}else if(busiObject instanceof PortLagInfo){   // lag
				result = portLagActionForList(action,list);
			}else if(busiObject instanceof QosQueue){ //QOS PORT
				result = qosQueueActionForList(action,list);
			}else if(busiObject instanceof Tunnel){  //Tunnel端口
				result = tunnelActionForList(action,list);	 
			}else if(busiObject instanceof Segment){    //段
				result = segmentActionForList(action,list);	
			}else if(busiObject instanceof PwInfo){    //pw
				result = pwActionForList(action,list);	
			}else if(busiObject instanceof ElineInfo){    //eline
				result = eLineActionForList(action,list);	
			}else if(busiObject instanceof ElanInfo){    //elan
				result = eLanActionForList(action,list);	
			}else if(busiObject instanceof EtreeInfo){    //Etree
				result = eTreeActionForList(action,list);	
			}else if(busiObject instanceof CesInfo){    //Ces
				result = CESActionForList(action,list);	
			}else if(busiObject instanceof OamInfo){    //Oam
				result = oamActionForList(action,list);	
			}else if(busiObject instanceof E1Info){    //E1    
				result = pdhActionForList(action,list);	
			}else if(busiObject instanceof OSPFInterface){    // ospf
				result = OSPFActionForList(action,list);	
			}else if(busiObject instanceof SiteInst){    //段搜索
				result = siteActionForList(action,list);	
			}else if(busiObject instanceof LoopProtectInfo){    //环保护
				result = loopProtectActionForList(action,list);	
			}else if(busiObject instanceof  PerformanceTaskInfo){//长期定时任务
				result =  (String) performanceTaskAction(action,object);			
			}else if(busiObject instanceof  PortConfigInfo){//端口配置
				result =  (String) ptpPortActionForList(action,object);				
			}else if(busiObject instanceof  ClockSource){   //时钟源配置
				result =  (String) portSourseActionForList(action,object);				
			}else if(busiObject instanceof  MspProtect){    //MSP保护业务
				result =  (String) mspActionForList(action,object);				
			}else if(busiObject instanceof  DualProtect){    //双规保护业务
				result =  (String) dualActionForList(action,object);				
			}else if(busiObject instanceof  QosMappingMode){    //exp映射
				result =  (String) qosMappingModeActionForList(action,object);				
			}else if(busiObject instanceof  StaticUnicastInfo){                 //静态单播
				result =  (String) staticUnicastActionForList(action,object); 
			}else if(busiObject instanceof  DualInfo){    //双归保护
				result =  (String) dualInfoActionForList(action,object);		
//			}else if(busiObject instanceof QinqInst){//QinQ
//				result = (String)qinQActionDelete(action, object);
			}else if(busiObject instanceof  AclInfo){                 //ACL配置管理
				result =  (String) aclAction(action,object); 
			}else if(busiObject instanceof MacLearningInfo){        //MAC学习数目管理
				result =  (String) macLearnAction(action,object); 
			}else if(busiObject instanceof MacManagementInfo){
				result =  (String) macManageAction(action,object); //黑名单 mac管理
			}else if(busiObject instanceof EthServiceInfo){
				result = (String)ethServiceAction(action, object); //以太网二层业务配置块
			}else if(busiObject instanceof BfdInfo){
				result = (String)bfdActionForList(action, object); //bfd
			}else if(busiObject instanceof SsMacStudy){
				result = (String)secondMacActionForList(action, object); //二层MAC学习
			}else if(busiObject instanceof ARPInfo){
				result = (String)arpActionForList(action, object);//arp配置
			}
		}
		return result;
	}

	private String phbMappingExpAction(String action, Object object) {
		QosMappingModeService_MB service = null;
		try {
			QosMappingMode mode = (QosMappingMode) object;
			if("0".equals(SiteTypeUtil(mode.getSiteId())+"")){
				return null;
			}
			service = (QosMappingModeService_MB) ConstantUtil.serviceFactory.newService_MB
			(Services.QosMappingModeService);
			List<QosMappingMode> qosMappingModeList = new ArrayList<QosMappingMode>();
			qosMappingModeList.add(mode);
			service.saveOrUpdate(qosMappingModeList);
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}finally{
			UiUtil.closeService_MB(service);		
		}
		return ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
	}

	private String port2LayerAttrAction(String action, Object object) {
		Port2LayerAttr port2Layer = (Port2LayerAttr) object;
		Port2LayerAttrService_MB service = null;
		try {
			if("0".equals(SiteTypeUtil(port2Layer.getSiteId())+"")){
				return null;
			}
			service = (Port2LayerAttrService_MB) ConstantUtil.serviceFactory.
			newService_MB(Services.PORT_2LAYER_ATTR);
			service.saveOrUpdate(port2Layer);
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}finally{
			UiUtil.closeService_MB(service);
		}
		return ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
	}

	private String ethServiceAction(String action, Object object) throws Exception{
//		EthServiceInfo ethServiceInfo = (EthServiceInfo)object;
		List<EthServiceInfo> ethServiceInfoList = null;
		EthService_MB ethService = null;
		try {
			ethServiceInfoList = (List<EthServiceInfo>) object;
			if(0 == (SiteTypeUtil(ethServiceInfoList.get(0).getSiteId()))){
				return null;
			}
			ethService = (EthService_MB) ConstantUtil.serviceFactory.newService_MB(Services.ETHSERVICE);
			if(TypeAndActionUtil.ACTION_SAVEANDUPDATE.equals(action)){
				if(ethServiceInfoList.get(0).getId()>0){
					ethService.update(ethServiceInfoList.get(0));
				}else{
					ethService.save(ethServiceInfoList);
				}
				return ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
				
			}else if(TypeAndActionUtil.ACTION_DELETE.equals(action)){
				ethService.delete(ethServiceInfoList);
				return  ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}finally{
			UiUtil.closeService_MB(ethService);
		}
		return null;
	}

	/**
	 * 虚拟网元智能风扇操作
	 * @throws Exception 
	 */
	private String smartFanAction(String action, Object object) throws Exception {
		SmartFan fan = (SmartFan) object;
		SmartFanService_MB service = null;
		try {
			
			if("0".equals(SiteTypeUtil(fan.getSiteId())+"")){
				return null;
			}
			service = (SmartFanService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SMARTFNASERVICE);
			service.update(fan);
			
		} catch (Exception e) {
			throw e;
		}finally{
			UiUtil.closeService_MB(service);
		}
		return ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
	}

//	@SuppressWarnings("unchecked")
//	private String qinQActionDelete(String action, Object object) throws Exception {
//		
//		String result = null;
//		QinQInstService service = null;
//		
//		try {
//			List<QinqInst> list = (List<QinqInst>)object;
//			for (QinqInst qinq : list) {
//				int aSiteId = qinq.getaSiteId();
//				int zSiteId = qinq.getzSiteId();
//				if(0 != aSiteId &&0 != zSiteId){
//					if(!(SiteTypeUtil(aSiteId)+"").equals(SiteTypeUtil(zSiteId)+"")){
//						return ResourceUtil.srcStr(StringKeysTip.LBL_DIFSITE);	
//					}else if(("0".equals(SiteTypeUtil(aSiteId)+"")&& ("0".equals(SiteTypeUtil(zSiteId)+"")))){
//						return result;
//					}
//				}
//			}
//			service = (QinQInstService) ConstantUtil.serviceFactory.newService(Services.QinQ);
//			if(TypeAndActionUtil.ACTION_DELETE.equals(action)){
//				service.delete(list);
//				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
//			}
//		} catch (Exception e) {
//			throw e;
//		}finally{
//			UiUtil.closeService(service);
//		}
//		return result;
//	}

//	private  String qinQAction(String action, Object object) throws Exception {
//		String result = null;
//		QinqInst qinq = (QinqInst)object;
//		QinQInstService service = null;
//		try {
//			int aSiteId = qinq.getaSiteId();
//			int zSiteId = qinq.getzSiteId();
//			if(0 != aSiteId &&0 != zSiteId){
//				if(!(SiteTypeUtil(aSiteId)+"").equals(SiteTypeUtil(zSiteId)+"")){
//					return ResourceUtil.srcStr(StringKeysTip.LBL_DIFSITE);	
//				}else if(("0".equals(SiteTypeUtil(aSiteId)+"")&& ("0".equals(SiteTypeUtil(zSiteId)+"")))){
//					return result;
//				}
//			}
//			service = (QinQInstService) ConstantUtil.serviceFactory.newService(Services.QinQ);
//			if(TypeAndActionUtil.ACTION_INSERT.equals(action)){
//				service.save( qinq);
//				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
//			}
//			else if(TypeAndActionUtil.ACTION_UPDATE.equals(action)){
//				service.update(qinq);
//				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
//			}
//		} catch (Exception e) {
//			throw e;
//		}finally{
//			UiUtil.closeService(service);
//		}
//		return result;
//	}

	private String v35PortInfo(String action, Object object) {
		String result = null;
		V35PortInfo v35PortInfo  = null;
		int siteId = 0;
		V35PortService_MB v35PortService = null;
		
		try {
			v35PortInfo  = (V35PortInfo) object;
			if(null!=v35PortInfo){
				siteId = v35PortInfo.getSiteId();
			}
			//0为真是网元 、1为虚拟网元或离线网元
			if(0!=siteId){
				if("0".equals(SiteTypeUtil(siteId)+"")){
					return result;
				}
			}
			v35PortService = (V35PortService_MB) ConstantUtil.serviceFactory.newService_MB(Services.V35PORT);
			if(TypeAndActionUtil.ACTION_SAVEANDUPDATE.equals(action)){
				v35PortService.saveOrUpdate(v35PortInfo);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
			}
			
		} catch (Exception e) {
			ExceptionManage.dispose(e, SiteUtil.class);
		}finally{
			v35PortInfo = null;
			UiUtil.closeService_MB(v35PortService);
		}
		return result;	
	}

	/**
	 * 虚拟网元的mac的操作方法
	 * @param action
	 * @param object
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	private String macManageAction(String action, Object object) throws Exception {
		
		String result = null;
		List<MacManagementInfo> list = null;
		MacManagementInfo macInfo = null;
		MacManageService_MB service = null;
		
		try {
			
			if(object instanceof List){
				list = (List<MacManagementInfo>) object;
				macInfo = list.get(0);
			}else{
				macInfo = (MacManagementInfo) object;
				list = new ArrayList<MacManagementInfo>();
				list.add(macInfo);
			}
			
			if("0".equals(SiteTypeUtil(macInfo.getSiteId())+"")){
				return result;
			}
			service = (MacManageService_MB) ConstantUtil.serviceFactory.newService_MB(Services.MACMANAGE);
			if(TypeAndActionUtil.ACTION_INSERT.equals(action)){
				service.save( macInfo);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}else if(TypeAndActionUtil.ACTION_DELETE.equals(action)){
				service.delete(list);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}
			else if(TypeAndActionUtil.ACTION_UPDATE.equals(action)){
				service.update(macInfo);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}
		} catch (Exception e) {
			throw e;
		}finally{
			UiUtil.closeService_MB(service);
		}
		return result;
	}
	
	/**
	 * 虚拟网元的mac学习数目限制的操作方法
	 * @param action
	 * @param object
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	private String macLearnAction(String action, Object object) throws Exception {
		
		String result = null;
		List<MacLearningInfo> list = null;
		MacLearningInfo macInfo = null;
//		macInfo = (MacLearningInfo) object;
		MacLearningService_MB service = null;
		
		try {
			if(object instanceof List){
				list = (List<MacLearningInfo>) object;
				macInfo = list.get(0);
			}else{
				macInfo = (MacLearningInfo) object;
				list = new ArrayList<MacLearningInfo>();
				list.add(macInfo);
			}
			
			if("0".equals(SiteTypeUtil(macInfo.getSiteId())+"")){
				return result;
			}
			 service = (MacLearningService_MB) ConstantUtil.serviceFactory.newService_MB(Services.MACLEARN);
			if(TypeAndActionUtil.ACTION_INSERT.equals(action)){
				service.save( macInfo);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}else if(TypeAndActionUtil.ACTION_DELETE.equals(action)){
				service.delete(list);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}
			else if(TypeAndActionUtil.ACTION_UPDATE.equals(action)){
				service.update(macInfo);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}
		} catch (Exception e) {
			throw e;
		}finally{
			UiUtil.closeService_MB(service);
		}
		return result;
	}

	/**
	 * 删除双规保护
	 * @param action	操作方法
	 * @param object	操作对象
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private String dualInfoActionForList(String action, Object object) throws Exception {
		
		String result = null;
		int rootsiteId;
		int branchSiteId;
		List<DualInfo> dualInfos = (List<DualInfo>) object;
		DualInfoService_MB dualInfoService = null;
		
		try {
			dualInfoService = (DualInfoService_MB)ConstantUtil.serviceFactory.newService_MB(Services.DUALINFO);
			for(DualInfo dualInfo : dualInfos){
				rootsiteId = dualInfo.getRootSite();
				if("0".equals(SiteTypeUtil(rootsiteId)+"")){
					return result;
				}else if(dualInfo.getBranchMainSite()>0){
					branchSiteId = dualInfo.getBranchMainSite();
					if("0".equals(SiteTypeUtil(branchSiteId)+"")){
						return result;
					}
				}else if(dualInfo.getBranchProtectSite()>0){
					branchSiteId = dualInfo.getBranchProtectSite();
					if("0".equals(SiteTypeUtil(branchSiteId)+"")){
						return result;
					}
				}
			}
			if(TypeAndActionUtil.ACTION_DELETE.equals(action)){
				dualInfoService.delete(dualInfos);
			}
			if(TypeAndActionUtil.ACTION_UPDATE.equals(action)){
				dualInfoService.update(dualInfos);
			}
			if(TypeAndActionUtil.ACTION_INSERT.equals(action)){
				dualInfoService.insert(dualInfos);
			}
			result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
			
		} catch (Exception e) {
			throw  e;
		}finally{
			UiUtil.closeService_MB(dualInfoService);
		}
		return result;
	}
	/**
	 * 虚拟网元复位
	 * @param action 操作方法
	 * @param object
	 * @return
	 */
	private String slotInstAction(String action, Object object) {
		return ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
	}

	/**
	 * 删除虚拟网元静态单播
	 * @param action 操作方法
	 * @param object
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private String staticUnicastActionForList(String action,Object object) throws Exception {
		
		String result = null;
		SingleSpreadService_MB spreadService = null;
		
		try {
			List<StaticUnicastInfo> staticUnicastInfoList = (List<StaticUnicastInfo>) object;
			if(staticUnicastInfoList.size()>0&&"0".equals(SiteTypeUtil(staticUnicastInfoList.get(0).getSiteId())+"")){
				return result;
			}
			if(TypeAndActionUtil.ACTION_DELETE.equals(action)){
				spreadService = (SingleSpreadService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SINGELSPREAD);
				spreadService.delete(staticUnicastInfoList);
			    result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
			}
		} catch (Exception e) {
			throw e;
		}finally{
			UiUtil.closeService_MB(spreadService);
		}
		return result;
	}

	/**
	 * 虚拟网元静态单播
	 * @param action
	 * @param object
	 * @return
	 * @throws Exception
	 */
	private String staticUnicastAction(String action, Object object) throws Exception {
		String result = null;
		SingleSpreadService_MB spreadService = null;
		try {
			spreadService = (SingleSpreadService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SINGELSPREAD);
			StaticUnicastInfo staticUnicastInfo = (StaticUnicastInfo) object;
			if("0".equals(SiteTypeUtil(staticUnicastInfo.getSiteId())+"")){
				return result;
			}
			if(TypeAndActionUtil.ACTION_INSERT.equals(action)){
				spreadService.insert(staticUnicastInfo);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
			}
			if(TypeAndActionUtil.ACTION_UPDATE.equals(action)){
				spreadService.update(staticUnicastInfo);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}finally{
			UiUtil.closeService_MB(spreadService);
		}
		
		return result;
	}

	/**
	 * 映射虚拟网元操作
	 * @param action 操作对象
	 * @param object	QosMappingMode集合
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	private String qosMappingModeActionForList(String action,
			Object object) throws Exception {
		String result = null;
		List<QosMappingMode> osMappingModeList = (List<QosMappingMode>) object;
		int siteId = osMappingModeList.get(0).getSiteId();
		if(0!=siteId){
			//真是网元
			if("0".equals(SiteTypeUtil(siteId)+"")){
				return result;
			}
		}else{
			//虚拟网元
			result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
		}
		return result;
	}

	
	/**
	 * 删除双规保护虚拟网元操作
	 * @param action	操作方法
	 * @param object	操作对象
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private String dualActionForList(String action, Object object) throws Exception {
		String result = null;
		DualProtectService_MB dualProtectService = null;
		try {
			List<DualProtect> dualProtectList = (List<DualProtect>) object;
			dualProtectService = (DualProtectService_MB)ConstantUtil.serviceFactory.newService_MB(Services.DUALPROTECTSERVICE);
			int siteId = dualProtectList.get(0).getSiteId();
			if(0!=siteId){
				//真是网元
				if("0".equals(SiteTypeUtil(siteId)+"")){
					return result;
				}
			}
			//删除
			if(TypeAndActionUtil.ACTION_DELETE.equals(action)){
				for(DualProtect dualProtect:dualProtectList){
					 dualProtectService.delete(dualProtect);
				}
			}
			result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			UiUtil.closeService_MB(dualProtectService);
		}
		return result;
	}
	/**
	 * 双规保护虚拟网元操作
	 * @param action 操作方法
	 * @param object 操作对象
	 * @return
	 * @throws BusinessIdException
	 * @throws Exception
	 */
	private String dualAction(String action, Object object) throws BusinessIdException, Exception {
		String result = null;
		DualProtectService_MB dualProtectService = null;
		try {
			DualProtect dualProtect = (DualProtect) object;
			int siteId = dualProtect.getSiteId();
			if(0!=siteId){
				//真是网元
				if("0".equals(SiteTypeUtil(siteId)+"")){
					return result;
				}
			}
			dualProtectService = (DualProtectService_MB)ConstantUtil.serviceFactory.newService_MB(Services.DUALPROTECTSERVICE);
			if(TypeAndActionUtil.ACTION_INSERT.equals(action)){
				dualProtectService.saveOrUpdate(dualProtect);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
			}else if(TypeAndActionUtil.ACTION_UPDATE.equals(action)){
				dualProtectService.saveOrUpdate(dualProtect);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			UiUtil.closeService_MB(dualProtectService);
		}
		return result;
	}

	/**
	 * 删除msp保护虚拟网元操作
	 * @param action	操作方法
	 * @param object	操作对象
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private String mspActionForList(String action, Object object) throws Exception {
		String result = null;
		MspProtectService_MB mspProtectService = null;
		try {
			List<MspProtect> mspProtectList = (List<MspProtect>) object;
			mspProtectService = (MspProtectService_MB)ConstantUtil.serviceFactory.newService_MB(Services.MSPPROTECT);
			int siteId = mspProtectList.get(0).getSiteId();
			if(0!=siteId){
				//真是网元
				if("0".equals(SiteTypeUtil(siteId)+"")){
					return result;
				}
			}
			if(TypeAndActionUtil.ACTION_DELETE.equals(action)){
				for(MspProtect mspProtect:mspProtectList){
					mspProtectService.delete(mspProtect);
				}
			}
			result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
		} catch (Exception e) {
			throw e;
		}finally{
			UiUtil.closeService_MB(mspProtectService);
		}
		return result;
	}
	/**
	 * msp保护虚拟网元操作
	 * @param action 操作方法
	 * @param object 操作对象
	 * @return
	 * @throws BusinessIdException
	 * @throws Exception
	 */
	private String mspAction(String action, Object object) throws BusinessIdException, Exception {
		String result = null;
		MspProtect mspProtect = (MspProtect) object;
		int siteId = mspProtect.getSiteId();
		if(0!=siteId){
			//真是网元
			if("0".equals(SiteTypeUtil(siteId)+"")){
				return result;
			}
		}
		MspProtectService_MB mspProtectService = null;
		try {
			mspProtectService =(MspProtectService_MB)ConstantUtil.serviceFactory.newService_MB(Services.MSPPROTECT);
			if(TypeAndActionUtil.ACTION_INSERT.equals(action)){
				mspProtectService.saveOrUpdate(mspProtect);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
			}else if(TypeAndActionUtil.ACTION_UPDATE.equals(action)){
				mspProtectService.saveOrUpdate(mspProtect);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}finally{
			UiUtil.closeService_MB(mspProtectService);
		}
		
		return result;
	}

	/**
	 * 端口配置（多）虚拟网元操作
	 * @param action  方法名
	 * @param object	操作对象
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private String ptpPortActionForList(String action, Object object) throws Exception {
		String result = null;
		PortDispositionInfoService_MB portDispositionInfoService = null;
		try {
			List<PortConfigInfo> portConfigInfoList = (List<PortConfigInfo>) object;
			portDispositionInfoService = (PortDispositionInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PortDispositionInfoService);
			int siteId = portConfigInfoList.get(0).getSiteId();
			if(0!=siteId){
				//真是网元
				if("0".equals(SiteTypeUtil(siteId)+"")){
					return result;
				}
			}
			if(TypeAndActionUtil.ACTION_DELETE.equals(action)){
				portDispositionInfoService.delete(portConfigInfoList);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
				
			}
		} catch (Exception e) {
			throw e;
		}finally{
			UiUtil.closeService_MB(portDispositionInfoService);
		}
		return result;
	}
	/**
	 * 时钟源配置（多）虚拟网元操作
	 * @param action  方法名
	 * @param object	操作对象
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private String portSourseActionForList(String action, Object object) throws Exception {
		String result = null;
		FrequencyClockManageService_MB frequencyClockManageService = null;
		try {
			List<ClockSource> portConfigInfoList = (List<ClockSource>) object;
			frequencyClockManageService = (FrequencyClockManageService_MB)ConstantUtil.serviceFactory.newService_MB(Services.FrequencyClockManageService);
			int siteId = portConfigInfoList.get(0).getSiteId();
			if(0!=siteId){
				//真是网元
				if("0".equals(SiteTypeUtil(siteId)+"")){
					return result;
				}
			}
			if(TypeAndActionUtil.ACTION_DELETE.equals(action)){
				frequencyClockManageService.delete(portConfigInfoList);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
			}
		} catch (Exception e) {
			throw e;
		}finally{
			UiUtil.closeService_MB(frequencyClockManageService);
		}
		return result;
	}
	/**
	 * 时钟源配置（单）虚拟网元操作
	 * @param action  方法名
	 * @param object	操作对象
	 * @return
	 * @throws Exception
	 */
	private String clockSourceAction(String action, Object object) throws Exception {
		String result = null;
		ClockSource clockSource = (ClockSource) object;
		int siteId = clockSource.getSiteId();
		if(0!=siteId){
			//真是网元
			if("0".equals(SiteTypeUtil(siteId)+"")){
				return result;
			}
		}
		FrequencyClockManageService_MB frequencyClockManageService = null;
		try {
			frequencyClockManageService =(FrequencyClockManageService_MB)ConstantUtil.serviceFactory.newService_MB(Services.FrequencyClockManageService);
			if(TypeAndActionUtil.ACTION_INSERT.equals(action)){
				frequencyClockManageService.insert(clockSource);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
			}else if(TypeAndActionUtil.ACTION_UPDATE.equals(action)){
				frequencyClockManageService.update(clockSource);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
			}else if(TypeAndActionUtil.ACTION_SYNCHRO.equals(action)){
				frequencyClockManageService.select(clockSource);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}finally{
			UiUtil.closeService_MB(frequencyClockManageService);
		}
		
		
		
		clockSource=null;
		object=null;
		return result;
		
	}

	/**
	 * 时钟(状态和属性)虚拟网元操作
	 * @param action 	方法名
	 * @param object	操作对象
	 * @return
	 * @throws Exception
	 */
	private String frequencyInfoNeClockAction(String action, Object object) throws Exception {
		String result = null;
		FrequencyInfoNeClock frequencyInfoNeClock = (FrequencyInfoNeClock) object;
		FrequencyInfoNeClockService_MB frequencyInfo_neClockService = null;
		try {
			int siteId = frequencyInfoNeClock.getSiteId();
			if(0!=siteId){
				//真是网元
				if("0".equals(SiteTypeUtil(siteId)+"")){
					return result;
				}
			}
			frequencyInfo_neClockService = (FrequencyInfoNeClockService_MB) ConstantUtil.serviceFactory.newService_MB(Services.FrequencyInfo_neClockService);
			if(TypeAndActionUtil.ACTION_SELECT.equals(action)){
				frequencyInfo_neClockService.select(frequencyInfoNeClock.getSiteId());
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
			}else if(TypeAndActionUtil.ACTION_SAVEANDUPDATE.equals(action)){
				frequencyInfo_neClockService.update(frequencyInfoNeClock);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
			}
		} catch (Exception e) {
			throw e;
		}finally{
			UiUtil.closeService_MB(frequencyInfo_neClockService);
			frequencyInfoNeClock=null;
			object=null;
		}
		return result;
	}
	/**
	 * 时钟Ptp 虚拟网元操作
	 * @param action 方法名
	 * @param object	操作对象
	 * @return
	 * @throws Exception
	 */
	private String timeManageInfoAction(String action, Object object) throws Exception {
		String result = null;
		TimeManageInfo timeManageInfo = (TimeManageInfo) object;
		TimeManageInfoService_MB timeManageInfoService = null;
		try {
			int siteId = timeManageInfo.getSiteId();
			if(0!=siteId){
				//真是网元
				if("0".equals(SiteTypeUtil(siteId)+"")){
					return result;
				}
			}
			timeManageInfoService = (TimeManageInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.TimeManageInfoService);
			if(TypeAndActionUtil.ACTION_SELECT.equals(action)){
				timeManageInfoService.select(timeManageInfo.getSiteId());
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
			}else if(TypeAndActionUtil.ACTION_SAVEANDUPDATE.equals(action)){
				timeManageInfoService.update(timeManageInfo);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
			}
		} catch (Exception e) {
			throw e;
		}finally{
			UiUtil.closeService_MB(timeManageInfoService);
			timeManageInfo=null;
			object=null;
		}
		return result;
		
	}
	/**
	 *线路时钟接口虚拟网元操作
	 * @param action 方法名
	 * @param object	操作对象
	 * @return
	 * @throws Exception
	 */
	private String lineClockInterfaceAction(String action, Object object) throws Exception {
		String result = null;
		LineClockInterface lineClockInterface = (LineClockInterface) object;
		TimeLineClockInterfaceService_MB timeLineClockInterfaceService = null;
		try {
			int siteId = lineClockInterface.getSiteId();
			if(0!=siteId){
				//真是网元
				if("0".equals(SiteTypeUtil(siteId)+"")){
					return result;
				}
			}
			timeLineClockInterfaceService = (TimeLineClockInterfaceService_MB)ConstantUtil.serviceFactory.newService_MB(Services.TimeLineClockInterfaceService);
			if(TypeAndActionUtil.ACTION_SYNCHRO.equals(action)){
				timeLineClockInterfaceService.select(lineClockInterface.getSiteId());
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
			}else if(TypeAndActionUtil.ACTION_UPDATE.equals(action)){
				timeLineClockInterfaceService.update(lineClockInterface);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
			}
		} catch (Exception e) {
			throw e;
		}finally{
			UiUtil.closeService_MB(timeLineClockInterfaceService);
			lineClockInterface=null;
			object=null;
		}
		return result;
		
	}
	/**
	 * 端口配置虚拟网元操作
	 * @param action 方法名
	 * @param object	操作对象
	 * @return
	 * @throws Exception
	 */
	private String portConfigInfoAction(String action, Object object) throws Exception {
		String result = null;
		PortConfigInfo portConfigInfo = (PortConfigInfo) object;
		PortDispositionInfoService_MB portDispositionInfoService = null;
		try {
			int siteId = portConfigInfo.getSiteId();
			if(0!=siteId){
				//真是网元
				if("0".equals(SiteTypeUtil(siteId)+"")){
					return result;
				}
			}
			portDispositionInfoService = (PortDispositionInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PortDispositionInfoService);
			if(TypeAndActionUtil.ACTION_SYNCHRO.equals(action)){
				portDispositionInfoService.select(portConfigInfo.getSiteId());
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
			}else if(TypeAndActionUtil.ACTION_UPDATE.equals(action)){
				portDispositionInfoService.update(portConfigInfo);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
			}else if(TypeAndActionUtil.ACTION_INSERT.equals(action)){
				portDispositionInfoService.insertPortDispositionInfo(portConfigInfo);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
			}
		} catch (Exception e) {
			throw e;
		}finally{
			UiUtil.closeService_MB(portDispositionInfoService);
			portConfigInfo=null;
			object=null;
		}
		return result;
		
	}
	/**
	 * 外接时钟接口虚拟网元操作
	 * @param action 方法名
	 * @param object	操作对象
	 * @return
	 * @throws Exception
	 */
	private String exClockInterfaceAction(String action, Object object) throws Exception {
		
		String result = null;
		ExternalClockInterface exClockInterface = (ExternalClockInterface) object;
		ExternalClockInterfaceService_MB externalClockInterfaceService = null;
		try {
			int siteId = exClockInterface.getSiteId();
			if(0!=siteId){
				//真是网元
				if("0".equals(SiteTypeUtil(siteId)+"")){
					return result;
				}
			}
			externalClockInterfaceService = (ExternalClockInterfaceService_MB)ConstantUtil.serviceFactory.newService_MB(Services.ExternalClockInterfaceService);
			if(TypeAndActionUtil.ACTION_INSERT.equals(action)){
				externalClockInterfaceService.insertTimeManageInfo(exClockInterface);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
			}else if(TypeAndActionUtil.ACTION_UPDATE.equals(action)){
				externalClockInterfaceService.update(exClockInterface);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
			}
		} catch (Exception e) {
			throw e;
		}finally{
			UiUtil.closeService_MB(externalClockInterfaceService);
			exClockInterface=null;
			object=null;
		}
		return result;
		
	}

	/**
	 * 虚拟网元同步操作
	 * @param action  方法名
	 * @param siteId  网元ID
	 * @return
	 * @throws Exception 
	 */
	public String irtualSiteSynchroAction(String action, int siteId) throws Exception {
		String result = null;		
		if(TypeAndActionUtil.ACTION_SYNCHRO.equals(action)){	
			//虚拟网元
			if("1".equals(SiteTypeUtil(siteId)+"")){
				result = ResourceUtil.srcStr(StringKeysTip.LBL_IRTUALSITE);
			}
		}
		return result;
	}	
	/**
	 * 参数为int的虚拟网元方法
	 * @param action  方法名
	 * @param object   网元ID
	 * @return
	 */
	public Object selectSiteAction(String action, Object object){
		SiteService_MB siteService = null;
		SiteInst siteInst;
		Object result = null;
		if(object instanceof Integer){
			try {
				siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE);
				siteInst = siteService.select((Integer)object);
				result = siteInst;
			} catch (Exception e) {
				ExceptionManage.dispose(e,SiteUtil.class);
			}finally{
				UiUtil.closeService_MB(siteService);
			}
		}
		return result;
		
	}
	/**
	 * 环保护（批量）虚拟网元操作
	 * @param action 方法名
	 * @param list  操作对象集合
	 * @return
	 * @throws Exception 
	 */
	private String loopProtectActionForList(String action, List<LoopProtectInfo> list) throws Exception {
		String result = null;
		List<LoopProtectInfo> protectInfos = (List<LoopProtectInfo> )list;
		int count = 0;
		WrappingProtectService_MB wrappingProtectService = null;
		if(protectInfos.get(0).getActiveStatus()==EActiveStatus.ACTIVITY.getValue()){
			for(LoopProtectInfo protectInfo:protectInfos){
				//虚拟网元
				if("1".equals(SiteTypeUtil(protectInfo.getSiteId())+"")){
					count++;
				}
				
			}
			if(0==count){
				return result;
			}
			if(count!=protectInfos.size()){
				return ResourceUtil.srcStr(StringKeysTip.LBL_DIFSITE);	
			}
		}
		try {
			wrappingProtectService = (WrappingProtectService_MB) ConstantUtil.serviceFactory.newService_MB(Services.WRAPPINGPROTECT);
			if(TypeAndActionUtil.ACTION_INSERT.equals(action)){	
				wrappingProtectService.insert(protectInfos);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}
			if(TypeAndActionUtil.ACTION_UPDATE.equals(action)){	
				wrappingProtectService.update(protectInfos);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}
			if(TypeAndActionUtil.ACTION_DELETE.equals(action)){
				wrappingProtectService.deleteByLoopId(protectInfos);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}
			if(TypeAndActionUtil.ACTION_SYNCHRO.equals(action)){	
				result = ResourceUtil.srcStr(StringKeysTip.LBL_IRTUALSITE);
			}
			return result;
		} catch (Exception e) {
			ExceptionManage.dispose(e,SiteUtil.class);
		}finally{
			protectInfos=null;
			UiUtil.closeService_MB(wrappingProtectService);
		}
		return result;
	}
	/**
	 *  搜索段虚拟网元操作
	 * @param action 方法名
	 * @param list 操作对象集合
	 * @return
	 * @throws Exception
	 */
	private String siteActionForList(String action, List<SiteInst> list) throws Exception {
		if(TypeAndActionUtil.ACTION_SEARCHSEGMENT.equals(action)){
			for (SiteInst siteInst : list) {
				if("1".equals(UiUtil.getCodeById(siteInst.getSiteType()).getCodeValue())){
					return ResourceUtil.srcStr(StringKeysTip.LBL_IRTUALSITE);	
				}
			}
		}
		return null;
	}
	/**
	 * PDH虚拟网元操作
	 * @param action 方法名
	 * @param object 操作对象
	 * @return
	 * @throws Exception
	 */
	private String e1Action(String action, Object object) throws Exception{
		
		String result = null;
		E1Info e1Info = (E1Info) object;
		E1InfoService_MB e1InfoService = null;
		
		try {
			int siteId = e1Info.getSiteId();
			if(0!=siteId){
				//真是网元
				if("0".equals(SiteTypeUtil(siteId)+"")){
					return result;
				}
			}
			e1InfoService = (E1InfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.E1Info);
			if(TypeAndActionUtil.ACTION_UPDATE.equals(action)){
				e1InfoService.update(e1Info);
				return ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
			}else if(TypeAndActionUtil.ACTION_SYNCHRO.equals(action)){
				e1InfoService.selectByCondition(e1Info);
				return ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}else if(TypeAndActionUtil.ACTION_INSERT.equals(action)){
				e1InfoService.saveOrUpdate(e1Info);
				return ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}
		} catch (Exception e) {
			throw e;
		}finally{
			UiUtil.closeService_MB(e1InfoService);
			e1Info=null;
			object=null;
		}
		return result;
		
	}

	/**
	 * 长期定时任务
	 * @param action 方法名
	 * @param object 操作对象
	 * @return
	 * @throws Exception 
	 */
	private String performanceTaskAction(String action, Object object) throws Exception {
		String result =null;
		PerformanceTaskInfo performanceTaskInfo = (PerformanceTaskInfo)object;
		SiteInst siteInst = performanceTaskInfo.getSiteInst();
		//虚拟网元
		if("1".equals(SiteTypeUtil(siteInst.getSite_Inst_Id())+"")){
			result = ResourceUtil.srcStr(StringKeysTip.LBL_IRTUALSITE);	
		}
		
		siteInst=null;
		return result;
	}
	/**
	 * 当前性能虚拟网元操作
	 * @param action 方法名
	 * @param object	操作对象
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("null")
	private CurrentPerformanceFilter currentPerformanceAction(String action, Object object) throws Exception {
		CurrentPerformanceFilter filter = (CurrentPerformanceFilter) object;
		List<Integer> siteIds = filter.getSiteInsts();
		List<Integer> resultSiteIds = null;
		for(int siteId:siteIds){
			//真是网元
			if("0".equals(SiteTypeUtil(siteId)+"")){
				resultSiteIds.add(siteId);
			}
		}
		filter.setSiteInsts(resultSiteIds);
		
		siteIds=null;
		return filter;
	}
	/**
	 * 保护倒换虚拟网元操作
	 * @param action	 方法名
	 * @param object	操作对象
	 * @return
	 * @throws Exception
	 */
	private String protectRorateAction(String action, Object object) throws Exception {
		String result = null;
//		int aSiteId;
//		int zSiteId;
//		SiteRoate protectRorateInfo = (SiteRoate) object;
//		TunnelService tunnelService = (TunnelService) ConstantUtil.serviceFactory.newService(Services.Tunnel);;
//		Tunnel tunnel = new Tunnel();
//		tunnel.setTunnelId(protectRorateInfo.getTunnelId());
//		tunnel =  tunnelService.select_nojoin(tunnel).get(0);
//	
//		List<Lsp> lspList = tunnel.getLspParticularList();
//		for(Lsp lsp:lspList){
//			aSiteId = lsp.getASiteId();
//			zSiteId = lsp.getZSiteId();
//			if(0!=aSiteId&&0!=zSiteId){
//				if(!(SiteTypeUtil(aSiteId)+"").equals(SiteTypeUtil(zSiteId)+"")){
//					return ResourceUtil.srcStr(StringKeysTip.LBL_DIFSITE);	
//				}else if(("0".equals(SiteTypeUtil(aSiteId)+"")&& ("0".equals(SiteTypeUtil(zSiteId)+"")))){
//					return result;
//				}
//				
//			}else{
//				int siteId = 0;
//				if(0!=aSiteId){
//					siteId = aSiteId;
//				}
//				if(0!=zSiteId){
//					siteId = zSiteId;
//				}
//				if("0".equals(SiteTypeUtil(siteId)+"")){
//					return result;
//				}
//			}
//			if (tunnel.getTunnelStatus() == EActiveStatus.ACTIVITY.getValue()) {
//				return ResourceUtil.srcStr(StringKeysTip.LBL_IRTUALSITEACTIVITY);	
//			}
//		}
//		ProtectRorateInfoService protectRorateService = (ProtectRorateInfoService)ConstantUtil.serviceFactory.newService(Services.PROTECTRORATE);
//		if(TypeAndActionUtil.ACTION_INSERT.equals(action)){
//			protectRorateService.insert(protectRorateInfo);
//			result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
//		}
//		if(TypeAndActionUtil.ACTION_UPDATE.equals(action)){
//			protectRorateService.update(protectRorateInfo);
//			result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
//		}
//		protectRorateInfo=null;
//		protectRorateService=null;
		return result;	
	}
	/**
	 * ospf（批量）虚拟网元操作
	 * @param action	 方法名
	 * @param list	操作对象集合
	 * @return
	 * @throws Exception
	 */
	private String OSPFActionForList(String action, List<OSPFInterface> list) throws Exception {
		String result = null;
		for(OSPFInterface oSPFInterface:list){
			if("0".equals(SiteTypeUtil(Integer.parseInt(oSPFInterface.getNeId()))+"")){ 
				return result;
			}
		}
		OSPFInterfaceService_MB ospfInterfaceService = null;
		try {
			ospfInterfaceService = (OSPFInterfaceService_MB) ConstantUtil.serviceFactory.newService_MB(Services.OSPFINTERFACE);
			if(TypeAndActionUtil.ACTION_DELETE.equals(action)){
				for(OSPFInterface ospfInterface:list){
					ospfInterfaceService.delete(ospfInterface);
				}
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}finally{
			UiUtil.closeService_MB(ospfInterfaceService);
		}
		return result;
	}
	/**
	 * ospf虚拟网元操作
	 * @param action	 方法名
	 * @param list	操作对象
	 * @return
	 * @throws Exception
	 */
	private String OSPFInstAction(String action, Object object) throws Exception {
		String result = null;
		OSPFInterface ospfInterface = (OSPFInterface) object;
		OSPFInterfaceService_MB ospfInterfaceService = null;
		try {
			int siteId = Integer.parseInt(ospfInterface.getNeId());
			if(0!=siteId){
				if("0".equals(SiteTypeUtil(siteId)+"")){
					return result;
				}
			}
			ospfInterfaceService = (OSPFInterfaceService_MB) ConstantUtil.serviceFactory.newService_MB(Services.OSPFINTERFACE);
			if(TypeAndActionUtil.ACTION_INSERT.equals(action)){
				ospfInterfaceService.insert(ospfInterface);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
			}
			if(TypeAndActionUtil.ACTION_UPDATE.equals(action)){
				ospfInterfaceService.update(ospfInterface);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
			}
		} catch (Exception e) {
			throw e;
		}finally{
			ospfInterface=null;
			UiUtil.closeService_MB(ospfInterfaceService);
		}
		return result;	
	}
	/**
	 * e1虚拟网元操作
	 * @param action 方法名
	 * @param list	操作对象
	 * @return
	 * @throws Exception
	 */
	private String pdhActionForList(String action, List<E1Info> list) throws Exception {
		
		String result = null;
		int siteId = 0;
		E1InfoService_MB e1InfoService = null;
		
		try {
			// cx、wuhan 通用
			if(TypeAndActionUtil.ACTION_UPDATE.equals(action)){
				e1InfoService= (E1InfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.E1Info);
				for(E1Info e1Info:list){
					siteId = e1Info.getSiteId();
					if("0".equals(SiteTypeUtil(siteId)+"")){
						return result;
					}
					e1InfoService.update(e1Info);;
				}
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}
		} catch (Exception e) {
			throw e;
		}finally{
			UiUtil.closeService_MB(e1InfoService);
		}
		return result;
	}
	/**
	 * 网元虚拟网元操作
	 * @param action	方法名
	 * @param object	操作对象
	 * @return
	 * @throws Exception
	 */
	private String siteInstAction(String action, Object object) throws Exception {
		
		String result = null;
		SiteInst siteInst  = (SiteInst) object;
		SiteService_MB siteService = null;
		
		try {
			if(TypeAndActionUtil.ACTION_SAVEANDUPDATE.equals(action)){
				if("1".equals(UiUtil.getCodeById(siteInst.getSiteType()).getCodeValue())||0==siteInst.getLoginstatus()){
					siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE);
					siteService.saveOrUpdate(siteInst);
					result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
				}else{
					return result;
				}
				
				
			}if(TypeAndActionUtil.ACTION_CLEARSITE.equals(action)){
				if("0".equals(UiUtil.getCodeById(siteInst.getSiteType()).getCodeValue())||1==siteInst.getLoginstatus()){
					result = ResourceUtil.srcStr(StringKeysTip.LBL_IRTUALSITE);	
				}else{
					return result;
				}
			}
			if(TypeAndActionUtil.ACTION_UPDATE.equals(action)){
				//虚拟网元或者离线网元
				if("1".equals(UiUtil.getCodeById(siteInst.getSiteType()).getCodeValue())||0==siteInst.getLoginstatus()){
//				siteInst.setLoginstatus(1);   网元下载有影响  ，暂时注释掉
					siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE);
					siteService.saveOrUpdate(siteInst);
					result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
				}else{
					return result;
				}
			}
		} catch (Exception e) {
			throw e;
		}finally{
			UiUtil.closeService_MB(siteService);
		}
		return result;	
	}
	/**
	 * Qos 2个参数方法虚拟网元操作
	 * @param action	方法名
	 * @param object1  操作对象
	 * @param object2 操作对象
	 * @return
	 * @throws Exception
	 */
	public String irtualSiteAction(String action,Object object1,Object object2) throws Exception {
		String result = null;
		@SuppressWarnings("unchecked")
		List<QosInfo> list = (List<QosInfo>) object1;
		if(list.get(0) instanceof QosInfo){
			result = qosUpdateAction(action,list,object2);	
		}
		return result;
		
	}
	/**
	 * QOS虚拟网元操作
	 * @param action 方法名
	 * @param list	操作对象
	 * @param object	操作对象
	 * @return
	 * @throws Exception
	 */
	private String qosUpdateAction(String action, List<QosInfo> list,Object object) throws Exception {
		String result = null;
		int aSiteId ;
		int zSiteId ;
		PwInfo pwInfo;
		Tunnel tunnel;
		QosRelevanceService_MB qosRelevanceService = null;
		
		try {
			qosRelevanceService = (QosRelevanceService_MB) ConstantUtil.serviceFactory.newService_MB(Services.QOSRELEVANCE);
			if(object instanceof Tunnel){
				tunnel= (Tunnel) object;
				List<Lsp> lspList = tunnel.getLspParticularList();
				for(Lsp lsp:lspList){
					aSiteId = lsp.getASiteId();
					zSiteId = lsp.getZSiteId();
					if(0!=aSiteId&&0!=zSiteId){
						if("0".equals(SiteTypeUtil(aSiteId)+"")&& ("0".equals(SiteTypeUtil(zSiteId)+""))){
							return result;
						}
						
					}else{
						int siteId = 0;
						if(0!=aSiteId){
							siteId = aSiteId;
						}
						if(0!=zSiteId){
							siteId = zSiteId;
						}
						if("0".equals(SiteTypeUtil(siteId)+"")){
							return result;
						}
					}
					tunnel.setQosList(list);
					if(tunnel.getTunnelStatus() == UiUtil.getCodeByValue("PROTECTTYPE", "2").getId()){
						tunnel.getProtectTunnel().setQosList(list);
					}
//				if (tunnel.getTunnelStatus() == EActiveStatus.ACTIVITY.getValue()&&0==SiteUtil.SiteTypeUtil(ConstantUtil.siteId)) {
//					return ResourceUtil.srcStr(StringKeysTip.LBL_IRTUALSITEACTIVITY);	
//				}
				}
				
			}
			if(object instanceof PwInfo){
				pwInfo= (PwInfo) object;
				aSiteId = pwInfo.getASiteId();
				zSiteId = pwInfo.getZSiteId();
				if(0!=aSiteId&&0!=zSiteId){
					if(!(SiteTypeUtil(aSiteId)+"").equals(SiteTypeUtil(zSiteId)+"")){
						return ResourceUtil.srcStr(StringKeysTip.LBL_DIFSITE);	
					}else if(("0".equals(SiteTypeUtil(aSiteId)+"")&& ("0".equals(SiteTypeUtil(zSiteId)+"")))){
						return result;
					}
					
				}else{
					int siteId = 0;
					if(0!=aSiteId){
						siteId = aSiteId;
					}
					if(0!=zSiteId){
						siteId = zSiteId;
					}
					if("0".equals(SiteTypeUtil(siteId)+"")){
						return result;
					}
				}
				pwInfo.setQosList(list);
//			if (pwInfo.getPwStatus() == EActiveStatus.ACTIVITY.getValue()) {
//				return ResourceUtil.srcStr(StringKeysTip.LBL_IRTUALSITEACTIVITY);	
//			}
			}
			qosRelevanceService.save(qosRelevanceService.getList(object));
			result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
		} catch (Exception e) {
			throw e;
		}finally{
			UiUtil.closeService_MB(qosRelevanceService);
		}
		return result;
	}
	/**
	 * OAM虚拟网元操作
	 * @param action 	方法名
	 * @param object	操作对象
	 * @return
	 * @throws Exception
	 */
	private String oamAction(String action, Object object) throws Exception {
		
		String result = null;
		OamInfo oamInfo  = (OamInfo) object;
		int siteId = 0;
		OamInfoService_MB oamInfoService = null;
		try {
			
			OamMipInfo  oamMip = oamInfo.getOamMip();
			OamMepInfo oamMep = oamInfo.getOamMep();
			OamLinkInfo oamLinkInfo = oamInfo.getOamLinkInfo();
			if(null!=oamMip){
				siteId = oamMip.getSiteId();
			}
			if(null!=oamMep){
				siteId = oamMep.getSiteId();
			}
			if(null!=oamLinkInfo){
				siteId = oamLinkInfo.getSiteId();
			}
			if(0!=siteId){
				if("0".equals(SiteTypeUtil(siteId)+"")){
					return result;
				}
			}
			if (oamInfo != null) {
				oamInfoService = (OamInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.OamInfo);
				oamInfoService.saveOrUpdate(oamInfo);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
			}
		} catch (Exception e) {
			throw e;
		}finally{
			oamInfo = null;
			UiUtil.closeService_MB(oamInfoService);
		}
		return result;	
	}
	/**
	 * ETHOAM虚拟网元操作
	 * @param action 	方法名
	 * @param object	操作对象
	 * @return
	 * @throws Exception
	 */
	private String ethOamAction(String action, Object object) throws Exception {
		String result = null;
		OamEthernetInfo oamInfo  = (OamEthernetInfo) object;
		OamEthreNetService_MB oamInfoService = null;
		try {
			
			int mipSiteId = 0;
			if(null!=oamInfo){
				mipSiteId = oamInfo.getSiteId();
			}
			//0为真是网元 、1为虚拟网元或离线网元
			if(0!=mipSiteId){
				if("0".equals(SiteTypeUtil(mipSiteId)+"")){
					return result;
				}
			}
			oamInfoService = (OamEthreNetService_MB) ConstantUtil.serviceFactory.newService_MB(Services.OAMETHERNET);
			if(TypeAndActionUtil.ACTION_SAVEANDUPDATE.equals(action)){
				if (oamInfo != null) {
					if(oamInfo.getId()>0){
						oamInfoService.update(oamInfo);
					}else{
						oamInfoService.insert(oamInfo);
					}
					result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
				}
			}else if(TypeAndActionUtil.ACTION_DELETE.equals(action)){
				oamInfoService.delete(oamInfo);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
			}
		} catch (Exception e) {
			throw e;
		}finally{
			oamInfo = null;
			UiUtil.closeService_MB(oamInfoService);
		}
		return result;	
	}
	
	
	/**
	 * ACL配置虚拟网元操作
	 * @param action 	方法名
	 * @param object	操作对象
	 * @return
	 * @throws Exception
	 */
	private String aclAction(String action, Object object) throws Exception {
		String result = null;
		AclInfo aclInfo  = null;
		int siteId = 0;
		AclService_MB aclService = null;
		List<AclInfo> aclInfoList = null;
		try {
			aclInfoList =(List<AclInfo>)object;
			aclInfo  = aclInfoList.get(0);
			if(null!=aclInfo){
				siteId = aclInfo.getSiteId();
			}
			//0为真是网元 、1为虚拟网元或离线网元
			if(0!=siteId){
				if("0".equals(SiteTypeUtil(siteId)+"")){
					return result;
				}
			}
			aclService = (AclService_MB) ConstantUtil.serviceFactory.newService_MB(Services.ACLSERCVICE);
			if(TypeAndActionUtil.ACTION_SAVEANDUPDATE.equals(action)){
				if (aclInfo != null) {
					if(aclInfo.getId()>0){
						aclService.update(aclInfo);
					}else{
						aclService.save(aclInfo);
					}
					result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
				}
			}else if(TypeAndActionUtil.ACTION_DELETE.equals(action)){
				for(AclInfo aclInfoOther : aclInfoList){
					aclService.delete(aclInfoOther);
				}
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
				
			}
		} catch (Exception e) {
			throw e;
		}finally{
			UiUtil.closeService_MB(aclService);
		}
		return result;	
	}
	/**
	 * OAM(批量)虚拟网元操作
	 * @param action 方法名
	 * @param list 操作对象
	 * @return
	 * @throws Exception
	 */
	private String oamActionForList(String action, List<OamInfo> list) throws Exception {
		
		String result = null;
		OamInfoService_MB oamInfoService = null;
		int siteId = 0;
		
		try {
			if(TypeAndActionUtil.ACTION_DELETE.equals(action)){
				oamInfoService = (OamInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.OamInfo);
				for(OamInfo oamInfo:list){
					OamMipInfo  oamMip = oamInfo.getOamMip();
					OamMepInfo oamMep = oamInfo.getOamMep();
					OamLinkInfo oamLinkInfo = oamInfo.getOamLinkInfo();
					if(null!=oamMip){
						siteId = oamMip.getSiteId();
					}
					if(null!=oamMep){
						siteId = oamMep.getSiteId();
					}
					if(null!=oamLinkInfo){
						siteId = oamLinkInfo.getSiteId();
					}
					if(0!=siteId){
						if("0".equals(SiteTypeUtil(siteId)+"")){
							return result;
						}
					}
					
					oamInfoService.deleteById(oamInfo);
				}
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}
		} catch (Exception e) {
			throw e;
		}finally{
			UiUtil.closeService_MB(oamInfoService);
		}
		return result;
	}
	/**
	 * CES虚拟网元操作
	 * @param action	方法名
	 * @param list	操作对象
	 * @return
	 * @throws Exception
	 */
	private String CESActionForList(String action, List<CesInfo> list) throws Exception {
		String result = null;
		int aSiteId = 0;
		int zSiteId = 0;
		CesInfoService_MB cesInfoService = null;
		try {
			for(CesInfo cesInfo:list){
				aSiteId = cesInfo.getaSiteId();
				zSiteId = cesInfo.getzSiteId();
				if(0!=aSiteId&&0!=zSiteId){
					if(!(SiteTypeUtil(aSiteId)+"").equals(SiteTypeUtil(zSiteId)+"")){
						return ResourceUtil.srcStr(StringKeysTip.LBL_DIFSITE);	
					}else if(("0".equals(SiteTypeUtil(aSiteId)+"")&& ("0".equals(SiteTypeUtil(zSiteId)+"")))){
						return result;
					}
					
				}else{
					int siteId = 0;
					if(0!=aSiteId){
						siteId = aSiteId;
					}
					if(0!=zSiteId){
						siteId = zSiteId;
					}
					if("0".equals(SiteTypeUtil(siteId)+"")){
						return result;
					}
				}
//			if (cesInfo.getActiveStatus() == EActiveStatus.ACTIVITY.getValue()) {
//				return ResourceUtil.srcStr(StringKeysTip.LBL_IRTUALSITEACTIVITY);	
//			}
			}
			cesInfoService = (CesInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.CesInfo);
			if(TypeAndActionUtil.ACTION_INSERT.equals(action)){	
				cesInfoService.save(list);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}
			if(TypeAndActionUtil.ACTION_DELETE.equals(action)){
				cesInfoService.delete(list);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}
		} catch (Exception e) {
			throw e;
		}finally{
			UiUtil.closeService_MB(cesInfoService);
			list = null;
		}
		return result;
	}

	/*private static String CesAction(String action, Object object) throws Exception {
		String result = null;
		ElineInfo elineInfo = (ElineInfo) object;
		int aSiteId = elineInfo.getzSiteId();
		int zSiteId = elineInfo.getzSiteId();
		if(0!=aSiteId&&0!=zSiteId){
			if(!(SiteTypeUtil(aSiteId)+"").equals(SiteTypeUtil(zSiteId)+"")){
				return ResourceUtil.srcStr(StringKeysTip.LBL_DIFSITE);	
			}else if(("0".equals(SiteTypeUtil(aSiteId)+"")&& ("0".equals(SiteTypeUtil(zSiteId)+"")))){
					return result;
				}			
		}else{
			int siteId = 0;
			if(0!=aSiteId){
				siteId = aSiteId;
			}
			if(0!=zSiteId){
				siteId = zSiteId;
			}
			if("0".equals(SiteTypeUtil(siteId)+"")){
				return result;
			}
		}
		if (elineInfo.getActiveStatus() == EActiveStatus.ACTIVITY.getValue()) {
			return ResourceUtil.srcStr(StringKeysTip.LBL_IRTUALSITEACTIVITY);	
		}
		if(TypeAndActionUtil.ACTION_UPDATE.equals(action)){
			ElineService elineService = (ElineService) ConstantUtil.serviceFactory.newService(Services.Eline);
			elineService.update(elineInfo);
			return ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
		}
		return result;	
	}
*/
	/**
	 * ETREE虚拟网元操作
	 * @param action 方法名
	 * @param list 操作对象
	 * @return
	 * @throws Exception
	 */
	private String eTreeActionForList(String action, List<EtreeInfo> list) throws Exception {
		String result = null;
		int aSiteId ;
		int zSiteId ;
		List<EtreeInfo> etreeInfoList = (List<EtreeInfo>) list;
		EtreeInfoService_MB etreeService = null;
		try {
			
			for(EtreeInfo etreeInfo:list){
				aSiteId = etreeInfo.getBranchSite();
				zSiteId = etreeInfo.getRootSite();
				if(0!=aSiteId&&0!=zSiteId){
					if(!(SiteTypeUtil(aSiteId)+"").equals(SiteTypeUtil(zSiteId)+"")){
						return ResourceUtil.srcStr(StringKeysTip.LBL_DIFSITE);	
					}else if(("0".equals(SiteTypeUtil(aSiteId)+"")&& ("0".equals(SiteTypeUtil(zSiteId)+"")))){
						return result;
					}
				}else{
					int siteId = 0;
					if(0!=aSiteId){
						siteId = aSiteId;
					}
					if(0!=zSiteId){
						siteId = zSiteId;
					}
					if("0".equals(SiteTypeUtil(siteId)+"")){
						return result;
					}
				}
//			if (etreeInfo.getActiveStatus() == EActiveStatus.ACTIVITY.getValue()) {
//				return ResourceUtil.srcStr(StringKeysTip.LBL_IRTUALSITEACTIVITY);	
//			}
			}	
			etreeService = (EtreeInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.EtreeInfo);
			if(TypeAndActionUtil.ACTION_DELETE.equals(action)){
				EtreeDispatch EtreeDispatch = new EtreeDispatch();
				EtreeDispatch.deleteDao(list);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}
			if(TypeAndActionUtil.ACTION_INSERT.equals(action)){
				etreeService.insert(etreeInfoList);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}
			if(TypeAndActionUtil.ACTION_UPDATE.equals(action)){
				etreeService.update(etreeInfoList);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}
		} catch (Exception e) {
			throw e;
		}finally{
			UiUtil.closeService_MB(etreeService);
			etreeInfoList = null;
			list = null;
		}
		return result;
	}
	/**
	 * ELAN虚拟网元操作
	 * @param action 方法名
	 * @param list 操作对象
	 * @return
	 * @throws Exception
	 */
	private String eLanActionForList(String action, List<ElanInfo> list) throws Exception {
		String result = null;
		int aSiteId = 0;
		int zSiteId = 0;
		ElanInfoService_MB elanInfoService = null ;
		try {
			for(ElanInfo elanInfo:list){
				aSiteId = elanInfo.getaSiteId();
				zSiteId = elanInfo.getzSiteId();
				if(0!=aSiteId&&0!=zSiteId){
					if(!(SiteTypeUtil(aSiteId)+"").equals(SiteTypeUtil(zSiteId)+"")){
						return ResourceUtil.srcStr(StringKeysTip.LBL_DIFSITE);	
					}else if(("0".equals(SiteTypeUtil(aSiteId)+"")&& ("0".equals(SiteTypeUtil(zSiteId)+"")))){
						return result;
					}
				}else{
					int siteId = 0;
					if(0!=aSiteId){
						siteId = aSiteId;
					}
					if(0!=zSiteId){
						siteId = zSiteId;
					}
					if("0".equals(SiteTypeUtil(siteId)+"")){
						return result;
					}
				}
//			if (elanInfo.getActiveStatus() == EActiveStatus.ACTIVITY.getValue()) {
//				return ResourceUtil.srcStr(StringKeysTip.LBL_IRTUALSITEACTIVITY);	
//			}
			}	
			elanInfoService = (ElanInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.ElanInfo);
			if(TypeAndActionUtil.ACTION_DELETE.equals(action)){
				ElanDispatch elanDispatch =new ElanDispatch();
				elanDispatch.deleteDao(list);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}else if(TypeAndActionUtil.ACTION_INSERT.equals(action)){
				List<ElanInfo> elanInfoList = (List<ElanInfo>) list;
				elanInfoService.insert(elanInfoList);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}else if(TypeAndActionUtil.ACTION_UPDATE.equals(action)){
				elanInfoService.update(list);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}
		} catch (Exception e) {
			throw e;
		}finally{
			UiUtil.closeService_MB(elanInfoService);
			list = null;
		}
		return result;

	}
	/**
	 * ELINE(批量)虚拟网元操作
	 * @param action 方法名
	 * @param list 操作对象
	 * @return
	 * @throws Exception
	 */
	private String eLineActionForList(String action, List<ElineInfo> list) throws Exception {
		String result = null;
		int aSiteId;
		int zSiteId;
		ElineInfoService_MB elineService = null;
		try {
			for(ElineInfo elineInfo:list){
				aSiteId = elineInfo.getaSiteId();
				zSiteId = elineInfo.getzSiteId();
				if(0!=aSiteId&&0!=zSiteId){
					if(!(SiteTypeUtil(aSiteId)+"").equals(SiteTypeUtil(zSiteId)+"")){
						return ResourceUtil.srcStr(StringKeysTip.LBL_DIFSITE);	
					}else if(("0".equals(SiteTypeUtil(aSiteId)+"")&& ("0".equals(SiteTypeUtil(zSiteId)+"")))){
						return result;
					}
				}else{
					int siteId = 0;
					if(0!=aSiteId){
						siteId = aSiteId;
					}
					if(0!=zSiteId){
						siteId = zSiteId;
					}
					if("0".equals(SiteTypeUtil(siteId)+"")){
						return result;
					}
				}
				
			}	
			if(TypeAndActionUtil.ACTION_DELETE.equals(action)){
				if(TypeAndActionUtil.ACTION_DELETE.equals(action)){
					elineService = (ElineInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.Eline);
					elineService.delete(list);
					result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
				}
			}
		} catch (Exception e) {
			throw e;
		}finally{
			UiUtil.closeService_MB(elineService);
			list = null;
		}
		return result;
	}
	/**
	 * ETREE虚拟网元操作
	 * @param action 方法名
	 * @param object 操作对象
	 * @return
	 * @throws Exception
	 */
	private String eLineAction(String action, Object object) throws Exception {
		String result = null;
		ElineInfo elineInfo = (ElineInfo) object;
		int aSiteId = elineInfo.getaSiteId();
		int zSiteId = elineInfo.getzSiteId();
		ElineInfoService_MB elineService = null;
		
		try {
			if(0!=aSiteId&&0!=zSiteId){
				if(!(SiteTypeUtil(aSiteId)+"").equals(SiteTypeUtil(zSiteId)+"")){
					return ResourceUtil.srcStr(StringKeysTip.LBL_DIFSITE);	
				}else if(("0".equals(SiteTypeUtil(aSiteId)+"")&& ("0".equals(SiteTypeUtil(zSiteId)+"")))){
					return result;
				}
			}else{
				int siteId = 0;
				if(0!=aSiteId){
					siteId = aSiteId;
				}
				if(0!=zSiteId){
					siteId = zSiteId;
				}
				if("0".equals(SiteTypeUtil(siteId)+"")){
					return result;
				}
			}
//		if (elineInfo.getActiveStatus()== EActiveStatus.ACTIVITY.getValue()) {
//			return ResourceUtil.srcStr(StringKeysTip.LBL_IRTUALSITEACTIVITY);	
//		}
			elineService = (ElineInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.Eline);
			if(TypeAndActionUtil.ACTION_INSERT.equals(action)){
				
				elineService.save(elineInfo);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
			}
			if(TypeAndActionUtil.ACTION_UPDATE.equals(action)){
				elineService.update(elineInfo);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
			}
		} catch (Exception e) {
			throw e;
		}finally{
			elineInfo=null;
			UiUtil.closeService_MB(elineService);
			object=null;
		}
		return result;	
	}
	/**
	 * PW虚拟网元操作
	 * @param action 方法名
	 * @param object 操作对象
	 * @return
	 * @throws Exception
	 */
	private String pwAction(String action, Object object) throws Exception {
		String result = null;
		PwInfo pwInfo = (PwInfo) object;
		int aSiteId = pwInfo.getASiteId();
		int zSiteId = pwInfo.getZSiteId();
		PwInfoService_MB pwService = null;
		
		try {
			if(0!=aSiteId&&0!=zSiteId){
				if(!(SiteTypeUtil(aSiteId)+"").equals(SiteTypeUtil(zSiteId)+"")){
					return ResourceUtil.srcStr(StringKeysTip.LBL_DIFSITE);	
				}else if(("0".equals(SiteTypeUtil(aSiteId)+"")&& ("0".equals(SiteTypeUtil(zSiteId)+"")))){
					return result;
				}
			}else{
				int siteId = 0;
				if(0!=aSiteId){
					siteId = aSiteId;
				}
				if(0!=zSiteId){
					siteId = zSiteId;
				}
				if("0".equals(SiteTypeUtil(siteId)+"")){
					return result;
				}
			}
//		if (pwInfo.getPwStatus() == EActiveStatus.ACTIVITY.getValue()) {
//			return ResourceUtil.srcStr(StringKeysTip.LBL_IRTUALSITEACTIVITY);	
//		}
			pwService = (PwInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PwInfo);
			if(TypeAndActionUtil.ACTION_INSERT.equals(action)){
				pwService.save(pwInfo);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
			}
			if(TypeAndActionUtil.ACTION_UPDATE.equals(action)){
				pwService.update(pwInfo);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
			}
		} catch (Exception e) {
			throw e;
		}finally{
			UiUtil.closeService_MB(pwService);
			pwInfo = null;
			object = null;
		}
		return result;	
	}
	/**
	 * PW（批量）虚拟网元操作
	 * @param action 方法名
	 * @param list 操作对象
	 * @return
	 * @throws Exception
	 */
 	private String pwActionForList(String action, List<PwInfo> list) throws Exception {
		String result = null;
		int aSiteId ;
		int zSiteId ;
		PwInfoService_MB pwInfoService = null;
		
		try {
			for(PwInfo pwInfo:list){
				aSiteId = pwInfo.getASiteId();
				zSiteId = pwInfo.getZSiteId();
				
				if(0!=aSiteId&&0!=zSiteId){
					if(!(SiteTypeUtil(aSiteId)+"").equals(SiteTypeUtil(zSiteId)+"")){
						return ResourceUtil.srcStr(StringKeysTip.LBL_DIFSITE);	
					}else if(("0".equals(SiteTypeUtil(aSiteId)+"")&& ("0".equals(SiteTypeUtil(zSiteId)+"")))){
						return result;
					}
				}else{
					int siteId = 0;
					if(0!=aSiteId){
						siteId = aSiteId;
					}
					if(0!=zSiteId){
						siteId = zSiteId;
					}
					if("0".equals(SiteTypeUtil(siteId)+"")){
						return result;
					}
				}
//			if (pwInfo.getPwStatus() == EActiveStatus.ACTIVITY.getValue()) {
//				return ResourceUtil.srcStr(StringKeysTip.LBL_IRTUALSITEACTIVITY);	
//			}
			}		
			pwInfoService = (PwInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PwInfo);
			if(TypeAndActionUtil.ACTION_DELETE.equals(action)){
				pwInfoService.delete(list);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}
		} catch (Exception e) {
			throw e;
		}finally{
			UiUtil.closeService_MB(pwInfoService);
			list = null;
		}
		return result;
	}
 	/**
	 * 段虚拟网元操作
	 * @param action 方法名
	 * @param object 操作对象
	 * @return
	 * @throws Exception
	 */
	private String segmentAction(String action, Object object) throws Exception {
		String result = null;
		/*	Segment segment = (Segment) object;
		int aSiteId = segment.getASITEID();
		int zSiteId = segment.getZSITEID();
		SegmentDispatch segmentDispatch;
		if(0!=aSiteId&&0!=zSiteId){
			if(!(SiteTypeUtil(aSiteId)+"").equals(SiteTypeUtil(zSiteId)+"")){
				return ResourceUtil.srcStr(StringKeysTip.LBL_DIFSITE);	
			}else if(("0".equals(SiteTypeUtil(aSiteId)+"")&& ("0".equals(SiteTypeUtil(zSiteId)+"")))){
				return result;
			}
		}else{
			int siteId = 0;
			if(0!=aSiteId){
				siteId = aSiteId;
			}
			if(0!=zSiteId){
				siteId = zSiteId;
			}
			if("0".equals(SiteTypeUtil(siteId)+"")){
				return result;
			}
		}
		segmentDispatch = new SegmentDispatch();
		if(TypeAndActionUtil.ACTION_INSERT.equals(action)){
			segmentDispatch.insertDao(segment);
			result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
		}else if(TypeAndActionUtil.ACTION_UPDATE.equals(action)){
			segmentDispatch.updateDao(segment);;
			result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
		}
		segment=null;
		segmentDispatch=null;
			*/
		
		Segment segment = (Segment) object;
		int aSiteId = segment.getASITEID();
		int zSiteId = segment.getZSITEID();
		if("1".equals(SiteTypeUtil(aSiteId)+"")){
			segment.setASITEID(0);
			segment.setAPORTID(0);
		}
		if("1".equals(SiteTypeUtil(zSiteId)+"")){
			segment.setZSITEID(0);
			segment.setZPORTID(0);
		}
		return result;
	}
	/**
	 * AC虚拟网元操作
	 * @param action 方法名
	 * @param list 操作对象
	 * @return
	 * @throws Exception
	 */
	private String acPortInfoActionForList(String action, List<AcPortInfo> list) throws Exception {
		String result = null;
		AcCXServiceImpl acCXServiceImpl;
		AcWHServiceImpl acWHServiceImpl;
		SiteService_MB	siteService = null;
		try {
			for(AcPortInfo acPortInfo:list){
				if(0==SiteTypeUtil(acPortInfo.getSiteId())){
					return result;
				}
			}
			siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE);
			if(TypeAndActionUtil.ACTION_DELETE.equals(action)){ 
				if(siteService.getManufacturer(list.get(0).getSiteId()) == EManufacturer.WUHAN.getValue()){
					acWHServiceImpl = new AcWHServiceImpl();
					acWHServiceImpl.excutionDelete(list);
					result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
				}else{
					acCXServiceImpl = new AcCXServiceImpl(); 
					acCXServiceImpl.deleteAcList(list);
					result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
				}
				
			}
		} catch (Exception e) {
			throw e;
		}finally{
			UiUtil.closeService_MB(siteService);
		}
		return result;
	}
	/**
	 * 段（批量）虚拟网元操作
	 * @param action 方法名
	 * @param list 操作对象
	 * @return
	 * @throws Exception
	 */
	private String segmentActionForList(String action, List<Segment> list) throws Exception {
		String result = null;
		/*int aSiteId ;
		int zSiteId ;
		SegmentService segmentService ;
		for(Segment segment:list){
			aSiteId = segment.getASITEID();
			zSiteId = segment.getZSITEID();
			if(0!=aSiteId&&0!=zSiteId){
				if(!(SiteTypeUtil(aSiteId)+"").equals(SiteTypeUtil(zSiteId)+"")){
					return ResourceUtil.srcStr(StringKeysTip.LBL_DIFSITE);	
				}else if(("0".equals(SiteTypeUtil(aSiteId)+"")&& ("0".equals(SiteTypeUtil(zSiteId)+"")))){
					return result;
				}
			}else{
				int siteId = 0;
				if(0!=aSiteId){
					siteId = aSiteId;
				}
				if(0!=zSiteId){
					siteId = zSiteId;
				}
				if("0".equals(SiteTypeUtil(siteId)+"")){
					return result;
				}
			}
			segmentService = (SegmentService) ConstantUtil.serviceFactory.newService(Services.SEGMENT);
		if(TypeAndActionUtil.ACTION_DELETE.equals(action)){
			segmentService.deleteByIds(list);
			result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
		}
	}		
		segmentService=null;*/
		
		for(Segment segment:list){
			int aSiteId = segment.getASITEID();
			int zSiteId = segment.getZSITEID();
			if("1".equals(SiteTypeUtil(aSiteId)+"")){
				segment.setASITEID(0);
				segment.setAPORTID(0);
			}
			if("1".equals(SiteTypeUtil(zSiteId)+"")){
				segment.setZSITEID(0);
				segment.setZPORTID(0);
			}
		}
		return result;
		
	}
	/**
	 * tunnel虚拟网元操作
	 * @param action 方法名
	 * @param object 操作对象
	 * @return
	 * @throws Exception
	 */
	private String tunnelAction(String action, Object object) throws Exception {
		TunnelService_MB tunnelService = null;
		int aSiteId;
		int zSiteId;
		String result = null;
		Tunnel tunnel = (Tunnel) object;
		List<Lsp> lspList = tunnel.getLspParticularList();
		try {
			for(Lsp lsp:lspList){
				aSiteId = lsp.getASiteId();
				zSiteId = lsp.getZSiteId();
				if(0!=aSiteId&&0!=zSiteId){
					if(!(SiteTypeUtil(aSiteId)+"").equals(SiteTypeUtil(zSiteId)+"")){
						return ResourceUtil.srcStr(StringKeysTip.LBL_DIFSITE);	
					}else if(("0".equals(SiteTypeUtil(aSiteId)+"")&& ("0".equals(SiteTypeUtil(zSiteId)+"")))){
						return result;
					}
					
				}else{
					int siteId = 0;
					if(0!=aSiteId){
						siteId = aSiteId;
					}
					if(0!=zSiteId){
						siteId = zSiteId;
					}
					if("0".equals(SiteTypeUtil(siteId)+"")){
						return result;
					}
				}
//			if (tunnel.getTunnelStatus() == EActiveStatus.ACTIVITY.getValue()) {
//				return ResourceUtil.srcStr(StringKeysTip.LBL_IRTUALSITEACTIVITY);	
//			}
			}
			tunnelService = (TunnelService_MB) ConstantUtil.serviceFactory.newService_MB(Services.Tunnel);
			if(TypeAndActionUtil.ACTION_INSERT.equals(action)){
				tunnelService.save(tunnel);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
			}else if(TypeAndActionUtil.ACTION_UPDATE.equals(action)){
				tunnelService.update(tunnel);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);	
			}else if(TypeAndActionUtil.ACTION_ROTATE.equals(action)){
				return ResourceUtil.srcStr(StringKeysTip.LBL_IRTUALSITE);	
			}
		} catch (Exception e) {
			throw e;
		}finally{
			UiUtil.closeService_MB(tunnelService);
			tunnel = null;
			object = null;
			lspList = null;
		}
		return result;	
	}
	/**
	 * tunnel虚拟网元操作
	 * @param action 方法名
	 * @param list 操作对象
	 * @return
	 * @throws Exception
	 */
	private String tunnelActionForList(String action, List<Tunnel> list) throws Exception {
		String result = null;
		int aSiteId ;
		int zSiteId ;
		List<Lsp> lspList ;
		TunnelService_MB tunnelService = null;
		try {
			for(Tunnel tunnel:list){
				lspList = tunnel.getLspParticularList();
				for(Lsp lsp:lspList){
					aSiteId = lsp.getASiteId();
					zSiteId = lsp.getZSiteId();
					if(0!=aSiteId&&0!=zSiteId){
						if(!(SiteTypeUtil(aSiteId)+"").equals(SiteTypeUtil(zSiteId)+"")){
							return ResourceUtil.srcStr(StringKeysTip.LBL_DIFSITE);	
						}else if(("0".equals(SiteTypeUtil(aSiteId)+"")&& ("0".equals(SiteTypeUtil(zSiteId)+"")))){
							return result;
						}
						
					}else{
						int siteId = 0;
						if(0!=aSiteId){
							siteId = aSiteId;
						}
						if(0!=zSiteId){
							siteId = zSiteId;
						}
						if("0".equals(SiteTypeUtil(siteId)+"")){
							return result;
						}
					}
//				if (tunnel.getTunnelStatus() == EActiveStatus.ACTIVITY.getValue()) {
//					return ResourceUtil.srcStr(StringKeysTip.LBL_IRTUALSITEACTIVITY);	
//				}
				}
			}
			tunnelService = (TunnelService_MB) ConstantUtil.serviceFactory.newService_MB(Services.Tunnel);
			if(TypeAndActionUtil.ACTION_DELETE.equals(action)){
				tunnelService.delete(list);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}
		} catch (Exception e) {
			throw e;
		}finally{
			UiUtil.closeService_MB(tunnelService);
			list = null;
			lspList = null;
		}
		return result;
	}
						
	/**
	 * QOS虚拟网元操作
	 * @param action 方法名
	 * @param list 操作对象
	 * @return
	 * @throws Exception
	 */
	private String qosQueueActionForList(String action,List<QosQueue> list) throws Exception {
		
		String result = null;
		QosQueue qosQueue = (QosQueue) list.get(0);
		QosQueueService_MB qosQueueService = null;
		
		try {
			if("0".equals(SiteTypeUtil(qosQueue.getSiteId())+"")){
				return result;
			}
			qosQueueService = (QosQueueService_MB) ConstantUtil.serviceFactory.newService_MB(Services.QosQueue);
			if(TypeAndActionUtil.ACTION_SAVEANDUPDATE.equals(action)){
				
				qosQueueService.saveOrUpdate(list);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}
		} catch (Exception e) {
			throw e;
		}finally{
			UiUtil.closeService_MB(qosQueueService);
		}
		return result;
	}
	/**
	 * LAG（批量）虚拟网元操作
	 * @param action 方法名
	 * @param list 操作对象
	 * @return
	 * @throws Exception
	 */
	private String portLagActionForList(String action,List<PortLagInfo> list) throws Exception {
		
		String result = null;
		PortLagService_MB portLagService = null;
		try {
			PortLagInfo portLagInfo1 = (PortLagInfo) list.get(0); 
			if("0".equals(SiteTypeUtil(portLagInfo1.getSiteId())+"")){
				return result;
			}
			portLagService = (PortLagService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORTLAG);
			//cx 、wuhan 通用
			portLagService.delete(list);
			result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
		} catch (Exception e) {
			throw e;
		}finally{
			UiUtil.closeService_MB(portLagService);
		}
		return result;
		
	}
	/**
	 * MCN虚拟网元操作
	 * @param action 方法名
	 * @param object 操作对象
	 * @return
	 * @throws Exception
	 */
	private String MCNAction(String action, Object object) throws Exception {
		String result = null;
		MCNService_MB mcnService = null;
		try {
			MCN mcn = (MCN) object;
			if("0".equals(SiteTypeUtil(Integer.parseInt(mcn.getNeId()))+"")){
				return result;
			}
			mcnService = (MCNService_MB) ConstantUtil.serviceFactory.newService_MB(Services.MCN);
			mcnService.update(mcn);
			result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
		} catch (Exception e) {
			throw e;
		}finally{
			UiUtil.closeService_MB(mcnService);
		}
		return result;
	}
	/**
	 * sdh时隙管理
	 * @param action 方法名
	 * @param object 操作对象
	 * @return
	 * @throws Exception
	 */
	private String portStmTimeslotAction(String action,Object object) throws Exception {
		
		String result = null;
		PortStmTimeslot portStmTimeslot = (PortStmTimeslot) object;
		PortStmTimeslotService_MB porStmTimeslotService  = null;
		
		try {
			
			if("0".equals(SiteTypeUtil(portStmTimeslot.getSiteId())+"")){
				return result;
			}
			porStmTimeslotService = (PortStmTimeslotService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORTSTMTIMESLOT);
			if(TypeAndActionUtil.ACTION_UPDATE.equals(action)){
				porStmTimeslotService.update(portStmTimeslot);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}
		} catch (Exception e) {
			throw e;
		}finally{
			UiUtil.closeService_MB(porStmTimeslotService);
		}
		return result;
	}
	/**
	 * sdh虚拟网元操作
	 * @param action 方法名
	 * @param object 操作对象
	 * @return
	 * @throws Exception
	 */
	private String portStmAction(String action, Object object) throws Exception {
		String result = null;
		PortStmService_MB portStmService = null;
		try {
			
			PortStm portStm = (PortStm) object;
			if("0".equals(SiteTypeUtil(portStm.getSiteid())+"")){
				return result;
			}
			portStmService = (PortStmService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORTSTM);
			if(TypeAndActionUtil.ACTION_UPDATE.equals(action)){
				portStmService.update(portStm);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}
		} catch (Exception e) {
			throw e;
		}finally{
			UiUtil.closeService_MB(portStmService);
		}
		return result;
	}

	/**
	 * LAG虚拟网元操作
	 * @param action 方法名
	 * @param object 操作对象
	 * @return
	 * @throws Exception
	 */
	private String portLagAction(String action, Object object) throws Exception {
		String result = null;
		PortLagInfo portLagInfo = (PortLagInfo) object;
		SiteService_MB	siteService = null;
		PortLagService_MB portLagService = null;
		try {
			if("0".equals(SiteTypeUtil(portLagInfo.getSiteId())+"")){
				return result;
			}
			siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE);
			portLagService = (PortLagService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORTLAG);
			if(siteService.getManufacturer(portLagInfo.getSiteId()) == EManufacturer.WUHAN.getValue()){
				if(TypeAndActionUtil.ACTION_INSERT.equals(action)){
					portLagService.saveOrUpdate(portLagInfo);
					result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
				}else if(TypeAndActionUtil.ACTION_UPDATE.equals(action)){
					portLagService.saveOrUpdate(portLagInfo);
					result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
				}
			}else{
				if(TypeAndActionUtil.ACTION_INSERT.equals(action)){
					portLagService.insertLag(portLagInfo);
					result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
				}else if(TypeAndActionUtil.ACTION_UPDATE.equals(action)){
					portLagService.updateLag(portLagInfo);
					result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
				}
			}
		} catch (Exception e) {
			throw e;
		}finally{
			UiUtil.closeService_MB(portLagService);
			UiUtil.closeService_MB(siteService);
		}
		return result;
	}
	/**
	 * ETH端口虚拟网元操作
	 * @param action 方法名
	 * @param object 操作对象
	 * @return
	 * @throws Exception
	 */
	private String portAction(String action, Object object) throws Exception {
		String result = null;
		PortInst portInst = (PortInst) object;
		SiteService_MB	siteService = null;
		PortService_MB portService = null;
		QosQueueService_MB qosQueueService = null;
		try {
			if("0".equals(SiteTypeUtil(portInst.getSiteId())+"")){
				return result;
			}
			siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE);
			portService = (PortService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORT);
			qosQueueService = (QosQueueService_MB) ConstantUtil.serviceFactory.newService_MB(Services.QosQueue);
			if(TypeAndActionUtil.ACTION_UPDATE.equals(action)){
				portService.saveOrUpdate(portInst);
				//武汉独有
				if (siteService.getManufacturer(portInst.getSiteId())== EManufacturer.WUHAN.getValue()) {
					if(portInst.getQosQueues() != null && portInst.getQosQueues().size() != 8){
						qosQueueService.saveOrUpdate(portInst.getQosQueues());
					}
				}
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}else if(TypeAndActionUtil.ACTION_SYNCHRO.equals(action)){
				portService.select(portInst);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}finally{
			UiUtil.closeService_MB(portService);
			UiUtil.closeService_MB(siteService);
			UiUtil.closeService_MB(qosQueueService);
		}
		return result;
	}

	/**
	 * 板卡虚拟网元操作
	 * @param action 方法名
	 * @param object 操作对象
	 * @return
	 * @throws Exception
	 */
	private String cardAction(String action, Object object) throws Exception {
		String result = null;
		CardInst cardInst = (CardInst) object;
		CardService_MB cardService = null;
		SlotService_MB slotService = null;
		SlotInst slotInst = null;
		try {
			if("0".equals(SiteTypeUtil(cardInst.getSiteId())+"")){
				return result;
			}
			cardService = (CardService_MB) ConstantUtil.serviceFactory.newService_MB(Services.CARD);
			slotService = (SlotService_MB)ConstantUtil.serviceFactory.newService_MB(Services.SLOT);
			if(TypeAndActionUtil.ACTION_SAVEANDUPDATE.equals(action)){
				cardService.saveOrUpdate(cardInst);
				slotInst = new SlotInst();
				slotInst.setId(cardInst.getSlotId());
				
				if(cardInst.getCardx() == 226 && cardInst.getCardy() == 56)
				{
					slotInst.setMasterCardAddress("258");
					slotService.updateMasterMacAddress(slotInst);
				}else if(cardInst.getCardx() == 576 && cardInst.getCardy() == 56)
				{
					slotInst.setMasterCardAddress("259");
					slotService.updateMasterMacAddress(slotInst);
				}
				
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}else if(TypeAndActionUtil.ACTION_DELETE.equals(action)){
				cardService.delete(cardInst);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}
		} catch (Exception e) {
			throw e;
		}finally{
			UiUtil.closeService_MB(cardService);
			UiUtil.closeService_MB(slotService);
		}
		return result;
	}

	/**
	 * ac虚拟网元操作
	 * @param action 方法名
	 * @param object 操作对象
	 * @return
	 * @throws Exception
	 */
	private String acAction(String action, Object object) throws Exception {
		String result = null;
		AcPortInfo acPortInfo = (AcPortInfo) object;
		AcPortInfoService_MB acInfoService = null;
		List<AcPortInfo> acPortInfos = null;
		try {
			if(object instanceof List){
				acPortInfos = (List<AcPortInfo>) object;
			}else{
				acPortInfo = (AcPortInfo) object;
			}
			if("0".equals(SiteTypeUtil(acPortInfo.getSiteId())+"")){
				return result;
			}
			acInfoService = (AcPortInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.AcInfo);
			if(TypeAndActionUtil.ACTION_INSERT.equals(action)){
				for(AcPortInfo info : acPortInfos){
					acInfoService.saveOrUpdate(info.getBufferList(), info);
				}
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}else if(TypeAndActionUtil.ACTION_UPDATE.equals(action)){
				acInfoService.saveOrUpdate(acPortInfo.getBufferList(), acPortInfo);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}else if(TypeAndActionUtil.ACTION_DELETE.equals(action)){
				List<Integer> acId = new ArrayList<Integer>();
				acId.add(acPortInfo.getId());
				acInfoService.delete(acId);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}else if(TypeAndActionUtil.ACTION_SYNCHRO.equals(action)){
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}
		} catch (Exception e) {
			throw e;
		}finally{
			UiUtil.closeService_MB(acInfoService);
		}
		return result;
	}
	/**
	 * ces虚拟网元操作
	 * @param action 方法名
	 * @param object 操作对象
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private String CESAction(String action, Object object) throws Exception {
		
		String result = null;
		List<CesInfo> list = null;
		CesInfo cesInfo = null;
		CesInfoService_MB service = null;
		
		try {
			
			if(object instanceof List){
				list = (List<CesInfo>) object;
			}else{
				cesInfo = (CesInfo) object;
				list = new ArrayList<CesInfo>();
				list.add(cesInfo);
			}
			
			if("0".equals(SiteTypeUtil(cesInfo.getaSiteId())+"")){
				return result;
			}
			service = (CesInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.CesInfo);
			if(TypeAndActionUtil.ACTION_INSERT.equals(action)){
				service.save( cesInfo);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}else if(TypeAndActionUtil.ACTION_DELETE.equals(action)){
				service.delete(list);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}
			else if(TypeAndActionUtil.ACTION_SYNCHRO.equals(action)){
				service.select(cesInfo);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}
			else if(TypeAndActionUtil.ACTION_UPDATE.equals(action)){
				service.update(cesInfo);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}
		} catch (Exception e) {
			throw e;
		}finally{
			UiUtil.closeService_MB(service);
			cesInfo = null;
			object = null;
		}
		return result;
	}
	/**
	 * ac虚拟网元操作
	 * @param action 方法名
	 * @param object 操作对象
	 * @return
	 * @throws Exception
	 */
	private String pmValueLimiteInfoAction(String action, Object object) throws Exception {
		String result = null;
		PmValueLimiteInfo pmValueLimiteInfo = (PmValueLimiteInfo) object;
		PmLimiteService_MB pmLimiteService = null;
		try {
			if("0".equals(SiteTypeUtil(pmValueLimiteInfo.getSiteId())+"")){
				return result;
			}
			 pmLimiteService = (PmLimiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PmLimiteService);
			if(TypeAndActionUtil.ACTION_INSERT.equals(action)){
				pmLimiteService.saveOrUpdate(pmValueLimiteInfo);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}else if(TypeAndActionUtil.ACTION_DELETE.equals(action)){
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}else if(TypeAndActionUtil.ACTION_UPDATE.equals(action)){
				pmLimiteService.saveOrUpdate(pmValueLimiteInfo);
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}else if(TypeAndActionUtil.ACTION_SYNCHRO.equals(action)){
				//acInfoService.select(acPortInfo.getSiteId());
				result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}
		} catch (Exception e) {
			throw e;
		}finally{
			UiUtil.closeService_MB(pmLimiteService);
			pmValueLimiteInfo = null;
			object = null;
		}
		return result;
	}
	
	public boolean isNeOnLine(int siteId) throws Exception
	{
		boolean flag = false;
		SiteService_MB service = null;
		try
		{
			service = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE);
			if(service.queryNeStatus(siteId) == 1)
			{
				flag=true;
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			UiUtil.closeService_MB(service);
		}
		
		return flag;
		
	}

	/**
	 * 根据网元ID获取网元厂商
	 * @param siteId 网元主键
	 * @return 厂商
	 * @throws Exception
	 */
	public int getManufacturer(int siteId) throws Exception{
		int manufacturer=0;
		SiteService_MB siteService= null;
		SiteInst siteInst=null;
		try {
			siteService=(SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE);
			siteInst=siteService.select(siteId);
			if(siteInst == null){
				throw new Exception("根据ID查询网元出错");
			}
			manufacturer=Integer.parseInt(UiUtil.getCodeById(Integer.parseInt(siteInst.getCellEditon())).getCodeValue());
		} catch (Exception e) {
			ExceptionManage.dispose(e,DispatchBase.class);
		}finally{
			UiUtil.closeService_MB(siteService);
		}
		return manufacturer;
	}
	
	
	
	/**
	 * 验证网元是否为在线脱管网元
	 * 
	 * @param SiteId  网元ID
	 * 
	 * @return  1为在线脱管网元
	 * @throws Exception 
	 */
	public int SiteTypeOnlineUtil(int siteId) throws Exception {
		int flag =0;
		SiteService_MB siteService = null;
		SiteInst siteInst = null;
		try {
		    if (0 != siteId)
		    {
				siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE);
				siteInst = (SiteInst) siteService.select(siteId);
				if(null!=siteInst && "0".equals(UiUtil.getCodeById(siteInst.getSiteType()).getCodeValue())&& 0==siteInst.getLoginstatus()){
					flag =1;
				}
			}else
			{
				flag =1;
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,SiteUtil.class);
		} finally {
			UiUtil.closeService_MB(siteService);
		}
		return flag;
	}
	
	/**
	 * bfd虚拟网元操作
	 * @param action	方法名
	 * @param list	操作对象
	 * @return
	 * @throws Exception
	 */
	private String bfdActionForList(String action, Object object) throws Exception {
		String result = null;
		int siteId = 0;
		BfdInfoService_MB bfdInfoService = null;
		List<BfdInfo> list = null;
		BfdInfo bfdInfo=null;
		try{
		if(object instanceof List){
			list = (List<BfdInfo>) object;
			bfdInfo = list.get(0);
		}else{
			bfdInfo = (BfdInfo) object;
			list = new ArrayList<BfdInfo>();
			list.add(bfdInfo);
		}
		
		if("0".equals(SiteTypeUtil(bfdInfo.getSiteId())+"")){
			return result;
		}
		bfdInfoService = (BfdInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.BFDMANAGEMENT);
		if(TypeAndActionUtil.ACTION_INSERT.equals(action)){
			bfdInfoService.insert(bfdInfo);
			result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
		}else if(TypeAndActionUtil.ACTION_DELETE.equals(action)){
			bfdInfoService.delete(list);
			result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
		}
		else if(TypeAndActionUtil.ACTION_UPDATE.equals(action)){
			bfdInfoService.update(bfdInfo);
			result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
		}
		} catch (Exception e) {
			throw e;
		}finally{
			UiUtil.closeService_MB(bfdInfoService);
			list = null;
		}
		return result;
	}
	
	/**
	 * bfd虚拟网元操作
	 * @param action	方法名
	 * @param list	操作对象
	 * @return
	 * @throws Exception
	 */
	private String secondMacActionForList(String action, Object object) throws Exception {
		String result = null;
		int siteId = 0;
		SecondMacStudyService_MB secondMacStudyService = null;
		List<SsMacStudy> list = null;
		SsMacStudy ssMacStudy=null;
		try{
		if(object instanceof List){
			list = (List<SsMacStudy>) object;
			ssMacStudy = list.get(0);
		}else{
			ssMacStudy = (SsMacStudy) object;
			list = new ArrayList<SsMacStudy>();
			list.add(ssMacStudy);
		}
		
		if("0".equals(SiteTypeUtil(ssMacStudy.getSiteId())+"")){
			return result;
		}
		secondMacStudyService = (SecondMacStudyService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SECONDMACSTUDY);
		if(TypeAndActionUtil.ACTION_INSERT.equals(action)){
			secondMacStudyService.save(ssMacStudy);
			result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
		}else if(TypeAndActionUtil.ACTION_DELETE.equals(action)){
			for(int i=0;i<list.size();i++){
			  secondMacStudyService.delete(list.get(i));
			}
			result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
		}
		else if(TypeAndActionUtil.ACTION_UPDATE.equals(action)){
			secondMacStudyService.update(ssMacStudy);
			result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
		}
		} catch (Exception e) {
			throw e;
		}finally{
			UiUtil.closeService_MB(secondMacStudyService);
			list = null;
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private String arpActionForList(String action, Object object) throws Exception {
		String result = null;
		ARPInfoService_MB arpInfoService = null;
		List<ARPInfo> list = null;
		ARPInfo arpInfo = null;
		try{
		if(object instanceof List){
			list = (List<ARPInfo>) object;
			arpInfo = list.get(0);
		}else{
			arpInfo = (ARPInfo) object;
			list = new ArrayList<ARPInfo>();
			list.add(arpInfo);
		}
		
		if("0".equals(SiteTypeUtil(arpInfo.getSiteId())+"")){
			return result;
		}
		arpInfoService = (ARPInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.ARP);
		if(TypeAndActionUtil.ACTION_INSERT.equals(action)){
			arpInfoService.insert(arpInfo);
			result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
		}else if(TypeAndActionUtil.ACTION_DELETE.equals(action)){
			arpInfoService.delete(list);
			result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
		}
		else if(TypeAndActionUtil.ACTION_UPDATE.equals(action)){
			arpInfoService.update(arpInfo);
			result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
		}
		} catch (Exception e) {
			throw e;
		}finally{
			UiUtil.closeService_MB(arpInfoService);
		}
		return result;
	}
	
}	