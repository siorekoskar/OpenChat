package main.java.pl.edu.agh.kis.openchat.model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Oskar on 23/01/2017.
 */
public class MessageTest {

    @Test
    public void testMessagesTypes(){
        assertEquals(1, Message.MESSAGE);
        assertEquals(2, Message.CREATECHAT);
        assertEquals(3, Message.CONNECTTOCHAT);
        assertEquals(4, Message.CHATCONNECTION);
        assertEquals(5, Message.LOGINMSG);
        assertEquals(6, Message.ALLOWED);
        assertEquals(7, Message.REGISTER);
        assertEquals(8, Message.DISALLOWED);
        assertEquals(9, Message.EXISTS);
        assertEquals(10, Message.CHATLEFT);
        assertEquals(11, Message.NOTALLOWED);
        assertEquals(12, Message.USERINVITED);
        assertEquals(13, Message.DISCONNECT);
        assertEquals(14, Message.PRIVATEMESSAGE);
        assertEquals(15, Message.USERSREGISTEREDLIST);
        assertEquals(16, Message.ALREADYLOGGED);
        assertEquals(17, Message.CHATROOMEXISTS);
        assertEquals(18, Message.GETCHATROOMS);
        assertEquals(19, Message.CHATACTUALISE);
        assertEquals(20, Message.TOOMANYCHATS);
        assertEquals(21, Message.USERLEFT);
    }

}