package com.nms.model.ptn.path.eth;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.session.SqlSession;

import com.nms.db.bean.ptn.Businessid;
import com.nms.db.bean.ptn.SiteRoate;
import com.nms.db.bean.ptn.path.eth.DualInfo;
import com.nms.db.bean.ptn.path.protect.PwProtect;
import com.nms.db.bean.ptn.path.pw.PwInfo;
import com.nms.db.bean.ptn.port.AcPortInfo;
import com.nms.db.dao.ptn.BusinessidMapper;
import com.nms.db.dao.ptn.SiteRoateMapper;
import com.nms.db.dao.ptn.path.eth.DualInfoMapper;
import com.nms.db.dao.ptn.path.protect.PwProtectMapper;
import com.nms.db.enums.EServiceType;
import com.nms.model.equipment.shlef.SiteService_MB;
import com.nms.model.ptn.SiteRoateService_MB;
import com.nms.model.ptn.path.protect.PwProtectService_MB;
import com.nms.model.ptn.path.pw.PwInfoService_MB;
import com.nms.model.ptn.port.AcPortInfoService_MB;
import com.nms.model.util.ObjectService_Mybatis;
import com.nms.model.util.ServiceFactory;
import com.nms.model.util.Services;
import com.nms.ui.manager.BusinessIdException;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.ResourceUtil;
import com.nms.ui.manager.keys.StringKeysTip;
import com.nms.util.Mybatis_DBManager;

public class DualInfoService_MB extends ObjectService_Mybatis{
	public void setPtnuser(String ptnuser) {
		super.ptnuser = ptnuser;
	}

	public void setSqlSession(SqlSession sqlSession) {
		super.sqlSession = sqlSession;
	}
	private DualInfoMapper dualInfoMapper;
	
	public DualInfoMapper getDualInfoMapper() {
		return dualInfoMapper;
	}

	public void setDualInfoMapper(DualInfoMapper dualInfoMapper) {
		this.dualInfoMapper = dualInfoMapper;
	}
	
