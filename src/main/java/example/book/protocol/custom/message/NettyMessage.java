package example.book.protocol.custom.message;

/**
 * 心跳消息, 握手请求和握手应答消息统一由NettyMessage承载
 */
public final class NettyMessage {

    // 消息头
    private Header header;

    // 消息体, 对于请求消息, 它是方法的参数; 对于响应消息, 它是返回值
    private Object body;

    public final Header getHeader() {
        return header;
    }

    public final void setHeader(Header header) {
        this.header = header;
    }

    public final Object getBody() {
        return body;
    }

    public final void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "NettyMessage [header=" + header + "]";
    }
}
