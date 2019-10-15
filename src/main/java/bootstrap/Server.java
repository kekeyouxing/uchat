package bootstrap;

import server.AioTcpServer;
import server.AioTcpServerConfig;

public class Server {
    public static void main(String[] args){
        AioTcpServerConfig serverConfig = new AioTcpServerConfig();
        serverConfig.setHost("localhost");
        serverConfig.setPort(9010);

        AioTcpServer server = new AioTcpServer(serverConfig);
        server.start();
    }
}
