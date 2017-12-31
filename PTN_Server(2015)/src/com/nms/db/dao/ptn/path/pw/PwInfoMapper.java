﻿package com.nms.db.dao.ptn.path.pw;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.nms.db.bean.ptn.path.pw.PwInfo;

public interface PwInfoMapper {
	public List<PwInfo> queryByCondition(Map<String, Object> map);
    
	/**
     * 搜索时修改tunnelID
     * @param newTunnelId
     * @param tid
     */
	public void updateTunnelID(int newTunnelId, String tid);

	public List<PwInfo> queryByPwTunnelIdCondition(@Param("tunnelIdList")List<Integer> tunnelIdList);

	public List<PwInfo> queryFilte(Map<String, Object> map);

	public List<PwInfo> selectByPWId(PwInfo pwInfo);

	public List<PwInfo> queryPwBySiteIdAndIsSingle(PwInfo condition);

	public List<PwInfo> queryByPwidCondition_notjoin(PwInfo pwinfo);

	public List<PwInfo> findPwByIds(@Param("pwIdList")List<Integer> pwIdList);

	public List<PwInfo> queryNode(Map<String, Object> map);

	public int doSearch(String newPwName, int pwId, int pwId2);
	
	public void updateServiceByPwId(Map<String, Object> conditionMap);

	public void updatePwBuffByPwId(Map<String, Object> conditionMap);

	public void updateQosRelevanceByPwId(Map<String, Object> conditionMap);

	public void updateOamMepByPwId(Map<String, Object> conditionMap);

	public void updateCurrAlarmByPwId(Map<String, Object> conditionMap);

	public void updateHisAlarmByPwId(Map<String, Object> conditionMap);

	public void updatePerformanceByPwId(Map<String, Object> conditionMap);

	public void deleteByPwIdList(@Param("pwIdList")List<Integer> pwIdList);
	
	/**
	 * 根据tunnelId查询
	 * @param tunnelId
	 * @param connection
	 * @return
	 */
	public List<PwInfo> queryByTunnelId(@Param("tunnelId")int tunnelId);

	public List<PwInfo> selectSamePortByTunnelId(int tunnelId, int siteId);

	public List<PwInfo> query_synchro(int siteid, int pwServiceId, int pwtype);

	public int insert(PwInfo pw);

	public int update(PwInfo pw);

	public List<PwInfo> queryBySiteId(int siteId);
	/**
	 * 根据名称查询pwId
	 */
	public int queryPwIdByName(String pwName);

	public void setUser(int pwId, int id, int serviceType);
	
	public List<PwInfo> queryBySiteAndServiceId(@Param("siteId")int siteId,@Param("serviceId")int serviceId);
	
	public List<PwInfo> queryNotEth(PwInfo pwinfoSel);

	public int query_name(@Param("afterName")String afterName, @Param("beforeName")String beforeName);

	public int query_nameBySingle(@Param("afterName")String afterName, @Param("beforeName")String beforeName, @Param("siteId")int siteId);

	public void delete(int pwId);

	public void updateStatusBySiteId(int siteId, int status);

	public void updateStatus(Map<String, Object> map);

	public List<PwInfo> queryAll();

	public List<PwInfo> selectServiceIdsByPwIds(String pwIdList);

	public List<PwInfo> selectAll_North();

	public List<PwInfo> selectPage(@Param("siteId")Integer siteId, @Param("index")Integer index, @Param("size")Integer size);

	public PwInfo selectById(@Param("pwId")Integer pwId);

	public Integer updateLableById(@Param("pwId")Integer pwId, @Param("inlable")Integer inlable, @Param("outlable")Integer outLable);


}