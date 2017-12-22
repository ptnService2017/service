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

import com.nms.db.bean.equipment.card.CardInst;
import com.nms.model.equipment.card.CardService_MB;
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

public class EquipmentConvertXml {
	public static void main(String[] args) {
		ConstantUtil.serviceFactory = new ServiceFactory();
		SnmpConfig.getInstanse().init();
		new EquipmentConvertXml().getEquipmentXml();
	}
	
	public String getEquipmentXml() {
		//CMCC-PTN-NRM-ME-V1.0.0-20140411-1602-P00.xml
		String filePath = "";
		String version = ResourceUtil.srcStr(StringKeysLbl.LBL_SNMPMODEL_VERSION);
		String[] xmlPath = {"snmpData\\ZJ\\CS\\EB\\OMC\\CM\\"+DateUtil.getDate("yyyyMMdd"), "CMCC-PTN-NRM-EQ-"+version+"-"+this.getTime()+".xml"};
		FileTools fileTools = null;
		try {
			filePath = xmlPath[0] + File.separator + xmlPath[1];//生成文件路径
	    	List<CardInst> cardList = this.getCardList();
	    	this.createFile(xmlPath);//根据文件路径和文件名生成xml文件
	    	Document doc = this.getDocument(xmlPath);//生成doucument
		    this.createXML(doc, cardList);//生成xml文件内容
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

    private List<CardInst> getCardList() {
		List<CardInst> cardList = new ArrayList<CardInst>();
		CardService_MB cardService = null;
		try {
			cardService = (CardService_MB) ConstantUtil.serviceFactory.newService_MB(Services.CARD);
			cardList = cardService.select();
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			UiUtil.closeService_MB(cardService);
		}
		return cardList;
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
	private void createXML(Document doc, List<CardInst> cardList) throws Exception{
		doc.setXmlVersion("1.0");
		doc.setXmlStandalone(true);
		Element root = doc.createElement("dm:Descriptor");
		root.setAttribute("xmlns:dm", "http://www.tmforum.org/mtop/mtnm/Configure/v1");
		root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		root.setAttribute("xsi:schemaLocation", "http://www.tmforum.org/mtop/mtnm/Configure/v1 ../Inventory.xsd");
		Element cardNodeList = this.createFileContent(doc, cardList);
		root.appendChild(cardNodeList);
		doc.appendChild(root);
	}
	
	private Element createFileContent(Document doc, List<CardInst> cardList) throws Exception {
		Element equipmentList = doc.createElement("EquipmentList_T");
		String[] Serial = SnmpConfig.getInstanse().getSerialNumber().split(",");
		String[] PartNumber = SnmpConfig.getInstanse().getInstalledPartNumber().split(",");
		String[] sortVersion = SnmpConfig.getInstanse().getInstalledVersion().split(",");
		int i=0;
		SiteService_MB siteService = null; 
		try { 
			siteService = (SiteService_MB)ConstantUtil.serviceFactory.newService_MB(Services.SITE);
			for (CardInst card : cardList) {
				try{
					if(i > cardList.size())
					{
						i = cardList.size() - 2;
						if(i<0)
						{
							i=0;
						}
					}
					if(i>=Serial.length)
					{
						i=Serial.length - 1;
					}
					
					Element equipment = doc.createElement("Equipment_T");
					//name
					Element name = doc.createElement("name");
					Element node1 = doc.createElement("node");
					this.createElementNode(doc, "name", "EMS", node1);
					this.createElementNode(doc, "value", SnmpConfig.getInstanse().getsnmpEmsName(), node1);
					name.appendChild(node1);
					Element node2 = doc.createElement("node");
					this.createElementNode(doc, "name", "Equipment", node2);
					this.createElementNode(doc, "value", card.getId()+"", node2);
					name.appendChild(node2);
					equipment.appendChild(name);
					//userLabel
					this.createElementNode(doc, "userLabel", card.getSnmpName(), equipment);
					//nativeEMSName
					this.createElementNode(doc, "nativeEMSName", card.getSnmpName(), equipment);
					//installedEquipmentObjectType
					this.createElementNode(doc, "installedEquipmentObjectType", card.getCardName(), equipment);
//		        //installedPartNumber
//		        this.createElementNode(doc, "installedPartNumber", PartNumber[i], equipment);
					//硬件版本
					if(card.getCardName().contains("703")){
						this.createElementNode(doc, "installedPartNumber", "DX7.822.010V4"+card.getCardName().subSequence(4, 5), equipment);
//					}else if(card.getCardName().contains("E1")){
//						this.createElementNode(doc, "installedPartNumber", "DX7.03.0003V11", equipment);
					}else{
						this.createElementNode(doc, "installedPartNumber", "DX7.03.0125V12", equipment);
					}
					
					//installedSerialNumber//序列号
					this.createElementNode(doc, "installedSerialNumber", card.getInstalledSerialNumber(), equipment);
					//serviceState 运行状态
					
					if(siteService.queryNeStatus(card.getSiteId()) ==1 ){
						this.createElementNode(doc, "serviceState", "IN_SERVICE", equipment);
					}else{
						this.createElementNode(doc, "serviceState", "OUT_OF_SERVICE", equipment);
					}
					//installedVersion
//		        this.createElementNode(doc, "installedVersion", sortVersion[i], equipment);
					//installedVersion 软件版本
					if(card.getCardName().contains("703")){
						this.createElementNode(doc, "installedVersion", "V1.1.4", equipment);
					}else{
						if(card.getCardName().contains("PWR") || card.getCardName().contains("FAN")){
							this.createElementNode(doc, "installedVersion", " ", equipment);
						}else{
							this.createElementNode(doc, "installedVersion", "V1.0.9", equipment);
						}
					}
					equipmentList.appendChild(equipment);
					
					i++;
				}
				catch(Exception e){
					ExceptionManage.dispose(e, this.getClass());
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		 }finally{
			 UiUtil.closeService_MB(siteService);
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
