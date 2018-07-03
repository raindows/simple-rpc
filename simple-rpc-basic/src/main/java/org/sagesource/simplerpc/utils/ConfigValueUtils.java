package org.sagesource.simplerpc.utils;

import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>配置值工具类</p>
 * <pre>
 *     author      XueQi
 *     date        2018/7/3
 *     email       job.xueqi@outlook.com
 * </pre>
 */
public class ConfigValueUtils {

	/**
	 * 获取 JVM 的参数值
	 *
	 * @param property
	 * @param defaultValue
	 * @return
	 */
	public static String getJvmPropertyValue(String property, String defaultValue) {
		try {
			String value = System.getProperty(property);
			if (value == null) return defaultValue;
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			return defaultValue;
		}
	}

	/**
	 * 获取环境变量的参数值
	 *
	 * @param property
	 * @param defaultValue
	 * @return
	 */
	public static String getEnvPropertyValue(String property, String defaultValue) {
		try {
			String value = System.getenv(property);
			if (value == null) return defaultValue;
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			return defaultValue;
		}
	}

	// 缓存配置文件对象
	private static final ConcurrentHashMap<String, Properties> cachePropertiesMapper = new ConcurrentHashMap<>();
	private static       Object                                LOCK_OBJ              = new Object();

	/**
	 * 获取配置文件的参数值
	 *
	 * @param propertiesFile
	 * @param property
	 * @param defaultValue
	 * @return
	 */
	public static String getPropertiesValue(String propertiesFile, String property, String defaultValue) {
		Properties properties = cachePropertiesMapper.get(propertiesFile);
		if (properties == null) {
			synchronized (LOCK_OBJ) {
				InputStream in = null;
				try {
					in = ConfigValueUtils.class.getResourceAsStream(propertiesFile);
					properties.load(in);
					cachePropertiesMapper.putIfAbsent(propertiesFile, properties);
				} catch (Exception e) {
					e.printStackTrace();
					return defaultValue;
				} finally {
					try {
						if (in != null) in.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		return properties.getProperty(property, defaultValue);
	}
}
