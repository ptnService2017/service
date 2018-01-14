package com.nms.model.alarm;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.session.SqlSession;

import com.nms.db.bean.alarm.CurrentAlarmInfo;
import com.nms.db.bean.alarm.WarningLevel;
import com.nms.db.bean.equipment.shelf.SiteInst;
import com.nms.db.bean.equipment.slot.SlotInst;
import com.nms.db.dao.alarm.CurrentAlarmInfoMapper;
import com.nms.model.equipment.port.PortService_MB;
import com.nms.model.equipment.shlef.SiteService_MB;
import com.nms.model.util.ObjectService_Mybatis;
import com.nms.model.util.Services;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.ListingFilter;
import com.nms.ui.manager.ResourceUtil;
import com.nms.ui.manager.keys.StringKeysTip;
import com.nms.ui.ptn.alarm.AlarmTools;
import com.nms.ui.ptn.alarm.model.CurrentAlarmFilter;
public class CurAlarmService_MB extends ObjectService_Mybatis {
	private CurrentAlarmInfoMapper mapper = null;

	public void setPtnuser(String ptnuser) {
		super.ptnuser = ptnuser;
	}

	public void setSqlSession(SqlSession sqlSession) {
		super.sqlSession = sqlSession;
	}

	public void setMapper(CurrentAlarmInfoMapper mapper) {
		this.mapper = mapper;
	}

