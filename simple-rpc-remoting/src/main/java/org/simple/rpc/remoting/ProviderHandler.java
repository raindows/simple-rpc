package org.simple.rpc.remoting;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.simple.rpc.execute.ProviderRequestExecUtils;
import org.simple.rpc.serialization.RpcRequest;
import org.simple.rpc.serialization.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>Netty 请求处理 Handler</p>
 * <pre>
 *     author      XueQi
 *     date        2018/6/20
 *     email       job.xueqi@outlook.com
 * </pre>
 */
public class ProviderHandler extends SimpleChannelInboundHandler<RpcRequest> {
	private static Logger LOGGER = LoggerFactory.getLogger(ProviderHandler.class);

	/**
	 * 服务实现 Bean 接口-实现映射
	 */
	private final Map<String, Object> providerServiceBeanMap;

	/**
	 * 请求处理线程池
	 */
	private static ThreadPoolExecutor threadPoolExecutor = null;

	static {
		// 通过静态方法块初始化线程池
		threadPoolExecutor = new ThreadPoolExecutor(8, 16, 600L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(65535));
	}

	public ProviderHandler(Map<String, Object> providerServiceBeanMap) {
		this.providerServiceBeanMap = providerServiceBeanMap;
	}

	@Override
	protected void channelRead0(final ChannelHandlerContext channelHandlerContext, final RpcRequest rpcRequest) throws Exception {
		threadPoolExecutor.submit(new Runnable() {
			@Override
			public void run() {
				// 接受请求，未来可以在这里做 Trace 相关的操作
				LOGGER.info("receive request,trace_id={}", rpcRequest.getTraceId());

				// 执行方法调用
				RpcResponse response = ProviderRequestExecUtils.exec(rpcRequest, providerServiceBeanMap);

				// 返回请求结果
				channelHandlerContext.writeAndFlush(response).addListener(new ChannelFutureListener() {
					@Override
					public void operationComplete(ChannelFuture channelFuture) throws Exception {
						//  FIXME 操作完成回调监听事件 上报 trace 逻辑
					}
				});
			}
		});
	}
}
