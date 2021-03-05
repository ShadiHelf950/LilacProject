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

public class ManagerFreezeCustomerController implements Initializable {

	@FXML // fx:id="managerLbl"
    private Label managerLbl; // Value injected by FXMLLoader

    @FXML // fx:id="checkTxt"
    private Label checkTxt; // Value injected by FXMLLoader

    @FXML // fx:id="idTxt"
    private TextField idTxt; // Value injected by FXMLLoader

    @FXML // fx:id="backBtn"
    private Button backBtn; // Value injected by FXMLLoader

    @FXML // fx:id="getDetailsBtn"
    private Button getDetailsBtn; // Value injected by FXMLLoader

    @FXML // fx:id="employeesIdsLbl"
    private Label customersIdsLbl; // Value injected by FXMLLoader

    @FXML // fx:id="IdLbl"
    private Label IdLbl; // Value injected by FXMLLoader

    @FXML // fx:id="nameLbl"
    private Label nameLbl; // Value injected by FXMLLoader

    @FXML // fx:id="storeLbl"
    private Label storeLbl; // Value injected by FXMLLoader

    @FXML // fx:id="getCustomersBtn"
    private Button getCustomersBtn; // Value injected by FXMLLoader

    @FXML // fx:id="freezeCustomerBtn"
    private Button freezeCustomerBtn; // Value injected by FXMLLoader

    @FXML // fx:id="creditCardNumberLbl"
    private Label creditCardNumberLbl; // Value injected by FXMLLoader

    @FXML // fx:id="expirationDateLbl"
    private Label expirationDateLbl; // Value injected by FXMLLoader

    @FXML // fx:id="subscriptionLbl"
    private Label subscriptionLbl; // Value injected by FXMLLoader

    @FXML
    void getCustomersAction(ActionEvent event) throws Exception
    {
    	

    		LilacApp.client.accept("get cus " + managerLbl.getText());
		    String response = LilacApp.client.getResponse();
		        
		     
		    String x = response.substring(0, response.length()-1);
		    
		    customersIdsLbl.setText(x);
		
		
		
    }
    
	@FXML
	void freezeCustomerAction(ActionEvent event) throws Exception
	{
		int flag = 0;
		
		if(customersIdsLbl.getText().isEmpty() == false)
		{
			String[] tokens = customersIdsLbl.getText().trim().split("\\,");
			   
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
	    		
	    	    LilacApp.client.accept("freeze customer " + idTxt.getText());
	    	    String response =  LilacApp.client.getResponse();
	    	           
	    	        
	    	     checkTxt.setText("Customer No. " + response + " was frozen successfully");
	    	}
	    	else
	    	{
	    		checkTxt.setText("Error! Invalid ID number was entered");
	    	}
		}
		else
		{
			checkTxt.setText("Error! No valid Ids to choose from");
		}		    	
    }
    
    @FXML
    void backAction(ActionEvent event) throws Exception
    {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("ManagersSection.fxml"));
    	AnchorPane pane = loader.load();
    	ManagerSectionController cs = (ManagerSectionController)loader.getController();
    	cs.initializeInfo(managerLbl.getText());
    	Scene scene = new Scene(pane);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
    
    @FXML
    void getDetailsAction(ActionEvent event) throws Exception
    {
       int flag = 0;
       
      if(customersIdsLbl.getText().isEmpty() == false)
 	 {
       String[] tokens = customersIdsLbl.getText().trim().split("\\,");
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
    	   
    	   LilacApp.client.accept("get customer " + idTxt.getText());
           String response =  LilacApp.client.getResponse();
           
           
           String[] temp = response.trim().split("-");
           
           nameLbl.setText(temp[0]);
           storeLbl.setText(temp[1]);
           creditCardNumberLbl.setText(temp[2]);
           expirationDateLbl.setText(temp[3]);
           subscriptionLbl.setText(temp[4]);
       }
       else
       {
    	 checkTxt.setText("Error! Invalid ID number was entered");   
       }
 	 }
     else
     {
    	 checkTxt.setText("Error! No valid Ids to choose from");  
     }
    }
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
	
	}
	
	public void initializeManager(String str) {
		managerLbl.setText(str);	
	}
}
