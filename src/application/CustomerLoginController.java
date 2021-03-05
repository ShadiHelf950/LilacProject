/**
 * Sample Skeleton for 'CustomerLogin.fxml' Controller Class
 */

package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CustomerLoginController {

	@FXML // fx:id="userNameTxt"
    private TextField userNameTxt; // Value injected by FXMLLoader

    @FXML // fx:id="passwordTxt"
    private TextField passwordTxt; // Value injected by FXMLLoader

    @FXML // fx:id="loginLbl"
    private Label loginLbl; // Value injected by FXMLLoader

    @FXML // fx:id="CustomerLoginBtn"
    private Button CustomerLoginBtn; // Value injected by FXMLLoader

    @FXML // fx:id="HomeBtn"
    private Button HomeBtn; // Value injected by FXMLLoader

    @FXML // fx:id="createBtn"
    private Button createBtn; // Value injected by FXMLLoader

    @SuppressWarnings("static-access")
	@FXML
    void LoginCustomer(ActionEvent event) throws Exception
    {
      if(userNameTxt.getText().isEmpty() == false && passwordTxt.getText().isEmpty() == false)
      {
    	String[] temp = userNameTxt.getText().trim().split("\\s++");
    	if(temp.length == 2)
    	{
    
    		LilacApp.client.accept("login customer " + userNameTxt.getText() + " " + passwordTxt.getText());
            
            String response = LilacApp.client.getResponse();
         
           
            
            if(response.contentEquals("Not Successful Customer login"))
            {
              loginLbl.setText("Invalid Login! Fields are not correct or customer already logged in");
              return;
            }
            else if(response.contentEquals("Successful Customer login"))
            {
            	FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomersSection.fxml"));
            	AnchorPane pane = loader.load();
            	CustomerSectionController cs = (CustomerSectionController)loader.getController();
            	cs.initializeInfo(userNameTxt.getText());
            	Scene scene = new Scene(pane);
                Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
    	}
    	else
    	{
    		loginLbl.setText("Error! Make sure that all the fields are not empty");
    	}
        
      }
      else
      {
    	  loginLbl.setText("Error! Make sure that all the fields are not empty");
      }
    	    
    }
    
    @FXML
    void backHome(ActionEvent event) throws Exception
    {
    	AnchorPane pane = FXMLLoader.load(getClass().getResource("LilacScene.fxml"));
    	Scene scene = new Scene(pane);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
    
    @FXML
    void createAccount(ActionEvent event) throws Exception
    {
    	AnchorPane pane = FXMLLoader.load(getClass().getResource("CustomersRegistration.fxml"));
    	Scene scene = new Scene(pane);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}