package multiServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread{

	private final int serverPort;
	private ArrayList<ServerWorker> workerList = new ArrayList<>(6);
	
	public Server(int serverPort) 
	{
		this.serverPort= serverPort;	
	}
	
	public List<ServerWorker> getWorkerList()
	{
		return workerList;
	}
	
	public void run(){
		try {
			ServerSocket serverSocket = new ServerSocket(serverPort);
			while(true) 
			{		//create connection between server and client
				System.out.println("About to accept client connection ....");
				Socket clientSocket = serverSocket.accept();
				System.out.println("Accepted connection from: " +clientSocket.getPort());
				ServerWorker worker = new ServerWorker(this,clientSocket);
				workerList.add(worker);
				worker.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void removeWorker(ServerWorker serverWorker) 
	{
		workerList.remove(serverWorker);
	}
	public void rearrangeWorker(ServerWorker serverWorker) 
	{
		workerList.remove(serverWorker);
		workerList.add(serverWorker);
	}
}
