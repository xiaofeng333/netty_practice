/**
 * NioEventLoopGroup 是线程组, 包含了一组NIO线程, 专门用于网络事件的处理, 实际上就是Reactor线程组。
 * ServerBootstrap Netty用于启动NIO服务端的辅助启动类, 目的是降低服务端的开发复杂度。
 * ChannelHandlerAdapter 用于对网络事件进行读写操作, 通常只需要关注channelRead和exceptionCaught方法。
 * ByteBuf 类似NIO中的{@link java.nio.ByteBuffer}
 * NioServerSocketChannel 功能对应NIO中的{@link java.nio.channels.ServerSocketChannel}
 * 该例程未能解决tcp粘包拆包
 */
package example.book.time;