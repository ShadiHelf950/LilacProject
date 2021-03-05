/**
 * Sample Skeleton for 'CustomerComplaint.fxml' Controller Class
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

public class EmployeeComplaintController  implements Initializable{

	@FXML // fx:id="descriptionTxt"
    private TextField descriptionTxt; // Value injected by FXMLLoader

    @FXML // fx:id="checkTxt"
    private Label checkTxt; // Value injected by FXMLLoader

    @FXML // fx:id="getDescriptionTxt"
    private Button getDescriptionBtn; // Value injected by FXMLLoader

    @FXML // fx:id="backBtn"
    private Button backBtn; // Value injected by FXMLLoader

    @FXML // fx:id="responseTxt"
    private TextField responseTxt; // Value injected by FXMLLoader

    @FXML // fx:id="sendResponseBtn"
    private Button sendResponseBtn; // Value injected by FXMLLoader

    @FXML // fx:id="complaintsIdsLbl"
    private Label complaintsIdsLbl; // Value injected by FXMLLoader

    @FXML // fx:id="complaintIdTxt"
    private TextField complaintIdTxt; // Value injected by FXMLLoader

    @FXML // fx:id="employeeNameLbl"
    private Label employeeNameLbl; // Value injected by FXMLLoader
    
    @FXML
    void getDescriptionAction(ActionEvent event) throws Exception
    {
    	
    	if(tryParseUnsignedInt(complaintIdTxt.getText()) == true)
        {
    		int complaintId = Integer.parseInt(complaintIdTxt.getText());
    		int flag = 0;
    		String[] tokens = complaintsIdsLbl.getText().trim().split("\\,");
    		
    		for(int i = 0; i < tokens.length; i++)
    		{
    			if(Integer.parseInt(tokens[i]) == complaintId)
    			{
    				flag = 1;
    				break;
    			}
    		}
    		
    		if(flag == 1)
    		{

    			LilacApp.client.accept("complaint employee " + complaintIdTxt.getText());
                String response = LilacApp.client.getResponse();
                
                
                if(response.contentEquals("Invalid Complaint ID"))
                {
             	   checkTxt.setText("Error! enter a valid complaint ID"); 
                }
                else
                {
                   descriptionTxt.setText(response);
             	   checkTxt.setText("Complaint Description and sender were retrieved successfully");
                }	
    		}
    		else
    		{
    			checkTxt.setText("Error! enter a valid complaint ID");
    		}
        }
        else
        {
     	   checkTxt.setText("Error! enter a valid complaint ID");
        }
    }

    @FXML
    void sendResponseAction(ActionEvent event) throws Exception
    {
    	if(tryParseUnsignedInt(complaintIdTxt.getText()) == true)
        {
    		int complaintId = Integer.parseInt(complaintIdTxt.getText());
    		int flag = 0;
    		String[] tokens = complaintsIdsLbl.getText().trim().split("\\,");
    		
    		for(int i = 0; i < tokens.length; i++)
    		{
    			if(Integer.parseInt(tokens[i]) == complaintId)
    			{
    				flag = 1;
    				break;
    			}
    		}
    		
    		if(flag == 1)
    		{
    			if(responseTxt.getText().isEmpty() == true)
    	    	{
    				checkTxt.setText("Error! enter a valid response");
    				return;
    	    	}
    			else
    			{
    	
    				LilacApp.client.accept("response,employee," + complaintIdTxt.getText() + "," + responseTxt.getText());
                    String response = LilacApp.client.getResponse();
                    
                 	checkTxt.setText("Response for complaint No. "+ response + " was set successfully");
                }		
    		}
    		else
        	{
        		checkTxt.setText("Error! enter a valid complaint ID");
        	}
    	}
    	
    	else
    	{
    		checkTxt.setText("Error! enter a valid complaint ID");
    	}
     }
    
    
    @FXML
    void backAction(ActionEvent event) throws Exception
    {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("EmployeesSection.fxml"));
    	AnchorPane pane = loader.load();
    	EmployeeSectionController cs = (EmployeeSectionController)loader.getController();
    	cs.initializeInfo(employeeNameLbl.getText());
    	Scene scene = new Scene(pane);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
 
	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
	    try
	    {
	    	LilacApp.client.accept("get complaints");
		  String response = LilacApp.client.getResponse();
	      complaintsIdsLbl.setText(response);
		} 	
	    catch (Exception e)
	    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void initializeInfo(String str) {
		employeeNameLbl.setText(str);	
	}
	
	private boolean tryParseUnsignedInt(String value)
	{  
		 try
		 {  
		    int x = Integer.parseInt(value);
		    if(x > 0){
		    	return true;
		    }
		    return false;  
		 }
		 catch (NumberFormatException e)
		 {  
		    return false;  
		 }  
	}
}