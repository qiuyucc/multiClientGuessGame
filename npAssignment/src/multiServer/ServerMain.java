package multiServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class ServerMain {
	public static void main(String [] args) 
	{
		//multiGame game =new multiGame();
		//game.creatLobby();
		//System.out.println(game.getUserList().size());
		
		int port =61918;
		Server server  = new Server(port);
		multiGame game = new multiGame();
		server.addGame(game);
		server.start();
		
	}
	
	
}
