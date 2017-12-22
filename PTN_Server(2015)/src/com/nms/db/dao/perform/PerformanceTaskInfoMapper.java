package com.nms.db.dao.perform;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.nms.db.bean.perform.PerformanceTaskInfo;


public interface PerformanceTaskInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PerformanceTaskInfo record);

    int insertSelective(PerformanceTaskInfo record);

    PerformanceTaskInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PerformanceTaskInfo record);

    int updateByPrimaryKey(PerformanceTaskInfo record);

	List<PerformanceTaskInfo> queryByFilter(Map<String, Object> map);

	List<PerformanceTaskInfo> queryByCondition(@Param("condition")PerformanceTaskInfo condition, @Param("monitorCycle")Integer monitorCycle,@Param("runstates")Integer runstates,
											   @Param("objectType")Integer objectType, @Param("siteId")Integer siteId);

	int query_name(@Param("afterName")String afterName, @Param("beforeName")String beforeName);

	List<PerformanceTaskInfo> queryByIdList(@Param("idList")List<Integer> idList);

	int deleteByids(@Param("idList")List<Integer> idList);
	
	public int deleteBySiteId(@Param("siteId")int siteId);

}