package com.nms.db.dao.alarm;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.nms.db.bean.alarm.CurrentAlarmInfo;
import com.nms.db.bean.alarm.TCAAlarm;
import com.nms.db.bean.equipment.slot.SlotInst;


public interface CurrentAlarmInfoMapper {
	public List<CurrentAlarmInfo> queryByCondition(Map<String, Object> map);

	public int update(CurrentAlarmInfo curr);

	/**
	 * 搜索时，更新告警
	 * @param newTunnelId
	 * @param tid
	 * @param i
	 */
	public void updateObjectid(int newTunnelId, String tid, int i);
	
	/**
	 * 关联查询 关联site和warninglevel表
	 * @param map
	 * @return
	 */
	public List<CurrentAlarmInfo> query_join(Map<String, Object> map);
	
	/**
	 * 通过槽位查询当前告警
	 * @param slotInst	槽位
	 */
	public List<CurrentAlarmInfo> queryByClent();

	public List<Integer> querySiteid();

	public Integer queryMaxId();

	public List<Integer> selectAllIdList(Map<String, Object> map);

	public List<CurrentAlarmInfo> selectByPage(Map<String, Object> map);

	public int delete(int curId);

	public int insert(CurrentAlarmInfo info);

	public List<CurrentAlarmInfo> queryCurrBySites(@Param("siteIdList")List<Integer> siteIdList);

	public int deleteCurrentAlarmInfo(Map<String, Object> map);

	public List<CurrentAlarmInfo> queryByClentAlarm(int alarmCode, int level);

	public List<CurrentAlarmInfo> queryByClentAlarmByObjName(int alarmCode, int level, String objectName);

	public List<CurrentAlarmInfo> queryByClentLevel(int level);
	
	public List<CurrentAlarmInfo> query_type_id(@Param("objectType")int objectType,@Param("objectIdList")List<Integer> objectIdList,@Param("siteId")int siteId);
	
	/**
	 * 查询tca告警
	 * @param tcaAlarm
	 * @return
	 */
	public List<TCAAlarm> queryTCAAlarm(TCAAlarm tcaAlarm);

	public List<CurrentAlarmInfo> queryByTime_join(String startTime, String endTime);

	public List<CurrentAlarmInfo> queryBySlot(SlotInst slotInst);

	public List<CurrentAlarmInfo> selectCurrentAlarmByCond(Map<String, Object> map);
    
	public void deleteBySiteId(int siteInstId);
}