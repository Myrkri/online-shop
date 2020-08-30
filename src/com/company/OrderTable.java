package com.company;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class OrderTable {
    JFrame frame1;
    JTable table;

    String driverName = "com.mysql.jdbc.Driver";
    String url = "jdbc:mysql://localhost:3306/games";
    String userName = "root";
    String password = "root";
    String[] columnNames = {"Game", "Price", "Quantity"};

    DefaultTableModel model = new DefaultTableModel();



    OrderTable() {
        frame1 = new JFrame("Cart");
        frame1.setSize(600, 300);
        frame1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame1.setLayout(new BorderLayout());
        model.setColumnIdentifiers(columnNames);
        table = new JTable();
        table.setModel(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFillsViewportHeight(true);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

       try {
            Class.forName(driverName);
            Connection con = DriverManager.getConnection(url, userName, password);

            String selectUser = "select * from useronline";

            PreparedStatement preparedStatement = con.prepareStatement(selectUser);
            ResultSet resultSet = preparedStatement.executeQuery();

            String userName = "";
            while (resultSet.next()){
                userName = resultSet.getString("userName");
            }


            String sql = "select gameName, price, quant from " + userName + "";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            String name = "";
            String price = "";
            int quant = 0;

           int ciper;

            while (rs.next()) {
                name = rs.getString("gameName");
                price = rs.getString("price");
                quant = rs.getInt("quant");
                ciper = Integer.parseInt(price);
                ciper = ciper * quant;
                model.addRow(new Object[]{name, ciper, quant});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        frame1.add(scroll);
        frame1.setVisible(false);
    }
}