
/**
 * Sample Skeleton for 'StaffRegistration.fxml' Controller Class
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
public class StaffRegistrationController {

    @FXML // fx:id="StaffLoginBtn"
    private Button StaffLoginBtn; // Value injected by FXMLLoader

    @FXML // fx:id="userNameTxt"
    private TextField userNameTxt; // Value injected by FXMLLoader

    @FXML // fx:id="passwordTxt"
    private TextField passwordTxt; // Value injected by FXMLLoader
    
    @FXML
    private Label loginLbl; // Value injected by FXMLLoader
    
    @FXML // fx:id="HomeBtn"
    private Button HomeBtn; // Value injected by FXMLLoader
    
    @FXML
    void LoginStaff(ActionEvent event) throws Exception
    {
      if(userNameTxt.getText().isEmpty() == false && passwordTxt.getText().isEmpty() == false)
      {
    	AnchorPane pane = null;
    
        LilacApp.client.accept("login staff " + userNameTxt.getText() + " " + passwordTxt.getText());
        String response = LilacApp.client.getResponse();
        
        if(response.contentEquals("Not Successful Staff login"))
        {
          loginLbl.setText("Invalid Login! Fields are not correct or member already logged in");
          return;
        }
        if(response.contentEquals("Successful Manager login"))
        {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("ManagersSection.fxml"));
        	pane = loader.load();
        	ManagerSectionController cs = (ManagerSectionController)loader.getController();
        	cs.initializeInfo(userNameTxt.getText());
        	Scene scene = new Scene(pane);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
        if(response.contentEquals("Successful Employee login"))
        {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("EmployeesSection.fxml"));
        	pane = loader.load();
        	EmployeeSectionController cs = (EmployeeSectionController)loader.getController();
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
    
    @FXML
    void backHome(ActionEvent event) throws Exception
    {
    	AnchorPane pane = FXMLLoader.load(getClass().getResource("LilacScene.fxml"));
    	Scene scene = new Scene(pane);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

}
