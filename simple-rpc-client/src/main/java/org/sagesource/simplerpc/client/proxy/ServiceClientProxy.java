package org.sagesource.simplerpc.client.proxy;

import org.apache.commons.lang3.StringUtils;
import org.sagesource.simplerpc.entity.ProtocolPoolConfig;
import org.sagesource.simplerpc.entity.ServerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p> 服务客户端代理</p>
 * <pre>
 *     author      XueQi
 *     date        2018/6/29
 *     email       job.xueqi@outlook.com
 * </pre>
 */
public class ServiceClientProxy {
	private static Logger LOGGER = LoggerFactory.getLogger(ServiceClientProxy.class);

	/**
	 * 同步客户端缓存对象
	 */
	private static final ConcurrentHashMap<String, Object> syncCacheClientMapper    = new ConcurrentHashMap<>();
	/**
	 * 异步客户端缓存对象
	 */
	private static final ConcurrentHashMap<String, Object> asyncCacheClientMapper   = new ConcurrentHashMap<>();
	// 服务父类接口名称
	private static final String                            SERVICE_IFACE_NAME       = "$Iface";
	private static final String                            SERVICE_ASYNC_IFACE_NAME = "$AsyncIface";

	/**
	 * 创建 Service Client
	 *
	 * @param serviceClass       服务类型
	 * @param version            版本号
	 * @param protocolPoolConfig 连接池配置
	 * @param <T>
	 * @return
	 */
	public static <T> T createClient(Class<T> serviceClass, String version, ProtocolPoolConfig protocolPoolConfig) throws Exception {
		// 获取接口类名称
		String serviceClassName = serviceClass.getName();

		// 判断是否为异步接口
		boolean isAsync = false;
		// 服务名称
		String serviceName = null;
		if (StringUtils.contains(serviceClassName, SERVICE_ASYNC_IFACE_NAME)) {
			isAsync = true;
			serviceName = serviceClassName.substring(0, serviceClassName.lastIndexOf(SERVICE_ASYNC_IFACE_NAME));
		} else {
			serviceName = serviceClassName.substring(0, serviceClassName.lastIndexOf(SERVICE_IFACE_NAME));
		}

		// 从缓存中获取客户端
		Object cacheClient = null;
		if (isAsync) {
			cacheClient = asyncCacheClientMapper.get(serviceName);
		} else {
			cacheClient = syncCacheClientMapper.get(serviceName);
		}
		if (cacheClient != null) {
			return (T) cacheClient;
		}

		// 获取连接池
		// TODO: 服务地址支持静态路由
		ServerInfo            serverInfo         = new ServerInfo().buildServiceName(serviceName).buildServiceVersion(version);

		// 基于 JDK 动态代理获取 Client
		ServiceClientProxyInvocationHandler proxyInvocationHandler = new ServiceClientProxyInvocationHandler()
				.buildProtocolPoolConfig(protocolPoolConfig)
				.buildServerInfo(serverInfo);

		T client = (T) Proxy.newProxyInstance(serviceClass.getClassLoader(), new Class[]{serviceClass}, proxyInvocationHandler);
		if (client != null) {
			// 写入客户端缓存
			if (isAsync) {
				asyncCacheClientMapper.putIfAbsent(serviceName, client);
			} else {
				syncCacheClientMapper.putIfAbsent(serviceName, client);
			}
		}
		return client;
	}

}
