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
import com.nms.db.bean.equipment.shelf.SiteInst;
import com.nms.db.bean.equipment.slot.SlotInst;
import com.nms.model.equipment.card.CardService_MB;
import com.nms.model.equipment.shlef.SiteService_MB;
import com.nms.model.equipment.slot.SlotService_MB;
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


public class EquipmentHolderConvertXml {
	public static void main(String[] args) throws Exception {
		ConstantUtil.serviceFactory = new ServiceFactory();
		new EquipmentHolderConvertXml().getEquipmentHolderXml();
	}
	
	public String getEquipmentHolderXml() {
		//CMCC-PTN-NRM-EH-V1.0.0-20140411-1602.xml
		String filePath = "";
		String version = ResourceUtil.srcStr(StringKeysLbl.LBL_SNMPMODEL_VERSION);
		String[] xmlPath = {"snmpData\\ZJ\\CS\\EB\\OMC\\CM\\"+DateUtil.getDate("yyyyMMdd"), "CMCC-PTN-NRM-EH-"+version+"-"+this.getTime()+".xml"};
		FileTools fileTools = null;
		try {
			filePath = xmlPath[0] + File.separator + xmlPath[1];//生成文件路径
	    	List<SlotInst> slotList = this.getSlotList();
	    	this.createFile(xmlPath);//根据文件路径和文件名生成xml文件
	    	Document doc = this.getDocument(xmlPath);//生成doucument
		    this.createXML(doc, slotList);//生成xml文件内容
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

    private List<SlotInst> getSlotList() {
		List<SlotInst> cardList = new ArrayList<SlotInst>();
		SlotService_MB slotService = null;
		try {
			slotService = (SlotService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SLOT);
			cardList = slotService.select();
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			UiUtil.closeService_MB(slotService);
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
	    	ExceptionManage.dispose(e, getClass());
	    }
		return null;
	}

    /**
     * 根据需求生成相应的xml文件
     * @throws Exception 
     */
    private void createXML(Document doc, List<SlotInst> slotList) throws Exception{
		doc.setXmlVersion("1.0");
		doc.setXmlStandalone(true);
		Element root = doc.createElement("dm:Descriptor");
		root.setAttribute("xmlns:dm", "http://www.tmforum.org/mtop/mtnm/Configure/v1");
		root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		root.setAttribute("xsi:schemaLocation", "http://www.tmforum.org/mtop/mtnm/Configure/v1 ../Inventory.xsd");
		Element cardNodeList = this.createFileContent(doc, slotList);
		root.appendChild(cardNodeList);
		doc.appendChild(root);
	}
	
	private Element createFileContent(Document doc, List<SlotInst> slotList) throws Exception {
		Element equipmentHolderList = doc.createElement("EquipmentOrHolderList_T");
		SiteService_MB siteService = null;
		CardService_MB cardService = null;
		try {
			cardService = (CardService_MB)ConstantUtil.serviceFactory.newService_MB(Services.CARD);
			siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE);
			for (SlotInst slot : slotList) {
				if(slot.getSlotType().equals("710_reserve1")){
					CardInst cardInst = new CardInst();
					cardInst.setId(slot.getCardId());
					List<CardInst> cardInsts = cardService.select(cardInst);
					if(cardInsts != null && cardInsts.size()>0){
						slot.setSnmpName(cardInsts.get(0).getSnmpName());
					}
				}
				try{
					SiteInst siteInst = siteService.select(slot.getSiteId());
					Element equipmentHolder = doc.createElement("EquipmentHolder_T");
					//name
					Element name = doc.createElement("name");
					Element node1 = doc.createElement("node");
					this.createElementNode(doc, "name", "EMS", node1);
					this.createElementNode(doc, "value", SnmpConfig.getInstanse().getsnmpEmsName(), node1);
					name.appendChild(node1);
					Element node2 = doc.createElement("node");
					this.createElementNode(doc, "name", "ManagedElement", node2);
					this.createElementNode(doc, "value", siteInst.getCellId(), node2);
					name.appendChild(node2);
					Element node3 = doc.createElement("node");
					this.createElementNode(doc, "name", "EquipmentHolder", node3);
					this.createElementNode(doc, "value", "/rack="+siteInst.getRack()+"/shelf="+siteInst.getShelf()+"/slot="+slot.getId()+"", node3);
					name.appendChild(node3);
					equipmentHolder.appendChild(name);
					//userLabel
					this.createElementNode(doc, "userLabel", slot.getSnmpName(), equipmentHolder);
					//nativeEMSName
					this.createElementNode(doc, "nativeEMSName", slot.getSnmpName(), equipmentHolder);
					//holderType
					this.createElementNode(doc, "holderType", "slot", equipmentHolder);
					//holderState
					this.createElementNode(doc, "holderState", slot.getCardId() == 0 ?"EMPTY":"INSTALLED_AND_EXPECTED", equipmentHolder);
					
					equipmentHolderList.appendChild(equipmentHolder);
				}
				catch(Exception e){
					ExceptionManage.dispose(e, this.getClass());
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}finally{
			UiUtil.closeService_MB(cardService);
			UiUtil.closeService_MB(siteService);
		}
		return equipmentHolderList;
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
