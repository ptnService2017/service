package com.nms.snmp.ninteface.util;

import com.nms.snmp.ninteface.impl.alarm.AlarmTcpTracpMainThread;

public class NorthConfig {
	public static String northServiceIp="127.0.0.1";//网管服务器IP
	public static Integer northAlarmPort=31232;//北向告警socket端口号
	public static Integer northTelnetPort=31241;//北向tenlet socket端口号
	public static AlarmTcpTracpMainThread alarmTcpTracpMainThread;
	
}
