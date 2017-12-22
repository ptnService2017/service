package com.nms.model.ptn.qos;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.nms.db.bean.ptn.path.pw.PwInfo;
import com.nms.db.bean.ptn.path.tunnel.Tunnel;
import com.nms.db.bean.ptn.port.AcPortInfo;
import com.nms.db.bean.ptn.qos.QosInfo;
import com.nms.db.bean.ptn.qos.QosRelevance;
import com.nms.db.dao.ptn.qos.QosRelevanceMapper;
import com.nms.db.enums.EServiceType;
import com.nms.model.ptn.path.tunnel.TunnelService_MB;
import com.nms.model.util.ObjectService_Mybatis;
import com.nms.model.util.Services;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.ExceptionManage;

public class QosRelevanceService_MB extends ObjectService_Mybatis{
	public void setPtnuser(String ptnuser) {
		super.ptnuser = ptnuser;
	}

	public void setSqlSession(SqlSession sqlSession) {
		super.sqlSession = sqlSession;
	}
	
	private QosRelevanceMapper mapper;

	public QosRelevanceMapper getQosRelevanceMapper() {
		return mapper;
	}

	public void setQosRelevanceMapper(QosRelevanceMapper qosRelevanceMapper) {
		this.mapper = qosRelevanceMapper;
	}

	/**
	 * 根据条件查询
	 * @param qosRelevance 查询条件
	 * @return
	 * @throws Exception 
	 */
	public List<QosRelevance> select(QosRelevance qosRelevance) throws Exception{
		if(qosRelevance == null){
			qosRelevance = new QosRelevance();
		}
		return this.mapper.queryByCondition(qosRelevance);
	}

