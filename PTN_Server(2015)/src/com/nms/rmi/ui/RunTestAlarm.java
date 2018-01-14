package com.nms.rmi.ui;

import java.util.ArrayList;
import java.util.List;

import com.nms.db.bean.alarm.CurrentAlarmInfo;
import com.nms.db.bean.alarm.HisAlarmInfo;
import com.nms.model.alarm.CurAlarmService_MB;
import com.nms.model.alarm.HisAlarmService_MB;
import com.nms.model.util.ServiceFactory;
import com.nms.model.util.Services;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.UiUtil;
import com.nms.util.Mybatis_DBManager;

public class RunTestAlarm extends Thread{

	@Override
	public void run() {
		CurAlarmService_MB curAlarmService_MB = null;
		HisAlarmService_MB hisAlarmService_MB = null;
		try {
			hisAlarmService_MB = (HisAlarmService_MB) ConstantUtil.serviceFactory.newService_MB(Services.HisAlarm);
			curAlarmService_MB = (CurAlarmService_MB) ConstantUtil.serviceFactory.newService_MB(Services.CurrentAlarm);
			HisAlarmInfo hisAlarmInfo = hisAlarmService_MB.queryHisNorthIndex();
			CurrentAlarmInfo currentAlarmInfo = curAlarmService_MB.queryNorthTest();
			List<CurrentAlarmInfo> CList = new ArrayList<CurrentAlarmInfo>();
			List<HisAlarmInfo> list = new ArrayList<HisAlarmInfo>();
			for (int i = 1; i < 910000; i++) {
				list.add(hisAlarmInfo);
				CList.add(currentAlarmInfo);
				if(i%500==0){
					hisAlarmService_MB.insertNorthBatch(list);
					list  = new ArrayList<HisAlarmInfo>();
					curAlarmService_MB.insertNorthBatch(CList);
					CList = new ArrayList<CurrentAlarmInfo>();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			UiUtil.closeService_MB(hisAlarmService_MB);
			UiUtil.closeService_MB(curAlarmService_MB);
		}
		
	}
	
	public static void main(String[] args) {
		Mybatis_DBManager.init("127.0.0.1");
		 ConstantUtil.serviceFactory = new ServiceFactory();
		 RunTestAlarm alarm = new RunTestAlarm();
		 new Thread(alarm).start();
	}
}
