package com.nms.model.ptn.path.eth;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.session.SqlSession;

import com.nms.db.bean.equipment.shelf.SiteInst;
import com.nms.db.bean.ptn.Businessid;
import com.nms.db.bean.ptn.path.eth.EtreeInfo;
import com.nms.db.bean.ptn.path.pw.PwInfo;
import com.nms.db.bean.ptn.path.pw.PwNniInfo;
import com.nms.db.bean.ptn.port.AcPortInfo;
import com.nms.db.dao.ptn.BusinessidMapper;
import com.nms.db.dao.ptn.path.eth.EtreeInfoMapper;
import com.nms.db.enums.EActionType;
import com.nms.db.enums.EServiceType;
import com.nms.model.equipment.shlef.SiteService_MB;
import com.nms.model.ptn.path.pw.PwInfoService_MB;
import com.nms.model.ptn.path.pw.PwNniInfoService_MB;
import com.nms.model.ptn.port.AcPortInfoService_MB;
import com.nms.model.util.ObjectService_Mybatis;
import com.nms.model.util.Services;
import com.nms.service.impl.util.TypeAndActionUtil;
import com.nms.ui.manager.BusinessIdException;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.DateUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.ResourceUtil;
import com.nms.ui.manager.UiUtil;
import com.nms.ui.manager.keys.StringKeysTip;

public class EtreeInfoService_MB extends ObjectService_Mybatis{
	
	public void setPtnuser(String ptnuser) {
		super.ptnuser = ptnuser;
	}

	public void setSqlSession(SqlSession sqlSession) {
		super.sqlSession = sqlSession;
	}
	
	private EtreeInfoMapper mapper;

	public EtreeInfoMapper getEtreeInfoMapper() {
		return mapper;
	}

	public void setEtreeInfoMapper(EtreeInfoMapper EtreeInfoMapper) {
		this.mapper = EtreeInfoMapper;
	}

	/**
	 * @throws Exception
	 */
	public Map<Integer, List<EtreeInfo>> selectBySiteAndisSingle(int siteId,int isSingle) throws Exception {
		return this.convertListToMap(this.mapper.selectBySiteAndisSingle(siteId, isSingle));
	}
	
