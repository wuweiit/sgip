package org.marker.protocol.sgip.msg;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.marker.protocol.tools.Utils;


/**
 * 绑定命令响应
 * @author marker
 * @date 2012-10-12
 * */
public class BindResp extends Message {

	//Bind执行命令是否成功
	private int result;
	//保留，扩展用
	private String reserve;

	public BindResp() {
		super(Message.SGIP_CONNECT_RESP);
		result = 0;
		getHead().setLength(getLength() + 9);
	}

	public BindResp(MsgHead head) {
		super(Message.SGIP_CONNECT_RESP);
		result = 0;
		getHead().setLength(getLength() + 9);
		getHead().setSequence1(head.getSequence1());
		getHead().setSequence2(head.getSequence2());
		getHead().setSequence3(head.getSequence3());
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

	public void write(OutputStream out) throws IOException {
		getHead().write(out);
		out.write((byte)result);
		byte bs[] = new byte[8];
		if (reserve != null)
			System.arraycopy(reserve.getBytes(), 0, bs, 0,
					Math.min(reserve.getBytes().length, 8));
		out.write(bs);
		out.flush();
	}

	public void read(InputStream in) throws IOException {
		byte bs[] = new byte[9];
		int rc = in.read(bs);
		if (rc < 9) {
			throw new EOFException(String.valueOf(rc));
		} else {
			result  = bs[0];
			reserve = Utils.getString(bs, 1, 8);
			return;
		}
	}
}
