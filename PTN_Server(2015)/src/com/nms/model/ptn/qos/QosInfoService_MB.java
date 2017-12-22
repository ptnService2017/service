﻿package com.nms.model.ptn.qos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.nms.db.bean.ptn.path.pw.PwInfo;
import com.nms.db.bean.ptn.path.tunnel.Tunnel;
import com.nms.db.bean.ptn.port.AcPortInfo;
import com.nms.db.bean.ptn.port.Acbuffer;
import com.nms.db.bean.ptn.qos.QosInfo;
import com.nms.db.bean.ptn.qos.QosRelevance;
import com.nms.db.dao.ptn.qos.QosInfoMapper;
import com.nms.db.enums.EManufacturer;
import com.nms.db.enums.EQosDirection;
import com.nms.db.enums.EServiceType;
import com.nms.db.enums.QosTemplateTypeEnum;
import com.nms.model.equipment.shlef.SiteService_MB;
import com.nms.model.ptn.path.pw.PwInfoService_MB;
import com.nms.model.ptn.path.tunnel.TunnelService_MB;
import com.nms.model.ptn.port.AcBufferService_MB;
import com.nms.model.util.ObjectService_Mybatis;
import com.nms.model.util.Services;
import com.nms.ui.manager.BusinessIdException;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.ExceptionManage;

public class QosInfoService_MB extends ObjectService_Mybatis{
	public void setPtnuser(String ptnuser) {
		super.ptnuser = ptnuser;
	}

	public void setSqlSession(SqlSession sqlSession) {
		super.sqlSession = sqlSession;
	}
	
	private QosInfoMapper qosInfoMapper;

	public QosInfoMapper getQosInfoMapper() {
		return qosInfoMapper;
	}

	public void setQosInfoMapper(QosInfoMapper qosInfoMapper) {
		this.qosInfoMapper = qosInfoMapper;
	}

