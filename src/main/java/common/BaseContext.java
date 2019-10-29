package common;

import server.AioTcpServerConfig;
import util.UUIDGenerator;

import java.nio.channels.AsynchronousSocketChannel;

/**
 * @author keyouxing
 */
public class BaseContext extends AbstractLifecycle implements Context{
    private AioTcpServerConfig config;
    private AsynchronousSocketChannel socketChannel;
    private String id;

    public BaseContext(AioTcpServerConfig config, AsynchronousSocketChannel socketChannel) {
        this.config = config;
        this.socketChannel = socketChannel;
        start();
    }

    @Override
    public void init() {
        id = UUIDGenerator.createUuid();

    }

    @Override
    public void destroy() {

    }
}
