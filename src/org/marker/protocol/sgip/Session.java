package org.marker.protocol.sgip;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.marker.protocol.exception.BindException;
import org.marker.protocol.exception.ConnectionException;
import org.marker.protocol.sgip.conn.Connection;
import org.marker.protocol.sgip.msg.Bind;
import org.marker.protocol.sgip.msg.BindResp;
import org.marker.protocol.sgip.msg.Deliver;
import org.marker.protocol.sgip.msg.Message;
import org.marker.protocol.sgip.msg.Report;
import org.marker.protocol.sgip.msg.Submit;
import org.marker.protocol.sgip.msg.Unbind;
import org.marker.protocol.sgip.thread.ListenThread;



/**
 * 抽象网关会话类
 * @author marker
 * */
public abstract class Session {
	
	//网关连接对象
	private Connection conn;
	
	private boolean connected = false; //连接状态
	private boolean bound     = false; //bind状态 
	private ListenThread listenThread = null;//上行、状态报告监听器
	
	private long freeTime = 30000;//空闲时间
	private String localUser = null;//本地用户名
	private String localPass = null;//本地密码
    private int localPort    = 8801;//默认本地监听端口
	
    //绑定对象备份（当网关断开连接后，重新登录使用。）
    private Bind bind_bak;
    
    //通讯节点
    private long nodeId;
	

    
	/**
	 * 构造方法初始化连接对象
	 * @param conn 连接对象
	 */
	public Session(Connection conn) {
		this.conn = conn; 
	}

	
	
	
	protected void log(int level, String msg, Throwable t) {
		System.out.println((new SimpleDateFormat("hh-mm-ss")).format(new Date())
				+ "->" + msg);
		if (t != null)
			t.printStackTrace();
	}

	
	
	/**
	 * 登录到SMG服务器
	 * @param bind 绑定命令
	 * @return BindResp 绑定结果
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws ConnectionException 
	 * */
	public BindResp open(Bind bind) throws BindException, IOException {
		startTimer();
		if (bind_bak != null)
			closeNotException();
		if (bind_bak != bind)
			bind_bak = bind;
		if (listenThread == null) {
			listenThread = new ListenThread(this);
			listenThread.start();
		}
		try{
			conn.open();
			connected = true;
			conn.send(bind);
			BindResp resp = (BindResp) conn.recv();
			if (resp.getResult() == 0)
				bound = true;
			else
				throw new BindException("login fiald status:"+resp.getResult());
			return resp;
		}catch (BindException exception) {
			throw exception;
		}catch (Exception e) {
			try {
				Thread.sleep(5000);
				return open(bind);//重新打开
			} catch (InterruptedException e1) { }
		} 
		return null; 
	}
	
	 Timer timer ;
	/**
	 * 发送短信
	 * @param Submit 绑定命令
	 * @return BindResp 绑定结果
	 * */
	public Message sendSubmit(Submit msg) throws Exception {
		startTimer();
		
		
		
		
		
		if (!isConnected() || !isBound())
			open(bind_bak);
		try { 
			conn.send(msg); 
			return conn.recv();
		} catch (Exception e) {
			this.connected = false;
		}
		bound = false;
		Thread.sleep(1000L);//睡眠1秒重新发送
		return Session.this.sendSubmit(msg);
	}

	
	
	public void startTimer(){
		 if(timer != null){  timer.cancel();  }
		 timer = new Timer();
		 timer.schedule(new TimerTask() { 
			public void run() {
				 Session.this.closeNotException();
			}
		}, freeTime);
		
	}
	
	/**
	 * 无异常关闭
	 * */
	public void closeNotException() {
		try {
			close();
		} catch (Exception exception) { }finally{
			timer =  null;
		}
	}

	
	//抛出异常关闭
	public void close() throws ConnectionException {
		connected = false;
		bound     = false;
		try {
			conn.send(new Unbind());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (IOException e) { 
				e.printStackTrace();
			}
		}
	}
	
	
 
	
	
	
	
	public abstract void onMessage(Deliver deliver);
	public abstract void onReport(Report report);
	public abstract void onTerminate(); 
	
 
	
 

	public boolean isBound() {
		return bound;
	}

	public boolean isConnected() {
		return connected;
	}

 

	public String getLocalPass() {
		return localPass;
	}

	public void setLocalPass(String localPass) {
		this.localPass = localPass;
	}

	public int getLocalPort() {
		return localPort;
	}

	/**
	 * 本地监听短信
	 * @param localPort 端口
	 * */
	public void setLocalPort(int localPort) {
		this.localPort = localPort;
	}

	public String getLocalUser() {
		return localUser;
	}
	/**
	 * 网关登录名
	 * @param localUser 用户名
	 * */
	public void setLocalUser(String localUser) {
		this.localUser = localUser;
	}
	



	public long getFreeTime() {
		return freeTime;
	}




	public void setFreeTime(long freeTime) {
		this.freeTime = freeTime;
	}




	public long getNodeId() {
		return nodeId;
	}




	public void setNodeId(long nodeId) {
		this.nodeId = nodeId;
	}
 
	
	
}
