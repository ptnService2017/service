package com.nms.model.ptn.port;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.nms.db.bean.ptn.Businessid;
import com.nms.db.bean.ptn.oam.OamInfo;
import com.nms.db.bean.ptn.port.AcPortInfo;
import com.nms.db.bean.ptn.port.Acbuffer;
import com.nms.db.bean.ptn.port.PortLagInfo;
import com.nms.db.bean.ptn.qos.QosInfo;
import com.nms.db.bean.ptn.qos.QosRelevance;
import com.nms.db.dao.ptn.BusinessidMapper;
import com.nms.db.dao.ptn.port.AcPortInfoMapper;
import com.nms.db.dao.ptn.qos.QosInfoMapper;
import com.nms.db.dao.ptn.qos.QosRelevanceMapper;
import com.nms.db.enums.EActionType;
import com.nms.db.enums.EManufacturer;
import com.nms.db.enums.EServiceType;
import com.nms.db.enums.OamTypeEnum;
import com.nms.model.equipment.shlef.SiteService_MB;
import com.nms.model.ptn.oam.OamInfoService_MB;
import com.nms.model.ptn.qos.QosInfoService_MB;
import com.nms.model.ptn.qos.QosRelevanceService_MB;
import com.nms.model.util.ObjectService_Mybatis;
import com.nms.model.util.Services;
import com.nms.ui.manager.BusinessIdException;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.ExceptionManage;

public class AcPortInfoService_MB extends ObjectService_Mybatis{
	private final static int ISUSEDSTATUS = 1;
	
	public void setPtnuser(String ptnuser) {
		super.ptnuser = ptnuser;
	}

	public void setSqlSession(SqlSession sqlSession) {
		super.sqlSession = sqlSession;
	}
	
	private AcPortInfoMapper mapper;

	public AcPortInfoMapper getAcPortInfoMapper() {
		return mapper;
	}

	public void setAcPortInfoMapper(AcPortInfoMapper acPortInfoMapper) {
		this.mapper = acPortInfoMapper;
	}

