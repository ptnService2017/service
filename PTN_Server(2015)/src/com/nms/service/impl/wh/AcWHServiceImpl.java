package com.nms.service.impl.wh;

import java.util.ArrayList;
import java.util.List;

import com.nms.db.bean.equipment.port.PortInst;
import com.nms.db.bean.ptn.CccInfo;
import com.nms.db.bean.ptn.path.eth.DualInfo;
import com.nms.db.bean.ptn.path.eth.ElanInfo;
import com.nms.db.bean.ptn.path.eth.ElineInfo;
import com.nms.db.bean.ptn.path.eth.EtreeInfo;
import com.nms.db.bean.ptn.port.AcPortInfo;
import com.nms.db.bean.ptn.port.PortLagInfo;
import com.nms.model.equipment.port.PortService_MB;
import com.nms.model.ptn.CccService_MB;
import com.nms.model.ptn.path.eth.DualInfoService_MB;
import com.nms.model.ptn.path.eth.ElanInfoService_MB;
import com.nms.model.ptn.path.eth.ElineInfoService_MB;
import com.nms.model.ptn.path.eth.EtreeInfoService_MB;
import com.nms.model.ptn.port.AcPortInfoService_MB;
import com.nms.model.ptn.port.PortLagService_MB;
import com.nms.model.util.Services;
import com.nms.service.OperationServiceI;
import com.nms.service.impl.base.WHOperationBase;
import com.nms.service.impl.util.ResultString;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.UiUtil;

public class AcWHServiceImpl extends WHOperationBase implements OperationServiceI {
	@Override
	public String excutionDelete(List objectList) throws Exception {
		AcPortInfoService_MB acService = null;
		List<AcPortInfo> acInfoList = null;
		List<Integer> acIdList = null;
		PortService_MB portService = null;
		PortLagService_MB portLagService = null;
		PortInst portInst = null;
		PortLagInfo portLagInfo = null;
		List<PortLagInfo> portLagInfoList = null;
		try {
		   portLagInfoList = new ArrayList<PortLagInfo>();
		   acInfoList = objectList;
		   acIdList = new ArrayList<Integer>();
		   acService = (AcPortInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.AcInfo);
		   portService = (PortService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORT);
		   portLagService = (PortLagService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORTLAG);
		   for(AcPortInfo acInfo : acInfoList) {
			   acIdList.add(acInfo.getId());   
			   portInst = new PortInst();
			   portInst.setSiteId(acInfo.getSiteId());
			   portInst.setPortId(acInfo.getPortId());
			   portInst = portService.select(portInst).get(0);
			   if(portInst.getLagId()>0){
				   portLagInfo = new PortLagInfo();
				   portLagInfo.setType(0);
				   portLagInfo.setSiteId(acInfo.getSiteId());
				   portLagInfo.setId(portInst.getLagId());
				   portLagInfoList = portLagService.selectLAGByCondition(portLagInfo);
				   if(portLagInfoList != null && portLagInfoList.size()>0){
					portLagInfo = portLagInfoList.get(0);
				   }
				   portLagInfo.setIsUsed(0);
				   portLagService.saveOrUpdate(portLagInfo);
			   }
		   }
		   acService.delete(acIdList);
		   return ResultString.CONFIG_SUCCESS;
		} catch (Exception e) {
			throw e;
		}finally{
			UiUtil.closeService_MB(portService);
			UiUtil.closeService_MB(acService);
			UiUtil.closeService_MB(portLagService);
			portLagInfoList = null;
		}
	}

	@Override
	public String excutionInsert(Object object) throws Exception {
		AcPortInfoService_MB acService = null;
		List<AcPortInfo> acInfos = null;
		try {
			acService = (AcPortInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.AcInfo);
			acInfos = (List<AcPortInfo>) object;
			for(AcPortInfo acInfo:acInfos){
				acService.saveOrUpdate(acInfo.getBufferList(), acInfo);
			}
			return ResultString.CONFIG_SUCCESS;
		} catch (Exception e) {
			throw e;
		}finally{
			UiUtil.closeService_MB(acService);
		}
		
	}
	
