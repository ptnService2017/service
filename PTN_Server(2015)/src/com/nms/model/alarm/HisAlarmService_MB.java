package com.nms.model.alarm;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.nms.db.bean.alarm.HisAlarmInfo;
import com.nms.db.bean.alarm.WarningLevel;
import com.nms.db.bean.equipment.shelf.SiteInst;
import com.nms.db.dao.alarm.HisAlarmInfoMapper;
import com.nms.db.enums.EManufacturer;
import com.nms.model.equipment.shlef.SiteService_MB;
import com.nms.model.util.ObjectService_Mybatis;
import com.nms.model.util.Services;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.ResourceUtil;
import com.nms.ui.manager.keys.StringKeysTip;
import com.nms.ui.ptn.alarm.AlarmTools;
import com.nms.ui.ptn.alarm.model.CurrentAlarmFilter;

public class HisAlarmService_MB extends ObjectService_Mybatis {
	private HisAlarmInfoMapper mapper = null;
	
	public void setPtnuser(String ptnuser) {
		super.ptnuser = ptnuser;
	}

	public void setSqlSession(SqlSession sqlSession) {
		super.sqlSession = sqlSession;
	}
	
	public HisAlarmInfoMapper getMapper() {
		return mapper;
	}
	
	public void setMapper(HisAlarmInfoMapper mapper) {
		this.mapper = mapper;
	}

