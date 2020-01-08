package common;

/**
 * @author keyouxing
 */
public interface Connection {
    /**
     * connection和context一一对应
     * @return 返回和connection对应的context
     */
    Context getContext();
}
