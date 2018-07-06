package org.sagesource.simplerpc.basic.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * <p>IP 工具类</p>
 * <pre>
 *     author      XueQi
 *     date        2018/6/29
 *     email       job.xueqi@outlook.com
 * </pre>
 */
public class InetSocketAddressUtils {
	private static Logger LOGGER = LoggerFactory.getLogger(InetSocketAddressUtils.class);

	// 默认 IP 地址
	private static final String DEFAULT_LOCAL_IP = "127.0.0.1";

	// 本地 IP 地址
	private static String localIP;

	static {
		initLocalIP();
	}

	/**
	 * 初始化本地 IP 地址
	 */
	private static void initLocalIP() {
		try {
			localIP = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			LOGGER.error("Init Local IP Error.", e);
		}
	}

	public static String getLocalIP() {
		return localIP;
	}
}
