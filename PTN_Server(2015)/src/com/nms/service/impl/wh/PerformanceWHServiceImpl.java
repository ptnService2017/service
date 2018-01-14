package com.nms.service.impl.wh;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nms.db.bean.equipment.port.PortInst;
import com.nms.db.bean.equipment.shelf.SiteInst;
import com.nms.db.bean.equipment.slot.SlotInst;
import com.nms.db.bean.path.Segment;
import com.nms.db.bean.perform.CurrentPerforInfo;
import com.nms.db.bean.perform.HisPerformanceInfo;
import com.nms.db.bean.perform.PerformanceInfo;
import com.nms.db.bean.perform.PerformanceTaskInfo;
import com.nms.db.bean.ptn.Businessid;
import com.nms.db.bean.ptn.path.pw.MsPwInfo;
import com.nms.db.bean.ptn.path.pw.PwInfo;
import com.nms.db.bean.ptn.path.tunnel.Lsp;
import com.nms.db.enums.EMonitorCycle;
import com.nms.db.enums.EObjectType;
import com.nms.drive.service.bean.NEObject;
import com.nms.drive.service.bean.PerformanceDataBlockObject;
import com.nms.drive.service.bean.PerformanceInfoObject;
import com.nms.drive.service.bean.PerformanceLineObject;
import com.nms.drive.service.bean.PerformanceMasterCardObject;
import com.nms.drive.service.bean.PerformanceObject;
import com.nms.drive.service.impl.CoderUtils;
import com.nms.model.equipment.port.PortService_MB;
import com.nms.model.equipment.shlef.SiteService_MB;
import com.nms.model.equipment.slot.SlotService_MB;
import com.nms.model.path.SegmentService_MB;
import com.nms.model.ptn.BusinessidService_MB;
import com.nms.model.ptn.path.pw.MsPwInfoService_MB;
import com.nms.model.ptn.path.pw.PwInfoService_MB;
import com.nms.model.ptn.path.tunnel.LspInfoService_MB;
import com.nms.model.ptn.path.tunnel.TunnelService_MB;
import com.nms.model.util.Services;
import com.nms.service.bean.ActionObject;
import com.nms.service.bean.OperationObject;
import com.nms.service.impl.base.WHOperationBase;
import com.nms.service.impl.util.SiteUtil;
import com.nms.service.impl.util.WhImplUtil;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.DateUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.ResourceUtil;
import com.nms.ui.manager.UiUtil;
import com.nms.ui.manager.keys.StringKeysObj;
import com.nms.ui.manager.keys.StringKeysTip;

public class PerformanceWHServiceImpl extends WHOperationBase {
	private Map<Integer, String> lspNameMap = new HashMap<Integer, String>();
	private Map<Integer, String> portNameMap = new HashMap<Integer, String>();
	private Map<Integer, String> segmentNameMap = new HashMap<Integer, String>();
	private Map<Integer, String> tunnelNameMap = new HashMap<Integer, String>();
	private Map<Integer, String> serviceIdPwNameMap = new HashMap<Integer, String>();
	private Map<Integer, String> pwIdPwNameNameMap = new HashMap<Integer, String>();

	/**
	 * 根据网元id列表，查询网元下所有槽位的当前性能信息，并在当前性能界面中呈现
	 * 
	 * @param siteIdList
	 *            网元数据库id列表
	 * @return List<CurrentPerformanceInfo> 根据网元查询当前性能信息
	 */
	public List<CurrentPerforInfo> queryCurrPerforBySites(Integer siteId,int performanceCount,int performanceBeginCount,long performanceBeginDataTime,int performanceType) throws Exception {
		OperationObject operationObject = null;
		OperationObject operationObjectResult = null;
		List<CurrentPerforInfo> perforInfos = null;
		try {
			SiteUtil siteUtil=new SiteUtil();
			perforInfos = new ArrayList<CurrentPerforInfo>();
			if("0".equals(siteUtil.SiteTypeUtil(siteId)+"")){//非失连网元、非虚拟网元下发设备
				ConstantUtil.isCurrentPerformnace = true;
				operationObject = this.getOperationObject(siteId, "queryPerforBySite",performanceCount,performanceBeginCount,performanceBeginDataTime,performanceType);
				super.sendAction(operationObject);
				operationObjectResult = super.verification(operationObject);
				if (operationObjectResult.isSuccess()) {
					// 将设备查询信息封装到CurrentPerforInfo中
					perforInfos = this.insertCurrentPerforInfo(operationObjectResult);
				} 
			}
		} catch (Exception e) {
			throw e;
		}
		return perforInfos;
	}
	/**
	 * 根据网元id列表，查询网元下所有槽位的当前性能信息，并在当前性能界面中呈现
	 * @param siteIdList
	 *  网元数据库id列表
	 * @return List<CurrentPerformanceInfo> 根据网元查询当前性能信息
	 */
	public List<CurrentPerforInfo> queryCurrPerforByCard(Integer siteId, String cardAddress,int performanceCount,int performanceBeginCount,long performanceBeginDataTime,int performanceType) throws Exception {
		OperationObject operationObject = null;
		OperationObject operationObjectResult = null;
		List<CurrentPerforInfo> perforInfos = null;
		try {
			SiteUtil siteUtil=new SiteUtil();
			if("0".equals(siteUtil.SiteTypeUtil(siteId)+"")){//非失连网元、非虚拟网元下发设备
				operationObject = this.getOperationObject(siteId, "queryPerforBySite",performanceCount,performanceBeginCount,performanceBeginDataTime,performanceType);
				super.sendAction(operationObject);
				operationObjectResult = super.verification(operationObject);
				if (operationObjectResult.isSuccess()) {
					// 将设备查询信息封装到CurrentPerforInfo中
					perforInfos = insertCurrentPerforInfo(operationObjectResult);
				} 
			}else{
				perforInfos = new ArrayList<CurrentPerforInfo>();
			}
			
		} catch (Exception e) {
			throw e;
		}
		return perforInfos;

	}

