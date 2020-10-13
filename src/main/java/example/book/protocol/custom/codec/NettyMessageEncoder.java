package example.book.protocol.custom.codec;

import example.book.protocol.custom.message.Header;
import example.book.protocol.custom.message.NettyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class NettyMessageEncoder extends MessageToByteEncoder<NettyMessage> {

    private NettyMarshallingEncoder nettyMarshallingEncoder;

    public NettyMessageEncoder() throws IOException {
        nettyMarshallingEncoder = new NettyMarshallingEncoder();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, NettyMessage msg, ByteBuf sendBuf) throws Exception {
        if (msg == null || msg.getHeader() == null) {
            throw new Exception("The encode message is null");
        }
        Header header = msg.getHeader();
        sendBuf.writeInt(header.getCrcCode());
        sendBuf.writeInt(header.getLength());
        sendBuf.writeLong(header.getSessionID());
        sendBuf.writeByte(header.getType().value());
        sendBuf.writeByte(header.getPriority());
        sendBuf.writeInt(header.getAttachment().size());
        String key;
        byte[] keyArray;
        Object value;
        for (Map.Entry<String, Object> param : header.getAttachment().entrySet()) {
            key = param.getKey();
            keyArray = key.getBytes(StandardCharsets.UTF_8);
            sendBuf.writeInt(keyArray.length);
            sendBuf.writeBytes(keyArray);
            value = param.getValue();
            nettyMarshallingEncoder.encode(value, sendBuf);
        }
        Object body = msg.getBody();
        if (body != null) {
            nettyMarshallingEncoder.encode(body, sendBuf);
        } else {
            sendBuf.writeInt(0);
        }

        // 使用LengthFieldBasedFrameDecoder, 在此减去报文头长度和长度值字段的长度
        sendBuf.setInt(4, sendBuf.readableBytes() - 8);
    }
}
