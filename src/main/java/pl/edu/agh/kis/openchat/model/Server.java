package main.java.pl.edu.agh.kis.openchat.model;

import main.java.pl.edu.agh.kis.openchat.controller.DBControllerInterface;
import main.java.pl.edu.agh.kis.openchat.controller.DataBaseControllerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Oskar on 14/01/2017.
 */

/**
 * Chat server class, holds all the informations about users, chatrooms, messages
 * and connects to database
 */
public class Server {

    private static int uniqueID;
    private ArrayList<ClientThread> clientThreads;
    private ArrayList<String> users;
    private ArrayList<ChatRoom> chatRooms;
    private ArrayList<User> usersRegistered;

    private int port;
    private boolean keepGoing;
    private DBControllerInterface dbController;


    /**
     * Constructor of server
     * @param port port on which you want to start the server
     * @param url url of the host
     * @param admin login to database
     * @param password password to database on this server
     * @param dbControllerInterface class implementing the controller of the database
     */
    public Server(int port, String url, String admin, String password, DBControllerInterface dbControllerInterface) {

        this.port = port;
        users = new ArrayList<>();
        clientThreads = new ArrayList<>();
        chatRooms = new ArrayList<>();
        dbController = dbControllerInterface;

        try {
            dbController.connect(url, admin, password);
            dbController.load();
            usersRegistered = new ArrayList<>(dbController.getUsers());
        } catch (Exception e) {
            e.printStackTrace();
        }
        ChatRoom allChat = new ChatRoom(ChatRoom.PUBLIC, "none", "All Chat");
        chatRooms.add(allChat);
    }

    private class HandleDB extends Thread {

        Socket socket;
        ClientThread ct;

        HandleDB(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            ct = new ClientThread(socket);
            clientThreads.add(ct);
            users.add(ct.getUsername());
            actualizeUsers();
            actualizeChats();
            sendUsersRegistered();
            ct.start();
        }
    }

