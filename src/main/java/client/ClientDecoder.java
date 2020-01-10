package client;

import common.Decoder;
import common.TcpConnection;
import common.TcpConnectionImpl;

import java.nio.ByteBuffer;

/**
 * @author keyouxing
 */
public class ClientDecoder implements Decoder {

    @Override
    public void decode(ByteBuffer buffer, TcpConnection connection) {
        TcpConnectionImpl con = (TcpConnectionImpl)connection;
        if (con.action != null){
            con.action.accept(buffer);
        }
    }
}
