import com.communication.socket.client.CreateClientConnect;
import com.communication.socket.connect.CreateConnectAbs;
import com.swing.jframe.SwingConsole;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class MainFrame extends JFrame {

    JLabel jLabel;

    JButton jButtonCreateClient = new JButton("CreateClient"), jButton1 = new JButton("jbt1"), jButton2 = new JButton("jbt2");

    private ActionListener jbtActionCreateClient = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            CreateConnectAbs createConnectAbs = new CreateClientConnect("localhost", 8080);
        }
    };

    private ActionListener jbt1 = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            mainFrame.jLabel.setText("This is jbt1 !");
        }
    };

    private ActionListener jbt2 = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            mainFrame.jLabel.setText("This is jbt2 !");
        }
    };

    public MainFrame(String name) {
        super(name);
        jLabel = new JLabel("good");
        setLayout(new FlowLayout());
        jButtonCreateClient.addActionListener(jbtActionCreateClient);
        jButton1.addActionListener(jbt1);
        jButton2.addActionListener(jbt2);
        add(jLabel);
        add(jButton1);
        add(jButton2);
        add(jButtonCreateClient);
    }

    static MainFrame mainFrame, mainFrame1;

    public static void main(String[] args) throws InterruptedException {
        mainFrame = new MainFrame("socket");
        SwingConsole.run(mainFrame, "", 800, 600);
        mainFrame1 = new MainFrame("ServerSocket");
        SwingConsole.run(mainFrame1, "", 800, 600);
//        TimeUnit.SECONDS.sleep(3);
        //插入事件必用方法
//        SwingUtilities.invokeLater(
//                new Runnable() {
//                    public void run() {
//                        mainFrame.jLabel.setText("The shortest distance between two people is a smile !");
//                    }
//                }
//        );
    }
}
