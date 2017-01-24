package main.java.pl.edu.agh.kis.openchat.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Oskar on 14/01/2017.
 */

/**
 * Chatroom class that implements serializable,
 * can be private or public
 * Hold information about admin, chatname, active users, and messages inside
 */
public class ChatRoom implements Serializable {
    static final boolean PRIVATE = true, PUBLIC = false;
    static final long serialVersionUID = 40L;

    private String chatName;
    private String admin;
    private ArrayList<String> usersIn;
    private String messages;
    private boolean isPrivate;
    private ArrayList<String> areAllowed;

    /**
     * 4 parameter constructor
     * @param chatType type of chat (public or private)
     * @param admin admin of the chat
     * @param chatName name of the chat
     */
    public ChatRoom(boolean chatType, String admin, String chatName) {
        this.isPrivate = chatType;
        this.admin = admin;
        this.usersIn = new ArrayList<>();
        this.areAllowed = new ArrayList<>();
        this.chatName = chatName;
        this.messages = "";
    }

    /**
     * 1 parameter constructor, lets you copy the chat
     * @param chat
     */
    public ChatRoom(ChatRoom chat) {
        this(chat.isPrivate(), chat.getAdmin(), chat.getChatName());

    }

    /**
     * Appends message to chat
     * @param msg
     */
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
