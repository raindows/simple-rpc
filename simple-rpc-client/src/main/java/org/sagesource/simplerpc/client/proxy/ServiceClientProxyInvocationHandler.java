package org.sagesource.simplerpc.client.proxy;

import org.apache.commons.pool2.ObjectPool;
import org.apache.thrift.protocol.TProtocol;
import org.sagesource.simplerpc.basic.entity.ProtocolPoolConfig;
import org.sagesource.simplerpc.client.pool.ClientProtocolPoolFactory;
import org.sagesource.simplerpc.core.zookeeper.ServiceAddressProviderAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
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
	private static final String SERVICE_ICLIENT_NAME = "$Client";

	// 服务地址获取代理
	private ServiceAddressProviderAgent serviceAddressProviderAgent;
	// 连接池配置信息
	private ProtocolPoolConfig          protocolPoolConfig;
	// 服务名称
	private String                      serviceName;
	// 服务版本号
	private String                      version;

	public ServiceClientProxyInvocationHandler() {
		this.serviceAddressProviderAgent = new ServiceAddressProviderAgent();
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
	 * 设置服务名称
	 *
	 * @param serviceName
	 * @return
	 */
	public ServiceClientProxyInvocationHandler buildServiceName(String serviceName) {
		this.serviceName = serviceName;
		serviceAddressProviderAgent.buildServiceName(serviceName);
		return this;
	}

	/**
	 * 设置版本号
	 *
	 * @param version
	 * @return
	 */
	public ServiceClientProxyInvocationHandler buildVersion(String version) {
		this.version = version;
		serviceAddressProviderAgent.buildVersion(version);
		return this;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		this.serviceAddressProviderAgent.init();

		// 从连接池中获取连接
		TProtocol             protocol           = null;
		Object                result             = null;
		ObjectPool<TProtocol> clientProtocolPool = null;
		try {
			// 获取线程池
			clientProtocolPool = ClientProtocolPoolFactory.getInstance().createOrObtain(this.protocolPoolConfig, this.serviceAddressProviderAgent);
			protocol = clientProtocolPool.borrowObject();

			// 创建客户端对象
			String clientInterfaceName  = serviceName.concat(SERVICE_ICLIENT_NAME);
			Class  clientInterfaceClazz = Class.forName(clientInterfaceName);

			// 实例化构造方法
			Object clientInstance = clientInterfaceClazz.getConstructor(TProtocol.class).newInstance(protocol);
			result = method.invoke(clientInstance, args);
		} catch (Exception e) {
			if (e instanceof InvocationTargetException) {
				// 动态代理执行阶段异常
				InvocationTargetException ex = (InvocationTargetException) e;
				if (ex.getTargetException().getCause() instanceof SocketTimeoutException) {
					// 对于连接超时的请求，在返回数据源的时候，需要关闭连接，避免连接被其他请求复用，获取到错误的结果
					protocol.getTransport().flush();
					protocol.getTransport().close();
					// FIXME 超时异常需要包装抛出
				}
			}
			// todo: 未来异常需要分组并上传到监控
			throw e;
		} finally {
			if (clientProtocolPool != null && protocol != null) {
				clientProtocolPool.returnObject(protocol);
			}
		}
		return result;
	}
}
