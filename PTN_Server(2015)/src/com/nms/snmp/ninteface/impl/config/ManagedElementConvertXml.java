package com.nms.snmp.ninteface.impl.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nms.db.bean.equipment.shelf.SiteInst;
import com.nms.model.equipment.shlef.SiteService_MB;
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


public class ManagedElementConvertXml {
	public static void main(String[] args) throws Exception {
		ConstantUtil.serviceFactory = new ServiceFactory();
		SnmpConfig.getInstanse().init();
		new ManagedElementConvertXml().getManagedEquipmentXml();
	}
	
	public String getManagedEquipmentXml() {
		//CMCC-PTN-NRM-ME-V1.0.0-20140411-1602-P00.xml
		String filePath = "";
		String version = ResourceUtil.srcStr(StringKeysLbl.LBL_SNMPMODEL_VERSION);
		String[] xmlPath = {"snmpData\\ZJ\\CS\\EB\\OMC\\CM\\"+DateUtil.getDate("yyyyMMdd"), "CMCC-PTN-NRM-ME-"+version+"-"+this.getTime()+".xml"};
		FileTools fileTools = null;
		try {
			filePath = xmlPath[0] + File.separator + xmlPath[1];//生成文件路径
	    	List<SiteInst> siteList = this.getAllSites();
	    	this.createFile(xmlPath);//根据文件路径和文件名生成xml文件
	    	Document doc = this.getDocument(xmlPath);//生成doucument
		    this.createXML(doc, siteList);//生成xml文件内容
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

	private List<SiteInst> getAllSites()
	{
		List<SiteInst> siteList = null;
		SiteService_MB siteService = null;
		try {
			siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE);
			siteList = siteService.select();
		}
	 catch (Exception e) {
		 ExceptionManage.dispose(e, this.getClass());
	} finally {
		UiUtil.closeService_MB(siteService);
	}
		
		return siteList;
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
	private void createXML(Document doc, List<SiteInst> siteList) throws Exception{
		doc.setXmlVersion("1.0");
		doc.setXmlStandalone(true);
		Element root = doc.createElement("dm:Descriptor");
		root.setAttribute("xmlns:dm", "http://www.tmforum.org/mtop/mtnm/Configure/v1");
		root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		root.setAttribute("xsi:schemaLocation", "http://www.tmforum.org/mtop/mtnm/Configure/v1 ../Inventory.xsd");
		Element slotNodeList = this.createFileContent(doc, siteList);
		root.appendChild(slotNodeList);
		doc.appendChild(root);
	}
	
	private Element createFileContent(Document doc, List<SiteInst> siteList) throws Exception {
		Element equipmentList = doc.createElement("ManagedElementList_T");
		for (SiteInst site : siteList) {
			try{
		        Element equipment = doc.createElement("ManagedElement_T");
		      //name
		        Element name = doc.createElement("name");
		        Element node1 = doc.createElement("node");
		        this.createElementNode(doc, "name", "EMS", node1);
		        this.createElementNode(doc, "value", SnmpConfig.getInstanse().getsnmpEmsName(), node1);
		        name.appendChild(node1);
		        Element node2 = doc.createElement("node");
		        this.createElementNode(doc, "name", "ManagedElement", node2);
		        this.createElementNode(doc, "value", site.getSite_Inst_Id()+"", node2);
		        name.appendChild(node2);
		        equipment.appendChild(name);
		        //userLabel
		        this.createElementNode(doc, "userLabel", site.getCellId(), equipment);
		        //nativeEMSName
		        this.createElementNode(doc, "nativeEMSName", site.getCellId(), equipment);
		        //location
		        this.createElementNode(doc, "location", site.getSiteLocation(), equipment);
		        //version
		        this.createElementNode(doc, "version", SnmpConfig.getInstanse().getSoftwareVersion(), equipment);
		        //productName
		        this.createElementNode(doc, "productName", site.getCellType(), equipment);
		        //communicationState
		        String isUsed = site.getLoginstatus()==0 ? "CS_UNAVAILABLE":"CS_AVAILABLE";
		        this.createElementNode(doc, "communicationState", isUsed, equipment);
		        //supportedRates
		        Element supportedRates = doc.createElement("supportedRates");
		        this.createElementNode(doc, "layer", "401", supportedRates);
		        this.createElementNode(doc, "layer", "414", supportedRates);
		        this.createElementNode(doc, "layer", "415", supportedRates);
		        equipment.appendChild(supportedRates);
		        //additionalInfo
		        Element additionalInfo = doc.createElement("additionalInfo");
		        for (int i = 0; i < 3; i++) {
		        	Element oneParam = doc.createElement("oneParam");
		        	if(i == 0){
		        		//METype
		        		this.createElementNode(doc, "name", "METype", oneParam);
		        		this.createElementNode(doc, "value", site.getCellType(), oneParam);
					}else if(i == 1){
						//ManagedIP
						this.createElementNode(doc, "name", "ManagedIP", oneParam);
		        		this.createElementNode(doc, "value", site.getCellDescribe(), oneParam);
					}else if(i == 2){
						//ManagedIPMask
						this.createElementNode(doc, "name", "ManagedIPMask", oneParam);
		        		this.createElementNode(doc, "value", "255.255.255.0", oneParam);
					}
		        	additionalInfo.appendChild(oneParam);
				}
		        equipment.appendChild(additionalInfo);
		        equipmentList.appendChild(equipment);
			}
		    catch(Exception e){
		    	ExceptionManage.dispose(e, this.getClass());
		    }
		 }
		return equipmentList;
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
