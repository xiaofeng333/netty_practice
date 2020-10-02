package example.book.time.client;

import example.book.time.TimeServerConst;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.StandardCharsets;

public class TimeClientHandler extends ChannelHandlerAdapter {

    private final ByteBuf firstMessage;

    public TimeClientHandler() {
        byte[] req = TimeServerConst.QUERY_TIME_ORDER.getBytes();
        firstMessage = Unpooled.buffer(req.length);
        firstMessage.writeBytes(req);
    }

    /**
     * 当客户端和服务端TCP链路建立成功之后, Netty的NIO线程会调用该方法
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(firstMessage);
    }


    /**
     * 当服务端返回应答消息时, 该方法被调用
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, StandardCharsets.UTF_8);
        System.out.println("TimeClientHandler.channelRead(), Now is: " + body);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        // 释放资源
        cause.printStackTrace();
        ctx.close();
    }
}
