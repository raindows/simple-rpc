package org.sagesource.simplerpc.core.loadbalance;

import org.sagesource.simplerpc.entity.ServerInfo;
import org.sagesource.simplerpc.exception.SimpleRpcException;

/**
 * <p>LoadBalanceEngine</p>
 * <pre>
 *     author      XueQi
 *     date        2018/7/2
 *     email       job.xueqi@outlook.com
 * </pre>
 */
public interface LoadBalanceEngine {

	/**
	 * 获取服务可用的服务列表
	 *
	 * @param serviceName
	 * @param version
	 * @return
	 * @throws SimpleRpcException
	 */
	ServerInfo availableServerInfo(String serviceName, String version) throws SimpleRpcException;

}
