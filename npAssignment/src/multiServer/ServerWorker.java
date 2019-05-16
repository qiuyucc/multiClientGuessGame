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
			System.out.println(server.getGameList().get(0).getGoal());
			handleClientSocket();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void handleClientSocket() throws IOException, InterruptedException {
	
		InputStream is = clientSocket.getInputStream();
		this.os = clientSocket.getOutputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String line;
		List<ServerWorker> workerList = server.getWorkerList();
		line = reader.readLine();
		String[] tokens = line.split(" ");
		String cmd = tokens[0];

		if ("login".equalsIgnoreCase(cmd)) {
			this.play = handleLogin(tokens);
		}
		while (play) {

			this.sleep(20000);
			sendMessage();
			while ((line = reader.readLine()) != null) {
				String[] token = line.split(" ");
				if (token != null && token.length > 0) {
					cmd = token[0];
					if ("q".equalsIgnoreCase(cmd)) {
						handleLogoff();
						break;
					} else if ("guess".equalsIgnoreCase(cmd)) {
						validate(token);
					} else if ("p".equalsIgnoreCase(cmd)) {
						playAgain();
						break;
					} else if ("e".equalsIgnoreCase(cmd)) {
						// System.out.println("here");
						exitRound();
					} else {
						String msg = "unknown command " + cmd + "\n";
						os.write(msg.getBytes());
					}
				}
			}

		}
		clientSocket.close();

	}

	private void validate(String[] tokens) throws IOException {
		List<multiGame> goalList = server.getGameList();
		// List<ServerWorker> playerList = server.getPlayerList();
		if (tokens.length == 2) {
			this.count++;
			String number = tokens[1];
			int num = Integer.parseInt(number);
			System.out.println("user" + clientSocket.getPort() + " guess:" + number + " current attempt:" + this.count);
			if (this.count <= 4) {
				if (num == goalList.get(0).getGoal()) {
					String msg = "Congratulation!" + "\n";
					server.removePlayer(this);
					os.write(msg.getBytes());
					if (server.getPlayerList().isEmpty()) {
						announceResult();
						server.newGame();
						System.out.println(server.getGameList().get(0).getGoal());
					}
				} else if (num > goalList.get(0).getGoal()) {
					String msg = "result " + num + " is bigger than the generated number" + "\n";
					os.write(msg.getBytes());
				} else if (num < goalList.get(0).getGoal()) {
					String msg = "result " + num + " is smaller than the generated number" + "\n";
					os.write(msg.getBytes());
				}

			} else if (this.count == 5) {
				String msg = "fail " + "You have reached maximum guess of 4!" + "\n";
				this.count = 4;
				server.removePlayer(this);
				System.out.println(msg);
				os.write(msg.getBytes());
				if (server.getPlayerList().isEmpty()) {
					announceResult();
					server.newGame();
					System.out.println(server.getGameList().get(0).getGoal());
				}
			}
		}
	}

	private void announceResult() throws IOException {
		List<ServerWorker> workerList = server.getWorkerList();
		if (workerList.size() >= 3) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < 3; i++) {
				sb.append((i + 1) + "." + workerList.get(i).getUsername() + " " + workerList.get(i).getCount()
						+ "times. ");
			}
			String msg = "announcement " + sb + "\n";
			for (int i = 0; i < workerList.size(); i++) {
				workerList.get(i).send(msg);

			}
		} else {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < workerList.size(); i++) {
				sb.append((i + 1) + "." + workerList.get(i).getUsername() + " " + workerList.get(i).getCount()
						+ "times. ");
			}
			String msg = "announcement " + sb + "\n";
			for (int i = 0; i < workerList.size(); i++) {
				workerList.get(i).send(msg);
			}
		}

	}

	private void startGame() throws IOException {

		String cmd = "start " + "game" + "\n";
		os.write(cmd.getBytes());
	}

	private void sendMessage() throws IOException, InterruptedException {
		List<ServerWorker> workerList = server.getWorkerList();
		boolean found = false;
		if (workerList.size() >= 3) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < 3; i++) {
				sb.append((i + 1) + "." + workerList.get(i).getUsername() + " ");
			}
			String msg = "Players List:" + sb + "\n";
			for (int i = 0; i < 3; i++) {
				if (this.getUsername().equalsIgnoreCase(workerList.get(i).getUsername())) {
					found = true;
				}
			}
			if (found == true) {
				this.send(msg);
				this.startGame();
				server.addPlayer(this);
			} else {
				String msg2 = "Wait for the next round...\n";
				this.send(msg2);
			}

		} else {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < workerList.size(); i++) {
				sb.append((i + 1) + "." + workerList.get(i).getUsername() + " ");
			}
			String msg = "Players List:" + sb + "\n";
			for (int i = 0; i < workerList.size(); i++) {
				if (this.getUsername().equalsIgnoreCase(workerList.get(i).getUsername())) {
					found =true;
				}
			}
			if (found == true) {
				this.send(msg);
				this.startGame();
				server.addPlayer(this);
			} else {
				String msg2 = "Wait for the next round...\n";
				this.send(msg2);
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

	public int getCount() {
		return count;
	}

	private boolean handleLogin(String[] tokens) throws IOException {
		List<ServerWorker> workerList = server.getWorkerList();

		if (tokens.length == 2) {
			if (workerList.size() < 6) {
				String username = tokens[1];
				this.username = username;
				String msg = "wait in lobby....\n";
				os.write(msg.getBytes());
				System.out.println("user" + clientSocket.getPort() + " add into lobby!");
				return true;
			}
			String msg = "lobby already full!";
			os.write(msg.getBytes());
			return false;
		}
		return false;

	}

	private void handleLogoff() throws IOException {
		play = false;
		this.count = 0;
		server.removeWorker(this);
		List<ServerWorker> workerList = server.getWorkerList();
		System.out.println("user" + clientSocket.getPort() + " leave the game!");
		String msg = "Exit the game! Thank you" + "\n";
		os.write(msg.getBytes());
		clientSocket.close();

	}

	private void playAgain() throws IOException {
		this.count = 0;
		String msg = "wait in lobby....\n";
		System.out.println("user" + clientSocket.getPort() + " wants play again!" + "");
		os.write(msg.getBytes());
		server.rearrangeWorker(this);

	}

	private void exitRound() throws IOException {
		this.count = 4;
		System.out.println("user" + clientSocket.getPort() + " exit the round");
		String msg = "fail " + "exit this round!" + "\n";
		os.write(msg.getBytes());
		server.removePlayer(this);
		if (server.getPlayerList().isEmpty()) {
			announceResult();
			server.newGame();
			System.out.println(server.getGameList().get(0).getGoal());
		}
		// server.rearrangeWorker(this);
	}
}
