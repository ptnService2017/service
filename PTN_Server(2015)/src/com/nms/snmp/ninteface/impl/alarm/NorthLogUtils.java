package com.nms.snmp.ninteface.impl.alarm;

import org.apache.log4j.Logger;

public class NorthLogUtils {
	private static Logger northClientLogger = Logger.getLogger("northClientAdpter");//客户端登陆日志
	private static Logger alarmrealtimeLogger = Logger.getLogger("alarmrealtimeLogger");//告警实时日志
	private static Logger alarmrunLogger = Logger.getLogger("alarmrunLogger");//告警流水日志
	
	public static void northClientLog(String log){
		northClientLogger.info(log);
	}
	
	public static void alarmrealtimeLog(String log){
		alarmrealtimeLogger.info(log);
	}
	
	public static void alarmrunLog(String log){
		alarmrunLogger.info(log);
	}
}
