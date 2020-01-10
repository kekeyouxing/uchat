package client;

import common.Config;
import common.Context;
import common.TcpConnection;
import common.TcpConnectionImpl;
import server.AioTcpServerContext;
import util.AdaptiveBufferSizePredictor;
import util.BufferSizePredictor;
import util.BufferUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

/**
 * @author keyouxing
 */
public class AioTcpClientContext implements Context {

    private final AioTcpClientConfig config;
    private final AsynchronousSocketChannel socketChannel;
    private TcpConnectionImpl connection;
    private final BufferSizePredictor bufferSizePredictor = new AdaptiveBufferSizePredictor();
    public AioTcpClientContext(AioTcpClientConfig config, AsynchronousSocketChannel socketChannel) {
        this.config = config;
        this.socketChannel = socketChannel;
        this.connection = new TcpConnectionImpl(this);
    }

    @Override
    public Config getConfig() {
        return config;
    }

    @Override
    public AsynchronousSocketChannel getSocketChannel() {
        return socketChannel;
    }

    @Override
    public TcpConnection getConnection() {
        return connection;
    }
    private class AioReadHandler implements CompletionHandler<Integer, AioTcpClientContext> {

        private ByteBuffer buffer;

        public AioReadHandler(ByteBuffer buffer) {
            this.buffer = buffer;
        }

        @Override
        public void completed(Integer currentReadBytes, AioTcpClientContext context) {
            bufferSizePredictor.previousReceivedBufferSize(currentReadBytes);
            buffer.flip();
            try {
                config.getDecoder().decode(buffer, connection);
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                read();
            }
        }

        @Override
        public void failed(Throwable exc, AioTcpClientContext attachment) {

        }


    }

    public void startRead() {
        read();
    }
    private void read() {
        ByteBuffer buffer = allocateReadBuffer();
        socketChannel.read(buffer, config.getTimeout(), TimeUnit.MILLISECONDS, this, new AioReadHandler(buffer));
    }
    private ByteBuffer allocateReadBuffer() {
        int size = BufferUtils.normalizeBufferSize(bufferSizePredictor.nextBufferSize());
        return ByteBuffer.allocate(size);
    }
}
