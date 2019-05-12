package singleVersion;

public class singleGame {
	private boolean correct;
	private int goal;
	
	public singleGame() 
	{
		goal = (int)(Math.random()*10);
		correct = false;
	}
	
	
	public int getGoal() 
	{
		return goal;
	}
	
	public void setCorrect() 
	{
		this.correct = true;
	}
	
	public boolean getCorrect() 
	{
		return correct;
	}
	
	public String validate(int guess) 
	{
		String msg = null;
		if (guess ==goal) 
		{
			msg = "Congratulation!";
			correct = true;
		}	
		else if(guess >goal)
			msg = "The client's guess number " + guess + " is bigger than the generated number";
		else 
			msg ="The client's guess number " + guess + " is smaller than the generated number";
		return msg;
	}
}
