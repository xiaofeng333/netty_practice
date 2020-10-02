/**
 * 使用java序列化对对象进行编码和解码
 * {@link io.netty.handler.codec.serialization.ObjectDecoder} 负责对实现{@link java.io.Serializable}的pojo对象进行解码。
 * {@link io.netty.handler.codec.serialization.ObjectEncoder} 在消息发送时自动将实现Serializable的pojo对象进行编码。
 */
package example.book.codec.serial;