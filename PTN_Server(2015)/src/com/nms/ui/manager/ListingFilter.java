package com.nms.ui.manager;

import java.util.ArrayList;
import java.util.List;

import com.nms.db.bean.alarm.CurrentAlarmInfo;
import com.nms.db.bean.alarm.HisAlarmInfo;
import com.nms.db.bean.alarm.TCAAlarm;
import com.nms.db.bean.equipment.shelf.SiteInst;
import com.nms.db.bean.equipment.slot.SlotInst;
import com.nms.db.bean.event.OamEventInfo;
import com.nms.db.bean.path.Segment;
import com.nms.db.bean.perform.PerformanceTaskInfo;
import com.nms.db.bean.ptn.path.ces.CesInfo;
import com.nms.db.bean.ptn.path.eth.DualInfo;
import com.nms.db.bean.ptn.path.eth.ElanInfo;
import com.nms.db.bean.ptn.path.eth.ElineInfo;
import com.nms.db.bean.ptn.path.eth.EtreeInfo;
import com.nms.db.bean.ptn.path.protect.LoopProtectInfo;
import com.nms.db.bean.ptn.path.pw.PwInfo;
import com.nms.db.bean.ptn.path.tunnel.Tunnel;
import com.nms.db.bean.report.SSAlarm;
import com.nms.db.bean.report.SSCard;
import com.nms.db.bean.report.SSLabel;
import com.nms.db.bean.report.SSPort;

import com.nms.model.equipment.shlef.SiteService_MB;
import com.nms.model.ptn.path.tunnel.TunnelService_MB;
import com.nms.model.util.Services;

/**
 * 列表权限过滤类
 * 
 * @author kk
 * 
 */
public class ListingFilter {

	// 登陆用户下所有网元ID
	private List<Integer> siteIdListAll = null;

