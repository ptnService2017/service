﻿package com.nms.drive.analysis.datablock;

import java.util.ArrayList;
import java.util.List;

import com.nms.drive.analysis.xmltool.AnalysisCommandByDriveXml;
import com.nms.drive.analysis.xmltool.AnalysisDriveXmlToCommand;
import com.nms.drive.analysis.xmltool.bean.DriveAttribute;
import com.nms.drive.analysis.xmltool.bean.DriveRoot;
import com.nms.drive.analysis.xmltool.bean.ListRoot;
import com.nms.drive.service.bean.TunnelObject;
import com.nms.drive.service.impl.CoderUtils;

/**
 * 
 * @author 彭冲
 * tunnel解析
 *
 */
public class AnalysisTUNNELTable extends AnalysisBase {

	private int dataCount = 170;// 每个数据块的长度
	private int NEhead = 5;// NE头长度
	private int controlPanelHead = 101;// 盘头长度
	private int dataBlockHead = 25;// 配置块头信息长度
	private int clauses = 2;// 条目数长度

	public static void main(String[] args) throws Exception {
		String str = "com/nms/drive/analysis/xmltool/file/TUNNEL表(06H).xml";
		List<TunnelObject> tunnelObjectList = new ArrayList<TunnelObject>();
		TunnelObject tunnelObject = new TunnelObject();
		tunnelObjectList.add(tunnelObject);
		AnalysisTUNNELTable analysisTUNNELTable = new AnalysisTUNNELTable();
		for (int i = 0; i < 10000; i++) {
			analysisTUNNELTable.analysisObjectToCommand(tunnelObjectList, str);
			Thread.sleep(100);
			System.out.println(i);
		}
		Thread.sleep(900000000);
	}
	/**
	 * 根据对象生成字节数组
	 * 
	 * @param tunnelObject对象
	 * @param configXml配置XML
	 * @return 命令
	 * @throws Exception
	 */
	public byte[] analysisObjectToCommand(List<TunnelObject> tunnelObjectList, String configXml) throws Exception {
		
		try {
			DriveRoot driveRoot_config = super.LoadDriveXml(configXml);
			String file = driveRoot_config.getDriveAttributeList().get(0).getListRootList().get(0).getFile();
			driveRoot_config.getDriveAttributeList().get(0).getListRootList().clear();// 清除本身自带的一个ListRoot
			driveRoot_config.getDriveAttributeList().get(0).setValue(tunnelObjectList.size() + "");// 条目数
			for (int j = 0; j < tunnelObjectList.size(); j++) {

				DriveRoot driveRoot_config_1 = super.LoadDriveXml(file);
				ListRoot listroot = new ListRoot();
				listroot.setDriveAttributeList(driveRoot_config_1.getDriveAttributeList());
				StringBuffer  sb = new StringBuffer();
				sb.append(tunnelObjectList.get(j).getMegIcc()+":");
				sb.append(tunnelObjectList.get(j).getMegUmc());
				String[] meg = sb.toString().split(":");
				String[] sourceMac = tunnelObjectList.get(j).getForeLSP().getSourceMac().split("-");
				String[] targetMac = tunnelObjectList.get(j).getForeLSP().getTargetMac().split("-");
				String[] sourceMac_after = tunnelObjectList.get(j).getAfterLSP().getSourceMac().split("-");
				String[] targetMac_after = tunnelObjectList.get(j).getAfterLSP().getTargetMac().split("-");
				// 将 tunnelObject 对象信息 赋值到 driveRoot_config 对象中
				for (int i = 0; i < driveRoot_config_1.getDriveAttributeList().size(); i++) {
					DriveAttribute driveAttribute = listroot.getDriveAttributeList().get(i);
					ObjectToDriveAttribute(tunnelObjectList.get(j), driveAttribute, meg,sourceMac,targetMac,sourceMac_after,targetMac_after);
				}
				driveRoot_config.getDriveAttributeList().get(0).getListRootList().add(listroot);
			}

			AnalysisDriveXmlToCommand analysisDriveXmlToCommand = new AnalysisDriveXmlToCommand();
			byte[] dataCommand = analysisDriveXmlToCommand.analysisCommands(driveRoot_config);
//			CoderUtils.print16String(dataCommand);
			return dataCommand;

		} catch (Exception e) {
			throw e;
		}
	}

