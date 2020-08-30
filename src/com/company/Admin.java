package com.company;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Admin extends JFrame implements ActionListener {

    String[] columnNames = {"ID", "Image", "Game", "Genre", "Release year", "PG", "Price", "Availability", "Platform"};
    JTable table;
    DefaultTableModel model = new DefaultTableModel();
    JPanel panel = new JPanel(new GridLayout(0,1));
    String[] columnNames1 = {"ID", "Login", "Email"};
    JTable table1;
    DefaultTableModel model1 = new DefaultTableModel();

    JButton add = new JButton("Add");
    JButton delete = new JButton("Delete");

    JTextField image = new JTextField(15);
    JTextField gameName = new JTextField(15);
    JTextField genre = new JTextField(15);
    JTextField releaseYear = new JTextField(15);
    JTextField pg = new JTextField(15);
    JTextField price = new JTextField(15);
    JTextField availib = new JTextField(15);
    JTextField platf = new JTextField(15);

    JPanel panel1 = new JPanel(new GridLayout(0, 2));

    Admin(){
        setSize(1000, 650);
        setLayout(new FlowLayout());
        setTitle("Admin page");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setVisible(true);

        model.setColumnIdentifiers(columnNames);
        table = new JTable();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setModel(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFillsViewportHeight(true);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scroll);

        model1.setColumnIdentifiers(columnNames1);
        table1 = new JTable();
        table1.setModel(model1);
        table1.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table1.setFillsViewportHeight(true);
        JScrollPane scroll1 = new JScrollPane(table1);
        scroll1.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll1.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scroll1);

        receiveGames();
        receiveUsers();

        panel1.add(new JLabel("Name of Image"));
        panel1.add(image);
        panel1.add(new JLabel("Name of the game"));
        panel1.add(gameName);
        panel1.add(new JLabel("Genre"));
        panel1.add(genre);
        panel1.add(new JLabel("Release year"));
        panel1.add(releaseYear);
        panel1.add(new JLabel("Game PG"));
        panel1.add(pg);
        panel1.add(new JLabel("Game price"));
        panel1.add(price);
        panel1.add(new JLabel("Availability"));
        panel1.add(availib);
        panel1.add(new JLabel("Platform"));
        panel1.add(platf);

        add(panel1);

        add(add);
        add(delete);

        add.addActionListener(this);
        delete.addActionListener(this);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        int gamRow = table.getSelectedRow();
        int usrRow = table1.getSelectedRow();
        String gamCell = "";
        String usrCell = "";

        if (e.getSource() == delete) {
            int choice = JOptionPane.showConfirmDialog(null, "Do you want to delete?");
            if (choice == 0) {
                if (gamRow >= 0) {
                    try {
                        gamCell = table.getModel().getValueAt(gamRow, 0).toString();
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        JOptionPane.showMessageDialog(null, "Please, choose one of the values from tables");
                    }
                } else {
                    try {
                        usrCell = table1.getModel().getValueAt(usrRow, 0).toString();
                    }catch (ArrayIndexOutOfBoundsException ex1){
                        JOptionPane.showMessageDialog(null, "Please, choose one of the values from tables");
                    }
                }
                ConnectDb conn = new ConnectDb();
                try {
                    conn.createCon();

                    conn.deleteDynGam(gamCell);
                    conn.deleteDynUsr(usrCell);

                    clear();
                    otherClear();
                    receiveGames();
                    receiveUsers();
                    GamesBase gamesBase = new GamesBase();
                    MainTable mainTable = new MainTable();
                    mainTable.resultTable();
                    mainTable.frame1.dispose();
                    gamesBase.resultTable();
                    gamesBase.frame1.dispose();

                } catch (Exception e1) {
                    System.out.println(e1.getMessage());
                }
            }
        }

        String img;
        String name;
        String genre;
        String price;
        String pg;
        String avil;
        String release;
        String platform;

        if (e.getSource() == add){
            ConnectDb con = new ConnectDb();

            img = image.getText();
            name = gameName.getText();
            genre = this.genre.getText();
            price = this.price.getText();
            pg = this.pg.getText();
            avil = availib.getText();
            release = releaseYear.getText();
            platform = platf.getText();

            try {
                con.createCon();
                con.addGamesData(img, name, genre,release, pg, price, avil, platform);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            this.dispose();
        }
    }

    public void receiveGames(){
        String url = "jdbc:mysql://localhost:3306/games";
        String userName = "root";
        String password = "root";

        String id = "";
        String image = "";
        String name= "";
        String genre = "";
        String release = "";
        String price = "";
        String pg = "";
        String avail = "";
        String platf = "";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, userName, password);
            String sql = "SELECT * FROM gamelist";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next())
            {
                id = resultSet.getString("gameId");
                image = resultSet.getString("image");
                name = resultSet.getString("gameName");
                genre = resultSet.getString("genre");
                release = resultSet.getString("releaseYear");
                price = resultSet.getString("pg");
                pg = resultSet.getString("price");
                avail = resultSet.getString("availability");
                platf = resultSet.getString("platform");
                model.addRow(new Object[]{id, image, name, genre, release, price, pg, avail, platf});

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void receiveUsers(){

        String url = "jdbc:mysql://localhost:3306/games";
        String userName = "root";
        String password = "root";

        String id = "";
        String name= "";
        String email = "";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, userName, password);
            String sql = "select * from users";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next())
            {
                id = resultSet.getString("idusers");
                name = resultSet.getString("login");
                email = resultSet.getString("email");
                model1.addRow(new Object[]{id, name, email});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addTableModelListener(e -> {

            updateDynamicG(e);

        });
        model1.addTableModelListener(ev -> {

            updateDynamicU(ev);

            });
    }
    public void clear(){
        model.setRowCount(0);
    }
    public void otherClear(){
        model1.setRowCount(0);
    }


    private void updateDynamicG(TableModelEvent e)  {

        if (e.getType() == TableModelEvent.UPDATE) {

            model = (DefaultTableModel) e.getSource();
            int target = e.getFirstRow();
            int column = e.getColumn();

            String data = String.valueOf(model.getValueAt(table.getSelectedRow(),table.getSelectedColumn()));

            String id =  model.getValueAt(target, 0).toString();

            ConnectDb con = new ConnectDb();
            try {
                con.createCon();
                con.dynamicUpdateG(column, id, data);
            } catch (Exception s){
                System.out.println(s);
            }
        }
    }
    private void updateDynamicU(TableModelEvent e){
        if (e.getType() == TableModelEvent.UPDATE){
            model1 = (DefaultTableModel) e.getSource();
            int target = e.getFirstRow();
            int column = e.getColumn();

            String data = String.valueOf(model1.getValueAt(table1.getSelectedRow(), table1.getSelectedColumn()));

            String idUsr = model1.getValueAt(target, 0).toString();

            ConnectDb conn = new ConnectDb();
            try{
                conn.createCon();
                conn.updateDynamicUsr(column, idUsr, data);

            }catch (Exception e1){
                System.out.println(e1);
            }
        }
    }
}