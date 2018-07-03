package org.sagesource.simplerpc.core.zookeeper.curator.framework;

import org.apache.curator.framework.imps.CuratorFrameworkImpl;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.sagesource.simplerpc.core.zookeeper.curator.EnhancedChildrenAddRemoveListener;
import org.sagesource.simplerpc.core.zookeeper.curator.EnhancedCuratorFrameworkFactory;
import org.sagesource.simplerpc.core.zookeeper.curator.EnhancedNodeCacheListener;
import org.sagesource.simplerpc.core.zookeeper.curator.EnhancedPathChildrenCacheListener;

import java.util.List;

/**
 * <p> 客户端实现类 </p>
 * <pre>
 *     author      XueQi
 *     date        2018/7/3
 *     email       job.xueqi@outlook.com
 * </pre>
 */
public class EnhancedCuratorFrameworkImpl extends CuratorFrameworkImpl implements EnhancedCuratorFramework {
	public EnhancedCuratorFrameworkImpl(EnhancedCuratorFrameworkFactory.Builder builder) {
		super(builder.toCuratorBuilder());
	}

	@Override
	public byte[] watchNode(String path, EnhancedNodeCacheListener listener) throws Exception {
		return new byte[0];
	}

	@Override
	public List<ChildData> watchChildrenNodes(String path, EnhancedPathChildrenCacheListener listener) throws Exception {
		return null;
	}

	@Override
	public void watchChildrenAddRemove(String path, EnhancedChildrenAddRemoveListener listener) throws Exception {

	}
}
