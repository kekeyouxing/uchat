package common;

import java.nio.ByteBuffer;

public interface Session {
    void write(ByteBuffer buffer);
}
