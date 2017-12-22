package com.nms.service.impl.base;

import java.util.List;

import com.nms.db.bean.equipment.shelf.SiteInst;
import com.nms.db.bean.equipment.slot.SlotInst;
import com.nms.model.equipment.shlef.SiteService_MB;
import com.nms.model.equipment.slot.SlotService_MB;
import com.nms.model.util.Services;
import com.nms.service.impl.util.ResultString;
import com.nms.service.notify.Message;
import com.nms.service.notify.NotifyPublisher;
import com.nms.service.notify.Message.MessageType;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.UiUtil;

public class DispatchBase {
	
	/**
	 * 根据网元ID获取网元厂商
	 * @param siteId 网元主键
	 * @return 厂商
	 * @throws Exception
	 */
	public int getManufacturer(int siteId) throws Exception{
		int manufacturer=0;
		SiteService_MB siteService= null;
		SiteInst siteInst=null;
		try {
			siteService=(SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE);
			siteInst=siteService.select(siteId);
			if(siteInst == null){
				throw new Exception("根据ID查询网元出错");
			}
			manufacturer=Integer.parseInt(UiUtil.getCodeById(Integer.parseInt(siteInst.getCellEditon())).getCodeValue());
		} catch (Exception e) {
			ExceptionManage.dispose(e,DispatchBase.class);
		}finally{
			UiUtil.closeService_MB(siteService);
		}
		return manufacturer;
	}
	
	public static String getNEType(int siteId) throws Exception {
		String manufacturer = "";
		SiteService_MB siteService = null;
		SiteInst siteInst = null;
		try {
			siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE);
			siteInst = siteService.select(siteId);
			if (siteInst == null) {
				throw new Exception("根据ID查询网元出错");
			}
			manufacturer = siteInst.getCellType();
		} catch (Exception e) {
			ExceptionManage.dispose(e,DispatchBase.class);
		} finally {
			UiUtil.closeService_MB(siteService);
		}
		return manufacturer;
	}
	//根据SLOT ID查询slot number
	public int getSlotNumber(int slotId){
		SlotService_MB slotService = null;
		int slotNum = -1;
		try {
			slotService = (SlotService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SLOT);
			SlotInst slot = new SlotInst();
			slot.setId(slotId);
			List<SlotInst> slotList = slotService.select(slot);
			if(slotList.size()==1){
				slotNum = slotList.get(0).getNumber();
			}
		}catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}finally {
			UiUtil.closeService_MB(slotService);
		}
		return slotNum;
	}
	/**
	 * 网管发生操作，上报corba
	 * @param objname 具体操作名称  网元=managedElement 等
	 * @param messageType 操作对象。新建、删除等
	 * @param object 操作对象，如果是网元，此对象就是siteinst
	 * @param type 上报对象的类型
	 * @param result 是否上报的标识
	 * @throws Exception
	 */
	public void notifyCorba(String objname,MessageType messageType,Object object,String type, String result) throws Exception{
		Message message=null;
		try {
			if (result.equals(ResultString.CONFIG_SUCCESS)) {
				message=new Message();
				message.setMsgBody(object);
				message.setMsgObjName(objname);
				message.setMsgType(messageType);
				message.setSubEventName(type);
				NotifyPublisher.getInstance().notifyListeners(message);
			}
			
		} catch (Exception e) {
			throw e;
		} finally{
			message=null;
		}
	}
}
