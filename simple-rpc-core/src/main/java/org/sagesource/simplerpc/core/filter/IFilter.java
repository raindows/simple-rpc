package org.sagesource.simplerpc.core.filter;

/**
 * <p> 过滤器接口 </p>
 * <pre>
 *     author      XueQi
 *     date        2018/7/9
 *     email       job.xueqi@outlook.com
 * </pre>
 */
public interface IFilter {

	/**
	 * 前置过滤器
	 */
	void beforeFilter();

	void postFilter();
}
