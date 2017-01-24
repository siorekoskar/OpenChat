package main.java.pl.edu.agh.kis.openchat.controller;

import main.java.pl.edu.agh.kis.openchat.model.User;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Oskar on 22/01/2017.
 */
public interface DBControllerInterface {

     /**
      * List of users in database
      * @return users from database
      */
     List<User> getUsers();

     /**
      * Add user to list of users
      * @param user user with data about password and username
      */
     void addUser(User user);

     /**
      * Remove user from list
      * @param index index of user to be removed
      */
     void removeUser(int index);

     /**
      * Check whether user exists in database
      * @param username username of the user
      * @return true if he does
      * @throws SQLException if there is a problem with connection
      */
     boolean checkIfUserExistsServ(String username)throws SQLException;

     /**
      * Save current list of users into database
      * @throws SQLException
      */
     void save() throws SQLException;

     /**
      * Load users from database into list
      * @throws SQLException
      */
     void load() throws SQLException;

     /**
      * Connect into database
      * @param url host of database
      * @param user username of admin
      * @param password password of admin
      * @throws Exception
      */
     void connect(String url, String user, String password) throws Exception;

     /**
      * Disconnect from database
      */
     void disconnect();
}
