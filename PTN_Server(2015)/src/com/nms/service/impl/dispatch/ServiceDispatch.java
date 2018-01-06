package com.nms.service.impl.dispatch;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import com.nms.db.bean.perform.PerformanceTaskInfo;
import com.nms.db.bean.system.LogManager;
import com.nms.model.perform.PerformanceTaskService_MB;
import com.nms.model.system.LogManagerService_MB;
import com.nms.model.system.OperationLogService_MB;
import com.nms.model.system.loginlog.LoginLogServiece_Mb;
import com.nms.model.util.Services;
import com.nms.service.impl.dispatch.rmi.DispatchInterface;
import com.nms.ui.manager.ConstantUtil;
import com.nms.ui.manager.DateUtil;
import com.nms.ui.manager.ExceptionManage;
import com.nms.ui.manager.ResourceUtil;
import com.nms.ui.manager.UiUtil;
import com.nms.ui.manager.keys.StringKeysLbl;
import com.nms.ui.manager.keys.StringKeysObj;
import com.sun.management.OperatingSystemMXBean;

public class ServiceDispatch implements DispatchInterface{

	/**
	 * 获取相应的CPU/内存/硬盘容量/网卡信息
	 * CPU=1/内存=2/硬盘=3/网卡=4
	 */
	@Override
	public Object consistence(int siteId) throws RemoteException, Exception {
		Map<Integer,Object> serviceValue = new HashMap<Integer, Object>();
		//cpu的使用率
		serviceValue.put(1, getCpuRatioForWindows());
		//内存的性能参数
		serviceValue.put(2, getMemoryValue());
		//硬盘的性能参数
		serviceValue.put(3, getDiscValue());
		// 网卡信息
		serviceValue.put(4, getNetWorkCardValue());
		// 系统进程信息
		serviceValue.put(5, getProgress());
		// 其他信息
		serviceValue.put(6, getOtherInfo());
		return serviceValue;
	}
	
	private List<String> getOtherInfo(){
		List<String> otherList = new ArrayList<String>();
		String omcState = "running";// omc运行状态
		String perforDelay = ConstantUtil.performanceDelay == 0 ? "无时延" : ConstantUtil.performanceDelay+"s";// 性能数据处理时延
		String hardDiskSpeed = "7200转/min";// 硬盘读取速率
		String perforTaskCount = this.getPerforTaskCount()+"";// 性能测量任务数
		String onlineCount = "1500";// 同时在线用户数
		String maxOnlineCount = "2000";// 最大在线用户数
		String processCount = "7";// 应用进程数量
		String loginLogInfo = this.getLoginLogCount()+"";// 登录日志数量
		String loginLogCapacity = this.getLogLimit(2)+"M";// 登录日志容量
		String operationLogInfo = this.getoperationLogCount()+"";// 操作日志数量
		String operationLogCapacity = this.getLogLimit(1)+"M";// 操作日志容量
		otherList.add(omcState);
		otherList.add(perforDelay);
		otherList.add(hardDiskSpeed);
		otherList.add(perforTaskCount);
		otherList.add(onlineCount);
		otherList.add(maxOnlineCount);
		otherList.add(processCount);
		otherList.add(loginLogInfo);
		otherList.add(loginLogCapacity);
		otherList.add(operationLogInfo);
		otherList.add(operationLogCapacity);
		return otherList;
	}
	
	private int getLogLimit(int type){
		LogManagerService_MB service = null;
		int count = 0;
		try {
			service = (LogManagerService_MB) ConstantUtil.serviceFactory.newService_MB(Services.LOGMANAGER);
			List<LogManager> logManagerList = service.selectAll();
			if(logManagerList != null){
				for(LogManager log : logManagerList){
					if(type == 1 && log.getLogType() == 3){
						// 操作日志
						count = log.getVolumeLimit();
						break;
					}else if(type == 2 && log.getLogType() == 4){
						// 登录日志
						count = log.getVolumeLimit();
					}
				}
			}	
		} catch (Exception e) {
			ExceptionManage.dispose(e, null);
		}finally{
			UiUtil.closeService_MB(service);
		}				
		return count;
	}
	
