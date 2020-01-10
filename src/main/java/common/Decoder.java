package common;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.CharacterCodingException;

public interface Decoder {

    public void decode(ByteBuffer buffer, TcpConnection connection) throws IOException;
}
