package server;

import common.AioTcpLifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AioTcpServer extends AioTcpLifecycle implements Runnable {
    Logger logger = LoggerFactory.getLogger(AioTcpServer.class);
    private AsynchronousServerSocketChannel serverSocket;

    public AioTcpServer(int port) throws Exception {
        //创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(20);

        serverSocket = AsynchronousServerSocketChannel.open(channelGroup).bind(new InetSocketAddress(port));
    }
 
    public void run() {
        accept(serverSocket);
        try {
            Thread.currentThread().sleep(400000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void accept(AsynchronousServerSocketChannel serverSocket){
        try {
            serverSocket.accept(idGenerator.getAndIncrement(), new CompletionHandler<AsynchronousSocketChannel, Integer>() {
                @Override
                public void completed(AsynchronousSocketChannel socket, Integer sessionId) {
                    accept(serverSocket);
                    startRead(socket);
                }

                @Override
                public void failed(Throwable exc, Integer attachment) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startRead(AsynchronousSocketChannel socket) {

        ByteBuffer clientBuffer = ByteBuffer.allocate(1024);

        AioReadHandler rd=new AioReadHandler(socket);
        // 读数据到clientBuffer, 同时将clientBuffer作为attachment
        socket.read(clientBuffer, clientBuffer, rd);

    }
    public static void main(String... args) throws Exception {
        AioTcpServer server = new AioTcpServer(9008);
        new Thread(server).start();
    } 
}
