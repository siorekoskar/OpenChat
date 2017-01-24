package main.java.pl.edu.agh.kis.openchat.model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Oskar on 23/01/2017.
 */
public class PrivateMessageTest {

    @Test
    public void testMessagesStayTheSame(){
        PrivateMessage msg = new PrivateMessage("from", "msg", "to");
        String from = "from";
        String msg1 = "msg";
        String to = "to";
        assertEquals(from, msg.getMessageFrom());
        assertEquals(msg1, msg.getMessage());
        assertEquals(to, msg.getMessageTo());
    }

}