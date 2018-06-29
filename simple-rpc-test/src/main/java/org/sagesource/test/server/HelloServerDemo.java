package org.sagesource.test.server;

import org.apache.thrift.TProcessor;
import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.sagesource.test.api.HelloWorldService;
import org.sagesource.test.api.impl.HelloWorldServiceImpl;

/**
 * Created by wangxiaoyan on 2018/6/24.
 */
public class HelloServerDemo {

	public static final int SERVER_PORT = 8090;

	public void startServer() {
		try {
			System.out.println("HelloWorld TSimpleServer start ....");

			TProcessor tprocessor = new HelloWorldService.Processor(new HelloWorldServiceImpl());

			// 简单的单线程服务模型，一般用于测试
			TNonblockingServerSocket     serverTransport = new TNonblockingServerSocket(SERVER_PORT);
			TThreadedSelectorServer.Args tArgs           = new TThreadedSelectorServer.Args(serverTransport);
			tArgs.processorFactory(new TProcessorFactory(tprocessor));
			tArgs.transportFactory(new TFramedTransport.Factory());
			tArgs.protocolFactory(new TBinaryProtocol.Factory());

			TServer server = new TThreadedSelectorServer(tArgs);
			server.serve();

		} catch (Exception e) {
			System.out.println("Server start error!!!");
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HelloServerDemo server = new HelloServerDemo();
		server.startServer();
	}

}
