package com.nms.service.impl.dispatch;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nms.corba.ninterface.util.ELayerRate;
import com.nms.db.bean.ptn.path.pw.MsPwInfo;
import com.nms.db.bean.ptn.path.pw.PwInfo;
import com.nms.db.bean.ptn.path.tunnel.Lsp;
import com.nms.db.bean.ptn.path.tunnel.Tunnel;
import com.nms.db.enums.EActiveStatus;
import com.nms.db.enums.EManufacturer;
import com.nms.db.enums.EObjectType;
import com.nms.model.ptn.path.pw.MsPwInfoService_MB;
import com.nms.model.ptn.path.pw.PwInfoService_MB;
import com.nms.model.ptn.path.tunnel.TunnelService_MB;
import com.nms.model.util.Services;
import com.nms.service.OperationServiceI;
import com.nms.service.impl.base.DispatchBase;
import com.nms.service.impl.cx.TunnelCXServiceImpl;
import com.nms.service.impl.dispatch.rmi.DispatchInterface;
import com.nms.service.impl.util.ResultString;
import com.nms.service.impl.util.SiteUtil;
import com.nms.service.impl.util.WhImplUtil;
import com.nms.service.impl.wh.TunnelWHServiceImpl;
import com.nms.service.notify.Message.MessageType;
import com.nms.ui.manager.BusinessIdException;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.ResourceUtil;
import com.nms.ui.manager.UiUtil;
import com.nms.ui.manager.keys.StringKeysTip;

public class TunnelDispatch extends DispatchBase implements DispatchInterface {

