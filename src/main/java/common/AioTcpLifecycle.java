package common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ThreadPoolUtil;

import java.io.IOException;
import java.nio.channels.AsynchronousChannelGroup;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

public class AioTcpLifecycle implements Lifecycle {

    private Logger logger = LoggerFactory.getLogger(AioTcpLifecycle.class);

    protected AtomicInteger idGenerator = new AtomicInteger();
    public AsynchronousChannelGroup channelGroup;
    ThreadPoolExecutor executor = ThreadPoolUtil.createThreadPool();

    public AioTcpWork work;

    protected Config config;
    @Override
    public void init() {
        try {
            if(channelGroup == null){
                channelGroup = AsynchronousChannelGroup.withThreadPool(executor);
            }
            work = new AioTcpWork(new AioTcpListener(config), config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
