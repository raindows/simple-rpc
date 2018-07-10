package org.sagesource.simplerpc.client;

import org.sagesource.simplerpc.core.Context;

/**
 * <p></p>
 * <pre>
 *     author      XueQi
 *     date        2018/7/9
 *     email       job.xueqi@outlook.com
 * </pre>
 */
public class ThreadClientContext {
	private static ThreadLocal<Context> clientContextThreadLocal = new InheritableThreadLocal<>();

	public static void set(Context context) {
		clientContextThreadLocal.set(context);
	}

	public static Context get() {
		return clientContextThreadLocal.get();
	}

	public static void remove() {
		clientContextThreadLocal.remove();
	}
}
