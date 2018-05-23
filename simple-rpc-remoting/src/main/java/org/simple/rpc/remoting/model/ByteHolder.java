package org.simple.rpc.remoting.model;

/**
 * <p>存放网络传输对象的实际byte数组</p>
 * <pre>
 *     author      XueQi
 *     date        2018/5/23
 *     email       sage.xue@vipshop.com
 * </pre>
 */
public class ByteHolder {

	/**
	 * 传输数据
	 */
	private transient byte[] bytes;

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	/**
	 * 获取数据长度
	 *
	 * @return
	 */
	public int size() {
		return this.bytes == null ? 0 : bytes.length;
	}
}
