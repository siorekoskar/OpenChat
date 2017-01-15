package chat.model;

import java.io.Serializable;

/**
 * Created by Oskar on 14/01/2017.
 */
public class Message implements Serializable{
    protected static final long serialVersionUID = 1112122200L;

    public static final int MESSAGE = 1, CREATECHAT = 2;

    private int type;
    private String message;
    private String user;
    private String sentToChat;

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    private ChatRoom chatRoom;

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

    public int getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public String getUser(){
        return user;
    }


}
