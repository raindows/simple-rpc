package org.simple.rpc.common.domain;

/**
 * <p>请求通信对象</p>
 * <pre>
 *     author      XueQi
 *     date        2018/6/12
 *     email       job.xueqi@outlook.com
 * </pre>
 */
public class RpcRequest {
	/**
	 * 调用链追踪 traceId
	 */
	private String     traceId;
	/**
	 * 请求目标 Class 名称
	 */
	private String     targetClassName;
	/**
	 * 请求目标 Method 名称
	 */
	private String     targetMethodName;
	/**
	 * 请求参数方法参数类型列表
	 */
	private Class<?>[] parameterTypes;
	/**
	 * 请求参数列表
	 */
	private Object[]   parameters;

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	public String getTargetClassName() {
		return targetClassName;
	}

	public void setTargetClassName(String targetClassName) {
		this.targetClassName = targetClassName;
	}

	public String getTargetMethodName() {
		return targetMethodName;
	}

	public void setTargetMethodName(String targetMethodName) {
		this.targetMethodName = targetMethodName;
	}

	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}

	public void setParameterTypes(Class<?>[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}

	public Object[] getParameters() {
		return parameters;
	}

	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}
}
