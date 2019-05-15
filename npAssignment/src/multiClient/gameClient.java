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

		gameClient client = new gameClient("10.132.101.103", 8833);
		//gameClient client = new gameClient("192.168.1.137", 8833);
		Scanner kb = new Scanner(System.in);
		String line, input;
		if (!client.connect()) {
			System.out.println("Connect failed.");
		} else {
			System.out.println("Connect successful");
			System.out.println("Enter username:");
			String username = kb.nextLine();
			client.login(username);

			while (true) {
		
				while ((line = client.getBufferedIn().readLine()) != null) {
					String[] tokens = StringUtils.split(line);
					//String response = client.getBufferedIn().readLine();
					System.out.println("Server Response:"+line);
					if (tokens != null && tokens.length > 0) {
						String cmd = tokens[0];
						if ("start".equals(cmd)) {
							while (client.guessNumber() == false) {
							}
							break;
						}
					
					}
				}
				System.out.println("User Command:");
				if ((input = kb.nextLine()).equalsIgnoreCase("q")) {
					client.logoff();
					break;
				}
				if ((input = kb.nextLine()).equalsIgnoreCase("p")) {
					client.playAgain();
					break;
				}
			}

		}
	}

	private void login(String username) throws IOException {
		String cmd = "login " + username + "\n";
		serverOut.write(cmd.getBytes());
		String response = bufferedIn.readLine();
		System.out.println("Server Response: " + response);
		/*
		 * if ("wait in lobby....".equalsIgnoreCase(response)) { return true; } else {
		 * return false; }
		 */
	}

	private void logoff() throws IOException {
		String cmd = "q\n";
		serverOut.write(cmd.getBytes());
		String response = bufferedIn.readLine();
		System.out.println("Server Response: " + response);

	}

	private boolean guessNumber() throws IOException {
		System.out.println("Guess the number:");
		Scanner kb = new Scanner(System.in);
		int number = kb.nextInt();
		String cmd = "guess " + number + "\n";
		serverOut.write(cmd.getBytes());
		String response = bufferedIn.readLine();
		System.out.println("Server Response: " + response);
		if ("Congratulation!".equalsIgnoreCase(response)
				|| "You have reached maximum guess of 4".equalsIgnoreCase(response)) {
			return true;
		}
		return false;
	}

	private void playAgain() throws IOException {
		String cmd = "p\n";
		serverOut.write(cmd.getBytes());
		String response = bufferedIn.readLine();
		System.out.println("Server Response: " + response);
	}

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
