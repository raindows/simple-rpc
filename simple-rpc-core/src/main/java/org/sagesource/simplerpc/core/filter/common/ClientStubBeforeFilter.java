package org.sagesource.simplerpc.core.filter.common;

import org.sagesource.simplerpc.core.context.Context;
import org.sagesource.simplerpc.core.filter.AbstractBaseFilter;
import org.sagesource.simplerpc.core.trace.TraceSupport;

/**
 * <p>客户端默认前置过滤器</p>
 * <pre>
 *     author      XueQi
 *     date        2018/7/11
 *     email       sage.xue@vipshop.com
 * </pre>
 */
public class ClientStubBeforeFilter extends AbstractBaseFilter {
	@Override
	protected void doBeforeFilter(Context context) {
		// 1. TraceId生成
		TraceSupport.set(null);
	}

	@Override
	protected void doPostFilter(Context context) {

	}
}
