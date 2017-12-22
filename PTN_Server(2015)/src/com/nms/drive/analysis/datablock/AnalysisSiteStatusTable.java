package com.nms.drive.analysis.datablock;

import java.util.List;

import com.nms.drive.analysis.AnalysisObjectService;
import com.nms.drive.service.bean.ARPObject;
import com.nms.drive.service.bean.status.BfdStatusObject;
import com.nms.drive.service.bean.status.E1LegStatusObject;
import com.nms.drive.service.bean.status.FrequencySourceObject;
import com.nms.drive.service.bean.status.FrequencyStatusObject;
import com.nms.drive.service.bean.status.LagStatusObject;
import com.nms.drive.service.bean.status.PortStatusObject;
import com.nms.drive.service.bean.status.ProtectStatusObject;
import com.nms.drive.service.bean.status.PtpBasicStatusObject;
import com.nms.drive.service.bean.status.PtpPortStatusObject;
import com.nms.drive.service.bean.status.PwProtectStatusObject;
import com.nms.drive.service.bean.status.PwStatusObject;
import com.nms.drive.service.bean.status.SiteStatusObject;
import com.nms.drive.service.bean.status.TunnelStatusObject;
import com.nms.drive.service.bean.status.VpwsStatusObject;
import com.nms.drive.service.bean.status.WrappingStatusObject;
import com.nms.drive.service.impl.CoderUtils;
import com.nms.service.bean.ActionObject;

/**
 * 查询状态
 * @author pc
 *
 */
public class AnalysisSiteStatusTable extends AnalysisBase{
	private int contorl = 7;//网元信息，包括网元地址，盘地址
	private int marking = 25;//某种状态的标示信息
	
