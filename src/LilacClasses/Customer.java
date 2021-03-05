package LilacClasses;

import java.util.*;

public class Customer extends Person
{
  private List<Payment> creditCards;
  private List<Order> cart;
  private Subscription subscription;
  private int numOfComplaints;
  private boolean status;
  private String storeName;
  private String regretsResponses;
  
  public Customer(int idNumber, String fN, String sN, String uN,
			      String pw, String p, String ad, String e, boolean c,
	              String host, int port, int n, boolean s, String stN, String res)
  {
	  super(idNumber,fN,sN,uN,pw,p,ad,e,c,host,port);
	  numOfComplaints = n;
	  status = s;
	  storeName = stN;
	  regretsResponses = res;
  }
  
  // add a credit card to the customer
  public boolean addPayment(Payment p)
  {
	 if(creditCards.size() == 0)
     {
    	creditCards = new ArrayList<Payment>();
    	creditCards.add(p);
        return true;  
     }
      
     for (Payment payment : creditCards)
   	 { 		      
       if(p.getId() == payment.getId()) return false;   		
     }
     creditCards.add(p);
     return true;  
  }
  
 // remove a credit card from the customer
  public boolean removePayment(int id)
  {
      for (Payment payment : creditCards)
  	  { 		      
        if(payment.getId() == id)
        {
        	creditCards.remove(payment);
        	return true;
        }
      }
      return false;  
  }
  
  // get a credit card from the customer
  public Payment getPayment(int id)
  {
    for (Payment payment : creditCards)
 	{ 		      
       if(payment.getId() == id)
       {
       	 return payment;
       }
    }
    return null;  
  }
  
  // add an order to the customer's cart
  public boolean addToCart(Order od)
  {
	if(cart.size() == 0)
    {
   	  cart = new ArrayList<Order>();
   	  cart.add(od);
      return true;  
    }
     
    for (Order order : cart)
  	 { 		      
      if(od.getId() == order.getId()) return false;   		
    }
    cart.add(od);
    return true;  
  }
  
  // remove an order from the customer's cart
  public boolean cancelOrder(int id)
  {
     for (Order order : cart)
 	  { 		      
       if(order.getId() == id)
       {
       	cart.remove(order);
       	return true;
       }
     }
     return false;  
  }
  
  // get an order from the customer's cart
  public Order getOrder(int id)
  {
    for (Order order : cart)
	  { 		      
      if(order.getId() == id)
      {
      	return order;
      }
    }
    return null;  
  }
  
  void confirmPurchase()
  {
	  client.accept("confirm order " + Integer.toString(id));
	  status = true;
  }
  
  // getters and setters for private fields
  
  public Subscription getSubscription() { return subscription; }
  public void setSubscription(boolean t, Date expDate, double price, boolean s)
  { subscription = new Subscription(t,expDate,price,s); }
  
  public int getNumOfComplaints() { return numOfComplaints; }
  public void setNumOfComplaints(int n) { numOfComplaints = n; }
  
  public boolean getStatus() { return status; }
  public void setStatus(boolean s) { status = s; }
  
  public String getStoreName() { return storeName; }
  public void setStoreName(String stN) { storeName = stN; }
  
  public String getRegretsResponses() { return regretsResponses; }
  public void setRegretsResponses(String res) { regretsResponses = res; }
}
