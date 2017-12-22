package com.nms.drive.analysis.datablock;

import java.util.ArrayList;
import java.util.List;

import com.nms.drive.analysis.xmltool.AnalysisCommandByDriveXml;
import com.nms.drive.analysis.xmltool.AnalysisDriveXmlToCommand;
import com.nms.drive.analysis.xmltool.bean.DriveAttribute;
import com.nms.drive.analysis.xmltool.bean.DriveRoot;
import com.nms.drive.service.bean.PortLAGbuffer;
import com.nms.drive.service.bean.PortLAGexitQueue;
import com.nms.drive.service.bean.PortObject;
import com.nms.drive.service.bean.PortSeniorConfig;
import com.nms.drive.service.bean.UniObject;
import com.nms.drive.service.impl.CoderUtils;

public class AnalysisPortConfig extends AnalysisBase{
	private int dataCount = 100;// 每个数据块的长度
	
	public byte[] analysisPortConfigObjectToCommands(PortObject protObject ,String configXml) throws Exception{
		DriveRoot portConfigDrivRoot = super.LoadDriveXml(configXml);
		String[] priority = protObject.getPortSeniorConfig().getPriority().split(",");
		UniObject uniObject = protObject.getUniObject();
		PortSeniorConfig portSeniorConfig = protObject.getPortSeniorConfig();
		for (int i = 0, j = portConfigDrivRoot.getDriveAttributeList().size(); i < j; i++) {
			DriveAttribute driveAttribute = portConfigDrivRoot.getDriveAttributeList().get(i);
			// 属性赋值
			LanToDriveAttribute(uniObject, driveAttribute);
			PortSeniorConfigToDriveAttribute(portSeniorConfig, driveAttribute, i,priority);
		}
		// 将集合转换成命令
		AnalysisDriveXmlToCommand analysisDriveXmlToCommand = new AnalysisDriveXmlToCommand();
		byte[] dataCommand = analysisDriveXmlToCommand.analysisCommands(portConfigDrivRoot);
		
		CoderUtils.print16String(dataCommand);
		return dataCommand;
	}

	private void PortSeniorConfigToDriveAttribute(PortSeniorConfig portSeniorConfig, DriveAttribute driveAttribute, int i, String[] priority) {
		//优先级赋值
		PriorityTODriveAttribute(driveAttribute,priority);
		
		//出口策略赋值
		PortLAGexitQueueTODriveAttribute(portSeniorConfig, driveAttribute);

		//队列缓存管理策略赋值
		PortLAGbufferTODriveAttribute(portSeniorConfig, driveAttribute);
		
		//其他的一些赋值
		OtherTODriveAttribute(portSeniorConfig, driveAttribute);
	}

	
	/**
	 * 优先级属性TOdriveattribute
	 */
	public void PriorityTODriveAttribute(DriveAttribute driveattribute,String[] priority){
		
			// (LAN1)优先级0
			String str1 = "优先级0";
			if (str1.equals(driveattribute.getDescription())) {
				driveattribute.setValue(priority[0]);
			}

			// (LAN1)优先级1
			String str2 = "优先级1";
			if (str2.equals(driveattribute.getDescription())) {
				driveattribute.setValue(priority[1]);
			}

			// (LAN1)优先级2
			String str3 = "优先级2";
			if (str3.equals(driveattribute.getDescription())) {
				driveattribute.setValue(priority[2]);
			}

			// (LAN1)优先级3
			String str4 = "优先级3";
			if (str4.equals(driveattribute.getDescription())) {
				driveattribute.setValue(priority[3]);
			}

			// (LAN1)优先级4
			String str5 = "优先级4";
			if (str5.equals(driveattribute.getDescription())) {
				driveattribute.setValue(priority[4]);
			}

			// (LAN1)优先级5
			String str6 = "优先级5";
			if (str6.equals(driveattribute.getDescription())) {
				driveattribute.setValue(priority[5]);
			}

			// (LAN1)优先级6
			String str7 = "优先级6";
			if (str7.equals(driveattribute.getDescription())) {
				driveattribute.setValue(priority[6]);
			}

			// (LAN1)优先级7
			String str8 = "优先级7";
			if (str8.equals(driveattribute.getDescription())) {
				driveattribute.setValue(priority[7]);
			}	
	}
	
