package chat.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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

    public Server(int port) {
        this.port = port;
        clientThreads = new ArrayList<>();
        chatRooms = new ArrayList<>();
    }

    public void start() {
        keepGoing = true;
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (keepGoing) {
                System.out.println("Server waiting for clients on port " + port);

                Socket socket = serverSocket.accept();
                System.out.println("one man");

                if (!keepGoing) {
                    break;
                }

                ClientThread t = new ClientThread(socket);
                clientThreads.add(t);
                t.start();
            }
            try {
                serverSocket.close();
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
            }
        }
    }

    synchronized  void remove(int id){
        for(int i = 0; i< clientThreads.size(); ++i){
            ClientThread ct = clientThreads.get(i);
            if(ct.id == id){
                clientThreads.remove(i);
                return;
            }
        }
    }

    class ClientThread extends Thread{
        Socket socket;
        DataInputStream sInput;
        DataOutputStream sOutput;
        int id;

        ClientThread(Socket socket){
            id = ++uniqueID;
            this.socket = socket;
            try{
                sOutput = new DataOutputStream((socket.getOutputStream()));
                sInput= new DataInputStream(socket.getInputStream());
                System.out.println("SOMEONE CONNECTED");
            } catch(IOException e){
                System.out.println("Exception");
                return;
            }
        }

        public void run(){
            boolean keepGoing = true;
            while(keepGoing){
                try{
                    String msg = sInput.readUTF();
                    broadcast(msg);
                } catch(IOException e){
                    System.out.println();
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
            catch(Exception e) {};
            try {
                if(socket != null) socket.close();
            }
            catch (Exception e) {}
        }

        private boolean writeMsg(String msg) {
            // if Client is still connected send the message to it
            if(!socket.isConnected()) {
                close();
                return false;
            }
            // write the message to the stream
            try {
                sOutput.writeUTF(msg);
            }
            // if an error occurs, do not abort just inform the user
            catch(IOException e) {
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
