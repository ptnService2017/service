﻿package com.nms.drive.analysis.datablock;


import java.util.ArrayList;
import java.util.List;

import com.nms.drive.analysis.AnalysisObjectService;
import com.nms.drive.service.bean.ARPObject;
import com.nms.drive.service.bean.AclObject;
import com.nms.drive.service.bean.BfdObject;
import com.nms.drive.service.bean.BlackWhiteMacObject;
import com.nms.drive.service.bean.CccObject;
import com.nms.drive.service.bean.ETHOAMAllObject;
import com.nms.drive.service.bean.EthServiceObject;
import com.nms.drive.service.bean.L2CPinfoObject;
import com.nms.drive.service.bean.MacManageObject;
import com.nms.drive.service.bean.PmValueLimiteObject;
import com.nms.drive.service.bean.Port2LayerObject;
import com.nms.drive.service.bean.PortObject;
import com.nms.drive.service.bean.QinQObject;
import com.nms.drive.service.bean.RoundProtectionObject;
import com.nms.drive.service.bean.SecondMacStudyObject;
import com.nms.drive.service.bean.TMSOAMObject;
import com.nms.drive.service.impl.CoderUtils;
import com.nms.service.bean.ActionObject;

/**
 * 
 * WS查询某NE某盘活动配置参数
 * 
 * @author 彭冲
 * 
 */

public class AnalysisAllBusinessTable extends AnalysisBase {
	
