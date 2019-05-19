package multiServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class ServerMain {
	public static void main(String [] args) 
	{
		//run the concurrent server
		int port =61918;
		Server server  = new Server(port);
		multiGame game = new multiGame();
		server.addGame(game);
		server.start();
		
	}
	
	
}
