package org.simple.rpc.common.exception.remoting;

/**
 * <p>Remoting 通用Exception</p>
 * <pre>
 *     author      XueQi
 *     date        2018/5/23
 *     email       sage.xue@vipshop.com
 * </pre>
 */
public class RemotingCommonException extends RemotingException {
	public RemotingCommonException(String message) {
		super(message);
	}

	public RemotingCommonException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
