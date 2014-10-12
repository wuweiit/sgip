package org.marker.protocol.sgip.msg;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 抽象消息类
 * @author marker
 * */
public abstract class Message {

	public static final int SGIP_CONNECT        = 0x1;
	public static final int SGIP_CONNECT_RESP   = 0x80000001;
	public static final int SGIP_TERMINATE      = 0x2;
	public static final int SGIP_TERMINATE_RESP = 0x80000002;
	public static final int SGIP_SUBMIT         = 0x3;
	public static final int SGIP_SUBMIT_RESP    = 0x80000003;
	public static final int SGIP_DELIVER        = 0x4;
	public static final int SGIP_DELIVER_RESP   = 0x80000004;
	public static final int SGIP_REPORT         = 0x5;
	public static final int SGIP_REPORT_RESP    = 0x80000005;
	public static final int SGIP_ACTIVE_TEST    = 0x10000001;//链路测试命令
	public static final int SGIP_ACTIVE_TEST_RESP = 0x10000010;

	
	
	//消息头
	private MsgHead head;

	
	
	
	
	public Message(int cmdId) {
		head = new MsgHead();
		head.setCommand(cmdId);
	}

	public MsgHead getHead() {
		return head;
	}

	public void setHead(MsgHead head) {
		this.head = head;
	}

	public int getCommand() {
		return head.getCommand();
	}

	public String getSequence() {
		return head.getSequence();
	}

	public int getLength() {
		return head.getLength();
	}

	public abstract void write(OutputStream outputstream) throws IOException;

	public abstract void read(InputStream inputstream) throws IOException;
}
