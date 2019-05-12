package multiVersion;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class multiClient {
	
	public static void main(String [] args) 
	{
		Socket s = null
		try 
		{
			String serverAddress = "";
			s = new Socket(serverAddress,61918);
		}catch(IOException e ) 
		{
			e.printStackTrace();
		}
		
	}

}
