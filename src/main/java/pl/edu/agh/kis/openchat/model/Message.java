package main.java.pl.edu.agh.kis.openchat.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Oskar on 14/01/2017.
 */

/**
 * Class holding various types of messages, class that is sent
 * between server and the client with informations about the type
 * of event occured and informations depending on it
 * implements Serializable
 */
public class Message implements Serializable{
    protected static final long serialVersionUID = 1112122200L;

    /**
     * Types of messages that are used in the whole project
     * which help communication between the server and client
     */
    public static final int MESSAGE = 1, CREATECHAT = 2, CONNECTTOCHAT=3, CHATCONNECTION=4, LOGINMSG=5,
            ALLOWED=6, REGISTER=7, DISALLOWED=8, EXISTS=9, CHATLEFT=10, NOTALLOWED=11,
            USERINVITED = 12, DISCONNECT = 13, PRIVATEMESSAGE=14, USERSREGISTEREDLIST=15,
            ALREADYLOGGED = 16, CHATROOMEXISTS=17, GETCHATROOMS = 18, CHATACTUALISE = 19
            ,TOOMANYCHATS = 20, USERLEFT=21;

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
    private ArrayList<User> usersRegistered;
    private ArrayList<Boolean> privateList;

    /**
     * 3 parameter constructor
     * @param type information about the type of message
     * @param user user who sent the message or who the message is adressed to
     * @param message string with message
     */
    public Message(int type, String user, String message){
        this.type = type;
        this.message = message;
        this.user = user;
    }

    /**
     * 4 parameter constructor
     * @param type information about the type of message
     * @param user user who sent the message or who the message is adressed to
     * @param message message to user
     * @param chatRoom chatroom which the message is from
     */
    public Message(int type, String user, String message, ChatRoom chatRoom){
        this(type, user, message);
        this.chatRoom = chatRoom;
    }

    /**
     * 4 parameter constructor
     * @param type information about the type of message
     * @param user user who sent the message or who the message is adressed to
     * @param message message to user
     * @param sentToChat string of chatName
     */
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


    public ArrayList<Boolean> getPrivateList() {
        return privateList;
    }

    public void setPrivateList(ArrayList<Boolean> privateList) {
        this.privateList = privateList;
    }


    public String getSentToChat() {
        return sentToChat;
    }

    public void setSentToChat(String sentToChat) {
        this.sentToChat = sentToChat;
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

    public ArrayList<User> getUsersRegistered() {
        return usersRegistered;
    }

    public void setUsersRegistered(ArrayList<User> usersRegistered) {
        this.usersRegistered = usersRegistered;
    }

}