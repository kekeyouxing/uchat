package server;

import common.AbstractServerContext;

import java.nio.channels.AsynchronousSocketChannel;

/**
 * @author keyouxing
 */
public class AioTcpServerContext extends AbstractServerContext {

    public AioTcpServerContext(AioTcpServerConfig config, AsynchronousSocketChannel socketChannel) {
        super(config, socketChannel);
    }

}
