package ui;

import javax.swing.*;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Samadoll on 2016-11-30.
 */
public class TestUI extends JFrame implements Observer{

    JTextArea inText;
    private Socket socket;


    public TestUI() {
        super("dsf");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500,500);
        this.setVisible(false);
        JPanel jPanel = new JPanel();
        this.add(jPanel);
        jPanel.setLayout(null);
        onCreate(jPanel);
        showLoginUI();
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    private void showLoginUI() {
        new LoginUI2(this);
    }

    public static void main(String[] args) {
        new TestUI();
    }

    @Override
    public void update(Observable o, Object arg) {
        switch ((String) arg) {
            case "/exit":
                System.exit(0);
                break;
            default:
                inText.setText((String) arg);
        }
    }

    private void onCreate(JPanel jPanel) {
        inText = new JTextArea();
        inText.setBounds(10,10, 400, 300);
        inText.setEditable(false);
        inText.setVisible(true);

        jPanel.add(inText);
    }

}
