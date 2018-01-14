package com.nms.snmp.ninteface.commandline;

import java.util.List;

import com.nms.db.bean.ptn.path.tunnel.Lsp;
import com.nms.db.bean.ptn.path.tunnel.Tunnel;
import com.nms.model.ptn.path.tunnel.TunnelService_MB;
import com.nms.model.util.Services;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.UiUtil;

public class CommandTunnel {
	
	public String tunnelCommand(String command,Boolean page,Integer size){
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
					list = tunnelService_MB.selectPage(Integer.parseInt(param[2]),(Integer.parseInt(param[3])-1)*size,size);
				}else{
					list = tunnelService_MB.selectPage(Integer.parseInt(param[2]),0,Integer.MAX_VALUE);
				}
				buffer.append("result:success"+"\r\n");
				for(Tunnel tunnel : list){
					buffer.append("siteId:"+param[2]+" ");
					buffer.append("tunnelId:"+tunnel.getTunnelId()+" ");
					buffer.append("tunnelName:"+tunnel.getTunnelName()+" ");
					List<Lsp> lspParticularList = tunnel.getLspParticularList();
					if (lspParticularList.size() == 1) {
						if (lspParticularList.get(0).getASiteId() == Integer.parseInt(param[2])) {
							buffer.append("inlable:"+lspParticularList.get(0).getBackLabelValue()+" ");
							buffer.append("inport:"+lspParticularList.get(0).getAPortId()+" ");
							buffer.append("outlable:"+lspParticularList.get(0).getFrontLabelValue()+" ");
							buffer.append("outport:"+lspParticularList.get(0).getAPortId()+" ");
						}else if(lspParticularList.get(0).getZSiteId() == Integer.parseInt(param[2])){
							buffer.append("inlable:"+lspParticularList.get(0).getFrontLabelValue()+" ");
							buffer.append("inport:"+lspParticularList.get(0).getZPortId()+" ");
							buffer.append("outlable:"+lspParticularList.get(0).getBackLabelValue()+" ");
							buffer.append("outport:"+lspParticularList.get(0).getZPortId()+" ");
						}
					}else if(lspParticularList.size() == 2){
						if (lspParticularList.get(0).getASiteId() == Integer.parseInt(param[2])) {
							buffer.append("f-inlable:"+lspParticularList.get(0).getBackLabelValue()+" ");
							buffer.append("f-inport:"+lspParticularList.get(0).getAPortId()+" ");
							buffer.append("f-outlable:"+lspParticularList.get(0).getFrontLabelValue()+" ");
							buffer.append("f-outport:"+lspParticularList.get(0).getAPortId()+" ");
						}else if(lspParticularList.get(0).getZSiteId() == Integer.parseInt(param[2])){
							buffer.append("f-inlable:"+lspParticularList.get(0).getFrontLabelValue()+" ");
							buffer.append("f-inport:"+lspParticularList.get(0).getZPortId()+" ");
							buffer.append("f-outlable:"+lspParticularList.get(0).getBackLabelValue()+" ");
							buffer.append("f-outport:"+lspParticularList.get(0).getZPortId()+" ");
						}
						if (lspParticularList.get(1).getASiteId() == Integer.parseInt(param[2])) {
							buffer.append("b-inlable:"+lspParticularList.get(1).getBackLabelValue()+" ");
							buffer.append("b-inport:"+lspParticularList.get(1).getAPortId()+" ");
							buffer.append("b-outlable:"+lspParticularList.get(1).getFrontLabelValue()+" ");
							buffer.append("b-outport:"+lspParticularList.get(1).getAPortId()+" ");
						}else if(lspParticularList.get(1).getZSiteId() == Integer.parseInt(param[2])){
							buffer.append("b-inlable:"+lspParticularList.get(1).getFrontLabelValue()+" ");
							buffer.append("b-inport:"+lspParticularList.get(1).getZPortId()+" ");
							buffer.append("b-outlable:"+lspParticularList.get(1).getBackLabelValue()+" ");
							buffer.append("b-outport:"+lspParticularList.get(1).getZPortId()+" ");
						}
					}
					buffer.append("active:"+(tunnel.getTunnelStatus()==1?"ACTIVE":"PENDING")+"\r\n");
				}
			}else if("querybase".equals(param[1])){
				Tunnel tunnel = tunnelService_MB.selectByID(Integer.parseInt(param[3]));
				if(tunnel != null){
					buffer.append("result:success"+"\r\n");
					buffer.append("siteId:"+param[2]+" ");
					buffer.append("tunnelId:"+tunnel.getTunnelId()+" ");
					buffer.append("tunnelName:"+tunnel.getTunnelName()+" ");
					List<Lsp> lspParticularList = tunnel.getLspParticularList();
					if (lspParticularList.size() == 1) {
						if (lspParticularList.get(0).getASiteId() == Integer.parseInt(param[2])) {
							buffer.append("inlable:"+lspParticularList.get(0).getBackLabelValue()+" ");
							buffer.append("inport:"+lspParticularList.get(0).getAPortId()+" ");
							buffer.append("outlable:"+lspParticularList.get(0).getFrontLabelValue()+" ");
							buffer.append("outport:"+lspParticularList.get(0).getAPortId()+" ");
						}else if(lspParticularList.get(0).getZSiteId() == Integer.parseInt(param[2])){
							buffer.append("inlable:"+lspParticularList.get(0).getFrontLabelValue()+" ");
							buffer.append("inport:"+lspParticularList.get(0).getZPortId()+" ");
							buffer.append("outlable:"+lspParticularList.get(0).getBackLabelValue()+" ");
							buffer.append("outport:"+lspParticularList.get(0).getZPortId()+" ");
						}
					}else if(lspParticularList.size() == 2){
						if (lspParticularList.get(0).getASiteId() == Integer.parseInt(param[2])) {
							buffer.append("f-inlable:"+lspParticularList.get(0).getBackLabelValue()+" ");
							buffer.append("f-inport:"+lspParticularList.get(0).getAPortId()+" ");
							buffer.append("f-outlable:"+lspParticularList.get(0).getFrontLabelValue()+" ");
							buffer.append("f-outport:"+lspParticularList.get(0).getAPortId()+" ");
						}else if(lspParticularList.get(0).getZSiteId() == Integer.parseInt(param[2])){
							buffer.append("f-inlable:"+lspParticularList.get(0).getFrontLabelValue()+" ");
							buffer.append("f-inport:"+lspParticularList.get(0).getZPortId()+" ");
							buffer.append("f-outlable:"+lspParticularList.get(0).getBackLabelValue()+" ");
							buffer.append("f-outport:"+lspParticularList.get(0).getZPortId()+" ");
						}
						if (lspParticularList.get(1).getASiteId() == Integer.parseInt(param[2])) {
							buffer.append("b-inlable:"+lspParticularList.get(1).getBackLabelValue()+" ");
							buffer.append("b-inport:"+lspParticularList.get(1).getAPortId()+" ");
							buffer.append("b-outlable:"+lspParticularList.get(1).getFrontLabelValue()+" ");
							buffer.append("b-outport:"+lspParticularList.get(1).getAPortId()+" ");
						}else if(lspParticularList.get(1).getZSiteId() == Integer.parseInt(param[2])){
							buffer.append("b-inlable:"+lspParticularList.get(1).getFrontLabelValue()+" ");
							buffer.append("b-inport:"+lspParticularList.get(1).getZPortId()+" ");
							buffer.append("b-outlable:"+lspParticularList.get(1).getBackLabelValue()+" ");
							buffer.append("b-outport:"+lspParticularList.get(1).getZPortId()+" ");
						}
					}
					buffer.append("active:"+(tunnel.getTunnelStatus()==1?"ACTIVE":"PENDING")+"\r\n");
				}else{
					buffer.append("result:0"+"\r\n");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			UiUtil.closeService_MB(tunnelService_MB);
		}
		buffer.append(">");
		return buffer.toString();
	}
}
