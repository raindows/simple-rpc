package org.sagesource.simplerpc.client.proxy;

import org.apache.commons.pool2.ObjectPool;
import org.apache.thrift.protocol.TProtocol;
import org.sagesource.simplerpc.basic.entity.ProtocolPoolConfig;
import org.sagesource.simplerpc.basic.exception.SimpleRpcException;
import org.sagesource.simplerpc.client.filter.TraceClientFilter;
import org.sagesource.simplerpc.client.pool.ClientProtocolPoolFactory;
import org.sagesource.simplerpc.core.filter.IFilter;
import org.sagesource.simplerpc.core.zookeeper.ServiceAddressProviderAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

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
	// 前置过滤器列表
	private static List<IFilter> beforeFilterList = new ArrayList<>();
	private static List<IFilter> postFilterList   = new ArrayList<>();

	// 静态初始化
	static {
		// trace filter
		beforeFilterList.add(new TraceClientFilter());
	}

	public ServiceClientProxyInvocationHandler(String serviceName, String version, ProtocolPoolConfig protocolPoolConfig) throws Exception {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("create proxy client serviceName:{} version:{} protocolPoolConfig:{}", serviceName, version, protocolPoolConfig);

		this.protocolPoolConfig = protocolPoolConfig;
		this.serviceName = serviceName;
		this.version = version;
		this.serviceAddressProviderAgent = new ServiceAddressProviderAgent(serviceName, version);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("proxy invocation serviceName:{} version:{} args:{}", this.serviceName, this.version, args);

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

			// 执行前置拦截器
			doBeforeFilter();
			result = method.invoke(clientInstance, args);
			doPostFilter();
		} catch (Exception e) {
			if (e instanceof InvocationTargetException) {
				// 动态代理执行阶段异常
				InvocationTargetException ex = (InvocationTargetException) e;
				if (ex.getTargetException().getCause() instanceof SocketTimeoutException) {
					// 对于请求超时，在返回数据源的时候，需要关闭连接，避免连接被其他请求复用，获取到错误的结果
					protocol.getTransport().flush();
					protocol.getTransport().close();
					throw new SimpleRpcException(MessageFormat.format("CALL SERVICE:[{0}] VERSION:[{1}] TIMEOUT", this.serviceName, this.version));
				} else if (ex.getTargetException().getCause() instanceof SocketException) {
					// 连接超时，将该对象从线程池中移除
					clientProtocolPool.invalidateObject(protocol);
					protocol = null;
					throw new SimpleRpcException(MessageFormat.format("CALL SERVICE:[{0}] VERSION:[{1}] Connection Error", this.serviceName, this.version));
				}
			}
			throw e;
		} finally {
			if (clientProtocolPool != null && protocol != null) {
				clientProtocolPool.returnObject(protocol);
			}
		}
		return result;
	}

	//........................//

	/**
	 * 执行拦截器
	 */
	private void doBeforeFilter() {
		for (IFilter iFilter : beforeFilterList) {
			iFilter.beforeFilter();
		}
	}

	/**
	 * 执行后置拦截器
	 */
	private void doPostFilter() {
		for (IFilter iFilter : postFilterList) {
			iFilter.postFilter();
		}
	}
}
