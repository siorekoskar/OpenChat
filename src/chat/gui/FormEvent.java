package chat.gui;

import java.util.EventObject;

/**
 * Created by Oskar on 08/01/2017.
 */
public class FormEvent extends EventObject{

    private String login;
    private String pass;

    public FormEvent(Object source, String login, String pass){
        super(source);

        this.login = login;
        this.pass = pass;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getLogin() {
        return login;
    }

    public String getPass() {
        return pass;
    }
}
