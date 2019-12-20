package common;


import java.nio.ByteBuffer;
import java.util.function.Consumer;

/**
 * @author keyouxing
 */
public interface TcpConnection extends Connection{
    /**
     * @param action
     * @return
     */
    TcpConnection receive(Consumer<ByteBuffer> action);
}