	@Override
	public String excutionUpdate(Object object) throws Exception {
		AcPortInfoService_MB acService = null;
		AcPortInfo acInfo = null;
		ElineWHServiceImpl elineWHServiceImpl = null;
		EtreeWHServiceImpl etreeWHServiceImpl = null;
		ElanWHServiceImpl elanWHServiceImpl = null;
		DualWHServceImpl dualWHServceImpl = null;
		CccWHServiceImpl cccWHServceImpl = null;
		ElineInfoService_MB elineService = null;
		EtreeInfoService_MB etreeService = null;
		ElanInfoService_MB elanInfoService = null;
		DualInfoService_MB dualInfoService = null;
		
		CccService_MB cccInfoService = null;
		List<ElineInfo> elineInfos = null;
		List<EtreeInfo> etreeInfos = null;
		List<ElanInfo> elanInfos = null;
		List<CccInfo> cccInfos = null;
		AcPortInfo beforeAcinfo = null;
		List<DualInfo> dualInfos = null;
		String result = "";
		try {
			acService = (AcPortInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.AcInfo);
			acInfo = (AcPortInfo) object;
			beforeAcinfo = new AcPortInfo();
			beforeAcinfo.setId(acInfo.getId());
			beforeAcinfo.setSiteId(acInfo.getSiteId());
			beforeAcinfo = acService.selectByCondition(beforeAcinfo).get(0);//回滚用
			ExceptionManage.infor("acWHserviceImpl--acIsUser1  = "+acInfo.getIsUser(), this.getClass());
			acService.saveOrUpdate(acInfo.getBufferList(), acInfo);
			ExceptionManage.infor("acWHserviceImpl--acIsUser2  = "+acInfo.getIsUser(), this.getClass());
			if(acInfo.getIsUser() == 1){
				elineService = (ElineInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.Eline);
				etreeService = (EtreeInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.EtreeInfo);
				elanInfoService = (ElanInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.ElanInfo);
				dualInfoService = (DualInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.DUALINFO);
				cccInfoService = (CccService_MB) ConstantUtil.serviceFactory.newService_MB(Services.CCCMANAGEMENT);
				dualInfos = dualInfoService.selectByAcIdAndSiteId(acInfo.getId(),acInfo.getSiteId());
				elineInfos = elineService.selectByAcIdAndSiteId(acInfo.getId(),acInfo.getSiteId());
				etreeInfos = etreeService.selectByAcIdAndSiteId(acInfo.getId(),acInfo.getSiteId());
				elanInfos = elanInfoService.selectByAcIdAndSiteId(acInfo.getId(),acInfo.getSiteId());
				cccInfos= cccInfoService.selectByAcIdAndSiteId(acInfo.getId(),acInfo.getSiteId());
				if(elineInfos != null && elineInfos.size()>0){
					elineWHServiceImpl = new ElineWHServiceImpl();
					result = elineWHServiceImpl.excutionUpdate(elineInfos.get(0));
				}else if(etreeInfos != null && etreeInfos.size()>0){
					etreeWHServiceImpl = new EtreeWHServiceImpl();
					result = etreeWHServiceImpl.excutionUpdate(etreeInfos);
				}else if(elanInfos != null && elanInfos.size()>0){
					elanWHServiceImpl = new ElanWHServiceImpl();
					result = elanWHServiceImpl.excutionUpdate(elanInfos);
				}else if(dualInfos != null && dualInfos.size()>0){
					dualWHServceImpl = new DualWHServceImpl();
					result = dualWHServceImpl.excutionUpdate(dualInfos);
				}else if(cccInfos != null && cccInfos.size()>0){
					cccWHServceImpl = new CccWHServiceImpl();
					result = cccWHServceImpl.excutionUpdate(cccInfos.get(0));
				}
			}else{
				result = ResultString.CONFIG_SUCCESS;
			}
			if(ResultString.CONFIG_SUCCESS.equals(result)){//配置成功
				return ResultString.CONFIG_SUCCESS;
			}else{//配置失败，回滚
				acService.saveOrUpdate(beforeAcinfo.getBufferList(), beforeAcinfo);
				return ResultString.CONFIG_TIMEOUT;
			}
		}finally{
			UiUtil.closeService_MB(elanInfoService);
			UiUtil.closeService_MB(etreeService);
			UiUtil.closeService_MB(elineService);
			UiUtil.closeService_MB(acService);
			UiUtil.closeService_MB(dualInfoService);
			UiUtil.closeService_MB(cccInfoService);
			dualWHServceImpl = null;
		}
	}
	
	@Override
	public Object synchro(int siteId) {
		return ResultString.SYNC_FAILED;
	}

}