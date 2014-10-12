package org.marker.protocol.exception;


/**
 * 绑定异常
 * @author marker
 * @date 2012-10-12
 * */
public class BindException extends Exception {
	
	private static final long serialVersionUID = 1L;
	

	public BindException(String result) {
		super(result);
	}

 
}
