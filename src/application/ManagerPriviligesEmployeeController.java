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

public class ManagerPriviligesEmployeeController implements Initializable {

	 @FXML // fx:id="managerLbl"
	    private Label managerLbl; // Value injected by FXMLLoader

	    @FXML // fx:id="checkTxt"
	    private Label checkTxt; // Value injected by FXMLLoader

	    @FXML // fx:id="idTxt"
	    private TextField idTxt; // Value injected by FXMLLoader

	    @FXML // fx:id="backBtn"
	    private Button backBtn; // Value injected by FXMLLoader

	    @FXML // fx:id="privilegesBtn"
	    private Button privilegesBtn; // Value injected by FXMLLoader

	    @FXML // fx:id="getDetailsBtn"
	    private Button getDetailsBtn; // Value injected by FXMLLoader

	    @FXML // fx:id="employeesIdsLbl"
	    private Label employeesIdsLbl; // Value injected by FXMLLoader

	    @FXML // fx:id="nameLbl"
	    private Label nameLbl; // Value injected by FXMLLoader

	    @FXML // fx:id="privilegesLbl"
	    private Label privilegesLbl; // Value injected by FXMLLoader

	    @FXML // fx:id="storeLbl"
	    private Label storeLbl; // Value injected by FXMLLoader

	    @FXML // fx:id="privilegesTxt"
	    private TextField privilegesTxt; // Value injected by FXMLLoader

	    @FXML // fx:id="getEmployeesBtn"
	    private Button getEmployeesBtn; // Value injected by FXMLLoader

	    @FXML // fx:id="fireEmployeeBtn"
	    private Button fireEmployeeBtn; // Value injected by FXMLLoader
    
	@FXML
	void fireEmployeeAction(ActionEvent event) throws Exception
	{
		int flag = 0;
		
		if(employeesIdsLbl.getText().isEmpty() == false)
		{
			String[] tokens = employeesIdsLbl.getText().trim().split("\\,");
			   
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
	 
	    		LilacApp.client.accept("fire employee " + idTxt.getText());
	    	    String response = LilacApp.client.getResponse();
	    	           
	    	        
	    	     checkTxt.setText("Employee No. " + response + " was disabled successfully");
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
    void getEmployeesAction(ActionEvent event) throws Exception
    {
 

    		LilacApp.client.accept("get emp " + managerLbl.getText());
		    String response = LilacApp.client.getResponse();
		        
		     
		    String x = response.substring(0, response.length()-1);
		    
		    employeesIdsLbl.setText(x);
		
		
	
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
    void privilegesAction(ActionEvent event) throws Exception
    {
    	int flag = 0;
    	
      if(employeesIdsLbl.getText().isEmpty() == false)
	  {
    	String[] tokens = employeesIdsLbl.getText().trim().split("\\,");
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
    		if(privilegesTxt.getText().contentEquals("Complaints,Catalog") == true ||
    		   privilegesTxt.getText().contentEquals("Catalog,Complaints") == true ||
    		   privilegesTxt.getText().contentEquals("Complaints") == true ||
    		   privilegesTxt.getText().contentEquals("Catalog") == true)
    		{
   
    			LilacApp.client.accept("change employee " + idTxt.getText() + " " + privilegesTxt.getText());
    	        String response = LilacApp.client.getResponse();
    	           
    	        
    	        checkTxt.setText("Employee No. " + response + " was updated successfully");
    		}
    		else
    		{
    			checkTxt.setText("Error! Invalid privilege value was entered");
    		}
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
    void getDetailsAction(ActionEvent event) throws Exception
    {
       int flag = 0;
       
      if(employeesIdsLbl.getText().isEmpty() == false)
 	 {
       String[] tokens = employeesIdsLbl.getText().trim().split("\\,");
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

    	   LilacApp.client.accept("get employee " + idTxt.getText());
           String response = LilacApp.client.getResponse();
           
           
           String[] temp = response.trim().split("-");
           
           nameLbl.setText(temp[0]);
           privilegesLbl.setText(temp[1]);
           storeLbl.setText(temp[2]);
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
