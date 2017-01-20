package chat.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oskar on 14/01/2017.
 */
public class ChatRoom implements Serializable {
    static final int PRIVATE = 1, PUBLIC = 0;

    private String chatName;
    private String admin;
    private ArrayList<String> usersIn;
    private String messages;
    private boolean isPrivate;
    private ArrayList<String> areAllowed;

    public ChatRoom(boolean chatType, String admin, String chatName) {
        this.isPrivate = chatType;
        this.admin = admin;
        this.usersIn = new ArrayList();
        this.chatName = chatName;
        this.messages = "";
    }

    public ChatRoom(ChatRoom chat) {
        this(chat.isPrivate(), chat.getAdmin(), chat.getChatName());

    }

    public void appendMsg(String msg) {
        messages += msg;
    }


    /////////////GETTERS AND SETTERS/////////////////
    public String getChatName() {
        return chatName;
    }

    public String getAdmin() {
        return admin;
    }

    public ArrayList<String> getUsersIn() {
        return usersIn;
    }

    public String getUsersAsString() {
        return usersIn.toString();
    }

    public String getMessages() {
        return messages;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public boolean userExists(String user) {
        return usersIn.contains(user);
    }

    public void deleteUser(String user){
        usersIn.remove(user);
    }


    public void addUsersIn(String user) {
        usersIn.add(user);
    }

    public void setUsersIn(ArrayList<String> usersIn) {
        this.usersIn = usersIn;
    }


    public ArrayList<String> getAreAllowed() {
        return areAllowed;
    }

    public void addAllowed(String user) {
        areAllowed.add(user);
    }


    public String toString() {
        return "Admin: " + getAdmin() + "\nChatname: " + getChatName() + "\nIsPrivate: " + isPrivate() + "\n";
    }
}
