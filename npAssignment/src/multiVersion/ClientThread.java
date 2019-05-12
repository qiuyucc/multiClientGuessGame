package multiVersion;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientThread extends Thread{
	
	private Socket socket = null;
	private DataInputStream in = null;
	private DataOutputStream out = null;
	
	public ClientThread(Socket socket) 
	{
		this.socket = socket;
	}
	
	public void run() 
	{	
		Scanner sc = new Scanner(System.in);
		try {
			in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean correct = false;
		//int attempt = 0;
		String input = null;
		String username = null;
		int count = 0;
		boolean registered = false;
		while(socket.isConnected()) 
		{
			try 
			{
				do 
				{
					System.out.println("Enter username:");
					username = sc.nextLine();
					out.writeUTF(username);
					out.flush();
					System.out.println("Registering with Server....");
					registered = true;
				}while(registered ==false);
				
				
				while(correct ==false) 
				{
					System.out.println("please have a guess:");
					input = sc.nextLine();
					count++;
					out.writeUTF(input);
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
				

			}catch(IOException e) 
			{
				e.printStackTrace();
			}
						
		}
		try {
			socket.close();
			out.close();
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
}
