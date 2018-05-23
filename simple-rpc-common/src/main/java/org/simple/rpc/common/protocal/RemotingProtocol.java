package org.simple.rpc.common.protocal;

/**
 * <p>网络传输协议信息</p>
 * <pre>
 *     author      XueQi
 *     date        2018/5/23
 *     email       sage.xue@vipshop.com
 * </pre>
 */
public class RemotingProtocol {

	/**
	 * 请求消息传输
	 */
	public static final byte REQUEST_REMOTING = 1;

	/**
	 * 响应消息传输
	 */
	public static final byte RESPONSE_REMOTING = 2;
}
