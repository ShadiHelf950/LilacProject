package LilacClasses;

public class Complaint
{
  private int id;
  private int customerId;
  private String description;
  private String response;
  private boolean status;
  private Date date;
  private Time time;
	  
  public Complaint(int idNumber, int customerIdNumber, String d, String r,
		           boolean s, Date dt, Time tm)
  {
	id = idNumber;
	customerId = customerIdNumber;
	description = d;
	response = r;
	status = s;
	date = dt;
	time = tm;
  }
	  
	//getters and setters for private fields
	  
	  public int getId() { return id; }
	  public void setId(int idNumber) { id = idNumber; }
	  
	  public int getCustomerId() { return customerId; }
	  public void setCustomerId(int customerIdNumber) { customerId = customerIdNumber; }
	  
	  public String getDescription() { return description; }
	  public void setDescription(String d) { description = d; }
	  
	  public String getResponse() { return response; }
	  public void setResponse(String r) { response = r; }
	  
	  public boolean getStatus() { return status; }
	  public void setStatus(boolean s) { status = s; }
	  
	  public Date getDate() { return date; }
	  public void setDate(Date dt) { date = dt; }
	  
	  public Time getTime() { return time; }
	  public void setTime(Time tm) { time = tm; }
}
