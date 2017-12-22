package com.nms.db.dao.system;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nms.db.bean.alarm.HisAlarmInfo;
import com.nms.db.bean.perform.HisPerformanceInfo;
import com.nms.db.bean.system.OperationLog;
import com.nms.db.bean.system.loginlog.LoginLog;

public interface TranferInfoMapper {
      
    public List<String>  tableColumnType(@Param("tableName")String tableName);
    public List<String>  tableColumnName(@Param("tableName")String tableName);
    public List<HisAlarmInfo>  selectHisAlarmInfo(@Param("count")Integer count);
    public List<HisPerformanceInfo>  selectHisPerformanceInfo(@Param("count")Integer count);
    public List<OperationLog>  selectOperationLog(@Param("count")Integer count);
    public List<LoginLog>  selectLoginLog(@Param("count")Integer count);
    public List<HisAlarmInfo>  getTableBeans(@Param("sql")String sql);
}