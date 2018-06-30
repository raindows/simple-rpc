package org.sagesource.simplerpc.client.proxy;

import org.apache.commons.pool2.ObjectPool;
import org.apache.thrift.protocol.TProtocol;
import org.sagesource.simplerpc.entity.ProtocolPoolConfig;
import org.sagesource.simplerpc.entity.ServerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * <p>Client 动态代理 Handler</p>
 * <pre>
 *     author      XueQi
 *     date        2018/6/29
 *     email       job.xueqi@outlook.com
 * </pre>
 */
public class ServiceClientProxyInvocationHandler implements InvocationHandler {
	private static Logger LOGGER = LoggerFactory.getLogger(ServiceClientProxyInvocationHandler.class);

	// 客户端接口名称后缀
	private static final String SERVICE_ICLIENT_NAME = "$Client";

	// 连接池配置信息
	private ProtocolPoolConfig    protocolPoolConfig;
	// 连接池
	private ObjectPool<TProtocol> clientProtocolPool;
	// 服务端信息
	private ServerInfo            serverInfo;

	/**
	 * 设置连接池
	 *
	 * @param clientProtocolPool
	 * @return
	 */
	public ServiceClientProxyInvocationHandler buildClientProtocolPool(ObjectPool clientProtocolPool) {
		this.clientProtocolPool = clientProtocolPool;
		return this;
	}

	/**
	 * 设置连接池配置信息
	 *
	 * @param protocolPoolConfig
	 * @return
	 */
	public ServiceClientProxyInvocationHandler buildProtocolPoolConfig(ProtocolPoolConfig protocolPoolConfig) {
		this.protocolPoolConfig = protocolPoolConfig;
		return this;
	}

	/**
	 * 设置服务端信息
	 *
	 * @param serverInfo
	 * @return
	 */
	public ServiceClientProxyInvocationHandler buildServerInfo(ServerInfo serverInfo) {
		this.serverInfo = serverInfo;
		return this;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		String serviceName = this.serverInfo.getServiceName();

		// 从连接池中获取连接
		TProtocol protocol = null;
		Object    result   = null;
		try {
			protocol = this.clientProtocolPool.borrowObject();

			// 创建客户端对象
			String clientInterfaceName  = serviceName.concat(SERVICE_ICLIENT_NAME);
			Class  clientInterfaceClazz = Class.forName(clientInterfaceName);

			// 实例化构造方法
			Object clientInstance = clientInterfaceClazz.getConstructor(TProtocol.class).newInstance(protocol);
			result = method.invoke(clientInstance, args);
		} catch (Exception e) {
			// fixme
			e.printStackTrace();
		} finally {
			this.clientProtocolPool.returnObject(protocol);
		}
		return result;
	}
}
