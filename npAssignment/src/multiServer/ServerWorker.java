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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class ServerWorker extends Thread {

	private final Socket clientSocket;
	private final Server server;
	private String username = null;
	private int count = 0;
	private OutputStream os;
	private boolean play = false;

	public ServerWorker(Server server, Socket clientSocket) {
		// TODO Auto-generated constructor stub
		this.server = server;
		this.clientSocket = clientSocket;
	}

	public void run() {
		try {
			System.out.println(server.getWorkerList().size());
			handleClientSocket();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void handleClientSocket() throws IOException, InterruptedException {
		// System.out.println("here");
		multiGame game = new multiGame();
		InputStream is = clientSocket.getInputStream();
		this.os = clientSocket.getOutputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String line;
		List<ServerWorker> workerList = server.getWorkerList();
		while(workerList.size()>0) 
		{
			while ((line = reader.readLine()) != null) {
				String[] tokens = StringUtils.split(line);
				System.out.println("here");
				if (tokens != null && tokens.length > 0) {
					String cmd = tokens[0];
					if ("q".equalsIgnoreCase(cmd)) {
						handleLogoff(os, clientSocket);
						break;
					} else if ("guess".equalsIgnoreCase(cmd)) {
						validate(tokens);
					} else if ("p".equalsIgnoreCase(cmd)) {
						playAgain(os, clientSocket);
						this.play =true;
					} else if ("login".equalsIgnoreCase(cmd)) {
						this.play = handleLogin(os, tokens, clientSocket);
					} else {
						String msg = "unknown command " + cmd + "\n";
						os.write(msg.getBytes());
					}
					while(play) 
					{
						this.sleep(10000);
						sendMessage();
					}

				}
		}
		
		}

		

		/*
		 * while (play) { this.sleep(1000); if (workerList.size() > 0) { sendMessage();
		 * }
		 * 
		 * }
		 */

		clientSocket.close();

	}

	private void validate(String[] tokens) {
		if (tokens.length == 2) {
			String number = tokens[0];
		}
		this.count++;
		this.play = false;
	}

	private void startGame() throws IOException {

		String cmd = "start " + "game" + "\n";
		os.write(cmd.getBytes());
	}

	private void sendMessage() throws IOException, InterruptedException {
		List<ServerWorker> workerList = server.getWorkerList();
		if (workerList.size() >= 3) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < 3; i++) {
				sb.append((i+1)+"."+workerList.get(i).getUsername()+" ");
			}
			String msg = "Players List:"+ sb +"\n";
			for (int i = 0; i < 3; i++) {
				if (this.getUsername().equalsIgnoreCase(workerList.get(i).getUsername())) {
					this.send(msg);
					this.startGame();
				} 
			}
		} else {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < workerList.size(); i++) {
				sb.append((i+1)+"."+workerList.get(i).getUsername()+" ");
			}
			String msg = "Players List:"+ sb +"\n";
			for (int i = 0; i < workerList.size(); i++) {
				if (this.getUsername().equalsIgnoreCase(workerList.get(i).getUsername())) {
					this.send(msg);
					this.startGame();
				} 
			}
		}
	}

	private void send(String msg) throws IOException {
		if (username != null) {
			os.write(msg.getBytes());
		}

	}

	public String getUsername() {
		return username;
	}

	private boolean handleLogin(OutputStream outputStream, String[] tokens, Socket client) throws IOException {
		List<ServerWorker> workerList = server.getWorkerList();
		if (tokens.length == 2) {
			if (workerList.size() < 6) {
				String username = tokens[1];
				this.username = username;
				String msg = "wait in lobby....\n";
				outputStream.write(msg.getBytes());
				System.out.println("user" + client.getPort() + " add into lobby!");
				return true;
			}
			String msg = "lobby already full!";
			outputStream.write(msg.getBytes());
			return false;
		}
		return false;

	}

	private void handleLogoff(OutputStream outputStream, Socket clientSocket) throws IOException {
		play = false;
		server.removeWorker(this);
		List<ServerWorker> workerList = server.getWorkerList();
		System.out.print("user" + clientSocket.getPort() + " leave the game!");
		String msg = "Exit the game! Thank you";
		outputStream.write(msg.getBytes());
		clientSocket.close();

	}

	private void playAgain(OutputStream os, Socket clientSocket2) throws IOException {
		play = true;
		server.rearrangeWorker(this);
		String msg = "wait in lobby....\n";
		System.out.print("user" + clientSocket.getPort() + " wants play again!");
		os.write(msg.getBytes());

	}

}
