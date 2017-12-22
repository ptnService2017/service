package com.nms.snmp.ninteface.impl.inventory;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nms.corba.ninterface.util.ELayerRate;
import com.nms.db.bean.ptn.path.ces.CesInfo;
import com.nms.db.bean.ptn.path.pw.PwInfo;
import com.nms.db.bean.ptn.path.tunnel.Tunnel;
import com.nms.db.enums.EPwType;
import com.nms.db.enums.EServiceType;
import com.nms.model.ptn.path.ces.CesInfoService_MB;
import com.nms.model.ptn.path.pw.PwInfoService_MB;
import com.nms.model.ptn.path.tunnel.TunnelService_MB;
import com.nms.model.util.Services;
import com.nms.service.impl.dispatch.PwDispatch;
import com.nms.service.impl.dispatch.TunnelDispatch;
import com.nms.service.impl.dispatch.rmi.DispatchInterface;
import com.nms.snmp.ninteface.framework.SnmpConfig;
import com.nms.snmp.ninteface.framework.TableHandler;
import com.nms.snmp.ninteface.util.FileTools;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.DateUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.ResourceUtil;
import com.nms.ui.manager.UiUtil;
import com.nms.ui.manager.keys.StringKeysLbl;

public class SubNetworkConnTable extends TableHandler{

