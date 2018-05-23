package org.simple.rpc.remoting.netty.encode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.simple.rpc.common.protocal.RemotingProtocol;
import org.simple.rpc.remoting.model.RemotingTransporter;
import org.simple.rpc.serialization.protostuff.ProtostuffSerializer;

/**
 * <p>Netty编码器</p>
 * <pre>
 *     author      XueQi
 *     date        2018/5/23
 *     email       sage.xue@vipshop.com
 * </pre>
 */
@ChannelHandler.Sharable    // 该编码器可以在多个Channel中使用 但要保证线程安全
public class RemotingEncoder extends MessageToByteEncoder<RemotingTransporter> {
	@Override
	protected void encode(ChannelHandlerContext channelHandlerContext, RemotingTransporter remotingTransporter, ByteBuf byteBuf) throws Exception {
		execEncodingMessage(remotingTransporter, byteBuf);
	}

	/**
	 * 编码消息
	 *
	 * @param remotingTransporter
	 * @param byteBuf
	 */
	private void execEncodingMessage(RemotingTransporter remotingTransporter, ByteBuf byteBuf) {
		// 将传输数据转换为byte数组
		byte[] data = ProtostuffSerializer.getInstance().writeObject(remotingTransporter);

		byteBuf.writeShort(RemotingProtocol.MAGIC_HEADER)           // 协议头
				.writeByte(remotingTransporter.getTransferType())   // 传输类型
				.writeByte(remotingTransporter.getCode())           // 传输事件code
				.writeLong(remotingTransporter.getRequestId())      // 请求流水号
				.writeInt(data.length)                              // 请求数据长度
				.writeBytes(data);                                  // 请求数据
	}
}
