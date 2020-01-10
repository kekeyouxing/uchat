package common;

import util.BufferUtils;

import java.nio.ByteBuffer;
import java.util.function.Consumer;

/**
 * @author keyouxing
 */
public class TcpConnectionImpl implements TcpConnection{
    private Context context;
    public Consumer<ByteBuffer> action;
    private static final String DEFAULT_CHARSET = "utf-8";
    private Session session;

    public TcpConnectionImpl(Context context) {
        this.context = context;
        this.session = new SessionImpl(context.getSocketChannel(), context.getConfig());
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public TcpConnection receive(Consumer<ByteBuffer> action) {
        this.action = action;
        return this;
    }

    @Override
    public TcpConnection write(String message) {
        return write(message, DEFAULT_CHARSET);
    }

    private TcpConnection write(String message, String charset) {
        ByteBuffer buffer = BufferUtils.toBuffer(message);
        session.write(buffer);
        return this;
    }
}
