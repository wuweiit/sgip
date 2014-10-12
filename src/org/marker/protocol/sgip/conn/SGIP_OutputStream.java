package org.marker.protocol.sgip.conn;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
/**
 * 具有获取请求完成时间的输出缓冲流
 * @author marker
 * */
public class SGIP_OutputStream extends BufferedOutputStream {

	private long lastAccess;

	public SGIP_OutputStream(OutputStream out) {
		super(out);
		lastAccess = 0L;
	}

	public SGIP_OutputStream(OutputStream out, int size) {
		super(out, size);
		lastAccess = 0L;
	}

	public synchronized void write(byte b[], int off, int len)
			throws IOException {
		super.write(b, off, len);
		lastAccess = System.currentTimeMillis();
	}

	public synchronized void write(int b) throws IOException {
		super.write(b);
		lastAccess = System.currentTimeMillis();
	}

	public long getLastAccess() {
		return lastAccess;
	}
}