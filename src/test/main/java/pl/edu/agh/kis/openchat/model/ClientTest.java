package main.java.pl.edu.agh.kis.openchat.model;

import main.java.pl.edu.agh.kis.openchat.controller.ClientController;
import main.java.pl.edu.agh.kis.openchat.controller.ClientControllerInterface;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

import static org.junit.Assert.*;

/**
 * Created by Oskar on 23/01/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ Client.class })
public class ClientTest {

    private ObjectInputStream is;
    private ObjectOutputStream os;
    private Socket testSocket;
    private ClientControllerInterface cg;
    private Client client;
    private OutputStreamWriter writer;

    @Before
    public void initialise()throws Exception{
        testSocket = Mockito.mock(Socket.class);
        PowerMockito.whenNew(Socket.class).withAnyArguments().thenReturn(testSocket);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out;
        out = new ObjectOutputStream(bos);

        //is = PowerMockito.mock(ObjectInputStream.class);

        /*byte[] bytearr = new byte[5000];
        ByteArrayInputStream bis = new ByteArrayInputStream(bytearr);
        is = new ObjectInputStream(bis);*/

        PowerMockito.whenNew(ObjectOutputStream.class).withAnyArguments().thenReturn(out);
        PowerMockito.whenNew(ObjectInputStream.class).withAnyArguments().thenReturn(is);

        cg = PowerMockito.mock(ClientControllerInterface.class);

        client = new Client("localhost", 3008);
        client.setController(cg);
    }

    @Test
    public void testStart(){
        assertEquals(true,client.start());
    }

    @Test
    public void testMsgSend() throws Exception{
        Message msg = new Message(Message.MESSAGE);

        client.start();
       // assertEquals(true,client.sendMessage(msg));
    }

    @Test
    public void testPrivMsgSend() throws Exception{
        PrivateMessage pm = new PrivateMessage("from", "msg", "to");
        client.start();

        //assertEquals(true, client.sendPrivateMessage(pm));
    }
}