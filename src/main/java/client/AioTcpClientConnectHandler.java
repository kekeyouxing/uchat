package client;

import common.Handler;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AioTcpClientConnectHandler implements CompletionHandler<Void, AioTcpClient> {
    private final AsynchronousSocketChannel channel;

    public AioTcpClientConnectHandler(AsynchronousSocketChannel channel) {
        this.channel = channel;
    }

    @Override
    public void completed(Void result, AioTcpClient client) {
        AioTcpClientContext clientContext = new AioTcpClientContext(client.getConfig(), channel);
        Handler handler = client.getConfig().getHandler();
        if (handler != null){
            handler.connectionOpenSuccess(clientContext);
        }
        clientContext.startRead();
    }

    @Override
    public void failed(Throwable exc, AioTcpClient attachment) {

    }
}
