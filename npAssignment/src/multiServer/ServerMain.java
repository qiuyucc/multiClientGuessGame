package multiServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class ServerMain {
	public static void main(String [] args) 
	{
		multiGame game =new multiGame();
		//game.creatLobby();
		//System.out.println(game.getUserList().size());
		
		int port =8833;
		Server server  = new Server(port);
		server.start();
		
	}
	
	
}
