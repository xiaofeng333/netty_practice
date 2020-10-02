package example.book.time.client;

import example.book.time.TimeServerConst;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Bootstrap 客户端辅助启动类
 */
public class TimeClient {

    public static void main(String[] args) throws Exception {
        new TimeClient().connect(TimeServerConst.PORT, "127.0.0.1");
    }

    public void connect(int port, String host) throws Exception {

        // 客户端处理IO读写的线程组
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new TimeClientHandler());
                        }
                    });

            // 发起异步连接操作, 然后调用同步方法等待连接成功
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();

            // 等待客户端链路关闭
            channelFuture.channel().closeFuture().sync();
        } finally {

            // 优雅退出, 释放NIO线程组
            group.shutdownGracefully();
        }
    }
}
