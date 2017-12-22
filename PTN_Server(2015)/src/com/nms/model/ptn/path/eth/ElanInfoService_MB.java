package com.nms.model.ptn.path.eth;

import java.text.ParseException;
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
import com.nms.db.bean.ptn.path.eth.ElanInfo;
import com.nms.db.bean.ptn.path.pw.PwInfo;
import com.nms.db.bean.ptn.path.pw.PwNniInfo;
import com.nms.db.bean.ptn.port.AcPortInfo;
import com.nms.db.dao.ptn.BusinessidMapper;
import com.nms.db.dao.ptn.path.eth.ElanInfoMapper;
import com.nms.db.enums.EActionType;
import com.nms.db.enums.EServiceType;
import com.nms.model.equipment.shlef.SiteService_MB;
import com.nms.model.ptn.path.pw.PwInfoService_MB;
import com.nms.model.ptn.path.pw.PwNniInfoService_MB;
import com.nms.model.ptn.port.AcPortInfoService_MB;
import com.nms.model.util.ObjectService_Mybatis;
import com.nms.model.util.Services;
import com.nms.ui.manager.BusinessIdException;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.DateUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.ResourceUtil;
import com.nms.ui.manager.UiUtil;
import com.nms.ui.manager.keys.StringKeysTip;

public class ElanInfoService_MB extends ObjectService_Mybatis{
	public void setPtnuser(String ptnuser) {
		super.ptnuser = ptnuser;
	}

	public void setSqlSession(SqlSession sqlSession) {
		super.sqlSession = sqlSession;
	}
	
	private ElanInfoMapper mapper;

	public ElanInfoMapper getElanInfoMapper() {
		return mapper;
	}

	public void setElanInfoMapper(ElanInfoMapper elanInfoMapper) {
		this.mapper = elanInfoMapper;
	}

