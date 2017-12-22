﻿package com.nms.ui.manager.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.nms.db.bean.equipment.card.CardInst;
import com.nms.db.bean.equipment.port.PortInst;
import com.nms.db.bean.equipment.slot.SlotInst;
import com.nms.db.enums.EManufacturer;
import com.nms.model.equipment.shlef.SiteService_MB;
import com.nms.model.util.CodeConfigItem;
import com.nms.model.util.Services;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.UiUtil;
import com.nms.ui.manager.xmlbean.CardXml;
import com.nms.ui.manager.xmlbean.EquipmentType;

public class EquimentDataUtil {

	private Map<String, List<CardXml>> cardSlotMap = null;
	/**
	 * 读取card_name_xmlpath.xml文件 把板卡与xml对应的关系存放到MAP中。 key= 板卡名称_设备类型。 例如 XCT_700b VALUE=此板卡对应的xml配置文件
	 */
	private Map<String, String> cardXmlPathMap = null;

	/**
	 * 获取设备类型，系统加载时调用即可。 如果需要查询可到ConstantUtil.equipmentTypeList中查找
	 * @throws Exception
	 */
	public void loadEquipmentType() throws Exception {

		DocumentBuilderFactory factory = null;
		DocumentBuilder builder = null;
		Document doc = null;
		org.w3c.dom.Element root = null;
		NodeList nodeList = null;
		EquipmentType equipmentType = null;
		org.w3c.dom.Element element = null;
		try {
			if(null==ConstantUtil.equipmentTypeList){
				ConstantUtil.equipmentTypeList = new ArrayList<EquipmentType>();
				factory = DocumentBuilderFactory.newInstance();
				// 使用DocumentBuilderFactory构建DocumentBulider 
				builder = factory.newDocumentBuilder();
				// 使用DocumentBuilder的parse()方法解析文件
				
				CodeConfigItem	codeConfigItem = CodeConfigItem.getInstance();
				
				if(codeConfigItem.getValueByKey("IconImageShowOrHide").equals("2")){                                                                         
					doc = builder.parse(UiUtil.class.getClassLoader().getResource("config/topo/zx_ EquipmentType.xml").toString());
				}else if(codeConfigItem.getValueByKey("IconImageShowOrHide").equals("3")){
					doc = builder.parse(UiUtil.class.getClassLoader().getResource("config/topo/jiujiangshanshui_ EquipmentType.xml").toString());
				}else if(codeConfigItem.getValueByKey("IconImageShowOrHide").equals("4")){
					doc = builder.parse(UiUtil.class.getClassLoader().getResource("config/topo/yixun/EquipmentType.xml").toString());
				}
				else if(codeConfigItem.getValueByKey("IconImageShowOrHide").equals("6"))
				{
					doc = builder.parse(UiUtil.class.getClassLoader().getResource("config/topo/keda_EquipmentType.xml").toString());
				}
				else{
					doc = builder.parse(UiUtil.class.getClassLoader().getResource("config/topo/EquipmentType.xml").toString());
				}
				root = doc.getDocumentElement();
				nodeList = root.getElementsByTagName("equipment");
				
				for (int i = 0; i < nodeList.getLength(); i++) {
					element = (org.w3c.dom.Element) nodeList.item(i);
					equipmentType = new EquipmentType();
					equipmentType.setTypeName(element.getAttribute("typeName"));
					equipmentType.setXmlPath(element.getAttribute("xmlPath"));
					equipmentType.setImagePath(element.getAttribute("imagePath"));
					equipmentType.setCxEquipmentName(element.getAttribute("cxEquipmentName"));
					if (null != element.getAttribute("manufacturer")) {
						if ("CHENXIAO".equals(element.getAttribute("manufacturer"))) {
							equipmentType.setManufacturer(EManufacturer.CHENXIAO.getValue());
						} else {
							equipmentType.setManufacturer(EManufacturer.WUHAN.getValue());
						}
					}
					ConstantUtil.equipmentTypeList.add(equipmentType);
				}
			}

		} catch (Exception e) {
			ExceptionManage.dispose(e, UiUtil.class);
		} finally {
			factory = null;
			builder = null;
			doc = null;
			root = null;
			nodeList = null;
			equipmentType = null;
			element = null;
		}
	}

