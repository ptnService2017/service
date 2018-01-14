package com.nms.snmp.ninteface.impl.config;

import com.nms.db.bean.equipment.shelf.SiteInst;
import com.nms.db.bean.ptn.port.PortLagInfo;
import com.nms.model.equipment.shlef.SiteService_MB;
import com.nms.model.ptn.port.PortLagService_MB;
import com.nms.model.util.ServiceFactory;
import com.nms.model.util.Services;
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

public class PTGXml
{
  public static void main(String[] args)
  {
    Mybatis_DBManager.init("127.0.0.1");
    ConstantUtil.serviceFactory = new ServiceFactory();
    SnmpConfig.getInstanse().init();
    new PTGXml().getPTGXml();
  }
  
  public String getPTGXml()
  {
    String filePath = "";
    String version = ResourceUtil.srcStr("LBL_SNMPMODEL_VERSION");
    String[] xmlPath = { "snmpData\\ZJ\\CS\\EB\\OMC\\CM\\"+DateUtil.getDate("yyyyMMdd"), "CM-PTN-PTG-A1-" + version + "-" + XmlUtil.getTime() + ".xml" };
    FileTools fileTools = null;
    try
    {
      filePath = xmlPath[0] + File.separator + xmlPath[1];
      List<PortLagInfo> list = getAll();
      createFile(xmlPath);
      Document doc = getDocument(xmlPath);
      createXML(doc, list);
      XmlUtil.createFile(doc, "CM-PTN-PTG-A1-",filePath);
    }
    catch (Exception e)
    {
      ExceptionManage.dispose(e, getClass());
    }
    return filePath;
  }
  
  private List<PortLagInfo> getAll()
  {
    List<PortLagInfo> list = null;
    PortLagService_MB lagService_MB = null;
    try
    {
    	lagService_MB = (PortLagService_MB)ConstantUtil.serviceFactory.newService_MB(Services.PORTLAG);
    	list = lagService_MB.selectAllPTG();
    }
    catch (Exception e)
    {
      ExceptionManage.dispose(e, getClass());
    }
    finally
    {
      UiUtil.closeService_MB(lagService_MB);
    }
    return list;
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
  
  private void createXML(Document doc, List<PortLagInfo> list)
  {
    doc.setXmlVersion("1.0");
    doc.setXmlStandalone(true);
    Element root = doc.createElement("DataFile");
    root.setAttribute("xmlns:dm", "http://www.tmforum.org/mtop/mtnm/Configure/v1");
    root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
    root.setAttribute("xsi:schemaLocation", "http://www.tmforum.org/mtop/mtnm/Configure/v1 ../Inventory.xsd");
    root.appendChild(XmlUtil.fileHeader(doc,"ProtectGroup"));
    Element emsList = createFileContent(doc, list);
    root.appendChild(emsList);
    doc.appendChild(root);
  }
  
  private Element createFileContent(Document doc, List<PortLagInfo> list)
  {
    Element Objects = doc.createElement("Objects");
    Element ObjectType = doc.createElement("ObjectType");
	ObjectType.setTextContent("PTG");
	Objects.appendChild(ObjectType);
    Element FieldName = doc.createElement("FieldName");
    createElementNode(doc, "N", "lagrmUID", FieldName, "i", "1");
    createElementNode(doc, "N", "NErmUID", FieldName, "i", "2");
    createElementNode(doc, "N", "nativeName", FieldName, "i", "3");
    createElementNode(doc, "N", "reversionMode", FieldName, "i", "4");
    createElementNode(doc, "N", "type", FieldName, "i", "5");
    Objects.appendChild(FieldName);
    
    Element FieldValue = doc.createElement("FieldValue");
    for (PortLagInfo info : list)
    {
      Element Object = doc.createElement("Object");
      Object.setAttribute("rmUID", "3301EBCS1PTG" + info.getId());
      createElementNode(doc, "V", "--", Object, "i", "1");
      createElementNode(doc, "V", "3301EBCS1NEL"+info.getSiteId(), Object, "i", "2");
      createElementNode(doc, "V", "lag/"+info.getId(), Object, "i", "3");
      createElementNode(doc, "V", "RM_REVERTIVE", Object, "i", "4");
      createElementNode(doc, "V", "1:1", Object, "i", "5");
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
