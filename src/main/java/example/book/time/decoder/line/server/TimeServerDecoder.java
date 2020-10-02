package example.book.time.decoder.line.server;

import example.book.time.TimeServerConst;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class TimeServerDecoder {

    public static void main(String[] args) throws Exception {
        new TimeServerDecoder().bind(TimeServerConst.PORT);
    }

    public void bind(int port) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChildChannelHandlerDecoder());

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

    private class ChildChannelHandlerDecoder extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel ch) throws Exception {

            // 指定了消息最大长度为1024
            ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
            ch.pipeline().addLast(new StringDecoder());
            ch.pipeline().addLast(new TimeServerHandlerDecoder());
        }
    }
}
