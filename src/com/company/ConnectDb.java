package com.company;

import javax.swing.*;
import java.io.*;
import java.sql.*;
import java.util.Random;

public class ConnectDb {
    public Connection con = null;
    public Statement stmt = null;
    public ResultSet resultSet = null;
    public PreparedStatement preparedStatement = null;
    int totalPrice=0;

    public void createCon() throws Exception {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/games", "root", "root");
        } catch (Exception e) {
            throw e;
        }
    }

    public void insertData(String name, String pass, String email, String answ) {
        try {

            stmt = con.createStatement();
            preparedStatement = con.prepareStatement("INSERT  INTO games.users (login, password, answer, email) VALUES (?,?,?,?)");

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, pass);
            preparedStatement.setString(3, answ);
            preparedStatement.setString(4, email);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public boolean selectData(String name, String pass) {
        boolean result = false;
        try {
            stmt = con.createStatement();
            preparedStatement = con.prepareStatement("SELECT login, password FROM users WHERE login='" + name + "'AND password='" + pass + "'");
            resultSet = preparedStatement.executeQuery();

            boolean recordFound = resultSet.next();

            if (recordFound) {
                result = true;

                UserPage user = new UserPage();
                LogForm logForm = new LogForm();
                user.loginName.setText(name);
                user.setVisible(true);
                logForm.dispose();

                String onlineCreate = "create table if not exists userOnline (id INTEGER PRIMARY KEY AUTO_INCREMENT, userName VARCHAR(255))";

                PreparedStatement preparedStatement = con.prepareStatement(onlineCreate);
                preparedStatement.executeUpdate();

                String createOnline = "Insert into userOnline (userName) VALUES (?)";

                PreparedStatement prpstUsOnl = con.prepareStatement(createOnline);
                prpstUsOnl.setString(1, name);
                prpstUsOnl.executeUpdate();

                String tableCreate = "create table if not exists " + name + "(id INTEGER PRIMARY KEY AUTO_INCREMENT, gameName VARCHAR(255),  price VARCHAR(255), quant VARCHAR(200))";

                PreparedStatement ps = con.prepareStatement(tableCreate);
                ps.executeUpdate();

                return result;
            } else if (!recordFound) {
                result = false;
                JOptionPane.showMessageDialog(null, "Your login or password is not correct");
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String selectSomeData(String name, String sansw) {
        String res = "";
        try {
            stmt = con.createStatement();

            preparedStatement = con.prepareStatement("SELECT login, answer FROM users WHERE login='" + name + "'AND answer='" + sansw + "'");
            resultSet = preparedStatement.executeQuery();

            boolean recordFound = resultSet.next();

            if (recordFound) {
                String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
                StringBuilder salt = new StringBuilder();
                Random rnd = new Random();
                while (salt.length() < 9) { // length of the random string.
                    int index = (int) (rnd.nextFloat() * CHARS.length());
                    salt.append(CHARS.charAt(index));
                }
                res = salt.toString();
                return res;
            } else if (!recordFound) {
                ForgetPass fog = new ForgetPass();
                fog.txt1.setText("");
                fog.txt2.setText("");
                JOptionPane.showMessageDialog(null, "You are not registered!,please Register.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public void insertPass(String login, String str) {
        try {
            stmt = con.createStatement();
            preparedStatement = con.prepareStatement("UPDATE users SET password=? WHERE login=?");
            preparedStatement.setString(1, str);
            preparedStatement.setString(2, login);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addGamesData(String img, String name, String genre, String release, String pg, String price, String availib, String platf) {
        try {
            stmt = con.createStatement();
            preparedStatement = con.prepareStatement("INSERT INTO gamelist (image, gameName, genre, releaseYear, pg, price, availability, platform) VALUES (?,?,?,?,?,?,?,?)");
            preparedStatement.setString(1, img);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, genre);
            preparedStatement.setString(4, release);
            preparedStatement.setString(5, pg);
            preparedStatement.setString(6, price);
            preparedStatement.setString(7, availib);
            preparedStatement.setString(8, platf);
            preparedStatement.executeUpdate();

            Admin sanctuary = new Admin();
            sanctuary.clear();
            sanctuary.receiveGames();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createOrder(String gameName, int gamePrice, int ciper, int overPrice) {


        totalPrice=totalPrice+(gamePrice*ciper);
        String root = "c:\\";
        String fileName = "Receipt";

        BufferedWriter bw = null;
        FileWriter fw = null;

        File file = new File(root + "\\" + fileName + ".txt");


        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            fw = new FileWriter(file, true);
            bw = new BufferedWriter(fw);

            bw.write("Name of the game: " + gameName + " || ");
            bw.write("Price of the game: " + gamePrice + " || ");
            bw.write("Amount of game copies: " + ciper + " || ");
            bw.write("Total price: " + overPrice + "");

            bw.newLine();

            bw.close();
            fw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void totalCost(){
        String root = "c:\\";
        String fileName = "Receipt";

        BufferedWriter bw = null;
        FileWriter fw = null;

        File file = new File(root + "\\" + fileName + ".txt");


        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            fw = new FileWriter(file, true);
            bw = new BufferedWriter(fw);
            bw.write("The Total Cost is:" +totalPrice);
            bw.newLine();

            bw.close();
            fw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void dynamicUpdateG(int column, String id, String data){
        try {
            stmt = con.createStatement();

            String sQL = "";

            if (column == 1){
                sQL =   "UPDATE gamelist SET image='" + data + "'WHERE gameid='"+id+"'" ;
            }
            if (column == 2){
                sQL =   "UPDATE gamelist SET gameName='" + data + "' WHERE gameid='"+id+ "'" ;
            }
            if (column == 3){
                sQL =   "UPDATE gamelist SET genre='" + data + "' WHERE gameid='"+id+ "'" ;
            }
            if (column == 4){
                sQL =   "UPDATE gamelist SET releaseYear='" + data + "' WHERE gameid='"+id+"'" ;
            }
            if (column == 5){
                sQL =   "UPDATE gamelist SET pg='" + data + "' WHERE gameid='"+id+"'" ;
            }
            if (column == 6){
                sQL =   "UPDATE gamelist SET price='" + data + "' WHERE gameid='"+id+"'" ;
            }
            if (column == 7){
                sQL =   "UPDATE gamelist SET availability='" + data + "' WHERE gameid='"+id+"'" ;
            }
            if (column == 8){
                sQL =   "UPDATE gamelist SET platform='" + data + "' WHERE gameid='"+id+"'" ;
            }
            preparedStatement = con.prepareStatement(sQL);

            preparedStatement.executeUpdate();
        }catch (SQLException s){
            System.out.println(s);
        }
    }
    public void updateDynamicUsr(int column, String id, String data){
        try {
            stmt = con.createStatement();

            String sql = "";

            if (column==1){
                sql =   "UPDATE users SET login='" + data + "'WHERE idusers='"+id+"'" ;
            }
            if (column==2){
                sql =   "UPDATE users SET email='" + data + "' WHERE idusers='"+id+ "'" ;
            }

            preparedStatement = con.prepareStatement(sql);

            preparedStatement.executeUpdate();
        }catch (SQLException s){
            System.out.println(s);
        }
    }
    public void deleteDynUsr(String id){
        try {
            stmt = con.createStatement();
            String sql = "Delete from users where idusers='"+id+"'" ;

            preparedStatement = con.prepareStatement(sql);
            preparedStatement.executeUpdate();
        }catch (SQLException s){
            System.out.println(s);
        }
    }
    public void deleteDynGam(String id){
        try {

            stmt = con.createStatement();
            String sql = "Delete from gamelist where gameid='"+id+"'" ;

            preparedStatement = con.prepareStatement(sql);
            preparedStatement.executeUpdate();

        }catch (SQLException s){
            System.out.println(s);
        }
    }

    public static void closeConnection(Connection conn, PreparedStatement stmt,ResultSet rs){

        try
        {
            conn.close();
            conn = null;
            stmt.close();
            stmt=null;
            rs.close();
            rs=null;
        }
        catch (Exception e)
        {
            System.out.println("Error! Connection was not closed!");
            System.out.println(e);
        }

    }

}