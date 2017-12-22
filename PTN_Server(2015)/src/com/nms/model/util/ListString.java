﻿package com.nms.model.util;

import java.util.ArrayList;
import java.util.List;

import com.nms.db.bean.equipment.port.PortInst;
import com.nms.db.bean.equipment.shelf.SiteInst;
import com.nms.db.bean.equipment.slot.SlotInst;
import com.nms.db.bean.path.Segment;
import com.nms.db.bean.report.PathStatisticsWidthRate;
import com.nms.db.bean.report.SSAlarm;
import com.nms.db.bean.report.SSCard;
import com.nms.db.bean.report.SSLabel;
import com.nms.db.bean.report.SSPath;
import com.nms.db.bean.report.SSPort;
import com.nms.db.bean.report.SSProfess;
import com.nms.db.bean.system.OperationLog;
import com.nms.db.bean.system.loginlog.LoginLog;
import com.nms.db.enums.EActiveStatus;
import com.nms.db.enums.EOperationLogType;
import com.nms.db.enums.EServiceType;
import com.nms.model.equipment.port.PortService_MB;
import com.nms.model.equipment.shlef.SiteService_MB;
import com.nms.model.path.SegmentService_MB;
import com.nms.service.impl.util.SiteUtil;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.DateUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.ResourceUtil;
import com.nms.ui.manager.UiUtil;
import com.nms.ui.manager.keys.StringKeysBtn;
import com.nms.ui.manager.keys.StringKeysLbl;
import com.nms.ui.manager.keys.StringKeysObj;
import com.nms.ui.manager.keys.StringKeysTab;

/**
 * 转换类
 * @author sy
 *
 */
