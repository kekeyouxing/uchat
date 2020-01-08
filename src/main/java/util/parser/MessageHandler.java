package util.parser;

import java.util.function.Consumer;

/**
 * @author keyouxing
 */
public interface MessageHandler<R, T> {
    /**
     * This method will be called when the client or server receives the data.
     * @param result Received data
     */
    void receive(R result);

    /**
     * This method will be called when the client or server receives the data completed.
     * @param action
     * @return
     */
    MessageHandler<R, T> complete(Consumer<T> action);

}