	private int control = 5;//盘描述
	private int dataBlockHeadLength = 25;//配置块头信息字节长度
	private int controlPanelLength = 100;//盘头信息字节长度
	private int numberLength = 1;//配置块的个数字节长度
	public ActionObject analysisCommandToObject(byte[] cammands) throws Exception{
		ActionObject actionObject = new ActionObject();
		CoderUtils.print16String(cammands);
		if(cammands.length>106){
			int number = cammands[control+numberLength+controlPanelLength-1];//配置块的个数
			int length = 0;//纯数据的起始点位置
			int start = controlPanelLength+control+numberLength;
			int end = controlPanelLength+control+numberLength;
			List<PortObject> protObjects = new ArrayList<PortObject>();
			List<TMSOAMObject> tmsoamObjects = new ArrayList<TMSOAMObject>();
			for (int i = 0; i < number; i++) {
				byte[] dataCammands = null;		
				byte[] dataBlockHead = new byte[dataBlockHeadLength];
				System.arraycopy(cammands,start+length, dataBlockHead, 0, dataBlockHeadLength);
				//配置数据块1标识码
				int getidentifier = CoderUtils.bytesToInt(dataBlockHead[2]);
				if(getidentifier == 3){//全局配置块
					dataCammands = getDataCammands(dataBlockHead, cammands, end, dataCammands);
					AnalysisObjectService analysisObjectService = new AnalysisObjectService();
					actionObject.setGlobalObject(analysisObjectService.analysisCommandToObject(dataCammands));
					length = length+dataCammands.length+25;
//				}else if(getidentifier == 4){//环网保护配置块
//					dataCammands = getDataCammands(dataBlockHead, cammands, end, dataCammands);
//					AnalysisObjectService analysisObjectService = new AnalysisObjectService();
//					actionObject.setRoundProtectionObject(analysisObjectService.analysisCommandToRoundP(dataCammands));
//					length = length+dataCammands.length+25;
				}else if(getidentifier == 5){//LSP保护配置块
					dataCammands = getDataCammands(dataBlockHead, cammands, end, dataCammands);
					AnalysisObjectService analysisObjectService = new AnalysisObjectService();
					actionObject.setLSPProtectionList(analysisObjectService.analysisCommandToLSPProtection(dataCammands));
					length = length+dataCammands.length+25;
				}else if(getidentifier == 6){//tunnel配置块
					dataCammands = getDataCammands(dataBlockHead, cammands, end, dataCammands);
					AnalysisObjectService analysisObjectService = new AnalysisObjectService();
					actionObject.setTunnelObjectList(analysisObjectService.AnalysisCommandToTunnal(dataCammands));
					length = length+dataCammands.length+25;
				}else if(getidentifier == 7){//VPWS业务配置块
					dataCammands = getDataCammands(dataBlockHead, cammands, end, dataCammands);
					AnalysisObjectService analysisObjectService = new AnalysisObjectService();
					actionObject.setElineObjectList(analysisObjectService.AnalysisCommandToELine(dataCammands));
					length = length+dataCammands.length+25;
				}else if(getidentifier == 8){//VPLS业务配置块
					dataCammands = getDataCammands(dataBlockHead, cammands, end, dataCammands);
					AnalysisObjectService analysisObjectService = new AnalysisObjectService();
					actionObject.setEtreeObjectList(analysisObjectService.analysisCommandToETree(dataCammands));
					length = length+dataCammands.length+25;
				}else if(getidentifier == 9){//PHB到TMC/TMP EXP映射表
					dataCammands = getDataCammands(dataBlockHead, cammands, end, dataCammands);
					AnalysisObjectService analysisObjectService = new AnalysisObjectService();
					actionObject.setPhbToEXPObject(analysisObjectService.analysisCommandToPHB(dataCammands));
					length = length+dataCammands.length+25;
				}else if(getidentifier == 10){//TMP EXP到PHB映射表
					dataCammands = getDataCammands(dataBlockHead, cammands, end, dataCammands);
					AnalysisObjectService analysisObjectService = new AnalysisObjectService();
					actionObject.setExpToPHBObject(analysisObjectService.analysisCommandToEXPObject(dataCammands));
					length = length+dataCammands.length+25;
				}else if(getidentifier == 11){//静态单播
					dataCammands = getDataCammands(dataBlockHead, cammands, end, dataCammands);
					AnalysisObjectService analysisObjectService = new AnalysisObjectService();
					actionObject.setStaticUnicast(analysisObjectService.analysisCommandToStaticU(dataCammands));
					length = length+dataCammands.length+25;
				}else if(getidentifier == 12){//静态组播
					dataCammands = getDataCammands(dataBlockHead, cammands, end, dataCammands);
					AnalysisObjectService analysisObjectService = new AnalysisObjectService();
					actionObject.setGroupSpreadObject(analysisObjectService.analysisCommandToGroupSpread(dataCammands));
					length = length+dataCammands.length+25;
				}else if(getidentifier == 31){//以太网OAM配置
					dataCammands = getDataCammands(dataBlockHead, cammands, end, dataCammands);
					AnalysisObjectService analysisObjectService = new AnalysisObjectService();
				    ETHOAMAllObject ethoamAllObject = new ETHOAMAllObject();
				    ethoamAllObject.getEthoamObjectList().addAll(analysisObjectService.AnalysisCommandToEthOam(dataCammands));
					actionObject.setEthoamAllObject(ethoamAllObject);
					length = length+dataCammands.length+25;
				}else if(getidentifier == 14){//TMC通道保护
					dataCammands = getDataCammands(dataBlockHead, cammands, end, dataCammands);
					AnalysisObjectService analysisObjectService = new AnalysisObjectService();
					actionObject.setTmcTunnelProectList(analysisObjectService.AnalysisCommandsToTMCTunnelProtectObject(dataCammands));
					length = length+dataCammands.length+25;
				}else if(getidentifier == 15){//TMP OAM故障管理配置
					dataCammands = getDataCammands(dataBlockHead, cammands, end, dataCammands);
					AnalysisObjectService analysisObjectService = new AnalysisObjectService();
					actionObject.setTmpOAMBugControlObject(analysisObjectService.analysisCommandToTMPOAMBugControl(dataCammands));
					length = length+dataCammands.length+25;
				}else if(getidentifier == 16){//TMC OAM故障管理配置
					dataCammands = getDataCammands(dataBlockHead, cammands, end, dataCammands);
					AnalysisObjectService analysisObjectService = new AnalysisObjectService();
					actionObject.setTmcOAMBugControlObject(analysisObjectService.analysisCommandToTMCOAMBugControl(dataCammands));
					length = length+dataCammands.length+25;
				}else if(getidentifier == 17){//TMS OAM故障管理配置
					dataCammands = getDataCammands(dataBlockHead, cammands, end, dataCammands);
					AnalysisObjectService analysisObjectService = new AnalysisObjectService();
					actionObject.setTmsOAMBugControlObject(analysisObjectService.analysisCommandToTMSOAMBugControl(dataCammands));
					length = length+dataCammands.length+25;
				}else if(getidentifier == 18){//PW层队列调度及缓存管理策略
					dataCammands = getDataCammands(dataBlockHead, cammands, end, dataCammands);
					AnalysisObjectService analysisObjectService = new AnalysisObjectService();
					actionObject.setPwQueueAndBufferManage(analysisObjectService.AnalysisCommandToPwQueueAndBufferManage(dataCammands));
					length = length+dataCammands.length+25;
				}else if(getidentifier == 19){//PW配置块
					dataCammands = getDataCammands(dataBlockHead, cammands, end, dataCammands);
					AnalysisObjectService analysisObjectService = new AnalysisObjectService();
					actionObject.setPwObjectList(analysisObjectService.AnalysisCommandToPw(dataCammands));
					length = length+dataCammands.length+25;
				}else if(getidentifier == 20){//端口聚合
					dataCammands = getDataCammands(dataBlockHead, cammands, end, dataCammands);
					AnalysisObjectService analysisObjectService = new AnalysisObjectService();
					actionObject.setPortLAGList(analysisObjectService.AnalysisCommandToPortLAG(dataCammands));
					length = length+dataCammands.length+25;
				}else if(getidentifier == 21){//端口配置
					AnalysisObjectService analysisObjectService = new AnalysisObjectService();
					for (int j = 0; j < (cammands.length-131)/100; j++) {
						dataCammands = new byte[100];
						System.arraycopy(cammands, 131+100*j, dataCammands, 0, 100);
						CoderUtils.print16String(dataCammands);
						protObjects.add(analysisObjectService.analysisPortConfigObjectToCommands(dataCammands));
					}
				}else if(getidentifier == 23){//IGMP SNOOPING配置
					dataCammands = getDataCammands(dataBlockHead, cammands, end, dataCammands);
					AnalysisObjectService analysisObjectService = new AnalysisObjectService();
					actionObject.setIgmpsnooping(analysisObjectService.analysisCommandToIGMP(dataCammands));
					length = length+dataCammands.length+25;
				}else if(getidentifier == 24){//E1仿真配置
					dataCammands = getDataCammands(dataBlockHead, cammands, end, dataCammands);
					AnalysisObjectService analysisObjectService = new AnalysisObjectService();
					actionObject.setE1Object(analysisObjectService.AnalysisCommandsToE1Object(dataCammands));
					length = length+dataCammands.length+25;
				}else if(getidentifier == 25){//端口高级配置
					dataCammands = getDataCammands(dataBlockHead, cammands, end, dataCammands);
					AnalysisObjectService analysisObjectService = new AnalysisObjectService();
					protObjects.add(analysisObjectService.analysisPortConfigObjectToCommands(dataCammands));
					length = length+dataCammands.length+25;
				}else if(getidentifier == 26){//TMS OAM配置块
					dataCammands = getDataCammands(dataBlockHead, cammands, end, dataCammands);
					AnalysisObjectService analysisObjectService = new AnalysisObjectService();
					tmsoamObjects.add(analysisObjectService.AnalysisCommandsToTMSOAMObject(dataCammands));
					length = length+dataCammands.length+25;
				}else if(getidentifier == 27){//以太网链路OAM
					dataCammands = getDataCammands(dataBlockHead, cammands, end, dataCammands);
					AnalysisObjectService analysisObjectService = new AnalysisObjectService();
					actionObject.setEthLinkOAMObject(analysisObjectService.analysisCommandToETHLinkOAM(dataCammands));
					length = length+dataCammands.length+25;
				}else if(getidentifier == 28){//时间同步配置块
					dataCammands = getDataCammands(dataBlockHead, cammands, end, dataCammands);
					AnalysisObjectService analysisObjectService = new AnalysisObjectService();
					actionObject.setTimesyncobject(analysisObjectService.analysisCommandsToTimeSynchrObject(dataCammands));
					length = length+dataCammands.length+25;
				}else if(getidentifier == 29){//时钟同步配置块
					dataCammands = getDataCammands(dataBlockHead, cammands, end, dataCammands);
					AnalysisObjectService analysisObjectService = new AnalysisObjectService();
					actionObject.setClockObject(analysisObjectService.AnalysisCommandsToClockObject(dataCammands));
					length = length+dataCammands.length+25;
				}else if(getidentifier == 30){//多段PW
					dataCammands = getDataCammands(dataBlockHead, cammands, end, dataCammands);
					AnalysisObjectService analysisObjectService = new AnalysisObjectService();
					actionObject.setMsPwObjects(analysisObjectService.AnalysisCommadsToMspw(dataCammands));
					length = length+dataCammands.length+25;
				}else if(getidentifier == 39){//V35端口配置
					dataCammands = getDataCammands(dataBlockHead, cammands, end, dataCammands);
					AnalysisObjectService analysisObjectService = new AnalysisObjectService();
					actionObject.setV35PortObject(analysisObjectService.AnalysisCommandToV35PortObject(dataCammands));
					length = length+dataCammands.length+25;
				}else if(getidentifier == 56){//智能风扇配置
					dataCammands = getDataCammands(dataBlockHead, cammands, end, dataCammands);
					AnalysisObjectService analysisObjectService = new AnalysisObjectService();
					actionObject.setSmartFanObjectList(analysisObjectService.AnalysisCommandToSmartFanObject(dataCammands));
					length = length+dataCammands.length+25;
				}else if(getidentifier == 57){//以太网二层业务配置块
					dataCammands = getDataCammands(dataBlockHead, cammands, end, dataCammands);
					AnalysisObjectService analysisObjectService = new AnalysisObjectService();
					EthServiceObject ethServiceObject = new EthServiceObject();
					ethServiceObject.setEthServiceInfoObjectList(analysisObjectService.AnalysisCommandToEthService(dataCammands));
					actionObject.setEthServiceObject(ethServiceObject);
					length = length+dataCammands.length+25;
				}else if(getidentifier == 60){//端口OQS
					AnalysisObjectService analysisObjectService = new AnalysisObjectService();
					for (int j = 0; j < (cammands.length-131)/40; j++) {
						dataCammands = new byte[40];
						System.arraycopy(cammands, 131+40*j, dataCammands, 0, 40);
						protObjects.add(analysisObjectService.analysisCommandToPortPri(dataCammands));
					}
				}else if(getidentifier == 61){//端口丢弃流
					dataCammands = getDataCammands(dataBlockHead, cammands, end, dataCammands);
					AnalysisObjectService analysisObjectService = new AnalysisObjectService();
					EthServiceObject ethServiceObject = analysisObjectService.AnalysisCommandToPortDiscard(dataCammands);
					actionObject.setEthServiceObject(ethServiceObject);
					length = length+dataCammands.length+25;
				}else if(getidentifier == 62){//端口2层属性
					dataCammands = getDataCammands(dataBlockHead, cammands, end, dataCammands);
					AnalysisObjectService analysisObjectService = new AnalysisObjectService();
					List<Port2LayerObject> objList = analysisObjectService.analysisCommandToPort2LayerObj(dataCammands);
					actionObject.setPort2LayerObjectList(objList);
					length = length+dataCammands.length+25;
				}else if(getidentifier == 32){//性能门限设置
					dataCammands = getDataCammands(dataBlockHead, cammands, end, dataCammands);
					AnalysisObjectService analysisObjectService = new AnalysisObjectService();
					PmValueLimiteObject pmValueObject = analysisObjectService.analysisCommandToPmLimiteObject(dataCammands);
					actionObject.setPmValueLimiteObject(pmValueObject);
					length = length+dataCammands.length+25;
				}else if(getidentifier == 37){//acl设置
					dataCammands = getDataCammands(dataBlockHead, cammands, end, dataCammands);
					AnalysisObjectService analysisObjectService = new AnalysisObjectService();
					AclObject aclObject = analysisObjectService.analysisCommandToAclObject(dataCammands);
					actionObject.setAclObject(aclObject);
					length = length+dataCammands.length+25;
				}else if(getidentifier == 63){//二层静态MAC地址设置
					dataCammands = getDataCammands(dataBlockHead, cammands, end, dataCammands);
					AnalysisObjectService analysisObjectService = new AnalysisObjectService();
					List<SecondMacStudyObject> secondMacStudyObjectList = analysisObjectService.analysisCommandToSecondMacObject(dataCammands);
					actionObject.setSecondMacStudyObjectList(secondMacStudyObjectList);
					length = length+dataCammands.length+25;
				}else if(getidentifier == 4){//环网保护
					dataCammands = getDataCammands(dataBlockHead, cammands, end, dataCammands);
					AnalysisObjectService analysisObjectService = new AnalysisObjectService();
					List<RoundProtectionObject> roundProtectionObject = analysisObjectService.analysisCommandToLoopProRotateObject(dataCammands);
					actionObject.setRoundProtectionObject(roundProtectionObject);
					length = length+dataCammands.length+25;
				}else if(getidentifier == 59){//l2cp
					dataCammands = getDataCammands(dataBlockHead, cammands, end, dataCammands);
					AnalysisObjectService analysisObjectService = new AnalysisObjectService();
					L2CPinfoObject l2cpObject = analysisObjectService.analysisCommandToL2cpObject(dataCammands);
					actionObject.setL2CPinfoObject(l2cpObject);
					length = length+dataCammands.length+25;
				}else if(getidentifier == 35){//MAC黑名单
					dataCammands = getDataCammands(dataBlockHead, cammands, end, dataCammands);
					AnalysisObjectService analysisObjectService = new AnalysisObjectService();
					List<MacManageObject> macManageObjectList = analysisObjectService.analysisCommandToMacManageObject(dataCammands);
					actionObject.setMacManageObjectList(macManageObjectList);
					length = length+dataCammands.length+25;
					
				}else if(getidentifier == 36){//黑白名单MAC
					dataCammands = getDataCammands(dataBlockHead, cammands, end, dataCammands);
					AnalysisObjectService analysisObjectService = new AnalysisObjectService();
					List<BlackWhiteMacObject> blackWhiteMacObjectList = analysisObjectService.analysisCommandToBlackWhiteMacObject(dataCammands);
					actionObject.setBlackWhiteMacObjectList(blackWhiteMacObjectList);
					length = length+dataCammands.length+25;
					
				}else if(getidentifier == 33){//QINQ
					dataCammands = getDataCammands(dataBlockHead, cammands, end, dataCammands);
					AnalysisObjectService analysisObjectService = new AnalysisObjectService();
					List<QinQObject> qinqObjectList = analysisObjectService.AnalysisCommandToQinQ(dataCammands);
					actionObject.setQinQObjects(qinqObjectList);
					length = length+dataCammands.length+25;
					
				}else if(getidentifier == 64){//ccc
					dataCammands = getDataCammands(dataBlockHead, cammands, end, dataCammands);
					AnalysisObjectService analysisObjectService = new AnalysisObjectService();
					List<CccObject> cccObjectList = analysisObjectService.AnalysisCommandToCcc(dataCammands);
					actionObject.setCccObjectList(cccObjectList);
					length = length+dataCammands.length+25;
				}else if(getidentifier == 65){//bfd
					dataCammands = getDataCammands(dataBlockHead, cammands, end, dataCammands);
					AnalysisObjectService analysisObjectService = new AnalysisObjectService();
					List<BfdObject> bfdObjectList = analysisObjectService.AnalysisCommandToBfd(dataCammands);
					actionObject.setBfdObjectList(bfdObjectList);
					length = length+dataCammands.length+25;	
				}else if(getidentifier == 66){//arp
					dataCammands = getDataCammands(dataBlockHead, cammands, end, dataCammands);
					AnalysisObjectService analysisObjectService = new AnalysisObjectService();
					List<ARPObject> arpObjectList = analysisObjectService.analysisCommandsToARPObject(dataCammands);
					actionObject.setArpObjectList(arpObjectList);
					length = length+dataCammands.length+25;	
				}
				
				end += dataCammands.length+25;
			}
			if(number>0){
				actionObject.setProtObjectList(protObjects);
				actionObject.setTmsoamObjects(tmsoamObjects);
			}
					    
		}else{
			actionObject.setClockObject(null);	
			actionObject.setL2CPinfoObject(null);
			actionObject.setGlobalObject(null);
		}

		return actionObject;
		
	}
	
	
	private byte[] getDataCammands(byte[] dataBlockHead,byte[]cammands,int length,byte[] dataCammands ){
		// 配置数据块数据长度
		byte[] dataLengthByte = {dataBlockHead[3],dataBlockHead[4],dataBlockHead[5],dataBlockHead[6]};
		int dataLength = CoderUtils.bytesToInt(dataLengthByte);
		//配置数据块数据
		dataCammands = new byte[dataLength];
		System.arraycopy(cammands, length+25, dataCammands, 0, dataLength);
		return dataCammands;		
	}
}
