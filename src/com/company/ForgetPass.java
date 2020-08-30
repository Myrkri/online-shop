package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ForgetPass extends JFrame implements ActionListener {


    JPanel pn = new JPanel(new GridLayout(6,5, 5,5));


    JPanel pn1 = new JPanel();

    JLabel lbl1 = new JLabel("Username");
    JTextField txt1 = new JTextField(15);

    JLabel lbl2 = new JLabel("Secret Question");
    JComboBox cmb1 = new JComboBox();

    JLabel lbl3 = new JLabel("Secret Question Answer");
    JTextField txt2 = new JTextField(15);

    JLabel lbl4 = new JLabel("New Password");

    JTextField txt3 = new JTextField(15);

    JButton btn1 = new JButton("Submit");
    JButton btn2 = new JButton("Cancel");

    ForgetPass(){
        setSize(450,200);
        setTitle("Forget Password");
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        add(pn);

        pn.add(lbl1);

        pn.add(txt1);

        pn.add(lbl2);
        cmb1.addItem("Name of your Dog");
        cmb1.addItem("Your favorite TV Program");
        cmb1.addItem("Name of your Wife");
        pn.add(cmb1);

        pn.add(lbl3);
        pn.add(txt2);

        pn.add(lbl4);

        pn.add(txt3);
        txt3.setEditable(false);

        pn.add(btn1);
        pn.add(btn2);

        btn1.addActionListener(this);
        btn2.addActionListener(this);

        setVisible(false);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String name;
        String answ;
        String result;

        if (e.getSource() == btn1){
            name = txt1.getText();
            answ = txt2.getText();
            ConnectDb con = new ConnectDb();
            try{
                con.createCon();
                result = con.selectSomeData(name, answ);
                con.insertPass(name, result);
                txt3.setText(result);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        }
        if (e.getSource() == btn2){

            LogForm log = new LogForm();
            log.setVisible(true);
            dispose();
        }

    }
}