	/**
	 * 把所有etree按组转为map 
	 * @param etreeInfoList
	 * @return key为组id  value为此组下的etree对象
	 * @throws Exception
	 */
	private Map<Integer, List<EtreeInfo>> convertListToMap(List<EtreeInfo> etreeInfoList) {
		Map<Integer, List<EtreeInfo>> etreeInfoMap = new HashMap<Integer, List<EtreeInfo>>();
		try {
			if(null!=etreeInfoList && etreeInfoList.size()>0){
				for (EtreeInfo etreeInfo : etreeInfoList) {
					int serviceId = etreeInfo.getServiceId();
					if (etreeInfoMap.get(serviceId) == null) {
						List<EtreeInfo> etreeInfoList_map = new ArrayList<EtreeInfo>();
						etreeInfoMap.put(serviceId, etreeInfoList_map);
					}
					etreeInfoMap.get(serviceId).add(etreeInfo);
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return etreeInfoMap;
	}
	
	/**
	 * 查询所有业务
	 * 
	 * @param serviceId
	 * @return
	 */
	public List<EtreeInfo> selectAll(int serviceType,String name) {
		List<EtreeInfo> etreeInfoList = null;
		try {
		   etreeInfoList = new ArrayList<EtreeInfo>();
		   etreeInfoList = this.mapper.queryAllEtree(serviceType,name);		
		} catch (Exception e) {
			ExceptionManage.dispose(e, getClass());
		}
		return etreeInfoList;
	}
	
	/**
	 * 查询所有业务
	 * 
	 * @param serviceId,name,brabchSite
	 * @return
	 */
	public List<EtreeInfo> selectAcIds(int serviceType,String name,int branchSite) {
		List<EtreeInfo> etreeInfoList = null;    
		try {
		   etreeInfoList = new ArrayList<EtreeInfo>();
		   etreeInfoList = this.mapper.queryAllEtrees(serviceType,name,branchSite);		
		} catch (Exception e) {
			ExceptionManage.dispose(e, getClass());
		}
		return etreeInfoList;
	}

	public Map<Integer, List<EtreeInfo>> filterSelect(EtreeInfo etreeInfo) {
		Map<Integer, List<EtreeInfo>> etreeInfoMap = new HashMap<Integer, List<EtreeInfo>>();
		List<EtreeInfo> etreeServiceList = null;
		AcPortInfoService_MB acService = null;
		List<Integer> acIds = null;
		try {
			acService = (AcPortInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.AcInfo,this.sqlSession);
			etreeServiceList = this.mapper.filterSelect(etreeInfo);
			
			if(etreeInfo.getAportId() >0)
			{
				acIds = acService.acByPort(etreeInfo.getAportId());
			}
			
			for (EtreeInfo etreeInfo_result : etreeServiceList) {
				etreeInfo_result.setCreateTime(DateUtil.strDate(etreeInfo_result.getCreateTime(), DateUtil.FULLTIME));
				//存在通过端口查询
				if(acIds != null)
				{
					if(!acIds.isEmpty())
					{
						if(acByFilter(acIds,etreeInfo_result.getAmostAcId()) || acByFilter(acIds,etreeInfo_result.getZmostAcId()))
						{
							etreeByFilter(etreeInfo_result,etreeInfoMap,etreeServiceList);
						}	
					}
				}else
				{
					etreeByFilter(etreeInfo_result,etreeInfoMap,etreeServiceList);
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return etreeInfoMap;
	}

	private void etreeByFilter(EtreeInfo etreeInfo_result,Map<Integer, List<EtreeInfo>> etreeInfoMap,List<EtreeInfo> etreeServiceList)
	{
		int serviceId = etreeInfo_result.getServiceId();
		if (etreeInfoMap.get(serviceId) == null) {
			List<EtreeInfo> etreeInfoList = new ArrayList<EtreeInfo>();
			for (EtreeInfo etree : etreeServiceList) {
				if (etree.getServiceId() == serviceId) {
					etreeInfoList.add(etree);
				}
			}
			etreeInfoMap.put(serviceId, etreeInfoList);
		}
	}
	
	/**
	 * 查询a/Z端是否满足查询条件
	 * @param acIds
	 * @param etreeInfo_result
	 * @return
	 */
	private boolean acByFilter(List<Integer> acIds,String mostAcId)
	{
	 Set<Integer> acList = null;
	 UiUtil uiutil  = null;
		try {
			uiutil = new UiUtil();
			acList = uiutil.getAcIdSets(mostAcId);
			for(Integer acId : acList)
			{
				if(acIds.contains(acId))
				{
					return true;
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, getClass());
		}
		return false;
	}

	public List<EtreeInfo> select(EtreeInfo etreeInfo) {
		List<EtreeInfo> etreeInfoList = null;
		try {
			etreeInfoList = this.mapper.queryByServiceId(etreeInfo.getServiceId());
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return etreeInfoList;
	}

	public List<EtreeInfo> selectByServiceId(int serviceId) {
		List<EtreeInfo> etreeInfoList = null;
		try {
			etreeInfoList = this.mapper.queryByServiceId(serviceId);
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return etreeInfoList;
	}

	public void search(List<SiteInst> siteInstList) throws Exception {
		List<Integer> siteIdList = null;
		Map<Integer, List<EtreeInfo>> map = null;
		Map<Integer, List<EtreeInfo>> map_create = null; // 要创建的map
		int createKey = 1; // 创建的map中key标识
		SiteService_MB siteService=null;
		try {
			siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE, this.sqlSession);
			siteIdList = siteService.getSiteIdList(siteInstList);
			map_create = new HashMap<Integer, List<EtreeInfo>>();
			for (SiteInst siteInst : siteInstList) {
				// 查出此网元所有根节点
				map = this.convertMap(this.mapper.queryByRoot(siteInst.getSite_Inst_Id()));

				// 遍历每一组根节点。寻找叶子节点
				for (int serviceId : map.keySet()) {
					this.putCrateMap(map_create, createKey, map.get(serviceId), siteIdList);
					createKey++;
				}
			}
			// 如果map中有值，就循环创建每一组etree。
			if (map_create.keySet().size() > 0) {
				for (int key : map_create.keySet()) {
					this.createEtree(map_create.get(key));
				}
			}
			this.sqlSession.commit();
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 把list转换成map serivceId为key
	 * @param etreeList 要转换的集合
	 * @throws Exception
	 */
	private Map<Integer, List<EtreeInfo>> convertMap(List<EtreeInfo> etreeList) throws Exception {
		Map<Integer, List<EtreeInfo>> map = null;
		List<EtreeInfo> etreeInfoList = null;
		try {
			map = new HashMap<Integer, List<EtreeInfo>>();

			for (EtreeInfo etreeInfo : etreeList) {
				if (null == map.get(etreeInfo.getServiceId())) {
					etreeInfoList = new ArrayList<EtreeInfo>();
					etreeInfoList.add(etreeInfo);

					map.put(etreeInfo.getServiceId(), etreeInfoList);
				} else {
					map.get(etreeInfo.getServiceId()).add(etreeInfo);
				}
			}

		} catch (Exception e) {
			throw e;
		} finally {
			etreeInfoList = null;
		}
		return map;
	}

	
	/**
	 * 根据根节点，查询出叶子节点，验证叶子节点是否在siteids集合中，如果在，把此组etree放到map中
	 * 
	 * @param map_create
	 *            创建的map集合
	 * @param key
	 *            创建的map中key
	 * @param rootEtrees
	 *            根节点
	 * @param siteIds
	 *            网元集合
	 * @throws Exception
	 */
	private void putCrateMap(Map<Integer, List<EtreeInfo>> map_create, int key, List<EtreeInfo> rootEtrees, List<Integer> siteIds) throws Exception {
		List<EtreeInfo> etreeInfoList = null;
		boolean flag = true;
		try {
			etreeInfoList = this.selectEtreeByPwId(this.getPwIds(rootEtrees));
			// 遍历查询出的etree集合
			//根节点必须与叶子节点数量完全对应
			if(etreeInfoList.size() == rootEtrees.size()*2){
				for (EtreeInfo etreeInfo : etreeInfoList) {
					// 如果是叶子节点，并且网元集合中不存在此网元，说明此次网元选择不足，不能合并
					if (etreeInfo.getBranchSite() > 0 && !siteIds.contains(etreeInfo.getBranchSite())) {
						flag = false;
						break;
					}
				}
			}else{
				flag = false;
			}
			// 如果通过验证，可以合并，把此组etree数据放到map中，统一做合并
			if (flag) {
				map_create.put(key, etreeInfoList);
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	public List<EtreeInfo> selectEtreeByPwId(List<Integer> pwIds) throws Exception {
		List<EtreeInfo> list = null;
		try {
			list = this.mapper.queryByPwIdCondition(pwIds);
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return list;
	}
	
	/**
	 * 获取etree中的所有pwid
	 * @param etreeInfoList etree集合
	 * @throws Exception
	 */
	private List<Integer> getPwIds(List<EtreeInfo> etreeInfoList) throws Exception {
		List<Integer> pwIds = null;
		try {
			pwIds = new ArrayList<Integer>();
			for (EtreeInfo etreeInfo : etreeInfoList) {
				pwIds.add(etreeInfo.getPwId());
			}
		} catch (Exception e) {
			throw e;
		}
		return pwIds;
	}

	/**
	 * 合并并且创建etree数据
	 * @param etreeInfoList
	 * @throws Exception
	 */
	private void createEtree(List<EtreeInfo> etreeInfoList) throws Exception {
		EtreeInfo branchEtree = null;
		List<EtreeInfo> etreeInfoList_create = null;
		int serviceId = 1;
		String etreeName = null;
		PwInfoService_MB pwService = null;
		try {
			etreeInfoList_create = new ArrayList<EtreeInfo>();
			for (EtreeInfo etreeInfo : etreeInfoList) {
				if (etreeInfo.getRootSite() > 0) {
					// 取叶子节点
					branchEtree = this.getBranch(etreeInfo, etreeInfoList);
					// 根据根节点和叶节点组成一个etree对象
					if(branchEtree != null){
						etreeInfoList_create.add(this.combination(etreeInfo, branchEtree));
					}
				}
			}
			if (etreeInfoList_create.size() > 0) {
				pwService = (PwInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PwInfo, this.sqlSession);
				// 取serviceid
				Integer maxId = this.mapper.selectMaxServiceId();
				if(maxId != null){
					serviceId = maxId + 1;
				}
				// 设置etree名称
				etreeName = "etree_" + new Date().getTime();
				for (EtreeInfo etreeInfo : etreeInfoList_create) {
					etreeInfo.setServiceId(serviceId);
					etreeInfo.setName(etreeName);
					// 插入
					this.mapper.insert(etreeInfo);
//					etreeInfo.setId(etreeId);
					// 修改pw关联
					pwService.setUser(etreeInfo.getPwId(), etreeInfo);
				}
				// 删除原有的数据
				for (EtreeInfo etreeInfo : etreeInfoList) {
					this.mapper.deleteByID(etreeInfo.getId());
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 根据根节点 找出对应的叶子节点
	 * @param rootEtree  根节点
	 * @param etreeInfoList
	 * @throws Exception
	 */
	private EtreeInfo getBranch(EtreeInfo rootEtree, List<EtreeInfo> etreeInfoList) throws Exception {
		EtreeInfo branchEtree = null;
		try {
			for (EtreeInfo etreeInfo : etreeInfoList) {
				if (etreeInfo.getPwId() == rootEtree.getPwId() && etreeInfo.getId() != rootEtree.getId()) {
					branchEtree = etreeInfo;
					return branchEtree;
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return branchEtree;
	}

	/**
	 * 组合根和叶为一个etree对象。
	 * @param rootEtree 根对象
	 * @param branchEtree 叶对象
	 * @throws Exception
	 */
	private EtreeInfo combination(EtreeInfo rootEtree, EtreeInfo branchEtree) throws Exception {
		EtreeInfo etreeInfo = new EtreeInfo();
		try {
			etreeInfo.setServiceType(rootEtree.getServiceType());
			etreeInfo.setActiveStatus(rootEtree.getActiveStatus());
			etreeInfo.setPwId(rootEtree.getPwId());
			etreeInfo.setaXcId(rootEtree.getaXcId());
			etreeInfo.setzXcId(branchEtree.getzXcId());
			etreeInfo.setRootSite(rootEtree.getRootSite());
			etreeInfo.setBranchSite(branchEtree.getBranchSite());
			etreeInfo.setaAcId(rootEtree.getaAcId());
			etreeInfo.setAmostAcId(rootEtree.getAmostAcId());
			etreeInfo.setZmostAcId(branchEtree.getZmostAcId());
			etreeInfo.setzAcId(branchEtree.getzAcId());
			etreeInfo.setCreateUser(branchEtree.getCreateUser());
			etreeInfo.setCreateTime(DateUtil.getDate(DateUtil.FULLTIME));
			etreeInfo.setJobStatus(rootEtree.getJobStatus());
		} catch (Exception e) {
			throw e;
		}
		return etreeInfo;
	}

	public Map<String, List<EtreeInfo>> selectNodeBySite(int siteId) {
		Map<String, List<EtreeInfo>> etreeInfoMap = new HashMap<String, List<EtreeInfo>>();
		List<EtreeInfo> etreeServiceList = null;

		try {
			etreeServiceList = this.mapper.queryAllBySite(siteId);
			for (EtreeInfo etreeInfo : etreeServiceList) {
				int serviceId = etreeInfo.getServiceId();
				if (etreeInfoMap.get(serviceId + "/" + etreeInfo.getServiceType()) == null) {
					List<EtreeInfo> etreeInfoList = new ArrayList<EtreeInfo>();
					for (EtreeInfo etree : etreeServiceList) {
						if (etree.getServiceId() == serviceId) {
							etreeInfoList.add(etree);
						}
					}
					etreeInfoMap.put(serviceId + "/" + etreeInfoList.get(0).getServiceType(), etreeInfoList);
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return etreeInfoMap;
	}
	
	/**
	 * 根据网元查询此网元下所有单网元eline业务
	 * @param siteId 网元主键
	 * @return 
	 * @throws Exception
	 */
	public Map<Integer, List<EtreeInfo>> selectBySite_node(int siteId) throws Exception{
		return this.convertListToMap(this.mapper.selectBySiteAndisSingle(siteId, 1));
	}
	
	/**
	 * 根据网元查询此网元下所有网络eline业务
	 * @param siteId 网元主键
	 * @return 
	 * @throws Exception
	 */
	public Map<Integer, List<EtreeInfo>> selectBySite_network(int siteId) throws Exception{
		return this.convertListToMap(this.mapper.selectBySiteAndisSingle(siteId, 0));
	}

	/**
	 * 验证名字是否重复
	 * @author kk
	 * @param afterName
	 *            修改之后的名字
	 * @param beforeName
	 *            修改之前的名字
	 * @return
	 * @throws Exception
	 * @Exception 异常对象
	 */
	public boolean nameRepetition(String afterName, String beforeName) throws Exception {
		int result = this.mapper.query_name(afterName, beforeName);
		if (0 == result) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * 单网元名称验证
	 * @param afterName
	 * @param beforeName
	 * @param siteId
	 * @return
	 * @throws Exception
	 */
	public boolean nameRepetitionBySingle(String afterName, String beforeName, int siteId) throws Exception {
		int result = this.mapper.query_nameBySingle(afterName, beforeName, siteId);
		if (0 == result) {
			return false;
		} else {
			return true;
		}

	}
	
	/*
	 * 查询所有的etree业务(每一条可能包含多条业务)
	 */
	public Map<Integer, List<EtreeInfo>> select() throws Exception {
		Map<Integer, List<EtreeInfo>> etreeInfoMap = new HashMap<Integer, List<EtreeInfo>>();
		List<EtreeInfo> etreeServiceList = null;

		try {
			etreeServiceList = new ArrayList<EtreeInfo>();
			etreeServiceList = this.mapper.queryAll();
			if(etreeServiceList!=null && etreeServiceList.size()>0){
				for (EtreeInfo etreeInfo : etreeServiceList) {
					int serviceId = etreeInfo.getServiceId();
					if (etreeInfoMap.get(serviceId) == null) {
						List<EtreeInfo> etreeInfoList = new ArrayList<EtreeInfo>();
						for (EtreeInfo etree : etreeServiceList) {
							if (etree.getServiceId() == serviceId) {
								etreeInfoList.add(etree);
							}
						}
						etreeInfoMap.put(serviceId, etreeInfoList);
					}
			}

			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return etreeInfoMap;
	}
	
	/**
	 * 通过acId,siteId查询line
	 * 
	 * @param acId
	 * @return
	 */
	public List<EtreeInfo> selectByAcIdAndSiteId(int acId, int siteId) {
		List<EtreeInfo> etreeInfos = null;
		List<EtreeInfo> etreeInsts = null;
		UiUtil uiUtil = null;
		try {
			etreeInfos = this.mapper.queryByAcIdAndSiteIdCondition(siteId);
			if(null != etreeInfos && !etreeInfos.isEmpty())
			{
				uiUtil = new UiUtil();
				etreeInsts = new ArrayList<EtreeInfo>();
				for(EtreeInfo etreeInfo : etreeInfos)
				{
				 if((uiUtil.getAcIdSets(etreeInfo.getAmostAcId()).contains(acId) && etreeInfo.getaSiteId() == siteId) 
					||(uiUtil.getAcIdSets(etreeInfo.getZmostAcId()).contains(acId)&& etreeInfo.getzSiteId() == siteId))
				 {
					 etreeInsts.add(etreeInfo);
				 }
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, getClass());
		}finally
		{
			 etreeInfos = null;
			 uiUtil = null;
		}
		return etreeInsts;
	}

	/**
	 * 通过网元id初始化某网元所有etree
	 */
	public void initializtionSite(int siteId) {
		Map<String, List<EtreeInfo>> etreeInfomaps = null;
		List<EtreeInfo> etreeInfos = null;
		List<EtreeInfo> etreeInfosList = null;
		try {
			etreeInfomaps = this.selectNodeBySite(siteId);
			for (String str : etreeInfomaps.keySet()) {
				etreeInfos = etreeInfomaps.get(str);// 一个key，对应一组etree
				etreeInfosList = this.selectByServiceId(etreeInfos.get(0).getServiceId());
				if (etreeInfosList != null && etreeInfosList.size() > 0) {
					if (etreeInfosList.get(0).getIsSingle() == 1) {// 判断是否是单网数据
						this.delete(etreeInfosList.get(0).getServiceId());
					} else {
						for (EtreeInfo etreeInfo : etreeInfosList) {
							if (etreeInfo.getRootSite() == siteId) {
								etreeInfo.setRootSite(0);
								etreeInfo.setaAcId(0);
								etreeInfo.setaXcId(0);
								etreeInfo.setASiteName("");
							} else if (etreeInfo.getBranchSite() == siteId) {
								etreeInfo.setBranchSite(0);
								etreeInfo.setzAcId(0);
								etreeInfo.setzXcId(0);
								etreeInfo.setZSiteName("");
								etreeInfo.setIsSingle(1);
							} else {
								etreeInfo.setIsSingle(1);
							}
						}
					}

				}
				this.update(etreeInfosList);
			}
			this.sqlSession.commit();
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
	}

	/**
	 * 通过etree主键ID 查询所有与此ID有关的etree数据（即： 与此ID相同的serviceID ）
	 * @param id
	 * @throws Exception
	 */
	public List<EtreeInfo> selectById(int id) {
		List<EtreeInfo> etreeInfoList = null;
		EtreeInfo etreeInfo=null;
		try {
			etreeInfo=new EtreeInfo();
			etreeInfo.setId(id);
			etreeInfoList = this.mapper.select(etreeInfo);
			if(etreeInfoList!=null&&etreeInfoList.size()==1){
				etreeInfo=etreeInfoList.get(0);
				etreeInfoList = this.mapper.queryByServiceId(etreeInfo.getServiceId());
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		if(etreeInfoList == null){
			return new ArrayList<EtreeInfo>();
		}else{
			return etreeInfoList;
		}
	}

	public List<EtreeInfo> selectById_all(int id) {
		List<EtreeInfo> etreeList = this.mapper.queryById(id);
		if(etreeList == null){
			return new ArrayList<EtreeInfo>();
		}else{
			return etreeList;
		}
	}

	public void updateStatusActivate(int siteId, int status) {
		try {
			this.mapper.updateStatus(siteId, status);
			this.sqlSession.commit();
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
	}

	public List<EtreeInfo> select_synchro(EtreeInfo etreeInfo, String role) {
		List<EtreeInfo> etreeList = null;
		if (TypeAndActionUtil.ACTION_ROOT.equals(role)) {
			//aSiteId只是用来做判断条件，和siteId没有关系
			etreeInfo.setaSiteId(1);
		} else {
			etreeInfo.setaSiteId(0);
		}
		etreeList = this.mapper.query_synchro(etreeInfo);
		if(etreeList == null){
			return new ArrayList<EtreeInfo>();
		}else{
			return etreeList;
		}
	}

	public List<Integer> insert(List<EtreeInfo> etreeInfoList) throws Exception {
		if (etreeInfoList == null) {
			throw new Exception("etreeinfoList is null");
		}
		List<Integer> resultList = new ArrayList<Integer>();
		if (etreeInfoList.isEmpty()) {
			return resultList;
		}
		List<EtreeInfo> DBetreeList = null;
		Integer maxId = this.mapper.selectMaxServiceId();
		int etreeServierId = 1;
		if(maxId != null){
			etreeServierId = maxId + 1;
		}
		Businessid rootXCIdInfo = null;
		PwInfoService_MB pwService = null;
		AcPortInfoService_MB acService = null;
		SiteService_MB siteService = null;
		BusinessidMapper businessMapper = null;
		try {
			pwService = (PwInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PwInfo, this.sqlSession);
			acService = (AcPortInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.AcInfo, this.sqlSession);
			siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE, this.sqlSession);
			businessMapper = this.sqlSession.getMapper(BusinessidMapper.class);
			//！= 0根节点；==0 就是叶子节点  
			if (etreeInfoList.get(0).getRootSite() != 0) {
				if (etreeInfoList.get(0).getaXcId() == 0) {
					try {
						rootXCIdInfo = businessMapper.queryBySiteId(etreeInfoList.get(0).getRootSite(), "etree").get(0);
					} catch (Exception e) {
						ExceptionManage.dispose(e, this.getClass());
					}
				} else {
					rootXCIdInfo = businessMapper.queryByIdValueSiteIdtype(etreeInfoList.get(0).getaXcId(), etreeInfoList.get(0).getRootSite(), "etree");
				}
				if (null == rootXCIdInfo) {
					throw new BusinessIdException(siteService.getSiteName(etreeInfoList.get(0).getRootSite()) + ResourceUtil.srcStr(StringKeysTip.TIP_ETREEID));
				}
				rootXCIdInfo.setIdStatus(1);
				businessMapper.update(rootXCIdInfo);
			}
			DBetreeList = new ArrayList<EtreeInfo>();
			for (EtreeInfo info : etreeInfoList) {
				//如果不等于空  axcId == 业务ID 否则为叶子节点 axcId == 0
				if (null != rootXCIdInfo) {
					info.setaXcId(rootXCIdInfo.getIdValue());
				}
				if (info.getBranchSite() != 0) {
					Businessid zEtreeXcId = null;
					if (info.getzXcId() == 0) {
						try {
							zEtreeXcId = businessMapper.queryBySiteId(info.getBranchSite(), "etree").get(0);
						} catch (Exception e) {
							ExceptionManage.dispose(e, this.getClass());
						}
					} else {
						zEtreeXcId = businessMapper.queryByIdValueSiteIdtype(info.getzXcId(), info.getBranchSite(), "etree");
					}
					if (null == zEtreeXcId) {
						throw new BusinessIdException(siteService.getSiteName(info.getBranchSite()) + ResourceUtil.srcStr(StringKeysTip.TIP_ETREEID));
					}
					info.setzXcId(zEtreeXcId.getIdValue());
					zEtreeXcId.setIdStatus(1);
					businessMapper.update(zEtreeXcId);
				}

				// 给Etree业务赋值serviID（业务Id）
				if (info.getId() == 0) {
					info.setServiceId(etreeServierId);
				}
				DBetreeList.add(info);
			}
			// 设置pw与etree业务的关联关系
			for (EtreeInfo DBInfo : DBetreeList) {
				DBInfo.setCreateTime(DateUtil.getDate(DateUtil.FULLTIME));
				this.mapper.insert(DBInfo);
				pwService.setUser(DBInfo.getPwId(), DBInfo);
										
				// 被业务使用的acId列表
				analysisAC(DBInfo,acService);
			}
            this.updateLanId(etreeInfoList);
			
			// 离线网元数据下载
			for (EtreeInfo EtreeInfo : DBetreeList) {
				if (0 != EtreeInfo.getaSiteId()) {
					super.dateDownLoad(EtreeInfo.getaSiteId(), EtreeInfo.getId(), EServiceType.ETREE.getValue(), EActionType.INSERT.getValue(), EtreeInfo.getServiceId() + "", null, 0, 0, null);
				}
				if (0 != EtreeInfo.getzSiteId()) {
					super.dateDownLoad(EtreeInfo.getzSiteId(), EtreeInfo.getId(), EServiceType.ETREE.getValue(), EActionType.INSERT.getValue(), EtreeInfo.getServiceId() + "", null, 0, 0, null);
				}
			}
			this.sqlSession.commit();
		} catch (BusinessIdException e) {
			throw e;
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return resultList;
	}

	private void analysisAC(EtreeInfo dBInfo, AcPortInfoService_MB acService) {
		try {
			this.updateACState(dBInfo.getAmostAcId(),acService,1,true);
			this.updateACState(dBInfo.getZmostAcId(),acService,1,true);
		} catch (Exception e){
			ExceptionManage.dispose(e, getClass());
		}	
	}
	
	/**
	 * 释放 和结合AC
	 * @param amostIds
	 * @param acService
	 * @param label 0 表示释放  1 表示结合AC
	 */
	private void updateACState(String amostIds,AcPortInfoService_MB acService,int label,boolean isRelateAc){
		Set<Integer> acSet = new HashSet<Integer>();
		UiUtil uiutil = new UiUtil();
		try {
			acSet = uiutil.getAcIdSets(amostIds);
			if(acSet.size() >0)
			{
				for(Integer acId : acSet)
				{
					if(isRelateAc)
					{
						this.analysisAc(acService,acId,label);
					}else
					{
						if(!this.isRelatedAC(acId))
						{
							this.analysisAc(acService,acId,label);
						}
					}
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, getClass());
		}
	}
	
	private void analysisAc(AcPortInfoService_MB acService,int acId,int label){
		AcPortInfo acPortInfo;
		try {
			acPortInfo = acService.selectById(acId);
			if(label == 0)
			{
				acPortInfo.setIsUser(0);
			}else
			{
				acPortInfo.setIsUser(1);
			}
			acService.updateUserType(acPortInfo);	
		} catch (Exception e) {
			ExceptionManage.dispose(e, getClass());
		}
	}
	
	/**
	 * 在删除之前判断着AC是否存在其他的业务的关联
	 * @param AcId
	 * @return true存在 false 不存在
	 */
	private boolean isRelatedAC(int acId){
		List<String> azAcIds = new ArrayList<String>();
		Set<Integer> azAcSet = new HashSet<Integer>();
		UiUtil uiUtil = new UiUtil();
		try {
			List<EtreeInfo> etreeList = this.mapper.isRelatedAcByEline(acId);
			if(etreeList != null && etreeList.size() > 0){
				return true;
			}else{
				etreeList = this.mapper.isRelatedAc();
				if(etreeList != null && etreeList.size() > 0){
					for (EtreeInfo etreeInfo : etreeList) {
						if(etreeInfo.getAmostAcId() != null && etreeInfo.getAmostAcId().equals("")){
							azAcIds.add(etreeInfo.getAmostAcId());
						}
						if(etreeInfo.getZmostAcId() != null && etreeInfo.getZmostAcId().equals("")){
							azAcIds.add(etreeInfo.getZmostAcId());
						}
					}
				}
				if(!azAcIds.isEmpty())
				{
					for(String acid : azAcIds)
					{
						azAcSet.addAll(uiUtil.getAcIdSets(acid));
					}
				}
				if(azAcSet.contains(acId))
				{
					return true;
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, getClass());
		}
		return false;
	}

	private void updateLanId(List<EtreeInfo> etreeInfoList) {
		AcPortInfoService_MB acService = null;
		PwNniInfoService_MB pwNniBufferService = null;
		UiUtil uiUtil = null;
		try {
			acService = (AcPortInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.AcInfo, this.sqlSession);
			pwNniBufferService =(PwNniInfoService_MB)ConstantUtil.serviceFactory.newService_MB(Services.PwNniBuffer, this.sqlSession);
		    //给LANID赋值			
	        List<EtreeInfo>  etreeInfoList1 = this.selectByServiceId(etreeInfoList.get(0).getServiceId());
		    List<Integer> siteIdList = null;			
		    siteIdList = this.getSiteIds(etreeInfoList1);
		    uiUtil = new UiUtil();
		    for(Integer siteId : siteIdList){
			    int count=1;
			    Set<Integer> acIdSet = new HashSet<Integer>();
			    List<Integer> pwIdList = new ArrayList<Integer>();
			    for (EtreeInfo etree : etreeInfoList1) {
			         if (etree.getRootSite() == siteId) {
				         acIdSet.addAll(uiUtil.getAcIdSets(etree.getAmostAcId()));
				         pwIdList.add(etree.getPwId());
			         } else if (etree.getBranchSite() == siteId) {
				        acIdSet.addAll(uiUtil.getAcIdSets(etree.getZmostAcId()));
				        pwIdList.add(etree.getPwId());
			         }				   
		    }
			// uni
			   for (Integer acId : acIdSet) {
				    AcPortInfo acInfo = new AcPortInfo();   
				    acInfo.setId(acId);
				    acInfo.setLanId(count);
				    acService.updateLanId(acInfo);
				    count++;
			   }
			
			// nni
			   PwNniInfo pwNNIInfo= null;
			   List<PwNniInfo>	pwNNIInfoList = new ArrayList<PwNniInfo>();
				for (Integer pwId : pwIdList) {
					pwNNIInfo = new PwNniInfo();
					pwNNIInfo.setSiteId(siteId);
					pwNNIInfo.setPwId(pwId);
					pwNNIInfoList.addAll(pwNniBufferService.select(pwNNIInfo));
				}
			    int cou = 11;
				for (PwNniInfo pwNNI : pwNNIInfoList) {
					PwNniInfo pwNni = new PwNniInfo();
					pwNni.setId(pwNNI.getId());
					pwNni.setLanId(cou);
					pwNni.setSiteId(siteId);						
					pwNniBufferService.updateLanId(pwNni);
					cou++;
				}
		    }
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
	}

	private List<Integer> getSiteIds(List<EtreeInfo> etreeInfoList) {
		List<Integer> siteIds = null;
		try {
			siteIds = new ArrayList<Integer>();
			for (EtreeInfo etreeInfo : etreeInfoList) {
				if (etreeInfo.getRootSite() > 0 ) {
					if (!siteIds.contains(etreeInfo.getRootSite()) ) {
						siteIds.add(etreeInfo.getRootSite());
					}
				}
				if (etreeInfo.getBranchSite() > 0 ) {
					if (!siteIds.contains(etreeInfo.getBranchSite())) {
						siteIds.add(etreeInfo.getBranchSite());
					}
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return siteIds;
	}

	/**
	 * 通过业务ID删除业务
	 * @param idList
	 */
	public void deleteById(List<Integer> idList){
		try {
			if(idList != null && idList.size() >0){
				for (Integer id : idList) {
					 this.mapper.deleteById(id);
				}
			}
			this.sqlSession.commit();
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
	}

	public void update(List<EtreeInfo> etreeInfoList) throws Exception {
		if (null == etreeInfoList || etreeInfoList.size() == 0) {
			throw new Exception("etreeinfoList is null");
		}
		PwInfo pwInfo = null;
		Businessid businessId = null;
		PwInfoService_MB pwService = null;
		AcPortInfoService_MB acService = null;
		try {
			pwService = (PwInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PwInfo, this.sqlSession);
			acService = (AcPortInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.AcInfo, this.sqlSession);		
			SiteService_MB	siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE, this.sqlSession);
			BusinessidMapper businessMapper = this.sqlSession.getMapper(BusinessidMapper.class);
			// 遍历要修改的etree数据，对每一条数据进行修改操作
			for (EtreeInfo etreeInfo : etreeInfoList) {

				// 修改了pw或者端口
				if (etreeInfo.getAction() == 1) {
					// 修改了pw
					if (null != etreeInfo.getBeforePw()) {
						// 释放之前的pw
						pwInfo = etreeInfo.getBeforePw();
						if(!isRelatedPW(pwInfo.getPwId())){
							pwInfo.setRelatedServiceId(0);
							pwInfo.setRelatedServiceType(0);
							pwService.updateRelatedService(pwInfo);
						}
						// 关联新的pw
						pwService.setUser(etreeInfo.getPwId(), etreeInfo);
						
					}
					// 修改了根端口
					if (null != etreeInfo.getBeforeAAcList()) {
						// 释放之前的ac对象
						for(AcPortInfo acportInst : etreeInfo.getBeforeAAcList())
						{													    							    							    
							if(!isRelatedAC(acportInst.getId())){
								acportInst.setIsUser(0);
								acportInst.setLanId(0);
								acService.updateUserType(acportInst);
							}
						}
						
						// 关联新的AC
						updateACState(etreeInfo.getAmostAcId(),acService,1,false);
					}
					// 修改了叶子端口
					if (null != etreeInfo.getBeforeZAcList()) 
					{
						// 释放之前的ac对象
						for(AcPortInfo acportInst : etreeInfo.getBeforeZAcList())
						{
							if(!isRelatedAC(acportInst.getId())){
								acportInst.setIsUser(0);
								acportInst.setLanId(0);
								acService.updateUserType(acportInst);
							}
						}
						// 关联新的AC
						updateACState(etreeInfo.getZmostAcId(),acService,1,false);
					}
				} 
				else if (etreeInfo.getAction() == 2) { // 新增了叶子节点
					if (etreeInfo.getBranchSite() != 0) {
						Businessid zEtreeXcId = null;
						if (etreeInfo.getzXcId() == 0) {
							try {
								zEtreeXcId = businessMapper.queryBySiteId(etreeInfo.getBranchSite(), "etree").get(0);
							} catch (Exception e) {
								ExceptionManage.dispose(e, this.getClass());
							}
						} else {
							zEtreeXcId = businessMapper.queryByIdValueSiteIdtype(etreeInfo.getzXcId(), etreeInfo.getBranchSite(), "etree");
						}
						if (null == zEtreeXcId) {
							throw new BusinessIdException(siteService.getSiteName(etreeInfo.getBranchSite()) + ResourceUtil.srcStr(StringKeysTip.TIP_ETREEID));
						}
						etreeInfo.setzXcId(zEtreeXcId.getIdValue());
						businessMapper.update(zEtreeXcId);
					}
					
					this.mapper.insert(etreeInfo);
					
					// 关联新的pw
					pwService.setUser(etreeInfo.getPwId(), etreeInfo);
					
					// 关联新的AC
					updateACState(etreeInfo.getZmostAcId(),acService,1,false);
					
					// 修改了根端口
					if (null != etreeInfo.getBeforeRootAc() && etreeInfo.getBeforeRootAc().size()>0) {
						// 释放之前的ac对象
						for(AcPortInfo acportInst : etreeInfo.getBeforeRootAc())
						{
							if(!isRelatedAC(acportInst.getId())){
								acportInst.setIsUser(0);
								acportInst.setLanId(0);
								acService.updateUserType(acportInst);
							}
						}
						// 关联新的AC
						updateACState(etreeInfo.getAmostAcId(),acService,1,false);
					}
					
				} else if (etreeInfo.getAction() == 3) { // 删除了叶子节点

					//释放叶子的businessid
					businessId = new Businessid();
					businessId.setIdStatus(0);
					businessId.setIdValue(etreeInfo.getzXcId());
					businessId.setType("etree");
					businessId.setSiteId(etreeInfo.getBranchSite());
					businessMapper.updateBusinessid(businessId);
					
					// 释放pw
					pwInfo = new PwInfo();
					pwInfo.setPwId(etreeInfo.getPwId());
					if(!isRelatedPW(etreeInfo.getPwId())){
						pwInfo=pwService.queryByPwId(pwInfo);
						pwInfo.setRelatedServiceId(0);
						pwInfo.setRelatedServiceType(0);
						pwService.updateRelatedService(pwInfo);
					}
					// 释放ac对象
					updateACState(etreeInfo.getZmostAcId(),acService,0,false);
					//删除叶子数据
					this.mapper.deleteById(etreeInfo.getId());
				}
				// 如果不是新增叶子、删除叶子 就修改etree数据
				if(etreeInfo.getAction()==1 || etreeInfo.getAction()==0){
					this.mapper.update(etreeInfo);
				}																				
			}
			
			// 离线网元数据下载
			EtreeInfo EtreeInfo = etreeInfoList.get(0);
			if (0 != EtreeInfo.getaSiteId()) {
				super.dateDownLoad(EtreeInfo.getaSiteId(), EtreeInfo.getServiceId(), EServiceType.ETREE.getValue(), EActionType.UPDATE.getValue(), EtreeInfo.getServiceId() + "", null, 0, 0, null);
			}
			if (0 != EtreeInfo.getzSiteId()) {
				super.dateDownLoad(EtreeInfo.getzSiteId(), EtreeInfo.getServiceId(), EServiceType.ETREE.getValue(), EActionType.UPDATE.getValue(), EtreeInfo.getServiceId() + "", null, 0, 0, null);
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		this.updateLanId(etreeInfoList);
	}
	
	/**
	 * 在删除之前判断着PW是否存在其他的业务的关联
	 * @param pwId
	 * @return true存在 false 不存在
	 */
	private boolean isRelatedPW(int pwId){
		try {
			List<EtreeInfo> etreeList = this.mapper.isRelatedPW(pwId);
			if(etreeList != null && etreeList.size() > 0){
				return true;
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, getClass());
		}
		return false;
	}

	public void clearLanId(List<EtreeInfo> etreeInfoList) {
		AcPortInfoService_MB acService = null;
		PwNniInfoService_MB pwNniBufferService = null;
		UiUtil uiUtil = null;
		try {
			acService = (AcPortInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.AcInfo, this.sqlSession);
			pwNniBufferService =(PwNniInfoService_MB)ConstantUtil.serviceFactory.newService_MB(Services.PwNniBuffer, this.sqlSession);
			//释放该条ETREE所关联的AC和PW的LANID
			List<Integer> siteIdLists = null;			
			siteIdLists = this.getSiteIds(etreeInfoList);
			uiUtil = new UiUtil();
			for(Integer siteId : siteIdLists){
				Set<Integer> acIdSet = new HashSet<Integer>();
				List<Integer> pwIdList = new ArrayList<Integer>();
				for (EtreeInfo etree : etreeInfoList) {
				    if (etree.getRootSite() == siteId) {
					    acIdSet.addAll(uiUtil.getAcIdSets(etree.getAmostAcId()));
					    pwIdList.add(etree.getPwId());
				    } else if (etree.getBranchSite() == siteId) {
					    acIdSet.addAll(uiUtil.getAcIdSets(etree.getZmostAcId()));
					    pwIdList.add(etree.getPwId());
				    }
			   }
			   // uni
			   for (Integer acId : acIdSet) {
				    AcPortInfo acInfo = new AcPortInfo();   
				    acInfo.setId(acId);
				    acInfo.setLanId(0);
				    acService.updateLanId(acInfo);
			   }
			   // nni
			   PwNniInfo pwNNIInfo= null;
			   List<PwNniInfo>	pwNNIInfoList = new ArrayList<PwNniInfo>();
				for (Integer pwId : pwIdList) {
					pwNNIInfo = new PwNniInfo();
					pwNNIInfo.setSiteId(siteId);
					pwNNIInfo.setPwId(pwId);
					pwNNIInfoList.addAll(pwNniBufferService.select(pwNNIInfo));
				}
				for (PwNniInfo pwNNI : pwNNIInfoList) {
					PwNniInfo pwNni = new PwNniInfo();
					pwNni.setId(pwNNI.getId());
					pwNni.setLanId(0);
					pwNni.setSiteId(siteId);						
					pwNniBufferService.updateLanId(pwNni);
				}
			}
		    this.sqlSession.commit();			
		} catch (Exception e) {
			ExceptionManage.dispose(e, getClass());
		}
	}

	public void relevancePw(List<EtreeInfo> etreeInfoList) throws Exception {
		Businessid businessId = null;
		SiteService_MB siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE, this.sqlSession);
		BusinessidMapper businessMapper = this.sqlSession.getMapper(BusinessidMapper.class);
		for (EtreeInfo etreeInfo : etreeInfoList) {
			if (etreeInfo.getAction() == 2) { // 新增了叶子节点
				//取叶子节点的businessid
				try {
					businessId = businessMapper.queryBySiteId(etreeInfo.getBranchSite(), "etree").get(0);
				} catch (Exception e) {
					ExceptionManage.dispose(e, this.getClass());
				}
				//如果没有取到，说明已经被用完。不能再创建
				if (null == businessId&&etreeInfo.getIsSingle()==0) {//单网络侧没有叶子节点
					throw new BusinessIdException(siteService.getSiteName(etreeInfo.getBranchSite()) + ResourceUtil.srcStr(StringKeysTip.TIP_ETREEID));
				}
				if(null != businessId){
					//如果取到，就把此businessid状态改成已使用
					etreeInfo.setzXcId(businessId.getIdValue());
					businessMapper.update(businessId);
				}
			}
		}
		this.sqlSession.commit();
	}

	public int delete(int serviceId) {
		int etreeResult = 0;
		List<EtreeInfo> etreeInfoList = null;
		PwInfo pwInfo = null;
		EtreeInfo etreeInfo = null;
		PwInfoService_MB pwService = null;
		AcPortInfoService_MB acService = null;
		try {
			pwService = (PwInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PwInfo, this.sqlSession);
			acService = (AcPortInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.AcInfo, this.sqlSession);
			BusinessidMapper businessMapper = this.sqlSession.getMapper(BusinessidMapper.class);
			// 解除与pw和ac引用关系
			etreeInfo = new EtreeInfo();
			etreeInfo.setServiceId(serviceId);
			etreeInfo.setServiceType(EServiceType.ETREE.getValue());
			etreeInfoList = this.select(etreeInfo);
			for (EtreeInfo info : etreeInfoList) {
				pwInfo = new PwInfo();
				pwInfo.setPwId(info.getPwId());
				//释放之前判断这个PW是否还存在其他的关联
				if(!isRelatedPW(info.getPwId())){
					pwInfo = pwService.selectBypwid_notjoin(pwInfo);
					pwInfo.setRelatedServiceId(0);
					pwInfo.setRelatedServiceType(0);
					pwService.updateRelatedService(pwInfo);
				}

				// 释放ac 之前判断这条AC是否被其他的业务所使用
				this.updateACState(info.getAmostAcId(),acService,0,false);
				this.updateACState(info.getZmostAcId(),acService,0,false);

				// 释放id
				Businessid businessId = new Businessid();
				businessId.setIdStatus(0);
				businessId.setIdValue(info.getaXcId());
				businessId.setType("etree");
				businessId.setSiteId(info.getRootSite());
				businessMapper.updateBusinessid(businessId);
				businessId.setIdValue(info.getzXcId());
				businessId.setSiteId(info.getBranchSite());
				businessMapper.updateBusinessid(businessId);
			}
			this.offLineAcion(etreeInfoList, pwService, acService);

			etreeResult = this.mapper.deleteByServiceId(serviceId);
			this.clearLanId(etreeInfoList);
			this.sqlSession.commit();
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return etreeResult;
	}
	
	/**
	 * 离线网元操作
	 * @param etreeInfoList
	 * @param pwService
	 * @throws Exception
	 */
	private void offLineAcion(List<EtreeInfo> etreeInfoList, PwInfoService_MB pwService, AcPortInfoService_MB acService) throws Exception {
		List<PwInfo> pwList;
		AcPortInfo acPortInfo;
		String aSitePws = "";
		PwInfo pwInfo;
		int zPwServiceId = 0;
		List<AcPortInfo> acPortInfoList;
		EtreeInfo offLineAction = etreeInfoList.get(0);
		if (offLineAction.getIsSingle() == 0) {
			for (EtreeInfo etree : etreeInfoList)
				aSitePws += etree.getPwId();
			pwList = pwService.selectServiceIdsByPwIds(aSitePws);
			if (null != pwList && pwList.size() > 0) {
				aSitePws = pwList.get(0).getApwServiceId() + "";
			}
			if (0 != offLineAction.getaSiteId()) {
				acPortInfo = new AcPortInfo();
				acPortInfo.setId(offLineAction.getaAcId());
				acPortInfoList = acService.selectByCondition(acPortInfo);
				if (null != acPortInfoList && acPortInfoList.size() > 0) {
					acPortInfo = acPortInfoList.get(0);
					super.dateDownLoad(offLineAction.getaSiteId(), offLineAction.getServiceId(), EServiceType.ETREE.getValue(), EActionType.DELETE.getValue(), offLineAction.getaXcId() + "", aSitePws, acPortInfo.getPortId(), acPortInfo.getAcBusinessId(), TypeAndActionUtil.ACTION_ROOT);
				}
			}
			if (0 != offLineAction.getzSiteId()) {
				acPortInfo = new AcPortInfo();
				acPortInfo.setId(offLineAction.getzAcId());
				acPortInfoList = acService.selectByCondition(acPortInfo);
				if (null != acPortInfoList && acPortInfoList.size() > 0) {
					acPortInfo = acPortInfoList.get(0);
					super.dateDownLoad(offLineAction.getzSiteId(), offLineAction.getServiceId(), EServiceType.ETREE.getValue(), EActionType.DELETE.getValue(), offLineAction.getzXcId() + "", aSitePws, acPortInfo.getPortId(), acPortInfo.getAcBusinessId(), TypeAndActionUtil.ACTION_BRANCH);
				}
			}
		} else {
			// 按业务保持离线网元数据
			// a段网元（根）
			if (null != etreeInfoList && etreeInfoList.size() > 0) {
				// 查询pwserviceid
				for (EtreeInfo obj : etreeInfoList) {
					aSitePws += obj.getPwId() + ",";
				}
				aSitePws = aSitePws.substring(0, aSitePws.length() - 1).trim();
				pwList = pwService.selectServiceIdsByPwIds(aSitePws);
				if (null != pwList && pwList.size() > 0) {
					aSitePws = "";
					for (PwInfo pwObj : pwList) {
						aSitePws += pwObj.getApwServiceId() + ",";
					}
					aSitePws = aSitePws.substring(0, aSitePws.length() - 1);
					if (0 != offLineAction.getaSiteId()) {
						acPortInfo = new AcPortInfo();
						acPortInfo.setId(offLineAction.getaAcId());
						acPortInfoList = acService.selectByCondition(acPortInfo);
						if (null != acPortInfoList && acPortInfoList.size() > 0) {
							acPortInfo = acPortInfoList.get(0);
							super.dateDownLoad(offLineAction.getaSiteId(), offLineAction.getServiceId(), EServiceType.ETREE.getValue(), EActionType.DELETE.getValue(), offLineAction.getaXcId() + "", aSitePws, acPortInfo.getPortId(), acPortInfo.getAcBusinessId(), TypeAndActionUtil.ACTION_ROOT);
						}
					}
				}

				// z段多网元（子）
				for (EtreeInfo info : etreeInfoList) {
					if (info.getaSiteId() > 0) {
						acPortInfo = new AcPortInfo();
						if (0 != info.getzAcId()) {
							acPortInfo.setId(info.getzAcId());
						} else {
							acPortInfo.setId(info.getaAcId());
						}
						acPortInfo = acService.selectByCondition(acPortInfo).get(0);
						pwInfo = new PwInfo();
						pwInfo.setPwId(info.getPwId());
						pwInfo = pwService.queryByPwId(pwInfo);
						if (info.getaSiteId() == pwInfo.getASiteId()) {
							zPwServiceId = pwInfo.getApwServiceId();
						}
						if (info.getaSiteId() == pwInfo.getZSiteId()) {
							zPwServiceId = pwInfo.getZpwServiceId();
						}
						super.dateDownLoad(info.getzSiteId(), offLineAction.getServiceId(), EServiceType.ETREE.getValue(), EActionType.DELETE.getValue(), info.getzXcId() + "", zPwServiceId + "", acPortInfo.getPortId(), acPortInfo.getAcBusinessId(), TypeAndActionUtil.ACTION_BRANCH);
					}
				}
			}
		}
	}

	public void updateStatusActivate(List<Integer> serviceIdList, int status) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("serviceIdList", serviceIdList);
			map.put("status", status);
			this.mapper.updateStatusByServiceId(map);
			this.sqlSession.commit();
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
	}
}
