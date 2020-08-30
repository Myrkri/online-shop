package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogForm extends JFrame implements ActionListener{
    JLabel username = new JLabel("Username");
    JTextField userTxt = new JTextField(15);

    JLabel password = new JLabel("Password");
    JPasswordField txtPassw = new JPasswordField(15);

    JButton login = new JButton("Login");
    JButton forgotPassword = new JButton("Forgot Password");
    JButton register = new JButton("Register");
    JButton cancel = new JButton("Cancel");
    String name;
    String pass;

    LogForm(){

        setSize(295, 200);
        setLayout(new FlowLayout());
        setTitle("Login screen");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        add(username);
        add(userTxt);
        add(password);
        add(txtPassw);
        txtPassw.setEchoChar('*');
        add(login);
        add(forgotPassword);
        add(register);
        add(cancel);

        login.addActionListener(this);
        forgotPassword.addActionListener(this);
        register.addActionListener(this);
        cancel.addActionListener(this);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        int x = getX();
        int y = getY();

        if(e.getSource() == login) {
            name = userTxt.getText();
            pass = txtPassw.getText();
            ConnectDb con = new ConnectDb();

            if (validStr(name) && validStr(pass)) {
                if (validAdmin(name) && validAdmin(pass)) {
                    Admin admin = new Admin();
                    admin.setVisible(true);
                }
                else if (!validAdmin(name) && !validAdmin(pass)){
                    try {
                        con.createCon();
                        con.selectData(name, pass);

                        new GamesBase().resultTable();

                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
            else {
                JOptionPane.showMessageDialog(null, "You entered wrong data, please check your mistakes");
            }
            txtPassw.setText("");
            userTxt.setText("");
        }

        if(e.getSource() == forgotPassword){
            ForgetPass fgpass = new ForgetPass();
            fgpass.setLocation(x, y);
            fgpass.setVisible(true);
        }
        if(e.getSource() == register){
            RegisterForm reg = new RegisterForm();
            reg.setLocation(x, y);
            reg.setVisible(true);
        }

        if(e.getSource() == cancel){
            dispose();
        }
    }
    private static final Pattern VALID_STRING =
            Pattern.compile("[A-Z0-9]", Pattern.CASE_INSENSITIVE);
    private static boolean validStr(String str){
        Matcher mat = VALID_STRING.matcher(str);
        return mat.find();
    }

    private static final Pattern checkLogin = Pattern.compile("admin", Pattern.CASE_INSENSITIVE);
    private static boolean validAdmin(String ad){
        Matcher mat = checkLogin.matcher(ad);
        return mat.find();
    }
}
