package client;

import common.AioTcpLifecycle;
import common.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AioTcpClient extends AioTcpLifecycle {
    Logger logger = LoggerFactory.getLogger(AioTcpClient.class);
    private JTextField jt=new JTextField();
    private static ConcurrentHashMap<Integer, AsynchronousSocketChannel> sockets =new ConcurrentHashMap<>();

    // 客户端静态对象
    private static AioTcpClient me;

    private Config config = new Config();

	// 构造函数初始化异步通道管理器
    private AioTcpClient() {

    }

    public int connect(String host, int port){
        int sessionId = idGenerator.getAndIncrement();
        connect(host, port, sessionId);
        return sessionId;
    }

    private void connect(String host, int port, int sessionId){

        AsynchronousSocketChannel channel;
        try {
            channel = AsynchronousSocketChannel.open(channelGroup);
            channel.setOption(StandardSocketOptions.TCP_NODELAY, true);
            channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            channel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
            channel.connect(new InetSocketAddress(host, port), sessionId, new CompletionHandler<Void, Integer>() {
                @Override
                public void completed(Void result, Integer sessionId) {
                    connect(host, port);
                    sockets.put(sessionId, channel);
                }

                @Override
                public void failed(Throwable exc, Integer attachment) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void start() {
        connect("localhost", 9008);

    }
    
    private void work() throws Exception{
        AioTcpClient client = new AioTcpClient();
        client.start();
    }

    private void send() {

		// 获取0号通道
        AsynchronousSocketChannel socket=sockets.get(0);

        String sendString=jt.getText();
        ByteBuffer clientBuffer=ByteBuffer.wrap(sendString.getBytes(StandardCharsets.UTF_8));

        socket.write(clientBuffer, clientBuffer, new AioSendHandler(socket));
    }
    private void createPanel() {
        me=this;
        JFrame f = new JFrame("Wallpaper");
        f.getContentPane().setLayout(new BorderLayout());       
        
        JPanel p=new JPanel(new FlowLayout(FlowLayout.LEFT));        
        JButton bt=new JButton("点我");
        p.add(bt);
        me=this;
        bt.addActionListener((e)-> {
           try {
                me.send();
            } catch (Exception ex) {
            }
        });
 
        f.getContentPane().add(jt,BorderLayout.CENTER);
        f.getContentPane().add(p, BorderLayout.EAST);
        
        f.setSize(450, 300);
        f.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo (null);
        f.setVisible (true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                AioTcpClient d = new AioTcpClient();
                d.createPanel();
                d.work();
            } catch (Exception ex) {

            }
        });
    } 
}