	public void ObjectToDriveAttribute(TunnelObject tunnelObject,DriveAttribute driveAttribute ,String[] meg,String[] sourceMac,String[] targetMac,String[] sourceMac_after,String[] targetMac_after){
		//(OAM)CV帧配置
		if("(OAM)CV帧配置".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(cvframe(tunnelObject)+"");
		}
		
		//(OAM)源MEP/MIP ID
		if("(OAM)源MEP/MIP ID".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(tunnelObject.getSourceMEP()+"");
		}
		//(OAM)宿MEP ID
		if("(OAM)宿MEP ID".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(tunnelObject.getEquityMEP()+"");
		}
		//(OAM)APS使能
		if("(OAM)APS使能".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(tunnelObject.getAspEnabl()+"");
		}
		// 前后向TUNNEL是否绑定成一个双向LSP
		if (driveAttribute.getDescription().equals("前后向TUNNEL是否绑定成一个双向LSP")) {
			driveAttribute.setValue(tunnelObject.getBindingLsp() + "");
		}else if("角色".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(tunnelObject.getRole()+ "");
		}
		else if("(OAM)OAM使能".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(tunnelObject.getOamEnable()+"");
		}
		// LSP ID
		if (driveAttribute.getDescription().equals("LSP ID")) {
			driveAttribute.setValue(tunnelObject.getTunnelId() + "");
		}

		// LSP类型:0/1=E-LSP/L-LSP
		if (driveAttribute.getDescription().equals("LSP类型")) {
			driveAttribute.setValue(tunnelObject.getLspqosType() + "");
		}

		// LSP模型:0/1=统一模型/管道模型
		if (driveAttribute.getDescription().equals("LSP模型")) {
			driveAttribute.setValue(tunnelObject.getLspModel() + "");
		}

		// 业务类型:0/1/2=工作业务/保护业务/额外业务
		if (driveAttribute.getDescription().equals("业务类型")) {
			driveAttribute.setValue(tunnelObject.getServiceType() + "");
		}
		// 所归属的SNCP  ID
		if (driveAttribute.getDescription().equals("所属SNCPid")) {
			driveAttribute.setValue(tunnelObject.getSncpID() + "");
		}

		// (前向TUNNEL)TUNNEL ID
		if (driveAttribute.getDescription().equals("(前向TUNNEL)TUNNEL ID")) {
			driveAttribute.setValue(tunnelObject.getForeLSP().getLspId() + "");
		}

		//(前向TUNNEL)TUNNEL节点类型
		if (driveAttribute.getDescription().equals("(前向TUNNEL)TUNNEL节点类型")) {
			driveAttribute.setValue(tunnelObject.getForeLSP().getTunnelType() + "");
		}
		// (前向TUNNEL)(入标记)槽位号
		if (driveAttribute.getDescription().equals("(前向TUNNEL)(入标记)槽位号")) {
			driveAttribute.setValue(tunnelObject.getForeLSP().getInSlot() + "");
		}

		// (前向TUNNEL)(入标记)端口号
		if (driveAttribute.getDescription().equals("(前向TUNNEL)(入标记)端口号")) {
			driveAttribute.setValue(tunnelObject.getForeLSP().getInProt() + "");
		}

		// (前向TUNNEL)(入标记)标签
		if (driveAttribute.getDescription().equals("(前向TUNNEL)(入标记)标签")) {
			driveAttribute.setValue(tunnelObject.getForeLSP().getInLable() + "");
		}
		// (前向TUNNEL)(入标记)TTL
		if (driveAttribute.getDescription().equals("(前向TUNNEL)(入标记)TTL")) {
			driveAttribute.setValue(tunnelObject.getForeLSP().getInTTL() + "");
		}
		
		// (前向TUNNEL)(入标记)TC
		if (driveAttribute.getDescription().equals("(前向TUNNEL)(入标记)TC")) {
			driveAttribute.setValue(tunnelObject.getForeLSP().getInTC() + "");
		}
		//(前向TUNNEL)(宽带控制)带宽控制使能
		if("(前向TUNNEL)(宽带控制)带宽控制使能".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(tunnelObject.getForeLSP().getBandwidthEnable()+"");
		}
		//(前向TUNNEL)(宽带控制)CIR
		if("(前向TUNNEL)(宽带控制)CIR".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(tunnelObject.getForeLSP().getCir()+"");
		}
		//(前向TUNNEL)(宽带控制)PIR
		if("(前向TUNNEL)(宽带控制)PIR".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(tunnelObject.getForeLSP().getPir()+"");
		}
		//(前向TUNNEL)(宽带控制)PBS
		if("(前向TUNNEL)(宽带控制)PBS".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(tunnelObject.getForeLSP().getPbs()+"");
		}
		//(前向TUNNEL)COS
		if("(前向TUNNEL)COS".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(tunnelObject.getForeLSP().getCos()+"");
		}
		//(前向TUNNEL)(宽带控制)CBS
		if("(前向TUNNEL)(宽带控制)CBS".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(tunnelObject.getForeLSP().getCbs()+"");
		}
		// (前向TUNNEL)(出标记)槽位号
		if (driveAttribute.getDescription().equals("(前向TUNNEL)(出标记)槽位号")) {
			driveAttribute.setValue(tunnelObject.getForeLSP().getOutSlot() + "");
		}

		// (前向TUNNEL)(出标记)端口号
		if (driveAttribute.getDescription().equals("(前向TUNNEL)(出标记)端口号")) {
			driveAttribute.setValue(tunnelObject.getForeLSP().getOutprot() + "");
		}

		// (前向TUNNEL)(出标记)标签
		if (driveAttribute.getDescription().equals("(前向TUNNEL)(出标记)标签")) {
			driveAttribute.setValue(tunnelObject.getForeLSP().getOutLable() + "");
		}

		// (前向TUNNEL)(出标记)TTL
		if (driveAttribute.getDescription().equals("(前向TUNNEL)(出标记)TTL")) {
			driveAttribute.setValue(tunnelObject.getForeLSP().getOutTTL() + "");
		}
		
		// (前向TUNNEL)(出标记)TC
		if (driveAttribute.getDescription().equals("(前向TUNNEL)(出标记)TC")) {
			driveAttribute.setValue(tunnelObject.getForeLSP().getOutTC() + "");
		}
		// (后向TUNNEL)TUNNEL ID
		if (driveAttribute.getDescription().equals("(后向TUNNEL)TUNNEL ID")) {
			driveAttribute.setValue(tunnelObject.getAfterLSP().getLspId() + "");
		}
		
		//(后向TUNNEL)TUNNEL节点类型
		if (driveAttribute.getDescription().equals("(后向TUNNEL)TUNNEL节点类型")) {
			driveAttribute.setValue(tunnelObject.getAfterLSP().getTunnelType() + "");
		}
		
		//(后向TUNNEL)(宽带控制)带宽控制使能
		if("(后向TUNNEL)(宽带控制)带宽控制使能".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(tunnelObject.getAfterLSP().getBandwidthEnable()+"");
		}
		//(后向TUNNEL)(宽带控制)CIR
		if("(后向TUNNEL)(宽带控制)CIR".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(tunnelObject.getAfterLSP().getCir()+"");
		}
		//(后向TUNNEL)(宽带控制)PIR
		if("(后向TUNNEL)(宽带控制)PIR".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(tunnelObject.getAfterLSP().getPir()+"");
		}
		//(后向TUNNEL)(宽带控制)PBS
		if("(后向TUNNEL)(宽带控制)PBS".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(tunnelObject.getAfterLSP().getPbs()+"");
		}
		//(后向TUNNEL)COS
		if("(后向TUNNEL)COS".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(tunnelObject.getAfterLSP().getCos()+"");
		}
		//(后向TUNNEL)(宽带控制)CBS
		if("(后向TUNNEL)(宽带控制)CBS".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(tunnelObject.getAfterLSP().getCbs()+"");
		}
		// (后向TUNNEL)(入标记)槽位号
		if (driveAttribute.getDescription().equals("(后向TUNNEL)(入标记)槽位号")) {
			driveAttribute.setValue(tunnelObject.getAfterLSP().getInSlot() + "");
		}

		// (后向TUNNEL)(入标记)端口号
		if (driveAttribute.getDescription().equals("(后向TUNNEL)(入标记)端口号")) {
			driveAttribute.setValue(tunnelObject.getAfterLSP().getInProt() + "");
		}

		// (后向TUNNEL)(入标记)标签
		if (driveAttribute.getDescription().equals("(后向TUNNEL)(入标记)标签")) {
			driveAttribute.setValue(tunnelObject.getAfterLSP().getInLable() + "");
		}

		// (后向TUNNEL)(入标记)TTL
		if (driveAttribute.getDescription().equals("(后向TUNNEL)(入标记)TTL")) {
			driveAttribute.setValue(tunnelObject.getAfterLSP().getInTTL() + "");
		}
		
		// (后向TUNNEL)(入标记)TC
		if (driveAttribute.getDescription().equals("(后向TUNNEL)(入标记)TC")) {
			driveAttribute.setValue(tunnelObject.getAfterLSP().getInTC() + "");
		}
		// (后向TUNNEL)(出标记)槽位号
		if (driveAttribute.getDescription().equals("(后向TUNNEL)(出标记)槽位号")) {
			driveAttribute.setValue(tunnelObject.getAfterLSP().getOutSlot() + "");
		}

		// (后向TUNNEL)(出标记)端口号
		if (driveAttribute.getDescription().equals("(后向TUNNEL)(出标记)端口号")) {
			driveAttribute.setValue(tunnelObject.getAfterLSP().getOutprot() + "");
		}

		// (后向TUNNEL)(出标记)标签
		if (driveAttribute.getDescription().equals("(后向TUNNEL)(出标记)标签")) {
			driveAttribute.setValue(tunnelObject.getAfterLSP().getOutLable() + "");
		}
		
		// (后向TUNNEL)(出标记)TTL
		if (driveAttribute.getDescription().equals("(后向TUNNEL)(出标记)TTL")) {
			driveAttribute.setValue(tunnelObject.getAfterLSP().getOutTTL() + "");
		}
		
		// (后向TUNNEL)(出标记)TC
		if (driveAttribute.getDescription().equals("(后向TUNNEL)(出标记)TC")) {
			driveAttribute.setValue(tunnelObject.getAfterLSP().getOutTC() + "");
		}
		if(meg.length>1){
			String[] icc = meg[0].split(",");
			String[] umc = meg[1].split(",");
			//MEG ICC1
			if("(OAM)MEG ICC1".equals(driveAttribute.getDescription())){
				driveAttribute.setValue(icc[0]);
			}
			//MEG ICC2
			if("(OAM)MEG ICC2".equals(driveAttribute.getDescription())){
				driveAttribute.setValue(icc[1]);
			}
			//MEG ICC3
			if("(OAM)MEG ICC3".equals(driveAttribute.getDescription())){
				driveAttribute.setValue(icc[2]);
			}
			//MEG ICC4
			if("(OAM)MEG ICC4".equals(driveAttribute.getDescription())){
				driveAttribute.setValue(icc[3]);
			}
			//MEG ICC5
			if("(OAM)MEG ICC5".equals(driveAttribute.getDescription())){
				driveAttribute.setValue(icc[4]);
			}
			//MEG ICC6
			if("(OAM)MEG ICC6".equals(driveAttribute.getDescription())){
				driveAttribute.setValue(icc[5]);
			}
			//MEG UMC1
			if("(OAM)MEG UMC1".equals(driveAttribute.getDescription())){
				driveAttribute.setValue(umc[0]);
			}
			//MEG UMC2
			if("(OAM)MEG UMC2".equals(driveAttribute.getDescription())){
				driveAttribute.setValue(umc[1]);
			}
			//MEG UMC3
			if("(OAM)MEG UMC3".equals(driveAttribute.getDescription())){
				driveAttribute.setValue(umc[2]);
			}
			//MEG UMC4
			if("(OAM)MEG UMC4".equals(driveAttribute.getDescription())){
				driveAttribute.setValue(umc[3]);
			}
			//MEG UMC5
			if("(OAM)MEG UMC5".equals(driveAttribute.getDescription())){
				driveAttribute.setValue(umc[4]);
			}
			//MEG UMC6
			if("(OAM)MEG UMC6".equals(driveAttribute.getDescription())){
				driveAttribute.setValue(umc[5]);
			}
		}
		//(OAM)字节116
		if("(OAM)字节116".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(oamframe(tunnelObject)+"");
		}
		//(OAM)LM使能
		if("(OAM)LM使能".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(tunnelObject.getLmEnabl()+"");
		}
		//前向源MAC地址1
		if("前向源MAC地址1".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(sourceMac[0]);
		}
		//前向源MAC地址2
		if("前向源MAC地址2".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(sourceMac[1]);
		}
		//前向源MAC地址3
		if("前向源MAC地址3".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(sourceMac[2]);
		}
		//前向源MAC地址4
		if("前向源MAC地址4".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(sourceMac[3]);
		}
		//前向源MAC地址5
		if("前向源MAC地址5".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(sourceMac[4]);
		}
		//前向源MAC地址6
		if("前向源MAC地址6".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(sourceMac[5]);
		}
		//前向目的MAC地址1
		if("前向目的MAC地址1".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(targetMac[0]);
		}
		//前向目的MAC地址2
		if("前向目的MAC地址2".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(targetMac[1]);
		}
		//前向目的MAC地址3
		if("前向目的MAC地址3".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(targetMac[2]);
		}
		//前向目的MAC地址4
		if("前向目的MAC地址4".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(targetMac[3]);
		}
		//前向目的MAC地址5
		if("前向目的MAC地址5".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(targetMac[4]);
		}
		//前向目的MAC地址6
		if("前向目的MAC地址6".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(targetMac[5]);
		}
		
		//后向源MAC地址1
		if("后向源MAC地址1".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(sourceMac_after[0]);
		}
		//后向源MAC地址2
		if("后向源MAC地址2".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(sourceMac_after[1]);
		}
		//后向源MAC地址3
		if("后向源MAC地址3".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(sourceMac_after[2]);
		}
		//后向源MAC地址4
		if("后向源MAC地址4".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(sourceMac_after[3]);
		}
		//后向源MAC地址5
		if("后向源MAC地址5".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(sourceMac_after[4]);
		}
		//后向源MAC地址6
		if("后向源MAC地址6".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(sourceMac_after[5]);
		}
		//后向目的MAC地址1
		if("后向目的MAC地址1".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(targetMac_after[0]);
		}
		//后向目的MAC地址2
		if("后向目的MAC地址2".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(targetMac_after[1]);
		}
		//后向目的MAC地址3
		if("后向目的MAC地址3".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(targetMac_after[2]);
		}
		//后向目的MAC地址4
		if("后向目的MAC地址4".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(targetMac_after[3]);
		}
		//后向目的MAC地址5
		if("后向目的MAC地址5".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(targetMac_after[4]);
		}
		//后向目的MAC地址6
		if("后向目的MAC地址6".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(targetMac_after[5]);
		}
		//前向增加外层VLAN使能
		if("前向增加外层VLAN使能".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(tunnelObject.getForeLSP().getVlanEnable()+"");
		}
		//前向外层VLAN值
		if("前向外层VLAN值".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(tunnelObject.getForeLSP().getOutVlanValue()+"");
		}
		//前向外层TP_ID
		if("前向外层TP_ID".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(tunnelObject.getForeLSP().getTpId()+"");
		}
		//后向增加外层VLAN使能
		if("后向增加外层VLAN使能".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(tunnelObject.getAfterLSP().getVlanEnable()+"");
		}
		//后向外层VLAN值
		if("后向外层VLAN值".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(tunnelObject.getAfterLSP().getOutVlanValue()+"");
		}
		//后向外层TP_ID
		if("后向外层TP_ID".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(tunnelObject.getAfterLSP().getTpId()+"");
		}else if("(前向TUNNEL)源IP1".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(tunnelObject.getForeLSP().getSourceIP().split("\\.")[0]);
		}else if("(前向TUNNEL)源IP2".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(tunnelObject.getForeLSP().getSourceIP().split("\\.")[1]);
		}else if("(前向TUNNEL)源IP3".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(tunnelObject.getForeLSP().getSourceIP().split("\\.")[2]);
		}else if("(前向TUNNEL)源IP4".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(tunnelObject.getForeLSP().getSourceIP().split("\\.")[3]);
		}else if("(前向TUNNEL)目的IP1".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(tunnelObject.getForeLSP().getTargetIP().split("\\.")[0]);
		}else if("(前向TUNNEL)目的IP2".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(tunnelObject.getForeLSP().getTargetIP().split("\\.")[1]);
		}else if("(前向TUNNEL)目的IP3".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(tunnelObject.getForeLSP().getTargetIP().split("\\.")[2]);
		}else if("(前向TUNNEL)目的IP4".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(tunnelObject.getForeLSP().getTargetIP().split("\\.")[3]);
		}else if("(后向TUNNEL)源IP1".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(tunnelObject.getAfterLSP().getSourceIP().split("\\.")[0]);
		}else if("(后向TUNNEL)源IP2".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(tunnelObject.getAfterLSP().getSourceIP().split("\\.")[1]);
		}else if("(后向TUNNEL)源IP3".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(tunnelObject.getAfterLSP().getSourceIP().split("\\.")[2]);
		}else if("(后向TUNNEL)源IP4".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(tunnelObject.getAfterLSP().getSourceIP().split("\\.")[3]);
		}else if("(后向TUNNEL)目的IP1".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(tunnelObject.getAfterLSP().getTargetIP().split("\\.")[0]);
		}else if("(后向TUNNEL)目的IP2".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(tunnelObject.getAfterLSP().getTargetIP().split("\\.")[1]);
		}else if("(后向TUNNEL)目的IP3".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(tunnelObject.getAfterLSP().getTargetIP().split("\\.")[2]);
		}else if("(后向TUNNEL)目的IP4".equals(driveAttribute.getDescription())){
			driveAttribute.setValue(tunnelObject.getAfterLSP().getTargetIP().split("\\.")[3]);
		}
	}
	
