package org.sagesource.simplerpc.basic.entity;

/**
 * <p>服务端信息封装，包括 IP HOST 服务 版本号信息 等</p>
 * <pre>
 *     author      XueQi
 *     date        2018/6/29
 *     email       job.xueqi@outlook.com
 * </pre>
 */
public class ServerInfo {

	// 服务端 IP
	private String serverIP;
	// 服务端口号
	private int    port;
	// 服务名称
	private String serviceName;
	// 服务版本号
	private String serviceVersion;
	// 服务权重
	private int    weight;

	/**
	 * 构建服务端 IP
	 *
	 * @param serverIP
	 * @return
	 */
	public ServerInfo buildServerIP(String serverIP) {
		this.serverIP = serverIP;
		return this;
	}

	/**
	 * 构建端口
	 *
	 * @param port
	 * @return
	 */
	public ServerInfo buildPort(int port) {
		this.port = port;
		return this;
	}

	/**
	 * 构建服务名称
	 *
	 * @param serviceName
	 * @return
	 */
	public ServerInfo buildServiceName(String serviceName) {
		this.serviceName = serviceName;
		return this;
	}

	/**
	 * 构建服务版本号
	 *
	 * @param serviceVersion
	 * @return
	 */
	public ServerInfo buildServiceVersion(String serviceVersion) {
		this.serviceVersion = serviceVersion;
		return this;
	}

	/**
	 * 构建权重
	 *
	 * @param weight
	 * @return
	 */
	public ServerInfo buildWeight(int weight) {
		this.weight = weight;
		return this;
	}

	@Override
	public int hashCode() {
		return 1;
	}

	@Override
	public boolean equals(Object obj) {
		ServerInfo serverInfo = (ServerInfo) obj;

		if (serverInfo.serviceVersion.equals(this.serviceVersion)
				&& serverInfo.serviceName.equals(this.serviceName)
				&& serverInfo.serverIP.equals(this.serverIP)
				&& serverInfo.port == this.port)
			return true;
		return false;
	}

	public String getServerIP() {
		return serverIP;
	}

	public int getPort() {
		return port;
	}

	public String getServiceName() {
		return serviceName;
	}

	public String getServiceVersion() {
		return serviceVersion;
	}

	public int getWeight() {
		return weight;
	}

}
