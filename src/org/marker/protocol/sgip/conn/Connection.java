package org.marker.protocol.sgip.conn;

import java.io.IOException;
import java.net.Socket;

import org.marker.protocol.exception.ConnectionException;
import org.marker.protocol.sgip.msg.Bind;
import org.marker.protocol.sgip.msg.BindResp;
import org.marker.protocol.sgip.msg.Deliver;
import org.marker.protocol.sgip.msg.DeliverResp;
import org.marker.protocol.sgip.msg.Message;
import org.marker.protocol.sgip.msg.MsgHead;
import org.marker.protocol.sgip.msg.Report;
import org.marker.protocol.sgip.msg.ReportResp;
import org.marker.protocol.sgip.msg.Submit;
import org.marker.protocol.sgip.msg.SubmitResp;
import org.marker.protocol.sgip.msg.Unbind;
import org.marker.protocol.sgip.msg.UnbindResp;

/**
 * 链接到SMG对象
 * @author marker
 * */
public class Connection {

	//超时时间
	private int connectionTimeout;
	//缓冲大小
	private int ioBufferSize;
	//套接字
	protected Socket socket;
	
	protected SGIP_OutputStream out;//输出流
	protected SGIP_InputStream  in; //输入流
	
	//网关ip地址
	private String host;
	//网关端口
	private int port;

	
	
	/**
	 * @param host SMG的IP地址
	 * @param port SMG的端口
	 * */
	public Connection(String host, int port) {
		this.connectionTimeout = 30000;
		this.ioBufferSize = 2048;
		this.host = host;
		this.port = port;
	}
	
	
	/**
	 * @param host SMG的IP地址
	 * @param port SMG的端口
	 * @param connectionTimeout 超时时间
	 * @param ioBufferSize 缓冲大小
	 * */
	public Connection(String host, int port, int connectionTimeout, int ioBufferSize) {
		this.connectionTimeout = connectionTimeout;
		this.ioBufferSize = ioBufferSize;
		this.host = host;
		this.port = port;
	}


	
	/**
	 * 打开链接
	 * */
	public synchronized void open() throws IOException {
		this.closeNotException();// 关闭并清空socket链接
		socket = new Socket(host, port);
		socket.setSoTimeout(2000);// 设置连接超时时间
		out = new SGIP_OutputStream(socket.getOutputStream(), ioBufferSize);
		in = new SGIP_InputStream(socket.getInputStream(), ioBufferSize);

	}

	
	
	/**
	 * 发送命令 
	 * @param Message 消息
	 * @throws ConnectionException 
	 * @throws ConnectionException 连接异常
	 * @throws IOException 
	 * */
	public synchronized void send(Message msg) throws ConnectionException, IOException {
		if (socket == null || out == null || in == null) {
			throw new ConnectionException("connection is invalid!");
		} else {
			msg.write(out);
			out.flush();//刷新缓冲输出流
		}
	}

	
	
	/**
	 * 获取结果
	 * @return Message 消息
	 * */
	public synchronized Message recv() throws IOException {
		MsgHead head = new MsgHead();
		head.read(in);//读取消息头
		Message msg = null;
		switch (head.getCommand()) {
		case -2147483647://Bind响应命令
			msg = new BindResp();
			msg.read(in);
			break; 
		case -2147483644:
			msg = new DeliverResp();
			msg.read(in);
			break; 
		case -2147483643:
			msg = new ReportResp();
			msg.read(in);
			break; 
		case -2147483645:
			msg = new SubmitResp();
			msg.read(in);
			break; 
		case -2147483646:
			msg = new UnbindResp();
			msg.read(in);
			break;
		}
		if (msg != null) msg.setHead(head);
		return msg; 
	}
	
	

	//无异常关闭连接
	public void closeNotException() {
		try {
			this.close();
		} catch (IOException exception) { }
	}

	
	//关闭连接（可能抛出异常）
	public void close() throws IOException {
		try {
			if (in     != null) in.close();//关闭输入流
			if (out    != null) out.close();//关闭输出流
			if (socket != null) { socket.close();}//关闭套接字
		} finally {
			socket = null;
		}
	}
	
	
	
	
	
	public int getIoBufferSize() {
		return ioBufferSize;
	}

	public void setIoBufferSize(int ioBufferSize) {
		this.ioBufferSize = ioBufferSize;
	}

	public boolean checkLink() {
		return !socket.isConnected() && !socket.isClosed();
	}

	//获取活动时间
	public long getLastActive(boolean isSend) {
		return isSend ? out.getLastAccess() : in.getLastAccess();
	}
}
