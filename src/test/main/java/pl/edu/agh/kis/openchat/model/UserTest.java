package main.java.pl.edu.agh.kis.openchat.model;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Oskar on 23/01/2017.
 */
public class UserTest {

    @Test
    public void testId(){
        User a = new User("a", "1");
        User b = new User("b","2");
        assertEquals(2, User.getCount());
    }

    @Test
    public void testMessagesCount(){
        PrivateMessage msg1 = new PrivateMessage("from", "msg", "to");
        PrivateMessage msg2 = new PrivateMessage("from", "msg", "to");

        User a = new User("a","1");
        a.addPrivateMessage(msg1);
        a.addPrivateMessage(msg2);

        List listOfMsg = a.getPrivateMessages();
        assertNotNull(listOfMsg);
        assertEquals(2, listOfMsg.size());
    }

    @Test
    public void messagesStaySame(){
        PrivateMessage msg1 = new PrivateMessage("from1", "msg", "to");
        PrivateMessage msg2 = new PrivateMessage("from2", "msg", "to");

        User a = new User("a", "1");
        a.addPrivateMessage(msg1);
        a.addPrivateMessage(msg2);

        List listOfMsg = a.getPrivateMessages();
        PrivateMessage msg11 = (PrivateMessage)listOfMsg.get(0);
        PrivateMessage msg22 = (PrivateMessage)listOfMsg.get(1);

        assertEquals(msg11.getMessageFrom(), "from1");
        assertEquals(msg22.getMessageFrom(), "from2");
    }


}