	/**
	 * 根据告警级别查询告警的相应数目
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int queryCurByAlarmlevel(int alarmlevel) throws Exception {
		int count = 0;
		CurrentAlarmInfo currentAlarmInfo = null;
		List<CurrentAlarmInfo> currentAlarmInfoList = null;
		ListingFilter listingFilter = null;
		List<Object> objectList = null;
		
		List<CurrentAlarmInfo> currentAlarm =null;
		List<CurrentAlarmInfo> currentAlarm1 =null;
		
		try {
			
			currentAlarmInfoList = new ArrayList<CurrentAlarmInfo>();
			// 根据网元等级查询
			currentAlarmInfo = new CurrentAlarmInfo();
			currentAlarmInfo.setWarningLevel_temp(alarmlevel);
			currentAlarm = new ArrayList<CurrentAlarmInfo>();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("currentAlarm", currentAlarmInfo);
			map.put("type", 0);
			currentAlarm=this.mapper.query_join(map);
			db2Bean(currentAlarm);
			currentAlarmInfoList.addAll(currentAlarm);
			
			
			//等级为2时 才去查询登录而产生的告警
			if(alarmlevel ==2){
				currentAlarm1 = new ArrayList<CurrentAlarmInfo>();
				currentAlarm1=this.mapper.queryByClent();
				db2Bean(currentAlarm1);
				for(int i=0;i<currentAlarm1.size();i++){
					currentAlarm1.get(i).setSiteName("EMS服务器");
				}
				currentAlarmInfoList.addAll(currentAlarm1);
			}
			this.filterByAlarmReversal(currentAlarmInfoList);
			// 查询后过滤没有权限的网元
			listingFilter = new ListingFilter();
			objectList = (List<Object>) listingFilter.filterList(currentAlarmInfoList);
			count = objectList.size();
		} catch (Exception e) {
			throw e;
		} finally {
			currentAlarmInfo = null;
			currentAlarmInfoList = null;
			listingFilter = null;
			objectList = null;
		}
		return count;
	}
	
	private void filterByAlarmReversal(List<CurrentAlarmInfo> alarmList){
		try {
			if(alarmList != null && !alarmList.isEmpty()){
				PortService_MB portService = (PortService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORT, this.sqlSession);
				Map<Integer, List<Integer>> siteIdPortMap = portService.selectAlarmReversal();
				if(siteIdPortMap.size() > 0){
					List<CurrentAlarmInfo> deleteList = new ArrayList<CurrentAlarmInfo>();
					List<Integer> codeList = new ArrayList<Integer>();
					codeList.add(192);codeList.add(193);codeList.add(11);codeList.add(12);codeList.add(121);
					codeList.add(122);codeList.add(123);codeList.add(190);codeList.add(191);codeList.add(16);
					codeList.add(17);codeList.add(18);codeList.add(68);codeList.add(35);codeList.add(36);
					codeList.add(37);codeList.add(211);codeList.add(212);codeList.add(72);
					Set<Integer> set = siteIdPortMap.keySet();
					for(Integer siteId : set){
						for(CurrentAlarmInfo alarm : alarmList){
							if(alarm.getSiteId() == siteId){
								List<Integer> numberList = siteIdPortMap.get(siteId);
								if(!numberList.isEmpty()){
									for(Integer number : numberList){
										int code = alarm.getAlarmCode();
										if(alarm.getObjectId() == number && codeList.contains(code)){
											deleteList.add(alarm);
										}
									}
								}
							}
						}
					}
					alarmList.removeAll(deleteList);
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
	}
	
	/**
	 * 查询时赋值
	 * 需要将数据类型做转换
	 * @param curInfo
	 */
	private void db2Bean(List<CurrentAlarmInfo> currentAlarm){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		AlarmTools alarmTools = new AlarmTools();		
		try {
			if(currentAlarm!=null && currentAlarm.size()>0){
				for(int i=0;i<currentAlarm.size();i++){
					if(currentAlarm.get(i).getHappenedtime() !=null && !"".equals(currentAlarm.get(i).getHappenedtime())){
				        currentAlarm.get(i).setRaisedTime(sdf.parse(currentAlarm.get(i).getHappenedtime()));
						if(currentAlarm.get(i).getConfirmtime() !=null && !"".equals(currentAlarm.get(i).getConfirmtime())){
							currentAlarm.get(i).setAckTime(sdf.parse(currentAlarm.get(i).getConfirmtime()));
							currentAlarm.get(i).setAcked(true);
						}else{
							currentAlarm.get(i).setAcked(false);
						}
						if(currentAlarm.get(i).getCleanTime() !=null && !"".equals(currentAlarm.get(i).getCleanTime())){
							currentAlarm.get(i).setClearedTime(sdf.parse(currentAlarm.get(i).getCleanTime()));
							currentAlarm.get(i).setCleared(true);
						}else{
							currentAlarm.get(i).setCleared(false);
						}
				
						if( currentAlarm.get(i).getIsClear() == 1){
							currentAlarm.get(i).setIsCleared(ResourceUtil.srcStr(StringKeysTip.TIP_CLEARED));
						}else{
							currentAlarm.get(i).setIsCleared(ResourceUtil.srcStr(StringKeysTip.TIP_UNCLEARED));
						}
					
						if("zh_CN".equals(ResourceUtil.language)){
							currentAlarm.get(i).setAlarmDesc(currentAlarm.get(i).getWarningLevel().getWarningdescribe());
						}else{
							currentAlarm.get(i).setAlarmDesc(currentAlarm.get(i).getWarningLevel().getWarningname());
						}	
					   currentAlarm.get(i).setAlarmSeverity(alarmTools.getAlarmSeverity(currentAlarm.get(i).getWarningLevel().getWarninglevel_temp()));
				    }				
				}
			}
		} catch (ParseException e) {
			ExceptionManage.dispose(e, this.getClass());
		}
     }

	public List<Integer> querySiteIdList() {
		List<Integer> siteIdList = new ArrayList<Integer>();		
		siteIdList = this.mapper.querySiteid();		
		return siteIdList;
	}

	public int selectMaxId() {
		Integer maxId = this.mapper.queryMaxId();
		if(maxId == null){
			return 0;
		}else{
			return maxId;
		}
	}

