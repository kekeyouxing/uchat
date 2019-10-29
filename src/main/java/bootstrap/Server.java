package bootstrap;

import common.*;
import server.AioTcpServer;
import server.AioTcpServerConfig;
import server.ServerDecoder;
import common.StringParser;

import java.util.function.Consumer;

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
        server.getConfig().setHost(host);
        server.getConfig().setPort(port);
        start();
    }

    private Server accept(Consumer<TcpConnection> accept) {
        this.accept = accept;
        return this;
    }

    @Override
    public void init(){
        server.getConfig().setDecoder(new ServerDecoder());
        server.getConfig().setHandler(new ServerHandler());
        server.listen();
    }

    @Override
    public void destroy(){

    }

    private class ServerHandler implements Handler {
        @Override
        public void connectionOpenSuccess(Context context) {
            TcpConnectionImpl connection = new TcpConnectionImpl(context);
            if(accept!=null){
                accept.accept(connection);
            }
        }
    }

    public static void main(String[] args){
        AioTcpServerConfig serverConfig = new AioTcpServerConfig();
        Server server = new Server(serverConfig);

        server.accept(connect -> {
            System.out.println("服务器连接成功");
            StringParser parser = new StringParser();
            parser.complete(message->{
                String msg = message.trim();
                System.out.println("client receive: "+msg);
            });
            connect.receive(parser::receive);
        }).listen("localhost", 9008);

        try {
            Thread.sleep(400000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
