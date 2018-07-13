package org.sagesource.simplerpc.provider.thread;

import org.apache.thrift.TProcessor;
import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.sagesource.simplerpc.basic.entity.ServerInfo;
import org.sagesource.simplerpc.core.protocol.TEnhanceTransProtocol;
import org.sagesource.simplerpc.core.zookeeper.ServiceRegisterProviderAgent;
import org.sagesource.simplerpc.core.zookeeper.ZookeeperClientFactory;

/**
 * <p>服务提供方线程</p>
 * <pre>
 *     author      XueQi
 *     date        2018/7/12
 *     email       job.xueqi@outlook.com
 * </pre>
 */
public class ProviderServerThread extends Thread {
	private ServerInfo serverInfo;
	private TServer    server;
	private volatile boolean isStart = false;

	public ProviderServerThread(ServerInfo serverInfo, TProcessor processor) throws Exception {
		this.serverInfo = serverInfo;

		TNonblockingServerSocket     serverTransport = new TNonblockingServerSocket(serverInfo.getPort());
		TThreadedSelectorServer.Args tArgs           = new TThreadedSelectorServer.Args(serverTransport);
		tArgs.processorFactory(new TProcessorFactory(processor));
		tArgs.transportFactory(new TFramedTransport.Factory());
		tArgs.protocolFactory(new TEnhanceTransProtocol.Factory(new TCompactProtocol.Factory()));
		this.server = new TThreadedSelectorServer(tArgs);
	}

	@Override
	public void run() {
		try {
			isStart = true;
			ServiceRegisterProviderAgent serviceRegisterProviderAgent = new ServiceRegisterProviderAgent();
			serviceRegisterProviderAgent.register(this.serverInfo);
			// 在线程中启动服务，并注册服务信息
			this.server.serve();
		} catch (Exception e) {
			//FIXME 启动失败 关闭zk连接 更新启动状态为失败
			ZookeeperClientFactory.close();
			e.printStackTrace();
		}
	}

	public void close() {
		if (this.server != null)
			this.server.stop();
	}
}
