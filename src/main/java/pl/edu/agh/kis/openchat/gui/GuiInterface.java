package main.java.pl.edu.agh.kis.openchat.gui;

import main.java.pl.edu.agh.kis.openchat.controller.ClientControllerInterface;
import main.java.pl.edu.agh.kis.openchat.model.ChatRoom;
import main.java.pl.edu.agh.kis.openchat.model.Message;
import main.java.pl.edu.agh.kis.openchat.model.User;

import java.util.List;

/**
 * Created by Oskar on 22/01/2017.
 */
public interface GuiInterface {
     /**
      * Setter for controller
      * @param clientController class that implements ClientControllerInterface
      */
     void setClientController(ClientControllerInterface clientController);

     /**
      * If chat with that name already exists, pop a dialog
      * @param chatname name of chat
      */
     void popChatAlreadyExistsDialog(String chatname);

     /**
      * Sends a message to controller
      * @param msg message with informations about message sent from gui
      */
     void sendMsg(Message msg);

     /**
      * If something fails, pop a fail dialog with message
      * @param msg message that shows in dialog
      */
     void popFail(String msg);

     /**
      * If something succeds, pop a succes dialog with message
      * @param msg message that shows in dialog
      */
     void popSuccess(String msg);

     /**
      * Pop a logged in dialog if user succeds with logging
      * @param user name of user
      */
     void popLoggedDialog(String user);

     /**
      * Pop a dialog that shows that usser is not connected
      * @param msg message displayed with reason of fail
      */
     void popNotConnectedDialog(String msg);

     /**
      * Pop a dialog if connected
      */
     void popConnectedDialog();

     /**
      * Shows users in current chatroom
      * @param users list of users in chat
      */
     void setUsersInChat(List users);

     /**
      * Shows messages in inbox
      * @param messages list of messages to show
      */
     void sendInboxMessages(List messages);

     /**
      * If user left the chat, actualize the list of users
      * @param users current list of users in chat
      */
     void userLeftChat(List users);

     /**
      * IF user left server, actualize list of users
      * @param users list of users in server
      */
     void actualizeCauseUserLeftServer(List users);

     /**
      * If a new chat was made, add it to panel of chats
      * @param chat chat with messages and it's name
      */
     void addChatToPanel(ChatRoom chat);

     /**
      * If user joined server, add him to list
      * @param user username
      */
     void addUserWhoJoinedServer(User user);

     /**
      * Sets list of registered users
      * @param list list of users registered
      */
     void sendUsersRegisteredList(List list);

     /**
      * Add private message to list of messages in inbox
      * @param msg private message content
      */
     void sendPrivateMsgToGui(String msg);

     /**
      * Set list of your chatrooms
      * @param chatRooms list of chatrooms which you are admin
      */
     void setYourChatRooms(List chatRooms);

     /**
      * Actualizes chats on list
      * @param chats list of public chats
      * @param privateList list of private chats
      */
     void actualizeChats(List chats, List privateList);

     /**
      * Set host and port in frame
      * @param host host of server
      * @param port port of server
      */
     void setHostAndPort(String host, int port);
}
