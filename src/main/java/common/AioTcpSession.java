package common;

import util.AdaptiveBufferSizePredictor;
import util.BufferSizePredictor;
import util.BufferUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.TimeUnit;

public class AioTcpSession implements Session{
    AsynchronousSocketChannel socket;
    Integer sesssionId;
    private final BufferSizePredictor bufferSizePredictor = new AdaptiveBufferSizePredictor();
    private Config config;
    public AioTcpSession(AsynchronousSocketChannel socket, Integer sessionId, Config config) {
        this.socket = socket;
        this.sesssionId = sessionId;
        this.config = config;
    }

    public AsynchronousSocketChannel getSocket() {
        return socket;
    }

    public void setSocket(AsynchronousSocketChannel socket) {
        this.socket = socket;
    }

    public Integer getSesssionId() {
        return sesssionId;
    }

    public void setSesssionId(Integer sesssionId) {
        this.sesssionId = sesssionId;
    }

    public BufferSizePredictor getBufferSizePredictor() {
        return bufferSizePredictor;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    private ByteBuffer allocateReadBuffer() {
        int size = BufferUtils.normalizeBufferSize(bufferSizePredictor.nextBufferSize());
        return ByteBuffer.allocate(size);
    }
    @Override
    public void read() {
        ByteBuffer buffer = allocateReadBuffer();

        socket.read(buffer, config.getTimeout(), TimeUnit.MILLISECONDS, this, new AioReadHandler(buffer));
    }

    private class AioReadHandler implements CompletionHandler<Integer, AioTcpSession> {

        private ByteBuffer buffer;

        public AioReadHandler(ByteBuffer buffer) {
            this.buffer = buffer;
        }

        @Override
        public void completed(Integer currentReadBytes, AioTcpSession session) {
            // Update the predictor.
            session.bufferSizePredictor.previousReceivedBufferSize(currentReadBytes);
            buffer.flip();
            try {
                config.getDecoder().decode(buffer, session);
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                read();
            }
        }

        @Override
        public void failed(Throwable exc, AioTcpSession session) {

        }
    }

}
