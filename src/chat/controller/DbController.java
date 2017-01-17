package chat.controller;

import chat.gui.FormEvent;
import chat.model.Database;
import chat.model.User;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Oskar on 08/01/2017.
 */
public class DbController {

    Database db = new Database();

    public List<User> getUsers(){
        return db.getUsers();
    }

    public void addUser(User user){
        /*String login = ev.getLogin();
        String pass = ev.getPass();

        User user = new User(login, pass);

        System.out.println(login+ pass);*/

        db.addUser(user);
    }

    public void removeUser(int index){
        db.removeUser(index);
    }

    public boolean checkIfUserExists(FormEvent e) throws SQLException{

        User user = new User(e.getLogin(), e.getPass());
        return db.checkIfUserExists(user);
    }

    public boolean checkIfUserExistsServ(String username)throws SQLException{
        User user = new User(username);
        return db.checkIfUserExists(user);
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
