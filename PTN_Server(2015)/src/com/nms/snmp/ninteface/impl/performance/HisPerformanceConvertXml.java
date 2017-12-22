package com.nms.snmp.ninteface.impl.performance;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nms.corba.ninterface.util.ELayerRate;
import com.nms.db.bean.equipment.card.CardInst;
import com.nms.db.bean.equipment.shelf.SiteInst;
import com.nms.db.bean.perform.HisPerformanceInfo;
import com.nms.model.equipment.card.CardService_MB;
import com.nms.model.equipment.shlef.SiteService_MB;
import com.nms.model.perform.HisPerformanceService_Mb;
import com.nms.model.util.Services;
import com.nms.snmp.ninteface.framework.SnmpConfig;
import com.nms.snmp.ninteface.util.FileTools;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.ResourceUtil;
import com.nms.ui.manager.UiUtil;
import com.nms.ui.manager.keys.StringKeysLbl;

public class HisPerformanceConvertXml {
	public static void main(String[] args) throws Exception {
	
//		String time = "2014-12-12 12:24:24";
//		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		 SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
//		 System.out.println(sdf1.format(sdf.parse(time))+".0");
	}
	
    /**
     * 调用此方法将数据库中的历史性能数据转换成xml文件
     * CMCC-PTN-PM-ME-V1.0.0-20140411-1602-P00.xml
     * 如果参数为null,则查询所有性能,否则按时间段查询
     * happenedTime数组, 第一个元素是起始时间,第二个元素是结束时间
     * @param taskIdList 
     * @param type 1/2 15min/24hour
     */
	public String getHisPerformanceXml(String[] happenedTime, List<Integer> taskIdList, int type) {
		String filePath = "";
		String version = ResourceUtil.srcStr(StringKeysLbl.LBL_SNMPMODEL_VERSION);
		String[] xmlPath = {"snmpData\\PM", "CMCC-PTN-PM-ME-"+version+".xml"};
		FileTools fileTools = null;
		try {
			if(happenedTime != null){
//				if(type == 1){
//					xmlPath[0] = "snmpData\\hisPerformance_15min";
//				}else{
//					xmlPath[0] = "snmpData\\hisPerformance_24hour";
//				}
				//hisPerformance_2014-08-06 113000.xml
				//hisPerformance_2014-08-06 000000.xml
				xmlPath[1] = "CMCC-PTN-PM-ME-"+version+"-"+this.convertTime(happenedTime[0])+".xml";
			}
			filePath = xmlPath[0] + File.separator + xmlPath[1];//生成文件路径
	    	Map<Integer, List<HisPerformanceInfo>> hisPerformanceList = this.getHisPerformance(happenedTime, taskIdList);//获取历史性能数据
	    	this.createFile(xmlPath);//根据文件路径和文件名生成xml文件
	    	Document doc = this.getDocument(xmlPath);//生成doucument
		    this.createXML(doc, hisPerformanceList);//生成xml文件内容
		    fileTools = new FileTools();
		    fileTools.putFile(doc, filePath);//根据xml文件内容生成对应的文件
		    fileTools.zipFile(filePath, filePath+".zip");//xml文件压缩成zip格式的文件
		} catch (Exception e){
			ExceptionManage.dispose(e, this.getClass());
		}
		return filePath;
	}
	
	/**
     * 将起始时间转换成我们所需的时间
     * 参数time格式为 YYYY-MM-DD HH:MM:SS
     * 转换之后: YYYYMMDD-HHMM
     * @throws Exception 
     */
    public  String convertTime(String time) throws Exception {
		if(time == null || ("").equals(time)){
			return null;
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd-HHmm");   
	        try {   
	            Date date = sdf.parse(time);   
	            return sdf2.format(date);   
	        } catch (Exception e) {   
	            return null;   
	        }   
	    }
	}

