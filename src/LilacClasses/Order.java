package LilacClasses;

import java.util.*;

public class Order
{
  private int id;
  private int numOfProducts;
  private double totalPrice;
  private List<CatalogProduct> catalogProducts;
  private List<CustomProduct> customProducts;
  private String congrat;
  private int customerId;
  private String paymentDetails;
  private Date purchaseDate;
  private Time purchaseTime;
  private boolean pickUpType;
  private String deliveryAddress;
  private int recieverId;
  private String recieverName;
  private String recieverPhone;
  private double shippingCost;
  private boolean orderType;
  private boolean status;
  private String regretRequest;
  private String regretResponse;
		  
   public Order(int idNumber,int num, double price, String c,
		        int customerIdNumber, String pd, Date dt, Time tm,
		        boolean pt, String da, int rId, String rName, String rPhone,
		        double cost, boolean s, String req, String res)
   {
      id = idNumber;
      numOfProducts = num;
      totalPrice = price;
      congrat = c;
      customerId = customerIdNumber;
      paymentDetails = pd;
      purchaseDate = dt;
      purchaseTime = tm;
      pickUpType = pt;
      deliveryAddress = da;
      recieverId = rId;
      recieverName = rName;
      recieverPhone  = rPhone;
      shippingCost = cost;
      status = s;
      regretRequest = req;
      regretResponse = res;
   }
		  
    // getters and setters for private fields
		  
    public int getId() { return id; }
	public void setId(int idNumber) { id = idNumber; }
    
    public int getNumOfProducts() { return numOfProducts; }
    public void setNumOfProducts(int num) { numOfProducts = num; }
    
    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double price) { totalPrice = price; }
	
    // get a catalog product from the catalog product list given catalog product id as input
    public CatalogProduct getCatalogProduct(int id)
    {
       for (CatalogProduct product : catalogProducts)
       { 		      
          if(product.getId() == id) return product;   		
       }
       return null;
    }
    
    // add a catalog product to the catalog product list given catalog product object as input
    public boolean addCatalogProduct(CatalogProduct p)
    {
      if(catalogProducts.size() == 0)
      {
    	 catalogProducts = new ArrayList<CatalogProduct>();
    	 catalogProducts.add(p);
         return true;  
      }
      
      for (CatalogProduct product : catalogProducts)
   	  { 		      
        if(p.getId() == product.getId()) return false;   		
      }
      catalogProducts.add(p);
      return true;
    }
    
    // remove a catalog product from the catalog product list given catalog product id as input
    public boolean removeCatalogProduct(int id)
    {
       for (CatalogProduct product : catalogProducts)
       { 		      
          if(product.getId() == id)
          {
        	  catalogProducts.remove(product);
        	  return true;
          }
       }
       return false;
    }
    
    // get a custom product from the custom product list given custom product id as input
    public CustomProduct getCustomProduct(int id)
    {
       for (CustomProduct product : customProducts)
       { 		      
          if(product.getId() == id) return product;   		
       }
       return null;
    }
    
    // add a custom product to the custom product list given custom product object as input
    public boolean addCustomProduct(CustomProduct p)
    {
      if(customProducts.size() == 0)
      {
      	customProducts = new ArrayList<CustomProduct>();
      	customProducts.add(p);
        return true;  
      }
      
      for (CustomProduct product : customProducts)
   	  { 		      
        if(p.getId() == product.getId()) return false;   		
      }
      customProducts.add(p);
      return true;
    }
    
    // remove a custom product from the custom product list given custom product id as input
    public boolean removeCustomProduct(int id)
    {
       for (CustomProduct product : customProducts)
       { 		      
          if(product.getId() == id)
          {
        	  customProducts.remove(product);
        	  return true;
          }
       }
       return false;
    }
    
    // continuation of getters and setters for private fields
    
	public String getCongrat() { return congrat; }
	public void setCongrat(String c) { congrat = c; }
		  
	public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerIdNumber) { customerId = customerIdNumber; }
	
	public String getPaymnetDetails() { return paymentDetails; }
	public void setPaymentDetails(String p) { paymentDetails = p; }
		  
	public Date getPurchaseDate() { return purchaseDate; }
	public void setPurchaseDate(Date dt) { purchaseDate = dt; }
		  
	public Time getPurchaseTime() { return purchaseTime; }
	public void setPurchaseTime(Time tm) { purchaseTime = tm; }
	
	public boolean getPickUpType() { return pickUpType; }
	public void setPickUpType(boolean p) { pickUpType = p; }
	
	public String getDeliveryAddress() { return deliveryAddress; }
	public void setDeliveryAddress(String address) { deliveryAddress = address; }
	
	public int getRecieverId() { return recieverId; }
    public void setRecieverId(int recieverIdNumber) { recieverId = recieverIdNumber; }
    
    public String getRecieverName() { return recieverName; }
	public void setRecieverName(String name) { recieverName = name; }
	
	public String getRecieverPhone() { return recieverPhone; }
	public void setRecieverPhone(String phone) { recieverPhone = phone; }
	
	public double getShippingCost() { return shippingCost; }
	public void setShippingCost(double cost) { shippingCost = cost; }
	
	public boolean getOrderType() { return orderType; }
	public void setOrderType(boolean t) { orderType = t; }
	
	public boolean getStatus() { return status; }
	public void setStatus(boolean s) { status = s; }
	
	public String getRegretRequest() { return regretRequest; }
	public void setRegretRequest(String r) { regretRequest = r; }
	
	public String getRegretResponse() { return regretResponse; }
	public void setRegretResponse(String r) { regretResponse = r; }
}
