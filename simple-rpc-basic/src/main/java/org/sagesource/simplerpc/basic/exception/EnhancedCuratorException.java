package org.sagesource.simplerpc.basic.exception;

/**
 * <p>ZK异常</p>
 * <pre>
 *     author      XueQi
 *     date        2018/7/3
 *     email       job.xueqi@outlook.com
 * </pre>
 */
public class EnhancedCuratorException extends SimpleRpcException {
	public EnhancedCuratorException() {
	}

	public EnhancedCuratorException(String message) {
		super(message);
	}

	public EnhancedCuratorException(String message, Throwable cause) {
		super(message, cause);
	}

	public EnhancedCuratorException(Throwable cause) {
		super(cause);
	}
}
