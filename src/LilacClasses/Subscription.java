package LilacClasses;

public class Subscription
{
  private boolean type;
  private Date expirationDate;
  private double pricePerMonth;
  private boolean status;
		  
  public Subscription(boolean t, Date expDate, double price, boolean s)
  {
	type = t;
	expirationDate = new Date(expDate);
	pricePerMonth = price;
	status = s;
  }
		  
  // getters and setters for private fields
  
  public boolean getType() { return type; }
  public void setType(boolean t) { type = t; }
  
  public Date getExpirationDate() { return expirationDate; }
  public void setType(Date expDate) { expirationDate = expDate; }
  
  public double getPricePerMonth() { return pricePerMonth; }
  public void setPricePerMonth(double price) { pricePerMonth = price; }
  
  public boolean getStatus() { return status; }
  public void setStatus(boolean s) { status = s; }
}
