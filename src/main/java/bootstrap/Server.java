package bootstrap;

import server.AioTcpServerConfig;
import server.SimpleServer;

public class Server {
    public static void main(String[] args){
        AioTcpServerConfig serverConfig = new AioTcpServerConfig();
        serverConfig.setHost("localhost");
        serverConfig.setPort(9009);
        SimpleServer server = new SimpleServer(serverConfig);
        server.start();
    }
}
