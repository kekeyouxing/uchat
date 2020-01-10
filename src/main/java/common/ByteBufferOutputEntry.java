package common;

import java.nio.ByteBuffer;

public class ByteBufferOutputEntry extends AbstractOutputEntry<ByteBuffer>{

    public ByteBufferOutputEntry(ByteBuffer data){
        super(data);
    }
    @Override
    public OutputEntryType getOutPutEntryType() {
        return OutputEntryType.BYTE_BUFFER;
    }

    @Override
    public long remaining() {
        return getData().remaining();
    }
}