	/**
	 * 将查询的设备性能信息，转化成List<CurrentPerforInfo>当前性能对象(当前性能界面呈现)
	 * 
	 * @param operationObject
	 *            查询设备的操作对象
	 * @return
	 */
	public List<CurrentPerforInfo> insertCurrentPerforInfo(OperationObject operationObject) throws Exception {
		List<CurrentPerforInfo> currentPerforInfoList = null;
		SiteService_MB siteService = null;
		SlotService_MB slotService = null;
		SiteInst siteInst = null;
		SlotInst slotInst = null;
		SimpleDateFormat sdf = null;
		CurrentPerforInfo info = null;
		List<CurrentPerforInfo> infos = null;
		String time = null;
		try {
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			time = sdf.format(System.currentTimeMillis());
			siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE);
			slotService = (SlotService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SLOT);
			siteInst = new SiteInst();
			currentPerforInfoList = new ArrayList<CurrentPerforInfo>();
			for (ActionObject obj : operationObject.getActionObjectList()) {
				try {
					siteInst = new SiteInst();
					if (obj.getPerformanceObject() != null) {
						PerformanceObject performanceObject = obj.getPerformanceObject();
						// 封装网元信息
						siteInst.setSite_Hum_Id(String.valueOf(performanceObject.getNeAddress() % 256));
						siteInst.setFieldID(performanceObject.getNeAddress() / 256);
						siteInst = siteService.selectByNeAddress(siteInst);
						//获取对象名称
						this.getObjectNameMap(siteInst);
						for (PerformanceMasterCardObject performanceMasterCardObject : performanceObject.getPerformanceMasterCardObjectList()) {
							slotInst = new SlotInst();
							slotInst.setSiteId(siteInst.getSite_Inst_Id());
							slotInst.setMasterCardAddress(String.valueOf(performanceMasterCardObject.getMasterCardAddress()));
							slotInst = slotService.select(slotInst).get(0);
							for (PerformanceDataBlockObject lineObject : performanceMasterCardObject.getPerformanceDataBlockObjectList()) {
								for (PerformanceLineObject infoObject : lineObject.getPerformanceLineObjectList()) {
									for (PerformanceInfoObject performanceInfoObject : infoObject.getPerformanceInfoObjectList()) {
										info = new CurrentPerforInfo();
										info.setMonitorCycle((performanceObject.getPerformanceDataType() == 0) ? 1 : 2);
										info.setSiteId(siteInst.getSite_Inst_Id());
										info.setSiteName(siteInst.getCellId());
										info.setSlotId(slotInst.getId());
										setIdAndType(infoObject, info);// 确定性能类型
										info.setPerformanceCode(performanceInfoObject.getPerformanceCode());// 性能类型
										info.setPerformanceValue(performanceInfoObject.getPerformanceValue());
										info.setMasterCardAddress(performanceMasterCardObject.getMasterCardAddress());
										info.setStartTime(lineObject.getDateTime());
										info.setPerformanceEndTime(lineObject.getEndTime());
										info.setPerformanceTime(time);
										if(info.getObjectId() != 0){
											currentPerforInfoList.add(info);
										}
									}
								}
							}
						}
						//每个网元添加一个cpu利用率的性能
						addCpuRatio(siteInst, currentPerforInfoList, 1);
					}
					
				} catch (Exception e) {
					ExceptionManage.dispose(e, getClass());
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			UiUtil.closeService_MB(slotService);
			UiUtil.closeService_MB(siteService);
		}
		infos = expandCurrentPerforInfo(currentPerforInfoList);
		addLTPerformance(infos);
		return infos;
	}

	/**
	 * 获取对象名称map
	 */
	private void getObjectNameMap(SiteInst siteInst) {
		this.lspNameMap.clear();
		this.getLspNameMap(siteInst);
		this.portNameMap.clear();
		this.getPortNameMap(siteInst);
		this.segmentNameMap.clear();
		this.getSegmentNameMap(siteInst);
		this.tunnelNameMap.clear();
		this.getTunnelNameMap(siteInst);
		this.serviceIdPwNameMap.clear();
		this.pwIdPwNameNameMap.clear();
		this.getPwNameMap(siteInst);
	}
	
	private void getLspNameMap(SiteInst siteInst) {
		TunnelService_MB tunnelService = null;
		LspInfoService_MB lspService = null;
		try {
			tunnelService = (TunnelService_MB) ConstantUtil.serviceFactory.newService_MB(Services.Tunnel);
			Map<Integer, String> tId_NameMap = tunnelService.selectAllTunnelName();
			lspService = (LspInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.LSPINFO);
			List<Lsp> lspList = lspService.selectBusinessId(siteInst.getSite_Inst_Id());
			if(lspList.size() > 0){
				for (Lsp lsp : lspList) {
					this.lspNameMap.put(lsp.getAtunnelbusinessid(), tId_NameMap.get(lsp.getTunnelId()));
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			UiUtil.closeService_MB(lspService);
			UiUtil.closeService_MB(tunnelService);
		}
	}
	
	private void getPortNameMap(SiteInst siteInst){
		PortService_MB portService = null;
		try {
			portService = (PortService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORT);
			PortInst port = new PortInst();
			port.setSiteId(siteInst.getSite_Inst_Id());
			List<PortInst> portList = portService.select(port);
			if(portList != null){
				for (PortInst portInst : portList) {
					if(!portInst.getPortType().equals("system")){
						String name = portInst.getPortName();
						if(name.contains("e1")){
							this.portNameMap.put(portInst.getNumber(), name.substring(0,name.lastIndexOf(".")));
						}else{
							this.portNameMap.put(portInst.getNumber(), name);
						}
					}
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			UiUtil.closeService_MB(portService);
		}
	}
	
	private void getSegmentNameMap(SiteInst siteInst){
		PortService_MB portService = null;
		SegmentService_MB segmentService = null;
		try {
			portService = (PortService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORT);
			segmentService = (SegmentService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SEGMENT);
			PortInst port = new PortInst();
			port.setSiteId(siteInst.getSite_Inst_Id());
			List<PortInst> portList = portService.select(port);
			Segment segment = null;
			if(portList != null){
				for (PortInst portInst : portList) {
					if(portInst.getPortType().contains("NNI")){
						segment = new Segment();
						segment.setASITEID(siteInst.getSite_Inst_Id());
						segment.setAPORTID(portInst.getPortId());
						List<Segment> segmentList = segmentService.selectBySiteIdAndPort(segment);
						if(segmentList != null && !segmentList.isEmpty()){
							Segment s = segmentList.get(0);
							this.segmentNameMap.put(portInst.getNumber(), s.getId()+"/"+s.getNAME());
						}
					}
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			UiUtil.closeService_MB(portService);
			UiUtil.closeService_MB(segmentService);
		}
	}
	
	private void getTunnelNameMap(SiteInst siteInst){
		TunnelService_MB tunnelService = null;
		try {
			tunnelService = (TunnelService_MB) ConstantUtil.serviceFactory.newService_MB(Services.Tunnel);
			this.tunnelNameMap = tunnelService.selectTunnelNameBySiteId(siteInst.getSite_Inst_Id());
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			UiUtil.closeService_MB(tunnelService);
		}
	}
	
	private void getPwNameMap(SiteInst siteInst){
		BusinessidService_MB busiService = null;
		PwInfoService_MB pwInfoService = null;
		try {
			busiService = (BusinessidService_MB) ConstantUtil.serviceFactory.newService_MB(Services.BUSINESSID);
			pwInfoService = (PwInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PwInfo);
			Businessid bId = new Businessid();
			bId.setIdStatus(1);
			bId.setSiteId(siteInst.getSite_Inst_Id());
			bId.setType("ethpw");
			List<Businessid> bIdList = busiService.queryByCondition(bId);
			for (Businessid businessid : bIdList) {
				List<PwInfo> pwList = pwInfoService.selectBysiteIdAndServiceId(siteInst.getSite_Inst_Id(),businessid.getIdValue());
				if(pwList != null && pwList.size() > 0){
					this.serviceIdPwNameMap.put(businessid.getIdValue(), pwList.get(0).getPwName());
				}
			}
			List<PwInfo> pwInfoList = pwInfoService.select();
			if(pwInfoList != null && pwInfoList.size() > 0){
				for (PwInfo pwInfo : pwInfoList) {
					this.pwIdPwNameNameMap.put(pwInfo.getPwId(), pwInfo.getPwName());
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			UiUtil.closeService_MB(busiService);
			UiUtil.closeService_MB(pwInfoService);
		}
	}
	
	/**
	 * 根据网元id列表，查询网元下所有槽位的历史性能，并保存到数据库中
	 * 
	 * @param siteIdList
	 *            网元数据库id列表
	 * @return                                         
	 */
	
	
	/**
	 * int performanceCount,int performanceBeginCount,long performanceBeginDataTime,int performanceType
	 */
	public List<HisPerformanceInfo> queryHisPerforBySite(Integer siteId,PerformanceTaskInfo taskInfo) throws Exception {
		OperationObject operationObject = null;
		OperationObject operationObjectResult = null;
		List<HisPerformanceInfo> hisPerforInfo = null;
		try {
			SiteUtil siteUtil=new SiteUtil();
			if("0".equals(siteUtil.SiteTypeUtil(siteId)+"")){//非失连网元、非虚拟网元下发设备
				// 获取槽位的历史性能 通过siteid
				ConstantUtil.isCurrentPerformnace = true;
				operationObject = this.getOperationObject(siteId, "queryPerforBySite",taskInfo.getPerformanceCount(),taskInfo.getPerformanceBeginCount(),taskInfo.getPerformanceBeginDataTime(),taskInfo.getPerformanceType());
				super.sendAction(operationObject);// 下发配置来获取设备的性能
				operationObjectResult = super.verification(operationObject);// 验证是否成功
				// 如果成功---将查询的设备性能信息，转化成List<HisPerformanceInfo>历史性能对象
				if (operationObjectResult.isSuccess()) {
					hisPerforInfo = insertHisPerformanceInfo(operationObjectResult, "");
				}
			}else{
				hisPerforInfo = new ArrayList<HisPerformanceInfo>();

			}
		} catch (Exception e) {
			throw e;
		}
		return hisPerforInfo;
	}
	
	
	public void queryHisPerforTask(Integer siteId,PerformanceTaskInfo taskInfo) throws Exception {
		OperationObject operationObject = null;
		SiteUtil siteUtil = null;
		try {
			siteUtil = new SiteUtil();
			if("0".equals(siteUtil.SiteTypeUtil(siteId)+"")){//非失连网元、非虚拟网元下发设备
				// 获取槽位的历史性能 通过siteid
				ExceptionManage.infor("当前性能任务执行的时间"+DateUtil.getDate(DateUtil.FULLTIME)+"--网元id"+taskInfo.getSiteInst().getSite_Hum_Id()+"周期"+taskInfo.getPerformanceType(), PerformanceWHServiceImpl.class);
				ConstantUtil.isCurrentPerformnace = false;
				if(taskInfo.getPerformanceType() == 0){
					operationObject = this.getOperationObject(siteId, "queryPerforBySite",1,1,taskInfo.getPerformanceBeginDataTime(),taskInfo.getPerformanceType());
				}else{
					operationObject = this.getOperationObject(siteId, "queryPerforBySite",taskInfo.getPerformanceCount(),taskInfo.getPerformanceBeginCount(),taskInfo.getPerformanceBeginDataTime(),taskInfo.getPerformanceType());
				}
				
				super.sendAction(operationObject);// 下发配置来获取设备的性能
			}
		} catch (Exception e) {
			throw e;
		}finally
		{
			operationObject = null;
			siteUtil = null;
		}
	}

	/**
	 * 根据网元id列表，查询网元下所有槽位的历史性能，并保存到数据库中
	 * 
	 * @param siteIdList
	 *            网元数据库id列表
	 * @return
	 */
	public List<HisPerformanceInfo> queryHisPerforByCard(Integer siteIdList, String cardAddress,int performanceCount,int performanceBeginCount,long performanceBeginDataTime,int performanceType) throws Exception {
		OperationObject operationObject = null;
		OperationObject operationObjectResult = null;
		List<HisPerformanceInfo> hisPerforInfo = null;
		try {
			SiteUtil siteUtil=new SiteUtil();
			if("0".equals(siteUtil.SiteTypeUtil(siteIdList)+"")){//非失连网元、非虚拟网元下发设备
				// 获取槽位的历史性能 通过siteid
				operationObject = this.getOperationObject(siteIdList, "queryPerforBySite",performanceCount,performanceBeginCount,performanceBeginDataTime,performanceType);
				super.sendAction(operationObject);// 下发配置来获取设备的性能
				operationObjectResult = super.verification(operationObject);// 验证是否成功
				// 如果成功---将查询的设备性能信息，转化成List<HisPerformanceInfo>历史性能对象
				if (operationObjectResult.isSuccess()) {
					hisPerforInfo = insertHisPerformanceInfo(operationObjectResult, "");
				}
			}else{
				hisPerforInfo = new ArrayList<HisPerformanceInfo>();
			}
		} catch (Exception e) {
			throw e;
		}
		return hisPerforInfo;
	}

	/**
	 * 将查询的设备性能信息，转化成List<HisPerformanceInfo>历史告警对象
	 * 
	 * @param operationObject
	 *            查询设备的操作对象
	 * @return
	 */
	public List<HisPerformanceInfo> insertHisPerformanceInfo(OperationObject operationObject, String cardOrSite) throws Exception {
		List<HisPerformanceInfo> currentPerforInfoList = null;
		SiteService_MB siteService = null;
		SlotService_MB slotService = null;
		SiteInst siteInst = null;
		SlotInst slotInst = null;
		SimpleDateFormat sdf = null;
		HisPerformanceInfo info = null;
		List<HisPerformanceInfo> infos = null;
		try {
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE);
			slotService = (SlotService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SLOT);
			siteInst = new SiteInst();
			currentPerforInfoList = new ArrayList<HisPerformanceInfo>();
			for (ActionObject obj : operationObject.getActionObjectList()) {
				try {
					siteInst = new SiteInst();
					if (obj.getPerformanceObject() != null) {
						PerformanceObject performanceObject = obj.getPerformanceObject();
						// 封装网元信息
						siteInst.setSite_Hum_Id(String.valueOf(performanceObject.getNeAddress() % 256));
						siteInst.setFieldID(performanceObject.getNeAddress() / 256);
						siteInst = siteService.selectByNeAddress(siteInst);
						//获取对象名称
						this.getObjectNameMap(siteInst);
						for (PerformanceMasterCardObject performanceMasterCardObject : performanceObject.getPerformanceMasterCardObjectList()) {
							slotInst = new SlotInst();
							slotInst.setSiteId(siteInst.getSite_Inst_Id());
							slotInst.setMasterCardAddress(String.valueOf(performanceMasterCardObject.getMasterCardAddress()));
							slotInst = slotService.select(slotInst).get(0);
							if (slotInst.getMasterCardAddress().equals(performanceMasterCardObject.getMasterCardAddress() + "")) {
								for (PerformanceDataBlockObject lineObject : performanceMasterCardObject.getPerformanceDataBlockObjectList()) {
									for (PerformanceLineObject infoObject : lineObject.getPerformanceLineObjectList()) {
										for (PerformanceInfoObject performanceInfoObject : infoObject.getPerformanceInfoObjectList()) {
											info = new HisPerformanceInfo();
											info.setMonitorCycle(EMonitorCycle.forms((performanceObject.getPerformanceDataType() == 0) ? 1 : 2));
											info.setMonitor(performanceObject.getPerformanceDataType() == 0 ? 1 : 2);
											info.setSiteId(siteInst.getSite_Inst_Id());
											info.setSiteName(siteInst.getCellId());
											info.setSlotId(slotInst.getId());
											setIdAndType(infoObject, info);// 确定性能类型
											info.setPerformanceCode(performanceInfoObject.getPerformanceCode());// 性能类型
											info.setPerformanceValue(performanceInfoObject.getPerformanceValue());// 性能值
											info.setPerformanceTime(sdf.format(new Date()));//结束时间
											info.setStartTime(DateUtil.updateTime(lineObject.getDateTime(),DateUtil.FULLTIME));
											info.setPerformanceEndTime(DateUtil.updateTime(lineObject.getEndTime(),DateUtil.FULLTIME));
											// info.setPerformanceName()
											// 用于标记查询的历史性能是网元或板卡的
											// 1：网元的历史系能；2:板卡的历史性能
											if (cardOrSite.trim().equals("")) {
												info.setIsCardOrSite(1);
											} else {
												info.setIsCardOrSite(2);
											}
											if(info.getObjectId() != 0){
												currentPerforInfoList.add(info);
											}
										}
									}
								}
							}
						}
						//每个网元添加一个cpu利用率的性能
						addCpuRatio(siteInst, currentPerforInfoList, 2);
					}
				} catch (Exception e) {
					ExceptionManage.dispose(e, getClass());
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,PerformanceWHServiceImpl.class);
			throw e;
		} finally {
			UiUtil.closeService_MB(slotService);
			UiUtil.closeService_MB(siteService);
		}
		infos = expandHisPerformanceInfo(currentPerforInfoList);
		addLTHisPerformance(infos);
		return infos;
	}
	
	/**
	 * 添加tunnel和pw的LT性能
	 */
	private static void addLTHisPerformance(List<HisPerformanceInfo> perforInfoList){
		if(perforInfoList.size() > 0){
			List<HisPerformanceInfo> performanceList = new ArrayList<HisPerformanceInfo>();
			for(HisPerformanceInfo info : perforInfoList){
				int code = info.getPerformanceCode();
				if(code == 136 || code == 137 || code == 138 || code == 139 ||
						code == 140 || code == 141 || code == 142 || code == 143){
					HisPerformanceInfo currInfo = new HisPerformanceInfo();
					currInfo.setSlotId(info.getSlotId());
					currInfo.setStartTime(info.getStartTime());
					currInfo.setPerformanceEndTime(info.getPerformanceEndTime());
					currInfo.setPerformanceCode(code+24);
					currInfo.setPerformanceValue(info.getPerformanceValue());
					currInfo.setPerformanceTime(info.getPerformanceTime());
					currInfo.setMonitorCycle(info.getMonitorCycle());
					currInfo.setMonitor(info.getMonitor());
					currInfo.setSiteId(info.getSiteId());
					currInfo.setSiteName(info.getSiteName());
					currInfo.setObjectType(info.getObjectType());
					currInfo.setObjectName(info.getObjectName());
					currInfo.setIsCardOrSite(info.getIsCardOrSite());
					currInfo.setObjectId(info.getObjectId());
					performanceList.add(currInfo);
				}
			}
			if(performanceList.size() > 0){
				perforInfoList.addAll(performanceList);
			}
		}
	}
	
	/**
	 * 添加tunnel和pw的LT性能
	 */
	private static void addLTPerformance(List<CurrentPerforInfo> perforInfoList){
		if(perforInfoList.size() > 0){
			List<CurrentPerforInfo> performanceList = new ArrayList<CurrentPerforInfo>();
			for(CurrentPerforInfo info : perforInfoList){
				int code = info.getPerformanceCode();
				if(code == 136 || code == 137 || code == 138 || code == 139 ||
						code == 140 || code == 141 || code == 142 || code == 143){
					CurrentPerforInfo currInfo = new CurrentPerforInfo();
					currInfo.setSlotId(info.getSlotId());
					currInfo.setStartTime(info.getStartTime());
					currInfo.setPerformanceEndTime(info.getPerformanceEndTime());
					currInfo.setPerformanceCode(code+24);
					currInfo.setPerformanceValue(info.getPerformanceValue());
					currInfo.setPerformanceTime(info.getPerformanceTime());
					currInfo.setMonitorCycle(info.getMonitorCycle());
					currInfo.setSiteId(info.getSiteId());
					currInfo.setSiteName(info.getSiteName());
					currInfo.setObjectType(info.getObjectType());
					currInfo.setObjectName(info.getObjectName());
					currInfo.setIsCardOrSite(info.getIsCardOrSite());
					currInfo.setObjectId(info.getObjectId());
					performanceList.add(currInfo);
				}
			}
			if(performanceList.size() > 0){
				perforInfoList.addAll(performanceList);
			}
		}
	}

	/**
	 * 添加网元cpu利用率性能
	 * @param slotId 
	 */
	@SuppressWarnings("unchecked")
	private static void addCpuRatio(SiteInst siteInst, List perforInfoList, int type) {
		if(perforInfoList.size() > 0){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(type == 1){
				//当前性能
				CurrentPerforInfo info = new CurrentPerforInfo();
				CurrentPerforInfo currPerform = (CurrentPerforInfo) perforInfoList.get(0);
				info.setSlotId(currPerform.getSlotId());
				info.setStartTime(currPerform.getStartTime());
				info.setPerformanceEndTime(currPerform.getPerformanceEndTime());
				info.setPerformanceCode(116);// 性能类型
				info.setPerformanceValue(getCpuRatioValue(siteInst));// 性能值
				info.setPerformanceTime(sdf.format(new Date()));//结束时间
				info.setMonitorCycle(1);//只有15min
				info.setSiteId(siteInst.getSite_Inst_Id());
				info.setSiteName(siteInst.getCellId());
				info.setObjectType(EObjectType.CPURATIO);
				info.setObjectName("cpuRatio");
				// 用于标记查询的历史性能是网元或板卡的
				// 1：网元的历史系能；2:板卡的历史性能
				info.setIsCardOrSite(1);
				perforInfoList.add(info);
			}else{
				//历时性能
				HisPerformanceInfo info = new HisPerformanceInfo();
				HisPerformanceInfo hisPerform = (HisPerformanceInfo) perforInfoList.get(0);
				info.setSlotId(hisPerform.getSlotId());
				info.setStartTime(hisPerform.getStartTime());
				info.setPerformanceEndTime(hisPerform.getPerformanceEndTime());
				info.setPerformanceCode(116);// 性能类型
				info.setPerformanceValue(getCpuRatioValue(siteInst));// 性能值
				info.setPerformanceTime(sdf.format(new Date()));//结束时间
				info.setMonitorCycle(EMonitorCycle.forms(1));//只有15min
				info.setSiteId(siteInst.getSite_Inst_Id());
				info.setSiteName(siteInst.getCellId());
				info.setObjectType(EObjectType.CPURATIO);
				info.setObjectName("cpuRatio");
				// 用于标记查询的历史性能是网元或板卡的
				// 1：网元的历史系能；2:板卡的历史性能
				info.setIsCardOrSite(1);
				perforInfoList.add(info);
			}
		}
	}

	/**
	 * 根据businessId表中用去的id数进行cpu利用率统计
	 */
	private static int getCpuRatioValue(SiteInst siteInst) {
		BusinessidService_MB businessService = null;
		try {
			businessService = (BusinessidService_MB) ConstantUtil.serviceFactory.newService_MB(Services.BUSINESSID);
			int usedCount = businessService.select(siteInst.getSite_Inst_Id());
			if(usedCount == 0){
				return 2;
			}
			if(siteInst.getCellType().contains("703")){
				//703系列   2952个业务id
				return 2+usedCount/60;
			}else{
				//710系列    3512个业务id
				return 2+usedCount/70;
			}
			
		} catch (Exception e) {
			ExceptionManage.dispose(e,PerformanceWHServiceImpl.class);
		} finally {
			UiUtil.closeService_MB(businessService);
		}
		return 2;
	}
	
	/**
	 * 根据网元id列表，封装性能的OperationObject对象
	 * 
	 * 
	 * @param siteIdList网元id列表
	 * @return
	 * @throws Exception
	 */
	private OperationObject getOperationObject(Integer siteId, String type,int performanceCount,int performanceBeginCount) throws Exception {
		OperationObject operationObject = null;
		ActionObject actionObject = null;
		NEObject neObject = null;
		try {
			operationObject = new OperationObject();
			WhImplUtil whImplUtil = new WhImplUtil();
			neObject =  whImplUtil.siteIdToNeObject(siteId);// 根据ID到site-inst表中取域ID+网元ID付给neObject对象
			actionObject = new ActionObject();
			actionObject.setActionId(super.getActionId(operationObject.getActionObjectList()));// 随即取数据
			actionObject.setNeObject(neObject);
			actionObject.getPerformanceObject().setNeAddress(neObject.getNeAddress());// 将neObJECT的neaddress对象复制给actionObject
			actionObject.getPerformanceObject().setPerformanceMasterCardObjectList(this.getMasterCardObjectList(siteId));
			actionObject.setPerformanceCount(performanceCount);
			actionObject.setPerformanceBeginCount(performanceBeginCount);
			actionObject.setType(type);
			operationObject.getActionObjectList().add(actionObject);

		} catch (Exception e) {
			throw e;
		}
		return operationObject;
	}

	/**
	 * 根据网元id列表，封装性能的OperationObject对象
	 * 
	 * @param siteIdList网元id列表
	 * @return
	 * @throws Exception
	 */
	private OperationObject getOperationObject(Integer siteId, String type,int performanceCount,int performanceBeginCount,long performanceBeginDataTime,int performanceType) throws Exception {
		OperationObject operationObject = null;
		ActionObject actionObject = null;
		NEObject neObject = null;
		try {
			operationObject = new OperationObject();
			WhImplUtil whImplUtil = new WhImplUtil();
			neObject = whImplUtil.siteIdToNeObject(siteId);// 根据ID到site-inst表中取域ID+网元ID付给neObject对象
			actionObject = new ActionObject();
			actionObject.setActionId(super.getActionId(operationObject.getActionObjectList()));// 随即取数据
			actionObject.setNeObject(neObject);
			actionObject.getPerformanceObject().setNeAddress(neObject.getNeAddress());// 将neObJECT的neaddress对象复制给actionObject
			actionObject.getPerformanceObject().setPerformanceMasterCardObjectList(this.getMasterCardObjectList(siteId));
			actionObject.setPerformanceCount(performanceCount);
			actionObject.setPerformanceBeginCount(performanceBeginCount);
			actionObject.setType(type);
			actionObject.setPerformanceBeginDataTime(performanceBeginDataTime);
			actionObject.setPerformanceType(performanceType);
			operationObject.getActionObjectList().add(actionObject);

		} catch (Exception e) {
			throw e;
		}
		return operationObject;
	}
	/**
	 * 根据网元id，封装槽位信息PerformanceMasterCardObject
	 * 
	 * @param siteId
	 * @return
	 * @throws Exception
	 */
	private List<PerformanceMasterCardObject> getMasterCardObjectList(Integer siteId) throws Exception {
		List<PerformanceMasterCardObject> masterCardObjectList = null;
		SlotService_MB slotService = null;
		SlotInst slotInst = null;
		List<SlotInst> slotInstList = null;
		PerformanceMasterCardObject masterCardObject = null;
		try {
			masterCardObjectList = new ArrayList<PerformanceMasterCardObject>();
			slotService = (SlotService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SLOT);
			slotInst = new SlotInst();
			slotInst.setSiteId(siteId);
			slotInstList = slotService.select(slotInst);
			for (SlotInst slot : slotInstList) {
				masterCardObject = new PerformanceMasterCardObject();
				masterCardObject.setMasterCardAddress(Integer.parseInt(slot.getMasterCardAddress()));
				masterCardObjectList.add(masterCardObject);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			UiUtil.closeService_MB(slotService);
			slotInst = null;
			slotInstList = null;
			masterCardObject = null;
		}
		return masterCardObjectList;
	}

	/**
	 * 解析设备线路，产生业务id，业务类型、业务名称等信息
	 * 
	 * @param lineObject
	 * @param info
	 */
	public void setIdAndType(PerformanceLineObject performanceLineObject, PerformanceInfo info) {
		String lineCode = Integer.toHexString(performanceLineObject.getLine());
		while (lineCode.length() < 4) {
			lineCode = 0 + lineCode;
		}
		int codeType = Integer.parseInt(lineCode.substring(0, 2));
		MsPwInfoService_MB msPwInfoService = null;
		try {
			if (codeType == 0) {
				try {
					int codeType2 = Integer.parseInt(lineCode.substring(2, 4));
					if(codeType2 == 5){// 电源
						info.setObjectType(EObjectType.POWER);
						info.setObjectName("POWERALM");
					}else if(codeType2 == 8){// 主控卡温度
						info.setObjectType(EObjectType.CI_TEMP);
						info.setObjectName("CiTemperature");
					}else if(codeType2 == 13){// 槽位2的温度
						info.setObjectType(EObjectType.CARDSLOT2TEMP);
						info.setObjectName("CardSlot2Temp");
					}else if(codeType2 == 14){// 槽位3的温度
						info.setObjectType(EObjectType.CARDSLOT3TEMP);
						info.setObjectName("CardSlot3Temp");
					}
				} catch (Exception e) {
					ExceptionManage.dispose(e, this.getClass());
				}
			} else if (codeType == 1) {// 时钟
				info.setObjectType(EObjectType.CLOCK);
				info.setObjectName(ResourceUtil.srcStr(StringKeysObj.CLOCK));
			} else if (codeType == 2) {// 端口
				info.setObjectType(EObjectType.PORT);
				info.setObjectId(performanceLineObject.getLine() % 256 + 1);
				String portName = this.portNameMap.get(info.getObjectId());
				if(portName != null){
					info.setObjectName(portName);
				}else{
					info.setObjectName("未知端口号:"+(performanceLineObject.getLine() % 256 + 1));
				}
			} else if (codeType == 3) {// 段层(TMS)
				int number = performanceLineObject.getLine() % 256 + 1;
				info.setObjectType(EObjectType.SEGMENT);
				String name_Id = this.segmentNameMap.get(number);
				if(name_Id != null){
					info.setObjectId(Integer.parseInt(name_Id.split("/", 2)[0]));
					info.setObjectName("TMS/" + name_Id.split("/", 2)[1]);
				}
			} else if (codeType == 4) {// 时间同步
				info.setObjectType(null);
				if (lineCode.substring(2).equals("00")) {
					info.setObjectName(ResourceUtil.srcStr(StringKeysObj.PTP_MODULE));
				} else if (lineCode.substring(2).equals("01")) {
					info.setObjectName(ResourceUtil.srcStr(StringKeysObj.TOD_PORT));
				} else {
					int portNum = Integer.parseInt(lineCode.substring(2)); 
					info.setObjectName(ResourceUtil.srcStr(StringKeysObj.PTP_PORT) + (portNum - 1));
				}
			} else if (codeType == 5) {// Wrapping
				info.setObjectType(null);
				info.setObjectId(performanceLineObject.getLine() % 256 + 1);
			} else if (codeType == 10) {// LSP1+1保护
				// info.setObjectType(EObjectType.LSP);
				// info.setObjectId(Integer.parseInt(lineCode.substring(0, 1)) +
				// 1);
				// lspService = (LspService)
				// ConstantUtil.serviceFactory.newService(Services.LSPPARTICULAR);
				// lsp = new LspI();
				// lsp.setLspid(info.getObjectId());
				// lspList = lspService.select(lsp);
				// if (lspList != null && lspList.size() > 0) {
				// lspInfo = lspList.get(0);
				// info.setObjectName(lspInfo.getLspName());
				// }
			} else if (codeType == 20) {// LSP1:1保护
				info.setObjectType(EObjectType.LSP);
				info.setObjectId(performanceLineObject.getLine() % 256 + 1);
				info.setObjectName("baohu");
			} else if (codeType == 30) {// TMP通道
				info.setObjectType(EObjectType.TUNNEL);
				int tmsNum = performanceLineObject.getLine() % 256 + 1;
				info.setObjectId(tmsNum);
				String objName = this.lspNameMap.get(tmsNum);
				if(objName != null){
					info.setObjectName(objName);
				}
			}else if (codeType == 35) {// TMP通道
				info.setObjectType(EObjectType.TUNNEL);
				int tmsNum = performanceLineObject.getLine() % 256 + 1;
				String name = "";
				if(tmsNum%2==0){
					name = "后向";
					tmsNum = tmsNum/2;
				}else{
					name = "前向";
					tmsNum = (tmsNum+1)/2;
				}
				info.setObjectId(tmsNum);
				String objName = this.tunnelNameMap.get(tmsNum);
				if(objName != null){
					info.setObjectName(name+"-"+objName);
				}
			}  else if (codeType == 40) {// TMC通路
				info.setObjectType(EObjectType.PW);
				int serviceId = performanceLineObject.getLine() % 256 + 1;
				info.setObjectId(serviceId);
				String objName = this.serviceIdPwNameMap.get(serviceId);
				if(objName != null){
					info.setObjectName(objName);
				}
			} else if (codeType == 50) {// VPWS业务实例
				info.setObjectType(EObjectType.VPWS);
				info.setObjectId(Integer.parseInt(lineCode.substring(0, 1)) + 1);
			} else if (codeType == 60) {// VPLS业务实例
				info.setObjectType(EObjectType.VPLS);
				info.setObjectId(Integer.parseInt(lineCode.substring(0, 1)) + 1);
			} else if (codeType == 70) {// 1731线路
				info.setObjectType(EObjectType.ETHOAM);
				info.setObjectId(Integer.parseInt(lineCode.substring(2)) + 1);
				info.setObjectName("ETH_OAM"+(Integer.parseInt(lineCode.substring(2)) + 1));
			} else if (codeType == 80) {// E1
				info.setObjectType(EObjectType.E1);
				info.setObjectName("线路" + (Integer.parseInt(lineCode.substring(2)) + 1));
			}else if (codeType == 90) {// MSPW
				int id = performanceLineObject.getLine() % 256 + 1;
				msPwInfoService = (MsPwInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.MSPWSERVICE);
				MsPwInfo msPwInfo = new MsPwInfo();
				msPwInfo.setSiteId(info.getSiteId());
				List<MsPwInfo> msPwInfos = msPwInfoService.select(msPwInfo);
				if(msPwInfos.size() >= id){
					msPwInfo = msPwInfos.get(id-1);
					info.setObjectType(EObjectType.PW);
					info.setObjectId(msPwInfo.getPwId());
					String objName = this.pwIdPwNameNameMap.get(msPwInfo.getPwId());
					if(objName != null){
						info.setObjectName(objName);
					}
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,PerformanceWHServiceImpl.class);
		} finally {
			UiUtil.closeService_MB(msPwInfoService);
		}
	}

	/**
	 * 获得扩展性能值
	 * @param perforInfos
	 * @return
	 */
	public static List<HisPerformanceInfo> expandHisPerformanceInfo(List<HisPerformanceInfo> perforInfos) {
		List<HisPerformanceInfo> hisPerformanceInfoList = new ArrayList<HisPerformanceInfo>();
		for (int i = 0; i < perforInfos.size(); i++) {
			try {
				HisPerformanceInfo hisPerformanceInfo = perforInfos.get(i);
				if (i == 0) {
					if (hisPerformanceInfo.getPerformanceCode() != 147) {
						hisPerformanceInfoList.add(hisPerformanceInfo);
					}
				}
				if (i > 0) {
					if(hisPerformanceInfo.getPerformanceCode() == 116){
						hisPerformanceInfoList.add(hisPerformanceInfo);
					}else{
						if (hisPerformanceInfo.getPerformanceCode() != 147) {
							if (perforInfos.get(i - 1).getPerformanceCode() == 147) {// 扩展性能，需要将上一个性能与之合并
								if(isFloat(hisPerformanceInfo)){
									hisPerformanceInfo.setPerformanceValue(CoderUtils.floatToInt(perforInfos.get(i - 1).getPerformanceValue(), hisPerformanceInfo.getPerformanceValue()));
								}else{
									hisPerformanceInfo.setPerformanceValue(perforInfos.get(i - 1).getPerformanceValue() * 65536 + hisPerformanceInfo.getPerformanceValue());
								}
								hisPerformanceInfoList.add(hisPerformanceInfo);
							}else {// 非扩展性能直接添加
								hisPerformanceInfoList.add(hisPerformanceInfo);
							}
						}
					}
				}
			} catch (Exception e) {
				ExceptionManage.dispose(e,PerformanceWHServiceImpl.class);
			}
		}
		return hisPerformanceInfoList;
	}
	
	/**
	 * 获得扩展性能值
	 * 
	 * @param perforInfos
	 * @return
	 */
	public static List<CurrentPerforInfo> expandCurrentPerforInfo(List<CurrentPerforInfo> perforInfos) {
		
		List<CurrentPerforInfo> currentPerforInfoList = new ArrayList<CurrentPerforInfo>();
		for (int i = 0; i < perforInfos.size(); i++) {
			try {
				CurrentPerforInfo currentPerforInfo = perforInfos.get(i);
				if (i == 0) {
					if (currentPerforInfo.getPerformanceCode() != 147) {
						currentPerforInfoList.add(currentPerforInfo);
					}
				}
				if (i > 0) {
					if(currentPerforInfo.getPerformanceCode() == 116)
					{
						currentPerforInfoList.add(currentPerforInfo);
					}else{
						if (currentPerforInfo.getPerformanceCode() != 147) {
							if (perforInfos.get(i - 1).getPerformanceCode() == 147) {// 扩展性能，需要将上一个性能与之合并
								if(isFloat(currentPerforInfo)){
									currentPerforInfo.setPerformanceValue(CoderUtils.floatToInt(perforInfos.get(i - 1).getPerformanceValue(), currentPerforInfo.getPerformanceValue()));
								}else
								{
									currentPerforInfo.setPerformanceValue(perforInfos.get(i - 1).getPerformanceValue() * 65536 + currentPerforInfo.getPerformanceValue());
								}
								currentPerforInfoList.add(currentPerforInfo);
							}else {// 非扩展性能直接添加
								currentPerforInfoList.add(currentPerforInfo);
							}
						}
					}
				}
			} catch (Exception e) {
				ExceptionManage.dispose(e, PerformanceWHServiceImpl.class);
			}
		}
		return currentPerforInfoList;
	}
	
	/**
	 * 根据网元Id清除当前网元性能
	 * @param siteId
	 * @return
	 * @throws Exception 
	 */
	public String cleanCurrPerforBySites(Integer siteId) throws Exception {
		OperationObject operationObject = null;
		OperationObject operationObjectResult = null;
		String stauts=null;
		try {
			SiteUtil siteUtil = new SiteUtil();
			if ("0".equals(siteUtil.SiteTypeUtil(siteId) + "")) {
				operationObject = this.getClearOperationObject(siteId,"cleanPerforBySite");
				super.sendAction(operationObject);
				operationObjectResult = super.verification(operationObject);
				if (operationObjectResult.isSuccess()) {
					stauts=ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
				}else{
					stauts=ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_FALSE);
				}
			}else{
				stauts=ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}
		} catch (Exception e) {
			throw e;
		}

		return stauts;
	}
	
	public String cleanCurrPerforByCard(Integer siteId,String cardAddress) {
		OperationObject operationObject = null;
		OperationObject operationObjectResult = null;
		String stauts=null;
		try {
			SiteUtil siteUtil = new SiteUtil();
			if ("0".equals(siteUtil.SiteTypeUtil(siteId) + "")) {
				operationObject = this.getClearOperationObject(siteId,"cleanPerforBySite");
				super.sendAction(operationObject);
				operationObjectResult = super.verification(operationObject);
				if (operationObjectResult.isSuccess()) {
					stauts=ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
				}else{
					stauts=ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_FALSE);
				}
			}else{
				stauts=ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}
		} catch (Exception e) {
			try {
				throw e;
			} catch (Exception e1) {
				ExceptionManage.dispose(e1,this.getClass());
			}
		}

		return stauts;
	}
	
	/**
	 * 根据网元id列表，封装性能的OperationObject对象
	 * 
	 * @param siteIdList网元id列表
	 * @return
	 * @throws Exception
	 */
	private OperationObject getClearOperationObject(Integer siteId, String type) throws Exception {
		OperationObject operationObject = null;
		ActionObject actionObject = null;
		NEObject neObject = null;
		try {
			operationObject = new OperationObject();
			WhImplUtil whImplUtil = new WhImplUtil();
			neObject = whImplUtil.siteIdToNeObject(siteId);// 根据ID到site-inst表中取域ID+网元ID付给neObject对象
			actionObject = new ActionObject();
			actionObject.setActionId(super.getActionId(operationObject.getActionObjectList()));// 随即取数据
			actionObject.setNeObject(neObject);
			actionObject.getPerformanceObject().setNeAddress(neObject.getNeAddress());// 将neObJECT的neaddress对象复制给actionObject
			actionObject.getPerformanceObject().setPerformanceMasterCardObjectList(this.getMasterCardObjectList(siteId));
			actionObject.setType(type);
			operationObject.getActionObjectList().add(actionObject);

		} catch (Exception e) {
			throw e;
		}
		return operationObject;
	}
	
	/**
	 * 性能值是否是float类型
	 * @param currentPerforInfo
	 * @return
	 */
	private static boolean isFloat(Object object){
		if(object instanceof CurrentPerforInfo ){
			CurrentPerforInfo currentPerforInfo = (CurrentPerforInfo) object;
			if(currentPerforInfo.getPerformanceCode() == 80){
				return true;
			}
			else if(currentPerforInfo.getPerformanceCode() == 81){
				return true;
			}
			else if(currentPerforInfo.getPerformanceCode() == 82){
				return true;
			}
			else if(currentPerforInfo.getPerformanceCode() == 83){
				return true;
			}
			else if(currentPerforInfo.getPerformanceCode() == 84){
				return true;
			}
			else if(currentPerforInfo.getPerformanceCode() == 85){
				return true;
			}
			else if(currentPerforInfo.getPerformanceCode() == 89){
				return true;
			}
			else if(currentPerforInfo.getPerformanceCode() == 153){
				return true;
			}
			else if(currentPerforInfo.getPerformanceCode() == 159){
				return true;
			}
			else if(currentPerforInfo.getPerformanceCode() == 158){
				return true;
			}
			else if(currentPerforInfo.getPerformanceCode() == 114){
				return true;
			}
			else if(currentPerforInfo.getPerformanceCode() == 115){
				return true;
			}
		}else if(object instanceof HisPerformanceInfo){
			HisPerformanceInfo hisPerformanceInfo = (HisPerformanceInfo) object;
			if(hisPerformanceInfo.getPerformanceCode() == 80){
				return true;
			}
			else if(hisPerformanceInfo.getPerformanceCode() == 81){
				return true;
			}
			else if(hisPerformanceInfo.getPerformanceCode() == 82){
				return true;
			}
			else if(hisPerformanceInfo.getPerformanceCode() == 83){
				return true;
			}
			else if(hisPerformanceInfo.getPerformanceCode() == 84){
				return true;
			}
			else if(hisPerformanceInfo.getPerformanceCode() == 85){
				return true;
			}
			else if(hisPerformanceInfo.getPerformanceCode() == 89){
				return true;
			}
			else if(hisPerformanceInfo.getPerformanceCode() == 153){
				return true;
			}else if(hisPerformanceInfo.getPerformanceCode() == 159){
				return true;
			}
			else if(hisPerformanceInfo.getPerformanceCode() == 158){
				return true;
			}
			else if(hisPerformanceInfo.getPerformanceCode() == 114){
				return true;
			}
			else if(hisPerformanceInfo.getPerformanceCode() == 115){
				return true;
			}
		}
		return false;
	}
}

