package org.simple.rpc.execute;

import org.simple.rpc.serialization.RpcRequest;
import org.simple.rpc.serialization.RpcResponse;

import java.util.Map;

/**
 * <p>服务请求执行工具</p>
 * <pre>
 *     author      XueQi
 *     date        2018/6/20
 *     email       job.xueqi@outlook.com
 * </pre>
 */
public class ProviderRequestExecUtils {
	/**
	 * 执行请求
	 *
	 * @param request
	 * @return
	 */
	public static RpcResponse exec(RpcRequest request, Map<String, Object> providerServiceBean) {
		RpcResponse rpcResponse = new RpcResponse();
		rpcResponse.setTraceId(request.getTraceId());

		String interfaceName = request.getTargetClassName();
		Object serviceBean   = providerServiceBean.get(interfaceName);
		if (serviceBean == null) {
			// FIXME 服务不存在处理逻辑
		} else {

		}
		return rpcResponse;
	}

}
