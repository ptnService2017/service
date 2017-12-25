﻿package com.nms.model.util;

import org.apache.ibatis.session.SqlSession;

import com.nms.db.dao.alarm.CurrentAlarmInfoMapper;
import com.nms.db.dao.alarm.HisAlarmInfoMapper;
import com.nms.db.dao.alarm.WarningLevelMapper;
import com.nms.db.dao.client.ClientMapper;
import com.nms.db.dao.equipment.card.CardInstMapper;
import com.nms.db.dao.equipment.port.E1InfoMapper;
import com.nms.db.dao.equipment.port.Port2LayerAttrMapper;
import com.nms.db.dao.equipment.port.PortAttrMapper;
import com.nms.db.dao.equipment.port.PortInstMapper;
import com.nms.db.dao.equipment.port.PortStmMapper;
import com.nms.db.dao.equipment.port.PortStmTimeslotMapper;
import com.nms.db.dao.equipment.port.V35PortInfoMapper;
import com.nms.db.dao.equipment.shelf.EquipInstMapper;
import com.nms.db.dao.equipment.shelf.SiteInstMapper;
import com.nms.db.dao.equipment.slot.SlotInstMapper;
import com.nms.db.dao.path.SegmentMapper;
import com.nms.db.dao.path.SetNameRuleMapper;
import com.nms.db.dao.perform.CapabilityMapper;
import com.nms.db.dao.perform.HisPerformanceInfoMapper;
import com.nms.db.dao.perform.PerformanceTaskInfoMapper;
import com.nms.db.dao.perform.PmValueLimiteInfoMapper;
import com.nms.db.dao.ptn.ARPMapper;
import com.nms.db.dao.ptn.AclInstMapper;
import com.nms.db.dao.ptn.AllConfigInfoMapper;
import com.nms.db.dao.ptn.BfdInfoMapper;
import com.nms.db.dao.ptn.BlackListMacMapper;
import com.nms.db.dao.ptn.BlackWhiteMacNameMapper;
import com.nms.db.dao.ptn.BusinessidMapper;
import com.nms.db.dao.ptn.CccInfoMapper;
import com.nms.db.dao.ptn.EthLoopMapper;
import com.nms.db.dao.ptn.EthServiceInstMapper;
import com.nms.db.dao.ptn.L2cpInstMapper;
import com.nms.db.dao.ptn.LabelInfoMapper;
import com.nms.db.dao.ptn.MacLearningLimitMapper;
import com.nms.db.dao.ptn.MacStudyAddressMapper;
import com.nms.db.dao.ptn.PortDiscardInstMapper;
import com.nms.db.dao.ptn.RouteBusinessMapper;
import com.nms.db.dao.ptn.SiteRoateMapper;
import com.nms.db.dao.ptn.SmartFanMapper;
import com.nms.db.dao.ptn.TimeSyncMapper;
import com.nms.db.dao.ptn.clock.ExternalClockInterfaceMapper;
import com.nms.db.dao.ptn.clock.FrequencyInfoMapper;
import com.nms.db.dao.ptn.clock.FrequencyInfoNeClockMapper;
import com.nms.db.dao.ptn.clock.LineClockInterfaceMapper;
import com.nms.db.dao.ptn.clock.PortConfigInfoMapper;
import com.nms.db.dao.ptn.clock.TimeManageInfoMapper;
import com.nms.db.dao.ptn.clock.TodConfigInfoMapper;
import com.nms.db.dao.ptn.ecn.CCNMapper;
import com.nms.db.dao.ptn.ecn.MCNMapper;
import com.nms.db.dao.ptn.ecn.OSPFAREAInfoMapper;
import com.nms.db.dao.ptn.ecn.OSPFInfoMapper;
import com.nms.db.dao.ptn.ecn.OSPFInterfaceMapper;
import com.nms.db.dao.ptn.ecn.OSPFinfo_whMapper;
import com.nms.db.dao.ptn.ecn.OspfRedistributeMapper;
import com.nms.db.dao.ptn.oam.OamEthernetInfoMapper;
import com.nms.db.dao.ptn.oam.OamLinkInfoMapper;
import com.nms.db.dao.ptn.oam.OamMepInfoMapper;
import com.nms.db.dao.ptn.oam.OamMipInfoMapper;
import com.nms.db.dao.ptn.path.GroupSpreadInfoMapper;
import com.nms.db.dao.ptn.path.StaticUnicastInfoMapper;
import com.nms.db.dao.ptn.path.ces.CesInfoMapper;
import com.nms.db.dao.ptn.path.eth.DualInfoMapper;
import com.nms.db.dao.ptn.path.eth.ElanInfoMapper;
import com.nms.db.dao.ptn.path.eth.ElineInfoMapper;
import com.nms.db.dao.ptn.path.eth.EtreeInfoMapper;
import com.nms.db.dao.ptn.path.protect.LoopProtectInfoMapper;
import com.nms.db.dao.ptn.path.protect.MspProtectMapper;
import com.nms.db.dao.ptn.path.protect.ProtectCorbaMapper;
import com.nms.db.dao.ptn.path.protect.ProtectRorateInfoMapper;
import com.nms.db.dao.ptn.path.protect.PwProtectMapper;
import com.nms.db.dao.ptn.path.pw.MsPwInfoMapper;
import com.nms.db.dao.ptn.path.pw.PwInfoMapper;
import com.nms.db.dao.ptn.path.pw.PwNniInfoMapper;
import com.nms.db.dao.ptn.path.tunnel.LspinfoMapper;
import com.nms.db.dao.ptn.path.tunnel.TunnelMapper;
import com.nms.db.dao.ptn.port.AcPortInfoMapper;
import com.nms.db.dao.ptn.port.AcbufferMapper;
import com.nms.db.dao.ptn.port.DualProtectMapper;
import com.nms.db.dao.ptn.port.DualRelevanceMapper;
import com.nms.db.dao.ptn.port.PortLagInfoMapper;
import com.nms.db.dao.ptn.qos.QosInfoMapper;
import com.nms.db.dao.ptn.qos.QosMappingAttrMapper;
import com.nms.db.dao.ptn.qos.QosMappingModeMapper;
import com.nms.db.dao.ptn.qos.QosMappingTemplateMapper;
import com.nms.db.dao.ptn.qos.QosQueueMapper;
import com.nms.db.dao.ptn.qos.QosRelevanceMapper;
import com.nms.db.dao.ptn.qos.QosTemplateInfoMapper;
import com.nms.db.dao.report.StaticsticsMapper;
import com.nms.db.dao.system.FieldMapper;
import com.nms.db.dao.system.LogManagerMapper;
import com.nms.db.dao.system.NetWorkMapper;
import com.nms.db.dao.system.OfflinesItebusiMapper;
import com.nms.db.dao.system.OperationLogMapper;
import com.nms.db.dao.system.PerformanceRamInfoMapper;
import com.nms.db.dao.system.PtndbInstMapper;
import com.nms.db.dao.system.SiteLockMapper;
import com.nms.db.dao.system.SystemLogMapper;
import com.nms.db.dao.system.TranferInfoMapper;
import com.nms.db.dao.system.UdaAttrMapper;
import com.nms.db.dao.system.UdaGroupMapper;
import com.nms.db.dao.system.UserDesginInfoMapper;
import com.nms.db.dao.system.WorkIpsMapper;
import com.nms.db.dao.system.code.CodeGroupMapper;
import com.nms.db.dao.system.code.CodeMapper;
import com.nms.db.dao.system.loginlog.LoginLogMapper;
import com.nms.db.dao.system.loginlog.UserLockMapper;
import com.nms.db.dao.system.roleManage.RoleInfoMapper;
import com.nms.db.dao.system.roleManage.RoleManageMapper;
import com.nms.db.dao.system.roleManage.RoleRelevanceMapper;
import com.nms.db.dao.system.user.UserFieldMapper;
import com.nms.db.dao.system.user.UserInstMapper;
import com.nms.model.alarm.CurAlarmService_MB;
import com.nms.model.alarm.HisAlarmService_MB;
import com.nms.model.alarm.TCAAlarmService_MB;
import com.nms.model.alarm.WarningLevelService_MB;
import com.nms.model.client.ClientService_MB;
import com.nms.model.equipment.card.CardService_MB;
import com.nms.model.equipment.port.E1InfoService_MB;
import com.nms.model.equipment.port.Port2LayerAttrService_MB;
import com.nms.model.equipment.port.PortAttrService_MB;
import com.nms.model.equipment.port.PortService_MB;
import com.nms.model.equipment.port.PortStmService_MB;
import com.nms.model.equipment.port.PortStmTimeslotService_MB;
import com.nms.model.equipment.port.V35PortService_MB;
import com.nms.model.equipment.shlef.EquipInstService_MB;
import com.nms.model.equipment.shlef.SiteService_MB;
import com.nms.model.equipment.slot.SlotService_MB;
import com.nms.model.path.NameRuleService_MB;
import com.nms.model.path.SegmentService_MB;
import com.nms.model.perform.CapabilityService_MB;
import com.nms.model.perform.HisPerformanceService_Mb;
import com.nms.model.perform.PerformanceTaskService_MB;
import com.nms.model.perform.PmLimiteService_MB;
import com.nms.model.ptn.ARPInfoService_MB;
import com.nms.model.ptn.AclService_MB;
import com.nms.model.ptn.AllConfigService_MB;
import com.nms.model.ptn.BfdInfoService_MB;
import com.nms.model.ptn.BlackWhiteMacService_MB;
import com.nms.model.ptn.BusinessidService_MB;
import com.nms.model.ptn.CccService_MB;
import com.nms.model.ptn.EthLoopServcie_MB;
import com.nms.model.ptn.EthService_MB;
import com.nms.model.ptn.L2CPService_MB;
import com.nms.model.ptn.LabelInfoService_MB;
import com.nms.model.ptn.MacLearningService_MB;
import com.nms.model.ptn.MacManageService_MB;
import com.nms.model.ptn.PortDiscardService_MB;
import com.nms.model.ptn.RouteBusinessService_MB;
import com.nms.model.ptn.SecondMacStudyService_MB;
import com.nms.model.ptn.SiteRoateService_MB;
import com.nms.model.ptn.SmartFanService_MB;
import com.nms.model.ptn.TimeSyncService_MB;
import com.nms.model.ptn.clock.ClockFrequService_MB;
import com.nms.model.ptn.clock.ClockSourceService_Corba_MB;
import com.nms.model.ptn.clock.ExternalClockInterfaceService_MB;
import com.nms.model.ptn.clock.FrequencyInfoNeClockService_MB;
import com.nms.model.ptn.clock.PortDispositionInfoService_MB;
import com.nms.model.ptn.clock.TimeLineClockInterfaceService_MB;
import com.nms.model.ptn.clock.TimeManageInfoService_MB;
import com.nms.model.ptn.clock.TodDispositionInfoService_MB;
import com.nms.model.ptn.ecn.CCNService_MB;
import com.nms.model.ptn.ecn.MCNService_MB;
import com.nms.model.ptn.ecn.OSPFAREAService_MB;
import com.nms.model.ptn.ecn.OSPFInfoService_MB;
import com.nms.model.ptn.ecn.OSPFInterfaceService_MB;
import com.nms.model.ptn.ecn.Ospf_whService_MB;
import com.nms.model.ptn.ecn.RedistributeService_MB;
import com.nms.model.ptn.oam.OamEthreNetService_MB;
import com.nms.model.ptn.oam.OamInfoService_MB;
import com.nms.model.ptn.path.GroupSpreadService_MB;
import com.nms.model.ptn.path.SingleSpreadService_MB;
import com.nms.model.ptn.path.ces.CesInfoService_MB;
import com.nms.model.ptn.path.eth.DualInfoService_MB;
import com.nms.model.ptn.path.eth.ElanInfoService_MB;
import com.nms.model.ptn.path.eth.ElineInfoService_MB;
import com.nms.model.ptn.path.eth.EtreeInfoService_MB;
import com.nms.model.ptn.path.protect.MspProtectService_MB;
import com.nms.model.ptn.path.protect.ProtectRorateInfoService_MB;
import com.nms.model.ptn.path.protect.ProtectServiceCorba_MB;
import com.nms.model.ptn.path.protect.PwProtectService_MB;
import com.nms.model.ptn.path.protect.WrappingProtectService_MB;
import com.nms.model.ptn.path.pw.MsPwInfoService_MB;
import com.nms.model.ptn.path.pw.PwInfoService_MB;
import com.nms.model.ptn.path.pw.PwNniInfoService_MB;
import com.nms.model.ptn.path.tunnel.LspInfoService_MB;
import com.nms.model.ptn.path.tunnel.TunnelService_MB;
import com.nms.model.ptn.port.AcBufferService_MB;
import com.nms.model.ptn.port.AcPortInfoService_MB;
import com.nms.model.ptn.port.DualProtectService_MB;
import com.nms.model.ptn.port.DualRelevanceService_MB;
import com.nms.model.ptn.port.PortLagService_MB;
import com.nms.model.ptn.qos.QosInfoService_MB;
import com.nms.model.ptn.qos.QosMappingModeAttrService_MB;
import com.nms.model.ptn.qos.QosMappingModeService_MB;
import com.nms.model.ptn.qos.QosMappingTemplateService_MB;
import com.nms.model.ptn.qos.QosQueueService_MB;
import com.nms.model.ptn.qos.QosRelevanceService_MB;
import com.nms.model.ptn.qos.QosTemplateService_MB;
import com.nms.model.report.StaticsticsService_MB;
import com.nms.model.system.DataBaseService_MB;
import com.nms.model.system.DataCompareService_MB;
import com.nms.model.system.FieldService_MB;
import com.nms.model.system.LogManagerService_MB;
import com.nms.model.system.NetService_MB;
import com.nms.model.system.OffLinesiteBusiService_MB;
import com.nms.model.system.OperationLogService_MB;
import com.nms.model.system.PerformanceRamService_MB;
import com.nms.model.system.SiteLockService_MB;
import com.nms.model.system.SubnetService_MB;
import com.nms.model.system.SystemLogService_MB;
import com.nms.model.system.TranferService_Mb;
import com.nms.model.system.UdaAttrService_MB;
import com.nms.model.system.UdaGroupService_MB;
import com.nms.model.system.UserDesginService_Mb;
import com.nms.model.system.WorkIpsService_MB;
import com.nms.model.system.code.CodeGroupService_MB;
import com.nms.model.system.code.CodeService_MB;
import com.nms.model.system.loginlog.LoginLogServiece_Mb;
import com.nms.model.system.loginlog.UserLockServiece_MB;
import com.nms.model.system.roleManage.RoleInfoService_MB;
import com.nms.model.system.roleManage.RoleManageService_MB;
import com.nms.model.system.roleManage.RoleRelevanceService_MB;
import com.nms.model.system.user.UserFieldService_MB;
import com.nms.model.system.user.UserInstServiece_Mb;
import com.nms.ui.manager.ExceptionManage;
import com.nms.util.Mybatis_DBManager;


