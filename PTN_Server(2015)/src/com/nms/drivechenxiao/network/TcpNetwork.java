package com.nms.drivechenxiao.network;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
/**基本tcp连接处理类
 * **/
public class TcpNetwork {

	private Socket clientSocket = null;
	private OutputStream outputStream = null;
	private InputStream inputStream = null;
	
	public boolean isLive(){
		try{
			clientSocket.sendUrgentData(0xFF);
            return true;
        }catch(Exception e){
            return false;
        }
	}

	public void connect(String ipAddres, int port) throws Exception {
		try {
			clientSocket = new Socket(ipAddres, port);
			outputStream = clientSocket.getOutputStream();
			inputStream = clientSocket.getInputStream();
		} catch (Exception e) {
			throw e;
		}
	}

	public Socket getClientSocket() {
		return clientSocket;
	}

	public OutputStream getOutputStream() {
		return outputStream;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void close() {
		try {
			if (clientSocket != null) {
				clientSocket.close();
			}
			if (outputStream != null) {
				outputStream.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
		} catch (Exception e) {
		}
	}

}
