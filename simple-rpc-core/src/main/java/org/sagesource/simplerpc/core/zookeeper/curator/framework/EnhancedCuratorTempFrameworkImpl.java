package org.sagesource.simplerpc.core.zookeeper.curator.framework;

import org.apache.curator.framework.imps.CuratorTempFrameworkImpl;
import org.sagesource.simplerpc.core.zookeeper.curator.EnhancedCuratorFrameworkFactory;

/**
 * <p></p>
 * <pre>
 *     author      XueQi
 *     date        2018/7/3
 *     email       job.xueqi@outlook.com
 * </pre>
 */
public class EnhancedCuratorTempFrameworkImpl extends CuratorTempFrameworkImpl implements EnhancedCuratorTempFramework {
	public EnhancedCuratorTempFrameworkImpl(EnhancedCuratorFrameworkFactory.Builder factory, long inactiveThresholdMs) {
		super(factory.toCuratorBuilder(), inactiveThresholdMs);
	}
}
