package com.nms.db.dao.ptn.qos;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nms.db.bean.ptn.qos.QosMappingMode;

public interface QosMappingModeMapper {
    public List<QosMappingMode> queryByCondition(QosMappingMode qosMappingMode);
    
    public int insert(@Param("qos")QosMappingMode qos);
    
    public int deleteBySiteId(int siteId);

	public void delete(int id);

	public void update(QosMappingMode mappingMode);
}