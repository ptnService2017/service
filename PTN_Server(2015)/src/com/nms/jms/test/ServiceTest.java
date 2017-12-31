package com.nms.jms.test;

import org.apache.log4j.Logger;

import com.nms.jms.common.ApplicationBeanFactory;



public class ServiceTest {
	private static final Logger log = Logger.getLogger("alarmrunLogger");
			
	public static void main(String[] args) throws Exception {
//		ApplicationBeanFactory.initBeanFactory("applicationContext-jms-send.xml","targetConnectionFactory");
//		Thread1 thread1 = new Thread1("alarm");
//		new Thread(thread1).start();
//		Thread1 thread2 = new Thread1("service");
//		new Thread(thread2).start();
		
//		ApplicationBeanFactory.initBeanFactory("applicationContext-jms-northAlarm.xml","targetConnectionFactory");
//		log.info("设置");
		ApplicationBeanFactory.initBeanFactory("applicationContext-jms-serviceCourseT.xml","targetConnectionFactory");
		
	}
}
