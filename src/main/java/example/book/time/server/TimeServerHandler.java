package example.book.time.server;

import example.book.time.TimeServerConst;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.StandardCharsets;
import java.util.Date;

public class TimeServerHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;

        // buf.readableBytes() 获取缓存区可读的字节数
        byte[] req = new byte[buf.readableBytes()];

        // 将缓冲区的字节数组复制到新建的byte数组中
        buf.readBytes(req);
        String body = new String(req, StandardCharsets.UTF_8);
        System.out.println("TimeServerHandler.channelRead(), the time server receive order: " + body);
        String currentTime = TimeServerConst.QUERY_TIME_ORDER.equalsIgnoreCase(body) ? new Date().toString() : "BAD ORDER";
        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());

        // 异步发送应答消息给客户端
        ctx.write(resp);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

        // 将消息发送队列中的消息写入到SocketChannel中发送给对方。
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
