package example.book.echo.server;

import example.book.echo.EchoConst;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class EchoServer {

    public static void main(String[] args) throws InterruptedException {
        new EchoServer().bind(EchoConst.PORT);
    }

    public void bind(int port) throws InterruptedException {

        // 配置服务器的NIO线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ByteBuf delimiter = Unpooled.copiedBuffer(EchoConst.DELIMITER.getBytes());

                            // 1024表示单个消息的最大长度, 当达到该长度仍未找到分隔符, 抛出TooLongFrameException
                            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new EchoServerHandler());
                        }
                    });

            // 绑定端口, 同步等待成功
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();

            // 等待服务器监听端口关闭
            channelFuture.channel().closeFuture().sync();
        } finally {

            // 优雅退出, 释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
