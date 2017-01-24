package main.java.pl.edu.agh.kis.openchat.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oskar on 08/01/2017.
 */

/**
 * Class that hold information about amount of users, its ids,
 * login, password and his private messages
 */
public class User implements Serializable{

    private static final long serialVersionUID = 42L;

    //////////////////FIELDS////////////////////////////////////

    private static int count = 0;
    private int id;
    private String login;
    private String pass;
    private ArrayList<PrivateMessage> privateMessages = new ArrayList<>();

    //////////////////CONSTRUCTORS////////////////////////////////

    /**
     * 2 parameter constructor of class
     * @param login
     * @param pass
     */
    public User(String login, String pass) {

        this.login = login;
        this.pass = pass;
        this.id = count;

        count++;
    }

    /**
     * 3 parameter constructor of class, starts constructor with 2 parameters
     * @param id takes id which you want user to have
     * @param login new users login
     * @param pass new users password
     */
    public User(int id, String login, String pass){

        this(login, pass);
        this.id = id;

        count++;
    }

    /**
     * Constructor with 1 parameter
     * @param username new users username
     */
    public User(String username){
        this.login = username;
    }

    ///////////////////GETTERS AND SETTERS//////////////////////////

    /**
     * Method that adds private message to user
     * @param pm private message with various informations
     */
    public void addPrivateMessage(PrivateMessage pm){
        privateMessages.add(pm);
    }

    /**
     * List of private messages that user has received
     * @return list of private messages
     */
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
