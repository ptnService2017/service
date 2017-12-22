package com.nms.db.dao.ptn.path.eth;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.nms.db.bean.ptn.path.eth.ElanInfo;

public interface ElanInfoMapper {
	public List<ElanInfo> filterSelect(ElanInfo elanInfo);

	public List<ElanInfo> selectById(@Param("serviceId")int serviceId);

	public List<ElanInfo> queryNodeBySite(ElanInfo condition);

	public List<ElanInfo> queryByPwId(@Param("pwIdList")List<Integer> pwIdList);

	public Integer selectMaxServiceId();

	public int delete(int serviceId);

	public int insert(ElanInfo elanInfo);

	public int queryByName(String elanName);

	public List<ElanInfo> selectBySiteId(int siteId);
	
	/**
	 * 通过条件查询
	 * @param elaninfo
	 */
	public List<ElanInfo> queryElan(ElanInfo elanInfo);
	
	public int query_name(@Param("afterName")String afterName, @Param("beforeName")String beforeName);

	public int query_nameBySingle(@Param("afterName")String afterName, @Param("beforeName")String beforeName, @Param("siteId")int siteId);
	
	public List<ElanInfo> queryAll();

	public List<ElanInfo> selectBySiteAndisSingle(int siteId, int i);

	public List<ElanInfo> queryByAcIdAndSiteIdCondition(int siteId);

	public List<ElanInfo> select(ElanInfo elanInfo);

	public List<ElanInfo> queryById(int serviceId);

	public void updateStatus(int siteId, int status);

	public void updateStatusByServiceId(Map<String, Object> map);

	public List<ElanInfo> query_synchro(ElanInfo elanInfo);

	public List<ElanInfo> isRelatedPW(int pwId);

	public List<ElanInfo> isRelatedAcByEline(int acId);

	public List<ElanInfo> isRelatedAc();

	public void deleteById(int id);

	public void update(ElanInfo info);

	public List<Map<String, Object>> selectAll_ETHNorth();

	public List<Map<String, Object>> selectAll_ESINorth();
}