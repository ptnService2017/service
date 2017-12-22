package com.nms.model.ptn.path.eth;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.nms.db.bean.ptn.Businessid;
import com.nms.db.bean.ptn.oam.OamInfo;
import com.nms.db.bean.ptn.oam.OamMepInfo;
import com.nms.db.bean.ptn.oam.OamMipInfo;
import com.nms.db.bean.ptn.path.eth.ElineInfo;
import com.nms.db.bean.ptn.path.pw.PwInfo;
import com.nms.db.bean.ptn.path.pw.PwNniInfo;
import com.nms.db.bean.ptn.port.AcPortInfo;
import com.nms.db.dao.ptn.BusinessidMapper;
import com.nms.db.dao.ptn.path.eth.ElineInfoMapper;
import com.nms.db.dao.ptn.path.pw.PwInfoMapper;
import com.nms.db.dao.ptn.port.AcPortInfoMapper;
import com.nms.db.enums.EActionType;
import com.nms.db.enums.EServiceType;
import com.nms.db.enums.OamTypeEnum;
import com.nms.model.equipment.shlef.SiteService_MB;
import com.nms.model.ptn.oam.OamInfoService_MB;
import com.nms.model.ptn.path.pw.PwInfoService_MB;
import com.nms.model.ptn.port.AcPortInfoService_MB;
import com.nms.model.util.ObjectService_Mybatis;
import com.nms.model.util.ServiceFactory;
import com.nms.model.util.Services;
import com.nms.ui.manager.BusinessIdException;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.DateUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.ResourceUtil;
import com.nms.ui.manager.keys.StringKeysTip;
import com.nms.util.Mybatis_DBManager;

public class ElineInfoService_MB extends ObjectService_Mybatis {
	public void setPtnuser(String ptnuser) {
		super.ptnuser = ptnuser;
	}

	public void setSqlSession(SqlSession sqlSession) {
		super.sqlSession = sqlSession;
	}

	private ElineInfoMapper mapper;

	public ElineInfoMapper getElineInfoMapper() {
		return mapper;
	}

	public void setElineInfoMapper(ElineInfoMapper ElineInfoMapper) {
		this.mapper = ElineInfoMapper;
	}

