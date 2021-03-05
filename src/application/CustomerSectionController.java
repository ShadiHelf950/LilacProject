/**
 * Sample Skeleton for 'CustomerSection.fxml' Controller Class
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

public class CustomerSectionController implements Initializable{

    @FXML // fx:id="titleTxt"
    private Label titleTxt; // Value injected by FXMLLoader

    @FXML // fx:id="viewCatalogBtn"
    private Button viewCatalogBtn; // Value injected by FXMLLoader

    @FXML // fx:id="addCustomBtn"
    private Button addCustomBtn; // Value injected by FXMLLoader

    @FXML // fx:id="viewCartBtn"
    private Button viewCartBtn; // Value injected by FXMLLoader

    @FXML // fx:id="complaintBtn"
    private Button complaintBtn; // Value injected by FXMLLoader

    @FXML // fx:id="cancelOrderBtn"
    private Button cancelOrderBtn; // Value injected by FXMLLoader

    @FXML // fx:id="logoutBtn"
    private Button logoutBtn; // Value injected by FXMLLoader

    private String cartsProducts; 
    
    @FXML
    void addCustomAction(ActionEvent event) throws Exception
    {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomProductSection.fxml"));
    	AnchorPane pane = loader.load();
    	CustomProductSectionController cs = (CustomProductSectionController)loader.getController();
    	cs.initializeInfo(titleTxt.getText());
    	cs.setCartsProducts(cartsProducts);
    	Scene scene = new Scene(pane);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML
    void cancelOrderAction(ActionEvent event) throws Exception
    {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerCancellation.fxml"));
    	AnchorPane pane = loader.load();
    	CustomerCancellationController cs = (CustomerCancellationController)loader.getController();
    	cs.initializeInfo(titleTxt.getText());
    	cs.setCartsProducts(cartsProducts);
    	Scene scene = new Scene(pane);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML
    void complaintAction(ActionEvent event) throws Exception
    {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerComplaint.fxml"));
    	AnchorPane pane = loader.load();
    	CustomerComplaintController cs = (CustomerComplaintController)loader.getController();
    	cs.initializeInfo(titleTxt.getText());
    	cs.setCartsProducts(cartsProducts);
    	Scene scene = new Scene(pane);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML
    void logoutAction(ActionEvent event) throws Exception
    {
        LilacApp.client.accept("logout customer " + titleTxt.getText());
	        
        AnchorPane pane = FXMLLoader.load(getClass().getResource("LilacScene.fxml"));
    	Scene scene = new Scene(pane);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML
    void viewCartAction(ActionEvent event) throws Exception
    {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerCart.fxml"));
    	AnchorPane pane = loader.load();
    	CustomerCartController cs = (CustomerCartController)loader.getController();
    	cs.initializeTitle(titleTxt.getText());
    	cs.setCartsProducts(cartsProducts);
    	Scene scene = new Scene(pane);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show(); 
    }

    @FXML
    void viewCatalogAction(ActionEvent event) throws Exception
    {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("Catalog.fxml"));
    	AnchorPane pane = loader.load();
    	CatalogController cs = (CatalogController)loader.getController();
    	cs.initializeTitle(titleTxt.getText());
    	cs.setCartsProducts(cartsProducts);
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
    
	public void setCartProducts(String str) {
		cartsProducts = str;	
	}
}