	public Map<Integer, List<ElanInfo>> filterSelect(ElanInfo elanInfo) {
		Map<Integer, List<ElanInfo>> elanInfoMap = new HashMap<Integer, List<ElanInfo>>();
		List<ElanInfo> elanServiceList = null;
		AcPortInfoService_MB acService = null;
		List<Integer> acIds = null;
		try {
			elanServiceList = this.mapper.filterSelect(elanInfo);
			this.convertTime(elanServiceList);
			acService = (AcPortInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.AcInfo, this.sqlSession);
			if(elanInfo.getAportId() >0)
			{
				acIds = acService.acByPort(elanInfo.getAportId());
			}
			for (ElanInfo elaninfor : elanServiceList) {
				//存在通过端口查询
				if(acIds != null)
				{
					if(!acIds.isEmpty())
					{
						if(acByFilter(acIds,elaninfor.getAmostAcId()) || acByFilter(acIds,elaninfor.getZmostAcId()))
						{
							etreeByFilter(elaninfor,elanInfoMap,elanServiceList);
						}	
					}
				}else
				{
					etreeByFilter(elaninfor,elanInfoMap,elanServiceList);
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return elanInfoMap;
	}
	
	private void etreeByFilter(ElanInfo elaninfor,Map<Integer, List<ElanInfo>> elanInfoMap,List<ElanInfo> elanServiceList)
	{
		int serviceId = elaninfor.getServiceId();
		if (elanInfoMap.get(serviceId) == null) {
			List<ElanInfo> elanInfoList = new ArrayList<ElanInfo>();
			for (ElanInfo elan : elanServiceList) {
				if (elan.getServiceId() == serviceId) {
					elanInfoList.add(elan);
				}
			}
			elanInfoMap.put(serviceId, elanInfoList);
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

	public List<ElanInfo> select(int elanId) {
		List<ElanInfo> elaninfoList = null;
		try {
			elaninfoList = this.mapper.selectById(elanId);
			this.convertTime(elaninfoList);
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return elaninfoList;
	}
	
	private void convertTime(List<ElanInfo> elaninfoList) throws ParseException{
		if(elaninfoList != null && elaninfoList.size() > 0){
			for (ElanInfo elanInfo : elaninfoList) {
				elanInfo.setCreateTime(DateUtil.strDate(elanInfo.getCreateTime(), DateUtil.FULLTIME));
			}
		}
	}
	
	public List<ElanInfo> select(ElanInfo elaninfo) throws Exception {
		List<ElanInfo> infoList = null;
		try {
			infoList = this.mapper.selectById(elaninfo.getServiceId());
			this.convertTime(infoList);
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return infoList;
	}

	public List<ElanInfo> selectByServiceId(int serviceId) throws ParseException {
		List<ElanInfo> elanInfos = this.mapper.selectById(serviceId);
		this.convertTime(elanInfos);
		return elanInfos;
	}

	public void search(List<SiteInst> siteInstList) throws Exception {
		List<Integer> siteIdList = null;
		Map<Integer, List<ElanInfo>> map = null;
		Map<Integer, List<ElanInfo>> map_create = null; // 要创建的map
		int createKey = 1; // 创建的map中key标识
		SiteService_MB siteService = null;
		try {
			siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE, this.sqlSession);
			siteIdList = siteService.getSiteIdList(siteInstList);
			map_create = new HashMap<Integer, List<ElanInfo>>();
			for (SiteInst siteInst : siteInstList) {
				// 查出此网元所有elan信息
				map = this.getMapBySite(siteInst.getSite_Inst_Id());
				// 遍历每一组elan 找出与此elan相匹配的elan
				for (int serviceId : map.keySet()) {
					// 如果此serviceId不在要创建的map中，才去验证。防止重复
//					if(map.get(serviceId).size()>1){
						if (!this.serviceIdIsExist(map_create, serviceId)) {
							this.putCreateMap(map_create, createKey, map.get(serviceId), siteIdList);
							createKey++;
						}
//					}
				}
			}
			// 如果map中有值，就循环创建每一组elan。
			if (map_create.keySet().size() > 0) {
				for (int key : map_create.keySet()) {
					this.createElan(map_create.get(key));
				}
			}
			this.sqlSession.commit();
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 验证serviceid在map中是否存在。
	 * @param map_create 要创建的map集合
	 * @param serviceId 要验证的serviceid
	 * @return
	 * @throws Exception
	 */
	private boolean serviceIdIsExist(Map<Integer, List<ElanInfo>> map_create, int serviceId) throws Exception {
		boolean flag = false;
		try {
			for (int key : map_create.keySet()) {
				for (ElanInfo elanInfo : map_create.get(key)) {
					if (elanInfo.getServiceId() == serviceId) {
						flag = true;
						break;
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return flag;
	}

	/**
	 * 根据网元ID查出所有此单网元下的所有elan
	 * @param siteId 网元ID
	 * @return key为serivceId 一组elan 为一条map
	 * @throws Exception
	 */
	private Map<Integer, List<ElanInfo>> getMapBySite(int siteId) throws Exception {
		List<ElanInfo> elanInfoList = null;
		Map<Integer, List<ElanInfo>> map_result = null;
		List<ElanInfo> elanInfoList_value = null;
		try {
			ElanInfo condition = new ElanInfo();
			condition.setSiteId(siteId);
			condition.setIsSingle(1);
			elanInfoList = this.mapper.queryNodeBySite(condition);
			this.convertTime(elanInfoList);
			map_result = new HashMap<Integer, List<ElanInfo>>();
			for (ElanInfo elanInfo : elanInfoList) {
				if (null == map_result.get(elanInfo.getServiceId())) {
					elanInfoList_value = new ArrayList<ElanInfo>();
					elanInfoList_value.add(elanInfo);
					map_result.put(elanInfo.getServiceId(), elanInfoList_value);
				} else {
					map_result.get(elanInfo.getServiceId()).add(elanInfo);
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return map_result;
	}
	
	/**
	 * 根据elanInfoList，查询出对应的elan，验证叶是否在siteids集合中，如果在，把此组elan放到map中
	 * @param map_create 创建的map集合
	 * @param key 创建的map中key
	 * @param elanInfoList 要匹配的elan集合
	 * @param siteIds 网元集合
	 * @throws Exception
	 */
	private void putCreateMap(Map<Integer, List<ElanInfo>> map_create, int key, List<ElanInfo> elanInfoList, List<Integer> siteIds) throws Exception {
		List<ElanInfo> elanList = null;
		boolean flag = true;
		try {
			elanList = this.selectElanbypwid(this.getPwIds(elanInfoList));
			// 遍历查询出的elan集合
			for (ElanInfo elanInfo : elanList) {
				// 如果是此elan不在要匹配的elan集合中，并且 网元id不存在网元集合中，则不匹配此elan
				if (!this.elanInfoIsExist(elanInfo, elanInfoList, "id")) {
					if (!siteIds.contains(elanInfo.getaSiteId())) {
						flag = false;
						break;
					} else {
						elanInfoList.addAll(this.selectByServiceId(elanInfo.getServiceId()));
					}
				}
			}
			ArrayList<ElanInfo> elanInfoLitst_create = new ArrayList<ElanInfo>();
			for (ElanInfo elanInfo_element : elanInfoList) {
				if (!this.elanInfoIsExist(elanInfo_element, elanInfoLitst_create, "pwid")) {
					ElanInfo elanInfo = this.getElanByPw(elanInfo_element, elanInfoList);
					if (null != elanInfo) {
						elanInfoLitst_create.add(this.combination(elanInfo_element, elanInfo));
					} else {
						return;
					}
				}
			}
			// 如果通过验证，可以合并，把此组elan数据放到map中，统一做合并
			if (flag) {
				map_create.put(key, elanInfoList);
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	public List<ElanInfo> selectElanbypwid(List<Integer> idList) throws Exception {
		List<ElanInfo> infoList = null;
		try {
			infoList = this.mapper.queryByPwId(idList);
			this.convertTime(infoList);
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return infoList;
	}
	
	/**
	 * 从集合中找出跟elanInfo相同pw的elan
	 * @param elanInfo
	 * @param elanInfoList
	 * @return
	 * @throws Exception
	 */
	private ElanInfo getElanByPw(ElanInfo elanInfo, List<ElanInfo> elanInfoList) throws Exception {
		ElanInfo elanInfo_result = null;
		try {
			for (ElanInfo elanInfo_element : elanInfoList) {
				if (elanInfo.getId() != elanInfo_element.getId() && elanInfo.getPwId() == elanInfo_element.getPwId()) {
					elanInfo_result = elanInfo_element;
					break;
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return elanInfo_result;
	}

	/**
	 * 组合两个elan为一条elan对象
	 * @param elanInfo_a a对象
	 * @param elanInfo_z z对象
	 * @return
	 * @throws Exception
	 */
	private ElanInfo combination(ElanInfo elanInfo_a, ElanInfo elanInfo_z) throws Exception {
		ElanInfo elanInfo = new ElanInfo();
		PwInfoService_MB pwInfoService = null;
		try {
			pwInfoService = (PwInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PwInfo,sqlSession);
			PwInfo pwInfo = pwInfoService.selectByPwId(elanInfo_a.getPwId());
			if(pwInfo != null){
				elanInfo.setServiceType(elanInfo_a.getServiceType());
				elanInfo.setActiveStatus(elanInfo_a.getActiveStatus());
				elanInfo.setPwId(elanInfo_a.getPwId());
				elanInfo.setCreateUser(elanInfo_z.getCreateUser());
				elanInfo.setCreateTime(DateUtil.getDate(DateUtil.FULLTIME));
				elanInfo.setJobStatus(elanInfo_a.getJobStatus());
				if(pwInfo.getASiteId() == elanInfo_a.getaSiteId()){
					elanInfo.setAxcId(elanInfo_a.getAxcId());
					elanInfo.setZxcId(elanInfo_z.getAxcId());
					elanInfo.setaSiteId(elanInfo_a.getaSiteId());
					elanInfo.setzSiteId(elanInfo_z.getaSiteId());
					elanInfo.setaAcId(elanInfo_a.getaAcId());
					elanInfo.setAmostAcId(elanInfo_a.getAmostAcId());
					elanInfo.setzAcId(elanInfo_z.getaAcId());
					elanInfo.setZmostAcId(elanInfo_z.getAmostAcId());
				}else if(pwInfo.getASiteId() == elanInfo_z.getaSiteId()){
					elanInfo.setAxcId(elanInfo_z.getAxcId());
					elanInfo.setZxcId(elanInfo_a.getAxcId());
					elanInfo.setaSiteId(elanInfo_z.getaSiteId());
					elanInfo.setzSiteId(elanInfo_a.getaSiteId());
					elanInfo.setaAcId(elanInfo_z.getaAcId());
					elanInfo.setAmostAcId(elanInfo_z.getAmostAcId());
					elanInfo.setzAcId(elanInfo_a.getaAcId());
					elanInfo.setZmostAcId(elanInfo_a.getAmostAcId());
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return elanInfo;
	}

	/**
	 * 验证一个elan是否在集合中存在
	 * @param elanInfo
	 * @param elanInfoList
	 * @param type id=比较ID，pwid=比较pwid
	 * @return
	 * @throws Exception
	 */
	private boolean elanInfoIsExist(ElanInfo elanInfo, List<ElanInfo> elanInfoList, String type) throws Exception {
		boolean flag = false;
		try {
			for (ElanInfo elanInfo_element : elanInfoList) {
				if ("id".equals(type)) {
					if (elanInfo.getId() == elanInfo_element.getId()) {
						flag = true;
						break;
					}
				} else if ("pwid".equals(type)) {
					if (elanInfo.getPwId() == elanInfo_element.getPwId()) {
						flag = true;
						break;
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return flag;
	}

	/**
	 * 搜索后，组合并且创建elan
	 * @param elanInfoLitst
	 * @throws Exception
	 */
	private void createElan(List<ElanInfo> elanInfoLitst) throws Exception {
		List<ElanInfo> elanInfoLitst_create = null;
		int serviceId = 1;
		String elanName = null;
		ElanInfo elanInfo = null;
		PwInfoService_MB pwService = null;
		try {
			elanInfoLitst_create = new ArrayList<ElanInfo>();
			for (ElanInfo elanInfo_element : elanInfoLitst) {
				if (!this.elanInfoIsExist(elanInfo_element, elanInfoLitst_create, "pwid")) {
					elanInfo = this.getElanByPw(elanInfo_element, elanInfoLitst);
					if (null != elanInfo) {
						elanInfoLitst_create.add(this.combination(elanInfo_element, elanInfo));
					} else {
						return;
					}
				}
			}
			if (elanInfoLitst_create.size() > 0) {
				pwService = (PwInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PwInfo, this.sqlSession);
				// 取serviceid
				Integer maxId = this.mapper.selectMaxServiceId();
				if(maxId  != null){
					serviceId = maxId + 1;
				}
				// 设置elan名称
				elanName = "elan_" + new Date().getTime();
				for (ElanInfo elanInfo_element : elanInfoLitst_create) {
					elanInfo_element.setServiceId(serviceId);
					elanInfo_element.setName(elanName);
					// 插入
					this.mapper.insert(elanInfo_element);
//					int elanId = this.mapper.queryByName(elanName);
//					elanInfo_element.setId(elanId);
					// 修改pw关联
					pwService.setUser(elanInfo_element.getPwId(), elanInfo_element);
				}
				// 删除原有的数据
				for (ElanInfo elanInfo_element : elanInfoLitst) {
					this.mapper.delete(elanInfo_element.getServiceId());
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 获取elan中的所有pwid
	 * @param elanInfoList elan集合
	 * @return
	 * @throws Exception
	 */
	private List<Integer> getPwIds(List<ElanInfo> elanInfoList) throws Exception {
		List<Integer> pwIds = null;
		try {
			pwIds = new ArrayList<Integer>();
			for (ElanInfo elanInfo : elanInfoList) {
				pwIds.add(elanInfo.getPwId());
			}
		} catch (Exception e) {
			throw e;
		}
		return pwIds;
	}

	public Map<Integer, List<ElanInfo>> selectBySiteId(int siteId) throws Exception {
		return this.convertListToMap(this.mapper.selectBySiteId(siteId));
	}

	/**
	 * 把所有etree按组转为map 
	 * @param etreeInfoList
	 * @return key为组id  value为此组下的etree对象
	 * @throws Exception
	 */
	private Map<Integer, List<ElanInfo>> convertListToMap(List<ElanInfo> elanInfoList) throws Exception{
		Map<Integer, List<ElanInfo>> elanInfoMap = new HashMap<Integer, List<ElanInfo>>();
		List<ElanInfo> elanInfoList_map =null;
		try {
			this.convertTime(elanInfoList);
			if(null!=elanInfoList && elanInfoList.size()>0){
				for (ElanInfo elanInfo : elanInfoList) {
					int serviceId = elanInfo.getServiceId();
					if (elanInfoMap.get(serviceId) == null) {
						elanInfoList_map = new ArrayList<ElanInfo>();
						elanInfoMap.put(serviceId, elanInfoList_map);
					}
					elanInfoMap.get(serviceId).add(elanInfo);
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return elanInfoMap;
	}
	
	public List<ElanInfo> selectElan(ElanInfo elaninfo) throws Exception {
		List<ElanInfo> infoList = null;
		try {
			infoList = new ArrayList<ElanInfo>();
			infoList = this.mapper.queryElan(elaninfo);
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return infoList;
	}
	
	public Map<String, List<ElanInfo>> selectSiteNodeBy(int siteid) throws Exception {

		Map<String, List<ElanInfo>> map = new HashMap<String, List<ElanInfo>>();
		List<ElanInfo> infoList = null;
		try {
			ElanInfo elanInfo = new ElanInfo();
			elanInfo.setSiteId(siteid);
			elanInfo.setIsSingle(0);
			infoList = new ArrayList<ElanInfo>();
			infoList = this.mapper.queryNodeBySite(elanInfo);
			int i, j;
			if(infoList!=null && infoList.size()>0){
				for (i = 0; i < infoList.size(); i++) {
					List<ElanInfo> temp = null;
					if (map.get(infoList.get(i).getServiceId() + "/" + infoList.get(i).getServiceType()) == null) {
						temp = new ArrayList<ElanInfo>();
					} else {
						continue;
					}
					for (j = i; j < infoList.size(); j++) {
						if (infoList.get(i).getServiceId() == infoList.get(j).getServiceId()) {
							temp.add(infoList.get(j));
						}
					}
					map.put(infoList.get(i).getServiceId() + "/" + infoList.get(i).getServiceType(), temp);
				}
		    }
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return map;

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
	 * 查询所有的elan业务(每一条可能包含多条业务)
	 */
	public Map<Integer, List<ElanInfo>> select() throws Exception {
		Map<Integer, List<ElanInfo>> elanInfoMap = new HashMap<Integer, List<ElanInfo>>();
		List<ElanInfo> elanServiceList = null;
		try {
			elanServiceList = new ArrayList<ElanInfo>();			
			elanServiceList = this.mapper.queryAll();
			if(elanServiceList!=null && elanServiceList.size()>0){
				for (ElanInfo etreeInfo : elanServiceList) {
					int serviceId = etreeInfo.getServiceId();
					if (elanInfoMap.get(serviceId) == null) {
						List<ElanInfo> elanInfoList = new ArrayList<ElanInfo>();
						for (ElanInfo elan : elanServiceList) {
							if (elan.getServiceId() == serviceId) {
								elanInfoList.add(elan);
							}
						}
						elanInfoMap.put(serviceId, elanInfoList);
					}	
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return elanInfoMap;
	}
	
	/**
	 * 根据网元查询此网元下所有单网元eline业务
	 * @param siteId 网元主键
	 * @return 
	 * @throws Exception
	 */
	public Map<Integer, List<ElanInfo>> selectBySite_node(int siteId) throws Exception{
		
		return this.convertListToMap(this.mapper.selectBySiteAndisSingle(siteId, 1));
	}
	
	/**
	 * 根据网元查询此网元下所有网络eline业务
	 * @param siteId 网元主键
	 * @return 
	 * @throws Exception
	 */
	public Map<Integer, List<ElanInfo>> selectBySite_network(int siteId) throws Exception{
		return this.convertListToMap(this.mapper.selectBySiteAndisSingle(siteId, 0));
	}
	
	/**
	 * 通过acId,siteId查询line
	 * 
	 * @param acId
	 * @return
	 */
	public List<ElanInfo> selectByAcIdAndSiteId(int acId, int siteId) {
		List<ElanInfo> elanInfos = null;
		List<ElanInfo> elanInsts = null;
		UiUtil uiUtil = null;
		try {
			elanInfos = this.mapper.queryByAcIdAndSiteIdCondition(siteId);
			if(null != elanInfos && !elanInfos.isEmpty())
			{
				uiUtil = new UiUtil();
				elanInsts = new ArrayList<ElanInfo>();
				for(ElanInfo elanInfo : elanInfos)
				{
				 if((uiUtil.getAcIdSets(elanInfo.getAmostAcId()).contains(acId) && elanInfo.getaSiteId() == siteId) 
					||(uiUtil.getAcIdSets(elanInfo.getZmostAcId()).contains(acId)&& elanInfo.getzSiteId() == siteId))
				 {
					 elanInsts.add(elanInfo);
				 }
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, getClass());
		}finally
		{
			elanInfos = null;
			uiUtil = null;
		}
		return elanInsts;
	}

	/**
	 * 通过elan主键ID 查询所有与此ID有关的elan数据（即： 与此ID相同的serviceID ）
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public List<ElanInfo> selectById(int id) {
		ElanInfo elanInfo = null;
		List<ElanInfo> elanInfoList = null;
		try {
			elanInfo = new ElanInfo();
			elanInfo.setId(id);
			elanInfoList = this.mapper.select(elanInfo);
			if (elanInfoList != null && elanInfoList.size() == 1) {
				elanInfo = elanInfoList.get(0);
				elanInfoList = this.selectByServiceId(elanInfo.getServiceId());
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		if(elanInfoList == null){
			return new ArrayList<ElanInfo>();
		}else{
			return elanInfoList;
		}
	}

	public List<ElanInfo> selectById_all(int serviceId) {
		List<ElanInfo> elanList = this.mapper.queryById(serviceId);
		if(elanList == null){
			return new ArrayList<ElanInfo>();
		}else{
			return elanList;
		}
	}

	public void initializtionSite(int siteId) {
		Map<String, List<ElanInfo>> elanInfomaps = null;
		List<ElanInfo> elanInfos = null;
		List<ElanInfo> elanInfosList = null;
		try {
			elanInfomaps = this.selectSiteNodeBy(siteId);
			for (String str : elanInfomaps.keySet()) {
				elanInfos = elanInfomaps.get(str);// 一个key，对应一组elan
				elanInfosList = this.selectByServiceId(elanInfos.get(0).getServiceId());
				if (elanInfosList != null && elanInfosList.size() > 0) {
					if (elanInfosList.get(0).getIsSingle() == 1) {// 判断是否是单网数据
						this.delete(elanInfosList.get(0).getServiceId());
					} else {
						for (ElanInfo elanInfo : elanInfosList) {
							if (elanInfo.getaSiteId() == siteId) {
								elanInfo.setaSiteId(0);
								elanInfo.setaAcId(0);
								elanInfo.setAxcId(0);
								elanInfo.setASiteName("");
								elanInfo.setIsSingle(1);
							} else if (elanInfo.getzSiteId() == siteId) {
								elanInfo.setzSiteId(0);
								elanInfo.setzAcId(0);
								elanInfo.setZxcId(0);
								elanInfo.setZSiteName("");
								elanInfo.setIsSingle(1);
							} else {
								elanInfo.setIsSingle(1);
							}
						}
					}
				}
				this.update(elanInfosList);
			}
			this.sqlSession.commit();
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
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

	public int insert(List<ElanInfo> elaninfos) throws Exception {
		if (elaninfos == null) {
			throw new Exception("elaninfo is null");
		}
		SiteService_MB siteService = null;
		PwInfoService_MB pwService = null;
		AcPortInfoService_MB acService = null;
		List<ElanInfo> DBElanList = null;
		List<Integer> acIdList = null;
		Set<Integer> acIdSet = null;
		Set<Integer> siteIdSet = new HashSet<Integer>();
		Map<Integer, Businessid> siteIdAndXcBusIdMap = new HashMap<Integer, Businessid>();
		int result = 0;
		if (elaninfos.isEmpty()) {
			return result;
		}
		try {
			Integer maxId = this.mapper.selectMaxServiceId(); // 得到etree业务Id
			int elanServierId = 1;
			if(maxId != null){
				elanServierId = maxId + 1;
			}
			siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE, this.sqlSession);
			pwService = (PwInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PwInfo, this.sqlSession);
			acService = (AcPortInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.AcInfo, this.sqlSession);	
			BusinessidMapper businessMapper = this.sqlSession.getMapper(BusinessidMapper.class);
			DBElanList = new ArrayList<ElanInfo>();
			acIdList = new ArrayList<Integer>();
			acIdSet = new HashSet<Integer>();
			Businessid XCId = null;
			for (ElanInfo info : elaninfos) {
				if (info.getaSiteId() > 0 && info.getAxcId() == 0) {
					siteIdSet.add(info.getaSiteId());
				}
				if (info.getzSiteId() > 0 && info.getZxcId() == 0) {
					siteIdSet.add(info.getzSiteId());
				}
			}
			for (Integer siteId : siteIdSet) {
				try {
					XCId = businessMapper.queryBySiteId(siteId, "etree").get(0);
				} catch (Exception e) {
					ExceptionManage.dispose(e, this.getClass());
				}
				if (null == XCId) {
					throw new BusinessIdException(siteService.getSiteName(siteId) + ResourceUtil.srcStr(StringKeysTip.TIP_ELANID));
				}

				siteIdAndXcBusIdMap.put(siteId, XCId);
				XCId.setIdStatus(1);
				businessMapper.update(XCId);
			}
			Businessid rootXCIdInfo = null;
			for (ElanInfo e_info : elaninfos) {
				if (null != siteIdAndXcBusIdMap.get(e_info.getaSiteId())) {
					if (e_info.getAxcId() == 0) {
						e_info.setAxcId(siteIdAndXcBusIdMap.get(e_info.getaSiteId()).getIdValue());
					}
				}else if(e_info.getAxcId()>0)
				{
					rootXCIdInfo = businessMapper.queryByIdValueSiteIdtype(e_info.getAxcId(), e_info.getaSiteId(), "etree");
					rootXCIdInfo.setIdStatus(1);
					businessMapper.update(rootXCIdInfo);
				}
				if (null != siteIdAndXcBusIdMap.get(e_info.getzSiteId())) {
					if (e_info.getZxcId() == 0) {
						e_info.setZxcId(siteIdAndXcBusIdMap.get(e_info.getzSiteId()).getIdValue());
					}
				}
				else if(e_info.getZxcId() >0)
				{
					rootXCIdInfo = businessMapper.queryByIdValueSiteIdtype(e_info.getZxcId(), e_info.getzSiteId(), "etree");
					rootXCIdInfo.setIdStatus(1);
					businessMapper.update(rootXCIdInfo);
				}
				// 给Elan业务赋值serviID（业务Id）
				if (e_info.getId() == 0) {
					e_info.setServiceId(elanServierId);
				}
				DBElanList.add(e_info);
			}
			// 设置pw与etree业务的关联关系
			for (ElanInfo DBInfo : DBElanList) {
				DBInfo.setCreateTime(DateUtil.getDate(DateUtil.FULLTIME));
				this.mapper.insert(DBInfo);
				pwService.setUser(DBInfo.getPwId(), DBInfo);
	/**************修改存在多AC情况下 2015-3-9 张坤******************************/
				this.addAcIdList(DBInfo,acIdSet);
	/*************************************************************************/
			}
			// 设置ac被使用
			acIdList.addAll(acIdSet);
			if(acIdList.size() >0)
			{
				List<AcPortInfo> acPortInfoList = acService.select(acIdList);
				for (AcPortInfo acPort : acPortInfoList) {
					acPort.setIsUser(1);
					acService.updateUserType(acPort);
				}
			}
              
			// 离线网元数据下载
			ElanInfo elanInfo = DBElanList.get(0);
			if (0 != elanInfo.getaSiteId()) {
				super.dateDownLoad(elanInfo.getaSiteId(), elanInfo.getServiceId(), EServiceType.ELAN.getValue(), EActionType.INSERT.getValue(), elanInfo.getServiceId() + "", null, 0, 0, null);
			}
			if (0 != elanInfo.getzSiteId()) {
				super.dateDownLoad(elanInfo.getzSiteId(), elanInfo.getServiceId(), EServiceType.ELAN.getValue(), EActionType.INSERT.getValue(), elanInfo.getServiceId() + "", null, 0, 0, null);
			}
		} catch (BusinessIdException e) {
			throw e;
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		this.updateLanId(elaninfos);   
		this.sqlSession.commit();
		return result;
	}
	
	private void addAcIdList(ElanInfo dBInfo, Set<Integer> acIdSet) {
		if(null != dBInfo.getAmostAcId()&& !"".equals(dBInfo.getAmostAcId())){
			for(String acId : dBInfo.getAmostAcId().split(",")){
				acIdSet.add(Integer.parseInt(acId.trim()));
			}
		}
		if(null != dBInfo.getZmostAcId()&& !"".equals(dBInfo.getZmostAcId())){
			for(String acId : dBInfo.getZmostAcId().split(",")){
				acIdSet.add(Integer.parseInt(acId.trim()));
			}
		}
	}
	
	private void updateLanId(List<ElanInfo> elanInfoList) throws Exception {
		UiUtil uiUtil = null;
		AcPortInfoService_MB acService = null;
		PwNniInfoService_MB pwNniBufferService=null;
		try {
			acService = (AcPortInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.AcInfo, this.sqlSession);
			pwNniBufferService =(PwNniInfoService_MB)ConstantUtil.serviceFactory.newService_MB(Services.PwNniBuffer, this.sqlSession);
		    //修改LANID赋值
	        List<ElanInfo> elanInfoList1 = this.selectByServiceId(elanInfoList.get(0).getServiceId());
		    List<Integer> siteIdList = null;			
		    siteIdList = this.getSiteIds(elanInfoList1);
		    uiUtil = new UiUtil(); 
		    for(Integer siteId : siteIdList){
			    int count=1;
			    Set<Integer> acIdSet1 = new HashSet<Integer>();
			    List<Integer> pwIdList = new ArrayList<Integer>();
			    for (ElanInfo elanInfo : elanInfoList1) {
				    if(elanInfo.getaSiteId() == siteId){
				       acIdSet1.addAll(uiUtil.getAcIdSets(elanInfo.getAmostAcId()));
				       pwIdList.add(elanInfo.getPwId());
				    }else if(elanInfo.getzSiteId() == siteId){
				       acIdSet1.addAll(uiUtil.getAcIdSets(elanInfo.getZmostAcId()));
				       pwIdList.add(elanInfo.getPwId());
				    }
			     }
			   // uni
			   for (Integer acId : acIdSet1) {
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


	public void update(List<ElanInfo> elanInfoList) throws Exception {
		PwInfo pwInfo = null;
		PwInfoService_MB pwService = null;
		AcPortInfoService_MB acService = null;
		Businessid businessId = null;
		SiteService_MB siteService = null;
		try {
			pwService = (PwInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PwInfo, this.sqlSession);
			acService = (AcPortInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.AcInfo, this.sqlSession);	
			siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE, this.sqlSession);
			BusinessidMapper businessMapper = this.sqlSession.getMapper(BusinessidMapper.class);
			for (ElanInfo info : elanInfoList) {
				if(info.getAction() == 1)
				{
					//修改了PW
					if(null != info.getBeforePw())
					{
						pwInfo = info.getBeforePw();
						if(!isRelatedPW(pwInfo.getPwId()))
						{
							pwInfo.setRelatedServiceId(0);
							pwInfo.setRelatedServiceType(0);
							if(pwInfo.getPwId() > 0)
							{
								pwService.updateRelatedService(pwInfo);
							}
						}
						pwService.setUser(info.getPwId(), info);
					}
					// 修改了端口
					if (null != info.getBeforeAAcList()) {
						// 释放之前的ac对象
						for(AcPortInfo acportInst : info.getBeforeAAcList())
						{
							 
							if(!isRelatedAC(acportInst.getId())){
								acportInst.setIsUser(0);
								acportInst.setLanId(0);
								acService.updateUserType(acportInst);
							}
						}
						// 关联新的AC
						updateACState(info.getAmostAcId(),acService,1);
					}
					if (null != info.getBeforeZAcList()) {
						// 释放之前的ac对象
						for(AcPortInfo acportInst : info.getBeforeZAcList())
						{
							if(!isRelatedAC(acportInst.getId())){
								acportInst.setIsUser(0);
								acportInst.setLanId(0);
								acService.updateUserType(acportInst);
							}
						}
						// 关联新的AC
						updateACState(info.getZmostAcId(),acService,1);
					}
				}else if(info.getAction() == 2)//新增
				{
					this.setXCId(info.getZxcId(),info.getzSiteId(),info,1, siteService, businessMapper);
					this.setXCId(info.getAxcId(),info.getaSiteId(),info,0, siteService, businessMapper);
					this.mapper.insert(info);
					// 关联新的pw
					pwService.setUser(info.getPwId(), info);
					//关联AC
					updateACState(info.getZmostAcId(),acService,1);
				}else if(info.getAction() == 3)//删除
				{
					//释放叶子的businessid
					businessId = new Businessid();
					businessId.setIdStatus(0);
					businessId.setType("etree");
					if(info.getSiteId() == info.getaSiteId())
					{
						businessId.setIdValue(info.getAxcId());
						businessId.setSiteId(info.getaSiteId());
						// 释放ac对象
						updateACState(info.getAmostAcId(),acService,0);
					}else if(info.getSiteId() == info.getzSiteId())
					{
						businessId.setIdValue(info.getZxcId());
						businessId.setSiteId(info.getzSiteId());
						// 释放ac对象
						updateACState(info.getZmostAcId(),acService,0);
					}
					businessMapper.updateBusinessid(businessId);
					
					// 释放pw
					pwInfo = new PwInfo();
					pwInfo.setPwId(info.getPwId());
					if(!isRelatedPW(info.getPwId())){
						pwInfo=pwService.queryByPwId(pwInfo);
						pwInfo.setRelatedServiceId(0);
						pwInfo.setRelatedServiceType(0);
						if(pwInfo.getPwId() > 0)
						{
							pwService.updateRelatedService(pwInfo);
						}
					}
//					// 释放ac对象
//					updateACState(info.getZmostAcId(),acService,0);
					//删除叶子数据
					this.mapper.deleteById(info.getId());
				}
				// 如果不是新增叶子、删除叶子 就修改etree数据
				if(info.getAction()==1 || info.getAction()==0){
					this.mapper.update(info);
				}
			 }
							
			// 离线网元数据下载
			ElanInfo elanInfo = elanInfoList.get(0);
			if (0 != elanInfo.getaSiteId()) {
				super.dateDownLoad(elanInfo.getaSiteId(), elanInfo.getServiceId(), EServiceType.ELAN.getValue(), EActionType.INSERT.getValue(), elanInfo.getServiceId() + "", null, 0, 0, null);
			}
			if (0 != elanInfo.getzSiteId()) {
				super.dateDownLoad(elanInfo.getzSiteId(), elanInfo.getServiceId(), EServiceType.ELAN.getValue(), EActionType.INSERT.getValue(), elanInfo.getServiceId() + "", null, 0, 0, null);
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, getClass());
		}
		//给LAN ID赋值
		this.updateLanId(elanInfoList);
		this.sqlSession.commit();
	}
	
	private void setXCId(int xcId,int siteId,ElanInfo elanInfo,int label, SiteService_MB siteService, BusinessidMapper businessMapper) throws Exception{
		if(siteId != 0){
			Businessid elanXcId = null;
			if(xcId == 0)
			{
				try {
					elanXcId = businessMapper.queryBySiteId(siteId, "etree").get(0);
				} catch (Exception e) {
					ExceptionManage.dispose(e, this.getClass());
				}
			}else
			{
				elanXcId = businessMapper.queryByIdValueSiteIdtype(xcId, siteId, "etree");
			}
			if(elanXcId == null)
			{
				throw new BusinessIdException(siteService.getSiteName(siteId) + ResourceUtil.srcStr(StringKeysTip.TIP_ELANID));
			}
			if(label ==0 )
			{
				elanInfo.setAxcId(elanXcId.getIdValue());
			}else
			{
				elanInfo.setZxcId(elanXcId.getIdValue());
			}
			elanXcId.setIdStatus(1);
			businessMapper.update(elanXcId);
		}
		
	}

	public void clearLanId(List<ElanInfo> elanInfoList) {
		AcPortInfoService_MB acService = null;
		PwNniInfoService_MB pwNniBufferService = null;
		UiUtil uiUtil = null;
		try {	
			acService = (AcPortInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.AcInfo, this.sqlSession);
			pwNniBufferService =(PwNniInfoService_MB)ConstantUtil.serviceFactory.newService_MB(Services.PwNniBuffer, this.sqlSession);
			//释放该条Elan所关联的AC和PW的LANID			
			List<Integer> siteIdLists = null;			
			siteIdLists = this.getSiteIds(elanInfoList);
			uiUtil = new UiUtil();
			for(Integer siteId : siteIdLists){
				Set<Integer> acIdSet = new HashSet<Integer>();
				List<Integer> pwIdList = new ArrayList<Integer>();
				for (ElanInfo elan : elanInfoList) {													
					if(elan.getaSiteId() == siteId){
				       acIdSet.addAll(uiUtil.getAcIdSets(elan.getAmostAcId()));
				       pwIdList.add(elan.getPwId());
				    }else if(elan.getzSiteId() == siteId){
				       acIdSet.addAll(uiUtil.getAcIdSets(elan.getZmostAcId()));
				       pwIdList.add(elan.getPwId());
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
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
	}
	
	/**
	 * 获取siteID集合
	 * 
	 * @param elanInfoList
	 *            elan对象
	 * @return siteID集合
	 * @throws Exception
	 */
	private List<Integer> getSiteIds(List<ElanInfo> elanInfoList) throws Exception {
		List<Integer> siteIds = null;
		try {
			siteIds = new ArrayList<Integer>();
			for (ElanInfo elan : elanInfoList) {
				if (elan.getaSiteId()!=0 && !siteIds.contains(elan.getaSiteId())) {
					siteIds.add(elan.getaSiteId());
				}
				if (elan.getzSiteId() !=0 && !siteIds.contains(elan.getzSiteId())) {
					siteIds.add(elan.getzSiteId());
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return siteIds;
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

	public int delete(int elanId) {
		int result = 0;
		PwInfo pwInfo = null;
		List<ElanInfo> elanInfoList = null;
		PwInfoService_MB pwService = null;
		AcPortInfoService_MB acService = null;
		try {
			pwService = (PwInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PwInfo, this.sqlSession);
			acService = (AcPortInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.AcInfo, this.sqlSession);
			BusinessidMapper businessMapper = this.sqlSession.getMapper(BusinessidMapper.class);
			elanInfoList = this.select(elanId);
			for (ElanInfo info : elanInfoList) {
				pwInfo = new PwInfo();
				pwInfo.setPwId(info.getPwId());
				if(!this.isRelatedPW(info.getPwId())){
					pwInfo = pwService.selectBypwid_notjoin(pwInfo);
					pwInfo.setRelatedServiceId(0);
					pwInfo.setRelatedServiceType(0);
					if(pwInfo.getPwId() >0)
					{
						pwService.updateRelatedService(pwInfo);
					}
				}
				// 释放ac  
				this.updateACState(info.getAmostAcId(),acService,0);
				this.updateACState(info.getZmostAcId(),acService,0);

				// 释放id
				Businessid businessId = new Businessid();
				businessId.setIdStatus(0);
				businessId.setIdValue(info.getAxcId());
				if (info.getaSiteId() > 0) {
					businessId.setType("etree");
					businessId.setSiteId(info.getaSiteId());
					businessMapper.updateBusinessid(businessId);
				}

				if (info.getzSiteId() > 0) {
					businessId.setType("etree");
					businessId.setIdValue(info.getZxcId());
					businessId.setSiteId(info.getzSiteId());
					businessMapper.updateBusinessid(businessId);
				}
			}
			this.clearLanId(elanInfoList);
			// 离线网元操作
			this.offLineAcion(elanInfoList, pwService, acService);						
			result = this.mapper.delete(elanId);
			this.sqlSession.commit();
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return result;
	}
	
	/**
	 * 在删除之前判断着PW是否存在其他的业务的关联
	 * @param pwId
	 * @return true存在 false 不存在
	 */
	private boolean isRelatedPW(int pwId){
		try {
			List<ElanInfo> elanList = this.mapper.isRelatedPW(pwId);
			if(elanList != null && elanList.size() > 0){
				return true;
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, getClass());
		}
		return false;
	}

	/**
	 * 释放 和结合AC
	 * @param amostIds
	 * @param acService
	 * @param label 0 表示释放  1 表示结合AC
	 */
	private void updateACState(String amostIds,AcPortInfoService_MB acService,int label){
		Set<Integer> acSet = new HashSet<Integer>();
		UiUtil uiutil = new UiUtil();
		try {
			acSet = uiutil.getAcIdSets(amostIds);
			if(acSet.size() >0)
			{
				for(Integer acId : acSet)
				{
					if(!this.isRelatedAC(acId))
					{
						this.analysisAc(acService,acId,label);
					}	
				}
			}
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
			List<ElanInfo> elanList = this.mapper.isRelatedAcByEline(acId);
			if(elanList != null && elanList.size() > 0){
				return true;
			}else{
				elanList = this.mapper.isRelatedAc();
				if(elanList != null && elanList.size() > 0){
					for (ElanInfo elanInfo : elanList) {
						if(elanInfo.getAmostAcId() != null && elanInfo.getAmostAcId().equals("")){
							azAcIds.add(elanInfo.getAmostAcId());
						}
						if(elanInfo.getZmostAcId() != null && elanInfo.getZmostAcId().equals("")){
							azAcIds.add(elanInfo.getZmostAcId());
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
				if(azAcSet.contains(acId)){
					return true;
				}	
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, getClass());
		}
		return false;
	}
	
	private void analysisAc(AcPortInfoService_MB acService,int acId,int label){
		AcPortInfo acPortInfo = null;
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
	 * 离线网元操作
	 * @param etreeInfoList
	 * @param pwService
	 * @throws Exception
	 */
	private void offLineAcion(List<ElanInfo> elanInfoList, PwInfoService_MB pwService, AcPortInfoService_MB acService) throws Exception {
		List<PwInfo> pwList = new ArrayList<PwInfo>();
		List<AcPortInfo> acPortInfoList = new ArrayList<AcPortInfo>();
		ElanInfo offLineAction = null;
		AcPortInfo acPortInfo;
		String aSitePws1 = "";
		for (int i = 0; i < elanInfoList.size(); i++) {
			offLineAction = elanInfoList.get(i);
			aSitePws1 += offLineAction.getPwId() + ",";
		}
		aSitePws1 = (String) aSitePws1.subSequence(0, aSitePws1.length() - 1);
		pwList = pwService.selectServiceIdsByPwIds(aSitePws1);
		if (null != pwList && pwList.size() > 0) {
			aSitePws1 = "";
			for (PwInfo pwObj : pwList) {
				aSitePws1 += pwObj.getApwServiceId() + ",";
			}
			aSitePws1 = aSitePws1.substring(0, aSitePws1.length() - 1);
			// a段网元
			if (0 != offLineAction.getaSiteId()) {
				acPortInfo = new AcPortInfo();
				acPortInfo.setId(offLineAction.getaAcId());
				acPortInfoList = acService.selectByCondition(acPortInfo);
				if (null != acPortInfoList && acPortInfoList.size() > 0) {
					acPortInfo = acPortInfoList.get(0);
					super.dateDownLoad(offLineAction.getaSiteId(), offLineAction.getServiceId(), EServiceType.ELAN.getValue(), EActionType.DELETE.getValue(), offLineAction.getAxcId() + "", aSitePws1, acPortInfo.getPortId(), acPortInfo.getAcBusinessId(), null);
				}
			}
			// z段网元
			if (0 != offLineAction.getzSiteId()) {
				acPortInfo = new AcPortInfo();
				acPortInfo.setId(offLineAction.getzAcId());
				acPortInfoList = acService.selectByCondition(acPortInfo);
				if (null != acPortInfoList && acPortInfoList.size() > 0) {
					acPortInfo = acPortInfoList.get(0);
					super.dateDownLoad(offLineAction.getzSiteId(), offLineAction.getServiceId(), EServiceType.ELAN.getValue(), EActionType.DELETE.getValue(), offLineAction.getZxcId() + "", aSitePws1, acPortInfo.getPortId(), acPortInfo.getAcBusinessId(), null);
				}
			}
		}
	}
	
	public List<ElanInfo> select_synchro(ElanInfo elanInfo) {
		List<ElanInfo> elanList = null;
		elanList = this.mapper.query_synchro(elanInfo);
		if(elanList == null){
			return new ArrayList<ElanInfo>();
		}else{
			return elanList;
		}
	}
	
	public List<Map<String,Object>> selectAll_ETHNorth() {
		List<Map<String,Object>> list = null;
		list = this.mapper.selectAll_ETHNorth();
		if(list == null){
			return new ArrayList<Map<String,Object>>();
		}
		return list;
	}
	
	public List<Map<String,Object>> selectAll_ESINorth() {
		List<Map<String,Object>> list = null;
		list = this.mapper.selectAll_ESINorth();
		if(list == null){
			return new ArrayList<Map<String,Object>>();
		}
		return list;
	}
}
