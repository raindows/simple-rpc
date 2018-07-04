package org.sagesource.simplerpc.core.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.sagesource.simplerpc.core.zookeeper.utils.ZKConstants;

import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>Zookeeper客户端工厂</p>
 * <pre>
 *     author      XueQi
 *     date        2018/7/3
 *     email       job.xueqi@outlook.com
 * </pre>
 */
public class ZookeeperClientFactory implements ZKConstants {

	// 连接客户端缓存 key - connStr
	private static final ConcurrentHashMap<String, CuratorFramework> cacheClientMapper = new ConcurrentHashMap<>();

	public static CuratorFramework createClient(String connectionStr) {
		return createClient(connectionStr, DEFAULT_SESSION_TIMEOUT_MS, DEFAULT_CONNECTION_TIMEOUT_MS);
	}

	public static CuratorFramework createClient(String connectionStr, int sessionTimeout, int connectionTimeout) {
		return createClient(connectionStr, sessionTimeout, connectionTimeout, DEFAULT_CHARSET);
	}

	public static CuratorFramework createClient(String connectionStr, int sessionTimeout, int connectionTimeout, String namespase) {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(DEFAULT_SLEEPMS_BETWEENRETRY, DEFAULT_RETRY_TIMES);
		return createClient(connectionStr, sessionTimeout, connectionTimeout, namespase, retryPolicy);
	}

	public static CuratorFramework createClient(String connectionStr, int sessionTimeout, int connectionTimeout, String namespase, RetryPolicy retryPolicy) {
		CuratorFramework client = cacheClientMapper.get(connectionStr);
		if (client == null) {
			synchronized (ZookeeperClientFactory.class) {
				client = cacheClientMapper.get(connectionStr);
				if (client == null) {
					CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder();
					client = builder.connectString(connectionStr).sessionTimeoutMs(sessionTimeout).connectionTimeoutMs(connectionTimeout)
							.canBeReadOnly(true).namespace(namespase).retryPolicy(retryPolicy).defaultData(null)
							.build();
					cacheClientMapper.putIfAbsent(connectionStr, client);
				}
			}
		}
		return client;
	}
}
