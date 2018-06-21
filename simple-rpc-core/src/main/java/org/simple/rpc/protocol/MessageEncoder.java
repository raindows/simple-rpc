package org.simple.rpc.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.simple.rpc.serialization.protostuff.ProtostuffSerializer;

/**
 * <p>Netty消息编码处理器</p>
 * <pre>
 *     author      XueQi
 *     date        2018/6/12
 *     email       job.xueqi@outlook.com
 * </pre>
 */
public class MessageEncoder extends MessageToByteEncoder {

	private Class<?> genericClass;

	public MessageEncoder(Class<?> genericClass) {
		this.genericClass = genericClass;
	}

	@Override
	public void encode(ChannelHandlerContext channelHandlerContext, Object in, ByteBuf byteBuf) throws Exception {
		if (genericClass.isInstance(in)) {
			// 如果待传输对象和期望类型相同 编码传输
			byte[] data = ProtostuffSerializer.getInstance().writeObject(in);

			// 输出消息长度
			byteBuf.writeInt(data.length);
			// 输出消息内容
			byteBuf.writeBytes(data);
		}
	}
}
