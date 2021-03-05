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

public class EmployeeSectionController implements Initializable{

	@FXML
    private Label titleTxt;

    @FXML
    private Button addCatalogBtn;

    @FXML
    private Button editCatalogBtn;

    @FXML
    private Button saleBtn;

    @FXML
    private Button complaintBtn;

    @FXML
    private Button logoutBtn;
    
    @FXML
    private Label checkTxt; 

    @FXML
    void addCatalogAction(ActionEvent event) throws Exception
    {
 
        LilacApp.client.accept("privilege employee Catalog " + titleTxt.getText());
        String response = LilacApp.client.getResponse();
        
    	
        if(response.contentEquals("OK"))
        {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("EmployeeAddCatalog.fxml"));
        	AnchorPane pane = loader.load();
        	EmployeeAddCatalogController cs = (EmployeeAddCatalogController)loader.getController();
        	cs.initializeInfo(titleTxt.getText());
        	Scene scene = new Scene(pane);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
        else
        {
        	checkTxt.setText("Error! you dont have a permission to execute this action");
        }
    }
    
    @FXML
    void editCatalogAction(ActionEvent event) throws Exception
    {   

    	LilacApp.client.accept("privilege employee Catalog " + titleTxt.getText());
        String response = LilacApp.client.getResponse();
        
    	
        if(response.contentEquals("OK"))
        {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("EmployeeEditCatalog.fxml"));
        	AnchorPane pane = loader.load();
        	EmployeeEditCatalogController cs = (EmployeeEditCatalogController)loader.getController();
        	cs.initializeInfo(titleTxt.getText());
        	Scene scene = new Scene(pane);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
        else
        {
        	checkTxt.setText("Error! you dont have a permission to execute this action");
        }
    }

    @FXML
    void saleAction(ActionEvent event) throws Exception
    {
 
    	LilacApp.client.accept("privilege employee Catalog " + titleTxt.getText());
        String response = LilacApp.client.getResponse();
        
    	
        if(response.contentEquals("OK"))
        {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("EmployeeSale.fxml"));
        	AnchorPane pane = loader.load();
        	EmployeeSaleController cs = (EmployeeSaleController)loader.getController();
        	cs.initializeInfo(titleTxt.getText());
        	Scene scene = new Scene(pane);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
        else
        {
        	checkTxt.setText("Error! you dont have a permission to execute this action");
        }
    }

    @FXML
    void complaintAction(ActionEvent event) throws Exception
    {
    
    	LilacApp.client.accept("privilege employee Complaints " + titleTxt.getText());
        String response = LilacApp.client.getResponse();
        
    	
        if(response.contentEquals("OK"))
        {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("EmployeeComplaint.fxml"));
        	AnchorPane pane = loader.load();
        	EmployeeComplaintController cs = (EmployeeComplaintController)loader.getController();
        	cs.initializeInfo(titleTxt.getText());
        	Scene scene = new Scene(pane);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();	
        }
        else
        {
        	checkTxt.setText("Error! you dont have a permission to execute this action");
        }
    }

    @FXML
    void logoutAction(ActionEvent event) throws Exception
    {
        
    	LilacApp.client.accept("logout employee " + titleTxt.getText());
        
        AnchorPane pane = FXMLLoader.load(getClass().getResource("LilacScene.fxml"));
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
