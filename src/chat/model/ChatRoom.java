package chat.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oskar on 14/01/2017.
 */
public class ChatRoom {
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

    public String getMessages() {
        return messages;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    private String chatName;
    private String admin;
    private List usersIn;
    private String messages;
    private boolean isPrivate;

    public ChatRoom(boolean chatType, String admin, String chatName){
        this.isPrivate = chatType;
        this.admin = admin;
        this.usersIn = new ArrayList();
        this.chatName = chatName;
    }

    public void appendMsg(String msg){
        messages+=msg;
    }

    public String toString(){
        return "Admin: " + getAdmin() +"\nChatname: " + getChatName() +"\nIsPrivate: " + isPrivate()+"\n";
    }
}
