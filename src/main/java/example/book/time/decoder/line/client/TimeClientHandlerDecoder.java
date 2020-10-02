package example.book.time.decoder.line.client;

import example.book.time.TimeServerConst;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class TimeClientHandlerDecoder extends ChannelHandlerAdapter {

    private final byte[] req;

    /**
     * 统计返回次数
     */
    private int counter;

    public TimeClientHandlerDecoder() {
        this.req = (TimeServerConst.QUERY_TIME_ORDER + System.getProperty(TimeServerConst.LINE_SEPARATOR)).getBytes();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf message;
        for (int i = 0; i < 100; i++) {
            message = Unpooled.buffer(req.length);
            message.writeBytes(req);
            ctx.writeAndFlush(message);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String resp = (String) msg;
        System.out.println("Now is: " + resp + "; the counter is: " + ++counter);
    }
}
