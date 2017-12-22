package com.nms.service.impl.dispatch;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import com.nms.db.bean.ptn.ARPInfo;
import com.nms.db.enums.EManufacturer;
import com.nms.model.ptn.ARPInfoService_MB;
import com.nms.model.util.Services;
import com.nms.service.OperationServiceI;
import com.nms.service.impl.base.DispatchBase;
import com.nms.service.impl.dispatch.rmi.DispatchInterface;
import com.nms.service.impl.util.ResultString;
import com.nms.service.impl.util.SiteUtil;
import com.nms.service.impl.util.TypeAndActionUtil;
import com.nms.service.impl.util.WhImplUtil;
import com.nms.service.impl.wh.ArpWHServiceImpl;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.ResourceUtil;
import com.nms.ui.manager.UiUtil;
import com.nms.ui.manager.keys.StringKeysTip;

public class ARPDispatch extends DispatchBase implements DispatchInterface {
	@SuppressWarnings("unchecked")
	@Override
	public String excuteDelete(Object object) throws RemoteException, Exception {
		String result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_FAIL);
		List<ARPInfo> arpList = null;
		OperationServiceI operationServiceI = null;
		ARPInfoService_MB service = null;
		try {
			if (object != null ) {
				arpList = (List<ARPInfo>) object;				
				//虚拟网元操作
				SiteUtil siteUtil = new SiteUtil();
				String siteCheck = (String) siteUtil.irtualSiteAction(TypeAndActionUtil.ACTION_DELETE, arpList);
				if(null != siteCheck){
					return siteCheck;
				}
				//通过网元ID判断设备类型
				if(null != arpList && arpList.size() > 0){
					int manufacturer = super.getManufacturer(arpList.get(0).getSiteId());
					if (manufacturer == EManufacturer.WUHAN.getValue()) {
						service = (ARPInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.ARP);
						//先删除数据库
						service.delete(arpList);
						//武汉设备转换
						operationServiceI = new ArpWHServiceImpl();
						result = operationServiceI.excutionDelete(arpList);
						if (ResultString.CONFIG_SUCCESS.equals(result)) {
							result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
						}else{
							//失败，则回滚
							for (ARPInfo arpInfo : arpList) {
								service.insert(arpInfo);
							}
						}
					}
				} 
			}else {
				throw new Exception("arpList is null");
			}
			if (ResultString.CONFIG_SUCCESS.equals(result)) {		
				String str = this.getOfflineSiteIdNames(arpList.get(0).getSiteId());
				if(str.equals("")){
					return ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
				}else{
					return ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS)+","+str+ResultString.NOT_ONLINE_SUCCESS;
				}
			} else {
				return result;
			}		
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			UiUtil.closeService_MB(service);
		}
		return result;
	}

	@Override
	public String excuteInsert(Object object) throws RemoteException, Exception {
		String result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_FAIL);
		ARPInfo arp = null;
		OperationServiceI operationServiceI = null;
		ARPInfoService_MB service = null;
		try {
			if (object != null ) {
				arp = (ARPInfo) object;				
				//虚拟网元操作
				SiteUtil siteUtil = new SiteUtil();
				String siteCheck = (String) siteUtil.irtualSiteAction(TypeAndActionUtil.ACTION_INSERT, arp);
				if(null != siteCheck){
					return siteCheck;
				}
				//通过网元ID判断设备类型
				int manufacturer = super.getManufacturer(arp.getSiteId());
				if (manufacturer == EManufacturer.WUHAN.getValue()) {
					//先入库
					service = (ARPInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.ARP);
					service.insert(arp);
					//武汉设备转换
					operationServiceI = new ArpWHServiceImpl();
					result = operationServiceI.excutionInsert(arp);
					if (ResultString.CONFIG_SUCCESS.equals(result)) {
						result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
					}else{
						List<ARPInfo> arpList = new ArrayList<ARPInfo>();
						arpList.add(arp);
						service.delete(arpList);
					}
				}
			}else {
				throw new Exception("arp is null");
			}
			if (ResultString.CONFIG_SUCCESS.equals(result)) {		
				String str = this.getOfflineSiteIdNames(arp.getSiteId());
				if(str.equals("")){
					return ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
				}else{
					return ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS)+","+str+ResultString.NOT_ONLINE_SUCCESS;
				}
			} else {
				return result;
			}		
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			UiUtil.closeService_MB(service);
		}
		return result;
	}

	@Override
	public String excuteUpdate(Object object) throws RemoteException, Exception {
		String result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_FAIL);
		ARPInfo arp = null;
		OperationServiceI operationServiceI = null;
		ARPInfoService_MB service = null;
		try {
			if (object != null ) {
				arp = (ARPInfo) object;				
				//虚拟网元操作
				SiteUtil siteUtil = new SiteUtil();
				String siteCheck = (String) siteUtil.irtualSiteAction(TypeAndActionUtil.ACTION_UPDATE, arp);
				if(null != siteCheck){
					return siteCheck;
				}
				//通过网元ID判断设备类型
				int manufacturer = super.getManufacturer(arp.getSiteId());
				if (manufacturer == EManufacturer.WUHAN.getValue()) {
					service = (ARPInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.ARP);
					//先把旧数据保存
					ARPInfo arpInfo_before = service.queryById(arp.getId());
					service.update(arp);
					//武汉设备转换
					operationServiceI = new ArpWHServiceImpl();
					result = operationServiceI.excutionUpdate(arp);
					if (ResultString.CONFIG_SUCCESS.equals(result)) {
						result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
					}else{
						service.update(arpInfo_before);
					}
				}
			}else {
				throw new Exception("arp is null");
			}
			if (ResultString.CONFIG_SUCCESS.equals(result)) {		
				String str = this.getOfflineSiteIdNames(arp.getSiteId());
				if(str.equals("")){
					return ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
				}else{
					return ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS)+","+str+ResultString.NOT_ONLINE_SUCCESS;
				}
			} else {
				return result;
			}		
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			UiUtil.closeService_MB(service);
		}
		return result;
	}

	@Override
	public String synchro(int siteId) throws RemoteException, Exception {
		try {
			SiteUtil siteUtil = new SiteUtil();
			if(0 == siteUtil.SiteTypeUtil(siteId)){
				if (super.getManufacturer(siteId) == EManufacturer.WUHAN.getValue()) {
					OperationServiceI operationServiceI = new ArpWHServiceImpl();
					return (String)operationServiceI.synchro(siteId);
				}
			}
		} catch (Exception e) {
			throw e;
		}
		
		return ResultString.QUERY_FAILED; 
	}

	@Override
	public Object consistence(int siteId) throws RemoteException, Exception {
		return null;
	}

	/**
	 * 获取离线网元名称
	 */
	private String getOfflineSiteIdNames(int siteId) throws Exception {
		String str = "";
		try {
			List<Integer> siteIds = new ArrayList<Integer>();		
			siteIds.add(siteId);			
			str = new WhImplUtil().getNeNames(siteIds);
		} catch (Exception e) {
			throw e;
		}
		return str;
	}
}