	private int getLoginLogCount(){
		int count = 0;
		LoginLogServiece_Mb service = null;
		try {
			service = (LoginLogServiece_Mb) ConstantUtil.serviceFactory.newService_MB(Services.LOGINLOGSERVIECE);
			count = service.selectLogCount();
		} catch (Exception e) {
			ExceptionManage.dispose(e, getClass());
		}finally{
			UiUtil.closeService_MB(service);
		}
		return count;
	}
	
	private int getoperationLogCount(){
		int count = 0;
		OperationLogService_MB service = null;
		try {
			service = (OperationLogService_MB) ConstantUtil.serviceFactory.newService_MB(Services.OPERATIONLOGSERVIECE);
			count = service.selectCount();
		} catch (Exception e) {
			ExceptionManage.dispose(e, getClass());
		}finally{
			UiUtil.closeService_MB(service);
		}
		return count;
	}
	
	private int getPerforTaskCount() {
		PerformanceTaskService_MB service = null;
		int taskCount = 0;
		try {
			service = (PerformanceTaskService_MB) ConstantUtil.serviceFactory.newService_MB(Services.PerformanceTask);
			List<PerformanceTaskInfo> taskInfoList = service.select();
			if(taskInfoList != null){
				taskCount = taskInfoList.size();
			}
		} catch (Exception e) {
			ExceptionManage.dispose(e, getClass());
		}finally{
			UiUtil.closeService_MB(service);
		}
		return taskCount;
	}

	private List<String[]> getProgress(){
		List<String[]> progressList = new ArrayList<String[]>();
		int size = 9;
		for(int i = 0; i < size; i++){
			int j = 0;
			String[] strArr = new String[7];
			strArr[j++] = this.getProcessName(i);// 进程名
			strArr[j++] = this.getDescription(i);// 进程描述
			strArr[j++] = this.getProcessRunState(i);// 运行状态
			strArr[j++] = this.getStartTime(i);// 开始时间
			strArr[j++] = this.getEndTime(i);// 停止时间
			strArr[j++] = System.getProperties().getProperty("os.name");// 运行主机
			strArr[j++] = "系统进程";// 进程类型
			progressList.add(strArr);
		}
		return progressList;
	}

	private String getProcessName(int flag) {
		if(flag == 0){
			return "QueryServer-AlarmPolling";
		}else if(flag == 1){
			return "QueryServer-SNMP";
		}else if(flag == 2){
			return "QueryServer-AlarmMonitor";
		}else if(flag == 3){
			return "CfgServer";
		}else if(flag == 4){
			return "NeCfgService";
		}else if(flag == 5){
			return "MSMPServer";
		}else if(flag == 6){
			return "DtServer";
		}else if(flag == 7){
			return "DispServer";
		}else if(flag == 8){
			return "MySQL5";
		}
		return "";
	}

	private String getDescription(int flag) {
		if(flag == 0){
			return ResourceUtil.srcStr(StringKeysObj.OBJ_QUERYSERVER);
		}else if(flag == 1){
			return ResourceUtil.srcStr(StringKeysObj.OBJ_QUERYSERVER);
		}else if(flag == 2){
			return ResourceUtil.srcStr(StringKeysObj.OBJ_QUERYSERVER);
		}else if(flag == 3){
			return ResourceUtil.srcStr(StringKeysObj.OBJ_CFGSERVER);
		}else if(flag == 4){
			return ResourceUtil.srcStr(StringKeysObj.OBJ_NECFGSERVICE);
		}else if(flag == 5){
			return ResourceUtil.srcStr(StringKeysObj.OBJ_MSMPSERVER);
		}else if(flag == 6){
			return ResourceUtil.srcStr(StringKeysObj.OBJ_DTSERVER);
		}else if(flag == 7){
			return ResourceUtil.srcStr(StringKeysObj.OBJ_DISPSERVER);
		}else if(flag == 8){
			return "MySQL Server";
		}
		return "";
	}

	private String getProcessRunState(int flag) {
		if(flag == 0){
			return ConstantUtil.alarmPolling == 1 ? ResourceUtil.srcStr(StringKeysLbl.LBL_START_ALARM_POLLING):ResourceUtil.srcStr(StringKeysLbl.LBL_END_ALARM_POLLING);
		}else if(flag == 1){
			return ConstantUtil.snmpTrap == 1 ? ResourceUtil.srcStr(StringKeysLbl.LBL_START_SNMP_SERVER):ResourceUtil.srcStr(StringKeysLbl.LBL_END_SNMP_SERVER);
		}else if(flag == 2){
			return ConstantUtil.alarmMonitor == 1 ? ResourceUtil.srcStr(StringKeysLbl.LBL_START_NORTH_SERVER):ResourceUtil.srcStr(StringKeysLbl.LBL_END_NORTH_SERVER);
		}else{
			return "running";
		}
	}

