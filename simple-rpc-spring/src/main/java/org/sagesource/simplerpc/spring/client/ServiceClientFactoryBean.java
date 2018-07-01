package org.sagesource.simplerpc.spring.client;

import org.sagesource.simplerpc.client.proxy.ServiceClientProxy;
import org.sagesource.simplerpc.entity.ProtocolPoolConfig;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * <p></p>
 * <pre>
 *     author      XueQi
 *     date        2018/7/1
 *     email       job.xueqi@outlook.com
 * </pre>
 */
public class ServiceClientFactoryBean implements FactoryBean, InitializingBean {
	/**
	 * 接口类型
	 */
	private Class              interfaceType;
	/**
	 * 服务版本信息
	 */
	private String             serviceVersion;
	/**
	 * 客户端连接池配置
	 */
	private ProtocolPoolConfig protocolPoolConfig;

	@Override
	public Object getObject() throws Exception {
		return ServiceClientProxy.createClient(interfaceType, serviceVersion, protocolPoolConfig);
	}

	@Override
	public Class<?> getObjectType() {
		return interfaceType;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (protocolPoolConfig == null) {
			// 连接池配置为空 创建默认值
			protocolPoolConfig = new ProtocolPoolConfig();
		}
	}

	public void setInterfaceType(Class interfaceType) {
		this.interfaceType = interfaceType;
	}

	public void setServiceVersion(String serviceVersion) {
		this.serviceVersion = serviceVersion;
	}

	public void setProtocolPoolConfig(ProtocolPoolConfig protocolPoolConfig) {
		this.protocolPoolConfig = protocolPoolConfig;
	}
}