	/**
     * 根据条件查询历史性能数据
	 * @param happenedTime 
	 * @param taskIdList 
     */
    private Map<Integer,List<HisPerformanceInfo>> getHisPerformance(String[] happenedTime, List<Integer> taskIdList) {
		List<HisPerformanceInfo> hisPerformanceList = new ArrayList<HisPerformanceInfo>();
		HisPerformanceService_Mb hisPerformanceService = null;
		List<SiteInst> siteInsts = null;
		SiteService_MB siteService = null;
		Map<Integer,List<HisPerformanceInfo>> mapHisPerformanceInfos = new HashMap<Integer, List<HisPerformanceInfo>>();
		try {
			hisPerformanceService = (HisPerformanceService_Mb) ConstantUtil.
			serviceFactory.newService_MB(Services.HisPerformance);
			siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE);
			siteInsts = siteService.select();
			//时间段为null,说明是查询所有
			for(SiteInst siteInst : siteInsts){
				if(happenedTime == null){
					HisPerformanceInfo hisPerformanceInfo = new HisPerformanceInfo();
					hisPerformanceInfo.setSiteId(siteInst.getSite_Inst_Id());
					hisPerformanceList = hisPerformanceService.select(hisPerformanceInfo);
					mapHisPerformanceInfos.put(siteInst.getSite_Inst_Id(), hisPerformanceList);
				}else{
					String startTime = happenedTime[0];
					String endTime = happenedTime[1];
					if(startTime != null && endTime != null){
						hisPerformanceList = hisPerformanceService.selectByTime(startTime, endTime, taskIdList,siteInst.getSite_Inst_Id());
						mapHisPerformanceInfos.put(siteInst.getSite_Inst_Id(), hisPerformanceList);
					}else{
						throw new Exception("the time format is error !");
					}
				}
			}
			
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		} finally {
			UiUtil.closeService_MB(hisPerformanceService);
			UiUtil.closeService_MB(siteService);
		}
		return mapHisPerformanceInfos;
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
	private void createXML(Document doc, Map<Integer, List<HisPerformanceInfo>> hisPerformanceList){
		doc.setXmlVersion("1.0");
		doc.setXmlStandalone(true);
		Element root = doc.createElement("PmFile");
		root.setAttribute("xmlns:dm", "http://www.tmforum.org/mtop/mtnm/HistoryData/v1");
		root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		root.setAttribute("xsi:schemaLocation", "http://www.tmforum.org/mtop/mtnm/HistoryData/v1 ../historyData.xsd");
//		Element FileHeader = this.createFileHeader(doc);
		Element pmList = this.createFileContent(doc, hisPerformanceList);
//		root.appendChild(FileHeader);
		root.appendChild(pmList);
		doc.appendChild(root);
	}
	
//	private Element createFileHeader(Document doc) {
//		Element FileHeader = doc.createElement("FileHeader");
//		this.createElementNode(doc, "InfoModelReferenced", "CMCC-MSS-PM-V3.2.0", FileHeader);
//		this.createElementNode(doc, "DnPrefix", "CMCC BeiJing.XiCheng", FileHeader);
//		this.createElementNode(doc, "SenderName", "CMCC BeiJing.XiCheng\\,SubNetwork=1\\,ManagementNode=1\\,IRPAgent=1", FileHeader);
//		this.createElementNode(doc, "VendorName", "vendorname", FileHeader);
//		this.createElementNode(doc, "JobId", "{200}", FileHeader);
//		this.createElementNode(doc, "BeginTime", this.getCurrTime(), FileHeader);
//		this.createElementNode(doc, "EndTime", this.getCurrTime(), FileHeader);
//		return FileHeader;
//	}
//	
//	private String getCurrTime(){
//		Date dt = new Date();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		return sdf.format(dt);
//	}
	
	private Element createFileContent(Document doc, Map<Integer, List<HisPerformanceInfo>> hisPerformanceList) {
		Element pmList = doc.createElement("PMDataList_T");
		SiteService_MB siteService = null;
		try{
			siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE);
			for(Integer siteId : hisPerformanceList.keySet()){
				SiteInst siteInst = siteService.select(siteId);
				for(HisPerformanceInfo p : hisPerformanceList.get(siteId)){
					Element pm = doc.createElement("PMData_T");
					this.createTpName(doc, pm, p,siteInst);
				    this.createElementNode(doc, "layerRate", this.getLayerRate(p), pm);//层速率
				    this.createElementNode(doc, "granularity", p.getMonitor() == 1 ? "15min" : "24h", pm);//采集粒度 15min/24h
//				    this.createElementNode(doc, "enterprise", p.getSiteName(), pm);//网元标识名
//				    this.createElementNode(doc, "severity", p.getPerformanceCode()+"", pm);//性能代码
				    this.createElementNode(doc, "retrievalTime", this.getRetrievalTime(p.getStartTime()), pm);//采集时间
				    this.createPMMeasurement_T(doc, pm, p);
//				    this.createElementNode(doc, "pmName", p.getCapability().getCapabilityname(), pm);//性能名称
//				    this.createElementNode(doc, "pmValue", this.getPerformanceValue(p), pm);//性能值
//				    this.createElementNode(doc, "eventTime", p.getStartTime(), pm);//性能时间
				    pmList.appendChild(pm); 
				}
				   
			}  
		}catch(Exception e){
	    	ExceptionManage.dispose(e, this.getClass());
	    }finally{
	    	UiUtil.closeService_MB(siteService);
	    }
	    
		
		