	/**
	 * oamframe帧转换为xml格式
	 * @param info
	 * @return
	 */
	private int oamframe(TunnelObject info){
		StringBuffer ccm_sb = new StringBuffer();
		ccm_sb.append(info.getOam_bao());
		ccm_sb.append(info.getOam_mel());
		ccm_sb.append(info.getOam_tmp());	
		ccm_sb.append(info.getOam_resore());
		char[] result = ccm_sb.toString().toCharArray();
		int a = CoderUtils.convertAlgorism(result);
		return a ;
	}
	
	/**
	 * cvframe帧转换为xml格式
	 * @param info
	 * @return
	 */
	private int cvframe(TunnelObject info){
		StringBuffer loopback = new StringBuffer();
		loopback.append(info.getCvReserve());
		loopback.append(info.getCvCycle());
		loopback.append(info.getCvEnabl());		
		char[] result = loopback.toString().toCharArray();
		int a = CoderUtils.convertAlgorism(result);
		return a ;
	}
	
	/**
	 * 根据字节数组生成对象
	 * 
	 * @param commands命令
	 * @param configXml配置XML
	 * @return TunnelObject
	 */
	public List<TunnelObject> analysisCommandToObject(byte[] commands, String configXml) throws Exception {
		List<TunnelObject> tunnelObjectList = new ArrayList<TunnelObject>();
		DriveRoot driveRoot_config = super.LoadDriveXml(configXml);
		String file = driveRoot_config.getDriveAttributeList().get(0).getListRootList().get(0).getFile();

		// 起始长度
		int start = clauses;

		// 条目数
		int count = (commands.length - clauses) / dataCount;
		for (int j = 0; j < count; j++) {

			byte[] a = new byte[dataCount];
			System.arraycopy(commands, start + j * dataCount, a, 0, a.length);
			AnalysisCommandByDriveXml analysisCommandByDriveXml = new AnalysisCommandByDriveXml();
			analysisCommandByDriveXml.setCommands(a);
			TunnelObject tunnelObject = new TunnelObject();
			StringBuffer sourceMac = new StringBuffer();
			StringBuffer targetMac = new StringBuffer();
			StringBuffer sourceMac_after = new StringBuffer();
			StringBuffer targetMac_after = new StringBuffer();
			StringBuffer sourceIP = new StringBuffer();
			StringBuffer targetIP = new StringBuffer();
			StringBuffer sourceIP_after = new StringBuffer();
			StringBuffer targetIP_after = new StringBuffer();
			try {
				DriveRoot driveRoot = analysisCommandByDriveXml.analysisDriveAttrbute(file);
				StringBuffer sb_megicc = new StringBuffer();//用于拼接megicc
				StringBuffer sb_megumc = new StringBuffer();//用于拼接megumc
				// 将 driveRoot 信息 赋值 TunnelObject 对象中
				for (int i = 0; i < driveRoot.getDriveAttributeList().size(); i++) {
					
					DriveAttribute driveattribute = driveRoot.getDriveAttributeList().get(i);
					DriveAttributeToCommands(tunnelObject, driveattribute, sb_megicc,sb_megumc,sourceMac,targetMac,sourceMac_after,targetMac_after,sourceIP,targetIP,sourceIP_after,targetIP_after);
				}
				
			} catch (Exception e) {
				throw e;
			}
			tunnelObjectList.add(tunnelObject);
		}
		return tunnelObjectList;
	}
	
