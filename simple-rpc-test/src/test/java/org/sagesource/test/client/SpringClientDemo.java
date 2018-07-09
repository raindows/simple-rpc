package org.sagesource.test.client;

import org.apache.thrift.TException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sagesource.simplerpc.core.trace.ThreadTrace;
import org.sagesource.simplerpc.core.trace.TraceFun;
import org.sagesource.test.api.HelloWorldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * <p></p>
 * <pre>
 *     author      XueQi
 *     date        2018/7/1
 *     email       job.xueqi@outlook.com
 * </pre>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:spring-client.xml"})
public class SpringClientDemo {

	@Autowired
	@Qualifier("helloWorldService")
	private HelloWorldService.Iface helloWorldService;

	@Test
	public void test() throws Exception {
		for (int i = 0; i < 1; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						ThreadTrace.set(TraceFun.getTrace());
						System.out.println(helloWorldService.sayHello("sage"));
					} catch (TException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
		while (true) ;
		/*ClientProtocolPoolFactory.close();
		ServiceAddressProviderAgent.close();
		ZookeeperClientFactory.close();*/
	}

	@Test
	public void test2() throws TException {
		ThreadTrace.set(TraceFun.getTrace());
		System.out.println(helloWorldService.sayHello("sage"));
	}
}
