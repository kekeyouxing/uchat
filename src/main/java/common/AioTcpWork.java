package common;

import java.nio.channels.AsynchronousSocketChannel;

public class AioTcpWork {
    private AioTcpListener listener;
    public AioTcpWork(AioTcpListener listener){
        this.listener = listener;
    }

    public void registerSession(AsynchronousSocketChannel socket, Integer sessionId) throws Throwable {
        Session session = new AioTcpSession(socket, sessionId);
        listener.sessionOpened(session);
        session.read();
    }

}
