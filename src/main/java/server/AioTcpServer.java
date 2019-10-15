package server;

import common.AioTcpLifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AioTcpServer extends AioTcpLifecycle implements Runnable {
    Logger logger = LoggerFactory.getLogger(AioTcpServer.class);

    private AsynchronousServerSocketChannel serverSocket;

    public AioTcpServer(AioTcpServerConfig config){

        this.serverConfig = config;
        bind(serverConfig.getHost(), serverConfig.getPort());
        super.init();
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

    public void run() {
        listen(serverSocket);
        try {
            Thread.currentThread().sleep(400000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void listen(AsynchronousServerSocketChannel serverSocket){
        try {
            serverSocket.accept(idGenerator.getAndIncrement(), new CompletionHandler<AsynchronousSocketChannel, Integer>() {
                @Override
                public void completed(AsynchronousSocketChannel socket, Integer sessionId) {
                    try {
                        work.registerSession(socket, sessionId);
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }finally {
                        listen(serverSocket);
                    }

                }

                @Override
                public void failed(Throwable exc, Integer sessionId) {
                    try {
                        listener.failedOpeningSession(sessionId, exc);
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void start(){
        new Thread(this).start();
    }
}