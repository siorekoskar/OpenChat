package chat.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oskar on 14/01/2017.
 */
public class ChatRoom {
    private ChatType chatType;
    private String chatName;
    private User admin;
    private List usersIn;
    private String messages;

    public ChatRoom(ChatType chatType, User admin, String chatName){
        this.chatType = chatType;
        this.admin = admin;
        this.usersIn = new ArrayList();
        this.chatName = chatName;
    }

    public void appendMsg(String msg){
        messages+=msg;
    }
}
