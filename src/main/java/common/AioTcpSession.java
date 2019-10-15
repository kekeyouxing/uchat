package common;

import server.AioReadHandler;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

public class AioTcpSession implements Session{
    AsynchronousSocketChannel socket;
    Integer sesssionId;
    public AioTcpSession(AsynchronousSocketChannel socket, Integer sessionId) {
        this.socket = socket;
        this.sesssionId = sessionId;
    }

    @Override
    public void read() {
        ByteBuffer clientBuffer = ByteBuffer.allocate(1024);

        AioReadHandler rd=new AioReadHandler(socket);

        socket.read(clientBuffer, clientBuffer, rd);
    }

}
