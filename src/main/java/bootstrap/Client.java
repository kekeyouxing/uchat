package bootstrap;

import client.AioTcpClient;
import client.AioTcpClientConfig;
import client.ClientDecoder;
import common.*;
import util.parser.StringParser;

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
            if(accept!=null){
                accept.accept(context.getConnection());
            }
        }
    }

    public static void main(String[] args) {
        Thread thread1 = new Thread(()->{
            AioTcpClientConfig config1 = new AioTcpClientConfig();
            Client client = new Client(config1);
            client.accept(connection -> {
                StringParser parser = new StringParser();
                parser.complete(message ->{
                    String s = message.trim();
                    System.out.println("client receives message -> " + s);
                });
                connection.receive(parser::receive);
                for (int i = 0; i < 5; i++) {
                    connection.write("I am from client1-" + i + "\r\n");
                }
            }).connect("localhost", 9008);
        });
        Thread thread2 = new Thread(()->{
            AioTcpClientConfig config2 = new AioTcpClientConfig();
            Client client2 = new Client(config2);
            client2.accept(connection -> {
                StringParser parser = new StringParser();
                parser.complete(message ->{
                    String s = message.trim();
                    System.out.println("client receives message -> " + s);
                });
                connection.receive(parser::receive);
                for (int i = 6; i < 10; i++) {
                    connection.write("I am from client2-" + i + "\r\n");
                }
            }).connect("localhost", 9008);
        });
        thread1.start();
        thread2.start();
        try {
            Thread.sleep(400000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
