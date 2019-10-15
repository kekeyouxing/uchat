package client;

import common.AioTcpLifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;

public class AioTcpClient extends AioTcpLifecycle {
    Logger logger = LoggerFactory.getLogger(AioTcpClient.class);

//    AsynchronousSocketChannel socketTest;
    public AioTcpClient(AioTcpClientConfig config){
        this.config = config;
        super.init();
    }
    public int connect(String host, int port){
        int sessionId = idGenerator.getAndIncrement();
        connect(host, port, sessionId);
        return sessionId;
    }


    private void connect(String host, int port, int sessionId){

        AsynchronousSocketChannel channel;
        try {
            channel = AsynchronousSocketChannel.open(channelGroup);
            channel.setOption(StandardSocketOptions.TCP_NODELAY, true);
            channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            channel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
            channel.connect(new InetSocketAddress(host, port), sessionId, new CompletionHandler<Void, Integer>() {
                @Override
                public void completed(Void result, Integer sessionId) {
                    work.registerSession(channel, sessionId);
                }

                @Override
                public void failed(Throwable exc, Integer attachment) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void write(AsynchronousSocketChannel socket, String content){

        ByteBuffer clientBuffer=ByteBuffer.wrap(content.getBytes(StandardCharsets.UTF_8));

        socket.write(clientBuffer, clientBuffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer byteBuffer) {

            }

            @Override
            public void failed(Throwable exc, ByteBuffer byteBuffer) {

            }
        });
    }

//    @Override
//    public void run() {
//
//        write(socketTest,"你好");
//        try {
//            Thread.currentThread().sleep(400000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//    public void start(String host, int port){
//        connect(host, port);
//        new Thread(this).start();
//    }
}

