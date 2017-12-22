package com.nms.model.ptn.path.ces;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.nms.db.bean.equipment.port.PortInst;
import com.nms.db.bean.equipment.port.PortStmTimeslot;
import com.nms.db.bean.ptn.Businessid;
import com.nms.db.bean.ptn.path.ces.CesInfo;
import com.nms.db.bean.ptn.path.pw.PwInfo;
import com.nms.db.dao.equipment.port.PortInstMapper;
import com.nms.db.dao.equipment.port.PortStmTimeslotMapper;
import com.nms.db.dao.ptn.BusinessidMapper;
import com.nms.db.dao.ptn.path.ces.CesInfoMapper;
import com.nms.db.dao.ptn.path.pw.PwInfoMapper;
import com.nms.db.enums.EActionType;
import com.nms.db.enums.ECesType;
import com.nms.db.enums.EManufacturer;
import com.nms.db.enums.EPwType;
import com.nms.db.enums.EServiceType;
import com.nms.model.equipment.port.PortService_MB;
import com.nms.model.equipment.port.PortStmTimeslotService_MB;
import com.nms.model.equipment.shlef.SiteService_MB;
import com.nms.model.ptn.path.pw.PwInfoService_MB;
import com.nms.model.util.ObjectService_Mybatis;
import com.nms.model.util.Services;
import com.nms.ui.manager.BusinessIdException;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.ResourceUtil;
import com.nms.ui.manager.keys.StringKeysTip;

public class CesInfoService_MB extends ObjectService_Mybatis{
	public void setPtnuser(String ptnuser) {
		super.ptnuser = ptnuser;
	}

	public void setSqlSession(SqlSession sqlSession) {
		super.sqlSession = sqlSession;
	}
	private CesInfoMapper cesInfoMapper;
	
	public CesInfoMapper getCesInfoMapper() {
		return cesInfoMapper;
	}

	public void setCesInfoMapper(CesInfoMapper cesInfoMapper) {
		this.cesInfoMapper = cesInfoMapper;
	}
	
	/**
	 * 过滤查询，ces列表页面用
	 * @param cesInfo 过滤条件
	 * @return
	 * @throws Exception
	 */
	public List<CesInfo> filterSelect(CesInfo cesInfo) throws Exception{
		return this.cesInfoMapper.filterQuery(cesInfo);
	}
	
	/**
	 * 查询单网元下的ces
	 * 
	 * @param siteId
	 * @return
	 * @throws Exception
	 */
	public List<CesInfo> selectNodeBySite(int siteId) throws Exception {
		List<CesInfo> cesinfos = null;
		try {
			cesinfos = this.cesInfoMapper.queryNodeBySite(siteId);

			for (CesInfo cesInfo : cesinfos) {
				cesInfo.setNode(true);
			}

		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}

		return cesinfos;
	}

