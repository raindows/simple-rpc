package org.simple.rpc.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>RPC Service 注解类</p>
 * <pre>
 *     author      XueQi
 *     date        2018/6/19
 *     email       job.xueqi@outlook.com
 * </pre>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcService {

	Class<?> value();
}