	@SuppressWarnings("unchecked")
	public String excuteDelete(Object object) throws Exception {
		List<Tunnel> tunnelList = null;
		String result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_FAIL);
		OperationServiceI operationServiceI_wh = null;
		OperationServiceI operationServiceI_cx = null;
		String result_wh = null;
		String result_cx = null;
		TunnelService_MB tunnelService = null;
		List<Tunnel> tunnelList_activate = null;
		tunnelList = (List<Tunnel>) object;
		String str = getNotOnlineSiteIdNames(tunnelList);
		try {
			tunnelService = (TunnelService_MB) ConstantUtil.serviceFactory.newService_MB(Services.Tunnel);
			if (tunnelList != null && tunnelList.size() > 0) {
				/** 验证是否在pw中被引用 */
				if (this.pwIsExist(tunnelList)) {
					result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_FAIL_TUNNEL_USE);
				} else {
					// 获取tunnel中已经激活的tunnel
					tunnelList_activate = new ArrayList<Tunnel>();
					for (Tunnel tunnel : tunnelList) {
						if (tunnel.getTunnelStatus() == EActiveStatus.ACTIVITY.getValue()) {
//							if(tunnel.getProtectTunnelId()>0){
//								protectTunnel = new Tunnel();
//								protectTunnel.setTunnelId(tunnel.getProtectTunnelId());
//								protectTunnel = tunnelService.select_nojoin(protectTunnel).get(0);
//								tunnelList_activate.add(protectTunnel);
//							}
							tunnelList_activate.add(tunnel);
						}
					}
				
					if (tunnelList_activate.size() > 0) {
						this.updateStatus(tunnelList_activate, EActiveStatus.UNACTIVITY.getValue());

						// 下发武汉的
						operationServiceI_wh = new TunnelWHServiceImpl();
						result_wh = operationServiceI_wh.excutionDelete(tunnelList_activate);
						// 下发晨晓的
						operationServiceI_cx = new TunnelCXServiceImpl();
						result_cx = operationServiceI_cx.excutionDelete(tunnelList_activate);

						// 如果两次下发都成功。就插入直接返回 否则获取失败消息。 并回滚已经成功的
						if (ResultString.CONFIG_SUCCESS.equals(result_cx) && ResultString.CONFIG_SUCCESS.equals(result_wh)) {
							tunnelService.delete(tunnelList);
							result = ResultString.CONFIG_SUCCESS;
						} else {
							this.updateStatus(tunnelList_activate, EActiveStatus.ACTIVITY.getValue());
							for (Tunnel tunnel : tunnelList_activate) {
								operationServiceI_wh.excutionInsert(tunnel);
							}
							// 如果晨晓的成功 新建晨晓。 错误消息返回武汉的
							if (ResultString.CONFIG_SUCCESS.equals(result_cx)) {
								for (Tunnel tunnel : tunnelList_activate) {
									operationServiceI_cx.excutionInsert(tunnel);
								}
								result = result_wh;
							} else {
								result = result_cx;
							}
						}
					} else {
						tunnelService = (TunnelService_MB) ConstantUtil.serviceFactory.newService_MB(Services.Tunnel);
						tunnelService.delete(tunnelList);
						result = ResultString.CONFIG_SUCCESS;
					}
				}
			} else {
				throw new Exception("objectList is null");
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			UiUtil.closeService_MB(tunnelService);
		}
		if (ResultString.CONFIG_SUCCESS.equals(result)) {
			WhImplUtil whImplUtil = new WhImplUtil();
			for(Tunnel tunnel : tunnelList){
				whImplUtil.deleteAlarmAndPerformance(EObjectType.TUNNEL, tunnel.getLspParticularList().get(0).getAtunnelbusinessid(),tunnel.getaSiteId());
				whImplUtil.deleteAlarmAndPerformance(EObjectType.TUNNEL, tunnel.getLspParticularList().get(tunnel.getLspParticularList().size()-1).getZtunnelbusinessid(),tunnel.getzSiteId());
				if(tunnel.getProtectTunnel() != null){
					whImplUtil.deleteAlarmAndPerformance(EObjectType.TUNNEL, tunnel.getProtectTunnel().getLspParticularList().get(0).getAtunnelbusinessid(),tunnel.getProtectTunnel().getaSiteId());
					whImplUtil.deleteAlarmAndPerformance(EObjectType.TUNNEL, tunnel.getProtectTunnel().getLspParticularList().get(tunnel.getProtectTunnel().getLspParticularList().size()-1).getZtunnelbusinessid(),tunnel.getProtectTunnel().getzSiteId());
					whImplUtil.deleteAlarmAndPerformance(EObjectType.LSP, tunnel.getAprotectId(),tunnel.getaSiteId());
					whImplUtil.deleteAlarmAndPerformance(EObjectType.LSP, tunnel.getZprotectId(),tunnel.getzSiteId());
				}
			}
			//成功上报操作信息到caoba
			for (Tunnel tunnel : tunnelList) {
				if (tunnel.getIsSingle()>0) {//单网元
					super.notifyCorba("CrossConnection", MessageType.DELETION, tunnel, ELayerRate.TUNNEL.getValue()+"",result);
				}else {//端到端
					super.notifyCorba("SubnetworkConnection", MessageType.DELETION, tunnel, ELayerRate.TUNNEL.getValue()+"",result);
				}
			}
			if(str.equals("")){
				return ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}else{
				return ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS)+","+str+ResultString.NOT_ONLINE_SUCCESS;
			}
		} else {
			return result;
		}
	}

	@SuppressWarnings("unchecked")
	public String excuteInsert(Object object) throws Exception {
		List<Tunnel> tunnelList = null;
//		Tunnel tunnel = null;
		String result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_FAIL);
		TunnelService_MB tunnelService = null;
		OperationServiceI operationServiceI_wh = null;
