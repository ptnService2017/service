package com.nms.db.dao.ptn;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nms.db.bean.ptn.Businessid;


public interface BusinessidMapper {
	public List<Businessid> query(Businessid condition);

	public void update(Businessid condition);
	
	public int insert(@Param("siteId")Integer siteId,@Param("type")String type,@Param("idValue")Integer idValue,@Param("idStatus")Integer idstatus);

	/**
	 * 通过网元id删除
	 * @param siteId
	 */
	void delete(int siteId);

	/**
	 * 查询可用id
	 * @param siteId
	 * @param type
	 * @param i
	 * @return
	 */
	List<Businessid> queryUseBySiteIDType(int siteId, String type, int i);
	
	/**
	 * 查询指定business
	 * @param idvalue
	 * @param siteid
	 * @param type
	 * @return
	 */
	Businessid queryByIdValueSiteIdtype(int idvalue, int siteid, String type);

	/**
	 * 初始化网元业务id
	 * @param siteId
	 * @param i
	 */
	void initializtionSite(int siteId);

	/**
	 * 根据网元id，类型查询可用id
	 * @param siteId
	 * @param type
	 * @return
	 */
	List<Businessid> queryList(int siteId, String type);

	/**
	 * 修改状态
	 * @param businessId
	 */
	void updateBusinessid(Businessid businessId);

	/**
	 * 条件查询
	 * @param bId
	 * @return
	 */
	List<Businessid> queryByCondition(Businessid bId);

	/**
	 * 条件查询
	 * @param hashMap
	 * @return
	 */
	List<Businessid> queryListForCondition(HashMap<String, Object> hashMap);

	List<Integer> queryBySite(HashMap<String, Object> hashMap);

	public List<Businessid> queryBySiteId(int siteId, String type);
	
	public void updateBusinessidForSite(Businessid businessId); 

	public int queryUsedIdCount(int siteId);

}