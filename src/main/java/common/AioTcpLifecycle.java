package common;

import util.ThreadPoolUtil;

import java.io.IOException;
import java.nio.channels.AsynchronousChannelGroup;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

public class AioTcpLifecycle implements Lifecycle {

    protected AtomicInteger idGenerator = new AtomicInteger();
    public AsynchronousChannelGroup channelGroup;
    ThreadPoolExecutor executor = ThreadPoolUtil.createThreadPool();

    @Override
    public void init() {
        try {
            channelGroup = AsynchronousChannelGroup.withThreadPool(executor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
