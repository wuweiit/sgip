package org.marker.protocol.sgip.msg;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
 
public class Unbind extends Message {

	public Unbind() {
		super(Message.SGIP_TERMINATE);
	}

	public void write(OutputStream out) throws IOException {
		getHead().write(out);
		out.flush();
	}

	//取消绑定
	public void read(InputStream inputstream) throws IOException { }
}
