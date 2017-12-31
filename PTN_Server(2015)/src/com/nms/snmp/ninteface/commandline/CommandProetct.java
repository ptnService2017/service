package com.nms.snmp.ninteface.commandline;

import java.util.List;

import com.nms.db.bean.ptn.path.tunnel.Tunnel;
import com.nms.model.ptn.path.tunnel.TunnelService_MB;
import com.nms.model.util.Services;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.UiUtil;

public class CommandProetct {

	
	public String proetctCommand(String command,Boolean page,Integer size){
		String[] param = command.split(";");
		if(param.length != 4){
			return "param error";
		}
		TunnelService_MB tunnelService_MB = null;
		StringBuffer buffer = new StringBuffer();
		try {
			tunnelService_MB = (TunnelService_MB) ConstantUtil.serviceFactory.newService_MB(Services.Tunnel);
			if("queryall".equals(param[1])){
				List<Tunnel> list = null;
				if(page){
					list = tunnelService_MB.queryProetctPage(Integer.parseInt(param[2]),(Integer.parseInt(param[3])-1)*size,size);
				}else{
					list = tunnelService_MB.queryProetctPage(Integer.parseInt(param[2]),0,Integer.MAX_VALUE);
				}
				buffer.append("result:success"+"\r\n");
				for(Tunnel tunnel : list){
					buffer.append("siteId:"+Integer.parseInt(param[2])+" ");
					buffer.append("proetctId:"+tunnel.getTunnelId()+" ");
					buffer.append("protectBusessId:"+(tunnel.getaSiteId() ==Integer.parseInt(param[2])?tunnel.getAprotectId():tunnel.getZprotectId()) +" ");
					buffer.append("proetctName:"+tunnel.getTunnelName()+" ");
					buffer.append("reversionMode:"+(tunnel.getRotateMode().equals("MANUAL")?"RM_REVERTIVE":"RM_NON_REVERTIVE")+" ");
					buffer.append("type:"+"1:1"+"\r\n");
				}
			}else if("querybase".equals(param[1])){
				Tunnel tunnel = tunnelService_MB.selectByID(Integer.parseInt(param[3]));
				if(tunnel != null){
					buffer.append("siteId:"+Integer.parseInt(param[2])+" ");
					buffer.append("proetctId:"+tunnel.getTunnelId()+" ");
					buffer.append("protectBusessId:"+(tunnel.getaSiteId() ==Integer.parseInt(param[2])?tunnel.getAprotectId():tunnel.getZprotectId()) +" ");
					buffer.append("proetctName:"+tunnel.getTunnelName()+" ");
					buffer.append("reversionMode:"+(tunnel.getRotateMode().equals("MANUAL")?"RM_REVERTIVE":"RM_NON_REVERTIVE")+" ");
					buffer.append("type:"+"1:1"+"\r\n");
				}else{
					buffer.append("result:0"+"\r\n");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			UiUtil.closeService_MB(tunnelService_MB);
		}
		
		return buffer.toString();
		
	}
}
