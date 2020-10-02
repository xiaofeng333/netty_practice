/**
 * 使用{@link io.netty.handler.codec.LineBasedFrameDecoder}和{@link io.netty.handler.codec.string.StringDecoder}解决粘包拆包问题
 * LineBasedFrameDecoder依次遍历ByteBuf中的可读字节, 判断是否有\n或\rn, 以换行符为结束标志的解码器。同时支持配置单行的最大长度。
 * 如果连续读取到最大长度后仍然没有发现换行符, 就会抛出异常, 同时忽略掉之前读到的异常码流。
 * StringDecoder的功能是将接收到的对象转换成字符串, 然后继续调用后面的Handler。
 */
package example.book.time.decoder.line;