 
package org.marker.protocol.sgip.msg;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
 
public class UnbindResp extends Message {

	public UnbindResp() {
		super(Message.SGIP_TERMINATE_RESP);
	}

	public UnbindResp(MsgHead head) {
		super(Message.SGIP_TERMINATE_RESP);
		getHead().setSequence1(head.getSequence1());
		getHead().setSequence2(head.getSequence2());
		getHead().setSequence3(head.getSequence3());
	}

	public void write(OutputStream out) throws IOException {
		getHead().write(out);
		out.flush();
	}

	public void read(InputStream in) throws IOException {

	}
}
