package org.marker.protocol.sgip.msg;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.marker.protocol.tools.Sequence;
import org.marker.protocol.tools.Tools;
import org.marker.protocol.tools.Utils;


public class Submit extends Message {

	private static DateFormat fmt = new SimpleDateFormat("yyMMddHHmmss032+");
 
//	private static final DateFormat seqFmt = new SimpleDateFormat("yyyyMMddhhmmssSSS");
	
	
	private String SPNumber;//SP的接入号码
	private String ChargeNumber;//付费号码
	private String UserNumber[];//接收该短消息的手机号
	private String CorpId;//企业代码，取值范围0-99999
	private String ServiceType;//业务代码，由SP定义
	private int FeeType;//计费类型
	private int FeeValue;//取值范围0-99999，该条短消息的收费值，单位为分，由SP定义
	private int GivenValue;//取值范围0-99999，赠送用户的话费，单位为分，由SP定义，特指由SP向用户发送广告时的赠送话费
	private int AgentFlag;//代收费标志，0：应收；1：实收
	private int MorelatetoMTFlag;//引起MT消息的原因
	private int Priority;//
	private String ExpireTime;//短消息寿命的终止时间
	private String ScheduleTime;//短消息定时发送的时间
	private int ReportFlag;//状态报告标记: 1-该条消息无论最后是否成功都要返回状态报告
	private int MessageType;//信息类型
	private int TP_pid;
	private int TP_udhi;
	private int MessageCoding;//短消息的编码格式
	private byte[] MessageContent;//短消息的内容
	private String Reserve;

	private long NodeId;
	
	
	
	public long getNodeId() {
		return NodeId;
	}



	public void setNodeId(long nodeId) {
		this.NodeId = nodeId;
		super.getHead().setSequence1(this.NodeId);//设置通信节点命令源
	}



	private int MessageContentLength ;
	
 
	public Submit(){
		super(Message.SGIP_SUBMIT);
	}
	
	
	
	
	public Submit(
			int TP_pid,
			int TP_udhi,
			String SPNumber,
			String ChargeNumber,
			String[] UserNumber,
			String CorpId,
			long NodeId,
			String ServiceType,
			int FeeType,
			int FeeValue,
			int GivenValue,
			int AgentFlag,
			int MorelatetoMTFlag,
			int Priority,
			String ExpireTime,
			String ScheduleTime,
			int ReportFlag,
			int MessageCoding,
			int MessageType,
	       byte[] MessageContent,
	       int MessageContentLength,
	       String Reserve) {
		super(Message.SGIP_SUBMIT);
		this.TP_pid = TP_pid;
		this.TP_udhi = TP_udhi;
		this.SPNumber =SPNumber;
		this.ChargeNumber = ChargeNumber;
		this.UserNumber = UserNumber;
		this.CorpId = CorpId;
		this.ServiceType = ServiceType;
		this.FeeType = FeeType;
		this.FeeValue =FeeValue;
		this.GivenValue = GivenValue;
		this.AgentFlag = AgentFlag;
		this.MorelatetoMTFlag = MorelatetoMTFlag;
		this.Priority = Priority;
		this.ExpireTime = ExpireTime;
		this.ReportFlag = ReportFlag;
		this.MessageType = MessageType;
		this.MessageCoding = MessageCoding;
		this.MessageContent = MessageContent;
		this.MessageContentLength = MessageContentLength;
		this.Reserve = Reserve;
		this.NodeId = NodeId;
		super.getHead().setSequence1(NodeId);//设置通信节点命令源
		super.getHead().setSequence2(Tools.getCurrentSequenceDate());
		super.getHead().setSequence3(Sequence.next());
	}
	
	
	
	
	
	

	public void write(OutputStream out) throws IOException {
		getHead().setLength(143 + 21 * UserNumber.length + this.MessageContentLength);
		getHead().write(out);
		byte bs[] = new byte[21];
		System.arraycopy(SPNumber.getBytes(), 0, bs, 0, SPNumber.getBytes().length);
		out.write(bs);
		bs = new byte[21];
		System.arraycopy(ChargeNumber.getBytes(), 0, bs, 0,
				ChargeNumber.getBytes().length);
		out.write(bs);
		//构造手机号码
		out.write(UserNumber.length);
		for (int i = 0; i < UserNumber.length; i++) {
			bs = new byte[21];
			System.arraycopy(UserNumber[i].getBytes(), 0, bs, 0,
					UserNumber[i].getBytes().length);
			out.write(bs); 
		}
		
		bs = new byte[5];
		System.arraycopy(CorpId.getBytes(), 0, bs, 0, CorpId.getBytes().length);
		out.write(bs);
		bs = new byte[10];
		System.arraycopy(ServiceType.getBytes(), 0, bs, 0,
				ServiceType.getBytes().length);
		out.write(bs);
		out.write(FeeType);
		bs = new byte[6];
		String sValue = String.valueOf(FeeValue);
		System.arraycopy(sValue.getBytes(), 0, bs, 0, sValue.getBytes().length);
		out.write(bs);
		bs = new byte[6];
		sValue = String.valueOf(GivenValue);
		System.arraycopy(sValue.getBytes(), 0, bs, 0, sValue.getBytes().length);
		out.write(bs);
		out.write(AgentFlag);
		out.write(MorelatetoMTFlag);
		out.write(Priority);
		bs = new byte[16];
		sValue = ExpireTime != null ? fmt.format(ExpireTime) :"";
		System.arraycopy(sValue.getBytes(), 0, bs, 0, sValue.getBytes().length);
		out.write(bs);
		bs = new byte[16];
		sValue = ScheduleTime != null ? fmt.format(ScheduleTime) : "";
		System.arraycopy(sValue.getBytes(), 0, bs, 0, sValue.getBytes().length);
		out.write(bs);
		out.write(ReportFlag);
		out.write(TP_pid);
		out.write(TP_udhi);
		out.write(MessageCoding);
		out.write(MessageType);
		out.write(Utils.IntToBytes4(MessageContentLength));
		
		
		out.write(MessageContent);
		bs = new byte[8];
		if (Reserve != null)
			System.arraycopy(Reserve.getBytes(), 0, bs, 0,
					Reserve.getBytes().length);
		out.write(bs);
		out.flush();
	}

