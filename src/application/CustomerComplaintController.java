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


public class CustomerComplaintController  implements Initializable{

    @FXML // fx:id="senderTxt"
    private TextField senderTxt; // Value injected by FXMLLoader

    @FXML // fx:id="descriptionTxt"
    private TextField descriptionTxt; // Value injected by FXMLLoader

    @FXML // fx:id="loginLbl"
    private Label loginLbl; // Value injected by FXMLLoader

    @FXML // fx:id="sendBtn"
    private Button sendBtn; // Value injected by FXMLLoader

    @FXML // fx:id="backBtn"
    private Button backBtn; // Value injected by FXMLLoader

    @FXML // fx:id="responseIdTxt"
    private TextField responseIdTxt; // Value injected by FXMLLoader

    @FXML // fx:id="responseTxt"
    private TextField responseTxt; // Value injected by FXMLLoader

    @FXML // fx:id="checkTxt"
    private Label checkTxt; // Value injected by FXMLLoader
    
    @FXML
    private Button responseBtn; // Value injected by FXMLLoader
    
    private String cartsProduct; 

    @FXML
    void backAction(ActionEvent event) throws Exception
    {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomersSection.fxml"));
    	AnchorPane pane = loader.load();
    	CustomerSectionController cs = (CustomerSectionController)loader.getController();
    	cs.initializeInfo(senderTxt.getText());
    	cs.setCartProducts(cartsProduct);
    	Scene scene = new Scene(pane);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML
    void getResponse(ActionEvent event) throws Exception
    {
       if(tryParseUnsignedInt(responseIdTxt.getText()) == true)
       {
    	   
    	   LilacApp.client.accept("response customer " + senderTxt.getText() + " " + responseIdTxt.getText());
           String response = LilacApp.client.getResponse();
           
           
           if(response.contentEquals("Invalid Response ID"))
           {
        	   checkTxt.setText("Error! enter a valid response ID"); 
           }
           else
           {
        	   responseTxt.setText(response);
        	   checkTxt.setText("Response was retrieved successfully");
           }
       }
       else
       {
    	   checkTxt.setText("Error! enter a valid response ID");
       }
    }
    
    @FXML
    void sendComplaint(ActionEvent event) throws Exception
    {
       if(descriptionTxt.getText().isEmpty() == false)
       {
    	  
    	   LilacApp.client.accept("complaint customer " + senderTxt.getText() + " " + descriptionTxt.getText());
           String response = LilacApp.client.getResponse();
           
           checkTxt.setText("Complaint number " + response + " was sent successfully");
       }
       else
       {
    	   checkTxt.setText("Error! Description field should not be empty");
       }
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	
	public void initializeInfo(String str) {
		senderTxt.setText(str);	
	}
	
	public void setCartsProducts(String str) {
		cartsProduct = str;	
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