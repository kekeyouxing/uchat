package common;

/**
 * @author keyouxing
 */
public class Config {

    /**
     *  The maximum time for the I/O operation to complete
     */
    private int timeout = 30 * 1000;

    private Decoder decoder;

    private Handler handler;


    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public Decoder getDecoder() {
        return decoder;
    }

    public void setDecoder(Decoder decoder) {
        this.decoder = decoder;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }
}
