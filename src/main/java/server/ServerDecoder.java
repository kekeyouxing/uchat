package server;

import common.AioTcpSession;
import common.Decoder;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class ServerDecoder implements Decoder {
    private CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
    @Override
    public void decode(ByteBuffer buffer, AioTcpSession session) {
        try {
            String msg = decoder.decode(buffer).toString();
            System.out.println("收到" + session.getSocket().getRemoteAddress().toString() + "的消息:" + msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
