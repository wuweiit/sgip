package org.marker.protocol.sgip.thread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import org.marker.protocol.sgip.Session;


/**
 * 本地监听线程（接收网关消息服务）
 * @author marker
 * */
public class ListenThread extends Thread {
	//坚挺状态
	private boolean bLoop  = true;
	//服务器套接字
	private ServerSocket socketListener;
	//网关会话对象
	private Session session;
	
	
	
	/** 构造方法 */
	public ListenThread(Session ssn) throws IOException {
		this.session = ssn;
		this.socketListener = new ServerSocket(session.getLocalPort());//启动服务套接字
	}

	
	
	public void run() { 
		try {
			while(bLoop){ 
				Socket socketinfo = socketListener.accept();//阻塞状态
				new Thread(new HandleThread(socketinfo, session)).start();
			}
		} catch (SocketException socketexception) {
			socketexception.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 本地监听器
	 * */
	public void stopMe() {
		bLoop = false;//设置监听状态
	}
	
	
}