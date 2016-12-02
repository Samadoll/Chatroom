package ui;

import model.Client3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.*;

/**
 * Created by Samadoll on 2016-11-30.
 */
public class TestUI extends JFrame implements Observer {

    private JTextArea inText;
    private JTextArea inputArea;
    private JButton send;
    private Socket socket;
    private Client3 client3;


    public TestUI() {
        super("dsf");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 500);
        this.setVisible(false);
        JPanel jPanel = new JPanel();
        this.add(jPanel);
        jPanel.setLayout(null);
        onCreate(jPanel);
        inText.setText("");
        //showLoginUI();
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    private void showLoginUI() {
        new LoginUI2();
    }

    public void setClient(Client3 client) {
        this.client3 = client;
    }

    @Override
    public void update(Observable o, Object arg) {

        switch ((String) arg) {
            case "/exit":
                System.exit(0);
                break;
            case "/Registered":
                //inText.setText("Congratulations! You have successfully registered.Now heading to Login ");
                JOptionPane.showMessageDialog(this, "Congratulation! You Have Finished Your Registration, Now Heading To Login");
                break;
            case "/Show":
                setVisible(true);
                inText.setText(welcomeWords());
                break;
            default:
                Date now = new Date();
                inText.append("\n\n"+ "<" + now +">" + "\n" + arg);
                inText.setCaretPosition(inText.getText().length());
        }

    }

    private void onCreate(JPanel jPanel) {
        inText = new JTextArea();
//        inText.setBounds(10, 10, 400, 300);
        inText.setEditable(false);
        inText.setVisible(true);
        inText.setLineWrap(true);
        inText.setWrapStyleWord(true);

        inputArea = new JTextArea();
//        inputArea.setBounds(10,315,350,100);
        inputArea.setEditable(true);
        inputArea.setVisible(true);
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);

        send = new JButton("Send");
        send.setBounds(360,315,50,100);
        send.setVisible(true);
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (!inputArea.getText().equals(""))
                        client3.sendMsg(inputArea.getText().trim());
                } catch (IOException e1) {}
                inputArea.setText("");
            }
        });

        JScrollPane j1 = new JScrollPane(inText);
        j1.setBounds(10,10,400,300);
        JScrollPane j2 = new JScrollPane(inputArea);
        j2.setBounds(10,315,350,100);

        jPanel.add(j1);
        jPanel.add(j2);
        jPanel.add(send);
    }

    private String welcomeWords() {
        return "Function Key: \n" + "/exit to exit \n" + "/online to check who's online \n"
                + "/ToXXXX: or /ToXXX/XXX: to send private message to one user or mulitple users \n"
                + "Welcome. Chat now.";

    }
}
