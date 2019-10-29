package util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolUtil {
    private static ThreadPoolExecutor threadPool= null;

    public static ThreadPoolExecutor createThreadPool(){
        if(threadPool == null){
            synchronized (ThreadPoolUtil.class){
                if(threadPool == null){
                    int corePoolSize = 50;
                    int maximumPoolSize = 100;
                    long keepAliveTime= 10;
                    BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<>();
                    threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
                    return threadPool;
                }
            }
        }
        return threadPool;
    }
}