	/**
	 * 插入业务与qos的关联
	 * @param qosRelevanceList qos关联集合
	 * @param qosInfoList qos集合
	 * @return
	 * @throws Exception
	 */
	public void save(List<QosRelevance> qosRelevanceList) throws Exception {
		if (null == qosRelevanceList || qosRelevanceList.size() == 0) {
			throw new Exception("qosRelevanceList is null");
		}
		QosInfoService_MB qosInfoService = null;
		int groupId = 0;
		QosRelevance qosRelevance_select = null;
		List<QosRelevance> qosRelevanceList_select = null;
		boolean isAdd = true; // 是否为增加操作
		try {
			qosInfoService = (QosInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.QosInfo, this.sqlSession);
			// 通过objid和objtype查询 应该新建还是修改操作
			qosRelevance_select = new QosRelevance();
			qosRelevance_select.setObjId(qosRelevanceList.get(0).getObjId());
			qosRelevance_select.setObjType(qosRelevanceList.get(0).getObjType());
			qosRelevanceList_select = this.mapper.queryByCondition(qosRelevance_select);
			if (null != qosRelevanceList_select && qosRelevanceList_select.size() > 0) {
				isAdd = false;
			}
			for (QosRelevance qosRelevance : qosRelevanceList) {
				if(null != qosRelevance.getQosInfoList() && qosRelevance.getQosInfoList().size() > 0){
					groupId = qosInfoService.getGroupId(qosRelevance);
					qosRelevance.setQosGroupId(groupId);
					if (isAdd) {
						this.mapper.insert(qosRelevance);
					} else {
						this.mapper.update(qosRelevance);
					}
				}
			}
			this.sqlSession.commit();
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
	}

	/**
	 * 根据对象不同，组织不同的qos关联集合
	 * @param object tunnel pw ac
	 * @return
	 * @throws Exception
	 */
	public List<QosRelevance> getList(Object object) throws Exception {
		if (null == object) {
			throw new Exception("object is null");
		}
		List<Integer> siteIdList = null;
		TunnelService_MB tunnelService = null;
		EServiceType eServiceType = null;
		PwInfo pwInfo = null;
		AcPortInfo acPortInfo = null;
		List<QosInfo> qosInfoList = null;
		Tunnel tunnel = null;
		QosRelevance qosRelevance = null;
		List<QosRelevance> qosRelevanceList = null;
		int objId = 0;
		try {
			if (object instanceof Tunnel) {
				tunnel = (Tunnel) object;
				tunnelService = (TunnelService_MB) ConstantUtil.serviceFactory.newService_MB(Services.Tunnel,sqlSession);
				siteIdList = tunnelService.getSiteId(tunnel);
				eServiceType = EServiceType.TUNNEL;
				qosInfoList = tunnel.getQosList();
				objId = tunnel.getTunnelId();
			} else if (object instanceof PwInfo) {
				siteIdList = new ArrayList<Integer>();
				pwInfo = (PwInfo) object;
				if(pwInfo.getASiteId()!=0){
					siteIdList.add(pwInfo.getASiteId());
				}
				if(pwInfo.getZSiteId()!=0){
					siteIdList.add(pwInfo.getZSiteId());
				}
				eServiceType = EServiceType.PW;
				qosInfoList = pwInfo.getQosList();
				objId = pwInfo.getPwId();
			} else if (object instanceof AcPortInfo) {
				acPortInfo = (AcPortInfo) object;
				siteIdList = new ArrayList<Integer>();
				siteIdList.add(acPortInfo.getSiteId());
				eServiceType = EServiceType.ACPORT;
				qosInfoList = new ArrayList<QosInfo>();
				qosInfoList.add(acPortInfo.getSimpleQos());
				objId = acPortInfo.getId();
			}
			// 遍历所有网元，创建qosrelevance对象
			qosRelevanceList = new ArrayList<QosRelevance>();
			for (int siteId : siteIdList) {
				qosRelevance = new QosRelevance();
				qosRelevance.setObjType(eServiceType.toString());
				qosRelevance.setQosInfoList(qosInfoList);
				qosRelevance.setObjId(objId);
				qosRelevance.setSiteId(siteId);
				qosRelevance.setObject(object);
				qosRelevance.setQosInfoList(this.getQosInfoList(qosInfoList, siteId));
				qosRelevanceList.add(qosRelevance);
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return qosRelevanceList;
	}

	/**
	 * 业务同步时调用此方法
	 * @param objId 业务主键
	 * @param qosInfo qos对象。用来取groupid和siteid
	 * @param objType 业务类型
	 * @throws Exception 
	 */
	public void synchro(int objId, QosInfo qosInfo, String objType) throws Exception {
		QosRelevance qosRelevance = null;
		List<QosRelevance> qosReleavceList=null;
		try {
			//查询是否存在
			qosRelevance = new QosRelevance();
			qosRelevance.setObjId(objId);
			qosRelevance.setObjType(objType);
			qosRelevance.setSiteId(qosInfo.getSiteId());
			qosReleavceList = this.mapper.queryByCondition(qosRelevance);
			if(objType.equals("TUNNEL")||objType.equals("PW")){
			  if(qosReleavceList != null && qosReleavceList.size()>0){
				  qosRelevance.setQosGroupId(qosReleavceList.get(0).getQosGroupId());
			  }else{
				  qosRelevance.setQosGroupId(qosInfo.getGroupId());
			  }
			}else{
				qosRelevance.setQosGroupId(qosInfo.getGroupId());
			}
			//如果不存在。新建创建  否则修改
			if(null == qosReleavceList || qosReleavceList.size() == 0){
				this.mapper.insert(qosRelevance);
			}else{
				this.mapper.update(qosRelevance);
			}
			this.sqlSession.commit();
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
	}
	
	private List<QosInfo> getQosInfoList(List<QosInfo> qosInfoList,int siteId) throws Exception{
		QosInfo qosInfo_result=null;
		List<QosInfo> qosInfoList_result=null;
		try {
			qosInfoList_result=new ArrayList<QosInfo>();
			for(QosInfo qosInfo : qosInfoList){
				qosInfo_result=new QosInfo();
				qosInfo_result.setId(qosInfo.getId());
				qosInfo_result.setSiteId(siteId);
				qosInfo_result.setGroupId(qosInfo.getGroupId());
				qosInfo_result.setQosType(qosInfo.getQosType());
				qosInfo_result.setQosname(qosInfo.getQosname());
				qosInfo_result.setSeq(qosInfo.getSeq());
				qosInfo_result.setCos(qosInfo.getCos());
				qosInfo_result.setDirection(qosInfo.getDirection());
				qosInfo_result.setCir(qosInfo.getCir());
				qosInfo_result.setCbs(qosInfo.getCbs());
				qosInfo_result.setEir(qosInfo.getEir());
				qosInfo_result.setEbs(qosInfo.getEbs());
				qosInfo_result.setPir(qosInfo.getPir());
				qosInfo_result.setPbs(qosInfo.getPbs());
				qosInfo_result.setStrategy(qosInfo.getStrategy());
				qosInfo_result.setColorSence(qosInfo.getColorSence());
				qosInfo_result.setStatus(qosInfo.getStatus());
				qosInfo_result.setBufferList(qosInfo.getBufferList());
				qosInfoList_result.add(qosInfo_result);
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return qosInfoList_result;
	}
	
	/**
	 * 根据条件查询
	 * @param qosRelevance 查询条件
	 * @return
	 * @throws Exception 
	 */
	public List<QosRelevance> selectFor700Plus(QosRelevance qosRelevance) throws Exception{
		return this.mapper.queryByConditionFor700Plus(qosRelevance);
	}
	
	/**
	 * 修改qos的状态
	 * @param object	业务类型
	 * @throws Exception 
	 */
	public void updateQosStatus(Object object) throws Exception{
		QosRelevance qosRelevance=null;
		List<QosRelevance> qosRelevanceList=null;
		String groupId=null;
		QosInfoService_MB qosInfoService=null;
		try {
			qosRelevance=new QosRelevance();
			if(object instanceof Tunnel){
				qosRelevance.setObjType(EServiceType.TUNNEL.toString());
				qosRelevance.setObjId(((Tunnel)object).getTunnelId());
			}else if(object instanceof PwInfo){
				qosRelevance.setObjType(EServiceType.PW.toString());
				qosRelevance.setObjId(((PwInfo)object).getPwId());
			}else if(object instanceof AcPortInfo){
				qosRelevance.setObjType(EServiceType.ACPORT.toString());
				qosRelevance.setObjId(((AcPortInfo)object).getId());
			}
			
			//查询此业务下的所有qosgroupid
			qosRelevanceList=this.select(qosRelevance);
			groupId="";
			for(QosRelevance qosRelevance_select : qosRelevanceList){
				groupId+=qosRelevance_select.getQosGroupId()+",";
			}
			
			qosInfoService=(QosInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.QosInfo, this.sqlSession);
			qosInfoService.updateStatus(0, null , groupId.substring(0, groupId.length()-1), 2);
			this.sqlSession.commit();
		} catch (Exception e) {
			throw e;
		}
	}
}
