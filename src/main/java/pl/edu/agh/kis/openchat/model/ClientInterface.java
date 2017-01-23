package main.java.pl.edu.agh.kis.openchat.model;

import main.java.pl.edu.agh.kis.openchat.controller.ClientControllerInterface;

/**
 * Created by Oskar on 22/01/2017.
 */
public interface ClientInterface {
    boolean start();
    void sendMessage(Message msg);
    public void sendPrivateMessage(PrivateMessage msg);
    public void disconnect();
    public void userInvited(String userInvited,String toChat, String admin);
    void setController(ClientControllerInterface cg);
}