	public void read(InputStream inputstream) throws IOException {
	}

 

	public int getAgentFlag() {
		return AgentFlag;
	}



	public void setAgentFlag(int agentFlag) {
		AgentFlag = agentFlag;
	}



 

//	public void setContent(byte content[]) {
//		messageLen = Math.min(140, content.length);
//		this.content = new byte[content.length];
//		System.arraycopy(content, 0, this.content, 0, messageLen);
//	}

//	private byte[] dealContent(String content) {
//		int cl = content.getBytes().length;
//		MessageContentLength = Math.min(140, cl);
//		byte[] byteMsgContent = new byte[cl];
//		System.arraycopy(content.getBytes(), 0,byteMsgContent , 0, MessageContentLength);
//		if (cl > content.length()) MessageCoding = 15;
//		return byteMsgContent;
//	}

 

	public String getCorpId() {
		return CorpId;
	}



	public void setCorpId(String corpId) {
		CorpId = corpId;
	}


 

 

	public String getChargeNumber() {
		return ChargeNumber;
	}



	public void setChargeNumber(String chargeNumber) {
		ChargeNumber = chargeNumber;
	}



 

	public int getFeeType() {
		return FeeType;
	}



	public void setFeeType(int feeType) {
		FeeType = feeType;
	}



 

	public int getFeeValue() {
		return FeeValue;
	}



	public void setFeeValue(int feeValue) {
		FeeValue = feeValue;
	}



 

	public int getGivenValue() {
		return GivenValue;
	}



	public void setGivenValue(int givenValue) {
		GivenValue = givenValue;
	}



 
 

 

	public String[] getUserNumber() {
		return UserNumber;
	}



	public void setUserNumber(String[] userNumber) {
		UserNumber = userNumber;
	}


 

 

	public int getMorelatetoMTFlag() {
		return MorelatetoMTFlag;
	}



	public void setMorelatetoMTFlag(int morelatetoMTFlag) {
		MorelatetoMTFlag = morelatetoMTFlag;
	}



	public String getSPNumber() {
		return SPNumber;
	}



	public void setSPNumber(String sPNumber) {
		SPNumber = sPNumber;
	}



 

	public int getPriority() {
		return Priority;
	}



	public void setPriority(int priority) {
		Priority = priority;
	}


 
	

 
 
 
 

 


 


	public byte[] getMessageContent() {
		return MessageContent;
	}



	public void setMessageContent(byte[] messageContent) {
		MessageContent = messageContent;
	}



	public int getMessageContentLength() {
		return MessageContentLength;
	}



	public void setMessageContentLength(int messageContentLength) {
		MessageContentLength = messageContentLength;
	}



	public String getReserve() {
		return Reserve;
	}



	public void setReserve(String reserve) {
		Reserve = reserve;
	}



 


	public String getServiceType() {
		return ServiceType;
	}



	public void setServiceType(String serviceType) {
		ServiceType = serviceType;
	}



 
 

	public int getReportFlag() {
		return ReportFlag;
	}



	public void setReportFlag(int reportFlag) {
		ReportFlag = reportFlag;
	}



	public int getMessageType() {
		return MessageType;
	}



	public void setMessageType(int messageType) {
		MessageType = messageType;
	}



	public int getTP_pid() {
		return TP_pid;
	}



	public void setTP_pid(int tP_pid) {
		TP_pid = tP_pid;
	}



	public int getTP_udhi() {
		return TP_udhi;
	}



	public void setTP_udhi(int tP_udhi) {
		TP_udhi = tP_udhi;
	}



	public int getMessageCoding() {
		return MessageCoding;
	}



	public void setMessageCoding(int messageCoding) {
		MessageCoding = messageCoding;
	}

	
	


//	private String getSeqNum() {
//		String key = seqFmt.format(new Date());
//		if (key.equals(dtKey)) {
//			iSeq++;
//		} else {
//			dtKey = key;
//			iSeq = 0;
//		}
//		return key + Integer.parseInt(iSeq + "", 2);
//	}

}
