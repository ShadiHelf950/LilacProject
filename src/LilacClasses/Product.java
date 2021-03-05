package LilacClasses;

public abstract class Product
{
  protected int id;
  protected double price;
  protected String type; // this field is from type (enum Type)
					  
  public Product(int idNumber,  double p, String t)
  {
	 id = idNumber;
	 price = p;
	 type = t;
   }
					  
   // getters and setters for private fields
			  
	public int getId() { return id; }
    public void setId(int idNumber) { id = idNumber; }

			  
	public double getPrice() { return price; }
	public void setPrice(double p) { price = p; }
		  
	public String getType() { return type; }
	public void setImage(String t) { type = t; }
		  
}
