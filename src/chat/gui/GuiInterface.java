package chat.gui;

import chat.model.ChatRoom;
import chat.model.Message;
import chat.model.User;

import javax.swing.*;
import java.util.List;

/**
 * Created by Oskar on 22/01/2017.
 */
public interface GuiInterface {
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
}
