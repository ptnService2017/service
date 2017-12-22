﻿package com.nms.ui.manager;

import com.nms.db.enums.EServiceType;
import com.nms.model.client.ClientService_MB;
import com.nms.model.path.NameRuleService_MB;
import com.nms.model.path.SegmentService_MB;
import com.nms.model.perform.PerformanceTaskService_MB;
import com.nms.model.ptn.CccService_MB;
import com.nms.model.ptn.path.ces.CesInfoService_MB;
import com.nms.model.ptn.path.eth.DualInfoService_MB;
import com.nms.model.ptn.path.eth.ElanInfoService_MB;
import com.nms.model.ptn.path.eth.ElineInfoService_MB;
import com.nms.model.ptn.path.eth.EtreeInfoService_MB;
import com.nms.model.ptn.path.protect.WrappingProtectService_MB;
import com.nms.model.ptn.path.pw.PwInfoService_MB;
import com.nms.model.ptn.path.tunnel.TunnelService_MB;
import com.nms.model.ptn.port.AcPortInfoService_MB;
import com.nms.model.system.SubnetService_MB;
import com.nms.model.util.Services;

public class VerifyNameUtil {

	/**
	 * 
	 * 验证名称是否重复
	 * 
	 * @author kk
	 * 
	 * @param type
	 *            类型，对应EServiceType枚举类
	 * @param afterName
	 *            修改之后的名称（如果是新建，就给新建的名称）
	 * @param beforeName
	 *            修改之前的名称(如果是新建，给NULL)
	 * 
	 * @return
	 * 
	 * @Exception 异常对象
	 */
	public boolean verifyName(int type, String afterName, String beforeName) {
		boolean flag = true;
		SegmentService_MB segmentService = null;
		TunnelService_MB tunnelService = null;
		PwInfoService_MB pwInfoService  = null;
		ElineInfoService_MB elineService = null;
		CesInfoService_MB cesInfoService = null;
		EtreeInfoService_MB etreeService = null;
		ElanInfoService_MB elanInfoService = null;
		ClientService_MB clientService = null;
		WrappingProtectService_MB wrappingProtectService = null;
		PerformanceTaskService_MB taskService = null;
//		QinQInstService qinqService = null;
		DualInfoService_MB dualService = null;
		try {
			if(beforeName != null && afterName.equals(beforeName)){
				//beforeName不为null，说明是修改，如果两次名称相同，则无需查库验证
				flag = false;
			}else{
				if (type == EServiceType.ACPORT.getValue()) {
//					AcInfoService acInfoService = (AcInfoService) ConstantUtil.serviceFactory.newService(Services.AcInfo);
//					flag = acInfoService.nameRepetition(afterName, beforeName);
				} else if (type == EServiceType.TUNNEL.getValue()) {
					tunnelService = (TunnelService_MB) ConstantUtil.serviceFactory.newService_MB(Services.Tunnel);
					flag = tunnelService.nameRepetition(afterName, beforeName,0);
				} else if (type == EServiceType.PW.getValue()) {
					pwInfoService = (PwInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PwInfo);
					flag = pwInfoService.nameRepetition(afterName, beforeName);
				} else if (type == EServiceType.ELINE.getValue()) {
					elineService = (ElineInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.Eline);
					flag = elineService.nameRepetition(afterName, beforeName,0);
				} else if (type == EServiceType.CES.getValue()) {
					cesInfoService = (CesInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.CesInfo);
					flag = cesInfoService.nameRepetition(afterName, beforeName,0);
				}else if (type == EServiceType.ETREE.getValue()) {
					etreeService = (EtreeInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.EtreeInfo);
					flag = etreeService.nameRepetition(afterName, beforeName);
				}else if (type == EServiceType.ELAN.getValue()) {
					elanInfoService = (ElanInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.ElanInfo);
					flag = elanInfoService.nameRepetition(afterName, beforeName);
				}else if (type == EServiceType.CLIENT.getValue()) {
					clientService = (ClientService_MB) ConstantUtil.serviceFactory.newService_MB(Services.CLIENTSERVICE);
					flag = clientService.nameRepetition(afterName, beforeName);
				}else if (type == EServiceType.SECTION.getValue()) {
					segmentService = (SegmentService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SEGMENT);
					flag = segmentService.nameRepetition(afterName, beforeName);
				}else if (type == EServiceType.WRAPPINGPROTECT.getValue()) {
					wrappingProtectService = (WrappingProtectService_MB) ConstantUtil.serviceFactory.newService_MB(Services.WRAPPINGPROTECT);;
				    flag = wrappingProtectService.nameRepetition(afterName, beforeName);
			    }else if (type == EServiceType.PERFORMANCETASK.getValue()) {
				    taskService = (PerformanceTaskService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PerformanceTask);
				    flag = taskService.nameRepetition(afterName, beforeName);
//			    }else if (type == EServiceType.QINQ.getValue()) {
//			    	qinqService = (QinQInstService) ConstantUtil.serviceFactory.newService(Services.QinQ);
//			    	flag = qinqService.nameRepetition(afterName, beforeName);
			    }else if (type == EServiceType.DUAL.getValue()) {
			    	dualService = (DualInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.DUALINFO);
			    	flag = dualService.nameRepetition(afterName, beforeName);
			    }
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,VerifyNameUtil.class);
		}finally{
			UiUtil.closeService_MB(tunnelService);
			UiUtil.closeService_MB(pwInfoService);
			UiUtil.closeService_MB(elineService);
			UiUtil.closeService_MB(cesInfoService);
			UiUtil.closeService_MB(etreeService);
			UiUtil.closeService_MB(elanInfoService);
			UiUtil.closeService_MB(clientService);
			UiUtil.closeService_MB(segmentService);
			UiUtil.closeService_MB(wrappingProtectService);
			UiUtil.closeService_MB(taskService);
//			UiUtil.closeService(qinqService);
			UiUtil.closeService_MB(dualService);
		}
		return flag;
	}
	
