package org.simple.rpc.serialization;

/**
 * <p>序列化抽象类</p>
 * <pre>
 *     author      XueQi
 *     date        2018/5/23
 *     email       sage.xue@vipshop.com
 * </pre>
 */
public interface Serializer {

	/**
	 * 序列化对象
	 *
	 * @param obj
	 * @param <T>
	 * @return
	 */
	<T> byte[] writeObject(T obj);

	/**
	 * 反序列化对象
	 *
	 * @param bytes
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	<T> T readObject(byte[] bytes, Class<T> clazz);

}
