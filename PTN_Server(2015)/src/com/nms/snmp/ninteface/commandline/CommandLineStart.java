package com.nms.snmp.ninteface.commandline;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.nms.model.util.ServiceFactory;
import com.nms.snmp.ninteface.framework.SnmpConfig;
import com.nms.snmp.ninteface.util.NorthConfig;
import com.nms.ui.manager.ConstantUtil;
import com.nms.util.Mybatis_DBManager;

/**
 * 北向操作指令telnet监听
 * @author peng
 *
 */
public class CommandLineStart extends Thread{
	private ServerSocket serverSocket;
	private ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(150);
	
	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(NorthConfig.northTelnetPort);
			while(true){
				Socket socket = serverSocket.accept();
				CommandLineRun commandLineRun = new CommandLineRun(socket);
				threadPoolExecutor.execute(commandLineRun);
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		 Mybatis_DBManager.init("127.0.0.1");
		 ConstantUtil.serviceFactory = new ServiceFactory();
		 SnmpConfig.getInstanse().init();
		CommandLineStart commandLineStart = new CommandLineStart();
		new Thread(commandLineStart).start();
	}
}
