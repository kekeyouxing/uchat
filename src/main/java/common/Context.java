package common;

import java.nio.channels.AsynchronousSocketChannel;

/**
 * @author keyouxing
 */
public interface Context {

    /**
     * 获取Config
     * @return Config
     */
    Config getConfig();

    /**
     * 获取SocketChannel
     * @return AsynchronousSocketChannel
     */
    AsynchronousSocketChannel getSocketChannel();
}
