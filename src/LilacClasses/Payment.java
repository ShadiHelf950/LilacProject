package LilacClasses;

public class Payment
{
  private int id;
  private String number;
  private int ccv;
  private double balance;
  private Date expirationDate;
			  
  public Payment(int idNumber, String pNumber, int pCCV, double b, Date expDate)
  {
    id = idNumber;
    number = pNumber;
    ccv = pCCV;
    balance = b;
    expirationDate = new Date(expDate);
  }
			  
  // getters and setters for private fields
	  
  public int getId() { return id; }
  public void setId(int idNumber) { id = idNumber; }
	  
  public String getNumber() { return number; }
  public void setNumber(String pNumber) { number = pNumber; }
	  
  public int getCCV() { return ccv; }
  public void setCCV(int pCCV) { ccv = pCCV; }
  
  public double getBalance() { return balance; }
  public void setBalance(double b) { balance = b; }
  
  public Date getExpirationDate() { return expirationDate; }
  public void setType(Date expDate) { expirationDate = expDate; }
}
