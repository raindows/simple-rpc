package org.sagesource.simplerpc.provider;

import org.apache.commons.lang3.StringUtils;
import org.apache.thrift.TProcessor;
import org.sagesource.simplerpc.basic.entity.ServerInfo;
import org.sagesource.simplerpc.basic.utils.InetSocketAddressUtils;
import org.sagesource.simplerpc.config.SystemConfigClientManager;
import org.sagesource.simplerpc.provider.thread.ProviderServerThread;

import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.Constructor;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <p> 简单的服务提供方工厂</p>
 * <pre>
 *     author      XueQi
 *     date        2018/7/12
 *     email       job.xueqi@outlook.com
 * </pre>
 */
public class SimpleServiceProviderFactory {
	private static final CopyOnWriteArrayList<ProviderServerThread> providerThreadList = new CopyOnWriteArrayList<>();

	/**
	 * 服务端口
	 */
	private int port;
	/**
	 * 权重
	 */
	private int weight = 1;
	/**
	 * 服务实现类
	 */
	private Object serviceImpl;
	/**
	 * 服务版本号
	 */
	private String serviceVersion;

	public SimpleServiceProviderFactory(int port, int weight, Object serviceImpl, String serviceVersion) {
		this.port = port;
		this.weight = weight;
		this.serviceImpl = serviceImpl;
		this.serviceVersion = serviceVersion;
	}

	public SimpleServiceProviderFactory(int port, Object serviceImpl, String serviceVersion) {
		this.port = port;
		this.serviceImpl = serviceImpl;
		this.serviceVersion = serviceVersion;
	}

	/**
	 * 创建服务提供方
	 */
	public void createServiceProvider() throws Exception {
		String     appName    = SystemConfigClientManager.getSystemConfigClient().appName();
		String     localIp    = InetSocketAddressUtils.getLocalIP();
		ServerInfo serverInfo = new ServerInfo();

		// 获取实现类的接口信息
		Class<? extends Object> serviceImplClass = serviceImpl.getClass();
		Class<?>[]              interfaces       = serviceImplClass.getInterfaces();
		if (interfaces == null || interfaces.length == 0) {
			throw new IllegalClassFormatException("service-class should implements Iface");
		}

		// 获取 Iface 接口， 再获取 Processor
		TProcessor processor = null;
		for (Class<?> anInterface : interfaces) {
			String interfaceName = anInterface.getSimpleName();
			if (!StringUtils.contains(interfaceName, "IFace")) continue;

			// 获取服务名称
			String serviceName = anInterface.getEnclosingClass().getName();
			// 获取处理器名称
			String processorName = serviceName + "$Processor";

			ClassLoader    classLoader = Thread.currentThread().getContextClassLoader();
			Constructor<?> constructor = classLoader.loadClass(processorName).getConstructor(anInterface);

			processor = (TProcessor) constructor.newInstance(this.serviceImpl);
			serverInfo.setServiceName(serviceName);
			break;
		}

		if (processor == null) {
			throw new IllegalClassFormatException("service-class should implements Iface, TProcessor is null");
		}

		// 需要在后台线程中启动服务监听
		serverInfo.setWeight(this.weight);
		serverInfo.setAppName(appName);
		serverInfo.setServiceVersion(this.serviceVersion);
		serverInfo.setServerIP(localIp);
		serverInfo.setPort(this.port);
		ProviderServerThread providerServerThread = new ProviderServerThread(serverInfo, processor);
		providerServerThread.start();
		providerThreadList.add(providerServerThread);
	}

	/**
	 * 关闭服务
	 */
	public static void close() {
		for (ProviderServerThread providerServerThread : providerThreadList) {
			providerServerThread.close();
		}
	}

}
