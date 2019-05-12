package multiVersion;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerThread extends Thread{

	private Socket socket = null;
	private DataInputStream in = null;
	private DataOutputStream out = null;
	private boolean correct= false;
    
	
	public ServerThread(Socket socket) 
	{
		this.socket= socket;
	}
	
	public void run() 
	{
		String input;
		user u = null;
		int count =0;
		boolean connection = true;
		multiGame game =new multiGame();
		correct = game.getCorrect();
		System.out.println("The number has been generated");
		try 
		{
		   while(connection) 
		   {
			   in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			   out= new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			  do 
			  {
				   input = in.readUTF();
				   u = new user(input);
				   game.add(u);
				   String sentMsg = "User has been registered!";
				   out.writeUTF(sentMsg);
				   out.flush();
			  }while(u.getRegistered()==false);
				  		   
			   while(correct ==false) 
			   {
				   input = in.readUTF();
				   System.out.println("User guess:" + input);
				   count ++;
				   //sent to client
				   String sentMsg = game.validate(Integer.parseInt(input));
				   correct = game.getCorrect();
				   out.writeUTF(sentMsg);
				   out.writeBoolean(correct);
				   out.flush(); 				   
				   //while count reach to 4, the game over, server record thee user's count
				   if (count==4 && correct ==false) 
					   break;	   
			   }
			   u.setCount(count);
			   
		   }
		}catch(IOException e) 
		{
			e.printStackTrace();
		}finally 
		{
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
	
	
	
}
