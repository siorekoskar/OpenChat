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
     void setClientController(ClientControllerInterface clientController);
     void popChatAlreadyExistsDialog(String chatname);
     void sendMsg(Message msg);
     void popFail(String msg);
     void popSuccess(String msg);
     void popLoggedDialog(String user);
     void popNotConnectedDialog(String msg);
     void popConnectedDialog();
     void setUsersInChat(List users);
     void sendInboxMessages(List messages);
     void userLeftChat(List users);
     void actualizeCauseUserLeftServer(List users);
     void addChatToPanel(ChatRoom chat);
     void addUserWhoJoinedServer(User user);
     void sendUsersRegisteredList(List list);
     void sendPrivateMsgToGui(String msg);
     void setYourChatRooms(List chatRooms);
     void actualizeChats(List chats, List privateList);
     void setHostAndPort(String host, int port);
}
