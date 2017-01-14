package chat.model;

import java.io.Serializable;

/**
 * Created by Oskar on 14/01/2017.
 */
public class Message implements Serializable{
    protected static final long serialVersionUID = 1112122200L;

    static final int MESSAGE = 1, CREATECHAT = 2;

    private int type;
    private String message;
    private String user;

    public Message(int type, String user, String message){
        this.type = type;
        this.message = message;
        this.user = user;
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
