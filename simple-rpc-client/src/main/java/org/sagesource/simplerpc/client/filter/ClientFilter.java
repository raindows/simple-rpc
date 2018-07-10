package org.sagesource.simplerpc.client.filter;

import org.sagesource.simplerpc.client.ClientContext;
import org.sagesource.simplerpc.client.ThreadClientContext;
import org.sagesource.simplerpc.core.filter.IFilter;

/**
 * <p>客户端操作 Filter</p>
 * <pre>
 *     author      XueQi
 *     date        2018/7/9
 *     email       job.xueqi@outlook.com
 * </pre>
 */
public abstract class ClientFilter implements IFilter {

	@Override
	public final void beforeFilter() {
		ClientContext clientContext = (ClientContext) ThreadClientContext.get();
		if (clientContext == null) ThreadClientContext.set(clientContext);
		doBeforeFilter(clientContext);
	}

	@Override
	public void postFilter() {
		ClientContext clientContext = (ClientContext) ThreadClientContext.get();
		doPostFilter(clientContext);
	}

	/**
	 * 执行前置过滤器逻辑
	 *
	 * @param clientContext
	 */
	protected abstract void doBeforeFilter(ClientContext clientContext);

	/**
	 * 执行后置过滤器逻辑
	 *
	 * @param clientContext
	 */
	protected abstract void doPostFilter(ClientContext clientContext);
}