	@Override
	public Object getInterfaceData(List<VariableBinding> vbList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean setInterfaceData(List<VariableBinding> vbList) {
		String id = "";
		String index = "";
		String oid = "";
		String[] oids = null;
		Map<Integer,Object> subNetWorkConnData = new HashMap<Integer, Object>();
		try {
			subNetWorkConnData = getSubNetWorkConnData();
			if(vbList != null && vbList.size()>0){
				 for(VariableBinding vb : vbList){
					oid = vb.getOid().toString();
					oids = oid.trim().split("\\.");
					id =oids[oids.length-1];
					index = oids[oids.length-2];
					for(Integer indexId : subNetWorkConnData.keySet()){
						if(indexId == Integer.parseInt(id)){
							dispathSubNetWork(subNetWorkConnData.get(indexId),index,vb.getVariable().toString().split(";")[1]);
				 }
			  }
			}
		   }
		} catch (Exception e) {
		 ExceptionManage.dispose(e,this.getClass());
		}
		return true;
	}
	/**
	 * set操作,相当于网管的修改操作
	 * vbList里面的对象类似: 1.1.3.6.1.4.1.44484.1.10.2.1.2.1 = 22;
	 * 等号前面是OID,后面是修改后的值
	 * 根据OID的最后一位判断该属性值所属的pw对象的pwId
	 * 根据OID的倒数第二位判断是什么属性
	 */
	@Override
	public void setTable(List<VariableBinding> vbList) {
		for (VariableBinding vb : vbList) {
			moTable.setValue(vb);
		}
	}

	@Override
	public void updateTable(Object obj) {
		Map<Integer,Object> subNetWorkConnData = new HashMap<Integer,Object>();
		PwInfoService_MB pwService = null;
		TunnelService_MB tunnelService = null;
		CesInfoService_MB cesService = null;
		try {
			int rowIndex = 1;
			pwService = (PwInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PwInfo);
			List<PwInfo> pwList = pwService.selectAll();
			for (PwInfo pw : pwList) {
				Variable[] rowValues = new Variable[] {
					new OctetString(pw.getPwId()+""),
					new OctetString(pw.getPwName()),
					new OctetString(SnmpConfig.getInstanse().getEmsNativeName()),
					new OctetString("bidirection"),//方向
					new OctetString(ELayerRate.PW.getValue()+""),//层速率
					new OctetString(pw.getPwStatus()==1? "activity":"unactivity"),//1/2 激活/未激活
					new OctetString("E-PW"),//SNC类型
					new OctetString("siteId:"+pw.getASiteId()+"/inLabel:"+pw.getInlabelValue()),
					new OctetString("siteId:"+pw.getZSiteId()+"/outLabel:"+pw.getOutlabelValue()),
					new OctetString("createTime:"+pw.getCreateTime()+"/"+"ServerConnections:"+
								pw.getPwId()+"/"+"ServiceType:"+pw.getType().toString()),//附加信息
				};
				addRow(new OID(""+(rowIndex)), rowValues);
				subNetWorkConnData.put(rowIndex, pw);
				rowIndex++;
			}
			
			tunnelService = (TunnelService_MB) ConstantUtil.serviceFactory.newService_MB(Services.Tunnel);
			List<Tunnel> tunnelList = tunnelService.selectAllData();
			Tunnel tunnel = null;
			String frontLabelValue = "";
			String backLabelValue = "";
			if(tunnelList != null && tunnelList.size() >0){
			  for(int i = 0; i < tunnelList.size(); i++){
				  tunnel = tunnelList.get(i);
				  if(tunnel.getLspParticularList()!= null && tunnel.getLspParticularList().size()>0){
				    	frontLabelValue = tunnel.getLspParticularList().get(0).getFrontLabelValue()+"";
				    	backLabelValue =  tunnel.getLspParticularList().get(0).getBackLabelValue()+"";
				    }
				  Variable[] variables = new Variable[]{
				    new OctetString(tunnel.getTunnelId()+""),
				    new OctetString(tunnel.getTunnelName()),
				    new OctetString(SnmpConfig.getInstanse().getEmsNativeName()),
				    new OctetString("bidirection"),
				    new OctetString(ELayerRate.TUNNEL.getValue()+""),
				    new OctetString(tunnel.getTunnelStatus() == 1 ?"activity":"unactivity"),
				    new OctetString("E-LSP"),
				    new OctetString("aSite="+tunnel.getaSiteId()+"/aPort="+tunnel.getaPortId()+"/frontLabel="+frontLabelValue),
				    new OctetString("zSite="+tunnel.getaSiteId()+"/zPort="+tunnel.getaPortId()+"/backLabel="+backLabelValue),
				    new OctetString("createTime:"+tunnel.getCreateTime()+"/"+"ServerConnections:"+
							tunnel.getTunnelId()+"/"+"ServiceType:"+EServiceType.TUNNEL.toString()),
				  };
				  addRow(new OID(rowIndex+""), variables);
				  subNetWorkConnData.put(rowIndex, tunnel);
				  rowIndex++;
			  }
			}
			cesService = (CesInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.CesInfo);
			List<CesInfo> cesList = cesService.selectAll();
			for (CesInfo ces : cesList) {
				Variable[] rowValues = new Variable[] {
					new OctetString(ces.getId()+""),
					new OctetString(ces.getName()),
					new OctetString(SnmpConfig.getInstanse().getEmsNativeName()),
					new OctetString("bidirection"),//方向
					new OctetString(ELayerRate.CES.getValue()+""),//层速率
					new OctetString(ces.getActiveStatus()==1? "activity":"unactivity"),//1/2 激活/未激活
					new OctetString("E-E1"),//SNC类型
					new OctetString("siteId:"+ces.getaSiteId()+"/"+"portId:"+ces.getaAcId()),
					new OctetString("siteId:"+ces.getzSiteId()+"/"+"portId:"+ces.getzAcId()),
					new OctetString("createTime:"+ces.getCreateTime()+"/"+"ServerConnections:"+
								ces.getPwId()+"/"+"ServiceType:"+EPwType.PDH.toString()),//附加信息
				};
				addRow(new OID(""+(rowIndex)), rowValues);
				subNetWorkConnData.put(rowIndex, ces);
				rowIndex++;
			}
			
			//生成对应的文件
//			createSubNetWorkConnFile(subNetWorkConnData);
		} catch (Exception e) {
			 ExceptionManage.dispose(e,this.getClass());
		} finally {
			UiUtil.closeService_MB(pwService);
			UiUtil.closeService_MB(cesService);
			UiUtil.closeService_MB(tunnelService);
		}
	}

	/**
	 * 获取所有的数据
	 * @return
	 */
	private Map<Integer,Object> getSubNetWorkConnData(){
		Map<Integer,Object> subNetWorkConnData = new HashMap<Integer,Object>();
		int index = 1 ;
		TunnelService_MB tunnelService = null;
		PwInfoService_MB pwService = null;
		try {
			tunnelService = (TunnelService_MB) ConstantUtil.serviceFactory.newService_MB(Services.Tunnel);
			List<Tunnel> tunnelList = tunnelService.selectAllData();
			pwService = (PwInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PwInfo);
			List<PwInfo> pwList = pwService.selectAll();
			
			if(pwList != null && pwList.size()>0){
				for(PwInfo pwInfo : pwList){
					subNetWorkConnData.put(index++, pwInfo);
				}
			}
			
			if(tunnelList != null && tunnelList.size()>0){
				for(Tunnel tunnel : tunnelList){
					subNetWorkConnData.put(index++, tunnel);
				}
			}
			
		} catch (Exception e) {
		  ExceptionManage.dispose(e,this.getClass());
		} finally {
			UiUtil.closeService_MB(pwService);
			UiUtil.closeService_MB(tunnelService);
		}
		return subNetWorkConnData;
	}
	
	/**
	 * 根据不同的对象处理tunnel/pw的数据
	 * 
	 * @param object
	 */
	private void dispathSubNetWork(Object object,String id,String value){
		DispatchInterface dispath = null;
		try {
			if(object instanceof Tunnel){
				Tunnel tunnel = (Tunnel) object;
				if(id.equals("3")){
					tunnel.setTunnelName(value);
				}else if(id.equals("7")){
					if("activity".equals(value)){
					  tunnel.setTunnelStatus(1);
					}else{
					  tunnel.setTunnelStatus(0);	
					}
				}
				dispath = new TunnelDispatch();
				dispath.excuteUpdate(tunnel);
				
			}else if(object instanceof PwInfo){
				PwInfo pwInfo = (PwInfo) object;
				if(id.equals("3")){
					pwInfo.setPwName(value);
				}else if(id.equals("7")){
					if("activity".equals(value)){
					  pwInfo.setPwStatus(1);
					}else{
					  pwInfo.setPwStatus(0); 
					}
				}
				dispath = new PwDispatch();
				dispath.excuteUpdate(pwInfo);
			}
		} catch (Exception e) {
			 ExceptionManage.dispose(e,this.getClass());
		}
	}

	
//文件头文件
 private void createSubNetWorkConnFile(Map<Integer,Object> allSubNetWorkConnData){
	 FileTools fileTool = new FileTools();
	 try {
		 if(allSubNetWorkConnData.size() >0){
			 String version = ResourceUtil.srcStr(StringKeysLbl.LBL_SNMPMODEL_VERSION);
			 String[] xmlPath = {"snmpData\\ZJ\\CS\\EB\\OMC\\CM\\"+DateUtil.getDate("yyyyMMdd"), "CMCC-PTN-NRM-SNC-"+version+"-"+this.getTime()+"-P00.xml"};
			 String filePath = fileTool.createFile(xmlPath);
			 Document doc = fileTool.getDocument();
			 createXml(doc,allSubNetWorkConnData);
			 fileTool.putFile(doc, filePath);
			 fileTool.zipFile(filePath, filePath+".zip");
		 }
	} catch (Exception e) {
		 ExceptionManage.dispose(e,this.getClass());
	}
 }
 
 	private String getTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd-HHmm");
		return format.format(System.currentTimeMillis());
	}

private void createXml(Document doc, Map<Integer, Object> allSubNetWorkConnData) {
	FileTools fileTool = new FileTools();
	try {
		doc.setXmlVersion("1.0");
		Element root =fileTool.rootElement(doc);
	    Element terminationPointList = this.crateFileContent(doc,allSubNetWorkConnData);
		root.appendChild(terminationPointList);
	    doc.appendChild(root);
	} catch (Exception e) {
		 ExceptionManage.dispose(e,this.getClass());
	}

}

private Element crateFileContent(Document doc,Map<Integer, Object> allSubNetWorkConnData) {
	Element subNetWorkList = doc.createElement("SubnetworkConnectionList_T");
	for(Integer indexId : allSubNetWorkConnData.keySet()){
		Element subData = doc.createElement("SubnetworkConnection_T");
		try {
			this.crateElementNodeName(doc,"name",allSubNetWorkConnData.get(indexId),subData);//Name
			this.crateElementNodeName(doc,"userLabel",allSubNetWorkConnData.get(indexId),subData);
			this.crateElementNodeName(doc,"nativeEMSName",allSubNetWorkConnData.get(indexId),subData);
			this.crateElementNodeName(doc,"direction",allSubNetWorkConnData.get(indexId),subData);
			this.crateElementNodeName(doc,"rate",allSubNetWorkConnData.get(indexId),subData);
			this.crateElementNodeName(doc,"sncType",allSubNetWorkConnData.get(indexId),subData);
			this.crateElementNodeName(doc,"aEnd",allSubNetWorkConnData.get(indexId),subData);
			this.crateElementNodeName(doc,"zEnd",allSubNetWorkConnData.get(indexId),subData);
			this.crateElementNodeName(doc,"additionalInfo",allSubNetWorkConnData.get(indexId),subData);
			subNetWorkList.appendChild(subData);
		} catch (Exception e) {
		   ExceptionManage.dispose(e,this.getClass());
		}
	}
	return subNetWorkList;
}

//创建名称
private void crateElementNodeName(Document doc, String childName, Object object,Element subData) {
	Element d = doc.createElement(childName);
	if("name".equals(childName)){
		Element node = doc.createElement("node");
		this.createElementNode(doc, "name","EMS", node);
		this.createElementNode(doc, "value",SnmpConfig.getInstanse().getsnmpEmsName(), node);
		Element nodeOther = doc.createElement("node");
		this.createElementNode(doc, "name","SubnetworkConnection", nodeOther);
		if(object instanceof Tunnel){
			 this.createElementNode(doc, "value",((Tunnel)object).getTunnelId()+"", nodeOther);
		}else if(object instanceof PwInfo){
			 this.createElementNode(doc, "value",((PwInfo)object).getPwId()+"", nodeOther);
		}else{
			this.createElementNode(doc, "value", ((CesInfo)object).getId()+"", nodeOther);
		}
		d.appendChild(node);
		d.appendChild(nodeOther);
	}else if("userLabel".equals(childName)){
		if(object instanceof Tunnel){
			d.setTextContent(((Tunnel)object).getTunnelName());
		}else if(object instanceof PwInfo){
			d.setTextContent(((PwInfo)object).getPwName());
		}else{
			d.setTextContent(((CesInfo)object).getName());
		}
	}else if("nativeEMSName".equals(childName)){
		d.setTextContent(SnmpConfig.getInstanse().getEmsNativeName());
	}else if("sncState".equals(childName)){
		if(object instanceof Tunnel){
			d.setTextContent(((Tunnel)object).getTunnelStatus() == 1 ?"activity":"unactivity");
		}else if(object instanceof PwInfo){
			d.setTextContent(((PwInfo)object).getPwStatus() == 1?"activity":"unactivity");
		}else{
			d.setTextContent(((CesInfo)object).getActiveStatus() == 1?"activity":"unactivity");
		}
	}else if("direction".equals(childName)){
		d.setTextContent("bidirection");
	}else if("rate".equals(childName)){
		if(object instanceof Tunnel){
			d.setTextContent(ELayerRate.TUNNEL.getValue()+"");
		}else if(object instanceof PwInfo){
			d.setTextContent(ELayerRate.PW.getValue()+"");
		}else{
			d.setTextContent(ELayerRate.CES.getValue()+"");
		}
	}else if("sncType".equals(childName)){
		if(object instanceof Tunnel){
			d.setTextContent("tunnel");
		}else if(object instanceof PwInfo){
			d.setTextContent("pw");
		}else{
			d.setTextContent("ces");
		}
	}else if("aEnd".equals(childName)){
		d.appendChild(cretateAZNode(doc,object,1));
	}else if("zEnd".equals(childName) ){
		d.appendChild(cretateAZNode(doc,object,2));
	}else if("additionalInfo".equals(childName) ){
		Element oneParam1 = doc.createElement("oneParam"); 
		Element oneParam2 = doc.createElement("oneParam"); 
		Element oneParam3 = doc.createElement("oneParam"); 
		Element oneParam4 = doc.createElement("oneParam"); 
		Element oneParam5 = doc.createElement("oneParam"); 
		Element oneParam6 = doc.createElement("oneParam"); 
		Element oneParam7 = doc.createElement("oneParam"); 
		Element oneParam8 = doc.createElement("oneParam"); 
		Element oneParam9 = doc.createElement("oneParam"); 
		this.createElementNode(doc, "name","CreateTime", oneParam1);
		this.createElementNode(doc, "name","ServerConnections", oneParam2);
		this.createElementNode(doc, "name","ServiceType", oneParam3);
		this.createElementNode(doc, "name","EmulationType", oneParam4);
		this.createElementNode(doc, "name","E1ChannelList", oneParam5);
		this.createElementNode(doc, "name","RTPEnable", oneParam6);
		this.createElementNode(doc, "name","FrameNumber", oneParam7);
		this.createElementNode(doc, "name","PDVT", oneParam8);
		this.createElementNode(doc, "name","EcapsulateType", oneParam9);
		
		if(object instanceof Tunnel){
			this.createElementNode(doc, "value",((Tunnel)object).getCreateTime(), oneParam1);
			this.createElementNode(doc, "value"," ", oneParam2);
			this.createElementNode(doc, "value"," ", oneParam3);
			this.createElementNode(doc, "value"," ", oneParam4);
			this.createElementNode(doc, "value"," ", oneParam5);
			this.createElementNode(doc, "value"," ", oneParam6);
			this.createElementNode(doc, "value"," ", oneParam7);
			this.createElementNode(doc, "value"," ", oneParam8);
			this.createElementNode(doc, "value"," ", oneParam9);
		}else if(object instanceof PwInfo){
			this.createElementNode(doc, "value",((PwInfo)object).getCreateTime(), oneParam1);
			this.createElementNode(doc, "value",((PwInfo)object).getPwId()+"", oneParam2);
			this.createElementNode(doc, "value","ETH/E1", oneParam3);
			this.createElementNode(doc, "value","E1", oneParam4);
			this.createElementNode(doc, "value","1", oneParam5);
			this.createElementNode(doc, "value","1", oneParam6);
			this.createElementNode(doc, "value","4", oneParam7);
			this.createElementNode(doc, "value","2", oneParam8);
			this.createElementNode(doc, "value","SAToP", oneParam9);
		}else{
			this.createElementNode(doc, "value",((CesInfo)object).getCreateTime(), oneParam1);
			this.createElementNode(doc, "value",((CesInfo)object).getId()+"", oneParam2);
			this.createElementNode(doc, "value","PDH", oneParam3);
			this.createElementNode(doc, "value","E1", oneParam4);
			this.createElementNode(doc, "value","1", oneParam5);
			this.createElementNode(doc, "value","1", oneParam6);
			this.createElementNode(doc, "value","4", oneParam7);
			this.createElementNode(doc, "value","2", oneParam8);
			this.createElementNode(doc, "value","SAToP", oneParam9);
		}
		d.appendChild(oneParam1);
		d.appendChild(oneParam2);
		d.appendChild(oneParam3);
		d.appendChild(oneParam4);
		d.appendChild(oneParam5);
		d.appendChild(oneParam6);
		d.appendChild(oneParam7);
		d.appendChild(oneParam8);
		d.appendChild(oneParam9);
	}
	subData.appendChild(d);
 }
 

/**
 * 根据名称创建元素,并赋值
 */
private void createElementNode(Document doc, String childName, String childValue, Element element){
	Element e = doc.createElement(childName);
    e.setTextContent(childValue);
    element.appendChild(e);
}

/**
 * 
 * @param doc
 * @param object
 * @param label 用于标记是A端还是Z端 1：A 2：Z
 * @return
 */
private Element cretateAZNode(Document doc,Object object,int label){
	Element node = doc.createElement("tpData");
	Element nodeTpName = doc.createElement("tpName");
	Element nodeTransmissParams = doc.createElement("transmissionParams");
	Element nodeName = doc.createElement("node");
	this.createElementNode(doc, "name","EMS", nodeName);
	this.createElementNode(doc, "value",SnmpConfig.getInstanse().getsnmpEmsName(), nodeName);
	Element nodeOther = doc.createElement("node");
	Element nodePtp = doc.createElement("node");
	this.createElementNode(doc, "name","ManagedElement", nodeOther);
	this.createElementNode(doc, "name","PTP", nodePtp);
	Element transmissParamsNode = doc.createElement("LayeredParameterList_T");
	this.createElementNode(doc, "layer",ELayerRate.PORT.getValue()+"", transmissParamsNode);
	if(object instanceof Tunnel){
		if(label ==1){
			this.createElementNode(doc, "value",((Tunnel)object).getaSiteId()+"", nodeOther);
			this.createElementNode(doc, "value","/site="+((Tunnel)object).getaSiteId()+"/port="+((Tunnel)object).getaPortId(), nodePtp);
		}else{
			this.createElementNode(doc, "value",((Tunnel)object).getzSiteId()+"", nodeOther);
			this.createElementNode(doc, "value","/site="+((Tunnel)object).getzSiteId()+"/port="+((Tunnel)object).getzPortId(), nodePtp);
		}
	}else if(object instanceof PwInfo){
		if(label ==1){
			this.createElementNode(doc, "value",((PwInfo)object).getASiteId()+"", nodeOther);
			this.createElementNode(doc, "value","/site="+((PwInfo)object).getASiteId()+"/port="+((PwInfo)object).getaPortConfigId(), nodePtp);
		}else{                                     
			this.createElementNode(doc, "value",((PwInfo)object).getZSiteId()+"", nodeOther);
			this.createElementNode(doc, "value","/site="+((PwInfo)object).getZSiteId()+"/port="+((PwInfo)object).getzPortConfigId(), nodePtp);
		}
	}else{
		if(label == 1){
			this.createElementNode(doc, "value", ((CesInfo)object).getaSiteId()+"", nodeOther);
			this.createElementNode(doc, "value","/site="+((CesInfo)object).getaSiteId()+"/port="+((CesInfo)object).getAportId(), nodePtp);
		}else{
			this.createElementNode(doc, "value", ((CesInfo)object).getzSiteId()+"", nodeOther);
			this.createElementNode(doc, "value","/site="+((CesInfo)object).getzSiteId()+"/port="+((CesInfo)object).getZportId(), nodePtp);
		}
	}
	nodeTransmissParams.appendChild(transmissParamsNode);
	nodeTpName.appendChild(nodeName);
	nodeTpName.appendChild(nodeOther);
	nodeTpName.appendChild(nodePtp);
	node.appendChild(nodeTpName);
	node.appendChild(nodeTransmissParams);
	return node;
}
 
}