	public List<Integer> selectCurrAlarmId(CurrentAlarmFilter filter, List<Integer> siteIdList) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("filter", filter);
		map.put("siteIdList", siteIdList);
		if(filter != null && filter.getObjectType() != null){
			map.put("type", filter.getObjectType().getValue());
		}else{
			map.put("type", 0);
		}
		return this.mapper.selectAllIdList(map);
	}

	public List<CurrentAlarmInfo> selectByPage(int direction, int id, CurrentAlarmFilter filter,
			List<Integer> siteIdList, int pageCount) throws Exception {
		List<CurrentAlarmInfo> currfoList = new ArrayList<CurrentAlarmInfo>();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("direction", direction);
			map.put("id", id);
			map.put("filter", filter);
			map.put("siteIdList", siteIdList);
			map.put("pageCount", pageCount);
			if(filter != null && filter.getObjectType() != null){
				map.put("type", filter.getObjectType().getValue());
			}else{
				map.put("type", 0);
			}
			currfoList = this.mapper.selectByPage(map);
			this.filterByAlarmReversal(currfoList);
			this.wrapCurAlarmInfo(currfoList);
			this.db2Bean(currfoList);
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return currfoList;
	}
	
	private void wrapCurAlarmInfo(List<CurrentAlarmInfo> curInfoList) throws Exception {
		SiteInst siteInst = null;
		List<WarningLevel> warnList = null;
		AlarmTools alarmTools = new AlarmTools();
		WarningLevelService_MB warningLevelService = null;
		SiteService_MB siteService = null;
		try {
			warningLevelService = (WarningLevelService_MB) ConstantUtil.serviceFactory.newService_MB(Services.WarningLevel, this.sqlSession);
			siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE, this.sqlSession);
			for (CurrentAlarmInfo curInfo : curInfoList) {
				// 封装网元信息
				siteInst = new SiteInst();
				siteInst = siteService.select(curInfo.getSiteId());
				curInfo.setSiteName(siteInst.getCellId() + "");
				// 封装告警数据
				WarningLevel condition = new WarningLevel();
				condition.setWarningcode(curInfo.getAlarmCode());
				condition.setWarninglevel(curInfo.getAlarmLevel());
				if (siteInst.getManufacturer() == 0) {
					condition.setManufacturer(1); // 1表示武汉
				} else {
					condition.setManufacturer(2); // 2表示晨晓
				}
				warnList = warningLevelService.select(condition);
				if (warnList != null && warnList.size() > 0) {
					WarningLevel warningLevel = warnList.get(0);
					curInfo.setWarningLevel(warningLevel);
					curInfo.setAlarmSeverity(alarmTools.getAlarmSeverity(curInfo.getWarningLevel().getWarninglevel_temp())); // 2表示晨晓
					curInfo.setAlarmDesc(warningLevel.getWarningdescribe());
				}
				if (curInfo.getAckTime() != null && !"".equals(curInfo.getAckTime())) {
					curInfo.setAcked(true);
				} else {
					curInfo.setAcked(false);
				}
				if (curInfo.getClearedTime() != null && !"".equals(curInfo.getClearedTime())) {
					curInfo.setCleared(true);
				} else {
					curInfo.setCleared(false);
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public int delete(int curId) throws Exception {
		if (curId == 0) {
			throw new Exception("curId is null");
		}
		int resultcesId = 0;
		try {
			resultcesId = this.mapper.delete(curId);
			this.sqlSession.commit();
		} catch (Exception e) {
			throw e;
		}
		return resultcesId;
	}

	public int saveOrUpdate(CurrentAlarmInfo curInfo) throws Exception {
		if (curInfo == null) {
			throw new Exception("taskInfo is null");
		}
		int resultcesId = 0;
		SiteInst siteInst = null;
		List<WarningLevel> warnList = null;
		WarningLevel warningLevel = null;
		WarningLevelService_MB warningLevelService = null;
		SiteService_MB siteService = null;
		try {
			warningLevelService = (WarningLevelService_MB) ConstantUtil.serviceFactory.newService_MB(Services.WarningLevel, this.sqlSession);
			siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE, this.sqlSession);
			siteInst = new SiteInst();
			if(curInfo.getSiteId() > 0)
			{
				siteInst = siteService.select(curInfo.getSiteId());
			}
			warningLevel = new WarningLevel();
			warningLevel.setWarningcode(curInfo.getAlarmCode());
			warningLevel.setWarninglevel(curInfo.getAlarmLevel());
			curInfo.setWarningLevel(warningLevel);
			if (siteInst.getManufacturer() == 0) {
				warningLevel.setManufacturer(1); // 1表示武汉
			} else {
				warningLevel.setManufacturer(2); // 2表示晨晓
			}

			warnList = warningLevelService.select(warningLevel);
			if (warnList != null && warnList.size() > 0) {
				curInfo.setWarningLevel_temp(warnList.get(0).getWarninglevel_temp());
				curInfo.setWarningLevel(warnList.get(0));
			}

			this.bean2Db(curInfo);
			if (curInfo.getId() != 0) {
				resultcesId = this.mapper.update(curInfo);
			} else {
				resultcesId = this.mapper.insert(curInfo);
				curInfo.setId(resultcesId);
			}
			this.sqlSession.commit();
		} catch (Exception e) {
			throw e;
		}
		return resultcesId;
	}

	/**
	 * 条件查询或者新增，修改操作时
	 * 需要将数据类型做转换
	 * @param curInfo
	 */
	private void bean2Db(CurrentAlarmInfo curInfo) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (curInfo.getRaisedTime() != null) {
			curInfo.setHappenedtime(sdf.format(curInfo.getRaisedTime()));
		}
		if (curInfo.getAckTime() != null ) {
			curInfo.setConfirmtime(sdf.format(curInfo.getAckTime()));
		}
		if (curInfo.getClearedTime() != null) {
			curInfo.setCleanTime(sdf.format(curInfo.getClearedTime()));
		}
		if(curInfo.getIsCleared()!=null && curInfo.getIsCleared().equals(ResourceUtil.srcStr(StringKeysTip.TIP_CLEARED))){
			curInfo.setIsClear(1);
		}
	}

	/**
	 * 查询所有
	 * @return key为网元主键 value为此网元的所有告警
	 * @throws Exception 
	 * @throws Exception
	 */
	public Map<Integer, List<CurrentAlarmInfo>> selectAll() throws Exception {
		Map<Integer, List<CurrentAlarmInfo>> map = null;
		List<CurrentAlarmInfo> currentAlarmInfoList = null;
		List<CurrentAlarmInfo> currentAlarmInfoList_site = null;
		try {
			map = new HashMap<Integer, List<CurrentAlarmInfo>>();
			Map<String, Object> conditionMap = new HashMap<String, Object>();
			conditionMap.put("currentAlarm", new CurrentAlarmInfo());
			conditionMap.put("type", 0);
			currentAlarmInfoList = this.mapper.query_join(conditionMap);
			this.filterByAlarmReversal(currentAlarmInfoList);
			this.db2Bean(currentAlarmInfoList);
			for (CurrentAlarmInfo currentAlarmInfo : currentAlarmInfoList) {
				if (null == map.get(currentAlarmInfo.getSiteId())) {
					currentAlarmInfoList_site = new ArrayList<CurrentAlarmInfo>();
					currentAlarmInfoList_site.add(currentAlarmInfo);
					map.put(currentAlarmInfo.getSiteId(), currentAlarmInfoList_site);
				} else {
					map.get(currentAlarmInfo.getSiteId()).add(currentAlarmInfo);
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			currentAlarmInfoList = null;
			currentAlarmInfoList_site = null;
		}
		return map;
	}

	public List<CurrentAlarmInfo> queryCurBySites(List<Integer> siteIdList) throws Exception {
		List<CurrentAlarmInfo> curInfoList = new ArrayList<CurrentAlarmInfo>();
		try {
			List<CurrentAlarmInfo> list = this.mapper.queryCurrBySites(siteIdList);
			if (list != null) {
				curInfoList.addAll(list);
				this.filterByAlarmReversal(curInfoList);
			}
			this.db2Bean(curInfoList);
		} catch (Exception e) {
			throw e;
		}
		return curInfoList;
	}

	@SuppressWarnings("unchecked")
	public int queryCurrAlarmBylevel(int alarmlevel) throws Exception {
		int count = 0;
		CurrentAlarmInfo currentAlarmInfo = null;
		List<CurrentAlarmInfo> currentAlarmInfoList = null;
		ListingFilter listingFilter = null;
		List<Object> objectList = null;
		try {
			currentAlarmInfoList = new ArrayList<CurrentAlarmInfo>();
			// 根据网元等级查询
			currentAlarmInfo = new CurrentAlarmInfo();
			currentAlarmInfo.setWarningLevel_temp(alarmlevel);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("alarm", currentAlarmInfo);
			map.put("type", 0);
			currentAlarmInfoList.addAll(this.mapper.queryByCondition(map));
			this.filterByAlarmReversal(currentAlarmInfoList);
			// 查询后过滤没有权限的网元
			listingFilter = new ListingFilter();
			objectList = (List<Object>) listingFilter.filterList(currentAlarmInfoList);
			count = objectList.size();
		} catch (Exception e) {
			throw e;
		} finally {
			currentAlarmInfo = null;
			currentAlarmInfoList = null;
			listingFilter = null;
			objectList = null;
		}
		return count;
	}
		
	/**
	 * 查询非设备的告警
	 * @return
	 */
	public List<CurrentAlarmInfo> alarmByAlarmLevel()throws Exception{
		List<CurrentAlarmInfo> currentAlarmInfoList = null;
		try {
			currentAlarmInfoList = new ArrayList<CurrentAlarmInfo>();
			currentAlarmInfoList = this.mapper.queryByClent();
			this.db2Bean(currentAlarmInfoList);
		} catch (Exception e) {
			throw e;
		}
		return currentAlarmInfoList;
	}

	public int selectCount() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("alarm", new CurrentAlarmInfo());
		map.put("type", 0);
		return this.mapper.queryByCondition(map).size();
	}

	public int deleteCurrentAlarmInfo(CurrentAlarmInfo currentAlarmInfo) throws Exception {
		if (currentAlarmInfo == null) {
			throw new Exception("currentAlarmInfo is null");
		}
		int resultcesId = 0;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("alarm", currentAlarmInfo);
			if(currentAlarmInfo != null && currentAlarmInfo.getObjectType() != null){
				map.put("type", currentAlarmInfo.getObjectType().getValue());
			}else{
				map.put("type", 0);
			}
			resultcesId = this.mapper.deleteCurrentAlarmInfo(map);
			this.sqlSession.commit();
		} catch (Exception e) {

			throw e;
		}
		return resultcesId;
	}

	public List<CurrentAlarmInfo> select(CurrentAlarmInfo condition) throws Exception {
		List<CurrentAlarmInfo> curInfoList = null;
		try {
			Map<String, Object> conditionMap = new HashMap<String, Object>();
			conditionMap.put("currentAlarm", condition);
			conditionMap.put("type", 0);
			curInfoList = this.mapper.query_join(conditionMap);
			this.filterByAlarmReversal(curInfoList);
			this.db2Bean(curInfoList);
		} catch (Exception e) {
			throw e;
		}
		return curInfoList;
	}

	/**
	 * 查询指定的非设备的告警
	 * @return
	 * @throws Exception
	 */
	public List<CurrentAlarmInfo> queryClientAlarm(int alarmCode,int level)throws Exception {
	  List<CurrentAlarmInfo> currentAlarmInfoList = new ArrayList<CurrentAlarmInfo>();
	  try {
		  currentAlarmInfoList = this.mapper.queryByClentAlarm(alarmCode, level);
		  this.db2Bean(currentAlarmInfoList);
	  } catch (Exception e) {
	      throw e;
	  }
	  return currentAlarmInfoList;
   }
  
	/**
	 * 查询指定的非设备的告警
	 * @return
	 * @throws Exception
	 */
	public List<CurrentAlarmInfo> queryClientDISCAlarm(int alarmCode, int level, String objectName)throws Exception {
	  List<CurrentAlarmInfo> currentAlarmInfoList = new ArrayList<CurrentAlarmInfo>();
	  try {
		 objectName = objectName.split(":")[0];
	     currentAlarmInfoList = this.mapper.queryByClentAlarmByObjName(alarmCode, level, objectName);
	     this.db2Bean(currentAlarmInfoList);
	  } catch (Exception e) {
		 throw e;
	  }
	  return currentAlarmInfoList;
    }

	/**
	 * 查询非设备的告警
	 * @return
	 */
	public List<CurrentAlarmInfo> alarmByAlarmLevel(int level)throws Exception{
		List<CurrentAlarmInfo> currentAlarmInfoList = null;
		try {
			currentAlarmInfoList = new ArrayList<CurrentAlarmInfo>();
			currentAlarmInfoList = this.mapper.queryByClentLevel(level);
			this.db2Bean(currentAlarmInfoList);
		} catch (Exception e) {
			throw e;
		}
		return currentAlarmInfoList;
	}

	public List<CurrentAlarmInfo> select() throws Exception {
		List<CurrentAlarmInfo> curInfoList = null;
		try {
			curInfoList = new ArrayList<CurrentAlarmInfo>();
			Map<String, Object> conditionMap = new HashMap<String, Object>();
			conditionMap.put("currentAlarm", new CurrentAlarmInfo());
			conditionMap.put("type", 0);
			curInfoList = this.mapper.query_join(conditionMap);
			this.filterByAlarmReversal(curInfoList);
			this.db2Bean(curInfoList);
		} catch (Exception e) {
			throw e;
		}
		return curInfoList;
	}
	
	/**
	 * 根据类型和id集合查询
	 * 
	 * @author kk
	 * @param siteId 
	 * 
	 * @param
	 * 
	 * @return
	 * @throws Exception
	 * @throws Exception
	 * 
	 * @Exception 异常对象
	 */
	public List<CurrentAlarmInfo> select_type_id(int objectType, List<Integer> objectIdList, int siteId) throws Exception {

		List<CurrentAlarmInfo> currentAlarmInfoList = null;
		try {
			currentAlarmInfoList =new ArrayList<CurrentAlarmInfo>();
			if(objectIdList.size()>0){
			   currentAlarmInfoList = this.mapper.query_type_id(objectType, objectIdList, siteId);
			   this.filterByAlarmReversal(currentAlarmInfoList);
			   wrapCurAlarmInfo(currentAlarmInfoList);
			}
		} catch (Exception e) {
			throw e;
		}
		return currentAlarmInfoList;
	}

	public int delete(List<Integer> idList) throws Exception {
		int result = 0;
		try {
			if (idList == null || idList.size() == 0) {
				return 0;
			}
			for (Integer id : idList) {
				result = this.delete(id);
			}
			this.sqlSession.commit();
		} catch (Exception e) {
			throw e;
		}
		return result;
	}

	/**
	 * 批量插入当前告警(武汉处理轮询告警)
	 * @param currentAlarmInfos
	 * @throws SQLException 
	 */
	public void insertList(List<CurrentAlarmInfo> currentAlarmInfos,int siteId){
		SiteInst siteInst = null;
		WarningLevel warningLevel = null;
		WarningLevelService_MB warningLevelService = null;
		SiteService_MB siteService = null;
		Map<String, WarningLevel> map = null;
		try {
			warningLevelService = (WarningLevelService_MB) ConstantUtil.serviceFactory.newService_MB(Services.WarningLevel, this.sqlSession);
			siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE, this.sqlSession);
			siteInst = new SiteInst();
			//插入前，先删除该网元下的所有当前告警
			CurrentAlarmInfo alarmInfo = new  CurrentAlarmInfo();
			alarmInfo.setSiteId(siteId);
			this.deleteCurrentAlarmInfo(alarmInfo);
			if(currentAlarmInfos != null && currentAlarmInfos.size()>0){
				
				//获取告警级别map
				List<WarningLevel> warningLevels = warningLevelService.select();
				map = new HashMap<String, WarningLevel>();
				for(WarningLevel level:warningLevels){
					map.put(level.getManufacturer()+":"+level.getWarningcode()+":"+level.getWarninglevel(), level);
				}
				
				//厂商信息
				siteInst = siteService.select(currentAlarmInfos.get(0).getSiteId());
			}
			for(CurrentAlarmInfo curInfo: currentAlarmInfos){
				if (siteInst.getManufacturer() == 0) {
					warningLevel = map.get("1:"+curInfo.getAlarmCode()+":"+curInfo.getAlarmLevel()); // 1表示武汉
				} else {
					warningLevel = map.get("2:"+curInfo.getAlarmCode()+":"+curInfo.getAlarmLevel());// 2表示晨晓
				}
				if (warningLevel != null ) {
					//告警级别赋值
					curInfo.setWarningLevel_temp(warningLevel.getWarninglevel_temp());
					curInfo.setWarningLevel(warningLevel);
				}
				this.bean2Db(curInfo);
				this.mapper.insert(curInfo);
			} 
			this.sqlSession.commit();
		}catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
	}

	public List<CurrentAlarmInfo> selectByStartTimeAndEndTime(String startTime, String endTime) {
		List<CurrentAlarmInfo> currentAlarmInfos = null;
		try {
			currentAlarmInfos = this.mapper.queryByTime_join(startTime, endTime);
			this.filterByAlarmReversal(currentAlarmInfos);
			this.wrapCurAlarmInfo(currentAlarmInfos);
			this.db2Bean(currentAlarmInfos);
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return currentAlarmInfos;
	}

	/**
	 * 获取槽位下 最高告警等级
	 * @param slotInst		槽位
	 * @return -1为未查询到结果
	 */
	public int queryTopAlarm(SlotInst slotInst) {
		List<CurrentAlarmInfo> list;
		try {
			list = this.mapper.queryBySlot(slotInst);
			if(null!=list && list.size()>0)
				return	list.get(0).getAlarmLevel();
		} catch (Exception e) {
			ExceptionManage.dispose(e, getClass());
		}
		return -1;
	}	

	public List<CurrentAlarmInfo> selectCurrentAlarmByCond(String startTime, String endTime, List<String> siteIdList,
			List<Integer> alarmSeverityList, List<Integer> alarmTypeList, String[] inludeProbCauseArr) {
		List<CurrentAlarmInfo> currentAlarmInfos = new ArrayList<CurrentAlarmInfo>();
		try {
			List<String> inludeProbCauseList = new ArrayList<String>();
			if(inludeProbCauseArr != null && inludeProbCauseArr.length > 0){
				for (int i = 0; i < inludeProbCauseArr.length; i++) {
					inludeProbCauseList.add(inludeProbCauseArr[i]);
				}
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("startTime", startTime);
			map.put("endTime", endTime);
			map.put("siteIdList", siteIdList);
			map.put("alarmSeverityList", alarmSeverityList);
			map.put("alarmTypeList", alarmTypeList);
			map.put("inludeProbCauseList", inludeProbCauseList);
			currentAlarmInfos = this.mapper.selectCurrentAlarmByCond(map);
			this.filterByAlarmReversal(currentAlarmInfos);
			if(currentAlarmInfos == null){
				currentAlarmInfos = new ArrayList<CurrentAlarmInfo>();
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return currentAlarmInfos;
	}
	
	public List<Map<String, Object>> queryNorthRun(Integer alarmSeq,String time){
		ExceptionManage.infor("alarmSeq::"+alarmSeq+"time：："+time,this.getClass());
		return this.mapper.queryNorthRun(alarmSeq,time);
	}
	
	public CurrentAlarmInfo queryNorthTest(){
		return this.mapper.queryNorthTest();
	}
	
	public void insertNorthBatch(List<CurrentAlarmInfo> list){
		this.mapper.insertNorthBatch(list);
		this.sqlSession.commit();
	}
	
}
