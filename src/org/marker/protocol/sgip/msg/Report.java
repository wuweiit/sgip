package org.marker.protocol.sgip.msg;

import java.io.*;

import org.marker.protocol.tools.Utils;

/**
 * 状态报告
 * @author marker
 * */
public class Report extends Message {

	//序列
	private String submitSeq;
	//类型
	private int type;
	//手机号码
	private String mobile;
	//状态
	private int result;
	//错误码
	private int errorCode;
	//保留
	private String reserve;

	
	
	public Report() {
		super(Message.SGIP_REPORT);
		type = 0;
		getHead().setLength(44 + getLength());
	}

	
	public void write(OutputStream outputstream) throws IOException { }

	
	
	public void read(InputStream in) throws IOException {
		byte bs[] = new byte[44];
		int rc = in.read(bs);
		if (rc < 44) {
			throw new EOFException(String.valueOf(rc));
		} else {
			submitSeq  = String.valueOf(Utils.Bytes4ToLong(bs));
			submitSeq += String.valueOf(Utils.Bytes4ToLong(bs, 4));
			submitSeq += String.valueOf(Utils.Bytes4ToLong(bs, 8));
			type = bs[12];
			mobile = Utils.getString(bs, 13, 21);
			result = bs[34];
			errorCode = bs[35];
			reserve = Utils.getString(bs, 36, 8);
		}
	}
	
	

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getReserve() {
		return reserve;
	}

	public void setReserve(String reserve) {
		this.reserve = reserve;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getSubmitSeq() {
		return submitSeq;
	}

	public void setSubmitSeq(String submitSeq) {
		this.submitSeq = submitSeq;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
