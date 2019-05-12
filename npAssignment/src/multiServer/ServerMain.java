package multiServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class ServerMain {
	public static void main(String [] args) 
	{
		int port =8818;
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			while(true) 
			{		//create connection between server and client
				System.out.println("About to accept client connection ....");
				Socket clientSocket = serverSocket.accept();
				System.out.println("Accepted connection from: " +clientSocket.getPort());
				ServerWorker worker = new ServerWorker(clientSocket);
				worker.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
}
