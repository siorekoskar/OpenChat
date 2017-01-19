package chat.model;

import chat.controller.ClientController;
import chat.controller.DbController;
import chat.gui.FormEvent;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Oskar on 14/01/2017.
 */
public class Server {

    private static int uniqueID;
    private ArrayList<ClientThread> clientThreads;
    private ArrayList<ChatRoom> chatRooms;
    private int port;
    private boolean keepGoing;
    private DbController dbController;

    public Server(int port) {
        this.port = port;
        clientThreads = new ArrayList<>();
        chatRooms = new ArrayList<>();
        dbController = new DbController();
        try {
            dbController.connect();
        } catch (Exception e) {
            e.printStackTrace();
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

                ClientThread t = new ClientThread(socket);
                clientThreads.add(t);

                actualizeUsers();
                actualizeChats();
                t.start();
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

    private synchronized void broadcast(String msg){
        for(int i = clientThreads.size(); --i>=0;){
            ClientThread ct = clientThreads.get(i);

            if(!ct.writeMsg(msg)) {
                clientThreads.remove(i);
                System.out.println("REMOVED CLIENT " + ct.username);
            }
        }
    }

    synchronized void remove(int id){
        for(int i = 0; i< clientThreads.size(); ++i){
            ClientThread ct = clientThreads.get(i);
            if(ct.id == id){
                clientThreads.remove(i);
                return;
            }
        }
    }

    private synchronized void createChatRoom(Message msg) throws IOException{
        for (int i = 0; i < clientThreads.size(); i++) {
            ClientThread ct = clientThreads.get(i);

            ChatRoom chatRoom = msg.getChatRoom();
            ChatRoom chatToAdd = new ChatRoom(chatRoom.isPrivate(), chatRoom.getAdmin(),
                    chatRoom.getChatName());
            chatRooms.add(chatToAdd);

            for (ChatRoom chat :
                    chatRooms) {
                if(!ct.writeMsg(new ChatRoom(chat))){
                    clientThreads.remove(i);
                }
            }
        }
    }

    private synchronized void actualizeUsers(){
        for (int i = 0; i < clientThreads.size(); i++) {
            ClientThread ct = clientThreads.get(i);

            for (int j = 0; j < clientThreads.size(); j++) {
                if(!ct.writeMsg(new User(clientThreads.get(j).getUsername()))){
                    clientThreads.remove(i);
                }
            }

        }


    }

    private synchronized void actualizeChats(){
        for (ClientThread ct :
                clientThreads) {
            for (ChatRoom chat :
                    chatRooms) {
                if(!ct.writeMsg(chat)){
                    clientThreads.remove(ct);
                }
            }
        }

    }

    private synchronized ChatRoom showChatRoom(Message msg){
        for (ChatRoom chat :
                chatRooms) {
            if (chat.getChatName().equals(msg.getMessage())) {
                return chat;
            }
        }
        return null;
    }

    private synchronized void sendMessage(Message msg, ChatRoom cm){
        cm.appendMsg(msg.getMessage() + "\n");
        for (ClientThread ct :
                clientThreads) {
            if(ct.currentChat == cm){
                ct.writeMsg(msg);
            }
        }
    }



    class ClientThread extends Thread{
        Socket socket;
        ObjectInputStream sInput;
        ObjectOutputStream sOutput;
        int id;
        private Message cm;
        private ChatRoom currentChat;

        public String getUsername() {
            return username;
        }

        private String username;


        ClientThread(Socket socket){

            this.socket = socket;
            try{
                sOutput = new ObjectOutputStream(socket.getOutputStream());
                sInput= new ObjectInputStream(socket.getInputStream());
                while(true){
                    Message user = (Message)sInput.readObject();
                    String userString = user.getUser();
                    String pass = user.getMessage();

                    try {
                        dbController.load();
                        if(user.getType() == Message.REGISTER){
                            if(dbController.checkIfUserExistsServ(userString)){
                                sOutput.writeObject(new Message(Message.EXISTS, userString,""));
                                continue;
                            }
                            dbController.addUser(new User(userString, pass));
                            dbController.save();
                            sOutput.writeObject(new Message(Message.REGISTER, userString, ""));

                        } else if (dbController.checkIfUserExistsServ(userString)) {
                            sOutput.writeObject(new Message(Message.ALLOWED, userString, ""));
                            username = userString;
                            break;
                        } else {
                            sOutput.writeObject(new Message(Message.DISALLOWED));
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
                currentChat = null;

                System.out.println(username + " connected");
            } catch(IOException e){
                e.printStackTrace();
                return;
            } catch(ClassNotFoundException e){
                e.printStackTrace();
            }
            id = ++uniqueID;
        }


        public void run(){
            boolean keepGoing = true;
            while(keepGoing){
                try{
                    cm = (Message) sInput.readObject();

                    switch(cm.getType()){
                        case Message.MESSAGE:
                            String sentMsg = cm.getUser() + ": " + cm.getMessage();
                            Message message = new Message(Message.MESSAGE, "", sentMsg);
                            sendMessage(message, currentChat);
                            break;
                        case Message.CREATECHAT:
                            createChatRoom(cm);
                            break;
                        case Message.CONNECTTOCHAT:
                            ChatRoom chat = showChatRoom(cm);
                            String user = cm.getUser();
                            if(!chat.userExists(user)) {
                                chat.addUsersIn(user);
                            }
                            currentChat = chat;
                            String chatStructure = chat.getUsersAsString() + "\n" + chat.getMessages();
                            Message msg = new Message(Message.CHATCONNECTION, "", chatStructure);
                            writeMsg(msg);
                            break;
                    }

                } catch(IOException e){
                    e.printStackTrace();
                    break;
                } catch (ClassNotFoundException e){
                    e.printStackTrace();
                    break;
                }
            }

            remove(id);
            close();
        }

        private void close() {
            // try to close the connection
            try {
                if(sOutput != null) sOutput.close();
            }
            catch(Exception e) {}
            try {
                if(sInput != null) sInput.close();
            }
            catch(Exception e) {}
            try {
                if(socket != null) socket.close();
            }
            catch (Exception e) {}
           // keepGoing = false;
        }

        private boolean writeMsg(Object msg) {
            // if Client is still connected send the message to it
            if(!socket.isConnected()) {
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

        // create a server object and start it
        Server server = new Server(portNumber);
        server.start();
    }
}
