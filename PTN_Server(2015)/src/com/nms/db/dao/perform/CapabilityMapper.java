package com.nms.db.dao.perform;

import java.util.List;

import com.nms.db.bean.perform.Capability;


public interface CapabilityMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Capability record);

    int insertSelective(Capability record);

    Capability selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Capability record);

    int updateByPrimaryKey(Capability record);

	List<Capability> queryByCondition(Capability capability);
	
	public List<Capability> queryByCapaName(Capability capability);
}