	public List<AcPortInfo> queryByAcPortInfo(AcPortInfo acInfo) {
		List<AcPortInfo> acPortInfoList = null;
		try {
			acPortInfoList = new ArrayList<AcPortInfo>();
			acPortInfoList = this.mapper.queryByCondition(acInfo);
			AcBufferService_MB bufservice = (AcBufferService_MB) ConstantUtil.serviceFactory.newService_MB(Services.UniBuffer, this.sqlSession);
			for (AcPortInfo acPortInfo : acPortInfoList) {
				List<Acbuffer> acbufferList = bufservice.select(acPortInfo.getId());
				acPortInfo.setBufferList(acbufferList);
			}
			this.getAcQos(acPortInfoList);
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return acPortInfoList;
	}

	private void getAcQos(List<AcPortInfo> acPortInfoList) {
		List<QosInfo> qosInfoList = null;
		try {
			QosInfoService_MB qosInfoService = (QosInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.QosInfo, this.sqlSession);
			for (AcPortInfo acPortInfo : acPortInfoList) {
				qosInfoList = qosInfoService.getQosByObj(EServiceType.ACPORT.toString(), acPortInfo.getId());
				if (null != qosInfoList && qosInfoList.size() == 1) {
					acPortInfo.setSimpleQos(qosInfoList.get(0));
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
	}

	public int saveOrUpdate(List<Acbuffer> bufferInfos, AcPortInfo acPortInfo) throws Exception {
		if (acPortInfo == null) {
			throw new Exception("acPortInfos is null");
		}
		int result = 0;
		int acId = 0;
		AcBufferService_MB bufservice = null;
		OamInfoService_MB oamInfoService = null;
		SiteService_MB siteService = null;
		BusinessidMapper busiIdMapper = null;
		PortLagService_MB portLagService = null;
		QosRelevanceService_MB qosRelevanceService = null;
		try {
			bufservice = (AcBufferService_MB) ConstantUtil.serviceFactory.newService_MB(Services.UniBuffer, this.sqlSession);
			oamInfoService = (OamInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.OamInfo, this.sqlSession);
			siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE, this.sqlSession);
			// 下发Ac
			if (acPortInfo.getId() > 0) {
				this.mapper.update(acPortInfo);
				// 设置ac与流的关联关系，下发流
				for (Acbuffer buffer : bufferInfos) {
					buffer.setId(0);
					buffer.setAcId(acPortInfo.getId());
					buffer.setPortId(acPortInfo.getPortId());
				}
				result = acPortInfo.getId();
				acPortInfo.getSimpleQos().setQosname(null);
				// 离线网元数据下载
				if (!acPortInfo.isDataDownLoad()) {
					super.dateDownLoad(acPortInfo.getSiteId(), result, EServiceType.ACPORT.getValue(), EActionType.UPDATE.getValue());
				}
			} else {
				if (siteService.getManufacturer(acPortInfo.getSiteId()) != EManufacturer.WUHAN.getValue()) {
					busiIdMapper = this.sqlSession.getMapper(BusinessidMapper.class);
					Businessid acSerBusinessId = null;
					if (acPortInfo.getAcBusinessId() == 0) {
						acSerBusinessId = busiIdMapper.queryList(acPortInfo.getSiteId(), "ac").get(0);
					} else {
						acSerBusinessId = busiIdMapper.queryByIdValueSiteIdtype(acPortInfo.getAcBusinessId(), acPortInfo.getSiteId(), "ac");
					}
					if (acSerBusinessId == null) {
						throw new BusinessIdException("acSerBusinessId is null");
					}
					acPortInfo.setAcBusinessId(acSerBusinessId.getIdValue());
					this.mapper.insert(acPortInfo);
					acId = acPortInfo.getId();
					Businessid bIdCondition = new Businessid();
					bIdCondition.setId(acSerBusinessId.getId());
					bIdCondition.setIdStatus(ISUSEDSTATUS);
					busiIdMapper.update(bIdCondition);
					// 离线网元数据下载
					super.dateDownLoad(acPortInfo.getSiteId(), acId, EServiceType.ACPORT.getValue(), EActionType.INSERT.getValue());
				} else {
					this.mapper.insert(acPortInfo);
					acId = acPortInfo.getId();
				}
				// 设置ac与流的关联关系，下发流
				for (Acbuffer buffer : bufferInfos) {
					if (buffer.getAcId() == 0) {
						buffer.setAcId(acId);
						buffer.setPortId(acPortInfo.getPortId());
					}
				}
				acPortInfo.setId(acId);
				result = acId;
				acPortInfo.setId(acId);
			}
			if(bufferInfos.size()>0){
				acPortInfo.getSimpleQos().setBufferList(bufferInfos);
			}
			qosRelevanceService = (QosRelevanceService_MB) ConstantUtil.serviceFactory.newService_MB(Services.QOSRELEVANCE, this.sqlSession);
			List<QosRelevance> qosRelevanceList = qosRelevanceService.getList(acPortInfo);
				qosRelevanceService.save(qosRelevanceList);
				if (siteService.getManufacturer(acPortInfo.getSiteId()) != EManufacturer.CHENXIAO.getValue()) {
					// 保存或者更新细分流
					if (!bufferInfos.isEmpty()) {
						bufservice.deletebyAcId(acPortInfo.getId());
						bufservice.saveOrUpdate(bufferInfos);
					}
				}else{
					acPortInfo.setSimpleQos(qosRelevanceList.get(0).getQosInfoList().get(0));
				}
			// lag与ac的关联
			if (acPortInfo.getLagId() > 0) {// 如果ac为lag，把相应的lag变为被使用
				portLagService = (PortLagService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORTLAG, this.sqlSession);
				PortLagInfo portLagInfo = new PortLagInfo();
				portLagInfo.setId(acPortInfo.getLagId());
				portLagInfo.setType(1);
				portLagInfo = portLagService.selectLAGByCondition(portLagInfo).get(0);
				portLagInfo.setIsUsed(1);
				portLagService.updateStatus(portLagInfo);
			}
			if (acPortInfo.getOamList() != null && acPortInfo.getOamList().size() > 0) {
				List<OamInfo> oamList = acPortInfo.getOamList();
				for (OamInfo oamInfo : oamList) {
					if (oamInfo.getOamType() == OamTypeEnum.AMEP || oamInfo.getOamType() == OamTypeEnum.ZMEP) {
						oamInfo.getOamMep().setServiceId(acId);
						oamInfo.getOamMep().setObjId(acPortInfo.getAcBusinessId());
					} else if (oamInfo.getOamType() == OamTypeEnum.MEP) {

					} else if (oamInfo.getOamType() == OamTypeEnum.MIP) {

					}
					oamInfoService.saveOrUpdate(oamInfo);
				}
			}
			this.sqlSession.commit();
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
			bufservice = null;
			oamInfoService = null;
			siteService = null;
			busiIdMapper = null;
			portLagService = null;
			
		}
		return result;
	}
	
	/**
	 * 根据主键查询AC对象
	 * 
	 * @param id
	 *            主键
	 * @return
	 * @throws Exception
	 */
	public AcPortInfo selectById(int id) throws Exception {
		AcPortInfo acPortInfo = null;
		List<AcPortInfo> acPortInfoList = null;
		try {
			acPortInfo = new AcPortInfo();
			acPortInfo.setId(id);
			acPortInfoList = this.selectByCondition(acPortInfo);
			this.getAcQos(acPortInfoList);
			this.getAcBuffer(acPortInfoList);
			if (null != acPortInfoList && acPortInfoList.size() == 1) {
				acPortInfo = acPortInfoList.get(0);
			}
		} catch (Exception e) {
			throw e;
		}
		return acPortInfo;
	}
	
	/**
	 * 查询ac
	 * 
	 * @param siteId
	 *            网元id
	 * @param xcid
	 *            设备名称
	 * @return
	 * @throws Exception
	 */
	public List<AcPortInfo> selectByCondition(AcPortInfo acPortInfo) throws Exception {

		List<AcPortInfo> acPortInfoList = null;
		try {
			acPortInfoList = mapper.queryByCondition(acPortInfo);
			this.getAcQos(acPortInfoList);
		} catch (Exception e) {
			throw e;
		}
		return acPortInfoList;
	}
	
	/**
	 * 把数据库里对应的流信息添加到ac中
	 * 
	 * @param acPortInfoList
	 * @throws Exception
	 */
	private void getAcBuffer(List<AcPortInfo> acPortInfoList) throws Exception {
		List<Acbuffer> acBufferList = null;
		AcBufferService_MB bufservice = null;
		try {
			bufservice = (AcBufferService_MB) ConstantUtil.serviceFactory.newService_MB(Services.UniBuffer, this.sqlSession);

			for (AcPortInfo acPortInfo : acPortInfoList) {

				acBufferList = bufservice.select(acPortInfo.getId());
				if (null != acBufferList && acBufferList.size() > 0) {
					acPortInfo.setBufferList(acBufferList);
				}
			}

		} catch (Exception e) {
			throw e;
		} finally {
//			UiUtil.closeService(bufservice);
		}

	}

	public List<Integer> acByPort(int portId) {
		List<Integer> idList = new ArrayList<Integer>();
		try {
			idList = this.mapper.selectByPortId(portId);
		} catch (Exception e) {
			ExceptionManage.dispose(e, getClass());
		}
		return idList;
	}

	public List<AcPortInfo> select(List<Integer> acIdList) throws Exception {
		List<AcPortInfo> acPortInfoList = null;
		List<Acbuffer> acbufferList = null;
		AcBufferService_MB bufservice = null;
		try {
			acPortInfoList = this.mapper.queryByAcIdCondition(acIdList);
			bufservice = (AcBufferService_MB) ConstantUtil.serviceFactory.newService_MB(Services.UniBuffer, this.sqlSession);
			for (AcPortInfo acPortInfo : acPortInfoList) {
				acbufferList = bufservice.select(acPortInfo.getId());
				acPortInfo.setBufferList(acbufferList);
			}
			this.getAcQos(acPortInfoList);
		} catch (Exception e) {
			throw e;
		}
		return acPortInfoList;
	}

	public List<AcPortInfo> selectBySiteId(int siteId) throws Exception {
		List<AcPortInfo> acPortInfoList = null;
		AcPortInfo acInfo = new AcPortInfo();
		acInfo.setSiteId(siteId);
		try {
			acPortInfoList = this.mapper.queryByCondition(acInfo);
			this.getAcQos(acPortInfoList);
			this.getAcBuffer(acPortInfoList);
		} catch (Exception e) {
			throw e;
		}
		return acPortInfoList;
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
	public boolean nameRepetitionBySingle(String afterName, String beforeName, int siteId) throws Exception {
		int result = this.mapper.query_nameBySingle(afterName, beforeName, siteId);
		if (0 == result) {
			return false;
		} else {
			return true;
		}
	}
	

	public void updateUserType(AcPortInfo info) throws Exception {
	  this.mapper.update(info);
	  sqlSession.commit();
	}

	/**
	 * 跟新所有的ac的使用状态
	 * @param acIdList
	 * @throws Exception
	 */
	public void updateState(List<Integer> acIdList) throws Exception {

		try {
			if(acIdList != null && acIdList.size()>0){
				mapper.updateAcState(acIdList);
			}
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * 同步时查询ac
	 * 
	 * @param siteId
	 *            网元id
	 * @param xcid
	 *            设备名称
	 * @return
	 * @throws Exception
	 */
	public List<AcPortInfo> selectByCondition_synchro(AcPortInfo acPortInfo) throws Exception {

		List<AcPortInfo> acPortInfoList = null;
		try {
			acPortInfoList = mapper.selectByCondition_synchro(acPortInfo);
			this.getAcQos(acPortInfoList);
		} catch (Exception e) {
			throw e;
		}
		return acPortInfoList;
	}
	
	/**
	 * 根据端口ID和vlanid 查询ac数据 武汉同步时用
	 * 
	 * @author kk
	 * @param acRelevanceArr 
	 * 
	 * @param
	 * 
	 * @return
	 * @throws Exception 
	 * @throws Exception
	 * 
	 * @Exception 异常对象
	 */
	public List<AcPortInfo> select_vlan(AcPortInfo acPortInfo, int[] acRelevanceArr) throws Exception  {
		List<AcPortInfo> acportInfoList = null;
		try {
			List<String> relevanceValueList = new ArrayList<String>();
			// 获取流中所有vlanid等关联规则的值
			for (Acbuffer buffer : acPortInfo.getBufferList()) {
				relevanceValueList.add(this.appendResult(acRelevanceArr, buffer));
			}
			acportInfoList = new ArrayList<AcPortInfo>();
			if (!relevanceValueList.isEmpty()) {
				List<AcPortInfo> acList = this.queryByAcPortInfo(acPortInfo);
				if(acList != null && !acList.isEmpty()){
					for (int i = 0; i < acList.size(); i++) {
						List<Acbuffer> bufferList = acList.get(i).getBufferList();
						for (int j = 0; j < bufferList.size(); j++) {
							Acbuffer buffer = bufferList.get(j);
							String result = this.appendResult(acRelevanceArr, buffer);
							if(relevanceValueList.contains(result)){
								acportInfoList.add(acList.get(i));
								break;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return acportInfoList;
	}
	
	private String appendResult(int[] acRelevanceArr, Acbuffer buffer) {
		StringBuffer sb = new StringBuffer();
		sb.append("");
		if(acRelevanceArr[0] == 1)
			sb.append(buffer.getVlanId());
		if(acRelevanceArr[1] == 1)
			sb.append(buffer.getEightIp());
		if(acRelevanceArr[2] == 1)
			sb.append(buffer.getSourceMac());
		if(acRelevanceArr[3] == 1)
			sb.append(buffer.getTargetMac());
		if(acRelevanceArr[4] == 1)
			sb.append(buffer.getSourceIp());
		if(acRelevanceArr[5] == 1)
			sb.append(buffer.getTargetIp());
		return sb.toString();
	}

	/**
	 * 根据网元ID获取网元厂商
	 * 
	 * @param siteId
	 *            网元主键
	 * @return 厂商
	 * @throws Exception
	 */
	/*
	 * private String getManufacturer(int siteId) throws Exception { String manufacturer = null; SiteService siteService = null; SiteInst siteInst = null; try { siteService = (SiteService) ConstantUtil.serviceFactory .newService(Services.SITE); siteInst = siteService.select(siteId); if (siteInst == null) { throw new Exception("根据ID查询网元出错"); } manufacturer = UiUtil.getCodeById( Integer.parseInt(siteInst.getCellEditon())).getCodeName(); return manufacturer; } catch (Exception e) { throw e; } finally { siteService = null; siteInst = null; } }
	 */
	public int update(AcPortInfo acPortInfo) throws Exception {
		int result = 0;
		OamInfoService_MB oamInfoService = null;
		QosRelevanceService_MB qosRelevanceService = null;
		List<QosRelevance> qosRelevanceList = null;
		AcBufferService_MB acBufferService = null;
		try {
			oamInfoService = (OamInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.OamInfo, this.sqlSession);
			mapper.update(acPortInfo);
			// 离线网元数据下载
			if (!acPortInfo.isDataDownLoad()) {
				super.dateDownLoad(acPortInfo.getSiteId(), acPortInfo.getId(), EServiceType.ACPORT.getValue(), EActionType.UPDATE.getValue());
			}
			// 保存或者更新简单流
			qosRelevanceService = (QosRelevanceService_MB) ConstantUtil.serviceFactory.newService_MB(Services.QOSRELEVANCE, this.sqlSession);
			qosRelevanceList = qosRelevanceService.getList(acPortInfo);
			qosRelevanceService.save(qosRelevanceList);

			List<OamInfo> oamList = acPortInfo.getOamList();
			if (oamList != null && oamList.size() > 0) {
				for (OamInfo oamInfo : oamList) {
					if (oamInfo.getOamType() == OamTypeEnum.AMEP || oamInfo.getOamType() == OamTypeEnum.ZMEP || oamInfo.getOamType() == OamTypeEnum.MEP) {
						oamInfo.getOamMep().setServiceId(acPortInfo.getId());
						oamInfo.getOamMep().setObjId(acPortInfo.getSiteId());
						oamInfo.setOamType(OamTypeEnum.MEP);
					} else if (oamInfo.getOamType() == OamTypeEnum.MIP) {

					}
					oamInfoService.saveOrUpdate(oamInfo);
				}
			}
			acBufferService = (AcBufferService_MB) ConstantUtil.serviceFactory.newService_MB(Services.UniBuffer,this.sqlSession);
			for (Acbuffer buffer : acPortInfo.getBufferList()) {
				if (buffer.getAcId() == 0) {
					buffer.setAcId(acPortInfo.getId());
					buffer.setPortId(acPortInfo.getPortId());
				}else{
					buffer.setId(0);
				}
			}
			acBufferService.deletebyAcId(acPortInfo.getId());
			acBufferService.saveOrUpdate(acPortInfo.getBufferList());
			sqlSession.commit();
		} catch (Exception e) {
			sqlSession.rollback();
			throw e;
		} finally {
//			UiUtil.closeService(oamInfoService);
//			UiUtil.closeService(qosRelevanceService);
		}
		return result;

	}
	
	/**
	 * 删除
	 * 
	 * @param acId
	 * @return
	 * @throws Exception
	 */
	public void delete(int acId) throws Exception {
		List<Integer> acIdList = null;
		try {
			acIdList = new ArrayList<Integer>();
			acIdList.add(acId);
			this.delete(acIdList);
		} catch (Exception e) {
			throw e;
		} finally {
			acIdList = null;
		}
	}

	public int updateActiveStatus(int siteId, int value) throws Exception {
		return this.mapper.updateActiveStatus(siteId, value);
		
	}
	
	/**
	 * 批量删除ac
	 * 
	 * @param acIdList
	 * @throws Exception
	 */
	public void delete(List<Integer> acIdList) throws Exception {
		if (acIdList.isEmpty()) {
			throw new Exception("acId List为空");
		}
		List<AcPortInfo> acportInfoList = null;
		Businessid businessid = null;
		QosRelevanceMapper qosRelevanceMapper = null;
		QosRelevance qosRelevance = null;
		List<PortLagInfo> portLagInfoList;
		PortLagService_MB portLagService = (PortLagService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORTLAG, this.sqlSession);
		AcBufferService_MB bufservice = null;
		QosInfoMapper qosInfoMapper = null;
		BusinessidMapper businessidMapper = null;
		try {
			acportInfoList = mapper.queryByAcIdCondition(acIdList);
			businessidMapper = sqlSession.getMapper(BusinessidMapper.class);
			// if (UiUtil.getManufacturer(ConstantUtil.siteId) != EManufacturer.WUHAN.getValue()) {
			if (!acportInfoList.isEmpty()) {
				for (AcPortInfo acInfo : acportInfoList) {
					businessid = new Businessid();
					businessid.setIdStatus(0);
					businessid.setType("ac");
					businessid.setSiteId(acInfo.getSiteId());
					businessid.setIdValue(acInfo.getAcBusinessId());
					businessidMapper.updateBusinessid(businessid);
					// 离线网元数据下载
					if (0 != acInfo.getLagId()) {
						PortLagInfo portLagInfo = new PortLagInfo();
						portLagInfo.setLagID(acInfo.getLagId());
						portLagInfoList = portLagService.selectByCondition(portLagInfo);
						if (null != portLagInfoList && portLagInfoList.size() > 0) {
							portLagInfo = portLagInfoList.get(0);
							super.dateDownLoad(acInfo.getSiteId(), acInfo.getId(), EServiceType.ACPORT.getValue(), EActionType.DELETE.getValue(), acInfo.getAcBusinessId() + "", portLagInfo.getLagID() + "", acInfo.getPortId(), acInfo.getAcBusinessId(), null);
						}
					} else {
						super.dateDownLoad(acInfo.getSiteId(), acInfo.getId(), EServiceType.ACPORT.getValue(), EActionType.DELETE.getValue(), acInfo.getAcBusinessId() + "", "", acInfo.getPortId(), acInfo.getAcBusinessId(), null);
					}
				}
			}
			// }
			// 删除对应ac的细分流
			bufservice = (AcBufferService_MB) ConstantUtil.serviceFactory.newService_MB(Services.UniBuffer, this.sqlSession);
			for (Integer id : acIdList) {
				bufservice.deletebyAcId(id); // 删除细分流
				qosInfoMapper = sqlSession.getMapper(QosInfoMapper.class);
				qosRelevanceMapper = sqlSession.getMapper(QosRelevanceMapper.class);
				//删除之前先判断该条QoS是否被其他ac使用，如果被其他ac使用，则不删除，否则删除
				//先删除qosInfo,再删除qosRelevance
				List<QosRelevance> qosRelavanceList = this.getQosRelevanceList(id,qosRelevanceMapper);
				this.checkIsOccupy(id, qosRelavanceList,qosRelevanceMapper);
				if(qosRelavanceList != null && !qosRelavanceList.isEmpty()){
					for (QosRelevance relevance : qosRelavanceList) {
						if(!relevance.isRepeat()){
							qosInfoMapper.deleteByGroupId(relevance.getQosGroupId());
						}
					}
				}
				qosRelevance = new QosRelevance();
				qosRelevance.setObjType(EServiceType.ACPORT.toString());
				qosRelevance.setObjId(id);
				qosRelevanceMapper.deleteByCondition(qosRelevance);
			}

			// 删除对应的lag使用关系
			for (AcPortInfo acPortInfo : acportInfoList) {
				if (acPortInfo.getLagId() > 0) {// 如果ac为lag，把相应的lag变为被使用
					PortLagInfo portLagInfo = new PortLagInfo();
					portLagInfo.setId(acPortInfo.getLagId());
					portLagInfo.setType(1);
					portLagInfo = portLagService.selectLAGByCondition(portLagInfo).get(0);
					portLagInfo.setIsUsed(0);
					portLagService.updateStatus(portLagInfo);
				}
			}

			mapper.deleteIds(acIdList);
			sqlSession.commit();
		} catch (Exception e) {
			sqlSession.rollback();
			throw e;
		} 
	}
	
	/**
	 * 判断ac引用的该条QoS是否被其他ac使用
	 * @param qosRelavanceList 
	 */
	private void checkIsOccupy(int acId, List<QosRelevance> qosRelevanceList,QosRelevanceMapper qosRelevanceMapper) {
		try {
			if(qosRelevanceList != null && !qosRelevanceList.isEmpty()){
				QosRelevance qosRelevance = new QosRelevance();
				for (QosRelevance relevance : qosRelevanceList) {
					qosRelevance.setSiteId(relevance.getSiteId());
					qosRelevance.setObjId(0);
					qosRelevance.setObjType(EServiceType.ACPORT.toString());
					qosRelevance.setQosGroupId(relevance.getQosGroupId());
					List<QosRelevance> qosList = qosRelevanceMapper.queryByCondition(qosRelevance); 
					if(qosList != null && qosList.size() > 1){
						relevance.setRepeat(true);
					}else{
						relevance.setRepeat(false);
					}
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
	}
	
	private List<QosRelevance> getQosRelevanceList(int acId,QosRelevanceMapper qosRelevanceMapper) throws Exception{
		QosRelevance qosRelevance = new QosRelevance();
		qosRelevance.setObjId(acId);
		qosRelevance.setObjType(EServiceType.ACPORT.toString());
		return qosRelevanceMapper.queryByCondition(qosRelevance);
	}

	public int updateLanId(AcPortInfo acInfo) {
		int result = this.mapper.updateLanId(acInfo.getLanId(), acInfo.getId());
		this.sqlSession.commit();
		return result;
	}

	
	public List<Map<String,Object>> northList() {
		List<Map<String,Object>> list = this.mapper.northList();
		return list;
	}
	
}
