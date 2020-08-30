package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterForm extends JFrame implements ActionListener {

    JTextField txt1 = new JTextField(15); //login
    JPasswordField txt2 = new JPasswordField(15); // pass
    JTextField txt3 = new JTextField(15); //email
    JTextField txt4 = new JTextField(15);//secret answer

    JComboBox comboBox = new JComboBox();

    JButton button = new JButton("Register");
    JButton button2 = new JButton("Cancel");

    RegisterForm(){
        setSize(200, 350);
        setLayout(new FlowLayout());
        setTitle("Registration");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);



        comboBox.addItem("Name of your dog");
        comboBox.addItem("Your favorite TV program");
        comboBox.addItem("Your first teacher");

        add(new JLabel("Username"));
        add(txt1);
        add(new JLabel("Password"));
        add(txt2);
        txt2.setEchoChar('*');
        add(new JLabel("Email"));
        add(txt3);
        add(new JLabel("Secret question"));
        add(comboBox);
        add(new JLabel("Your answer"));
        add(txt4);

        add(button);
        add(button2);

        button.addActionListener(this);
        button2.addActionListener(this);

        setVisible(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String name;
        String pass;
        String email;
        String answ;

        if (e.getSource() == button){

            name = txt1.getText();
            pass = txt2.getText();
            email = txt3.getText();
            answ = txt4.getText();

            if (validatEmail(email) && validStr(name) && validStr(answ) && validStr(pass)){
                try {
                    ConnectDb conn = new ConnectDb();
                    conn.createCon();
                    conn.insertData(name, pass, email, answ);
                    JOptionPane.showMessageDialog(null, "Data sent successfully");
                    UserPage user = new UserPage();
                    user.setVisible(true);
                    this.dispose();
                } catch (Exception e1) {
                    System.out.println();
                }
                return;
            }
            else {
                JOptionPane.showMessageDialog(null, "You entered wrong data, please check your mistakes");
                JOptionPane.showMessageDialog(null, "Hint: User name can contain numbers but secret answer must contain only letters \n Email should be valid ");
            }

        }
        if (e.getSource() == button2){
            dispose();
        }
    }
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private static boolean validatEmail(String email){
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        return matcher.find();
    }
    private static final Pattern VALID_STRING =
            Pattern.compile("[A-Z0-9]", Pattern.CASE_INSENSITIVE);
    private static boolean validStr(String str){
        Matcher mat = VALID_STRING.matcher(str);
        return mat.find();
    }
}
