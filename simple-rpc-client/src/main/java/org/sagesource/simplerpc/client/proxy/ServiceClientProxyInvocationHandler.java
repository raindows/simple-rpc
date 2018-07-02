package org.sagesource.simplerpc.client.proxy;

import org.apache.commons.pool2.ObjectPool;
import org.apache.thrift.protocol.TProtocol;
import org.sagesource.simplerpc.client.pool.ClientProtocolPoolFactory;
import org.sagesource.simplerpc.entity.ProtocolPoolConfig;
import org.sagesource.simplerpc.entity.ServerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.SocketTimeoutException;

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
	private static final String SERVICE_ICLIENT_NAME       = "$Client";
	private static final String SERVICE_ASYNC_ICLIENT_NAME = "$AsyncClient";

	// 连接池配置信息
	private ProtocolPoolConfig protocolPoolConfig;
	// 服务端信息
	private ServerInfo         serverInfo;

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
		TProtocol             protocol           = null;
		Object                result             = null;
		ObjectPool<TProtocol> clientProtocolPool = null;
		try {
			// 获取线程池
			clientProtocolPool = ClientProtocolPoolFactory.getInstance().createOrObtain(this.protocolPoolConfig, this.serverInfo);
			protocol = clientProtocolPool.borrowObject();

			// 创建客户端对象
			String clientInterfaceName  = serviceName.concat(SERVICE_ICLIENT_NAME);
			Class  clientInterfaceClazz = Class.forName(clientInterfaceName);

			// 实例化构造方法
			Object clientInstance = clientInterfaceClazz.getConstructor(TProtocol.class).newInstance(protocol);
			result = method.invoke(clientInstance, args);
		} catch (SocketTimeoutException ex) {
			// 对于连接超时的请求，在返回数据源的时候，需要关闭连接，避免连接被其他请求复用，获取到错误的结果
			protocol.getTransport().flush();
			protocol.getTransport().close();
		} catch (Exception e) {
			// fixme
			e.printStackTrace();
		} finally {
			if (clientProtocolPool != null) {
				clientProtocolPool.returnObject(protocol);
			}
		}
		return result;
	}
}
