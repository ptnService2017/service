package com.nms.snmp.ninteface.commandline;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class CommandLineRun implements Runnable{
	private Socket socket;
	private Boolean page = false;
	private Boolean hasLogin = false;
	
	public CommandLineRun(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		String clientIp = socket.getInetAddress().getHostAddress();
		InputStream socketInStream = null;
		BufferedReader br = null;
		OutputStream socketOutStream = null;
		try {
			socketInStream = socket.getInputStream();  
			br = new BufferedReader(new InputStreamReader(socketInStream, "UTF-8"));  
            //  
			socketOutStream = socket.getOutputStream();  
            String clientRequestString = null;
            CommandLineConfig commandLineConfig = new CommandLineConfig();
            while((clientRequestString = br.readLine()) != null){
            	String reslut = "no param\r\n>";
            	System.out.println("接收到客户端 " + clientIp + " 的信息:" + clientRequestString);
            	if(hasLogin){
            		if("setpage;1".equals(clientRequestString)){
                		page = true;
                		reslut ="success\r\n>";
                	}else if("setpage;0".equals(clientRequestString)){
                		page = false;
                		reslut ="success\r\n>";
                	}else if(clientRequestString.contains(";")){
                		reslut = commandLineConfig.parsingCommand(clientRequestString,page);
                	}else if("exit".equals(clientRequestString)){
                		socket.close();
                		break;
                	}
            	}else{
            		if(clientRequestString.contains("username") && clientRequestString.contains("password")){
            			String[] str = clientRequestString.split(";");
            			if(str.length != 2){
            				reslut ="param error \r\n>";
            			}else{
            				if(str[0].equals("username-admin") && str[1].equals("password-admin")){
            					hasLogin = true;
            					reslut ="success\r\n>";
            				}else{
            					reslut ="username or passwor error \r\n>";
            				}
            			}
            		}else{
            			reslut ="need login\r\n>";
            		}
            	}
            	
            	System.out.println("发送到客户端 " +reslut);
            	socketOutStream.write(reslut.getBytes("UTF-8"));
            }  
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				socketInStream.close();
				br.close();
				socketOutStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}

	public Boolean getPage() {
		return page;
	}

	public void setPage(Boolean page) {
		this.page = page;
	}

}
