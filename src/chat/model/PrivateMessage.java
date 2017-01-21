package chat.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Oskar on 21/01/2017.
 */
public class PrivateMessage implements Serializable{

    private String messageFrom;
    private String message;
    private String messageTo;

     public PrivateMessage(String messageFrom, String message, String messageTo){
        this.messageFrom = messageFrom;
        this.messageTo = messageTo;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessageTo() {
        return messageTo;
    }
    public void setMessageTo(String messageTo) {
        this.messageTo = messageTo;
    }
    public String getMessageFrom() {
        return messageFrom;
    }
    public void setMessageFrom(String messageFrom) {
        this.messageFrom = messageFrom;
    }

}
