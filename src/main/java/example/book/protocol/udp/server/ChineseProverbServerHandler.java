package example.book.protocol.udp.server;

import example.book.protocol.udp.ChineseProverbConst;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

import java.util.concurrent.ThreadLocalRandom;

import static example.book.protocol.udp.ChineseProverbConst.QUERY;

public class ChineseProverbServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {


    @Override
    protected void messageReceived(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        String req = packet.content().toString(CharsetUtil.UTF_8);
        System.out.println(req);
        if (QUERY.equals(req)) {

            // 构造的DatagramPacket, 第一个参数是发送的内容, 第二个参数是返回的目的地址
            ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer("谚语查询结果: "
                    + ChineseProverbConst.DICTIONARY[ThreadLocalRandom.current().nextInt(ChineseProverbConst.DICTIONARY.length)], CharsetUtil.UTF_8),
                    packet.sender()));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