	public List<CesInfo> filterSingle(CesInfo cesInfo, int siteId) throws Exception{
		return this.cesInfoMapper.filterSingle(cesInfo, siteId);
	}
	
	
	public CesInfo selectByid(CesInfo cesInfo) throws Exception {
		CesInfo cesInfo2 = null;
		List<CesInfo> cesInfoList = null;
		try {
			cesInfoList = new ArrayList<CesInfo>();
			cesInfo2 =new CesInfo();
			cesInfoList = this.cesInfoMapper.queryByIdCondition(cesInfo);
			if(cesInfoList!=null && cesInfoList.size()>0){
				cesInfo2=cesInfoList.get(0);
			}
			
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return cesInfo2;
	}
	
	public void doSearch(List<CesInfo> cesInfos) throws Exception{
		String ids = new String("(");
		for (CesInfo cesInfo : cesInfos) {
			ids = ids + cesInfo.getId() + ",";
		}
		ids = ids.substring(0, ids.length() - 1) + ")";
		String name = "_"+System.currentTimeMillis();
		int s1Id = cesInfos.get(0).getId();
		int s2Id = cesInfos.get(1).getId();
		this.cesInfoMapper.doSearche_insert(name,s1Id,s2Id);
		this.cesInfoMapper.deleteByIds(ids);
		sqlSession.commit();
	}
	
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<CesInfo> select() throws Exception {
		List<CesInfo> cesInfoList = null;
		try {
			cesInfoList = new ArrayList<CesInfo>();
			cesInfoList = this.cesInfoMapper.queryByCondition(new CesInfo());
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return cesInfoList;
	}
	
	/**
	 * 获取ces所用到端口名称 显示用
	 * 
	 * @param cesInfo
	 *            ces业务对象
	 * @param type
	 *            表示A或Z端
	 * @return
	 * @throws Exception
	 */
	public String getCesPortName(CesInfo cesInfo, String type) throws Exception {

		int portid = 0;
		String type_port = null;
		PortService_MB portService = null;
		PortInst portinst = null;
		List<PortInst> portInstList = null;
		PortStmTimeslotService_MB portStmTimeslotService = null;
		PortStmTimeslot portStmTimeslot = null;
		String result = null;
		try {

			if ("a".equals(type)) {
				if (cesInfo.getCestype() == ECesType.PDH.getValue() || cesInfo.getCestype() == ECesType.PDHSDH.getValue()) {
					type_port = "pdh";
				} else {
					type_port = "sdh";
				}
				portid = cesInfo.getaAcId();
			} else {
				if (cesInfo.getCestype() == ECesType.PDH.getValue() || cesInfo.getCestype() == ECesType.SDHPDH.getValue()) {
					type_port = "pdh";
				} else {
					type_port = "sdh";
				}
				portid = cesInfo.getzAcId();
			}

			if ("pdh".equals(type_port)) {
				portService = (PortService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORT, this.sqlSession);
				portinst = new PortInst();
				portinst.setPortId(portid);
				portInstList = new ArrayList<PortInst>();
				portInstList = portService.select(portinst);

				if (null != portInstList && portInstList.size() == 1) {
					result = portInstList.get(0).getPortName();
				} else {
					result = "";
				}
			} else {
				portStmTimeslotService = (PortStmTimeslotService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORTSTMTIMESLOT, this.sqlSession);
				portStmTimeslot =new PortStmTimeslot();
				portStmTimeslot = portStmTimeslotService.selectById(portid);

				if (null != portStmTimeslot) {
					result = portStmTimeslot.getTimeslotnumber();
				} else {
					result = "";
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			type_port = null;
			portinst = null;
			portInstList = null;
			portStmTimeslot = null;
		}
		return result;
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
	public boolean nameRepetition(String afterName, String beforeName,int siteId) throws Exception {

		int result = this.cesInfoMapper.query_name(afterName, beforeName, siteId);
		if (0 == result) {
			return false;
		} else {
			return true;
		}

	}

	public List<CesInfo> selectByPwId(CesInfo ces) {
		List<CesInfo> list = new ArrayList<CesInfo>();
		try {
			list = this.cesInfoMapper.selectByPwId(ces.getPwId());
			if(list == null){
				list = new ArrayList<CesInfo>();
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return list;
	}
	
	public List<CesInfo> queryByCondition_nojoin(CesInfo cesinfoCondition) throws Exception {
		return this.cesInfoMapper.queryByCondition_nojoin(cesinfoCondition);
	}
	
	
	/**
	 * 根据主键ID 查询CES（部分网络层，单网元）
	 * @param id
	 * @return
	 */
	public CesInfo selectServiceInfoById(int id) {
		List<CesInfo> cesList = null;
		CesInfo cesInfo=null;
		try {
			cesList = this.cesInfoMapper.selectServiceInfoById(id);
			if(cesList != null && cesList.size()>0){
				cesInfo=cesList.get(0);
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return cesInfo;
	}
	
	public void update(CesInfo cesInfo) throws Exception {

		if (cesInfo == null) {
			throw new Exception("pwinfo is null");
		}
		CesInfo beforCes = null;
		PwInfoMapper pwInfoMapper = null;
		try {
			beforCes = new CesInfo();
			beforCes.setId(cesInfo.getId());
			beforCes = cesInfoMapper.queryByCondition_nojoin(beforCes).get(0);
			pwInfoMapper = sqlSession.getMapper(PwInfoMapper.class);
			this.cesInfoMapper.updateByPrimaryKey(cesInfo);

			if (beforCes.getPwId() != cesInfo.getPwId()) {
				pwInfoMapper.setUser(beforCes.getPwId(), 0, 0);
				pwInfoMapper.setUser(cesInfo.getPwId(), cesInfo.getId(), EServiceType.CES.getValue());
			}
			setUsedForPort(beforCes,0);//释放修改过的端口端口或者时隙也被使用
			setUsedForPort(cesInfo, 1); // 更新端口或者时隙也被使用
			
			//离线网元数据下载
			if(0!=cesInfo.getaSiteId()){
				super.dateDownLoad(cesInfo.getaSiteId(),cesInfo.getId(), EServiceType.CES.getValue(), EActionType.UPDATE.getValue());
			}
			if(0!=cesInfo.getzSiteId()){
				super.dateDownLoad(cesInfo.getzSiteId(),cesInfo.getId(), EServiceType.CES.getValue(), EActionType.UPDATE.getValue());
			}
			sqlSession.commit();
		} catch (Exception e) {
			sqlSession.rollback();
			ExceptionManage.dispose(e,this.getClass());
		} finally{
			pwInfoMapper = null;
		}
	}
	
	private void setUsedForPort(CesInfo cesInfo, int isused) {
		List<Integer> pdhportList = null; // e1
		PortStmTimeslotMapper portStmTimeslotMapper = null;
		PortInstMapper portInstMapper = null;
		try {
			portStmTimeslotMapper = sqlSession.getMapper(PortStmTimeslotMapper.class);
			portInstMapper = sqlSession.getMapper(PortInstMapper.class);
			pdhportList = new ArrayList<Integer>();
			if (cesInfo.getCestype() == ECesType.SDH.getValue()) {
				portStmTimeslotMapper.setUsed(cesInfo.getaAcId(), isused);
				portStmTimeslotMapper.setUsed(cesInfo.getzAcId(), isused);
			} else if (cesInfo.getCestype() == ECesType.SDHPDH.getValue()) {
				portStmTimeslotMapper.setUsed(cesInfo.getaAcId(), isused);
				
				pdhportList.add(cesInfo.getzAcId());
				portInstMapper.updateOccupyByIdList(pdhportList, isused);
				
			} else if (cesInfo.getCestype() == ECesType.PDH.getValue()) {

				pdhportList.add(cesInfo.getzAcId());
				pdhportList.add(cesInfo.getaAcId());
				portInstMapper.updateOccupyByIdList(pdhportList, isused);

			} else if (cesInfo.getCestype() == ECesType.PDHSDH.getValue()) {
				portStmTimeslotMapper.setUsed(cesInfo.getzAcId(), isused);

				pdhportList.add(cesInfo.getaAcId());
				portInstMapper.updateOccupyByIdList(pdhportList, isused);
			}

		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			pdhportList = null;
			portStmTimeslotMapper = null;
			portInstMapper = null;
		}
	}
	
	public int save(CesInfo cesInfo) throws Exception,BusinessIdException {

		if (cesInfo == null) {
			throw new Exception("pwinfo is null");
		}
		int resultcesId = 0;
		Businessid aServiceId = null;
		Businessid zServiceId = null;
		SiteService_MB siteService = null;
		BusinessidMapper businessidMapper = null;
		PwInfoMapper pwInfoMapper = null;
		try {
			siteService=(SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE, this.sqlSession);
			businessidMapper = sqlSession.getMapper(BusinessidMapper.class);
			pwInfoMapper = sqlSession.getMapper(PwInfoMapper.class);
			if(cesInfo.getaSiteId()!=0){
				
				if(cesInfo.getAxcId()==0){
					if(siteService.getManufacturer(cesInfo.getaSiteId()) == EManufacturer.valueOf("WUHAN").getValue()){
						aServiceId = businessidMapper.queryList(cesInfo.getaSiteId(), "eline").get(0);
					}else{
						aServiceId = businessidMapper.queryList(cesInfo.getaSiteId(), "ces").get(0);
					}	
				}else {
					if(siteService.getManufacturer(cesInfo.getaSiteId()) == EManufacturer.valueOf("WUHAN").getValue()){
						aServiceId=businessidMapper.queryByIdValueSiteIdtype(cesInfo.getAxcId(), cesInfo.getaSiteId(), "eline");
					}else{
						aServiceId=businessidMapper.queryByIdValueSiteIdtype(cesInfo.getAxcId(), cesInfo.getaSiteId(), "ces");
					}
				}
				if (aServiceId == null) {
					throw new BusinessIdException(siteService.getSiteName(cesInfo.getaSiteId())+ResourceUtil.srcStr(StringKeysTip.TIP_CESID));
				}
				cesInfo.setAxcId(aServiceId.getIdValue());
				aServiceId.setIdStatus(1);
				businessidMapper.update(aServiceId);

			}
			if(cesInfo.getzSiteId()!=0){
				
				if(cesInfo.getZxcId()==0){
					if(siteService.getManufacturer(cesInfo.getzSiteId()) == EManufacturer.valueOf("WUHAN").getValue()){
						zServiceId = businessidMapper.queryList(cesInfo.getzSiteId(), "eline").get(0);
					}else{
						zServiceId = businessidMapper.queryList(cesInfo.getzSiteId(), "ces").get(0);
					}	
				}else {
					if(siteService.getManufacturer(cesInfo.getzSiteId()) == EManufacturer.valueOf("WUHAN").getValue()){
						zServiceId=businessidMapper.queryByIdValueSiteIdtype(cesInfo.getZxcId(), cesInfo.getzSiteId(), "eline");
					}else{
						zServiceId=businessidMapper.queryByIdValueSiteIdtype(cesInfo.getZxcId(), cesInfo.getzSiteId(), "ces");
					}
				}
				if (zServiceId == null) {
					throw new BusinessIdException(siteService.getSiteName(cesInfo.getzSiteId())+ResourceUtil.srcStr(StringKeysTip.TIP_CESID));
				}

				cesInfo.setZxcId(zServiceId.getIdValue());
				zServiceId.setIdStatus(1);
				businessidMapper.update(zServiceId);
			}
			cesInfoMapper.insert(cesInfo);
			resultcesId = cesInfo.getId();
			pwInfoMapper.setUser(cesInfo.getPwId(), resultcesId, EServiceType.CES.getValue());
			setUsedForPort(cesInfo, 1); // 更新端口或者时隙也被使用

			//离线网元数据下载
			if(0!=cesInfo.getaSiteId()){
				super.dateDownLoad(cesInfo.getaSiteId(),resultcesId, EServiceType.CES.getValue(), EActionType.INSERT.getValue());
			}
			if(0!=cesInfo.getzSiteId()){
				super.dateDownLoad(cesInfo.getzSiteId(),resultcesId, EServiceType.CES.getValue(), EActionType.INSERT.getValue());
			}
			sqlSession.commit();
		} catch (BusinessIdException e) {
			sqlSession.rollback();
			throw e;
		}catch (Exception e) {
			sqlSession.rollback();
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			businessidMapper = null;
			pwInfoMapper = null;
		}
		return resultcesId;
	}
	
	public void save(List<CesInfo> cesInfoList) throws Exception,BusinessIdException {
		for (CesInfo cesInfo : cesInfoList) {
			int cesid = this.save(cesInfo);
			cesInfo.setId(cesid);
		}
	}
	
	public void update(List<CesInfo> cesInfoList) throws Exception {
		for (CesInfo cesInfo : cesInfoList) {
			this.update(cesInfo);
		}
	}
	
	/**
	 * 批量删除
	 */
	public int delete(List<CesInfo> infos) throws Exception {
		int result = 0;
		List<Integer> idList = null;
		SiteService_MB siteService = null;
		PwInfoMapper  pwInfoMapper = null;
		BusinessidMapper businessidMapper = null;
		try {
			siteService=(SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE, this.sqlSession);
			pwInfoMapper = sqlSession.getMapper(PwInfoMapper.class);
			businessidMapper = sqlSession.getMapper(BusinessidMapper.class);
			
			// 解除pw与ces业务的引用关系
			for (CesInfo cesInfo : infos) {
				pwInfoMapper.setUser(cesInfo.getPwId(), 0, EServiceType.CES.getValue());
				setUsedForPort(cesInfo, 0); //修改端口使用状态
			}

			idList = new ArrayList<Integer>();
			for (CesInfo info : infos) {

				// 释放id
				Businessid businessId = new Businessid();
				businessId.setIdStatus(0);
				businessId.setIdValue(info.getAxcId());
				if (info.getaSiteId() > 0 && siteService.getManufacturer(info.getaSiteId()) == EManufacturer.valueOf("WUHAN").getValue()) {
					businessId.setType("eline");
				}else{
					businessId.setType("ces");
				}
				businessId.setSiteId(info.getaSiteId());
				businessidMapper.updateBusinessid(businessId);
				
				if (info.getzSiteId() > 0 && siteService.getManufacturer(info.getzSiteId()) == EManufacturer.valueOf("WUHAN").getValue()) {
					businessId.setType("eline");
				}else{
					businessId.setType("ces");
				}
				businessId.setIdValue(info.getZxcId());
				businessId.setSiteId(info.getzSiteId());
				businessidMapper.updateBusinessid(businessId);

				idList.add(info.getId());
				
				//离线网元数据下载
				offLineAcion(info);
				result = cesInfoMapper.deleteByPrimaryKey(info.getId());
				
			}
			sqlSession.commit();
			
		} catch (Exception e) {
			sqlSession.rollback();
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			pwInfoMapper = null;
			businessidMapper = null;
		}
		return result;
	}
	
	
	/**
	 * 离线网元操作
	 * @param etreeInfoList
	 * @param pwService
	 * @throws Exception
	 */
	private void offLineAcion(CesInfo cesInfo) throws Exception {
		PwInfoService_MB pwService = (PwInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PwInfo, this.sqlSession);
		PwInfo pwInfo = new PwInfo();
		int aPortType = 0;
		int zPortType = 0;
		if(EPwType.ETH.getValue()==cesInfo.getCestype()){
			aPortType=EPwType.ETH.getValue();
			zPortType=EPwType.ETH.getValue();
		}else if(EPwType.PDH.getValue()==cesInfo.getCestype()){
			aPortType=EPwType.PDH.getValue();
			zPortType=EPwType.PDH.getValue();
		}else if(EPwType.SDH.getValue()==cesInfo.getCestype()){
			aPortType=EPwType.SDH.getValue();
			zPortType=EPwType.SDH.getValue();
		}else if(EPwType.PDH_SDH.getValue()==cesInfo.getCestype()){
			aPortType=EPwType.PDH.getValue();
			zPortType=EPwType.SDH.getValue();
		}else if(EPwType.SDH_PDH.getValue()==cesInfo.getCestype()){
			aPortType=EPwType.SDH.getValue();
			zPortType=EPwType.PDH.getValue();
		}
		
		pwInfo.setPwId(cesInfo.getPwId());
		pwInfo = pwService.selectBypwid_notjoin(pwInfo);
		if(0!=cesInfo.getaSiteId()){
			super.dateDownLoad(cesInfo.getaSiteId(),cesInfo.getServiceId(), EServiceType.CES.getValue(), EActionType.DELETE.getValue(), cesInfo.getAxcId()+"",pwInfo.getApwServiceId()+"",cesInfo.getaAcId(),0,aPortType+"");
		}
		if(0!=cesInfo.getzSiteId()){
			super.dateDownLoad(cesInfo.getzSiteId(),cesInfo.getServiceId(),  EServiceType.CES.getValue(), EActionType.DELETE.getValue(), cesInfo.getZxcId()+"",pwInfo.getZpwServiceId()+"",cesInfo.getzAcId(),0,zPortType+"");
			
		}
	}
	
	public void updateActiveStatus(List<Integer> idList, int status) throws Exception {

		try {
			cesInfoMapper.updateStatus(idList, status);
			sqlSession.commit();
		} catch (Exception e) {
			sqlSession.rollback();
			ExceptionManage.dispose(e,this.getClass());
		} 

	}	
	
	/**
	 * 同步时查询ces
	 * 
	 * @param siteId
	 *            网元id
	 * @param xcid
	 *            设备名称
	 * @return
	 * @throws Exception
	 */
	public List<CesInfo> select_synchro(int siteId, int xcid) throws Exception {

		List<CesInfo> cesInfoList = null;
		try {
			cesInfoList = this.cesInfoMapper.querySynchro(siteId, xcid);
		} catch (Exception e) {
			throw e;
		}
		return cesInfoList;
	}

	public void updateActiveStatus(int siteId, int value) {
		cesInfoMapper.updateStatusBysiteId(siteId, value);
	}
	
	/**
	 * 通过网元id初始化某网元所有ces
	 * 
	 * @param siteId
	 * @throws SQLException
	 */
	public void initializtionSite(int siteId) throws SQLException{
		List<CesInfo> cesInfos = null;
		List<CesInfo> singleCesInfos = null;
		List<CesInfo> infos = null;
		try {
			cesInfos = this.selectNodeBySite(siteId);
			if(cesInfos != null && cesInfos.size()>0){
				singleCesInfos = new ArrayList<CesInfo>();
				infos = new ArrayList<CesInfo>();
				for(CesInfo cesInfo : cesInfos){
					if(cesInfo.getIsSingle() == 1){//单网元数据，直接删除
						singleCesInfos.add(cesInfo);
					}else{//网络侧的，初始化该网元，并成为单网元业务
						if(cesInfo.getaSiteId() == siteId){
							cesInfo.setaAcId(0);
							cesInfo.setAportId(0);
							cesInfo.setAxcId(0);
							cesInfo.setASiteName("");
							cesInfo.setaSiteId(0);
							cesInfo.setIsSingle(1);
						}else{
							cesInfo.setzAcId(0);
							cesInfo.setZportId(0);
							cesInfo.setZxcId(0);
							cesInfo.setZSiteName("");
							cesInfo.setzSiteId(0);
							cesInfo.setIsSingle(1);
						}
						infos.add(cesInfo);
					}
				}
				update(infos);
				delete(singleCesInfos);
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}finally{
			cesInfos = null;
			singleCesInfos = null;
			infos = null;
		}
	}

	public List<CesInfo> selectAll_node(int siteId) {
		try {
			List<CesInfo> cesList = this.cesInfoMapper.queryBySingle(siteId, 1);
			if(cesList == null){
				cesList = new ArrayList<CesInfo>();
			}
			return cesList;
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
			return new ArrayList<CesInfo>();
		}
		
	}

	public CesInfo queryBySiteAndId(int siteId, int id) {
		List<CesInfo> cesList = new ArrayList<CesInfo>();
		CesInfo cesInfo=null;
		try {
			cesList = this.cesInfoMapper.selectServiceInfoById(id);
			if(cesList.size()==1){
				cesInfo=cesList.get(0);
				if(siteId!=cesInfo.getaSiteId()&&siteId!=cesInfo.getzSiteId()){
					cesInfo=null;
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return cesInfo;
	}
	
	public List<CesInfo> selectAll() {
		List<CesInfo> cesInfoList = null;
		try {
			cesInfoList = this.cesInfoMapper.selectAll();
			if(cesInfoList==null){
				cesInfoList= new ArrayList<CesInfo>();
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return cesInfoList;
	}

	public List<CesInfo> select(CesInfo cesInfo) throws Exception {
		List<CesInfo> cesInfoList = null;
		try {
			cesInfoList = this.cesInfoMapper.queryByCondition(cesInfo);
			if(cesInfoList==null){
				cesInfoList=new ArrayList<CesInfo>();
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return cesInfoList;
	}
}
