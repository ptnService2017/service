package com.nms.snmp.ninteface.impl.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.management.AttributeValueExp;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nms.db.bean.equipment.card.CardInst;
import com.nms.db.bean.equipment.port.PortInst;
import com.nms.db.bean.equipment.shelf.SiteInst;
import com.nms.drive.service.impl.CoderUtils;
import com.nms.model.equipment.card.CardService_MB;
import com.nms.model.equipment.port.PortService_MB;
import com.nms.model.equipment.shlef.SiteService_MB;
import com.nms.model.util.ServiceFactory;
import com.nms.model.util.Services;
import com.nms.rmi.ui.util.RmiKeys;
import com.nms.service.impl.dispatch.rmi.bean.ServiceBean;
import com.nms.snmp.ninteface.framework.SnmpConfig;
import com.nms.snmp.ninteface.util.FileTools;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.DateUtil;
import com.nms.ui.manager.DispatchUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.ResourceUtil;
import com.nms.ui.manager.UiUtil;
import com.nms.ui.manager.keys.StringKeysLbl;
import com.nms.util.Mybatis_DBManager;

public class PRTXml {
	public static void main(String[] args) {
		Mybatis_DBManager.init("127.0.0.1");
		ConstantUtil.serviceFactory = new ServiceFactory();
		SnmpConfig.getInstanse().init();
		new PRTXml().getPRTXml();
	}
	
	public String getPRTXml() {
		//CMCC-PTN-NRM-ME-V1.0.0-20140411-1602-P00.xml
		String filePath = "";
		String version = ResourceUtil.srcStr(StringKeysLbl.LBL_SNMPMODEL_VERSION);
		String[] xmlPath = {"snmpData\\ZJ\\CS\\EB\\OMC\\CM\\"+DateUtil.getDate("yyyyMMdd"), "CM-PTN-PRT-A1-"+version+"-"+XmlUtil.getTime()+".xml"};
		FileTools fileTools = null;
		try {
			filePath = xmlPath[0] + File.separator + xmlPath[1];//生成文件路径
			List<PortInst> portInstList = this.getPortList();
	    	this.createFile(xmlPath);//根据文件路径和文件名生成xml文件
	    	Document doc = this.getDocument(xmlPath);//生成doucument
		    this.createXML(doc,portInstList);//生成xml文件内容
		    XmlUtil.createFile(doc, "CM-PTN-PRT-A1-",filePath);
		} catch (Exception e){
			ExceptionManage.dispose(e, this.getClass());
		}
		return filePath;
	}

	private List<PortInst> getPortList() {
		PortService_MB portService = null;
		List<PortInst> portList = new ArrayList<PortInst>();
		try {
			portService = (PortService_MB)ConstantUtil.serviceFactory.newService_MB(Services.PORT);
			PortInst portInst = new PortInst();
			portList = portService.northPort();
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			UiUtil.closeService_MB(portService);
		}
		return portList;
	}
    
	private String getTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd-HHmm");
		return format.format(System.currentTimeMillis());
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
     */
	private void createXML(Document doc,List<PortInst> portInstList){
		doc.setXmlVersion("1.0");
		doc.setXmlStandalone(true);
		Element root = doc.createElement("DataFile");
		root.setAttribute("xmlns:dm", "http://www.tmforum.org/mtop/mtnm/Configure/v1");
		root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		root.setAttribute("xsi:schemaLocation", "http://www.tmforum.org/mtop/mtnm/Configure/v1 ../Inventory.xsd");
		root.appendChild(XmlUtil.fileHeader(doc,"Port"));
		Element emsList = this.createFileContent(doc,portInstList);
		root.appendChild(emsList);
		doc.appendChild(root);
	}
	
