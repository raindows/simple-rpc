package org.simple.rpc.common.exception.remoting;

/**
 * <p>Remoting网络传输Exception</p>
 * <pre>
 *     author      XueQi
 *     date        2018/5/23
 *     email       sage.xue@vipshop.com
 * </pre>
 */
public class RemotingException extends Exception {

	public RemotingException(String message) {
		super(message);
	}

	public RemotingException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
