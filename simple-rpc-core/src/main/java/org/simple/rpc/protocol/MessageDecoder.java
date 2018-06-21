package org.simple.rpc.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.simple.rpc.serialization.protostuff.ProtostuffSerializer;

import java.util.List;

/**
 * <p>Netty消息解码处理器</p>
 * <pre>
 *     author      XueQi
 *     date        2018/6/12
 *     email       job.xueqi@outlook.com
 * </pre>
 */
public class MessageDecoder extends ByteToMessageDecoder {

	private Class<?> genericClass;

	public MessageDecoder(Class<?> genericClass) {
		this.genericClass = genericClass;
	}

	@Override
	public void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
		// 读取前4位（int）获取报文长度，如果接收到的消息不足4位 丢弃等待下一次消息合并过来 一起读取
		if (byteBuf.readableBytes() < 4) {
			return;
		}

		// 记录当前读取的指针位置
		byteBuf.markReaderIndex();
		// 获取消息长度 头报文
		int dataLength = byteBuf.readInt();

		// 报文没有完全传输完毕
		if (byteBuf.readableBytes() < dataLength) {
			byteBuf.resetReaderIndex();
			return;
		}

		// 读取实际报文
		byte[] data = new byte[dataLength];
		byteBuf.readBytes(data);

		Object object = ProtostuffSerializer.getInstance().readObject(data, genericClass);
		list.add(object);
	}
}
