package main.java.pl.edu.agh.kis.openchat.model;

import java.sql.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Oskar on 08/01/2017.
 */

/**
 * Database with methods allowing it to store users, add,
 * load and save with database engine
 */
public class Database {

    //////////////////FIELDS//////////////////////////////////////
    private List<User> users;
    private Connection conn;

    //////////////////CONSTRUCTORS////////////////////////////////

    /**
     * Constructor of the database, creates linkedlist
     */
    public Database() {
        users = new LinkedList<>();
    }

    //////////////////METHODS/////////////////////////////////////
    public List<User> getUsers() {
        return Collections.unmodifiableList(users);
    }

    /**
     * Lets you remove user from list
     * @param index index of user to be deleted
     * @return returns that user in case that information is needed
     */
    public User removeUser(int index) {
        User a = users.remove(index);
        return a;
    }

    /**
     * Add user to list in the class object
     * @param user user that is added
     */
    public void addUser(User user) {
        users.add(user);
    }


    /**
     * Method that allows connection to database
     * @param url host of database
     * @param user login database information
     * @param password password to database
     * @return true if connected, false otherwise
     * @throws Exception if connection problem exists
     */
    public boolean connect(String url, String user, String password) throws Exception {

        if (conn != null) {
            return false;
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
        return true;
    }

    /**
     * Lets you disconnect from database
     * @return true if disconnected, false otherwise
     */
    public boolean disconnect() {
        if (conn != null) {
            try {
                conn.close();
                return true;
            } catch (SQLException e) {
                System.out.println("Cant close connection");
            }
        }
        return false;
    }

    /**
     * Check whether user exists in database
     * @param user user that is wanted to be checked
     * @return true if he exists, false otherwise
     * @throws SQLException if there is a problem with connection
     */
    public boolean checkIfUserExists(User user) throws SQLException {

        String login = user.getLogin();


        //String checkIfExistsSql = "select count(*) as count from user where login = '"
            //    + login + "'";

        String checkIfExistsSql = "select count(*) as count from user where login=?";

        PreparedStatement checkStmt = null;
        int count = 0;

        try {
            if (conn != null) {
                checkStmt = conn.prepareStatement(checkIfExistsSql);
                checkStmt.setString(1,login);
                ResultSet checkResult = checkStmt.executeQuery();
                checkResult.next();
                count = checkResult.getInt(1);
            }
        } finally {
            if (checkStmt != null) checkStmt.close();
        }

        return (count != 0);
    }


    /**
     * Save current list of users to database
     * @throws SQLException if connection problem happens
     */
    public void save() throws SQLException {
        if (conn != null) {

            String checkSql = "Select count(*) as count from user where id=?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);

            String insertSql = "insert into user (login, password) values (?,?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertSql);

            String updateSql = "update user set login=?, password=? where id = ?";
            //PreparedStatement updateStmt = conn.prepareStatement(updateSql);

            try {
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
            }catch(SQLException e){
                throw e;
            } finally {
                checkStmt.close();
                insertStmt.close();
            }
        }

    }

    /**
     * Load users from database to list
     * @return true if loaded, false otherwise
     * @throws SQLException if connection problem happens
     */
    public boolean load() throws SQLException {

        if (conn != null) {
            users.clear();

            String sql = "select id, login, password from user order by id";

            Statement selectStmt = null;
            ResultSet results = null;
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
            }catch(SQLException e){
                if (results != null) results.close();
                throw e;
            } finally {
                if (selectStmt != null) selectStmt.close();
            }
            return true;
        }
        return false;
    }
    /////////DO

}