package com.nms.corba.ninterface.impl.notification.cvthander;

import flowDomainFragment.FlowDomainFragment_T;
import flowDomainFragment.FlowDomainFragment_THelper;
import globaldefs.NameAndStringValue_T;
import globaldefs.ProcessingFailureException;

import java.util.Map;

import notifications.NVList_THelper;
import notifications.NameAndAnyValue_T;
import notifications.ObjectType_T;

import org.omg.CosNotification.Property;
import org.omg.CosNotification.StructuredEvent;

import com.nms.corba.ninterface.framework.CorbaConfig;
import com.nms.corba.ninterface.framework.CorbaServer;
import com.nms.corba.ninterface.framework.notify.CorbaNotifyCvtHandler;
import com.nms.corba.ninterface.framework.notify.INotifyProcess;
import com.nms.corba.ninterface.impl.service.tool.CorbaFlowDomainConvertTools;
import com.nms.db.bean.ptn.path.eth.ElanInfo;
import com.nms.db.bean.ptn.path.eth.ElineInfo;
import com.nms.db.bean.ptn.path.eth.EtreeInfo;
import com.nms.db.enums.EServiceType;
import com.nms.service.notify.Message;
import com.nms.ui.manager.ExceptionManage;

/**
 * corba端流域上报 eline，etree，elan
 * @author xuxx
 */
public class FDFr_CvtHandler extends CorbaNotifyCvtHandler implements INotifyProcess {

	@Override
	public ObjectType_T getObjectType() {
		return ObjectType_T.OT_AID;
	}

	/**
	 * 修改的属性消息的处理
	 * @param msg 上报的消息
	 * @return 修改的属性列表
	 */
	@SuppressWarnings("unchecked")
	private NameAndAnyValue_T[] getAttributeList(Message msg){
		NameAndAnyValue_T[] attributeList = null;
		//判断消息体的类型
		if (null == msg.getSubEventName() || "".equals(msg.getSubEventName())) {
			return null;
		}
		if (null != ((Map<String, String>)msg.getMsgBody()).get("userLabel") 
				&& null != ((Map<String, String>)msg.getMsgBody()).get("status")) {
			attributeList = new NameAndAnyValue_T[3];
			attributeList[0] = new NameAndAnyValue_T("userLabel", CorbaServer.getInstanse().createAny());
			attributeList[0].value.insert_string(((Map<String, String>)msg.getMsgBody()).get("userLabel"));
			attributeList[1] = new NameAndAnyValue_T("nativeEMSName", CorbaServer.getInstanse().createAny());
			attributeList[1].value.insert_string(((Map<String, String>)msg.getMsgBody()).get("userLabel"));
			attributeList[2] = new NameAndAnyValue_T("status", CorbaServer.getInstanse().createAny());
			attributeList[2].value.insert_string(((Map<String, String>)msg.getMsgBody()).get("status"));
		}else if (null != ((Map<String, String>)msg.getMsgBody()).get("userLabel") 
				&& null == ((Map<String, String>)msg.getMsgBody()).get("status"))  {
			attributeList = new NameAndAnyValue_T[2];
			attributeList[0] = new NameAndAnyValue_T("userLabel", CorbaServer.getInstanse().createAny());
			attributeList[0].value.insert_string(((Map<String, String>)msg.getMsgBody()).get("userLabel"));
			attributeList[1] = new NameAndAnyValue_T("nativeEMSName", CorbaServer.getInstanse().createAny());
			attributeList[1].value.insert_string(((Map<String, String>)msg.getMsgBody()).get("userLabel"));
		}else if (null == ((Map<String, String>)msg.getMsgBody()).get("userLabel") 
				&& null != ((Map<String, String>)msg.getMsgBody()).get("status")) {
			attributeList = new NameAndAnyValue_T[1];
			attributeList[0] = new NameAndAnyValue_T("status", CorbaServer.getInstanse().createAny());
			attributeList[0].value.insert_string(((Map<String, String>)msg.getMsgBody()).get("status"));
		}
		return attributeList;
	}
	
