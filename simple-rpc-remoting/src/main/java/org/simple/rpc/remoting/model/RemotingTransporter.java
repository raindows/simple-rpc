package org.simple.rpc.remoting.model;

import org.simple.rpc.common.protocal.RemotingProtocol;
import org.simple.rpc.common.transport.body.CommonCustomBody;

import java.util.concurrent.atomic.AtomicLong;

/**
 * <p>RPC框架中 网络传输的唯一对象</p>
 * <pre>
 *     author      XueQi
 *     date        2018/5/23
 *     email       sage.xue@vipshop.com
 * </pre>
 */
public class RemotingTransporter extends ByteHolder {
	private static final AtomicLong REQ_ATOMIC_LONG = new AtomicLong(0L);

	/**
	 * 请求类型<br/>
	 * 1: 消费者定位服务 2:服务者发布服务
	 */
	private           byte             code;
	/**
	 * 传输的内容主体信息
	 */
	private transient CommonCustomBody customBody;
	/**
	 * 请求时间戳
	 */
	private transient long             timestamp;
	/**
	 * 传输的对象类型
	 */
	private           byte             transferType;
	/**
	 * 请求id
	 */
	private           long             requestId;

	public RemotingTransporter() {
	}

	/**
	 * 创建请求传输对象
	 *
	 * @param code
	 * @param customBody
	 * @return
	 */
	public static RemotingTransporter createRequestRemotingTransporter(byte code, CommonCustomBody customBody) {
		RemotingTransporter remotingTransporter = new RemotingTransporter();
		remotingTransporter.setCode(code);
		remotingTransporter.setCustomBody(customBody);
		remotingTransporter.setTransferType(RemotingProtocol.REQUEST_REMOTING);
		remotingTransporter.setRequestId(REQ_ATOMIC_LONG.getAndIncrement());

		return remotingTransporter;
	}

	/**
	 * 创建响应传输对象
	 *
	 * @param code
	 * @param customBody
	 * @param requestId
	 * @return
	 */
	public static RemotingTransporter createResponseRemotingTransporter(byte code, CommonCustomBody customBody, long requestId) {
		RemotingTransporter remotingTransporter = new RemotingTransporter();
		remotingTransporter.setCode(code);
		remotingTransporter.setCustomBody(customBody);
		remotingTransporter.setTransferType(RemotingProtocol.RESPONSE_REMOTING);
		remotingTransporter.setRequestId(requestId);

		return remotingTransporter;
	}

	public byte getCode() {
		return code;
	}

	public void setCode(byte code) {
		this.code = code;
	}

	public CommonCustomBody getCustomBody() {
		return customBody;
	}

	public void setCustomBody(CommonCustomBody customBody) {
		this.customBody = customBody;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public byte getTransferType() {
		return transferType;
	}

	public void setTransferType(byte transferType) {
		this.transferType = transferType;
	}

	public long getRequestId() {
		return requestId;
	}

	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}
}
