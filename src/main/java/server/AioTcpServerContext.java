package server;

import common.BaseContext;

import java.nio.channels.AsynchronousSocketChannel;

/**
 * @author keyouxing
 */
public class AioTcpServerContext extends BaseContext {


    public AioTcpServerContext(AioTcpServerConfig config, AsynchronousSocketChannel socketChannel) {
        super(config, socketChannel);
    }
}
