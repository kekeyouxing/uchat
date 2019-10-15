package bootstrap;

import client.AioTcpClient;
import client.AioTcpClientConfig;

public class Client {
    public static void main(String[] args) {

        try {
            AioTcpClientConfig config = new AioTcpClientConfig();
            AioTcpClient client = new AioTcpClient(config);

        } catch (Exception e) {

        }

    }
}
