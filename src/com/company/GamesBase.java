package com.company;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class GamesBase{

    JFrame frame1;
    JTable table;

    private String driverName = "com.mysql.jdbc.Driver";
    private String url = "jdbc:mysql://localhost:3306/games";
    private String userName = "root";
    private String password = "root";
    private String[] columnNames = {"Image", "Game", "Genre", "Release year", "PG", "Price, $", "Availability", "Platform", "Button"};
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
            buildTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        frame1.add(scroll);

        Action buy = new AbstractAction()
        {
            public void actionPerformed(ActionEvent e) {
                JTable table = (JTable) e.getSource();
                int modelRow = Integer.valueOf(e.getActionCommand());
                String name = table.getModel().getValueAt(modelRow, 1).toString();
                String price = table.getModel().getValueAt(modelRow, 5).toString();
                String login = "";

                String check = "0";

                String quant = JOptionPane.showInputDialog("How many games do you want to buy?");
                if (!quant.equals(check) && quant != null) {
                    try {

                        Class.forName(driverName);
                        Connection con = DriverManager.getConnection(url, userName, password);

                        String fetchOnline = "select * from useronline";
                        PreparedStatement ps = con.prepareStatement(fetchOnline);
                        ResultSet resultSet = ps.executeQuery();

                        if (resultSet.next()) {
                            login = resultSet.getString("userName");
                        }

                        String sql = "insert into " + login + " (gameName, price, quant) values (?,?,?)";
                        PreparedStatement ps1 = con.prepareStatement(sql);
                        ps1.setString(1, name);
                        ps1.setString(2, price);
                        ps1.setString(3, quant);

                        ps1.executeUpdate();



                    }

                    catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                else {JOptionPane.showMessageDialog(null, "Enter number of copies that you want to buy");}
            }
        };


        ButtonColumn buttonColumn = new ButtonColumn(table, buy, 8);
        buttonColumn.setMnemonic(KeyEvent.VK_D);

        frame1.setVisible(true);
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
        String buttName = "Buy";

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
                model.addRow(new Object[]{icon1, name, genre, release, pg, price, avail, plat, buttName});
            }
        } catch (Exception except) {
            except.printStackTrace();
        }
    }
}
