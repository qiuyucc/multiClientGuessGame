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
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class singleServer {

	public static void main (String [] args) throws IOException
	{
		int attempt;

	
	    ServerSocket listener =new ServerSocket(61918);
	    
		boolean connection =true;
		try {
			while(connection) 
			{   //set up connection with clients 
				Socket socket = listener.accept();
				singleGame game = new singleGame();
				boolean correct = game.getCorrect();
				System.out.println("The number has been generated");
				//in, out as input and output stream
				DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
				DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
				try 
				{   		
					while(correct ==false) 
					{
						attempt = in.readInt();
						System.out.println("User guess: "+ attempt);
						//sent to client
						String sentMsg = game.validate(attempt);
						correct = game.getCorrect();
						out.writeUTF(sentMsg);
						out.writeBoolean(correct);
						out.flush();	
					}
						
				}finally 
				{
					socket.close();
					out.close();
					in.close();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally 
		{
			listener.close();
			
		}
		
	}
}
