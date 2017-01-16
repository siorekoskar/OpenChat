package chat.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oskar on 14/01/2017.
 */
public class ChatRoom implements Serializable{
    static final int PRIVATE = 1, PUBLIC = 0;

    public String getChatName() {
        return chatName;
    }

    public String getAdmin() {
        return admin;
    }

    public List getUsersIn() {
        return usersIn;
    }

    public String getUsersAsString(){
        return usersIn.toString();
    }

    public String getMessages() {
        return messages;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public boolean userExists(String user){
        return usersIn.contains(user);
    }

    private String chatName;
    private String admin;

    public void addUsersIn(String user) {
        usersIn.add(user);
    }

    private ArrayList<String> usersIn;
    private String messages;
    private boolean isPrivate;

    public ChatRoom(boolean chatType, String admin, String chatName){
        this.isPrivate = chatType;
        this.admin = admin;
        this.usersIn = new ArrayList();
        this.chatName = chatName;
        this.messages = "";
    }

    public ChatRoom(ChatRoom chat){
        this(chat.isPrivate(), chat.getAdmin(), chat.getChatName());

    }

    public void appendMsg(String msg){
        messages+=msg;
    }

    public String toString(){
        return "Admin: " + getAdmin() +"\nChatname: " + getChatName() +"\nIsPrivate: " + isPrivate()+"\n";
    }
}
