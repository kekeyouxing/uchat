package bootstrap;

import server.AioTcpServer;

public class Server {
    public static void main(String[] args) throws Exception {
        AioTcpServer server = new AioTcpServer(9009);
        new Thread(server).start();
    }
}
