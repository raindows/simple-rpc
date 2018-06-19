package org.simple.rpc.remoting;

import org.apache.commons.collections4.MapUtils;
import org.simple.rpc.config.RpcService;
import org.simple.rpc.config.ServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>服务提供方 Server 类</p>
 * <pre>
 *     author      XueQi
 *     date        2018/6/19
 *     email       job.xueqi@outlook.com
 * </pre>
 */
public class ProviderServer implements ApplicationContextAware, InitializingBean {
	private static Logger LOGGER = LoggerFactory.getLogger(ProviderServer.class);

	/**
	 * 服务实现 Bean 接口-实现映射
	 */
	private static ConcurrentHashMap<String, Object> providerServiceBean = new ConcurrentHashMap<>();

	/**
	 * 服务端配置
	 */
	private ServerConfig serverConfig;

	@Override
	public void afterPropertiesSet() throws Exception {
		// 判断是否注入ServerConfig对象，如果注入，以该配置为准，否则自动获取
		if (serverConfig == null) {

		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// 获取容器中所有使用RpcService注解的 Bean
		Map<String, Object> serviceBeanMapper = applicationContext.getBeansWithAnnotation(RpcService.class);
		if (MapUtils.isNotEmpty(serviceBeanMapper)) {
			// 容器中存在 Provider Service Bean
			for (Object serviceBean : serviceBeanMapper.values()) {
				String interfaceName = serviceBean.getClass().getAnnotation(RpcService.class).value().getName();
				if (providerServiceBean.containsKey(interfaceName)) {
					// 已存在同名的 Service 实现 Bean，抛出异常结束
					throw new BeanDefinitionStoreException(
							String.format("provider interface : %s, service bean already exist : %s", interfaceName, providerServiceBean.get(interfaceName)));
				} else {
					LOGGER.info("provider interface : {}, register service bean", interfaceName);
					providerServiceBean.put(interfaceName, serviceBean);
				}
			}
		}
	}

	//...............//
	public void setServerConfig(ServerConfig serverConfig) {
		this.serverConfig = serverConfig;
	}
}
