package com.nms.db.dao.ptn.path.eth;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.nms.db.bean.ptn.path.eth.EtreeInfo;

public interface EtreeInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EtreeInfo record);

    int insertSelective(EtreeInfo record);

    EtreeInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EtreeInfo record);

    int updateByPrimaryKey(EtreeInfo record);

	public List<EtreeInfo> selectBySiteAndisSingle(@Param("siteId")int siteId, @Param("isSingle")int isSingle);

	/**
	 * 验证Pw是否被某业务使用(不仅仅是eline)
	 * @param pwId
	 * @return
	 */
	List<EtreeInfo> queryByPwId(int pwId);
	
	public List<EtreeInfo> queryAllEtree(@Param("serviceType")int serviceType,@Param("name")String name);
	
	public List<EtreeInfo> queryAllEtrees(@Param("serviceType")int serviceType,@Param("name")String name,@Param("branchsite")int branchsite);

	public List<EtreeInfo> filterSelect(EtreeInfo etreeInfo);

	public List<EtreeInfo> queryByServiceId(int serviceId);

	public List<EtreeInfo> queryByRoot(int siteId);

	public List<EtreeInfo> queryByPwIdCondition(@Param("pwIdList")List<Integer> pwIdList);

	public Integer selectMaxServiceId();

	public void deleteByID(int id);

	public List<EtreeInfo> queryAllBySite(int siteId);
	
	public List<EtreeInfo> queryAll();

	public int query_name(@Param("afterName")String afterName, @Param("beforeName")String beforeName);

	public int query_nameBySingle(@Param("afterName")String afterName, @Param("beforeName")String beforeName, @Param("siteId")int siteId);

	public List<EtreeInfo> queryByAcIdAndSiteIdCondition(int siteId);

	public List<EtreeInfo> select(EtreeInfo etreeInfo);

	public List<EtreeInfo> queryById(int id);

	public void updateStatus(int siteId, int status);

	public List<EtreeInfo> query_synchro(EtreeInfo etreeInfo);

	public List<EtreeInfo> isRelatedAcByEline(int acId);

	public List<EtreeInfo> isRelatedAc();

	public void deleteById(int id);

	public void update(EtreeInfo etreeInfo);

	public List<EtreeInfo> isRelatedPW(int pwId);

	public int deleteByServiceId(int serviceId);

	public void updateStatusByServiceId(Map<String, Object> map);
}