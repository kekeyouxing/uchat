package server;

import common.AbstractLifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ThreadPoolUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author keyouxing
 */
public class AioTcpServer extends AbstractLifecycle {
    private Logger logger = LoggerFactory.getLogger(AioTcpServer.class);

    private AsynchronousChannelGroup channelGroup;
    private AioTcpServerConfig config;
    private ThreadPoolExecutor executor = ThreadPoolUtil.createThreadPool();
    private AioTcpServerAcceptHandler acceptHandler;
    private AsynchronousServerSocketChannel serverSocket;
    public AioTcpServer(AioTcpServerConfig config){
        this.config = config;
    }

    public AioTcpServerConfig getConfig() {
        return config;
    }

    public AsynchronousServerSocketChannel getServerSocket() {
        return serverSocket;
    }

    public void listen(){
        start();
        listen(serverSocket);
    }

    public void listen(AsynchronousServerSocketChannel serverSocket){
        serverSocket.accept(this, acceptHandler);
    }

    private void bind(String host, int port){
        AsynchronousServerSocketChannel serverSocket = null;
        try {
            serverSocket = AsynchronousServerSocketChannel.open(channelGroup);
            serverSocket.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            serverSocket.bind(new InetSocketAddress(host, port));
        } catch (Exception e) {
            logger.error("Server bind error", e);
        }
        this.serverSocket = serverSocket;
    }

    @Override
    public void init() {
        try {
            channelGroup = AsynchronousChannelGroup.withThreadPool(executor);
            acceptHandler = new AioTcpServerAcceptHandler();
            bind(config.getHost(), config.getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy(){
        
    }
}