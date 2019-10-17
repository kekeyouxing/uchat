package bootstrap;

import common.*;
import server.AioTcpServer;
import server.AioTcpServerConfig;
import server.ServerDecoder;

import java.util.function.Consumer;

public class Server extends AbstractLifecycle {
    private AioTcpServer server;
    private Consumer<TcpConnection> accept;
    public Server(AioTcpServerConfig serverConfig){
        this.server = new AioTcpServer(serverConfig);
    }

    private void listen(String host, Integer port){
        server.config.setHost(host);
        server.config.setPort(port);
        start();
    }

    private Server accept(Consumer<TcpConnection> accept) {
        this.accept = accept;
        return this;
    }

    @Override
    public void init(){
        server.config.setDecoder(new ServerDecoder());
        server.config.setHandler(new ServerHandler());
        server.listen();
    }

    @Override
    public void destroy(){

    }

    private class ServerHandler implements Handler {
        @Override
        public void sessionOpen(Session session) {
            TcpConnectionImpl connection = new TcpConnectionImpl(session);
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
        }).listen("localhost", 9008);

        try {
            Thread.sleep(400000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
