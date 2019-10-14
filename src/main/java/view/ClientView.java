package view;

import client.AioTcpClient;

import javax.swing.*;
import java.awt.*;

public class ClientView {
    AioTcpClient client;
    int sessionId;
    public ClientView(AioTcpClient client, int sessionId){
        this.client = client;
        this.sessionId = sessionId;
    }
    public void createPanel() {
        JFrame frame = new JFrame("Wallpaper");
        frame.getContentPane().setLayout(new BorderLayout());
        JPanel panel=new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton button=new JButton("点我");
        JTextField text=new JTextField();

        button.addActionListener((e)-> {
            try {
                client.send(text.getText(), sessionId);
            } catch (Exception ex) {
            }
        });
        panel.add(button);
        frame.getContentPane().add(text, BorderLayout.CENTER);
        frame.getContentPane().add(panel, BorderLayout.EAST);


        frame.setSize(450, 300);
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo (null);
        frame.setVisible (true);
    }
}