	public List<ElineInfo> selectByCondition(ElineInfo eline) {
		List<ElineInfo> elineinfoList = null;
		try {
			elineinfoList = this.mapper.queryByCondition(eline);
			if (elineinfoList != null && !elineinfoList.isEmpty()) {
				for (ElineInfo elineInfo : elineinfoList) {
					elineInfo.setCreateTime(DateUtil.strDate(elineInfo.getCreateTime(), DateUtil.FULLTIME));
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return elineinfoList;
	}

	public List<ElineInfo> selectBySiteId(int siteId) {
		List<ElineInfo> elineinfoList = null;
		try {
			elineinfoList = this.mapper.selectBySiteId(siteId);
			if (elineinfoList != null && !elineinfoList.isEmpty()) {
				for (ElineInfo elineInfo : elineinfoList) {
					elineInfo.setCreateTime(DateUtil.strDate(elineInfo.getCreateTime(), DateUtil.FULLTIME));
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return elineinfoList;
	}

	public Object selectElineByCondition(ElineInfo elineInfo) {
		List<ElineInfo> elineinfoList = null;
		try {
			elineinfoList = mapper.querySingleByCondition(elineInfo);
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
		}
		return elineinfoList;
	}

	/**
	 * 根据网元查询此网元下所有单网元eline业务
	 * 
	 * @param siteId
	 *            网元主键
	 * @return
	 * @throws Exception
	 */
	public List<ElineInfo> selectBySite_node(int siteId) throws Exception {
		return this.mapper.selectBySiteAndisSingle(siteId, 1);
	}

	/**
	 * 根据网元查询此网元下所有网络eline业务
	 * 
	 * @param siteId
	 *            网元主键
	 * @return
	 * @throws Exception
	 */
	public List<ElineInfo> selectBySite_network(int siteId) throws Exception {
		return this.mapper.selectBySiteAndisSingle(siteId, 0);
	}

	/**
	 * 通过acId,siteId查询line
	 * 
	 * @param acId
	 * @return
	 * @throws SQLException
	 */
	public List<ElineInfo> selectByAcIdAndSiteId(int acId, int siteId) throws Exception {
		List<ElineInfo> elineInfos = null;
		elineInfos = this.mapper.queryByAcIdAndSiteIdCondition(acId, siteId);
		return elineInfos;
	}

	/**
	 * 查询单网元下的所有eline
	 * 
	 * @param siteId
	 *            网元id
	 * @return
	 * @throws Exception
	 */
	public List<ElineInfo> selectNodeBySiteAndServiceId(int siteId, int serviceId) throws Exception {

		List<ElineInfo> elineInfoList = null;
		try {
			elineInfoList = new ArrayList<ElineInfo>();
			elineInfoList = this.mapper.queryNodeBySiteAndServiceId(siteId, serviceId);
			for (ElineInfo elineInfo : elineInfoList) {
				elineInfo.setNode(true);
			}

		} catch (Exception e) {
			throw e;
		}
		return elineInfoList;
	}

	public List<ElineInfo> selectByCondition_nojoin(ElineInfo elineinfo) throws Exception {
		List<ElineInfo> elineinfoList = null;
		try {
			elineinfoList = mapper.queryByCondition_notjoin(elineinfo);
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
		}
		return elineinfoList;
	}

	/**
	 * 查询单网元下的所有eline
	 * 
	 * @param siteId
	 *            网元id
	 * @return
	 * @throws Exception
	 */
	public List<ElineInfo> selectNodeBySite(int siteId) throws Exception {

		List<ElineInfo> elineInfoList = null;
		OamInfoService_MB oamInfoService = null;
		OamInfo oamInfo = null;
		OamMepInfo oamMepInfo = null;
		OamMipInfo oamMipInfo = null;
		try {
			oamInfoService = (OamInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.OamInfo, this.sqlSession);
			elineInfoList = this.mapper.queryNodeBySite(siteId);
			for (ElineInfo elineInfo : elineInfoList) {
				elineInfo.setNode(true);

				oamInfo = new OamInfo();
				oamMepInfo = new OamMepInfo();
				oamMepInfo.setServiceId(elineInfo.getId());
				oamMepInfo.setObjType(EServiceType.ELINE.toString());
				oamInfo.setOamMep(oamMepInfo);

				oamMipInfo = new OamMipInfo();
				oamMipInfo.setServiceId(elineInfo.getId());
				oamMipInfo.setObjType(EServiceType.ELINE.toString());
				oamInfo.setOamMip(oamMipInfo);
				elineInfo.setOamList(oamInfoService.queryByServiceId(oamInfo));
			}

		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			// UiUtil.closeService(oamInfoService);
		}
		return elineInfoList;
	}

	/**
	 * 搜索eline
	 * 
	 * @param elineInfos
	 */
	public void doSearch(List<ElineInfo> elineInfos) {
		String ids = new String("(");
		for (ElineInfo elineInfo : elineInfos) {
			ids = ids + elineInfo.getId() + ",";
		}
		ids = ids.substring(0, ids.length() - 1) + ")";
		String name = elineInfos.get(0).getaAcId() + "_" + System.currentTimeMillis();
		int s1Id = elineInfos.get(0).getId();
		int s2Id = elineInfos.get(1).getId();
		try {
			mapper.doSearche_insert(name, s1Id, s2Id);
			mapper.deleteByIds(ids);
			sqlSession.commit();
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
	}

	public List<ElineInfo> select() throws Exception {
		List<ElineInfo> elineinfoList = null;
		OamInfo oamInfo;
		OamMepInfo oamMepInfo;
		OamMipInfo oamMipInfo;
		OamInfoService_MB oamInfoService = null;
		try {
			ElineInfo elineinfo = new ElineInfo();
			elineinfoList = new ArrayList<ElineInfo>();
			elineinfoList = this.mapper.queryByCondition(elineinfo);
			oamInfoService = (OamInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.OamInfo, this.sqlSession);
			if (elineinfoList != null && elineinfoList.size() > 0) {
				for (ElineInfo elineInfo : elineinfoList) {
					oamInfo = new OamInfo();
					oamMepInfo = new OamMepInfo();
					oamMepInfo.setServiceId(elineInfo.getId());
					oamMepInfo.setObjType("ELINE");
					oamInfo.setOamMep(oamMepInfo);
					oamMipInfo = new OamMipInfo();
					oamMipInfo.setServiceId(elineInfo.getId());
					oamMipInfo.setObjType("ELINE");
					oamInfo.setOamMip(oamMipInfo);
					elineInfo.setOamList(oamInfoService.queryByServiceId(oamInfo));
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			// UiUtil.closeService(oamInfoService);
		}
		return elineinfoList;
	}

	/**
	 * 验证名字是否重复
	 * 
	 * @author kk
	 * 
	 * @param afterName
	 *            修改之后的名字
	 * @param beforeName
	 *            修改之前的名字
	 * 
	 * @return
	 * @throws Exception
	 * 
	 * @Exception 异常对象
	 */
	public boolean nameRepetition(String afterName, String beforeName, int siteId) throws Exception {

		int result = this.mapper.query_name(afterName, beforeName, siteId);
		if (0 == result) {
			return false;
		} else {
			return true;
		}
	}

	public List<ElineInfo> selectElineBySite(int siteId) {
		List<ElineInfo> elineInfoList = new ArrayList<ElineInfo>();
		AcPortInfoService_MB acService = null;
		PwInfoService_MB pwService = null;
		try {
			acService = (AcPortInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.AcInfo, this.sqlSession);
			pwService = (PwInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PwInfo, this.sqlSession);
			elineInfoList = this.mapper.queryNodeBySite(siteId);
			for (ElineInfo elineInfo : elineInfoList) {
				elineInfo.setNode(true);
				elineInfo.getAcPortList().add(this.getAcInfo(siteId, elineInfo, acService));
				elineInfo.getPwNniList().add(this.getPwNniInfo(siteId, elineInfo, pwService));
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return elineInfoList;
	}

	private AcPortInfo getAcInfo(int siteId, ElineInfo elineInfo, AcPortInfoService_MB acService) throws Exception {
		int id = 0;
		if (elineInfo.getaSiteId() == siteId) {
			id = elineInfo.getaAcId();
		} else {
			id = elineInfo.getzAcId();
		}
		return acService.selectById(id);
	}

	private PwNniInfo getPwNniInfo(int siteId, ElineInfo elineInfo, PwInfoService_MB pwService) throws Exception {
		PwInfo pw = new PwInfo();
		pw.setPwId(elineInfo.getPwId());
		pw = pwService.selectBypwid_notjoin(pw);
		if (pw != null) {
			if (pw.getASiteId() == siteId) {
				return pw.getaPwNniInfo();
			} else if (pw.getZSiteId() == siteId) {
				return pw.getzPwNniInfo();
			}
		}
		return null;
	}

	public void updateActiveStatusByType(int siteId, int status, int type) throws Exception {
		try {
			mapper.updateStatusByType(siteId, status, type);
			sqlSession.commit();
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
	}

	/**
	 * 查询单网元下的所有eline
	 * 
	 * @param siteId
	 *            网元id
	 * @return
	 * @throws Exception
	 */
	public List<ElineInfo> select_synchro(int siteId, int xcid) throws Exception {

		List<ElineInfo> elineInfoList = null;
		try {
			elineInfoList = this.mapper.querySynchro(siteId, xcid);

		} catch (Exception e) {
			throw e;
		}
		return elineInfoList;
	}

	public int save(ElineInfo elineinfo) throws Exception, BusinessIdException {

		if (elineinfo == null) {
			throw new Exception("elineinfo is null");
		}

		int result = 0;
		OamInfoService_MB oamInfoService = null;
		SiteService_MB siteService = null;
		BusinessidMapper businessidMapper = null;
		PwInfoMapper pwInfoMapper = null;
		AcPortInfoMapper acportInfoMapper = null;
		try {
			siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE, this.sqlSession);
			oamInfoService = (OamInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.OamInfo, this.sqlSession);
			businessidMapper = sqlSession.getMapper(BusinessidMapper.class);
			pwInfoMapper = sqlSession.getMapper(PwInfoMapper.class);
			acportInfoMapper = sqlSession.getMapper(AcPortInfoMapper.class);
			// 如果A端配置网元和端口。取设备ID
			if (elineinfo.getaAcId() != 0) {
				Businessid aElineServiceId = null;
				if (elineinfo.getaXcId() == 0) {
					aElineServiceId = businessidMapper.queryList(elineinfo.getaSiteId(), "eline").get(0);
				} else {
					aElineServiceId = businessidMapper.queryByIdValueSiteIdtype(elineinfo.getaXcId(), elineinfo.getaSiteId(), "eline");
				}
				if (aElineServiceId == null) {
					throw new BusinessIdException(siteService.getSiteName(elineinfo.getaSiteId()) + ResourceUtil.srcStr(StringKeysTip.TIP_ELINEID));
				}

				elineinfo.setaXcId(aElineServiceId.getIdValue());
				Businessid bCondition = new Businessid();
				bCondition.setId(aElineServiceId.getId());
				bCondition.setIdStatus(1);
				businessidMapper.update(bCondition);
			}
			// 如果Z端配置网元和端口。取设备ID
			if (elineinfo.getzAcId() != 0) {
				Businessid zElineServiceId = null;
				if (elineinfo.getzXcId() == 0) {
					zElineServiceId = businessidMapper.queryList(elineinfo.getzSiteId(), "eline").get(0);
				} else {
					zElineServiceId = businessidMapper.queryByIdValueSiteIdtype(elineinfo.getzXcId(), elineinfo.getzSiteId(), "eline");
				}
				if (zElineServiceId == null) {
					throw new BusinessIdException(siteService.getSiteName(elineinfo.getzSiteId()) + ResourceUtil.srcStr(StringKeysTip.TIP_ELINEID));
				}

				elineinfo.setzXcId(zElineServiceId.getIdValue());
				Businessid bCondition = new Businessid();
				bCondition.setId(zElineServiceId.getId());
				bCondition.setIdStatus(1);
				businessidMapper.update(bCondition);
			}
			mapper.insert(elineinfo);
			result = elineinfo.getId();

			pwInfoMapper.setUser(elineinfo.getPwId(), result, EServiceType.ELINE.getValue());
			// 判断ac不等于0 就修改ac状态
			if (elineinfo.getaAcId() != 0) {
				acportInfoMapper.setUser(elineinfo.getaAcId(), 1);
			}
			if (elineinfo.getzAcId() != 0) {
				acportInfoMapper.setUser(elineinfo.getzAcId(), 1);
			}

			List<OamInfo> oamList = elineinfo.getOamList();
			for (OamInfo oamInfo : oamList) {
				if (oamInfo.getOamType() == OamTypeEnum.AMEP) {
					oamInfo.getOamMep().setServiceId(result);
					oamInfo.getOamMep().setObjId(elineinfo.getaXcId());
					oamInfo.setOamType(OamTypeEnum.AMEP);
				} else if (oamInfo.getOamType() == OamTypeEnum.ZMEP) {
					oamInfo.getOamMep().setServiceId(result);
					oamInfo.getOamMep().setObjId(elineinfo.getzXcId());
					oamInfo.setOamType(OamTypeEnum.ZMEP);
				}
				if (oamInfo.getOamType() == OamTypeEnum.MIP) {

				}
				oamInfoService.saveOrUpdate(oamInfo);
			}

			// 离线网元数据下载
			if (0 != elineinfo.getaSiteId()) {
				super.dateDownLoad(elineinfo.getaSiteId(), result, EServiceType.ELINE.getValue(), EActionType.INSERT.getValue());
			}
			if (0 != elineinfo.getzSiteId()) {
				super.dateDownLoad(elineinfo.getzSiteId(), result, EServiceType.ELINE.getValue(), EActionType.INSERT.getValue());
			}
			sqlSession.commit();
		} catch (BusinessIdException e) {
			throw e;
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			businessidMapper = null;
			pwInfoMapper = null;
			acportInfoMapper = null;
		}
		return result;
	}

	public int update(ElineInfo elineinfo) throws Exception {

		if (elineinfo == null) {
			throw new Exception("elineinfo is null");
		}

		int result = 0;
		ElineInfo elineBefore = null;
		Businessid businessId = null;
		SiteService_MB siteService = null;
		BusinessidMapper businessidMapper = null;
		PwInfoMapper pwInfoMapper = null;
		AcPortInfoMapper acportInfoMapper = null;
		try {
			siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE, this.sqlSession);
			businessidMapper = sqlSession.getMapper(BusinessidMapper.class);
			pwInfoMapper = sqlSession.getMapper(PwInfoMapper.class);
			acportInfoMapper = sqlSession.getMapper(AcPortInfoMapper.class);

			elineBefore = new ElineInfo();
			elineBefore.setId(elineinfo.getId());
			elineBefore = mapper.queryByCondition_notjoin(elineBefore).get(0);

			result = mapper.updateByPrimaryKey(elineinfo);

			if (elineBefore.getPwId() != elineinfo.getPwId()) {
				if (!isRelatedPW(elineBefore.getPwId())) {
					pwInfoMapper.setUser(elineBefore.getPwId(), 0, 0);
				}
				pwInfoMapper.setUser(elineinfo.getPwId(), elineinfo.getId(), EServiceType.ELINE.getValue());
			}

			// 释放之前的id
			businessId = new Businessid();
			businessId.setIdStatus(0);
			businessId.setIdValue(elineBefore.getaXcId());
			businessId.setSiteId(elineBefore.getaSiteId());
			businessId.setType("eline");
			businessidMapper.updateBusinessid(businessId);
			// 释放之前的id
			businessId = new Businessid();
			businessId.setIdStatus(0);
			businessId.setIdValue(elineBefore.getzXcId());
			businessId.setSiteId(elineBefore.getzSiteId());
			businessId.setType("eline");
			businessidMapper.updateBusinessid(businessId);

			// 判断ac不等于0 就修改ac状态
			if (elineinfo.getaAcId() != 0) {
				if (elineBefore.getaAcId() != elineinfo.getaAcId()) {
					if (!isRelatedAC(elineBefore.getaAcId())) {
						acportInfoMapper.setUser(elineBefore.getaAcId(), 0);
					}
					acportInfoMapper.setUser(elineinfo.getaAcId(), 1);
				}

				if (elineinfo.getaXcId() != 0) {
					businessId = businessidMapper.queryByIdValueSiteIdtype(elineinfo.getaXcId(), elineinfo.getaSiteId(), "eline");
				} else {
					businessId = businessidMapper.queryList(elineinfo.getaSiteId(), "eline").get(0);
				}

				if (businessId == null) {
					throw new BusinessIdException(siteService.getSiteName(elineinfo.getaSiteId()) + ResourceUtil.srcStr(StringKeysTip.TIP_ELINEID));
				} else {
					elineinfo.setaXcId(businessId.getIdValue());
					businessId.setIdStatus(1);
					businessidMapper.update(businessId);
				}
			}
			if (elineinfo.getzAcId() != 0) {
				if (elineBefore.getzAcId() != elineinfo.getzAcId()) {
					if (!isRelatedAC(elineBefore.getzAcId())) {
						acportInfoMapper.setUser(elineBefore.getzAcId(), 0);
					}
					acportInfoMapper.setUser(elineinfo.getzAcId(), 1);
				}

				if (elineinfo.getzXcId() != 0) {
					businessId = businessidMapper.queryByIdValueSiteIdtype(elineinfo.getzXcId(), elineinfo.getzSiteId(), "eline");
				} else {
					businessId = businessidMapper.queryList(elineinfo.getzSiteId(), "eline").get(0);
				}
				if (businessId == null) {
					throw new BusinessIdException(siteService.getSiteName(elineinfo.getzSiteId()) + ResourceUtil.srcStr(StringKeysTip.TIP_ELINEID));
				} else {
					elineinfo.setzXcId(businessId.getIdValue());
					businessId.setIdStatus(1);
					businessidMapper.update(businessId);
				}
			}
			// 离线网元数据下载
			if (0 != elineinfo.getaSiteId()) {
				super.dateDownLoad(elineinfo.getaSiteId(), result, EServiceType.ELINE.getValue(), EActionType.UPDATE.getValue());
			}
			if (0 != elineinfo.getzSiteId()) {
				super.dateDownLoad(elineinfo.getzSiteId(), result, EServiceType.ELINE.getValue(), EActionType.UPDATE.getValue());
			}
			sqlSession.commit();
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {

		}
		return result;
	}

	/**
	 * 在删除之前判断着PW是否存在其他的业务的关联
	 * 
	 * @param pwId
	 * @return true存在 false 不存在
	 */
	private boolean isRelatedPW(int pwId) {
		try {
			int i = this.mapper.isRelatedPW(pwId);
			if (i > 0) {
				return true;
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, getClass());
		}
		return false;
	}

	/**
	 * 在删除之前判断着AC是否存在其他的业务的关联
	 * 
	 * @param AcId
	 * @return true存在 false 不存在
	 */
	private boolean isRelatedAC(int acId){
		List<ElineInfo> elineInfos = null;
		int isRelatedAc = 0;
		try {
			isRelatedAc = this.mapper.isRelatedAcByVPWS(acId);
			if(isRelatedAc >0)
			{
				return true;
			}else
			{
				elineInfos = this.mapper.isRelatedACByVPLS(acId);
				if(elineInfos.size()>0){
					for(ElineInfo elineInfo :elineInfos){
						if(elineInfo.getAmostAcId() != null && !elineInfo.getAmostAcId().equals("")){
							for(String str : elineInfo.getAmostAcId().split(",")){
								if(acId == Integer.parseInt(str)){
									return true;
								}
							}
						}
					}
				}else{
					return false;
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, getClass());
		}
		return false;
	}
	
	public static void main(String[] args) {
		try {
			Mybatis_DBManager.init("10.18.1.10");
			ConstantUtil.serviceFactory = new ServiceFactory();
			ElineInfoService_MB tunnelServiceMB = (ElineInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.Eline);
			List<ElineInfo> elineInfos = tunnelServiceMB.getElineInfoMapper().isRelatedACByVPLS(1);
			System.out.println(elineInfos == null);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}

	}

	public List<ElineInfo> selectElineByPwId(List<Integer> pwIdList) {
		List<ElineInfo> list = null;
		try {
			list = this.mapper.queryAllElineByPwId(pwIdList);
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return list;
	}

	public List<ElineInfo> selectByPwId(ElineInfo eline) {
		List<ElineInfo> list = new ArrayList<ElineInfo>();
		try {
			list = this.mapper.selectByPwId(eline.getPwId());
			if(list == null){
				list = new ArrayList<ElineInfo>();
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return list;
	}
	
	public boolean isElineVPWS(int siteId, int serviceId) throws Exception {
		boolean flag = false;
		List<ElineInfo> elineInfoList = null;
		
		try {
			elineInfoList = this.mapper.queryNodeBySiteAndServiceId(siteId, serviceId);
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		
		if(elineInfoList!=null && elineInfoList.size()>0)
		{
			flag = true;
		}
		
		return flag;
	}
	
	public void delete(List<ElineInfo> elineList) throws Exception {
		OamInfo oamInfo = null;
		OamMepInfo oamMepInfo = null;
		OamInfoService_MB oamInfoService = null;
		PwInfoMapper pwInfoMapper = null;
		AcPortInfoMapper acPortInfoMapper = null;
		BusinessidMapper businessidMapper = null;
		try {
			oamInfoService = (OamInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.OamInfo, this.sqlSession);
			pwInfoMapper = sqlSession.getMapper(PwInfoMapper.class);
			acPortInfoMapper = sqlSession.getMapper(AcPortInfoMapper.class);
			businessidMapper = sqlSession.getMapper(BusinessidMapper.class);
			for (ElineInfo eline : elineList) {
				oamInfo = new OamInfo();
				oamMepInfo = new OamMepInfo();
				oamMepInfo.setServiceId(eline.getId());
				oamMepInfo.setObjType(EServiceType.ELINE.toString());
				oamInfo.setOamMep(oamMepInfo);
				oamInfoService.delete(oamInfo);

				// 释放pw使权限
				if(!isRelatedPW(eline.getPwId())){
					pwInfoMapper.setUser(eline.getPwId(), 0, 0);
				}

				// 释放AC使用权限
				if (eline.getaAcId() != 0) {
					if(!isRelatedAC(eline.getaAcId())){
						acPortInfoMapper.setUser(eline.getaAcId(), 0);
					}
				}
				if (eline.getzAcId() != 0) {
					if(!isRelatedAC(eline.getzAcId())){
						acPortInfoMapper.setUser(eline.getzAcId(), 0);
					}
				}

				// 释放id
				Businessid businessId = new Businessid();
				businessId.setIdStatus(0);
				businessId.setIdValue(eline.getaXcId());
				businessId.setType("eline");
				businessId.setSiteId(eline.getaSiteId());
				businessidMapper.updateBusinessid(businessId);
				businessId.setIdValue(eline.getzXcId());
				businessId.setSiteId(eline.getzSiteId());
				businessidMapper.updateBusinessid(businessId);
				mapper.deleteByPrimaryKey(eline.getId());
				//离线网元操作
				offLineAcion(eline);
			}
			sqlSession.commit();
		} catch (Exception e) {
			sqlSession.rollback();
			ExceptionManage.dispose(e,this.getClass());
		} finally{
			pwInfoMapper = null;
			acPortInfoMapper = null;
			businessidMapper = null;
		}

	}
	
	/**
	 * 离线网元操作
	 * @param etreeInfoList
	 * @param pwService
	 * @throws Exception
	 */
	private void offLineAcion(ElineInfo offLineAction) throws Exception {
		PwInfoService_MB pwService = (PwInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PwInfo, this.sqlSession);
		AcPortInfoService_MB acInfoService= (AcPortInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.AcInfo, this.sqlSession);
		AcPortInfo acPortInfo;
		PwInfo pwInfo = new PwInfo();
		pwInfo.setPwId(offLineAction.getPwId());
		pwInfo = pwService.selectBypwid_notjoin(pwInfo);
		List<AcPortInfo> acPortInfoList = null;
		 //a段网元
		if(0!=offLineAction.getaSiteId()){
			acPortInfo = new AcPortInfo();
			acPortInfo.setId(offLineAction.getaAcId());
			acPortInfoList = acInfoService.selectByCondition(acPortInfo);
			if(null!=acPortInfoList&&acPortInfoList.size()>0){
				acPortInfo = acPortInfoList.get(0);
				super.dateDownLoad(offLineAction.getaSiteId(),offLineAction.getServiceId(), EServiceType.ELINE.getValue(), EActionType.DELETE.getValue(),offLineAction.getaXcId()+"",pwInfo.getApwServiceId()+"",acPortInfo.getPortId(),acPortInfo.getAcBusinessId(),null);
			}
		}
		 //z段网元
		if(0!=offLineAction.getzSiteId()){
			acPortInfo = new AcPortInfo();
			acPortInfo.setId(offLineAction.getzAcId());
			acPortInfoList = acInfoService.selectByCondition(acPortInfo);
		 	if(null!=acPortInfoList&&acPortInfoList.size()>0){
		 		acPortInfo = acPortInfoList.get(0);
		 		super.dateDownLoad(offLineAction.getzSiteId(),offLineAction.getServiceId(), EServiceType.ELINE.getValue(), EActionType.DELETE.getValue(),offLineAction.getzXcId()+"",pwInfo.getZpwServiceId()+"",acPortInfo.getPortId(),acPortInfo.getAcBusinessId(),null);
			}
		}
	}
	
	public void updateActiveStatus(List<Integer> idList, int status) throws Exception {
		try {
			mapper.updateStatus(idList, status);
			sqlSession.commit();
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} 

	}
	
	/**
	 * 根据主键id获取业务类型(针对所有业务)
	 * @param serviceId
	 * @return
	 */
	public int getServiceTypeByServiceId(int serviceId){
		int serviceType = mapper.getServiceTypeByServiceId(serviceId);
		return serviceType;
	}
	
	/**
	 * 通过网元id初始化某网元所有eline
	 * @param siteId
	 * @throws SQLException
	 */
	public void initializtionSite(int siteId) throws SQLException{
		List<ElineInfo> elineInfos = null;
		List<ElineInfo> singleElines = null;
		List<ElineInfo> eInfos = null;
		try {
			elineInfos = this.selectNodeBySite(siteId);
			singleElines = new ArrayList<ElineInfo>();
			eInfos = new ArrayList<ElineInfo>();
			if(elineInfos != null && elineInfos.size()>0){
				for(ElineInfo elineInfo : elineInfos){
					if(elineInfo.getIsSingle() ==1){//单网元数据，直接删除
						singleElines.add(elineInfo);
					}else{//网络侧的，初始化该网元，并成为单网元业务
						if(elineInfo.getaSiteId() == siteId){
							elineInfo.setaAcId(0);
							elineInfo.setAportId(0);
							elineInfo.setaXcId(0);
							elineInfo.setASiteName("");
							elineInfo.setaSiteId(0);
							elineInfo.setIsSingle(1);
						}else{
							elineInfo.setzAcId(0);
							elineInfo.setZportId(0);
							elineInfo.setzXcId(0);
							elineInfo.setZSiteName("");
							elineInfo.setzSiteId(0);
							elineInfo.setIsSingle(1);
						}
						eInfos.add(elineInfo);
					}
					
				}
			}
			this.delete(singleElines);
			this.update(eInfos);
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}finally{
			elineInfos = null;
			singleElines = null;
			eInfos = null;
		}
	}
	
	public void update(List<ElineInfo> elineinfoList) throws Exception {
		for (ElineInfo elineInfo : elineinfoList) {
			this.update(elineInfo);
		}
	}
}