	public ActionObject analysisCommandToObject(byte[] cammands) throws Exception{
		ActionObject actionObject = new ActionObject();
		SiteStatusObject siteStatusInfo = new SiteStatusObject();
		int number = cammands[contorl-1];//配置块的个数
		int start = marking*number+contorl;
		for (int i = 0; i < number; i++) {
			int markStatus = cammands[9+marking*i];
			byte[] datas = {cammands[10+marking*i],cammands[11+marking*i],cammands[12+marking*i],cammands[13+marking*i]};
			int dataLength = CoderUtils.bytesToInt(datas);
			byte[] bytes = null;
			if(markStatus == 17){//TUNNEL状态信息
				List<TunnelStatusObject> tunnelStatusList = null;
				bytes = new byte[dataLength];
				System.arraycopy(cammands,start ,bytes, 0, bytes.length);
				AnalysisObjectService analysisObjectService = new AnalysisObjectService();
				tunnelStatusList = analysisObjectService.AnalysisTunnelStatus(bytes);
				siteStatusInfo.setTunnelStatusListObject(tunnelStatusList);
			}else if(markStatus == 18){//环网保护状态信息
				List<WrappingStatusObject> wrappingStatusObjectList = null;
				bytes = new byte[dataLength];
				System.arraycopy(cammands,start ,bytes, 0, bytes.length);
				AnalysisObjectService analysisObjectService = new AnalysisObjectService();
				wrappingStatusObjectList = analysisObjectService.AnalysisWrappingStatus(bytes);
				siteStatusInfo.setWrappingStatusObjectList(wrappingStatusObjectList);
			}else if(markStatus == 19){//LSP保护状态信息
				List<ProtectStatusObject> protectStatusList = null;
				bytes = new byte[dataLength];
				System.arraycopy(cammands,start ,bytes, 0, bytes.length);
				AnalysisObjectService analysisObjectService = new AnalysisObjectService();
				protectStatusList = analysisObjectService.AnalysisLspProetctStatus(bytes);
				siteStatusInfo.setProtectStatusListObject(protectStatusList);
			}else if(markStatus == 20){//时钟状态基本信息
				FrequencyStatusObject frequencyStatusInfo = null;
				bytes = new byte[dataLength];
				System.arraycopy(cammands,start ,bytes, 0, bytes.length);
				AnalysisClcokBasicStatusTable analysisClcokBasicStatusTable = new AnalysisClcokBasicStatusTable();
				frequencyStatusInfo = analysisClcokBasicStatusTable.analysisCommandsToFrequencyStatusInfo(bytes, "com/nms/drive/analysis/xmltool/file/时钟状态基本信息(0x14).xml");
				siteStatusInfo.setFrequencyStatusObject(frequencyStatusInfo);
			}else if(markStatus == 21){//时钟状态变长信息
				List<FrequencySourceObject> frequencySources = null;
				bytes = new byte[dataLength];
				System.arraycopy(cammands,start, bytes, 0, bytes.length);
				AnalysisClcokChangeStatusTable analysisClcokChangeStatusTable = new AnalysisClcokChangeStatusTable();
				frequencySources = analysisClcokChangeStatusTable.analysisCommandsToFrequencyStatusInfo(bytes, "com/nms/drive/analysis/xmltool/file/时钟状态变长信息(0x15)_SUB.xml");
				siteStatusInfo.setFrequencySourcesObject(frequencySources);
			}else if(markStatus == 22){//PW状态信息
				List<PwStatusObject> pwStatus = null;
				bytes = new byte[dataLength];
				System.arraycopy(cammands,start, bytes, 0, bytes.length);
				AnalysisObjectService analysisObjectService = new AnalysisObjectService();
				pwStatus = analysisObjectService.AnalysisPwStatus(bytes);
				siteStatusInfo.setPwStatusObject(pwStatus);
			}else if(markStatus == 23){//端口状态信息
				List<PortStatusObject> portStatusList = null;
				bytes = new byte[dataLength];
				System.arraycopy(cammands,start, bytes, 0, bytes.length);
				AnalysisObjectService analysisObjectService = new AnalysisObjectService();
				portStatusList = analysisObjectService.AnalysisPortStatus(bytes);
				siteStatusInfo.setPortStatusListObject(portStatusList);
			}else if(markStatus == 29){//业务状态信息
				List<VpwsStatusObject> vpwsStatusList = null;
				bytes = new byte[dataLength];
				System.arraycopy(cammands,start, bytes, 0, bytes.length);
				AnalysisObjectService analysisObjectService = new AnalysisObjectService();
				vpwsStatusList = analysisObjectService.AnalysisVpwsStatus(bytes);
				siteStatusInfo.setVpwsStatusListObject(vpwsStatusList);
			}else if(markStatus == 30){//E1仿真状态信息
				List<E1LegStatusObject> e1LegStatusList = null;
				bytes = new byte[dataLength];
				System.arraycopy(cammands,start, bytes, 0, bytes.length);
				AnalysisObjectService analysisObjectService = new AnalysisObjectService();
				e1LegStatusList = analysisObjectService.AnalysisE1LegStatus(bytes);
				siteStatusInfo.setE1LegStatusObjectList(e1LegStatusList);
				
			}else if(markStatus == 33){//pw保护状态信息
				List<PwProtectStatusObject> pwProtectStatusList = null;
				bytes = new byte[dataLength];
				System.arraycopy(cammands,start, bytes, 0, bytes.length);
				AnalysisObjectService analysisObjectService = new AnalysisObjectService();
				pwProtectStatusList = analysisObjectService.AnalysisPwProtectStatus(bytes);
				siteStatusInfo.setPwProtectStatusObjectList(pwProtectStatusList);
			}else if(markStatus == 66){//lag端口聚合状态信息
				List<LagStatusObject> lagStatusList = null;
				bytes = new byte[dataLength];
				System.arraycopy(cammands,start, bytes, 0, bytes.length);
				AnalysisObjectService analysisObjectService = new AnalysisObjectService();
				lagStatusList = analysisObjectService.AnalysisLagStatus(bytes);
				siteStatusInfo.setLagStatusObjectList(lagStatusList);
			}else if(markStatus == 25){//ptp端口信息
				List<PtpPortStatusObject> ptpPortStatusList = null;
				bytes = new byte[dataLength];
				System.arraycopy(cammands,start, bytes, 0, bytes.length);
				AnalysisObjectService analysisObjectService = new AnalysisObjectService();
				ptpPortStatusList = analysisObjectService.AnalysisPtpPortStatus(bytes);
				siteStatusInfo.setPtpPortStatusObjectList(ptpPortStatusList);
			}else if(markStatus == 24){//ptp基本端口信息
				List<PtpBasicStatusObject> ptpBasicStatusList = null;
				bytes = new byte[dataLength];
				System.arraycopy(cammands,start, bytes, 0, bytes.length);
				AnalysisObjectService analysisObjectService = new AnalysisObjectService();
				ptpBasicStatusList = analysisObjectService.AnalysisPtpBasicStatus(bytes);
				siteStatusInfo.setPtpBasicStatusObjectList(ptpBasicStatusList);
			}else if(markStatus == 67){//Bfd
				List<BfdStatusObject> bfdStatusList = null;
				bytes = new byte[dataLength];
				System.arraycopy(cammands,start, bytes, 0, bytes.length);
				AnalysisObjectService analysisObjectService = new AnalysisObjectService();
				bfdStatusList = analysisObjectService.AnalysisBfdStatus(bytes);
				siteStatusInfo.setBfdStatusListObject(bfdStatusList);
			}else if(markStatus == 68){//arp
				List<ARPObject> arpStatusList = null;
				bytes = new byte[dataLength];
				System.arraycopy(cammands,start, bytes, 0, bytes.length);
				AnalysisObjectService analysisObjectService = new AnalysisObjectService();
				arpStatusList = analysisObjectService.AnalysisArpStatus(bytes);
				siteStatusInfo.setArpStatusObjectList(arpStatusList);
			}
			start = start+bytes.length;
		}
		actionObject.setSiteStatusObject(siteStatusInfo);
		return actionObject;
		
	}
}
