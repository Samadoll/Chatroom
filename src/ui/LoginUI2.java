package ui;

import model.Client3;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


/**
 * Created by Samadoll on 2016-11-28.
 */
public class LoginUI2 extends JFrame implements ActionListener{

    private TestUI testUI;

    private JTextField userText;
    private JPasswordField passwordField;
    private String aChoice;
    private int remainingTry;
    private Client3 client3;


    public LoginUI2(TestUI testUI) {
        super("Login");
        client3 = new Client3(testUI);
        this.remainingTry = 5;
        this.testUI = testUI;
        aChoice = "";
        this.setSize(350,200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        this.add(panel);
        paintComponents(panel);
        this.setVisible(true);
        testUI.setVisible(false);
    }

    private void paintComponents(JPanel panel) {
        panel.setLayout(null);
        onCreate(panel);
    }

    private void onCreate(JPanel panel) {

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(10,40,80,25);
        panel.add(usernameLabel);

        userText = new JTextField(20);
        userText.setBounds(100,40,165,25);
        panel.add(userText);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10,70,80,25);
        panel.add(passwordLabel);
        passwordField = new JPasswordField(20);
        passwordField.setBounds(100,70,165,25);
        panel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(50,100,80,25);
        loginButton.addActionListener(this);
        panel.add(loginButton);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(150,100,80,25);
        registerButton.addActionListener(new RegisterAction());
        panel.add(registerButton);
    }

    private String getChoice() {
//        System.out.println(aChoice);
        return aChoice;
    }

    private void setChoice(String choice) {
        this.aChoice = choice;
    }

    private String getUsername() {
//        System.out.println(userText.getText());
        return userText.getText();
    }

    private String getPassword() {
//        System.out.println(passwordField.getPassword());
        String pw = "";
        for (char c: passwordField.getPassword()) {
            pw += c;
        }
        return pw;
    }

    private int getRemainingTry() {
        return remainingTry;
    }

    private void setRemainingTry(int attempts) {
        this.remainingTry = attempts;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        setChoice("1");
        getChoice();
        getUsername();
        getPassword();
        try {
            tryLogin();
        } catch (IOException e1) {
            JOptionPane.showMessageDialog(testUI, e1);
        }
    }

    private class RegisterAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            setChoice("2");
            getChoice();
            getUsername();
            getPassword();
            try {
                fillRegisterInfo();
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(testUI, e1);
            }

        }
    }

    private void fillRegisterInfo() throws IOException{

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
        client3.login(getUsername(), getPassword(), socket);


    }
}
