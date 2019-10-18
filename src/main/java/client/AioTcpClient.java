package client;

import common.AbstractLifecycle;
import common.Handler;
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

/**
 * @author keyouxing
 */
public class AioTcpClient extends AbstractLifecycle {

    private Logger logger = LoggerFactory.getLogger(AioTcpClient.class);
    private AsynchronousChannelGroup channelGroup;
    public AioTcpClientConfig config;
    private ThreadPoolExecutor executor = ThreadPoolUtil.createThreadPool();

    public AioTcpClient(AioTcpClientConfig config){
        this.config = config;
    }
    public void connect(){
        start();
        connect(config.getHost(), config.getPort());
    }

    public AioTcpClientConfig getConfig() {
        return config;
    }

    private void connect(String host, int port){

        AsynchronousSocketChannel channel;
        try {
            channel = AsynchronousSocketChannel.open(channelGroup);
            channel.setOption(StandardSocketOptions.TCP_NODELAY, true);
            channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            channel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
            channel.connect(new InetSocketAddress(host, port), this, new CompletionHandler<Void, AioTcpClient>() {
                @Override
                public void completed(Void result, AioTcpClient client) {
                    AioTcpClientContext clientContext = new AioTcpClientContext(client.getConfig(), channel);

                    Handler handler = client.getConfig().getHandler();
                    if (handler != null){
                        handler.connectionOpenSuccess(clientContext);
                    }

                }

                @Override
                public void failed(Throwable exc, AioTcpClient attachment) {

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

