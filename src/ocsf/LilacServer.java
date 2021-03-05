package ocsf;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class LilacServer extends AbstractServer 
{	
  //Class variables *************************************************
	
  /**
   * The default port to listen on and the connection info.
   */
	
  final public static int DEFAULT_PORT =5555;
  static  public enum Type {FlowerOrdering ,GrowingFlowerPot ,BridalBouquet ,FlowersSet}
  static private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
  static private final String DB = "sLjOAIvCFs";
  static private final String DB_URL = "jdbc:mysql://remotemysql.com/"+ DB + "?useSSL=false";
  static private final String USER = "sLjOAIvCFs";
  static private final String PASS = "i1g7kzhtF9";
  
  /**
   * The interface type variable. It allows the implementation of 
   * the display method in the client.
   */
  ChatIF serverUI;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public LilacServer(int port) 
  {
    super(port);
  }

   /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   * @param serverUI The interface type variable.
   */
  public LilacServer(int port, ChatIF serverUI) throws IOException
  {
    super(port);
    this.serverUI = serverUI;
  }
  
  private double hoursDiff(String date1,String time1,String date2,String time2)
  {
	  double year1,month1,days1,year2,month2,days2;
	  double hours1,minutes1,seconds1,hours2,minutes2,seconds2;
	  
	  String[] d1 = date1.trim().split("/"); 
	  String[] t1 = time1.trim().split(":");
	  String[] d2 = date2.trim().split("/");
	  String[] t2 = time2.trim().split(":");
	  
	  year1 = Double.parseDouble(d1[0]);
	  month1 = Double.parseDouble(d1[1]);
	  days1 = Double.parseDouble(d1[2]);
	  
	  year2 = Double.parseDouble(d2[0]);
	  month2 = Double.parseDouble(d2[1]);
	  days2 = Double.parseDouble(d2[2]);
	  
	  hours1 = Double.parseDouble(t1[0]);
	  minutes1 = Double.parseDouble(t1[1]);
	  seconds1 = Double.parseDouble(t1[2]);
	  
	  hours2 = Double.parseDouble(t2[0]);
	  minutes2 = Double.parseDouble(t2[1]);
	  seconds2 = Double.parseDouble(t2[2]);
	  
	  double h1 = year1*365*24 + month1*30*24 + days1*24 + hours1 + minutes1/60 + seconds1/3600;
	  double h2 = year2*365*24 + month2*30*24 + days2*24 + hours2 + minutes2/60 + seconds2/3600;
	  
	  return h1-h2;
  }
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
    if (msg.toString().startsWith("#login "))
    {
      if (client.getInfo("loginID") != null)
      {
        try
        {
          client.sendToClient("You are already logged in.");
        }
        catch (IOException e)
        {
        }
        return;
      }
      client.setInfo("loginID", msg.toString().substring(7));
    }
    else 
    {
      if (client.getInfo("loginID") == null)
      {
        try
        {
          client.sendToClient("You need to login before you can chat.");
          client.close();
        }
        catch (IOException e) {}
        return;
      }
      
      String clientRequest = msg.toString();
      
      String[] tokens = clientRequest.trim().split("\\s++");
      String[] tokens1 = clientRequest.trim().split("\\$");
      String[] tokens2 = clientRequest.trim().split("\\,");
	  StringBuilder RequestResult = new StringBuilder();
      Connection conn = null;
	  Statement stmt = null;
	  PreparedStatement preparedStmt = null;
	  String sql;
	  int connectionStatus = -1;
	  int st = -1;
	  ResultSet rs;
	  int count1=0,count2=0;
	  
		try
		{
			if(tokens[0].contentEquals("login") && tokens[1].contentEquals("staff"))
			{
				Class.forName(JDBC_DRIVER);
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				
				sql = "SELECT * FROM Managers WHERE Name = ? AND Password = ?";
				preparedStmt = conn.prepareStatement(sql);
				preparedStmt.setString(1, tokens[2] + " " +tokens[3]);
				preparedStmt.setString(2, tokens[4]);
				rs = preparedStmt.executeQuery();
				
				while (rs.next()) {
				      count1++;
				      connectionStatus = rs.getInt("Connected");  
				}
				preparedStmt.close();
				
				sql = "SELECT * FROM Employees WHERE Name = ? AND Password = ?";
				preparedStmt = conn.prepareStatement(sql);
				preparedStmt.setString(1, tokens[2] + " " +tokens[3]);
				preparedStmt.setString(2, tokens[4]);
				rs = preparedStmt.executeQuery();
				
				while (rs.next()) {
				      count2++;
				      connectionStatus = rs.getInt("Connected");
				      st = rs.getInt("Status");     
				}
				
				preparedStmt.close();
				if(count1 == 1) {
					
					if(connectionStatus == 0)
					{
						sql = "UPDATE Managers SET Connected = ? WHERE Name = ?";
						preparedStmt = conn.prepareStatement(sql);
						preparedStmt.setInt(1, 1);
						preparedStmt.setString(2, tokens[2] + " " +tokens[3]);
						preparedStmt.executeUpdate();
						RequestResult.append("Successful Manager login");	
					}
					else
					{
						RequestResult.append("Not Successful Staff login");
					}
				}
				else if(count2 == 1) {
					
					if(connectionStatus == 0 && st == 1)
					{
						sql = "UPDATE Employees SET Connected = ? WHERE Name = ?";
						preparedStmt = conn.prepareStatement(sql);
						preparedStmt.setInt(1, 1);
						preparedStmt.setString(2, tokens[2] + " " +tokens[3]);
						preparedStmt.executeUpdate();
						RequestResult.append("Successful Employee login");	
					}
					else
					{
						RequestResult.append("Not Successful Staff login");
					}
					
				}
				else {
					RequestResult.append("Not Successful Staff login");
				}
			}
			
			else if(tokens[0].contentEquals("login") && tokens[1].contentEquals("customer"))
			{
				Class.forName(JDBC_DRIVER);
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				
				sql = "SELECT * FROM Customers WHERE Name = ? AND Password = ?";
				preparedStmt = conn.prepareStatement(sql);
				preparedStmt.setString(1, tokens[2] + " " +tokens[3]);
				preparedStmt.setString(2, tokens[4]);
				rs = preparedStmt.executeQuery();
				
				while (rs.next()) {
				      count1++;
				      connectionStatus = rs.getInt("Connected");
				      st = rs.getInt("Status");
				}
				preparedStmt.close();
				
				if(count1 == 1) {
					
					if(connectionStatus == 0 &&  st == 1)
					{
						sql = "UPDATE Customers SET Connected = ? WHERE Name = ?";
						preparedStmt = conn.prepareStatement(sql);
						preparedStmt.setInt(1, 1);
						preparedStmt.setString(2, tokens[2] + " " +tokens[3]);
						preparedStmt.executeUpdate();
						RequestResult.append("Successful Customer login");
					}
					else
					{
						RequestResult.append("Not Successful Customer login");
					}
				}
				else {
					RequestResult.append("Not Successful Customer login");
				}
			}
			
			else if(tokens[0].contentEquals("logout") && tokens[1].contentEquals("customer"))
			{
				Class.forName(JDBC_DRIVER);
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				sql = "UPDATE Customers SET Connected = ? WHERE Name = ?";
				preparedStmt = conn.prepareStatement(sql);
				preparedStmt.setInt(1, 0);
				preparedStmt.setString(2, tokens[2] + " " +tokens[3]);
				preparedStmt.executeUpdate();
				RequestResult.append("Successful Customer logout");
			}
			
			else if(tokens[0].contentEquals("register") && tokens[1].contentEquals("customer"))
			{
				Class.forName(JDBC_DRIVER);
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				
				sql = "SELECT * FROM Customers WHERE Name = ?";
				preparedStmt = conn.prepareStatement(sql);
				preparedStmt.setString(1, tokens[2] + " " + tokens[3]);
				rs = preparedStmt.executeQuery();
				
				while (rs.next()) {
				      count1++;
				}
				preparedStmt.close();
				
				sql = "SELECT * FROM Customers WHERE ID = ?";
				preparedStmt = conn.prepareStatement(sql);
				preparedStmt.setInt(1, Integer.parseInt(tokens[5]));
				rs = preparedStmt.executeQuery();
				
				while (rs.next()) {
				      count2++;
				}
				preparedStmt.close();
				
                if(count1 != 0 || count2 != 0)
                {
                	RequestResult.append("Not Successful Customer registration");
				}
                else
                {
                	sql = "INSERT INTO Customers " +
                    "(ID, Name, Password, Connected, SubscriptionType, Status, StoreName, CreditCardNumber, Balance, ExpirationDate) "
                	+ "VALUES (?,?,?,?,?,?,?,?,?,?)";
 				    preparedStmt = conn.prepareStatement(sql);
 					preparedStmt.setInt(1, Integer.parseInt(tokens[5]));
 					preparedStmt.setString(2, tokens[2] + " " + tokens[3]);
 					preparedStmt.setString(3, tokens[4]);
 					preparedStmt.setInt(4, 1);
 					preparedStmt.setString(5, tokens[6]);
 					preparedStmt.setInt(6, 1);
 					preparedStmt.setString(7, tokens[7]);
 					preparedStmt.setString(8, tokens[8]);
 					preparedStmt.setDouble(9, 0);
 					preparedStmt.setString(10, tokens[9]);
 					preparedStmt.executeUpdate();
 					RequestResult.append("Successful Customer registration");
                }
			}
			
			else if(tokens[0].contentEquals("complaint") && tokens[1].contentEquals("customer"))
			{
				Class.forName(JDBC_DRIVER);
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				
				int complaintId = 0;
	 			sql = "SELECT * FROM Complaints";
	 			preparedStmt = conn.prepareStatement(sql);
                rs = preparedStmt.executeQuery();
				
				while (rs.next()) {
					complaintId++;
				}
				preparedStmt.close();
				
				String senderName = tokens[2] + " " + tokens[3];
				StringBuilder sb = new StringBuilder();
				for(int i = 4; i < tokens.length; i++)
				{
					if (i == tokens.length-1)
					{
					   sb.append(tokens[i]);
					}
					else
					{
					   sb.append(tokens[i] + " ");
					}
					
				}
				String description = sb.toString();
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
				LocalDateTime now = LocalDateTime.now();  
				String[] temp = (dtf.format(now)).trim().split("\\s++");
				String date = temp[0];
				String time = temp[1];
				
				sql = "INSERT INTO Complaints " +
	            "(ID, CustomerName, Description, Response, Status, Date, Time) "
	            + "VALUES (?,?,?,?,?,?,?)";
	 		    preparedStmt = conn.prepareStatement(sql);
	 		    preparedStmt.setInt(1,complaintId+1);
	 			preparedStmt.setString(2, senderName);
	 			preparedStmt.setString(3,description);
	 			preparedStmt.setString(4,"");
	 			preparedStmt.setInt(5,0);
	 			preparedStmt.setString(6,date);
	 			preparedStmt.setString(7,time);
	 			preparedStmt.executeUpdate();
	 			
	 			String month;
			 	String[] dateTokens = date.split("/");
			 	
			 	if(dateTokens[1].contentEquals("01") == true)
			 	{
			 		month = "JanuaryComplaints";
			 	}
			 	else if(dateTokens[1].contentEquals("02") == true)
			 	{
			 		month = "FebruaryComplaints";
			 	}
			 	else if(dateTokens[1].contentEquals("03") == true)
			 	{
			 		month = "MarchComplaints";
			 	}
			 	else if(dateTokens[1].contentEquals("04") == true)
			 	{
			 		month = "AprilComplaints";
			 	}
			 	else if(dateTokens[1].contentEquals("05") == true)
			 	{
			 		month = "MayComplaints";
			 	}
			 	else if(dateTokens[1].contentEquals("06") == true)
			 	{
			 		month = "JuneComplaints";
			 	}
			 	else if(dateTokens[1].contentEquals("07") == true)
			 	{
			 		month = "JulyComplaints";
			 	}
			 	else if(dateTokens[1].contentEquals("08") == true)
			 	{
			 		month = "AugustComplaints";
			 	}
			 	else if(dateTokens[1].contentEquals("09") == true)
			 	{
			 		month = "SeptemberComplaints";
			 	}
			 	else if(dateTokens[1].contentEquals("10") == true)
			 	{
			 		month = "OctoberComplaints";
			 	}
			 	else if(dateTokens[1].contentEquals("11") == true)
			 	{
			 		month = "NovemberComplaints";
			 	}
			 	else
			 	{
			 		month = "DecemberComplaints";
			 	}
			 	
			 	String storeName = "";
			 	
			 	sql = "SELECT * FROM Customers WHERE Name = ?";
				preparedStmt = conn.prepareStatement(sql);
				preparedStmt.setString(1,senderName);
				rs = preparedStmt.executeQuery();
			 	
				while (rs.next()) {
					storeName = rs.getString("StoreName");
			    }
				
			 	int previousComplaints = -1;
			 	
			 	sql = "SELECT * FROM ComplaintsReport WHERE StoreName = ?";
				preparedStmt = conn.prepareStatement(sql);
				preparedStmt.setString(1,storeName);
				rs = preparedStmt.executeQuery();
			 	
				while (rs.next()) {
				  previousComplaints = rs.getInt(month);
				}
				
			 	sql = "UPDATE ComplaintsReport SET " + month + " = ? WHERE StoreName = ?";
				preparedStmt = conn.prepareStatement(sql);
				preparedStmt.setDouble(1,previousComplaints+1);
			    preparedStmt.setString(2,storeName);
				preparedStmt.executeUpdate();
	 			
	 			RequestResult.append(Integer.toString(complaintId+1));
			}
			
			else if(tokens[0].contentEquals("response") && tokens[1].contentEquals("customer"))
			{
				Class.forName(JDBC_DRIVER);
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				
				String response = "X";
				String date = "X";
				String time = "X";
				int status = -1;
				String senderName = tokens[2] + " " + tokens[3];
				int responseId = Integer.parseInt(tokens[4]);
				
				sql = "SELECT * FROM Complaints WHERE ID = ? AND CustomerName = ?";
	 			preparedStmt = conn.prepareStatement(sql);
	 			preparedStmt.setInt(1,responseId);
	 			preparedStmt.setString(2,senderName);
                rs = preparedStmt.executeQuery();
				
				while (rs.next()) {
					response = rs.getString("Response");
					date = rs.getString("Date");
					time = rs.getString("Time");
					status = rs.getInt("Status"); 
				}
				
				if(response.contentEquals("X"))
				{
					RequestResult.append("Invalid Response ID");	
				}
				
				else
				{
					DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
					LocalDateTime now = LocalDateTime.now();
					String[] temp = (dtf.format(now)).trim().split("\\s++");
					String nowDate = temp[0];
					String nowTime = temp[1];
					
					preparedStmt.close();
					
					double h = hoursDiff(nowDate,nowTime,date,time);
					
					if(h > 24 && status == 0)
					{
						String x = "We are deeply sorry! A response was not given"
								+ " within 24 hours";
						
						sql = "UPDATE Complaints SET Response = ? WHERE ID = ? AND CustomerName = ?";
						preparedStmt = conn.prepareStatement(sql);
						preparedStmt.setString(1,x);
					    preparedStmt.setInt(2,responseId);
			 			preparedStmt.setString(3,senderName);
						preparedStmt.executeUpdate();
						
						RequestResult.append(x);
					}
					
					else if(h <= 24 && status == 0)
					{
						RequestResult.append("No response yet");
					}
					
					else if(status == 1)
					{
						RequestResult.append(response);
					}
				}	
			}
			
			else if(tokens[0].contentEquals("get") && tokens[1].contentEquals("catalog"))
			{
				Class.forName(JDBC_DRIVER);
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				
				int id;
				String name;
				double price;
				String image;
				String type;
				String sale;
				
				sql = "SELECT * FROM CatalogProducts";
	 			preparedStmt = conn.prepareStatement(sql);
                rs = preparedStmt.executeQuery();
				
				while (rs.next()) {
					id= rs.getInt("ID");
					name = rs.getString("Name");
					price = rs.getDouble("Price");
					image = rs.getString("Image");
					type = rs.getString("Type");
					sale = rs.getString("Sale");
					RequestResult.append(Integer.toString(id) + " " + name + " " + Double.toString(price)
					                     + " " + image + " " + type + " " + sale + "\n");
				}
			}
			
			else if(tokens1[0].contentEquals("order"))
			{
				Class.forName(JDBC_DRIVER);
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				
				String name = tokens1[1];
				String payment = tokens1[8];
				String storeName = tokens1[4];
				String store = null;
				String sub = null;
				
				sql = "SELECT * FROM Customers WHERE Name = ?";
	 			preparedStmt = conn.prepareStatement(sql);
	 			preparedStmt.setString(1,name);
                rs = preparedStmt.executeQuery();
				
				while (rs.next()) {
					sub  = rs.getString("SubscriptionType");
					store  = rs.getString("StoreName");
				}
				if((payment.contentEquals("Subscription") && sub.contentEquals("None")) ||
				   storeName.contentEquals(store) == false)
				{
					RequestResult.append("Unsuccessful Order");
				}
				else
				{
					preparedStmt.close();
					
					int orderId = 0;
		 			sql = "SELECT * FROM Orders";
		 			preparedStmt = conn.prepareStatement(sql);
	                rs = preparedStmt.executeQuery();
					
					while (rs.next()) {
						orderId++;
					}
					preparedStmt.close();
					
					int pickUpType = Integer.parseInt(tokens1[3]);
					String recieverName;
					String deliveryAddress;
					String phone;
					String congrat;
					String arrivalDate;
					String orderType = tokens1[5];
					
					 if(tokens1[9].contentEquals("-1"))
					 {
						 arrivalDate = "";
					 }
					 else
					 {
						 arrivalDate = tokens1[9];
					 }
					
					double discount = -1;
					double price = Double.parseDouble(tokens1[6]);
					
					if(payment.contentEquals("Subscription"))
					{
						sql = "SELECT * FROM SubscriptionTypes WHERE Type = ?";
			 			preparedStmt = conn.prepareStatement(sql);
			 			preparedStmt.setString(1,sub);
		                rs = preparedStmt.executeQuery();
						
						while (rs.next()) {
							discount  = rs.getDouble("DiscountPerMonth");
						}
						
						price = (1-discount)*price;
					}
					
					String productsInfo  = tokens1[7];
					
					if(pickUpType == 0)
					{
						recieverName = "";
						deliveryAddress = "";
						phone = "";
					}
					else
					{
						recieverName = tokens1[10];
						deliveryAddress = tokens1[11];
						phone = tokens1[12];
					}
					if(tokens1[2].contentEquals("-1"))
                    {
                    	congrat = "";
                    }
					else
					{
						congrat = tokens1[2];
					}
					
					DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
					LocalDateTime now = LocalDateTime.now();  
					String[] temp = (dtf.format(now)).trim().split("\\s++");
					String orderDate = temp[0];
					String orderTime = temp[1];
					
					if(arrivalDate.isEmpty() == false)
					{
						if(hoursDiff(arrivalDate,orderTime,orderDate,orderTime) <= 0)
						{
							RequestResult.append("Unsuccessful Order");
						}
						else
						{
							sql = "INSERT INTO Orders " +
							"(ID, Price, ProductsInfo, Congrat, CustomerName, Payment, PurchaseDate,"
							+ " PurchaseTime, PickUpType, DeliveryAddress, Status, StoreName, RecieverName,"
							+ " RecieverPhone, OrderType, CancelResponse, ArrivalDate) "
							+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
								 	preparedStmt = conn.prepareStatement(sql);
								 	
								    preparedStmt.setInt(1,orderId+1);
								    preparedStmt.setDouble(2, Math.round(price));
								 	preparedStmt.setString(3,productsInfo);
								    preparedStmt.setString(4,congrat);
								 	preparedStmt.setString(5,name);
								 	preparedStmt.setString(6,payment);
								 	preparedStmt.setString(7,orderDate);
								 	preparedStmt.setString(8,orderTime);
								 	preparedStmt.setInt(9,pickUpType);
								 	preparedStmt.setString(10,deliveryAddress);
								 	preparedStmt.setInt(11,1);
								 	preparedStmt.setString(12,storeName);
								 	preparedStmt.setString(13,recieverName);
								 	preparedStmt.setString(14,phone);
								 	preparedStmt.setString(15,orderType);
								 	preparedStmt.setString(16,"");
								 	preparedStmt.setString(17,arrivalDate);
								 	
								 	preparedStmt.executeUpdate();
								 	
								 	String month;
								 	String[] dateTokens = orderDate.split("/");
								 	
								 	if(dateTokens[1].contentEquals("01") == true)
								 	{
								 		month = "JanuaryEarnings";
								 	}
								 	else if(dateTokens[1].contentEquals("02") == true)
								 	{
								 		month = "FebruaryEarnings";
								 	}
								 	else if(dateTokens[1].contentEquals("03") == true)
								 	{
								 		month = "MarchEarnings";
								 	}
								 	else if(dateTokens[1].contentEquals("04") == true)
								 	{
								 		month = "AprilEarnings";
								 	}
								 	else if(dateTokens[1].contentEquals("05") == true)
								 	{
								 		month = "MayEarnings";
								 	}
								 	else if(dateTokens[1].contentEquals("06") == true)
								 	{
								 		month = "JuneEarnings";
								 	}
								 	else if(dateTokens[1].contentEquals("07") == true)
								 	{
								 		month = "JulyEarnings";
								 	}
								 	else if(dateTokens[1].contentEquals("08") == true)
								 	{
								 		month = "AugustEarnings";
								 	}
								 	else if(dateTokens[1].contentEquals("09") == true)
								 	{
								 		month = "SeptemberEarnings";
								 	}
								 	else if(dateTokens[1].contentEquals("10") == true)
								 	{
								 		month = "OctoberEarnings";
								 	}
								 	else if(dateTokens[1].contentEquals("11") == true)
								 	{
								 		month = "NovemberEarnings";
								 	}
								 	else
								 	{
								 		month = "DecemberEarnings";
								 	}
								 	
								 	double previousEarnings = -1;
								 	
								 	sql = "SELECT * FROM MonthlyEarnings WHERE StoreName = ?";
									preparedStmt = conn.prepareStatement(sql);
									preparedStmt.setString(1,storeName);
									rs = preparedStmt.executeQuery();
								 	
									while (rs.next()) {
									  previousEarnings = rs.getDouble(month);
									}
									
								 	sql = "UPDATE MonthlyEarnings SET " + month + " = ? WHERE StoreName = ?";
									preparedStmt = conn.prepareStatement(sql);
									preparedStmt.setDouble(1,previousEarnings+Math.round(price));
								    preparedStmt.setString(2,storeName);
									preparedStmt.executeUpdate();
								 			
									if(dateTokens[1].contentEquals("01") == true)
								 	{
								 		month = "January";
								 	}
								 	else if(dateTokens[1].contentEquals("02") == true)
								 	{
								 		month = "February";
								 	}
								 	else if(dateTokens[1].contentEquals("03") == true)
								 	{
								 		month = "March";
								 	}
								 	else if(dateTokens[1].contentEquals("04") == true)
								 	{
								 		month = "April";
								 	}
								 	else if(dateTokens[1].contentEquals("05") == true)
								 	{
								 		month = "May";
								 	}
								 	else if(dateTokens[1].contentEquals("06") == true)
								 	{
								 		month = "June";
								 	}
								 	else if(dateTokens[1].contentEquals("07") == true)
								 	{
								 		month = "July";
								 	}
								 	else if(dateTokens[1].contentEquals("08") == true)
								 	{
								 		month = "August";
								 	}
								 	else if(dateTokens[1].contentEquals("09") == true)
								 	{
								 		month = "September";
								 	}
								 	else if(dateTokens[1].contentEquals("10") == true)
								 	{
								 		month = "October";
								 	}
								 	else if(dateTokens[1].contentEquals("11") == true)
								 	{
								 		month = "November";
								 	}
								 	else
								 	{
								 		month = "December";
								 	} 
									
									String storeTable = "";
									
									if(storeName.contentEquals("LilacHaifa") == true)
									{
										storeTable = "LilacHaifaOrders";
									}
									else
									{
										storeTable = "LilacTelAvivOrders";
									}
									
									int addOnSet = 0;
									int addOnFlowerpot = 0;
									int addOnArrangements = 0;
									int addOnBridal = 0;
									
									String[] products = productsInfo.trim().split("\n");
									
									for(int i = 0; i < products.length; i++)
									{
										String[] p = products[i].trim().split("\\s++");
										
										if(p[1].contentEquals("Set") == true)
										{
											addOnSet++;
										}
										if(p[1].contentEquals("Flowerpot") == true)
										{
											addOnFlowerpot++;
										}
										if(p[1].contentEquals("Arrangements") == true)
										{
											addOnArrangements++;
										}
										if(p[1].contentEquals("Bridal") == true)
										{
											addOnBridal++;
										}
									}
									
									if(addOnSet > 0)
									{
                                        int previousCount = -1;
										
										sql = "SELECT * FROM " + storeTable + " WHERE Month = ?";
										preparedStmt = conn.prepareStatement(sql);
										preparedStmt.setString(1,month);
										rs = preparedStmt.executeQuery();
									 	
										while (rs.next()) {
											previousCount = rs.getInt("SetCount");
										}
										
										sql = "UPDATE " + storeTable + " SET SetCount = ? WHERE Month = ?";
										preparedStmt = conn.prepareStatement(sql);
										preparedStmt.setInt(1,previousCount+addOnSet);
									    preparedStmt.setString(2,month);
										preparedStmt.executeUpdate();
									}
									
									if(addOnFlowerpot > 0)
									{
                                        int previousCount = -1;
										
										sql = "SELECT * FROM " + storeTable + " WHERE Month = ?";
										preparedStmt = conn.prepareStatement(sql);
										preparedStmt.setString(1,month);
										rs = preparedStmt.executeQuery();
									 	
										while (rs.next()) {
											previousCount = rs.getInt("FlowerpotCount");
										}
										
										sql = "UPDATE " + storeTable + " SET FlowerpotCount = ? WHERE Month = ?";
										preparedStmt = conn.prepareStatement(sql);
										preparedStmt.setInt(1,previousCount+addOnFlowerpot);
									    preparedStmt.setString(2,month);
										preparedStmt.executeUpdate();
									}
									
									if(addOnArrangements > 0)
									{
                                       int previousCount = -1;
										
										sql = "SELECT * FROM " + storeTable + " WHERE Month = ?";
										preparedStmt = conn.prepareStatement(sql);
										preparedStmt.setString(1,month);
										rs = preparedStmt.executeQuery();
									 	
										while (rs.next()) {
											previousCount = rs.getInt("ArrangementsCount");
										}
										
										sql = "UPDATE " + storeTable + " SET ArrangementsCount = ? WHERE Month = ?";
										preparedStmt = conn.prepareStatement(sql);
										preparedStmt.setInt(1,previousCount+addOnArrangements);
									    preparedStmt.setString(2,month);
										preparedStmt.executeUpdate();
									}
									
									if(addOnBridal > 0)
									{
                                        int previousCount = -1;
						
										sql = "SELECT * FROM " + storeTable + " WHERE Month = ?";
										preparedStmt = conn.prepareStatement(sql);
										preparedStmt.setString(1,month);
										rs = preparedStmt.executeQuery();
									 	
										while (rs.next()) {
											previousCount = rs.getInt("BridalCount");
										}
										
										sql = "UPDATE " + storeTable + " SET BridalCount = ? WHERE Month = ?";
										preparedStmt = conn.prepareStatement(sql);
										preparedStmt.setInt(1,previousCount+addOnBridal);
									    preparedStmt.setString(2,month);
										preparedStmt.executeUpdate();   
									}
									
								 	RequestResult.append(Integer.toString(orderId+1));		
						}
						
					}
					else
					{
						sql = "INSERT INTO Orders " +
								"(ID, Price, ProductsInfo, Congrat, CustomerName, Payment, PurchaseDate,"
								+ " PurchaseTime, PickUpType, DeliveryAddress, Status, StoreName, RecieverName,"
								+ " RecieverPhone, OrderType, CancelResponse, ArrivalDate) "
								+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
									 	preparedStmt = conn.prepareStatement(sql);
									 	
									    preparedStmt.setInt(1,orderId+1);
									    preparedStmt.setDouble(2, Math.round(price));
									 	preparedStmt.setString(3,productsInfo);
									    preparedStmt.setString(4,congrat);
									 	preparedStmt.setString(5,name);
									 	preparedStmt.setString(6,payment);
									 	preparedStmt.setString(7,orderDate);
									 	preparedStmt.setString(8,orderTime);
									 	preparedStmt.setInt(9,pickUpType);
									 	preparedStmt.setString(10,deliveryAddress);
									 	preparedStmt.setInt(11,1);
									 	preparedStmt.setString(12,storeName);
									 	preparedStmt.setString(13,recieverName);
									 	preparedStmt.setString(14,phone);
									 	preparedStmt.setString(15,orderType);
									 	preparedStmt.setString(16,"");
									 	preparedStmt.setString(17,arrivalDate);
									 	
									 	preparedStmt.executeUpdate();
									 			
									 	String month;
									 	String[] dateTokens = orderDate.split("/");
									 	
									 	if(dateTokens[1].contentEquals("01") == true)
									 	{
									 		month = "JanuaryEarnings";
									 	}
									 	else if(dateTokens[1].contentEquals("02") == true)
									 	{
									 		month = "FebruaryEarnings";
									 	}
									 	else if(dateTokens[1].contentEquals("03") == true)
									 	{
									 		month = "MarchEarnings";
									 	}
									 	else if(dateTokens[1].contentEquals("04") == true)
									 	{
									 		month = "AprilEarnings";
									 	}
									 	else if(dateTokens[1].contentEquals("05") == true)
									 	{
									 		month = "MayEarnings";
									 	}
									 	else if(dateTokens[1].contentEquals("06") == true)
									 	{
									 		month = "JuneEarnings";
									 	}
									 	else if(dateTokens[1].contentEquals("07") == true)
									 	{
									 		month = "JulyEarnings";
									 	}
									 	else if(dateTokens[1].contentEquals("08") == true)
									 	{
									 		month = "AugustEarnings";
									 	}
									 	else if(dateTokens[1].contentEquals("09") == true)
									 	{
									 		month = "SeptemberEarnings";
									 	}
									 	else if(dateTokens[1].contentEquals("10") == true)
									 	{
									 		month = "OctoberEarnings";
									 	}
									 	else if(dateTokens[1].contentEquals("11") == true)
									 	{
									 		month = "NovemberEarnings";
									 	}
									 	else
									 	{
									 		month = "DecemberEarnings";
									 	}
									 	
									 	double previousEarnings = -1;
									 	rs.close();
									 	
									 	sql = "SELECT * FROM MonthlyEarnings WHERE StoreName = ?";
										preparedStmt = conn.prepareStatement(sql);
										preparedStmt.setString(1,storeName);
										rs = preparedStmt.executeQuery();
									 	
										while (rs.next()) {
										  previousEarnings = rs.getDouble(month);
										}
										
									 	sql = "UPDATE MonthlyEarnings SET " + month + " = ? WHERE StoreName = ?";
										preparedStmt = conn.prepareStatement(sql);
										preparedStmt.setDouble(1,previousEarnings+Math.round(price));
									    preparedStmt.setString(2,storeName);
										preparedStmt.executeUpdate();
									 	
										if(dateTokens[1].contentEquals("01") == true)
									 	{
									 		month = "January";
									 	}
									 	else if(dateTokens[1].contentEquals("02") == true)
									 	{
									 		month = "February";
									 	}
									 	else if(dateTokens[1].contentEquals("03") == true)
									 	{
									 		month = "March";
									 	}
									 	else if(dateTokens[1].contentEquals("04") == true)
									 	{
									 		month = "April";
									 	}
									 	else if(dateTokens[1].contentEquals("05") == true)
									 	{
									 		month = "May";
									 	}
									 	else if(dateTokens[1].contentEquals("06") == true)
									 	{
									 		month = "June";
									 	}
									 	else if(dateTokens[1].contentEquals("07") == true)
									 	{
									 		month = "July";
									 	}
									 	else if(dateTokens[1].contentEquals("08") == true)
									 	{
									 		month = "August";
									 	}
									 	else if(dateTokens[1].contentEquals("09") == true)
									 	{
									 		month = "September";
									 	}
									 	else if(dateTokens[1].contentEquals("10") == true)
									 	{
									 		month = "October";
									 	}
									 	else if(dateTokens[1].contentEquals("11") == true)
									 	{
									 		month = "November";
									 	}
									 	else
									 	{
									 		month = "December";
									 	} 
										
										String storeTable = "";
										
										if(storeName.contentEquals("LilacHaifa") == true)
										{
											storeTable = "LilacHaifaOrders";
										}
										else
										{
											storeTable = "LilacTelAvivOrders";
										}
										
										int addOnSet = 0;
										int addOnFlowerpot = 0;
										int addOnArrangements = 0;
										int addOnBridal = 0;
										
										String[] products = productsInfo.trim().split("\n");
										
										for(int i = 0; i < products.length; i++)
										{
											String[] p = products[i].trim().split("\\s++");
											
											if(p[1].contentEquals("Set") == true)
											{
												addOnSet++;
											}
											if(p[1].contentEquals("Flowerpot") == true)
											{
												addOnFlowerpot++;
											}
											if(p[1].contentEquals("Arrangements") == true)
											{
												addOnArrangements++;
											}
											if(p[1].contentEquals("Bridal") == true)
											{
												addOnBridal++;
											}
										}
										
										if(addOnSet > 0)
										{
	                                        int previousCount = -1;
											
											sql = "SELECT * FROM " + storeTable + " WHERE Month = ?";
											preparedStmt = conn.prepareStatement(sql);
											preparedStmt.setString(1,month);
											rs = preparedStmt.executeQuery();
										 	
											while (rs.next()) {
												previousCount = rs.getInt("SetCount");
											}
											
											sql = "UPDATE " + storeTable + " SET SetCount = ? WHERE Month = ?";
											preparedStmt = conn.prepareStatement(sql);
											preparedStmt.setInt(1,previousCount+addOnSet);
										    preparedStmt.setString(2,month);
											preparedStmt.executeUpdate();
										}
										
										if(addOnFlowerpot > 0)
										{
	                                        int previousCount = -1;
											
											sql = "SELECT * FROM " + storeTable + " WHERE Month = ?";
											preparedStmt = conn.prepareStatement(sql);
											preparedStmt.setString(1,month);
											rs = preparedStmt.executeQuery();
										 	
											while (rs.next()) {
												previousCount = rs.getInt("FlowerpotCount");
											}
											
											sql = "UPDATE " + storeTable + " SET FlowerpotCount = ? WHERE Month = ?";
											preparedStmt = conn.prepareStatement(sql);
											preparedStmt.setInt(1,previousCount+addOnFlowerpot);
										    preparedStmt.setString(2,month);
											preparedStmt.executeUpdate();
										}
										
										if(addOnArrangements > 0)
										{
	                                       int previousCount = -1;
											
											sql = "SELECT * FROM " + storeTable + " WHERE Month = ?";
											preparedStmt = conn.prepareStatement(sql);
											preparedStmt.setString(1,month);
											rs = preparedStmt.executeQuery();
										 	
											while (rs.next()) {
												previousCount = rs.getInt("ArrangementsCount");
											}
											
											sql = "UPDATE " + storeTable + " SET ArrangementsCount = ? WHERE Month = ?";
											preparedStmt = conn.prepareStatement(sql);
											preparedStmt.setInt(1,previousCount+addOnArrangements);
										    preparedStmt.setString(2,month);
											preparedStmt.executeUpdate();
										}
										
										if(addOnBridal > 0)
										{
	                                        int previousCount = -1;
							
											sql = "SELECT * FROM " + storeTable + " WHERE Month = ?";
											preparedStmt = conn.prepareStatement(sql);
											preparedStmt.setString(1,month);
											rs = preparedStmt.executeQuery();
										 	
											while (rs.next()) {
												previousCount = rs.getInt("	BridalCount");
											}
											
											sql = "UPDATE " + storeTable + " SET BridalCount = ? WHERE Month = ?";
											preparedStmt = conn.prepareStatement(sql);
											preparedStmt.setInt(1,previousCount+addOnBridal);
										    preparedStmt.setString(2,month);
											preparedStmt.executeUpdate();   
										}
										
									 	RequestResult.append(Integer.toString(orderId+1));		
					}
				}
			}
			
			else if(tokens[0].contentEquals("cancel") && tokens[1].contentEquals("order"))
			{
				Class.forName(JDBC_DRIVER);
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				
				String arrivalDate = "X";
				String arrivalTime = "X";
				String productsInfo = "";
				double price = -1;
				String senderName = tokens[2] + " " + tokens[3];
				int orderId = Integer.parseInt(tokens[4]);
				int status = -1;
				
				sql = "SELECT * FROM Orders WHERE ID = ? AND CustomerName = ?";
	 			preparedStmt = conn.prepareStatement(sql);
	 			preparedStmt.setInt(1,orderId);
	 			preparedStmt.setString(2,senderName);
                rs = preparedStmt.executeQuery();
				
				while (rs.next()) {
					price = rs.getDouble("Price");
					arrivalDate = rs.getString("ArrivalDate");
					arrivalTime = rs.getString("PurchaseTime"); 
					status = rs.getInt("Status");
					productsInfo = rs.getString("ProductsInfo");
				}
				preparedStmt.close();
				
				if(status == 0 || status == -1)
				{
					RequestResult.append("Invalid Cancellation");	
				}
				
				else
				{
					if(arrivalDate.isEmpty() == true)
					{
						RequestResult.append("A Quick order can not be cancelled and is "
								+ "guaranteed to arrive on time");
					}
					
					else
					{
						status = 0;
						
						DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
						LocalDateTime now = LocalDateTime.now();
						String[] temp = (dtf.format(now)).trim().split("\\s++");
						
						String nowDate = temp[0];
						String nowTime = temp[1];
						
						double refund = -1;
						
						double h = hoursDiff(arrivalDate,arrivalTime,nowDate,nowTime);
						
						if(h >= 3)
						{
							refund = price;
						}
						
						else if(h <= 1)
						{
							refund = 0;
						}
						
						else
						{
							refund = price*0.5;
						}
						
						double balance = -1;
						
						sql = "SELECT * FROM Customers WHERE Name = ?";
			 			preparedStmt = conn.prepareStatement(sql);
			 			preparedStmt.setString(1,senderName);
		                rs = preparedStmt.executeQuery();
						
		                while (rs.next()) {
							balance = rs.getDouble("Balance");
						}
		                
		                balance += refund;
		                
		                sql = "UPDATE Customers SET Balance = ? WHERE Name = ?";
						preparedStmt = conn.prepareStatement(sql);
						preparedStmt.setDouble(1,balance);
					    preparedStmt.setString(2,senderName);
						preparedStmt.executeUpdate();
		                
						String cancellationResponse = "Your cancellation refund for order No. "
	                                                   + orderId + " is " + refund;
						
						sql = "UPDATE Orders SET Price = ?, Status = ?, CancelResponse = ?"
								+ "WHERE ID = ? AND CustomerName = ?";
						preparedStmt = conn.prepareStatement(sql);
						preparedStmt.setDouble(1,Math.round(price-refund));
					    preparedStmt.setInt(2,0);
					    preparedStmt.setString(3,cancellationResponse);
					    preparedStmt.setInt(4,orderId);
					    preparedStmt.setString(5,senderName);
						preparedStmt.executeUpdate();
						
						sql = "SELECT * FROM Orders WHERE ID = ? AND CustomerName = ?";
						preparedStmt = conn.prepareStatement(sql);
						preparedStmt.setInt(1,orderId);
					    preparedStmt.setString(2,senderName);
						rs = preparedStmt.executeQuery();
					 	
						String orderDate = "";
						String storeName = "";
						
						while (rs.next()) {
						  orderDate = rs.getString("PurchaseDate");
						  storeName = rs.getString("StoreName");
						}
						
						String month;
					 	String[] dateTokens = orderDate.split("/");
					 	
					 	if(dateTokens[1].contentEquals("01") == true)
					 	{
					 		month = "JanuaryEarnings";
					 	}
					 	else if(dateTokens[1].contentEquals("02") == true)
					 	{
					 		month = "FebruaryEarnings";
					 	}
					 	else if(dateTokens[1].contentEquals("03") == true)
					 	{
					 		month = "MarchEarnings";
					 	}
					 	else if(dateTokens[1].contentEquals("04") == true)
					 	{
					 		month = "AprilEarnings";
					 	}
					 	else if(dateTokens[1].contentEquals("05") == true)
					 	{
					 		month = "MayEarnings";
					 	}
					 	else if(dateTokens[1].contentEquals("06") == true)
					 	{
					 		month = "JuneEarnings";
					 	}
					 	else if(dateTokens[1].contentEquals("07") == true)
					 	{
					 		month = "JulyEarnings";
					 	}
					 	else if(dateTokens[1].contentEquals("08") == true)
					 	{
					 		month = "AugustEarnings";
					 	}
					 	else if(dateTokens[1].contentEquals("09") == true)
					 	{
					 		month = "SeptemberEarnings";
					 	}
					 	else if(dateTokens[1].contentEquals("10") == true)
					 	{
					 		month = "OctoberEarnings";
					 	}
					 	else if(dateTokens[1].contentEquals("11") == true)
					 	{
					 		month = "NovemberEarnings";
					 	}
					 	else
					 	{
					 		month = "DecemberEarnings";
					 	}
					 	
					 	double previousEarnings = -1;
					 	rs.close();
					 	
					 	sql = "SELECT * FROM MonthlyEarnings WHERE StoreName = ?";
						preparedStmt = conn.prepareStatement(sql);
						preparedStmt.setString(1,storeName);
						rs = preparedStmt.executeQuery();
					 	
						while (rs.next()) {
						  previousEarnings = rs.getDouble(month);
						}
						
					 	sql = "UPDATE MonthlyEarnings SET " + month + " = ? WHERE StoreName = ?";
						preparedStmt = conn.prepareStatement(sql);
						preparedStmt.setDouble(1,previousEarnings-refund);
					    preparedStmt.setString(2,storeName);
						preparedStmt.executeUpdate();
						
						if(dateTokens[1].contentEquals("01") == true)
					 	{
					 		month = "January";
					 	}
					 	else if(dateTokens[1].contentEquals("02") == true)
					 	{
					 		month = "February";
					 	}
					 	else if(dateTokens[1].contentEquals("03") == true)
					 	{
					 		month = "March";
					 	}
					 	else if(dateTokens[1].contentEquals("04") == true)
					 	{
					 		month = "April";
					 	}
					 	else if(dateTokens[1].contentEquals("05") == true)
					 	{
					 		month = "May";
					 	}
					 	else if(dateTokens[1].contentEquals("06") == true)
					 	{
					 		month = "June";
					 	}
					 	else if(dateTokens[1].contentEquals("07") == true)
					 	{
					 		month = "July";
					 	}
					 	else if(dateTokens[1].contentEquals("08") == true)
					 	{
					 		month = "August";
					 	}
					 	else if(dateTokens[1].contentEquals("09") == true)
					 	{
					 		month = "September";
					 	}
					 	else if(dateTokens[1].contentEquals("10") == true)
					 	{
					 		month = "October";
					 	}
					 	else if(dateTokens[1].contentEquals("11") == true)
					 	{
					 		month = "November";
					 	}
					 	else
					 	{
					 		month = "December";
					 	} 
						
						String storeTable = "";
						
						if(storeName.contentEquals("LilacHaifa") == true)
						{
							storeTable = "LilacHaifaOrders";
						}
						else
						{
							storeTable = "LilacTelAvivOrders";
						}
						
						int cancelledSet = 0;
						int cancelledFlowerpot = 0;
						int cancelledArrangements = 0;
						int cancelledBridal = 0;
						
						String[] products = productsInfo.trim().split("\n");
						
						for(int i = 0; i < products.length; i++)
						{
							String[] p = products[i].trim().split("\\s++");
							
							if(p[1].contentEquals("Set") == true)
							{
								cancelledSet++;
							}
							if(p[1].contentEquals("Flowerpot") == true)
							{
								cancelledFlowerpot++;
							}
							if(p[1].contentEquals("Arrangements") == true)
							{
								cancelledArrangements++;
							}
							if(p[1].contentEquals("Bridal") == true)
							{
								cancelledBridal++;
							}
						}
						
						if(cancelledSet > 0)
						{
                            int previousCount = -1;
							
							sql = "SELECT * FROM " + storeTable + " WHERE Month = ?";
							preparedStmt = conn.prepareStatement(sql);
							preparedStmt.setString(1,month);
							rs = preparedStmt.executeQuery();
						 	
							while (rs.next()) {
								previousCount = rs.getInt("SetCount");
							}
							
							sql = "UPDATE " + storeTable + " SET SetCount = ? WHERE Month = ?";
							preparedStmt = conn.prepareStatement(sql);
							preparedStmt.setInt(1,previousCount-cancelledSet);
						    preparedStmt.setString(2,month);
							preparedStmt.executeUpdate();
						}
						
						if(cancelledFlowerpot > 0)
						{
                            int previousCount = -1;
							
							sql = "SELECT * FROM " + storeTable + " WHERE Month = ?";
							preparedStmt = conn.prepareStatement(sql);
							preparedStmt.setString(1,month);
							rs = preparedStmt.executeQuery();
						 	
							while (rs.next()) {
								previousCount = rs.getInt("FlowerpotCount");
							}
							
							sql = "UPDATE " + storeTable + " SET FlowerpotCount = ? WHERE Month = ?";
							preparedStmt = conn.prepareStatement(sql);
							preparedStmt.setInt(1,previousCount-cancelledFlowerpot);
						    preparedStmt.setString(2,month);
							preparedStmt.executeUpdate();
						}
						
						if(cancelledArrangements > 0)
						{
                           int previousCount = -1;
							
							sql = "SELECT * FROM " + storeTable + " WHERE Month = ?";
							preparedStmt = conn.prepareStatement(sql);
							preparedStmt.setString(1,month);
							rs = preparedStmt.executeQuery();
						 	
							while (rs.next()) {
								previousCount = rs.getInt("ArrangementsCount");
							}
							
							sql = "UPDATE " + storeTable + " SET ArrangementsCount = ? WHERE Month = ?";
							preparedStmt = conn.prepareStatement(sql);
							preparedStmt.setInt(1,previousCount-cancelledArrangements);
						    preparedStmt.setString(2,month);
							preparedStmt.executeUpdate();
						}
						
						if(cancelledBridal > 0)
						{
                            int previousCount = -1;
			
							sql = "SELECT * FROM " + storeTable + " WHERE Month = ?";
							preparedStmt = conn.prepareStatement(sql);
							preparedStmt.setString(1,month);
							rs = preparedStmt.executeQuery();
						 	
							while (rs.next()) {
								previousCount = rs.getInt("BridalCount");
							}
							
							sql = "UPDATE " + storeTable + " SET BridalCount = ? WHERE Month = ?";
							preparedStmt = conn.prepareStatement(sql);
							preparedStmt.setInt(1,previousCount-cancelledBridal);
						    preparedStmt.setString(2,month);
							preparedStmt.executeUpdate();   
						}
						
						RequestResult.append(cancellationResponse);
					}
				}	
			}
			
			else if(tokens[0].contentEquals("change") && tokens[1].contentEquals("employee"))
			{
				Class.forName(JDBC_DRIVER);
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				
				int employeeId = Integer.parseInt(tokens[2]);
				String privileges = tokens[3];
				
				sql = "UPDATE Employees SET Privileges = ? WHERE ID = ?";
				preparedStmt = conn.prepareStatement(sql);
				preparedStmt.setString(1,privileges);
				preparedStmt.setInt(2,employeeId);				
				preparedStmt.executeUpdate();
				RequestResult.append(employeeId);
			}
			
			else if(tokens[0].contentEquals("fire") && tokens[1].contentEquals("employee"))
			{
				Class.forName(JDBC_DRIVER);
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				
				int employeeId = Integer.parseInt(tokens[2]);
				
				sql = "UPDATE Employees SET Status = ? WHERE ID = ?";
				preparedStmt = conn.prepareStatement(sql);
				preparedStmt.setInt(1,0);
				preparedStmt.setInt(2,employeeId);		
				preparedStmt.executeUpdate();
				RequestResult.append(employeeId);
			}
			
			else if(tokens[0].contentEquals("freeze") && tokens[1].contentEquals("customer"))
			{
				Class.forName(JDBC_DRIVER);
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				
				int customerId = Integer.parseInt(tokens[2]);
				
				sql = "UPDATE Customers SET Status = ? WHERE ID = ?";
				preparedStmt = conn.prepareStatement(sql);
				preparedStmt.setInt(1,0);
				preparedStmt.setInt(2,customerId);		
				preparedStmt.executeUpdate();
				RequestResult.append(customerId);
			}
			
			else if(tokens[0].contentEquals("logout") && tokens[1].contentEquals("employee"))
			{
				Class.forName(JDBC_DRIVER);
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				
				sql = "UPDATE Employees SET Connected = ? WHERE Name = ?";
				preparedStmt = conn.prepareStatement(sql);
				preparedStmt.setInt(1, 0);
				preparedStmt.setString(2, tokens[2] + " " +tokens[3]);
				preparedStmt.executeUpdate();
				RequestResult.append("Successful Employee logout");
			}
			
			else if(tokens[0].contentEquals("logout") && tokens[1].contentEquals("manager"))
			{
				Class.forName(JDBC_DRIVER);
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				
				sql = "UPDATE Managers SET Connected = ? WHERE Name = ?";
				preparedStmt = conn.prepareStatement(sql);
				preparedStmt.setInt(1, 0);
				preparedStmt.setString(2, tokens[2] + " " +tokens[3]);
				preparedStmt.executeUpdate();
				RequestResult.append("Successful Manager logout");
			}
			
			else if(tokens[0].contentEquals("complaint") && tokens[1].contentEquals("employee"))
			{
				Class.forName(JDBC_DRIVER);
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				
				int complaintId = Integer.parseInt(tokens[2]);
				String sender = "-1";
				String description = "-1";
				
				sql = "SELECT * FROM Complaints WHERE ID = ?";
	 			preparedStmt = conn.prepareStatement(sql);
	 			preparedStmt.setInt(1,complaintId);
                rs = preparedStmt.executeQuery();
				
                while (rs.next()) {
                	sender = rs.getString("CustomerName");
                	description = rs.getString("Description");
				}
                
                RequestResult.append(sender + ": " + description);   	
			}
			
			else if(tokens[0].contentEquals("get") && tokens[1].contentEquals("complaints"))
			{
				Class.forName(JDBC_DRIVER);
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				
				StringBuilder sb = new StringBuilder();
				int id;
				
				sql = "SELECT * FROM Complaints WHERE Status = ?";
	 			preparedStmt = conn.prepareStatement(sql);
	 			preparedStmt.setInt(1,0);
                rs = preparedStmt.executeQuery();
				
                while (rs.next()) {
                	id = rs.getInt("ID");
                	sb.append(id + ",");
				}
                
                if(sb.toString().isEmpty() == true)
                {
                	RequestResult.append("None"); 
                }
                else
                {
                	String complaintsIds = sb.toString().substring(0, sb.toString().length()-1);
                	RequestResult.append(complaintsIds);
                }
			}
			
			else if(tokens2[0].contentEquals("response") && tokens2[1].contentEquals("employee"))
			{
				Class.forName(JDBC_DRIVER);
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				
				int complaintId = Integer.parseInt(tokens2[2]);
				String complaintResponse = tokens2[3];
				
				sql = "UPDATE Complaints SET Status = ?, Response = ? WHERE ID = ?";
				preparedStmt = conn.prepareStatement(sql);
				preparedStmt.setInt(1,1);
				preparedStmt.setString(2, complaintResponse);
				preparedStmt.setInt(3,complaintId);
				preparedStmt.executeUpdate();
				
				RequestResult.append(complaintId);
			}
			
			
			
			else if(tokens[0].contentEquals("privilege") && tokens[1].contentEquals("employee"))
			{
				Class.forName(JDBC_DRIVER);
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				
				String employeePrivilege = tokens[2];
				String employeeName = tokens[3] + " " + tokens[4];
				String privileges = "";
				
				sql = "SELECT * FROM Employees WHERE Name = ?";
	 			preparedStmt = conn.prepareStatement(sql);
	 			preparedStmt.setString(1,employeeName);
                rs = preparedStmt.executeQuery();
                
                while (rs.next()) {
                	privileges = rs.getString("Privileges");
				}
                
                String[] temp = privileges.trim().split("\\,");
                
                int flag = 0;
                
                for(int i = 0; i < temp.length; i++)
                {
                	if(employeePrivilege.contentEquals(temp[i]))
                	{
                		flag = 1;
                		break;
                	}
                }
                if(flag == 1)
                {
                	RequestResult.append("OK");
                }
			}
			
			else if(tokens[0].contentEquals("get") && tokens[1].contentEquals("employee"))
			{
				Class.forName(JDBC_DRIVER);
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				
				int employeeId = Integer.parseInt(tokens[2]);
				
				String name = "";
				String priviliges = "";
				String store = "";
				
				sql = "SELECT * FROM Employees WHERE ID = ?";
	 			preparedStmt = conn.prepareStatement(sql);
	 			preparedStmt.setInt(1,employeeId);
                rs = preparedStmt.executeQuery();
                
                while (rs.next()) {
                	name = rs.getString("Name");
                	priviliges = rs.getString("Privileges");
                	store = rs.getString("StoreName");
				}
                
                String d = name + "-" + priviliges + "-" + store;
                
                RequestResult.append(d);
			}
			
			else if(tokens[0].contentEquals("get") && tokens[1].contentEquals("customer"))
			{
				Class.forName(JDBC_DRIVER);
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				
				int customerId = Integer.parseInt(tokens[2]);
				
				String name = "";
		        String store = "";
		        String creditCardNumber = "";
		        String expirationDate = "";
		        String subscription = "";
				
				sql = "SELECT * FROM Customers WHERE ID = ?";
	 			preparedStmt = conn.prepareStatement(sql);
	 			preparedStmt.setInt(1,customerId);
                rs = preparedStmt.executeQuery();
                
                while (rs.next()) {
                	name = rs.getString("Name");
                	store = rs.getString("StoreName");
                	creditCardNumber = rs.getString("CreditCardNumber");
    		        expirationDate = rs.getString("ExpirationDate");
    		        subscription = rs.getString("SubscriptionType");
				}
              
                String d = name + "-" + store + "-" + creditCardNumber
                		   + "-" + expirationDate + "-" + subscription;
                
                RequestResult.append(d);
			}
			
			else if(tokens[0].contentEquals("get") && tokens[1].contentEquals("product"))
			{
				Class.forName(JDBC_DRIVER);
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				
				int productId = Integer.parseInt(tokens[2]);
				
				String name = "";
				double price = -1;
				String type = "";
				
				sql = "SELECT * FROM CatalogProducts WHERE ID = ?";
	 			preparedStmt = conn.prepareStatement(sql);
	 			preparedStmt.setInt(1,productId);
                rs = preparedStmt.executeQuery();
                
                while (rs.next()) {
                	name = rs.getString("Name");
    				price = rs.getDouble("Price");
    				type = rs.getString("Type");
				}
                
                RequestResult.append(name + "," + price + "," + type);
			}
			
			else if(tokens[0].contentEquals("get") && tokens[1].contentEquals("ids"))
			{
				Class.forName(JDBC_DRIVER);
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				
				int id;
	
				sql = "SELECT * FROM CatalogProducts";
	 			preparedStmt = conn.prepareStatement(sql);
                rs = preparedStmt.executeQuery();
				
				while (rs.next()) {
					id= rs.getInt("ID");
					RequestResult.append(Integer.toString(id) + ",");
				}
			}
			
			else if(tokens[0].contentEquals("get") && tokens[1].contentEquals("emp"))
			{
				Class.forName(JDBC_DRIVER);
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				
				int id;
				String managerName = tokens[2] + " " + tokens[3];
				String storeName = "";
				
				sql = "SELECT * FROM Managers WHERE Name = ?";
	 			preparedStmt = conn.prepareStatement(sql);
	 			preparedStmt.setString(1,managerName);
                rs = preparedStmt.executeQuery();
				
				while (rs.next()) {
					storeName= rs.getString("StoreName");
				}
				
				if(storeName.contentEquals("LilacHaifa") == true)
				{
					preparedStmt.close();
					sql = "SELECT * FROM Employees WHERE StoreName = ?";
		 			preparedStmt = conn.prepareStatement(sql);
		 			preparedStmt.setString(1,storeName);
	                rs = preparedStmt.executeQuery();
					
					while (rs.next()) {
						id= rs.getInt("ID");
						RequestResult.append(Integer.toString(id) + ",");
					}	
				}
				
				else if(storeName.contentEquals("LilacTelAviv") == true)
				{
					rs.close();
					preparedStmt.close();
					sql = "SELECT * FROM Employees WHERE StoreName = ?";
		 			preparedStmt = conn.prepareStatement(sql);
		 			preparedStmt.setString(1,storeName);
	                rs = preparedStmt.executeQuery();
					
					while (rs.next()) {
						id= rs.getInt("ID");
						RequestResult.append(Integer.toString(id) + ",");
					}	
				}
				
				else
				{
					rs.close();
					preparedStmt.close();
					sql = "SELECT * FROM Employees";
		 			preparedStmt = conn.prepareStatement(sql);
	                rs = preparedStmt.executeQuery();
					
					while (rs.next()) {
						id= rs.getInt("ID");
						RequestResult.append(Integer.toString(id) + ",");
					}				
	 			}
	
			}
			
			else if(tokens[0].contentEquals("get") && tokens[1].contentEquals("cus"))
			{
				Class.forName(JDBC_DRIVER);
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				
				int id;
				String managerName = tokens[2] + " " + tokens[3];
				String storeName = "";
				
				sql = "SELECT * FROM Managers WHERE Name = ?";
	 			preparedStmt = conn.prepareStatement(sql);
	 			preparedStmt.setString(1,managerName);
                rs = preparedStmt.executeQuery();
				
				while (rs.next()) {
					storeName= rs.getString("StoreName");
				}
				
				if(storeName.contentEquals("LilacHaifa") == true)
				{
					preparedStmt.close();
					sql = "SELECT * FROM Customers WHERE StoreName = ? AND Balance < ?";
		 			preparedStmt = conn.prepareStatement(sql);
		 			preparedStmt.setString(1,storeName);
		 			preparedStmt.setInt(2,0);
	                rs = preparedStmt.executeQuery();
					
					while (rs.next()) {
						id= rs.getInt("ID");
						RequestResult.append(Integer.toString(id) + ",");
					}	
				}
				
				else if(storeName.contentEquals("LilacTelAviv") == true)
				{
					rs.close();
					preparedStmt.close();
					sql = "SELECT * FROM Customers WHERE StoreName = ? AND Balance < ?";
		 			preparedStmt = conn.prepareStatement(sql);
		 			preparedStmt.setString(1,storeName);
		 			preparedStmt.setInt(2,0);
	                rs = preparedStmt.executeQuery();
					
					while (rs.next()) {
						id= rs.getInt("ID");
						RequestResult.append(Integer.toString(id) + ",");
					}	
				}
				
				else
				{
					rs.close();
					preparedStmt.close();
					sql = "SELECT * FROM Customers WHERE Balance < ?";
		 			preparedStmt = conn.prepareStatement(sql);
		 			preparedStmt.setInt(1,0);
	                rs = preparedStmt.executeQuery();
					
					while (rs.next()) {
						id= rs.getInt("ID");
						RequestResult.append(Integer.toString(id) + ",");
					}				
	 			}
	
			}
			
			else if(tokens[0].contentEquals("update") && tokens[1].contentEquals("product"))
			{
				Class.forName(JDBC_DRIVER);
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				
				int id = Integer.parseInt(tokens[2]);
				String name = tokens[3];
				String price = tokens[4];
				String type = tokens[5];
				int count = 0;
				
				if(name.contentEquals("-1") == true && price.contentEquals("-1") == true &&
				   type.contentEquals("-1") == false)
				{
					sql = "UPDATE CatalogProducts SET Type = ? WHERE ID = ?";
					preparedStmt = conn.prepareStatement(sql);
					preparedStmt.setString(1,type);
					preparedStmt.setInt(2,id);
					preparedStmt.executeUpdate();
					preparedStmt.close();
					RequestResult.append("Successful Update");
				}
		  else if(name.contentEquals("-1") == true && price.contentEquals("-1") == false &&
				   type.contentEquals("-1") == true)
				{
					sql = "UPDATE CatalogProducts SET Price = ? WHERE ID = ?";
					preparedStmt = conn.prepareStatement(sql);
					preparedStmt.setDouble(1,Double.parseDouble(price));
					preparedStmt.setInt(2,id);
					preparedStmt.executeUpdate();
					preparedStmt.close();
					RequestResult.append("Successful Update");
				}
		  else if(name.contentEquals("-1") == true && price.contentEquals("-1") == false &&
						   type.contentEquals("-1") == false)
				{
					sql = "UPDATE CatalogProducts SET Price = ?, Type = ? WHERE ID = ?";
					preparedStmt = conn.prepareStatement(sql);
					preparedStmt.setDouble(1,Double.parseDouble(price));
					preparedStmt.setString(2,type);
					preparedStmt.setInt(3,id);
					preparedStmt.executeUpdate();
					preparedStmt.close();
					RequestResult.append("Successful Update");
				}
		  else if(name.contentEquals("-1") == false && price.contentEquals("-1") == true &&
				   type.contentEquals("-1") == true)
				{
					
					sql = "SELECT * FROM CatalogProducts WHERE Name = ?";
		 			preparedStmt = conn.prepareStatement(sql);
		 			preparedStmt.setString(1,name);
	                rs = preparedStmt.executeQuery();
	                
	                while (rs.next()){
	                	count++;
					}
	                preparedStmt.close();
	                
	                if(count == 0)
	                {
	                	sql = "UPDATE CatalogProducts SET Name = ? WHERE ID = ?";
						preparedStmt = conn.prepareStatement(sql);
						preparedStmt.setString(1,name);
						preparedStmt.setInt(2,id);
						preparedStmt.executeUpdate();
						preparedStmt.close();
						RequestResult.append("Successful Update");	
	                }
				}
		  else if(name.contentEquals("-1") == false && price.contentEquals("-1") == true &&
				   type.contentEquals("-1") == false)
				{
					
					sql = "SELECT * FROM CatalogProducts WHERE Name = ?";
		 			preparedStmt = conn.prepareStatement(sql);
		 			preparedStmt.setString(1,name);
	                rs = preparedStmt.executeQuery();
	                
	                while (rs.next()){
	                	count++;
					}
	                preparedStmt.close();
	                
	                if(count == 0)
	                {
	                	sql = "UPDATE CatalogProducts SET Name = ?, Type = ? WHERE ID = ?";
						preparedStmt = conn.prepareStatement(sql);
						preparedStmt.setString(1,name);
						preparedStmt.setString(2,type);
						preparedStmt.setInt(3,id);
						preparedStmt.executeUpdate();
						preparedStmt.close();
						RequestResult.append("Successful Update");	
	                }
				}
		  else if(name.contentEquals("-1") == false && price.contentEquals("-1") == false &&
				   type.contentEquals("-1") == true)
				{
					
					sql = "SELECT * FROM CatalogProducts WHERE Name = ?";
		 			preparedStmt = conn.prepareStatement(sql);
		 			preparedStmt.setString(1,name);
	                rs = preparedStmt.executeQuery();
	                
	                while (rs.next()){
	                	count++;
					}
	                preparedStmt.close();
	                
	                if(count == 0)
	                {
	                	sql = "UPDATE CatalogProducts SET Name = ?, Price = ? WHERE ID = ?";
						preparedStmt = conn.prepareStatement(sql);
						preparedStmt.setString(1,name);
						preparedStmt.setDouble(2,Double.parseDouble(price));
						preparedStmt.setInt(3,id);
						preparedStmt.executeUpdate();
						preparedStmt.close();
						RequestResult.append("Successful Update");	
	                }
				}
		  else if(name.contentEquals("-1") == false && price.contentEquals("-1") == false &&
				   type.contentEquals("-1") == false)
				{
					
					sql = "SELECT * FROM CatalogProducts WHERE Name = ?";
		 			preparedStmt = conn.prepareStatement(sql);
		 			preparedStmt.setString(1,name);
	                rs = preparedStmt.executeQuery();
	                
	                while (rs.next()){
	                	count++;
					}
	                preparedStmt.close();
	                
	                if(count == 0)
	                {
	                	sql = "UPDATE CatalogProducts SET Name = ?, Price = ?, Type = ? WHERE ID = ?";
						preparedStmt = conn.prepareStatement(sql);
						preparedStmt.setString(1,name);
						preparedStmt.setDouble(2,Double.parseDouble(price));
						preparedStmt.setString(3,type);
						preparedStmt.setInt(4,id);
						preparedStmt.executeUpdate();
						preparedStmt.close();
						RequestResult.append("Successful Update");	
	                }
				}
			}
			
			else if(tokens[0].contentEquals("add") && tokens[1].contentEquals("product"))
			{
				Class.forName(JDBC_DRIVER);
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				
				String name = tokens[2];
				String price = tokens[3];
				String type = tokens[4];
				int count = 0;
				int idNumber = 0;
				
				sql = "SELECT * FROM CatalogProducts WHERE Name = ?";
	 			preparedStmt = conn.prepareStatement(sql);
	 			preparedStmt.setString(1,name);
                rs = preparedStmt.executeQuery();
                
                while (rs.next()){
                	count++;
				}
                
                if(count == 0)
                {
                	preparedStmt.close();
                	sql = "SELECT * FROM CatalogProducts";
    	 			preparedStmt = conn.prepareStatement(sql);
                    rs = preparedStmt.executeQuery();	
                    
                    while (rs.next()){
                    	idNumber++;
    				}
                    
                    sql = "INSERT INTO CatalogProducts " +
            	            "(ID, Name, Price, Image, Type, Sale) "
            	            + "VALUES (?,?,?,?,?,?)";
            	 		    preparedStmt = conn.prepareStatement(sql);
            	 		    preparedStmt.setInt(1,idNumber+1);
            	 			preparedStmt.setString(2,name);
            	 			preparedStmt.setDouble(3,Double.parseDouble(price));
            	 			preparedStmt.setString(4,"None");
            	 			preparedStmt.setString(5,type);
            	 			preparedStmt.setString(6,"None");
            	 			preparedStmt.executeUpdate();
            	 			
            	 			RequestResult.append(Integer.toString(idNumber+1));
                }
                else
                {
                	RequestResult.append("Unsuccessful Addition");
                }
			}
			
			else if(tokens[0].contentEquals("sale") && tokens[1].contentEquals("product"))
			{
				Class.forName(JDBC_DRIVER);
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				
				int id = Integer.parseInt(tokens[2]);
				int discount = Integer.parseInt(tokens[3]);
				double price = -1;
				
				sql = "SELECT * FROM CatalogProducts WHERE ID = ?";
	 			preparedStmt = conn.prepareStatement(sql);
	 			preparedStmt.setInt(1,id);
                rs = preparedStmt.executeQuery();
                
                while (rs.next()){
                	price = rs.getDouble("Price");
				}
                preparedStmt.close();
				
                double percentage = discount;
                percentage /= 100;
				double newPrice =  Math.floor((1-percentage)*price);
				
				sql = "UPDATE CatalogProducts SET Price = ?, Sale = ? WHERE ID = ?";
				preparedStmt = conn.prepareStatement(sql);
				preparedStmt.setDouble(1,newPrice);
				preparedStmt.setString(2,"(" + discount +"%-Discount)");
				preparedStmt.setInt(3,id);
				preparedStmt.executeUpdate();
				preparedStmt.close();
				RequestResult.append(id);	
			}
			
			else if(tokens[0].contentEquals("get") && tokens[1].contentEquals("histComplaint"))
			{
				Class.forName(JDBC_DRIVER);
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				
				String managerName = tokens[2] + " " + tokens[3];
				String storeName = tokens[4];
				String managerStore = "";
				   
				sql = "SELECT * FROM Managers WHERE Name = ?";
		 	    preparedStmt = conn.prepareStatement(sql);
		 		preparedStmt.setString(1,managerName);
	            rs = preparedStmt.executeQuery();
					
			    while (rs.next()) {
				  managerStore = rs.getString("StoreName");
				}
			    
			    if(storeName.contentEquals("LilacHaifa") == true && managerStore.contentEquals("LilacHaifa") == true)
			    {
			    	preparedStmt.close();
			    	sql = "SELECT * FROM ComplaintsReport WHERE StoreName = ?";
			 	    preparedStmt = conn.prepareStatement(sql);
			 		preparedStmt.setString(1,"LilacHaifa");
		            rs = preparedStmt.executeQuery();
		            int m1=-1,m2=-1,m3=-1,m4=-1,m5=-1,m6=-1,m7=-1,m8=-1,m9=-1,m10=-1,m11=-1,m12=-1;
		            int q1,q2,q3,q4;
						
				    while (rs.next()) {
					  m1 = rs.getInt("JanuaryComplaints");
					  m2 = rs.getInt("FebruaryComplaints");
					  m3 = rs.getInt("MarchComplaints");
					  m4 = rs.getInt("AprilComplaints");
					  m5 = rs.getInt("MayComplaints");
					  m6 = rs.getInt("JuneComplaints");
					  m7 = rs.getInt("JulyComplaints");
					  m8 = rs.getInt("AugustComplaints");
					  m9 = rs.getInt("SeptemberComplaints");
					  m10 = rs.getInt("OctoberComplaints");
					  m11 = rs.getInt("NovemberComplaints");
					  m12 = rs.getInt("DecemberComplaints");
				    }
				    
				    q1 = m1+m2+m3;
				    q2 = m4+m5+m6;
				    q3 = m7+m8+m9;
				    q4 = m10+m11+m12;
				    
				    String q = q1 + "-" + q2 + "-" + q3 + "-" + q4;
				    RequestResult.append(q);	
			    }
			    else if(storeName.contentEquals("LilacTelAviv") == true && managerStore.contentEquals("LilacTelAviv") == true)
			    {
			    	rs.close();
			    	preparedStmt.close();
			    	sql = "SELECT * FROM ComplaintsReport WHERE StoreName = ?";
			 	    preparedStmt = conn.prepareStatement(sql);
			 		preparedStmt.setString(1,"LilacTelAviv");
		            rs = preparedStmt.executeQuery();
		            int m1=-1,m2=-1,m3=-1,m4=-1,m5=-1,m6=-1,m7=-1,m8=-1,m9=-1,m10=-1,m11=-1,m12=-1;
		            int q1,q2,q3,q4;
						
				    while (rs.next()) {
					  m1 = rs.getInt("JanuaryComplaints");
					  m2 = rs.getInt("FebruaryComplaints");
					  m3 = rs.getInt("MarchComplaints");
					  m4 = rs.getInt("AprilComplaints");
					  m5 = rs.getInt("MayComplaints");
					  m6 = rs.getInt("JuneComplaints");
					  m7 = rs.getInt("JulyComplaints");
					  m8 = rs.getInt("AugustComplaints");
					  m9 = rs.getInt("SeptemberComplaints");
					  m10 = rs.getInt("OctoberComplaints");
					  m11 = rs.getInt("NovemberComplaints");
					  m12 = rs.getInt("DecemberComplaints");
				    }
				    
				    q1 = m1+m2+m3;
				    q2 = m4+m5+m6;
				    q3 = m7+m8+m9;
				    q4 = m10+m11+m12;
				    
				    String q = q1 + "-" + q2 + "-" + q3 + "-" + q4;
				    RequestResult.append(q);	
			    }
			    else if(storeName.contentEquals("All") == true && managerStore.contentEquals("All") == true)
			    {
			    	rs.close();
			    	preparedStmt.close();
			    	sql = "SELECT * FROM ComplaintsReport WHERE StoreName = ?";
			 	    preparedStmt = conn.prepareStatement(sql);
			 		preparedStmt.setString(1,"LilacHaifa");
		            rs = preparedStmt.executeQuery();
		            int m1=-1,m2=-1,m3=-1,m4=-1,m5=-1,m6=-1,m7=-1,m8=-1,m9=-1,m10=-1,m11=-1,m12=-1;
		            int q1,q2,q3,q4;
						
				    while (rs.next()) {
					  m1 = rs.getInt("JanuaryComplaints");
					  m2 = rs.getInt("FebruaryComplaints");
					  m3 = rs.getInt("MarchComplaints");
					  m4 = rs.getInt("AprilComplaints");
					  m5 = rs.getInt("MayComplaints");
					  m6 = rs.getInt("JuneComplaints");
					  m7 = rs.getInt("JulyComplaints");
					  m8 = rs.getInt("AugustComplaints");
					  m9 = rs.getInt("SeptemberComplaints");
					  m10 = rs.getInt("OctoberComplaints");
					  m11 = rs.getInt("NovemberComplaints");
					  m12 = rs.getInt("DecemberComplaints");
				    }
				    
				    q1 = m1+m2+m3;
				    q2 = m4+m5+m6;
				    q3 = m7+m8+m9;
				    q4 = m10+m11+m12;
				    
				    rs.close();
			    	preparedStmt.close();
			    	sql = "SELECT * FROM ComplaintsReport WHERE StoreName = ?";
			 	    preparedStmt = conn.prepareStatement(sql);
			 		preparedStmt.setString(1,"LilacTelAviv");
		            rs = preparedStmt.executeQuery();
				    
		            while (rs.next()) {
						  m1 = rs.getInt("JanuaryComplaints");
						  m2 = rs.getInt("FebruaryComplaints");
						  m3 = rs.getInt("MarchComplaints");
						  m4 = rs.getInt("AprilComplaints");
						  m5 = rs.getInt("MayComplaints");
						  m6 = rs.getInt("JuneComplaints");
						  m7 = rs.getInt("JulyComplaints");
						  m8 = rs.getInt("AugustComplaints");
						  m9 = rs.getInt("SeptemberComplaints");
						  m10 = rs.getInt("OctoberComplaints");
						  m11 = rs.getInt("NovemberComplaints");
						  m12 = rs.getInt("DecemberComplaints");
				    }
		            
		            q1 += m1;
		            q1 += m2;
		            q1 += m3;
		            q2 += m4;
		            q2 += m5;
		            q2 += m6;
		            q3 += m7;
		            q3 += m8;
		            q3 += m9;
		            q4 += m10;
		            q4 += m11;
		            q4 += m12;
		            
				    String q = q1 + "-" + q2 + "-" + q3 + "-" + q4;
				    RequestResult.append(q);	
			    }
			    else if(storeName.contentEquals("LilacHaifa") == true && managerStore.contentEquals("All") == true)
			    {
			    	rs.close();
			    	preparedStmt.close();
			    	sql = "SELECT * FROM ComplaintsReport WHERE StoreName = ?";
			 	    preparedStmt = conn.prepareStatement(sql);
			 		preparedStmt.setString(1,"LilacHaifa");
		            rs = preparedStmt.executeQuery();
		            int m1=-1,m2=-1,m3=-1,m4=-1,m5=-1,m6=-1,m7=-1,m8=-1,m9=-1,m10=-1,m11=-1,m12=-1;
		            int q1,q2,q3,q4;
						
				    while (rs.next()) {
					  m1 = rs.getInt("JanuaryComplaints");
					  m2 = rs.getInt("FebruaryComplaints");
					  m3 = rs.getInt("MarchComplaints");
					  m4 = rs.getInt("AprilComplaints");
					  m5 = rs.getInt("MayComplaints");
					  m6 = rs.getInt("JuneComplaints");
					  m7 = rs.getInt("JulyComplaints");
					  m8 = rs.getInt("AugustComplaints");
					  m9 = rs.getInt("SeptemberComplaints");
					  m10 = rs.getInt("OctoberComplaints");
					  m11 = rs.getInt("NovemberComplaints");
					  m12 = rs.getInt("DecemberComplaints");
				    }
				    
				    q1 = m1+m2+m3;
				    q2 = m4+m5+m6;
				    q3 = m7+m8+m9;
				    q4 = m10+m11+m12;
				    
				    String q = q1 + "-" + q2 + "-" + q3 + "-" + q4;
				    RequestResult.append(q);	
			    }
			    else if(storeName.contentEquals("LilacTelAviv") == true && managerStore.contentEquals("All") == true)
			    {
			    	rs.close();
			    	preparedStmt.close();
			    	sql = "SELECT * FROM ComplaintsReport WHERE StoreName = ?";
			 	    preparedStmt = conn.prepareStatement(sql);
			 		preparedStmt.setString(1,"LilacTelAviv");
		            rs = preparedStmt.executeQuery();
		            int m1=-1,m2=-1,m3=-1,m4=-1,m5=-1,m6=-1,m7=-1,m8=-1,m9=-1,m10=-1,m11=-1,m12=-1;
		            int q1,q2,q3,q4;
						
				    while (rs.next()) {
					  m1 = rs.getInt("JanuaryComplaints");
					  m2 = rs.getInt("FebruaryComplaints");
					  m3 = rs.getInt("MarchComplaints");
					  m4 = rs.getInt("AprilComplaints");
					  m5 = rs.getInt("MayComplaints");
					  m6 = rs.getInt("JuneComplaints");
					  m7 = rs.getInt("JulyComplaints");
					  m8 = rs.getInt("AugustComplaints");
					  m9 = rs.getInt("SeptemberComplaints");
					  m10 = rs.getInt("OctoberComplaints");
					  m11 = rs.getInt("NovemberComplaints");
					  m12 = rs.getInt("DecemberComplaints");
				    }
				    
				    q1 = m1+m2+m3;
				    q2 = m4+m5+m6;
				    q3 = m7+m8+m9;
				    q4 = m10+m11+m12;
				    
				    String q = q1 + "-" + q2 + "-" + q3 + "-" + q4;
				    RequestResult.append(q);	
			    }
			    else
			    {
			    	RequestResult.append("Unsuccessful Complaint Histogram");
			    }
			}
			
			else if(tokens[0].contentEquals("get") && tokens[1].contentEquals("comp"))
			{
				Class.forName(JDBC_DRIVER);
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				
				String d = "";
				int m1 = -1,m2 = -1;
				String managerName = tokens[2] + " " + tokens[3];
				String storeName1 = tokens[4];
				String storeName2 = tokens[5];
				
				String month1 = tokens[6];
				
				if(month1.contentEquals("1") == true)
				{
					month1 = "JanuaryComplaints";
				}
				else if(month1.contentEquals("2") == true)
				{
					month1 = "FebruaryComplaints";
				}
				else if(month1.contentEquals("3") == true)
				{
					month1 = "MarchComplaints";
				}
				else if(month1.contentEquals("4") == true)
				{
					month1 = "AprilComplaints";
				}
				else if(month1.contentEquals("5") == true)
				{
					month1 = "MayComplaints";
				}
				else if(month1.contentEquals("6") == true)
				{
					month1 = "JuneComplaints";
				}
				else if(month1.contentEquals("7") == true)
				{
					month1 = "JulyComplaints";
				}
				else if(month1.contentEquals("8") == true)
				{
					month1 = "AugustComplaints";
				}
				else if(month1.contentEquals("9") == true)
				{
					month1 = "SeptemberComplaints";
				}
				else if(month1.contentEquals("10") == true)
				{
					month1 = "OctoberComplaints";
				}
				else if(month1.contentEquals("11") == true)
				{
					month1 = "NovemberComplaints";
				}
				else if(month1.contentEquals("12") == true)
				{
					month1 = "DecemberComplaints";
				}
				
				String month2 = tokens[7];
				
				if(month2.contentEquals("1") == true)
				{
					month2 = "JanuaryComplaints";
				}
				else if(month2.contentEquals("2") == true)
				{
					month2 = "FebruaryComplaints";
				}
				else if(month2.contentEquals("3") == true)
				{
					month2 = "MarchComplaints";
				}
				else if(month2.contentEquals("4") == true)
				{
					month2 = "AprilComplaints";
				}
				else if(month2.contentEquals("5") == true)
				{
					month2 = "MayComplaints";
				}
				else if(month2.contentEquals("6") == true)
				{
					month2 = "JuneComplaints";
				}
				else if(month2.contentEquals("7") == true)
				{
					month2 = "JulyComplaints";
				}
				else if(month2.contentEquals("8") == true)
				{
					month2 = "AugustComplaints";
				}
				else if(month2.contentEquals("9") == true)
				{
					month2 = "SeptemberComplaints";
				}
				else if(month2.contentEquals("10") == true)
				{
					month2 = "OctoberComplaints";
				}
				else if(month2.contentEquals("11") == true)
				{
					month2 = "NovemberComplaints";
				}
				else if(month2.contentEquals("12") == true)
				{
					month2 = "DecemberComplaints";
				}
				
				String managerStore = "";
				
				sql = "SELECT * FROM Managers WHERE Name = ?";
		 	    preparedStmt = conn.prepareStatement(sql);
		 		preparedStmt.setString(1,managerName);
	            rs = preparedStmt.executeQuery();
					
			    while (rs.next()) {
				  managerStore = rs.getString("StoreName");
				}
				
			    if(managerStore.contentEquals("LilacHaifa"))
			    {
			       if(storeName1.contentEquals("LilacHaifa") == false || storeName2.contentEquals("LilacHaifa") == false)
			       {
			    	   RequestResult.append("Unsuccessful Complaint Comparison");
			       }
			       else
			       {
			    	    preparedStmt.close();
			    	    sql = "SELECT * FROM ComplaintsReport WHERE StoreName = ?";
				 	    preparedStmt = conn.prepareStatement(sql);
				 		preparedStmt.setString(1,"LilacHaifa");
			            rs = preparedStmt.executeQuery();
							
					      while (rs.next()) {
						  m1 = rs.getInt(month1);
						  m2 = rs.getInt(month2);
					    }
					    d = m1 + "-" + m2; 
						RequestResult.append(d);  
			       }
			    }
			    
			    else if(managerStore.contentEquals("LilacTelAviv"))
			    {
			    	if(storeName1.contentEquals("LilacTelAviv") == false || storeName2.contentEquals("LilacTelAviv") == false)
				       {
			    		 RequestResult.append("Unsuccessful Complaint Comparison");
				       }
				       else
				       {
				    	    preparedStmt.close();
				    	    rs.close();
				    	    sql = "SELECT * FROM ComplaintsReport WHERE StoreName = ?";
					 	    preparedStmt = conn.prepareStatement(sql);
					 		preparedStmt.setString(1,"LilacTelAviv");
				            rs = preparedStmt.executeQuery();
								
						      while (rs.next()) {
							  m1 = rs.getInt(month1);
							  m2 = rs.getInt(month2);
						    }
						    d = m1 + "-" + m2; 
							RequestResult.append(d);  
				       }
			    }
			    
			    else if(managerStore.contentEquals("All"))
			    {
			      if(storeName1.contentEquals("LilacTelAviv") == true && storeName2.contentEquals("LilacTelAviv") == true)
			      {
			    	  preparedStmt.close();
			    	    rs.close();
			    	    sql = "SELECT * FROM ComplaintsReport WHERE StoreName = ?";
				 	    preparedStmt = conn.prepareStatement(sql);
				 		preparedStmt.setString(1,"LilacTelAviv");
			            rs = preparedStmt.executeQuery();
							
					      while (rs.next()) {
						  m1 = rs.getInt(month1);
						  m2 = rs.getInt(month2);
					    }
					    d = m1 + "-" + m2; 
						RequestResult.append(d);  
			      }
			      else if(storeName1.contentEquals("LilacTelAviv") == true && storeName2.contentEquals("LilacHaifa") == true)
			      {
			    	    preparedStmt.close();
			    	    rs.close();
			    	    sql = "SELECT * FROM ComplaintsReport WHERE StoreName = ?";
				 	    preparedStmt = conn.prepareStatement(sql);
				 		preparedStmt.setString(1,"LilacTelAviv");
			            rs = preparedStmt.executeQuery();
							
					      while (rs.next()) {
						  m1 = rs.getInt(month1);
					    }
					    
					      preparedStmt.close();
				    	    rs.close();
				    	    sql = "SELECT * FROM ComplaintsReport WHERE StoreName = ?";
					 	    preparedStmt = conn.prepareStatement(sql);
					 		preparedStmt.setString(1,"LilacHaifa");
				            rs = preparedStmt.executeQuery();
								
						      while (rs.next()) {
							  m2 = rs.getInt(month2);
						    }
					      
					    d = m1 + "-" + m2; 
						RequestResult.append(d);  
			      }
			      else if(storeName1.contentEquals("LilacHaifa") == true && storeName2.contentEquals("LilacHaifa") == true)
			      {
			    	  preparedStmt.close();
			    	    rs.close();
			    	    sql = "SELECT * FROM ComplaintsReport WHERE StoreName = ?";
				 	    preparedStmt = conn.prepareStatement(sql);
				 		preparedStmt.setString(1,"LilacHaifa");
			            rs = preparedStmt.executeQuery();
							
					      while (rs.next()) {
						  m1 = rs.getInt(month1);
						  m2 = rs.getInt(month2);
					    }
					    d = m1 + "-" + m2; 
						RequestResult.append(d);  
			      }
			      else if(storeName1.contentEquals("LilacHaifa") == true && storeName2.contentEquals("LilacTelAviv") == true)
			      {
			    	  preparedStmt.close();
			    	    rs.close();
			    	    sql = "SELECT * FROM ComplaintsReport WHERE StoreName = ?";
				 	    preparedStmt = conn.prepareStatement(sql);
				 		preparedStmt.setString(1,"LilacHaifa");
			            rs = preparedStmt.executeQuery();
							
					      while (rs.next()) {
						  m1 = rs.getInt(month1);
					    }
					    
					      preparedStmt.close();
				    	    rs.close();
				    	    sql = "SELECT * FROM ComplaintsReport WHERE StoreName = ?";
					 	    preparedStmt = conn.prepareStatement(sql);
					 		preparedStmt.setString(1,"LilacTelAviv");
				            rs = preparedStmt.executeQuery();
								
						      while (rs.next()) {
							  m2 = rs.getInt(month2);
						    }
					      
					    d = m1 + "-" + m2; 
						RequestResult.append(d); 
			      }
			    }
			}
			  
			else if(tokens[0].contentEquals("get") && tokens[1].contentEquals("histEarnings"))
			{
				Class.forName(JDBC_DRIVER);
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				
				String managerName = tokens[2] + " " + tokens[3];
				String storeName = tokens[4];
				String managerStore = "";
				   
				sql = "SELECT * FROM Managers WHERE Name = ?";
		 	    preparedStmt = conn.prepareStatement(sql);
		 		preparedStmt.setString(1,managerName);
	            rs = preparedStmt.executeQuery();
					
			    while (rs.next()) {
				  managerStore = rs.getString("StoreName");
				}
			    
			    if(storeName.contentEquals("LilacHaifa") == true && managerStore.contentEquals("LilacHaifa") == true)
			    {
			    	preparedStmt.close();
			    	sql = "SELECT * FROM MonthlyEarnings WHERE StoreName = ?";
			 	    preparedStmt = conn.prepareStatement(sql);
			 		preparedStmt.setString(1,"LilacHaifa");
		            rs = preparedStmt.executeQuery();
		            int m1=-1,m2=-1,m3=-1,m4=-1,m5=-1,m6=-1,m7=-1,m8=-1,m9=-1,m10=-1,m11=-1,m12=-1;
		            int q1,q2,q3,q4;
						
				    while (rs.next()) {
					  m1 = rs.getInt("JanuaryEarnings");
					  m2 = rs.getInt("FebruaryEarnings");
					  m3 = rs.getInt("MarchEarnings");
					  m4 = rs.getInt("AprilEarnings");
					  m5 = rs.getInt("MayEarnings");
					  m6 = rs.getInt("JuneEarnings");
					  m7 = rs.getInt("JulyEarnings");
					  m8 = rs.getInt("AugustEarnings");
					  m9 = rs.getInt("SeptemberEarnings");
					  m10 = rs.getInt("OctoberEarnings");
					  m11 = rs.getInt("NovemberEarnings");
					  m12 = rs.getInt("DecemberEarnings");
				    }
				    
				    q1 = m1+m2+m3;
				    q2 = m4+m5+m6;
				    q3 = m7+m8+m9;
				    q4 = m10+m11+m12;
				    
				    String q = q1 + "-" + q2 + "-" + q3 + "-" + q4;
				    RequestResult.append(q);	
			    }
			    else if(storeName.contentEquals("LilacTelAviv") == true && managerStore.contentEquals("LilacTelAviv") == true)
			    {
			    	rs.close();
			    	preparedStmt.close();
			    	sql = "SELECT * FROM MonthlyEarnings WHERE StoreName = ?";
			 	    preparedStmt = conn.prepareStatement(sql);
			 		preparedStmt.setString(1,"LilacTelAviv");
		            rs = preparedStmt.executeQuery();
		            int m1=-1,m2=-1,m3=-1,m4=-1,m5=-1,m6=-1,m7=-1,m8=-1,m9=-1,m10=-1,m11=-1,m12=-1;
		            int q1,q2,q3,q4;
						
				    while (rs.next()) {
				    	m1 = rs.getInt("JanuaryEarnings");
					    m2 = rs.getInt("FebruaryEarnings");
					    m3 = rs.getInt("MarchEarnings");
						m4 = rs.getInt("AprilEarnings");
						m5 = rs.getInt("MayEarnings");
						m6 = rs.getInt("JuneEarnings");
						m7 = rs.getInt("JulyEarnings");
						m8 = rs.getInt("AugustEarnings");
						m9 = rs.getInt("SeptemberEarnings");
						m10 = rs.getInt("OctoberEarnings");
						m11 = rs.getInt("NovemberEarnings");
					    m12 = rs.getInt("DecemberEarnings");
				    }
				    
				    q1 = m1+m2+m3;
				    q2 = m4+m5+m6;
				    q3 = m7+m8+m9;
				    q4 = m10+m11+m12;
				    
				    String q = q1 + "-" + q2 + "-" + q3 + "-" + q4;
				    RequestResult.append(q);	
			    }
			    else if(storeName.contentEquals("All") == true && managerStore.contentEquals("All") == true)
			    {
			    	rs.close();
			    	preparedStmt.close();
			    	sql = "SELECT * FROM MonthlyEarnings WHERE StoreName = ?";
			 	    preparedStmt = conn.prepareStatement(sql);
			 		preparedStmt.setString(1,"LilacHaifa");
		            rs = preparedStmt.executeQuery();
		            int m1=-1,m2=-1,m3=-1,m4=-1,m5=-1,m6=-1,m7=-1,m8=-1,m9=-1,m10=-1,m11=-1,m12=-1;
		            int q1,q2,q3,q4;
						
				    while (rs.next()) {
				    	m1 = rs.getInt("JanuaryEarnings");
					    m2 = rs.getInt("FebruaryEarnings");
					    m3 = rs.getInt("MarchEarnings");
						m4 = rs.getInt("AprilEarnings");
						m5 = rs.getInt("MayEarnings");
						m6 = rs.getInt("JuneEarnings");
						m7 = rs.getInt("JulyEarnings");
						m8 = rs.getInt("AugustEarnings");
						m9 = rs.getInt("SeptemberEarnings");
						m10 = rs.getInt("OctoberEarnings");
						m11 = rs.getInt("NovemberEarnings");
					    m12 = rs.getInt("DecemberEarnings");
				    }
				    
				    q1 = m1+m2+m3;
				    q2 = m4+m5+m6;
				    q3 = m7+m8+m9;
				    q4 = m10+m11+m12;
				    
				    rs.close();
			    	preparedStmt.close();
			    	sql = "SELECT * FROM MonthlyEarnings WHERE StoreName = ?";
			 	    preparedStmt = conn.prepareStatement(sql);
			 		preparedStmt.setString(1,"LilacTelAviv");
		            rs = preparedStmt.executeQuery();
				    
		            while (rs.next()) {
		            	m1 = rs.getInt("JanuaryEarnings");
					    m2 = rs.getInt("FebruaryEarnings");
					    m3 = rs.getInt("MarchEarnings");
						m4 = rs.getInt("AprilEarnings");
						m5 = rs.getInt("MayEarnings");
						m6 = rs.getInt("JuneEarnings");
						m7 = rs.getInt("JulyEarnings");
						m8 = rs.getInt("AugustEarnings");
						m9 = rs.getInt("SeptemberEarnings");
						m10 = rs.getInt("OctoberEarnings");
						m11 = rs.getInt("NovemberEarnings");
					    m12 = rs.getInt("DecemberEarnings");
				    }
		            
		            q1 += m1;
		            q1 += m2;
		            q1 += m3;
		            q2 += m4;
		            q2 += m5;
		            q2 += m6;
		            q3 += m7;
		            q3 += m8;
		            q3 += m9;
		            q4 += m10;
		            q4 += m11;
		            q4 += m12;
		            
				    String q = q1 + "-" + q2 + "-" + q3 + "-" + q4;
				    RequestResult.append(q);	
			    }
			    else if(storeName.contentEquals("LilacHaifa") == true && managerStore.contentEquals("All") == true)
			    {
			    	rs.close();
			    	preparedStmt.close();
			    	sql = "SELECT * FROM MonthlyEarnings WHERE StoreName = ?";
			 	    preparedStmt = conn.prepareStatement(sql);
			 		preparedStmt.setString(1,"LilacHaifa");
		            rs = preparedStmt.executeQuery();
		            int m1=-1,m2=-1,m3=-1,m4=-1,m5=-1,m6=-1,m7=-1,m8=-1,m9=-1,m10=-1,m11=-1,m12=-1;
		            int q1,q2,q3,q4;
						
				    while (rs.next()) {
				    	m1 = rs.getInt("JanuaryEarnings");
					    m2 = rs.getInt("FebruaryEarnings");
					    m3 = rs.getInt("MarchEarnings");
						m4 = rs.getInt("AprilEarnings");
						m5 = rs.getInt("MayEarnings");
						m6 = rs.getInt("JuneEarnings");
						m7 = rs.getInt("JulyEarnings");
						m8 = rs.getInt("AugustEarnings");
						m9 = rs.getInt("SeptemberEarnings");
						m10 = rs.getInt("OctoberEarnings");
						m11 = rs.getInt("NovemberEarnings");
					    m12 = rs.getInt("DecemberEarnings");
				    }
				    
				    q1 = m1+m2+m3;
				    q2 = m4+m5+m6;
				    q3 = m7+m8+m9;
				    q4 = m10+m11+m12;
				    
				    String q = q1 + "-" + q2 + "-" + q3 + "-" + q4;
				    RequestResult.append(q);	
			    }
			    else if(storeName.contentEquals("LilacTelAviv") == true && managerStore.contentEquals("All") == true)
			    {
			    	rs.close();
			    	preparedStmt.close();
			    	sql = "SELECT * FROM MonthlyEarnings WHERE StoreName = ?";
			 	    preparedStmt = conn.prepareStatement(sql);
			 		preparedStmt.setString(1,"LilacTelAviv");
		            rs = preparedStmt.executeQuery();
		            int m1=-1,m2=-1,m3=-1,m4=-1,m5=-1,m6=-1,m7=-1,m8=-1,m9=-1,m10=-1,m11=-1,m12=-1;
		            int q1,q2,q3,q4;
						
				    while (rs.next()) {
				    	m1 = rs.getInt("JanuaryEarnings");
					    m2 = rs.getInt("FebruaryEarnings");
					    m3 = rs.getInt("MarchEarnings");
						m4 = rs.getInt("AprilEarnings");
						m5 = rs.getInt("MayEarnings");
						m6 = rs.getInt("JuneEarnings");
						m7 = rs.getInt("JulyEarnings");
						m8 = rs.getInt("AugustEarnings");
						m9 = rs.getInt("SeptemberEarnings");
						m10 = rs.getInt("OctoberEarnings");
						m11 = rs.getInt("NovemberEarnings");
					    m12 = rs.getInt("DecemberEarnings");
				    }
				    
				    q1 = m1+m2+m3;
				    q2 = m4+m5+m6;
				    q3 = m7+m8+m9;
				    q4 = m10+m11+m12;
				    
				    String q = q1 + "-" + q2 + "-" + q3 + "-" + q4;
				    RequestResult.append(q);	
			    }
			    else
			    {
			    	RequestResult.append("Unsuccessful Earnings Histogram");
			    }
			}
			
			else if(tokens[0].contentEquals("get") && tokens[1].contentEquals("earn"))
			{
				Class.forName(JDBC_DRIVER);
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				
				String d = "";
				int m1 = -1,m2 = -1;
				String managerName = tokens[2] + " " + tokens[3];
				String storeName1 = tokens[4];
				String storeName2 = tokens[5];
				
				String month1 = tokens[6];
				
				if(month1.contentEquals("1") == true)
				{
					month1 = "JanuaryEarnings";
				}
				else if(month1.contentEquals("2") == true)
				{
					month1 = "FebruaryEarnings";
				}
				else if(month1.contentEquals("3") == true)
				{
					month1 = "MarchEarnings";
				}
				else if(month1.contentEquals("4") == true)
				{
					month1 = "AprilEarnings";
				}
				else if(month1.contentEquals("5") == true)
				{
					month1 = "MayEarnings";
				}
				else if(month1.contentEquals("6") == true)
				{
					month1 = "JuneEarnings";
				}
				else if(month1.contentEquals("7") == true)
				{
					month1 = "JulyEarnings";
				}
				else if(month1.contentEquals("8") == true)
				{
					month1 = "AugustEarnings";
				}
				else if(month1.contentEquals("9") == true)
				{
					month1 = "SeptemberEarnings";
				}
				else if(month1.contentEquals("10") == true)
				{
					month1 = "OctoberEarnings";
				}
				else if(month1.contentEquals("11") == true)
				{
					month1 = "NovemberEarnings";
				}
				else if(month1.contentEquals("12") == true)
				{
					month1 = "DecemberEarnings";
				}
				
				String month2 = tokens[7];
				
				if(month2.contentEquals("1") == true)
				{
					month2 = "JanuaryEarnings";
				}
				else if(month2.contentEquals("2") == true)
				{
					month2 = "FebruaryEarnings";
				}
				else if(month2.contentEquals("3") == true)
				{
					month2 = "MarchEarnings";
				}
				else if(month2.contentEquals("4") == true)
				{
					month2 = "AprilEarnings";
				}
				else if(month2.contentEquals("5") == true)
				{
					month2 = "MayEarnings";
				}
				else if(month2.contentEquals("6") == true)
				{
					month2 = "JuneEarnings";
				}
				else if(month2.contentEquals("7") == true)
				{
					month2 = "JulyEarnings";
				}
				else if(month2.contentEquals("8") == true)
				{
					month2 = "AugustEarnings";
				}
				else if(month2.contentEquals("9") == true)
				{
					month2 = "SeptemberEarnings";
				}
				else if(month2.contentEquals("10") == true)
				{
					month2 = "OctoberEarnings";
				}
				else if(month2.contentEquals("11") == true)
				{
					month2 = "NovemberEarnings";
				}
				else if(month2.contentEquals("12") == true)
				{
					month2 = "DecemberEarnings";
				}
				
				String managerStore = "";
				
				sql = "SELECT * FROM Managers WHERE Name = ?";
		 	    preparedStmt = conn.prepareStatement(sql);
		 		preparedStmt.setString(1,managerName);
	            rs = preparedStmt.executeQuery();
					
			    while (rs.next()) {
				  managerStore = rs.getString("StoreName");
				}
				
			    if(managerStore.contentEquals("LilacHaifa"))
			    {
			       if(storeName1.contentEquals("LilacHaifa") == false || storeName2.contentEquals("LilacHaifa") == false)
			       {
			    	   RequestResult.append("Unsuccessful Earnings Comparison");
			       }
			       else
			       {
			    	    preparedStmt.close();
			    	    sql = "SELECT * FROM MonthlyEarnings WHERE StoreName = ?";
				 	    preparedStmt = conn.prepareStatement(sql);
				 		preparedStmt.setString(1,"LilacHaifa");
			            rs = preparedStmt.executeQuery();
							
					      while (rs.next()) {
						  m1 = rs.getInt(month1);
						  m2 = rs.getInt(month2);
					    }
					    d = m1 + "-" + m2; 
						RequestResult.append(d);  
			       }
			    }
			    
			    else if(managerStore.contentEquals("LilacTelAviv"))
			    {
			    	if(storeName1.contentEquals("LilacTelAviv") == false || storeName2.contentEquals("LilacTelAviv") == false)
				       {
			    		 RequestResult.append("Unsuccessful Earnings Comparison");
				       }
				       else
				       {
				    	    preparedStmt.close();
				    	    rs.close();
				    	    sql = "SELECT * FROM MonthlyEarnings WHERE StoreName = ?";
					 	    preparedStmt = conn.prepareStatement(sql);
					 		preparedStmt.setString(1,"LilacTelAviv");
				            rs = preparedStmt.executeQuery();
								
						      while (rs.next()) {
							  m1 = rs.getInt(month1);
							  m2 = rs.getInt(month2);
						    }
						    d = m1 + "-" + m2; 
							RequestResult.append(d);  
				       }
			    }
			    
			    else if(managerStore.contentEquals("All"))
			    {
			      if(storeName1.contentEquals("LilacTelAviv") == true && storeName2.contentEquals("LilacTelAviv") == true)
			      {
			    	  preparedStmt.close();
			    	    rs.close();
			    	    sql = "SELECT * FROM MonthlyEarnings WHERE StoreName = ?";
				 	    preparedStmt = conn.prepareStatement(sql);
				 		preparedStmt.setString(1,"LilacTelAviv");
			            rs = preparedStmt.executeQuery();
							
					      while (rs.next()) {
						  m1 = rs.getInt(month1);
						  m2 = rs.getInt(month2);
					    }
					    d = m1 + "-" + m2; 
						RequestResult.append(d);  
			      }
			      else if(storeName1.contentEquals("LilacTelAviv") == true && storeName2.contentEquals("LilacHaifa") == true)
			      {
			    	    preparedStmt.close();
			    	    rs.close();
			    	    sql = "SELECT * FROM MonthlyEarnings WHERE StoreName = ?";
				 	    preparedStmt = conn.prepareStatement(sql);
				 		preparedStmt.setString(1,"LilacTelAviv");
			            rs = preparedStmt.executeQuery();
							
					      while (rs.next()) {
						  m1 = rs.getInt(month1);
					    }
					    
					      preparedStmt.close();
				    	    rs.close();
				    	    sql = "SELECT * FROM MonthlyEarnings WHERE StoreName = ?";
					 	    preparedStmt = conn.prepareStatement(sql);
					 		preparedStmt.setString(1,"LilacHaifa");
				            rs = preparedStmt.executeQuery();
								
						      while (rs.next()) {
							  m2 = rs.getInt(month2);
						    }
					      
					    d = m1 + "-" + m2; 
						RequestResult.append(d);  
			      }
			      else if(storeName1.contentEquals("LilacHaifa") == true && storeName2.contentEquals("LilacHaifa") == true)
			      {
			    	  preparedStmt.close();
			    	    rs.close();
			    	    sql = "SELECT * FROM MonthlyEarnings WHERE StoreName = ?";
				 	    preparedStmt = conn.prepareStatement(sql);
				 		preparedStmt.setString(1,"LilacHaifa");
			            rs = preparedStmt.executeQuery();
							
					      while (rs.next()) {
						  m1 = rs.getInt(month1);
						  m2 = rs.getInt(month2);
					    }
					    d = m1 + "-" + m2; 
						RequestResult.append(d);  
			      }
			      else if(storeName1.contentEquals("LilacHaifa") == true && storeName2.contentEquals("LilacTelAviv") == true)
			      {
			    	  preparedStmt.close();
			    	    rs.close();
			    	    sql = "SELECT * FROM MonthlyEarnings WHERE StoreName = ?";
				 	    preparedStmt = conn.prepareStatement(sql);
				 		preparedStmt.setString(1,"LilacHaifa");
			            rs = preparedStmt.executeQuery();
							
					      while (rs.next()) {
						  m1 = rs.getInt(month1);
					    }
					    
					      preparedStmt.close();
				    	    rs.close();
				    	    sql = "SELECT * FROM MonthlyEarnings WHERE StoreName = ?";
					 	    preparedStmt = conn.prepareStatement(sql);
					 		preparedStmt.setString(1,"LilacTelAviv");
				            rs = preparedStmt.executeQuery();
								
						      while (rs.next()) {
							  m2 = rs.getInt(month2);
						    }
					      
					    d = m1 + "-" + m2; 
						RequestResult.append(d); 
			      }
			    }
			}
			
			else if(tokens[0].contentEquals("get") && tokens[1].contentEquals("histOrders"))
			{
				Class.forName(JDBC_DRIVER);
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				
				String managerName = tokens[2] + " " + tokens[3];
				String storeName = tokens[4];
				String managerStore = "";
				   
				sql = "SELECT * FROM Managers WHERE Name = ?";
		 	    preparedStmt = conn.prepareStatement(sql);
		 		preparedStmt.setString(1,managerName);
	            rs = preparedStmt.executeQuery();
					
			    while (rs.next()) {
				  managerStore = rs.getString("StoreName");
				}
			    
			    if(storeName.contentEquals("LilacHaifa") == true && managerStore.contentEquals("LilacHaifa") == true)
			    {
			    	String q1 = "";
			    	String q2 = "";
			    	String q3 = "";
			    	String q4 = "";
			    	
			    	int s1 = 0;
			    	int s2 = 0;
			    	int s3 = 0;
			    	int s4 = 0;
			    	
			    	preparedStmt.close();
		    	    sql = "SELECT * FROM LilacHaifaOrders";
			 	    preparedStmt = conn.prepareStatement(sql);
		            rs = preparedStmt.executeQuery();
		            
		            Object[][] resultSet = new Object[12][5];
		            int row = 0;
		            while (rs.next()) {
		                for (int i = 0; i < 5; i++) {
		                    resultSet[row][i] = rs.getObject(i+1);
		                }
		                row++;
		            }
		            
		            s1 = (int)resultSet[4][1] + (int)resultSet[3][1] + (int)resultSet[7][1];
		            s2 = (int)resultSet[4][2] + (int)resultSet[3][2] + (int)resultSet[7][2];
		            s3 = (int)resultSet[4][3] + (int)resultSet[3][3] + (int)resultSet[7][3];
		            s4 = (int)resultSet[4][4] + (int)resultSet[3][4] + (int)resultSet[7][4];
		            
		            q1 = s1 + "-Arrangements " + s2 + "-Bridal " + s3 + "-Flowerpot " + s4 + "-Set";
		            
		            s1 = (int)resultSet[0][1] + (int)resultSet[8][1] + (int)resultSet[6][1];
		            s2 = (int)resultSet[0][2] + (int)resultSet[8][2] + (int)resultSet[6][2];
		            s3 = (int)resultSet[0][3] + (int)resultSet[8][3] + (int)resultSet[6][3];
		            s4 = (int)resultSet[0][4] + (int)resultSet[8][4] + (int)resultSet[6][4];
						
		            q2 = s1 + "-Arrangements " + s2 + "-Bridal " + s3 + "-Flowerpot " + s4 + "-Set";
		            
		            s1 = (int)resultSet[5][1] + (int)resultSet[1][1] + (int)resultSet[11][1];
		            s2 = (int)resultSet[5][2] + (int)resultSet[1][2] + (int)resultSet[11][2];
		            s3 = (int)resultSet[5][3] + (int)resultSet[1][3] + (int)resultSet[11][3];
		            s4 = (int)resultSet[5][4] + (int)resultSet[1][4] + (int)resultSet[11][4];
						
		            q3 = s1 + "-Arrangements " + s2 + "-Bridal " + s3 + "-Flowerpot " + s4 + "-Set";
		            
		            s1 = (int)resultSet[10][1] + (int)resultSet[9][1] + (int)resultSet[2][1];
		            s2 = (int)resultSet[10][2] + (int)resultSet[9][2] + (int)resultSet[2][2];
		            s3 = (int)resultSet[10][3] + (int)resultSet[9][3] + (int)resultSet[2][3];
		            s4 = (int)resultSet[10][4] + (int)resultSet[9][4] + (int)resultSet[2][4];
						
		            q4 = s1 + "-Arrangements " + s2 + "-Bridal " + s3 + "-Flowerpot " + s4 + "-Set";
		            
		            String q = q1 + "," + q2 + "," + q3 + "," + q4;
		            
		            RequestResult.append(q);
				     
			    }
			    else if(storeName.contentEquals("LilacTelAviv") == true && managerStore.contentEquals("LilacTelAviv") == true)
			    {
			    	String q1 = "";
			    	String q2 = "";
			    	String q3 = "";
			    	String q4 = "";
			    	
			    	int s1 = 0;
			    	int s2 = 0;
			    	int s3 = 0;
			    	int s4 = 0;
			    	
			    	rs.close();
			    	preparedStmt.close();
		    	    sql = "SELECT * FROM LilacTelAvivOrders";
			 	    preparedStmt = conn.prepareStatement(sql);
		            rs = preparedStmt.executeQuery();
		            
		            Object[][] resultSet = new Object[12][5];
		            int row = 0;
		            while (rs.next()) {
		                for (int i = 0; i < 5; i++) {
		                    resultSet[row][i] = rs.getObject(i+1);
		                }
		                row++;
		            }
		            
		            s1 = (int)resultSet[4][1] + (int)resultSet[3][1] + (int)resultSet[7][1];
		            s2 = (int)resultSet[4][2] + (int)resultSet[3][2] + (int)resultSet[7][2];
		            s3 = (int)resultSet[4][3] + (int)resultSet[3][3] + (int)resultSet[7][3];
		            s4 = (int)resultSet[4][4] + (int)resultSet[3][4] + (int)resultSet[7][4];
		            
		            q1 = s1 + "-Arrangements " + s2 + "-Bridal " + s3 + "-Flowerpot " + s4 + "-Set";
		            
		            s1 = (int)resultSet[0][1] + (int)resultSet[8][1] + (int)resultSet[6][1];
		            s2 = (int)resultSet[0][2] + (int)resultSet[8][2] + (int)resultSet[6][2];
		            s3 = (int)resultSet[0][3] + (int)resultSet[8][3] + (int)resultSet[6][3];
		            s4 = (int)resultSet[0][4] + (int)resultSet[8][4] + (int)resultSet[6][4];
						
		            q2 = s1 + "-Arrangements " + s2 + "-Bridal " + s3 + "-Flowerpot " + s4 + "-Set";
		            
		            s1 = (int)resultSet[5][1] + (int)resultSet[1][1] + (int)resultSet[11][1];
		            s2 = (int)resultSet[5][2] + (int)resultSet[1][2] + (int)resultSet[11][2];
		            s3 = (int)resultSet[5][3] + (int)resultSet[1][3] + (int)resultSet[11][3];
		            s4 = (int)resultSet[5][4] + (int)resultSet[1][4] + (int)resultSet[11][4];
						
		            q3 = s1 + "-Arrangements " + s2 + "-Bridal " + s3 + "-Flowerpot " + s4 + "-Set";
		            
		            s1 = (int)resultSet[10][1] + (int)resultSet[9][1] + (int)resultSet[2][1];
		            s2 = (int)resultSet[10][2] + (int)resultSet[9][2] + (int)resultSet[2][2];
		            s3 = (int)resultSet[10][3] + (int)resultSet[9][3] + (int)resultSet[2][3];
		            s4 = (int)resultSet[10][4] + (int)resultSet[9][4] + (int)resultSet[2][4];
						
		            q4 = s1 + "-Arrangements " + s2 + "-Bridal " + s3 + "-Flowerpot " + s4 + "-Set";
		            
		            String q = q1 + "," + q2 + "," + q3 + "," + q4;
		            
		            RequestResult.append(q);
			    }
			    else if(storeName.contentEquals("All") == true && managerStore.contentEquals("All") == true)
			    {
			    	String q1 = "";
			    	String q2 = "";
			    	String q3 = "";
			    	String q4 = "";
			    	
			    	int s1 = 0;
			    	int s2 = 0;
			    	int s3 = 0;
			    	int s4 = 0;
			    	
			    	int t1 = 0;
			    	int t2 = 0;
			    	int t3 = 0;
			    	int t4 = 0;
			    	
			    	rs.close();
			    	preparedStmt.close();
		    	    sql = "SELECT * FROM LilacHaifaOrders";
			 	    preparedStmt = conn.prepareStatement(sql);
		            rs = preparedStmt.executeQuery();
		            
		            Object[][] resultSet1 = new Object[12][5];
		            int row = 0;
		            while (rs.next()) {
		                for (int i = 0; i < 5; i++) {
		                    resultSet1[row][i] = rs.getObject(i+1);
		                }
		                row++;
		            }
		            ///////////////////////////////////////////////////////////////////////////
		            for(int i = 0; i < 12; i++)
		            {
		            	System.out.println(resultSet1[i][0] + " @ ");
		            }
		            ///////////////////////////////////////////////////////////////////////////
		            rs.close();
			    	preparedStmt.close();
		    	    sql = "SELECT * FROM LilacTelAvivOrders";
			 	    preparedStmt = conn.prepareStatement(sql);
		            rs = preparedStmt.executeQuery();
		            
		            Object[][] resultSet2 = new Object[12][5];
		            row = 0;
		            while (rs.next()) {
		                for (int i = 0; i < 5; i++) {
		                    resultSet2[row][i] = rs.getObject(i+1);
		                }
		                row++;
		            }
		            
		            s1 = (int)resultSet1[4][1] + (int)resultSet1[3][1] + (int)resultSet1[7][1];
		            s2 = (int)resultSet1[4][2] + (int)resultSet1[3][2] + (int)resultSet1[7][2];
		            s3 = (int)resultSet1[4][3] + (int)resultSet1[3][3] + (int)resultSet1[7][3];
		            s4 = (int)resultSet1[4][4] + (int)resultSet1[3][4] + (int)resultSet1[7][4];
		            
		            t1 = (int)resultSet2[4][1] + (int)resultSet2[3][1] + (int)resultSet2[7][1];
		            t2 = (int)resultSet2[4][2] + (int)resultSet2[3][2] + (int)resultSet2[7][2];
		            t3 = (int)resultSet2[4][3] + (int)resultSet2[3][3] + (int)resultSet2[7][3];
		            t4 = (int)resultSet2[4][4] + (int)resultSet2[3][4] + (int)resultSet2[7][4];
		            
		            int m1 = s1+t1;
		            int m2 = s2+t2;
		            int m3 = s3+t3;
		            int m4 = s4+t4;
		            
		            q1 = m1 + "-Arrangements " + m2 + "-Bridal " + m3 + "-Flowerpot " + m4 + "-Set";
		            
		            s1 = (int)resultSet1[0][1] + (int)resultSet1[8][1] + (int)resultSet1[6][1];
		            s2 = (int)resultSet1[0][2] + (int)resultSet1[8][2] + (int)resultSet1[6][2];
		            s3 = (int)resultSet1[0][3] + (int)resultSet1[8][3] + (int)resultSet1[6][3];
		            s4 = (int)resultSet1[0][4] + (int)resultSet1[8][4] + (int)resultSet1[6][4];
		            
		            t1 = (int)resultSet2[0][1] + (int)resultSet2[8][1] + (int)resultSet2[6][1];
		            t2 = (int)resultSet2[0][2] + (int)resultSet2[8][2] + (int)resultSet2[6][2];
		            t3 = (int)resultSet2[0][3] + (int)resultSet2[8][3] + (int)resultSet2[6][3];
		            t4 = (int)resultSet2[0][4] + (int)resultSet2[8][4] + (int)resultSet2[6][4];
					
		            m1 = s1+t1;
		            m2 = s2+t2;
		            m3 = s3+t3;
		            m4 = s4+t4;
		            
		            q2 = m1 + "-Arrangements " + m2 + "-Bridal " + m3 + "-Flowerpot " + m4 + "-Set";
		            
		            s1 = (int)resultSet1[5][1] + (int)resultSet1[1][1] + (int)resultSet1[11][1];
		            s2 = (int)resultSet1[5][2] + (int)resultSet1[1][2] + (int)resultSet1[11][2];
		            s3 = (int)resultSet1[5][3] + (int)resultSet1[1][3] + (int)resultSet1[11][3];
		            s4 = (int)resultSet1[5][4] + (int)resultSet1[11][4] + (int)resultSet1[11][4];
		            
		            t1 = (int)resultSet2[5][1] + (int)resultSet2[1][1] + (int)resultSet2[11][1];
		            t2 = (int)resultSet2[5][2] + (int)resultSet2[1][2] + (int)resultSet2[11][2];
		            t3 = (int)resultSet2[5][3] + (int)resultSet2[1][3] + (int)resultSet2[11][3];
		            t4 = (int)resultSet2[5][4] + (int)resultSet2[11][4] + (int)resultSet2[11][4];
						
		            m1 = s1+t1;
		            m2 = s2+t2;
		            m3 = s3+t3;
		            m4 = s4+t4;
		            
		            q3 = m1 + "-Arrangements " + m2 + "-Bridal " + m3 + "-Flowerpot " + m4 + "-Set";
		            
		            s1 = (int)resultSet1[10][1] + (int)resultSet1[9][1] + (int)resultSet1[2][1];
		            s2 = (int)resultSet1[10][2] + (int)resultSet1[9][2] + (int)resultSet1[2][2];
		            s3 = (int)resultSet1[10][3] + (int)resultSet1[9][3] + (int)resultSet1[2][3];
		            s4 = (int)resultSet1[10][4] + (int)resultSet1[9][4] + (int)resultSet1[2][4];
		            
		            t1 = (int)resultSet2[10][1] + (int)resultSet2[9][1] + (int)resultSet2[2][1];
		            t2 = (int)resultSet2[10][2] + (int)resultSet2[9][2] + (int)resultSet2[2][2];
		            t3 = (int)resultSet2[10][3] + (int)resultSet2[9][3] + (int)resultSet2[2][3];
		            t4 = (int)resultSet2[10][4] + (int)resultSet2[9][4] + (int)resultSet2[2][4];
		            
		            m1 = s1+t1;
		            m2 = s2+t2;
		            m3 = s3+t3;
		            m4 = s4+t4;
						
		            q4 = m1 + "-Arrangements " + m2 + "-Bridal " + m3 + "-Flowerpot " + m4 + "-Set";
		            
		            String q = q1 + "," + q2 + "," + q3 + "," + q4;
		            
		            RequestResult.append(q);
			    }
			    else if(storeName.contentEquals("LilacHaifa") == true && managerStore.contentEquals("All") == true)
			    {
			    	String q1 = "";
			    	String q2 = "";
			    	String q3 = "";
			    	String q4 = "";
			    	
			    	int s1 = 0;
			    	int s2 = 0;
			    	int s3 = 0;
			    	int s4 = 0;
			    	
			    	rs.close();
			    	preparedStmt.close();
		    	    sql = "SELECT * FROM LilacHaifaOrders";
			 	    preparedStmt = conn.prepareStatement(sql);
		            rs = preparedStmt.executeQuery();
		            
		            Object[][] resultSet = new Object[12][5];
		            int row = 0;
		            while (rs.next()) {
		                for (int i = 0; i < 5; i++) {
		                    resultSet[row][i] = rs.getObject(i+1);
		                }
		                row++;
		            }
		            
		            s1 = (int)resultSet[4][1] + (int)resultSet[3][1] + (int)resultSet[7][1];
		            s2 = (int)resultSet[4][2] + (int)resultSet[3][2] + (int)resultSet[7][2];
		            s3 = (int)resultSet[4][3] + (int)resultSet[3][3] + (int)resultSet[7][3];
		            s4 = (int)resultSet[4][4] + (int)resultSet[3][4] + (int)resultSet[7][4];
		            
		            q1 = s1 + "-Arrangements " + s2 + "-Bridal " + s3 + "-Flowerpot " + s4 + "-Set";
		            
		            s1 = (int)resultSet[0][1] + (int)resultSet[8][1] + (int)resultSet[6][1];
		            s2 = (int)resultSet[0][2] + (int)resultSet[8][2] + (int)resultSet[6][2];
		            s3 = (int)resultSet[0][3] + (int)resultSet[8][3] + (int)resultSet[6][3];
		            s4 = (int)resultSet[0][4] + (int)resultSet[8][4] + (int)resultSet[6][4];
						
		            q2 = s1 + "-Arrangements " + s2 + "-Bridal " + s3 + "-Flowerpot " + s4 + "-Set";
		            
		            s1 = (int)resultSet[5][1] + (int)resultSet[1][1] + (int)resultSet[11][1];
		            s2 = (int)resultSet[5][2] + (int)resultSet[1][2] + (int)resultSet[11][2];
		            s3 = (int)resultSet[5][3] + (int)resultSet[1][3] + (int)resultSet[11][3];
		            s4 = (int)resultSet[5][4] + (int)resultSet[1][4] + (int)resultSet[11][4];
						
		            q3 = s1 + "-Arrangements " + s2 + "-Bridal " + s3 + "-Flowerpot " + s4 + "-Set";
		            
		            s1 = (int)resultSet[10][1] + (int)resultSet[9][1] + (int)resultSet[2][1];
		            s2 = (int)resultSet[10][2] + (int)resultSet[9][2] + (int)resultSet[2][2];
		            s3 = (int)resultSet[10][3] + (int)resultSet[9][3] + (int)resultSet[2][3];
		            s4 = (int)resultSet[10][4] + (int)resultSet[9][4] + (int)resultSet[2][4];
						
		            q4 = s1 + "-Arrangements " + s2 + "-Bridal " + s3 + "-Flowerpot " + s4 + "-Set";
		            
		            String q = q1 + "," + q2 + "," + q3 + "," + q4;
		            
		            RequestResult.append(q);	
			    }
			    else if(storeName.contentEquals("LilacTelAviv") == true && managerStore.contentEquals("All") == true)
			    {
			    	String q1 = "";
			    	String q2 = "";
			    	String q3 = "";
			    	String q4 = "";
			    	
			    	int s1 = 0;
			    	int s2 = 0;
			    	int s3 = 0;
			    	int s4 = 0;
			    	
			    	rs.close();
			    	preparedStmt.close();
		    	    sql = "SELECT * FROM LilacTelAvivOrders";
			 	    preparedStmt = conn.prepareStatement(sql);
		            rs = preparedStmt.executeQuery();
		            
		            Object[][] resultSet = new Object[12][5];
		            int row = 0;
		            while (rs.next()) {
		                for (int i = 0; i < 5; i++) {
		                    resultSet[row][i] = rs.getObject(i+1);
		                }
		                row++;
		            }
		            
		            s1 = (int)resultSet[4][1] + (int)resultSet[3][1] + (int)resultSet[7][1];
		            s2 = (int)resultSet[4][2] + (int)resultSet[3][2] + (int)resultSet[7][2];
		            s3 = (int)resultSet[4][3] + (int)resultSet[3][3] + (int)resultSet[7][3];
		            s4 = (int)resultSet[4][4] + (int)resultSet[3][4] + (int)resultSet[7][4];
		            
		            q1 = s1 + "-Arrangements " + s2 + "-Bridal " + s3 + "-Flowerpot " + s4 + "-Set";
		            
		            s1 = (int)resultSet[0][1] + (int)resultSet[8][1] + (int)resultSet[6][1];
		            s2 = (int)resultSet[0][2] + (int)resultSet[8][2] + (int)resultSet[6][2];
		            s3 = (int)resultSet[0][3] + (int)resultSet[8][3] + (int)resultSet[6][3];
		            s4 = (int)resultSet[0][4] + (int)resultSet[8][4] + (int)resultSet[6][4];
						
		            q2 = s1 + "-Arrangements " + s2 + "-Bridal " + s3 + "-Flowerpot " + s4 + "-Set";
		            
		            s1 = (int)resultSet[5][1] + (int)resultSet[1][1] + (int)resultSet[11][1];
		            s2 = (int)resultSet[5][2] + (int)resultSet[1][2] + (int)resultSet[11][2];
		            s3 = (int)resultSet[5][3] + (int)resultSet[1][3] + (int)resultSet[11][3];
		            s4 = (int)resultSet[5][4] + (int)resultSet[1][4] + (int)resultSet[11][4];
						
		            q3 = s1 + "-Arrangements " + s2 + "-Bridal " + s3 + "-Flowerpot " + s4 + "-Set";
		            
		            s1 = (int)resultSet[10][1] + (int)resultSet[9][1] + (int)resultSet[2][1];
		            s2 = (int)resultSet[10][2] + (int)resultSet[9][2] + (int)resultSet[2][2];
		            s3 = (int)resultSet[10][3] + (int)resultSet[9][3] + (int)resultSet[2][3];
		            s4 = (int)resultSet[10][4] + (int)resultSet[9][4] + (int)resultSet[2][4];
						
		            q4 = s1 + "-Arrangements " + s2 + "-Bridal " + s3 + "-Flowerpot " + s4 + "-Set";
		            
		            String q = q1 + "," + q2 + "," + q3 + "," + q4;
		            
		            RequestResult.append(q);
			    }
			    else
			    {
			    	RequestResult.append("Unsuccessful Orders Histogram");
			    }
			}
			
			else if(tokens[0].contentEquals("get") && tokens[1].contentEquals("ord"))
			{
				Class.forName(JDBC_DRIVER);
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				
				StringBuilder sb = new StringBuilder();
				String managerName = tokens[2] + " " + tokens[3];
				String storeName1 = tokens[4];
				String storeName2 = tokens[5];
				
				String month1 = tokens[6];
				
				if(month1.contentEquals("1") == true)
				{
					month1 = "January";
				}
				else if(month1.contentEquals("2") == true)
				{
					month1 = "February";
				}
				else if(month1.contentEquals("3") == true)
				{
					month1 = "March";
				}
				else if(month1.contentEquals("4") == true)
				{
					month1 = "April";
				}
				else if(month1.contentEquals("5") == true)
				{
					month1 = "May";
				}
				else if(month1.contentEquals("6") == true)
				{
					month1 = "June";
				}
				else if(month1.contentEquals("7") == true)
				{
					month1 = "July";
				}
				else if(month1.contentEquals("8") == true)
				{
					month1 = "August";
				}
				else if(month1.contentEquals("9") == true)
				{
					month1 = "September";
				}
				else if(month1.contentEquals("10") == true)
				{
					month1 = "October";
				}
				else if(month1.contentEquals("11") == true)
				{
					month1 = "November";
				}
				else if(month1.contentEquals("12") == true)
				{
					month1 = "December";
				}
				
				String month2 = tokens[7];
				
				if(month2.contentEquals("1") == true)
				{
					month2 = "January";
				}
				else if(month2.contentEquals("2") == true)
				{
					month2 = "February";
				}
				else if(month2.contentEquals("3") == true)
				{
					month2 = "March";
				}
				else if(month2.contentEquals("4") == true)
				{
					month2 = "April";
				}
				else if(month2.contentEquals("5") == true)
				{
					month2 = "May";
				}
				else if(month2.contentEquals("6") == true)
				{
					month2 = "June";
				}
				else if(month2.contentEquals("7") == true)
				{
					month2 = "July";
				}
				else if(month2.contentEquals("8") == true)
				{
					month2 = "August";
				}
				else if(month2.contentEquals("9") == true)
				{
					month2 = "September";
				}
				else if(month2.contentEquals("10") == true)
				{
					month2 = "October";
				}
				else if(month2.contentEquals("11") == true)
				{
					month2 = "November";
				}
				else if(month2.contentEquals("12") == true)
				{
					month2 = "December";
				}
				
				String managerStore = "";
				
				sql = "SELECT * FROM Managers WHERE Name = ?";
		 	    preparedStmt = conn.prepareStatement(sql);
		 		preparedStmt.setString(1,managerName);
	            rs = preparedStmt.executeQuery();
					
			    while (rs.next()) {
				  managerStore = rs.getString("StoreName");
				}
				
			    if(managerStore.contentEquals("LilacHaifa"))
			    {
			       if(storeName1.contentEquals("LilacHaifa") == false || storeName2.contentEquals("LilacHaifa") == false)
			       {
			    	   RequestResult.append("Unsuccessful Orders Comparison");
			       }
			       else
			       {
			    	    int counterSet = 0, counterFlowerpot = 0, counterArrangements = 0, counterBridal = 0;
			    	    
			    	    preparedStmt.close();
			    	    sql = "SELECT * FROM LilacHaifaOrders WHERE Month = ?";
				 	    preparedStmt = conn.prepareStatement(sql);
				 		preparedStmt.setString(1,month1);
			            rs = preparedStmt.executeQuery();
							
					      while (rs.next()) {
					      counterArrangements = rs.getInt("ArrangementsCount");
					      counterBridal = rs.getInt("BridalCount");
					      counterFlowerpot = rs.getInt("FlowerpotCount");
					      counterSet = rs.getInt("SetCount");     
					    }
					      
					    sb.append(counterArrangements + "-Arrangements " + counterBridal + "-Bridal "
					    		 + counterFlowerpot + "-Flowerpot " + counterSet + "-Set" + ",");  
					    
					    preparedStmt.close();
			    	    sql = "SELECT * FROM LilacHaifaOrders WHERE Month = ?";
				 	    preparedStmt = conn.prepareStatement(sql);
				 		preparedStmt.setString(1,month2);
			            rs = preparedStmt.executeQuery();
							
					      while (rs.next()) {
					      counterArrangements = rs.getInt("ArrangementsCount");
					      counterBridal = rs.getInt("BridalCount");
					      counterFlowerpot = rs.getInt("FlowerpotCount");
					      counterSet = rs.getInt("SetCount");     
					    }
					      
					    sb.append(counterArrangements + "-Arrangements " + counterBridal + "-Bridal "
					    		 + counterFlowerpot + "-Flowerpot " + counterSet + "-Set");  
					    		
						RequestResult.append(sb.toString());  
			       }
			    }
			    
			    else if(managerStore.contentEquals("LilacTelAviv"))
			    {
			    	if(storeName1.contentEquals("LilacTelAviv") == false || storeName2.contentEquals("LilacTelAviv") == false)
				       {
			    		 RequestResult.append("Unsuccessful Orders Comparison");
				       }
				       else
				       {
				    	   int counterSet = 0, counterFlowerpot = 0, counterArrangements = 0, counterBridal = 0;
				    	    
				    	    rs.close();
				    	    preparedStmt.close();
				    	    sql = "SELECT * FROM LilacTelAvivOrders WHERE Month = ?";
					 	    preparedStmt = conn.prepareStatement(sql);
					 		preparedStmt.setString(1,month1);
				            rs = preparedStmt.executeQuery();
								
						      while (rs.next()) {
						      counterArrangements = rs.getInt("ArrangementsCount");
						      counterBridal = rs.getInt("BridalCount");
						      counterFlowerpot = rs.getInt("FlowerpotCount");
						      counterSet = rs.getInt("SetCount");     
						    }
						      
						    sb.append(counterArrangements + "-Arrangements " + counterBridal + "-Bridal "
						    		 + counterFlowerpot + "-Flowerpot " + counterSet + "-Set" + ",");  
						    
						    preparedStmt.close();
				    	    sql = "SELECT * FROM LilacTelAvivOrders WHERE Month = ?";
					 	    preparedStmt = conn.prepareStatement(sql);
					 		preparedStmt.setString(1,month2);
				            rs = preparedStmt.executeQuery();
								
						      while (rs.next()) {
						      counterArrangements = rs.getInt("ArrangementsCount");
						      counterBridal = rs.getInt("BridalCount");
						      counterFlowerpot = rs.getInt("FlowerpotCount");
						      counterSet = rs.getInt("SetCount");     
						    }
						      
						    sb.append(counterArrangements + "-Arrangements " + counterBridal + "-Bridal "
						    		 + counterFlowerpot + "-Flowerpot " + counterSet + "-Set");  
						    		
							RequestResult.append(sb.toString());  
				       }
			    }
			    
			    else if(managerStore.contentEquals("All"))
			    {
			      if(storeName1.contentEquals("LilacTelAviv") == true && storeName2.contentEquals("LilacTelAviv") == true)
			      {
			    	  int counterSet = 0, counterFlowerpot = 0, counterArrangements = 0, counterBridal = 0;
			    	    
			    	    rs.close();
			    	    preparedStmt.close();
			    	    sql = "SELECT * FROM LilacTelAvivOrders WHERE Month = ?";
				 	    preparedStmt = conn.prepareStatement(sql);
				 		preparedStmt.setString(1,month1);
			            rs = preparedStmt.executeQuery();
							
					      while (rs.next()) {
					      counterArrangements = rs.getInt("ArrangementsCount");
					      counterBridal = rs.getInt("BridalCount");
					      counterFlowerpot = rs.getInt("FlowerpotCount");
					      counterSet = rs.getInt("SetCount");     
					    }
					      
					    sb.append(counterArrangements + "-Arrangements " + counterBridal + "-Bridal "
					    		 + counterFlowerpot + "-Flowerpot " + counterSet + "-Set" + ",");  
					    
					    preparedStmt.close();
			    	    sql = "SELECT * FROM LilacTelAvivOrders WHERE Month = ?";
				 	    preparedStmt = conn.prepareStatement(sql);
				 		preparedStmt.setString(1,month2);
			            rs = preparedStmt.executeQuery();
							
					      while (rs.next()) {
					      counterArrangements = rs.getInt("ArrangementsCount");
					      counterBridal = rs.getInt("BridalCount");
					      counterFlowerpot = rs.getInt("FlowerpotCount");
					      counterSet = rs.getInt("SetCount");     
					    }
					      
					    sb.append(counterArrangements + "-Arrangements " + counterBridal + "-Bridal "
					    		 + counterFlowerpot + "-Flowerpot " + counterSet + "-Set");  
					    		
						RequestResult.append(sb.toString()); 
			      }
			      else if(storeName1.contentEquals("LilacTelAviv") == true && storeName2.contentEquals("LilacHaifa") == true)
			      {
			    	  int counterSet = 0, counterFlowerpot = 0, counterArrangements = 0, counterBridal = 0;
			    	    
			    	    rs.close();
			    	    preparedStmt.close();
			    	    sql = "SELECT * FROM LilacTelAvivOrders WHERE Month = ?";
				 	    preparedStmt = conn.prepareStatement(sql);
				 		preparedStmt.setString(1,month1);
			            rs = preparedStmt.executeQuery();
							
					      while (rs.next()) {
					      counterArrangements = rs.getInt("ArrangementsCount");
					      counterBridal = rs.getInt("BridalCount");
					      counterFlowerpot = rs.getInt("FlowerpotCount");
					      counterSet = rs.getInt("SetCount");     
					    }
					      
					    sb.append(counterArrangements + "-Arrangements " + counterBridal + "-Bridal "
					    		 + counterFlowerpot + "-Flowerpot " + counterSet + "-Set" + ",");  
					    
					    preparedStmt.close();
			    	    sql = "SELECT * FROM LilacHaifaOrders WHERE Month = ?";
				 	    preparedStmt = conn.prepareStatement(sql);
				 		preparedStmt.setString(1,month2);
			            rs = preparedStmt.executeQuery();
							
					      while (rs.next()) {
					      counterArrangements = rs.getInt("ArrangementsCount");
					      counterBridal = rs.getInt("BridalCount");
					      counterFlowerpot = rs.getInt("FlowerpotCount");
					      counterSet = rs.getInt("SetCount");     
					    }
					      
					    sb.append(counterArrangements + "-Arrangements " + counterBridal + "-Bridal "
					    		 + counterFlowerpot + "-Flowerpot " + counterSet + "-Set");  
					    		
						RequestResult.append(sb.toString());
			      }
			      else if(storeName1.contentEquals("LilacHaifa") == true && storeName2.contentEquals("LilacHaifa") == true)
			      {
			    	  int counterSet = 0, counterFlowerpot = 0, counterArrangements = 0, counterBridal = 0;
			    	    
			    	    rs.close();
			    	    preparedStmt.close();
			    	    sql = "SELECT * FROM LilacHaifaOrders WHERE Month = ?";
				 	    preparedStmt = conn.prepareStatement(sql);
				 		preparedStmt.setString(1,month1);
			            rs = preparedStmt.executeQuery();
							
					      while (rs.next()) {
					      counterArrangements = rs.getInt("ArrangementsCount");
					      counterBridal = rs.getInt("BridalCount");
					      counterFlowerpot = rs.getInt("FlowerpotCount");
					      counterSet = rs.getInt("SetCount");     
					    }
					      
					    sb.append(counterArrangements + "-Arrangements " + counterBridal + "-Bridal "
					    		 + counterFlowerpot + "-Flowerpot " + counterSet + "-Set" + ",");  
					    
					    preparedStmt.close();
			    	    sql = "SELECT * FROM LilacHaifaOrders WHERE Month = ?";
				 	    preparedStmt = conn.prepareStatement(sql);
				 		preparedStmt.setString(1,month2);
			            rs = preparedStmt.executeQuery();
							
					      while (rs.next()) {
					      counterArrangements = rs.getInt("ArrangementsCount");
					      counterBridal = rs.getInt("BridalCount");
					      counterFlowerpot = rs.getInt("FlowerpotCount");
					      counterSet = rs.getInt("SetCount");     
					    }
					      
					    sb.append(counterArrangements + "-Arrangements " + counterBridal + "-Bridal "
					    		 + counterFlowerpot + "-Flowerpot " + counterSet + "-Set");  
					    		
						RequestResult.append(sb.toString());
			      }
			      else if(storeName1.contentEquals("LilacHaifa") == true && storeName2.contentEquals("LilacTelAviv") == true)
			      {
			    	  int counterSet = 0, counterFlowerpot = 0, counterArrangements = 0, counterBridal = 0;
			    	    
			    	    rs.close();
			    	    preparedStmt.close();
			    	    sql = "SELECT * FROM LilacHaifaOrders WHERE Month = ?";
				 	    preparedStmt = conn.prepareStatement(sql);
				 		preparedStmt.setString(1,month1);
			            rs = preparedStmt.executeQuery();
							
					      while (rs.next()) {
					      counterArrangements = rs.getInt("ArrangementsCount");
					      counterBridal = rs.getInt("BridalCount");
					      counterFlowerpot = rs.getInt("FlowerpotCount");
					      counterSet = rs.getInt("SetCount");     
					    }
					      
					    sb.append(counterArrangements + "-Arrangements " + counterBridal + "-Bridal "
					    		 + counterFlowerpot + "-Flowerpot " + counterSet + "-Set" + ",");  
					    
					    preparedStmt.close();
			    	    sql = "SELECT * FROM LilacTelAvivOrders WHERE Month = ?";
				 	    preparedStmt = conn.prepareStatement(sql);
				 		preparedStmt.setString(1,month2);
			            rs = preparedStmt.executeQuery();
							
					      while (rs.next()) {
					      counterArrangements = rs.getInt("ArrangementsCount");
					      counterBridal = rs.getInt("BridalCount");
					      counterFlowerpot = rs.getInt("FlowerpotCount");
					      counterSet = rs.getInt("SetCount");     
					    }
					      
					    sb.append(counterArrangements + "-Arrangements " + counterBridal + "-Bridal "
					    		 + counterFlowerpot + "-Flowerpot " + counterSet + "-Set");  
					    		
						RequestResult.append(sb.toString());
			      }
			    }
			}
		}
		catch (SQLException se)
		{
		  se.printStackTrace();
		  System.out.println("SQLException: " + se.getMessage());
          System.out.println("SQLState: " + se.getSQLState());
          System.out.println("VendorError: " + se.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
				if (preparedStmt != null) {
					preparedStmt.close();
					preparedStmt = null;
				}	
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		System.out.println("Message received: " + msg + " from \"" + 
	     client.getInfo("loginID") + "\" " + client);
		 this.sendToAllClients(RequestResult.toString());
     
    }
  }

  /**
   * This method handles all data coming from the UI
   *
   * @param message The message from the UI
   */
  public void handleMessageFromServerUI(String message)
  {
    if (message.charAt(0) == '#')
    {
      runCommand(message);
    }
    else
    {
      // send message to clients
      serverUI.display(message);
      this.sendToAllClients("SERVER MSG> " + message);
    }
  }

  /**
   * This method executes server commands.
   *
   * @param message String from the server console.
   */
  private void runCommand(String message)
  {
    // run commands
    // a series of if statements

    if (message.equalsIgnoreCase("#quit"))
    {
      quit();
    }
    else if (message.equalsIgnoreCase("#stop"))
    {
      stopListening();
    }
    else if (message.equalsIgnoreCase("#close"))
    {
      try
      {
        close();
      }
      catch(IOException e) {}
    }
    else if (message.toLowerCase().startsWith("#setport"))
    {
      if (getNumberOfClients() == 0 && !isListening())
      {
        // If there are no connected clients and we are not 
        // listening for new ones, assume server closed.
        // A more exact way to determine this was not obvious and
        // time was limited.
        int newPort = Integer.parseInt(message.substring(9));
        setPort(newPort);
        //error checking should be added
        serverUI.display
          ("Server port changed to " + getPort());
      }
      else
      {
        serverUI.display
          ("The server is not closed. Port cannot be changed.");
      }
    }
    else if (message.equalsIgnoreCase("#start"))
    {
      if (!isListening())
      {
        try
        {
          listen();
        }
        catch(Exception ex)
        {
          serverUI.display("Error - Could not listen for clients!");
        }
      }
      else
      {
        serverUI.display
          ("The server is already listening for clients.");
      }
    }
    else if (message.equalsIgnoreCase("#getport"))
    {
      serverUI.display("Currently port: " + Integer.toString(getPort()));
    }
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }

  /**
   * Run when new clients are connected. Implemented by Benjamin Bergman,
   * Oct 22, 2009.
   *
   * @param client the connection connected to the client
   */
  protected void clientConnected(ConnectionToClient client) 
  {
    // display on server and clients that the client has connected.
    String msg = "A Client has connected";
    System.out.println(msg);
    this.sendToAllClients(msg);
  }

  /**
   * Run when clients disconnect. Implemented by Benjamin Bergman,
   * Oct 22, 2009
   *
   * @param client the connection with the client
   */
  synchronized protected void clientDisconnected(
    ConnectionToClient client)
  {
    // display on server and clients when a user disconnects
    String msg = client.getInfo("loginID").toString() + " has disconnected";

    System.out.println(msg);
    this.sendToAllClients(msg);
  }

  /**
   * Run when a client suddenly disconnects. Implemented by Benjamin
   * Bergman, Oct 22, 2009
   *
   * @param client the client that raised the exception
   * @param Throwable the exception thrown
   */
  synchronized protected void clientException(
    ConnectionToClient client, Throwable exception) 
  {
    String msg = client.getInfo("loginID").toString() + " has disconnected";

    System.out.println(msg);
    this.sendToAllClients(msg);
  }

  /**
   * This method terminates the server.
   */
  public void quit()
  {
    try
    {
      close();
    }
    catch(IOException e)
    {
    }
    System.exit(0);
  }
  
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    LilacServer sv = new LilacServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}
