package com.nms.snmp.ninteface.impl.config;

import com.nms.db.bean.ptn.path.ces.CesInfo;
import com.nms.model.ptn.path.ces.CesInfoService_MB;
import com.nms.model.util.ServiceFactory;
import com.nms.service.impl.dispatch.rmi.bean.ServiceBean;
import com.nms.snmp.ninteface.framework.SnmpConfig;
import com.nms.snmp.ninteface.util.FileTools;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.DateUtil;
import com.nms.ui.manager.DispatchUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.ResourceUtil;
import com.nms.ui.manager.UiUtil;
import com.nms.util.Mybatis_DBManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.rmi.ConnectException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TDMXml{
  public static void main(String[] args)
  {
    Mybatis_DBManager.init("127.0.0.1");
    ConstantUtil.serviceFactory = new ServiceFactory();
    SnmpConfig.getInstanse().init();
    new TDMXml().getTDMXml();
  }
  
  public String getTDMXml()
  {
    String filePath = "";
    String version = ResourceUtil.srcStr("LBL_SNMPMODEL_VERSION");
    String[] xmlPath = { "snmpData\\ZJ\\CS\\EB\\OMC\\CM\\"+DateUtil.getDate("yyyyMMdd"), "CM-PTN-TDM-A1-" + version + "-" + getTime() + ".xml" };
    FileTools fileTools = null;
    try
    {
      filePath = xmlPath[0] + File.separator + xmlPath[1];
      List<CesInfo> cesinfos = getCESList();
      createFile(xmlPath);
      Document doc = getDocument(xmlPath);
      createXML(doc, cesinfos);
      XmlUtil.createFile(doc, "CM-PTN-TDM-A1-");
    }
    catch (Exception e)
    {
      ExceptionManage.dispose(e, getClass());
    }
    return filePath;
  }
  
  private List<CesInfo> getCESList()
  {
    CesInfoService_MB cesInfoService_MB = null;
    List<CesInfo> cesInfos = null;
    try
    {
      cesInfoService_MB = (CesInfoService_MB)ConstantUtil.serviceFactory.newService_MB(45);
      cesInfos = cesInfoService_MB.select();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      UiUtil.closeService_MB(cesInfoService_MB);
    }
    return cesInfos;
  }
  
  private String getTime()
  {
    SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd-HHmm");
    return format.format(Long.valueOf(System.currentTimeMillis()));
  }
  
  private void createFile(String[] xmlPath)
    throws FileNotFoundException
  {
    File dirname = new File(xmlPath[0]);
    if (!dirname.exists()) {
      dirname.mkdirs();
    }
    new FileOutputStream(xmlPath[0] + File.separator + xmlPath[1]);
  }
  
  private Document getDocument(String[] xmlPath)
    throws Exception
  {
    try
    {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      return builder.newDocument();
    }
    catch (Exception e)
    {
      ExceptionManage.dispose(e, getClass());
    }
    return null;
  }
  
  private void createXML(Document doc, List<CesInfo> cesinfos)
  {
    doc.setXmlVersion("1.0");
    doc.setXmlStandalone(true);
    Element root = doc.createElement("DataFile");
    root.setAttribute("xmlns:dm", "http://www.tmforum.org/mtop/mtnm/Configure/v1");
    root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
    root.setAttribute("xsi:schemaLocation", "http://www.tmforum.org/mtop/mtnm/Configure/v1 ../Inventory.xsd");
    root.appendChild(XmlUtil.fileHeader(doc,"TDM"));
    Element emsList = createFileContent(doc, cesinfos);
    root.appendChild(emsList);
    doc.appendChild(root);
  }
  
  private Element createFileContent(Document doc, List<CesInfo> cesinfos)
  {
    Element Objects = doc.createElement("Objects");
    
    Element FieldName = doc.createElement("FieldName");
    createElementNode(doc, "N", "rmUID", FieldName, "i", "1");
    createElementNode(doc, "N", "nativeName", FieldName, "i", "2");
    createElementNode(doc, "N", "rate", FieldName, "i", "3");
    createElementNode(doc, "N", "direction", FieldName, "i", "4");
    createElementNode(doc, "N", "activeState", FieldName, "i", "5");
    createElementNode(doc, "N", "owner", FieldName, "i", "6");
    createElementNode(doc, "N", "owneSserviceType", FieldName, "i", "7");
    createElementNode(doc, "N", "aEnd1TprmUID", FieldName, "i", "8");
    createElementNode(doc, "N", "aEnd1NermUID", FieldName, "i", "9");
    createElementNode(doc, "N", "aEnd1PortrmUID", FieldName, "i", "10");
    createElementNode(doc, "N", "aEnd1CTPID", FieldName, "i", "11");
    createElementNode(doc, "N", "aEnd2TprmUID", FieldName, "i", "12");
    createElementNode(doc, "N", "aEnd2NermUID", FieldName, "i", "13");
    createElementNode(doc, "N", "aEnd2PortrmUID", FieldName, "i", "14");
    createElementNode(doc, "N", "aEnd2CTPID", FieldName, "i", "15");
    createElementNode(doc, "N", "zEnd1TprmUID", FieldName, "i", "16");
    createElementNode(doc, "N", "zEnd1NermUID", FieldName, "i", "17");
    createElementNode(doc, "N", "zEnd1PortrmUID", FieldName, "i", "18");
    createElementNode(doc, "N", "zEnd1CTPID", FieldName, "i", "19");
    createElementNode(doc, "N", "zEnd2TprmUID", FieldName, "i", "20");
    createElementNode(doc, "N", "zEnd2NermUID", FieldName, "i", "21");
    createElementNode(doc, "N", "zEnd2PortrmUID", FieldName, "i", "22");
    createElementNode(doc, "N", "zEnd2CTPID", FieldName, "i", "23");
    createElementNode(doc, "N", "PW1rmUID", FieldName, "i", "24");
    createElementNode(doc, "N", "PW2rmUI", FieldName, "i", "25");
    Objects.appendChild(FieldName);
    
    Element FieldValue = doc.createElement("FieldValue");
    for (CesInfo cesInfo : cesinfos)
    {
      Element Object = doc.createElement("Object");
      Object.setAttribute("rmUID", "3301EBCS1TDM" + cesInfo.getId());
      createElementNode(doc, "N", "3301EBCS1TDM" + cesInfo.getId(), Object, "i", "1");
      createElementNode(doc, "N", cesInfo.getName(), Object, "i", "2");
      createElementNode(doc, "N", "2M", Object, "i", "3");
      createElementNode(doc, "N", "CD_UNI", Object, "i", "4");
      createElementNode(doc, "N", cesInfo.getActiveStatus() == 1 ? "ACTIVE" : "PENDING", Object, "i", "5");
      createElementNode(doc, "N", "", Object, "i", "6");
      createElementNode(doc, "N", "", Object, "i", "7");
      createElementNode(doc, "N", "3301EBCS1NEL" + cesInfo.getaSiteId(), Object, "i", "8");
      createElementNode(doc, "N", "3301EBCS1NEL" + cesInfo.getaSiteId(), Object, "i", "9");
      createElementNode(doc, "N", "3301EBCS1PRT" + cesInfo.getAxcId(), Object, "i", "10");
      createElementNode(doc, "N", "", Object, "i", "11");
      createElementNode(doc, "N", "", Object, "i", "12");
      createElementNode(doc, "N", "", Object, "i", "13");
      createElementNode(doc, "N", "", Object, "i", "14");
      createElementNode(doc, "N", "", Object, "i", "15");
      createElementNode(doc, "N", "3301EBCS1NEL" + cesInfo.getzSiteId(), Object, "i", "16");
      createElementNode(doc, "N", "3301EBCS1NEL" + cesInfo.getzSiteId(), Object, "i", "17");
      createElementNode(doc, "N", "3301EBCS1PRT" + cesInfo.getZxcId(), Object, "i", "18");
      createElementNode(doc, "N", "", Object, "i", "19");
      createElementNode(doc, "N", "", Object, "i", "20");
      createElementNode(doc, "N", "", Object, "i", "21");
      createElementNode(doc, "N", "", Object, "i", "22");
      createElementNode(doc, "N", "", Object, "i", "23");
      createElementNode(doc, "N", "3301EBCS1PSW" + cesInfo.getPwId(), Object, "i", "24");
      createElementNode(doc, "N", "PW2rmUI", Object, "i", "25");
      FieldValue.appendChild(Object);
    }
    Objects.appendChild(FieldValue);
    return Objects;
  }
  
  private void createElementNode(Document doc, String childName, String childValue, Element node, String attname, String arrvalue)
  {
    Element e = doc.createElement(childName);
    e.setAttribute(attname, arrvalue);
    e.setTextContent(childValue);
    node.appendChild(e);
  }
  
  private boolean isLine()
  {
    String host = ConstantUtil.serviceIp;
    try
    {
      InetAddress address = null;
      if ((host != null) && (host.trim().length() > 0)) {
        address = InetAddress.getByName(host);
      }
      if (address.isReachable(5000)) {
        return true;
      }
    }
    catch (Exception e)
    {
      ExceptionManage.dispose(e, getClass());
    }
    return false;
  }
  
  private boolean verificationRmi()
    throws Exception
  {
    DispatchUtil dispatchUtil = null;
    ServiceBean serviceBean = null;
    boolean flag = false;
    try
    {
      if (!isLine()) {
        return false;
      }
      dispatchUtil = new DispatchUtil("rmiinit");
      serviceBean = dispatchUtil.init();
      if (serviceBean != null) {
        if (serviceBean.getConnectionResult() == 1)
        {
          ConstantUtil.serviceBean = serviceBean;
          flag = true;
        }
        else
        {
          flag = false;
        }
      }
    }
    catch (ConnectException e)
    {
      flag = false;
    }
    finally
    {
      dispatchUtil = null;
      serviceBean = null;
    }
    return flag;
  }
}