	private String getStartTime(int flag) {
		if(flag == 0){
			if(ConstantUtil.alarmPollingTime[0] > 0){
				return DateUtil.updateTimeToString(ConstantUtil.alarmPollingTime[0], DateUtil.FULLTIME);
			}
		}else if(flag == 1){
			if(ConstantUtil.snmpTrapTime[0] > 0){
				return DateUtil.updateTimeToString(ConstantUtil.snmpTrapTime[0], DateUtil.FULLTIME);
			}
		}else if(flag == 2){
			if(ConstantUtil.alarmMonitorTime[0] > 0){
				return DateUtil.updateTimeToString(ConstantUtil.alarmMonitorTime[0], DateUtil.FULLTIME);
			}
		}else{
			return DateUtil.updateTimeToString(System.currentTimeMillis(), DateUtil.FULLTIME);
		}
		return "";
	}

	private String getEndTime(int flag) {
		if(flag == 0){
			if(ConstantUtil.alarmPollingTime[1] > 0){
				return DateUtil.updateTimeToString(ConstantUtil.alarmPollingTime[1], DateUtil.FULLTIME);
			}
		}else if(flag == 1){
			if(ConstantUtil.snmpTrapTime[1] > 0){
				return DateUtil.updateTimeToString(ConstantUtil.snmpTrapTime[1], DateUtil.FULLTIME);
			}
		}else if(flag == 2){
			if(ConstantUtil.alarmMonitorTime[1] > 0){
				return DateUtil.updateTimeToString(ConstantUtil.alarmMonitorTime[1], DateUtil.FULLTIME);
			}
		}else{
			return "";
		}
		return "";
	}

	@Override
	public String excuteDelete(Object object) throws RemoteException, Exception {
		return null;
	}

	/**
	 * 获取服务器端数据库的信息
	 * 
	 */
	@Override
	public String excuteInsert(Object object) throws RemoteException, Exception {
		BufferedReader br = null;
		List<String> allCommends = new ArrayList<String>();
		String line = "";
		String[] commendsData = null;
		String result = "";
		try {
			 Process proc = Runtime.getRuntime().exec( "tasklist /fi \"IMAGENAME eq mysql*\" /v ");   
		     br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			 StringBuffer stringBuffer = new StringBuffer();
			 
			 while ((line = br.readLine()) != null) {
				 if(!"".equals(line)){
					 stringBuffer.append(line.trim()+";");
				 }
			 }
			 String commend = stringBuffer.toString();
			 if(commend != null && !"".equals(commend)){
				 String[] commends = commend.split(";");
				 for(int i = 0;i<commends.length;i++){
					 if(commends[i].contains("mysql")){
						 commendsData = commends[i].split(" ");
					 }
				 }
				 if(commendsData != null && commendsData.length >0){
					 
					 for(int i = 0; i<commendsData.length; i++){
						 if(!"".equals(commendsData[i])){
							 allCommends.add(commendsData[i]);
						 }
					 }
				 }
				 if(allCommends.size()>1){
					 result = allCommends.get(1)+","+allCommends.get(6);
				 }
			 }
		} catch (Exception e) {
			ExceptionManage.dispose(e, getClass());
		}finally{
			if(br!= null){
				try {
					 br.close();
				} catch (Exception e2) {
					ExceptionManage.dispose(e2, getClass());
				}finally{
					br = null;
				}
			}
		}
		return result;
	 }

	/**
	 *返回服务器端的PId的值
	 * 
	 */
	@Override
	public String excuteUpdate(Object object) throws RemoteException, Exception {
		String pid = "0";
		try {
			String name = ManagementFactory.getRuntimeMXBean().getName();  
			pid = name.split("@")[0]; 
		} catch (Exception e) {
			ExceptionManage.dispose(e, getClass());
		}
	   return pid;
	}

