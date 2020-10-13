package example.book.protocol.custom.client;

import example.book.protocol.custom.codec.NettyMessageDecoder;
import example.book.protocol.custom.codec.NettyMessageEncoder;
import example.book.protocol.custom.util.NettyConst;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NettyClient {
    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    private EventLoopGroup group = new NioEventLoopGroup();

    public static void main(String[] args) throws Exception {
        new NettyClient().connect(NettyConst.SERVER_PORT, NettyConst.IP);
    }

    public void connect(int port, String host) throws Exception {
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyMessageDecoder(1024 * 1024, 4, 4));
                            ch.pipeline().addLast("MessageEncoder", new NettyMessageEncoder());
                            ch.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(50));
                            ch.pipeline().addLast("LoginAuthHandler", new LoginAuthReqHandler());
                            ch.pipeline().addLast("HeartBeatHandler", new HeartBeatReqHandler());
                        }
                    });
            ChannelFuture future = bootstrap.connect(host, port).sync();
            System.out.println("Netty client start ok");
            future.channel().closeFuture().sync();
        } finally {

            // 所有资源释放完成之后, 清空资源, 再次发起重连操作
            executorService.execute(() -> {
                try {
                    TimeUnit.SECONDS.sleep(5);

                    // 发起重连操作
                    connect(NettyConst.SERVER_PORT, NettyConst.IP);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
