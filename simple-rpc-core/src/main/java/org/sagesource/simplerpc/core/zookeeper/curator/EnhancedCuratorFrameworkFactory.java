package org.sagesource.simplerpc.core.zookeeper.curator;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.ensemble.EnsembleProvider;
import org.apache.curator.ensemble.fixed.FixedEnsembleProvider;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.framework.api.CompressionProvider;
import org.apache.curator.framework.imps.DefaultACLProvider;
import org.apache.curator.framework.imps.GzipCompressionProvider;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.DefaultZookeeperFactory;
import org.apache.curator.utils.ZookeeperFactory;
import org.sagesource.simplerpc.core.zookeeper.curator.framework.EnhancedCuratorFramework;
import org.sagesource.simplerpc.core.zookeeper.curator.framework.EnhancedCuratorFrameworkImpl;
import org.sagesource.simplerpc.core.zookeeper.curator.framework.EnhancedCuratorTempFramework;
import org.sagesource.simplerpc.core.zookeeper.curator.framework.EnhancedCuratorTempFrameworkImpl;
import org.sagesource.simplerpc.core.zookeeper.utils.ZKConstants;
import org.sagesource.simplerpc.exception.EnhancedCuratorException;
import org.sagesource.simplerpc.utils.IPUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * <p>EnhancedCuratorFramework Factory</p>
 * <pre>
 *     author      XueQi
 *     date        2018/7/3
 *     email       job.xueqi@outlook.com
 * </pre>
 */
public class EnhancedCuratorFrameworkFactory implements ZKConstants {

	private static Map<String, EnhancedCuratorFramework> cachedCuratorFrameworkMap = new ConcurrentHashMap<String, EnhancedCuratorFramework>();

	/**
	 * 根据连接字符串创建 zk 客户端
	 *
	 * @param connectionStr
	 * @return
	 * @throws Exception
	 */
	public static EnhancedCuratorFramework getStartedClientByConnectString(String connectionStr) throws EnhancedCuratorException {
		// FIXME 支持环境变量获取
		int sessionTimeoutMs    = DEFAULT_SESSION_TIMEOUT_MS;
		int connectionTimeoutMs = DEFAULT_CONNECTION_TIMEOUT_MS;
		return getStartedClientByConnectString(connectionStr, sessionTimeoutMs, connectionTimeoutMs);
	}

	public static EnhancedCuratorFramework getStartedClientByConnectString(String connectString, int sessionTimeoutMs,
	                                                                       int connectionTimeoutMs) throws EnhancedCuratorException {
		int retryTimes     = DEFAULT_RETRY_TIMES;
		int sleepMsBetween = DEFAULT_SLEEPMS_BETWEENRETRY;
		// 重试配置
		RetryPolicy retryPolicy = new RetryNTimes(retryTimes, sleepMsBetween);
		return getSynchronizedClientByPartitionConnectString(connectString, sessionTimeoutMs, connectionTimeoutMs, retryPolicy);
	}

	/**
	 * 创建客户端
	 *
	 * @param connectString
	 * @param sessionTimeoutMs
	 * @param connectionTimeoutMs
	 * @param retryPolicy
	 * @return
	 * @throws Exception
	 */
	public static EnhancedCuratorFramework getSynchronizedClientByPartitionConnectString(final String connectString,
	                                                                                     final int sessionTimeoutMs,
	                                                                                     final int connectionTimeoutMs,
	                                                                                     final RetryPolicy retryPolicy) throws EnhancedCuratorException {
		// 校验连接字符串是否合法
		if (StringUtils.isEmpty(connectString)) {
			throw new EnhancedCuratorException("初始化 Zookeeper 连接错误，请检查 ZK 连接信息是否存在");
		}

		// 获取缓存客户端
		EnhancedCuratorFramework enhancedCuratorFramework = cachedCuratorFrameworkMap.get(connectString);
		if (enhancedCuratorFramework == null) {
			synchronized (EnhancedCuratorFrameworkFactory.class) {
				enhancedCuratorFramework = builder().connectString(connectString).sessionTimeoutMs(sessionTimeoutMs)
						.connectionTimeoutMs(connectionTimeoutMs).retryPolicy(retryPolicy).namespace(DEFAULT_NAMESPACES).build();
				enhancedCuratorFramework.start();
				// 将客户端保存到缓存中
				cachedCuratorFrameworkMap.put(connectString, enhancedCuratorFramework);
			}
		}
		return enhancedCuratorFramework;
	}

