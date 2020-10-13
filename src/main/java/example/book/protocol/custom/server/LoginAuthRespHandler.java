package example.book.protocol.custom.server;

import example.book.protocol.custom.message.Header;
import example.book.protocol.custom.message.NettyMessage;
import example.book.protocol.custom.pojo.MessageType;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LoginAuthRespHandler extends ChannelHandlerAdapter {

    private Map<String, Boolean> nodeCheck = new ConcurrentHashMap<>();
    private String[] whiteList = {"127.0.0.1", "192.168.1.4"};

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;

        // 如果是握手请求消息, 处理, 其它消息透传
        if (message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_REQ) {
            String nodeIndex = ctx.channel().remoteAddress().toString();
            NettyMessage loginResp;

            // 拒绝重复登录
            if (nodeCheck.containsKey(nodeIndex)) {
                loginResp = buildResponse((byte) -1);
            } else {
                InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
                String ip = address.getAddress().getHostAddress();
                boolean isOk = false;
                for (String white : whiteList) {
                    if (white.equals(ip)) {
                        isOk = true;
                        break;
                    }
                }
                if (isOk) {
                    nodeCheck.put(nodeIndex, true);
                    loginResp = buildResponse((byte) 0);
                } else {
                    loginResp = buildResponse((byte) -1);
                }
            }
            System.out.println("The login response is: " + loginResp + " body [" + loginResp.getBody() + "]" + "nodeIndex: " + nodeIndex);
            ctx.writeAndFlush(loginResp);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    private NettyMessage buildResponse(byte result) {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.LOGIN_RESP);
        message.setHeader(header);
        message.setBody(result);
        return message;
    }
}
