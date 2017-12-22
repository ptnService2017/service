package com.nms.ui.ptn.alarm.controller;

import java.util.ArrayList;
import java.util.List;

import com.nms.db.bean.equipment.shelf.SiteInst;
import com.nms.db.bean.system.Field;
import com.nms.model.equipment.shlef.SiteService_MB;
import com.nms.model.system.FieldService_MB;
import com.nms.model.util.Services;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.UiUtil;
import com.nms.util.Mybatis_DBManager;


/**
 * 主动获取网元失连，通过计数方式判断
 * @author pc
 *
 */
public class SiteConnectTask extends Thread{
	
	private boolean isRun=false;
	
	public SiteConnectTask(){
		this.setName("SiteConnectTask");
		this.start();
	}
	
	/**
	 * 外部停止线程
	 */
	public void stopThread() {
		this.isRun = false;
	}

	/**
	 * 外部开启线程
	 */
	public void startThread() {
		this.isRun = true;
	}
	
	@Override
	public void run() {
		while(isRun){
			SiteService_MB siteService = null;
			List<SiteInst> siteInsts = null;
			List<SiteInst> needupdateSites = null;//需修改状态网元
			List<Field> fields = null;
			FieldService_MB fieldService = null;
			List<SiteInst> mSites = null;
			try {
				siteService = (SiteService_MB) ConstantUtil.serviceFactory.newService_MB(Services.SITE);
				fieldService = (FieldService_MB) ConstantUtil.serviceFactory.newService_MB(Services.Field);
				needupdateSites = new ArrayList<SiteInst>();
				if(ConstantUtil.siteStausMap.keySet().size() == 0){//网元没有收到任何设备上报的网元状态信息
					siteInsts = siteService.select();
					for(SiteInst siteInst : siteInsts){
						if(siteInst.getManufacturer() == 0 && siteInst.getLoginstatus() == 1){
							siteInst.setLoginstatus(0);
							needupdateSites.add(siteInst);
						}
					}
				}else{//网管收到部分设备上报的网元状态信息
					
					//处理有M网元的域
					mSites = siteService.selectByISmsite();
					for(SiteInst mSiteInst : mSites){
						long time = System.currentTimeMillis();
						if(ConstantUtil.siteStausMap.get(mSiteInst.getCellDescribe()) == null || 
								(ConstantUtil.siteStausMap.get(mSiteInst.getCellDescribe()) != null 
									&& time-ConstantUtil.siteStausMap.get(mSiteInst.getCellDescribe())>10000)){
							siteInsts = siteService.querySitesByIp(mSiteInst.getCellDescribe());
							for(SiteInst siteInst : siteInsts){
								if(siteInst.getManufacturer() == 0 && siteInst.getLoginstatus() == 1){
									siteInst.setLoginstatus(0);
									needupdateSites.add(siteInst);
								}
							}
						}
					}
				
					//处理没有M网元的域
					fields = fieldService.queryNoMsite();
					if(fields !=null && fields.size()>0){
						for(Field field :fields){
							SiteInst inst = new SiteInst();
							inst.setFieldID(field.getId());
							siteInsts = siteService.selectByFieldId(inst);
							for(SiteInst siteInst : siteInsts){
								if(siteInst.getManufacturer() == 0 && siteInst.getLoginstatus() == 1){
									siteInst.setLoginstatus(0);
									needupdateSites.add(siteInst);
								}
							}
						}
					}
				}
				
				siteService.updateBatch(needupdateSites);
//				System.out.println("已经用了的最大连接数Mybatis_DBManager"+Mybatis_DBManager.getInstance().getDataSource().getNumBusyConnections());
//				System.out.println("空闲的最大连接数Mybatis_DBManager"+Mybatis_DBManager.getInstance().getDataSource().getNumIdleConnections());
				ExceptionManage.infor("已经用了的最大连接数Mybatis_DBManager"+Mybatis_DBManager.getInstance().getDataSource().getNumBusyConnections(), this.getClass());
				ExceptionManage.infor("空闲的最大连接数Mybatis_DBManager"+Mybatis_DBManager.getInstance().getDataSource().getNumIdleConnections(), this.getClass());
			} catch (Exception e) {
				ExceptionManage.dispose(e,this.getClass());
			}finally{
				UiUtil.closeService_MB(siteService);
				UiUtil.closeService_MB(fieldService);
				siteInsts = null;
				needupdateSites = null;//需修改状态网元
				fields = null;
			    mSites = null;
			}
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				ExceptionManage.dispose(e,this.getClass());
			}
		}	
	}

}