	//.....//
	// 本地 IP 地址
	private static final byte[]                  LOCAL_ADDRESS                 = IPUtils.getLocalIP().getBytes();
	private static final CompressionProvider     DEFAULT_COMPRESSION_PROVIDER  = new GzipCompressionProvider();
	private static final DefaultZookeeperFactory DEFAULT_ZOOKEEPER_FACTORY     = new DefaultZookeeperFactory();
	private static final DefaultACLProvider      DEFAULT_ACL_PROVIDER          = new DefaultACLProvider();
	private static final long                    DEFAULT_INACTIVE_THRESHOLD_MS = (int) TimeUnit.MINUTES.toMillis(3);

	/**
	 * Return a new builder that builds a CuratorFramework
	 *
	 * @return new builder
	 */
	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private EnsembleProvider ensembleProvider;
		private int sessionTimeoutMs    = ZKConstants.DEFAULT_SESSION_TIMEOUT_MS;
		private int connectionTimeoutMs = ZKConstants.DEFAULT_CONNECTION_TIMEOUT_MS;
		private RetryPolicy retryPolicy;
		private ThreadFactory threadFactory = null;
		private String namespace;
		private String              authScheme          = null;
		private byte[]              authValue           = null;
		private byte[]              defaultData         = LOCAL_ADDRESS;
		private CompressionProvider compressionProvider = DEFAULT_COMPRESSION_PROVIDER;
		private ZookeeperFactory    zookeeperFactory    = DEFAULT_ZOOKEEPER_FACTORY;
		private ACLProvider         aclProvider         = DEFAULT_ACL_PROVIDER;
		private boolean             canBeReadOnly       = false;

		/**
		 * Apply the current values and build a new CuratorFramework
		 *
		 * @return new CuratorFramework
		 */
		public EnhancedCuratorFramework build() {
			return new EnhancedCuratorFrameworkImpl(this);
		}

		/**
		 * Apply the current values and build a new temporary CuratorFramework. Temporary CuratorFramework instances are
		 * meant for single requests to ZooKeeper ensembles over a failure prone network such as a WAN. The APIs
		 * available from {@link CuratorTempFramework} are limited. Further, the connection will be closed after 3
		 * minutes of inactivity.
		 *
		 * @return temp instance
		 */
		public EnhancedCuratorTempFramework buildTemp() {
			return buildTemp(DEFAULT_INACTIVE_THRESHOLD_MS, TimeUnit.MILLISECONDS);
		}

		/**
		 * Apply the current values and build a new temporary CuratorFramework. Temporary CuratorFramework instances are
		 * meant for single requests to ZooKeeper ensembles over a failure prone network such as a WAN. The APIs
		 * available from {@link CuratorTempFramework} are limited. Further, the connection will be closed after
		 * <code>inactiveThresholdMs</code> milliseconds of inactivity.
		 *
		 * @param inactiveThreshold number of milliseconds of inactivity to cause connection close
		 * @param unit              threshold unit
		 * @return temp instance
		 */
		public EnhancedCuratorTempFramework buildTemp(long inactiveThreshold, TimeUnit unit) {
			return new EnhancedCuratorTempFrameworkImpl(this, unit.toMillis(inactiveThreshold));
		}

		/**
		 * Add connection authorization
		 *
		 * @param scheme the scheme
		 * @param auth   the auth bytes
		 * @return this
		 */
		public Builder authorization(String scheme, byte[] auth) {
			this.authScheme = scheme;
			this.authValue = (auth != null) ? Arrays.copyOf(auth, auth.length) : null;
			return this;
		}

		/**
		 * Set the list of servers to connect to. IMPORTANT: use either this or
		 * {@link #ensembleProvider(EnsembleProvider)} but not both.
		 *
		 * @param connectString list of servers to connect to
		 * @return this
		 */
		public Builder connectString(String connectString) {
			ensembleProvider = new FixedEnsembleProvider(connectString);
			return this;
		}

		/**
		 * Set the list ensemble provider. IMPORTANT: use either this or {@link #connectString(String)} but not both.
		 *
		 * @param ensembleProvider the ensemble provider to use
		 * @return this
		 */
		public Builder ensembleProvider(EnsembleProvider ensembleProvider) {
			this.ensembleProvider = ensembleProvider;
			return this;
		}

