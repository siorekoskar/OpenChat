package chat.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oskar on 14/01/2017.
 */
public class ChatRoom {
    static final int PRIVATE = 1, PUBLIC = 0;
    private String chatName;
    private String admin;
    private List usersIn;
    private String messages;
    private int chatType;

    public ChatRoom(int chatType, String admin, String chatName){
        this.chatType = chatType;
        this.admin = admin;
        this.usersIn = new ArrayList();
        this.chatName = chatName;
    }

    public void appendMsg(String msg){
        messages+=msg;
    }
}
