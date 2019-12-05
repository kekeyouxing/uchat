package bootstrap;

import client.AioTcpClient;
import client.AioTcpClientConfig;
import client.ClientDecoder;
import common.*;
import server.ServerDecoder;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * @author keyouxing
 */
public class Client extends AbstractLifecycle {

    private AioTcpClient client;
    private Consumer<TcpConnection> accept;
    public Client(AioTcpClientConfig config){
        this.client = new AioTcpClient(config);
    }

    public void connect(String host, int port){
        client.config.setHost(host);
        client.config.setPort(port);
        start();
    }

    public Client accept(Consumer<TcpConnection> accept){
        this.accept = accept;
        return this;
    }
    @Override
    public void init() {
        client.config.setDecoder(new ClientDecoder());
        client.config.setHandler(new ClientHandler());
        client.connect();
    }

    @Override
    public void destroy() {

    }

    private class ClientHandler implements Handler {
        @Override
        public void connectionOpenSuccess(Context context) {
            TcpConnectionImpl connection = new TcpConnectionImpl(context);
            if(accept!=null){
                accept.accept(connection);
            }
        }
    }

    public static void main(String[] args) {
        AioTcpClientConfig config = new AioTcpClientConfig();
        Client client = new Client(config);

        client.accept(connection -> {
            System.out.println("客户端连接成功");
        }).connect("localhost", 9008);

        try {
            Thread.sleep(400000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
