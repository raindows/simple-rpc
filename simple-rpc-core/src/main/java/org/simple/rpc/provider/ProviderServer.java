package org.simple.rpc.provider;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.apache.commons.collections4.MapUtils;
import org.simple.rpc.common.annotation.RpcService;
import org.simple.rpc.common.domain.config.ProviderServerConfig;
import org.simple.rpc.common.domain.RpcRequest;
import org.simple.rpc.common.domain.RpcResponse;
import org.simple.rpc.protocol.MessageDecoder;
import org.simple.rpc.protocol.MessageEncoder;
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
	private ProviderServerConfig providerServerConfig;

	/**
	 * Netty Boss线程 Group
	 */
	private EventLoopGroup bossGroup   = null;
	/**
	 * Netty 工作线程 Group
	 */
	private EventLoopGroup workerGroup = null;

	@Override
	public void afterPropertiesSet() throws Exception {
		// 判断是否注入ServerConfig对象，如果注入，以该配置为准，否则自动获取
		if (providerServerConfig == null) {

		}
		// 执行启动逻辑
		start();
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

	/**
	 * 启动方法
	 */
	private void start() throws Exception {
		if (bossGroup == null && workerGroup == null) {
			bossGroup = new NioEventLoopGroup();
			workerGroup = new NioEventLoopGroup();

			// 初始化Bootstrap
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel socketChannel) throws Exception {
							socketChannel.pipeline()
									.addLast(new LengthFieldBasedFrameDecoder(65535, 0, 4, 0, 0))
									.addLast(new MessageDecoder(RpcRequest.class))
									.addLast(new MessageEncoder(RpcResponse.class))
									.addLast(new ProviderHandler(providerServiceBean));
						}
					})
					.option(ChannelOption.SO_BACKLOG, 128)
					.childOption(ChannelOption.SO_KEEPALIVE, true);

			// 绑定端口信息
			String        address       = providerServerConfig.getAddress();
			int           port          = providerServerConfig.getPort();
			ChannelFuture channelFuture = serverBootstrap.bind(address, port);
			LOGGER.info("server start in host:{},port:{}", address, port);
			channelFuture.channel().closeFuture().sync();
		}
	}

	//...............//
	public void setProviderServerConfig(ProviderServerConfig providerServerConfig) {
		this.providerServerConfig = providerServerConfig;
	}
}