	/**
	 * 属性修改消息上报
	 * @param msg 上报消息
	 * @param idlEvent 可过滤事件体
	 */
	protected void convertObjectAttributeChg(Message msg,StructuredEvent idlEvent){
		idlEvent.filterable_data = new Property[5];

		NameAndStringValue_T[] ETHName = new NameAndStringValue_T[3];
		ETHName = getETHServiceNameChg(msg);
		if (null == ETHName) {
			idlEvent = null;
			return;
		}
		idlEvent.filterable_data[0] = filterable_objectName(ETHName);
		idlEvent.filterable_data[1] = filterable_objectType(getObjectType());
		idlEvent.filterable_data[2] = filterable_emsTime();
		idlEvent.filterable_data[3] = filterable_objectTypeQualifier("OT_FLOW_DOMAIN_FRAGMENT");
		idlEvent.filterable_data[4] = new Property();
		idlEvent.filterable_data[4].name = new String("attributeList");
		idlEvent.filterable_data[4].value = CorbaServer.getInstanse()
				.createAny();
		NameAndAnyValue_T[] attributeList = getAttributeList(msg);
		if (null == attributeList) {
			idlEvent = null;
			return;
		}
		NVList_THelper.insert(idlEvent.filterable_data[4].value, attributeList);
		idlEvent.remainder_of_body = CorbaServer.getInstanse().createAny();
			
	}

	/**
	 * 状态改变上报
	 * @param msg 上报消息
	 * @param idlEvent 可过滤事件体
	 */
	protected void convertObjectStateChg(Message msg, StructuredEvent idlEvent) {
		idlEvent.filterable_data = new Property[5];
		NameAndStringValue_T[] ETHName = new NameAndStringValue_T[3];
			ETHName = getETHServiceNameChg(msg);
		if (ETHName == null) {
			idlEvent = null;
			return ;
		}
		idlEvent.filterable_data[0] = filterable_objectName(ETHName);
		idlEvent.filterable_data[1] = filterable_objectType(getObjectType());
		idlEvent.filterable_data[2] = filterable_emsTime();
		idlEvent.filterable_data[3] = filterable_objectTypeQualifier("OT_CROSSCONNECTION");
		idlEvent.filterable_data[4] = new Property();
		idlEvent.filterable_data[4].name = new String("stateList");
		idlEvent.filterable_data[4].value = CorbaServer.getInstanse().createAny();

		NameAndAnyValue_T[]  stateList = getStateList(msg);
		if (null == stateList) {
			idlEvent = null;
			return ;
		}
		NVList_THelper.insert(idlEvent.filterable_data[4].value, stateList);
		idlEvent.remainder_of_body = CorbaServer.getInstanse().createAny();
	}
	
	/**
	 * 获得上报对象改变后的状态
	 * @param msg
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private NameAndAnyValue_T[] getStateList(Message msg){
		NameAndAnyValue_T[] stateList = null;
		//判断消息体的类型
		if (null == msg.getSubEventName() || "".equals(msg.getSubEventName())) {
			return null;
		}
		if (null != ((Map<String, String>)msg.getMsgBody()).get("status")) {
			stateList = new NameAndAnyValue_T[1];
			stateList[0] = new NameAndAnyValue_T("status", CorbaServer.getInstanse().createAny());
			if (((Map<String, String>)msg.getMsgBody()).get("status").equals("1")) {
				stateList[0].value.insert_string("ACTIVITY");
			}else if (((Map<String, String>)msg.getMsgBody()).get("status").equals("0")) {
				stateList[0].value.insert_string("NONE");
			}else {
				stateList[0].value.insert_string("UNACTIVITY");
			}
		}
		
		return stateList;
	}
    
	/**
	 * 删除的上报
	 * @param msg 上报消息
	 * @param idlEvent 可过滤事件体
	 */
	protected void convertObjectDeletion(Message msg, StructuredEvent idlEvent) {

		NameAndStringValue_T[] ETHName = new NameAndStringValue_T[3];
		ETHName = getETHServiceName(msg);
		if (null == ETHName) {
			idlEvent = null;
			return;
		}
		idlEvent.filterable_data = new Property[4];
		idlEvent.filterable_data[0] = filterable_objectName(ETHName);
		idlEvent.filterable_data[1] = filterable_objectType(getObjectType());
		idlEvent.filterable_data[2] = filterable_emsTime();
		idlEvent.filterable_data[3] = filterable_objectTypeQualifier("OT_FLOW_DOMAIN_FRAGMENT");
		idlEvent.remainder_of_body = CorbaServer.getInstanse().createAny();
		idlEvent.remainder_of_body.insert_string("");
			
	}

