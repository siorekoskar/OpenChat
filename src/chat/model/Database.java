package chat.model;

import java.sql.Connection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Oskar on 08/01/2017.
 */
public class Database{

    //////////////////FIELDS//////////////////////////////////////
    private List<User> users;
    private Connection conn;

    //////////////////CONSTRUCTORS////////////////////////////////
    public Database(){
        users = new LinkedList<>();
    }

    //////////////////METHODS/////////////////////////////////////
    public List<User> getUsers(){
        return Collections.unmodifiableList(users);
    }

    public void removeUser(int index){
        users.remove(index);
    }

    public void addUser(User user){
        users.add(user);
    }


    ///////////DO
    public void connect() throws Exception{

    }

    public void disconnect(){

    }

    public void save(){

    }

    public void load(){

    }
    /////////DO

}
