package com.nms.db.dao.system;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nms.db.bean.system.OperationLog;



public interface OperationLogMapper {

    public int insert(OperationLog operationLog);
    
    /**
	 * 根据某一时间，删除此时间点以前的数据
	 * @param removeTime
	 * @return
	 */
	public int deleteByTime(String removeTime);
	
	/**
	 * 根据主键集合，批量删除历史性能数据 
	 * @param idList
	 * @return
	 */
	public int delete(@Param("idList")List<Integer> idList);
	
	/**
	 * 数据库中有多少条记录
	 * @param connection
	 * @return
	 * @throws Exception
	 */
	public int selectOperationLogCount();
	
	/**
	 * operation_log表 更新表中信息
	 * @param operationLog
	 * @return
	 */
	public int updateOperationLog(OperationLog operationLog);
	
	/**
	 * 根据不同条件
	 * 查询 操作日志记录
	 * @param operationLog
	 * @return
	 */
	public List<OperationLog> select(@Param("operationLog")OperationLog operationLog,@Param("isSelect")Integer isSelect);

	/**
	 * 根据删除时间获取需要删除的log的id集合
	 * @param removeTime
	 * @return
	 */
	public List<Integer> selectByTime(String removeTime);

	/**
	 * 根据网元去查询log的id集合
	 * @param siteId
	 * @return
	 */
	public List<Integer> selectBySiteId(int siteId);
}