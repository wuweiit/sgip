package org.marker.protocol.exception;


/**
 * SGIP网关连接异常
 * @author marker
 * @date 2012-10-12
 * */
public class ConnectionException extends Exception {
	
	private static final long serialVersionUID = 1L;
	

	public ConnectionException(String  result) {
		super(result); 
	}

 
}
