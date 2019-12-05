package client;

import common.Config;
import common.Context;

import java.nio.channels.AsynchronousSocketChannel;

/**
 * @author keyouxing
 */
public class AioTcpClientContext implements Context {

    public AioTcpClientContext(AioTcpClientConfig config, Object p1) {
    }

    @Override
    public Config getConfig() {
        return null;
    }

    @Override
    public AsynchronousSocketChannel getSocketChannel() {
        return null;
    }
}
