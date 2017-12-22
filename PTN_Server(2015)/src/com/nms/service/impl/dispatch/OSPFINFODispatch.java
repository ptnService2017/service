package com.nms.service.impl.dispatch;

import java.rmi.RemoteException;
import java.util.List;

import com.nms.db.bean.ptn.ecn.OSPFInfo;
import com.nms.db.enums.EManufacturer;
import com.nms.service.OperationServiceI;
import com.nms.service.impl.base.DispatchBase;
import com.nms.service.impl.cx.OSPFINFOCXServiceImpl;
import com.nms.service.impl.dispatch.rmi.DispatchInterface;
import com.nms.service.impl.util.ResultString;
import com.nms.service.impl.wh.OSPFinfoWHServiceImpl;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.ResourceUtil;
import com.nms.ui.manager.keys.StringKeysTip;

public class OSPFINFODispatch extends DispatchBase implements DispatchInterface{
	// TXC

	public String excuteUpdate(Object object) throws Exception {
		int manufacturer = 0;
		OperationServiceI operationServiceI = null;
		String result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_FAIL);
		OSPFInfo oSPFInfo=null;
		try {
			if (object == null) {
				throw new Exception("oSPFInfo is null");
			}
			if(object instanceof List){
				operationServiceI = new OSPFinfoWHServiceImpl();
				result = operationServiceI.excutionUpdate(object);
			}else{
				oSPFInfo=(OSPFInfo) object;
				operationServiceI = new OSPFINFOCXServiceImpl();
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		if (ResultString.CONFIG_SUCCESS.equals(result)) {
			return ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
		} else {
			return result;
		}
	}

	public String excuteInsert(Object object) throws Exception {
		String NEType = "";
		OperationServiceI operationServiceI = null;
		String result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_FAIL);
		OSPFInfo oSPFInfo=null;
		try {
			if (object == null) {
				throw new Exception("oSPFInfo is null");
			}
			
			if(object instanceof List){
				operationServiceI = new OSPFinfoWHServiceImpl();
				result = operationServiceI.excutionInsert(object);
			}else{
				oSPFInfo=(OSPFInfo) object;
				operationServiceI = new OSPFINFOCXServiceImpl();
				NEType = super.getNEType(Integer.valueOf(oSPFInfo.getNeId()));
				if ("20A".equals(NEType)) {
					OSPFINFOCXServiceImpl cx = new OSPFINFOCXServiceImpl();
					result = cx.haveOSPF(oSPFInfo);
				} else {
					result = operationServiceI.excutionInsert(oSPFInfo);
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		if (ResultString.CONFIG_SUCCESS.equals(result)) {
			return ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
		} else {
			return result;
		}
	}

	public String excuteDelete(Object object) throws Exception {
		OSPFINFOCXServiceImpl operationServiceI = new OSPFINFOCXServiceImpl();
		String result = ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_FAIL);;
		OSPFInfo oSPFInfo=null;
		try {
			if (object == null) {
				throw new Exception("oSPFInfo is null");
			}
			oSPFInfo=(OSPFInfo) object;
			result = operationServiceI.excutionDelete(oSPFInfo);
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		if (ResultString.CONFIG_SUCCESS.equals(result)) {
			return ResourceUtil.srcStr(StringKeysTip.TIP_CONFIG_SUCCESS);
		} else {
			return result;
		}
	}

	
	/**
	 * synchro(根据网元id同步)
	 * 
	 * @author wangwf
	 * 
	 * @param siteId
	 * 
	 * @return
	 * @throws Exception
	 * 
	 * @Exception 异常对象
	 */
	@SuppressWarnings("unchecked")
	public String synchro(int siteId) throws Exception {
		OperationServiceI operationServiceI = null;
		String result = ResultString.QUERY_FAILED;
		try {
			if (super.getManufacturer(siteId) == EManufacturer.WUHAN.getValue()) {
				operationServiceI = new OSPFinfoWHServiceImpl();
			} else {
				operationServiceI = new OSPFINFOCXServiceImpl();
			}
			result = (String)operationServiceI.synchro(siteId);

		} catch (Exception e) {
			throw e;
		}
		return result;

	}

	@Override
	public Object consistence(int siteId) throws RemoteException, Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
