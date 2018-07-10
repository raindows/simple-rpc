package org.sagesource.simplerpc.config;

/**
 * <p></p>
 * <pre>
 *     author      XueQi
 *     date        2018/7/10
 *     email       job.xueqi@outlook.com
 * </pre>
 */
public interface ConfigKeyConstants {

	//...... 环境变量 KEY ......//
	// 框架 ZK 连接地址
	String SIMEPLE_RPC_ZK          = "SIMEPLE_RPC_ZK";
	// 静态路由环境变量
	String SIMPLERPC_STATIC_ROUTER = "SIMPLERPC_STATIC_ROUTER";

	//...... JVM 参数 KEY ......//
	String SIMPLERPC_STATIC_ROUTER_JVM = "simplerpc.static.route";
}
