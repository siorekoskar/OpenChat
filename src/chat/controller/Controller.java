package chat.controller;

import chat.gui.FormEvent;
import chat.model.Database;
import chat.model.User;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Oskar on 08/01/2017.
 */
public class Controller {

    Database db = new Database();

    public List<User> getUsers(){
        return db.getUsers();
    }

    public void addUser(FormEvent ev){
        String login = ev.getLogin();
        String pass = ev.getPass();

        User user = new User(login, pass);

        db.addUser(user);
    }

    public void removeUser(int index){
        db.removeUser(index);
    }

    public void save() throws SQLException{
        db.save();
    }

    public void load() throws SQLException{
        db.load();
    }

    public void connect() throws Exception{
        db.connect();
    }

    public void disconnect(){
        db.disconnect();
    }
}
