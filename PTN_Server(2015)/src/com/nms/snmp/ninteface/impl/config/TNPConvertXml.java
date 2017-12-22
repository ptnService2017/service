package com.nms.snmp.ninteface.impl.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nms.corba.ninterface.util.ELayerRate;
import com.nms.db.bean.ptn.path.protect.LoopProtectInfo;
import com.nms.db.bean.ptn.path.tunnel.Tunnel;
import com.nms.model.ptn.path.protect.WrappingProtectService_MB;
import com.nms.model.ptn.path.tunnel.TunnelService_MB;
import com.nms.model.util.ServiceFactory;
import com.nms.model.util.Services;
import com.nms.snmp.ninteface.framework.SnmpConfig;
import com.nms.snmp.ninteface.util.FileTools;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.DateUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.ResourceUtil;
import com.nms.ui.manager.UiUtil;
import com.nms.ui.manager.keys.StringKeysLbl;


public class TNPConvertXml {
	public static void main(String[] args) throws Exception {
		ConstantUtil.serviceFactory = new ServiceFactory();
		new TNPConvertXml().getTNPXml();
	}
	
	public String getTNPXml() {
		//CMCC-PTN-NRM-TNP-V1.0.0-20140411-1602.xml
		String filePath = "";
		String version = ResourceUtil.srcStr(StringKeysLbl.LBL_SNMPMODEL_VERSION);
		String[] xmlPath = {"snmpData\\ZJ\\CS\\EB\\OMC\\CM\\"+DateUtil.getDate("yyyyMMdd"), "CMCC-PTN-NRM-TNP-"+version+"-"+this.getTime()+".xml"};
		FileTools fileTools = null;
		try {
			filePath = xmlPath[0] + File.separator + xmlPath[1];//生成文件路径
	    	List<Tunnel> protTunnelList = this.getProtTunnelList();
	    	List<LoopProtectInfo> loopProtList = this.getLoopProtList();
	    	this.createFile(xmlPath);//根据文件路径和文件名生成xml文件
	    	Document doc = this.getDocument(xmlPath);//生成doucument
		    this.createXML(doc, protTunnelList, loopProtList);//生成xml文件内容
		    fileTools = new FileTools();
		    fileTools.putFile(doc, filePath);//根据xml文件内容生成对应的文件
		    fileTools.zipFile(filePath, filePath+".zip");
		} catch (Exception e){
			ExceptionManage.dispose(e, this.getClass());
		}
		return filePath;
	}
	
