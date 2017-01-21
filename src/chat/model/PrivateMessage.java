package chat.model;

import java.util.ArrayList;

/**
 * Created by Oskar on 21/01/2017.
 */
public class PrivateMessage {

    private String user;
    private String message;

    PrivateMessage(String user, String message){
        this.user = user;
        this.message = message;
    }


    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
