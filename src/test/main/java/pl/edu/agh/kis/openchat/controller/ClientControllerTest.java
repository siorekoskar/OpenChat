package main.java.pl.edu.agh.kis.openchat.controller;

import main.java.pl.edu.agh.kis.openchat.gui.GuiInterface;
import main.java.pl.edu.agh.kis.openchat.model.ClientInterface;
import main.java.pl.edu.agh.kis.openchat.model.Message;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;


import static org.junit.Assert.*;


/**
 * Created by Oskar on 23/01/2017.
 */
public class ClientControllerTest {
    private ClientInterface client;
    private GuiInterface gui;
    private ClientController clientController;
    private Message msg;

    @Before
    public void beforeEachTest(){
        //client = Mockito.mock(ClientInterface.class);
        PowerMockito.mock(ClientInterface.class);
       // gui = Mockito.mock(GuiInterface.class);
       // clientController = new ClientController();
        //msg = Mockito.mock(Message.class);
    }

    @Test
    public void test1(){
        assertEquals(1,1);
    }



}