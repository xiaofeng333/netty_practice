package example.book.protocol.udp.server;

import example.book.protocol.udp.ChineseProverbConst;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class ChineseProverbServer {

    public static void main(String[] args) throws Exception {
        new ChineseProverbServer().run(ChineseProverbConst.PORT);
    }

    public void run(int port) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioDatagramChannel.class)

                    // 设置socket参数支持广播
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new ChineseProverbServerHandler());
            bootstrap.bind(port).sync().channel().closeFuture().await();
        } finally {
            group.shutdownGracefully();
        }
    }
}
