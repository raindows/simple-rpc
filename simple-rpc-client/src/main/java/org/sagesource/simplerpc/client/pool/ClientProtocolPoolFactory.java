package org.sagesource.simplerpc.client.pool;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.thrift.protocol.TProtocol;
import org.sagesource.simplerpc.core.protocol.TProtocolPooledFactory;
import org.sagesource.simplerpc.entity.ProtocolPoolConfig;
import org.sagesource.simplerpc.entity.ServerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>客户端连接池创建工厂</p>
 * <pre>
 *     author      XueQi
 *     date        2018/6/29
 *     email       job.xueqi@outlook.com
 * </pre>
 */
public class ClientProtocolPoolFactory {
	private static Logger LOGGER = LoggerFactory.getLogger(ClientProtocolPoolFactory.class);

	private ClientProtocolPoolFactory() {
	}

	private static class SingleClass {
		private static final ClientProtocolPoolFactory INSTANCE = new ClientProtocolPoolFactory();
	}

	public static ClientProtocolPoolFactory getInstance() {
		return SingleClass.INSTANCE;
	}

	// 服务 - 连接池 缓存
	private static final ConcurrentHashMap<String, ObjectPool<TProtocol>> cachePoolMapper = new ConcurrentHashMap<>();
	// 锁对象
	private static       Object                                           LOCK_OBJ        = new Object();

	/**
	 * 获取 初始化服务连接池
	 *
	 * @param serverInfo
	 * @return
	 */
	public void create(ProtocolPoolConfig poolConfig, ServerInfo serverInfo) {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Create Client Protocol Pool. PoolConfig=[{}],ServerInfo=[{}]", ReflectionToStringBuilder.toString(poolConfig), ReflectionToStringBuilder.toString(serverInfo));

		// 服务名称,尝试从缓存中获取已存在的连接池
		String                serviceName = serverInfo.getServiceName();
		ObjectPool<TProtocol> cachePool   = cachePoolMapper.get(serviceName);
		if (cachePool == null) {

			// 创建线程池,防止同一服务端的多个服务，重复创建连接
			synchronized (LOCK_OBJ) {
				cachePool = cachePoolMapper.get(serviceName);
				if (cachePool == null) {
					ObjectPool<TProtocol> pool = new GenericObjectPool<>(
							new TProtocolPooledFactory()
									.buildKeepAlive(poolConfig.getKeepAlive())
									.buildTimeout(poolConfig.getTimeout())
									.buildServerInfo(serverInfo), poolConfig);
					cachePoolMapper.put(serviceName, pool);
				}
			}
		}
	}

	/**
	 * 获取连接池
	 *
	 * @param serverInfo
	 * @return
	 */
	public ObjectPool<TProtocol> getProtocolPool(ServerInfo serverInfo) {
		return cachePoolMapper.get(serverInfo.getServiceName());
	}
}