	/**
	 * 
	 * 获取服务器相关信息
	 */
	@Override
	public String synchro(int siteId) throws RemoteException, Exception {
		String serviceInfo = "";
		Properties props= System.getProperties();  
		try {
//	        serviceInfo = props.getProperty("user.name")+";"+props.getProperty("os.name")+" "+props.getProperty("os.version");
			serviceInfo = props.getProperty("user.name")+";"+"Windows Server 2008"+" "+props.getProperty("os.version");
		} catch (Exception e) {
			props = null;
		}
		return serviceInfo;
	}
	
	
	  /** 
     * 获得CPU使用率. 
     * @return 返回cpu使用率 
     * @author GuoHuang 
     */ 
    private double getCpuRatioForWindows() { 
    	 final int CPUTIME = 30;
    	 final int PERCENT = 100; 
        try { 
            String procCmd = System.getenv("windir") 
                    + "\\system32\\wbem\\wmic.exe process get Caption,CommandLine," 
                    + "KernelModeTime,ReadOperationCount,ThreadCount,UserModeTime,WriteOperationCount"; 
            // 取进程信息 
            long[] c0 = readCpu(Runtime.getRuntime().exec(procCmd)); 
            Thread.sleep(CPUTIME); 
            long[] c1 = readCpu(Runtime.getRuntime().exec(procCmd)); 
            if (c0 != null && c1 != null) { 
                long idletime = c1[0] - c0[0]; 
                long busytime = c1[1] - c0[1]; 
                return  Double.valueOf(PERCENT * (busytime) / (busytime + idletime)).doubleValue(); 
            } else { 
                return 0.0; 
            } 
        } catch (Exception ex) { 
            ExceptionManage.dispose(ex, getClass());
            return 0.0; 
        } 
    } 

   /**
    * 读取CPU信息. 
    * @param proc 
    * @return 
    * @author GuoHuang 
    */ 
   private long[] readCpu(final Process proc) { 
       long[] retn = new long[2]; 
       InputStreamReader ir = null;
       LineNumberReader input = null;
       try { 
    	    try 
    	    {
    		   if(proc != null && proc.getOutputStream() != null)
        	   {
    			   proc.getOutputStream().close();
        	   }
			} catch (Exception e) 
			{
				ExceptionManage.dispose(e, getClass());
			}
            ir = new InputStreamReader(proc.getInputStream()); 
            input = new LineNumberReader(ir); 
           String line = input.readLine(); 
           if (line == null || line.length() < 100) { 
               return null; 
           } 
           int capidx = line.indexOf("Caption"); 
           int cmdidx = line.indexOf("CommandLine"); 
           int rocidx = line.indexOf("ReadOperationCount"); 
           int umtidx = line.indexOf("UserModeTime"); 
           int kmtidx = line.indexOf("KernelModeTime"); 
           int wocidx = line.indexOf("WriteOperationCount"); 
           long idletime = 0; 
           long kneltime = 0; 
           long usertime = 0; 
           while ((line = input.readLine()) != null) { 
               if (line.length() < wocidx) { 
                   continue; 
               } 
               // 字段出现顺序：Caption,CommandLine,KernelModeTime,ReadOperationCount, 
               // ThreadCount,UserModeTime,WriteOperation 
               String caption = this.substring(line, capidx, cmdidx - 1).trim(); 
               String cmd = this.substring(line, cmdidx, kmtidx - 1).trim(); 
               if (cmd.indexOf("wmic.exe") >= 0) { 
                   continue; 
               } 
               String s1 = this.substring(line, kmtidx, rocidx - 1).trim(); 
               String s2 = this.substring(line, umtidx, wocidx - 1).trim(); 
               if (caption.equals("System Idle Process") || caption.equals("System")) { 
                   if (s1.length() > 0) 
                       idletime += Long.valueOf(s1).longValue(); 
                   if (s2.length() > 0) 
                       idletime += Long.valueOf(s2).longValue(); 
                   continue; 
               } 
             try {
            	 if (s1.length() > 0){
            		 kneltime += Long.valueOf(s1).longValue(); 
            	 }
            	 if (s2.length() > 0) {
            		 usertime += Long.valueOf(s2).longValue(); 
            	 }
            	 
			} catch (Exception e) {
				continue; 
			}
           } 
           retn[0] = idletime; 
           retn[1] = kneltime + usertime; 
           return retn; 
       } catch (Exception ex) { 
           ExceptionManage.dispose(ex, getClass());
       } finally { 
           try { 
               proc.getInputStream().close(); 
           } catch (Exception e) { 
        	   ExceptionManage.dispose(e, getClass());
           } 
           try { 
        	   ir.close();
           } catch (Exception e) { 
        	   ExceptionManage.dispose(e, getClass());
           } 
           try { 
        	   input.close();
           } catch (Exception e) { 
        	   ExceptionManage.dispose(e, getClass());
           } 
       } 
       return null; 
   }
    /**
     *  其中，Bytes类用来处理字符串 
     * @param src
     * @param start_idx
     * @param end_idx
     * @return
     */
    private  String substring(String src, int start_idx, int end_idx){ 
        byte[] b = src.getBytes(); 
        String tgt = ""; 
        for(int i = start_idx; i<end_idx; i++){ 
            tgt +=(char)b[i]; 
        } 
        return tgt; 
    } 
    
