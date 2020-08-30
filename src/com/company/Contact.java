package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Contact extends JFrame implements ActionListener{
    JTextArea textArea = new JTextArea(25,25);
    JButton send = new JButton("Send mail");
    JButton close = new JButton("Cancel");
    Contact(){
        setSize(300, 500);
        setLayout(new FlowLayout());
        setTitle("Main page");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        add(textArea);
        add(send);
        add(close);

        send.addActionListener(this);
        close.addActionListener(this);

        setVisible(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == send){
            textArea.setText("");
        }
        if (e.getSource() == close){
            dispose();
        }
    }
}
