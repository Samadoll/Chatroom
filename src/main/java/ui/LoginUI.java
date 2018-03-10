//package ui;
//
//import model.Client3;
//
//import javax.swing.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.Socket;
//
//
///**
// * Created by Samadoll on 2016-11-28.
// */
//public class LoginUI extends JFrame implements ActionListener{
//
//    private TestUI testUI;
//
//    private JTextField userText;
//    private JPasswordField passwordField;
//    private String aChoice;
//    private int remainingTry;
//
//
//    public LoginUI(TestUI testUI) {
//        super("Login");
//        this.remainingTry = 5;
//        this.testUI = testUI;
//        aChoice = "";
//        this.setSize(350,200);
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        JPanel panel = new JPanel();
//        this.add(panel);
//        paintComponents(panel);
//        this.setVisible(true);
//        testUI.setVisible(false);
//    }
//
//    private void paintComponents(JPanel panel) {
//        panel.setLayout(null);
//        onCreate(panel);
//    }
//
//    private void onCreate(JPanel panel) {
//
//        JLabel usernameLabel = new JLabel("Username:");
//        usernameLabel.setBounds(10,40,80,25);
//        panel.add(usernameLabel);
//
//        userText = new JTextField(20);
//        userText.setBounds(100,40,165,25);
//        panel.add(userText);
//
//        JLabel passwordLabel = new JLabel("Password:");
//        passwordLabel.setBounds(10,70,80,25);
//        panel.add(passwordLabel);
//        passwordField = new JPasswordField(20);
//        passwordField.setBounds(100,70,165,25);
//        panel.add(passwordField);
//
//        JButton loginButton = new JButton("Login");
//        loginButton.setBounds(50,100,80,25);
//        loginButton.addActionListener(this);
//        panel.add(loginButton);
//
//        JButton registerButton = new JButton("Register");
//        registerButton.setBounds(150,100,80,25);
//        registerButton.addActionListener(new RegisterAction());
//        panel.add(registerButton);
//    }
//
//    private String getChoice() {
////        System.out.println(aChoice);
//        return aChoice;
//    }
//
//    private void setChoice(String choice) {
//        this.aChoice = choice;
//    }
//
//    private String getUsername() {
////        System.out.println(userText.getText());
//        return userText.getText();
//    }
//
//    private String getPassword() {
////        System.out.println(passwordField.getPassword());
//        String pw = "";
//        for (char c: passwordField.getPassword()) {
//            pw += c;
//        }
//        return pw;
//    }
//
//    private int getRemainingTry() {
//        return remainingTry;
//    }
//
//    private void setRemainingTry(int attempts) {
//        this.remainingTry = attempts;
//    }
//
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        setChoice("1");
//        getChoice();
//        getUsername();
//        getPassword();
//        try {
//            tryLogin();
//        } catch (IOException e1) {
//            JOptionPane.showMessageDialog(testUI, e1);
//        }
//    }
//
//    private class RegisterAction implements ActionListener {
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            setChoice("2");
//            getChoice();
//            getUsername();
//            getPassword();
//            try {
//                fillRegisterInfo();
//            } catch (IOException e1) {
//                JOptionPane.showMessageDialog(testUI, "Sorry, the username already exists. Please Try Again.");
//            }
//
//        }
//    }
//
//    private void fillRegisterInfo() throws IOException{
//        String name = getUsername();
//        String pw = getPassword();
//        if (!name.matches("([A-Z]|[a-z])+") || pw == null || pw.length() < 3) {
//            JOptionPane.showMessageDialog(testUI, "Sorry, Your New Username Is Bad, Please Make A New One.");
//        } else {
//            Socket socket = new Socket("localhost", 12345);
//            PrintWriter tempOut = new PrintWriter(socket.getOutputStream(), true);
//            BufferedReader tempIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//
//            tempOut.println(name);
//            tempOut.println(pw);
//            tempOut.println("2");
//            String back = tempIn.readLine();
//            if (back.equals("true"))
//                JOptionPane.showMessageDialog(testUI, "Congratulation! You Have Finished Your Registration, Now Heading To Login");
//            else
//                throw new IOException();
//
//            tempIn.close();
//            tempOut.close();
//            socket.close();
//        }
//    }
//
//    private void tryLogin() throws IOException {
//        if (getRemainingTry() < 0)
//            throw new IOException("Too many attempts.");
//
//        Socket socket = new Socket("localhost", 12345);
//        BufferedReader tempIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//        PrintWriter tempOut = new PrintWriter(socket.getOutputStream(), true);
//
//        tempOut.println(getUsername());
//        tempOut.println(getPassword());
//        tempOut.println("1");
//        String back = tempIn.readLine();
//
//        if (back.equals("true")) {
//            this.setVisible(false);
//            testUI.setVisible(true);
////            Client3 client3 = new Client3(socket);
////            client3.addObserver(testUI);
//        } else {
//            setRemainingTry(getRemainingTry() - 1);
//            socket.close();
//            tempIn.close();
//            tempOut.close();
//            throw new IOException("Login fails. Please Try Again");
//        }
//    }
//}
