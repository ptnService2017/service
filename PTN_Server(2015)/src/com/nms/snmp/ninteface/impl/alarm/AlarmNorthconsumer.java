package com.nms.snmp.ninteface.impl.alarm;

import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import com.nms.drive.service.bean.AlarmObject;
import com.nms.jms.common.OpviewMessage;
import com.nms.jms.serviceCourse.CopyOfServiceCourseMessageListener;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.ptn.alarm.AlarmTools;

public class AlarmNorthconsumer implements MessageListener{

	@Override
	public void onMessage(Message message) {
		if(message instanceof ObjectMessage){
			ObjectMessage objectMessage = (ObjectMessage) message;
			OpviewMessage opviewMessage = null;
			try {
				if(objectMessage.getObject() instanceof OpviewMessage){
					opviewMessage = (OpviewMessage) objectMessage.getObject();
					ExceptionManage.infor(opviewMessage.getMessageSource()+"++++"+opviewMessage.getObject(), this.getClass());
					if(opviewMessage.getObject() != null && opviewMessage.getMessageSource().equals("alarmNorth")){//告警分支
					}
				}
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}
	
}
