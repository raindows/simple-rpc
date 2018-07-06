package org.sagesource.simplerpc.basic.core.zookeeper;

import com.alibaba.fastjson.JSON;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.sagesource.simplerpc.basic.entity.ServerInfo;
import org.sagesource.simplerpc.basic.utils.ConfigValueUtils;
import org.sagesource.simplerpc.basic.core.loadbalance.LoadBalanceFactory;
import org.sagesource.simplerpc.basic.core.zookeeper.utils.ZKConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>服务地址Agent</p>
 * <pre>
 *     author      XueQi
 *     date        2018/7/6
 *     email       job.xueqi@outlook.com
 * </pre>
 */
public class ServiceAddressProviderAgent implements ZKConstants {
	private static Logger LOGGER = LoggerFactory.getLogger(ServiceAddressProviderAgent.class);

	/**
	 * 服务名称
	 */
	private String serviceName;

	/**
	 * 服务版本号
	 */
	private String version;

	/**
	 * 锁对象 因为每一个服务是生成同一个动态代理 所以不需要 static 的锁对象
	 */
	private Object LOCK_OBJ = new Object();

	/**
	 * 服务提供方信息
	 */
	private final List<ServerInfo> serverInfoList = new ArrayList<>();

	/**
	 * 将调用的服务列表缓存，当serverInfoList为空的时候，可以尝试从缓存列表中获取机器信息
	 */
	private final Set<ServerInfo> traceCacheServer = new HashSet<>();

	public ServiceAddressProviderAgent buildServiceName(String serviceName) {
		this.serviceName = serviceName;
		return this;
	}

	public ServiceAddressProviderAgent buildVersion(String version) {
		this.version = version;
		return this;
	}

	/**
	 * 初始化
	 */
	public void init() {
		// 获取 zkClient, 通过 env 获取 zk 的连接字符串
		String zkConnStr = ConfigValueUtils.getEnvPropertyValue(SIMEPLE_RPC_ZK, null);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(">>>>>>>> try create zk client: {} <<<<<<<<", zkConnStr);
		}

		CuratorFramework zkClient = ZookeeperClientFactory.createClient(zkConnStr);

		// 构建服务节点
		String servicePath = builtServicePath();
		buildPathChildrenCache(zkClient, servicePath);
	}

	/**
	 * 构建子节点的监听事件
	 *
	 * @param client
	 * @param servicePath
	 */
	private void buildPathChildrenCache(final CuratorFramework client, String servicePath) {
		PathChildrenCache cachedPath = new PathChildrenCache(client, servicePath, true);
		cachedPath.getListenable().addListener(new PathChildrenCacheListener() {
			@Override
			public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
				// 节点事件
				PathChildrenCacheEvent.Type eventType = pathChildrenCacheEvent.getType();
				switch (eventType) {
					case CONNECTION_RECONNECTED:
						LOGGER.info(">>>>>>>> connection is reconnection <<<<<<<<");
						break;
					case CONNECTION_SUSPENDED:
						LOGGER.info(">>>>>>>> connection is supended <<<<<<<<");
						break;
					case CONNECTION_LOST:
						LOGGER.warn(">>>>>>>> connection lost, waiting.... <<<<<<<<");
						break;
					case INITIALIZED:
						LOGGER.warn(">>>>>>>> connection init.... <<<<<<<<");
						break;
					default:
				}

				// 当任何节点的事件变动，简单处理，rebuild并清空serverInfoList
				cachedPath.rebuild();
				rebuild();
			}

			// rebuild
			protected void rebuild() throws Exception {
				List<ChildData> childDataList = cachedPath.getCurrentData();
				if (childDataList == null || childDataList.isEmpty()) {
					// service 节点下没有机器数据，可能是服务端和 zk 的连接断开而已
					return;
				}

				// 获取新的机器列表
				List<ServerInfo> currentServerInfoList = new ArrayList<>();
				for (ChildData childData : childDataList) {
					String nodePath = childData.getPath();
					if (LOGGER.isDebugEnabled())
						LOGGER.debug(">>>>>>>> serviceName:{} version:{} path:{} <<<<<<<<", serviceName, version, nodePath);

					// 获取节点值
					String     nodePathVal = new String(childData.getData());
					ServerInfo serverInfo  = JSON.parseObject(nodePathVal, ServerInfo.class);
					currentServerInfoList.add(serverInfo);
				}
				// 将最新的机器列表 添加到节点列表
				synchronized (LOCK_OBJ) {
					serverInfoList.clear();
					serverInfoList.addAll(currentServerInfoList);
				}
			}
		});
	}

	/**
	 * 获取服务的机器列表
	 *
	 * @return
	 */
	public ServerInfo findServerAddressList() {
		if (this.serverInfoList == null || this.serverInfoList.isEmpty()) {

		}

		ServerInfo serverInfo = LoadBalanceFactory.getLoadBalanceEngine().availableServerInfo(this.serviceName, this.version, this.serverInfoList);
		traceCacheServer.add(serverInfo);
		return serverInfo;
	}

	/**
	 * 获取服务的 ZK 节点信息
	 *
	 * @return
	 */
	private String builtServicePath() {
		return "/" + this.serviceName + "/" + this.version;
	}

	public static void main(String[] args) {
		ServerInfo serverInfo = new ServerInfo()
				.buildServiceName("org.sagesource.test.api.HelloWorldService")
				.buildServiceVersion("1.0.0")
				.buildServerIP("127.0.0.1")
				.buildPort(8090)
				.buildWeight(1);

		System.out.println(JSON.toJSONString(serverInfo));
	}

}
