package example.book.echo.server;

import example.book.echo.EchoConst;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class EchoServerHandler extends ChannelHandlerAdapter {

    /**
     * 统计收到的请求
     */
    private int counter;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("This is " + ++counter + " times receive client: [ " + msg + " ]");
        byte[] respBytes = (msg + EchoConst.DELIMITER).getBytes();
        ByteBuf byteBuf = Unpooled.copiedBuffer(respBytes);
        ctx.writeAndFlush(byteBuf);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
