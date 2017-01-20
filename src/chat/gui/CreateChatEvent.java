package chat.gui;

import java.util.EventObject;

/**
 * Created by Oskar on 14/01/2017.
 */
public class CreateChatEvent extends EventObject {

    public String getChatName() {
        return chatName;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    private String chatName;
    private boolean isPrivate;

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    private String admin;

    public CreateChatEvent(Object source, String chatName, boolean isPrivate){
        super(source);

        this.chatName = chatName;
        this.isPrivate = isPrivate;
    }


}
