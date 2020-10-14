package example.book.codec.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;
import example.book.codec.protobuf.pojo.SubscribeReqProto;

import java.util.ArrayList;
import java.util.List;

public class TestSubscribeReqProto {
    public static void main(String[] args) throws InvalidProtocolBufferException {
        SubscribeReqProto.SubscribeReq req = createSubscribeReq();
        System.out.println("Before encode: " + req.toString());
        SubscribeReqProto.SubscribeReq decode = decode(encode(req));
        System.out.println("After encode: " + decode.toString());
        System.out.println("Assert equal: " + decode.equals(req));
    }

    private static byte[] encode(SubscribeReqProto.SubscribeReq req) {
        return req.toByteArray();
    }

    private static SubscribeReqProto.SubscribeReq decode(byte[] body) throws InvalidProtocolBufferException {
        return SubscribeReqProto.SubscribeReq.parseFrom(body);
    }

    private static SubscribeReqProto.SubscribeReq createSubscribeReq() {
        SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();
        builder.setSubReqID(1);
        builder.setUsername("netty");
        builder.setProductNme("Netty Book");
        List<String> address = new ArrayList<>();
        address.add("NJ");
        address.add("BJ");
        address.add("SH");
        builder.addAllAddress(address);
        return builder.build();
    }
}
