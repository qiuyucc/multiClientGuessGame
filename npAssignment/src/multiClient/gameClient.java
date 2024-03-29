package multiClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

public class gameClient {

	private final String serverName;
	private final int serverPort;
	private OutputStream serverOut;
	private InputStream serverIn;
	private BufferedReader bufferedIn;
	
	public gameClient(String serverName, int serverPort) {
		this.serverName = serverName;
		this.serverPort = serverPort;
	}

	public OutputStream getServerOut() {
		return serverOut;
	}

	public InputStream serverOut() {
		return serverIn;
	}

	public BufferedReader getBufferedIn() {
		return bufferedIn;
	}

	public static void main(String[] args) throws IOException {

		
		gameClient client = new gameClient("nprgprdap01.int.its.rmit.edu.au", 61918);
		Scanner kb = new Scanner(System.in);
		String line, input;
		boolean correct = false;
		if (!client.connect()) {
			System.out.println("Connect failed.");
		} else {
			System.out.println("Connect successful");
			System.out.println("Enter username:");
			//user login section before enter into for loop
			String username = kb.nextLine();
			client.login(username);
			while(true) 
			{
				//read the input from server, and call the function based on command
				while((line = client.getBufferedIn().readLine()) != null) {
					String[] tokens = line.split(" ");
					//String response = client.getBufferedIn().readLine();
					System.out.println("Server Response:"+line);
					if (tokens != null && tokens.length > 0) {
						String cmd = tokens[0];
						//if server send start game, client can guess the number
						if ("start".equalsIgnoreCase(cmd)) {	
							System.out.println("Guess the number:");
							String num = kb.nextLine();
							client.guessNumber(num);
						}else if("result".equalsIgnoreCase(cmd)) 
						{   
							System.out.println("Guess the number:");
							String num = kb.nextLine();
							client.guessNumber(num);
						}else if("Congratulation!".equals(cmd)||"fail".equalsIgnoreCase(cmd)) 
						{
							System.out.println("wating for result....");
						}else if("announcement".equalsIgnoreCase(cmd)) 
						{
							System.out.println("User command:");
							input = kb.nextLine();
							if (input.equalsIgnoreCase("q")) {
								client.logoff();
								break;
							}
							else if (input.equalsIgnoreCase("p")) {
								client.playAgain();
								break;
						}
						}
					}
				}
				
			}
			}
				

		}
	
	// send the login info to server by user input
	private void login(String username) throws IOException {
		String cmd = "login " + username + "\n";
		serverOut.write(cmd.getBytes());

	}
	//send the logoff request to server
	private void logoff() throws IOException {
		String cmd = "q\n";
		serverOut.write(cmd.getBytes());
	}
	// user guess the number
	private void guessNumber(String input) throws IOException {
		if(input.equalsIgnoreCase("e")) 
		{
			String cmd ="e\n";
			serverOut.write(cmd.getBytes());
		}else 
		{
			String cmd = "guess " + input + "\n";
			serverOut.write(cmd.getBytes());
		
			
		}
		//String response = bufferedIn.readLine();
		//System.out.println("Server Response: " + response);
	}
	//user want to play again
	private void playAgain() throws IOException {
		String cmd = "p"+"\n";
		serverOut.write(cmd.getBytes());
	}
	//set up connection
	private boolean connect() {
		try {
			Socket socket = new Socket(serverName, serverPort);
			System.out.println("Client port is " + socket.getLocalPort());
			this.serverOut = socket.getOutputStream();
			this.serverIn = socket.getInputStream();
			this.bufferedIn = new BufferedReader(new InputStreamReader(serverIn));
			return true;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