	/**
	 * 根据设备类型，获取equipmentType对象
	 * @param neType 设备类型。 700A 700B等。。
	 * @return
	 * @throws Exception
	 */
	public EquipmentType getEquipmentType(String neType) throws Exception {
		EquipmentType equipmentType_result=null;
		try {
			for (EquipmentType equipmentType : ConstantUtil.equipmentTypeList) {
				if(neType.equals(equipmentType.getTypeName())){
					equipmentType_result=equipmentType;
					break;
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, UiUtil.class);
		}
		return equipmentType_result;
	}

	// TODO
	/**
	 * 根据槽类型获取卡菜单
	 * 
	 * @param slotType
	 *            槽类型
	 * @return
	 * @throws Exception
	 */
	public List<CardXml> getCardMenu(String slotType) throws Exception {

		if (cardSlotMap == null) {
			cardSlotMap = new HashMap<String, List<CardXml>>();
			DocumentBuilderFactory factory = null;
			DocumentBuilder builder = null;
			Document doc = null;
			org.w3c.dom.Element root = null;
			NodeList nodeList = null;
			org.w3c.dom.Element parent = null;
			NodeList childList = null;
			org.w3c.dom.Element child = null;
			CardXml cardXml = null;
			List<CardXml> cardXmlList = null;

			try {
				factory = DocumentBuilderFactory.newInstance();
				// 使用DocumentBuilderFactory构建DocumentBulider
				builder = factory.newDocumentBuilder();
				// 使用DocumentBuilder的parse()方法解析文件
				doc = builder.parse(UiUtil.class.getClassLoader().getResource("config/topo/slot_card.xml").toString());
				root = doc.getDocumentElement();
				nodeList = root.getElementsByTagName("slotType");

				for (int i = 0; i < nodeList.getLength(); i++) {
					parent = (org.w3c.dom.Element) nodeList.item(i);
					childList = parent.getElementsByTagName("card");
					if (childList.getLength() > 0) {
						cardXmlList = new ArrayList<CardXml>();
						for (int j = 0; j < childList.getLength(); j++) {
							child = (org.w3c.dom.Element) childList.item(j);
							cardXml = new CardXml();
							cardXml.setName(child.getAttribute("name"));
							cardXml.setXmlPath(child.getAttribute("xmlPath"));
							cardXmlList.add(cardXml);
						}
						cardSlotMap.put(parent.getAttribute("name"), cardXmlList);
					}
				}
			} catch (Exception e) {
				ExceptionManage.dispose(e, UiUtil.class);
			} finally {
				factory = null;
				builder = null;
				doc = null;
				root = null;
				nodeList = null;
				parent = null;
				childList = null;
				child = null;
				cardXml = null;
				cardXmlList = null;
			}
		}
		return cardSlotMap.get(slotType);
	}

	/**
	 * 根据卡名称获取到对应的xml文件路径
	 * 
	 * @param cardName
	 * @return
	 * @throws Exception
	 */
	public String getXmlPathByCardName(String cardName) throws Exception {
		if (cardXmlPathMap == null) {
			DocumentBuilderFactory factory = null;
			DocumentBuilder builder = null;
			Document doc = null;
			org.w3c.dom.Element root = null;
			NodeList nodeList = null;
			org.w3c.dom.Element parent = null;
			try {
				cardXmlPathMap = new HashMap<String, String>();
				factory = DocumentBuilderFactory.newInstance();
				// 使用DocumentBuilderFactory构建DocumentBulider
				builder = factory.newDocumentBuilder();
				// 使用DocumentBuilder的parse()方法解析文件
				doc = builder.parse(UiUtil.class.getClassLoader().getResource("config/topo/card_name_xmlpath.xml").toString());
				root = doc.getDocumentElement();
				nodeList = root.getElementsByTagName("card");

				for (int i = 0; i < nodeList.getLength(); i++) {
					parent = (org.w3c.dom.Element) nodeList.item(i);
					cardXmlPathMap.put(parent.getAttribute("name") + "_" + parent.getAttribute("type"), parent.getAttribute("xmlPath"));
				}
			} catch (Exception e) {
				ExceptionManage.dispose(e, UiUtil.class);
			} finally {
				factory = null;
				builder = null;
				doc = null;
				root = null;
				nodeList = null;
				parent = null;
			}
		}
		return cardXmlPathMap.get(cardName);
	}

	/**
	 * 插卡
	 * 
	 * @param xmlPath
	 * @param slotInst
	 * @throws Exception
	 */
	public CardInst addCard(String xmlPath, SlotInst slotInst) throws Exception {

		DocumentBuilderFactory factory = null;
		DocumentBuilder builder = null;
		Document doc = null;
		org.w3c.dom.Element root = null;
		NodeList nodeList = null;
		CardInst cardInst = null;
		org.w3c.dom.Element parent = null;
		NodeList childList = null;
		PortInst portInst = null;
		org.w3c.dom.Element child = null;
		List<PortInst> portInstList = null;
		SiteService_MB siteService = null;
		int codeValue = CodeConfigItem.getInstance().getIconImageShowOrHide();
		try {
			siteService=(SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE);
			factory = DocumentBuilderFactory.newInstance();
			// 使用DocumentBuilderFactory构建DocumentBulider
			builder = factory.newDocumentBuilder();
			// 使用DocumentBuilder的parse()方法解析文件
			doc = builder.parse(this.getClass().getClassLoader().getResource(xmlPath).toString());
			root = doc.getDocumentElement();
			nodeList = root.getElementsByTagName("card");
			if (nodeList.getLength() == 1) {
				parent = (org.w3c.dom.Element) nodeList.item(0);
				portInstList = new ArrayList<PortInst>();
				cardInst = new CardInst();
				cardInst.setEquipId(slotInst.getEquipId());
				cardInst.setSlotId(slotInst.getId());
				cardInst.setSiteId(slotInst.getSiteId());
				cardInst.setSlotInst(slotInst);
				cardInst.setCardName(parent.getAttribute("cardName"));
				cardInst.setCardType(parent.getAttribute("cardType"));
				if((codeValue == 4 || codeValue == 5) && "/com/nms/ui/images/topo/soz/fengshan.png".equals(parent.getAttribute("imagePath"))){
					cardInst.setImagePath("/com/nms/ui/images/topo/soz/fengshan_yixun.png");
				}else{
					cardInst.setImagePath(parent.getAttribute("imagePath"));
				}
				cardInst.setCardx(slotInst.getSlotx());
				cardInst.setCardy(slotInst.getSloty());

				childList = parent.getElementsByTagName("port");
				if (childList.getLength() > 0) {
					for (int j = 0; j < childList.getLength(); j++) {
						child = (org.w3c.dom.Element) childList.item(j);
						portInst = new PortInst();
						portInst.setBandwidth(child.getAttribute("bandWidth"));
						portInst.setEquipId(slotInst.getEquipId());
						portInst.setImagePath(child.getAttribute("imagePath"));
						portInst.setModuleType(child.getAttribute("module"));
						if (child.getAttribute("seq").equals("")) {
							portInst.setPortName(child.getAttribute("portName"));
						} else {
							portInst.setPortName(child.getAttribute("portName") + "." + slotInst.getNumber() + "." + child.getAttribute("seq"));
						}
						portInst.setPortType(child.getAttribute("portType"));

						if (slotInst.getSlotType().contains("710_reserve")) {
							int num = 0;
							if (slotInst.getNumber() == 3) {
								num = 18;
							} else {
								num = 10;
							}
							if (child.getAttribute("number").length() > 0) {
								portInst.setNumber(num + Integer.parseInt(child.getAttribute("number")));
							}
						} else {
							if (child.getAttribute("number").length() > 0) {
								portInst.setNumber(Integer.parseInt(child.getAttribute("number")));
							}
						}

						portInst.setPortx(cardInst.getCardx() + Integer.parseInt(child.getAttribute("x")));
						portInst.setPorty(cardInst.getCardy() + Integer.parseInt(child.getAttribute("y")));
						portInst.setSiteId(slotInst.getSiteId());
						portInst.setSlotNumber(slotInst.getNumber());
						portInst.setIsOccupy(0);

						if (portInst.getPortType().equals("stm-1")) {
							createChildPort(portInst);
						}

						if (siteService.getManufacturer(slotInst.getSiteId()) == EManufacturer.WUHAN.getValue()) {
							if (portInst.getPortType().equals("e1")) {
								portInst.setPortName(portInst.getPortName() + ".1");
								portInstList.add(portInst);
								portInstList.add(this.updateE1(portInst));
							} else {
								portInstList.add(portInst);
							}
						} else {
							portInstList.add(portInst);
							if (portInst.getPortType().equals("e1")) {
								portInstList.add(updateE1(portInst));
							}
						}

					}
				}
				cardInst.setPortList(portInstList);

			}

		} catch (Exception e) {
			ExceptionManage.dispose(e, UiUtil.class);
		} finally {
			UiUtil.closeService_MB(siteService);
		}
		return cardInst;
	}

	/**
	 * 插卡
	 * 
	 * @param xmlPath
	 * @param slotInst
	 * @throws Exception
	 */
	public CardInst setCard(CardInst cardInst,String xmlPath) throws Exception {

		DocumentBuilderFactory factory = null;
		DocumentBuilder builder = null;
		Document doc = null;
		org.w3c.dom.Element root = null;
		NodeList nodeList = null;
		org.w3c.dom.Element parent = null;
		NodeList childList = null;
		PortInst portInst = null;
		org.w3c.dom.Element child = null;
		List<PortInst> portInstList = null;
		SiteService_MB siteService = null;
		try {
			siteService=(SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE);
			factory = DocumentBuilderFactory.newInstance();
			// 使用DocumentBuilderFactory构建DocumentBulider
			builder = factory.newDocumentBuilder();
			// 使用DocumentBuilder的parse()方法解析文件
			doc = builder.parse(this.getClass().getClassLoader().getResource(xmlPath).toString());
			root = doc.getDocumentElement();
			nodeList = root.getElementsByTagName("card");
			if (nodeList.getLength() == 1) {
				parent = (org.w3c.dom.Element) nodeList.item(0);
				portInstList = new ArrayList<PortInst>();

				childList = parent.getElementsByTagName("port");
				if (childList.getLength() > 0) {
					for (int j = 0; j < childList.getLength(); j++) {
						child = (org.w3c.dom.Element) childList.item(j);
						portInst = new PortInst();
						portInst.setBandwidth(child.getAttribute("bandWidth"));
						portInst.setEquipId(cardInst.getSlotInst().getEquipId());
						portInst.setImagePath(child.getAttribute("imagePath"));
						if (child.getAttribute("seq").equals("")) {
							portInst.setPortName(child.getAttribute("portName"));
						} else {
							portInst.setPortName(child.getAttribute("portName") + "." + cardInst.getSlotInst().getNumber() + "." + child.getAttribute("seq"));
						}
						portInst.setPortType(child.getAttribute("portType"));

						if (cardInst.getSlotInst().getSlotType().equals("710_reserve")) {
							int num = 0;
							if (cardInst.getSlotInst().getNumber() == 3) {
								num = 18;
							} else {
								num = 10;
							}
							if (child.getAttribute("number").length() > 0) {
								portInst.setNumber(num + Integer.parseInt(child.getAttribute("number")));
							}
						} else {
							if (child.getAttribute("number").length() > 0) {
								portInst.setNumber(Integer.parseInt(child.getAttribute("number")));
							}
						}

						portInst.setPortx(cardInst.getCardx() + Integer.parseInt(child.getAttribute("x")));
						portInst.setPorty(cardInst.getCardy() + Integer.parseInt(child.getAttribute("y")));
						portInst.setSiteId(cardInst.getSlotInst().getSiteId());
						portInst.setSlotNumber(cardInst.getSlotInst().getNumber());
						portInst.setIsOccupy(0);

						if (portInst.getPortType().equals("stm-1")) {
							createChildPort(portInst);
						}

						if (siteService.getManufacturer(cardInst.getSlotInst().getSiteId()) == EManufacturer.WUHAN.getValue()) {
							if (portInst.getPortType().equals("e1")) {
								portInst.setPortName(portInst.getPortName() + ".1");
								portInstList.add(portInst);
								portInstList.add(this.updateE1(portInst));
							} else {
								portInstList.add(portInst);
							}
						} else {
							portInstList.add(portInst);
							if (portInst.getPortType().equals("e1")) {
								portInstList.add(updateE1(portInst));
							}
						}

					}
				}
				cardInst.setPortList(portInstList);

			}

		} catch (Exception e) {
			ExceptionManage.dispose(e, UiUtil.class);
		} finally {
			UiUtil.closeService_MB(siteService);
		}
		return cardInst;
	}

	private void createChildPort(PortInst portInst) {

		List<PortInst> childPortList = new ArrayList<PortInst>();
		PortInst childPort = null;

		for (int i = 0; i < 64; i++) {
			childPort = new PortInst();
			childPort.setPortType(portInst.getPortType() + "_child");
			childPort.setPortName(portInst.getPortName() + "_" + (i + 1));
			childPort.setImagePath(portInst.getImagePath());
			childPort.setEquipId(portInst.getEquipId());
			childPort.setSiteId(portInst.getSiteId());
			childPortList.add(childPort);
		}
		portInst.setChildPortList(childPortList);
	}

	private PortInst updateE1(PortInst portInst) {
		PortInst portinst_new = new PortInst();

		String portname_front = portInst.getPortName().substring(0, portInst.getPortName().lastIndexOf(".") + 1);
		int portname_after = Integer.parseInt(portInst.getPortName().substring(portInst.getPortName().lastIndexOf(".") + 1, portInst.getPortName().length())) + 1;

		portinst_new.setPortName(portname_front + portname_after);
		portinst_new.setCardId(portInst.getCardId());
		portinst_new.setEquipId(portInst.getEquipId());
		portinst_new.setImagePath(portInst.getImagePath());
		portinst_new.setImpedance(portInst.getImpedance());
		portinst_new.setIsEnabled_code(portInst.getIsEnabled_code());
		portinst_new.setIsOccupy(portInst.getIsOccupy());
		portinst_new.setJobStatus(portInst.getJobStatus());
		portinst_new.setLagId(portInst.getLagId());
		portinst_new.setLagNumber(portInst.getLagNumber());
		portinst_new.setLinecoding(portInst.getLinecoding());
		portinst_new.setManageStatus(portInst.getManageStatus());
		portinst_new.setNumber(portInst.getNumber());
		portinst_new.setParentId(portInst.getParentId());
		portinst_new.setPortStatus(portInst.getPortStatus());
		portinst_new.setPortType(portInst.getPortType());
		portinst_new.setPortx(portInst.getPortx());
		portinst_new.setPorty(portInst.getPorty());
		portinst_new.setSiteId(portInst.getSiteId());
		portinst_new.setSlotNumber(portInst.getSlotNumber());
		return portinst_new;
	}
}
