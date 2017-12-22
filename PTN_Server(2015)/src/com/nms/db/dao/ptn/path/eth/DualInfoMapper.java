package com.nms.db.dao.ptn.path.eth;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nms.db.bean.ptn.path.eth.DualInfo;

public interface DualInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DualInfo record);

    int insertSelective(DualInfo record);

    DualInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DualInfo record);

    int updateByPrimaryKey(DualInfo record);

	List<DualInfo> queryByCondition(DualInfo dualInfo);

	List<DualInfo> queryAll(@Param("label")int label, @Param("siteId")int siteId);

	int query_name(@Param("afterName")String afterName, @Param("beforeName")String beforeName);

	List<DualInfo> queryByAcIdAndSiteIdCondition(int acId, int siteId);

	List<DualInfo> queryBypwIDs(@Param("pwIdList")List<Integer> pwIdList);

	List<DualInfo> queryBySiteId(int siteId,int eServiceTypeValue);

	List<DualInfo> queryBySiteIdAndBusinessId(int siteId, int businessId);

	Integer selectMaxServiceId();

	List<DualInfo> queryBySiteIdAndPWId(int mainPwId, int standPwId, int siteId);

	DualInfo queryById(int id);
	
	List<DualInfo> queryByIDs(@Param("IdList")List<Integer> IdList);
}