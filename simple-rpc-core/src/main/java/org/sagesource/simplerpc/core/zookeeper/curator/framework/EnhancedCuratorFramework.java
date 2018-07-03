package org.sagesource.simplerpc.core.zookeeper.curator.framework;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.sagesource.simplerpc.core.zookeeper.curator.EnhancedChildrenAddRemoveListener;
import org.sagesource.simplerpc.core.zookeeper.curator.EnhancedNodeCacheListener;
import org.sagesource.simplerpc.core.zookeeper.curator.EnhancedPathChildrenCacheListener;

import java.util.List;

/**
 * <p>CuratorFramework自定义实现</p>
 * <pre>
 *     author      XueQi
 *     date        2018/7/3
 *     email       job.xueqi@outlook.com
 * </pre>
 */
public interface EnhancedCuratorFramework extends CuratorFramework {
	/**
	 * watch node's change
	 *
	 * @param path
	 * @param listener
	 * @return
	 * @throws Exception
	 */
	public byte[] watchNode(String path, EnhancedNodeCacheListener listener) throws Exception;

	/**
	 * watch children nodes' add,remove,update
	 *
	 * @param path
	 * @param listener
	 * @return
	 * @throws Exception
	 */
	public List<ChildData> watchChildrenNodes(String path, EnhancedPathChildrenCacheListener listener) throws Exception;

	/**
	 * watch children nodes' add,remove
	 *
	 * @param path
	 * @param listener
	 * @return
	 * @throws Exception
	 */
	public void watchChildrenAddRemove(String path, EnhancedChildrenAddRemoveListener listener) throws Exception;
}
