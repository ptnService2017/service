package com.nms.snmp.ninteface.impl.ftp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.nms.db.enums.EMonitorCycle;
import com.nms.snmp.ninteface.framework.SnmpConfig;
import com.nms.ui.manager.ExceptionManage;

/**
 * @author guoqc
 * ftp自动传输功能
 * 每隔24小时自动上传文件
 */
public class FtpTransThread implements Runnable {
//	private String collectTime = "";
	private boolean suspendFlag = false;// 控制线程的执行
	private Integer timeType = 0;
	public static void main(String[] args) {
		SnmpConfig.getInstanse().init();
		FtpTransThread t = new FtpTransThread(3);
//		t.collectTime = "20140826";
		new Thread(t).start();
	}
	
	public FtpTransThread(Integer timeType) {
		this.timeType = timeType;
	}

	@Override
	public void run() {
		try {
			//定时采集网管配置并生成文件
//			TimerTask task = new FtpTransTimerTask(this.collectTime);
			TimerTask task = new FtpTransTimerTask();
			Timer timer = new Timer();
			long interval_24hour = EMonitorCycle.HOUR24.getInterval();
			timer.schedule(task, getIntervalTime(), interval_24hour);//安排指定的任务开始进行重复的固定延迟执行
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
	}
	
	private Date getIntervalTime() {
		try {
			Calendar calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH);
			int day = calendar.get(Calendar.DAY_OF_MONTH);//每天
			
			if(timeType==1){//定制每天的12:00:00执行，
				calendar.set(year, month, day, 12, 00, 00);
			}else if(timeType==2){//定制每天的24:00:00执行，
				calendar.set(year, month, day, 00, 00, 00);
			}else if(timeType==3){//测试
				calendar.set(year, month, day, 22, 19, 00);
			}
			Date date = calendar.getTime();
			return date;
		} catch (Exception e) {
			ExceptionManage.dispose(e,this.getClass());
		}
		return null;
	}

	/**
	 * 线程暂停
	 */
	public void setSuspendFlag() {
		this.suspendFlag = true;
	}

	/**
	 * 唤醒线程
	 */
	public synchronized void setResume() {
		this.suspendFlag = false;
		notify();
	}
	
	/**
	 * 返回线程状态
	 * @return
	 */
	public boolean getSuspendFlag(){
		return this.suspendFlag;
	}
}