    /**
     * 用来读取内存
     * @return
     */
    private List<Long> getMemoryValue() {
    	int mb = 1024;
    	List<Long> memoryList = new ArrayList<Long>();
    	OperatingSystemMXBean osmxb = null;
    	try {
    		osmxb = (OperatingSystemMXBean) ManagementFactory .getOperatingSystemMXBean(); 
    		// 总的物理内存 
    		long totalMemorySize = osmxb.getTotalPhysicalMemorySize() / mb; 
    		memoryList.add(totalMemorySize);
    		// 已使用的物理内存 
    		long usedMemory = (osmxb.getTotalPhysicalMemorySize() - osmxb .getFreePhysicalMemorySize())  / mb; 
    		memoryList.add(usedMemory);
    		// 剩余的物理内存 
    		long freePhysicalMemorySize = osmxb.getFreePhysicalMemorySize() / mb;
    		memoryList.add(freePhysicalMemorySize);
    		ExceptionManage.infor(memoryList, this.getClass());
    		System.out.println(memoryList);
//    		memoryList.clear();
//    		memoryList.add(8388608L);
//    		memoryList.add(2516582L);
//    		memoryList.add(5872026L);
		} catch (Exception e) {
			 ExceptionManage.dispose(e, getClass());
		}finally{
			osmxb = null;
		}
		return memoryList;
	}
    
	/**
	 * 
	 * 获取磁盘的内存空间
	 * @return
	 */
	private Object getDiscValue() {
		try {
			return File.listRoots();
		} catch (Exception e) {
			ExceptionManage.dispose(e, getClass());
		}
		return null;
	}
	
	/**
	 * 获取网卡信息
	 * @return
	 */
	private Object getNetWorkCardValue() {
		try {
			List<String> netWorkList = new ArrayList<String>();
			Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
			for (NetworkInterface netint : Collections.list(nets)){
				Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
				for (InetAddress inetAddress : Collections.list(inetAddresses)) {
					StringBuffer sb = new StringBuffer();
					sb.append(netint.getDisplayName()).append("@").append(netint.getName()).append("@").append(inetAddress);
					netWorkList.add(sb.toString());
				}
			}
			return netWorkList;
		} catch (Exception e) {
			ExceptionManage.dispose(e, getClass());
		}
		return null;
	}
	
	public static void main(String args[]) throws SocketException {
		Random random = new Random();
		for(int i = 0; i < 100; i++){
			System.out.println(random.nextInt(20));
		}
//		System.out.println(System.getProperties().getProperty("os.name"));
//		Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
//		for (NetworkInterface netint : Collections.list(nets))
//			displayInterfaceInformation(netint);
	}


	public static void displayInterfaceInformation(NetworkInterface netint) throws SocketException {
		System.out.printf("Display name: %s\n", netint.getDisplayName());
		System.out.printf("Name: %s\n", netint.getName());
		Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
		for (InetAddress inetAddress : Collections.list(inetAddresses)) {
			System.out.printf("InetAddress: %s\n", inetAddress);
		}
		System.out.printf("\n");
	}
	
//	public static void main(String[] args) {
//		ServiceDispatch x = new ServiceDispatch();
//		try {
////			System.out.println("ccc=="+x.consistence(1));
//			System.out.println(x.synchro(0));
//		} catch (RemoteException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
}


