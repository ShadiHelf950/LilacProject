/**
 * Sample Skeleton for 'ManagersSection.fxml' Controller Class
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class ManagerSectionController implements Initializable{

	    @FXML // fx:id="titleTxt"
	    private Label titleTxt; // Value injected by FXMLLoader

	    @FXML // fx:id="viewEarningsBtn"
	    private Button viewEarningsBtn; // Value injected by FXMLLoader

	    @FXML // fx:id="viewOrdersBtn"
	    private Button viewOrdersBtn; // Value injected by FXMLLoader

	    @FXML // fx:id="viewComplaintsBtn"
	    private Button viewComplaintsBtn; // Value injected by FXMLLoader

	    @FXML // fx:id="changePrivilegesBtn"
	    private Button changePrivilegesBtn; // Value injected by FXMLLoader

	    @FXML // fx:id="fireEmplyeeBtn"
	    private Button fireEmplyeeBtn; // Value injected by FXMLLoader

	    @FXML // fx:id="logoutBtn"
	    private Button logoutBtn; // Value injected by FXMLLoader

	    @FXML // fx:id="freezeCustomerBtn"
	    private Button freezeCustomerBtn; // Value injected by FXMLLoader

	    @FXML
	    void changePrivilegesAction(ActionEvent event) throws Exception
	    {
	    
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("ManagerPriviligesEmployee.fxml"));
        	AnchorPane pane = loader.load();
        	ManagerPriviligesEmployeeController cs = (ManagerPriviligesEmployeeController)loader.getController();
        	cs.initializeManager(titleTxt.getText());
        	Scene scene = new Scene(pane);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
	    }

	    @FXML
	    void freezeCustomerAction(ActionEvent event) throws Exception
	    {
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("ManagerFreezeCustomer.fxml"));
        	AnchorPane pane = loader.load();
        	ManagerFreezeCustomerController cs = (ManagerFreezeCustomerController)loader.getController();
        	cs.initializeManager(titleTxt.getText());
        	Scene scene = new Scene(pane);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
	    }

	    @FXML
	    void logoutAction(ActionEvent event) throws Exception
	    {
	
	        LilacApp.client.accept("logout manager " + titleTxt.getText());
		    
	        AnchorPane pane = FXMLLoader.load(getClass().getResource("LilacScene.fxml"));
	    	Scene scene = new Scene(pane);
	        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
	        window.setScene(scene);
	        window.show();
	    }

	    @FXML
	    void viewComplaintsAction(ActionEvent event) throws Exception
	    { 
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("ManagerComplaintsReport.fxml"));
        	AnchorPane pane = loader.load();
        	ManagerComplaintsReportController cs = (ManagerComplaintsReportController)loader.getController();
        	cs.initializeManager(titleTxt.getText());
        	Scene scene = new Scene(pane);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
	    }

	    @FXML
	    void viewEarningsAction(ActionEvent event) throws Exception
	    {
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("ManagerEarningsReport.fxml"));
        	AnchorPane pane = loader.load();
        	ManagerEarningsReportController cs = (ManagerEarningsReportController)loader.getController();
        	cs.initializeManager(titleTxt.getText());
        	Scene scene = new Scene(pane);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
	    }

	    @FXML
	    void viewOrdersAction(ActionEvent event) throws Exception
	    {
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("ManagerOrdersReport.fxml"));
        	AnchorPane pane = loader.load();
        	ManagerOrdersReportController cs = (ManagerOrdersReportController)loader.getController();
        	cs.initializeManager(titleTxt.getText());
        	Scene scene = new Scene(pane);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
	    }
    

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	
	public void initializeInfo(String str) {
		titleTxt.setText(str);	
	}
    
}