	private String getTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd-HHmm");
		return format.format(System.currentTimeMillis());
	}

    private List<Tunnel> getProtTunnelList() {
		List<Tunnel> tunnelList = new ArrayList<Tunnel>();
		TunnelService_MB tunnelService = null;
		try {
			tunnelService = (TunnelService_MB) ConstantUtil.serviceFactory.newService_MB(Services.Tunnel);
			List<Tunnel> tList = tunnelService.select();
			for (Tunnel tunnel : tList) {
				if(!tunnel.getTunnelType().equals("185")){
					tunnelList.add(tunnel);
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			UiUtil.closeService_MB(tunnelService);
		}
		return tunnelList;
	}
    
    private List<LoopProtectInfo> getLoopProtList() {
    	WrappingProtectService_MB protectService = null;
    	List<LoopProtectInfo> protectList = new ArrayList<LoopProtectInfo>();
		try {
			protectService = (WrappingProtectService_MB) ConstantUtil.serviceFactory.newService_MB(Services.WRAPPINGPROTECT);
			protectList = protectService.select();
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			UiUtil.closeService_MB(protectService);
		}
		return protectList;
	}
    
	/**
     * 根据文件路径和文件名生成xml文件
     */
    private void createFile(String[] xmlPath) throws FileNotFoundException {
    	//根据路径生成文件目录
    	File dirname = new File(xmlPath[0]);
    	//如果本地目录不存在，则需要创建一个目录
		if (!dirname.exists()) 
			dirname.mkdirs();
		//生成xml文件
    	new FileOutputStream(xmlPath[0]+File.separator+xmlPath[1]);
	}
    
    /**
     * 生成document
     */
    private Document getDocument(String[] xmlPath) throws Exception {
	    try {  
	     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
	     DocumentBuilder builder = factory.newDocumentBuilder();  
	     Document doc = builder.newDocument();  
	     return doc;
	    } catch (Exception e) {  
	     ExceptionManage.dispose(e,this.getClass());  
	    }
		return null;
	}

    /**
     * 根据需求生成相应的xml文件
     * @throws Exception 
     */
    private void createXML(Document doc, List<Tunnel> tunnelList, List<LoopProtectInfo> loopList) throws Exception{
		doc.setXmlVersion("1.0");
		doc.setXmlStandalone(true);
		Element root = doc.createElement("dm:Descriptor");
		root.setAttribute("xmlns:dm", "http://www.tmforum.org/mtop/mtnm/Configure/v1");
		root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		root.setAttribute("xsi:schemaLocation", "http://www.tmforum.org/mtop/mtnm/Configure/v1 ../Inventory.xsd");
		Element cardNodeList = this.createFileContent(doc, tunnelList, loopList);
		root.appendChild(cardNodeList);
		doc.appendChild(root);
	}
	
	private Element createFileContent(Document doc, List<Tunnel> tunnelList, List<LoopProtectInfo> loopList) throws Exception {
		Element TNPList = doc.createElement("TrailNtwProtectionList_T");
		for (Tunnel tunnel : tunnelList) {
			try{
				this.createFile(doc, tunnel.getTunnelName(), 1, tunnel.getShowSiteAname(), 
						tunnel.getAPortId()+"", tunnel.getShowSiteZname(), tunnel.getZPortId()+"",
						tunnel.getProtectTunnel().getTunnelName(), tunnel.getWaittime()+"",
						tunnel.getDelaytime()+"", tunnel.getTunnelStatus()==1 ? "activity":"unactivity",
						TNPList,"close");
			}
		    catch(Exception e){
		    	ExceptionManage.dispose(e, this.getClass());
		    }
		 }
		for (LoopProtectInfo l : loopList) {
			try{
				this.createFile(doc, l.getName(), 2, l.getNodeId()+"", l.getWestPort()+"",
						l.getTargetNodeId()+"", l.getEastPort()+"", "", l.getWaittime()+"",
						l.getDelaytime()+"", l.getActiveStatus()==1 ? "activity":"unactivity",
								TNPList,"open");
			}
		    catch(Exception e){
		    	ExceptionManage.dispose(e, this.getClass());
		    }
		}
		return TNPList;
	}

	private void createFile(Document doc, String tnpName, int type, String aSiteName, String aPortId,
		String zSiteName, String zPortId, String protTunnelName, String waitTime, String delayTime,	
		String status, Element TNPList,String tnpType) {
		Element tnp = doc.createElement("TrailNtwProtection_T");
        //name
        Element name = doc.createElement("name");
        Element node1 = doc.createElement("node");
        this.createElementNode(doc, "name", "EMS", node1);
        this.createElementNode(doc, "value", SnmpConfig.getInstanse().getsnmpEmsName(), node1);
        name.appendChild(node1);
        Element node2 = doc.createElement("node");
        this.createElementNode(doc, "name", "TNProtectionGroup", node2);
        this.createElementNode(doc, "value", tnpName, node2);
        name.appendChild(node2);
        tnp.appendChild(name);
        //userLabel
        this.createElementNode(doc, "userLabel", tnpName, tnp);
        //nativeEMSName
        this.createElementNode(doc, "nativeEMSName", tnpName, tnp);
        //reversionMode
        this.createElementNode(doc, "reversionMode", "UNKNOWN", tnp);
        //rate
        this.createElementNode(doc, "rate", type == 1?ELayerRate.TUNNEL.getValue()+"":"1", tnp);
        //trailNtwProtectionType
        this.createElementNode(doc, "trailNtwProtectionType",tnpType, tnp);
        //protectionGroupAName
        Element groupA = doc.createElement("protectionGroupAName");
        tnp.appendChild(groupA);
        Element nodeA1 = doc.createElement("node");
        groupA.appendChild(nodeA1);
        this.createElementNode(doc, "name", "EMS", nodeA1);
        this.createElementNode(doc, "value", SnmpConfig.getInstanse().getsnmpEmsName(), nodeA1);
        Element nodeA2 = doc.createElement("node");
        groupA.appendChild(nodeA2);
        this.createElementNode(doc, "name", "ManagedElement", nodeA2);
        this.createElementNode(doc, "value", aSiteName, nodeA2);
        Element nodeA3 = doc.createElement("node");
        groupA.appendChild(nodeA3);
        this.createElementNode(doc, "name", "PGP", nodeA3);
        this.createElementNode(doc, "value", aPortId, nodeA3);
        //protectionGroupZName
        Element groupZ = doc.createElement("protectionGroupZName");
        tnp.appendChild(groupZ);
        Element nodeZ1 = doc.createElement("node");
        groupZ.appendChild(nodeZ1);
        this.createElementNode(doc, "name", "EMS", nodeZ1);
        this.createElementNode(doc, "value", SnmpConfig.getInstanse().getsnmpEmsName(), nodeZ1);
        Element nodeZ2 = doc.createElement("node");
        groupZ.appendChild(nodeZ2);
        this.createElementNode(doc, "name", "ManagedElement", nodeZ2);
        this.createElementNode(doc, "value", zSiteName, nodeZ2);
        Element nodeZ3 = doc.createElement("node");
        groupZ.appendChild(nodeZ3);
        this.createElementNode(doc, "name", "PGP", nodeZ3);
        this.createElementNode(doc, "value", zPortId, nodeZ3);
        //workerTrailList
        Element workerTrailList = doc.createElement("workerTrailList");
        tnp.appendChild(workerTrailList);
        Element nameList = doc.createElement("nameList");
        workerTrailList.appendChild(nameList);
        Element workName = doc.createElement("name");
        nameList.appendChild(workName);
        Element workNode1 = doc.createElement("node");
        workName.appendChild(workNode1);
        this.createElementNode(doc, "name", "EMS", workNode1);
        this.createElementNode(doc, "value", SnmpConfig.getInstanse().getsnmpEmsName(), workNode1);
        Element workNode2 = doc.createElement("node");
        workName.appendChild(workNode2);
        this.createElementNode(doc, "name", "workerTrailName", workNode2);
        this.createElementNode(doc, "value", type==1?tnpName:"", workNode2);
        //protectionTrail
        Element protectionTrail = doc.createElement("protectionTrail");
        tnp.appendChild(protectionTrail);
        Element protName = doc.createElement("name");
        protectionTrail.appendChild(protName);
        Element protNode1 = doc.createElement("node");
        protName.appendChild(protNode1);
        this.createElementNode(doc, "name", "EMS", protNode1);
        this.createElementNode(doc, "value", SnmpConfig.getInstanse().getsnmpEmsName(), protNode1);
        Element protNode2 = doc.createElement("node");
        protName.appendChild(protNode2);
        this.createElementNode(doc, "name", "protectTrailName", protNode2);
        this.createElementNode(doc, "value", type==1?protTunnelName:"", protNode2);
        //tnpParameters
        Element tnpParameters = doc.createElement("tnpParameters");
        tnp.appendChild(tnpParameters);
        Element oneParam1 = doc.createElement("oneParam");
        tnpParameters.appendChild(oneParam1);
        this.createElementNode(doc, "name", "waitTime", oneParam1);
        this.createElementNode(doc, "value", waitTime, oneParam1);
        Element oneParam2 = doc.createElement("oneParam");
        tnpParameters.appendChild(oneParam2);
        this.createElementNode(doc, "name", "delayTime", oneParam2);
        this.createElementNode(doc, "value", delayTime, oneParam2);
        //additionalInfo
        Element additionalInfo = doc.createElement("additionalInfo");
        tnp.appendChild(additionalInfo);
        Element addOneParam1 = doc.createElement("oneParam");
        additionalInfo.appendChild(addOneParam1);
        this.createElementNode(doc, "name", "activeStatus", addOneParam1);
        this.createElementNode(doc, "value", status, addOneParam1);
        TNPList.appendChild(tnp);
	}

	/**
	 * 根据名称创建元素,并赋值
	 */
	private void createElementNode(Document doc, String childName, String childValue, Element node){
		Element e = doc.createElement(childName);
        e.setTextContent(childValue);
        node.appendChild(e);
	}
}
