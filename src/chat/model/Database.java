package chat.model;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Oskar on 08/01/2017.
 */
public class Database {

    //////////////////FIELDS//////////////////////////////////////
    private List<User> users;
    private Connection conn;

    //////////////////CONSTRUCTORS////////////////////////////////
    public Database() {
        users = new LinkedList<>();
    }

    //////////////////METHODS/////////////////////////////////////
    public List<User> getUsers() {
        return Collections.unmodifiableList(users);
    }

    public void removeUser(int index) {
        users.remove(index);
    }

    public void addUser(User user) {
        users.add(user);
    }


    ///////////DO
    public void connect(String url, String user, String password) throws Exception {

        if (conn != null) {
            return;
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new Exception("Driver not found");
        }

        //url = "jdbc:mysql://localhost:3306/user";
        url = "jdbc:mysql://" + url;
        conn = DriverManager.getConnection(url, user, password);

        //conn = DriverManager.getConnection(url, "root", "shihtzu1");

        System.out.println("Connected: " + conn);
    }

    public void disconnect() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("Cant close connection");
            }
        }
    }

    public boolean checkIfUserExists(User user) throws SQLException {

        String login = user.getLogin();

        final String checkIfExistsSql = "select count(*) as count from user where login = '"
                + login + "'";

        PreparedStatement checkStmt;
        int count = 0;

        if (conn != null) {
            checkStmt = conn.prepareStatement(checkIfExistsSql);
            ResultSet checkResult = checkStmt.executeQuery();
            checkResult.next();
            count = checkResult.getInt(1);
        }

        return (count != 0);
    }


    public void save() throws SQLException {
        if (conn != null) {

            String checkSql = "Select count(*) as count from user where id=?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);

            String insertSql = "insert into user (login, password) values (?,?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertSql);

            String updateSql = "update user set login=?, password=? where id = ?";
            //PreparedStatement updateStmt = conn.prepareStatement(updateSql);


            for (User user : users) {
                int id = user.getId();
                String login = user.getLogin();
                String password = user.getPass();

                checkStmt.setInt(1, id);

                ResultSet checkResult = checkStmt.executeQuery();
                checkResult.next();

                try {
                    int count = checkResult.getInt(1);

                    if (count == 0) {
                        System.out.println("Inserting person with ID " + id);

                        int col = 1;
                        insertStmt.setInt(col, id);
                        insertStmt.setString(col++, login);
                        insertStmt.setString(col++, password);

                        insertStmt.executeUpdate();
                    } else {/*
                System.out.println("Updating person with ID" + id);

                int col = 1;
                updateStmt.setString(col++, login);
                updateStmt.setString(col++, password);
                updateStmt.setInt(col++, id);

                updateStmt.executeUpdate();*/
                    }
                } finally {
                    checkResult.close();
                    checkStmt.close();
                }
            }
        }

    }

    public void load() throws SQLException {

        if (conn != null) {
            users.clear();

            String sql = "select id, login, password from user order by id";

            Statement selectStmt=null;
            ResultSet results=null;
            try {
                selectStmt = conn.createStatement();
                results = selectStmt.executeQuery(sql);

                while (results.next()) {
                    int id = results.getInt("id");
                    String login = results.getString("login");
                    String password = results.getString("password");

                    User user = new User(id, login, password);

                    users.add(user);

                }
            } finally {
                if(results!=null)results.close();
                if(selectStmt!=null)selectStmt.close();
            }

        }
    }
    /////////DO

}
