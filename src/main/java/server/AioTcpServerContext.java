package server;

import common.Context;
import common.TcpConnection;
import common.TcpConnectionImpl;
import util.AdaptiveBufferSizePredictor;
import util.BufferSizePredictor;
import util.BufferUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.TimeUnit;

/**
 * @author keyouxing
 */
public class AioTcpServerContext implements Context {
    private AioTcpServerConfig config;
    private AsynchronousSocketChannel socketChannel;
    private final BufferSizePredictor bufferSizePredictor = new AdaptiveBufferSizePredictor();
    private TcpConnection connection;

    public AioTcpServerContext(AioTcpServerConfig config, AsynchronousSocketChannel socketChannel) {
        this.config = config;
        this.socketChannel = socketChannel;
        connection = new TcpConnectionImpl(this);
    }

    private ByteBuffer allocateReadBuffer() {
        int size = BufferUtils.normalizeBufferSize(bufferSizePredictor.nextBufferSize());
        return ByteBuffer.allocate(size);
    }
    @Override
    public AioTcpServerConfig getConfig() {
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

    private void read() {
        ByteBuffer buffer = allocateReadBuffer();
        socketChannel.read(buffer, config.getTimeout(), TimeUnit.MILLISECONDS, this, new AioReadHandler(buffer));
    }
    public void startRead() {
        read();
    }

    private class AioReadHandler implements CompletionHandler<Integer, AioTcpServerContext> {

        private ByteBuffer buffer;

        public AioReadHandler(ByteBuffer buffer) {
            this.buffer = buffer;
        }

        @Override
        public void completed(Integer currentReadBytes, AioTcpServerContext context) {
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
        public void failed(Throwable exc, AioTcpServerContext attachment) {

        }


    }

}
