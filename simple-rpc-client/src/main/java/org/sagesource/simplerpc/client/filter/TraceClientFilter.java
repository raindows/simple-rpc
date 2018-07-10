package org.sagesource.simplerpc.client.filter;

import org.sagesource.simplerpc.basic.Constants;
import org.sagesource.simplerpc.client.ClientContext;
import org.sagesource.simplerpc.core.trace.ThreadTrace;
import org.sagesource.simplerpc.core.trace.TraceFun;
import org.slf4j.MDC;

/**
 * <p>客户端调用 TraceFilter </p>
 * <pre>
 *     author      XueQi
 *     date        2018/7/9
 *     email       job.xueqi@outlook.com
 * </pre>
 */
public class TraceClientFilter extends ClientFilter implements Constants {
	@Override
	protected void doBeforeFilter(ClientContext clientContext) {
		// 获取本次请求的 traceId 如果不存在，则创建
		Long traceId = ThreadTrace.get();
		if (traceId == null || traceId == 0) {
			traceId = TraceFun.getTrace();
			ThreadTrace.set(traceId);
			MDC.put(TRACE_ID, String.valueOf(traceId));
		}
	}

	@Override
	protected void doPostFilter(ClientContext clientContext) {

	}
}
