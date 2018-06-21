package org.simple.rpc.config;

/**
 * <p>RPC Server端配置信息</p>
 * <pre>
 *     author      XueQi
 *     date        2018/6/19
 *     email       job.xueqi@outlook.com
 * </pre>
 */
public class ServerConfig {

	private String address;
	private int    port;

	public ServerConfig() {
	}

	public ServerConfig(String address, int port) {
		this.address = address;
		this.port = port;
	}

	public String getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
