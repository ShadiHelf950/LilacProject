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

public class ManagerComplaintsReportController implements Initializable {

	 @FXML // fx:id="managerLbl"
	    private Label managerLbl; // Value injected by FXMLLoader

	    @FXML // fx:id="checkTxt"
	    private Label checkTxt; // Value injected by FXMLLoader

	    @FXML // fx:id="storeNameTxt"
	    private TextField storeNameTxt; // Value injected by FXMLLoader

	    @FXML // fx:id="backBtn"
	    private Button backBtn; // Value injected by FXMLLoader

	    @FXML // fx:id="histBtn"
	    private Button histBtn; // Value injected by FXMLLoader

	    @FXML // fx:id="compareBtn"
	    private Button compareBtn; // Value injected by FXMLLoader

	    @FXML // fx:id="storeNameTxt1"
	    private TextField storeNameTxt1; // Value injected by FXMLLoader

	    @FXML // fx:id="month1Txt"
	    private TextField month1Txt; // Value injected by FXMLLoader

	    @FXML // fx:id="storeNameTxt2"
	    private TextField storeNameTxt2; // Value injected by FXMLLoader

	    @FXML // fx:id="month2Txt"
	    private TextField month2Txt; // Value injected by FXMLLoader

	    @FXML // fx:id="result1Lbl"
	    private Label result1Lbl; // Value injected by FXMLLoader

	    @FXML // fx:id="result2Lbl"
	    private Label result2Lbl; // Value injected by FXMLLoader

	    @FXML // fx:id="Q2Lbl"
	    private Label Q2Lbl; // Value injected by FXMLLoader

	    @FXML // fx:id="Q1Lbl"
	    private Label Q1Lbl; // Value injected by FXMLLoader

	    @FXML // fx:id="Q3Lbl"
	    private Label Q3Lbl; // Value injected by FXMLLoader

	    @FXML // fx:id="Q4Lbl"
	    private Label Q4Lbl; // Value injected by FXMLLoader


	    @FXML
	    void compareAction(ActionEvent event)  throws Exception
	    {	
	    	 if((storeNameTxt1.getText().contentEquals("LilacHaifa") == true ||
	  	         storeNameTxt1.getText().contentEquals("LilacTelAviv") == true) &&
	    		 (storeNameTxt2.getText().contentEquals("LilacHaifa") == true ||
		  	      storeNameTxt2.getText().contentEquals("LilacTelAviv") == true) &&
	    		 (tryParseMonth(month1Txt.getText()) == true && tryParseMonth(month2Txt.getText()) == true))
	  	   	    {

	    		 LilacApp.client.accept("get comp " + managerLbl.getText() + " " + storeNameTxt1.getText()
	  	             + " " + storeNameTxt2.getText() + " " + month1Txt.getText() + " " + month2Txt.getText());
	  	             String response = LilacApp.client.getResponse();
	  	             
	  	             
	  	             if(response.contentEquals("Unsuccessful Complaint Comparison") == true)
	  	             {
	  	            	checkTxt.setText("Error! Manager does not have the permission for this action"); 
	  	             }
	  	             else
	  	             {
	  	            	String[] temp = response.trim().split("-");
		  	             
		  	             result1Lbl.setText("Result1 : " + temp[0] + " Complaints");
		  	             result2Lbl.setText("Result2 : " + temp[1] + " Complaints");
	  	             }
	  	   	  }
	  	       else
	  	       {
	  	      	 checkTxt.setText("Error! Invalid store name for one of the fields or invalid month number");  
	  	       }
	    }

	    @FXML
	    void histAction(ActionEvent event) throws Exception
	    {   
	        if(storeNameTxt.getText().contentEquals("LilacHaifa") == true ||
	           storeNameTxt.getText().contentEquals("LilacTelAviv") == true ||
	           storeNameTxt.getText().contentEquals("All") == true)
	   	    {

	        	LilacApp.client.accept("get histComplaint " + managerLbl.getText() + " " + storeNameTxt.getText());
	             String response = LilacApp.client.getResponse();
	             
	             System.out.println("response = " + response); // I am scared so i will check this always
	             
	             if(response.contentEquals("Unsuccessful Complaint Histogram") == true)
  	             {
  	            	checkTxt.setText("Error! Manager does not have the permission for this action"); 
  	             }
  	             else
  	             {
  	            	String[] temp = response.trim().split("-");
  		             
  		             Q1Lbl.setText(temp[0] + " Complaints");
  		             Q2Lbl.setText(temp[1] + " Complaints");
  		             Q3Lbl.setText(temp[2] + " Complaints");
  		             Q4Lbl.setText(temp[3] + " Complaints"); 
  	             }
	   	  }
	       else
	       {
	      	 checkTxt.setText("Error! Invalid store name");  
	       }
	    }
    
    @FXML
    void backAction(ActionEvent event) throws Exception
    {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("ManagersSection.fxml"));
    	AnchorPane pane = loader.load();
    	ManagerSectionController cs = (ManagerSectionController)loader.getController();
    	cs.initializeInfo(managerLbl.getText());
    	Scene scene = new Scene(pane);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
	
	}
	
	public void initializeManager(String str) {
		managerLbl.setText(str);	
	}
	
	private boolean tryParseMonth(String value)
	{  
		 try
		 {  
		    int x = Integer.parseInt(value);
		    if(x >= 1 && x <= 12){
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
