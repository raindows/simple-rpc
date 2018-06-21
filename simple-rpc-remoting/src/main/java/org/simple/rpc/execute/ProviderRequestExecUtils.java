package org.simple.rpc.execute;

import org.simple.rpc.serialization.RpcRequest;
import org.simple.rpc.serialization.RpcResponse;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;

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
			String     methodName            = request.getTargetMethodName();
			Class<?>   serviceBeanClazz      = serviceBean.getClass();
			Class<?>[] requestParameterTypes = request.getParameterTypes();
			Object[]   requestParameters     = request.getParameters();

			// 执行服务端本地方法
			FastClass  serviceFastClass = FastClass.create(serviceBeanClazz);
			FastMethod serviceMethod    = serviceFastClass.getMethod(methodName, requestParameterTypes);
			Object     result           = null;
			try {
				result = serviceMethod.invoke(serviceBean, requestParameters);
				rpcResponse.setResult(result);
			} catch (Exception e) {
				// FIXME 方法执行异常进行捕获
				rpcResponse.setCause(e.toString());
			}
		}
		return rpcResponse;
	}

}
