package example.book.protocol.http.server;

import example.book.protocol.http.HttpServerConst;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

public class HttpFileServer {

    public static void main(String[] args) throws Exception {
        new HttpFileServer().run(HttpServerConst.PORT, HttpServerConst.DEFAULT_URL);
    }

    public void run(int port, String url) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {

                            // http请求消息解码器
                            ch.pipeline().addLast("http-decoder", new HttpRequestDecoder());

                            // 将多个消息转换为单一的FullHttpRequest或FullHTTPResponse, 因为http解码器在每个http消息中会生成多个消息对象
                            ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65536));

                            // 添加http响应器, 对http响应消息进行编码
                            ch.pipeline().addLast("http-encoder", new HttpResponseEncoder());

                            // 支持异步发送大的码流, 但不占用过多的内存
                            ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                            ch.pipeline().addLast("fileServerHandler", new HttpFileServerHandler(url));
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            System.out.println("http文件目录服务器启动");
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
