package org.marker.protocol.sgip.conn;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
/**
 * 具有获取请求完成时间的输入缓冲流
 * @author marker
 * */
public class SGIP_InputStream extends BufferedInputStream {

	private long lastAccess;

	public SGIP_InputStream(InputStream in) {
		super(in);
		lastAccess = 0L;
	}

	public SGIP_InputStream(InputStream in, int size) {
		super(in, size);
		lastAccess = 0L;
	}

	public int read() throws IOException {
		int rc = super.read();
		lastAccess = System.currentTimeMillis();
		return rc;
	}

	public int read(byte b[], int off, int len) throws IOException {
		int rc = super.read(b, off, len);
		lastAccess = System.currentTimeMillis();
		return rc;
	}

	public int read(byte b[]) throws IOException {
		int rc = super.read(b);
		lastAccess = System.currentTimeMillis();
		return rc;
	}

	public long getLastAccess() {
		return lastAccess;
	}
}