//		OperationServiceI operationServiceI_cx = null;
		String result_wh = null;
		String result_cx = ResultString.CONFIG_SUCCESS;
		List<Tunnel> tunnels = new ArrayList<Tunnel>();
		try {
			tunnelService = (TunnelService_MB) ConstantUtil.serviceFactory.newService_MB(Services.Tunnel);
			tunnelList = (List<Tunnel>) object;
		
			//先入库
			for (Tunnel t : tunnelList) {
				Tunnel tunnel = this.saveTunnel(t);
				tunnels.add(tunnel);
			}
			// 如果是激活状态 就下发到设备
			if (tunnelList.get(0).getTunnelStatus() == EActiveStatus.ACTIVITY.getValue()) {
//				if(!tunnel.isDataDownLoad()){
//					tunnel = this.saveTunnel(tunnel);
//				}
				// 下发武汉的
				operationServiceI_wh = new TunnelWHServiceImpl();
				result_wh = operationServiceI_wh.excutionInsert(tunnelList);
				// 下发晨晓的
//				operationServiceI_cx = new TunnelCXServiceImpl();
//				result_cx =operationServiceI_cx.excutionInsert(tunnelList);

				// 如果两次下发都成功。就插入直接返回 否则获取失败消息。 并回滚已经成功的
				if (ResultString.CONFIG_SUCCESS.equals(result_cx) && ResultString.CONFIG_SUCCESS.equals(result_wh)) {
					result = ResultString.CONFIG_SUCCESS;
				} else {
					for (Tunnel tunnel : tunnels) {
						tunnel.setTunnelStatus(EActiveStatus.UNACTIVITY.getValue());
						tunnelService.update(tunnel);
					}
					// 删除晨晓和武汉的数据
					operationServiceI_wh.excutionDelete(tunnels);
//					if(!tunnel.isDataDownLoad()){
						// 修改状态为未激活
//						tunnel.setTunnelStatus(EActiveStatus.UNACTIVITY.getValue());
//						tunnelService.update(tunnel);
//
//						tunnels = new ArrayList<Tunnel>();
//						tunnels.add(tunnel);
//						// 删除晨晓和武汉的数据
//						operationServiceI_wh.excutionDelete(tunnels);
//					}
					// 如果晨晓的成功。 错误消息返回武汉的
//					if (ResultString.CONFIG_SUCCESS.equals(result_cx)) {
//						operationServiceI_cx.excutionDelete(tunnels);
//						result = result_wh;
//					} else {
//						result = result_cx;
//					}
				}
			} else {
				result = ResultString.CONFIG_SUCCESS;
			}
			//成功上报操作信息到corba
//			for (Tunnel t : tunnelList) {
//				String type = "CrossConnection";//单网元
//				if (t.getIsSingle() == 0) {
//					//端到端
//					type = "SubnetworkConnection";
//				}
//				super.notifyCorba(type, MessageType.CREATION, t, ELayerRate.TUNNEL.getValue()+"",result);
//			}
		} catch (Exception e) {
			for (Tunnel tunnel : tunnels) {
				tunnel.setTunnelStatus(EActiveStatus.UNACTIVITY.getValue());
				tunnelService.update(tunnel);
			}
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			UiUtil.closeService_MB(tunnelService);
		}
		if (ResultString.CONFIG_SUCCESS.equals(result)) {
			String str = this.getNotOnlineSiteIdNames(tunnelList);
			if(str.equals("")){
				return ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}else{
				return ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS)+","+str+ResultString.NOT_ONLINE_SUCCESS;
			}
		} else {
			return result;
		}
	}

	public String excuteUpdate(Object object) throws Exception {
		Tunnel tunnel = null;
		Tunnel beforeTunnel = null;
		String result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_FAIL);
		TunnelService_MB tunnelService = null;
		OperationServiceI operationServiceI_wh = null;
		OperationServiceI operationServiceI_cx = null;
		String result_wh = null;
		String result_cx = null;
		try {
			tunnelService = (TunnelService_MB) ConstantUtil.serviceFactory.newService_MB(Services.Tunnel);
			tunnel = (Tunnel) object;
			
			// 保存修改以前的对象，失败后回滚用
			beforeTunnel = new Tunnel();
			beforeTunnel.setTunnelId(tunnel.getTunnelId());
			beforeTunnel = tunnelService.select_nojoin(beforeTunnel).get(0);

			// 两次状态都是为非激活，则不用下设备
			if (beforeTunnel.getTunnelStatus() == EActiveStatus.UNACTIVITY.getValue() && tunnel.getTunnelStatus() == EActiveStatus.UNACTIVITY.getValue()) {
				tunnelService.update(tunnel);
				result = ResultString.CONFIG_SUCCESS;
			} else {

				// 去激活操作要作校验
				if (tunnel.getTunnelStatus() == EActiveStatus.UNACTIVITY.getValue() && beUsedByActivePw(tunnel)) {
					result = ResourceUtil.srcStr(StringKeysTip.TIP_ISUSEDBYPW_UNACTIVE);
				} else {
					// 先修改数据库
					tunnelService.update(tunnel);
					// 把之前的激活状态给要修改的tunnel对象
					tunnel.setBefore_activity(beforeTunnel.getTunnelStatus());
					// 下发武汉的
					operationServiceI_wh = new TunnelWHServiceImpl();
					result_wh = operationServiceI_wh.excutionUpdate(tunnel);
					// 下发晨晓的
					operationServiceI_cx = new TunnelCXServiceImpl();
					result_cx = operationServiceI_cx.excutionUpdate(tunnel);
					// 如果两次下发都成功。直接返回 否则获取失败消息。 并回滚已经成功的
					if (ResultString.CONFIG_SUCCESS.equals(result_cx) && ResultString.CONFIG_SUCCESS.equals(result_wh)) {
						result = ResultString.CONFIG_SUCCESS;
					} else {
						// 回滚数据库
						tunnelService.update(beforeTunnel);
						// 修改成下发之前的配置
						operationServiceI_wh.excutionUpdate(beforeTunnel);
						operationServiceI_cx.excutionUpdate(beforeTunnel);
						// 如果晨晓的成功 修改晨晓。 错误消息返回武汉的
						if (ResultString.CONFIG_SUCCESS.equals(result_cx)) {
							result = result_wh;
						} else {
							result = result_cx;
						}
					}
				}
			}
			//如果修改的字段被修改，则上报消息通知
			reportMsg(tunnel, beforeTunnel ,result);
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			beforeTunnel = null;
			UiUtil.closeService_MB(tunnelService);
			operationServiceI_wh = null;
			operationServiceI_cx = null;
			result_wh = null;
			result_cx = null;
		}
		if (ResultString.CONFIG_SUCCESS.equals(result)) {
			List<Tunnel> list = new ArrayList<Tunnel>();
			list.add(tunnel);
			String str = getNotOnlineSiteIdNames(list);
			if(str.equals("")){
				return ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
			}else{
				return ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS)+","+str+ResultString.NOT_ONLINE_SUCCESS;
			}
		} else {
			return result;
		}
	}

	/**
	 * 保存tunnel（入库）
	 * 
	 * @param tunnel
	 * @return
	 * @throws Exception
	 */
	private Tunnel saveTunnel(Tunnel tunnel) throws Exception,BusinessIdException  {
		Tunnel tunnelResult = null;
		TunnelService_MB tunnelService = null;
		try {
			tunnelService = (TunnelService_MB) ConstantUtil.serviceFactory.newService_MB(Services.Tunnel);
			tunnelService.save(tunnel);
			int tunnelId = tunnel.getTunnelId();
			if (tunnel.getIsSingle() == 0) {
				tunnelResult = new Tunnel();
				tunnelResult.setTunnelId(tunnelId);
				tunnelResult = tunnelService.select(tunnelResult).get(0);
			} else {
				tunnel.setTunnelId(tunnelId);
				tunnelResult = tunnel;
			}
		} catch (BusinessIdException e) {
			throw e;
		}catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}finally{
			UiUtil.closeService_MB(tunnelService);
		}
		return tunnelResult;
	}

	/**
	 * 删除时验证pw中是否引用
	 * 
	 * @return
	 * @throws Exception
	 */
	private boolean pwIsExist(List<Tunnel> tunnelList) throws Exception {

		boolean flag = false;
		List<Integer> tunnelIds = null;
		PwInfoService_MB pwInfoService = null;
		MsPwInfoService_MB msPwInfoService = null;
		List<MsPwInfo> msPwInfos = null;
		try {
			tunnelIds = new ArrayList<Integer>();
			for (Tunnel tunnel : tunnelList) {
				tunnelIds.add(tunnel.getTunnelId());
			}

			pwInfoService = (PwInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PwInfo);
			msPwInfoService = (MsPwInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.MSPWSERVICE);
			int count = pwInfoService.selectPwInfoByTunnelId(tunnelIds).size();
			if (count > 0) {
				flag = true;
			}
			msPwInfos = msPwInfoService.selectByTunnelIds(tunnelIds);
			if(msPwInfos.size()>0){
				flag = true;
			}
			return flag;
		} catch (Exception e) {
			throw e;
		} finally {
			tunnelIds = null;
			UiUtil.closeService_MB(pwInfoService);
			UiUtil.closeService_MB(msPwInfoService);
		}
	}

	/**
	 * 修改tunnel状态
	 * 
	 * @param tunnelList
	 *            tunnel集合
	 * @param status
	 *            2为修改成删除 1为修改成激活
	 * @throws Exception
	 */
	private void updateStatus(List<Tunnel> tunnelList, int status) throws Exception {
		List<Integer> tunnelIds = null;
		TunnelService_MB tunnelService = null;

		try {
			tunnelIds = new ArrayList<Integer>();
			for (Tunnel tunnel : tunnelList) {
				tunnelIds.add(tunnel.getTunnelId());
				if(tunnel.getProtectTunnelId()>0){
					tunnelIds.add(tunnel.getProtectTunnelId());
				}
			}

			tunnelService = (TunnelService_MB) ConstantUtil.serviceFactory.newService_MB(Services.Tunnel);

			if (EActiveStatus.ACTIVITY.getValue() == status) {
				tunnelService.updateStatusActivate(tunnelIds,EActiveStatus.ACTIVITY.getValue());
			} else if (EActiveStatus.UNACTIVITY.getValue() == status) {
				tunnelService.updateStatusActivate(tunnelIds,EActiveStatus.UNACTIVITY.getValue());
			} else {
				throw new Exception("status is error");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			tunnelIds = null;
			UiUtil.closeService_MB(tunnelService);
		}
	}

	/**
	 * 
	 * 
	 * beUsedByActivePw(判断此tunnel所关联的pw是否为激活状态)
	 * 
	 * @author kk
	 * 
	 * @param
	 * 
	 * @return 已激活 true 未激活false
	 * 
	 * @Exception 异常对象
	 */
	private boolean beUsedByActivePw(Tunnel beforeTunnel) throws Exception {

		PwInfoService_MB pwInfoService = null;
		List<PwInfo> pwList = null;
		List<Integer> tunnelIds = null;
		MsPwInfoService_MB msPwInfoService = null;
		List<MsPwInfo> msPwInfos = null;
		try {
			pwInfoService = (PwInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PwInfo);
			msPwInfoService = (MsPwInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.MSPWSERVICE);
			tunnelIds = new ArrayList<Integer>();
			tunnelIds.add(beforeTunnel.getTunnelId());
			pwList = pwInfoService.selectPwInfoByTunnelId(tunnelIds);
			for(PwInfo pwInfo : pwList){
				if(pwInfo.getPwStatus() == EActiveStatus.ACTIVITY.getValue()){
					return true;
				}
			}
			msPwInfos = msPwInfoService.selectByTunnelIds(tunnelIds);
			for(MsPwInfo msPwInfo : msPwInfos){
				PwInfo pwInfo  = pwInfoService.selectByPwId(msPwInfo.getPwId());
				if(pwInfo != null && pwInfo.getPwStatus() == EActiveStatus.ACTIVITY.getValue()){
					return true;
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			UiUtil.closeService_MB(pwInfoService);
			UiUtil.closeService_MB(msPwInfoService);
			pwList = null;
			tunnelIds = null;
		}
		return false;
	}

	/**
	 * synchro(根据网元id同步)
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
	public String synchro(int siteId) throws Exception {
		OperationServiceI operationServiceI = null;
		try {
			//虚拟网元不同步设备
			SiteUtil siteUtil=new SiteUtil();
			if(0==siteUtil.SiteTypeUtil(siteId)){
				if (super.getManufacturer(siteId) == EManufacturer.WUHAN.getValue()) {
					operationServiceI = new TunnelWHServiceImpl();
				} else {
					operationServiceI = new TunnelCXServiceImpl();
				}
				return (String)operationServiceI.synchro(siteId);
			}
		} catch (Exception e) {
			throw e;
		}
		return ResultString.QUERY_FAILED;
	}

	/**
	 * 判断是否符合修改上报
	 * @param tunnel 要修改的tunnel
	 * @param beforeTunnel 原有的tunnel
	 * @param result 是否上报标识
	 * @throws Exception 
	 */
	private void reportMsg(Tunnel tunnel,Tunnel beforeTunnel,String result) throws Exception{
		Map<String, String> attributeMap = new HashMap<String, String>();//上报的修改的属性集合
		Map<String, String> stateMap = new HashMap<String, String>();//上报的状态的属性集合
		if (null != tunnel.getTunnelName() && null != beforeTunnel.getTunnelName() && !tunnel.getTunnelName().equals(beforeTunnel.getTunnelName())) {
			attributeMap.put("userLabel", tunnel.getTunnelName());
		}
		if (tunnel.getTunnelStatus() != beforeTunnel.getTunnelStatus()) {
			stateMap.put("status", tunnel.getTunnelStatus()+"");
		}
		//成功上报操作信息到caoba
		try {
			//属性修改上报
			if (attributeMap.size()>0) {
				attributeMap.put("id", tunnel.getTunnelId()+"");
				if (tunnel.getIsSingle()>0) {//单网元
					if (tunnel.getASiteId()>0) {
						attributeMap.put("siteId", tunnel.getASiteId()+"");
					}else if (tunnel.getZSiteId()>0) {
						attributeMap.put("siteId", tunnel.getZSiteId()+"");
					}
					super.notifyCorba("CrossConnection", MessageType.ATTRIBUTECHG, attributeMap, ELayerRate.TUNNEL.getValue()+"",result);
				}else {//端到端
					super.notifyCorba("SubnetworkConnection", MessageType.ATTRIBUTECHG , attributeMap, ELayerRate.TUNNEL.getValue()+"",result);
				}
			}
			//状态修改上报
			if (stateMap.size()>0) {
				stateMap.put("id", tunnel.getTunnelId()+"");
				if (tunnel.getIsSingle()>0) {//单网元
					if (tunnel.getASiteId()>0) {
						stateMap.put("siteId", tunnel.getASiteId()+"");
					}else if (tunnel.getZSiteId()>0) {
						stateMap.put("siteId", tunnel.getZSiteId()+"");
					}
					super.notifyCorba("CrossConnection", MessageType.STATECHG, stateMap, ELayerRate.TUNNEL.getValue()+"",result);
				}else {//端到端
					super.notifyCorba("SubnetworkConnection", MessageType.STATECHG , stateMap, ELayerRate.TUNNEL.getValue()+"",result);
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	
	/**
	 * tunnel一致性检测
	 * 
	 */
	@Override
	public Object consistence(int siteId) throws RemoteException, Exception {
		List<Tunnel> tunnels = null;
		TunnelWHServiceImpl tunnelWHServiceImpl = new TunnelWHServiceImpl();
		tunnels = tunnelWHServiceImpl.consistence(siteId);
		return tunnels;
	}
	
	private String getNotOnlineSiteIdNames(List<Tunnel> tunnelList) throws Exception {
		List<Integer> siteIds = null;
		TunnelService_MB tunnelService = null;
		Tunnel protectTunnel = null;
		String str = "";
		try {
			siteIds = new ArrayList<Integer>();
			tunnelService = (TunnelService_MB) ConstantUtil.serviceFactory.newService_MB(Services.Tunnel);
			for (Tunnel tunnel : tunnelList) {
				if (tunnel.getProtectTunnelId() > 0) {
					protectTunnel = new Tunnel();
					protectTunnel.setTunnelId(tunnel.getProtectTunnelId());
					protectTunnel = tunnelService.select_nojoin(protectTunnel).get(0);
					for (Lsp lspParticular : protectTunnel.getLspParticularList()) {
						if (lspParticular.getASiteId() > 0) {
							if (!siteIds.contains(lspParticular.getASiteId()) && super.getManufacturer(lspParticular.getASiteId()) == EManufacturer.WUHAN.getValue()) {
								siteIds.add(lspParticular.getASiteId());
							}
						}
						if (lspParticular.getZSiteId() > 0) {
							if (!siteIds.contains(lspParticular.getZSiteId()) && lspParticular.getZPortId() > 0 && super.getManufacturer(lspParticular.getZSiteId()) == EManufacturer.WUHAN.getValue()) {
								siteIds.add(lspParticular.getZSiteId());
							}
						}
					}

				}

				for (Lsp lspParticular : tunnel.getLspParticularList()) {
					if (lspParticular.getASiteId() > 0) {
						if (!siteIds.contains(lspParticular.getASiteId()) && super.getManufacturer(lspParticular.getASiteId()) == EManufacturer.WUHAN.getValue()) {
							siteIds.add(lspParticular.getASiteId());
						}
					}
					if (lspParticular.getZSiteId() > 0) {
						if (!siteIds.contains(lspParticular.getZSiteId()) && lspParticular.getZPortId() > 0 && super.getManufacturer(lspParticular.getZSiteId()) == EManufacturer.WUHAN.getValue()) {
							siteIds.add(lspParticular.getZSiteId());
						}
					}
				}
			}

		} catch (Exception e) {
			throw e;
		} finally {
			UiUtil.closeService_MB(tunnelService);
		}
		WhImplUtil whImplUtil = new WhImplUtil();
		str = whImplUtil.getNeNames(siteIds);
		return str;
	}
	
}
