package main.java.pl.edu.agh.kis.openchat.controller;

import main.java.pl.edu.agh.kis.openchat.model.User;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Oskar on 22/01/2017.
 */
public interface DBControllerInterface {
     List<User> getUsers();
     void addUser(User user);
     void removeUser(int index);
     boolean checkIfUserExistsServ(String username)throws SQLException;
     void save() throws SQLException;
     void load() throws SQLException;
     void connect(String url, String user, String password) throws Exception;
     void disconnect();
}
