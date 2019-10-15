package common;

import client.AioTcpClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.AioTcpServerConfig;
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

    protected AioTcpClientConfig clientConfig;
    protected AioTcpServerConfig serverConfig;

    protected AioTcpListener listener;
    @Override
    public void init() {
        try {
            if(clientConfig == null){
                clientConfig = new AioTcpClientConfig();
            }
            if(serverConfig == null){
                //serverConfig = new AioTcpServerConfig();
            }
            if(listener == null){
                listener = new AioTcpListener();
            }
            channelGroup = AsynchronousChannelGroup.withThreadPool(executor);
            work = new AioTcpWork(listener);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
