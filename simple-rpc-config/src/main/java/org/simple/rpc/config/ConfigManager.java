package org.simple.rpc.config;

/**
 * <p>配置管理接口</p>
 * <pre>
 *     author      XueQi
 *     date        2018/6/21
 *     email       job.xueqi@outlook.com
 * </pre>
 */
public interface ConfigManager {

	/**
	 * 获取字符串配置
	 *
	 * @param key
	 * @return
	 */
	public String getStringValue(String key);

	public String getStringValue(String key, String defaultValue);

	/**
	 * 获取int值配置
	 *
	 * @param key
	 * @return
	 */
	public int getIntValue(String key);

	public int getIntValue(String key, int defaultValue);
}
