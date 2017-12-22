package com.nms.model.alarm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.nms.db.bean.alarm.CurrentAlarmInfo;
import com.nms.db.bean.alarm.HisAlarmInfo;
import com.nms.db.bean.alarm.WarningLevel;
import com.nms.db.bean.equipment.shelf.SiteInst;
import com.nms.db.dao.alarm.CurrentAlarmInfoMapper;
import com.nms.db.dao.alarm.HisAlarmInfoMapper;
import com.nms.db.dao.alarm.WarningLevelMapper;
import com.nms.db.dao.equipment.shelf.SiteInstMapper;
import com.nms.model.util.ObjectService_Mybatis;
import com.nms.ui.manager.ExceptionManage;

public class WarningLevelService_MB extends ObjectService_Mybatis {
	private WarningLevelMapper mapper = null;
	
	public void setPtnuser(String ptnuser) {
		super.ptnuser = ptnuser;
	}

	public void setSqlSession(SqlSession sqlSession) {
		super.sqlSession = sqlSession;
	}
	
	public void setMapper(WarningLevelMapper mapper) {
		this.mapper = mapper;
	}
	
	/**
	 * 根据条件查询
	 * @param warninglevel  查询条件
	 * @return List<WarningLevel> 集合
	 * @throws Exception
	 */
	public List<WarningLevel> select(WarningLevel warninglevel) throws Exception {
		List<WarningLevel> warninglevelList = null;
		try {
			warninglevelList= new ArrayList<WarningLevel>();
			warninglevelList = this.mapper.queryByCondition(warninglevel);
		} catch (Exception e) {
			throw e;
		}
		return warninglevelList;
	}
	
	/**
	 * 查询全部
	 * @return  List<WarningLevel> 集合
	 * @throws Exception
	 */
	public List<WarningLevel> select() throws Exception {
		List<WarningLevel> warninglevelList = null;
		try {
			WarningLevel warningLevel=new WarningLevel();
			warninglevelList = this.mapper.queryByCondition(warningLevel);
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return warninglevelList;
	}
	
	/**
	 * 通过主键删除warningLevel对象
	 * @param id  主键
	 * @return 删除的记录数
	 * @throws Exception
	 */
	public int delete(int id) throws Exception {
		int result = 0;
		try {
			result = this.mapper.delete(id);
			this.sqlSession.commit();
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return result;
	}
	
	/**
	 * 新增或修改warningLevel对象，通过warningLevel.getId()来判断是修改还是新增
	 * @param warningLevel 实体
	 * @return 执行成功插入的记录数
	 * @throws Exception
	 */
	public int saveOrUpdate(WarningLevel warningLevel) throws Exception {
		if (warningLevel == null) {
			throw new Exception("warningLevel is null");
		}
		int result = 0;
		CurrentAlarmInfoMapper curAlarmMapper = null;
		HisAlarmInfoMapper hisAlarmMapper = null;
		SiteInstMapper siteMapper = null;
		SiteInst site = null;
		CurrentAlarmInfo currAlarm = null;
		List<CurrentAlarmInfo> currentAlarmList = null;
		HisAlarmInfo hisAlarm = null;
		List<HisAlarmInfo> hisAlarmList = null;
		WarningLevel contion = null;
		Map<Integer,Integer> siteMap = new HashMap<Integer,Integer>();
		List<SiteInst> siteList = null;
		try {
			if (warningLevel.getId() == 0) {
				result = this.mapper.insert(warningLevel);
			} else {
				//修改告警级别的同时,也把当前告警和历史告警修改成相同的级别
				curAlarmMapper = this.sqlSession.getMapper(CurrentAlarmInfoMapper.class);
				siteMapper = this.sqlSession.getMapper(SiteInstMapper.class);
				hisAlarmMapper = this.sqlSession.getMapper(HisAlarmInfoMapper.class);
				//获取所有网元,放入map中
				site = new SiteInst();
				siteList = siteMapper.queryByCondition(site);
				if(siteList != null && siteList.size() > 0){
					for (SiteInst siteInst : siteList) {
						siteMap.put(siteInst.getSite_Inst_Id(), siteInst.getManufacturer());
					}
				}
				//修改当前告警的级别
				contion = new WarningLevel();
				contion.setId(warningLevel.getId());
				contion = this.mapper.queryByCondition(contion).get(0);
				currAlarm = new CurrentAlarmInfo();
				currAlarm.setAlarmCode(contion.getWarningcode());
				currAlarm.setAlarmLevel(contion.getWarninglevel());
				Map<String, Object> currentMap = new HashMap<String, Object>();
				currentMap.put("alarm", currAlarm);
				currentMap.put("type", 0);
				currentAlarmList = curAlarmMapper.queryByCondition(currentMap);
				if(currentAlarmList != null && currentAlarmList.size() > 0){
					for (CurrentAlarmInfo curr : currentAlarmList) {
						curr.setWarningLevel_temp(warningLevel.getWarninglevel_temp());
						if(siteMap.get(curr.getSiteId()) == 0 && warningLevel.getManufacturer() == 1){
							//表示武汉
							curAlarmMapper.update(curr);
						}
						if(siteMap.get(curr.getSiteId()) == 1 && warningLevel.getManufacturer() == 2){
							//表示晨晓
							curAlarmMapper.update(curr);
						}
					}
				}
				//修改历史告警的级别
				hisAlarm = new HisAlarmInfo();
				hisAlarm.setAlarmCode(contion.getWarningcode());
				hisAlarm.setAlarmLevel(contion.getWarninglevel());
				Map<String, Object> hisMap = new HashMap<String, Object>();
				hisMap.put("alarm", hisAlarm);
				hisMap.put("type", 0);
				hisAlarmList = hisAlarmMapper.queryByCondition(hisMap);
				if(hisAlarmList != null && hisAlarmList.size() > 0){
					for (HisAlarmInfo his : hisAlarmList) {
						his.setWarningLevel_temp(warningLevel.getWarninglevel_temp());
						if(siteMap.get(his.getSiteId()) == 0 && warningLevel.getManufacturer() == 1){
							//表示武汉
							hisAlarmMapper.update(his);
						}
						if(siteMap.get(his.getSiteId()) == 1 && warningLevel.getManufacturer() == 2){
							//表示晨晓
							hisAlarmMapper.update(his);
						}
					}
				}
				result = this.mapper.update(warningLevel);
			}
			this.sqlSession.commit();
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return result;
	}
}
