package com.company;


import com.mysql.cj.log.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserPage extends JFrame implements ActionListener {

    JButton openSearchPanel = new JButton("Open Search panel");
    JButton searchButton = new JButton("Search");
    JButton clearButton = new JButton("Clear");
    JButton contact = new JButton("Contact with Support");
    JButton addToCart = new JButton("Cart");
    JButton logOut = new JButton("Log out");
    JLabel label = new JLabel("Name or genre of the game: ");

    String driverName = "com.mysql.jdbc.Driver";
    String url = "jdbc:mysql://localhost:3306/games";
    String userName = "root";
    String password = "root";
    JPanel panel = new JPanel();

    JLabel loginName = new JLabel();

    JPanel labelPanel = new JPanel();

    JTextField txt = new JTextField(15);

    UserPage(){
        setSize(350, 200);
        setLocation(1000, 0);
        setLayout(new FlowLayout());
        setTitle("Main page");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        labelPanel.add(new JLabel("Welcome "));
        labelPanel.add(loginName);

        add(labelPanel);

        add(openSearchPanel);
        add(contact);
        add(addToCart);
        add(logOut);
        add(panel);
        panel.add(label);
        panel.add(txt);

        add(searchButton);
        add(clearButton);

        label.setVisible(false);
        txt.setVisible(false);
        searchButton.setVisible(false);
        clearButton.setVisible(false);

        openSearchPanel.addActionListener(this);
        contact.addActionListener(this);
        addToCart.addActionListener(this);
        logOut.addActionListener(this);
        txt.addActionListener(this);
        searchButton.addActionListener(this);
        clearButton.addActionListener(this);

        setVisible(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String name;
        if (e.getSource() == openSearchPanel){
            label.setVisible(true);
            txt.setVisible(true);
            searchButton.setVisible(true);
            clearButton.setVisible(true);

        }

        if (e.getSource() == searchButton){
            name = txt.getText();
            SearchResult search = new SearchResult();
            search.showTableData(name);
        }

        if (e.getSource() == clearButton){
            txt.setText("");
        }

        if (e.getSource() == contact){

            Contact contact = new Contact();
            contact.setVisible(true);

        }

        if (e.getSource() == addToCart){

            ConnectDb conn = new ConnectDb();
            try {
                conn.createCon();
                OrderTable order = new OrderTable();
                order.frame1.setVisible(true);
                PurchasePanel purchasePanel = new PurchasePanel();
                purchasePanel.setVisible(true);

            } catch (Exception e1) {
                e1.printStackTrace();
            }


        }

        if (e.getSource() == logOut){
            int result = JOptionPane.showConfirmDialog(null, "Are you sure that you want to exit?");

            if(result == 0){

                try {
                    Class.forName(driverName);
                    Connection con = DriverManager.getConnection(url, userName, password);

                    String selectUser = "select * from useronline";

                    PreparedStatement preparedStatement = con.prepareStatement(selectUser);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    String userName = "";
                    while (resultSet.next()) {
                        userName = resultSet.getString("userName");
                    }
                    String dropTable = "DROP table " + userName + "";
                    PreparedStatement prp = con.prepareStatement(dropTable);
                    prp.executeUpdate();



                }catch (Exception ex){

                }



                dispose();
            }
        }

    }
}