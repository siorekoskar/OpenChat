package chat.model;

/**
 * Created by Oskar on 22/01/2017.
 */
public interface ClientInterface {
    boolean start();
    void sendMessage(Message msg);
    public void sendPrivateMessage(PrivateMessage msg);
    public void disconnect();
    public void userInvited(String userInvited,String toChat, String admin);
}
