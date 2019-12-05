package util.parser;

import common.AbstractLifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

/**
 * @author keyouxing
 */
public abstract class AbstractMessageHandler<R, T> implements MessageHandler<R, T> {

    Logger logger = LoggerFactory.getLogger(AbstractLifecycle.class);
    public Consumer<T> action;
    @Override
    public void receive(R obj) {
        check();
        parse(obj);
    }

    /**
     * @param obj
     */
    protected abstract void parse(R obj);

    private void check() {
        if(this.action == null){
            logger.error("message accept method is null");
        }
    }

    @Override
    public MessageHandler<R, T> complete(Consumer<T> action) {
        this.action = action;
        return this;
    }
}
