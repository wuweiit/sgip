package org.marker.protocol.sgip.thread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import org.marker.protocol.sgip.Session;
import org.marker.protocol.sgip.msg.Bind;
import org.marker.protocol.sgip.msg.BindResp;
import org.marker.protocol.sgip.msg.Deliver;
import org.marker.protocol.sgip.msg.DeliverResp;
import org.marker.protocol.sgip.msg.Message;
import org.marker.protocol.sgip.msg.MsgHead;
import org.marker.protocol.sgip.msg.Report;
import org.marker.protocol.sgip.msg.ReportResp;
import org.marker.protocol.sgip.msg.Unbind;
import org.marker.protocol.sgip.msg.UnbindResp;



/**
 * 当本地服务接收到Socket时，就创建这个线程
 * @author marker
 * */
public class HandleThread extends Thread {

	private Socket socket;
	private Session session;
	public HandleThread(Socket socket, Session ssn) {
		this.socket = socket;
		this.session    = ssn;
	}
	
	public void run() {
		DataInputStream   in = null;
		DataOutputStream out = null;
		try {
			//ServerSocket 的绑定状态 
			in  = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			 
			boolean terminal = false;
			while(!terminal) {
				MsgHead head = new MsgHead();
				head.read(in);
				switch (head.getCommand()) {
				
					case Message.SGIP_CONNECT: 
						Bind bind = new Bind();
						bind.read(in);
						int result = 0;
						String localUser = session.getLocalUser();//本地网关用户名
						String localPass = session.getLocalPass();//本地网关密码
						if (localUser != null && localPass != null && (!bind.getName().equals(localUser) || !bind.getPassword().equals(localPass))){
							result = 1;//登录失败
						} 
						BindResp resp = new BindResp(head);
						resp.setResult(result);
						resp.write(out);
						break;

					case Message.SGIP_DELIVER:
						Deliver msg = new Deliver();
						msg.setHead(head);//设置消息头
						msg.read(in);
						(new DeliverResp(head)).write(out);
						session.onMessage(msg);
						break;

					case Message.SGIP_REPORT:
						Report rpt = new Report();
						rpt.read(in);
						(new ReportResp(head)).write(out);
						session.onReport(rpt);
						break;

					case Message.SGIP_TERMINATE:
						terminal = true;//放弃连接
						Unbind ub = new Unbind();
						ub.read(in); 
						UnbindResp unbindresp = new UnbindResp(head);
						unbindresp.write(out);
						session.onTerminate();
						break;
					default: continue;
				}
			}
			return;
		} catch (Exception e) {
			System.out.println("与SMG服务器链接异常");
		} finally {
			try {
				if (in  != null) in.close(); 
				if (out != null) out.close();
				if (socket != null)  socket.close(); 
			} catch (Exception exception2) {exception2.printStackTrace(); }
		}
	}
}