	/**
	 * 新建消息上报
	 * @param msg 上报消息
	 * @param idlEvent 可过滤事件体
	 */
	protected void convertObjectCreation(Message msg, StructuredEvent idlEvent) {

		NameAndStringValue_T[] ETHName;
		ETHName = getETHServiceName(msg);
		if (null == ETHName) {
			idlEvent = null;
			return;
		}
		idlEvent.filterable_data = new Property[4];
		idlEvent.filterable_data[0] = filterable_objectName(ETHName);
		idlEvent.filterable_data[1] = filterable_objectType(getObjectType());
		idlEvent.filterable_data[2] = filterable_emsTime();
		idlEvent.filterable_data[3] = filterable_objectTypeQualifier("OT_FLOW_DOMAIN_FRAGMENT");
		idlEvent.remainder_of_body = CorbaServer.getInstanse().createAny();

		convertTool(msg,idlEvent);
	}

	/**
	 * 根据上报不同信息给对象赋值
	 * @param msg 上报的信息
	 * @return
	 */
	private NameAndStringValue_T[] getETHServiceName(Message msg){
		if (null == msg.getSubEventName() || "".equals(msg.getSubEventName())) {
			return null;
		}
		NameAndStringValue_T[] ETHName = new NameAndStringValue_T[3];
		ETHName[0] = new NameAndStringValue_T("EMS",CorbaConfig.getInstanse().getCorbaEmsName());
		ETHName[1] = new NameAndStringValue_T("FlowDomain","1");
		if ((EServiceType.ELINE.getValue()+"").equals(msg.getSubEventName())) {
			ETHName[2] = new NameAndStringValue_T("FlowDomainFragment",((ElineInfo)msg.getMsgBody()).getId()+"");
		}else if ((EServiceType.ETREE.getValue()+"").equals(msg.getSubEventName())) {
			ETHName[2] = new NameAndStringValue_T("FlowDomainFragment",((EtreeInfo)msg.getMsgBody()).getId()+"");
		}else if ((EServiceType.ELAN.getValue()+"").equals(msg.getSubEventName())) {
			ETHName[2] = new NameAndStringValue_T("FlowDomainFragment",((ElanInfo)msg.getMsgBody()).getId()+"");
		}
		return ETHName;
	}

	/**
	 * 修改属性时根据上报不同信息给对象赋值
	 * @param msg 上报的信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private NameAndStringValue_T[] getETHServiceNameChg(Message msg){
		if (null == msg.getSubEventName() || "".equals(msg.getSubEventName())) {
			return null;
		}
		NameAndStringValue_T[] ETHName = new NameAndStringValue_T[3];
		ETHName[0] = new NameAndStringValue_T("EMS",CorbaConfig.getInstanse().getCorbaEmsName());
		ETHName[1] = new NameAndStringValue_T("FlowDomain","1");
		ETHName[2] = new NameAndStringValue_T("FlowDomainFragment",((Map<String, String>)msg.getMsgBody()).get("id"));
		return ETHName;
	}
		
	/**
	 * 根据具体类型进行转换
	 * @param msg 上报消息
	 * @param idlEvent 可过滤事件体
	 */
	private void convertTool(Message msg, StructuredEvent idlEvent) {
		if (null == msg.getSubEventName() || "".equals(msg.getSubEventName())) {
			idlEvent = null;
			return ;
		}
		CorbaFlowDomainConvertTools domainConvertTools = new CorbaFlowDomainConvertTools();
		try {
			if ((EServiceType.ELINE.getValue()+"").equals(msg.getSubEventName())) {
				FlowDomainFragment_T eline = domainConvertTools.convertFDFr((ElineInfo)msg.getMsgBody());
				FlowDomainFragment_THelper.insert(idlEvent.remainder_of_body, eline);
			}else if ((EServiceType.ETREE.getValue()+"").equals(msg.getSubEventName())) {
				FlowDomainFragment_T eline = domainConvertTools.convertFDFr((EtreeInfo)msg.getMsgBody());
				FlowDomainFragment_THelper.insert(idlEvent.remainder_of_body, eline);
			}else if ((EServiceType.ELAN.getValue()+"").equals(msg.getSubEventName())) {
				FlowDomainFragment_T eline = domainConvertTools.convertFDFr((ElanInfo)msg.getMsgBody());
				FlowDomainFragment_THelper.insert(idlEvent.remainder_of_body, eline);
			}
		} catch (ProcessingFailureException e) {
			idlEvent = null;
			ExceptionManage.dispose(e,this.getClass());
		}
	}
	
}
