package org.simple.rpc.serialization;

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
	 * 响应错误信息描述 FIXME 未来替换成 Throwable
	 */
	private String error;
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

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
}
