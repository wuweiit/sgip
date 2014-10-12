package org.marker.protocol;


/**
 * 协议链接对象接口
 * 
 * */
public interface Connection {

	
	
	boolean isbind();
	
	
	
	
	/**
	 * 关闭链接
	 * */
	public void close();
}
