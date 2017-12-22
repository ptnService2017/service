package com.nms.model.ptn.path.tunnel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.nms.db.bean.path.Segment;
import com.nms.db.bean.ptn.Businessid;
import com.nms.db.bean.ptn.LabelInfo;
import com.nms.db.bean.ptn.SiteRoate;
import com.nms.db.bean.ptn.oam.OamInfo;
import com.nms.db.bean.ptn.oam.OamMepInfo;
import com.nms.db.bean.ptn.oam.OamMipInfo;
import com.nms.db.bean.ptn.path.ces.CesInfo;
import com.nms.db.bean.ptn.path.tunnel.Lsp;
import com.nms.db.bean.ptn.path.tunnel.Tunnel;
import com.nms.db.bean.ptn.qos.QosInfo;
import com.nms.db.bean.ptn.qos.QosRelevance;
import com.nms.db.dao.alarm.CurrentAlarmInfoMapper;
import com.nms.db.dao.alarm.HisAlarmInfoMapper;
import com.nms.db.dao.perform.HisPerformanceInfoMapper;
import com.nms.db.dao.ptn.BusinessidMapper;
import com.nms.db.dao.ptn.LabelInfoMapper;
import com.nms.db.dao.ptn.SiteRoateMapper;
import com.nms.db.dao.ptn.oam.OamMepInfoMapper;
import com.nms.db.dao.ptn.oam.OamMipInfoMapper;
import com.nms.db.dao.ptn.path.ces.CesInfoMapper;
import com.nms.db.dao.ptn.path.pw.MsPwInfoMapper;
import com.nms.db.dao.ptn.path.pw.PwInfoMapper;
import com.nms.db.dao.ptn.path.tunnel.LspinfoMapper;
import com.nms.db.dao.ptn.path.tunnel.TunnelMapper;
import com.nms.db.dao.ptn.qos.QosInfoMapper;
import com.nms.db.dao.ptn.qos.QosRelevanceMapper;
import com.nms.db.enums.EActionType;
import com.nms.db.enums.EManufacturer;
import com.nms.db.enums.EQosDirection;
import com.nms.db.enums.EServiceType;
import com.nms.db.enums.OamTypeEnum;
import com.nms.model.equipment.port.PortService_MB;
import com.nms.model.equipment.shlef.SiteService_MB;
import com.nms.model.path.SegmentService_MB;
import com.nms.model.ptn.BusinessidService_MB;
import com.nms.model.ptn.LabelInfoService_MB;
import com.nms.model.ptn.SiteRoateService_MB;
import com.nms.model.ptn.oam.OamInfoService_MB;
import com.nms.model.ptn.qos.QosInfoService_MB;
import com.nms.model.ptn.qos.QosRelevanceService_MB;
import com.nms.model.util.LabelManage;
import com.nms.model.util.ObjectService_Mybatis;
import com.nms.model.util.ServiceFactory;
import com.nms.model.util.Services;
import com.nms.ui.manager.BusinessIdException;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.DateUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.ResourceUtil;
import com.nms.ui.manager.UiUtil;
import com.nms.ui.manager.keys.StringKeysTip;
import com.nms.util.Mybatis_DBManager;

public class TunnelService_MB extends ObjectService_Mybatis {
	public void setSqlSession(SqlSession sqlSession) {
		super.sqlSession = sqlSession;
	}

	public void setPtnuser(String ptnuser) {
		super.ptnuser = ptnuser;
	}

	private TunnelMapper tunnelMapper;
	

	/**
	 * 过滤查询tunnel ， tunnel列表中用到
	 * 
	 * @param tunnelFilter
	 * @return
	 * @throws Exception
	 */
	public List<Tunnel> filterSelect(Tunnel tunnelFilter) throws Exception {
		List<Tunnel> tunnelList = null;
		try {
			tunnelList = this.tunnelMapper.filterSelect(tunnelFilter);
			this.setTunnelLsp(tunnelList, false);
		} catch (Exception e) {
			throw e;
		}
		return tunnelList;
	}

	/**
	 * 根据条件查询 不关联其他表
	 * 
	 * @param tunnel
	 * @return
	 * @throws Exception
	 */
	public List<Tunnel> select_nojoin(Tunnel tunnel) throws Exception {
		Lsp lspParticular = null;
		LspinfoMapper lspParticularMapper = null;
		List<Tunnel> infos = null;
		List<Lsp> lspparticularList = null;
		try {
			infos = this.tunnelMapper.queryByCondition_nojoin(tunnel);
			if (null != infos && infos.size() != 0) {
				lspParticularMapper = sqlSession.getMapper(LspinfoMapper.class);
				for (int i = 0; i < infos.size(); i++) {
					lspParticular = new Lsp();
					lspParticular.setTunnelId(infos.get(i).getTunnelId());
					lspparticularList = lspParticularMapper.queryByCondition(lspParticular);
					infos.get(i).setLspParticularList(lspparticularList);
				}

				setOtherInfomationforTunnel(infos);
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return infos;
	}

	/**
	 * 通过主键id查询
	 * 
	 * @param tunnelID
	 * @return
	 */
	public Tunnel selectByID(Integer tunnelID) {
		Tunnel tunnel = null;
		tunnel = tunnelMapper.selectByPrimaryKey(tunnelID);
		return tunnel;
	}

	/**
	 * 新建tunnel
	 * 
	 * @param tunnel
	 * @return
	 * @throws Exception
	 */
	public int save(Tunnel tunnel) throws Exception {

		if (tunnel == null) {
			throw new Exception("tunnel is null");
		}

		Lsp lspParticular = null;
		LspinfoMapper lspinfoMapper = null;
		BusinessidService_MB businessidServiceMB = null;
		OamInfoService_MB oamInfoServiceMB = null;
		// ProtectInfoService pInfoService = null;
		Businessid businessid_tunnel = null;

		List<Lsp> lspparticularlist = null;
		Map<Integer, Integer> siteServicemap = null;
		int tunnelId = 0;
		int isSingle = -1;
		int protectTunnelId = 0;
		QosRelevanceService_MB qosRelevanceServiceMB = null;
		List<QosRelevance> qosRelevanceList = null;
		SiteService_MB siteServiceMB = null;
		LabelInfoService_MB labelInfoServiceMB = null;
		try {
			sqlSession.commit(false);
			siteServiceMB = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE, this.sqlSession);
			businessidServiceMB = (BusinessidService_MB) ConstantUtil.serviceFactory.newService_MB(Services.BUSINESSID, this.sqlSession);
			oamInfoServiceMB = (OamInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.OamInfo, this.sqlSession);
			LabelInfoMapper labelInfoMapper = sqlSession.getMapper(LabelInfoMapper.class);
			labelInfoServiceMB = (LabelInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.LABELINFO, this.sqlSession);
			siteServicemap = new HashMap<Integer, Integer>();//
			// 用于保存网元，以及对应的ServiceId
			tunnel.setCreateTime(DateUtil.getDate(DateUtil.FULLTIME));
			tunnelMapper.insert(tunnel);
			tunnelId = tunnel.getTunnelId();
			isSingle = tunnel.getIsSingle();

			lspinfoMapper = sqlSession.getMapper(LspinfoMapper.class);
			lspparticularlist = tunnel.getLspParticularList();
			if (lspparticularlist != null && lspparticularlist.size() > 0) {
				for (int j = 0; j < lspparticularlist.size(); j++) {
					lspParticular = lspparticularlist.get(j);
					lspParticular.setTunnelId(tunnelId);

					// // 配置标签
					this.getLabel(lspParticular, isSingle, siteServiceMB, labelInfoMapper, labelInfoServiceMB);

					// /* 如果j>0说明有多条leg，就有xc存在 xc的上条数据的z设备id应该等于的二条数据的a设备id */
					if (j > 0) {
						lspParticular.setAtunnelbusinessid(businessid_tunnel.getIdValue());
					} else {
						// // 数据中存在a网元时，配置a端的设备id
						if (lspParticular.getAPortId() > 0) {
							// /** 获取A端的tunnel业务id */
							businessid_tunnel = this.setbusinessId(lspParticular, siteServicemap, businessidServiceMB, "a");
						}
					}
					// 数据中存在Z网元时，配置Z端的设备id
					if (lspParticular.getZPortId() > 0) {
						/** 获取Z端的tunnel业务id */
						businessid_tunnel = this.setbusinessId(lspParticular, siteServicemap, businessidServiceMB, "z");
					}

					lspinfoMapper.insert(lspParticular);
				}

			}

			for (OamInfo oamInfo : tunnel.getOamList()) {
				if (oamInfo.getOamType() == OamTypeEnum.AMEP || oamInfo.getOamType() == OamTypeEnum.ZMEP) {
					oamInfo.getOamMep().setServiceId(tunnelId);
					oamInfo.getOamMep().setObjId(siteServicemap.get(oamInfo.getOamMep().getSiteId()));

					// 如果tunnel的类型是保护，并且是晨晓的设备⾿就去修改保护tunnel oam的objid
					if (tunnel.getProtectTunnelId() > 0 && siteServiceMB.getManufacturer(oamInfo.getOamMep().getSiteId()) == EManufacturer.CHENXIAO.getValue()) {
						oamInfoServiceMB.update_mep_objid(siteServicemap.get(oamInfo.getOamMep().getSiteId()), oamInfo.getOamMep().getSiteId(), tunnel.getProtectTunnelId(), EServiceType.TUNNEL.toString());
					}
				} else if (oamInfo.getOamType() == OamTypeEnum.MIP) {
					oamInfo.getOamMip().setServiceId(tunnelId);
					oamInfo.getOamMip().setObjId(siteServicemap.get(oamInfo.getOamMip().getSiteId()));
				} else if (oamInfo.getOamType() == OamTypeEnum.MEP) {
					oamInfo.getOamMep().setServiceId(tunnelId);
				}
				oamInfoServiceMB.saveOrUpdate(oamInfo);
			}

			qosRelevanceServiceMB = (QosRelevanceService_MB) ConstantUtil.serviceFactory.newService_MB(Services.QOSRELEVANCE, this.sqlSession);
			qosRelevanceList = qosRelevanceServiceMB.getList(tunnel);
			qosRelevanceServiceMB.save(qosRelevanceList);

			// 保存保护tunnel
			if (null != tunnel.getProtectTunnel()) {
				tunnel.getProtectTunnel().setTunnelName(tunnel.getTunnelName() + "_protect");
				tunnel.getProtectTunnel().setTunnelStatus(tunnel.getTunnelStatus());
				this.savaProtect(tunnel.getProtectTunnel());
				protectTunnelId = tunnel.getProtectTunnel().getTunnelId();
				tunnel.setProtectTunnelId(protectTunnelId);
				tunnelMapper.updateByPrimaryKey(tunnel);
			}

			// 离线网元数据下载
			if (0 != tunnel.getASiteId()) {
				super.dateDownLoad(tunnel.getASiteId(), tunnelId, EServiceType.TUNNEL.getValue(), EActionType.INSERT.getValue(), tunnel.getLspParticularList().get(0).getAtunnelbusinessid() + "", null, tunnel.getAPortId(), 0, null);
			}
			if (0 != tunnel.getZSiteId()) {
				super.dateDownLoad(tunnel.getZSiteId(), tunnelId, EServiceType.TUNNEL.getValue(), EActionType.INSERT.getValue(), tunnel.getLspParticularList().get(0).getZtunnelbusinessid() + "", null, tunnel.getZPortId(), 0, null);
			}
			sqlSession.commit();
		} catch (BusinessIdException e) {
			sqlSession.rollback();
			throw e;
		} catch (Exception e) {
			sqlSession.rollback();
			ExceptionManage.dispose(e, this.getClass());
		} 
		return tunnelId;

	}

	/**
	 * 先把保护tunnel插入到数据库䶿
	 * 
	 * @author kk
	 * 
	 * @param
	 * 
	 * @return
	 * @throws Exception
	 * 
	 * @Exception 异常对象
	 */
	public int savaProtect(Tunnel protectTunnel) throws Exception {
		BusinessidService_MB businessidService_MB = null;
		// QosInfoService qosInfoService = null;
		int tunnelId = 0;
		int isSingle = -1;
		Lsp lspParticular = null;
		LspinfoMapper lspinfoMapper = null;
		List<Lsp> lspparticularlist = null;
		Businessid businessid_tunnel = null;
		Map<Integer, Integer> siteServicemap = null;
		OamInfoService_MB oamInfoService_MB = null;
		Businessid abusinessid = null;
		Businessid zbusinessid = null;
		QosRelevanceService_MB qosRelevanceService_MB = null;
		List<QosRelevance> qosRelevanceList = null;
		SiteRoateService_MB siteRoateService_MB = null;// 添加保护tunnel时，添加倒换命令
		SiteRoate siteRoate = null;
		SiteService_MB siteService_MB = null;
		LabelInfoService_MB labelInfoServiceMB = null;
		try {
			LabelInfoMapper labelInfoMapper = sqlSession.getMapper(LabelInfoMapper.class);
			labelInfoServiceMB = (LabelInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.LABELINFO, this.sqlSession);
			siteService_MB = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE, this.sqlSession);
			businessidService_MB = (BusinessidService_MB) ConstantUtil.serviceFactory.newService_MB(Services.BUSINESSID, this.sqlSession);
			oamInfoService_MB = (OamInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.OamInfo, this.sqlSession);

			siteServicemap = new HashMap<Integer, Integer>();// 用于保存网元，以及对应的ServiceId

			// 生成保护id(武汉)
			if (protectTunnel.getASiteId() != 0 && siteService_MB.getManufacturer(protectTunnel.getASiteId()) == EManufacturer.WUHAN.getValue()) {
				abusinessid = businessidService_MB.select(protectTunnel.getASiteId(), "lspprotect");
				if (abusinessid == null) {
					throw new BusinessIdException(siteService_MB.getSiteName(protectTunnel.getASiteId()) + ResourceUtil.srcStr(StringKeysTip.TIP_TUNNEL_PROTECT_ID));
				}
				businessidService_MB.update(abusinessid.getId(), 1);// 将该保护id设置为不可用
				protectTunnel.setAprotectId(abusinessid.getIdValue());
			}
			if (protectTunnel.getZSiteId() != 0 && siteService_MB.getManufacturer(protectTunnel.getZSiteId()) == EManufacturer.WUHAN.getValue()) {
				zbusinessid = businessidService_MB.select(protectTunnel.getZSiteId(), "lspprotect");
				if (zbusinessid == null) {
					throw new BusinessIdException(siteService_MB.getSiteName(protectTunnel.getZSiteId()) + ResourceUtil.srcStr(StringKeysTip.TIP_TUNNEL_PROTECT_ID));
				}
				businessidService_MB.update(zbusinessid.getId(), 1);// 将该保护id设置为不可用
				protectTunnel.setZprotectId(zbusinessid.getIdValue());
			}
			
			protectTunnel.setCreateTime(DateUtil.getDate(DateUtil.FULLTIME));
			tunnelMapper.insert(protectTunnel);
			tunnelId = protectTunnel.getTunnelId();
			// 添加倒换命令
			siteRoateService_MB = (SiteRoateService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITEROATE, this.sqlSession);
			siteRoate = new SiteRoate();
			siteRoate.setType("tunnel");
			siteRoate.setRoate(-1);
			siteRoate.setTypeId(tunnelId);
			if (protectTunnel.getASiteId() > 0) {
				siteRoate.setSiteId(protectTunnel.getASiteId());
				siteRoateService_MB.insert(siteRoate);
			}
			if (protectTunnel.getZSiteId() > 0) {
				siteRoate.setSiteId(protectTunnel.getZSiteId());
				siteRoateService_MB.insert(siteRoate);
			}
			// }
			protectTunnel.setTunnelId(tunnelId);
			isSingle = protectTunnel.getIsSingle();

			lspinfoMapper = sqlSession.getMapper(LspinfoMapper.class);
			lspparticularlist = protectTunnel.getLspParticularList();

			for (int j = 0; j < lspparticularlist.size(); j++) {
				lspParticular = lspparticularlist.get(j);
				lspParticular.setTunnelId(tunnelId);

				// 配置标签
				this.getLabel(lspParticular, isSingle, siteService_MB, labelInfoMapper, labelInfoServiceMB);

				/* 如果j>0说明有多条leg，就有xc存在 xc的上条数据的z设备id应该等于的二条数据的a设备id */
				if (j > 0) {
					if (lspParticular.getASiteId() != protectTunnel.getASiteId()) {
						lspParticular.setAtunnelbusinessid(businessid_tunnel.getIdValue());
					}
				} else {
					// 数据中存在a网元时，配置a端的设备id
					if (lspParticular.getAPortId() > 0) {
						if (lspParticular.getASiteId() != protectTunnel.getASiteId()) {
							businessid_tunnel = this.setbusinessId(lspParticular, siteServicemap, businessidService_MB, "a");
						} else {
							// 如果是武汉设备，取businessid
							// 如果是晨晓设备，把主用tunnel的businessid赋到siteServicemap䶿
							if (siteService_MB.getManufacturer(lspParticular.getASiteId()) == EManufacturer.WUHAN.getValue()) {
								businessid_tunnel = this.setbusinessId(lspParticular, siteServicemap, businessidService_MB, "a");
							} else {
								if (null == businessid_tunnel) {
									siteServicemap.put(lspParticular.getASiteId(), 0);
								} else {
									siteServicemap.put(lspParticular.getASiteId(), businessid_tunnel.getIdValue());
								}
							}
						}
					}
				}
				// 数据中存在Z网元时，配置Z端的设备id
				if (lspParticular.getZPortId() > 0) {
					if (lspParticular.getZSiteId() != protectTunnel.getZSiteId()) {
						businessid_tunnel = this.setbusinessId(lspParticular, siteServicemap, businessidService_MB, "z");
					} else {
						// 如果是武汉设备，取businessid
						// 如果是晨晓设备，把主用tunnel的businessid赋到siteServicemap䶿
						if (siteService_MB.getManufacturer(lspParticular.getZSiteId()) == EManufacturer.WUHAN.getValue()) {
							businessid_tunnel = this.setbusinessId(lspParticular, siteServicemap, businessidService_MB, "z");
						} else {
							if (null == businessid_tunnel) {
								siteServicemap.put(lspParticular.getZSiteId(), 0);
							} else {
								siteServicemap.put(lspParticular.getZSiteId(), businessid_tunnel.getIdValue());
							}
						}
					}
				}

				lspinfoMapper.insert(lspParticular);
			}

			// oam
			for (OamInfo oamInfo : protectTunnel.getOamList()) {
				if (oamInfo.getOamType() == OamTypeEnum.AMEP || oamInfo.getOamType() == OamTypeEnum.ZMEP) {
					oamInfo.getOamMep().setServiceId(tunnelId);
					oamInfo.getOamMep().setObjId(siteServicemap.get(oamInfo.getOamMep().getSiteId()));
				} else if (oamInfo.getOamType() == OamTypeEnum.MIP) {
					oamInfo.getOamMip().setServiceId(tunnelId);
					oamInfo.getOamMip().setObjId(siteServicemap.get(oamInfo.getOamMip().getSiteId()));
				} else if (oamInfo.getOamType() == OamTypeEnum.MEP) {
					oamInfo.getOamMep().setServiceId(tunnelId);
				}
				oamInfoService_MB.saveOrUpdate(oamInfo);
			}

			qosRelevanceService_MB = (QosRelevanceService_MB) ConstantUtil.serviceFactory.newService_MB(Services.QOSRELEVANCE, this.sqlSession);
			qosRelevanceList = qosRelevanceService_MB.getList(protectTunnel);
			qosRelevanceService_MB.save(qosRelevanceList);

			// 离线网元数据下载
			if (0 != protectTunnel.getASiteId()) {
				super.dateDownLoad(protectTunnel.getASiteId(), tunnelId, EServiceType.TUNNEL.getValue(), EActionType.INSERT.getValue());
			}
			if (0 != protectTunnel.getZSiteId()) {
				super.dateDownLoad(protectTunnel.getZSiteId(), tunnelId, EServiceType.TUNNEL.getValue(), EActionType.INSERT.getValue());
			}
		} catch (Exception e) {
			throw e;
		} finally {
		}

		return tunnelId;
	}

