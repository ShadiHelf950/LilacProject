/**
 * Sample Skeleton for 'CustomerCheckout.fxml' Controller Class
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

public class EmployeeSaleController implements Initializable {

	@FXML // fx:id="titleTxt"
    private Label titleTxt; // Value injected by FXMLLoader

    @FXML // fx:id="checkTxt"
    private Label checkTxt; // Value injected by FXMLLoader

    @FXML // fx:id="idTxt"
    private TextField idTxt; // Value injected by FXMLLoader

    @FXML // fx:id="backBtn"
    private Button backBtn; // Value injected by FXMLLoader

    @FXML // fx:id="saleBtn"
    private Button saleBtn; // Value injected by FXMLLoader

    @FXML // fx:id="getDetailsBtn"
    private Button getDetailsBtn; // Value injected by FXMLLoader

    @FXML // fx:id="catalogIdsLbl"
    private Label catalogIdsLbl; // Value injected by FXMLLoader

    @FXML // fx:id="nameLbl"
    private Label nameLbl; // Value injected by FXMLLoader

    @FXML // fx:id="priceLbl"
    private Label priceLbl; // Value injected by FXMLLoader

    @FXML // fx:id="typeLbl"
    private Label typeLbl; // Value injected by FXMLLoader

    @FXML // fx:id="discountTxt"
    private TextField discountTxt; // Value injected by FXMLLoader

    @FXML
    void backAction(ActionEvent event) throws Exception
    {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("EmployeesSection.fxml"));
    	AnchorPane pane = loader.load();
    	EmployeeSectionController cs = (EmployeeSectionController)loader.getController();
    	cs.initializeInfo(titleTxt.getText());
    	Scene scene = new Scene(pane);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
    

    @FXML
    void saleAction(ActionEvent event) throws Exception
    {
    	int flag = 0;
    	String[] tokens = catalogIdsLbl.getText().trim().split("\\,");
    	for(int i = 0; i < tokens.length; i++)
        {
     	   if(idTxt.getText().contentEquals(tokens[i]) == true)
     	   {
     		   flag = 1;
     		   break;
     	   }
        }
    	
    	if(flag == 1)
    	{
    		if(tryParseValidDiscount(discountTxt.getText()) == true)
    		{
    			
    			LilacApp.client.accept("sale product " + idTxt.getText() + " " + discountTxt.getText());
    	        String response = LilacApp.client.getResponse();
    	           
    	        
    	        checkTxt.setText("Product No. " + response + " was discounted successfully");
    		}
    		else
    		{
    			checkTxt.setText("Error! Invalid discount value was entered");
    		}
    	}
    	else
    	{
    		checkTxt.setText("Error! Invalid ID number was entered");
    	}
    }

    @FXML
    void getDetailsAction(ActionEvent event) throws Exception
    {
       int flag = 0;
       String[] tokens = catalogIdsLbl.getText().trim().split("\\,");
       for(int i = 0; i < tokens.length; i++)
       {
    	   if(idTxt.getText().contentEquals(tokens[i]) == true)
    	   {
    		   flag = 1;
    		   break;
    	   }
       }
       if(flag == 1)
       {
    	   
    	
    	   LilacApp.client.accept("get product " + idTxt.getText());
           String response = LilacApp.client.getResponse();
           
           
           String[] temp = response.trim().split("\\,");
           
           nameLbl.setText(temp[0]);
           priceLbl.setText(temp[1]);
           typeLbl.setText(temp[2]);
       }
       else
       {
    	 checkTxt.setText("Error! Invalid ID number was entered");   
       }
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		
			
			LilacApp.client.accept("get ids");
		    String response="";
			try {
				response = LilacApp.client.getResponse();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		        
		     
		    String x = response.substring(0, response.length()-1);
		    
		    catalogIdsLbl.setText(x);
		
		
		
       
	}
	
	public void initializeInfo(String str) {
		titleTxt.setText(str);	
	}
	
	private boolean tryParseValidDiscount(String value)
	{  
	 try
	 {  
	    int x = Integer.parseInt(value);
	    
	    if(x >= 1 && x <= 100)
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
