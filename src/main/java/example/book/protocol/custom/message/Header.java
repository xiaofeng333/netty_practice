package example.book.protocol.custom.message;

import example.book.protocol.custom.pojo.MessageType;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息头定义
 */
public final class Header {

    // Netty消息校验码, 由三部分组成, 0xABEF固定值, 表明该消息是Netty协议消息, 2个字节; 主版本号: 1~255, 1个字节; 次版本号: 1~255, 1个字节
    // crcCode = 0xABEF + 主版本号 + 次版本号
    private int crcCode = 0xabef0101;

    // 消息长度, 包含消息头和消息体
    private int length;

    // 会话ID, 集群节点内全局唯一, 由会话ID生成器生成
    private long sessionID;

    // 消息类型
    private MessageType type;

    // 消息优先级, 0~255
    private byte priority;

    // 附件, 可选字段, 用于扩展消息头
    private Map<String, Object> attachment = new HashMap<>();

    public final int getCrcCode() {
        return crcCode;
    }

    public final void setCrcCode(int crcCode) {
        this.crcCode = crcCode;
    }

    public final int getLength() {
        return length;
    }

    public final void setLength(int length) {
        this.length = length;
    }

    public final long getSessionID() {
        return sessionID;
    }

    public final void setSessionID(long sessionID) {
        this.sessionID = sessionID;
    }

    public final MessageType getType() {
        return type;
    }

    public final void setType(MessageType type) {
        this.type = type;
    }

    public final byte getPriority() {
        return priority;
    }

    public final void setPriority(byte priority) {
        this.priority = priority;
    }

    public final Map<String, Object> getAttachment() {
        return attachment;
    }

    public final void setAttachment(Map<String, Object> attachment) {
        this.attachment = attachment;
    }

    @Override
    public String toString() {
        return "Header{" +
                "crcCode=" + crcCode +
                ", length=" + length +
                ", sessionID=" + sessionID +
                ", type=" + type +
                ", priority=" + priority +
                ", attachment=" + attachment +
                '}';
    }
}