	private Element createFileContent(Document doc,List<PortInst> portInstList) {
		Element Objects = doc.createElement("Objects");
		Element ObjectType = doc.createElement("ObjectType");
		ObjectType.setTextContent("PRT");
		Objects.appendChild(ObjectType);
		Element FieldName = doc.createElement("FieldName");
		this.createElementNode(doc, "N", "nermUID", FieldName, "i", "1");
		this.createElementNode(doc, "N", "cardrmUID", FieldName, "i", "2");
		this.createElementNode(doc, "N", "holderrmUID", FieldName, "i", "3");
		this.createElementNode(doc, "N", "portNo", FieldName, "i", "4");
		this.createElementNode(doc, "N", "nativeName", FieldName, "i", "5");
		this.createElementNode(doc, "N", "physicalOrLogical", FieldName, "i", "6");
		this.createElementNode(doc, "N", "portType", FieldName, "i", "7");
		this.createElementNode(doc, "N", "portSubType", FieldName, "i", "8");
		this.createElementNode(doc, "N", "signalType", FieldName, "i", "9");
		this.createElementNode(doc, "N", "portRate", FieldName, "i", "10");
		this.createElementNode(doc, "N", "direction", FieldName, "i", "11");
		this.createElementNode(doc, "N", "role", FieldName, "i", "12");
		this.createElementNode(doc, "N", "IPAddress", FieldName, "i", "13");
		this.createElementNode(doc, "N", "IPMask", FieldName, "i", "14");
		this.createElementNode(doc, "N", "isOverlay", FieldName, "i", "15");
		Objects.appendChild(FieldName);
		
		Element FieldValue = doc.createElement("FieldValue");
		for (PortInst portInst :portInstList) {
			Element Object = doc.createElement("Object");
			Object.setAttribute("rmUID","3301EBCS1PRT"+portInst.getPortId());
			this.createElementNode(doc, "V", "3301EBCS1NEL"+portInst.getSiteId(), Object, "i", "1");
			this.createElementNode(doc, "V", "3301EBCS1CRD"+portInst.getCardId(), Object, "i", "2");
			this.createElementNode(doc, "V", "3301EBCS1CEQH"+portInst.getSlotNumber(), Object, "i", "3");
			this.createElementNode(doc, "V", number(portInst), Object, "i", "4");
			this.createElementNode(doc, "V", portInst.getPortName(), Object, "i", "5");
			this.createElementNode(doc, "V", "ptp", Object, "i", "6");
			this.createElementNode(doc, "V", portInst.getPortName().contains("e1")?"TDM":"ETH", Object, "i", "7");
			this.createElementNode(doc, "V", portInst.getPortName().contains("e1")?"TDM":"ETH", Object, "i", "8");
			String type = "electrical";
			if(portInst.getImagePath().contains("port_l.png") || portInst.getImagePath().contains("port_g.png")){
				type = "optical";
			}
			this.createElementNode(doc, "V", type, Object, "i", "9");
			
			String speed = "GE";
			if(portInst.getPortName().contains("e1.")){
				speed = "2M";
			}else if(portInst.getPortName().contains("fe")){
				speed = "FE";
			}
			this.createElementNode(doc, "V", speed, Object, "i", "10");
			
			this.createElementNode(doc, "V", "D_BIDIRECTIONAL", Object, "i", "11");
			this.createElementNode(doc, "V", role(portInst), Object, "i", "12");
			this.createElementNode(doc, "V", ip(portInst), Object, "i", "13");
			this.createElementNode(doc, "V", portInst.getCirCount() == 1?"255.255.255.0":"--", Object, "i", "14");
			this.createElementNode(doc, "V", "false", Object, "i", "15");
			FieldValue.appendChild(Object);
		}
		
		Objects.appendChild(FieldValue);
		return Objects;
	}
	
	private String ip(PortInst portInst){
		String ip = "--";
		portInst.setCirCount(0);
		if(portInst.getPortName().equals("F1") || portInst.getPortName().equals("F2") 
				|| portInst.getPortName().equals("NMS")){
			ip = portInst.getSnmpName();
			portInst.setCirCount(1);
		}else if(portInst.getPortName().equals("TNT/CON")){
			ip = "10.26.3.234";
			portInst.setCirCount(1);
		}
		return ip;
	}
	
	private String role(PortInst portInst){
		String role = "NA";
		if(portInst.getPortName().equals("F1")){
			role = "Master";
		}else if(portInst.getPortName().equals("F2")){
			role = "Backup";
		}
		return role;
	}
	
	private String number(PortInst portInst){
		String number = portInst.getNumber()+"";
		if(portInst.getPortName().equals("F1")){
			number = "31";
		}else if(portInst.getPortName().equals("F2")){
			number = "32";
		}else if(portInst.getPortName().equals("TNT/CON")){
			number = "33";
		}else if(portInst.getPortName().equals("ALM")){
			number = "34";
		}else if(portInst.getPortName().equals("CLK")){
			number = "35";
		}else if(portInst.getPortName().equals("TOD")){
			number = "36";
		}
		return number;
	}

	/**
	 * 根据名称创建元素,并赋值
	 */
	private void createElementNode(Document doc, String childName, String childValue, Element node,String attname,String arrvalue){
		Element e = doc.createElement(childName);
		e.setAttribute(attname,arrvalue);
        e.setTextContent(childValue);
        node.appendChild(e);
	}
	
	//用于定时来跟新客户端是否是连上服务器
	private boolean isLine(){
		 String host = ConstantUtil.serviceIp;
		 try {
			 InetAddress address = null;
			 if (host != null && host.trim().length() > 0) {
				 address = InetAddress.getByName(host);
			 }
			 if (address.isReachable(5000)){
				 return true;
			 }
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}
		return false;
	 }
	
	//用来验证网管是否正常
	private boolean verificationRmi() throws Exception {
		DispatchUtil dispatchUtil = null;
		ServiceBean serviceBean = null;
		boolean flag = false;
		try {
			if(!isLine()){
				return false;
			}
			dispatchUtil = new DispatchUtil(RmiKeys.RMI_INIT);
			serviceBean = dispatchUtil.init();
			if (null != serviceBean) {
				// 1为成功
				if (serviceBean.getConnectionResult() == 1) {
					ConstantUtil.serviceBean = serviceBean;
					flag = true;
				} else {
					flag = false;
				}
			}

		} catch (java.rmi.ConnectException e) {
			flag = false;
		} finally {
			dispatchUtil = null;
			serviceBean = null;
		}
		return flag;
	}

}