	/**
	 * 查询业务关联的qos集合
	 * 
	 * @param objtype
	 *            业务类型
	 * @param objId
	 *            业务主键
	 * @return
	 * @throws Exception
	 */
	public List<QosInfo> getQosByObj(String objtype, int objId) throws Exception {

		QosRelevanceService_MB qosRelevanceService_MB = null;

		QosRelevance qosRelevance = null;
		List<QosRelevance> qosRelevanceList = null;
		List<QosInfo> qosInfoList = null;
		QosInfo qosInfo = null;
		try {

			qosRelevanceService_MB = (QosRelevanceService_MB) ConstantUtil.serviceFactory.newService_MB(Services.QOSRELEVANCE,sqlSession);

			qosRelevance = new QosRelevance();
			qosRelevance.setObjId(objId);
			qosRelevance.setObjType(objtype);
			qosRelevanceList = new ArrayList<QosRelevance>();
			qosRelevanceList = qosRelevanceService_MB.select(qosRelevance);
			if (null != qosRelevanceList && !qosRelevanceList.isEmpty()) {
				qosInfo = new QosInfo();
				qosInfo.setGroupId(qosRelevanceList.get(0).getQosGroupId());
				qosInfoList = new ArrayList<QosInfo>();
				qosInfoList = this.qosInfoMapper.queryByCondition(qosInfo);
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return qosInfoList;
	}

		/**
	 * 根据类型和ID查出来qos集合与参数qos集合进行比较
	 * 
	 * @param objId
	 *            业务主键
	 * @param eServiceType
	 *            业务类型
	 * @param qosInfoList
	 *            要比较的qos集合
	 * @return true=相同 false=不同
	 * @throws Exception
	 */
	public boolean compareQos(int objId, EServiceType eServiceType, List<QosInfo> qosInfoList) throws Exception {
		List<QosInfo> qosInfoList_select = null;
		boolean flag = true;
		QosInfo qosinfo_ui = null;
		try {
			// 根据条件查询出qos集合
			qosInfoList_select = this.getQosByObj(eServiceType.toString(), objId);
			// 如果查询出来。 挨个比较带宽
			if (null != qosInfoList_select && !qosInfoList_select.isEmpty()) {
				for (QosInfo qosInfo_db : qosInfoList_select) {
					// 从qos集合中取出来qosui的对象
					qosinfo_ui = this.getQosInfo(qosInfoList, qosInfo_db.getCos(), qosInfo_db.getDirection());
					// 如果没查询到 直接返回
					if (qosinfo_ui == null) {
						flag = false;
						break;
					} else {
						QosInfo qosInfo = this.calculateQos(qosInfo_db, eServiceType, objId);
						// 如果db对象的cir或者eir小于界面对象的。 就返回false
						if (qosInfo.getCir() < qosinfo_ui.getCir() || qosInfo.getEir() < qosinfo_ui.getEir()) {
							flag = false;
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return flag;
	}

	/**
	 * 计算剩余qos 如果是tunnel 把此tunnel上的pw带宽计算一次
	 * @param qosinfo
	 *            qos对象
	 * @param eServiceType
	 *            类型
	 * @param objid
	 *            业务ID
	 * @throws Exception
	 */
	public QosInfo calculateQos(QosInfo qosinfo, EServiceType eServiceType, int objid) throws Exception {
		PwInfoService_MB pwInfoService = null;
		List<Integer> tunnelIds = null;
		List<PwInfo> pwinfoList = null;
		int cir = qosinfo.getCir();
		int eir = qosinfo.getEir();
		QosInfo qosinfo_db = null;
		QosInfo qosInfo_result = new QosInfo();
		try {
			// 如果是tunnel才去验证
			if (eServiceType.getValue() == EServiceType.TUNNEL.getValue()) {
				// 查询出此tunnel的所有pw
				pwInfoService = (PwInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PwInfo, this.sqlSession);
				tunnelIds = new ArrayList<Integer>();
				tunnelIds.add(objid);
				pwinfoList = pwInfoService.selectPwInfoByTunnelId(tunnelIds);
				for (PwInfo pwinfo_db : pwinfoList) {
					qosinfo_db = this.getQosInfo(pwinfo_db.getQosList(), qosinfo.getCos(), qosinfo.getDirection());
					if (null != qosinfo_db) {
						cir -= qosinfo_db.getCir();
						eir -= qosinfo_db.getEir();
					}
				}
			}
			qosInfo_result.setCir(cir);
			qosInfo_result.setEir(eir);
		} catch (Exception e) {
			throw e;
		}
		return qosInfo_result;
	}

	/**
	 * 根据方向和cos 从qos集合中查询qos对象
	 * @param qosInfoList qos集合
	 * @param cos 策略
	 * @param direction 前向后向
	 * @return 找到了返回qosinfo对象。 否则返回null
	 * @throws Exception
	 */
	public QosInfo getQosInfo(List<QosInfo> qosInfoList, int cos, String direction) throws Exception {
		try {
			for (QosInfo qosinfo : qosInfoList) {
				if (qosinfo.getCos() == cos && direction.equals(qosinfo.getDirection())) {
					return qosinfo;
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}

	/**
	 * 获取groupid
	 * @param qosInfList qos集合 用此参数去比较
	 * @return groupId
	 */
	public int getGroupId(QosRelevance qosRelevance) throws Exception, BusinessIdException {
		if (null == qosRelevance) {
			throw new Exception("qosRelevance is null");
		}
		AcBufferService_MB bufservice = (AcBufferService_MB) ConstantUtil.serviceFactory.newService_MB(Services.UniBuffer,sqlSession);
		Map<Integer, List<QosInfo>> map = null;
		boolean flag = false;
		int result = 0;
		Acbuffer buffer;
		List<QosInfo> qosInfoList = null;
		SiteService_MB siteService = null;
		try {
			siteService=(SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE,sqlSession);
			qosInfoList = qosRelevance.getQosInfoList();
			
			if(null == qosInfoList || qosInfoList.size()==0){
				throw new Exception("qos is null");
			}
			
			if(qosInfoList.get(0).getGroupId()>0){
				result=qosInfoList.get(0).getGroupId();
			}else{
				map = this.getMapByQos(qosRelevance.getSiteId(), qosInfoList.get(0).getQosType(), 0);
				// 遍历map 比较带宽是否相等
				for (int groupId : map.keySet()) {
					if (this.groupIdType(groupId, qosRelevance)) {
						flag = this.compare(qosInfoList, map.get(groupId));
						if (flag) {
							//武汉不需检测buffer信息
							result = groupId;
							break;
						}
					}
				}
				// 如果没有匹配到qos，需要新建一个qos
				if (result == 0) {
					result = this.save(qosInfoList);
					// 保存流
					if (siteService.getManufacturer(qosInfoList.get(0).getSiteId()) == EManufacturer.CHENXIAO.getValue() && qosRelevance.getObjType().equals(EServiceType.ACPORT.toString())) {
						List<Acbuffer> bufferInfos = qosInfoList.get(0).getBufferList();
						if (bufferInfos.size() != 0) {
							for (int i = 0; i < bufferInfos.size(); i++) {
								buffer = bufferInfos.get(i);
								buffer.setSimpleQosId(qosInfoList.get(0).getGroupId());
								buffer.setQosName(qosInfoList.get(0).getQosname() + i);
								if (2 != Integer.parseInt(buffer.getQosType())) {
									buffer.setAppendBufferName(qosInfoList.get(0).getQosname() + i);
								}
							}
						}
						bufservice.saveOrUpdate(bufferInfos);
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return result;
	}

	/**
	 * 验证要存储的类型和已经被用的qos是否相同。 晨晓设备tunnel、xc、pw不能引用一个qos
	 * 
	 * @param groupId
	 * @param qosRelevance
	 * @return 相同或者没被使用，返回true， 不相同返回false
	 * @throws Exception
	 */
	private boolean groupIdType(int groupId, QosRelevance qosRelevance) throws Exception {
		boolean flag = false;
		QosRelevance qosRelevance_select = null;
		List<QosRelevance> qosRelevanceList = null;
		QosRelevanceService_MB qosRelevanceService = null;
		String type_select = null;
		String type_param = null;
		try {
			// 同过groupid和siteid查询
			qosRelevanceService = (QosRelevanceService_MB) ConstantUtil.serviceFactory.newService_MB(Services.QOSRELEVANCE,sqlSession);
			qosRelevance_select = new QosRelevance();
			qosRelevance_select.setQosGroupId(groupId);
			qosRelevance_select.setSiteId(qosRelevance.getSiteId());
			qosRelevanceList = qosRelevanceService.select(qosRelevance_select);
			if (null == qosRelevanceList || qosRelevanceList.size() == 0) {
				flag = true;
			} else {
				qosRelevance_select = qosRelevanceList.get(0);
				type_select = this.getType(qosRelevance_select);
				type_param = this.getType(qosRelevance);

				if (type_param.equals(type_select)) {
					flag = true;
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return flag;
	}

	/**
	 * 获取类型，如果是tunnel，验证是否为xc
	 * @param qosRelevance
	 * @return tunnel、pw、xc
	 * @throws Exception
	 */
	private String getType(QosRelevance qosRelevance) throws Exception {
		TunnelService_MB tunnelService = null;
		Tunnel tunnel = null;
		String result = null;
		List<Tunnel> tunnelList = null;
		try {
			if (EServiceType.TUNNEL.toString().equals(qosRelevance.getObjType())) {
				tunnelService = (TunnelService_MB) ConstantUtil.serviceFactory.newService_MB(Services.Tunnel, this.sqlSession);
				tunnel = new Tunnel();
				tunnel.setTunnelId(qosRelevance.getObjId());
				tunnelList = tunnelService.select_nojoin(tunnel);
				if (null != tunnelList && tunnelList.size() == 1) {
					tunnel = tunnelList.get(0);
					if (tunnel.getASiteId() == qosRelevance.getSiteId() || tunnel.getZSiteId() == qosRelevance.getSiteId()) {
						result = EServiceType.TUNNEL.toString();
					} else {
						result = "XC";
					}
				}
			} else {
				result = qosRelevance.getObjType();
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return result;
	}

	/**
	 * 根据网元ID和qostype 查询出所有qos
	 * @param siteId 网元ID
	 * @param qosType qos类型
	 * @return key=groupid value=qosinfo
	 * @throws Exception
	 */
	private Map<Integer, List<QosInfo>> getMapByQos(int siteId, String qosType, int status) throws Exception {
		QosInfo qosInfo = null;
		List<QosInfo> qosInfoList = null;
		Map<Integer, List<QosInfo>> map = null;
		List<QosInfo> qosInfoList_map = null;
		try {
			// 查询出所有qos
			qosInfo = new QosInfo();
			qosInfo.setSiteId(siteId);
			qosInfo.setQosType(qosType);
			qosInfo.setStatus(status);
			qosInfoList = this.qosInfoMapper.queryByCondition(qosInfo);
			// 把list转化弄成map
			map = new HashMap<Integer, List<QosInfo>>();
			for (QosInfo qosInfo_select : qosInfoList) {
				if (null == map.get(qosInfo_select.getGroupId())) {
					qosInfoList_map = new ArrayList<QosInfo>();
					qosInfoList_map.add(qosInfo_select);
					map.put(qosInfo_select.getGroupId(), qosInfoList_map);
				} else {
					map.get(qosInfo_select.getGroupId()).add(qosInfo_select);
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return map;
	}

	/**
	 * 比较两组qos是否一致
	 * @param qosInfoList_1
	 * @param qosInfoList_2
	 * @return
	 * @throws Exception
	 */
	private boolean compare(List<QosInfo> qosInfoList_1, List<QosInfo> qosInfoList_2) throws Exception {
		boolean flag = true;
		try {
			for (QosInfo qosInfo_1 : qosInfoList_1) {
				for (QosInfo qosInfo_2 : qosInfoList_2) {
					if (qosInfo_1.getCos() == qosInfo_2.getCos()) {
						if (qosInfo_1.getCir() == qosInfo_2.getCir() && qosInfo_1.getCbs() == qosInfo_2.getCbs() && qosInfo_1.getEir() == qosInfo_2.getEir() && qosInfo_1.getEbs() == qosInfo_2.getEbs()) {
							qosInfo_1.setQosname(qosInfo_2.getQosname());
							continue;
						} else {
							flag = false;
							break;
						}
					} else {
						flag = false;
					}
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return flag;
	}

	/**
	 * 保存qos
	 * 
	 * @param qosInfoList
	 *            要保存的qos集合
	 * @return groupid
	 * @throws Exception
	 */
	public int save(List<QosInfo> qosInfoList) throws Exception, BusinessIdException {
		if (null == qosInfoList || qosInfoList.size() == 0) {
			throw new Exception("qosInfoList is null");
		}
		int groupId = 0;
		Map<Integer, List<QosInfo>> map = null;
		String qosname = null;
		SiteService_MB siteService = null;
		try {
			siteService=(SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE, this.sqlSession);
			// 晨晓的设备需要验证是否超过64个
			if (siteService.getManufacturer(qosInfoList.get(0).getSiteId()) == EManufacturer.CHENXIAO.getValue()) {
				map = this.getMapByQos(qosInfoList.get(0).getSiteId(), qosInfoList.get(0).getQosType(), 2);
				if (map.keySet().size() >= 64) {
					throw new BusinessIdException("数量超出");
				}
			}
			try {
				groupId = this.qosInfoMapper.queryMaxGroupId() + 1;
			} catch (Exception e) {
				groupId = 1;
			}
			for (QosInfo qosInfo : qosInfoList) {
				qosInfo.setGroupId(groupId);
				// 如果没有名称。自动取名称。 如果已有名称，修改businessid
				if (null == qosInfo.getQosname() || "".equals(qosInfo.getQosname())) {
					if (null == qosname || "".equals(qosname)) {
						qosname = this.getQosName(qosInfo);
					}
					qosInfo.setQosname(qosname);
				}
				// 如果是0 说明是创建时插入qos 设备还没有
				if (qosInfo.getStatus() == 0) {
					qosInfo.setStatus(1);
				}
				this.qosInfoMapper.insert(qosInfo);
			}
			this.sqlSession.commit();
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return groupId;
	}

	/**
	 * 获取qos名称，qostype+businessid.value
	 * @param qosInfo 根据类型和siteid获取
	 * @return
	 * @throws Exception
	 */
	private String getQosName(QosInfo qosInfo) throws Exception, BusinessIdException {
		String qosName = null;
		int i = 0;
		QosInfo qosInfo_select = null;
		List<QosInfo> qosInfoList = null;
		try {
			while (true) {
				qosName = qosInfo.getQosType().toLowerCase() + (qosInfo.getGroupId() + i);
				// 查询qosName是否已经存在
				qosInfo_select = new QosInfo();
				qosInfo_select.setQosname(qosName);
				qosInfo_select.setSiteId(qosInfo.getSiteId());
				qosInfoList = this.qosInfoMapper.queryByCondition(qosInfo_select);
				if (null == qosInfoList || qosInfoList.size() == 0) {
					break;
				} else {
					i++;
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return qosName;
	}

	/**
	 * 创建业务时，验证pw和AC的qos是否匹配 pw的qos要大于每个AC的简单qos+细分qos之和
	 * 
	 * @param pwInfo
	 *            要验证的pw
	 * @param acPortInfoList
	 * @return
	 * @throws Exception
	 */
	public boolean checkPwAndAcQos(PwInfo pwInfo , List<QosInfo> qosInfoList,List<AcPortInfo> acPortInfoList) throws Exception{
		if(null == pwInfo){
			throw new Exception("pwinfo is null");
		}
		if(null == acPortInfoList || acPortInfoList.size() == 0){
			throw new Exception("acPortInfoList is null");
		}
		QosInfo qosInfo=null;
		int direction=0;
		int acCirSum=0;
		boolean flag=true;
		try {
			for(AcPortInfo acPortInfo : acPortInfoList){
				//如果此AC为pw的A端。 那么比较前向
				if(acPortInfo.getSiteId()==pwInfo.getASiteId()){
					direction=EQosDirection.FORWARD.getValue();
				}else if(acPortInfo.getSiteId()==pwInfo.getZSiteId()){
					//如果此AC为pw的Z端。 那么比较后
					direction=EQosDirection.BACKWARD.getValue();
				}else{
					continue;
				}
				//根据方向和优先级从pw的去qos中获取一个qos
				qosInfo = this.getQosInfo(qosInfoList, acPortInfo.getSimpleQos().getCos(), direction+"");
				//如果没有找到qos 说明qos的cos和ac的cos不匹配
				if(null==qosInfo){
					flag=false;
					break;
				}else{
					//计算AC的cir总和
					acCirSum=this.getAcCirSum(acPortInfo);
					//如果ac的总和大于pw的cir 验证不通过
					if(acCirSum>qosInfo.getCir()){
						flag=false;
						break;
					}
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return flag;
	}
	
	/**
	 * 计划ac的带宽总和
	 * @param acPortInfo 要计算的AC对象。 包含流集合、简单qos对象
	 * @return cir总和
	 * @throws Exception
	 */
	private int getAcCirSum(AcPortInfo acPortInfo) throws Exception{
		int result=0;
		try {
			result=acPortInfo.getSimpleQos().getCir();
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return result;
	}
	
	public List<QosInfo> selectByCondition(String objType, List<Integer> objectIdList) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("objType", objType);
		map.put("condition", objectIdList);
		return this.qosInfoMapper.selectByCondition(map);
	}

	/**
	 * 根据qos条件查询
	 * @param qos
	 * @return
	 * @throws Exception
	 */
	public List<QosInfo> queryByCondition(QosInfo qos) throws Exception {
		List<QosInfo> qosInfoList = null;
		try {
			qosInfoList = this.qosInfoMapper.queryByCondition(qos);
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return qosInfoList;
	}
	
	/**
	 * 修改qos带宽
	 * @param qosInfoList
	 * @throws Exception
	 */
	public void updateQos(List<QosInfo> qosInfoList) throws Exception {
		if (null == qosInfoList || qosInfoList.size() == 0) {
			throw new Exception("qosinfoList is null");
		}
		try {
			for (QosInfo qosInfo : qosInfoList) {
				if (QosTemplateTypeEnum.ELSP.toString().equals(qosInfo.getQosType())) {
					this.qosInfoMapper.updateElsp(qosInfo);
				} else {
					this.qosInfoMapper.update(qosInfo);
				}
			}
			this.sqlSession.commit();
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 根据网元ID或者组id集合删除
	 * @param siteId  网元主键
	 * @param groupIds 以逗号区分的组ID
	 * @param status 状态 1=设备不存在，2=设备存在
	 * @throws Exception
	 */
	public void updateStatus(int siteId, String type, String groupIds, int status) throws Exception {
		this.qosInfoMapper.updateStatus(siteId, type, groupIds, status);
		this.sqlSession.commit();
	}

	/**
	 * 查询业务关联的qos集合
	 * 
	 * @param objtype
	 *            业务类型
	 * @param objId
	 *            业务主键
	 * @return
	 * @throws Exception
	 */
	public List<QosInfo> getQosByObj(String objtype, int objId, int siteId) throws Exception {
		QosRelevanceService_MB qosRelevanceService = null;
		QosRelevance qosRelevance = null;
		List<QosRelevance> qosRelevanceList = null;
		List<QosInfo> qosInfoList = null;
		QosInfo qosInfo = null;
		try {
			qosRelevanceService = (QosRelevanceService_MB) ConstantUtil.serviceFactory.newService_MB(Services.QOSRELEVANCE, this.sqlSession);
			qosRelevance = new QosRelevance();
			qosRelevance.setObjId(objId);
			qosRelevance.setObjType(objtype);
			qosRelevance.setSiteId(siteId);
			qosRelevanceList = qosRelevanceService.select(qosRelevance);

			if (null != qosRelevanceList && qosRelevanceList.size() > 0) {
				qosInfo = new QosInfo();
				qosInfo.setGroupId(qosRelevanceList.get(0).getQosGroupId());
				qosInfoList = this.qosInfoMapper.queryByCondition(qosInfo);
			}
		} catch (Exception e) {
			throw e;
		}
		return qosInfoList;
	}
}
