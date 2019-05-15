package multiServer;

import java.util.ArrayList;
import java.util.List;

public class multiGame {
	private boolean correct;
	private int numberOfPlayer;
	static  int goal;
	//one game has multi-players
	private ArrayList<user> userList = new ArrayList<>(6);
	
	
	public multiGame() 
	{
		
	}
	public List<user> getUserList()
	{
		return userList;
	}
	public void startGame() 
	{
		goal = (int)(Math.random()*0);
		correct =false;
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
			System.out.println("user lobby already full");
			return false;
		}
			
	}
	
	public void printUserList() 
	{
		for(int i=0;i<userList.size();i++) 
		{
			System.out.println(userList.get(i).getUsername()+" "+userList.get(i).getPort());
		}
	}
	
	//if user play again
	public  void adjustOrder(user u) 
	{   
		//every time server takes first three users in lobby
		for(int i=0; i<userList.size();i++) 
		{
			user userTemp =(user)userList.get(i);
			if(userTemp.equals(u)) 
			{   //remove the user & add it at back
				userList.remove(i);			
			}	
		}
		userList.add(u);
	}
	
	public void removeUser(user u) 
	{
		for(int i=0; i<userList.size();i++) 
		{
			user userTemp =(user)userList.get(i);
			if(userTemp.equals(u)) 
			{   //remove the user & add it at back
				userList.remove(i);			
			}	
		}
	}
	

	
	public boolean searchUser(int port)
	{
		if(userList.size()!=0) 
		{
			for(int i =0; i<userList.size();i++) 
			{
				if(!(userList.get(i)== null)) 
				{
					user u = (user)userList.get(i);
					if(u.getPort()==port) 
					{
						return true;
					}
				}		
			}
		}
		
		return false;
	}
	
	public user identfyUser(int port)
	{
		if(userList.size()!=0) 
		{
			for(int i =0; i<userList.size();i++) 
			{
				if(!(userList.get(i)== null)) 
				{
					user u = (user)userList.get(i);
					if(u.getPort()==port) 
					{
						return u;
					}
				}		
			}
		}
		
		return null;
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