		return pmList;
	}

	/**
	 * 根据性能类型获取层速率
	 * @param p 
	 */
	private String getLayerRate(HisPerformanceInfo p) {
		String type = p.getCapability().getCapabilitytype();
		String name = p.getCapability().getCapabilityname();
		if(type.contains("PORT")){
			if(name.contains("2M")){
				//e1端口
				return ELayerRate.TDMPORT.getValue()+"";
			}else{
				//以太网端口
				return ELayerRate.PORT.getValue()+"";
			}
		}else if(type.contains("TMS")){
			//段
			return ELayerRate.TOPOLOGICALLINK.getValue()+"";
		}else if(type.contains("TMP/TMC")){
			if(name.contains("TMP")){
				//tunnel
				return ELayerRate.TUNNEL.getValue()+"";
			}else{
				//pw
				return ELayerRate.PW.getValue()+"";
			}
		}else {
			return 1+"";
		}
	}

	/**
	 * 获取采集时间
	 * @throws ParseException 
	 */
	private String getRetrievalTime(String startTime) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf1.format(sdf.parse(startTime))+".0";
	}
	
	private void createTpName(Document doc, Element pm, HisPerformanceInfo p,SiteInst siteInst) {
		Element tpName = doc.createElement("tpName");
		pm.appendChild(tpName);
		//厂商
		Element EMSNode = doc.createElement("node");
		tpName.appendChild(EMSNode);
		this.createElementNode(doc, "name", "EMS", EMSNode);
		this.createElementNode(doc, "value", SnmpConfig.getInstanse().getEmsNativeName(), EMSNode);
		//网元
		Element MENode = doc.createElement("node");
		tpName.appendChild(MENode);
		this.createElementNode(doc, "name", "ManagedElement", MENode);
		this.createElementNode(doc, "value", p.getSiteName(), MENode);
		//架框槽
		Element SlotNode = doc.createElement("node");
		tpName.appendChild(SlotNode);
		this.createElementNode(doc, "name", "EquipmentHolder", SlotNode);
		this.createElementNode(doc, "value", "/rack="+siteInst.getRack()+"/shelf="+siteInst.getShelf()+"/slot="+p.getSlotId(), SlotNode);
		//板卡
		Element cardNode = doc.createElement("node");
		tpName.appendChild(cardNode);
		this.createElementNode(doc, "name", "Equipment", cardNode);
		this.createElementNode(doc, "value", this.getCardName(p.getSlotId()), cardNode);
	}

	private String getCardName(int slotId) {
		CardService_MB cardService = null;
		try {
			cardService = (CardService_MB) ConstantUtil.serviceFactory.newService_MB(Services.CARD);
			CardInst card = new CardInst();
			card.setSlotId(slotId);
			List<CardInst> cardList = cardService.select(card);
			if(cardList.size() > 0){
				return cardList.get(0).getId()+"";
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		} finally {
			UiUtil.closeService_MB(cardService);
		}
		return "";
	}

	private void createPMMeasurement_T(Document doc, Element pm, HisPerformanceInfo p) {
		Element PMMeasurement_TList = doc.createElement("pmMeasurementList");
		pm.appendChild(PMMeasurement_TList);
		Element PMMeasurement_T = doc.createElement("PMMeasurement_T");
		PMMeasurement_TList.appendChild(PMMeasurement_T);
		this.createElementNode(doc, "pmParameterName", p.getCapability().getCapabilityname(), PMMeasurement_T);
		this.createElementNode(doc, "pmLocation", p.getObjectName(), PMMeasurement_T);
		this.createElementNode(doc, "value", this.getPerformanceValue(p), PMMeasurement_T);
		this.createElementNode(doc, "unit", p.getCapability().getUnitName(), PMMeasurement_T);
		this.createElementNode(doc, "intervalStatus", "Valid", PMMeasurement_T);
	}

	/**
	 * 获取性能值和单位
	 */
	private String getPerformanceValue(HisPerformanceInfo p) {
		String unitName = p.getCapability().getUnitName();
		float perValue = p.getPerformanceValue();
		int perCode = p.getPerformanceCode();
		if(unitName.trim().toString().equals("%")){
			return ""+perValue/100;
		}else{
			if(perCode == 80 || perCode == 81 || perCode == 82 || perCode == 83
			  || perCode == 84 || perCode == 85 || perCode == 89 || perCode == 153){
				 // float  b   =  (float)(Math.round(getPerformanceValue*1000))/1000;(这里的100就是2位小数点,如果要其它位,如4位,这里两个100改成10000)
				 // float   f   =  34.232323;  
				 // float   f1   =  b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();  
				 // b.setScale(2,  BigDecimal.ROUND_HALF_UP) 表明四舍五入，保留两位小数
				BigDecimal b = new BigDecimal(perValue);  
				float f1 = b.setScale(3, BigDecimal.ROUND_HALF_UP).floatValue();
				return String.valueOf(f1);
			}else{
				return String.valueOf(perValue).trim().split("\\.")[0];
			}
		}
	}
	
	/**
	 * 根据名称创建元素,并赋值
	 */
	private void createElementNode(Document doc, String childName, String childValue, Element pmInfo){
		Element e = doc.createElement(childName);
        e.setTextContent(childValue);
        pmInfo.appendChild(e);
	}
}
