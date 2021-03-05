/**
 * Sample Skeleton for 'CustomProductSection.fxml' Controller Class
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


public class CustomProductSectionController implements Initializable{


	 @FXML // fx:id="RosePriceLbl"
	    private Label RosePriceLbl; // Value injected by FXMLLoader

	    @FXML // fx:id="typeTxt"
	    private TextField typeTxt; // Value injected by FXMLLoader

	    @FXML // fx:id="backBtn"
	    private Button backBtn; // Value injected by FXMLLoader

	    @FXML // fx:id="checkTxt"
	    private Label checkTxt; // Value injected by FXMLLoader

	    @FXML // fx:id="addToCartBtn"
	    private Button addToCartBtn; // Value injected by FXMLLoader

	    @FXML // fx:id="checkTxt1"
	    private Label checkTxt1; // Value injected by FXMLLoader

	    @FXML // fx:id="CarnationPriceLbl"
	    private Label CarnationPriceLbl; // Value injected by FXMLLoader

	    @FXML // fx:id="IrisPriceLbl"
	    private Label IrisPriceLbl; // Value injected by FXMLLoader

	    @FXML // fx:id="NarcissuPriceLbl"
	    private Label NarcissuPriceLbl; // Value injected by FXMLLoader

	    @FXML // fx:id="TulipPriceLbl"
	    private Label TulipPriceLbl; // Value injected by FXMLLoader

	    @FXML // fx:id="CarnationUnitTxt"
	    private TextField CarnationUnitTxt; // Value injected by FXMLLoader

	    @FXML // fx:id="NarcissuUnitTxt"
	    private TextField NarcissuUnitTxt; // Value injected by FXMLLoader

	    @FXML // fx:id="IrisUnitTxt"
	    private TextField IrisUnitTxt; // Value injected by FXMLLoader

	    @FXML // fx:id="RoseUnitTxt"
	    private TextField RoseUnitTxt; // Value injected by FXMLLoader

	    @FXML // fx:id="TuipUnitTxt"
	    private TextField TulipUnitTxt; // Value injected by FXMLLoader

	    @FXML // fx:id="CarnationColorLbl"
	    private Label CarnationColorLbl; // Value injected by FXMLLoader

	    @FXML // fx:id="IrisColorLbl"
	    private Label IrisColorLbl; // Value injected by FXMLLoader

	    @FXML // fx:id="NarcissuColorLbl"
	    private Label NarcissuColorLbl; // Value injected by FXMLLoader

	    @FXML // fx:id="RoseColorLbl"
	    private Label RoseColorLbl; // Value injected by FXMLLoader

	    @FXML // fx:id="TulipColorLbl"
	    private Label TulipColorLbl; // Value injected by FXMLLoader

	    @FXML // fx:id="totalPriceTxt"
	    private Label totalPriceTxt; // Value injected by FXMLLoader

	    @FXML // fx:id="senderTxt"
	    private Label senderTxt; // Value injected by FXMLLoader
	    
	    String cartsProducts;

	    @FXML
	    void addToCartAction(ActionEvent event) throws Exception
	    {
	    	String[] tokensCarnation = (CarnationPriceLbl.getText()).trim().split("\\s++");
	    	String[] tokensIris = (IrisPriceLbl.getText()).trim().split("\\s++");
	    	String[] tokensNarcissu = (NarcissuPriceLbl.getText()).trim().split("\\s++");
	    	String[] tokensRose = (RosePriceLbl.getText()).trim().split("\\s++");
	    	String[] tokensTulip = (TulipPriceLbl.getText()).trim().split("\\s++");
	    	
	    	int CarnationPrice = Integer.parseInt(tokensCarnation[0]);
	    	int IrisPrice = Integer.parseInt(tokensIris[0]);
	    	int NarcissuPrice = Integer.parseInt(tokensNarcissu[0]);
	    	int RosePrice = Integer.parseInt(tokensRose[0]);
	    	int TulipPrice = Integer.parseInt(tokensTulip[0]);
	    	
	    	int CarnationUnits = -1;
	    	int IrisUnits = -1;
	    	int NarcissuUnits = -1;
	    	int RoseUnits = -1;
	    	int TulipUnits = -1;
	    	
	    	if(tryParseUnsignedInt(CarnationUnitTxt.getText()) == false ||
	    	   tryParseUnsignedInt(IrisUnitTxt.getText()) == false	||
	    	   tryParseUnsignedInt(NarcissuUnitTxt.getText()) == false ||
	    	   tryParseUnsignedInt(RoseUnitTxt.getText()) == false ||
	    	   tryParseUnsignedInt(TulipUnitTxt.getText()) == false ||
	    	   (typeTxt.getText().contentEquals("Bridal") == false &&
	    	   typeTxt.getText().contentEquals("Arrangements") == false &&
	    	   typeTxt.getText().contentEquals("Set") == false &&
	    	   typeTxt.getText().contentEquals("Flowerpot") == false ))
	    	{
	    		checkTxt.setText("Error! Units fields must be valid and type field cant be empty");
	    	}
	    	else
	    	{
	    		CarnationUnits = Integer.parseInt(CarnationUnitTxt.getText());
	    		IrisUnits = Integer.parseInt(IrisUnitTxt.getText());
		    	NarcissuUnits = Integer.parseInt(NarcissuUnitTxt.getText());
		    	RoseUnits = Integer.parseInt(RoseUnitTxt.getText());
		    	TulipUnits = Integer.parseInt(TulipUnitTxt.getText());
		    	
		    	if(CarnationUnits == 0 && IrisUnits == 0 && NarcissuUnits == 0 &&
		    	   RoseUnits == 0 && TulipUnits == 0)
		    	{
		    		checkTxt.setText("Error! Units fields must be valid and type field cant be empty");
		    	}
		    	else
		    	{ 
		    		int totalPrice = CarnationPrice*CarnationUnits + IrisPrice*IrisUnits +
	        		         NarcissuPrice*NarcissuUnits + RosePrice*RoseUnits + 
	        		         TulipPrice*TulipUnits;
	        
	              StringBuilder sb = new StringBuilder();
	        
	              sb.append("Custom: ");
	        
	              sb.append(typeTxt.getText());
	        
	              if(CarnationUnits > 0)
	              {
	                sb.append(" " + CarnationUnits + "-Carnation");
	              }
	        
	              if(IrisUnits > 0)
	              {
	        	    sb.append(" " + IrisUnits + "-Iris");	
	              }
	        
	              if(NarcissuUnits > 0)
	              {
	        	    sb.append(" " + NarcissuUnits + "-Narcissu");
	              }
	        
	              if(RoseUnits > 0)
	              {
	        	    sb.append(" " + RoseUnits + "-Rose");
	              }
	        
	             if(TulipUnits > 0)
	             {
	        	   sb.append(" " + TulipUnits + "-Tulip");
	             }
	        
	             sb.append(" Price = " + totalPrice + "\n");
	        
	             FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerCart.fxml"));
	             AnchorPane pane = loader.load();
	    	     CustomerCartController cs = (CustomerCartController)loader.getController();
	    	     if(cartsProducts == null)
	    	     {
	    	    	 cs.setCartsProducts(sb.toString());	 
	    	     }
	    	     else
	    	     {
	    	    	 cs.setCartsProducts(cartsProducts + sb.toString());
	    	     }
	    	     
	    	     cs.initializeTitle(senderTxt.getText());
	    	     Scene scene = new Scene(pane);
	             Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
	             window.setScene(scene);
	             window.show();
		     }
	      }
	    }

	    @FXML
	    void backAction(ActionEvent event) throws Exception
	    {
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomersSection.fxml"));
	    	AnchorPane pane = loader.load();
	    	CustomerSectionController cs = (CustomerSectionController)loader.getController();
	    	cs.initializeInfo(senderTxt.getText());
	    	cs.setCartProducts(cartsProducts);
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
			senderTxt.setText(str);	
		}
		
		public void setCartsProducts(String str) {
			cartsProducts = str;
		}
		
		private boolean tryParseUnsignedInt(String value)
		{  
			 try
			 {  
			    int x = Integer.parseInt(value);
			    if(x >= 0){
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
