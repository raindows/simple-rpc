package org.simple.rpc.common.exception.remoting;

/**
 * <p>Remoting 上下文Exception</p>
 * <pre>
 *     author      XueQi
 *     date        2018/5/24
 *     email       sage.xue@vipshop.com
 * </pre>
 */
public class RemotingContextException extends RemotingException {
	public RemotingContextException(String message) {
		super(message);
	}

	public RemotingContextException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