	public int saveOrUpdate(HisAlarmInfo hisInfo) throws Exception {
		if (hisInfo == null) {
			throw new Exception("taskInfo is null");
		}
		int resultcesId = 0;
		SiteInst siteInst = null;
		WarningLevel warningLevel = null;
		List<WarningLevel> warnList = null;
		SiteService_MB siteService = null;
		WarningLevelService_MB warningLevelService = null;
		try {
			warningLevelService = (WarningLevelService_MB) ConstantUtil.serviceFactory.newService_MB(Services.WarningLevel, this.sqlSession);
			siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE, this.sqlSession);
			siteInst = new SiteInst();
			siteInst = siteService.select(hisInfo.getSiteId());
			warningLevel = new WarningLevel();
			warningLevel.setWarningcode(hisInfo.getAlarmCode());
			warningLevel.setWarninglevel(hisInfo.getAlarmLevel());
			if (siteInst.getManufacturer()==0) {
				warningLevel.setManufacturer(1); // 1表示武汉
			} else {
				warningLevel.setManufacturer(2); // 2表示晨晓
			}
		
			warnList = warningLevelService.select(warningLevel);
			if(warnList != null && warnList.size() > 0){
				hisInfo.setWarningLevel_temp(warnList.get(0).getWarninglevel_temp());
			}
			this.bean2Db(hisInfo);
			if (hisInfo.getId() != 0) {
				resultcesId = this.mapper.update(hisInfo);
			} else {
				resultcesId = this.mapper.insert(hisInfo);
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
	 * @param hisInfo
	 */
	private void bean2Db(HisAlarmInfo hisInfo) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		if(hisInfo.getObjectType() != null){
//			hisInfo.setObjectType_db(hisInfo.getObjectType().getValue());
//		}
		if (hisInfo.getRaisedTime() != null) {
			hisInfo.setHappenedtime(sdf.format(hisInfo.getRaisedTime()));
		}
		if (hisInfo.getAckTime() != null ) {
			hisInfo.setConfirmtime(sdf.format(hisInfo.getAckTime()));
		}
		if (hisInfo.getClearedTime() != null) {
			hisInfo.setCleanTime(sdf.format(hisInfo.getClearedTime()));
		}
		if(hisInfo.getIsCleared()!=null && hisInfo.getIsCleared().equals(ResourceUtil.srcStr(StringKeysTip.TIP_CLEARED))){
			hisInfo.setIsClear(1);
		}
	}

	public List<HisAlarmInfo> selectByPage(int direction, int id, CurrentAlarmFilter filter, List<Integer> siteIdList, int pageCount) {
		List<HisAlarmInfo> hisInfoList = new ArrayList<HisAlarmInfo>();
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
			hisInfoList = this.mapper.selectByPage(map);
			this.wrapHisAlarmInfo(hisInfoList);
			this.db2Bean(hisInfoList);
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return hisInfoList;
	}
	
	private void db2Bean(List<HisAlarmInfo> hisInfoList) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		AlarmTools alarmTools = new AlarmTools();		
		try {
			if(hisInfoList!=null && hisInfoList.size()>0){
				for(int i=0;i<hisInfoList.size();i++){
					if(hisInfoList.get(i).getHappenedtime() !=null && !"".equals(hisInfoList.get(i).getHappenedtime())){
				        hisInfoList.get(i).setRaisedTime(sdf.parse(hisInfoList.get(i).getHappenedtime()));
						if(hisInfoList.get(i).getConfirmtime() !=null && !"".equals(hisInfoList.get(i).getConfirmtime())){
							hisInfoList.get(i).setAckTime(sdf.parse(hisInfoList.get(i).getConfirmtime()));
							hisInfoList.get(i).setAcked(true);
						}else{
							hisInfoList.get(i).setAcked(false);
						}
						if(hisInfoList.get(i).getCleanTime() !=null && !"".equals(hisInfoList.get(i).getCleanTime())){
							hisInfoList.get(i).setClearedTime(sdf.parse(hisInfoList.get(i).getCleanTime()));
							hisInfoList.get(i).setCleared(true);
						}else{
							hisInfoList.get(i).setCleared(false);
						}
				
						if( hisInfoList.get(i).getIsClear() == 1){
							hisInfoList.get(i).setIsCleared(ResourceUtil.srcStr(StringKeysTip.TIP_CLEARED));
						}else{
							hisInfoList.get(i).setIsCleared(ResourceUtil.srcStr(StringKeysTip.TIP_UNCLEARED));
						}
					
						if("zh_CN".equals(ResourceUtil.language)){
							hisInfoList.get(i).setAlarmDesc(hisInfoList.get(i).getWarningLevel().getWarningdescribe());
						}else{
							hisInfoList.get(i).setAlarmDesc(hisInfoList.get(i).getWarningLevel().getWarningname());
						}	
//					   hisInfoList.get(i).setObjectType(EObjectType.forms(hisInfoList.get(i).getObjectType_db()));
					   hisInfoList.get(i).setAlarmSeverity(alarmTools.getAlarmSeverity(hisInfoList.get(i).getWarningLevel().getWarninglevel_temp()));
				    }				
				}
			}
		} catch (ParseException e) {
			ExceptionManage.dispose(e, this.getClass());
		}
	}

	private void wrapHisAlarmInfo(List<HisAlarmInfo> hisInfoList) throws Exception {
		List<WarningLevel> warnList = null;
		AlarmTools alarmTools = new AlarmTools();
		WarningLevelService_MB warningLevelService = null;
		SiteService_MB siteService = null;
		try {
			warningLevelService = (WarningLevelService_MB) ConstantUtil.serviceFactory.newService_MB(Services.WarningLevel, this.sqlSession);
			siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE, this.sqlSession);
			Map<Integer, String> siteIdAndNameMap = this.getsiteIdAndNameMap(siteService);
			for (HisAlarmInfo hisInfo : hisInfoList) {
				// 封装网元信息
				String siteNameAndManuf = siteIdAndNameMap.get(hisInfo.getSiteId());
				int manufacturer = Integer.parseInt(siteNameAndManuf.split("/", 2)[0]);
				String siteName = siteNameAndManuf.split("/", 2)[1];
				if(siteName != null && !"".equals(siteName)){
					hisInfo.setSiteName(siteName);
				}else{
					hisInfo.setSiteName("EMS服务器");
				}
				// 封装告警数据
				WarningLevel condition = new WarningLevel();
				condition.setWarningcode(hisInfo.getAlarmCode());
				condition.setWarninglevel(hisInfo.getAlarmLevel());
				if(manufacturer == EManufacturer.WUHAN.getValue()){
					condition.setManufacturer(1);  //1表示武汉
				}else {
					condition.setManufacturer(2);  //2表示晨晓
				}
				warnList = warningLevelService.select(condition);
				if (warnList != null && warnList.size() > 0) {
					WarningLevel warningLevel = warnList.get(0);
					hisInfo.setWarningLevel(warningLevel);
//					if(UiUtil.getManufacturer(siteInst.getSite_Inst_Id()) == EManufacturer.valueOf("WUHAN").getValue()){
//						hisInfo.setAlarmSeverity(AlarmTools.getWHAlarmSeverity(hisInfo.getWarningLevel().getWarninglevel()));  //1表示武汉
//					}else {
						hisInfo.setAlarmSeverity(alarmTools.getAlarmSeverity(hisInfo.getWarningLevel().getWarninglevel_temp()));  //2表示晨晓
//					}
					if("zh_CN".equals(ResourceUtil.language)){
						hisInfo.setAlarmDesc(warningLevel.getWarningdescribe());
					}else{
						hisInfo.setAlarmDesc(warningLevel.getWarningname());
					}
				}
				else
				{
					String msg="不显示的告警码为：" + hisInfo.getAlarmCode() + "  级别为：" + hisInfo.getAlarmLevel();
					ExceptionManage.infor(msg, this.getClass());
				}
				if (hisInfo.getAckTime() != null && !"".equals(hisInfo.getAckTime())) {
					hisInfo.setAcked(true);
				} else {
					hisInfo.setAcked(false);
				}
				if (hisInfo.getClearedTime() != null && !"".equals(hisInfo.getClearedTime())) {
					hisInfo.setCleared(true);
				} else {
					hisInfo.setCleared(false);
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	private Map<Integer, String> getsiteIdAndNameMap(SiteService_MB siteService) {
		Map<Integer, String> siteIdAndNameMap = new HashMap<Integer, String>();
		try {
			List<SiteInst> siteList = siteService.select();
			if(siteList != null){
				for (SiteInst site : siteList) {
					siteIdAndNameMap.put(site.getSite_Inst_Id(), site.getManufacturer()+"/"+
							site.getCellId());
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return siteIdAndNameMap;
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

	
	public List<HisAlarmInfo> queryHisBySites(List<Integer> siteIdList) throws Exception {
		List<HisAlarmInfo> hisInfoList = new ArrayList<HisAlarmInfo>();
		try {
			hisInfoList = this.mapper.queryHisBySites(siteIdList);
			this.wrapHisAlarmInfo(hisInfoList);
			this.db2Bean(hisInfoList);
		} catch (Exception e) {
			throw e;
		}
		return hisInfoList;
	}

	public List<HisAlarmInfo> queryHisBySlots(Integer siteId, List<Integer> slotIds) throws Exception {
		List<HisAlarmInfo> hisInfoList = new ArrayList<HisAlarmInfo>();
		AlarmTools alarmTools = new AlarmTools();
		WarningLevelService_MB warningLevelService = null;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("siteId", siteId);
			map.put("slotIdList", slotIds);
			hisInfoList = this.mapper.queryHisBySlots(map);
			warningLevelService = (WarningLevelService_MB) ConstantUtil.serviceFactory.newService_MB(Services.WarningLevel, this.sqlSession);
			for (HisAlarmInfo hisInfo : hisInfoList) {
				// 封装告警数据
				WarningLevel warningLevel = hisInfo.getWarningLevel();
				warningLevel = warningLevelService.select(warningLevel).get(0);
				hisInfo.setWarningLevel(warningLevel);
				hisInfo.setAlarmSeverity(alarmTools.getAlarmSeverity(hisInfo.getWarningLevel().getWarninglevel_temp()));
				if (hisInfo.getAckTime() != null && !"".equals(hisInfo.getAckTime())) {
					hisInfo.setAcked(true);
				} else {
					hisInfo.setAcked(false);
				}
				if (hisInfo.getClearedTime() != null && !"".equals(hisInfo.getClearedTime())) {
					hisInfo.setCleared(true);
				} else {
					hisInfo.setCleared(false);
				}
			}
			this.db2Bean(hisInfoList);
		} catch (Exception e) {
			throw e;
		}
		return hisInfoList;
	}

	public List<HisAlarmInfo> select() throws Exception {
		List<HisAlarmInfo> hisInfoList = null;
		try {
			HisAlarmInfo condition = new HisAlarmInfo();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("alarm", condition);
			map.put("type", 0);
			hisInfoList = this.mapper.queryByCondition(map);
			this.wrapHisAlarmInfo(hisInfoList);
			this.db2Bean(hisInfoList);
		} catch (Exception e) {
			throw e;
		}
		return hisInfoList;
	}
	
	/**
	 * 查看
	 * @param tableName 是否存在
	 * 
	 * @return
	 */
	public boolean selectTable(String tableName){
		boolean flag=false;						
		ResultSet resultset = null;
		try {
			resultset  = this.sqlSession.getConnection().getMetaData().getTables(null, null,  tableName, null );
			if(resultset.next()){
				flag=true;					
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, getClass());
		}finally{
			if(resultset != null){
				try {
					resultset.close();
				} catch (Exception e2) {
					ExceptionManage.dispose(e2, getClass());
				}finally{
					resultset = null;
				}
			}
		}
		return flag;		
	}
	
	/**
	 * 批量添加 sql語句
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public int insertSql(List<String> sql) throws Exception{
		int result=0;
		try {
			this.sqlSession.getConnection().setAutoCommit(false);
			@SuppressWarnings("rawtypes")
			Iterator i=sql.iterator();
			while(i.hasNext()){
				String insertSql=(String) i.next();
				if(insertSql.startsWith("insert")||insertSql.equals("INSERT")){
					PreparedStatement preparedStatement = null;
					preparedStatement = this.sqlSession.getConnection().prepareStatement(insertSql);
					int resultSet = preparedStatement.executeUpdate();	
					if(resultSet>0){
						result++;
					}
				}
			}
			if(!this.sqlSession.getConnection().getAutoCommit()){
				this.sqlSession.getConnection().commit();
			}
		} catch (Exception e) {
			this.sqlSession.getConnection().rollback();
			ExceptionManage.dispose(e, getClass());
		}finally{
			this.sqlSession.getConnection().setAutoCommit(true);
		}
		return result;
	}
	
	/**
	 * 查看 历史告警表多少条记录
	 * 返回   result 条
	 * @param list 符合条件的网元id
	 * @param filter 过滤条件
	 * @return
	 * @throws Exception
	 */
	public int selectCount(CurrentAlarmFilter filter, List<Integer> siteIdList)throws Exception{
		int result = 0;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("filter", filter);
		map.put("siteIdList", siteIdList);
		if(filter != null && filter.getObjectType() != null){
			map.put("type", filter.getObjectType().getValue());
		}else{
			map.put("type", 0);
		}
		result=this.mapper.selectAlarmCount(map);
		return result;
	}
	
	/**
	 * 根据告警主键集合，批量删除
	 * 
	 * @param idList
	 * @return
	 * @throws Exception
	 */
	public int delete(List<Integer> idList) throws Exception {
		int result = 0;
		try {
			if (idList == null || idList.size() == 0) {
				return 0;
			}
			result = this.mapper.deleteByIds(idList);
			this.sqlSession.commit();
		} catch (Exception e) {
			throw e;
		}
		return result;
	}
	
	public List<HisAlarmInfo> select(HisAlarmInfo condition) throws Exception {
		List<HisAlarmInfo> hisInfoList = null;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("alarm", condition);
			map.put("type", condition.getObjectType() == null ? 0:condition.getObjectType().getValue());
			hisInfoList = this.mapper.queryByCondition(map);
			if (hisInfoList != null && hisInfoList.size() > 0) {
				this.wrapHisAlarmInfo(hisInfoList);
			}
		} catch (Exception e) {
			throw e;
		}
		return hisInfoList;
	}

	public List<HisAlarmInfo> selectByTime(String startTime, String endTime) throws Exception {
		List<HisAlarmInfo> hisInfoList = null;
		try {
			hisInfoList = this.mapper.queryByTime(startTime, endTime);
			this.wrapHisAlarmInfo(hisInfoList);
			this.db2Bean(hisInfoList);
		} catch (Exception e) {
			throw e;
		}
		return hisInfoList;
	}

	public int deleteHisAlarmInfo(HisAlarmInfo hisAlarmInfo) throws Exception {
		if (hisAlarmInfo == null) {
			throw new Exception("hisAlarmInfo is null");
		}
		int result = 0;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("hisAlarm", hisAlarmInfo);
			map.put("type", hisAlarmInfo.getObjectType() == null ? 0:hisAlarmInfo.getObjectType().getValue());
			result = this.mapper.deleteHisAlarmInfo(map);
			this.sqlSession.commit();
		} catch (Exception e) {
			throw e;
		}
		return result;
	}

	public List<HisAlarmInfo> selectHisAlarmByCond(String raiseTimeDuration,String clearTimeDuration, List<String> siteIdList,
			List<Integer> alarmSeverityList, List<Integer> alarmTypeList, String[] probableCauseArray) {
		List<HisAlarmInfo> hisAlarmInfos = null;
		try {
			List<String> probableCauseList = new ArrayList<String>();
			if(probableCauseArray != null && probableCauseArray.length > 0){
				for (int i = 0; i < probableCauseArray.length; i++) {
					probableCauseList.add(probableCauseArray[i]);
				}
			}
			List<String> raiseTimeList = new ArrayList<String>();
			if(raiseTimeDuration != null){
				for (int i = 0; i < raiseTimeDuration.split("\\|").length; i++) {
					raiseTimeList.add(raiseTimeDuration.split("\\|")[i]);
				}
			}
			List<String> clearTimeList = new ArrayList<String>();
			if(clearTimeDuration != null){
				for (int i = 0; i < clearTimeDuration.split("\\|").length; i++) {
					clearTimeList.add(clearTimeDuration.split("\\|")[i]);
				}
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("raiseTimeList", raiseTimeList);
			if(raiseTimeList.size() > 0){
				map.put("raiseTime1", raiseTimeList.get(0));
				map.put("raiseTime2", raiseTimeList.get(1));
			}
			map.put("clearTimeList", clearTimeList);
			if(clearTimeList.size() > 0){
				map.put("clearTime1", clearTimeList.get(0));
				map.put("clearTime2", clearTimeList.get(1));
			}
			map.put("siteIdList", siteIdList);
			map.put("alarmSeverityList", alarmSeverityList);
			map.put("alarmTypeList", alarmTypeList);
			map.put("probableCauseList", probableCauseList);
			hisAlarmInfos = this.mapper.selectHisAlarmByCond(map);
			if(hisAlarmInfos == null){
				hisAlarmInfos = new ArrayList<HisAlarmInfo>();
			}
			this.wrapHisAlarmInfo(hisAlarmInfos);
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return hisAlarmInfos;
	}
	
	public int saveNorth(HisAlarmInfo hisInfo) throws Exception {
		if (hisInfo == null) {
			throw new Exception("taskInfo is null");
		}
		int resultcesId = 0;
		try {
			resultcesId = this.mapper.saveNorth(hisInfo);
			this.sqlSession.commit();
		} catch (Exception e) {
			throw e;
		}
		return resultcesId;
	}
	
	public List<Map<String,Object>> sysNorthAlarm(Map<String,String> param){
		List<Map<String,Object>> list = this.mapper.sysNorthAlarm(param);
		return list;
	}
	
	public List<Map<String,Object>> sysNorthAlarmIndex(Integer index){
		List<Map<String,Object>> list = this.mapper.sysNorthAlarmIndex(index);
		return list;
	}
	
	/**
	 * 北向流水，告警清除时，查询当前告警id
	 * @param hisInfo
	 * @return
	 */
	public List<HisAlarmInfo>  queryHisNorth(HisAlarmInfo hisInfo){
		return this.mapper.queryHisNorth(hisInfo);
	}
}
