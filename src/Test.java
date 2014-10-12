import java.io.UnsupportedEncodingException;

import org.marker.protocol.sgip.Session;
import org.marker.protocol.sgip.conn.Connection;
import org.marker.protocol.sgip.msg.Bind;
import org.marker.protocol.sgip.msg.BindResp;
import org.marker.protocol.sgip.msg.Deliver;
import org.marker.protocol.sgip.msg.Report;
import org.marker.protocol.sgip.msg.Submit;
import org.marker.protocol.sgip.msg.SubmitResp;

/**
 * 
 * @author marker
 * 联系qq: 903595558
 * 本软件供大家免费使用哦，本人不提供网关申请代理。程序为SGIP1.2协议，不保证支持其他版本协议。
 * SGIP模拟器下载地址: http://simpleteam.com/sgip.php
 * 需要配置网关参数，有问题可以加我QQ质询或者发送邮件到我的QQ邮箱。
 * 希望大家支持我的博客: http://www.yl-blog.com
 * */

public class Test {
	 
	public static void testMT() throws Exception{
		Connection conn = new Connection("127.0.0.1", 8001);
	 
        Bind bind = new Bind(1,"1065502882007","1065502882007");
        System.out.println("logining...");
        //注意Session只能创建一个，如果要提高效率，可以参考我博客的并发库案例。
        Session session = new Session(conn) {
			
			@Override
			public void onReport(Report report) { 
				System.out.println("发送状态："+report.getResult()+" 序列号："+report.getSubmitSeq());
			        
			}
			
			@Override
			public void onMessage(Deliver deliver) {
				System.out.println("收到短信");
			}

			@Override
			public void onTerminate() {
				System.out.println("SMG主动断开哦");
			}
		};
		//本地用户帐号密码（网关访问本地参数）
        session.setLocalUser("1065502882007");
        session.setLocalPass("1065502882007");
        session.setLocalPort(8801);
        session.setFreeTime(30000);//设置空闲时间（重要更新，客户端主动断开链接依据）
        
        BindResp resp = session.open(bind);
       
        System.out.println("登录状态 " + resp.getResult());

        
    	String SPNumber     = "1065502882007" + "001";//之后为附加码
		String ChargeNumber = "1065502882007";
		String[] UserNumber = "13118135097".split(",");//拆分手机号码
		String CorpId       = "82007";
		String ServiceType  = "99882007";
		int FeeType         = 1;
		int FeeValue     	= 0;
		int GivenValue  	= 0;
		int AgentFlag       = 1;
		int MorelatetoMTFlag = 3;//
		int Priority = 0;
		String ExpireTime   = null;//短消息寿命的终止时间
		String ScheduleTime = null;//短消息定时发送的时间
		int ReportFlag = 1;//1模拟下行， 2模拟上行
		int TP_pid = 0;
		int TP_udhi = 0;
		int MessageCoding = 15;
		int MessageType = 0;
		byte[] MessageContent = null; 
		try {
			MessageContent = "我喜欢，我愿意。SGIP1.2协议包！".getBytes("GBK");
		} catch (UnsupportedEncodingException e1) { 
			e1.printStackTrace();
		}
		int MessageLen = MessageContent.length;
		String reserve = "0";
		//通讯节点
		long nodeid = (3000000000L + Long.parseLong("0270") * 100000L + Long.parseLong(CorpId));
		
		Submit s = new Submit(TP_pid, TP_udhi, SPNumber, ChargeNumber, UserNumber, CorpId,
				nodeid, ServiceType, FeeType, FeeValue, GivenValue, AgentFlag,
				MorelatetoMTFlag, Priority, ExpireTime, ScheduleTime,
				ReportFlag, MessageCoding, MessageType, MessageContent,
				MessageLen, reserve);
		
		SubmitResp sresp = (SubmitResp) session.sendSubmit(s);
        
        System.out.println("提交状态："+sresp.getResult()+" 序列号："+sresp.getSequence());
        
        
        
	}
	

	public static void testMO() throws Exception{
		Connection conn = new Connection("127.0.0.1", 8001);
	 
        Bind bind = new Bind(1,"1065502882007","1065502882007");
        System.out.print("logining...");
        Session session = new Session(conn) {
			
			@Override
			public void onReport(Report report) {
				System.out.println("状态报告来了");
				System.out.println("发送状态："+report.getResult()+" 序列号："+report.getSubmitSeq());
			        
			}
			
			@Override
			public void onMessage(Deliver deliver) {
				System.out.println("收到短信");
				try {
					System.out.println("手机："+deliver.getUserNumber()+" 短信内容："+new String(deliver.getContent(),"GBK")+" 标识"+deliver.getReserve());
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onTerminate() {
				System.out.println("断开哦");
			}
		};
		//本地用户帐号密码（网关访问本地参数）
        session.setLocalUser("1065502882007");
        session.setLocalPass("1065502882007");
        session.setLocalPort(8801);
        
        BindResp resp = session.open(bind);
       
        System.out.println("登录状态 " + resp.getResult());

        
    	String SPNumber     = "1065502882007" + "001";//之后为附加码
		String ChargeNumber = "1065502882007";
		String[] UserNumber = "13118135097".split(",");//拆分手机号码
		String CorpId       = "82007";
		String ServiceType  = "99882007";
		int FeeType         = 1;
		int FeeValue     	= 0;
		int GivenValue  	= 0;
		int AgentFlag       = 1;
		int MorelatetoMTFlag = 3;//
		int Priority = 0;
		String ExpireTime   = null;//短消息寿命的终止时间
		String ScheduleTime = null;//短消息定时发送的时间
		int ReportFlag = 2;//2 模拟上行，1下行有状态报告。（根据实际情况而定）
		int TP_pid = 0;
		int TP_udhi = 0;
		int MessageCoding = 15;
		int MessageType = 0;
		byte[] MessageContent = null;
		try {
			MessageContent = "我喜欢，我愿意。SGIP1.2协议包！".getBytes("GBK");
		} catch (UnsupportedEncodingException e1) { 
			e1.printStackTrace();
		}
		int MessageLen = MessageContent.length;
		String reserve = " ";
		//通讯节点
		long nodeid = (3000000000L + Long.parseLong("0270") * 100000L + Long.parseLong(CorpId));
		
		Submit s = new Submit(TP_pid, TP_udhi, SPNumber, ChargeNumber, UserNumber, CorpId,
				nodeid, ServiceType, FeeType, FeeValue, GivenValue, AgentFlag,
				MorelatetoMTFlag, Priority, ExpireTime, ScheduleTime,
				ReportFlag, MessageCoding, MessageType, MessageContent,
				MessageLen, reserve);
		
		SubmitResp sresp = (SubmitResp) session.sendSubmit(s);
        
        System.out.println("提交状态："+sresp.getResult()+" 序列号："+sresp.getSequence());
        
        session.closeNotException();
        
	}
	
	public static void main(String[] args) throws Exception {
		//测试下行和状态报告
		testMT();
		
		
		//测试上行
//		testMO();
	}
	
	
	
	
}
