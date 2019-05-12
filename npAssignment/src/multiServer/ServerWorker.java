package multiServer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;



public class ServerWorker extends Thread {

	private final Socket clientSocket;
	public ServerWorker(Socket clientSocket) {
		// TODO Auto-generated constructor stub
		this.clientSocket= clientSocket;
	}
	
	public void run() 
	{
		try {
			handleClientSocket();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void handleClientSocket() throws IOException,InterruptedException
	{
		//DataInputStream in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
		//DataOutputStream out = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
		InputStream is = clientSocket.getInputStream();
		OutputStream os =clientSocket.getOutputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String line;
		while((line = reader.readLine())!=null) 
		{
			String[] tokens = StringUtils.split(line);
			if(tokens !=null&&tokens.length>0) 
			{
				String cmd =tokens[0];
			}
			
			
			if("e".equalsIgnoreCase(line)) 
				break;
		}
		
		clientSocket.close();

	}
	
	private void handleLogin(OutputStream outputStream, String username, Socket clientSocket) throws IOException
	{
		multiGame game = new multiGame();
		if(game.searchUser(username) ==false) 
		{
			user u  = new user(username,clientSocket.getPort());
			if(game.add(u)==true) 
			{
				outputStream.write(("user"+u.getPort()+" add into lobby!").getBytes());
				System.out.println("user"+u.getPort()+" add into lobby!");
			}
			else 
				outputStream.write(("lobby already full!").getBytes());			
		}else 
		{
			outputStream.write(("error registeration - same username").getBytes());
		}
	}
}
