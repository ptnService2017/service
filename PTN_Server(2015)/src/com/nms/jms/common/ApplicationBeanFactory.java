package com.nms.jms.common;


import java.net.InetAddress;
import java.sql.Date;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.nms.snmp.ninteface.util.NorthConfig;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.ExceptionManage;

public class ApplicationBeanFactory {
	private static ApplicationContext context;
	private static ApplicationContext serviceContext;
	
	/**
	 * 初始化队列
	 * @throws Exception 
	 */
	public static void initBeanFactory(String path,String connectionFactory) throws Exception{
		try {
		InetAddress addr = InetAddress.getLocalHost();
		String serverIp = addr.getHostAddress().toString();
		String jmsIp = "tcp://" + serverIp + ":61616";
		ActiveMQConnectionFactory jmsConnectionFactory = null;
		if(path.contains("applicationContext-jms-send.xml")){
			context = new ClassPathXmlApplicationContext(path);
			jmsConnectionFactory = getActiveMQConnectionFactory(connectionFactory, context);
			ExceptionManage.infor("DATAX:   告警send已启动"+new Date(System.currentTimeMillis()), ApplicationBeanFactory.class);
		}else if(path.contains("applicationContext-jms-serviceCourse.xml")){
			serviceContext = new ClassPathXmlApplicationContext(path);
			jmsConnectionFactory = getActiveMQConnectionFactory(connectionFactory, serviceContext);
			ExceptionManage.infor("DATAX:   告警receive已启动"+new Date(System.currentTimeMillis()),ApplicationBeanFactory.class);
		}else if(path.contains("applicationContext-jms-serviceCourseT.xml")){
			jmsIp = "tcp://" + NorthConfig.northServiceIp + ":61616";
			serviceContext = new ClassPathXmlApplicationContext(path);
			jmsConnectionFactory = getActiveMQConnectionFactory(connectionFactory, serviceContext);
			ExceptionManage.infor("DATAX:   告警receive已启动"+new Date(System.currentTimeMillis()),ApplicationBeanFactory.class);
		}
		jmsConnectionFactory.setBrokerURL(jmsIp);
		} catch (Exception e) {
		 ExceptionManage.dispose(e, ApplicationBeanFactory.class);
		}
	}
	
	private static ActiveMQConnectionFactory getActiveMQConnectionFactory(String connectionFactory,ApplicationContext context){
		ActiveMQConnectionFactory jmsConnectionFactory = (ActiveMQConnectionFactory) context.getBean(connectionFactory);
		return jmsConnectionFactory;
	}
	/**
	 * 获取配置文件的bean对象
	 * @param beanName
	 * @return
	 */
	 public static Object getBean(String beanName){
		 return context.getBean(beanName);
	 }
	 
	 
	 public static void initNorthBeanFactory(String path,String connectionFactory) throws Exception{
		 try {
			 String jmsIp = "tcp://" + NorthConfig.northServiceIp + ":61616";
			 ActiveMQConnectionFactory jmsConnectionFactory = null;
			 serviceContext = new ClassPathXmlApplicationContext(path);
			 jmsConnectionFactory = getActiveMQConnectionFactory(connectionFactory, serviceContext);
			 ExceptionManage.infor("北向告警监听"+new Date(System.currentTimeMillis()),ApplicationBeanFactory.class);
			 jmsConnectionFactory.setBrokerURL(jmsIp);
		 } catch (Exception e) {
			 ExceptionManage.dispose(e, ApplicationBeanFactory.class);
		 }
	 }
}
