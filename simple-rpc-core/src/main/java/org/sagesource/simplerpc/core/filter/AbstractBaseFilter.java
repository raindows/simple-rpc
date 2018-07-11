package org.sagesource.simplerpc.core.filter;

import org.sagesource.simplerpc.core.context.Context;
import org.sagesource.simplerpc.core.context.ThreadContext;

/**
 * <p></p>
 * <pre>
 *     author      XueQi
 *     date        2018/7/10
 *     email       job.xueqi@outlook.com
 * </pre>
 */
public abstract class AbstractBaseFilter implements IFilter {

	@Override
	public final void beforeFilter() {
		Context context = ThreadContext.get();
		if (context == null) {
			context = new Context();
			ThreadContext.set(context);
		}
		doBeforeFilter(context);
	}

	/**
	 * 前置拦截器逻辑
	 *
	 * @param context
	 */
	protected abstract void doBeforeFilter(Context context);

	@Override
	public final void postFilter() {
		Context context = ThreadContext.get();
		doPostFilter(context);
	}

	/**
	 * 后置拦截器逻辑
	 *
	 * @param context
	 */
	protected abstract void doPostFilter(Context context);
}
