package main.java.pl.edu.agh.kis.openchat.controller;

import main.java.pl.edu.agh.kis.openchat.model.Database;
import main.java.pl.edu.agh.kis.openchat.model.User;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Oskar on 08/01/2017.
 */
public class DbController implements DBControllerInterface {

    Database db = new Database();

    @Override
    public List<User> getUsers(){
        return db.getUsers();
    }

    @Override
    public void addUser(User user){

        db.addUser(user);
    }

    @Override
    public void removeUser(int index){
        db.removeUser(index);
    }

    @Override
    public boolean checkIfUserExistsServ(String username)throws SQLException{
        User user = new User(username);
        return db.checkIfUserExists(user);
    }

    @Override
    public void save() throws SQLException{
        db.save();
    }

    @Override
    public void load() throws SQLException{
        db.load();
    }

    @Override
    public void connect(String url, String user, String password) throws Exception{
        db.connect(url, user, password);
    }

    @Override
    public void disconnect(){
        db.disconnect();
    }
}
