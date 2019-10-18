package common;

import java.nio.ByteBuffer;
import java.util.function.Consumer;

/**
 * @author keyouxing
 */
public interface TcpConnection {

    /**
     *  This method will be called when the client or server receives the data.
     * @param buffer
     */
    public void receive(Consumer<ByteBuffer> buffer);
}
