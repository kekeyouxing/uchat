package client;

import java.util.concurrent.*;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

// 异步连接返回值为Void
// attachment为连接好的通道
public class AioConnectHandler implements CompletionHandler<Void,AsynchronousSocketChannel>
{
    private Integer content = 0;
    
    public AioConnectHandler(Integer value){
        this.content = value;
    }

    @Override
    public void completed(Void attachment, AsynchronousSocketChannel connector) {

        // 向连接好的通道发起异步读
        startRead(connector);

    } 
 
    @Override
    public void failed(Throwable exc, AsynchronousSocketChannel attachment) { 
        exc.printStackTrace(); 
    } 

    public void startRead(AsynchronousSocketChannel socket) {

        ByteBuffer clientBuffer = ByteBuffer.allocate(1024);
        socket.read(clientBuffer, clientBuffer, new AioReadHandler(socket));
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
}

