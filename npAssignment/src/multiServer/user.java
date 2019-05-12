package multiServer;

public class user {
	private String username;
	//private int attempt;
	private int count;
	private boolean registered =false;
	private int port;
	
	public user(String username, int port) 
	{
		this.username =username;
		this.port = port;
		//this.attempt = -1;
		this.count = 0;
		this.registered =true;
	}
	
	public void setUsername(String username)
	{
		this.username = username;
	}
	
	public String getUsername() 
	{
		return username;
	}
	
	
	public boolean getRegistered()
	{
		return registered;
	}
	/*public int getAttempt() 
	{
		return attempt;
	}*/
	
	public int getCount() 
	{
		return count;
	}
	
	public void setCount(int count) 
	{
		this.count = count;
	}

	public int getPort() {
		// TODO Auto-generated method stub
		return port;
	}
	
	/*public void setAttempt(int num) 
	{
		this.attempt = num;
	}*/
	
}
