package server;

import common.AbstractServerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * @author keyouxing
 */
public class AioTcpAcceptHandler implements CompletionHandler<AsynchronousSocketChannel, AioTcpServer> {

    private Logger logger = LoggerFactory.getLogger(AioTcpAcceptHandler.class);
    @Override
    public void completed(AsynchronousSocketChannel socketChannel, AioTcpServer server) {

        AioTcpServerContext serverContext = new AioTcpServerContext(server.getConfig(), socketChannel);


        server.listen(server.getServerSocket());

    }

    @Override
    public void failed(Throwable exc, AioTcpServer server) {

        server.listen(server.getServerSocket());
        logger.error("["+server.getConfig().getHost()+"]"+"监听出现异常", exc);

    }
}
