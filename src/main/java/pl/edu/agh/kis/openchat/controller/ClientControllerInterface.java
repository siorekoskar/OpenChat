package main.java.pl.edu.agh.kis.openchat.controller;

import main.java.pl.edu.agh.kis.openchat.gui.CreateChatEvent;
import main.java.pl.edu.agh.kis.openchat.gui.GuiInterface;

import main.java.pl.edu.agh.kis.openchat.model.ChatRoom;
import main.java.pl.edu.agh.kis.openchat.model.ClientInterface;
import main.java.pl.edu.agh.kis.openchat.model.Message;
import main.java.pl.edu.agh.kis.openchat.model.User;
import java.util.List;

/**
 * Created by Oskar on 22/01/2017.
 */
public interface ClientControllerInterface {

     /**
      * Set client which connects to server
      * @param client class that implements ClientInterface
      */
     void setClient(ClientInterface client);

     /**
      * Set gui
      * @param gui class that implements GuiInterface
      */
     void setGui(GuiInterface gui);

     /**
      * Send list of your chatrooms into gui
      * @param chatRooms list of your chatrooms
      */
     void sendYourChatRooms(List chatRooms);

     /**
      * Send your private messages into gui
      * @param msg content of private message
      */
     void sendPrivateMsgToGui(String msg);

     /**
      * Tell gui that user is not connected
      * @param msg
      */
     void notConnected(String msg);

     /**
      * Tell gui that user is connected
      */
     void connected();

     /**
      * Send private message to client
      * @param msg content of message
      * @param messageTo to whom it's been sent
      * @param messageFrom from who it's been sent
      */
     void sendPrivateMessage(String msg, String messageTo, String messageFrom);

     /**
      * Send that some user left to gui
      * @param users List of active users
      */
     void sendUserLeft(List users);

     /**
      * Send message that data user sent was wrong
      */
     void sendDisallowed();

     /**
      * Send message that someone is already logged in
      */
     void sendNotAllowed();


     void sendAllowed(String user);

     /**
      * Tell gui that user has been registered
      * @param user username
      */
     void sendRegistered(String user);

     /**
      * Tell gui that user is already registered
      */
     void sendExists();

     /**
      * Send gui list of messages in inbox
      * @param messages list of private messages and events
      */
     void sendInboxMessages(List messages);

     /**
      * Tell server that user has been invited to chat
      * @param selected who was invited
      * @param toChat to which chat
      * @param username by whom
      */
     void userInvited(String selected, String toChat, String username);

     /**
      * Send message to current chat in gui
      * @param msg Message containing informations about user and message
      */
     void sendMessage(Message msg);

     /**
      * Send users message to server
      * @param msg Message containing informations about user and message
      */
     void sendMsg(Message msg);

     /**
      * Tell server user left the chat
      */
     void disconnect();

     /**
      * Send gui the list of active users in current chat
      * @param users
      */
     void sendChatUsers(List users);

     /**
      * Tell gui that some user left the server
      * @param users list of active users
      */
     void sendLeft(List users);

     /**
      * Send server information about new chat created by user
      * @param ev event object with informations about chatname and username
      */
     void newChatCreated(CreateChatEvent ev);

     /**
      * Send Chatroom to gui with informations about users in and old messages to gui
      * @param chat Chatroom with users in and old messages
      */
     void sendChat(ChatRoom chat);

     /**
      * Send information about new user in server
      * @param user
      */
     void sendUser(User user);

     /**
      * Send list of users registered to gui
      * @param list list of users registered
      */
     void sendUsersRegisteredList(List list);

     /**
      * Tell gui that user is already logged on server
      * @param username username of user that is already logged
      */
     void sendAlreadyLogged(String username);

     /**
      * Tell gui that chat with this name is already active on the server
      * @param chat name of chat
      */
     void sendChatExists(String chat);

     /**
      * Send list of public and private chats to gui
      * @param chats public chats
      * @param privateList private chats
      */
     void sendChats(List chats, List privateList);

     /**
      * Tell gui that user already has too many chatrooms
      */
     void sendTooManyChats();


}
