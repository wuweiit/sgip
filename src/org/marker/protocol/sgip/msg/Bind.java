package org.marker.protocol.sgip.msg;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.marker.protocol.tools.Sequence;
import org.marker.protocol.tools.Tools;
import org.marker.protocol.tools.Utils;

/**
 * 绑定命令（SP登录SMG网关）
 * @author marker
 * @date 2012-10-12
 * @version 1.0
 * */
public class Bind extends Message {

	//服务器端给客户端分配的登录名
	private String name;
	//服务器端和Login Name对应的密码
	private String password;
	//登录类型  
	private int loginType = 1;//1：SP向SMG建立的连接，用于发送命令
	//保留，扩展用
	private String reserve;

	
	
	public Bind() {
		super(Message.SGIP_CONNECT);
		getHead().setLength(super.getLength() + 41);
	}
	
	
	/**
	 * Bind构造方法
	 * @param loginType 登录类型
	 * @param name     登录名
	 * @param password 登录密码 
	 * */
	public Bind(int loginType, String name, String password){
		super(Message.SGIP_CONNECT);
		super.getHead().setLength(getLength() + 41);
		this.loginType = loginType;
		this.name = name;
		this.password = password;
	}
	
	/**
	 * Bind构造方法
	 * @param loginType 登录类型
	 * @param name     登录名
	 * @param password 登录密码 
	 * @param nodeid   通讯节点
	 * */
	public Bind(int loginType, String name, String password, long nodeid){
		super(Message.SGIP_CONNECT);
		super.getHead().setLength(getLength() + 41);
		this.loginType = loginType;
		this.name = name;
		this.password = password;
		super.getHead().setSequence1(nodeid);//设置通信节点命令源
		super.getHead().setSequence2(Tools.getCurrentSequenceDate());
		super.getHead().setSequence3(Sequence.next());
		
	}

	/**
	 * Bind构造方法 
	 * @param name     登录名
	 * @param password 登录密码 
	 * */
	public Bind(String name, String password){
		super(Message.SGIP_CONNECT);
		super.getHead().setLength(getLength() + 41);
		this.name = name;
		this.password = password;
	}	
	
	
	/**
	 * Bind构造方法 
	 * @param name     登录名
	 * @param password 登录密码 
	 * */
	public Bind(String name, String password,long nodeid){
		super(Message.SGIP_CONNECT);
		super.getHead().setLength(getLength() + 41);
		this.name = name;
		this.password = password;
		super.getHead().setSequence1(nodeid);//设置通信节点命令源
		super.getHead().setSequence2(Tools.getCurrentSequenceDate());
		super.getHead().setSequence3(Sequence.next());
	}	
	
	
	public void write(OutputStream out) throws IOException {
		getHead().write(out);
		out.write((byte)loginType);
		byte bs[] = new byte[16];
		System.arraycopy(name.getBytes(), 0, bs, 0,
				Math.min(name.getBytes().length, 16));
		out.write(bs);
		bs = new byte[16];
		System.arraycopy(password.getBytes(), 0, bs, 0,
				Math.min(password.getBytes().length, 16));
		out.write(bs);
		bs = new byte[8];
		if (reserve != null)
			System.arraycopy(reserve.getBytes(), 0, bs, 0,
					Math.min(reserve.getBytes().length, 8));
		out.write(bs);
		out.flush();
	}

	
	
	
	public void read(InputStream in) throws IOException {
		byte bs[] = new byte[41];
		int rc = in.read(bs);
		if (rc < 41) {
			throw new EOFException(String.valueOf(rc));
		} else {
			loginType = bs[0];
			name     = Utils.getString(bs, 1, 16);
			password = Utils.getString(bs, 17, 16);
			reserve  = Utils.getString(bs, 33, 8);
			return;
		}
	}

	public int getLoginType() {
		return loginType;
	}

	public void setLoginType(int loginType) {
		this.loginType = loginType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getReserve() {
		return reserve;
	}

	public void setReserve(String reserve) {
		this.reserve = reserve;
	}
}
