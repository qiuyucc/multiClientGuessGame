package multiServer;

import java.util.ArrayList;
import java.util.List;

public class multiGame {
	private boolean correct;
	private int numberOfPlayer;
	static int goal;

	public multiGame() {
		goal = (int) (Math.random() * 0);
		correct = false;
	}

	public int getGoal() {
		return goal;
	}

}
