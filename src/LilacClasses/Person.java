package LilacClasses;

import ocsf.LilacClient;

public abstract class Person
{
	protected int id;
	protected String firstName; 
	protected String sureName;
	protected String userName;
	protected String password;
	protected String phone;
	protected String address;
	protected String email;
	protected boolean connected;
	protected LilacClient client;
	
	public Person(int idNumber, String fN, String sN, String uN,
			String pw, String p, String ad, String e, boolean c,
	        String host, int port)
    {
      id = idNumber;
      firstName = fN;
      sureName = sN;
      userName = uN;
      password = pw;
      phone = p;
      address = ad;
      email = e;
      connected = c;
      client = new LilacClient(uN,host,port);
    }
	
	// getters and setters for private fields
	  
    public int getId() { return id; }
	public void setId(int idNumber) { id = idNumber; }
	
	public String getFristName() { return firstName; }
	public void setFirstName(String fN) { firstName = fN; }
	
	public String getSureName() { return sureName; }
	public void setSureName(String sN) { sureName = sN; }
	
	public String getUserName() { return userName; }
	public void setUserName(String uN) { userName = uN; }
	
	public String getPassword() { return password; }
	public void setPassword(String pw) { password = pw; }
	
	public String getPhone() { return phone; }
	public void setPhone(String p) { phone = p; }
	
	public String getAddress() { return address; }
	public void setAddress(String ad) { address = ad; }
	
	public String getEmail() { return email; }
	public void setEmail(String e) { email = e; }
	
	public boolean geConnected() { return connected; }
	public void setCoonected(boolean c) { connected = c; }
	
	// register account 
	void registerAccount(String accountType)
	{
		client.accept("register " + accountType + " " + Integer.toString(id) + " " + firstName
				      + " " + sureName + " " + userName + " " + password + " " + phone + " " +
				      address + " " + email);
		connected = true;
	}
	
	// login account 
    void loginAccount(String accountType)
	{
		client.accept("login " + accountType + " " + Integer.toString(id));
		connected = true;
	}
    
    // logout account 
    void logoutAccount(String accountType)
	{
    	client.accept("logout " + accountType + " " + Integer.toString(id));
    	connected = false;
	}
    
    // delete account 
    void deleteAccount(String accountType)
	{
    	client.accept("delete " + accountType + " " + Integer.toString(id));
	}
}
