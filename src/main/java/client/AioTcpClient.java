package client;

import common.AbstractLifecycle;
import common.AioTcpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ThreadPoolUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author keyouxing
 */
public class AioTcpClient extends AbstractLifecycle {

    private Logger logger = LoggerFactory.getLogger(AioTcpClient.class);
    private AsynchronousChannelGroup channelGroup;
    private AtomicInteger idGenerator = new AtomicInteger();
    public AioTcpClientConfig config;
    private ThreadPoolExecutor executor = ThreadPoolUtil.createThreadPool();

    public AioTcpClient(AioTcpClientConfig config){
        this.config = config;
    }
    public int connect(){
        start();
        int sessionId = idGenerator.getAndIncrement();
        connect(config.getHost(), config.getPort(), sessionId);
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
                    AioTcpSession session = new AioTcpSession(channel, sessionId, config);
                    config.getHandler().sessionOpen(session);

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

