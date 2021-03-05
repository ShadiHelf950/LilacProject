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

public class CustomerCancellationController  implements Initializable{

	@FXML // fx:id="senderTxt"
    private TextField senderTxt; // Value injected by FXMLLoader

    @FXML // fx:id="loginLbl"
    private Label loginLbl; // Value injected by FXMLLoader

    @FXML // fx:id="backBtn"
    private Button backBtn; // Value injected by FXMLLoader

    @FXML // fx:id="orderIdTxt"
    private TextField orderIdTxt; // Value injected by FXMLLoader

    @FXML // fx:id="responseTxt"
    private TextField responseTxt; // Value injected by FXMLLoader

    @FXML // fx:id="checkTxt"
    private Label checkTxt; // Value injected by FXMLLoader

    @FXML // fx:id="responseBtn"
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
       if(tryParseUnsignedInt(orderIdTxt.getText()) == true)
       {
    	   LilacApp.client.accept("cancel order " + senderTxt.getText() + " " + orderIdTxt.getText());
           String response = LilacApp.client.getResponse();
           
           
           if(response.contentEquals("Invalid Cancellation"))
           {
        	   checkTxt.setText("Error! either ID is wrong or you dont have a refund"); 
           }
           else
           {
        	   responseTxt.setText(response);
           }
       }
       else
       {
    	   checkTxt.setText("Error! enter a valid response ID");
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