	public void DriveAttributeToCommands(TunnelObject tunnelObject,DriveAttribute driveAttribute ,StringBuffer sb_megicc,StringBuffer sb_megumc,StringBuffer sourceMac
			,StringBuffer targetMac,StringBuffer sourceMac_after,StringBuffer targetMac_after,StringBuffer sourceIP,StringBuffer targetIP
			,StringBuffer sourceIP_after,StringBuffer targetIP_after){
		
		// 前后向TUNNEL是否绑定成一个双向LSP
		if (driveAttribute.getDescription().equals("前后向TUNNEL是否绑定成一个双向LSP")) {
			tunnelObject.setBindingLsp(Integer.parseInt(driveAttribute.getValue()));
		}else if(driveAttribute.getDescription().equals("角色")){
			tunnelObject.setRole(Integer.parseInt(driveAttribute.getValue()));
		}

		// LSP ID
		if (driveAttribute.getDescription().equals("LSP ID")) {
			tunnelObject.setTunnelId(Integer.parseInt(driveAttribute.getValue()));
		}

		// LSP类型:0/1=E-LSP/L-LSP
		if (driveAttribute.getDescription().equals("LSP类型")) {
			tunnelObject.setLspqosType(Integer.parseInt(driveAttribute.getValue()));
		}

		// LSP模型:0/1=统一模型/管道模型
		if (driveAttribute.getDescription().equals("LSP模型")) {
			tunnelObject.setLspModel(Integer.parseInt(driveAttribute.getValue()));
		}

		// 业务类型:0/1/2=工作业务/保护业务/额外业务
		if (driveAttribute.getDescription().equals("业务类型")) {
			tunnelObject.setServiceType(Integer.parseInt(driveAttribute.getValue()));
		}
		// 所归属的SNCP  ID
		if (driveAttribute.getDescription().equals("所属SNCPid")) {
			tunnelObject.setSncpID(Integer.parseInt(driveAttribute.getValue()));
		}
		// (前向TUNNEL)TUNNEL ID
		if (driveAttribute.getDescription().equals("(前向TUNNEL)TUNNEL ID")) {
			tunnelObject.getForeLSP().setLspId(Integer.parseInt(driveAttribute.getValue()));
		}

		//(前向TUNNEL)TUNNEL节点类型
		if (driveAttribute.getDescription().equals("(前向TUNNEL)TUNNEL节点类型")) {
			tunnelObject.getForeLSP().setTunnelType(Integer.parseInt(driveAttribute.getValue()));
		}
		// (前向TUNNEL)(入标记)槽位号
		if (driveAttribute.getDescription().equals("(前向TUNNEL)(入标记)槽位号")) {
			tunnelObject.getForeLSP().setInSlot(Integer.parseInt(driveAttribute.getValue()));
		}

		// (前向TUNNEL)(入标记)端口号
		if (driveAttribute.getDescription().equals("(前向TUNNEL)(入标记)端口号")) {
			tunnelObject.getForeLSP().setInProt(Integer.parseInt(driveAttribute.getValue()));
		}

		// (前向TUNNEL)(入标记)标签
		if (driveAttribute.getDescription().equals("(前向TUNNEL)(入标记)标签")) {
			tunnelObject.getForeLSP().setInLable(Integer.parseInt(driveAttribute.getValue()));
		}
		
		// (前向TUNNEL)(入标记)TTL
		if (driveAttribute.getDescription().equals("(前向TUNNEL)(入标记)TTL")) {
			tunnelObject.getForeLSP().setInTTL(Integer.parseInt(driveAttribute.getValue()));
		}

		// (前向TUNNEL)(入标记)TC
		if (driveAttribute.getDescription().equals("(前向TUNNEL)(入标记)TC")) {
			tunnelObject.getForeLSP().setInTC(Integer.parseInt(driveAttribute.getValue()));
		}
		//(前向TUNNEL)(宽带控制)带宽控制使能
		if("(前向TUNNEL)(宽带控制)带宽控制使能".equals(driveAttribute.getDescription())){
			tunnelObject.getForeLSP().setBandwidthEnable(Integer.parseInt(driveAttribute.getValue()));
		}

		//(前向TUNNEL)(宽带控制)CIR
		if (driveAttribute.getDescription().equals("(前向TUNNEL)(宽带控制)CIR")) {
			tunnelObject.getForeLSP().setCir(Integer.parseInt(driveAttribute.getValue()));
		}
		
		//(前向TUNNEL)(宽带控制)PIR
		if (driveAttribute.getDescription().equals("(前向TUNNEL)(宽带控制)PIR")) {
			tunnelObject.getForeLSP().setPir(Integer.parseInt(driveAttribute.getValue()));
		}
		
		//(前向TUNNEL)(宽带控制)CBS
		if (driveAttribute.getDescription().equals("(前向TUNNEL)(宽带控制)CBS")) {
			tunnelObject.getForeLSP().setCbs(Integer.parseInt(driveAttribute.getValue()));
		}
		
		//(前向TUNNEL)(宽带控制)PBS
		if (driveAttribute.getDescription().equals("(前向TUNNEL)(宽带控制)PBS")) {
			tunnelObject.getForeLSP().setPbs(Integer.parseInt(driveAttribute.getValue()));
		}
		////(前向TUNNEL)COS
		if (driveAttribute.getDescription().equals("(前向TUNNEL)COS")) {
			tunnelObject.getForeLSP().setCos(Integer.parseInt(driveAttribute.getValue()));
		}
		// (前向TUNNEL)(出标记)槽位号
		if (driveAttribute.getDescription().equals("(前向TUNNEL)(出标记)槽位号")) {
			tunnelObject.getForeLSP().setOutSlot(Integer.parseInt(driveAttribute.getValue()));
		}

		// (前向TUNNEL)(出标记)端口号
		if (driveAttribute.getDescription().equals("(前向TUNNEL)(出标记)端口号")) {
			tunnelObject.getForeLSP().setOutprot(Integer.parseInt(driveAttribute.getValue()));
		}

		// (前向TUNNEL)(出标记)标签
		if (driveAttribute.getDescription().equals("(前向TUNNEL)(出标记)标签")) {
			tunnelObject.getForeLSP().setOutLable(Integer.parseInt(driveAttribute.getValue()));
		}
		// (前向TUNNEL)(出标记)TTL
		if (driveAttribute.getDescription().equals("(前向TUNNEL)(出标记)TTL")) {
			tunnelObject.getForeLSP().setOutTTL(Integer.parseInt(driveAttribute.getValue()));
		}

		// (前向TUNNEL)(出标记)TC
		if (driveAttribute.getDescription().equals("(前向TUNNEL)(出标记)TC")) {
			tunnelObject.getForeLSP().setOutTC(Integer.parseInt(driveAttribute.getValue()));
		}
		
		// (后向TUNNEL)TUNNEL ID
		if (driveAttribute.getDescription().equals("(后向TUNNEL)TUNNEL ID")) {
			tunnelObject.getAfterLSP().setLspId(Integer.parseInt(driveAttribute.getValue()));
		}

		//(后向TUNNEL)TUNNEL节点类型
		if (driveAttribute.getDescription().equals("(后向TUNNEL)TUNNEL节点类型")) {
			tunnelObject.getAfterLSP().setTunnelType(Integer.parseInt(driveAttribute.getValue()));
		}
		// (后向TUNNEL)(入标记)槽位号
		if (driveAttribute.getDescription().equals("(后向TUNNEL)(入标记)槽位号")) {
			tunnelObject.getAfterLSP().setInSlot(Integer.parseInt(driveAttribute.getValue()));
		}

		// (后向TUNNEL)(入标记)端口号
		if (driveAttribute.getDescription().equals("(后向TUNNEL)(入标记)端口号")) {
			tunnelObject.getAfterLSP().setInProt(Integer.parseInt(driveAttribute.getValue()));
		}

		// (后向TUNNEL)(入标记)标签
		if (driveAttribute.getDescription().equals("(后向TUNNEL)(入标记)标签")) {
			tunnelObject.getAfterLSP().setInLable(Integer.parseInt(driveAttribute.getValue()));
		}

		// (后向TUNNEL)(入标记)TTL
		if (driveAttribute.getDescription().equals("(后向TUNNEL)(入标记)TTL")) {
			tunnelObject.getAfterLSP().setInTTL(Integer.parseInt(driveAttribute.getValue()));
		}

		// (后向TUNNEL)(入标记)TC
		if (driveAttribute.getDescription().equals("(后向TUNNEL)(入标记)TC")) {
			tunnelObject.getAfterLSP().setInTC(Integer.parseInt(driveAttribute.getValue()));
		}
		// (后向TUNNEL)(出标记)槽位号
		if (driveAttribute.getDescription().equals("(后向TUNNEL)(出标记)槽位号")) {
			tunnelObject.getAfterLSP().setOutSlot(Integer.parseInt(driveAttribute.getValue()));
		}

		// (后向TUNNEL)(出标记)端口号
		if (driveAttribute.getDescription().equals("(后向TUNNEL)(出标记)端口号")) {
			tunnelObject.getAfterLSP().setOutprot(Integer.parseInt(driveAttribute.getValue()));
		}

		// (后向TUNNEL)(出标记)标签
		if (driveAttribute.getDescription().equals("(后向TUNNEL)(出标记)标签")) {
			tunnelObject.getAfterLSP().setOutLable(Integer.parseInt(driveAttribute.getValue()));
		}
		
		// (后向TUNNEL)(出标记)TTL
		if (driveAttribute.getDescription().equals("(后向TUNNEL)(出标记)TTL")) {
			tunnelObject.getAfterLSP().setOutTTL(Integer.parseInt(driveAttribute.getValue()));
		}

		// (后向TUNNEL)(出标记)TC
		if (driveAttribute.getDescription().equals("(后向TUNNEL)(出标记)TC")) {
			tunnelObject.getAfterLSP().setOutTC(Integer.parseInt(driveAttribute.getValue()));
		}
		
		
		//(后向TUNNEL)(宽带控制)带宽控制使能
		if("(后向TUNNEL)(宽带控制)带宽控制使能".equals(driveAttribute.getDescription())){
			tunnelObject.getAfterLSP().setBandwidthEnable(Integer.parseInt(driveAttribute.getValue()));
		}
		
		//(后向TUNNEL)(宽带控制)PIR
		if (driveAttribute.getDescription().equals("(后向TUNNEL)(宽带控制)PIR")) {
			tunnelObject.getAfterLSP().setPir(Integer.parseInt(driveAttribute.getValue()));
		}
		
		//(后向TUNNEL)(宽带控制)CIR
		if (driveAttribute.getDescription().equals("(后向TUNNEL)(宽带控制)CIR")) {
			tunnelObject.getAfterLSP().setCir(Integer.parseInt(driveAttribute.getValue()));
		}
		
		//(后向TUNNEL)(宽带控制)PBS
		if (driveAttribute.getDescription().equals("(后向TUNNEL)(宽带控制)PBS")) {
			tunnelObject.getAfterLSP().setPbs(Integer.parseInt(driveAttribute.getValue()));
		}
		
		//(后向TUNNEL)(宽带控制)CBS
		if (driveAttribute.getDescription().equals("(后向TUNNEL)(宽带控制)CBS")) {
			tunnelObject.getAfterLSP().setCbs(Integer.parseInt(driveAttribute.getValue()));
		}
		
		//(后向TUNNEL)COS
		if (driveAttribute.getDescription().equals("(后向TUNNEL)COS")) {
			tunnelObject.getAfterLSP().setCos(Integer.parseInt(driveAttribute.getValue()));
		}
		//(OAM)源MEP/MIP ID
		if("(OAM)源MEP/MIP ID".equals(driveAttribute.getDescription())){
			tunnelObject.setSourceMEP(Integer.parseInt(driveAttribute.getValue()));
		}
		//(OAM)宿MEP ID
		if("(OAM)宿MEP ID".equals(driveAttribute.getDescription())){
			tunnelObject.setEquityMEP(Integer.parseInt(driveAttribute.getValue()));
		}
		//(OAM)APS使能
		if("(OAM)APS使能".equals(driveAttribute.getDescription())){
			tunnelObject.setAspEnabl(Integer.parseInt(driveAttribute.getValue()));
		}else if("(OAM)OAM使能".equals(driveAttribute.getDescription())){
			tunnelObject.setOamEnable(Integer.parseInt(driveAttribute.getValue()));
		}
		// MEG ICC1
		if ("(OAM)MEG ICC1".equals(driveAttribute.getDescription())) {
			sb_megicc.append(driveAttribute.getValue() + ",");
		}

		// MEG ICC2
		if ("(OAM)MEG ICC2".equals(driveAttribute.getDescription())) {
			sb_megicc.append(driveAttribute.getValue() + ",");
		}

		// MEG ICC3
		if ("(OAM)MEG ICC3".equals(driveAttribute.getDescription())) {
			sb_megicc.append(driveAttribute.getValue() + ",");
		}

		// MEG ICC4
		if ("(OAM)MEG ICC4".equals(driveAttribute.getDescription())) {
			sb_megicc.append(driveAttribute.getValue() + ",");
		}

		// MEG ICC5
		if ("(OAM)MEG ICC5".equals(driveAttribute.getDescription())) {
			sb_megicc.append(driveAttribute.getValue() + ",");
		}

		// MEG ICC6
		if ("(OAM)MEG ICC6".equals(driveAttribute.getDescription())) {
			sb_megicc.append(driveAttribute.getValue());
			tunnelObject.setMegIcc(sb_megicc.toString());
		}
		// MEG UMC1
		if ("(OAM)MEG UMC1".equals(driveAttribute.getDescription())) {
			sb_megumc.append(driveAttribute.getValue() + ",");
		}

		// MEG UMC2
		if ("(OAM)MEG UMC2".equals(driveAttribute.getDescription())) {
			sb_megumc.append(driveAttribute.getValue() + ",");
		}

		// MEG UMC3
		if ("(OAM)MEG UMC3".equals(driveAttribute.getDescription())) {
			sb_megumc.append(driveAttribute.getValue() + ",");
		}

		// MEG UMC4
		if ("(OAM)MEG UMC4".equals(driveAttribute.getDescription())) {
			sb_megumc.append(driveAttribute.getValue() + ",");
		}

		// MEG UMC5
		if ("(OAM)MEG UMC5".equals(driveAttribute.getDescription())) {
			sb_megumc.append(driveAttribute.getValue() + ",");
		}

		// MEG UMC6
		if ("(OAM)MEG UMC6".equals(driveAttribute.getDescription())) {
			sb_megumc.append(driveAttribute.getValue());
			tunnelObject.setMegUmc(sb_megumc.toString());
		}
		//oam帧配置
		if("(OAM)字节116".equals(driveAttribute.getDescription())){
			TOoam(tunnelObject, driveAttribute);
		}
		//DM帧配置
		if("(OAM)CV帧配置".equals(driveAttribute.getDescription())){
			TOcv(tunnelObject, driveAttribute);
		}
		//LM使能
		if("(OAM)LM使能".equals(driveAttribute.getDescription())){
			tunnelObject.setLmEnabl(Integer.parseInt(driveAttribute.getValue()));
		}
		//前向源MAC地址1
		if("前向源MAC地址1".equals(driveAttribute.getDescription())){
			sourceMac.append(CoderUtils.synchTransformMac(driveAttribute.getValue())+"-");
		}
		//前向源MAC地址2
		if("前向源MAC地址2".equals(driveAttribute.getDescription())){
			sourceMac.append(CoderUtils.synchTransformMac(driveAttribute.getValue())+"-");
		}
		//前向源MAC地址3
		if("前向源MAC地址3".equals(driveAttribute.getDescription())){
			sourceMac.append(CoderUtils.synchTransformMac(driveAttribute.getValue())+"-");
		}
		//前向源MAC地址4
		if("前向源MAC地址4".equals(driveAttribute.getDescription())){
			sourceMac.append(CoderUtils.synchTransformMac(driveAttribute.getValue())+"-");
		}
		//前向源MAC地址5
		if("前向源MAC地址5".equals(driveAttribute.getDescription())){
			sourceMac.append(CoderUtils.synchTransformMac(driveAttribute.getValue())+"-");
		}
		//前向源MAC地址6
		if("前向源MAC地址6".equals(driveAttribute.getDescription())){
			sourceMac.append(CoderUtils.synchTransformMac(driveAttribute.getValue()));
			tunnelObject.getForeLSP().setSourceMac(sourceMac.toString());
		}
		//前向目的MAC地址1
		if("前向目的MAC地址1".equals(driveAttribute.getDescription())){
			targetMac.append(CoderUtils.synchTransformMac(driveAttribute.getValue())+"-");
		}
		//前向目的MAC地址2
		if("前向目的MAC地址2".equals(driveAttribute.getDescription())){
			targetMac.append(CoderUtils.synchTransformMac(driveAttribute.getValue())+"-");
		}
		//前向目的MAC地址3
		if("前向目的MAC地址3".equals(driveAttribute.getDescription())){
			targetMac.append(CoderUtils.synchTransformMac(driveAttribute.getValue())+"-");
		}
		//前向目的MAC地址4
		if("前向目的MAC地址4".equals(driveAttribute.getDescription())){
			targetMac.append(CoderUtils.synchTransformMac(driveAttribute.getValue())+"-");
		}
		//前向目的MAC地址5
		if("前向目的MAC地址5".equals(driveAttribute.getDescription())){
			targetMac.append(CoderUtils.synchTransformMac(driveAttribute.getValue())+"-");
		}
		//前向目的MAC地址6
		if("前向目的MAC地址6".equals(driveAttribute.getDescription())){
			targetMac.append(CoderUtils.synchTransformMac(driveAttribute.getValue()));
			tunnelObject.getForeLSP().setTargetMac(targetMac.toString());
		}
		//后向源MAC地址1
		if("后向源MAC地址1".equals(driveAttribute.getDescription())){
			sourceMac_after.append(CoderUtils.synchTransformMac(driveAttribute.getValue())+"-");
		}
		//后向源MAC地址2
		if("后向源MAC地址2".equals(driveAttribute.getDescription())){
			sourceMac_after.append(CoderUtils.synchTransformMac(driveAttribute.getValue())+"-");
		}
		//后向源MAC地址3
		if("后向源MAC地址3".equals(driveAttribute.getDescription())){
			sourceMac_after.append(CoderUtils.synchTransformMac(driveAttribute.getValue())+"-");
		}
		//后向源MAC地址4
		if("后向源MAC地址4".equals(driveAttribute.getDescription())){
			sourceMac_after.append(CoderUtils.synchTransformMac(driveAttribute.getValue())+"-");
		}
		//后向源MAC地址5
		if("后向源MAC地址5".equals(driveAttribute.getDescription())){
			sourceMac_after.append(CoderUtils.synchTransformMac(driveAttribute.getValue())+"-");
		}
		//后向源MAC地址6
		if("后向源MAC地址6".equals(driveAttribute.getDescription())){
			sourceMac_after.append(CoderUtils.synchTransformMac(driveAttribute.getValue()));
			tunnelObject.getAfterLSP().setSourceMac(sourceMac_after.toString());
		}
		//后向目的MAC地址1
		if("后向目的MAC地址1".equals(driveAttribute.getDescription())){
			targetMac_after.append(CoderUtils.synchTransformMac(driveAttribute.getValue())+"-");
		}
		//后向目的MAC地址2
		if("后向目的MAC地址2".equals(driveAttribute.getDescription())){
			targetMac_after.append(CoderUtils.synchTransformMac(driveAttribute.getValue())+"-");
		}
		//后向目的MAC地址3
		if("后向目的MAC地址3".equals(driveAttribute.getDescription())){
			targetMac_after.append(CoderUtils.synchTransformMac(driveAttribute.getValue())+"-");
		}
		//后向目的MAC地址4
		if("后向目的MAC地址4".equals(driveAttribute.getDescription())){
			targetMac_after.append(CoderUtils.synchTransformMac(driveAttribute.getValue())+"-");
		}
		//后向目的MAC地址5
		if("后向目的MAC地址5".equals(driveAttribute.getDescription())){
			targetMac_after.append(CoderUtils.synchTransformMac(driveAttribute.getValue())+"-");
		}
		//后向目的MAC地址6
		if("后向目的MAC地址6".equals(driveAttribute.getDescription())){
			targetMac_after.append(CoderUtils.synchTransformMac(driveAttribute.getValue()));
			tunnelObject.getAfterLSP().setTargetMac(targetMac_after.toString());
		}
		//前向增加外层VLAN使能
		if("前向增加外层VLAN使能".equals(driveAttribute.getDescription())){
			tunnelObject.getForeLSP().setVlanEnable(Integer.parseInt(driveAttribute.getValue()));
		}
		//前向外层VLAN值
		if("前向外层VLAN值".equals(driveAttribute.getDescription())){
			tunnelObject.getForeLSP().setOutVlanValue(Integer.parseInt(driveAttribute.getValue()));
		}
		//前向外层TP_ID
		if("前向外层TP_ID".equals(driveAttribute.getDescription())){
			tunnelObject.getForeLSP().setTpId(Integer.parseInt(driveAttribute.getValue()));
		}
		//后向增加外层VLAN使能
		if("后向增加外层VLAN使能".equals(driveAttribute.getDescription())){
			tunnelObject.getAfterLSP().setVlanEnable(Integer.parseInt(driveAttribute.getValue()));
		}
		//后向外层VLAN值
		if("后向外层VLAN值".equals(driveAttribute.getDescription())){
			tunnelObject.getAfterLSP().setOutVlanValue(Integer.parseInt(driveAttribute.getValue()));
		}
		//后向外层TP_ID
		if("后向外层TP_ID".equals(driveAttribute.getDescription())){
			tunnelObject.getAfterLSP().setTpId(Integer.parseInt(driveAttribute.getValue()));
		}else if("(前向TUNNEL)源IP1".equals(driveAttribute.getDescription())){
			sourceIP.append(Integer.parseInt(driveAttribute.getValue())+".");
		}else if("(前向TUNNEL)源IP2".equals(driveAttribute.getDescription())){
			sourceIP.append(Integer.parseInt(driveAttribute.getValue())+".");
		}else if("(前向TUNNEL)源IP3".equals(driveAttribute.getDescription())){
			sourceIP.append(Integer.parseInt(driveAttribute.getValue())+".");
		}else if("(前向TUNNEL)源IP4".equals(driveAttribute.getDescription())){
			sourceIP.append(Integer.parseInt(driveAttribute.getValue()));
			tunnelObject.getForeLSP().setSourceIP(sourceIP.toString());
		}else if("(前向TUNNEL)目的IP1".equals(driveAttribute.getDescription())){
			targetIP.append(Integer.parseInt(driveAttribute.getValue())+".");
		}else if("(前向TUNNEL)目的IP2".equals(driveAttribute.getDescription())){
			targetIP.append(Integer.parseInt(driveAttribute.getValue())+".");
		}else if("(前向TUNNEL)目的IP3".equals(driveAttribute.getDescription())){
			targetIP.append(Integer.parseInt(driveAttribute.getValue())+".");
		}else if("(前向TUNNEL)目的IP4".equals(driveAttribute.getDescription())){
			targetIP.append(Integer.parseInt(driveAttribute.getValue()));
			tunnelObject.getForeLSP().setTargetIP(targetIP.toString());
		}else if("(后向TUNNEL)源IP1".equals(driveAttribute.getDescription())){
			sourceIP_after.append(Integer.parseInt(driveAttribute.getValue())+".");
		}else if("(后向TUNNEL)源IP2".equals(driveAttribute.getDescription())){
			sourceIP_after.append(Integer.parseInt(driveAttribute.getValue())+".");
		}else if("(后向TUNNEL)源IP3".equals(driveAttribute.getDescription())){
			sourceIP_after.append(Integer.parseInt(driveAttribute.getValue())+".");
		}else if("(后向TUNNEL)源IP4".equals(driveAttribute.getDescription())){
			sourceIP_after.append(Integer.parseInt(driveAttribute.getValue()));
			tunnelObject.getAfterLSP().setSourceIP(sourceIP_after.toString());
		}else if("(后向TUNNEL)目的IP1".equals(driveAttribute.getDescription())){
			targetIP_after.append(Integer.parseInt(driveAttribute.getValue())+".");
		}else if("(后向TUNNEL)目的IP2".equals(driveAttribute.getDescription())){
			targetIP_after.append(Integer.parseInt(driveAttribute.getValue())+".");
		}else if("(后向TUNNEL)目的IP3".equals(driveAttribute.getDescription())){
			targetIP_after.append(Integer.parseInt(driveAttribute.getValue())+".");
		}else if("(后向TUNNEL)目的IP4".equals(driveAttribute.getDescription())){
			targetIP_after.append(Integer.parseInt(driveAttribute.getValue()));
			tunnelObject.getAfterLSP().setTargetIP(targetIP_after.toString());
		}
	}
	/**
	 * 命令转lm
	 * @param info
	 * @param driveAttribute
	 */
	private void TOoam(TunnelObject info,DriveAttribute driveAttribute){
		String str = CoderUtils.convertBinary(Integer.parseInt(driveAttribute.getValue()));
		str = eightBinary(str);
		info.setOam_bao(str.substring(0,3));
		info.setOam_mel(str.substring(3, 6));
		info.setOam_tmp(str.substring(6, 7));
		info.setOam_resore(str.substring(7));
	}
	/**
	 * 命令转dm
	 * @param info
	 * @param driveAttribute
	 */
	private void TOcv(TunnelObject info,DriveAttribute driveAttribute){
		String str = CoderUtils.convertBinary(Integer.parseInt(driveAttribute.getValue()));
		str = eightBinary(str);
		info.setCvReserve(str.substring(0,4));
		info.setCvCycle(str.substring(4, 7));
		info.setCvEnabl(str.substring(7));
	}
	/**
	 * 将字符串补足8位
	 * @param str
	 */
	private String eightBinary(String str){
		String s = null;
		StringBuffer sb = new StringBuffer();
		if(str.length()<8){			
			for (int i = str.length(); i < 8; i++) {
				sb.append("0");
			}		
		}
		sb.append(str);
		s = sb.toString();
		return s;
	}
	
}
