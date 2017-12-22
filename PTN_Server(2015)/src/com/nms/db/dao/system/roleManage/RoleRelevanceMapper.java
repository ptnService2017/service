package com.nms.db.dao.system.roleManage;

import java.util.List;

import com.nms.db.bean.system.roleManage.RoleInfo;
import com.nms.db.bean.system.roleManage.RoleRelevance;



public interface RoleRelevanceMapper {
    int deleteByPrimaryKey(Integer id);

    public int insert(RoleRelevance record);

    int insertSelective(RoleRelevance record);

    RoleRelevance selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RoleRelevance record);

    int updateByPrimaryKey(RoleRelevance record);
    
    /**
	 * 删除
	 * @param  （roleInfo）角色Id  
	 * @return
	 */
	public int delete(RoleInfo roleInfo);
	
	/**
	 * 查询权限表
	 * @param roleInfo
	 * @param connection
	 * @return
	 * @throws Exception
	 */
	public List<RoleRelevance> select(RoleRelevance roleRelevance);
}