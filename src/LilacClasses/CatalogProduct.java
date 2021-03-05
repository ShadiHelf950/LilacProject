package LilacClasses;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

public class CatalogProduct extends Product
{
  private String name;
  private Image image;
  private String discount;

  public CatalogProduct(int idNumber,  double p, String t, String n,
		  Image im,String discount)
  {
	  super(idNumber,p,t);
	  name = n;
	  image = im;
	  this.discount=discount;
  }
  
//getters and setters for private fields
  
  public String getName() { return name; }
  public void setName(String n) { name = n; }
  
  
  public String getdiscount() { return discount; }
  public void setdiscount(String n) { this.discount = n; }
  
  public StringProperty discountProperty() {
      return new SimpleStringProperty(discount);
  }
  
  
  public Image getImage() {
	//  Image newImage = image.getScaledInstance(200, 200,1);

	  return image; }
  public void setImage(Image im) { image = im; }
  
  
}
