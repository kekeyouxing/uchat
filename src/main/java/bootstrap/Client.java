package bootstrap;

import client.AioTcpClient;
import view.ClientView;

public class Client {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                AioTcpClient client = new AioTcpClient();
                int sessionId = client.connect("localhost", 9010);
                ClientView view = new ClientView(client, sessionId);
                view.createPanel();
            } catch (Exception e) {

            }
        });

    }
}
