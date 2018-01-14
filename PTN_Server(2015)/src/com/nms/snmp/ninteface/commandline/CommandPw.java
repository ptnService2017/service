package com.nms.snmp.ninteface.commandline;

import java.util.List;

import com.nms.db.bean.ptn.path.pw.PwInfo;
import com.nms.db.enums.EPwType;
import com.nms.model.ptn.path.pw.PwInfoService_MB;
import com.nms.model.util.Services;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.UiUtil;

public class CommandPw {
	public String pwCommand(String command,Boolean page,Integer size){
		String[] param = command.split(";");
		if(param.length != 4){
			return "param error";
		}
		PwInfoService_MB pwService_MB = null;
		StringBuffer buffer = new StringBuffer();
		try {
			pwService_MB = (PwInfoService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PwInfo);
			if("queryall".equals(param[1])){
				List<PwInfo> list = null;
				if(page){
					list = pwService_MB.selectPage(Integer.parseInt(param[2]),(Integer.parseInt(param[3])-1)*size,size);
				}else{
					list = pwService_MB.selectPage(Integer.parseInt(param[2]),0,Integer.MAX_VALUE);
				}
				buffer.append("result:success"+"\r\n");
				for(PwInfo pwInfo : list){
					buffer.append("siteId:"+Integer.parseInt(param[2])+" ");
					buffer.append("pwId:"+pwInfo.getPwId()+" ");
					buffer.append("tunnelId:"+pwInfo.getTunnelId()+" ");
					buffer.append("pwName:"+pwInfo.getPwName()+" ");
					buffer.append("inlabel:"+(pwInfo.getASiteId()== Integer.parseInt(param[2])?pwInfo.getInlabelValue():pwInfo.getOutlabelValue())+" ");
					buffer.append("outlabel:"+(pwInfo.getZSiteId()== Integer.parseInt(param[2])?pwInfo.getOutlabelValue():pwInfo.getInlabelValue())+" ");
					buffer.append("active:"+(pwInfo.getPwStatus()==1?"ACTIVE":"PENDING")+"\r\n");
				}
			}else if("querybase".equals(param[1])){
				PwInfo pwInfo = pwService_MB.selectById(Integer.parseInt(param[3]));
				if(pwInfo != null){
					buffer.append("siteId:"+Integer.parseInt(param[2])+" ");
					buffer.append("pwId:"+pwInfo.getPwId()+" ");
					buffer.append("tunnelId:"+pwInfo.getTunnelId()+" ");
					buffer.append("pwName:"+pwInfo.getPwName()+" ");
					buffer.append("inlabel:"+(pwInfo.getASiteId()== Integer.parseInt(param[2])?pwInfo.getInlabelValue():pwInfo.getOutlabelValue())+" ");
					buffer.append("outlabel:"+(pwInfo.getZSiteId()== Integer.parseInt(param[2])?pwInfo.getOutlabelValue():pwInfo.getInlabelValue())+" ");
					buffer.append("active:"+(pwInfo.getPwStatus()==1?"ACTIVE":"PENDING")+"\r\n");
				
				}else{
					buffer.append("result:0"+"\r\n");
				}
			}else if("create".equals(param[1])){
				PwInfo pwInfo = new PwInfo();
				Integer siteId = Integer.parseInt(param[2]);
				String[] strs = param[3].split("-");
				if(strs.length == 4){
					pwInfo.setASiteId(siteId);
					pwInfo.setTunnelId(Integer.parseInt(strs[0]));
					pwInfo.setType(EPwType.ETH);
					pwInfo.setInlabelValue(Integer.parseInt(strs[1]));
					pwInfo.setOutlabelValue(Integer.parseInt(strs[2]));
					pwInfo.setPwStatus(Integer.parseInt(strs[3]));
					pwInfo.setIsSingle(1);
					pwInfo.setBusinessType("0");
					pwInfo.setPwName("Pw_"+System.currentTimeMillis());
					pwService_MB.save(pwInfo);
					buffer.append("result:success"+"\r\n");
					buffer.append("pwId:"+pwInfo.getPwId()+"\r\n");
				}else{
					buffer.append("result:param error"+"\r\n");
				}
				
			}else if("update".equals(param[1])){
				PwInfo pwInfo = pwService_MB.selectById(Integer.parseInt(param[2]));
				if(pwInfo == null){
					buffer.append("result:pwid not exist"+"\r\n");
				}else{
					String[] strs = param[3].split("-");
					if(strs.length == 3){
						pwInfo.setPwId(Integer.parseInt(param[2]));
						pwInfo.setInlabelValue(Integer.parseInt(strs[0]));
						pwInfo.setOutlabelValue(Integer.parseInt(strs[1]));
						pwInfo.setPwStatus(Integer.parseInt(strs[2]));
						pwService_MB.updateLableById(Integer.parseInt(param[2]), Integer.parseInt(strs[0]), Integer.parseInt(strs[1]));
						buffer.append("result:success"+"\r\n");
						buffer.append("pwId:"+pwInfo.getPwId()+"\r\n");
					}else{
						buffer.append("result:param error"+"\r\n");
					}
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			UiUtil.closeService_MB(pwService_MB);
		}
		buffer.append(">");
		return buffer.toString();
	}
}
