package org.sagesource.test.api.impl;

import org.apache.thrift.TException;
import org.sagesource.test.api.HelloWorldService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wangxiaoyan on 2018/6/24.
 */
public class HelloWorldServiceImpl implements HelloWorldService.Iface {
	private static Logger LOGGER = LoggerFactory.getLogger(HelloWorldServiceImpl.class);

	@Override
	public String sayHello(String username) throws TException {
		LOGGER.info(">>>>>>>> server receive:{}", username);
		return "Hi," + username + " welcome";
	}

}
