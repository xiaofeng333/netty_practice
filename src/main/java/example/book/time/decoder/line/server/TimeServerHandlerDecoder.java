package example.book.time.decoder.line.server;

import example.book.time.TimeServerConst;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.Date;

public class TimeServerHandlerDecoder extends ChannelHandlerAdapter {

    /**
     * 统计收到的消息个数
     */
    private int counter;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body = (String) msg;
        System.out.println("The time server receive order: " + body + "; the counter is: " + ++counter);
        String currentTime = TimeServerConst.QUERY_TIME_ORDER.equalsIgnoreCase(body) ? new Date().toString() : "BAD ORDER";
        currentTime += System.getProperty(TimeServerConst.LINE_SEPARATOR);
        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
        ctx.writeAndFlush(resp);
    }
}
