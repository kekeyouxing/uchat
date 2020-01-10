package common;

import java.nio.channels.AsynchronousSocketChannel;

/**
 * @author keyouxing
 */
public interface Context {

    Config getConfig();

    AsynchronousSocketChannel getSocketChannel();

    TcpConnection getConnection();
}
