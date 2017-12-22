package com.nms.model.util;

import com.nms.db.bean.ptn.LabelInfo;
import com.nms.db.dao.ptn.LabelInfoMapper;
import com.nms.model.equipment.shlef.SiteService_MB;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.UiUtil;

public class LabelManage {
	private  final int INIT_LABELVALUE = 16;
	private  final int ADD_COUNT = 99;
	
	
	public void addLabel(int aSiteId, int zSiteId, String type, LabelInfoMapper mapper) throws Exception {
		int begin = 0;
		int end = 0;
		int a = 0;
		int z = 0;
		boolean aType = true;//true/false = 晨晓/武汉
		boolean zType = true;//true/false = 晨晓/武汉
		try {
			aType = this.isChenXiao(aSiteId);
			zType = this.isChenXiao(zSiteId);
			//目前设备的芯片不支持同一端口的lsp的入标签和该端口上的pw的入标签一样,所以要用下面的代码
			if(type.equals("TUNNEL") || type.equals("PW")){
				type = "WH";
			}
			//如果以后芯片支持同一端口的lsp的入标签和该端口上的pw的入标签一样,就用下面的代码,把上面的代码关掉
			LabelInfo condition = new LabelInfo();
			condition.setSiteid(aSiteId);
			condition.setType(type);
			String aMaxValue = mapper.queryMaxLabelValue(condition);
			condition.setSiteid(zSiteId);
			String zMaxValue = mapper.queryMaxLabelValue(condition);
			if(aMaxValue == null){
				a  = 0;
			}else{
				a = Integer.parseInt(aMaxValue);
			}
			
			if(zMaxValue == null){
				z = 0;
			}else{
				z = Integer.parseInt(zMaxValue);
			}
			
			if(a >= z && z != 0){
				begin=z+1;
				end=begin+ADD_COUNT;
			}
			if(a < z && a != 0){
				begin=a+1;
				end=begin+ADD_COUNT;
			}
			if(a==0 || z==0){
				begin=INIT_LABELVALUE;
				end=ADD_COUNT+1;
			}
			for(int i = begin; i <= end; i++){
				//给a网元分配标签
				LabelInfo label = new LabelInfo();
				label.setSiteid(aSiteId);
				if(i == end){
					label.setLsrId(1);
				}else{
					label.setLsrId(0);
				}
				label.setLabelValue(i);
				label.setLabelStatus(1);
				label.setType(aType ? "CX" : type);
				mapper.insert(label);
				//给z网元分配标签
				label.setSiteid(zSiteId);
				label.setType(zType ? "CX" : type);
				mapper.insert(label);
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e,LabelManage.class);
		}
	}
	
	/**
	 * 判断该网元是晨晓设备还是武汉设备
	 */
	private boolean isChenXiao(int siteId) throws Exception{
		SiteService_MB siteService = null;
		try {
			siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE);
			//等于1是晨晓设备
			int manufacturer = siteService.getManufacturer(siteId);
			if(manufacturer == 1){
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			throw e;
		}finally{
			UiUtil.closeService_MB(siteService);
		}
	}
}
