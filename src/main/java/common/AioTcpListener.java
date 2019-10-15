package common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AioTcpListener implements Listener{
    Logger logger = LoggerFactory.getLogger(AioTcpListener.class);

    public AioTcpListener(Config config) {
    }

    @Override
    public void sessionOpened(Session session) throws Throwable {
        logger.info("Tcp three-way handshake succeeded, session is {}", session);
    }

    @Override
    public void sessionClosed(Session session) throws Throwable {

    }

    @Override
    public void messageReceived(Session session, Object message) throws Throwable {

    }
}
