package main.java.pl.edu.agh.kis.openchat.model;

import main.java.pl.edu.agh.kis.openchat.controller.ClientControllerInterface;

/**
 * Created by Oskar on 22/01/2017.
 */
public interface ClientInterface {

    /**
     * Start client and connect to server
     * @return true if connection succeds
     */
    boolean start();

    /**
     * Send message to server
     * @param msg message with data about user and message sent
     * @return
     */
    boolean sendMessage(Message msg);

    /**
     * Send private message to server
     * @param msg information about who sent message, to whom, and what he wrote
     */
    void sendPrivateMessage(PrivateMessage msg);

    /**
     * Disconnect from server
     */
    void disconnect();

    /**
     * Invite user to your chat
     * @param userInvited user who was invited
     * @param toChat which chat user was invited to
     * @param admin admin of the chat
     */
    void userInvited(String userInvited,String toChat, String admin);

    /**
     * Set controller that sends data to client
     * @param cg class that implements ClientControllerInterface
     */
    void setController(ClientControllerInterface cg);
}
