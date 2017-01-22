package chat.controller;

import chat.gui.FormEvent;
import chat.model.Database;
import chat.model.User;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Oskar on 08/01/2017.
 */
public class DbController implements DBControllerInterface {

    Database db = new Database();

    public List<User> getUsers(){
        return db.getUsers();
    }

    public void addUser(User user){

        db.addUser(user);
    }

    public void removeUser(int index){
        db.removeUser(index);
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

    public void connect(String url, String user, String password) throws Exception{
        db.connect(url, user, password);
    }

    public void disconnect(){
        db.disconnect();
    }
}
