package chat.model;

import java.io.DataInputStream;
import java.io.DataOutput;
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
    private ArrayList<ClientThread> al;
    private int port;
    private boolean keepGoing;

    public Server(int port) {
        this.port = port;
        al = new ArrayList<ClientThread>();
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
                al.add(t);
                t.start();
            }
            try {
                serverSocket.close();
                for (int i = 0; i < al.size(); ++i) {
                    try {
                        ClientThread tc = al.get(i);
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
        for(int i = al.size(); --i>=0;){
            ClientThread ct = al.get(i);

            if(!ct.writeMsg(msg)) {
                al.remove(i);
            }
        }
    }

    synchronized  void remove(int id){
        for(int i =0; i<al.size();++i){
            ClientThread ct = al.get(i);
            if(ct.id == id){
                al.remove(i);
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
        // start server on port 1500 unless a PortNumber is specified
        int portNumber = 3306;
     /*   switch(args.length) {
            case 1:
                try {
                    portNumber = Integer.parseInt(args[0]);
                }
                catch(Exception e) {
                    System.out.println("Invalid port number.");
                    System.out.println("Usage is: > java Server [portNumber]");
                    return;
                }
            case 0:
                break;
            default:
                System.out.println("Usage is: > java Server [portNumber]");
                return;

        }*/
        // create a server object and start it
        Server server = new Server(portNumber);
        server.start();
    }
}
