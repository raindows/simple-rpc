package org.simple.rpc.common.domain;

/**
 * <p>响应通信对象</p>
 * <pre>
 *     author      XueQi
 *     date        2018/6/12
 *     email       job.xueqi@outlook.com
 * </pre>
 */
public class RpcResponse {
	/**
	 * 调用链追踪 traceId
	 */
	private String traceId;
	/**
	 * 响应错误信息
	 */
	private String cause;
	/**
	 * 响应 code FIXME 规划框架码表
	 */
	private String code;
	/**
	 * 响应结果
	 */
	private Object result;

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
