/**
 * Sample Skeleton for 'CustomerCart.fxml' Controller Class
 */

package application;

import java.net.URL;
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


public class CustomerCartController  implements Initializable
{
	  @FXML // fx:id="bodyTxt"
	  private Label bodyTxt; // Value injected by FXMLLoader

	  @FXML // fx:id="backBtn"
	  private Button backBtn; // Value injected by FXMLLoader

	  @FXML // fx:id="CheckOutBtn"
	  private Button CheckOutBtn; // Value injected by FXMLLoader
	  
	  @FXML
	  private Label titleTxt;  // Value injected by FXMLLoader
	  
	  @FXML // fx:id="deleteProductBtn"
	   private Button deleteProductBtn; // Value injected by FXMLLoader

	  @FXML // fx:id="numberTxt"
	  private TextField numberTxt; // Value injected by FXMLLoader
	  
	  @FXML // fx:id="checkTxt"
	  private Label checkTxt; // Value injected by FXMLLoader

	  @FXML
	  void deleteProductAction(ActionEvent event)
	  {
		  if(bodyTxt.getText().isEmpty())
		  {
			  checkTxt.setText("The cart is empty");
		  }
		  else if(tryParsePositiveInt(numberTxt.getText()) == false)
		  {
			  checkTxt.setText("Invalid product number");
		  }
		  else
		  {
			  int num = Integer.parseInt(numberTxt.getText());
			  String[] tokens = bodyTxt.getText().trim().split("\n");
			  StringBuilder sb = new StringBuilder();
			  
			  if(num > tokens.length)
			  {
				  checkTxt.setText("Invalid product number"); 
			  }
			  else
			  {
			     for(int i = 0; i < tokens.length; i++)
			     {
			    	 if(i != num-1)
			    	 {
			    		 sb.append(tokens[i] + "\n");
			    	 }
			     }
			     bodyTxt.setText(sb.toString());
			  }
		  }
			  
	  }

	  @FXML
	  void checkOutAction(ActionEvent event) throws Exception
	  {
		  FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerCheckout.fxml"));
	      AnchorPane pane = loader.load();
	      CustomerCheckoutController cs = (CustomerCheckoutController)loader.getController();
	      cs.initializeInfo(titleTxt.getText());
	      cs.setCartProducts(bodyTxt.getText());
	      Scene scene = new Scene(pane);
	      Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
	      window.setScene(scene);
	      window.show();
	  }

	  @FXML
	  void backAction(ActionEvent event) throws Exception
	  {
		  FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomersSection.fxml"));
	      AnchorPane pane = loader.load();
	      CustomerSectionController cs = (CustomerSectionController)loader.getController();
	      cs.initializeInfo(titleTxt.getText());
	      if(bodyTxt.getText() == null)
	      {
	    	  cs.setCartProducts("");
	      }
	      else
	      {
	    	  String[] tokens = bodyTxt.getText().trim().split("\\s++");
		      if(tokens[0].contentEquals("Order"))
		      {
		    	  cs.setCartProducts("");
		      }
		      else
		      {
		    	  cs.setCartProducts(bodyTxt.getText());  
		      }  
	      }
	      
	      Scene scene = new Scene(pane);
	      Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
	      window.setScene(scene);
	      window.show();
	  }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	
	public void setCartsProducts(String str) {
		bodyTxt.setText(str);
	}
	
	public void initializeTitle(String str) {
		titleTxt.setText(str);	
	}
	
	private boolean tryParsePositiveInt(String value) {  
		 try
		 {  
		    int x = Integer.parseInt(value); 
		    if(x > 0)
		    {
		      return true;
		    }
		    else
		    {
		    	return false;
		    }
		      
		 }
		 catch (NumberFormatException e)
		 {  
		    return false;  
		 }  
    }
}
