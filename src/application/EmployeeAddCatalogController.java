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

public class EmployeeAddCatalogController implements Initializable {

	 @FXML // fx:id="titleTxt"
	    private Label titleTxt; // Value injected by FXMLLoader

	    @FXML // fx:id="checkTxt"
	    private Label checkTxt; // Value injected by FXMLLoader

	    @FXML // fx:id="nameTxt"
	    private TextField nameTxt; // Value injected by FXMLLoader

	    @FXML // fx:id="priceTxt"
	    private TextField priceTxt; // Value injected by FXMLLoader

	    @FXML // fx:id="backBtn"
	    private Button backBtn; // Value injected by FXMLLoader

	    @FXML // fx:id="addBtn"
	    private Button addBtn; // Value injected by FXMLLoader

	    @FXML // fx:id="typeTxt"
	    private TextField typeTxt; // Value injected by FXMLLoader

	    @FXML
	    void addAction(ActionEvent event) throws Exception
	    {
	    	int flag1 = 0, flag2 = 0, flag3 = 0;
	        String str = nameTxt.getText();
	        int count = 0;
	        	
	        if(str.isEmpty() == false)
	        {
	        	for(int i = 0; i < str.length(); i++)
	            { 
	                if((str.charAt(i) >= 'a' && str.charAt(i) <= 'z') ||
	                   (str.charAt(i) >= 'A' && str.charAt(i) <= 'Z'))
	                {
	                	count++;
	                }
	            }
	        	if(count == str.length())
	        	{
	        		flag1 = 1;
	        	}
	        	}
	        	
	        	if(typeTxt.getText().contentEquals("Set") == true ||
	        	   typeTxt.getText().contentEquals("Bridal") == true ||
	        	   typeTxt.getText().contentEquals("Arrangements") == true || 
	        	   typeTxt.getText().contentEquals("Flowerpot") == true)
	        	{
	        		flag2 = 1;
	        	}
	        	
	        	if(tryParseValidDouble(priceTxt.getText()) == true)
	        	{
	        		flag3 = 1;
	        	}
	        	
	        	if(flag1 == 1 && flag2 == 1 && flag3 == 1)
	        	{
	        		
	      
	        		LilacApp.client.accept("add product " + nameTxt.getText()+ " " + priceTxt.getText()
	    		    + " " + typeTxt.getText());
	    		    String response = LilacApp.client.getResponse();
	    		        
	    		    
	    		    if(response.contentEquals("Unsuccessful Addition"))
	    		    {
	    		    	checkTxt.setText("Error! A product with the same name already exists");	
	    		    }
	    		    else
	    		    {
	    		    	checkTxt.setText("Product No. " + response + " was added successfully");
	    		    }
	        	}
	        	else
	        	{
	        		checkTxt.setText("Error! At least one field must be valid");
	        	}
	    }

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

  
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		
	}
	
	public void initializeInfo(String str) {
		titleTxt.setText(str);	
	}
	
	private boolean tryParseValidDouble(String value)
	{  
	 try
	 {  
	    double x = Double.parseDouble(value);
	    
	    if(x >= 1)
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
