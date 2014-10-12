package org.marker.protocol.sgip.msg;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.marker.protocol.tools.Utils;

 
public class Deliver extends Message {

	private String UserNumber;
	private String SPNumber;
	private int tpPid;
	private int tpUdhi;
	private int encoding;
	private int messageLen;
	private byte content[];
	private String reserve;

	public Deliver() {
		super(Message.SGIP_DELIVER);
		tpPid = 0;
		tpUdhi = 0;
	}

	public void write(OutputStream out) throws IOException {
		int len = content.length;
		getHead().setLength(57 + len);
		getHead().write(out);
		byte bs[] = new byte[21];
		System.arraycopy(UserNumber.getBytes(), 0, bs, 0,
				Math.min(21, UserNumber.getBytes().length));
		out.write(bs);
		bs = new byte[21];
		System.arraycopy(SPNumber.getBytes(), 0, bs, 0,
				Math.min(21, SPNumber.getBytes().length));
		out.write(bs);
		out.write(tpPid);
		out.write(tpUdhi);
		out.write(encoding);
		out.write(Utils.IntToBytes4(messageLen));
		out.write(content);
		bs = new byte[8];
		System.arraycopy(reserve.getBytes(), 0, bs, 0,
				Math.min(8, reserve.getBytes().length));
		out.write(bs);
		out.flush();
	}

	public void read(InputStream in) throws IOException {
		byte bs[] = new byte[49];
		int rc = in.read(bs);
		if (rc < 49)
			throw new EOFException(String.valueOf(rc));
		UserNumber = Utils.getString(bs, 0, 21);
		SPNumber   = Utils.getString(bs, 21, 21);
		tpPid  = bs[42];
		tpUdhi = bs[43];
		encoding = bs[44];
		messageLen = Utils.Bytes4ToInt(bs, 45);
		bs = new byte[messageLen + 8];
		rc = in.read(bs);
		if (rc < messageLen + 8) {
			throw new EOFException(String.valueOf(rc));
		} else {
			content = new byte[messageLen];
			System.arraycopy(bs, 0, content, 0, messageLen);
			reserve = Utils.getString(bs, messageLen, 8);
		}
	}

	public byte[] getContent() {
		return content;
	}

	public String getMessageContent() throws UnsupportedEncodingException {
		return new String(content, encoding != 8 ? "GBK" : "UTF-16BE");
	}

	public void setContent(byte content[]) {
		this.content = content;
	}

	public int getEncoding() {
		return encoding;
	}

	public void setEncoding(int encoding) {
		this.encoding = encoding;
	}

	public int getMessageLen() {
		return messageLen;
	}

	public void setMessageLen(int messageLen) {
		this.messageLen = messageLen;
	}

	public String getUserNumber() {
		return UserNumber;
	}

	public void setUserNumber(String UserNumber) {
		this.UserNumber = UserNumber;
	}

	public String getSPNumber() {
		return SPNumber;
	}

	public void setSPNumber(String SPNumber) {
		this.SPNumber = SPNumber;
	}

	public String getReserve() {
		return reserve;
	}

	public void setReserve(String reserve) {
		this.reserve = reserve;
	}

	public int getTpPid() {
		return tpPid;
	}

	public void setTpPid(int tpPid) {
		this.tpPid = tpPid;
	}

	public int getTpUdhi() {
		return tpUdhi;
	}

	public void setTpUdhi(int tpUdhi) {
		this.tpUdhi = tpUdhi;
	}
}
