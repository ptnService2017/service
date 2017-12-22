package com.nms.db.dao.system.loginlog;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nms.db.bean.system.loginlog.LoginLog;

public interface LoginLogMapper {
    
   public int insertLoginLog(LoginLog loginLog);
   
   public List<LoginLog> findState(@Param("loginLog")LoginLog loginLog, @Param("maxId")Integer maxId);
   
   public int updateExitLoginLog(LoginLog loginLog);
   
   /**
	 * 查看登陆日志
	 */
	public List<LoginLog> queryByCondition(@Param("loginLog")LoginLog loginLog,@Param("isSelect")Integer isSelect);

	/**
	 * 删除网元的失败的状态
	 * @param resultSet
	 * @return
	 */
	public int deleteFailState(@Param("loginLog")LoginLog loginLog);
	
	/**
	 * 根据角色ID修改在线用户的离开时间 达到强制下线的目的
	 * @param roleId
	 *            角色主键
	 * @throws Exception
	 */
	public void updateByRole(int roleId);
	
	/*
	 * *在线查询*
	 */
	public List<LoginLog> selectOnLine();
	
	/**
	 * 查询 表login_log user_id和logintTime，为查询条件
	 * 
	 * @param loginTime
	 * @param connection
	 *            连接
	 * @return
	 * @throws Exception
	 */
	public List<LoginLog> findLoginTime(LoginLog loginlog);
	
	public int deleteByIdsAndTime(@Param("ids")List<Integer> idList,@Param("time")String time);
	
	/**
	 * 数据库中有多少条记录
	 * @return
	 */
	public int selectLogCount();
	
	/**
	 * 根据主键集合，批量删除历史性能数据
	 * 
	 * @param idList
	 * @param connection
	 * @return
	 * @throws Exception
	 */
	public int deleteByIds(@Param("idList")List<Integer> idList);
	
	public int deleteAll();
}