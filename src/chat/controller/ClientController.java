package chat.controller;

import chat.gui.CreateChatEvent;
import chat.gui.MainFrame;
import chat.model.*;

import java.util.List;

/**
 * Created by Oskar on 10/01/2017.
 */
public class ClientController {

    private Client chatClient;
    private MainFrame frame;

    public void sendYourChatRooms(List chatRooms){
        frame.setYourChatRooms(chatRooms);
    }

    public void sendPrivateMsgToGui(String msg) {
        frame.sendPrivateMsgToGui(msg);
    }

    public void notConnected(String msg) {
        frame.popNotConnectedDialog(msg);
    }

    public void connected() {
        frame.popConnectedDialog();
    }

    public void sendPrivateMessage(String msg, String messageTo, String messageFrom) {
        PrivateMessage privateMessage = new PrivateMessage(messageFrom, msg, messageTo);
        chatClient.sendPrivateMessage(privateMessage);
    }

    public void sendUserLeft(List users) {
        frame.actualizeCauseUserLeftServer(users);
    }

    public void sendDisallowed() {
        frame.popFail("Wrong username/password");
    }

    public void sendNotAllowed() {
        frame.popFail("You are not allowed to join");
    }

    public void sendAllowed(String user) {
        frame.popLoggedDialog(user);
    }

    public void sendRegistered(String user) {
        frame.popSuccess("Registered as: " + user);
    }

    public void sendExists() {
        frame.popSuccess("User already exists");
    }

    public void sendInboxMessages(List messages) {
        frame.sendInboxMessages(messages);
    }

    public void userInvited(String selected, String toChat, String username) {
        System.out.println("You invited " + selected);
        chatClient.userInvited(selected, toChat, username);
    }

    public ClientController(String serverName, int serverPort, MainFrame frame) {
        chatClient = new Client(serverName, serverPort, this);
        this.frame = frame;
        chatClient.start();
    }

    public void sendMessage(Message msg) {
        chatClient.sendMessage(msg);
    }

    public void sendMsg(Message msg) {
        frame.sendMsg(msg);
        if (msg.getType() == Message.CHATCONNECTION) {
            frame.setUsersInChat(msg.getChat().getUsersIn());
        }
    }

    public void disconnect() {
        chatClient.disconnect();
    }

    public void sendChatUsers(List users) {
        frame.setUsersInChat(users);
    }

    public void sendLeft(List users) {
        frame.userLeftChat(users);
    }

    public void newChatCreated(CreateChatEvent ev) {
        String chatName = ev.getChatName();
        String admin = ev.getAdmin();
        boolean isPrivate = ev.isPrivate();
        ChatRoom chatRoom = new ChatRoom(isPrivate, admin, chatName);
        Message msg = new Message(Message.CREATECHAT, admin, "", chatRoom);
        chatClient.sendMessage(msg);
        //ystem.out.println(chatRoom.toString());
    }

    public void sendChat(ChatRoom chat) {
        frame.addChatToPanel(chat);
    }

    public void sendUser(User user) {
        frame.addUserWhoJoinedServer(user);
    }

    public void sendUsersRegisteredList(List list) {
        frame.sendUsersRegisteredList(list);
    }

    public void sendAlreadyLogged(String username) {
        //frame.popAlreadyLoggedDialog(username);
        frame.popFail("User " + username + " already logged");
    }

    public void sendChatExists(String chat){
        frame.popChatAlreadyExistsDialog(chat);
    }

    public void sendChats(List chats, List privateList){
        frame.actualizeChats(chats, privateList);
    }
    public void sendTooManyChats(){
        frame.popFail("Too many chats. Max is 2");
    }
}