package server;


public class SimpleServer {
    private AioTcpServer server;

    public SimpleServer(AioTcpServerConfig config){
        server = new AioTcpServer();
        server.setConfig(config);
        server.init();
    }

    public void start(){
        new Thread(server).start();
    }

}
