package multiServer;

import java.util.ArrayList;
import java.util.List;

public class multiGame {
	private boolean correct;
	private int numberOfPlayer;
	private int goal;
	//one game has multi-players
	private ArrayList userList;
	
	
	public multiGame() 
	{
		
	}
	public void startGame() 
	{
		goal = (int)(Math.random()*0);
		correct =false;
	}
	
	public void creatLobby() 
	{
		userList = new ArrayList<user>(6);
	}

	//add user
	public boolean add(user u) 
	{
		if(userList.size()<=6) 
		{
			userList.add(u);
			return true;
		}
		else 
		{
			System.out.println("user lobbyFul");
			return false;
		}
			
	}
	
	//if user play again
	public synchronized void adjustOrder(user u) 
	{   
		//every time server takes first three users in lobby
		for(int i=0; i<3;i++) 
		{
			user userTemp =(user)userList.get(i);
			if(userTemp.equals(u)) 
			{   //remove the user & add it at back
				userList.remove(i);
				userList.add(u);
			}
			
		}
	}
	
	public boolean searchUser(String username)
	{
		for(int i =0; i<6;i++) 
		{
			user u = (user)userList.get(i);
			if(u.getUsername() ==username) 
			{
				return true;
			}
		}
		return false;
	}
	

	
	public List getUserList() 
	{
		return userList;
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
	
	public synchronized String validate(int guess)
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