	/**
	 * 单网元名称验证
	 * @param type
	 * @param afterName
	 * @param beforeName
	 * @param siteId
	 * @return
	 */
	public boolean verifyNameBySingle(int type, String afterName, String beforeName,int siteId) {
		boolean flag = true;
		AcPortInfoService_MB acInfoService = null;
		TunnelService_MB tunnelService = null;
		PwInfoService_MB pwInfoService = null;
		ElineInfoService_MB elineService = null;
		CesInfoService_MB cesInfoService = null;
		EtreeInfoService_MB etreeService = null;
		ElanInfoService_MB elanInfoService = null;
		SubnetService_MB subnetService = null;
		NameRuleService_MB nameService=null;
		CccService_MB cccService=null;
		try {
			if(beforeName != null && afterName.equals(beforeName)){
				//beforeName不为null，说明是修改，如果两次名称相同，则无需查库验证
				flag = false;
			}else{
				if (type == EServiceType.ACPORT.getValue()) {
					acInfoService = (AcPortInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.AcInfo);
					flag = acInfoService.nameRepetitionBySingle(afterName, beforeName,siteId);
				} else if (type == EServiceType.TUNNEL.getValue()) {
					tunnelService = (TunnelService_MB) ConstantUtil.serviceFactory.newService_MB(Services.Tunnel);
					flag = tunnelService.nameRepetition(afterName, beforeName,siteId);
				} else if (type == EServiceType.PW.getValue()) {
					pwInfoService = (PwInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PwInfo);
					flag = pwInfoService.nameRepetitionBySingle(afterName, beforeName,siteId);
				} else if (type == EServiceType.ELINE.getValue()) {
					elineService = (ElineInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.Eline);
					flag = elineService.nameRepetition(afterName, beforeName,siteId);
				} else if (type == EServiceType.CES.getValue()) {
					cesInfoService = (CesInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.CesInfo);
					flag = cesInfoService.nameRepetition(afterName, beforeName,siteId);
				}else if (type == EServiceType.ETREE.getValue()) {
					etreeService = (EtreeInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.EtreeInfo);
					flag = etreeService.nameRepetitionBySingle(afterName, beforeName,siteId);
				}else if (type == EServiceType.ELAN.getValue()) {
					elanInfoService = (ElanInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.ElanInfo);
					flag = elanInfoService.nameRepetitionBySingle(afterName, beforeName,siteId);
				}else if (type == EServiceType.SUBNET.getValue()) {
					subnetService = (SubnetService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SUBNETSERVICE);
					flag = subnetService.nameRepetition(afterName, beforeName,siteId);
//				}else if (type == EServiceType.CLIENT.getValue()) {
//					ClientService clientService = (ClientService) ConstantUtil.serviceFactory.newService(Services.CLIENTSERVICE);
//					flag = clientService.nameRepetition(afterName, beforeName);
				}else if (type == EServiceType.RULENAME.getValue()) {
					nameService = (NameRuleService_MB) ConstantUtil.serviceFactory.newService_MB(Services.NAMERULESERVICE);
					flag = nameService.nameRepetition(afterName, beforeName,siteId);
				}else if (type == EServiceType.CCC.getValue()) {
					cccService = (CccService_MB) ConstantUtil.serviceFactory.newService_MB(Services.CCCMANAGEMENT);
					flag = cccService.nameRepetitionBySingle(afterName, beforeName,siteId);
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,VerifyNameUtil.class);
		}finally{
			UiUtil.closeService_MB(acInfoService);
			UiUtil.closeService_MB(tunnelService);
			UiUtil.closeService_MB(pwInfoService);
			UiUtil.closeService_MB(elineService);
			UiUtil.closeService_MB(cesInfoService);
			UiUtil.closeService_MB(etreeService);
			UiUtil.closeService_MB(elanInfoService);
			UiUtil.closeService_MB(subnetService);
			UiUtil.closeService_MB(cccService);
		}
		return flag;
	}

}
