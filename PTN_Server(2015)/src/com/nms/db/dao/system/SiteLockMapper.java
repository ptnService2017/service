package com.nms.db.dao.system;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nms.db.bean.system.SiteLock;



public interface SiteLockMapper {
    int deleteByPrimaryKey(Integer id);

    public int insert(SiteLock record);

    int insertSelective(SiteLock record);

    SiteLock selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SiteLock record);

    int updateByPrimaryKey(SiteLock record);
    
    /**
	 * 查询网元被锁住的记录
	 * @return
	 */
	public List<SiteLock> queryLockBySiteId(@Param("siteId")int siteId);
	
	/**
	 * 修改锁住状态
	 * @param siteLock
	 * @param connection
	 * @return
	 * @throws Exception 
	 */
	public int updateSiteLock(@Param("clearUser")String clearUser,@Param("siteId")int siteId,@Param("date")String date);
}