package ui;

import model.Client;
import model.Client3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.Socket;
import java.util.*;
import java.util.List;

/**
 * Created by Samadoll on 2016-11-30.
 */
public class TestUI extends JFrame implements Observer {

    private JTextArea inText;
    private JTextArea inputArea;
    private JButton send;
    private Socket socket;
    private JTextArea onlinenum;
    private Client3 client3;
    private GridBagConstraints gbc;


    public TestUI() {
        super("ChatRoom");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 500);
        this.setVisible(false);
        JPanel jPanel = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        this.add(jPanel);
        onCreate(jPanel);
        inText.setText("");
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setClient(Client3 client) {
        this.client3 = client;
    }

    @Override
    public void update(Observable o, Object arg) {

        switch ((String) arg) {
            case "/Registered":
                JOptionPane.showMessageDialog(this, "Congratulation! You Have Finished Your Registration, Now Heading To Login");
                break;
            case "/Show":
                setVisible(true);
                inText.setText(welcomeWords());
                break;
            case "/onlineList/":
                Client3 client3 = (Client3) o;
                List<String> userList = client3.getUserList();
                String finaluser = "";
                for (String name : userList) {
                    finaluser = finaluser + "\n" +name;
                }
                onlinenum.setText("Online People" + finaluser);
                onlinenum.setCaretPosition(onlinenum.getText().length());
                break;
            default:
                Date now = new Date();
                inText.append("\n\n" + "<" + now + ">" + "\n" + arg);
                inText.setCaretPosition(inText.getText().length());
        }
    }

    private void onCreate(JPanel jPanel) {
        inText = new JTextArea();
        inText.setEditable(false);
        inText.setVisible(true);
        inText.setLineWrap(true);
        inText.setWrapStyleWord(true);

        onlinenum = new JTextArea();
        onlinenum.setVisible(true);
        onlinenum.setEditable(false);
        inText.setLineWrap(true);
        inText.setWrapStyleWord(true);

        inputArea = new JTextArea();
        inputArea.setEditable(true);
        inputArea.setVisible(true);
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        inputArea.addKeyListener(new KeyAdapter() {

            boolean ctrlPressed;
            boolean enterPressed;

            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    enterPressed = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                    ctrlPressed = true;
                }
                if (ctrlPressed && enterPressed) {
                    try {
                        if (!inputArea.getText().equals(""))
                            client3.sendMsg(inputArea.getText().trim());
                    } catch (IOException e1) {
                    }
                    inputArea.setText("");
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    enterPressed = false;
                }
                if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                    ctrlPressed = false;
                }
            }
        });

        send = new JButton("Send");
        send.setVisible(true);
        send.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (!inputArea.getText().equals(""))
                        client3.sendMsg(inputArea.getText());
                } catch (IOException e1) {
                }
                inputArea.setText("");
            }
        });

        JScrollPane j1 = new JScrollPane(inText);
        JScrollPane j2 = new JScrollPane(inputArea);
        JScrollPane j3 = new JScrollPane(onlinenum);


        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.ipady = 60;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(3, 3, 0, 3);

        jPanel.add(j1, gbc);

        gbc.gridx = 1;
        jPanel.add(j3, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.ipady = 30;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 3, 3, 2);

        jPanel.add(j2, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 0, 3, 3);

        jPanel.add(send, gbc);
    }

    private String welcomeWords() {
        return "Function Key: \n" +  "/online to check who's online \n"
                + "/ToXXXX: or /ToXXX/XXX: to send private message to one user or mulitple users \n"
                + "Welcome. Chat now.";

    }
}
