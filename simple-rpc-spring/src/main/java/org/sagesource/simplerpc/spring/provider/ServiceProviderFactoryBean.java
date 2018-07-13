package org.sagesource.simplerpc.spring.provider;

import org.sagesource.simplerpc.core.zookeeper.ZookeeperClientFactory;
import org.sagesource.simplerpc.provider.SimpleServiceProviderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * <p></p>
 * <pre>
 *     author      XueQi
 *     date        2018/7/13
 *     email       job.xueqi@outlook.com
 * </pre>
 */
public class ServiceProviderFactoryBean implements InitializingBean, FactoryBean {
	private static Logger LOGGER = LoggerFactory.getLogger(ServiceProviderFactoryBean.class);

	/**
	 * 服务端口号
	 */
	private int    port;
	/**
	 * 服务实现类
	 */
	private Object serviceRef;
	/**
	 * 服务版本号
	 */
	private String version;
	/**
	 * 服务节点权重
	 */
	private int    weight;

	private SimpleServiceProviderFactory simpleServiceProviderFactory;

	@Override
	public Object getObject() throws Exception {
		return this.simpleServiceProviderFactory;
	}

	@Override
	public Class<?> getObjectType() {
		return SimpleServiceProviderFactory.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// 设置默认权重
		if (weight <= 0) this.weight = 1;

		SimpleServiceProviderFactory simpleServiceProviderFactory = new SimpleServiceProviderFactory(this.port, this.weight, this.serviceRef, this.version);
		simpleServiceProviderFactory.createServiceProvider();
		this.simpleServiceProviderFactory = simpleServiceProviderFactory;
	}

	/**
	 * 关闭方法
	 */
	public void close() {
		ZookeeperClientFactory.close();
		SimpleServiceProviderFactory.close();
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setServiceRef(Object serviceRef) {
		this.serviceRef = serviceRef;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}
}
