package common;

import java.nio.channels.AsynchronousSocketChannel;

public class AioTcpWork {
    private AioTcpListener listener;
    private Config config;
    public AioTcpWork(AioTcpListener listener, Config config){
        this.listener = listener;
        this.config = config;
    }

    public void registerSession(AsynchronousSocketChannel socket, Integer sessionId) {
        Session session = new AioTcpSession(socket, sessionId, config);
        try {
            listener.sessionOpened(session);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        session.read();
    }

}