	/**
	 * 查询某一条dual业务(可能包含多条pw)
	 */
	public List<DualInfo> select(DualInfo dualInfo) throws Exception {
		List<DualInfo> dualInfoList = null;
		PwProtect pwProtect = null;
		PwProtectService_MB pwProtetcServiceMB = null;
		try {
			pwProtetcServiceMB = (PwProtectService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PWPROTECT, this.sqlSession);
			dualInfoList = this.dualInfoMapper.queryByCondition(dualInfo);
			for(DualInfo info : dualInfoList){//查找对应pw保护
				if(info.getBranchMainSite()>0){
					pwProtect = new PwProtect();
					pwProtect.setServiceId(info.getId());
					pwProtect = pwProtetcServiceMB.select(pwProtect).get(0);
					info.setPwProtect(pwProtect);
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return dualInfoList;
	}
	
	/**
	 * 查询所有的双归保护业务(每一条可能包含多条业务)
	 * @param label(0:代表网络侧; 1:代表单站测) 判断是查询网络侧还是查询单站侧的
	 */
	public Map<Integer, List<DualInfo>> select(int label,int siteId) throws Exception {
		Map<Integer, List<DualInfo>> dualInfoMap = new HashMap<Integer, List<DualInfo>>();
		List<DualInfo> dualInfoServiceList = null;
		PwProtect pwProtect = null;
		PwProtectService_MB pwProtetcServiceMB = null;
		List<PwProtect> pwProtectList = null;
		try {
			dualInfoServiceList = this.dualInfoMapper.queryAll(label,siteId);
			pwProtetcServiceMB = (PwProtectService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PWPROTECT, this.sqlSession);
			for (DualInfo dualInfo : dualInfoServiceList) {
				if(dualInfo.getBranchProtectSite() >0||dualInfo.getBranchMainSite()>0 ||(dualInfo.getBranchMainSite() == 0&&dualInfo.getBranchProtectSite() ==0 && dualInfo.getIsSingle() ==1)){
					pwProtect = new PwProtect();
					pwProtect.setServiceId(dualInfo.getId());
					pwProtectList = pwProtetcServiceMB.select(pwProtect);
					if(pwProtectList != null && pwProtectList.size() >0)
					{
						pwProtect = pwProtectList.get(0);
						dualInfo.setPwProtect(pwProtect);
					}
				}
				int serviceId = dualInfo.getServiceId();
				if (dualInfoMap.get(serviceId) == null) {
					List<DualInfo> dualInfoList = new ArrayList<DualInfo>();
					for (DualInfo info : dualInfoServiceList) {
						if (info.getServiceId() == serviceId) {
							dualInfoList.add(info);
						}
					}
					dualInfoMap.put(serviceId, dualInfoList);
				}

			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
//			UiUtil.closeService(pwProtetcService);
			 pwProtect = null;
			 pwProtectList = null;
			 dualInfoServiceList = null;
		}
		return dualInfoMap;
	}
	
	/**
	 * 判断名字是否重复
	 * 重复 返回true
	 * 不重复 返回false
	 * @throws Exception 
	 * @throws Exception 
	 */
	public boolean nameRepetition(String afterName, String beforeName) throws Exception {
		int result = this.dualInfoMapper.query_name(afterName, beforeName);
		if (0 == result) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * 通过acId,siteId查询Dual
	 * @param acId
	 * @return
	 * @throws SQLException 
	 */
	public List<DualInfo> selectByAcIdAndSiteId(int acId,int siteId) throws Exception{
		List<DualInfo> dualInfos = null;
		dualInfos = this.dualInfoMapper.queryByAcIdAndSiteIdCondition(acId,siteId);
		return dualInfos;
	}
	
	
	/**
	 * 根据网元id查询
	 * @param siteId
	 * @return
	 */
	public List<DualInfo> selectBySiteId(int siteId){
		List<DualInfo> dualInfos = null;
		try {
			dualInfos = dualInfoMapper.queryBySiteId(siteId,EServiceType.DUAL.getValue());
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return dualInfos;
	}
	

	public List<DualInfo> selectByPwIDs(List<Integer> pwIdList) {
		List<DualInfo> dualInfos = new ArrayList<DualInfo>();
		try {
			dualInfos = this.dualInfoMapper.queryBypwIDs(pwIdList);
			if(dualInfos == null){
				dualInfos = new ArrayList<DualInfo>();
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return dualInfos;
	}
	
	/**
	 * 根据网元id,businessId查询
	 * @param siteId
	 * @return
	 */
	public DualInfo selectBySiteIdAndBusinessId(int siteId,int businessId){
		List<DualInfo> dualInfos = null;
		DualInfo info=null;
		try {
			dualInfos = dualInfoMapper.queryBySiteIdAndBusinessId(siteId, businessId);
			if(dualInfos!=null&&dualInfos.size()>0){
				info=dualInfos.get(0);
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}finally{
			dualInfos = null;
		}
		return info;
	}
	
	/**
	 * 插入数据库
	 * @param dualInfoList
	 * @return
	 * @throws BusinessIdException
	 */
	public int insert(List<DualInfo> dualInfoList) throws Exception,BusinessIdException{
		if(dualInfoList == null){
			throw new Exception("etreeinfo is null");
		}
		List<Integer> acIdList = new ArrayList<Integer>();
		Set<Integer> acIdSet = new HashSet<Integer>();
		PwInfoService_MB pwService = null;
		AcPortInfoService_MB acService = null;
		PwProtectService_MB pwProtetcService = null;
		SiteService_MB siteService = null;
		SiteRoateService_MB siteRoateService = null;
		SiteRoate siteRoate = null;
        int pwProtectId = 0;
		BusinessidMapper businessidMapper = null;
		try {
		    siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE, this.sqlSession);
			pwService = (PwInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PwInfo, this.sqlSession);
			acService = (AcPortInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.AcInfo, this.sqlSession);
			pwProtetcService = (PwProtectService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PWPROTECT, this.sqlSession);
			siteRoateService = (SiteRoateService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITEROATE, this.sqlSession);
			businessidMapper = sqlSession.getMapper(BusinessidMapper.class);
			
			Businessid rootXCIdInfo = null;
			Integer serviceId = 1;
			if(dualInfoMapper.selectMaxServiceId() != null){
				serviceId = dualInfoMapper.selectMaxServiceId()+1;
			}
			
			for(DualInfo dualInfo : dualInfoList){//获取目的辅和主节点业务id
				//获取根节点业务id
				if (dualInfo.getRootSite() != 0) {
					
					if(dualInfo.getaXcId() == 0)
					{
						rootXCIdInfo = businessidMapper.queryList(dualInfo.getRootSite(), "eline").get(0);
					}else
					{
						rootXCIdInfo=businessidMapper.queryByIdValueSiteIdtype(dualInfo.getaXcId(), dualInfo.getRootSite(), "eline");
					}
					if (null == rootXCIdInfo) {
						throw new BusinessIdException(siteService.getSiteName(dualInfoList.get(0).getRootSite()) + ResourceUtil.srcStr(StringKeysTip.TIP_ELINEID));
					}
					rootXCIdInfo.setIdStatus(1);
					businessidMapper.update(rootXCIdInfo);
					if(rootXCIdInfo != null){
						dualInfo.setaXcId(rootXCIdInfo.getIdValue());
					}
				}
				if(dualInfo.getBranchMainSite() != 0){//目的主节点
					if(dualInfo.getzXcId() == 0)
					{
						rootXCIdInfo = businessidMapper.queryList(dualInfo.getBranchMainSite(), "eline").get(0);
					}else
					{
						rootXCIdInfo = businessidMapper.queryByIdValueSiteIdtype(dualInfo.getzXcId(), dualInfo.getBranchMainSite(), "eline");
					}
					if (null == rootXCIdInfo) {
						throw new BusinessIdException(siteService.getSiteName(dualInfo.getBranchMainSite()) + ResourceUtil.srcStr(StringKeysTip.TIP_ELINEID));
					}
					rootXCIdInfo.setIdStatus(1);
					businessidMapper.update(rootXCIdInfo);
					if(rootXCIdInfo != null){
						dualInfo.setzXcId(rootXCIdInfo.getIdValue());
					}
					
				}
				if(dualInfo.getBranchProtectSite() != 0){//目的辅节点
					
					if(dualInfo.getzXcId() == 0)
					{
						rootXCIdInfo = businessidMapper.queryList(dualInfo.getBranchProtectSite(), "eline").get(0);
					}else
					{
						rootXCIdInfo = businessidMapper.queryByIdValueSiteIdtype(dualInfo.getzXcId(), dualInfo.getBranchProtectSite(), "eline");
					}
					if (null == rootXCIdInfo) {
						throw new BusinessIdException(siteService.getSiteName(dualInfo.getBranchProtectSite()) + ResourceUtil.srcStr(StringKeysTip.TIP_ELINEID));
					}
					rootXCIdInfo.setIdStatus(1);
					businessidMapper.update(rootXCIdInfo);
					if(rootXCIdInfo != null){
						dualInfo.setzXcId(rootXCIdInfo.getIdValue());
					}
					
				}
				if(dualInfo.getServiceId() == 0)
				{
					dualInfo.setServiceId(serviceId);
				}
				
				// 被业务使用的acId列表
				if (dualInfo.getaAcId() != 0) {
					acIdSet.add(dualInfo.getaAcId());
				}
				if (dualInfo.getzAcId() != 0) {
					acIdSet.add(dualInfo.getzAcId());
				}
				dualInfoMapper.insert(dualInfo);
				pwService.setUser(dualInfo.getPwId(), dualInfo);
				if(dualInfo.getBranchMainSite()>0 || (dualInfo.getIsSingle() == 1 && dualInfo.getPwProtect() != null)){//保存pw保护配置
					dualInfo.getPwProtect().setServiceId(dualInfo.getId());
					pwProtetcService.insert(dualInfo.getPwProtect());
					pwProtectId = dualInfo.getPwProtect().getId();
					dualInfo.getPwProtect().setId(pwProtectId);
					siteRoate = new SiteRoate();
					siteRoate.setSiteId(dualInfo.getPwProtect().getSiteId());
					siteRoate.setType("pw");
					siteRoate.setRoate(-1);
					siteRoate.setTypeId(pwProtectId );
					siteRoateService.insert(siteRoate);
				}
			}
			// 设置ac被使用
			acIdList.addAll(acIdSet);
			List<AcPortInfo> acPortInfoList = acService.select(acIdList);
			for (AcPortInfo acPort : acPortInfoList) {
				acPort.setIsUser(1);
				acService.updateUserType(acPort);
			}
			
			sqlSession.commit();
		} catch (Exception e) {
			sqlSession.rollback();
			ExceptionManage.dispose(e, this.getClass());
		} 
		return 0;
	}
	
	/**
	 * 更新
	 * @param dualInfos
	 * @return
	 * @throws SQLException
	 */
	public int update(List<DualInfo> dualInfos) throws SQLException{
		int result = 0;
		PwProtectMapper pwProtectMapper = null;
		try {
			pwProtectMapper = sqlSession.getMapper(PwProtectMapper.class);
			for(DualInfo dualInfo : dualInfos){
				dualInfoMapper.updateByPrimaryKey(dualInfo);
				if(dualInfo.getPwProtect() != null){
					pwProtectMapper.updateByPrimaryKey(dualInfo.getPwProtect());
				}
			}
			sqlSession.commit();
		} catch (Exception e) {
			sqlSession.rollback();
			ExceptionManage.dispose(e, this.getClass());
		}
		
		return result;
	}
	
	public List<DualInfo> selectMainPwAndSiteId(int mainPwId,int standPwId,int siteId) throws Exception
	{
		return dualInfoMapper.queryBySiteIdAndPWId(mainPwId,standPwId,siteId);
		
	}
	
	/**
	 * 删除双归保护，并且释放ac，pw关联关系
	 * @param dualInfos
	 * @return
	 */
	public int delete(List<DualInfo> dualInfos){
		
		int dualResult = 0;
		PwInfo pwInfo = null;
		List<Integer> acIdlist = null;
		Set<Integer> acIdSet = null;
		List<AcPortInfo> acInfoList = null;
		PwInfoService_MB pwService = null;
		AcPortInfoService_MB acService = null;
		PwProtectService_MB pwProtetcService = null;
		SiteRoateMapper siteRoateMapper = null;
		SiteRoate siteRoate = null;
		BusinessidMapper businessidMapper = null;
		try {
			acIdSet = new HashSet<Integer>();
			acIdlist = new ArrayList<Integer>();

			pwService = (PwInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PwInfo, this.sqlSession);
			acService = (AcPortInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.AcInfo, this.sqlSession);
			pwProtetcService = (PwProtectService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PWPROTECT, this.sqlSession);
			businessidMapper = sqlSession.getMapper(BusinessidMapper.class);
			
			// 解除与pw和ac引用关系
			for (DualInfo dualInfo : dualInfos) {
				if(dualInfo.getBranchMainSite()>0||(dualInfo.getIsSingle() == 1 && dualInfo.getPwProtect() != null)){
					pwProtetcService.delete(dualInfo.getPwProtect());
					//删除siteRoate
					siteRoate = new SiteRoate();
					siteRoateMapper = sqlSession.getMapper(SiteRoateMapper.class);
					siteRoate.setTypeId(dualInfo.getPwProtect().getId());
					siteRoate.setType("pw");
					siteRoateMapper.delete(siteRoate);
				}
				pwInfo = new PwInfo();
				pwInfo.setPwId(dualInfo.getPwId());
				pwInfo = pwService.selectBypwid_notjoin(pwInfo);
				pwInfo.setRelatedServiceId(0);
				pwInfo.setRelatedServiceType(0);
				pwService.updateRelatedService(pwInfo);
				// 释放ac
				if (dualInfo.getaAcId() != 0) {
					acIdSet.add(dualInfo.getaAcId());
				}
				if (dualInfo.getzAcId() != 0) {
					acIdSet.add(dualInfo.getzAcId());
				}

				// 释放id
				Businessid businessId = new Businessid();
				businessId.setIdStatus(0);
				businessId.setIdValue(dualInfo.getaXcId());
				businessId.setType("eline");
				businessId.setSiteId(dualInfo.getRootSite());
				businessidMapper.updateBusinessid(businessId);
				if(dualInfo.getBranchMainSite()>0){
					businessId.setIdValue(dualInfo.getzXcId());
					businessId.setSiteId(dualInfo.getBranchMainSite());
					businessidMapper.updateBusinessid(businessId);
				}else{
					businessId.setIdValue(dualInfo.getzXcId());
					businessId.setSiteId(dualInfo.getBranchProtectSite());
					businessidMapper.updateBusinessid(businessId);
				}
				dualResult = this.dualInfoMapper.deleteByPrimaryKey(dualInfo.getId());
			}
			acIdlist.addAll(acIdSet);
			acInfoList = acService.select(acIdlist);
			for (AcPortInfo info : acInfoList) {
				info.setIsUser(0);
				acService.updateUserType(info);
			}
			
			sqlSession.commit();
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
		}
		return dualResult;
	}
	
	public static void main(String[] args) throws Exception {
		Mybatis_DBManager.init("10.18.1.10");
		ConstantUtil.serviceFactory = new ServiceFactory();
		DualInfoService_MB dualInfoServiceMB = (DualInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.DUALINFO);
		dualInfoServiceMB.select(0, 0);
	}

	/**
	 * 根据主键id查询dual
	 */
	public DualInfo queryById(int id) {
		return this.dualInfoMapper.queryById(id);
	}
	
	public List<DualInfo> selectByIds(List<Integer> ids){
		List<DualInfo> dualInfos = new ArrayList<DualInfo>();
		dualInfos = dualInfoMapper.queryByIDs(ids);
		return dualInfos;
	}
}
