package bootstrap;

import common.ServerDecoder;
import common.TcpConnection;
import server.AioTcpServer;
import server.AioTcpServerConfig;

import java.util.function.Consumer;

public class Server {
    private Consumer<TcpConnection> accept;
    AioTcpServer server;
    public Server accept(Consumer<TcpConnection> consumer){
        this.accept = consumer;
        return this;
    }

    public Server(AioTcpServer server){
        this.server = server;
    }

    public static void main(String[] args){

        AioTcpServerConfig serverConfig = new AioTcpServerConfig();
        serverConfig.setHost("localhost");
        serverConfig.setPort(9008);

        ServerDecoder decoder = new ServerDecoder();
        serverConfig.setDecoder(decoder);

        AioTcpServer server = new AioTcpServer(serverConfig);

        server.start();
    }
}
