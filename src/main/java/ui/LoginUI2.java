package ui;

import model.Client3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.Socket;


/**
 * Created by Samadoll on 2016-11-28.
 */
public class LoginUI2 extends JFrame implements ActionListener {

    private TestUI2 testUI; // change Here for TestUI and TestUI2
    private JTextField userText;
    private JPasswordField passwordField;
    private int remainingTry;
    private Client3 client3;
    private GridBagConstraints gbc;


    public LoginUI2() {
        super("Login");
        client3 = new Client3();
        testUI = new TestUI2(); // Change Here for TestUI and TestUI2
        testUI.setVisible(false);
        client3.addObserver(testUI);
        this.remainingTry = 5;
        this.setSize(450, 230);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        this.add(panel);
        paintComponents(panel);
        this.setVisible(true);
    }

    private void paintComponents(JPanel panel) {
        onCreate(panel);
    }

    private void onCreate(JPanel panel) {

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 3;
        gbc.ipady = 3;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(3, 3, 0, 0);

        JLabel usernameLabel = new JLabel("Username:", SwingConstants.CENTER);
        usernameLabel.setFont(new Font("Serif", Font.PLAIN, 25));
        panel.add(usernameLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 3, 0, 0);

        JLabel passwordLabel = new JLabel("Password:", SwingConstants.CENTER);
        passwordLabel.setFont(new Font("Serif", Font.PLAIN, 25));
        panel.add(passwordLabel, gbc);

        gbc.gridwidth = 3;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(3, 2, 0, 3);

        userText = new JTextField();
        userText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        tryLogin();
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(testUI,e1);
                    }
                }
            }

        });

        panel.add(userText, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(3, 2, 0, 3);

        passwordField = new JPasswordField();
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        tryLogin();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }

        });
        panel.add(passwordField, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(3, 3, 3, 3);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        panel.add(loginButton, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.insets = new Insets(3, 0, 3, 3);


        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new RegisterAction());
        panel.add(registerButton, gbc);
    }

    private String getUsername() {
//        System.out.println(userText.getText());
        return userText.getText();
    }

    private String getPassword() {
//        System.out.println(passwordField.getPassword());
        String pw = "";
        for (char c : passwordField.getPassword()) {
            pw += c;
        }
        return pw;
    }

    public int getRemainingTry() {
        return remainingTry;
    }

    public void setRemainingTry(int attempts) {
        this.remainingTry = attempts;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            tryLogin();
        } catch (IOException e1) {
            JOptionPane.showMessageDialog(testUI, e1);
        }
    }

    private class RegisterAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                fillRegisterInfo();
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(testUI, e1);
            }

        }
    }

    private void fillRegisterInfo() throws IOException {

        Socket socket = new Socket("localhost", 12345);
        client3.setLoginUI2(this);
        try {
            client3.register(getUsername(), getPassword(), socket);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(testUI, e);
        }
    }

    private void tryLogin() throws IOException {
        if (getRemainingTry() < 0)
            throw new IOException("Too many attempts.");

        Socket socket = new Socket("localhost", 12345);
        client3.setLoginUI2(this);
        testUI.setClient(client3);
        client3.login(getUsername(), getPassword(), socket);
        testUI.setTitle("ChatRoom<" + getUsername() + ">");

    }

    public static void main(String[] args) {
        new LoginUI2();
    }
}
