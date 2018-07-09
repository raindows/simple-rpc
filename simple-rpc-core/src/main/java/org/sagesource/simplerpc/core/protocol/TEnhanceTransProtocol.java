package org.sagesource.simplerpc.core.protocol;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.*;
import org.apache.thrift.transport.TTransport;
import org.sagesource.simplerpc.core.trace.ThreadTrace;

/**
 * <p>增强 TTprotocol</p>
 * <pre>
 *     author      XueQi
 *     date        2018/7/9
 *     email       job.xueqi@outlook.com
 * </pre>
 */
public class TEnhanceTransProtocol extends TProtocolDecorator {
	private static final String TRACEID_SEPARATOR = ":";

	// 重写 ThriftProtocolFactory
	public static class Factory implements TProtocolFactory {
		private TProtocolFactory protocolFactory;

		public Factory(TProtocolFactory protocolFactory) {
			this.protocolFactory = protocolFactory;
		}

		@Override
		public TProtocol getProtocol(TTransport tTransport) {
			return new TEnhanceTransProtocol(this.protocolFactory.getProtocol(tTransport));
		}
	}

	public TEnhanceTransProtocol(TProtocol protocol) {
		super(protocol);
	}

	/**
	 * 重写消息发送
	 *
	 * @param tMessage
	 * @throws TException
	 */
	@Override
	public void writeMessageBegin(TMessage tMessage) throws TException {
		Long traceId = ThreadTrace.get();
		super.writeMessageBegin(new TMessage(traceId + TRACEID_SEPARATOR + tMessage.name, tMessage.type, tMessage.seqid));
	}

	@Override
	public TMessage readMessageBegin() throws TException {
		TMessage message      = super.readMessageBegin();
		String   name         = message.name;
		int      traceIdIndex = name.indexOf(TRACEID_SEPARATOR);
		if (traceIdIndex == -1) {
			throw new TProtocolException(TProtocolException.UNKNOWN, "Expected trace protocol is error");
		}

		String traceId = name.substring(0, traceIdIndex);
		if (traceId == null || "".equals(traceId)) {
			throw new TProtocolException(TProtocolException.INVALID_DATA, "Expected traceId is null");
		}

		ThreadTrace.remove();
		ThreadTrace.set(Long.parseLong(traceId));
		name = name.substring(traceIdIndex + 1);
		return new TMessage(name, message.type, message.seqid);
	}
}