public class ServiceFactory {

	
	/**
	 * 获得对象服务
	 * 
	 * @param services
	 * @return
	 * @throws Exception
	 */
	public ObjectService_Mybatis newService_MB(int services) throws Exception {
		
		SqlSession sqlSession = Mybatis_DBManager.getSqlSession();
		return this.getService_MB(services, sqlSession);
	}

	/**
	 * 获得对象服务
	 * 
	 * @param services
	 * @return
	 * @throws Exception
	 */
	public ObjectService_Mybatis newService_MB(int services,SqlSession sqlSession) throws Exception {
		return this.getService_MB(services, sqlSession);
	}
	
	/**
	 * 根据不同关键字获取service
	 * @param services
	 * @param sqlSession
	 * @return
	 */
	private ObjectService_Mybatis getService_MB(int services, SqlSession sqlSession) {
		String ptnuser = null;								
        try {
    	  if(sqlSession.getConnection().isClosed()){
		     sqlSession =Mybatis_DBManager.getSqlSession();
    	  }
	    } catch (Exception e1) {
	    	ExceptionManage.dispose(e1,this.getClass());
	    }
								
		switch (services) {
		case Services.LOGINLOGSERVIECE:
			LoginLogServiece_Mb loginlogserviece_Mb= new LoginLogServiece_Mb();
			loginlogserviece_Mb.setPtnuser(ptnuser);
			loginlogserviece_Mb.setSqlSession(sqlSession);
			loginlogserviece_Mb.setMapper(sqlSession.getMapper(LoginLogMapper.class));
			return loginlogserviece_Mb;
		case Services.UserInst:
			UserInstServiece_Mb userInstServiece_Mb= new UserInstServiece_Mb();
			userInstServiece_Mb.setPtnuser(ptnuser);
			userInstServiece_Mb.setSqlSession(sqlSession);
			userInstServiece_Mb.setMapper(sqlSession.getMapper(UserInstMapper.class));
			return userInstServiece_Mb;
		case Services.USERDESGINSERIVE:
			UserDesginService_Mb userDesginServiece_Mb= new UserDesginService_Mb();
			userDesginServiece_Mb.setPtnuser(ptnuser);
			userDesginServiece_Mb.setSqlSession(sqlSession);
			userDesginServiece_Mb.setMapper(sqlSession.getMapper(UserDesginInfoMapper.class));
			return userDesginServiece_Mb;
		case Services.HisPerformance:
			HisPerformanceService_Mb hisPerformanceService_Mb= new HisPerformanceService_Mb();
			hisPerformanceService_Mb.setPtnuser(ptnuser);			
			hisPerformanceService_Mb.setSqlSession(sqlSession);
			hisPerformanceService_Mb.setMapper(sqlSession.getMapper(HisPerformanceInfoMapper.class));
			return hisPerformanceService_Mb;
		case Services.SEGMENT:
			SegmentService_MB segmentService = new SegmentService_MB();
			segmentService.setPtnuser(ptnuser);
			segmentService.setSqlSession(sqlSession);
			segmentService.setMapper(sqlSession.getMapper(SegmentMapper.class));
			return segmentService;
		case Services.SITE:
			SiteService_MB siteService = new SiteService_MB();
			siteService.setPtnuser(ptnuser);
			siteService.setSqlSession(sqlSession);
			siteService.setSiteInstMapper(sqlSession.getMapper(SiteInstMapper.class));
			return siteService;
		case Services.Code:
			CodeService_MB codeservice = new CodeService_MB();
			codeservice.setPtnuser(ptnuser);
			codeservice.setSqlSession(sqlSession);
			codeservice.setMapper(sqlSession.getMapper(CodeMapper.class));
			return codeservice;
		case Services.PORT:
			PortService_MB portService = new PortService_MB();
			portService.setPtnuser(ptnuser);
			portService.setSqlSession(sqlSession);
			portService.setMapper(sqlSession.getMapper(PortInstMapper.class));
			return portService;
		case Services.PortAttr:
			PortAttrService_MB portAttrService = new PortAttrService_MB();
			portAttrService.setPtnuser(ptnuser);
			portAttrService.setSqlSession(sqlSession);
			portAttrService.setMapper(sqlSession.getMapper(PortAttrMapper.class));
			return portAttrService;
		case Services.Tunnel:
			TunnelService_MB tunnelServiceMB = new TunnelService_MB();
			tunnelServiceMB.setPtnuser(ptnuser);
			tunnelServiceMB.setSqlSession(sqlSession);
			tunnelServiceMB.setTunnelMapper(sqlSession.getMapper(TunnelMapper.class));
			return tunnelServiceMB;
		case Services.OamInfo:
			OamInfoService_MB oamInfoServiceMB = new OamInfoService_MB();
			oamInfoServiceMB.setPtnuser(ptnuser);
			oamInfoServiceMB.setSqlSession(sqlSession);
			oamInfoServiceMB.setOamMepInfoMapper(sqlSession.getMapper(OamMepInfoMapper.class));
			oamInfoServiceMB.setOamMipInfoMapper(sqlSession.getMapper(OamMipInfoMapper.class));
			oamInfoServiceMB.setLinkInfoMapper(sqlSession.getMapper(OamLinkInfoMapper.class));
			return oamInfoServiceMB;
		case Services.TRANFERSERVICE:
			TranferService_Mb tranferService = new TranferService_Mb();
			tranferService.setPtnuser(ptnuser);
			tranferService.setSqlSession(sqlSession);
			tranferService.setMapper(sqlSession.getMapper(TranferInfoMapper.class));
			return tranferService;
		case Services.CesInfo:
			CesInfoService_MB cesInfoServiceMB = new CesInfoService_MB();
			cesInfoServiceMB.setPtnuser(ptnuser);
			cesInfoServiceMB.setSqlSession(sqlSession);
			cesInfoServiceMB.setCesInfoMapper(sqlSession.getMapper(CesInfoMapper.class));
			return cesInfoServiceMB;
		case Services.DUALINFO:
			DualInfoService_MB dualInfoServiceMB = new DualInfoService_MB();
			dualInfoServiceMB.setPtnuser(ptnuser);
			dualInfoServiceMB.setSqlSession(sqlSession);
			dualInfoServiceMB.setDualInfoMapper(sqlSession.getMapper(DualInfoMapper.class));
			return dualInfoServiceMB;
		case Services.ElanInfo:
			ElanInfoService_MB elanInfoServiceMB = new ElanInfoService_MB();
			elanInfoServiceMB.setPtnuser(ptnuser);
			elanInfoServiceMB.setSqlSession(sqlSession);
			elanInfoServiceMB.setElanInfoMapper(sqlSession.getMapper(ElanInfoMapper.class));
			return elanInfoServiceMB;
		case Services.Eline:
			ElineInfoService_MB elineInfoServiceMB = new ElineInfoService_MB();
			elineInfoServiceMB.setPtnuser(ptnuser);
			elineInfoServiceMB.setSqlSession(sqlSession);
			elineInfoServiceMB.setElineInfoMapper(sqlSession.getMapper(ElineInfoMapper.class));
			return elineInfoServiceMB;
		case Services.EtreeInfo:
			EtreeInfoService_MB etreeInfoServiceMB = new EtreeInfoService_MB();
			etreeInfoServiceMB.setPtnuser(ptnuser);
			etreeInfoServiceMB.setSqlSession(sqlSession);
			etreeInfoServiceMB.setEtreeInfoMapper(sqlSession.getMapper(EtreeInfoMapper.class));
			return etreeInfoServiceMB;
		case Services.MSPPROTECT:
			MspProtectService_MB mspProtectServiceMB = new MspProtectService_MB();
			mspProtectServiceMB.setPtnuser(ptnuser);
			mspProtectServiceMB.setSqlSession(sqlSession);
			mspProtectServiceMB.setMspProtectMapper(sqlSession.getMapper(MspProtectMapper.class));
			return mspProtectServiceMB;
		case Services.PWPROTECT:
			PwProtectService_MB pwProtectServiceMB = new PwProtectService_MB();
			pwProtectServiceMB.setPtnuser(ptnuser);
			pwProtectServiceMB.setSqlSession(sqlSession);
			pwProtectServiceMB.setPwProtectMapper(sqlSession.getMapper(PwProtectMapper.class));
			return pwProtectServiceMB;
		case Services.MSPWSERVICE:
			MsPwInfoService_MB msPwInfoServiceMB = new MsPwInfoService_MB();
			msPwInfoServiceMB.setPtnuser(ptnuser);
			msPwInfoServiceMB.setSqlSession(sqlSession);
			msPwInfoServiceMB.setMsPwInfoMapper(sqlSession.getMapper(MsPwInfoMapper.class));
			return msPwInfoServiceMB;
		case Services.PwInfo:
			PwInfoService_MB pwInfoServiceMB = new PwInfoService_MB();
			pwInfoServiceMB.setPtnuser(ptnuser);
			pwInfoServiceMB.setSqlSession(sqlSession);
			pwInfoServiceMB.setPwInfoMapper(sqlSession.getMapper(PwInfoMapper.class));
			return pwInfoServiceMB;
		case Services.PwNniBuffer:
			PwNniInfoService_MB pwNniInfoServiceMB = new PwNniInfoService_MB();
			pwNniInfoServiceMB.setPtnuser(ptnuser);
			pwNniInfoServiceMB.setSqlSession(sqlSession);
			pwNniInfoServiceMB.setPwNniInfoMapper(sqlSession.getMapper(PwNniInfoMapper.class));
			return pwNniInfoServiceMB;
		case Services.LSPINFO:
			LspInfoService_MB lspInfoServiceMB = new LspInfoService_MB();
			lspInfoServiceMB.setPtnuser(ptnuser);
			lspInfoServiceMB.setSqlSession(sqlSession);
			lspInfoServiceMB.setLspInfoMapper(sqlSession.getMapper(LspinfoMapper.class));
			return lspInfoServiceMB;
		case Services.SINGELSPREAD:
			SingleSpreadService_MB singleSpreadServiceMB = new SingleSpreadService_MB();
			singleSpreadServiceMB.setPtnuser(ptnuser);
			singleSpreadServiceMB.setSqlSession(sqlSession);
			singleSpreadServiceMB.setStaticUnicastInfoMapper(sqlSession.getMapper(StaticUnicastInfoMapper.class));
			return singleSpreadServiceMB;
		case Services.GROUPSPREAD:
			GroupSpreadService_MB groupSpreadServiceMB = new GroupSpreadService_MB();
			groupSpreadServiceMB.setPtnuser(ptnuser);
			groupSpreadServiceMB.setSqlSession(sqlSession);
			groupSpreadServiceMB.setGroupSpreadInfoMapper(sqlSession.getMapper(GroupSpreadInfoMapper.class));
			return groupSpreadServiceMB;
		case Services.UniBuffer:
			AcBufferService_MB acBufferServiceMB = new AcBufferService_MB();
			acBufferServiceMB.setPtnuser(ptnuser);
			acBufferServiceMB.setSqlSession(sqlSession);
			acBufferServiceMB.setAcbufferMapper(sqlSession.getMapper(AcbufferMapper.class));
			return acBufferServiceMB;
		case Services.AcInfo:
			AcPortInfoService_MB acPortInfoServiceMB = new AcPortInfoService_MB();
			acPortInfoServiceMB.setPtnuser(ptnuser);
			acPortInfoServiceMB.setSqlSession(sqlSession);
			acPortInfoServiceMB.setAcPortInfoMapper(sqlSession.getMapper(AcPortInfoMapper.class));
			return acPortInfoServiceMB;
		case Services.DUALPROTECTSERVICE:
			DualProtectService_MB dualProtectServiceMB = new DualProtectService_MB();
			dualProtectServiceMB.setPtnuser(ptnuser);
			dualProtectServiceMB.setSqlSession(sqlSession);
			dualProtectServiceMB.setDualProtectMapper(sqlSession.getMapper(DualProtectMapper.class));
			return dualProtectServiceMB;
		case Services.DUALRELEVANCE:
			DualRelevanceService_MB dualRelevanceServiceMB = new DualRelevanceService_MB();
			dualRelevanceServiceMB.setPtnuser(ptnuser);
			dualRelevanceServiceMB.setSqlSession(sqlSession);
			dualRelevanceServiceMB.setDualRelevanceMapper(sqlSession.getMapper(DualRelevanceMapper.class));
			return dualRelevanceServiceMB;
		case Services.PORTLAG:
			PortLagService_MB portLagServiceMB = new PortLagService_MB();
			portLagServiceMB.setPtnuser(ptnuser);
			portLagServiceMB.setSqlSession(sqlSession);
			portLagServiceMB.setPortLagInfoMapper(sqlSession.getMapper(PortLagInfoMapper.class));
			return portLagServiceMB;
		case Services.CurrentAlarm:
			CurAlarmService_MB CurAlarmService = new CurAlarmService_MB();
			CurAlarmService.setPtnuser(ptnuser);
			CurAlarmService.setSqlSession(sqlSession);
			CurAlarmService.setMapper(sqlSession.getMapper(CurrentAlarmInfoMapper.class));
			return CurAlarmService;	
		case Services.HisAlarm:
			HisAlarmService_MB hisAlarmService = new HisAlarmService_MB();
			hisAlarmService.setPtnuser(ptnuser);
			hisAlarmService.setSqlSession(sqlSession);
			hisAlarmService.setMapper(sqlSession.getMapper(HisAlarmInfoMapper.class));
			return hisAlarmService;
		case Services.TCAALARM:
			TCAAlarmService_MB tcaAlarmService = new TCAAlarmService_MB();
			tcaAlarmService.setPtnuser(ptnuser);
			tcaAlarmService.setSqlSession(sqlSession);
			tcaAlarmService.setMapper(sqlSession.getMapper(CurrentAlarmInfoMapper.class));
			return tcaAlarmService;
		case Services.WarningLevel:
			WarningLevelService_MB warninglevelservice = new WarningLevelService_MB();
			warninglevelservice.setPtnuser(ptnuser);
			warninglevelservice.setSqlSession(sqlSession);
			warninglevelservice.setMapper(sqlSession.getMapper(WarningLevelMapper.class));
			return warninglevelservice;
		case Services.CLIENTSERVICE:
			ClientService_MB clientService = new ClientService_MB();
			clientService.setPtnuser(ptnuser);
			clientService.setSqlSession(sqlSession);
			clientService.setMapper(sqlSession.getMapper(ClientMapper.class));
			return clientService;
		case Services.CARD:
			CardService_MB cardService = new CardService_MB();
			cardService.setPtnuser(ptnuser);
			cardService.setSqlSession(sqlSession);
			cardService.setMapper(sqlSession.getMapper(CardInstMapper.class));
			return cardService;
		case Services.E1Info:
			E1InfoService_MB e1InfoService = new E1InfoService_MB();
			e1InfoService.setPtnuser(ptnuser);
			e1InfoService.setSqlSession(sqlSession);
			e1InfoService.setMapper(sqlSession.getMapper(E1InfoMapper.class));
			return e1InfoService;
		case Services.PORT_2LAYER_ATTR:
			Port2LayerAttrService_MB port2LayerAttrService = new Port2LayerAttrService_MB();
			port2LayerAttrService.setPtnuser(ptnuser);
			port2LayerAttrService.setSqlSession(sqlSession);
			port2LayerAttrService.setMapper(sqlSession.getMapper(Port2LayerAttrMapper.class));
			return port2LayerAttrService;
		case Services.PORTSTM:
			PortStmService_MB portstmservice = new PortStmService_MB();
			portstmservice.setPtnuser(ptnuser);
			portstmservice.setSqlSession(sqlSession);
			portstmservice.setMapper(sqlSession.getMapper(PortStmMapper.class));
			return portstmservice;
		case Services.PORTSTMTIMESLOT:
			PortStmTimeslotService_MB portStmTimeslotService = new PortStmTimeslotService_MB();
			portStmTimeslotService.setPtnuser(ptnuser);
			portStmTimeslotService.setSqlSession(sqlSession);
			portStmTimeslotService.setMapper(sqlSession.getMapper(PortStmTimeslotMapper.class));
			return portStmTimeslotService;
		case Services.V35PORT:
			V35PortService_MB v35PortService = new V35PortService_MB();
			v35PortService.setPtnuser(ptnuser);
			v35PortService.setSqlSession(sqlSession);
			v35PortService.setMapper(sqlSession.getMapper(V35PortInfoMapper.class));
			return v35PortService;
		case Services.Equip:
			EquipInstService_MB equipinstservice = new EquipInstService_MB();
			equipinstservice.setPtnuser(ptnuser);
			equipinstservice.setSqlSession(sqlSession);
			equipinstservice.setMapper(sqlSession.getMapper(EquipInstMapper.class));
			return equipinstservice;
		case Services.SLOT:
			SlotService_MB slotService = new SlotService_MB();
			slotService.setPtnuser(ptnuser);
			slotService.setSqlSession(sqlSession);
			slotService.setMapper(sqlSession.getMapper(SlotInstMapper.class));
			return slotService;
		case Services.NAMERULESERVICE:
			NameRuleService_MB nameRuleSerivice = new NameRuleService_MB();
			nameRuleSerivice.setPtnuser(ptnuser);
			nameRuleSerivice.setSqlSession(sqlSession);
			nameRuleSerivice.setMapper(sqlSession.getMapper(SetNameRuleMapper.class));
			return nameRuleSerivice;
		case Services.Capability:
			CapabilityService_MB capabilityservice = new CapabilityService_MB();
			capabilityservice.setPtnuser(ptnuser);
			capabilityservice.setSqlSession(sqlSession);
			capabilityservice.setMapper(sqlSession.getMapper(CapabilityMapper.class));
			return capabilityservice;
		case Services.PerformanceTask:
			PerformanceTaskService_MB perfTaskService = new PerformanceTaskService_MB();
			perfTaskService.setPtnuser(ptnuser);
			perfTaskService.setSqlSession(sqlSession);
			perfTaskService.setMapper(sqlSession.getMapper(PerformanceTaskInfoMapper.class));
			return perfTaskService;
		case Services.PmLimiteService:
			PmLimiteService_MB pmLimiteService = new PmLimiteService_MB();
			pmLimiteService.setPtnuser(ptnuser);
			pmLimiteService.setSqlSession(sqlSession);
			pmLimiteService.setMapper(sqlSession.getMapper(PmValueLimiteInfoMapper.class));
			return pmLimiteService;
		case Services.ClockFrequ:
			ClockFrequService_MB clockService = new ClockFrequService_MB();
			clockService.setPtnuser(ptnuser);
			clockService.setSqlSession(sqlSession);
			clockService.setMapper(sqlSession.getMapper(FrequencyInfoMapper.class));
			return clockService;
		case Services.ClockSource_CORBA:
			ClockSourceService_Corba_MB clockSourceService_Corba = new ClockSourceService_Corba_MB();
			clockSourceService_Corba.setPtnuser(ptnuser);
			clockSourceService_Corba.setSqlSession(sqlSession);
			return clockSourceService_Corba;
		case Services.ExternalClockInterfaceService:
			ExternalClockInterfaceService_MB externalClockInterfaceService = new ExternalClockInterfaceService_MB();
			externalClockInterfaceService.setPtnuser(ptnuser);
			externalClockInterfaceService.setSqlSession(sqlSession);
			externalClockInterfaceService.setMapper(sqlSession.getMapper(ExternalClockInterfaceMapper.class));
			return externalClockInterfaceService;	
		case Services.FrequencyInfo_neClockService:
			FrequencyInfoNeClockService_MB frequencyInfo_neClockService = new FrequencyInfoNeClockService_MB();
			frequencyInfo_neClockService.setPtnuser(ptnuser);
			frequencyInfo_neClockService.setSqlSession(sqlSession);
			frequencyInfo_neClockService.setMapper(sqlSession.getMapper(FrequencyInfoNeClockMapper.class));
			return frequencyInfo_neClockService; 
		case Services.PortDispositionInfoService:
			PortDispositionInfoService_MB portDispositionInfoService = new PortDispositionInfoService_MB();
			portDispositionInfoService.setPtnuser(ptnuser);
			portDispositionInfoService.setSqlSession(sqlSession);
			portDispositionInfoService.setMapper(sqlSession.getMapper(PortConfigInfoMapper.class));
			return portDispositionInfoService;
		case Services.TimeLineClockInterfaceService:
			TimeLineClockInterfaceService_MB timeLineClockInterfaceService = new TimeLineClockInterfaceService_MB();
			timeLineClockInterfaceService.setPtnuser(ptnuser);
			timeLineClockInterfaceService.setSqlSession(sqlSession);
			timeLineClockInterfaceService.setMapper(sqlSession.getMapper(LineClockInterfaceMapper.class));
			return timeLineClockInterfaceService;
		case Services.TimeManageInfoService:
			TimeManageInfoService_MB timeManageInfoService = new TimeManageInfoService_MB();
			timeManageInfoService.setPtnuser(ptnuser);
			timeManageInfoService.setSqlSession(sqlSession);
			timeManageInfoService.setMapper(sqlSession.getMapper(TimeManageInfoMapper.class));
			return timeManageInfoService;
		case Services.TodDispositionInfoService:
			TodDispositionInfoService_MB todDispositionInfoService = new TodDispositionInfoService_MB();
			todDispositionInfoService.setPtnuser(ptnuser);
			todDispositionInfoService.setSqlSession(sqlSession);
			todDispositionInfoService.setMapper(sqlSession.getMapper(TodConfigInfoMapper.class));
			return todDispositionInfoService;
		case Services.CCN:
			CCNService_MB ccnService = new CCNService_MB();
			ccnService.setPtnuser(ptnuser);
			ccnService.setSqlSession(sqlSession);
			ccnService.setMapper(sqlSession.getMapper(CCNMapper.class));
			return ccnService;
		case Services.MCN:
			MCNService_MB mcnService = new MCNService_MB();
			mcnService.setPtnuser(ptnuser);
			mcnService.setSqlSession(sqlSession);
			mcnService.setMapper(sqlSession.getMapper(MCNMapper.class));
			return mcnService;
		case Services.OSPFAREA:// TXC
			OSPFAREAService_MB oSPFAREAService = new OSPFAREAService_MB();
			oSPFAREAService.setPtnuser(ptnuser);
			oSPFAREAService.setSqlSession(sqlSession);
			oSPFAREAService.setMapper(sqlSession.getMapper(OSPFAREAInfoMapper.class));
			return oSPFAREAService;
		case Services.OSPFInfo:// TXC
			OSPFInfoService_MB oSPFInfoService = new OSPFInfoService_MB();
			oSPFInfoService.setPtnuser(ptnuser);
			oSPFInfoService.setSqlSession(sqlSession);
			oSPFInfoService.setMapper(sqlSession.getMapper(OSPFInfoMapper.class));
			return oSPFInfoService;
		case Services.OSPFINTERFACE:
			OSPFInterfaceService_MB ospfInterfaceService = new OSPFInterfaceService_MB();
			ospfInterfaceService.setPtnuser(ptnuser);
			ospfInterfaceService.setSqlSession(sqlSession);
			ospfInterfaceService.setMapper(sqlSession.getMapper(OSPFInterfaceMapper.class));
			return ospfInterfaceService;
		case Services.REDISTRIBUTE:
			RedistributeService_MB redistributeService = new RedistributeService_MB();
			redistributeService.setPtnuser(ptnuser);
			redistributeService.setSqlSession(sqlSession);
			redistributeService.setMapper(sqlSession.getMapper(OspfRedistributeMapper.class));
			return redistributeService;
		case Services.CodeGroup:
			CodeGroupService_MB codeGroupService = new CodeGroupService_MB();
			codeGroupService.setPtnuser(ptnuser);
			codeGroupService.setSqlSession(sqlSession);
			codeGroupService.setMapper(sqlSession.getMapper(CodeGroupMapper.class));
			return codeGroupService;
		case Services.USERLOCKSERVIECE:
			UserLockServiece_MB userLockService = new UserLockServiece_MB();
			userLockService.setPtnuser(ptnuser);
			userLockService.setSqlSession(sqlSession);
			userLockService.setMapper(sqlSession.getMapper(UserLockMapper.class));
			return userLockService;
		case Services.ROLEMANAGESERVICE:
			RoleManageService_MB roleManageService = new RoleManageService_MB();
			roleManageService.setPtnuser(ptnuser);
			roleManageService.setSqlSession(sqlSession);
			roleManageService.setMapper(sqlSession.getMapper(RoleManageMapper.class));
			return roleManageService;
		case Services.ROLEINFOSERVICE:
			RoleInfoService_MB roleInfoService = new RoleInfoService_MB();
			roleInfoService.setPtnuser(ptnuser);
			roleInfoService.setSqlSession(sqlSession);
			roleInfoService.setMapper(sqlSession.getMapper(RoleInfoMapper.class));
			return roleInfoService;
		case Services.ROLERELEVANCESERVICE:
			RoleRelevanceService_MB roleRelevanceService = new RoleRelevanceService_MB();
			roleRelevanceService.setPtnuser(ptnuser);
			roleRelevanceService.setSqlSession(sqlSession);
			roleRelevanceService.setMapper(sqlSession.getMapper(RoleRelevanceMapper.class));
			return roleRelevanceService;
		case Services.USERFIELD:
			UserFieldService_MB userFieldService = new UserFieldService_MB();
			userFieldService.setPtnuser(ptnuser);
			userFieldService.setSqlSession(sqlSession);
			userFieldService.setMapper(sqlSession.getMapper(UserFieldMapper.class));
			return userFieldService;
		case Services.DATABASEINFO:
			DataBaseService_MB dataBaseService = new DataBaseService_MB();
			dataBaseService.setPtnuser(ptnuser);
			dataBaseService.setSqlSession(sqlSession);
			dataBaseService.setMapper(sqlSession.getMapper(PtndbInstMapper.class));
			return dataBaseService;
		case Services.Field:
			FieldService_MB fieldService = new FieldService_MB();
			fieldService.setPtnuser(ptnuser);
			fieldService.setSqlSession(sqlSession);
			fieldService.setMapper(sqlSession.getMapper(FieldMapper.class));
			return fieldService;
		case Services.NETWORKSERVICE:
			NetService_MB netWorkService = new NetService_MB();
			netWorkService.setPtnuser(ptnuser);
			netWorkService.setSqlSession(sqlSession);
			netWorkService.setMapper(sqlSession.getMapper(NetWorkMapper.class));
			return netWorkService;
		case Services.OFFLINESITEBUSISERVICE:
			OffLinesiteBusiService_MB offLinesiteBusiService = new OffLinesiteBusiService_MB();
			offLinesiteBusiService.setPtnuser(ptnuser);
			offLinesiteBusiService.setSqlSession(sqlSession);
			offLinesiteBusiService.setMapper(sqlSession.getMapper(OfflinesItebusiMapper.class));
			return offLinesiteBusiService;
		case Services.OPERATIONLOGSERVIECE:
			OperationLogService_MB operationLogService = new OperationLogService_MB();
			operationLogService.setPtnuser(ptnuser);
			operationLogService.setSqlSession(sqlSession);
			operationLogService.setMapper(sqlSession.getMapper(OperationLogMapper.class));
			return operationLogService;
		case Services.PERFORMANCERAM:
			PerformanceRamService_MB performanceRamService = new PerformanceRamService_MB();
			performanceRamService.setPtnuser(ptnuser);
			performanceRamService.setSqlSession(sqlSession);
			performanceRamService.setMapper(sqlSession.getMapper(PerformanceRamInfoMapper.class));
			return performanceRamService;
		case Services.SITELOCK:
			SiteLockService_MB siteLockService = new SiteLockService_MB();
			siteLockService.setPtnuser(ptnuser);
			siteLockService.setSqlSession(sqlSession);
			siteLockService.setMapper(sqlSession.getMapper(SiteLockMapper.class));
			return siteLockService;
		case Services.SUBNETSERVICE:
			SubnetService_MB subnetService = new SubnetService_MB();
			subnetService.setPtnuser(ptnuser);
			subnetService.setSqlSession(sqlSession);
			subnetService.setMapper(sqlSession.getMapper(FieldMapper.class));
			return subnetService;
		case Services.UDAATTR:
			UdaAttrService_MB udaAttrService = new UdaAttrService_MB();
			udaAttrService.setPtnuser(ptnuser);
			udaAttrService.setSqlSession(sqlSession);
			udaAttrService.setMapper(sqlSession.getMapper(UdaAttrMapper.class));
			return udaAttrService;
		case Services.UDAGROUP:
			UdaGroupService_MB udaGroupService = new UdaGroupService_MB();
			udaGroupService.setPtnuser(ptnuser);
			udaGroupService.setSqlSession(sqlSession);
			udaGroupService.setMapper(sqlSession.getMapper(UdaGroupMapper.class));
			return udaGroupService;
		case Services.ACLSERCVICE:
			AclService_MB aclService = new AclService_MB();
			aclService.setPtnuser(ptnuser);
			aclService.setSqlSession(sqlSession);
			aclService.setMapper(sqlSession.getMapper(AclInstMapper.class));
			return aclService;
		case Services.BLACKWHITEMAC:
			BlackWhiteMacService_MB blackWhiteMacService = new BlackWhiteMacService_MB();
			blackWhiteMacService.setPtnuser(ptnuser);
			blackWhiteMacService.setSqlSession(sqlSession);
			blackWhiteMacService.setMapper(sqlSession.getMapper(BlackWhiteMacNameMapper.class));
			return blackWhiteMacService;
		case Services.CCCMANAGEMENT:
			CccService_MB cccService = new CccService_MB();
			cccService.setPtnuser(ptnuser);
			cccService.setSqlSession(sqlSession);
			cccService.setMapper(sqlSession.getMapper(CccInfoMapper.class));
			return cccService;
		case Services.ETHLOOPSERVICE:
			EthLoopServcie_MB ethLoopService = new EthLoopServcie_MB();
			ethLoopService.setPtnuser(ptnuser);
			ethLoopService.setSqlSession(sqlSession);
			ethLoopService.setMapper(sqlSession.getMapper(EthLoopMapper.class));
			return ethLoopService;
		case Services.ETHSERVICE:
			EthService_MB ethService = new EthService_MB();
			ethService.setPtnuser(ptnuser);
			ethService.setSqlSession(sqlSession);
			ethService.setMapper(sqlSession.getMapper(EthServiceInstMapper.class));
			return ethService;
		case Services.L2CPSERVICE:
			L2CPService_MB l2cpService = new L2CPService_MB();
			l2cpService.setPtnuser(ptnuser);
			l2cpService.setSqlSession(sqlSession);
			l2cpService.setMapper(sqlSession.getMapper(L2cpInstMapper.class));
			return l2cpService;
		case Services.MACLEARN:
			MacLearningService_MB macLearningService = new MacLearningService_MB();
			macLearningService.setPtnuser(ptnuser);
			macLearningService.setSqlSession(sqlSession);
			macLearningService.setMapper(sqlSession.getMapper(MacLearningLimitMapper.class));
			return macLearningService;
		case Services.MACMANAGE:
			MacManageService_MB macManageService = new MacManageService_MB();
			macManageService.setPtnuser(ptnuser);
			macManageService.setSqlSession(sqlSession);
			macManageService.setMapper(sqlSession.getMapper(BlackListMacMapper.class));
			return macManageService;
		case Services.PortDiscardService:
			PortDiscardService_MB portDiscardService = new PortDiscardService_MB();
			portDiscardService.setPtnuser(ptnuser);
			portDiscardService.setSqlSession(sqlSession);
			portDiscardService.setMapper(sqlSession.getMapper(PortDiscardInstMapper.class));
			return portDiscardService;
		case Services.ROUTEBUSINESS:
			RouteBusinessService_MB routeBusinessService = new RouteBusinessService_MB();
			routeBusinessService.setPtnuser(ptnuser);
			routeBusinessService.setSqlSession(sqlSession);
			routeBusinessService.setMapper(sqlSession.getMapper(RouteBusinessMapper.class));
			return routeBusinessService;
		case Services.SECONDMACSTUDY:
			SecondMacStudyService_MB secondMacStudyService = new SecondMacStudyService_MB();
			secondMacStudyService.setPtnuser(ptnuser);
			secondMacStudyService.setSqlSession(sqlSession);
			secondMacStudyService.setMapper(sqlSession.getMapper(MacStudyAddressMapper.class));
			return secondMacStudyService;
		case Services.SMARTFNASERVICE:
			SmartFanService_MB smartFanService = new SmartFanService_MB();
			smartFanService.setPtnuser(ptnuser);
			smartFanService.setSqlSession(sqlSession);
			smartFanService.setMapper(sqlSession.getMapper(SmartFanMapper.class));
			return smartFanService;
		case Services.TIME_SYNC_MANGE:
			TimeSyncService_MB timeSyncService = new TimeSyncService_MB();
			timeSyncService.setPtnuser(ptnuser);
			timeSyncService.setSqlSession(sqlSession);
			timeSyncService.setMapper(sqlSession.getMapper(TimeSyncMapper.class));
			return timeSyncService;
		case Services.QosInfo:
			QosInfoService_MB qosInfoServiceMB = new QosInfoService_MB();
			qosInfoServiceMB.setPtnuser(ptnuser);
			qosInfoServiceMB.setSqlSession(sqlSession);
			qosInfoServiceMB.setQosInfoMapper(sqlSession.getMapper(QosInfoMapper.class));
			return qosInfoServiceMB;
		case Services.QosMappingModeAttrService:
			QosMappingModeAttrService_MB qosMappingModeAttrServiceMB = new QosMappingModeAttrService_MB();
			qosMappingModeAttrServiceMB.setPtnuser(ptnuser);
			qosMappingModeAttrServiceMB.setSqlSession(sqlSession);
			qosMappingModeAttrServiceMB.setQosMappingAttrMapper(sqlSession.getMapper(QosMappingAttrMapper.class));
			return qosMappingModeAttrServiceMB;
		case Services.QosMappingModeService:
			QosMappingModeService_MB qosMappingModeServiceMB = new QosMappingModeService_MB();
			qosMappingModeServiceMB.setPtnuser(ptnuser);
			qosMappingModeServiceMB.setSqlSession(sqlSession);
			qosMappingModeServiceMB.setQosMappingModeMapper(sqlSession.getMapper(QosMappingModeMapper.class));
			return qosMappingModeServiceMB;
		case Services.QOSRELEVANCE:
			QosRelevanceService_MB qosRelevanceServiceMB = new QosRelevanceService_MB();
			qosRelevanceServiceMB.setPtnuser(ptnuser);
			qosRelevanceServiceMB.setSqlSession(sqlSession);
			qosRelevanceServiceMB.setQosRelevanceMapper(sqlSession.getMapper(QosRelevanceMapper.class));
			return qosRelevanceServiceMB;
		case Services.QosTemplate:
			QosTemplateService_MB qosTemplateServiceMB = new QosTemplateService_MB();
			qosTemplateServiceMB.setPtnuser(ptnuser);
			qosTemplateServiceMB.setSqlSession(sqlSession);
			qosTemplateServiceMB.setQosTemplateInfoMapper(sqlSession.getMapper(QosTemplateInfoMapper.class));
			return qosTemplateServiceMB;
		case Services.LABELINFO:
			LabelInfoService_MB labelInfoService = new LabelInfoService_MB();
			labelInfoService.setPtnuser(ptnuser);
			labelInfoService.setSqlSession(sqlSession);
			labelInfoService.setMapper(sqlSession.getMapper(LabelInfoMapper.class));
			return labelInfoService;
		case Services.BUSINESSID:
			BusinessidService_MB businessidServiceMB = new BusinessidService_MB();
			businessidServiceMB.setPtnuser(ptnuser);
			businessidServiceMB.setSession(sqlSession);
			businessidServiceMB.setMapper(sqlSession.getMapper(BusinessidMapper.class));
			return businessidServiceMB;
		case Services.QosQueue:
			QosQueueService_MB qosQueueService = new QosQueueService_MB();
			qosQueueService.setPtnuser(ptnuser);
			qosQueueService.setSession(sqlSession);
			qosQueueService.setQosQueueMapper(sqlSession.getMapper(QosQueueMapper.class));
			return qosQueueService;				
		case Services.STATISTICS:
			StaticsticsService_MB staticsticsService = new StaticsticsService_MB();
			staticsticsService.setPtnuser(ptnuser);
			staticsticsService.setSqlSession(sqlSession);
			staticsticsService.setMapper(sqlSession.getMapper(StaticsticsMapper.class));
			return staticsticsService;	
		case Services.ALLCONFIG:
			AllConfigService_MB allConfigService = new AllConfigService_MB();
			allConfigService.setPtnuser(ptnuser);
			allConfigService.setSession(sqlSession);
			allConfigService.setMapper(sqlSession.getMapper(AllConfigInfoMapper.class));
			return allConfigService;
		case Services.SITEROATE:
			SiteRoateService_MB roateServiceMB = new SiteRoateService_MB();
			roateServiceMB.setPtnuser(ptnuser);
			roateServiceMB.setSqlSession(sqlSession);
			roateServiceMB.setMapper(sqlSession.getMapper(SiteRoateMapper.class));
			return roateServiceMB;		
		case Services.BFDMANAGEMENT:
			BfdInfoService_MB bfdService = new BfdInfoService_MB();
			bfdService.setPtnuser(ptnuser);
			bfdService.setSession(sqlSession);
			bfdService.setMapper(sqlSession.getMapper(BfdInfoMapper.class));
			return bfdService;
		case Services.WRAPPINGPROTECT:
			WrappingProtectService_MB wrappingProtectService = new WrappingProtectService_MB();
			wrappingProtectService.setPtnuser(ptnuser);
			wrappingProtectService.setSession(sqlSession);
			wrappingProtectService.setMapper(sqlSession.getMapper(LoopProtectInfoMapper.class));
			return wrappingProtectService;
		case Services.OAMETHERNET:
			OamEthreNetService_MB OamEthreNetService = new OamEthreNetService_MB();
			OamEthreNetService.setPtnuser(ptnuser);
			OamEthreNetService.setSession(sqlSession);
			OamEthreNetService.setMapper(sqlSession.getMapper(OamEthernetInfoMapper.class));
			return OamEthreNetService;
		case Services.QOSMAPPINGTEMPLATESERVICE:
			QosMappingTemplateService_MB qosMappingTemplateService_MB = new QosMappingTemplateService_MB();
			qosMappingTemplateService_MB.setPtnuser(ptnuser);
			qosMappingTemplateService_MB.setSqlSession(sqlSession);
			qosMappingTemplateService_MB.setQosMappingTemplateMapper(sqlSession.getMapper(QosMappingTemplateMapper.class));
			return qosMappingTemplateService_MB;
		case Services.PROTECTRORATE:
			ProtectRorateInfoService_MB protectRorateInfoService_MB = new ProtectRorateInfoService_MB();
			protectRorateInfoService_MB.setPtnuser(ptnuser);
			protectRorateInfoService_MB.setSqlSession(sqlSession);
			protectRorateInfoService_MB.setProtectRorateInfoMapper(sqlSession.getMapper(ProtectRorateInfoMapper.class));
			return protectRorateInfoService_MB;
		case Services.WORKIPSSERVICE:
			WorkIpsService_MB workIpsService_MB = new WorkIpsService_MB();
			workIpsService_MB.setPtnuser(ptnuser);
			workIpsService_MB.setSqlSession(sqlSession);
			workIpsService_MB.setMapper(sqlSession.getMapper(WorkIpsMapper.class));
			return workIpsService_MB;
		case Services.DATACOMPARESERVICE:
			DataCompareService_MB dataCompareService_MB = new DataCompareService_MB();
			dataCompareService_MB.setPtnuser(ptnuser);
			dataCompareService_MB.setSqlSession(sqlSession);		
			return dataCompareService_MB;
		case Services.PROTECT_CORBA:
			ProtectServiceCorba_MB protectServiceCorbaMB = new ProtectServiceCorba_MB();
			protectServiceCorbaMB.setPtnuser(ptnuser);
			protectServiceCorbaMB.setSqlSession(sqlSession);
			protectServiceCorbaMB.setProtectCorbaMapper(sqlSession.getMapper(ProtectCorbaMapper.class));
			return protectServiceCorbaMB;	
		case Services.ARP:
			ARPInfoService_MB arpInfoService = new ARPInfoService_MB();
			arpInfoService.setPtnuser(ptnuser);
			arpInfoService.setSqlSession(sqlSession);
			arpInfoService.setMapper(sqlSession.getMapper(ARPMapper.class));
			return arpInfoService;
		case Services.OSPF_WH:
			Ospf_whService_MB ospfWhServiceMB = new Ospf_whService_MB();
			ospfWhServiceMB.setPtnuser(ptnuser);
			ospfWhServiceMB.setSqlSession(sqlSession);
			ospfWhServiceMB.setFinfoWhMapper(sqlSession.getMapper(OSPFinfo_whMapper.class));
			return ospfWhServiceMB;
		case Services.LOGMANAGER:
			LogManagerService_MB logManagerServiceMB = new LogManagerService_MB();
			logManagerServiceMB.setPtnuser(ptnuser);
			logManagerServiceMB.setSqlSession(sqlSession);
			logManagerServiceMB.setMapper(sqlSession.getMapper(LogManagerMapper.class));
			return logManagerServiceMB;	
		case Services.SYSTEMLOG:
			SystemLogService_MB systemLogServiceMB = new SystemLogService_MB();
			systemLogServiceMB.setPtnuser(ptnuser);
			systemLogServiceMB.setSqlSession(sqlSession);
			systemLogServiceMB.setMapper(sqlSession.getMapper(SystemLogMapper.class));
			return systemLogServiceMB;		
		};
		return null;
	}

	
}