	/**
	 * 出口策略赋值
	 * @param PortSeniorConfig
	 * @param driveattribute
	 * @param i
	 */
	public void PortLAGexitQueueTODriveAttribute(PortSeniorConfig portSeniorConfig, DriveAttribute driveattribute){
		
			// (LAN1)(出口队列调度策略)(队列0)模式
			String str0_mode = "(出口队列调度策略)(队列0)模式";
			if (str0_mode.equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getExitQueueList().get(0).getMode()+"");
			}
			//(LAN1)(队列0)权重
			String str0_weight = "(队列0)权重";
			if (str0_weight.equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getExitQueueList().get(0).getPopedom()+"");
			}
			
			// (LAN1)(出口队列调度策略)(队列1)模式
			String str1_mode = "(出口队列调度策略)(队列1)模式";
			if (str1_mode.equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getExitQueueList().get(1).getMode()+"");
			}
			//(LAN1)(队列1)权重
			String str1_weight = "(队列1)权重";
			if (str1_weight.equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getExitQueueList().get(1).getPopedom()+"");
			}
			
			// (LAN1)(出口队列调度策略)(队列2)模式
			String str2_mode = "(出口队列调度策略)(队列2)模式";
			if (str2_mode.equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getExitQueueList().get(2).getMode()+"");
			}
			//(LAN1)(队列2)权重
			String str2_weight = "(队列2)权重";
			if (str2_weight.equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getExitQueueList().get(2).getPopedom()+"");
			}

			// (LAN1)(出口队列调度策略)(队列3)模式
			String str3_mode = "(出口队列调度策略)(队列3)模式";
			if (str3_mode.equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getExitQueueList().get(3).getMode()+"");
			}
			//(LAN1)(队列3)权重
			String str3_weight = "(队列3)权重";
			if (str3_weight.equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getExitQueueList().get(3).getPopedom()+"");
			}

			// (LAN1)(出口队列调度策略)(队列4)模式
			String str4_mode = "(出口队列调度策略)(队列4)模式";
			if (str4_mode.equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getExitQueueList().get(4).getMode()+"");
			}
			//(LAN1)(队列4)权重
			String str4_weight = "(队列4)权重"; 
			if (str4_weight.equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getExitQueueList().get(4).getPopedom()+"");
			}

			// (LAN1)(出口队列调度策略)(队列5)模式
			String str5_mode = "(出口队列调度策略)(队列5)模式";
			if (str5_mode.equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getExitQueueList().get(5).getMode()+"");
			}
			//(LAN1)(队列5)权重
			String str5_weight = "(队列5)权重"; 
			if (str5_weight.equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getExitQueueList().get(5).getPopedom()+"");
			}

			// (LAN1)(出口队列调度策略)(队列6)模式
			String str6_mode = "(出口队列调度策略)(队列6)模式";
			if (str6_mode.equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getExitQueueList().get(6).getMode()+"");
			}
			//(LAN1)(队列6)权重
			String str6_weight = "(队列6)权重"; 
			if (str6_weight.equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getExitQueueList().get(6).getPopedom()+"");
			}

			// (LAN1)(出口队列调度策略)(队列7)模式
			String str7_mode = "(出口队列调度策略)(队列7)模式";
			if (str7_mode.equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getExitQueueList().get(7).getMode()+"");
			}
			//(LAN1)(队列7)权重
			String str7_weight = "(队列7)权重"; 
			if (str7_weight.equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getExitQueueList().get(7).getPopedom()+"");
			}
			
			if ("丢弃概率1".equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getExitQueueList().get(0).getDiscardProbability()+"");
			}
			if ("丢弃概率2".equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getExitQueueList().get(1).getDiscardProbability()+"");
			}
			if ("丢弃概率3".equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getExitQueueList().get(2).getDiscardProbability()+"");
			}
			if ("丢弃概率4".equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getExitQueueList().get(3).getDiscardProbability()+"");
			}
			if ("丢弃概率5".equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getExitQueueList().get(4).getDiscardProbability()+"");
			}
			if ("丢弃概率6".equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getExitQueueList().get(5).getDiscardProbability()+"");
			}
			if ("丢弃概率7".equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getExitQueueList().get(6).getDiscardProbability()+"");
			}
			if ("丢弃概率8".equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getExitQueueList().get(7).getDiscardProbability()+"");
			}
	}
	
	/**
	 * 队列缓存管理策略赋值
	 * @param portSeniorConfig
	 * @param driveattribute
	 * @param i
	 */
	public void PortLAGbufferTODriveAttribute(PortSeniorConfig portSeniorConfig, DriveAttribute driveattribute){
		
			// (LAN1)(队列缓存管理策略)(队列0)模式
			String str0_mode = "(队列缓存管理策略)(队列0)模式";
			if (str0_mode.equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getBufferList().get(0).getMode()+"");
			}
			//(LAN1)(队列0)START门限
			String str0_startthreshold = "(队列0)START门限";
			if (str0_startthreshold.equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getBufferList().get(0).getStartThreshold()+"");
			}
			//(LAN1)(队列0)END门限
			String str0_endthreshold = "(队列0)END门限";
			if (str0_endthreshold.equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getBufferList().get(0).getEndThreshold()+"");
			}
			// (LAN1)(队列缓存管理策略)(队列1)模式
			String str1_mode = "(队列缓存管理策略)(队列1)模式";
			if (str1_mode.equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getBufferList().get(1).getMode()+"");
			}
			//(LAN1)(队列1)START门限
			String str1_startthreshold = "(队列1)START门限";
			if (str1_startthreshold.equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getBufferList().get(1).getStartThreshold()+"");
			}
			//(LAN1)(队列1)END门限
			String str1_endthreshold = "(队列1)END门限";
			if (str1_endthreshold.equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getBufferList().get(1).getEndThreshold()+"");
			}
			
			
			// (LAN1)(队列缓存管理策略)(队列2)模式
			String str2_mode = "(队列缓存管理策略)(队列2)模式";
			if (str2_mode.equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getBufferList().get(2).getMode()+"");
			}
			//(LAN1)(队列2)START门限
			String str2_startthreshold = "(队列2)START门限";
			if (str2_startthreshold.equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getBufferList().get(2).getStartThreshold()+"");
			}
			//(LAN1)(队列2)END门限
			String str2_endthreshold = "(队列2)END门限";
			if (str2_endthreshold.equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getBufferList().get(2).getEndThreshold()+"");
			}
			

			// (LAN1)(队列缓存管理策略)(队列3)模式
			String str3_mode = "(队列缓存管理策略)(队列3)模式";
			if (str3_mode.equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getBufferList().get(3).getMode()+"");
			}
			//(LAN1)(队列3)START门限
			String str3_startthreshold = "(队列3)START门限";
			if (str3_startthreshold.equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getBufferList().get(3).getStartThreshold()+"");
			}
			//(LAN1)(队列3)END门限
			String str3_endthreshold = "(队列3)END门限";
			if (str3_endthreshold.equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getBufferList().get(3).getEndThreshold()+"");
			}
			

			// (LAN1)(队列缓存管理策略)(队列4)模式
			String str4_mode = "(队列缓存管理策略)(队列4)模式";
			if (str4_mode.equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getBufferList().get(4).getMode()+"");
			}
			//(LAN1)(队列4)START门限
			String str4_startthreshold = "(队列4)START门限"; 
			if (str4_startthreshold.equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getBufferList().get(4).getStartThreshold()+"");
			}
			//(LAN1)(队列4)END门限
			String str4_endthreshold = "(队列4)END门限";
			if (str4_endthreshold.equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getBufferList().get(4).getEndThreshold()+"");
			}
			

			// (LAN1)(队列缓存管理策略)(队列5)模式
			String str5_mode = "(队列缓存管理策略)(队列5)模式";
			if (str5_mode.equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getBufferList().get(5).getMode()+"");
			}
			//(LAN1)(队列5)START门限
			String str5_startthreshold = "(队列5)START门限"; 
			if (str5_startthreshold.equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getBufferList().get(5).getStartThreshold()+"");
			}
			//(LAN1)(队列5)END门限
			String str5_endthreshold = "(队列5)END门限";
			if (str5_endthreshold.equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getBufferList().get(5).getEndThreshold()+"");
			}
			

			// (LAN1)(队列缓存管理策略)(队列6)模式
			String str6_mode = "(队列缓存管理策略)(队列6)模式";
			if (str6_mode.equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getBufferList().get(6).getMode()+"");
			}
			//(LAN1)(队列6)START门限
			String str6_startthreshold = "(队列6)START门限"; 
			if (str6_startthreshold.equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getBufferList().get(6).getStartThreshold()+"");
			}
			//(LAN1)(队列6)END门限
			String str6_endthreshold = "(队列6)END门限";
			if (str6_endthreshold.equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getBufferList().get(6).getEndThreshold()+"");
			}
			

			// (LAN1)(队列缓存管理策略)(队列7)模式
			String str7_mode = "(队列缓存管理策略)(队列7)模式";
			if (str7_mode.equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getBufferList().get(7).getMode()+"");
			}
			//(LAN1)(队列7)START门限
			String str7_startthreshold = "(队列7)START门限"; 
			if (str7_startthreshold.equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getBufferList().get(7 ).getStartThreshold()+"");
			}
			//(LAN1)(队列7)END门限
			String str7_endthreshold = "(队列7)END门限";
			if (str7_endthreshold.equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getBufferList().get(7).getEndThreshold()+"");
			}
	}
	
	/**
	 * 其他的一些赋值
	 * @param portSeniorConfig
	 * @param driveattribute
	 */
	public void OtherTODriveAttribute(PortSeniorConfig portSeniorConfig, DriveAttribute driveattribute){
		
			// (LAN1)出口限速
			String str1 = "出口限速";
			if (str1.equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getPortLimitation()+"");
			}

			// (LAN1)广播包抑制
			String str2 = "广播包抑制";
			if (str2.equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getBroadcastBate()+"");
			}

			// (LAN1)广播包流量
			String str3 = "广播包流量";
			if (str3.equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getBroadcastFlux()+"");
			}

			// (LAN1)组播包抑制
			String str4 = "组播包抑制";
			if (str4.equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getGroupBroadcastBate()+"");
			}

			// (LAN1)组播包流量
			String str5 = "组播包流量";
			if (str5.equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getGroupBroadcastFlux()+"");
			}

			// (LAN1)洪泛包抑制
			String str6 = "洪泛包抑制";
			if (str6.equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getFloodBate()+"");
			}

			// (LAN1)洪泛包流量
			String str7 = "洪泛包流量";
			if (str7.equals(driveattribute.getDescription())) {
				driveattribute.setValue(portSeniorConfig.getFloodFlux()+"");
			}
	}

	/**
	 * UniObject值赋给driveAttribute
	 * 
	 * @param UniObject
	 * @param driveAttribute
	 */
	public void LanToDriveAttribute(UniObject uniObject, DriveAttribute driveattribute) {

		// 端口号
		if ("端口号".equals(driveattribute.getDescription())) {
			driveattribute.setValue(uniObject.getPortNumber() + "");
		}

		// 端口模式
		if ("端口模式".equals(driveattribute.getDescription())) {
			driveattribute.setValue(uniObject.getPortMode() + "");
		}
		
		// (LAN1)VLAN关联设置
		String str1 = "VLAN关联设置";
		if (str1.equals(driveattribute.getDescription())) {
			driveattribute.setValue(uniObject.getRelevanceVlan() + "");
		}

		// (LAN1)802.1P关联设置
		String str2 = "802.1P关联设置";
		if (str2.equals(driveattribute.getDescription())) {
			driveattribute.setValue(uniObject.getRelevance802_1P() + "");
		}

		// (LAN1)源MAC地址关联设置
		String str3 = "源MAC地址关联设置";
		if (str3.equals(driveattribute.getDescription())) {
			driveattribute.setValue(uniObject.getRelevanceSourceMac() + "");
		}

		// (LAN1)目的MAC地址关联设置
		String str4 = "目的MAC地址关联设置";
		if (str4.equals(driveattribute.getDescription())) {
			driveattribute.setValue(uniObject.getRelevanceTargetMac() + "");
		}

		// (LAN1)源IP地址关联设置
		String str5 = "源IP地址关联设置";
		if (str5.equals(driveattribute.getDescription())) {
			driveattribute.setValue(uniObject.getRelevanceSourceIP() + "");
		}

		// (LAN1)目的IP地址关联设置
		String str6 = "目的IP地址关联设置";
		if (str6.equals(driveattribute.getDescription())) {
			driveattribute.setValue(uniObject.getRelevanceTargetIP() + "");
		}

		// (LAN1)PW关联设置
		String str7 = "PW关联设置";
		if (str7.equals(driveattribute.getDescription())) {
			driveattribute.setValue(uniObject.getRelevancePw() + "");
		}

		// (LAN1)DSCP关联设置
		String str8 = "DSCP关联设置";
		if (str8.equals(driveattribute.getDescription())) {
			driveattribute.setValue(uniObject.getRelevanceDSCP() + "");
		}

		// (LAN1)端口使能
		String str9 = "端口使能";
		if (str9.equals(driveattribute.getDescription())) {
			driveattribute.setValue(uniObject.getPortEnable() + "");
		}

		// (LAN1)工作模式
		String str10 = "工作模式";
		if (str10.equals(driveattribute.getDescription())) {
			driveattribute.setValue(uniObject.getWorkPattern() + "");
		}

		// (LAN1)802.3流控
		String str11 = "802.3流控";
		if (str11.equals(driveattribute.getDescription())) {
			driveattribute.setValue(uniObject.getBuffer802_3Enable() + "");
		}

		// (LAN1)MTU
		String str12 = "MTU";
		if (str12.equals(driveattribute.getDescription())) {
			driveattribute.setValue(uniObject.getMtu() + "");
		}
		
		//QinQ使能
		String str13 = "QinQ使能";
		if(str13.equals(driveattribute.getDescription())){
			driveattribute.setValue(uniObject.getQinqEnable()+"");
		}
		
		//基于PRI到PHB映射
		String str14 = "基于PRI到PHB映射";
		if(str14.equals(driveattribute.getDescription())){
			driveattribute.setValue(uniObject.getMappingEnable()+"");
		}
		
		//激光器关闭使能
		if("激光器关闭使能".equals(driveattribute.getDescription())){
			driveattribute.setValue(uniObject.getLaserEnable()+"");
		}
		//源TCP/UDP端口号关联设置
		if("源TCP/UDP端口号关联设置".equals(driveattribute.getDescription())){
			driveattribute.setValue(uniObject.getSourceTcpPortRelevance()+"");
		}
		//宿TCP/UDP端口号关联设置
		if("宿TCP/UDP端口号关联设置".equals(driveattribute.getDescription())){
			driveattribute.setValue(uniObject.getEndTcpPortRelevance()+"");
		}
		//业务端口环回状态
		if("业务端口环回状态".equals(driveattribute.getDescription())){
			driveattribute.setValue(uniObject.getServicePort()+"");
		}
		//ToS关联设置
		if("ToS关联设置".equals(driveattribute.getDescription())){
			driveattribute.setValue(uniObject.getToSRelevance()+"");
		}
	}

	/**
	 * 命令转对象
	 * @param commands
	 * @param configXml
	 * @return
	 * @throws Exception
	 */
	public PortObject analysisCommandsToPortConfigObject(byte[] commands ,String configXml) throws Exception{
		PortObject protObject = new PortObject();
		UniObject uniObject = new UniObject();
		PortSeniorConfig portSeniorConfig = new PortSeniorConfig();
		List<PortLAGexitQueue> portLAGexitQueues = new ArrayList<PortLAGexitQueue>();
		List<PortLAGbuffer> portLAGbuffers = new ArrayList<PortLAGbuffer>();
		
		StringBuffer priority = new StringBuffer();
		for (int i = 0; i < 8; i++) {
			PortLAGexitQueue portLAGexitQueue = new PortLAGexitQueue();
			PortLAGbuffer portLAGbuffer = new PortLAGbuffer();
			portLAGexitQueues.add(portLAGexitQueue);
			portLAGbuffers.add(portLAGbuffer);
		}
		portSeniorConfig.setExitQueueList(portLAGexitQueues);
		portSeniorConfig.setBufferList(portLAGbuffers);
		byte[] a = new byte[dataCount];
		System.arraycopy(commands, 0, a, 0, a.length);
		
		AnalysisCommandByDriveXml analysisCommandByDriveXml = new AnalysisCommandByDriveXml();
		analysisCommandByDriveXml.setCommands(a);
		DriveRoot driveRoot_config = analysisCommandByDriveXml.analysisDriveAttrbute(configXml);
		for (int i = 0; i < driveRoot_config.getDriveAttributeList().size(); i++) {
			DriveAttribute driveattribute = driveRoot_config.getDriveAttributeList().get(i);
			// 属性赋值
			DriveAttributeToLan(uniObject, driveattribute);
			DriveAttributeTOOther(portSeniorConfig, driveattribute);
			DriveAttributeTOPortLAGbuffer(portSeniorConfig, driveattribute);
			DriveAttributeTOPortLAGexitQueue(portSeniorConfig, driveattribute);
			DriveAttributeTOPriority(driveattribute, priority, portSeniorConfig);
		}
		protObject.setPortSeniorConfig(portSeniorConfig);
		protObject.setUniObject(uniObject);
		return protObject;
		
		
	}
	
	/**
	 * driveAttribute值赋给UniObject
	 * 
	 * @param UniObject
	 * @param driveAttribute
	 */
	public void DriveAttributeToLan(UniObject uniObject, DriveAttribute driveattribute) {
		
		// 端口号
		if ("端口号".equals(driveattribute.getDescription())) {
			uniObject.setPortNumber(Integer.parseInt(driveattribute.getValue()));
		}

		// 端口模式
		if ("端口模式".equals(driveattribute.getDescription())) {
			uniObject.setPortMode(Integer.parseInt(driveattribute.getValue()));
		}
		
		// (LAN1)VLAN关联设置
		String str1 = "VLAN关联设置";
		if (str1.equals(driveattribute.getDescription())) {
			uniObject.setRelevanceVlan(Integer.parseInt(driveattribute.getValue()));
		}

		// 802.1P关联设置
		String str2 = "802.1P关联设置";
		if (str2.equals(driveattribute.getDescription())) {
			uniObject.setRelevance802_1P(Integer.parseInt(driveattribute.getValue()));
		}

		// 源MAC地址关联设置
		String str3 = "源MAC地址关联设置";
		if (str3.equals(driveattribute.getDescription())) {
			uniObject.setRelevanceSourceMac(Integer.parseInt(driveattribute.getValue()));
		}

		// 目的MAC地址关联设置
		String str4 = "目的MAC地址关联设置";
		if (str4.equals(driveattribute.getDescription())) {
			uniObject.setRelevanceTargetMac(Integer.parseInt(driveattribute.getValue()));
		}

		// 源IP地址关联设置
		String str5 = "源IP地址关联设置";
		if (str5.equals(driveattribute.getDescription())) {
			uniObject.setRelevanceSourceIP(Integer.parseInt(driveattribute.getValue()));
		}

		// 目的IP地址关联设置
		String str6 = "目的IP地址关联设置";
		if (str6.equals(driveattribute.getDescription())) {
			uniObject.setRelevanceTargetIP(Integer.parseInt(driveattribute.getValue()));
		}

		// PW关联设置
		String str7 = "PW关联设置";
		if (str7.equals(driveattribute.getDescription())) {
			uniObject.setRelevancePw(Integer.parseInt(driveattribute.getValue()));
		}

		// DSCP关联设置
		String str8 = "DSCP关联设置";
		if (str8.equals(driveattribute.getDescription())) {
			uniObject.setRelevanceDSCP(Integer.parseInt(driveattribute.getValue()));
		}

		// 端口使能
		String str9 = "端口使能";
		if (str9.equals(driveattribute.getDescription())) {
			uniObject.setPortEnable(Integer.parseInt(driveattribute.getValue()));
		}

		// 工作模式
		String str10 = "工作模式";
		if (str10.equals(driveattribute.getDescription())) {
			uniObject.setWorkPattern(Integer.parseInt(driveattribute.getValue()));
		}

		// 802.3流控
		String str11 = "802.3流控";
		if (str11.equals(driveattribute.getDescription())) {
			uniObject.setBuffer802_3Enable(Integer.parseInt(driveattribute.getValue()));
		}

		// MTU
		String str12 = "MTU";
		if (str12.equals(driveattribute.getDescription())) {
			uniObject.setMtu(Integer.parseInt(driveattribute.getValue()));
		}
		
		//QinQ使能
		String str13 = "QinQ使能";
		if(str13.equals(driveattribute.getDescription())){
			uniObject.setQinqEnable(Integer.parseInt(driveattribute.getValue()));
		}
		//基于PRI到PHB映射
		String str14 = "基于PRI到PHB映射";
		if(str14.equals(driveattribute.getDescription())){
			uniObject.setMappingEnable(Integer.parseInt(driveattribute.getValue()));
		}
		
		//激光器关闭使能
		if("激光器关闭使能".equals(driveattribute.getDescription())){
			uniObject.setLaserEnable(Integer.parseInt(driveattribute.getValue()));
		}
		//源TCP/UDP端口号关联设置
		if("源TCP/UDP端口号关联设置".equals(driveattribute.getDescription())){
			uniObject.setSourceTcpPortRelevance(Integer.parseInt(driveattribute.getValue()));
		}
		//宿TCP/UDP端口号关联设置
		if("宿TCP/UDP端口号关联设置".equals(driveattribute.getDescription())){
			uniObject.setEndTcpPortRelevance(Integer.parseInt(driveattribute.getValue()));
		}
		//业务端口环回状态
		if("业务端口环回状态".equals(driveattribute.getDescription())){
			uniObject.setServicePort(Integer.parseInt(driveattribute.getValue()));
		}
		//ToS关联设置
		if("ToS关联设置".equals(driveattribute.getDescription())){
			uniObject.setToSRelevance(Integer.parseInt(driveattribute.getValue()));
		}
	}
	
	/**
	 * 其他的一些赋值
	 * @param portSeniorConfig
	 * @param driveattribute
	 */
	public void DriveAttributeTOOther(PortSeniorConfig portSeniorConfig, DriveAttribute driveattribute){
		
			// (LAN1)出口限速
			String str1 = "出口限速";
			if (str1.equals(driveattribute.getDescription())) {
				portSeniorConfig.setPortLimitation(Integer.parseInt(driveattribute.getValue()));
			}

			// (LAN1)广播包抑制
			String str2 = "广播包抑制";
			if (str2.equals(driveattribute.getDescription())) {
				portSeniorConfig.setBroadcastBate(Integer.parseInt(driveattribute.getValue()));
			}

			// (LAN1)广播包流量
			String str3 = "广播包流量";
			if (str3.equals(driveattribute.getDescription())) {
				portSeniorConfig.setBroadcastFlux(Integer.parseInt(driveattribute.getValue()));
			}

			// (LAN1)组播包抑制
			String str4 = "组播包抑制";
			if (str4.equals(driveattribute.getDescription())) {
				portSeniorConfig.setGroupBroadcastBate(Integer.parseInt(driveattribute.getValue()));
			}

			// (LAN1)组播包流量
			String str5 = "组播包流量";
			if (str5.equals(driveattribute.getDescription())) {
				portSeniorConfig.setGroupBroadcastFlux(Integer.parseInt(driveattribute.getValue()));
			}

			// (LAN1)洪泛包抑制
			String str6 = "洪泛包抑制";
			if (str6.equals(driveattribute.getDescription())) {
				portSeniorConfig.setFloodBate(Integer.parseInt(driveattribute.getValue()));
			}

			// (LAN1)洪泛包流量
			String str7 = "洪泛包流量";
			if (str7.equals(driveattribute.getDescription())) {
				portSeniorConfig.setFloodFlux(Integer.parseInt(driveattribute.getValue()));
			}
	}
	
	/**
	 * 队列缓存管理策略赋值
	 * @param portSeniorConfig
	 * @param driveattribute
	 * @param i
	 */
	public void DriveAttributeTOPortLAGbuffer(PortSeniorConfig portSeniorConfig, DriveAttribute driveattribute){
		
			// (LAN1)(队列缓存管理策略)(队列0)模式
			String str0_mode = "(队列缓存管理策略)(队列0)模式";
			if (str0_mode.equals(driveattribute.getDescription())) {
				portSeniorConfig.getBufferList().get(0).setMode(Integer.parseInt(driveattribute.getValue()));
			}
			//(LAN1)(队列0)START门限
			String str0_startthreshold = "(队列0)START门限";
			if (str0_startthreshold.equals(driveattribute.getDescription())) {
				portSeniorConfig.getBufferList().get(0).setStartThreshold(Integer.parseInt(driveattribute.getValue()));
			}
			//(LAN1)(队列0)END门限
			String str0_endthreshold = "(队列0)END门限";
			if (str0_endthreshold.equals(driveattribute.getDescription())) {
				portSeniorConfig.getBufferList().get(0).setEndThreshold(Integer.parseInt(driveattribute.getValue()));
			}
			// (LAN1)(队列缓存管理策略)(队列1)模式
			String str1_mode = "(队列缓存管理策略)(队列1)模式";
			if (str1_mode.equals(driveattribute.getDescription())) {
				portSeniorConfig.getBufferList().get(1).setMode(Integer.parseInt(driveattribute.getValue()));
			}
			//(LAN1)(队列1)START门限
			String str1_startthreshold = "(队列1)START门限";
			if (str1_startthreshold.equals(driveattribute.getDescription())) {
				portSeniorConfig.getBufferList().get(1).setStartThreshold(Integer.parseInt(driveattribute.getValue()));
			}
			//(LAN1)(队列1)END门限
			String str1_endthreshold = "(队列1)END门限";
			if (str1_endthreshold.equals(driveattribute.getDescription())) {
				portSeniorConfig.getBufferList().get(1).setEndThreshold(Integer.parseInt(driveattribute.getValue()));
			}
			
			
			// (LAN1)(队列缓存管理策略)(队列2)模式
			String str2_mode = "(队列缓存管理策略)(队列2)模式";
			if (str2_mode.equals(driveattribute.getDescription())) {
				portSeniorConfig.getBufferList().get(2).setMode(Integer.parseInt(driveattribute.getValue()));
			}
			//(LAN1)(队列2)START门限
			String str2_startthreshold = "(队列2)START门限";
			if (str2_startthreshold.equals(driveattribute.getDescription())) {
				portSeniorConfig.getBufferList().get(2).setStartThreshold(Integer.parseInt(driveattribute.getValue()));
			}
			//(LAN1)(队列2)END门限
			String str2_endthreshold = "(队列2)END门限";
			if (str2_endthreshold.equals(driveattribute.getDescription())) {
				portSeniorConfig.getBufferList().get(2).setEndThreshold(Integer.parseInt(driveattribute.getValue()));
			}
			

			// (LAN1)(队列缓存管理策略)(队列3)模式
			String str3_mode = "(队列缓存管理策略)(队列3)模式";
			if (str3_mode.equals(driveattribute.getDescription())) {
				portSeniorConfig.getBufferList().get(3).setMode(Integer.parseInt(driveattribute.getValue()));
			}
			//(LAN1)(队列3)START门限
			String str3_startthreshold = "(队列3)START门限";
			if (str3_startthreshold.equals(driveattribute.getDescription())) {
				portSeniorConfig.getBufferList().get(3).setStartThreshold(Integer.parseInt(driveattribute.getValue()));
			}
			//(LAN1)(队列3)END门限
			String str3_endthreshold = "(队列3)END门限";
			if (str3_endthreshold.equals(driveattribute.getDescription())) {
				portSeniorConfig.getBufferList().get(3).setEndThreshold(Integer.parseInt(driveattribute.getValue()));
			}
			

			// (LAN1)(队列缓存管理策略)(队列4)模式
			String str4_mode = "(队列缓存管理策略)(队列4)模式";
			if (str4_mode.equals(driveattribute.getDescription())) {
				portSeniorConfig.getBufferList().get(4).setMode(Integer.parseInt(driveattribute.getValue()));
			}
			//(LAN1)(队列4)START门限
			String str4_startthreshold = "(队列4)START门限"; 
			if (str4_startthreshold.equals(driveattribute.getDescription())) {
				portSeniorConfig.getBufferList().get(4).setStartThreshold(Integer.parseInt(driveattribute.getValue()));
			}
			//(LAN1)(队列4)END门限
			String str4_endthreshold = "(队列4)END门限";
			if (str4_endthreshold.equals(driveattribute.getDescription())) {
				portSeniorConfig.getBufferList().get(4).setEndThreshold(Integer.parseInt(driveattribute.getValue()));
			}
			

			// (LAN1)(队列缓存管理策略)(队列5)模式
			String str5_mode = "(队列缓存管理策略)(队列5)模式";
			if (str5_mode.equals(driveattribute.getDescription())) {
				portSeniorConfig.getBufferList().get(5).setMode(Integer.parseInt(driveattribute.getValue()));
			}
			//(LAN1)(队列5)START门限
			String str5_startthreshold = "(队列5)START门限"; 
			if (str5_startthreshold.equals(driveattribute.getDescription())) {
				portSeniorConfig.getBufferList().get(5).setStartThreshold(Integer.parseInt(driveattribute.getValue()));
			}
			//(LAN1)(队列5)END门限
			String str5_endthreshold = "(队列5)END门限";
			if (str5_endthreshold.equals(driveattribute.getDescription())) {
				portSeniorConfig.getBufferList().get(5).setEndThreshold(Integer.parseInt(driveattribute.getValue()));
			}
			

			// (LAN1)(队列缓存管理策略)(队列6)模式
			String str6_mode = "(队列缓存管理策略)(队列6)模式";
			if (str6_mode.equals(driveattribute.getDescription())) {
				portSeniorConfig.getBufferList().get(6).setMode(Integer.parseInt(driveattribute.getValue()));
			}
			//(LAN1)(队列6)START门限
			String str6_startthreshold = "(队列6)START门限"; 
			if (str6_startthreshold.equals(driveattribute.getDescription())) {
				portSeniorConfig.getBufferList().get(6).setStartThreshold(Integer.parseInt(driveattribute.getValue()));
			}
			//(LAN1)(队列6)END门限
			String str6_endthreshold = "(队列6)END门限";
			if (str6_endthreshold.equals(driveattribute.getDescription())) {
				portSeniorConfig.getBufferList().get(6).setEndThreshold(Integer.parseInt(driveattribute.getValue()));
			}
			

			// (LAN1)(队列缓存管理策略)(队列7)模式
			String str7_mode = "(队列缓存管理策略)(队列7)模式";
			if (str7_mode.equals(driveattribute.getDescription())) {
				portSeniorConfig.getBufferList().get(7).setMode(Integer.parseInt(driveattribute.getValue()));
			}
			//(LAN1)(队列7)START门限
			String str7_startthreshold = "(队列7)START门限"; 
			if (str7_startthreshold.equals(driveattribute.getDescription())) {
				portSeniorConfig.getBufferList().get(7).setStartThreshold(Integer.parseInt(driveattribute.getValue()));
			}
			//(LAN1)(队列7)END门限
			String str7_endthreshold = "(队列7)END门限";
			if (str7_endthreshold.equals(driveattribute.getDescription())) {
				portSeniorConfig.getBufferList().get(7).setEndThreshold(Integer.parseInt(driveattribute.getValue()));
			}
	}
	
	/**
	 * 出口策略赋值
	 * @param PortSeniorConfig
	 * @param driveattribute
	 * @param i
	 */
	public void DriveAttributeTOPortLAGexitQueue(PortSeniorConfig portSeniorConfig, DriveAttribute driveattribute){
		
			// (LAN1)(出口队列调度策略)(队列0)模式
			String str0_mode = "(出口队列调度策略)(队列0)模式";
			if (str0_mode.equals(driveattribute.getDescription())) {
				portSeniorConfig.getExitQueueList().get(0).setMode(Integer.parseInt(driveattribute.getValue()));
			}
			//(LAN1)(队列0)权重
			String str0_weight = "(队列0)权重";
			if (str0_weight.equals(driveattribute.getDescription())) {
				portSeniorConfig.getExitQueueList().get(0).setPopedom(Integer.parseInt(driveattribute.getValue()));
			}
			
			// (LAN1)(出口队列调度策略)(队列1)模式
			String str1_mode = "(出口队列调度策略)(队列1)模式";
			if (str1_mode.equals(driveattribute.getDescription())) {
				portSeniorConfig.getExitQueueList().get(1).setMode(Integer.parseInt(driveattribute.getValue()));
			}
			//(LAN1)(队列1)权重
			String str1_weight = "(队列1)权重";
			if (str1_weight.equals(driveattribute.getDescription())) {
				portSeniorConfig.getExitQueueList().get(1).setPopedom(Integer.parseInt(driveattribute.getValue()));
			}
			
			// (LAN1)(出口队列调度策略)(队列2)模式
			String str2_mode = "(出口队列调度策略)(队列2)模式";
			if (str2_mode.equals(driveattribute.getDescription())) {
				portSeniorConfig.getExitQueueList().get(2).setMode(Integer.parseInt(driveattribute.getValue()));
			}
			//(LAN1)(队列2)权重
			String str2_weight = "(队列2)权重";
			if (str2_weight.equals(driveattribute.getDescription())) {
				portSeniorConfig.getExitQueueList().get(2).setPopedom(Integer.parseInt(driveattribute.getValue()));
			}

			// (LAN1)(出口队列调度策略)(队列3)模式
			String str3_mode = "(出口队列调度策略)(队列3)模式";
			if (str3_mode.equals(driveattribute.getDescription())) {
				portSeniorConfig.getExitQueueList().get(3).setMode(Integer.parseInt(driveattribute.getValue()));
			}
			//(LAN1)(队列3)权重
			String str3_weight = "(队列3)权重";
			if (str3_weight.equals(driveattribute.getDescription())) {
				portSeniorConfig.getExitQueueList().get(3).setPopedom(Integer.parseInt(driveattribute.getValue()));
			}

			// (LAN1)(出口队列调度策略)(队列4)模式
			String str4_mode = "(出口队列调度策略)(队列4)模式";
			if (str4_mode.equals(driveattribute.getDescription())) {
				portSeniorConfig.getExitQueueList().get(4).setMode(Integer.parseInt(driveattribute.getValue()));
			}
			//(LAN1)(队列4)权重
			String str4_weight = "(队列4)权重"; 
			if (str4_weight.equals(driveattribute.getDescription())) {
				portSeniorConfig.getExitQueueList().get(4).setPopedom(Integer.parseInt(driveattribute.getValue()));
			}

			// (LAN1)(出口队列调度策略)(队列5)模式
			String str5_mode = "(出口队列调度策略)(队列5)模式";
			if (str5_mode.equals(driveattribute.getDescription())) {
				portSeniorConfig.getExitQueueList().get(5).setMode(Integer.parseInt(driveattribute.getValue()));
			}
			//(LAN1)(队列5)权重
			String str5_weight = "(队列5)权重"; 
			if (str5_weight.equals(driveattribute.getDescription())) {
				portSeniorConfig.getExitQueueList().get(5).setPopedom(Integer.parseInt(driveattribute.getValue()));
			}

			// (LAN1)(出口队列调度策略)(队列6)模式
			String str6_mode = "(出口队列调度策略)(队列6)模式";
			if (str6_mode.equals(driveattribute.getDescription())) {
				portSeniorConfig.getExitQueueList().get(6).setMode(Integer.parseInt(driveattribute.getValue()));
			}
			//(LAN1)(队列6)权重
			String str6_weight = "(队列6)权重"; 
			if (str6_weight.equals(driveattribute.getDescription())) {
				portSeniorConfig.getExitQueueList().get(6).setPopedom(Integer.parseInt(driveattribute.getValue()));
			}

			// (LAN1)(出口队列调度策略)(队列7)模式
			String str7_mode = "(出口队列调度策略)(队列7)模式";
			if (str7_mode.equals(driveattribute.getDescription())) {
				portSeniorConfig.getExitQueueList().get(7).setMode(Integer.parseInt(driveattribute.getValue()));
			}
			//(LAN1)(队列7)权重
			String str7_weight = "(队列7)权重"; 
			if (str7_weight.equals(driveattribute.getDescription())) {
				portSeniorConfig.getExitQueueList().get(7).setPopedom(Integer.parseInt(driveattribute.getValue()));
			}
			if ("丢弃概率1".equals(driveattribute.getDescription())) {
				portSeniorConfig.getExitQueueList().get(0).setDiscardProbability(Integer.parseInt(driveattribute.getValue()));
			}
			if ("丢弃概率2".equals(driveattribute.getDescription())) {
				portSeniorConfig.getExitQueueList().get(1).setDiscardProbability(Integer.parseInt(driveattribute.getValue()));
			}
			if ("丢弃概率3".equals(driveattribute.getDescription())) {
				portSeniorConfig.getExitQueueList().get(2).setDiscardProbability(Integer.parseInt(driveattribute.getValue()));
			}
			if ("丢弃概率4".equals(driveattribute.getDescription())) {
				portSeniorConfig.getExitQueueList().get(3).setDiscardProbability(Integer.parseInt(driveattribute.getValue()));
			}
			if ("丢弃概率5".equals(driveattribute.getDescription())) {
				portSeniorConfig.getExitQueueList().get(4).setDiscardProbability(Integer.parseInt(driveattribute.getValue()));
			}
			if ("丢弃概率6".equals(driveattribute.getDescription())) {
				portSeniorConfig.getExitQueueList().get(5).setDiscardProbability(Integer.parseInt(driveattribute.getValue()));
			}
			if ("丢弃概率7".equals(driveattribute.getDescription())) {
				portSeniorConfig.getExitQueueList().get(6).setDiscardProbability(Integer.parseInt(driveattribute.getValue()));
			}
			if ("丢弃概率8".equals(driveattribute.getDescription())) {
				portSeniorConfig.getExitQueueList().get(7).setDiscardProbability(Integer.parseInt(driveattribute.getValue()));
			}
	}
	
	/**
	 * 优先级属性TOdriveattribute
	 */
	public void DriveAttributeTOPriority(DriveAttribute driveattribute,StringBuffer priority,PortSeniorConfig portSeniorConfig){
		
			// (LAN1)优先级0
			String str1 = "优先级0";
			if (str1.equals(driveattribute.getDescription())) {
				priority.append(driveattribute.getValue()+",");
			}

			// (LAN1)优先级1
			String str2 = "优先级1";
			if (str2.equals(driveattribute.getDescription())) {
				priority.append(driveattribute.getValue()+",");
			}

			// (LAN1)优先级2
			String str3 = "优先级2";
			if (str3.equals(driveattribute.getDescription())) {
				priority.append(driveattribute.getValue()+",");
			}

			// (LAN1)优先级3
			String str4 = "优先级3";
			if (str4.equals(driveattribute.getDescription())) {
				priority.append(driveattribute.getValue()+",");
			}

			// (LAN1)优先级4
			String str5 = "优先级4";
			if (str5.equals(driveattribute.getDescription())) {
				priority.append(driveattribute.getValue()+",");
			}

			// (LAN1)优先级5
			String str6 = "优先级5";
			if (str6.equals(driveattribute.getDescription())) {
				priority.append(driveattribute.getValue()+",");
			}

			// (LAN1)优先级6
			String str7 = "优先级6";
			if (str7.equals(driveattribute.getDescription())) {
				priority.append(driveattribute.getValue()+",");
			}

			// (LAN1)优先级7
			String str8 = "优先级7";
			if (str8.equals(driveattribute.getDescription())) {
				priority.append(driveattribute.getValue());
				portSeniorConfig.setPriority(priority.toString());
			}	
	}
	
}
