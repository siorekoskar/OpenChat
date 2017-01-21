package chat.model;

import chat.controller.ClientController;
import chat.controller.DbController;
import chat.gui.FormEvent;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Oskar on 14/01/2017.
 */
public class Server {

    private static int uniqueID;
    private ArrayList<ClientThread> clientThreads;
    private ArrayList<String> users;
    private ArrayList<ChatRoom> chatRooms;
    private ArrayList<User> usersRegistered;

    private int port;
    private boolean keepGoing;
    private DbController dbController;


    public Server(int port, String url, String admin, String password) {

        this.port = port;
        users = new ArrayList<>();
        clientThreads = new ArrayList<>();
        chatRooms = new ArrayList<>();
        dbController = new DbController();

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
                //ClientThread t = new ClientThread(socket);
                //ClientThread t = handler.getCt();
                // clientThreads.add(t);
                /// t.start();
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

    private synchronized void broadcast(String msg) {
        for (int i = clientThreads.size(); --i >= 0; ) {
            ClientThread ct = clientThreads.get(i);

            if (!ct.writeMsg(msg)) {
                clientThreads.remove(i);
                users.remove(i);
                System.out.println("REMOVED CLIENT " + ct.username);
            }
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

        for (int i = 0; i < clientThreads.size(); i++) {
            ClientThread ct = clientThreads.get(i);

            ChatRoom chatRoom = msg.getChatRoom();
            ChatRoom chatToAdd = new ChatRoom(chatRoom.isPrivate(), chatRoom.getAdmin(),
                    chatRoom.getChatName());
            chatToAdd.addAllowed(chatRoom.getAdmin());
            chatRooms.add(chatToAdd);

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
            for (ChatRoom chat :
                    chatRooms) {
                if (!ct.writeMsg(chat)) {
                    clientThreads.remove(ct);
                    users.remove(ct.getUsername());
                }
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

    public synchronized void sendToAll(Message msg) {

        msg.setUsersIn(users);
        for (ClientThread ct :
                clientThreads) {
            if (!ct.writeMsg(msg)) {
                clientThreads.remove(ct);
                users.remove(ct.getUsername());
            }
        }
    }

    private synchronized void removeFromOldChat(String username) {
        //String username = cm.getUser();
        for (ChatRoom chat :
                chatRooms) {
            if (chat.userExists(username)) {
                chat.deleteUser(username);
            }
        }
    }

    public synchronized void inviteUser(Message msg) {

        String toAdd = msg.getMessage();
        System.out.println("invited  " + toAdd);
        for (ChatRoom chat :
                chatRooms) {
            if (chat.getAdmin().equals(msg.getUser()) && !chat.getAreAllowed().contains(toAdd)) {
                System.out.println("added " + toAdd + " to chat " + chat.getChatName());
                chat.addAllowed(toAdd);
            }
        }
    }

    synchronized void userLeftActualise(String username, ChatRoom chat) {
        for (ClientThread ct :
                clientThreads) {
            if (chat != null) {
                removeFromOldChat(username);
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
    }

    synchronized void sendUsersRegistered(){
        for (ClientThread ct :
                clientThreads) {
            Message msg = new Message(Message.USERSREGISTEREDLIST);
            ArrayList<String> usersRegisteredString = new ArrayList<>();
            for (User user :
                    usersRegistered) {
                usersRegisteredString.add(user.getLogin());
            }
            msg.setUsersIn(usersRegisteredString);
            if(!ct.writeMsg(msg)){
                clientThreads.remove(ct);
                users.remove(ct.getUsername());
            }
        }
    }
    
    synchronized User findUser(String messageTo){
        for (User user :
                usersRegistered) {
            if (user.getLogin().equals(messageTo)) {
                return user;
            }
        }
        return null;
    }

    synchronized void sendPrivateMessageTo(String userTo, PrivateMessage pm){
        for (ClientThread ct :
                clientThreads) {
            if(ct.getUsername().equals(userTo)){
                if(!ct.writeMsg(pm)){
                    clientThreads.remove(ct);
                    users.remove(ct.getUsername());
                }
                return;
            }
        }
    }

    synchronized boolean checkIfLogged(String user){
        for (ClientThread ct :
                clientThreads) {
            if(ct.getUsername().equals(user)){
                return true;
            }
        }
        return false;
    }

    class ClientThread extends Thread {
        Socket socket;
        ObjectInputStream sInput;
        ObjectOutputStream sOutput;
        int id;
        private Message cm;
        private ChatRoom currentChat;
        public String currentChatS;
        private String username;
        private boolean firstLogged = true;

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
                            if(checkIfLogged(userString)){
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

            if ((!chat.isPrivate() || (chat.isPrivate() && chat.getAreAllowed().contains(user)))) {

                removeFromOldChat(user);

                if (!chat.userExists(user)) {
                    chat.addUsersIn(user);
                }
                ChatRoom oldChat = null;
                if (currentChat != null) oldChat = currentChat;
                currentChat = chat;
                currentChatS = chat.getChatName();

                String chatStructure = chat.getMessages();
                System.out.println(chat.getUsersAsString());
                Message msg = new Message(Message.CHATCONNECTION, "", chatStructure);
                msg.setChatName(currentChat.getChatName());
                msg.setUsersIn(chat.getUsersIn());
                actualizeChatUsers(chat, msg, oldChat);

                return true;
            }
            return false;
        }

        boolean checkIfChatRoomExists(String newChat){
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
                                if(!checkIfChatRoomExists(chatName)) {
                                    createChatRoom(cm);
                                } else {
                                    writeMsg(new Message(Message.CHATROOMEXISTS, "", chatName));
                                }
                                break;
                            case Message.CONNECTTOCHAT:
                                if (!connectToChat(cm.getUser(), cm.getMessage(), false)) {
                                    this.writeMsg(new Message(Message.NOTALLOWED));
                                }
                                break;
                            case Message.USERINVITED:
                                inviteUser(cm);
                                break;
                        }
                    } else if (obj instanceof PrivateMessage){
                        PrivateMessage pm = (PrivateMessage)obj;
                        System.out.println("Message from: " + pm.getMessageFrom() + " to: " +
                                pm.getMessageTo() + ": " + pm.getMessage());
                        User userTo = findUser(pm.getMessageTo());
                        userTo.addPrivateMessage(pm);
                        sendPrivateMessageTo(userTo.getLogin(), pm);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    userLeftActualise(username, currentChat);
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

        private void close() {
            // try to close the connection
            try {
                if (sOutput != null) sOutput.close();
            } catch (Exception e) {
            }
            try {
                if (sInput != null) sInput.close();
            } catch (Exception e) {
            }
            try {
                if (socket != null) socket.close();
            } catch (Exception e) {
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
    }


    public static void main(String[] args) {
        int portNumber = 3308;
        String url = "localhost:3306/user";
        String admin = "root";
        String password = "shihtzu1";

        // create a server object and start it
        Server server = new Server(portNumber, url, admin, password);
        server.start();
    }
}