	/**
	 * 创建一个新的构造函数 查询出登陆用户下所有的网元
	 * 
	 * @throws Exception
	 */
	public ListingFilter() throws Exception {
		SiteService_MB siteService = null;
		List<SiteInst> siteInstList = null;
		try {
			this.siteIdListAll = new ArrayList<Integer>();

			siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE);
			siteInstList = siteService.selectRootSite(ConstantUtil.user);
			if (null != siteInstList && siteInstList.size() > 0) {
				for (SiteInst siteInst : siteInstList) {
					this.siteIdListAll.add(siteInst.getSite_Inst_Id());
				}
			}

		} catch (Exception e) {
			throw e;
		} finally {
			UiUtil.closeService_MB(siteService);
			siteInstList = null;
		}
	}

	/**
	 * 过滤列表，把有权限的数据放入新的结果集中。
	 * 
	 * @param objectList
	 *            所有数据的结果集
	 * @return 有权限的结果集
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Object filterList(Object objectList) throws Exception {

		if (null == objectList) {
			throw new Exception("objectList is null");
		}
		List<Object> result = new ArrayList<Object>();
		List<Object> objects = null;
		try {
			if (objectList instanceof ArrayList) {
				objects = (List<Object>) objectList;
			}

			if (null != objects && objects.size() > 0) {

				// 如果登陆用户有管理所有域的权限。 跳过所有验证，直接返回所有列表
				if (ConstantUtil.user.getIsAll() == 1) {
					result.addAll(objects);
				} else {
					for (Object object : objects) {
						// 如果此对象有权限，把此对象传入到返回结果中
						if (this.filterObject(object)) {
							result.add(object);
						}
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return result;
	}

	/**
	 * 验证登陆用户是否有权限操作此对象 根据登陆用户权限域下的所有网元，比较对象中的网元是否在网元集合中。
	 * 
	 * @param object
	 *            业务对象。 segment、tunnel、pw、ces、eline
	 * @return true 有操作权限 false 无操作权限
	 * @throws Exception
	 */
	public boolean filterObject(Object object) throws Exception {

		if (null == object) {
			throw new Exception("object is null");
		}

		boolean result = true;

		List<Integer> siteIdList = null;
		try {
			// 获取此对象下所有网元id
			siteIdList = this.getSiteIds(object);
			
			if(this.siteIdListAll.size() == 0){
				result = false;
			}else{
				if (this.siteIdListAll.size() > 0 && siteIdList.size() > 0) {
					for (Integer siteId : siteIdList) {
						// 如果此网元ID在此用户管理的所有网元中不存在 那么返回false 并且结束循环
						if (!this.siteIdListAll.contains(siteId)) {
							result = false;
							break;
						}
					}
				}
			}

		} catch (Exception e) {
			throw e;
		} finally {
			siteIdList = null;
		}

		return result;
	}

	/**
	 * 根据对象取此对象中用到的所有网元。
	 * 
	 * @param object
	 *            业务对象。 segment、tunnel、pw、ces、eline
	 * @return 网元主键集合
	 * @throws Exception
	 */
	private List<Integer> getSiteIds(Object object) throws Exception {

		List<Integer> siteIdList = new ArrayList<Integer>();
		Segment segment = null;
		Tunnel tunnel = null;
		TunnelService_MB tunnelService = null;
		PwInfo pwInfo = null;
		CesInfo cesInfo = null;
		ElineInfo elineInfo = null;
		HisAlarmInfo hisAlarmInfo = null;
		CurrentAlarmInfo currentAlarmInfo = null;
		OamEventInfo oamEventInfo = null;
		TCAAlarm tcaAlarm = null;
		EtreeInfo etreeInfo = null;
		ElanInfo elanInfo = null;
		SlotInst slotInst=null;
		SSCard ssCard=null;
		SSAlarm ssAlarm=null;
		SSLabel ssLabel=null;
		SSPort ssPort =null;
		PerformanceTaskInfo performanceTaskInfo=null;
		LoopProtectInfo loopProtectInfo = null;
		DualInfo dualInfo = null;
		try {
			if (object instanceof Segment) { // 段
				segment = (Segment) object;
				siteIdList.add(segment.getASITEID());
				siteIdList.add(segment.getZSITEID());

			} else if (object instanceof Tunnel) { // tunnel
				tunnel = (Tunnel) object;
				tunnelService = (TunnelService_MB) ConstantUtil.serviceFactory.newService_MB(Services.Tunnel);
				siteIdList.addAll(tunnelService.getSiteId(tunnel));

			} else if (object instanceof PwInfo) { // pw
				pwInfo = (PwInfo) object;
				siteIdList.add(pwInfo.getASiteId());
				siteIdList.add(pwInfo.getZSiteId());

			} else if (object instanceof CesInfo) { // ces
				cesInfo = (CesInfo) object;
				siteIdList.add(cesInfo.getaSiteId());
				siteIdList.add(cesInfo.getzSiteId());
			} else if (object instanceof CurrentAlarmInfo) {
				currentAlarmInfo = (CurrentAlarmInfo) object;
				siteIdList.add(currentAlarmInfo.getSiteId());
			} else if (object instanceof HisAlarmInfo) {
				hisAlarmInfo = (HisAlarmInfo) object;
				siteIdList.add(hisAlarmInfo.getSiteId());
			} else if (object instanceof ElineInfo) { // eline
				elineInfo = (ElineInfo) object;
				siteIdList.add(elineInfo.getaSiteId());
				siteIdList.add(elineInfo.getzSiteId());
			}
			// kk begin
			else if (object instanceof OamEventInfo) { // oam事件
				oamEventInfo = (OamEventInfo) object;
				siteIdList.add(oamEventInfo.getSiteId());
			} else if (object instanceof TCAAlarm) { // tca告警
				tcaAlarm = (TCAAlarm) object;
				siteIdList.add(tcaAlarm.getSiteId());
			} else if (object instanceof EtreeInfo) { // etree业务
				etreeInfo = (EtreeInfo) object;
				siteIdList.add(etreeInfo.getRootSite());
				siteIdList.add(etreeInfo.getBranchSite());
			} else if (object instanceof ElanInfo) { // elan业务
				elanInfo = (ElanInfo) object;
				siteIdList.add(elanInfo.getaSiteId());
				siteIdList.add(elanInfo.getzSiteId());
			}else if (object instanceof PerformanceTaskInfo) { // PerformanceTaskInfo
				performanceTaskInfo = (PerformanceTaskInfo) object;
				siteIdList.add(performanceTaskInfo.getSiteInst().getSite_Inst_Id());
			}else if (object instanceof SlotInst) { // SlotInst
				slotInst = (SlotInst) object;
				siteIdList.add(slotInst.getSiteId());
			}else if (object instanceof SSCard) { // SSCard单板信息统计
				ssCard = (SSCard) object;
				siteIdList.add(ssCard.getSiteId());
			}else if (object instanceof SSAlarm) { // SSAlarm单板信息统计
				ssAlarm = (SSAlarm) object;
				siteIdList.add(ssAlarm.getSiteid());
			}else if (object instanceof SSLabel) { // SSLabel单板信息统计
				ssLabel = (SSLabel) object;
				siteIdList.add(ssLabel.getSiteId());
			}else if (object instanceof SSPort) { // SSPort单板信息统计
				ssPort = (SSPort) object;
				siteIdList.add(ssPort.getSiteId());
			} else if (object instanceof LoopProtectInfo) { // 环网保护
				loopProtectInfo = (LoopProtectInfo) object;
				siteIdList.add(loopProtectInfo.getSiteId());
			}else if (object instanceof DualInfo) { // etree业务
				dualInfo = (DualInfo) object;
				siteIdList.add(dualInfo.getRootSite());
				if(dualInfo.getBranchMainSite()>0){
					siteIdList.add(dualInfo.getBranchMainSite());
				}else if(dualInfo.getBranchProtectSite()>0){
					siteIdList.add(dualInfo.getBranchProtectSite());
				}
			} 
			// kk end
			else {
				throw new Exception("对象无效");
			}

		} catch (Exception e) {
			throw e;
		} finally {
			UiUtil.closeService_MB(tunnelService);
			segment = null;
			tunnel = null;
			pwInfo = null;
			hisAlarmInfo = null;
			cesInfo = null;
			elineInfo = null;
			currentAlarmInfo = null;
			performanceTaskInfo=null;
			slotInst=null;
			ssCard=null;
			ssAlarm=null;
			ssLabel=null;
			ssPort=null;
		}
		return siteIdList;
	}

	/**
	 * 过滤列表，把有权限的数据放入新的结果集中。
	 * 
	 * @param objectList
	 *            所有数据的结果集
	 * @return true 对此集合有操作权限。  false 没有
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public boolean filterByList(Object objectList) throws Exception {

		if (null == objectList) {
			throw new Exception("objectList is null");
		}

		boolean result = true;
		List<Object> objects = null;
		try {
			if (objectList instanceof ArrayList) {
				objects = (List<Object>) objectList;
			}

			if (null != objects && objects.size() > 0) {

				for (Object object : objects) {
					if (!this.filterObject(object)) {
						result = false;
						break;
					}
				}
			}

		} catch (Exception e) {
			throw e;
		} finally {
			objects = null;
		}
		return result;
	}

	public List<Integer> getSiteIdListAll() {
		return siteIdListAll;
	}
}
