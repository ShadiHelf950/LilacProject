/**
 * Sample Skeleton for 'LilacScene.fxml' Controller Class
 */

package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LilacController implements Initializable{

    @FXML // fx:id="StaffBtn"
    private Button StaffBtn; // Value injected by FXMLLoader

    @FXML // fx:id="customersBtn"
    private Button customersBtn; // Value injected by FXMLLoader
    
    @FXML
    private Button GuestBtn;
    
    @FXML // fx:id="pleaseLoginLbl"
    private Label pleaseLoginLbl; // Value injected by FXMLLoader



    @FXML
    void staffSection(ActionEvent event) throws Exception
    {
    	AnchorPane pane = FXMLLoader.load(getClass().getResource("StaffRegistration.fxml"));
    	Scene scene = new Scene( pane );
    	Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
    	window.setScene(scene);
    	window.show();
    }

    @FXML
    void customersSection(ActionEvent event) throws Exception
    {
    	AnchorPane pane = FXMLLoader.load(getClass().getResource("CustomersLogin.fxml"));
    	Scene scene = new Scene( pane );
    	Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
    	window.setScene(scene);
    	window.show();
    }
    
    @FXML
    void guestSection(ActionEvent event) throws Exception
    {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("Catalog.fxml"));
    	AnchorPane pane = loader.load();
    	CatalogController cs = (CatalogController)loader.getController();
    	cs.initializeTitle("Guest");
    	Scene scene = new Scene(pane);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show(); 
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	
	public void initializeMessage(String str) {
		pleaseLoginLbl.setText(str);	
	}
}