    /**
     * Method that allows you to start the server
     */
    public void start() {
        keepGoing = true;
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (keepGoing) {
                System.out.println("Server waiting for clients on port " + port);

                Socket socket = serverSocket.accept();
                System.out.println("Someone connected");

                if (!keepGoing) {
                    break;
                }

                HandleDB handler = new HandleDB(socket);
                handler.start();

            }
            try {
                serverSocket.close();
                dbController.disconnect();
                for (int i = 0; i < clientThreads.size(); ++i) {
                    try {
                        ClientThread tc = clientThreads.get(i);
                        tc.sInput.close();
                        tc.sOutput.close();
                        tc.socket.close();
                    } catch (IOException ioE) {
                    }
                }

            } catch (Exception e) {
                System.out.println("Exception closing the server and clients: " + e);
            }

        } catch (IOException e) {
            System.out.println("Something went bad");
        }


    }



    synchronized void remove(int id) {
        for (int i = 0; i < clientThreads.size(); ++i) {
            ClientThread ct = clientThreads.get(i);
            if (ct.id == id) {
                clientThreads.remove(i);
                users.remove(i);
                return;
            }
        }
    }

    private synchronized void createChatRoom(Message msg) throws IOException {

        ChatRoom chatRoom = msg.getChatRoom();
        ChatRoom chatToAdd = new ChatRoom(chatRoom.isPrivate(), chatRoom.getAdmin(),
                chatRoom.getChatName());
        chatToAdd.addAllowed(chatRoom.getAdmin());
        chatRooms.add(chatToAdd);
        for (int i = 0; i < clientThreads.size(); i++) {
            ClientThread ct = clientThreads.get(i);

            for (ChatRoom chat :
                    chatRooms) {
                if (!ct.writeMsg(new ChatRoom(chat))) {
                    clientThreads.remove(i);
                    users.remove(i);
                }
            }
        }
    }

    private synchronized void actualizeUsers() {
        for (int i = 0; i < clientThreads.size(); i++) {
            ClientThread ct = clientThreads.get(i);

            for (int j = 0; j < clientThreads.size(); j++) {
                if (!ct.writeMsg(new User(clientThreads.get(j).getUsername()))) {
                    clientThreads.remove(i);
                    users.remove(i);
                }
            }

        }
    }

    private synchronized void actualizeChatUsers(ChatRoom chat, Message msg, ChatRoom oldChat) {
        for (ClientThread ct :
                clientThreads) {

            msg.setChat(chat);
            if (ct.currentChatS != null
                    && ct.currentChat.getChatName().equals(chat.getChatName())) {

                ArrayList<String> users = new ArrayList<>(chat.getUsersIn());
                msg.users = users;
                if (!ct.writeMsg(msg)) {
                    clientThreads.remove(ct);
                    users.remove(ct.getUsername());
                }
            } else if (ct.currentChatS != null && ct.currentChat != null
                    && oldChat != null && ct.currentChat.getChatName().equals(oldChat.getChatName())) {

                ArrayList<String> users = new ArrayList<>(oldChat.getUsersIn());
                Message oldMess = new Message(Message.CHATLEFT);
                oldMess.setUsersIn(users);
                oldMess.setChat(oldChat);
                if (!ct.writeMsg(oldMess)) {
                    clientThreads.remove(ct);
                    users.remove(ct.getUsername());
                }
            }
        }
    }

    private synchronized void actualizeChats() {

        for (ClientThread ct :
                clientThreads) {
            /*for (ChatRoom chat :
                    chatRooms) {
                if (!ct.writeMsg(chat)) {
                    clientThreads.remove(ct);
                    users.remove(ct.getUsername());
                }
            }*/
            Message msg = new Message(Message.CHATACTUALISE, "", "");
            ArrayList<String> chats = new ArrayList();
            ArrayList<Boolean> chatsPrivate = new ArrayList<Boolean>();

            for (ChatRoom chat :
                    chatRooms) {
                String name = chat.getChatName();
                chats.add(name);
                chatsPrivate.add(chat.isPrivate());
            }
            msg.setListOfUndeclared(chats);
            msg.setPrivateList(chatsPrivate);

            if (!ct.writeMsg(msg)) {
                clientThreads.remove(ct);
                users.remove(ct.getUsername());
            }
        }

    }

    private synchronized ChatRoom showChatRoom(String chatSearched) {
        for (ChatRoom chat :
                chatRooms) {
            if (chat.getChatName().equals(chatSearched)) {
                return chat;
            }
        }
        return null;
    }

    private synchronized void sendMessage(Message msg, ChatRoom cm) {
        cm.appendMsg(msg.getMessage() + "\n");
        for (ClientThread ct :
                clientThreads) {
            if (ct.currentChat == cm) {
                if (!ct.writeMsg(msg)) {
                    clientThreads.remove(ct);
                    users.remove(ct.getUsername());
                }
            }
        }
    }

    private synchronized void sendToAll(Message msg) {

        msg.setUsersIn(users);
        for (ClientThread ct :
                clientThreads) {
            if (!ct.writeMsg(msg)) {
                clientThreads.remove(ct);
                users.remove(ct.getUsername());
            }
        }
    }

    private synchronized ChatRoom removeFromOldChat(String username) {
        //String username = cm.getUser();
        for (ChatRoom chat :
                chatRooms) {
            if (chat.userExists(username)) {
                chat.deleteUser(username);
                return chat;
            }
        }
        return null;
    }

    private synchronized void inviteUser(String admin, String toInvite, String toChat) {

        ChatRoom chatToInvite = findChat(toChat);
        chatToInvite.addAllowed(toInvite);
        ClientThread invitedClient = findClientThread(toInvite);
        PrivateMessage pm = new PrivateMessage(admin, "invited you to chat: " + toChat, toInvite);
        invitedClient.writeMsg(pm);

    }


    private synchronized void userLeftActualise(String username, ChatRoom chat) {

        removeFromOldChat(username);

        Iterator<ChatRoom> iter = chatRooms.iterator();
        while (iter.hasNext()) {
            ChatRoom chatR = iter.next();
            if (chatR.getAdmin().equals(username)) {
                if (chatR.getUsersIn().isEmpty()) {
                    iter.remove();
                    System.out.println(chatR.getChatName() + "removed");
                }
            }
        }

        actualizeChats();
        for (ClientThread ct :
                clientThreads) {

            ArrayList<String> users = new ArrayList<>(chat.getUsersIn());
            Message msg = new Message(Message.CHATLEFT);
            msg.setUsersIn(users);
            msg.setChat(chat);
            if (!ct.writeMsg(msg)) {
                clientThreads.remove(ct);
                users.remove(ct.getUsername());
            }
        }
    }

    private synchronized boolean isAnyUserInChat(ChatRoom chat) {
        System.out.println(chat.getUsersIn());
        boolean isEmpty = chat.getUsersIn().isEmpty();
        ClientThread ct = findClientThread(chat.getAdmin());
        return ((isEmpty && ct == null) && !chat.getChatName().equals("All Chat"));

    }

    private synchronized void sendUsersRegistered() {
        for (ClientThread ct :
                clientThreads) {
            Message msg = new Message(Message.USERSREGISTEREDLIST);
            ArrayList<String> usersRegisteredString = new ArrayList<>();
            for (User user :
                    usersRegistered) {
                usersRegisteredString.add(user.getLogin());
            }
            msg.setUsersIn(usersRegisteredString);
            if (!ct.writeMsg(msg)) {
                clientThreads.remove(ct);
                users.remove(ct.getUsername());
            }
        }
    }

    private synchronized User findUser(String messageTo) {
        for (User user :
                usersRegistered) {
            if (user.getLogin().equals(messageTo)) {
                return user;
            }
        }
        return null;
    }

    private synchronized void sendPrivateMessageTo(String userTo, PrivateMessage pm) {
        for (ClientThread ct :
                clientThreads) {
            if (ct.getUsername().equals(userTo)) {
                if (!ct.writeMsg(pm)) {
                    clientThreads.remove(ct);
                    users.remove(ct.getUsername());
                }
                return;
            }
        }
    }

    private synchronized boolean checkIfLogged(String user) {
        for (ClientThread ct :
                clientThreads) {
            if (ct.getUsername().equals(user)) {
                return true;
            }
        }
        return false;
    }

    private synchronized ChatRoom findChat(String chatName) {
        for (ChatRoom chat :
                chatRooms) {
            if (chatName.equals(chat.getChatName())) {
                return chat;
            }
        }
        return null;
    }

    private synchronized List findChatsOf(String username) {
        ArrayList<ChatRoom> chatsOfUser = new ArrayList();
        for (ChatRoom chat :
                chatRooms) {
            if (chat.getAdmin().equals(username)) {
                chatsOfUser.add(chat);
            }
        }

        return chatsOfUser;
    }

    private ClientThread findClientThread(String username) {
        for (ClientThread ct :
                clientThreads) {
            if (ct.getUsername().equals(username)) {
                return ct;
            }
        }
        return null;
    }

    private class ClientThread extends Thread {
        Socket socket;
        ObjectInputStream sInput;
        ObjectOutputStream sOutput;
        int id;
        private Message cm;
        private ChatRoom currentChat;
        public String currentChatS;
        private String username;
        int chatsIHave = 0;

        public String getUsername() {
            return username;
        }

        ClientThread(Socket socket) {

            this.socket = socket;
            try {
                sOutput = new ObjectOutputStream(socket.getOutputStream());
                sInput = new ObjectInputStream(socket.getInputStream());
                while (true) {
                    Message user = (Message) sInput.readObject();
                    String userString = user.getUser();
                    String pass = user.getMessage();

                    try {
                        // dbControl(user);
                        dbController.load();
                        if (user.getType() == Message.REGISTER) {
                            if (dbController.checkIfUserExistsServ(userString)) {
                                sOutput.writeObject(new Message(Message.EXISTS, userString, ""));
                                continue;
                            }
                            dbController.addUser(new User(userString, pass));
                            dbController.save();
                            dbController.load();
                            usersRegistered = new ArrayList<>(dbController.getUsers());
                            System.out.println(usersRegistered);
                            sendUsersRegistered();
                            sOutput.writeObject(new Message(Message.REGISTER, userString, ""));

                        } else if (dbController.checkIfUserExistsServ(userString)) {
                            if (checkIfLogged(userString)) {
                                sOutput.writeObject(new Message(Message.ALREADYLOGGED, userString, ""));
                            } else {
                                sOutput.writeObject(new Message(Message.ALLOWED, userString, ""));
                                username = userString;
                                User userFound = findUser(username);
                                List pms = userFound.getPrivateMessages();
                                for (Object obj :
                                        pms) {
                                    PrivateMessage pm = (PrivateMessage) obj;
                                    //sendPrivateMessageTo(username, );
                                    sOutput.writeObject(pm);
                                }
                                break;
                            }
                        } else {
                            sOutput.writeObject(new Message(Message.DISALLOWED));
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
                //currentChat = chatRooms.get(0);
                //currentChatS = "All Chat";
                connectToChat(username, chatRooms.get(0).getChatName(), false);


                System.out.println(username + " connected");
            } catch (IOException e) {
                e.printStackTrace();
                return;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            id = ++uniqueID;
        }

        private boolean connectToChat(String user, String chatSearched, boolean userLeft) {
            ChatRoom chat = showChatRoom(chatSearched);

            try {
                if ((!chat.isPrivate() || chat.getAreAllowed().contains(user))) {
                    removeFromOldChat(username);

                    Iterator<ChatRoom> iter = chatRooms.iterator();
                    while (iter.hasNext()) {
                        ChatRoom chatR = iter.next();
                        if (isAnyUserInChat(chatR)) {
                            iter.remove();
                            System.out.println(chatR.getChatName() + "removed");
                        }
                    }

                    actualizeChats();
                    if (!chat.userExists(user)) {
                        chat.addUsersIn(user);
                    }
                    ChatRoom oldChat = null;
                    if (currentChat != null) oldChat = currentChat;
                    currentChat = chat;
                    currentChatS = chat.getChatName();

                    String chatStructure = chat.getMessages();

                    System.out.println(chat.getUsersAsString());
                    Message msg = new Message(Message.CHATCONNECTION, username, chatStructure, username + " joined the chat");
                    msg.setChatName(currentChat.getChatName());
                    msg.setUsersIn(chat.getUsersIn());
                    actualizeChatUsers(chat, msg, oldChat);

                    return true;
                }
            } catch (RuntimeException e) {
                actualizeChats();
            }
            return false;
        }

        boolean checkIfChatRoomExists(String newChat) {
            for (ChatRoom chat :
                    chatRooms) {
                if (chat.getChatName().equals(newChat)) {
                    return true;
                }
            }
            return false;
        }

        public void run() {
            boolean keepGoing = true;
            while (keepGoing) {
                try {
                    Object obj = sInput.readObject();

                    if (obj instanceof Message) {
                        cm = (Message) obj;

                        switch (cm.getType()) {
                            case Message.MESSAGE:
                                String sentMsg = cm.getUser() + ": " + cm.getMessage();
                                Message message = new Message(Message.MESSAGE, "", sentMsg);
                                sendMessage(message, currentChat);
                                break;
                            case Message.CREATECHAT:
                                String chatName = cm.getChatRoom().getChatName();
                                if (!checkIfChatRoomExists(chatName) && !chatName.equals("") && chatsIHave < 2) {
                                    chatsIHave++;
                                    System.out.println("CHATS " + chatsIHave);
                                    createChatRoom(cm);
                                    Message msg = findListOfChats();
                                    writeMsg(msg);
                                } else if (chatsIHave > 1) {
                                    writeMsg(new Message(Message.TOOMANYCHATS));
                                } else {
                                    writeMsg(new Message(Message.CHATROOMEXISTS, "", chatName));
                                }
                                break;
                            case Message.CONNECTTOCHAT:
                                String user = cm.getUser();
                                String connectToChat = cm.getMessage();
                                if (!connectToChat(user, connectToChat, false)) {
                                    this.writeMsg(new Message(Message.NOTALLOWED));
                                    askForInvite(connectToChat);
                                }
                                break;
                            case Message.USERINVITED:
                                String admin = cm.getUser();
                                String toInvite = cm.getMessage();
                                String toChat = cm.getSentToChat();
                                inviteUser(admin, toInvite, toChat);
                                break;
                            default:
                                break;
                        }
                    } else if (obj instanceof PrivateMessage) {
                        PrivateMessage pm = (PrivateMessage) obj;
                        User userTo = findUser(pm.getMessageTo());
                        userTo.addPrivateMessage(pm);
                        sendPrivateMessageTo(userTo.getLogin(), pm);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    userLeftActualise(username, currentChat);
                    sendUserLeft(username, currentChat);
                    close();
                    break;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    close();
                    break;
                }
            }

            remove(id);
            close();
            sendToAll(new Message(Message.DISCONNECT));
        }

        void askForInvite(String chatName) {
            ChatRoom chat = findChat(chatName);
            String admin = chat.getAdmin();
            ClientThread ct = findClientThread(admin);
            ct.writeMsg(new PrivateMessage(username, " tried to join: "
                    + chatName, admin));
        }

        private void close() {
            // try to close the connection
            try {
                if (sOutput != null) sOutput.close();
            } catch (Exception e) {
                System.out.println("couldnt close output");
            }
            try {
                if (sInput != null) sInput.close();
            } catch (Exception e) {
                System.out.println("couldnt close input");
            }
            try {
                if (socket != null) socket.close();
            } catch (Exception e) {
                System.out.println("couldnt close socket");
            }
           // keepGoing = false;
        }

        private boolean writeMsg(Object msg) {
            // if Client is still connected send the message to it
            if (!socket.isConnected()) {
                close();
                return false;
            }
            // write the message to the stream

            try {
                sOutput.writeObject(msg);
            }
            // if an error occurs, do not abort just inform the user
            catch (IOException e) {
                System.out.println("Error sending message to ");

            }

            return true;
        }

        Message findListOfChats() {

            ArrayList<ChatRoom> listOfChats = (ArrayList) findChatsOf(username);
            ArrayList<String> yourChatRooms = new ArrayList<>();
            for (ChatRoom chat :
                    listOfChats) {
                yourChatRooms.add(chat.getChatName());
            }
            Message msg = new Message(Message.GETCHATROOMS);
            msg.setListOfUndeclared(yourChatRooms);
            return msg;
        }
    }

    private void sendUserLeft(String username, ChatRoom currentChat) {
        for (ClientThread ct :
                clientThreads) {
            if(ct.currentChat.getChatName().equals(currentChat.getChatName())){
                ct.writeMsg(new Message(Message.USERLEFT, username, "",username + " left the chat"));
            }
        }

    }


    public static void main(String[] args) {
        int portNumber = 3308;
        String url = "localhost:3306/user";
        String admin = "root";
        String password = "shihtzu1";

        DBControllerInterface dbController = DataBaseControllerFactory.returnDBController(DataBaseControllerFactory.DBCONTROLLER);
        // create a server object and start it
        Server server = new Server(portNumber, url, admin, password, dbController);
        server.start();
    }
}
