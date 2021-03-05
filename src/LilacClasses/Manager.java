package LilacClasses;

public class Manager extends Person
{
   public Manager(int idNumber, String fN, String sN, String uN,
		           String pw, String p, String ad, String e, boolean c,
                   String host, int port)
   {
     super(idNumber,fN,sN,uN,pw,p,ad,e,c,host,port);
   }
   
   // get quarter profits
   
   public String getQuarterProfits(int quarterNum ,String storeName)
   {
	   return "success";
   }
   
   // get quarter order details
   
   public String getOrders(int quarterNum ,String storeName)
   {
	   return "success";
   }
   
   // get quarter complaints
   
   public String getQuarterComplaints(int quarterNum ,String storeName)
   {
	   return "success";
   }
   
   // get months report from the same store or different stores
   
   public String getMonthsReport(String type, String storeName1,
		                         String storeName2,int month1,int month2)
   {
	   return "success";
   }
   
   // change the privileges an employee
   
   public boolean modifyEmployeePrivileges(int employeeId ,String privileges)
   {
	   return true;
   }
   
   // deactivate a customer's account
   
   public boolean freezeCustomerAccount(int customerId)
   {
	   return true;
   }
   
   // deactivate a employee's account
   
   public boolean fireEmplyee(int employeeId)
   {
	   return true;
   }
   
   // change the info an employee
   
   public boolean modifyEmployeeInfo(int employeeId ,String fields, String values)
   {
	   return true;
   }
   
 // change the info a customer
   
   public boolean modifyCustomerInfo(int customerId ,String fields, String values)
   {
	   return true;
   }
}
