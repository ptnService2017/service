package com.nms.snmp.ninteface.impl.config;

import com.nms.db.bean.path.Segment;
import com.nms.model.path.SegmentService_MB;
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

public class TPLXml
{
  public static void main(String[] args)
  {
    Mybatis_DBManager.init("127.0.0.1");
    ConstantUtil.serviceFactory = new ServiceFactory();
    SnmpConfig.getInstanse().init();
    new TPLXml().getTPLXml();
  }
  
  public String getTPLXml()
  {
    String filePath = "";
    String version = ResourceUtil.srcStr("LBL_SNMPMODEL_VERSION");
    String[] xmlPath = { "snmpData\\ZJ\\CS\\EB\\OMC\\CM\\"+DateUtil.getDate("yyyyMMdd"), "CM-PTN-TPL-A1-" + version + "-" + XmlUtil.getTime() + ".xml" };
    FileTools fileTools = null;
    try
    {
      filePath = xmlPath[0] + File.separator + xmlPath[1];
      List<Segment> segments = getTPLList();
      createFile(xmlPath);
      Document doc = getDocument(xmlPath);
      createXML(doc, segments);
      XmlUtil.createFile(doc, "CM-PTN-TPI-A1-",filePath);;
    }
    catch (Exception e)
    {
      ExceptionManage.dispose(e, getClass());
    }
    return filePath;
  }
  
  private List<Segment> getTPLList()
  {
    SegmentService_MB segmentService_MB = null;
    List<Segment> mapList = null;
    try
    {
      segmentService_MB = (SegmentService_MB)ConstantUtil.serviceFactory.newService_MB(9);
      mapList = segmentService_MB.select_north();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      UiUtil.closeService_MB(segmentService_MB);
    }
    return mapList;
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
  
  private void createXML(Document doc, List<Segment> segments)
  {
    doc.setXmlVersion("1.0");
    doc.setXmlStandalone(true);
    Element root = doc.createElement("DataFile");
    root.setAttribute("xmlns:dm", "http://www.tmforum.org/mtop/mtnm/Configure/v1");
    root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
    root.setAttribute("xsi:schemaLocation", "http://www.tmforum.org/mtop/mtnm/Configure/v1 ../Inventory.xsd");
    root.appendChild(XmlUtil.fileHeader(doc,"TopoLink"));
    Element emsList = createFileContent(doc, segments);
    root.appendChild(emsList);
    doc.appendChild(root);
  }
  
  private Element createFileContent(Document doc, List<Segment> segments)
  {
    Element Objects = doc.createElement("Objects");
    Element ObjectType = doc.createElement("ObjectType");
	ObjectType.setTextContent("TPL");
	Objects.appendChild(ObjectType);
    Element FieldName = doc.createElement("FieldName");
    createElementNode(doc, "N", "nativeName", FieldName, "i", "1");
    createElementNode(doc, "N", "aEndNermUID", FieldName, "i", "2");
    createElementNode(doc, "N", "zEndNermUID", FieldName, "i", "3");
    createElementNode(doc, "N", "aEndPortrmUID", FieldName, "i", "4");
    createElementNode(doc, "N", "zEndPortrmUID", FieldName, "i", "5");
    createElementNode(doc, "N", "rate", FieldName, "i", "6");
    createElementNode(doc, "N", "direction", FieldName, "i", "7");
    createElementNode(doc, "N", "reality", FieldName, "i", "8");
    Objects.appendChild(FieldName);
    
    Element FieldValue = doc.createElement("FieldValue");
    for (Segment segment : segments)
    {
      Element Object = doc.createElement("Object");
      Object.setAttribute("rmUID", "3301EBCS1TPL" + segment.getId());
      createElementNode(doc, "V", segment.getNAME(), Object, "i", "1");
      createElementNode(doc, "V", "3301EBCS1NEL" + segment.getASITEID(), Object, "i", "2");
      createElementNode(doc, "V", "3301EBCS1NEL" + segment.getZSITEID(), Object, "i", "3");
      createElementNode(doc, "V", "3301EBCS1PRT" + segment.getAPORTID(), Object, "i", "4");
      createElementNode(doc, "V", "3301EBCS1PRT" + segment.getZPORTID(), Object, "i", "5");
      createElementNode(doc, "V", segment.getNAME().contains("ge")?"GE":"FE", Object, "i", "6");
      createElementNode(doc, "V", "CD_BI", Object, "i", "7");
      createElementNode(doc, "V", "real", Object, "i", "8");
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
