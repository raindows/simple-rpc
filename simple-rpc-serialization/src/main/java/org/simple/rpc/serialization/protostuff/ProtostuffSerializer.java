package org.simple.rpc.serialization.protostuff;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>基于protostuff序列化</p>
 * <pre>
 *     author      XueQi
 *     date        2018/5/23
 *     email       sage.xue@vipshop.com
 * </pre>
 */
public class ProtostuffSerializer {
	// 由于创建schema的过程非常耗时 所以需要缓存class对应的schema
	private static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<>();
	private static Objenesis                objenesis    = new ObjenesisStd(true);

	// ---- 单例 ---- //
	private ProtostuffSerializer() {
	}

	private static final class Holder {
		private static final ProtostuffSerializer INSTANCE = new ProtostuffSerializer();
	}

	public static final ProtostuffSerializer getInstance() {
		return Holder.INSTANCE;
	}

	/**
	 * 序列化对象
	 *
	 * @param obj
	 * @param <T>
	 * @return
	 */
	public <T> byte[] writeObject(T obj) {
		// 获取待序列化的对象类型
		Class<T> clazz = (Class<T>) obj.getClass();
		// 分配本地内存
		LinkedBuffer buffer = null;
		try {
			buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
			Schema<T> schema = getSchema(clazz);
			return ProtobufIOUtil.toByteArray(obj, schema, buffer);
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		} finally {
			if (buffer != null) buffer.clear();
		}
	}

	/**
	 * 反序列化对象
	 *
	 * @param bytes
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public <T> T readObject(byte[] bytes, Class<T> clazz) {
		try {
			// 反射生成对象
			T         message = objenesis.newInstance(clazz);
			Schema<T> schema  = getSchema(clazz);
			ProtobufIOUtil.mergeFrom(bytes, message, schema);
			return message;
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	/**
	 * 获取 Schema Protostuff 序列化时 最耗时的阶段就是构建 schema 将对象的 schema 缓存化 提高效率
	 *
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	private static <T> Schema<T> getSchema(Class<T> clazz) {
		Schema<T> schema = (Schema<T>) cachedSchema.get(clazz);
		if (schema == null) {
			schema = RuntimeSchema.createFrom(clazz);
			cachedSchema.put(clazz, schema);
		}
		return schema;
	}
}
