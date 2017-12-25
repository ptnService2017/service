package com.nms.jms.jmsMeanager;


import javax.jms.Destination;

import org.springframework.jms.core.JmsTemplate;

import com.nms.jms.common.OpviewMessage;


/**
 * 发送消息接口
 * @author Administrator
 *	
 */
public class JmsSender {
	private JmsTemplate template;
	private Destination serverOtherNotify;
	private Destination alarmNotify;
	
	
	/**
	 * 服务进程
	 * @param message
	 */
	public void sendToServer(OpviewMessage message){
		template.convertAndSend(serverOtherNotify, message);
	}
	
	public void sendNorthAlarm(OpviewMessage message){
		template.convertAndSend(alarmNotify, message);
	}
	
	public JmsTemplate getTemplate() {
		return template;
	}

	public void setTemplate(JmsTemplate template) {
		this.template = template;
	}


	public Destination getServerOtherNotify() {
		return serverOtherNotify;
	}

	public void setServerOtherNotify(Destination serverOtherNotify) {
		this.serverOtherNotify = serverOtherNotify;
	}

	public Destination getAlarmNotify() {
		return alarmNotify;
	}

	public void setAlarmNotify(Destination alarmNotify) {
		this.alarmNotify = alarmNotify;
	}
	
	
}
