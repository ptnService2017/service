package com.nms.db.dao.system;

import com.nms.ui.ptn.performance.model.PerformanceRAMInfo;

public interface PerformanceRamInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PerformanceRAMInfo record);

    int insertSelective(PerformanceRAMInfo record);

    PerformanceRAMInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PerformanceRAMInfo record);

    int updateByPrimaryKey(PerformanceRAMInfo record);

	PerformanceRAMInfo queryByCondition(String userName);
}