		/**
		 * Sets the data to use when {@link PathAndBytesable#forPath(String)} is used. This is useful for debugging
		 * purposes. For example, you could set this to be the IP of the client.
		 *
		 * @param defaultData new default data to use
		 * @return this
		 */
		public Builder defaultData(byte[] defaultData) {
			this.defaultData = (defaultData != null) ? Arrays.copyOf(defaultData, defaultData.length) : null;
			return this;
		}

		/**
		 * As ZooKeeper is a shared space, users of a given cluster should stay within a pre-defined namespace. If a
		 * namespace is set here, all paths will get pre-pended with the namespace
		 *
		 * @param namespace the namespace
		 * @return this
		 */
		public Builder namespace(String namespace) {
			this.namespace = namespace;
			return this;
		}

		/**
		 * @param sessionTimeoutMs session timeout
		 * @return this
		 */
		public Builder sessionTimeoutMs(int sessionTimeoutMs) {
			this.sessionTimeoutMs = sessionTimeoutMs;
			return this;
		}

		/**
		 * @param connectionTimeoutMs connection timeout
		 * @return this
		 */
		public Builder connectionTimeoutMs(int connectionTimeoutMs) {
			this.connectionTimeoutMs = connectionTimeoutMs;
			return this;
		}

		/**
		 * @param retryPolicy retry policy to use
		 * @return this
		 */
		public Builder retryPolicy(RetryPolicy retryPolicy) {
			this.retryPolicy = retryPolicy;
			return this;
		}

		/**
		 * @param threadFactory thread factory used to create Executor Services
		 * @return this
		 */
		public Builder threadFactory(ThreadFactory threadFactory) {
			this.threadFactory = threadFactory;
			return this;
		}

		/**
		 * @param compressionProvider the compression provider
		 * @return this
		 */
		public Builder compressionProvider(CompressionProvider compressionProvider) {
			this.compressionProvider = compressionProvider;
			return this;
		}

		/**
		 * @param zookeeperFactory the zookeeper factory to use
		 * @return this
		 */
		public Builder zookeeperFactory(ZookeeperFactory zookeeperFactory) {
			this.zookeeperFactory = zookeeperFactory;
			return this;
		}

		/**
		 * @param aclProvider a provider for ACLs
		 * @return this
		 */
		public Builder aclProvider(ACLProvider aclProvider) {
			this.aclProvider = aclProvider;
			return this;
		}

		/**
		 * @param canBeReadOnly if true, allow ZooKeeper client to enter read only mode in case of a network partition.
		 *                      See {@link ZooKeeper#ZooKeeper(String, int, Watcher, long, byte[], boolean)} for details
		 * @return this
		 */
		public Builder canBeReadOnly(boolean canBeReadOnly) {
			this.canBeReadOnly = canBeReadOnly;
			return this;
		}

		public ACLProvider getAclProvider() {
			return aclProvider;
		}

		public ZookeeperFactory getZookeeperFactory() {
			return zookeeperFactory;
		}

		public CompressionProvider getCompressionProvider() {
			return compressionProvider;
		}

		public ThreadFactory getThreadFactory() {
			return threadFactory;
		}

		public EnsembleProvider getEnsembleProvider() {
			return ensembleProvider;
		}

		public int getSessionTimeoutMs() {
			return sessionTimeoutMs;
		}

		public int getConnectionTimeoutMs() {
			return connectionTimeoutMs;
		}

		public RetryPolicy getRetryPolicy() {
			return retryPolicy;
		}

		public String getNamespace() {
			return namespace;
		}

		public String getAuthScheme() {
			return authScheme;
		}

		public byte[] getAuthValue() {
			return (authValue != null) ? Arrays.copyOf(authValue, authValue.length) : null;
		}

		public byte[] getDefaultData() {
			return defaultData;
		}

		public boolean canBeReadOnly() {
			return canBeReadOnly;
		}

		private Builder() {
		}

		public CuratorFrameworkFactory.Builder toCuratorBuilder() {
			CuratorFrameworkFactory.Builder curatorBuilder = CuratorFrameworkFactory.builder();
			curatorBuilder.aclProvider(this.aclProvider).canBeReadOnly(canBeReadOnly)
					.compressionProvider(compressionProvider).connectionTimeoutMs(connectionTimeoutMs)
					.defaultData(defaultData).ensembleProvider(ensembleProvider).namespace(namespace)
					.retryPolicy(retryPolicy).sessionTimeoutMs(sessionTimeoutMs).threadFactory(threadFactory)
					.zookeeperFactory(zookeeperFactory);
			return curatorBuilder;
		}

	}
}
