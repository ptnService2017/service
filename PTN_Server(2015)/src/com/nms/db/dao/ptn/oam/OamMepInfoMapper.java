package com.nms.db.dao.ptn.oam;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nms.db.bean.ptn.oam.OamMepInfo;

public interface OamMepInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OamMepInfo record);

    int insertSelective(OamMepInfo record);

    OamMepInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OamMepInfo record);

    OamMepInfo existByObjIdAndType(OamMepInfo record);
    
    public OamMepInfo queryMepByCondition(OamMepInfo oamMep);

	/**
	 *  根据主键id修改objId
	 */
	public void updateMepById(OamMepInfo mep);
	
	/**
	 * 条件删除
	 * @param mep
	 */
	public void delete(OamMepInfo mep);
	
	/**
	 * 根据网元id，类型查询所有mep
	 * @return
	 */
	public List<OamMepInfo> queryMepByTypeAndSiteId(OamMepInfo mep);
	

	List<OamMepInfo> queryMepByMegId(String megIcc, String megUmc);

	/**
	 * 更新Objid
	 * @param map
	 */
	public void update_mep_objid(HashMap<String, Object> map);

	/**
	 * 根据objectid和objecttype验证是否存在
	 * @param objId
	 * @param objType
	 * @return
	 */
	public List<OamMepInfo> queryMepByObjIdAndType(int objId, String objType);

	/**
	 * lck查询
	 * @param mep
	 * @return
	 */
	List<OamMepInfo> selectByOamMepInfo(OamMepInfo mep);
	
	/**
	 * 根据对象类型和业务id查找oamMepInfo
	 */
	public List<OamMepInfo> queryMepByServiceId(@Param("oamMepInfo")OamMepInfo oamMepInfo);

	/**
	 * 搜索时，更新oam
	 * @param newTunnelId
	 * @param tid
	 */
	public void updateObjId(int newTunnelId, String tid,String type);

	public void update(OamMepInfo oamMep);
	
	public List<OamMepInfo> queryMepByType(@Param("oamMepInfo")OamMepInfo oamMepInfo);
	
	public int deleteBySiteId(@Param("siteId")int siteId);
	
}