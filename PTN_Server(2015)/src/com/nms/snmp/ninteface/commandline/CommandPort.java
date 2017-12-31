package com.nms.snmp.ninteface.commandline;

import java.util.List;

import com.nms.db.bean.equipment.port.PortInst;
import com.nms.model.equipment.port.PortService_MB;
import com.nms.model.util.Services;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.UiUtil;

public class CommandPort {

	public String portCommand(String command,Boolean page,Integer size){
		String[] param = command.split(";");
		if(param.length != 4){
			return "param error";
		}
		PortService_MB portService_MB = null;
		StringBuffer buffer = new StringBuffer();
		try {
			portService_MB = (PortService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PORT);
			if("queryall".equals(param[1])){
				List<PortInst> list = null;
				if(page){
					list = portService_MB.selectPage(Integer.parseInt(param[2]),Integer.parseInt(param[3]),size);
				}else{
					list = portService_MB.selectBySiteid(Integer.parseInt(param[2]));
				}
				buffer.append("result:success"+"\r\n");
				for(PortInst port : list){
					buffer.append("siteId:"+port.getSiteId()+" ");
					buffer.append("portId:"+port.getPortId()+" ");
					buffer.append("portName:"+port.getPortName()+" ");
					buffer.append("portNum:"+port.getNumber()+" ");
					buffer.append("portType:"+port.getPortType()+"\r\n");
				}
			}else if("querybase".equals(param[1])){
				PortInst port = portService_MB.selectPortybyid(Integer.parseInt(param[3]));
				if(port != null){
					buffer.append("result:success"+"\r\n");
					buffer.append("siteId:"+port.getSiteId()+" ");
					buffer.append("portId:"+port.getPortId()+" ");
					buffer.append("portName:"+port.getPortName()+" ");
					buffer.append("portNum:"+port.getNumber()+" ");
					buffer.append("portType:"+port.getPortType()+"\r\n");
				}else{
					buffer.append("result:0"+"\r\n");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			UiUtil.closeService_MB(portService_MB);
		}
		
		return buffer.toString();
	}
}
