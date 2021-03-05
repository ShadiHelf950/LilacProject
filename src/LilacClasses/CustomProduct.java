package LilacClasses;

import java.util.*;

public class CustomProduct extends Product
{
  private List<Flower> flowers;
  
  public CustomProduct(int idNumber, String d, double p, String t)
  {
	  super(idNumber,p,t);
	  flowers = new ArrayList<Flower>();
  }
  
  // add a flower type to the flowers list given flower object as input
  public boolean addFlowerType(Flower f)
  {
	  for (Flower flower : flowers)
	  { 		      
        if(flower.getId() == f.getId()) return false;   		
      }
      flowers.add(f);
      return true;
  }
  
  // remove a flower type from the flowers list given flower id as input
  public boolean removeFlowerType(int id)
  {
	 for (Flower flower : flowers)
	 { 		      
       if(flower.getId() == id)
       {
    	   flowers.remove(flower);
    	   return true;   		
       }
     }
	 return false;
  }
  
  // get a flower type from the flowers list given flower id as input
  public Flower getFlowerType(int id)
  {
	 for (Flower flower : flowers)
	 { 		      
       if(flower.getId() == id) return flower;   		
     }
	 return null;
  }
}
