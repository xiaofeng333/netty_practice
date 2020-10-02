package example.book.echo.client;

import example.book.echo.EchoConst;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class EchoClientHandler extends ChannelHandlerAdapter {

    /**
     * 统计接受消息的次数
     */
    private int counter;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        byte[] message = ("Hi, welcome to netty" + EchoConst.DELIMITER).getBytes();
        for (int i = 0; i < 10; i++) {
            ctx.writeAndFlush(Unpooled.copiedBuffer(message));
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("This is " + ++counter + " times receive server: [ " + msg + " ]");
    }
}
