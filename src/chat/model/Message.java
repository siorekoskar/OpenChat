package chat.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Oskar on 14/01/2017.
 */
public class Message implements Serializable{
    protected static final long serialVersionUID = 1112122200L;

    public static final int MESSAGE = 1, CREATECHAT = 2, CONNECTTOCHAT=3, CHATCONNECTION=4, LOGINMSG=5,
            ALLOWED=6, REGISTER=7, DISALLOWED=8, EXISTS=9, CHATLEFT=10, NOTALLOWED=11,
            USERINVITED = 12, DISCONNECT = 13, PRIVATEMESSAGE=14;

    private int type;
    private String message;
    private String user;
    public String sentToChat;
    public ArrayList<String> users;
    private ArrayList<String> usersIn;
    private String usersInChat;
    private String chatName;
    private ChatRoom chatRoom;
    private ChatRoom chat;
    private ArrayList<String> listOfUndeclared;

    public Message(int type, String user, String message){
        this.type = type;
        this.message = message;
        this.user = user;
    }

    public Message(int type, String user, String message, ChatRoom chatRoom){
        this(type, user, message);
        this.chatRoom = chatRoom;
    }

    public Message(int type, String user, String message, String sentToChat){
        this(type,user,message);
        this.sentToChat = sentToChat;
    }


    /////////////GETTERS AND SETTERS/////////////////
    public ChatRoom getChat() {
        return chat;
    }

    public void setChat(ChatRoom chat) {
        this.chat = chat;
    }


    public ArrayList<String> getUsersIn() {
        return usersIn;
    }

    public void setUsersIn(ArrayList<String> usersIn) {
        this.usersIn = usersIn;
    }

    public String getUsersInChat() {
        return usersInChat;
    }

    public void setUsersInChat(String usersInChat) {
        this.usersInChat = usersInChat;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    public Message(int type){
        this.type=type;
    }

    public int getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public String getUser(){
        return user;
    }

    public ArrayList<String> getListOfUndeclared() {
        return listOfUndeclared;
    }

    public void setListOfUndeclared(ArrayList<String> listOfUndeclared) {
        this.listOfUndeclared = listOfUndeclared;
    }



}
