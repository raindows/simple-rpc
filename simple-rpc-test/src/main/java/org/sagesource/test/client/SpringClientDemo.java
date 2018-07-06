package org.sagesource.test.client;

import org.apache.thrift.TException;
import org.junit.Test;
import org.junit.runner.RunWith;
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
	public void test() throws TException {
		/*System.setProperty("SIMPLERPC_STATIC_ROUTER", "*=127.0.0.1:8090");*/
		System.out.println(helloWorldService.sayHello("sage"));
	}
}
