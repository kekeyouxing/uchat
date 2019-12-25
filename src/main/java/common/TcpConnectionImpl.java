package common;

import java.nio.ByteBuffer;
import java.util.function.Consumer;

/**
 * @author keyouxing
 */
public class TcpConnectionImpl implements TcpConnection{
    private Context context;
    public Consumer<ByteBuffer> action;

    public TcpConnectionImpl(Context context) {
        this.context = context;
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
}
