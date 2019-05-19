package multiServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread{
	
	/*
	 * workerList: store the current client server
	 * gameList: store the random number used for each round
	 * playerList: store the users currently playing in the round
	 * */
	private final int serverPort;
	private ArrayList<ServerWorker> workerList = new ArrayList<>(6);
	private ArrayList<multiGame> gameList = new ArrayList<>(1);
	private ArrayList<ServerWorker> playerList = new ArrayList<>(3);
	
	public Server(int serverPort) 
	{
		this.serverPort= serverPort;	
	}
	
	public List<ServerWorker> getWorkerList()
	{
		return workerList;
	}
	
	public List<ServerWorker> getPlayerList()
	{
		return playerList;
	}
	
	public List<multiGame> getGameList()
	{
		return gameList;
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
				//for each client, will be store into list
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
	public void removePlayer(ServerWorker serverWorker) 
	{
		playerList.remove(serverWorker);
	}
	public void addPlayer(ServerWorker serverWorker) 
	{
		playerList.add(serverWorker);
	}
	
	public void addGame(multiGame g) 
	{
		gameList.add(g);
	}
	
	public void newGame() 
	{
		multiGame g = gameList.get(0);
		gameList.remove(g);
		multiGame newGame = new multiGame();
		gameList.add(newGame);
	};
	
	public void rearrangeWorker(ServerWorker serverWorker) 
	{
		workerList.remove(serverWorker);
		workerList.add(serverWorker);
	}
}
