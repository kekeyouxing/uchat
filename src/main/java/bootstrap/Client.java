package bootstrap;

import client.AioTcpClient;
import view.ClientView;

public class Client {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                AioTcpClient client = new AioTcpClient();
                int sessionId = client.connect("localhost", 9009);
                ClientView view = new ClientView(client, sessionId);
                view.createPanel();

                sessionId = client.connect("localhost", 9009);
                ClientView view1 = new ClientView(client, sessionId);
                view1.createPanel();
            } catch (Exception e) {

            }
        });

    }
}
