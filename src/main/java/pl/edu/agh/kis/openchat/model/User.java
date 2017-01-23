package main.java.pl.edu.agh.kis.openchat.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oskar on 08/01/2017.
 */
public class User implements Serializable{

    private static final long serialVersionUID = 42L;

    //////////////////FIELDS////////////////////////////////////
    private static int count = 1;
    private int id;
    private String login;
    private String pass;
    private ArrayList<PrivateMessage> privateMessages = new ArrayList<>();

    //////////////////CONSTRUCTORS////////////////////////////////

    public User(String login, String pass) {

        this.login = login;
        this.pass = pass;
        this.id = count;

        count++;
    }

    public User(int id, String login, String pass){

        this(login, pass);
        this.id = id;

        count++;
    }

    public User(String username){
        this.login = username;
    }

    ///////////////////GETTERS AND SETTERS//////////////////////////

    public void addPrivateMessage(PrivateMessage pm){
        privateMessages.add(pm);
    }

    public List getPrivateMessages(){
        return this.privateMessages;
    }

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        User.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    ////////////////////////////////////////////////////////////

    public String toString(){
        return id+login+pass;
    }
}
