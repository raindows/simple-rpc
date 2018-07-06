package org.sagesource.simplerpc.basic.core.zookeeper.utils;

/**
 * <p>ZK 常量配置</p>
 * <pre>
 *     author      XueQi
 *     date        2018/7/3
 *     email       job.xueqi@outlook.com
 * </pre>
 */
public interface ZKConstants {
	public static final String DEFAULT_CHARSET = "utf-8";
	public static final String COMMA           = ",";
	public static final String DOT             = ".";

	public static final String DEFAULT_NAMESPACES = "default";

	public static final int DEFAULT_SESSION_TIMEOUT_MS    = 60 * 1000;
	public static final int DEFAULT_CONNECTION_TIMEOUT_MS = 3 * 1000;
	public static final int DEFAULT_RETRY_TIMES           = 1;
	public static final int DEFAULT_SLEEPMS_BETWEENRETRY  = 30;

	//.... 配置 KEY ....//
	public static final String SIMEPLE_RPC_ZK              = "SIMEPLE_RPC_ZK";
	public static final String SIMPLERPC_STATIC_ROUTER     = "SIMPLERPC_STATIC_ROUTER";
	public static final String SIMPLERPC_STATIC_ROUTER_JVM = "simplerpc.static.router";
}
