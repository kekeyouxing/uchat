package bootstrap;

import common.*;

import java.util.function.Consumer;
import server.AioTcpServer;
import server.AioTcpServerConfig;
import server.ServerDecoder;
import util.parser.StringParser;

/**
 * @author keyouxing
 */
public class Server extends AbstractLifecycle {
    private AioTcpServer server;
    private Consumer<TcpConnection> accept;
    public Server(AioTcpServerConfig serverConfig){
        this.server = new AioTcpServer(serverConfig);
    }

    private void listen(String host, Integer port){
        this.server.getConfig().setHost(host);
        this.server.getConfig().setPort(port);
        this.server.getConfig().setDecoder(new ServerDecoder());
        this.server.getConfig().setHandler(new ServerHandler());
        start();
    }

    private Server accept(Consumer<TcpConnection> accept) {
        this.accept = accept;
        return this;
    }

    @Override
    public void init() {
        server.listen();
    }

    @Override
    public void destroy() {
    }

    private class ServerHandler implements Handler {
        @Override
        public void connectionOpenSuccess(Context context) {
            //context ---> server.getConfig(), socketChannel
            TcpConnectionImpl connection = new TcpConnectionImpl(context);
            if(accept!=null){
                accept.accept(connection);
            }
        }
    }

    public static void main(String[] args){
        AioTcpServerConfig serverConfig = new AioTcpServerConfig();
        Server server = new Server(serverConfig);

        server.accept(connection -> {
            System.out.println("服务器连接成功");
//            Context context = (AioTcpServerContext)connection.getContext();
//            AsynchronousSocketChannel socketChannel = (AsynchronousSocketChannel)context.getSocketChannel();
            StringParser parser = new StringParser();
            parser.complete(message->{
                message = message.trim();
            });
            parser.complete(message->{
                String msg = message.trim();
                System.out.println("server receive: "+msg);
            });
            connection.receive(parser::receive);
        }).listen("localhost", 9008);

        try {
            Thread.sleep(400000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
