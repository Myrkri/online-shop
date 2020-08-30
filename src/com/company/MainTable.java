package com.company;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MainTable {

    JFrame frame1;
    JTable table;

    private String driverName = "com.mysql.jdbc.Driver";
    private String url = "jdbc:mysql://localhost:3306/games";
    private String userName = "root";
    private String password = "root";
    private String[] columnNames = {"Image", "Game", "Genre", "Release year", "PG", "Price, $", "Availability", "Platform"};
    DefaultTableModel model = new DefaultTableModel();

    protected void resultTable(){

        frame1 = new JFrame("Games Shop");
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setSize(1000, 500);
        frame1.setLayout(new BorderLayout());
        model.setColumnIdentifiers(columnNames);
        table = new JTable(){
            @Override
            public Class<?> getColumnClass(int column) {
                switch (column)
                {
                    case 0: return Icon.class;
                    default: return Object.class;
                }
            }

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                int rendererWidth = component.getPreferredSize().width;
                TableColumn tableColumn = getColumnModel().getColumn(column);
                tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
                return component;
            }
        };

        table.setModel(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFillsViewportHeight(true);
        table.setRowHeight(table.getRowHeight()+100);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);


        try {
            clear();
            buildTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        frame1.add(scroll);
        frame1.setVisible(true);

        //MainOptForm optForm = new MainOptForm();
        //optForm.setLocation(frame1.getX() + frame1.getWidth(), frame1.getY());
        new LogForm().setLocation(frame1.getX() + frame1.getWidth(), frame1.getY());

    }

    public void buildTable() {
        String name;
        String genre;
        String release;
        String pg;
        String price;
        String avail;
        String plat;
        String image;
        String path = "E:\\Всякий хлам\\мои проги\\untitled\\pictures\\";

        try {
            Class.forName(driverName);
            Connection con = DriverManager.getConnection(url, userName, password);
            String sql = "select * from gamelist";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                image = rs.getString("image");
                ImageIcon icon1 = new ImageIcon(path + image + ".png");
                name = rs.getString("gameName");
                genre = rs.getString("genre");
                release = rs.getString("releaseYear");
                pg = rs.getString("pg");
                price = rs.getString("price");
                avail = rs.getString("availability");
                plat = rs.getString("platform");
                model.addRow(new Object[]{icon1, name, genre, release, pg, price, avail, plat});
            }

        } catch (Exception except) {
            except.printStackTrace();
        }
    }

    public void clear(){
        model.setRowCount(0);
    }
}