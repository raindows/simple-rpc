package org.sagesource.test.provider;

import org.junit.After;
import org.junit.Test;
import org.sagesource.simplerpc.core.zookeeper.ZookeeperClientFactory;
import org.sagesource.simplerpc.provider.SimpleServiceProviderFactory;
import org.sagesource.test.api.impl.HelloWorldServiceImpl;

/**
 * <p></p>
 * <pre>
 *     author      XueQi
 *     date        2018/7/13
 *     email       sage.xue@vipshop.com
 * </pre>
 */
public class SimpleServiceProviderTest {

	@Test
	public void test() throws Exception {
		SimpleServiceProviderFactory simpleServiceProviderFactory = new SimpleServiceProviderFactory(8999, new HelloWorldServiceImpl(), "1.0.0");
		simpleServiceProviderFactory.createServiceProvider();

		Thread.sleep(10000);
	}

	@After
	public void after() {
		SimpleServiceProviderFactory.close();
		ZookeeperClientFactory.close();
	}
}
