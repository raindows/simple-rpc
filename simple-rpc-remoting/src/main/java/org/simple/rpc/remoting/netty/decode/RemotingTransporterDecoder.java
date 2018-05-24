package org.simple.rpc.remoting.netty.decode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import org.simple.rpc.common.exception.remoting.RemotingContextException;
import org.simple.rpc.common.protocal.RemotingProtocol;
import org.simple.rpc.remoting.model.RemotingTransporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * <p>Netty解码器 对{@link RemotingTransporter}解码</p>
 * <pre>
 *     author      XueQi
 *     date        2018/5/24
 *     email       sage.xue@vipshop.com
 * </pre>
 */
public class RemotingTransporterDecoder extends ReplayingDecoder<RemotingTransporterDecoder.State> {
	private static Logger LOGGER = LoggerFactory.getLogger(RemotingTransporterDecoder.class);

	// 消息体最大长度
	private static final int MAX_BODY_SIZE = 1024 * 1024 * 5;

	private static final RemotingTransporter REMOTING_TRANSPORTER = new RemotingTransporter();

	public RemotingTransporterDecoder() {
		// 为了可以进入decode中的switch方法,对读取的信息进行解析
		super(State.HEADER_MAGIC);
	}

	@Override
	protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
		// ReplayingDecoder在应对分包粘包的场景下,如果读取到的字节数不完整,会抛出Signal类型的Error,然后还原指针位置,直到读取到为止
		// 状态机的作用,由于网络原因,可能有中间部分读取不完整,如果重头执行上述过程,非常消耗CPU;基于状态机,在指定指针位置,等待读取
		switch (state()) {
			case HEADER_MAGIC:
				// 校验是否为协议头数据
				checkMessageHeader(byteBuf.readShort());
				checkpoint(State.HEADER_TYPE);
				break;
			case HEADER_TYPE:
				// 传输类型
				REMOTING_TRANSPORTER.setTransferType(byteBuf.readByte());
				checkpoint(State.HEADER_CODE);
				break;
			case HEADER_CODE:
				// 传输事件code
				REMOTING_TRANSPORTER.setCode(byteBuf.readByte());
				checkpoint(State.HEADER_REQ_ID);
				break;
			case HEADER_REQ_ID:
				// 请求流水号
				REMOTING_TRANSPORTER.setRequestId(byteBuf.readLong());
				checkpoint(State.HEADER_BODY_LENGTH);
				break;
			case HEADER_BODY_LENGTH:
				// 请求数据长度
				REMOTING_TRANSPORTER.setSize(byteBuf.readInt());
				checkpoint(State.BODY);
				break;
			case BODY:
				// 传输数据
				int bodyLength = REMOTING_TRANSPORTER.getSize();
				checkBodyLength(bodyLength);
				byte[] datas = new byte[bodyLength];
				byteBuf.readBytes(datas);
				REMOTING_TRANSPORTER.setBytes(datas);
				break;
		}

		// 将状态机指向协议头,如果是粘包发送,一次接收包含两次信息,不影响读取后面的信息
		checkpoint(State.HEADER_MAGIC);
	}

	//..................//

	/**
	 * @param bodyLength
	 */
	private void checkBodyLength(int bodyLength) throws RemotingContextException {
		if (bodyLength > MAX_BODY_SIZE) {
			throw new RemotingContextException(String.format("message body size is bigger than limit value, actual size: %s, expect size:%s", bodyLength, MAX_BODY_SIZE));
		}
	}

	/**
	 * 校验消息头的协议头信息
	 *
	 * @param headerMagic
	 */
	private void checkMessageHeader(short headerMagic) throws RemotingContextException {
		if (RemotingProtocol.MAGIC_HEADER != headerMagic) {
			LOGGER.error("Message header magic not match, current magic:{},expect magic:{}", headerMagic, RemotingProtocol.MAGIC_HEADER);
			throw new RemotingContextException(String.format("message header magic is not equal: %s", headerMagic, RemotingProtocol.MAGIC_HEADER));
		}
	}

	/**
	 * 定义Netty {@link ReplayingDecoder}解码器的状态机
	 */
	enum State {
		// 协议头
		HEADER_MAGIC,
		// 传输类型
		HEADER_TYPE,
		// 传输事件code
		HEADER_CODE,
		// 请求流水号
		HEADER_REQ_ID,
		// 请求数据长度
		HEADER_BODY_LENGTH,
		// 消息内容
		BODY
	}
}
