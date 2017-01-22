package chat.controller;

import chat.gui.CreateChatEvent;
import chat.gui.GuiInterface;
import chat.gui.MainFrame;
import chat.model.*;

import java.util.List;

/**
 * Created by Oskar on 22/01/2017.
 */
public interface ClientControllerInterface {

     void setClient(ClientInterface client);
     void setGui(GuiInterface gui);
     void sendYourChatRooms(List chatRooms);
     void sendPrivateMsgToGui(String msg);
     void notConnected(String msg);
     void connected();
     void sendPrivateMessage(String msg, String messageTo, String messageFrom);
     void sendUserLeft(List users);
     void sendDisallowed();
     void sendNotAllowed();
     void sendAllowed(String user);
     void sendRegistered(String user);
     void sendExists();
     void sendInboxMessages(List messages);
     void userInvited(String selected, String toChat, String username);
     void sendMessage(Message msg);
     void sendMsg(Message msg);
     void disconnect();
     void sendChatUsers(List users);
     void sendLeft(List users);
     void newChatCreated(CreateChatEvent ev);
     void sendChat(ChatRoom chat);
     void sendUser(User user);
     void sendUsersRegisteredList(List list);
     void sendAlreadyLogged(String username);
     void sendChatExists(String chat);
     void sendChats(List chats, List privateList);
     void sendTooManyChats();


}