public class ListString {
	/**
	 * 将 传人的 List集合（bean） 转为  List(String[])
	 *     作为导出时传人的参数之一
	 * @param list
	 *     传人的 bean对象的 集合
	 * @param tableName
	 *       当多个页面公用一个Bean时
	 *           根据tabelName 判定具体是哪个界面
	 * @return  List<String[]>
	 *     返回：String[]的集合
	 * @throws Exception
	 */
	public  List<String[]>  tranListString(List list,String tableName) throws Exception{
		if(list==null){
			throw new Exception("list is null");
		}
		List<String[]> beanList=new ArrayList<String []>();
		String[] beanData=null;
		//网元配置信息统计： 线缆数目时需要查询
		SegmentService_MB segmentService = null;
		PortService_MB portService = null;
		SiteService_MB siteService = null;
		try {
			if(list.size()>0){
				Object object=list.get(0);
				if(object instanceof SSAlarm){    //导出告警统计
					for(int i=list.size()-1;i>=0;i--){
						beanData=new String[7];
						SSAlarm ssalarm=(SSAlarm) list.get(i);
						beanData[0]=ssalarm.getName();
						beanData[1]=ssalarm.getInstancy()+"";
						beanData[2]=ssalarm.getMostly()+"";
						beanData[3]=ssalarm.getSubordination()+"";
						beanData[4]=ssalarm.getClew()+"";
						beanData[5]=ssalarm.getUnknow()+"";
						beanData[6]=ssalarm.getAlarmCount()+"";
						beanList.add(beanData);
					}				
				}
				else if(object instanceof SSCard){    //导出单板统计
					for(int i=list.size()-1;i>=0;i--){
						beanData=new String[6];
						SSCard sscard=(SSCard) list.get(i);
						beanData[0]=sscard.getCardType();
						beanData[1]=sscard.getCardId();
						beanData[2]=sscard.getVersion();
						beanData[3]=sscard.getHardversion();
						beanData[4]=sscard.getInstalledSerialNumber();
						beanData[5]=sscard.getProtectWay();
						beanList.add(beanData);
					}				
				}
				else if(object instanceof SSLabel){    //导出标签信息统计
					for(int i=list.size()-1;i>=0;i--){
						beanData=new String[6];
						SSLabel sslabel=(SSLabel) list.get(i);
						beanData[0]=sslabel.getSiteName();
						beanData[1]=sslabel.getPortName();
						beanData[2]=sslabel.getLabelType();
						beanData[3]=sslabel.getLspUsed();
						beanData[4]=sslabel.getLspCanUsed();
						beanData[5]=sslabel.getLspCount();
						beanList.add(beanData);
					}				
				}
				else if(object instanceof PathStatisticsWidthRate){    
					for (int i = 0; i < list.size(); i++) {
						PathStatisticsWidthRate pathStatisticsWidthRate=(PathStatisticsWidthRate) list.get(i);
						if(tableName.equals("segmentWidthTable")){
							//物理连接宽带信息统计
							beanData=new String[17];
							beanData[0]=pathStatisticsWidthRate.getRateName();
							beanData[1]=pathStatisticsWidthRate.getForWard_BE();
							beanData[2]=pathStatisticsWidthRate.getForWard_AF1();
							beanData[3]=pathStatisticsWidthRate.getForWard_AF2();
							beanData[4]=pathStatisticsWidthRate.getForWard_AF3();
							beanData[5]=pathStatisticsWidthRate.getForWard_AF4();
							beanData[6]=pathStatisticsWidthRate.getForWard_EF();
							beanData[7]=pathStatisticsWidthRate.getForWard_CS6();
							beanData[8]=pathStatisticsWidthRate.getForWard_CS7();
							beanData[9]=pathStatisticsWidthRate.getBackWard_BE();
							beanData[10]=pathStatisticsWidthRate.getBackWard_AF1();
							beanData[11]=pathStatisticsWidthRate.getBackWard_AF2();
							beanData[12]=pathStatisticsWidthRate.getBackWard_AF3();
							beanData[13]=pathStatisticsWidthRate.getBackWard_AF4();
							beanData[14]=pathStatisticsWidthRate.getBackWard_EF();
							beanData[15]=pathStatisticsWidthRate.getBackWard_CS6();
							beanData[16]=pathStatisticsWidthRate.getBackWard_CS7();
						}else if(tableName.equals("pathWidthTable")){
							//导出服务路径宽带统计
							beanData=new String[18];
							beanData[0]=pathStatisticsWidthRate.getRateName();
							beanData[1]=pathStatisticsWidthRate.getQosType();
							beanData[2]=pathStatisticsWidthRate.getForWard_BE();
							beanData[3]=pathStatisticsWidthRate.getForWard_AF1();
							beanData[4]=pathStatisticsWidthRate.getForWard_AF2();
							beanData[5]=pathStatisticsWidthRate.getForWard_AF3();
							beanData[6]=pathStatisticsWidthRate.getForWard_AF4();
							beanData[7]=pathStatisticsWidthRate.getForWard_EF();
							beanData[8]=pathStatisticsWidthRate.getForWard_CS6();
							beanData[9]=pathStatisticsWidthRate.getForWard_CS7();
							beanData[10]=pathStatisticsWidthRate.getBackWard_BE();
							beanData[11]=pathStatisticsWidthRate.getBackWard_AF1();
							beanData[12]=pathStatisticsWidthRate.getBackWard_AF2();
							beanData[13]=pathStatisticsWidthRate.getBackWard_AF3();
							beanData[14]=pathStatisticsWidthRate.getBackWard_AF4();
							beanData[15]=pathStatisticsWidthRate.getBackWard_EF();
							beanData[16]=pathStatisticsWidthRate.getBackWard_CS6();
							beanData[17]=pathStatisticsWidthRate.getBackWard_CS7();
						}
						
						beanList.add(beanData);
					}				
				}
				else if(object instanceof SSPort){    //导出端口统计
					for(int i=list.size()-1;i>=0;i--){
						beanData=new String[7];
						SSPort ssPort=(SSPort) list.get(i);
						//beanData[0]=ssPort.getId()+"";
						beanData[0]=ssPort.getSiteName();
						beanData[1]=ssPort.getNeType();
						beanData[2]=ssPort.getPortType();
						beanData[3]=ssPort.getPortCount();
						beanData[4]=ssPort.getPortUsed();
						beanData[5]=ssPort.getPortUnUsed();
						beanData[6]=ssPort.getUsedRate();
						beanList.add(beanData);
					}	
				}
				else if(object instanceof SlotInst){    //导出槽位统计
					for(int i=list.size()-1;i>=0;i--){
						beanData=new String[3];
						SlotInst slotInst=(SlotInst) list.get(i);
						beanData[0]=slotInst.getCellid()+"";
						beanData[1]=slotInst.getNumber()+"";
						beanData[2]=slotInst.getCardname()+"";
						beanList.add(beanData);
					}				
				}
				else if(object instanceof SiteInst){    //导出网元配置统计
					if(tableName.equals("siteCount")){
						for(int i=list.size()-1;i>=0;i--){
							SiteInst siteInfo=(SiteInst) list.get(i);
							beanData=new String[3];
							beanData[0]=siteInfo.getCellType()+"";
							beanData[1]=siteInfo.getNeCount()+"";
							beanData[2] = siteInfo.getSitePercent();
							beanList.add(beanData);
						}
					}else{
						segmentService=(SegmentService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SEGMENT);						
						portService = (PortService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORT);
						SiteUtil siteUtil = new SiteUtil();
						for(int i=list.size()-1;i>=0;i--){
							beanData=new String[9];
							List<Segment> segmentList = null;
							SiteInst siteInst=(SiteInst) list.get(i);
							beanData[0]=siteInst.getCellId();
							beanData[1]=siteInst.getSite_Inst_Id()+"";
							beanData[2]=siteInst.getCellType();
							beanData[3]="0".equals(siteUtil.SiteTypeUtil(siteInst.getSite_Inst_Id())+"")?
									ResourceUtil.srcStr(StringKeysObj.STRING_ONLINE):
										ResourceUtil.srcStr(StringKeysObj.STRING_OFFLINE);
							beanData[4]=siteInst.getCellDescribe();
							beanData[5]="255.255.255.0";
							segmentList = segmentService.selectBySite(siteInst.getSite_Inst_Id());
							if(null != segmentList && !segmentList.isEmpty())
							{
								beanData[6]=segmentList.size()+"";
								beanData[7]=getCablePortNames(siteInst.getSite_Inst_Id(),portService,segmentList);
							}else
							{
								beanData[6]=0+"";
								beanData[7]="";
							}
							
							
							beanData[8]=siteInst.getCreateTime();
							beanList.add(beanData);
						}				
					}
						
					
				}
				
				else if(object instanceof Segment){    //导出物理连接信息统计
					for(int i=list.size()-1;i>=0;i--){
						beanData=new String[6];
						Segment segment=(Segment) list.get(i);
						beanData[0]=segment.getNAME();
						beanData[1]=segment.getBANDWIDTH()+"";
						beanData[2]=segment.getShowSiteAname();
						beanData[3]=segment.getShowPortAname();
						beanData[4]=segment.getShowSiteZname();
						beanData[5]=segment.getShowPortZname();
						beanList.add(beanData);
					}				
				}else if(object instanceof LoginLog){    //导出用户登陆日志
					for(int i=list.size()-1;i>=0;i--){
						beanData=new String[7];
						LoginLog loginLog=(LoginLog) list.get(i);
						beanData[0]=loginLog.getUser_name();
						beanData[1]=loginLog.getLoginTime();
						beanData[2]=loginLog.getOutTime();
						beanData[3]=loginLog.getOnLineTime();
						beanData[4]=loginLog.getIP();
						beanData[5]=this.loginState(loginLog.getLoginState());
						beanData[6]=this.loginState(loginLog.getLogoutState());
						beanList.add(beanData);
					}				
				}else if(object instanceof OperationLog){    //导出用户操作日志
					for(int i=list.size()-1;i>=0;i--){
						beanData=new String[6];
						OperationLog operationLog=(OperationLog) list.get(i);
						beanData[0]=operationLog.getUserName();
						beanData[1]=operationLog.getStartTime();
						beanData[2]=operationLog.getOverTime();
						beanData[3]=EOperationLogType.forms(operationLog.getOperationType()).toString();
						String resultName="";
						if(1==operationLog.getOperationResult()){
							resultName=ResourceUtil.srcStr(StringKeysBtn.BTN_EXPORT_ISUCCESS);
							
						}else if(2==operationLog.getOperationResult()){
							resultName=ResourceUtil.srcStr(StringKeysBtn.BTN_EXPORT_FALSE);
						}
						beanData[4]=resultName;
						beanData[5]=operationLog.getIP();
						beanList.add(beanData);
					}				
				}else if(object instanceof SSPath){    //导出端到端和单网元路径数量统计
					for(int i=list.size()-1;i>=0;i--){
						if (tableName.equals("PathNumStatisticsPanel")) {
							beanData=new String[4];
							SSPath sspath=(SSPath) list.get(i);
							beanData[0]=sspath.getPathType();
							beanData[1]=sspath.getPathCount();
							beanData[2]=sspath.getPathUsed();
							beanData[3]=sspath.getPathUnUsed();
							beanList.add(beanData);
						}else if (tableName.equals("NePathNumStatisticsPanel")) {
							beanData=new String[5];
							SSPath sspath=(SSPath) list.get(i);
							beanData[0]=sspath.getSiteName();
							beanData[1]=sspath.getPathType();
							beanData[2]=sspath.getPathCount();
							beanData[3]=sspath.getPathUsed();
							beanData[4]=sspath.getPathUnUsed();
							beanList.add(beanData);
						}
					}				
				}else if(object instanceof SSProfess){
					for(int i=list.size()-1;i>=0;i--){
						beanData=new String[19];
						SSProfess ssProfess=(SSProfess) list.get(i);
						beanData[0]=ssProfess.getName();
						beanData[1]=ssProfess.getDirection()==1?ResourceUtil.srcStr(StringKeysTab.TAB_TWOWAY):ResourceUtil.srcStr(StringKeysTab.TAB_ONEWAY);
						//beanData[2]=ssProfess.getRate()==1?"CES":ResourceUtil.srcStr(StringKeysTab.TAB_FLOWDOMAIN);
						beanData[2]=ssProfess.getRate();
						beanData[3]=ssProfess.getaSiteName();
						beanData[4]=ssProfess.getaPortName();
						beanData[5]=ssProfess.getzSiteName();
						beanData[6]=ssProfess.getzPortName();	
						if(ssProfess.getServiceType()==185){
							beanData[7]=ResourceUtil.srcStr(StringKeysObj.STRING_GENERAL);	
						}else if(ssProfess.getServiceType()==186){
							beanData[7]=ResourceUtil.srcStr(StringKeysLbl.LBL_11_PROTECT);	
						}else if(ssProfess.getServiceType()==5 && ssProfess.getId()==0){
							beanData[7]=ResourceUtil.srcStr(StringKeysObj.STRING_GENERAL);
						}else if(ssProfess.getServiceType()==5 && ssProfess.getId()>0){
							beanData[7]=ResourceUtil.srcStr(StringKeysObj.STRING_MULTISTAGE);
						}else{
						    beanData[7]=EServiceType.from(ssProfess.getServiceType()).toString(); 
						}
						beanData[8]=ssProfess.getAlarmCount()==0?ResourceUtil.srcStr(StringKeysTab.TAB_UNEXIT_ALARM):ResourceUtil.srcStr(StringKeysTab.TAB_EXIT_ALARM);
						beanData[9]=EActiveStatus.forms(ssProfess.getActiveStatus()).toString();
					//	if(ssProfess.getActiveTime() != null){
					//		beanData[10]=DateUtil.strDate(ssProfess.getActiveTime(),DateUtil.FULLTIME);
					//	}else{
					//		beanData[10]="";
					//	}
						if(ssProfess.getCreateTime() != null){
							beanData[10]=DateUtil.strDate(ssProfess.getCreateTime(),DateUtil.FULLTIME);
						}else{
							beanData[10]="";
						}
						beanData[11]=ssProfess.getClientName();
//						beanData[13]=ssProfess.getCos().equals("-")?"-":QosCosLevelEnum.from(Integer.parseInt(ssProfess.getCos())).toString();
//						beanData[14]=ssProfess.getCir();
//						beanData[15]=ssProfess.getCbs();
//						beanData[16]=ssProfess.getEir();
//						beanData[17]=ssProfess.getEbs();
						beanData[18]=ssProfess.getPir();
						beanList.add(beanData);
					}				
				}
				else if(object instanceof PortInst){
					 siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE);
					for(int i=list.size()-1;i>=0;i--){
						beanData=new String[3];
						PortInst portInst=(PortInst) list.get(i);
						beanData[0] = this.getSiteName(portInst.getSiteId(),siteService)+"/"+portInst.getPortName();
						beanData[1] = portInst.getCirCount()+"";
						beanData[2] = portInst.getCirCount()-portInst.getUseCirBandwidth()+"";
						beanList.add(beanData);
					}				
				}
				else{
					throw new Exception(" no find type");
				}
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());
		}finally{
			UiUtil.closeService_MB(segmentService);
			UiUtil.closeService_MB(siteService);
		}
		return beanList;
	}
	
	/**
	 * 登陆或退出状态
	 */
	private String loginState(int state) {
		if(state == 0){
			return "";
		}else if(state == 1){
			return ResourceUtil.srcStr(StringKeysBtn.BTN_EXPORT_FALSE);
		}else{
			return ResourceUtil.srcStr(StringKeysBtn.BTN_EXPORT_ISUCCESS);
		}
	}
	
	//通过网元ID来查询相应的网元名称
	private String getSiteName(int siteId,SiteService_MB siteService){
		
		try {
		 return siteService.select(siteId).getCellId();
		} catch (Exception e) {
			ExceptionManage.dispose(e, this.getClass());	
		}
		return "";
	}
	
	//查缆纤端口
	private String getCablePortNames(int siteId,PortService_MB portService,List<Segment> segmentList)
	{
		String portName = "";
		List<PortInst> portInstList = null;
		List<Integer> portList = null;
		try {
			portList = new ArrayList<Integer>();
			for(Segment segment : segmentList)
			{
				portList.add(segment.getAPORTID());
				portList.add(segment.getZPORTID());
			}
			if(!portList.isEmpty())
			{
			  portInstList = portService.getAllPortByIdsAndSiteId(portList,siteId);
				if(null != portInstList && !portInstList.isEmpty())
				{
					for(int i =0 ;i<portInstList.size() ; i++)
					{
						if(i == 0)
						{
							portName +=portInstList.get(i).getPortName();
						}
						else{
							portName += "/"+portInstList.get(i).getPortName();
						}
					}
				}	
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, getClass());
		}finally
		{
			 portInstList = null;
		}
		return portName;
	}
}