	/**
	 * 获取label
	 * 
	 * @param lsp
	 *            lsp对象
	 * @param labelInfoServiceMB2 
	 * @param labelInfoMapper2 
	 * @param siteServiceMB2 
	 * @return
	 * @throws Exception
	 */
	public void getLabel(Lsp lsp, int isSingle, SiteService_MB siteServiceMB, LabelInfoMapper labelInfoMapper, LabelInfoService_MB labelInfoServiceMB) throws Exception {
		if (null == lsp) {
			throw new Exception("lsp is null");
		}
		try {
			String type = "";
			int manufacturerA = 0;
			int manufacturerZ = 0;
			int label = 0;
			// 判断isSingle为0，则代表网络侧，1，则代表单站侧
			// 等于1是晨晓设备,入标签网元唯一
			manufacturerA = siteServiceMB.getManufacturer(lsp.getASiteId());
			manufacturerZ = siteServiceMB.getManufacturer(lsp.getZSiteId());
			if (isSingle == 0 || (lsp.getFrontLabelValue() == 0 && lsp.getBackLabelValue() == 0)) {
				// 没有填写标签值说明是批量创建Tunnel，自动分配前向标签
				if (lsp.getFrontLabelValue() == 0) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("aSiteId", lsp.getASiteId());
					map.put("zSiteId", lsp.getZSiteId());
					map.put("type", "WH");
					List<Integer> integers = new ArrayList<Integer>();
					map.put("condition", integers);
					map.put("manufacturerA", manufacturerA);
					map.put("manufacturerZ", manufacturerZ);
					List<Integer> labelList = labelInfoMapper.queryLabelListBySite(map);
					if (labelList.size() == 0) {
						label = this.initLabel(lsp.getASiteId(), lsp.getZSiteId(), labelInfoMapper, manufacturerA, manufacturerZ);
					} else {
						label = labelList.get(0);
					}
					lsp.setFrontLabelValue(label);
				}
				// 将前向标签插入labelInfo表中
				if (manufacturerZ == 1) {
					type = "CX";
				} else {
					type = "WH";
				}
				LabelInfo labelInfo = new LabelInfo();
				labelInfo.setLabelValue(lsp.getFrontLabelValue());
				labelInfo.setSiteid(lsp.getZSiteId());
				labelInfo.setType(type);
				labelInfoMapper.insertNewLabel(labelInfo);
				labelInfoMapper.updateBatch(labelInfo);
				// 没有填写标签值说明是批量创建Tunnel，自动分配后向标签
				if (lsp.getBackLabelValue() == 0) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("aSiteId", lsp.getASiteId());
					map.put("zSiteId", lsp.getZSiteId());
					map.put("type", "WH");
					List<Integer> integers = new ArrayList<Integer>();
					map.put("condition", integers);
					map.put("manufacturerA", manufacturerA);
					map.put("manufacturerZ", manufacturerZ);
					List<Integer> labelList = labelInfoMapper.queryLabelListBySite(map);
					if (labelList.size() == 0) {
						label = this.initLabel(lsp.getASiteId(), lsp.getZSiteId(), labelInfoMapper, manufacturerA, manufacturerZ);
					} else {
						label = labelList.get(0);
					}
					lsp.setBackLabelValue(label);
				}
				// 将后向标签插入labelInfo表中
				if (manufacturerA == 1) {
					type = "CX";
				} else {
					type = "WH";
				}
				LabelInfo labelInfo_A = new LabelInfo();
				labelInfo_A.setLabelValue(lsp.getBackLabelValue());
				labelInfo_A.setSiteid(lsp.getASiteId());
				labelInfo_A.setType(type);
				labelInfoMapper.insertNewLabel(labelInfo_A);
				labelInfoMapper.updateBatch(labelInfo_A);
			} else {
				// 填写了标签,说明是单网元配置，直接修改标签状态
				if (lsp.getASiteId() > 0) {
					if (manufacturerA == 1) {
						type = "CX";
					} else {
						type = "WH";
					}
					labelInfoServiceMB.saveOrUpdate(lsp.getBackLabelValue(), lsp.getASiteId(), 0, type);
				}
				if (lsp.getZSiteId() > 0) {
					if (manufacturerZ == 1) {
						type = "CX";
					} else {
						type = "WH";
					}
					labelInfoServiceMB.saveOrUpdate(lsp.getFrontLabelValue(), lsp.getZSiteId(), 0, type);
				}
			}
			this.sqlSession.commit();
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
	}

	/**
	 * 获取businessid 并修改businessid状徿
	 * 
	 * @author kk
	 * 
	 * @param
	 * 
	 * @return
	 * 
	 * @Exception 异常对象
	 */
	public Businessid setbusinessId(Lsp lspParticular, Map<Integer, Integer> siteServicemap, BusinessidService_MB businessidService_MB, String type) throws Exception {

		int businessId = 0;
		int siteId = 0;
		Businessid businessid_tunnel = null;
		SiteService_MB siteService_MB = null;
		try {
			siteService_MB = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE, this.sqlSession);
			if ("a".equals(type)) {
				businessId = lspParticular.getAtunnelbusinessid();
				siteId = lspParticular.getASiteId();
			} else {
				businessId = lspParticular.getZtunnelbusinessid();
				siteId = lspParticular.getZSiteId();
			}

			/** 获取A端的tunnel业务id */
			if (businessId == 0) {
				businessid_tunnel = businessidService_MB.select(siteId, "tunnel", 0);
			} else {
				businessid_tunnel = businessidService_MB.select(siteId, "tunnel", businessId);
			}
			if (businessid_tunnel == null) {
				throw new BusinessIdException(siteService_MB.getSiteName(siteId) + ResourceUtil.srcStr(StringKeysTip.TIP_TUNNELID));
			}
			if ("a".equals(type)) {
				lspParticular.setAtunnelbusinessid(businessid_tunnel.getIdValue());
			} else {
				lspParticular.setZtunnelbusinessid(businessid_tunnel.getIdValue());
			}
			siteServicemap.put(siteId, businessid_tunnel.getIdValue());
			businessidService_MB.update(businessid_tunnel.getId(), 1);
		} catch (Exception e) {
			throw e;
		} finally {
		}
		return businessid_tunnel;
	}

	/**
	 * 查询所有
	 * 
	 * @return
	 */
	public List<Tunnel> selectAll() {
		List<Tunnel> tunnels = tunnelMapper.selectAll();
		return tunnels;
	}

	/**
	 * 初始化标签
	 */
	private int initLabel(int asiteId, int zsiteId, LabelInfoMapper labelInfoMapper, int manufacturerA, int manufacturerZ) throws Exception {
		try {
			while (true) {
				LabelManage labelManage = new LabelManage();
				labelManage.addLabel(asiteId, zsiteId, "WH", labelInfoMapper);
				this.sqlSession.commit();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("aSiteId", asiteId);
				map.put("zSiteId", zsiteId);
				map.put("type", "WH");
				List<Integer> integers = new ArrayList<Integer>();
				map.put("condition", integers);
				map.put("manufacturerA", manufacturerA);
				map.put("manufacturerZ", manufacturerZ);
				List<Integer> labelList = labelInfoMapper.queryLabelListBySite(map);
				for (Integer label : labelList) {
					if (label > 0) {
						return label;
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 查询单网元的tunnel
	 * 
	 * @param siteId
	 * @return
	 * @throws Exception
	 */
	public List<Tunnel> selectNodesBySiteId(int siteId) throws Exception {

		List<Tunnel> tunnelList = null;
		Lsp lsp = null;
		LspinfoMapper lspDao = null;
		try {
			lspDao = sqlSession.getMapper(LspinfoMapper.class);
			tunnelList = this.tunnelMapper.quertyNodeBySite(siteId);
			for (Tunnel tunnel : tunnelList) {
				tunnel.setNode(true);
				lsp = new Lsp();
				lsp.setTunnelId(tunnel.getTunnelId());
				tunnel.setLspParticularList(lspDao.queryByCondition(lsp));
			}
			this.setOtherInfomationforTunnel(tunnelList);

		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			lsp = null;
			lspDao = null;
		}
		return tunnelList;
	}
	
	/**
	 * 查询武汉单网元的tunnel 包含保护tunnel
	 * 
	 * @param siteId
	 * @return
	 * @throws Exception
	 */
	public List<Tunnel> selectWHNodesBySiteId(int siteId) throws Exception {

		List<Tunnel> tunnelList = null;
		LspinfoMapper lspInfoMapper = null;
		List<Lsp> lspparticularList = null;
		try {
			lspInfoMapper = this.sqlSession.getMapper(LspinfoMapper.class);
			tunnelList = new ArrayList<Tunnel>();
			tunnelList = this.tunnelMapper.quertyWHNodeBySite(siteId);
			for (Tunnel tunnel : tunnelList) {
				tunnel.setNode(true);
				lspparticularList = new ArrayList<Lsp>();
				lspparticularList = lspInfoMapper.queryBySiteId(siteId, tunnel.getTunnelId());
				tunnel.setLspParticularList(lspparticularList);
			}
			this.setOtherInfomationforTunnel(tunnelList);

		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			lspparticularList = null;
			lspInfoMapper = null;
		}
		return tunnelList;
	}

	// 查询并设置OAM、QoS、保护信忿
	private void setOtherInfomationforTunnel(List<Tunnel> tunnels) {
		OamInfoService_MB oamInfoServiceMB = null;
		QosInfoService_MB qosInfoServiceMB = null;
		OamInfo oamInfo = null;
		OamMepInfo oamMepInfo = null;
		OamMipInfo oamMipInfo = null;
		List<OamInfo> oamInfoList;
		try {
			qosInfoServiceMB = (QosInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.QosInfo, this.sqlSession);
			oamInfoServiceMB = (OamInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.OamInfo, this.sqlSession);

			for (int i = 0; i < tunnels.size(); i++) {
				oamInfoList = new ArrayList<OamInfo>();
				oamInfo = new OamInfo();
				oamMepInfo = new OamMepInfo();
				oamMepInfo.setServiceId(tunnels.get(i).getTunnelId());
				oamMepInfo.setObjType("TUNNEL");
				oamInfo.setOamMep(oamMepInfo);

				oamMipInfo = new OamMipInfo();
				oamMipInfo.setServiceId(tunnels.get(i).getTunnelId());
				oamMipInfo.setObjType("TUNNEL");
				oamInfo.setOamMip(oamMipInfo);

				oamInfoList = oamInfoServiceMB.queryByServiceId(oamInfo);

				for (OamInfo obj : oamInfoList) {
					if (null != obj.getOamMep())
						if (obj.getOamMep().getSiteId() == tunnels.get(i).getaSiteId()) {
							obj.setOamType(OamTypeEnum.AMEP);
						} else {
							obj.setOamType(OamTypeEnum.ZMEP);
						}
					if (null != obj.getOamMip())
						obj.setOamType(OamTypeEnum.MIP);
				}
				tunnels.get(i).setOamList(oamInfoList);

				// 查询qos
				tunnels.get(i).setQosList(qosInfoServiceMB.getQosByObj(EServiceType.TUNNEL.toString(), tunnels.get(i).getTunnelId()));
				String type = tunnels.get(i).getTunnelType();
				if (Integer.parseInt(type) > 0) {// 过滤保护tunnel
					String code = UiUtil.getCodeById(Integer.parseInt(type)).getCodeValue();

					if (!"0".equals(type) && ("2".equals(code) || "3".equals(code)) && tunnels.get(i).getProtectTunnelId() > 0) {
						Tunnel tunnelSelect = new Tunnel();
						tunnelSelect.setTunnelId(tunnels.get(i).getProtectTunnelId());
						List<Tunnel> tunnelList = this.select_nojoin(tunnelSelect);
						if (null != tunnelList) {
							if (tunnelList.size() != 1) {
								tunnels.get(i).setProtectTunnel(null);
							} else {
								tunnels.get(i).setProtectTunnel(tunnelList.get(0));
							}
						}

					}
				}

				// if (tunnels.get(i).getProtectTunnelId() != 0) {
				// protectionInfo = new ProtectionInfo();
				// protectionInfo.setTunnelId(tunnels.get(i).getTunnelId());
				// tunnels.get(i).setProList(pInfoService.selectByTunnelId(protectionInfo));
				// }
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			// UiUtil.closeService(oamInfoService);
			// UiUtil.closeService(qosInfoService);
		}
	}

	/**
	 * 根据两个端口查询出跟这两个端口相关联的所有tunnel 段模块中查询剩余带宽时用
	 * 
	 * @param portId1
	 *            端口id=段的aportid
	 * @param portId2
	 *            端口id=段的zportid
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	public List<Tunnel> selectByPort(int portId1, int portId2) throws Exception {

		List<Tunnel> tunnelList = null;
		List<Tunnel> tunnels = null;
		try {
			tunnelList = new ArrayList<Tunnel>();
			tunnelList = this.tunnelMapper.queryPort(portId1, portId2);
			tunnels = new ArrayList<Tunnel>();
			for (Tunnel tunnel : tunnelList) {
				// 如果是保护的tunnel 并且入参为tunnel的a端或者z端 则不参与计算。 因为保护tunnel的xc才有实际意义
				if (!("0".equals(tunnel.getTunnelType()) && (tunnel.getAPortId() == portId1 || tunnel.getAPortId() == portId1 || tunnel.getAPortId() == portId2 || tunnel.getZPortId() == portId2))) {
					tunnels.add(tunnel);
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return tunnels;
	}

	/**
	 * 通过portId和网元id查询
	 * 
	 * @param siteId
	 * @param serviceId
	 * @return
	 * @throws Exception
	 */

	public List<Tunnel> selectByPortIdAndSiteId(int siteId, int portId) throws Exception {
		LspinfoMapper lspInfoMapper = null;
		Lsp lsp = null;
		List<Tunnel> tunnelList = null;
		try {
			tunnelList = new ArrayList<Tunnel>();
			Tunnel tunnels = new Tunnel();
			tunnels.setaSiteId(siteId);
			tunnels.setaPortId(portId);
			tunnelList = this.tunnelMapper.queryBySiteIdAndPortId(tunnels);
			for (Tunnel tunnel : tunnelList) {
				lspInfoMapper = this.sqlSession.getMapper(LspinfoMapper.class);
				lsp = new Lsp();
				lsp.setTunnelId(tunnel.getTunnelId());
				tunnel.setLspParticularList(lspInfoMapper.queryByCondition(lsp));
			}

		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			lspInfoMapper = null;
			lsp = null;
		}
		return tunnelList;
	}

	/**
	 * 获取tunnel下的所有网元ID
	 * 
	 * @param tunnel
	 * @return siteid集合
	 * @throws Exception
	 */
	public List<Integer> getSiteId(Tunnel tunnel) throws Exception {
		List<Integer> siteIdList = null;
		try {
			siteIdList = new ArrayList<Integer>();
			if (tunnel.getLspParticularList() != null && tunnel.getLspParticularList().size() > 0) {
				for (Lsp lsp : tunnel.getLspParticularList()) {
					if (!siteIdList.contains(lsp.getASiteId()) && lsp.getASiteId() != 0) {
						siteIdList.add(lsp.getASiteId());
					}
					if (!siteIdList.contains(lsp.getZSiteId()) && lsp.getZSiteId() != 0) {
						siteIdList.add(lsp.getZSiteId());
					}
				}
			}

		} catch (Exception e) {
			throw e;
		}
		return siteIdList;
	}

	public void update(Tunnel tunnel) throws Exception {
		// ProtectInfoService pInfoService = null;
		List<Lsp> lspparticularlist = null;
		OamInfoService_MB oamInfoService_MB = null;
		LabelInfoService_MB labelInfoService_MB = null;
		SiteService_MB siteService_MB = null;
		BusinessidService_MB businessidService_MB = null;
		Businessid abusinessid = null;
		Businessid zbusinessid = null;
		BusinessidMapper businessidmapper = null;
		Businessid businessId = null;
		try {
			this.sqlSession.getConnection().setAutoCommit(true);
			oamInfoService_MB = (OamInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.OamInfo, this.sqlSession);
			labelInfoService_MB = (LabelInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.LABELINFO, this.sqlSession);
			siteService_MB = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE, this.sqlSession);
			businessidService_MB = (BusinessidService_MB) ConstantUtil.serviceFactory.newService_MB(Services.BUSINESSID, this.sqlSession);
			businessidmapper = sqlSession.getMapper(BusinessidMapper.class);
			// 如果是普通的类型，但又有保护tunnel，则得删掉保护tunnel
			if (tunnel.getTunnelType().equals("185") && null != tunnel.getProtectTunnel()) {
				this.deleteProtect(tunnel);
				tunnel.setProtectTunnelId(0);
				tunnel.setProtectTunnel(null);
			}

			if (null != tunnel.getProtectTunnel()) {
				// 此处判断保护路由是否已经在数据库中，如没有则插入，如有则更新
				String protectName = tunnel.getTunnelName() + "_protect";
				Tunnel tunnel2 = this.selectByID(tunnel.getProtectTunnelId());
				if (tunnel2 != null && tunnel2.getTunnelId() > 0) {
					tunnel.getProtectTunnel().setTunnelStatus(tunnel.getTunnelStatus());
					// 生成保护id(武汉)

					if (tunnel.getProtectTunnel().getASiteId() != 0 && siteService_MB.getManufacturer(tunnel.getProtectTunnel().getASiteId()) == EManufacturer.WUHAN.getValue() && tunnel.getProtectTunnel().getAprotectId() == 0) {
						abusinessid = businessidService_MB.select(tunnel.getProtectTunnel().getASiteId(), "lspprotect");
						if (abusinessid == null) {
							throw new BusinessIdException(siteService_MB.getSiteName(tunnel.getProtectTunnel().getASiteId()) + ResourceUtil.srcStr(StringKeysTip.TIP_TUNNEL_PROTECT_ID));
						}
						businessidService_MB.update(abusinessid.getId(), 1);// 将该保护id设置为不可用
						tunnel.getProtectTunnel().setAprotectId(abusinessid.getIdValue());
					}
					if (tunnel.getProtectTunnel().getZSiteId() != 0 && siteService_MB.getManufacturer(tunnel.getProtectTunnel().getZSiteId()) == EManufacturer.WUHAN.getValue() && tunnel.getProtectTunnel().getZprotectId() == 0) {
						zbusinessid = businessidService_MB.select(tunnel.getProtectTunnel().getZSiteId(), "lspprotect");
						if (zbusinessid == null) {
							throw new BusinessIdException(siteService_MB.getSiteName(tunnel.getProtectTunnel().getZSiteId()) + ResourceUtil.srcStr(StringKeysTip.TIP_TUNNEL_PROTECT_ID));
						}
						businessidService_MB.update(zbusinessid.getId(), 1);// 将该保护id设置为不可用
						tunnel.getProtectTunnel().setZprotectId(zbusinessid.getIdValue());
					}
					tunnelMapper.updateByPrimaryKey(tunnel.getProtectTunnel());
				} else {// 将普通类型tunnel改为1:1 保护tunnel
					tunnel.getProtectTunnel().setTunnelName(protectName);
					tunnel.getProtectTunnel().setTunnelStatus(tunnel.getTunnelStatus());
					int protectTunnelId = this.savaProtect(tunnel.getProtectTunnel());
					tunnel.setProtectTunnelId(protectTunnelId);
				}

			} else if (tunnel.getProtectTunnel() == null && tunnel.getTunnelType().equals("185")) {// 网络侧删除TNP时，释放保护id
				if (tunnel.getAprotectId() > 0 && tunnel.getZprotectId() > 0) {
					businessId = new Businessid();
					businessId.setIdStatus(0);
					businessId.setIdValue(tunnel.getAprotectId());
					businessId.setType("lspprotect");
					businessId.setSiteId(tunnel.getASiteId());
					businessidmapper.updateBusinessid(businessId);
					tunnel.setAprotectId(0);
					businessId.setIdStatus(0);
					businessId.setIdValue(tunnel.getZprotectId());
					businessId.setType("lspprotect");
					businessId.setSiteId(tunnel.getZSiteId());
					businessidmapper.updateBusinessid(businessId);
					tunnel.setZprotectId(0);
				}
			}

			tunnelMapper.updateByPrimaryKey(tunnel);

			// 更新lsp信息
			LspinfoMapper lspParticularmapper = sqlSession.getMapper(LspinfoMapper.class);
			// 先释放之前的标签，再更新工作路径，更新标签
			// 释放标签
			lspparticularlist = lspParticularmapper.queryByTunnnelId(tunnel.getTunnelId());
			for (Lsp obj : lspparticularlist) {
				if (tunnel.getIsSingle() == 0) { // 修改标签状态，如果是网络配置，修改a z端否则只修改一端
					// labelInfoDao.updateStatusBatch(obj.getFrontLabelValue(),
					// obj.getASiteId(), 1, connection);
					labelInfoService_MB.updateBatch(obj.getBackLabelValue(), obj.getASiteId(), 1, "WH");
					labelInfoService_MB.updateBatch(obj.getFrontLabelValue(), obj.getZSiteId(), 1, "WH");
					// labelInfoDao.updateStatusBatch(obj.getBackLabelValue(),
					// obj.getZSiteId(), 1, connection);
				} else {
					if (obj.getASiteId() > 0) {
						labelInfoService_MB.updateBatch(obj.getBackLabelValue(), obj.getASiteId(), 1, "WH");
					}
					if (obj.getZSiteId() > 0) {
						labelInfoService_MB.updateBatch(obj.getFrontLabelValue(), obj.getZSiteId(), 1, "WH");
					}
				}
			}
			// 更新标签
			lspparticularlist = tunnel.getLspParticularList();
			for (Lsp obj : lspparticularlist) {
				// 将labelInfo中没有的数据插入labelInfo䶿
				if (tunnel.getIsSingle() == 0) {
					// labelInfoDao.insertNewLabel(obj.getFrontLabelValue(),
					// obj.getASiteId(), connection);
					labelInfoService_MB.insertNewLabel(obj.getFrontLabelValue(), obj.getZSiteId(), "WH");
					labelInfoService_MB.insertNewLabel(obj.getBackLabelValue(), obj.getASiteId(), "WH");
					// labelInfoDao.insertNewLabel(obj.getBackLabelValue(),
					// obj.getZSiteId(), connection);
					// labelInfoDao.updateStatusBatch(obj.getFrontLabelValue(),
					// obj.getASiteId(), 0, connection);
					labelInfoService_MB.updateBatch(obj.getFrontLabelValue(), obj.getZSiteId(), 0, "WH");
					labelInfoService_MB.updateBatch(obj.getBackLabelValue(), obj.getASiteId(), 0, "WH");
					// labelInfoDao.updateStatusBatch(obj.getBackLabelValue(),
					// obj.getZSiteId(), 0, connection);
				} else {
					if (obj.getASiteId() > 0) {
						labelInfoService_MB.insertNewLabel(obj.getBackLabelValue(), obj.getASiteId(), "WH");
						labelInfoService_MB.updateBatch(obj.getBackLabelValue(), obj.getASiteId(), 0, "WH");
					}
					if (obj.getZSiteId() > 0) {
						labelInfoService_MB.insertNewLabel(obj.getFrontLabelValue(), obj.getZSiteId(), "WH");
						labelInfoService_MB.updateBatch(obj.getFrontLabelValue(), obj.getZSiteId(), 0, "WH");
					}
				}
			}
			// 更新lsp
			for (int j = 0; j < lspparticularlist.size(); j++) {
				lspParticularmapper.updateByPrimaryKey(lspparticularlist.get(j));
			}
			// 先释放之前的标签，再更新保护路径，更新标竿
			if (tunnel.getProtectTunnelId() > 0) {
				if (tunnel.getProtectTunnel() != null) {
					// 释放标签
					lspparticularlist = lspParticularmapper.queryByTunnnelId(tunnel.getProtectTunnelId());
					for (Lsp obj : lspparticularlist) {
						if (tunnel.getIsSingle() == 0) { // 修改标签状态，如果是网络配置，修改a
															// z穿否则只修改一穿
							labelInfoService_MB.updateBatch(obj.getFrontLabelValue(), obj.getZSiteId(), 1, "WH");
							// labelInfoDao.updateStatusBatch(obj.getFrontLabelValue(),
							// obj.getZSiteId(), 1, connection);
							// labelInfoDao.updateStatusBatch(obj.getBackLabelValue(),
							// obj.getASiteId(), 1, connection);
							labelInfoService_MB.updateBatch(obj.getBackLabelValue(), obj.getASiteId(), 1, "WH");
						} else {
							if (obj.getASiteId() > 0) {
								labelInfoService_MB.updateBatch(obj.getBackLabelValue(), obj.getASiteId(), 1, "WH");
							}
							if (obj.getZSiteId() > 0) {
								labelInfoService_MB.updateBatch(obj.getFrontLabelValue(), obj.getZSiteId(), 1, "WH");
							}
						}
					}
					// 更新标签
					lspparticularlist = tunnel.getProtectTunnel().getLspParticularList();
					for (Lsp obj : lspparticularlist) {
						// 将labelInfo中没有的数据插入labelInfo䶿
						if (tunnel.getIsSingle() == 0) {
							labelInfoService_MB.insertNewLabel(obj.getFrontLabelValue(), obj.getZSiteId(), "WH");
							// labelInfoDao.insertNewLabel(obj.getFrontLabelValue(),
							// obj.getZSiteId(), connection);
							// labelInfoDao.insertNewLabel(obj.getBackLabelValue(),
							// obj.getASiteId(), connection);
							labelInfoService_MB.insertNewLabel(obj.getBackLabelValue(), obj.getASiteId(), "WH");
							labelInfoService_MB.updateBatch(obj.getFrontLabelValue(), obj.getZSiteId(), 0, "WH");
							// labelInfoDao.updateStatusBatch(obj.getFrontLabelValue(),
							// obj.getZSiteId(), 0, connection);
							// labelInfoDao.updateStatusBatch(obj.getBackLabelValue(),
							// obj.getASiteId(), 0, connection);
							labelInfoService_MB.updateBatch(obj.getBackLabelValue(), obj.getASiteId(), 0, "WH");
						} else {
							if (obj.getASiteId() > 0) {
								labelInfoService_MB.insertNewLabel(obj.getBackLabelValue(), obj.getASiteId(), "WH");
								labelInfoService_MB.updateBatch(obj.getBackLabelValue(), obj.getASiteId(), 0, "WH");
							}
							if (obj.getZSiteId() > 0) {
								labelInfoService_MB.insertNewLabel(obj.getFrontLabelValue(), obj.getZSiteId(), "WH");
								labelInfoService_MB.updateBatch(obj.getFrontLabelValue(), obj.getZSiteId(), 0, "WH");
							}
						}
					}
					for (int j = 0; j < lspparticularlist.size(); j++) {
						lspParticularmapper.updateByPrimaryKey(lspparticularlist.get(j));
					}
				}
			}
			// List<ProtectionInfo> pList = tunnel.getProList();
			// if (pList != null && pList.size() > 0) {
			// pInfoService.saveOrUpdate(tunnel.getProList());
			// }
			for (OamInfo oamInfo : tunnel.getOamList()) {
				if (oamInfo.getOamType() == OamTypeEnum.AMEP) {
					oamInfo.getOamMep().setServiceId(tunnel.getTunnelId());
					oamInfo.getOamMep().setObjId(tunnel.getLspParticularList().get(0).getAtunnelbusinessid());
					oamInfo.setOamType(OamTypeEnum.AMEP);
				} else if (oamInfo.getOamType() == OamTypeEnum.ZMEP) {
					oamInfo.getOamMep().setServiceId(tunnel.getTunnelId());
					oamInfo.getOamMep().setObjId(tunnel.getLspParticularList().get(tunnel.getLspParticularList().size() - 1).getZtunnelbusinessid());
					oamInfo.setOamType(OamTypeEnum.ZMEP);
				} else if (oamInfo.getOamType() == OamTypeEnum.MIP) {
					oamInfo.getOamMip().setServiceId(tunnel.getTunnelId());
					oamInfo.getOamMip().setObjId(tunnel.getLspParticularList().get(0).getZtunnelbusinessid());
					oamInfo.setOamType(OamTypeEnum.MIP);
				}
				oamInfoService_MB.saveOrUpdate(oamInfo);
			}
			if (tunnel.getProtectTunnelId() > 0 && tunnel.getProtectTunnel() != null) {
				if (tunnel.getProtectTunnel().getOamList().size() > 0) {
					tunnel = tunnel.getProtectTunnel();
					for (OamInfo oamInfo : tunnel.getOamList()) {
						if (oamInfo.getOamType() == OamTypeEnum.AMEP) {
							oamInfo.getOamMep().setServiceId(tunnel.getTunnelId());
							oamInfo.getOamMep().setObjId(tunnel.getLspParticularList().get(0).getAtunnelbusinessid());
							oamInfo.setOamType(OamTypeEnum.AMEP);
						} else if (oamInfo.getOamType() == OamTypeEnum.ZMEP) {
							oamInfo.getOamMep().setServiceId(tunnel.getTunnelId());
							oamInfo.getOamMep().setObjId(tunnel.getLspParticularList().get(tunnel.getLspParticularList().size() - 1).getZtunnelbusinessid());
							oamInfo.setOamType(OamTypeEnum.ZMEP);
						} else if (oamInfo.getOamType() == OamTypeEnum.MIP) {
							oamInfo.getOamMip().setServiceId(tunnel.getTunnelId());
							oamInfo.getOamMip().setObjId(tunnel.getLspParticularList().get(0).getZtunnelbusinessid());
							oamInfo.setOamType(OamTypeEnum.MIP);
						}
						oamInfoService_MB.saveOrUpdate(oamInfo);
					}
				}
			}

			// 离线网元数据下载
			if (0 != tunnel.getASiteId()) {
				super.dateDownLoad(tunnel.getASiteId(), tunnel.getTunnelId(), EServiceType.TUNNEL.getValue(), EActionType.UPDATE.getValue());
			}
			if (0 != tunnel.getZSiteId()) {
				super.dateDownLoad(tunnel.getZSiteId(), tunnel.getTunnelId(), EServiceType.TUNNEL.getValue(), EActionType.UPDATE.getValue());
			}
			this.sqlSession.getConnection().setAutoCommit(false);
		} catch (Exception e) {
			sqlSession.rollback();
			ExceptionManage.dispose(e, this.getClass());
		} finally {
		}
	}

	/**
	 * 
	 * 删除保护tunnel
	 * 
	 * @author kk
	 * 
	 * @param
	 * 
	 * @return
	 * @throws Exception
	 * 
	 * @Exception 异常对象
	 */
	private void deleteProtect(Tunnel tunnel) throws Exception {
		LspinfoMapper lspParticularmapper = null;
		List<Lsp> lspPList = null;
		BusinessidMapper businessidmapper = null;
		Businessid businessId = null;
		OamInfoService_MB oamInfoService_MB = null;
		OamInfo oamInfo = null;
		OamMepInfo oamMepInfo = null;
		OamMipInfo oamMipInfo = null;
		QosRelevanceMapper qosRelevancemapper = null;
		QosRelevance qosRelevance = null;
		// ProtectRorateInfoDao ProtectRorateInfoDao = null;
		// ProtectRorateInfo protectRorateInfo = null;
		SiteRoateMapper siteRoatemapper = null;
		SiteRoate siteRoate = null;
		LabelInfoService_MB labelInfoService_MB = null;
		QosInfoMapper qosInfomapper = null;
		try {
			// //删除相应保护倒换数据
			// ProtectRorateInfoDao = new ProtectRorateInfoDao();
			// protectRorateInfo = new ProtectRorateInfo();
			// protectRorateInfo.setTunnelId(tunnel.getTunnelId());
			// ProtectRorateInfoDao.delete(protectRorateInfo, connection);

			lspParticularmapper = sqlSession.getMapper(LspinfoMapper.class);
			oamInfoService_MB = (OamInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.OamInfo, this.sqlSession);
			labelInfoService_MB = (LabelInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.LABELINFO, this.sqlSession);
			businessidmapper = sqlSession.getMapper(BusinessidMapper.class);

			oamInfo = new OamInfo();
			oamMepInfo = new OamMepInfo();
			oamMepInfo.setServiceId(tunnel.getProtectTunnelId());
			oamMepInfo.setObjType("TUNNEL");
			oamInfo.setOamMep(oamMepInfo);

			oamMipInfo = new OamMipInfo();
			oamMipInfo.setServiceId(tunnel.getProtectTunnelId());
			oamMipInfo.setObjType("TUNNEL");
			oamInfo.setOamMip(oamMipInfo);
			oamInfoService_MB.delete(oamInfo);

			// 删除之前先判断该条QoS是否被其他tunnel使用，如果被其他tunnel使用，则不删除，否则删除
			// 先删除qosInfo,再删除qosRelevance
			qosInfomapper = sqlSession.getMapper(QosInfoMapper.class);
			qosRelevancemapper = sqlSession.getMapper(QosRelevanceMapper.class);
			List<QosRelevance> qosRelavanceList = this.getQosRelevanceList(tunnel.getProtectTunnel(), qosRelevancemapper);
			this.checkIsOccupy(tunnel.getProtectTunnel(), qosRelavanceList, qosRelevancemapper);
			if (qosRelavanceList != null && !qosRelavanceList.isEmpty()) {
				for (QosRelevance relevance : qosRelavanceList) {
					if (!relevance.isRepeat()) {
						qosInfomapper.deleteByGroupId(relevance.getQosGroupId());
					}
				}
			}
			qosRelevance = new QosRelevance();
			qosRelevance.setObjType(EServiceType.TUNNEL.toString());
			qosRelevance.setObjId(tunnel.getProtectTunnelId());
			qosRelevancemapper.deleteByCondition(qosRelevance);

			businessId = new Businessid();
			lspPList = lspParticularmapper.queryByTunnnelId(tunnel.getProtectTunnelId());
			for (Lsp obj : lspPList) {

				if (tunnel.getIsSingle() == 0) { // 修改标签状态，如果是网络配置，修改a z端否则只修改一端
					labelInfoService_MB.updateBatch(obj.getBackLabelValue(), obj.getASiteId(), 1, "WH");
					// labelInfoService.updateBatch(obj.getFrontLabelValue(),
					// obj.getZSiteId(), 1, "TUNNEL");
					// labelInfoService.updateBatch(obj.getBackLabelValue(),
					// obj.getASiteId(), 1, "TUNNEL");
					labelInfoService_MB.updateBatch(obj.getFrontLabelValue(), obj.getZSiteId(), 1, "WH");
				} else {
					if (obj.getASiteId() == 0) {
						labelInfoService_MB.updateBatch(obj.getFrontLabelValue(), obj.getZSiteId(), 1, "WH");
					}
					if (obj.getZSiteId() == 0) {
						labelInfoService_MB.updateBatch(obj.getBackLabelValue(), obj.getASiteId(), 1, "WH");
					}
				}

				businessId.setIdStatus(0);
				businessId.setIdValue(obj.getAtunnelbusinessid());
				businessId.setType("tunnel");
				businessId.setSiteId(obj.getASiteId());
				businessidmapper.updateBusinessid(businessId);
				businessId.setIdValue(obj.getZtunnelbusinessid());
				businessId.setSiteId(obj.getZSiteId());
				businessidmapper.updateBusinessid(businessId);

			}
			businessId.setIdStatus(0);
			businessId.setIdValue(tunnel.getProtectTunnel().getAprotectId());
			businessId.setType("lspprotect");
			businessId.setSiteId(tunnel.getProtectTunnel().getASiteId());
			businessidmapper.updateBusinessid(businessId);
			businessId.setIdStatus(0);
			businessId.setIdValue(tunnel.getProtectTunnel().getZprotectId());
			businessId.setType("lspprotect");
			businessId.setSiteId(tunnel.getProtectTunnel().getZSiteId());
			businessidmapper.updateBusinessid(businessId);
			lspParticularmapper.deleteByTunnelID(tunnel.getProtectTunnelId());
			tunnelMapper.deleteByPrimaryKey(tunnel.getProtectTunnelId());
			/**
			 * 删除 保护tunnel 时 删除 倒换信息
			 */
			siteRoate = new SiteRoate();
			siteRoatemapper = sqlSession.getMapper(SiteRoateMapper.class);
			siteRoate.setTypeId(tunnel.getProtectTunnelId());
			siteRoate.setType("tunnel");
			if (tunnel.getASiteId() > 0) {
				siteRoate.setSiteId(tunnel.getASiteId());
				siteRoatemapper.delete(siteRoate);
			}
			if (tunnel.getZSiteId() > 0) {
				siteRoate.setSiteId(tunnel.getZSiteId());
				siteRoatemapper.delete(siteRoate);
			}

		} catch (Exception e) {
			throw e;
		} finally {
		}

	}

	/**
	 * 给tunnel集合的lsp赋值
	 * 
	 * @throws Exception
	 */
	private void setTunnelLsp(List<Tunnel> tunnelList, boolean isNode) throws Exception {
		Lsp lspParticular = null;
		LspinfoMapper lspParticularMapper = sqlSession.getMapper(LspinfoMapper.class);
		List<Lsp> lspparticularList = null;
		List<Lsp> lspparticularList1 = null;
		try {
			if (null != tunnelList && tunnelList.size() != 0) {
				for (int i = 0; i < tunnelList.size(); i++) {
					if(tunnelList.get(i).getProtectTunnelId()!=0){
						Tunnel tunnel= new Tunnel();
						tunnel.setProtectTunnelId(tunnelList.get(i).getProtectTunnelId());
						tunnel.setTunnelType("0");
						tunnel.setIsSingle(0);
						List<Tunnel> protectTunnelList =null;
						protectTunnelList=this.tunnelMapper.queryProtectTunnel(tunnel);
						for (Tunnel tunnel1 : protectTunnelList) {
							tunnel1.setNode(true);
							Lsp lsp1 = new Lsp();
							lsp1.setTunnelId(tunnel1.getTunnelId());
							lspparticularList1 = new ArrayList<Lsp>();
							lspparticularList1=lspParticularMapper.queryByCondition(lsp1);
							tunnel1.setLspParticularList(lspparticularList1);
						}
						this.setOtherInfomationforTunnel(protectTunnelList);					
						if(protectTunnelList!=null && protectTunnelList.size()>0){
						   tunnelList.get(i).setProtectTunnel(protectTunnelList.get(0));						   
						}
					}
					tunnelList.get(i).setNode(isNode);
					lspParticular = new Lsp();
					lspParticular.setTunnelId(tunnelList.get(i).getTunnelId());
					lspparticularList= new ArrayList<Lsp>();
					lspparticularList = lspParticularMapper.queryByCondition(lspParticular);
					tunnelList.get(i).setLspParticularList(lspparticularList);
				}
				setOtherInfomationforTunnel(tunnelList);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			lspParticular = null;
			lspparticularList = null;
		}
	}

	
	
	private List<QosRelevance> getQosRelevanceList(Tunnel tunnel, QosRelevanceMapper qosRelevancemapper) throws Exception {
		QosRelevance qosRelevance = new QosRelevance();
		qosRelevance.setObjId(tunnel.getTunnelId());
		qosRelevance.setObjType(EServiceType.TUNNEL.toString());
		return qosRelevancemapper.queryByCondition(qosRelevance);
	}

	/**
	 * 判断tunnel引用的该条QoS是否被其他tunnel使用
	 * 
	 * @param qosRelavanceList
	 */
	private void checkIsOccupy(Tunnel tunnel, List<QosRelevance> qosRelevanceList, QosRelevanceMapper qosRelevancemapper) {
		try {
			if (qosRelevanceList != null && !qosRelevanceList.isEmpty()) {
				QosRelevance qosRelevance = new QosRelevance();
				for (QosRelevance relevance : qosRelevanceList) {
					qosRelevance.setSiteId(relevance.getSiteId());
					qosRelevance.setObjId(0);
					qosRelevance.setObjType(EServiceType.TUNNEL.toString());
					qosRelevance.setQosGroupId(relevance.getQosGroupId());
					List<QosRelevance> qosList = qosRelevancemapper.queryByCondition(qosRelevance);
					if (qosList != null && qosList.size() > 1) {
						relevance.setRepeat(true);
					} else {
						relevance.setRepeat(false);
					}
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
	}

	/**
	 * 查询全部 网络侧数据
	 * 
	 * @return List<Tunnel>集合
	 * @throws Exception
	 */
	public List<Tunnel> select() {

		List<Tunnel> tunnelList = null;
		Tunnel tunnel = null;
		try {
			tunnel = new Tunnel();
			tunnelList = tunnelMapper.queryByCondition(tunnel);
			this.setTunnelLsp(tunnelList, false);
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			tunnel = null;
		}
		return tunnelList;
	}

	/**
	 * 通过条件查询
	 * 
	 * @return List<Tunnel>集合
	 * @throws Exception
	 */
	public List<Tunnel> select(Tunnel tunnel) {

		List<Tunnel> tunnelList = null;
		try {
			tunnelList = tunnelMapper.queryByCondition(tunnel);
			this.setTunnelLsp(tunnelList, false);
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			tunnel = null;
		}
		return tunnelList;
	}

	public List<Integer> getPortIdsBySiteId(int siteId) throws Exception {

		List<Integer> portIds = new ArrayList<Integer>();
		List<Tunnel> tunnelList = null;
		Tunnel tunnelSelect = null;
		try {
			tunnelSelect = new Tunnel();
			tunnelList = this.select_nojoin(tunnelSelect);

			for (Tunnel tunnel : tunnelList) {
				if (tunnel.getASiteId() == siteId && !portIds.contains(tunnel.getAPortId()))
					portIds.add(tunnel.getAPortId());
				else if (tunnel.getZSiteId() == siteId && !portIds.contains(tunnel.getZPortId()))
					portIds.add(tunnel.getZPortId());
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			tunnelList = null;
			tunnelSelect = null;
		}
		return portIds;
	}

	/**
	 * 同步时修改， 修改激活状态、工作状徿
	 * 
	 * @author kk
	 * 
	 * @param tunnel
	 *            数据库对诿
	 * 
	 * @return
	 * @throws Exception
	 * @throw NumberFormatException
	 * 
	 * @Exception 异常对象
	 */
	public void update_synchro(Tunnel tunnel) throws NumberFormatException, Exception {
		QosRelevanceService_MB qosRelevanceServiceMB = null;
		try {
			this.tunnelMapper.updateStatus(tunnel);

			qosRelevanceServiceMB = (QosRelevanceService_MB) ConstantUtil.serviceFactory.newService_MB(Services.QOSRELEVANCE, this.sqlSession);
			if (null != tunnel.getQosList() && tunnel.getQosList().size() > 0) {
				qosRelevanceServiceMB.synchro(tunnel.getTunnelId(), tunnel.getQosList().get(0), EServiceType.TUNNEL.toString());
			}
			sqlSession.commit();
		} catch (Exception e) {
			throw e;
		} finally {
			// UiUtil.closeService(qosRelevanceService);
		}
	}

	/**
	 * 验证名字是否重复
	 * 
	 * @author kk
	 * 
	 * @param afterName
	 *            修改之后的名?
	 * @param beforeName
	 *            修改之前的名?
	 * 
	 * @return
	 * @throws Exception
	 * 
	 * @Exception 异常对象
	 */
	public boolean nameRepetition(String afterName, String beforeName, int siteId) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("afterName", afterName);
		map.put("beforeName", beforeName);
		map.put("siteId", siteId);
		int result = this.tunnelMapper.nameRepetition(map);
		if (0 == result) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 通过业务id和网元id查询
	 * 
	 * @param siteId
	 * @param serviceId
	 * @return
	 * @throws Exception
	 */

	public Tunnel selectBySiteIdAndServiceId(int siteId, int serviceId) throws Exception {

		LspinfoMapper lspinfoMapper = null;
		Tunnel tunnel = null;
		Lsp lsp = null;
		try {
			tunnel = this.tunnelMapper.queryBySiteIdAndServiceId(siteId, serviceId);
			if (tunnel != null) {
				lspinfoMapper = sqlSession.getMapper(LspinfoMapper.class);
				lsp = new Lsp();
				lsp.setTunnelId(tunnel.getTunnelId());
				tunnel.setLspParticularList(lspinfoMapper.queryByCondition(lsp));
			}

		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			lsp = null;
		}
		return tunnel;
	}

	/**
	 * 查询数据库中条件等于参数tunnel的tunnel集合。（eline快速配置业务时，查询是否存在可用tunnel时用ﺿ
	 * 
	 * @param tunnel
	 *            页面传递的tunnel对象
	 * @return tunnel 对象
	 * @throws Exception
	 */
	public Tunnel selectExistTunnel(Tunnel tunnel) throws Exception {
		List<Tunnel> tunnelList = null;
		Tunnel tunneResult = null;
		LspInfoService_MB lspServiceMB = null;
		QosInfoService_MB qosInfoService = null;
		List<Tunnel> tunnelListResult = null;
		try {
			lspServiceMB = (LspInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.LSPINFO, this.sqlSession);
			qosInfoService = (QosInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.QosInfo, this.sqlSession);
			tunnelList = this.tunnelMapper.queryExistTunnel(tunnel);
			if (null != tunnelList && tunnelList.size() > 0) {

				for (Tunnel tunnelSelect : tunnelList) {
					// 如果lsp比较路径相同。就接着比较qos
					if (lspServiceMB.compareLsp(tunnelSelect.getTunnelId(), tunnel.getLspParticularList())) {
						// 如果tunnel斿:1保护 比较保护的路弿
						if ("2".equals(UiUtil.getCodeById(Integer.parseInt(tunnelSelect.getTunnelType())).getCodeValue())) {
							if (!lspServiceMB.compareLsp(tunnelSelect.getTunnelId(), tunnel.getLspParticularList())) {
								continue;
							}
						}

						// 比较qos 如果返回的是成功 结束循环 直接把此tunnel对象返回到界面中
						if (qosInfoService.compareQos(tunnelSelect.getTunnelId(), EServiceType.TUNNEL, tunnel.getQosList())) {
							tunneResult = tunnelSelect;
							break;
						}

					}
				}
			}

			// 如果有返回的tunnel 就把tunnel下的lsp qos oam 补全
			if (null != tunneResult) {
				tunneResult.setLspParticularList(lspServiceMB.selectBytunnelId(tunneResult.getTunnelId()));
				tunnelListResult = new ArrayList<Tunnel>();
				tunnelListResult.add(tunneResult);
				this.setOtherInfomationforTunnel(tunnelListResult);
				tunneResult = tunnelListResult.get(0);
			}

		} catch (Exception e) {
			throw e;
		} finally {
		}
		return tunneResult;
	}

	/**
	 * 从tunnel对象中获取全部的lsp集合，如果有保护的lsp也返回
	 * 
	 * @param tunnel
	 * @return
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	public List<Lsp> getAllLsp(Tunnel tunnel) throws NumberFormatException, Exception {

		List<Lsp> lspList = null;
		lspList = new ArrayList<Lsp>();
		lspList.addAll(tunnel.getLspParticularList());
		// 如果是保护。把保护的lsp集合添加到集合中
		if ("2".equals(UiUtil.getCodeById(Integer.parseInt(tunnel.getTunnelType())).getCodeValue())) {
			lspList.addAll(tunnel.getProtectTunnel().getLspParticularList());
		}
		return lspList;
	}

	/**
	 * 获取可用businessId最小的数量
	 * 
	 * @param lspList
	 *            lsp集合
	 * @return
	 * @throws Exception
	 */
	public int getMinBusinessIdNum(List<Lsp> lspList) throws Exception {
		List<Integer> siteIdList = null;
		int minBusinessIdNum = 0;
		int businessIdNum = 0;
		BusinessidService_MB businessidServiceMB = null;
		try {
			businessidServiceMB = (BusinessidService_MB) ConstantUtil.serviceFactory.newService_MB(Services.BUSINESSID, this.sqlSession);

			// 获取lsp中所有去重复的网元ID集合
			siteIdList = new ArrayList<Integer>();
			for (Lsp lsp : lspList) {
				if (!siteIdList.contains(lsp.getASiteId())) {
					siteIdList.add(lsp.getASiteId());
				}
				if (!siteIdList.contains(lsp.getZSiteId())) {
					siteIdList.add(lsp.getZSiteId());
				}
			}

			// 遍历所有网元集合，查询businessid
			for (int i = 0; i < siteIdList.size(); i++) {
				businessIdNum = businessidServiceMB.getBusinessCount(siteIdList.get(i), "tunnel");
				if (i == 0) {
					minBusinessIdNum = businessIdNum;
				} else {
					if (businessIdNum < minBusinessIdNum) {
						minBusinessIdNum = businessIdNum;
					}
				}
			}

		} catch (Exception e) {
			throw e;
		} finally {
			// UiUtil.closeService(businessidService);
		}
		return minBusinessIdNum;
	}

	/**
	 * 通过lsp集合和已知的qos集合查询出最小的qos支持数量
	 * 
	 * @param lspList
	 *            lsp集合
	 * @param qosInfoList
	 *            qosInfo集合
	 * @return 返回-1 说明已知的qos带宽都为0.可以随便创建
	 * @throws Exception
	 */
	public int getMinQosNum(List<Lsp> lspList, List<QosInfo> qosInfoList, List<QosInfo> qosInfoList_before) throws Exception {
		int result = -1;
		int value = 0;
		try {

			for (Lsp lsp : lspList) {
				if (lsp.getAPortId() > 0) {
					value = this.getQosNum(lsp.getAPortId(), qosInfoList, "a", qosInfoList_before);
					if (value != -1) {
						if (result != -1) {
							if (result > value) {
								result = value;
							}
						} else {
							result = value;
						}
					}
				}

				if (lsp.getZPortId() > 0) {
					value = this.getQosNum(lsp.getZPortId(), qosInfoList, "z", qosInfoList_before);
					if (value != -1) {
						if (result != -1) {
							if (result > value) {
								result = value;
							}
						} else {
							result = value;
						}
					}
				}
			}

		} catch (Exception e) {
			throw e;
		}
		return result;
	}

	/**
	 * 根据端口计算出 端口QOS/qosInfoList的结果
	 * 
	 * @param portId
	 *            端口主键
	 * @param qosInfoList
	 *            已知的qos集合
	 * @param type
	 *            类型。a或z 用来验证比较前向还是后向
	 * @return 返回-1 说明已知的qos带宽都为0.可以随便创建
	 * @throws Exception
	 */
	private int getQosNum(int portId, List<QosInfo> qosInfoList, String type, List<QosInfo> qosInfo_before) throws Exception {
		Map<Integer, Integer> qosMap = null;
		PortService_MB portServiceMB = null;
		int result = -1;
		int value = 0;
		try {
			// 查询QOS剩余带宽。
			portServiceMB = (PortService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORT, this.sqlSession);
			qosMap = portServiceMB.getResidueQos(portId);
			for (QosInfo qosInfo : qosInfoList) {
				if (qosInfo.getCir() > 0) {
					// a端比较前向
					if ("a".equals(type)) {
						if (Integer.parseInt(qosInfo.getDirection()) == EQosDirection.FORWARD.getValue()) {
							value = (qosMap.get(qosInfo.getCos()) + this.getBeforeCir(qosInfo_before, qosInfo.getCos(), EQosDirection.FORWARD.getValue())) / qosInfo.getCir();
							if (result != -1) {
								if (result > value) {
									result = value;
								}
							} else {
								result = value;
							}
						}
					} else {
						// z端比较后向
						if (Integer.parseInt(qosInfo.getDirection()) == EQosDirection.BACKWARD.getValue()) {
							value = (qosMap.get(qosInfo.getCos()) + this.getBeforeCir(qosInfo_before, qosInfo.getCos(), EQosDirection.BACKWARD.getValue())) / qosInfo.getCir();
							if (result != -1) {
								if (result > value) {
									result = value;
								}
							} else {
								result = value;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			qosMap = null;
			// UiUtil.closeService(portService);
			value = 0;
		}
		return result;
	}

	/**
	 * 获取修改之前的CIR 如果参数为null 则返回0
	 * 
	 * @param qosInfoList
	 *            之前的qos集合
	 * @param cos
	 *            要匹配的cos
	 * @param direction
	 *            方向
	 * @return cir
	 * @throws Exception
	 */
	private int getBeforeCir(List<QosInfo> qosInfoList, int cos, int direction) throws Exception {
		int result = 0;
		try {
			if (null != qosInfoList && qosInfoList.size() > 0) {
				for (QosInfo qosInfo : qosInfoList) {
					if (Integer.parseInt(qosInfo.getDirection()) == direction && qosInfo.getCos() == cos) {
						result = qosInfo.getCir();
						break;
					}
				}
			}

		} catch (Exception e) {
			throw e;
		}
		return result;
	}

	/**
	 * 根据网元和保护Id查找主用tunnel，不关联
	 * 
	 * @param siteId
	 * @param protectId
	 * @return
	 * @throws Exception
	 */
	public Tunnel selectBySiteIdAndProtectId(int siteId, int protectId) throws Exception {

		LspinfoMapper lspinfoMapper = null;
		Tunnel tunnel = null;
		Lsp lsp = null;
		try {
			tunnel = this.tunnelMapper.queryBySiteIdAndProtectId(siteId, protectId);
			if (tunnel != null) {
				lspinfoMapper = sqlSession.getMapper(LspinfoMapper.class);
				lsp = new Lsp();
				lsp.setTunnelId(tunnel.getTunnelId());
				tunnel.setLspParticularList(lspinfoMapper.queryByCondition(lsp));
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			lsp = null;
		}
		return tunnel;
	}

	/**
	 * 根据tunnelA,Z过滤可用SNCP保护（武汉）
	 * 
	 * @return
	 */
	public List<Tunnel> selectSNCPtunnel(int asiteId, int zsiteId) {
		List<Tunnel> tunnels = null;
		LspinfoMapper lspinfoMapper = null;
		Lsp lsp = null;
		try {
			tunnels = this.tunnelMapper.querySNCPbySiteId(asiteId, zsiteId, "493");
			for (Tunnel tunnel : tunnels) {
				lspinfoMapper = sqlSession.getMapper(LspinfoMapper.class);
				lsp = new Lsp();
				lsp.setTunnelId(tunnel.getTunnelId());
				tunnel.setLspParticularList(lspinfoMapper.queryByCondition(lsp));
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			lsp = null;
		}
		return tunnels;
	}

	/**
	 * 根据site查询
	 * 
	 * @param siteId
	 *            网元ID
	 * @param connection
	 *            数据库连接
	 * @return
	 * @throws Exception
	 */
	public List<Tunnel> selectBySiteId(int siteId) throws Exception {
		List<Tunnel> tunnelList = null;
		try {
			tunnelList = this.tunnelMapper.selectBySiteId(siteId);

			this.setOtherInfomationforTunnel(tunnelList);
		} catch (Exception e) {
			throw e;
		}

		return tunnelList;
	}

	/**
	 * 查询同一端口下是否有多条tunnel
	 * 
	 * @param portId
	 * @param label
	 * @return
	 * @throws Exception
	 */
	public List<Integer> checkPortUsable(int portId) throws Exception {
		return this.tunnelMapper.checkPortUsable(portId);
	}

	/**
	 * 通过a、z端网元ID查询tunnel
	 * 
	 * @param aSiteId
	 *            a端网元ID
	 * @param zSiteId
	 *            z端网元ID
	 * @return
	 * @throws Exception
	 */
	public List<Tunnel> selectByASiteIdAndZSiteId(int aSiteId, int zSiteId) throws Exception {

		LspinfoMapper lspinfoMapper = null;
		List<Tunnel> tunnelList = null;
		Lsp lsp = null;
		try {
			tunnelList = this.tunnelMapper.selectByASiteIdAndZSiteId(aSiteId, zSiteId);
			for (Tunnel tunnel : tunnelList) {
				lspinfoMapper = sqlSession.getMapper(LspinfoMapper.class);
				lsp = new Lsp();
				lsp.setTunnelId(tunnel.getTunnelId());
				tunnel.setLspParticularList(lspinfoMapper.queryByCondition(lsp));
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			lsp = null;
		}
		return tunnelList;
	}

	/**
	 * 根据主键获取tunnel
	 * 
	 * @param tunnelId
	 * @return
	 * @throws Exception
	 */
	public Tunnel selectId(int tunnelId) throws Exception {
		Tunnel tunnel = null;
		List<Tunnel> tunnelList = null;
		LspinfoMapper lspinfoMapper = null;
		Lsp lsp = null;
		try {
			tunnel = tunnelMapper.selectByPrimaryKey(tunnelId);
			if (tunnel != null) {
				lspinfoMapper = sqlSession.getMapper(LspinfoMapper.class);
				lsp = new Lsp();
				lsp.setTunnelId(tunnel.getTunnelId());
				tunnel.setLspParticularList(lspinfoMapper.queryByCondition(lsp));
				tunnelList = new ArrayList<Tunnel>();
				tunnelList.add(tunnel);
				this.setOtherInfomationforTunnel(tunnelList);
			}

		} catch (Exception e) {
			throw e;
		}
		return tunnel;
	}

	/**
	 * 验证此tunnel的qos在pw中是否还有带宽 用tunnel的qos减去tunnel下的所有pw的qos
	 * 
	 * @param qosInfList
	 *            要验证的QOS集合
	 * @param tunnel
	 *            要验证的tunnel对象。
	 * @return true 有带宽 false 没有带宽
	 * @throws Exception
	 */
	public boolean checkingQosInPw(List<QosInfo> qosInfoList, Tunnel tunnel) throws Exception {

		if (null == qosInfoList || qosInfoList.size() == 0) {
			throw new Exception("qosInfoList is null");
		}

		if (null == tunnel) {
			throw new Exception("tunnel is null");
		}
		boolean flag = true;
		QosInfoService_MB qosInfoServiceMB = null;
		try {
			qosInfoServiceMB = (QosInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.QosInfo);

			// 遍历qos 计算每个qos的剩余带宽
			for (QosInfo qosInfo : qosInfoList) {
				QosInfo qosInfo_result = qosInfoServiceMB.calculateQos(qosInfo, EServiceType.TUNNEL, tunnel.getTunnelId());

				// 如果剩余带宽小于0 说明没有带宽了，直接返回。
				if (qosInfo_result.getCir() < 0 || qosInfo_result.getEir() < 0) {
					flag = false;
					break;
				}
			}

		} catch (Exception e) {
			throw e;
		} finally {
			// UiUtil.closeService(qosInfoService);
		}
		return flag;
	}

	/**
	 * 获取此tunnel下的所有网元ID
	 * 
	 * @param tunnel
	 *            tunnel对象
	 * @param isProtect
	 *            是否包含保护的网元
	 * @return siteID集合
	 * @throws Exception
	 */
	public List<Integer> getSiteIds(Tunnel tunnel, boolean isProtect) throws Exception {
		List<Integer> siteIds = null;
		Tunnel protectTunnel = null;
		SiteService_MB siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE, this.sqlSession);
		try {
			siteIds = new ArrayList<Integer>();
			if (tunnel.getIsSingle() == 1 && siteService.getManufacturer(ConstantUtil.siteId) == EManufacturer.WUHAN.getValue()) {
				siteIds.add(ConstantUtil.siteId);
			} else {
				if (isProtect) {// 包含保护所包括的网元
					if (tunnel.getProtectTunnelId() > 0) {
						protectTunnel = new Tunnel();
						protectTunnel.setTunnelId(tunnel.getProtectTunnelId());
						protectTunnel = this.select_nojoin(protectTunnel).get(0);
						for (Lsp lspParticular : protectTunnel.getLspParticularList()) {
							if (lspParticular.getASiteId() > 0) {
								if (!siteIds.contains(lspParticular.getASiteId()) && siteService.getManufacturer(lspParticular.getASiteId()) == EManufacturer.WUHAN.getValue()) {
									siteIds.add(lspParticular.getASiteId());
								}
							}
							if (lspParticular.getZSiteId() > 0) {
								if (!siteIds.contains(lspParticular.getZSiteId()) && lspParticular.getZPortId() > 0 && siteService.getManufacturer(lspParticular.getZSiteId()) == EManufacturer.WUHAN.getValue()) {
									siteIds.add(lspParticular.getZSiteId());
								}
							}
						}

					}
				}

				for (Lsp lspParticular : tunnel.getLspParticularList()) {
					if (lspParticular.getASiteId() > 0) {
						if (!siteIds.contains(lspParticular.getASiteId()) && siteService.getManufacturer(lspParticular.getASiteId()) == EManufacturer.WUHAN.getValue()) {
							siteIds.add(lspParticular.getASiteId());
						}
					}
					if (lspParticular.getZSiteId() > 0) {
						if (!siteIds.contains(lspParticular.getZSiteId()) && lspParticular.getZPortId() > 0 && siteService.getManufacturer(lspParticular.getZSiteId()) == EManufacturer.WUHAN.getValue()) {
							siteIds.add(lspParticular.getZSiteId());
						}
					}
				}
			}

		} catch (Exception e) {
			throw e;
		} finally {
			// UiUtil.closeService(siteService);
		}
		return siteIds;
	}

	/**
	 * 获取tunnel的名称 显示用
	 * 
	 * @param tunnelId
	 * @return
	 * @throws Exception
	 */
	public String getTunnelName(int tunnelId) throws Exception {

		Tunnel tunnel = null;
		String result = null;
		try {
			tunnel = tunnelMapper.selectByPrimaryKey(tunnelId);
			if (null != tunnel) {
				result = tunnel.getTunnelName();
			} else {
				result = "";
			}

		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			tunnel = null;
		}
		return result;
	}

	/**
	 * 根据site查询
	 * 
	 * @param siteId
	 *            网元ID
	 * @param connection
	 *            数据库连接
	 * @return
	 * @throws Exception
	 */
	public List<Tunnel> queryTunnelBySiteId(int siteId) throws Exception {
		List<Tunnel> tunnels = null;
		try {
			tunnels = this.tunnelMapper.queryTunnelBySiteId(siteId);
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return tunnels;
	}

	/**
	 * 单网元列表过滤查询
	 * 
	 * @param tunnel
	 *            查询条件
	 * @param connection
	 *            数据库连接
	 * @return
	 * @throws Exception
	 */
	public List<Tunnel> filterSelectNE(int siteId, Tunnel tunnel) throws Exception {
		List<Tunnel> tunnelList = null;
		try {
			tunnelList = this.tunnelMapper.filterSelectNE(siteId, tunnel);
			this.setTunnelLsp(tunnelList, true);
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
		}
		return tunnelList;
	}

	/**
	 * 武汉搜索
	 * 
	 * @param siteID
	 * @return
	 */
	public List<Tunnel> searchWh(int siteID) {
		List<Tunnel> tunnels = null;
		SegmentService_MB segmentService = null;
		List<Tunnel> needTunnels = null;
		List<Segment> segments = null;
		try {
			tunnels = this.tunnelMapper.searchWh(siteID);
			setTunnelLsp(tunnels, true);
			segmentService = (SegmentService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SEGMENT, sqlSession);
			needTunnels = new ArrayList<Tunnel>();
			for (Tunnel tunnel : tunnels) {
				for (Lsp lsp : tunnel.getLspParticularList()) {
					Segment segment = new Segment();
					if (lsp.getASiteId() != 0 && lsp.getAPortId() != 0) {
						segment.setASITEID(lsp.getASiteId());
						segment.setAPORTID(lsp.getAPortId());
						segments = segmentService.selectBySiteIdAndPort(segment);
						if (segments.size() == 0) {
							break;
						}
					} else if (lsp.getZSiteId() != 0 && lsp.getZPortId() != 0) {
						segment.setASITEID(lsp.getZSiteId());
						segment.setAPORTID(lsp.getZPortId());
						segments = segmentService.selectBySiteIdAndPort(segment);
						if (segments.size() == 0) {
							break;
						}
					}
				}
				needTunnels.add(tunnel);
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return needTunnels;
	}

	public void doSearchWh(List<Tunnel> tunnels) {
		// 旧的tunnelId
		String Tid = new String("(");
		for (Tunnel tunnel : tunnels) {
			Tid = Tid + tunnel.getTunnelId() + ",";
		}
		Tid = Tid.substring(0, Tid.length() - 1) + ")";
		int newTunnelId = 0;// sql 得到
		String newTunnelName = "tunnel_" + System.currentTimeMillis();
		PwInfoMapper pwInfoMapper = null;
		MsPwInfoMapper msPwInfoMapper = null;
		QosRelevanceMapper qosRelevanceMapper = null;
		OamMepInfoMapper oamMepInfoMapper = null;
		OamMipInfoMapper oamMipInfoMapper = null;
		CurrentAlarmInfoMapper currentAlarmInfoMapper = null;
		HisAlarmInfoMapper hisAlarmInfoMapper = null;
		HisPerformanceInfoMapper hisPerformanceInfoMapper = null;
		try {
			if (tunnels.size() == 2) {
				// 1 插入新tunnel 表 数据
				newTunnelId = tunnelMapper.insert_searchWH(tunnels.get(0).getTunnelId(), tunnels.get(1).getTunnelId(), newTunnelName);

				List<Lsp> lsps = new ArrayList<Lsp>();
				lsps.add(tunnels.get(0).getLspParticularList().get(0));
				lsps.add(tunnels.get(1).getLspParticularList().get(0));
				lspWhSearchdo(newTunnelId, lsps);
			} else {
				Tunnel startTunnel = tunnels.get(0);
				Tunnel endTunnel = tunnels.get(tunnels.size() - 1);
				newTunnelId = tunnelMapper.insert_searchWH(startTunnel.getTunnelId(), endTunnel.getTunnelId(), newTunnelName);
				// 插入新的lsp数据
				for (int i = 0; i < tunnels.size(); i++) {
					List<Lsp> lsps = new ArrayList<Lsp>();
					if (i == 0) {
						lsps.add(tunnels.get(0).getLspParticularList().get(0));
						lsps.add(tunnels.get(1).getLspParticularList().get(0));
						lspWhSearchdo(newTunnelId, lsps);
					} else if (i != tunnels.size() - 1) {
						lsps.add(tunnels.get(i).getLspParticularList().get(1));
						lsps.add(tunnels.get(i + 1).getLspParticularList().get(0));
						lspWhSearchdo(newTunnelId, lsps);
					}

				}
			}
			// 更新 pw表 ，把老的tunnel关联更新到新的tunnulid上
			pwInfoMapper = sqlSession.getMapper(PwInfoMapper.class);
			pwInfoMapper.updateTunnelID(newTunnelId,Tid);
			
			// 更新mspw表
			msPwInfoMapper = sqlSession.getMapper(MsPwInfoMapper.class);
			msPwInfoMapper.updateFrontTunnelId(newTunnelId,Tid);
			msPwInfoMapper.updateBackTunnelId(newTunnelId,Tid);
			
			// 更新 tunnel 表 ，当有保护时候,protectTunnelId =保护的tunnelID.
			tunnelMapper.updateProtectTunnelID(newTunnelId,Tid);
			
			// 更新qos表
			qosRelevanceMapper = sqlSession.getMapper(QosRelevanceMapper.class);
			qosRelevanceMapper.updateObjId(newTunnelId,Tid,"TUNNEL");
			
			// 更新oam表1
			oamMepInfoMapper = sqlSession.getMapper(OamMepInfoMapper.class);
			oamMepInfoMapper.updateObjId(newTunnelId,Tid,"TUNNEL");
			
			oamMipInfoMapper = sqlSession.getMapper(OamMipInfoMapper.class);
			oamMipInfoMapper.updateObjId(newTunnelId,Tid,"TUNNEL");
			
			// 更新告警
			currentAlarmInfoMapper = sqlSession.getMapper(CurrentAlarmInfoMapper.class);
			currentAlarmInfoMapper.updateObjectid(newTunnelId,Tid,3);
			
			hisAlarmInfoMapper = sqlSession.getMapper(HisAlarmInfoMapper.class);
			hisAlarmInfoMapper.updateObjectid(newTunnelId,Tid,3);
			
			// 更新性能
			hisPerformanceInfoMapper = sqlSession.getMapper(HisPerformanceInfoMapper.class);
			hisPerformanceInfoMapper.updateObjectid(newTunnelId,Tid,3);
			
			// 删除老的 tunnel 数据
			tunnelMapper.deleteByTunnelIds(Tid);

		} catch (Exception e) {
			sqlSession.rollback();
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			pwInfoMapper = null;
			msPwInfoMapper = null;
			qosRelevanceMapper = null;
			oamMepInfoMapper = null;
			oamMipInfoMapper = null;
			currentAlarmInfoMapper = null;
			hisAlarmInfoMapper = null;
			hisPerformanceInfoMapper = null;
		}
		sqlSession.commit();
	}

	private void lspWhSearchdo(int newTunnelId, List<Lsp> lsps) throws Exception{
		LspinfoMapper lspinfoMapper = null;
		lspinfoMapper = sqlSession.getMapper(LspinfoMapper.class);
		lspinfoMapper.insert_lspWhSearchdo(newTunnelId, lsps.get(0), lsps.get(1));
		lspinfoMapper.deleteByTunnelID(lsps.get(0).getTunnelId());	
	}

	/**
	 * 同步portId来查询tunnle
	 * @param portId
	 * @return
	 * @throws Exception
	 */
	public List<Tunnel> findTunnelByPortId(int portId) throws Exception
	{
		List<Tunnel> tunnelList = new ArrayList<Tunnel>();
		try {
			tunnelList = this.tunnelMapper.queryByportId(portId);
		} catch (Exception e) {
			ExceptionManage.dispose(e, getClass());
		}
		return tunnelList;
		
	}
	
	public Map<Integer,String> selectTunnelNameBySiteId(int siteId) throws Exception{
		List<Tunnel> tunnels = this.tunnelMapper.selectTunnelNameBySiteId(siteId);
		Map<Integer,String> map = new HashMap<Integer, String>();
		for (int i = 0; i < tunnels.size(); i++) {
			if(tunnels.get(i).getaSiteId() == siteId){
				map.put(tunnels.get(i).getAprotectId(), tunnels.get(i).getTunnelName());
			}else if(tunnels.get(i).getzSiteId() == siteId){
				map.put(tunnels.get(i).getZprotectId(), tunnels.get(i).getTunnelName());
			}
		}
		return map;
	}
	
	/**
	 * 通过tunnel名称查询tunnelId
	 * @param tunnelName
	 * @return
	 */
	public List<Integer> selectTunnelIdByTunnelName(String tunnelName){
		List<Integer> tunnelIdList = new ArrayList<Integer>();
		try {
			tunnelIdList = this.tunnelMapper.selectTunnelIdByTunnelName(tunnelName);
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return tunnelIdList;
	}
	
	public Tunnel selectByTunnelIdAndSiteId(int siteId, int tunnelId) throws Exception {

		LspinfoMapper lspinfoMapper = null;
		Tunnel tunnel = null;
		Lsp lsp = null;
		try {
			tunnel = this.tunnelMapper.queryBySiteIdAndTunnelId(siteId, tunnelId);
			if (tunnel != null) {
				lspinfoMapper = sqlSession.getMapper(LspinfoMapper.class);
				lsp = new Lsp();
				lsp.setTunnelId(tunnel.getTunnelId());
				tunnel.setLspParticularList(lspinfoMapper.queryByCondition(lsp));
				if(tunnel.getProtectTunnelId()>0){
					tunnel.setProtectTunnel(this.selectId(tunnel.getProtectTunnelId()));
				}
			}

		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			lsp = null;
		}
		return tunnel;
	}
	
	// 端到端、单点数据都可以板
	public List<Tunnel> selectNodeByTunnelId(Tunnel tunnel) {

		Lsp lspParticular = null;
		LspinfoMapper lspParticularMapper = null;
		List<Tunnel> infos = null;
		List<Lsp> lspparticularList = null;

		try {
			infos = tunnelMapper.queryByCondition_nojoin(tunnel);

			if (null != infos && infos.size() != 0) {
				for (int i = 0; i < infos.size(); i++) {
					lspParticular = new Lsp();
					lspParticularMapper = sqlSession.getMapper(LspinfoMapper.class);
					lspParticular.setTunnelId(infos.get(i).getTunnelId());

					lspparticularList = lspParticularMapper.queryByCondition(lspParticular);
					infos.get(i).setLspParticularList(lspparticularList);
				}

				setOtherInfomationforTunnel(infos);
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			lspParticular = null;
			lspParticularMapper = null;
			lspparticularList = null;
		}
		return infos;
	}
	
	
	public static void main(String[] args) {
		try {
			Mybatis_DBManager.init("10.18.1.10");
			ConstantUtil.serviceFactory = new ServiceFactory();
			TunnelService_MB tunnelServiceMB = (TunnelService_MB) ConstantUtil.serviceFactory.newService_MB(Services.Tunnel);
			CesInfoMapper e1InfoMapper = tunnelServiceMB.sqlSession.getMapper(CesInfoMapper.class);
			CesInfo cesInfo = new CesInfo();
			e1InfoMapper.insert(cesInfo);
			System.out.println(cesInfo.getId());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}

	}

	public TunnelMapper getTunnelMapper() {
		return tunnelMapper;
	}

	public void setTunnelMapper(TunnelMapper tunnelMapper) {
		this.tunnelMapper = tunnelMapper;
	}

	public List<Tunnel> select(int siteId) {
		LspinfoMapper lspinfoMapper = null;
		List<Tunnel> tunnelList = null;
		List<Lsp> lspparticularList = null;
		try {
			lspinfoMapper = sqlSession.getMapper(LspinfoMapper.class);
			tunnelList = this.tunnelMapper.queryBySiteId(siteId);
			if (null != tunnelList && tunnelList.size() != 0) {
				for (int i = 0; i < tunnelList.size(); i++) {
					lspparticularList = lspinfoMapper.queryBySiteId(siteId, tunnelList.get(i).getTunnelId());
					tunnelList.get(i).setLspParticularList(lspparticularList);
				}
				this.setOtherInfomationforTunnel(tunnelList);
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return tunnelList;
	}

	
	/**
	 * 通过主键删除tunnel对象
	 * 
	 * @param tunnelId
	 *            主键
	 * @return 删除的记录数
	 * @throws Exception
	 */
	public void delete(List<Tunnel> tunnelList) throws Exception {
		LabelInfoService_MB labelInfoService = null;
		LspinfoMapper lspinfoMapper = null;
		OamInfoService_MB oamInfoService = null;
		OamInfo oamInfo = null;
		OamMepInfo oamMepInfo = null;
		OamMipInfo oamMipInfo = null;
		List<Lsp> lspPList = null;
		BusinessidMapper businessidMapper = null;
		Businessid businessId = null;
//		QosInfoDao qosInfoDao = null;
		QosRelevance qosRelevance = null;
		QosRelevanceMapper qosRelevanceMapper = null;
		try {

			oamInfoService = (OamInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.OamInfo, this.sqlSession);
			labelInfoService = (LabelInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.LABELINFO, this.sqlSession);
			qosRelevanceMapper = sqlSession.getMapper(QosRelevanceMapper.class);
			businessidMapper = sqlSession.getMapper(BusinessidMapper.class);
			lspinfoMapper = sqlSession.getMapper(LspinfoMapper.class);

			for (Tunnel tunnel : tunnelList) {
				if (!"0".equals(tunnel.getTunnelType()) && ("2".equals(UiUtil.getCodeById(Integer.parseInt(tunnel.getTunnelType())).getCodeValue()) 
						|| "3".equals(UiUtil.getCodeById(Integer.parseInt(tunnel.getTunnelType())).getCodeValue()))) {
					this.deleteProtect(tunnel);
				}

				oamInfo = new OamInfo();
				oamMepInfo = new OamMepInfo();
				oamMepInfo.setServiceId(tunnel.getTunnelId());
				oamMepInfo.setObjType(EServiceType.TUNNEL.toString());
				oamInfo.setOamMep(oamMepInfo);

				oamMipInfo = new OamMipInfo();
				oamMipInfo.setServiceId(tunnel.getTunnelId());
				oamMipInfo.setObjType(EServiceType.TUNNEL.toString());
				oamInfo.setOamMip(oamMipInfo);
				oamInfoService.delete(oamInfo);

				//删除之前先判断该条QoS是否被其他tunnel使用，如果被其他tunnel使用，则不删除，否则删除
				//先删除qosInfo,再删除qosRelevance
				QosInfoMapper qosInfoMapper = sqlSession.getMapper(QosInfoMapper.class);
				List<QosRelevance> qosRelavanceList = this.getQosRelevanceList(tunnel,qosRelevanceMapper);
				this.checkIsOccupy(tunnel, qosRelavanceList,qosRelevanceMapper);
				if(qosRelavanceList != null && !qosRelavanceList.isEmpty()){
					for (QosRelevance relevance : qosRelavanceList) {
						if(!relevance.isRepeat()){
							qosInfoMapper.deleteByGroupId(relevance.getQosGroupId());
						}
					}
				}
				
				qosRelevance = new QosRelevance();
				qosRelevance.setObjType(EServiceType.TUNNEL.toString());
				qosRelevance.setObjId(tunnel.getTunnelId());
				qosRelevanceMapper.deleteByCondition(qosRelevance);

				// pList = new ArrayList<ProtectionInfo>();
				// if (tunnel.getProtectType() != 0) {
				// protection = new ProtectionInfo();
				// protection.setTunnelId(tunnel.getTunnelId());
				// pList = pInfoService.selectByTunnelId(protection);
				// pInfoService.delete(protection);
				// }

				lspPList = lspinfoMapper.queryByTunnnelId(tunnel.getTunnelId());
				for (Lsp obj : lspPList) {

					if (tunnel.getIsSingle() == 0) { // 修改标签状态，如果是网络配置，修改a z穿否则只修改一穿
						labelInfoService.updateBatch(obj.getBackLabelValue(), obj.getASiteId(), 1, "WH");
//						labelInfoService.updateBatch(obj.getFrontLabelValue(), obj.getZSiteId(), 1, "TUNNEL");
//						labelInfoService.updateBatch(obj.getBackLabelValue(), obj.getASiteId(), 1, "TUNNEL");
						labelInfoService.updateBatch(obj.getFrontLabelValue(), obj.getZSiteId(), 1, "WH");
					} else {
						if (obj.getASiteId() == 0) {
							labelInfoService.updateBatch(obj.getFrontLabelValue(), obj.getZSiteId(), 1, "WH");
						}
						if (obj.getZSiteId() == 0) {
							labelInfoService.updateBatch(obj.getBackLabelValue(), obj.getASiteId(), 1, "WH");
						}
					}

					businessId = new Businessid();
					businessId.setIdStatus(0);
					businessId.setIdValue(obj.getAtunnelbusinessid());
					businessId.setType("tunnel");
					businessId.setSiteId(obj.getASiteId());
					businessidMapper.updateBusinessid(businessId);
					businessId.setIdValue(obj.getZtunnelbusinessid());
					businessId.setSiteId(obj.getZSiteId());
					businessidMapper.updateBusinessid(businessId);

				}
				lspinfoMapper.deleteByTunnelID(tunnel.getTunnelId());
				tunnelMapper.deleteByPrimaryKey(tunnel.getTunnelId());

				// 离线网元数据下载
				if (0 != tunnel.getASiteId()) {
					super.dateDownLoad(tunnel.getASiteId(), 0, EServiceType.TUNNEL.getValue(), EActionType.DELETE.getValue(), tunnel.getLspParticularList().get(0).getAtunnelbusinessid() + "", null, tunnel.getAPortId(), 0, null);
				}
				if (0 != tunnel.getZSiteId()) {
					super.dateDownLoad(tunnel.getZSiteId(), 0, EServiceType.TUNNEL.getValue(), EActionType.DELETE.getValue(), tunnel.getLspParticularList().get(0).getZtunnelbusinessid() + "", null, tunnel.getZPortId(), 0, null);
				}
				// connection.commit();
			}
			sqlSession.commit();
		} catch (Exception e) {
			sqlSession.rollback();
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			lspinfoMapper = null;
			businessidMapper = null;
			qosRelevanceMapper = null;
		}
	}
	
	/**
	 * 修改删除状态为激沿
	 * 
	 * @param idList
	 *            主键集合
	 * @throws Exception
	 */
	public void updateStatusActivate(List<Integer> idList,int type) throws Exception {
		try {
			tunnelMapper.updateStatusIDs(idList,type);
			sqlSession.commit();
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
	}
	
	/**
	 * 修改所有tunnel的激活状徿
	 * 
	 * @author kk
	 * 
	 * @param actitity
	 *            激活状徿
	 * 
	 * @return
	 * @throws Exception
	 * 
	 * @Exception 异常对象
	 */
	public void update_activity(int siteId, int activity) throws Exception {
		this.tunnelMapper.update_activity(siteId, activity);
		sqlSession.commit();
	}
	
	public Map<Integer, String> selectAllTunnelName() {
		Map<Integer, String> tunnelNameMap = new HashMap<Integer, String>();
		try {
			List<Tunnel> tunnelList = this.tunnelMapper.selectAll();
			if(tunnelList.size() > 0){
				for (Tunnel t : tunnelList) {
					tunnelNameMap.put(t.getTunnelId(), t.getTunnelName());
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, getClass());
		}
		return tunnelNameMap;
	}
	
	/**
	 * 查询全部数据（单站侧和网络侧）
	 * @return List<Tunnel>集合
	 * @throws Exception
	 */
	public List<Tunnel> selectAllData() {

		List<Tunnel> tunnelList = null;
		try {
			tunnelList = tunnelMapper.selectAll();
			this.setTunnelLsp(tunnelList,false);
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
		}
		return tunnelList;
	}
	
	/**
	 * 根据网元id，初始化某网元tunnel
	 * 
	 * @param siteId
	 * @throws SQLException
	 */
	public void initializtionSite(int siteId) throws SQLException {
		List<Tunnel> tunnels = null;
		List<Tunnel> singleTunnels = null;
		try {
			tunnels = this.selectWHNodesBySiteId(siteId);
			singleTunnels = new ArrayList<Tunnel>();
			if (tunnels != null && tunnels.size() > 0) {
				for (Tunnel tunnel : tunnels) {
					if (tunnel.getIsSingle() == 1) {// 单网元数据，直接删除
						singleTunnels.add(tunnel);
					} else {// 网络侧的，初始化该网元，并成为单网元业务
						if (tunnel.getASiteId() == siteId) {
							tunnel.setASiteId(0);
							tunnel.setAPortId(0);
							tunnel.setIsSingle(1);
						} else if (tunnel.getZSiteId() == siteId) {
							tunnel.setZSiteId(0);
							tunnel.setZPortId(0);
							tunnel.setIsSingle(1);
						} else {
							tunnel.setIsSingle(1);
						}
						this.update(tunnel);
					}
				}
			}
			if (singleTunnels.size() > 0) {
				this.delete(singleTunnels);
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
		}
	}

	/**
	 * 通过条件查询Tunnel 关联site_roate表
	 * @param tunnel 条件
	 * @return	tunnel结果集
	 * @throws Exception
	 */
	public List<Tunnel> queryByCondition_joinRotate(Tunnel tunnel) throws Exception {
		Lsp lspParticular = null;
		LspinfoMapper lspMapper = null;
		List<Tunnel> infos = new ArrayList<Tunnel>();
		List<Lsp> lspparticularList = null;
		try {
			lspMapper = this.sqlSession.getMapper(LspinfoMapper.class);
			infos = this.tunnelMapper.queryByCondition_joinRotate(tunnel);
			if (null != infos && infos.size() != 0) {
				for (int i = 0; i < infos.size(); i++) {
					lspParticular = new Lsp();
					lspParticular.setTunnelId(infos.get(i).getTunnelId());

					lspparticularList = lspMapper.queryByCondition(lspParticular);
					infos.get(i).setLspParticularList(lspparticularList);
				}

				this.setOtherInfomationforTunnel(infos);
			}else{
				return new ArrayList<Tunnel>();
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}

		return infos;
	}

	public List<Tunnel> quertyNodeByTunnelCondition(int siteId, Tunnel tunnelCondition, boolean isAddIsSingle) {
		List<Tunnel> tunnelList = new ArrayList<Tunnel>();
		Lsp lsp = null;
		LspinfoMapper lspMapper = null;
		try {
			int flag = 0;
			if(isAddIsSingle){
				flag = 1;
			}
			tunnelList = this.tunnelMapper.quertyNodeBySiteAndType(siteId, tunnelCondition.getTunnelType(), tunnelCondition.getIsSingle(), flag);
			if(tunnelList != null && tunnelList.size() > 0){
				lspMapper = this.sqlSession.getMapper(LspinfoMapper.class);
				for (Tunnel tunnel : tunnelList) {
					tunnel.setNode(true);
					lsp = new Lsp();
					lsp.setTunnelId(tunnel.getTunnelId());
					tunnel.setLspParticularList(lspMapper.queryByCondition(lsp));
				}
				this.setOtherInfomationforTunnel(tunnelList);
			}else{
				return new ArrayList<Tunnel>();
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return tunnelList;
	}
}
