package server;

import common.Handler;
import common.TcpConnectionImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * @author keyouxing
 */
public class AioTcpServerAcceptHandler implements CompletionHandler<AsynchronousSocketChannel, AioTcpServer> {

    private Logger logger = LoggerFactory.getLogger(AioTcpServerAcceptHandler.class);
    @Override
    public void completed(AsynchronousSocketChannel socketChannel, AioTcpServer server) {

        AioTcpServerContext serverContext = new AioTcpServerContext(server.getConfig(), socketChannel);
        Handler handler = server.getConfig().getHandler();
        if(handler != null){
            handler.connectionOpenSuccess(serverContext);
        }
        serverContext.startRead();
        server.listen(server.getServerSocket());

    }

    @Override
    public void failed(Throwable exc, AioTcpServer server) {

        server.listen(server.getServerSocket());
        logger.error("["+server.getConfig().getHost()+"]"+"监听出现异常", exc);

    }
}
