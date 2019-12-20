package common;

import server.AioTcpServerConfig;

import java.nio.channels.AsynchronousSocketChannel;

/**
 * @author keyouxing
 */
public class AbstractServerContext extends AbstractLifecycle implements Context{
    private AioTcpServerConfig config;
    private AsynchronousSocketChannel socketChannel;

    public AbstractServerContext(AioTcpServerConfig config, AsynchronousSocketChannel socketChannel) {
        this.config = config;
        this.socketChannel = socketChannel;
        start();
    }

    @Override
    public AioTcpServerConfig getConfig() {
        return config;
    }

    @Override
    public AsynchronousSocketChannel getSocketChannel() {
        return socketChannel;
    }

    @Override
    public void init() {
    }

    @Override
    public void destroy() {

    }
}
