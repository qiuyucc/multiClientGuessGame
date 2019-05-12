package singleVersion;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class singleClient {

	public static void main(String[]args) throws IOException
	{
		//console menu to play around
		int selection =0;
		Scanner kb = new Scanner(System.in);
		do 
		{
			System.out.println("Welcome to Single-player Game!");
			System.out.println("------------------------------");
			System.out.println("1 - Start game!");
			System.out.println("2 - Quit");
			System.out.println("Insert Selection:");
			
			selection =kb.nextInt();
			switch(selection)
			{
			case 1:
				playGame();
				break;
			case 2:
				System.exit(0);
			}
		}while(selection!=2);
		
	}

	private static void playGame() throws UnknownHostException, IOException {
		
		boolean correct = false;
		boolean connection = true;
		int attempt = 0;
	
		String serverAddress = "nprgprdap01.int.its.rmit.edu.au";
		//String serverAddress = "10.132.108.32";
		Socket s = new Socket(serverAddress,61918);
		DataInputStream in = new DataInputStream(new BufferedInputStream(s.getInputStream()));
		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(s.getOutputStream()));
		//check the connection
		if(!s.isConnected()) 
			System.out.println("Connection not established");
		else
			System.out.println("Game started");
		
		Scanner sc = new Scanner(System.in);
		int count = 0;
		//make ture the socket is open
		while(connection)
		{    
			        while(correct ==false) 
			         {
			        	//send number to server
			        	 System.out.println("please have a guess:");
			        	 attempt = sc.nextInt();
			        	 count++;
			        	 out.writeInt(attempt);
			        	 out.flush();
			        	 System.out.println("Validate number with server...");
			        	 //receive msg from server
			        	 String message = in.readUTF();
				         correct = in.readBoolean();
				         //System.out.println(correct);
				         System.out.println("server : " +message);
				         if(count==4 && correct ==false) 
				         {
				        	  System.out.println("You have reach a maximum of 4 guesses");
				        	  break;
				         }
				        	 
			         }
			        connection= false;	          	
		}
		s.close();
		out.close();
		in.close();		
	}	
}
