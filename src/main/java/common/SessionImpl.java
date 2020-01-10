package common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class SessionImpl implements Session{
    private static Logger log = LoggerFactory.getLogger(SessionImpl.class);
    private final AsynchronousSocketChannel socketChannel;
    private final ReentrantLock writeLock = new ReentrantLock();
    private final AtomicBoolean waitingForClose = new AtomicBoolean(false);
    private boolean isWriting = false;
    private LinkedList<ByteBufferOutputEntry> entries = new LinkedList<>();
    private Config config;
    private final AtomicBoolean closed = new AtomicBoolean(false);

    public SessionImpl(AsynchronousSocketChannel socketChannel, Config config) {
        this.socketChannel = socketChannel;
        this.config = config;
    }

    class OutPutCompletionHandler<T> implements CompletionHandler<Integer, SessionImpl> {

        private OutputEntry entry;
        public OutPutCompletionHandler(OutputEntry<T> entry){
            this.entry = entry;
        }
        @Override
        public void completed(Integer result, SessionImpl attachment) {
            if(result < 0){
                log.info("The output channel is shutdown...");
                closeNow();
                return;
            }

            if(entry.remaining() > 0){
                _write(entry);
            }else {
                writingCompleted();
            }
        }

        @Override
        public void failed(Throwable exc, SessionImpl attachment) {

        }
    }

    private void writingCompleted() {
        OutputEntry entry = getNextOutputEntry();
        if(entry != null){
            _write(entry);
        }
    }

    private OutputEntry<?> getNextOutputEntry() {
        OutputEntry<?> entry = null;
        writeLock.lock();
        try {
            if(entries.isEmpty()){
                isWriting = false;
            }else {
                entry = entries.poll();
            }
        }finally {
            writeLock.unlock();
        }
        return entry;
    }

    private void closeNow() {
        if(closed.compareAndSet(false,true)){
            try {
                socketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {

            }
        }else {
            log.info("session is closed");
        }
    }

    @Override
    public void write(ByteBuffer buffer) {
        write(new ByteBufferOutputEntry(buffer));
    }

    private void write(ByteBufferOutputEntry entry) {

        if (entry==null){
            log.warn("entry is null... The entry can't write to endpoint");
            return;
        }
        if (waitingForClose.get() && entry.getOutPutEntryType() == OutputEntryType.DISCONNECTION){
            log.warn("session is waiting close... The entry can't write to endpoint");
            return;
        }
        boolean writeEntry = false;
        writeLock.lock();
        try {
            if(!isWriting){
                isWriting = true;
                writeEntry = true;
            }else {
                entries.offer(entry);
            }

        }finally {
            writeLock.unlock();
        }
        if(writeEntry){
            _write(entry);
        }
    }

    private void _write(OutputEntry<?> entry) {
        switch (entry.getOutPutEntryType()) {
            case BYTE_BUFFER: {
                ByteBufferOutputEntry bufferOutputEntry = (ByteBufferOutputEntry)entry;
                socketChannel.write(bufferOutputEntry.getData(), config.getTimeout(), TimeUnit.MILLISECONDS, this,
                        new OutPutCompletionHandler<>(bufferOutputEntry));
            }
            break;
        }
    }
}
