package server;

import common.AbstractLifecycle;
import common.AioTcpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ThreadPoolUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author keyouxing
 */
public class AioTcpServer extends AbstractLifecycle {
    private Logger logger = LoggerFactory.getLogger(AioTcpServer.class);

    private AtomicInteger idGenerator = new AtomicInteger();
    private AsynchronousChannelGroup channelGroup;
    public AioTcpServerConfig config;
    private ThreadPoolExecutor executor = ThreadPoolUtil.createThreadPool();

    public AioTcpServer(AioTcpServerConfig config){
        this.config = config;
    }

    public void listen(){
        start();
        listen(bind(config.getHost(), config.getPort()));
    }

    private AsynchronousServerSocketChannel bind(String host, int port){
        AsynchronousServerSocketChannel serverSocket = null;
        try {
            serverSocket = AsynchronousServerSocketChannel.open(channelGroup);
            serverSocket.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            serverSocket.bind(new InetSocketAddress(host, port));
        } catch (Exception e) {
            logger.error("Server bind error", e);
        }
        return serverSocket;
    }

    private void listen(AsynchronousServerSocketChannel serverSocket){
        try {
            serverSocket.accept(idGenerator.getAndIncrement(), new CompletionHandler<AsynchronousSocketChannel, Integer>() {
                @Override
                public void completed(AsynchronousSocketChannel socket, Integer sessionId) {

                    AioTcpSession session = new AioTcpSession(socket, sessionId, config);
                    config.getHandler().sessionOpen(session);

                    listen(serverSocket);
                }

                @Override
                public void failed(Throwable exc, Integer sessionId) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() {
        try {
            channelGroup = AsynchronousChannelGroup.withThreadPool(executor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy(){
        
    }
}