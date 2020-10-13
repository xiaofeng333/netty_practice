package example.book.protocol.custom.pojo;

/**
 * 0(业务请求消息) 1(业务响应消息) 2(业务ONE WAY消息, 即是请求又是响应消息) 3(握手请求消息) 4(握手应答消息) 5(心跳请求消息) 6(心跳应答消息)
 */
public enum MessageType {
    LOGIN_REQ((byte) 3),
    LOGIN_RESP((byte) 4),
    HEARTBEAT_REQ((byte) 5),
    HEARTBEAT_RESP((byte) 6);

    public byte value;

    MessageType(byte v) {
        this.value = v;
    }

    public static MessageType getMessageType(byte type) {
        switch (type) {
            case 3:
                return LOGIN_REQ;
            case 4:
                return LOGIN_RESP;
            case 5:
                return HEARTBEAT_REQ;
            case 6:
                return HEARTBEAT_RESP;
            default:
                return null;
        }
    }

    public byte value() {
        return value;
    }
}
