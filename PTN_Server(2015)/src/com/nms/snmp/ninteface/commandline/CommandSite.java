package com.nms.snmp.ninteface.commandline;

import java.util.List;
import java.util.Map;

import com.nms.db.bean.equipment.card.CardInst;
import com.nms.db.bean.equipment.shelf.SiteInst;
import com.nms.model.equipment.card.CardService_MB;
import com.nms.model.equipment.shlef.SiteService_MB;
import com.nms.model.util.Services;
import com.nms.service.impl.wh.SiteWHServiceImpl;
import com.nms.service.impl.wh.SlotResetWHServiceImpl;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.UiUtil;

public class CommandSite {
	
	
	public String siteCommand(String command,Boolean page,Integer size){
		String[] param = command.split(";");
		SiteService_MB siteService_MB = null;
		if(param.length != 3){
			return "param error";
		}
		StringBuffer buffer = new StringBuffer();
		CardService_MB cardService = null;
		try {
			siteService_MB = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE);
			
			if("queryall".equals(param[1])){
				List<SiteInst> list = null;
				if(page){
					list = siteService_MB.selectPage(size, Integer.parseInt(param[2]));
				}else{
					list = siteService_MB.select();
				}
				buffer.append("result:success"+"\r\n");
				for(SiteInst siteInst : list){
					getHardEdition(siteInst);
					buffer.append("id:"+siteInst.getSite_Inst_Id()+" ");
					buffer.append("nativeName:"+siteInst.getCellId()+" ");
					buffer.append("IPAddress:"+siteInst.getCellDescribe()+" ");
					buffer.append("hardwareVersion:"+siteInst.getHardEdition()+" ");
					buffer.append("softwareVersion:"+siteInst.getSoftEdition()+" ");
					buffer.append("state:"+(siteInst.getLoginstatus()==1?"available":"unavaliable")+"\r\n");
				}
			}else if("querybase".equals(param[1])){
				SiteInst siteInst = siteService_MB.select(Integer.parseInt(param[2]));
				if(siteInst != null){
					buffer.append("result:success"+"\r\n");
					getHardEdition(siteInst);
					buffer.append("id:"+siteInst.getSite_Inst_Id()+" ");
					buffer.append("nativeName:"+siteInst.getCellId()+" ");
					buffer.append("IPAddress:"+siteInst.getCellDescribe()+" ");
					buffer.append("hardwareVersion:"+siteInst.getHardEdition()+" ");
					buffer.append("softwareVersion:"+siteInst.getSoftEdition()+" ");
					buffer.append("state:"+(siteInst.getLoginstatus()==1?"available":"unavaliable")+"\r\n");
				}else{
					buffer.append("result:0");
				}
			}else if("settime".equals(param[1])){
				SiteInst siteInst = siteService_MB.select(Integer.parseInt(param[2]));
				if(siteInst != null){
					SiteWHServiceImpl siteWHServiceImpl = new SiteWHServiceImpl();
					String str = siteWHServiceImpl.currectTime(siteInst);
					if("配置成功".equals(str)){
						buffer.append("result:success");
					}else{//失败
						buffer.append("result:1");
					}
				}else{//网元不存在
					buffer.append("result:0");
				}
			}else if("restart".equals(param[1])){
				cardService = (CardService_MB) ConstantUtil.serviceFactory.newService_MB(Services.CARD);
				CardInst cardInst = new CardInst();
				cardInst.setSiteId(Integer.parseInt(param[2]));
				List<CardInst> list = cardService.select(cardInst);
				SlotResetWHServiceImpl impl = new SlotResetWHServiceImpl();
				String str = "";
				for(CardInst inst : list){
					if(inst.getCardName().contains("MCU") ||inst.getCardName().contains("703") || inst.getCardName().contains("XCTS1")||inst.getCardName().contains("XCTO1")
							 ||"CSG T2000_CARD".equals(inst.getCardName())|| "SP16".equals(inst.getCardName())	
							 || inst.getCardName().contains("ETN-200")|| "ESMC".equals(inst.getCardName())|| "EE16".equals(inst.getCardName())
					){
						str = impl.excutionInsert(inst);
					}
				}
				if("配置成功".equals(str)){
					buffer.append("result:success");
				}else{//失败
					buffer.append("result:1");
				}
			}else if("inspection".equals(param[1])){
				List<Map<String,Object>> list = null;
				if(page){
					list = siteService_MB.alarmInspection((Integer.parseInt(param[3])-1)*size,size);
				}else{
					list = siteService_MB.alarmInspection(0,Integer.MAX_VALUE);
				}
				buffer.append("result:success"+"\r\n");
				for(Map<String, Object> map : list){
					buffer.append("siteId:"+map.get("site_inst_id")+" ");
					buffer.append("loginstatus:"+map.get("loginstatus")+" ");
					Long i = (Long) map.get("alarmstatus");
					if(i!= null && i>1){
						buffer.append("alarmstatus:"+1+"\r\n");
					}else{
						buffer.append("alarmstatus:"+0+"\r\n");
					}
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			UiUtil.closeService_MB(cardService);
			UiUtil.closeService_MB(siteService_MB);
		}
		buffer.append(">");
		return buffer.toString();
	}
	
	
	private String getHardEdition(SiteInst siteInst){
		String type = "EB204.002V03";
		String soft = "V2.1.3";
		if(siteInst.getCellType().equals("ETN-5000")){
			type = "EB5000.003V01";
			soft = "V3.2.5";
		}else if(siteInst.getCellType().equals("ETN-200-204E")){
			type = "EB204E.002V03";
		}
		siteInst.setHardEdition(type);
		siteInst.setSoftEdition(soft);
		return type;
	}
}
