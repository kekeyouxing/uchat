package util.parser;

import java.nio.ByteBuffer;

/**
 * @author Administrator
 */
public abstract class AbstractByteBufferMessageHandler<T> extends AbstractMessageHandler<ByteBuffer, T>{

    ByteBuffer buffer;

    public ByteBuffer getBuffer() {
        return buffer;
    }

    @Override
    protected void parse(ByteBuffer buf) {
        merge(buf);
        parse();
    }

    private void merge(ByteBuffer buf) {
        if(buffer != null){
            if (buffer.hasRemaining()){
                ByteBuffer temp = ByteBuffer.allocate(buffer.remaining()+buf.remaining());
                temp.put(buffer).put(buf).flip();
                buf = temp;
            }else{
                buffer = buf;
            }
        }else {
            buffer = buf;
        }
    }

    /**
     * 抽象解析方法
     */
    abstract void parse();

}
