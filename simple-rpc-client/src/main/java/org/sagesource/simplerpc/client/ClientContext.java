package org.sagesource.simplerpc.client;

import org.sagesource.simplerpc.core.Context;

/**
 * <p>客户端调用上下文</p>
 * <pre>
 *     author      XueQi
 *     date        2018/7/9
 *     email       job.xueqi@outlook.com
 * </pre>
 */
public class ClientContext implements Context {

	private String serviceName;
	private String version;
	private String providerIp;
	private int    providerPort;

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getProviderIp() {
		return providerIp;
	}

	public void setProviderIp(String providerIp) {
		this.providerIp = providerIp;
	}

	public int getProviderPort() {
		return providerPort;
	}

	public void setProviderPort(int providerPort) {
		this.providerPort = providerPort;
	}
}
