package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PurchasePanel extends JFrame implements ActionListener {

    String driverName = "com.mysql.jdbc.Driver";
    String url = "jdbc:mysql://localhost:3306/games";
    String userName = "root";
    String password = "root";

    String uName = "";

    String name = "";
    int pprice = 0;
    int overPrice = 0;
    String gameName = "";
    int ciper = 0;

    JTextField card = new JTextField(15);
    JTextField expCard = new JTextField(15);
    JTextField cvc = new JTextField(5);

    JButton purchase = new JButton("Finish Purchase");

    JPanel txtPanel = new JPanel(new GridLayout(0,1));

    PurchasePanel(){

        setSize(350, 250);
        setLocation(600, 0);
        setLayout(new FlowLayout());
        setTitle("Cart");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        try {
            Class.forName(driverName);
            Connection con = DriverManager.getConnection(url, userName, password);

            String selectUser = "select * from useronline";

            PreparedStatement preparedStatement = con.prepareStatement(selectUser);
            ResultSet resultSet = preparedStatement.executeQuery();

            String nameOfUser = "";
            while (resultSet.next()) {
                nameOfUser = resultSet.getString("userName");
            }

            String sql = "select gameName, price, quant from " + nameOfUser + "";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                gameName = rs.getString("gameName");
                pprice = rs.getInt("price");
                ciper = Integer.parseInt(rs.getString("quant"));

                pprice = pprice * ciper;

                overPrice = overPrice + pprice;
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        add(new JLabel("Overall price is: " + overPrice));

        txtPanel.add(new JLabel("Card №:"));
        txtPanel.add(card);
        txtPanel.add(new JLabel("Expire Date:"));
        txtPanel.add(expCard);
        txtPanel.add(new JLabel("CVC:"));
        txtPanel.add(cvc);

        add(txtPanel);

        add(purchase);

        purchase.addActionListener(this);

        setVisible(false);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==purchase){

            card.setText("");
            expCard.setText("");
            cvc.setText("");

            ConnectDb conn = new ConnectDb();

            String root = "c:\\";
            String fileName = "Receipt";

            FileWriter fw1;
            BufferedWriter bw1;

            File file = new File(root + "\\" + fileName + ".txt");

            try {

                fw1 = new FileWriter(file, false);
                bw1 = new BufferedWriter(fw1);

                bw1.write("");

                bw1.close();
                fw1.close();

                Class.forName(driverName);
                Connection con = DriverManager.getConnection(url, userName, password);

                String selectUser = "select * from useronline";

                PreparedStatement preparedStatement = con.prepareStatement(selectUser);
                ResultSet resultSet = preparedStatement.executeQuery();


                while (resultSet.next()) {
                    uName = resultSet.getString("userName");
                }

                String sql = "select gameName, price, quant from " + uName + "";
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();

                int priceTotal = 0;

                while (rs.next()) {
                    gameName = rs.getString("gameName");
                    pprice = rs.getInt("price");

                    ciper = Integer.parseInt(rs.getString("quant"));

                    priceTotal = pprice * ciper;
                    overPrice=0;

                    overPrice = overPrice + priceTotal;

                    conn.createOrder(gameName, pprice, ciper, overPrice);
                }

                conn.totalCost();
                String dropTable = "DROP table " + uName + "";
                PreparedStatement prp = con.prepareStatement(dropTable);
                prp.executeUpdate();

            }catch (Exception ex){
                System.out.println(ex.getMessage());
            }
        }
    }
}