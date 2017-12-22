package com.nms.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.nms.db.bean.perform.PerformanceTaskInfo;
import com.nms.db.enums.ERunStates;
import com.nms.model.perform.PerformanceTaskService_MB;
import com.nms.model.util.Services;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.DateUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.UiUtil;

public class PerformanceThread  implements Runnable{

	private boolean flga = true;
	
	@Override
	public void run() {
		try {
			while(flga){
				queryPerformance();
				//每分钟执行一次
				Thread.sleep(60000);
			}
		} catch (InterruptedException e) {
			flga = false;
//			ExceptionManage.dispose(e, getClass());
		}
	}


	private void queryPerformance() {
		PerformanceTaskService_MB service = null;
		List<PerformanceTaskInfo> taskInfos = null;
		List<PerformanceTaskInfo> performanceTaskRunState = new ArrayList<PerformanceTaskInfo>();
		try {
			service = (PerformanceTaskService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PerformanceTask);
			taskInfos = service.select();
			if(taskInfos !=null && taskInfos.size()>0){
				for (PerformanceTaskInfo taskInfo : taskInfos) {
					try {
						if (taskInfo.getRunstates() == ERunStates.RUN) {
							if(isCreate(taskInfo)){
								performanceTaskRunState.add(taskInfo);
							}
						}
					} catch (Exception e) {
						ExceptionManage.dispose(e, getClass());
					}
				}
				//处理满足条件的采集任务
				if(performanceTaskRunState.size()>0){
					PerformanceHisPerThread PerformanceHisPerThread = new PerformanceHisPerThread(performanceTaskRunState);
					Thread threadHis = new Thread(PerformanceHisPerThread);
					threadHis.start();
				}
			}else{
				flga = false;
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, getClass());
		}finally{
			UiUtil.closeService_MB(service);
		}
	}

	private boolean isCreate(PerformanceTaskInfo taskInfo){
		
		SimpleDateFormat format = null;
		SimpleDateFormat formatOther = null;
		String startTime = "";
		String endTime = "";
		Date date = new Date();
		int cycle = 0;
		
		try {
			
		 format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 formatOther = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		 startTime =  taskInfo.getCreateTime();
		 cycle = taskInfo.getMonitorCycle().getValue();
		 endTime = taskInfo.getEndTime();
		 
		//如果配置结束时间:先比较是否不需要执行
		 if(endTime != null && !"".equals(endTime)){
			 //如果配置结束时间:先比较是否需要执行：如果当前时间大于结束时间 不需要执行否则需要执行
			 if(date.getTime() < format.parse(taskInfo.getEndTime().trim()).getTime()){
				 return isCycleTime(formatOther,startTime,date,format,cycle);
			 }else{
				 return false;
			 }
		 }else{
			 return isCycleTime(formatOther,startTime,date,format,cycle);
		 }
		 
		} catch (Exception e) {
			ExceptionManage.dispose(e, getClass());
		}finally{
			 format = null;
			 formatOther = null;
		}
	   return false;
		
	}
	
	private boolean isCycleTime(SimpleDateFormat formatOther,String startTime,Date date,SimpleDateFormat format,int cycle){
		try {
			//如果结束时间为空,则比较开始时间是否相等或者 是否当前时间是任务的周期时间
			long l = formatOther.parse(startTime).getTime();
			long l1 = formatOther.parse(format.format(date)).getTime();
			//开始时间相等
			if(l == l1){
				return true;
			}else if(l>l1){
				return false;
			}else{
				//如果不等 看当前时间是否为其开始时间+时间周期
				if(cycle == 1 && ((l1 -l)/(1000*60))%15 == 0 ){
					return true;
				}
				else if(cycle == 2 && (l1-l > 1000*60*60*24 || l1-l == 1000*60*60*24 )){
					if(((l1-l)%(1000*60*60*24)) ==0){
						return true;
					}
				}
			}
			
		} catch (Exception e) {
			ExceptionManage.dispose(e, getClass());
		}
		return false;
	}

	public boolean isFlga() {
		return flga;
	}


	public void setFlga(boolean flga) {
		this.flga = flga;
	}

	//结束任务,释放资源
	public void stop(){
		flga = false;
	}
	
}
