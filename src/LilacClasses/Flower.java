package LilacClasses;

public class Flower
{
  private int id;
  private String name;
  private double pricePerUnit;
  private int units;
  private String image;
  private String colors;
				  
	  public Flower(int idNumber, String Name, double price, int Units, String im, String c)
	  {
	    id = idNumber;
	    name = Name;
	    pricePerUnit = price;
	    units = Units;
	    image = im;
	    colors = c;
	  }
				  
	  // getters and setters for private fields
		  
	  public int getId() { return id; }
	  public void setId(int idNumber) { id = idNumber; }
		  
	  public String getName() { return name; }
	  public void setName(String Name) { name = Name; }
		  
	  public double getPricePerUnit() { return pricePerUnit; }
	  public void setPricePerUnit(double price) { pricePerUnit = price; }
	  
	  public int getUnits() { return units; }
	  public void setUnits(int Units) { units = Units; }
	  
	  public String getImage() { return image; }
	  public void setImage(String im) { image = im; }
	  
	  public String getColors() { return colors; }
	  public void setColors(String c) { colors = c; }
	  
}
