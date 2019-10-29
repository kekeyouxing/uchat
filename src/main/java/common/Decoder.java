package common;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;

public interface Decoder {

    public void decode(ByteBuffer buffer, AioTcpSession session) throws IOException;
}
