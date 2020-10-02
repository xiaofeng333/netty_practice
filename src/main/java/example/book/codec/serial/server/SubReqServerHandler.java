package example.book.codec.serial.server;

import example.book.codec.serial.SubReqConst;
import example.book.codec.serial.pojo.SubscribeReq;
import example.book.codec.serial.pojo.SubscribeResp;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class SubReqServerHandler extends ChannelHandlerAdapter {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SubscribeReq req = (SubscribeReq) msg;
        if (SubReqConst.USER_NAME.equals(req.getUserName())) {
            System.out.println("Server accept client subscribe req: [" + req.toString() + "]");
            SubscribeResp subscribeResp = new SubscribeResp();
            subscribeResp.setSubReqID(req.getSubReqID());
            subscribeResp.setRespCode(0);
            subscribeResp.setDesc("Netty book order succeed");
            ctx.writeAndFlush(subscribeResp);
        }
    }
}
