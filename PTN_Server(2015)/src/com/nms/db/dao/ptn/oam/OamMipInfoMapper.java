package com.nms.db.dao.ptn.oam;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nms.db.bean.ptn.oam.OamMipInfo;

public interface OamMipInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OamMipInfo record);

    int insertSelective(OamMipInfo record);

    OamMipInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OamMipInfo record);

    int updateByPrimaryKey(OamMipInfo record);
    
    public OamMipInfo queryMipByCondition(OamMipInfo oamMipInfo);
    
    public void delete(OamMipInfo oamMipInfo);
    
    /**
     * 根据对象类型和对象id查找oamMipInfo
     * @param oamMipInfo
     * @return
     */
    public List<OamMipInfo> queryMipByConditionForList(OamMipInfo oamMipInfo);

    /**
     * 搜索时，更新oam
     * @param newTunnelId
     * @param tid
     * @param string
     */
	void updateObjId(int newTunnelId, String tid, String string);
	
	public int deleteBySiteId(@Param("siteId")int siteId);
}