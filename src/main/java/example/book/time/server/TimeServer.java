package example.book.time.server;

import example.book.time.TimeServerConst;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TimeServer {

    public static void main(String[] args) {
        new TimeServer().bind(TimeServerConst.PORT);
    }

    public void bind(int port) {

        // 配置服务端的NIO线程组, 一个用于服务端接受客户端的连接, 一个用于进行SocketChannel的网络读写
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)

                    // 设置NioServerSocketChannel的TCP参数
                    .option(ChannelOption.SO_BACKLOG, 1024)

                    // 绑定IO事件的处理类, 用于处理网络I/O事件, 如记录日志, 对消息进行编解码等
                    .childHandler(new ChildChannelHandler());

            // 绑定端口, 同步等待成功。返回的ChannelFuture用于异步操作的通知回调
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();

            // 等待服务器监听端口关闭, 方法会阻塞在这里
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {

            // 优雅退出, 释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {


        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(new TimeServerHandler());
        }
    }
}
