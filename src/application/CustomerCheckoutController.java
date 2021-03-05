/**
 * Sample Skeleton for 'CustomerCheckout.fxml' Controller Class
 */

package application;

import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class CustomerCheckoutController implements Initializable {

	@FXML // fx:id="titleTxt"
    private Label titleTxt; // Value injected by FXMLLoader

    @FXML // fx:id="nameTxt"
    private TextField nameTxt; // Value injected by FXMLLoader

    @FXML // fx:id="checkTxt"
    private Label checkTxt; // Value injected by FXMLLoader

    @FXML // fx:id="congratTxt"
    private TextField congratTxt; // Value injected by FXMLLoader

    @FXML // fx:id="storeTxt"
    private TextField storeTxt; // Value injected by FXMLLoader

    @FXML // fx:id="recieverTxt"
    private TextField recieverTxt; // Value injected by FXMLLoader

    @FXML // fx:id="pickUpTxt"
    private TextField pickUpTxt; // Value injected by FXMLLoader

    @FXML // fx:id="backBtn"
    private Button backBtn; // Value injected by FXMLLoader

    @FXML // fx:id="payBtn"
    private Button payBtn; // Value injected by FXMLLoader

    @FXML // fx:id="deliveryTxt"
    private TextField deliveryTxt; // Value injected by FXMLLoader

    @FXML // fx:id="phoneTxt"
    private TextField phoneTxt; // Value injected by FXMLLoader

    @FXML // fx:id="orderTypeTxt"
    private TextField orderTypeTxt; // Value injected by FXMLLoader

    @FXML // fx:id="priceTxt"
    private Label priceTxt; // Value injected by FXMLLoader

    @FXML // fx:id="priceBtn"
    private Button priceBtn; // Value injected by FXMLLoader

    @FXML // fx:id="paymentTxt"
    private TextField paymentTxt; // Value injected by FXMLLoader

    @FXML // fx:id="arrivalDateTxt"
    private TextField arrivalDateTxt; // Value injected by FXMLLoader
    
    private String cartsProducts;

    @FXML
    void backAction(ActionEvent event) throws Exception
    {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerCart.fxml"));
	    AnchorPane pane = loader.load();
	    CustomerCartController cs = (CustomerCartController)loader.getController();
	    cs.initializeTitle(titleTxt.getText());
	    cs.setCartsProducts(cartsProducts);
	    Scene scene = new Scene(pane);
	    Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
	    window.setScene(scene);
	    window.show();
    }

    @FXML
    void payAction(ActionEvent event) throws Exception
    {
        double price = -1;
        String d;
    	
    	if(priceTxt.getText().isEmpty() == false)
        {
        	String[] tokens = priceTxt.getText().trim().split("\\s++");
        	price = Double.parseDouble(tokens[tokens.length-1]);
        }
        else
        {
           checkTxt.setText("Error! one or more fields are not valid");
     	   return;
        }
        
    	String c;
    	if(congratTxt.getText().isEmpty() == true)
    	{
    		c = "-1";
    	}
    	else
    	{
    		c = congratTxt.getText();
    	}
    	
    	if(nameTxt.getText().contentEquals(titleTxt.getText()) == false)
    	{
    	   checkTxt.setText("Error! one or more fields are not valid");
    	   return;
    	}
    	
    	if((storeTxt.getText().contentEquals("LilacHaifa") == false) &&
    	    	   (storeTxt.getText().contentEquals("LilacTelAviv") == false))
    	{
    		checkTxt.setText("Error! one or more fields are not valid");
     	    return;
    	}
    	
    	if((paymentTxt.getText().contentEquals("Visa") == false) &&
 	      (paymentTxt.getText().contentEquals("Subscription") == false))
 	    {
 		   checkTxt.setText("Error! one or more fields are not valid");
  	       return;
 	    }
    	
    	if((orderTypeTxt.getText().contentEquals("Quick") == false) &&
 	    	   ((orderTypeTxt.getText().contentEquals("Normal") == false)))
 	    {
 		   checkTxt.setText("Error! one or more fields are not valid");
  	       return;
 	    }
    	
    	if(arrivalDateTxt.getText().isEmpty() == false && orderTypeTxt.getText().contentEquals("Quick"))
    	{
    	   checkTxt.setText("Error! one or more fields are not valid");
     	   return;
    	}
    	
    	if((isDateValid(arrivalDateTxt.getText()) == false) && orderTypeTxt.getText().contentEquals("Normal"))
    	{
    	   checkTxt.setText("Error! one or more fields are not valid");
     	   return;
    	}
    	
    	if(arrivalDateTxt.getText().isEmpty() == true)
		{
			d = "-1";
		}
		else
		{
			d = arrivalDateTxt.getText();
		}
    	
    	if((pickUpTxt.getText().contentEquals("0") == false) &&
  	    	   ((pickUpTxt.getText().contentEquals("1") == false)))
  	    {
  		   checkTxt.setText("Error! one or more fields are not valid");
   	       return;
  	    }
     	
    	else
    	{
    		
    		if(pickUpTxt.getText().contentEquals("0") && recieverTxt.getText().isEmpty() == true &&
    	       deliveryTxt.getText().isEmpty() == true  && phoneTxt.getText().isEmpty() == true)
    	  	{
    			
    			
    			LilacApp.client.accept("order$" + nameTxt.getText() + "$" + c + "$" + pickUpTxt.getText() +
                "$" + storeTxt.getText() + "$" + orderTypeTxt.getText() + "$" + Double.toString(price)
                + "$" + cartsProducts + "$" + paymentTxt.getText() + "$" + d);
                String response = LilacApp.client.getResponse();
                
                
                
                if(response.contentEquals("Unsuccessful Order"))
                {
                	checkTxt.setText("Error! the order was not sent successfully");
    	   	    	return;
                }
                else
                {
                	FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerCart.fxml"));
                	AnchorPane pane = loader.load();
                	CustomerCartController cs = (CustomerCartController)loader.getController();
                	cs.initializeTitle(titleTxt.getText());
                	cs.setCartsProducts("Order No." + response + " was sent successfully");
                	Scene scene = new Scene(pane);
                    Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
                    window.setScene(scene);
                    window.show(); 
                }
                
    	  	}
    		
    		else if(pickUpTxt.getText().contentEquals("1") && recieverTxt.getText().isEmpty() == false &&
    	           deliveryTxt.getText().isEmpty() == false  && phoneTxt.getText().isEmpty() == false)
    	    {
    			String[] tokens = recieverTxt.getText().trim().split("\\s++");
    			if(tokens.length != 2)
    			{
    				checkTxt.setText("Error! one or more fields are not valid");
    	   	    	return;
    			}
    			if(phoneTxt.getText().length() != 9 || isNumber(phoneTxt.getText()) == false)
    			{
    				checkTxt.setText("Error! one or more fields are not valid");
    	   	    	return;
    			}
    			
    			LilacApp.client.accept("order$" + nameTxt.getText() + "$" + c + "$" + pickUpTxt.getText() +
                "$" + storeTxt.getText() + "$" + orderTypeTxt.getText() + "$" + Double.toString(price)
                + "$" + cartsProducts + "$" + paymentTxt.getText() + "$" + d
                + "$" + recieverTxt.getText() + "$" + deliveryTxt.getText() + "$" + phoneTxt.getText());
                String response = LilacApp.client.getResponse();
                
                
                if(response.contentEquals("Unsuccessful Order"))
                {
                	checkTxt.setText("Error! the order was not sent successfully");
    	   	    	return;
                }
                else
                {
                	FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerCart.fxml"));
                	AnchorPane pane = loader.load();
                	CustomerCartController cs = (CustomerCartController)loader.getController();
                	cs.initializeTitle(titleTxt.getText());
                	
                	if(nameTxt.getText().contentEquals(recieverTxt.getText()) == false)
                	{
                		cs.setCartsProducts("Order No." + response + " was sent successfully\n"
                        + "and you will recieve an SMS for your order on the phone number you sent");
                	}
                	else
                	{
                		cs.setCartsProducts("Order No." + response + " was sent successfully");
                	}
                	
                	Scene scene = new Scene(pane);
                    Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
                    window.setScene(scene);
                    window.show(); 
                }
    	    }
    		
    		else
    		{
    		   checkTxt.setText("Error! one or more fields are not valid");
   	    	   return;
    		}
    	}	 
    }
    
    @FXML
    void priceAction(ActionEvent event)
    {
    	if(cartsProducts.isEmpty() == true)
	    {
		   priceTxt.setText("Total Price is (Products Price) 0 + (Shipping Cost) 0 = 0");
		   return;
	    }
		
		double price = 0;
		String[] tokens = cartsProducts.trim().split("\n");
		String[] temp;
		for(int i = 0; i < tokens.length; i++)
		{
			temp = tokens[i].trim().split("\\s++");
			price += Double.parseDouble(temp[temp.length-1]);
		}
		price = Math.round(price);
		priceTxt.setText("Total Price is (Products Price) " + Double.toString(price) +
				          " + (Shipping Cost) 20 = " + Double.toString(price+20));
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		
	}
	
	public void initializeInfo(String str) {
		titleTxt.setText(str);	
	}
    
	public void setCartProducts(String str) {
		cartsProducts = str;	
	}
	
	private boolean isNumber(String str)
	{
		for(int i = 0; i < str.length(); i++)
		{
			char ch = str.charAt(i);
			if(ch < '0' || ch > '9')
			{
				return false;
			}
		}
		return true;
	}
	
	private boolean isDateValid(String date) 
	{
	        try {
	            DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
	            df.setLenient(false);
	            df.parse(date);
	            return true;
	        } catch (ParseException e) {
	            return false;
	        }
	}

}
