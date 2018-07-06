package org.sagesource.test.api.impl;

import org.apache.thrift.TException;
import org.sagesource.test.api.HelloWorldService;

/**
 * Created by wangxiaoyan on 2018/6/24.
 */
public class HelloWorldServiceImpl implements HelloWorldService.Iface {

    @Override
    public String sayHello(String username) throws TException {
        return "Hi," + username + " welcome";
    }

}
