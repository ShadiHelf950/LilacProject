package LilacClasses;

public class Employee extends Person
{
	private boolean status;
	private String privileges;
	
	public Employee(int idNumber, String fN, String sN, String uN,
		            String pw, String p, String ad, String e, boolean c,
                    String host, int port, boolean s, String pr)
   {
      super(idNumber,fN,sN,uN,pw,p,ad,e,c,host,port);
      status = s;
      privileges = pr;
   }
	
   // getters and setters for private fields
	
   public boolean getStatus() { return status; }
   public void setStatus(boolean s) { status = s; }
	  
   public String getPrivileges() { return privileges; }
   public void setPrivileges(String pr) { privileges = pr; }
   
   // add a product to the catalog
   

   
   // edit a product from the catalog
   
   public boolean editCatalog(int id, String fields, String values)
   {
	   return true;
   }
   
   // handle an order cancellation for an order
   
   public String handleOrderCancelation(int orderId)
   {
	   return "success";
   }
   
   // handle a customer complaint for an customer
   
   public String handleCustomerComplaint(int customerId)
   {
	   return "success";
   }
}
