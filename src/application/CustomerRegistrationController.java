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

public class CustomerRegistrationController {

	@FXML
    private TextField nameTxt;

    @FXML
    private Label loginLbl;

    @FXML
    private Label emailValidationLbl;

    @FXML
    private Button registerBtn;

    @FXML
    private TextField passwordTxt;

    @FXML
    private Button backBtn;

    @FXML
    private TextField idTxt;

    @FXML
    private TextField storeTxt;

    @FXML
    private TextField visaNumTxt;

    @FXML
    private TextField expDateTxt;

    @FXML
    private TextField subTxt;

    @FXML
    private Label registryStatus;


    @FXML
    void backToLogin(ActionEvent event) throws Exception
    {
    	AnchorPane pane = FXMLLoader.load(getClass().getResource("CustomersLogin.fxml"));
    	Scene scene = new Scene(pane);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @SuppressWarnings("static-access")
	@FXML
    void register(ActionEvent event) throws Exception
    {
    	int[] flags = {1,1,1,1,1,1,1};
    	int i,f = 1;
    	String temp1,temp2;
    	
    	String[] tokens = nameTxt.getText().trim().split("\\s++");
    	
    	if(tokens.length != 2)
    	{
    	   flags[0] = 0;
    	}
    	else
    	{
    		
    	  temp1 = tokens[0];
    	  temp2 = tokens[1];
    	  
    	  if((temp1.charAt(0) >= 'A' && temp1.charAt(0) <= 'Z') && (temp2.charAt(0) >= 'A' && temp2.charAt(0) <= 'Z'))
    	  {
    		  for(i = 1; i < temp1.length() && flags[0] == 1 ; i++)
        	  {
        		 if(temp1.charAt(i) < 'a' || temp1.charAt(i) > 'z')
        		 {
        			 flags[0] = 0;
        		 }
        	  }
    		  
    		  for(i = 1; i < temp2.length() && flags[0] == 1; i++)
        	  {
    			 if(temp2.charAt(i) < 'a' || temp2.charAt(i) > 'z')
         		 {
    				 flags[0] = 0;
         		 } 
        	  }
    	  }
    	  else
    	  {
    		  flags[0] = 0;
    	  }  
    	}
    	
    	if(passwordTxt.getText().length() < 6) flags[1] = 0;
    	
    	if(idTxt.getText().length() == 9)
    	{
    	  for(i = 0; i < idTxt.getText().length() && flags[2] == 1; i++)
      	  {
  			 if(idTxt.getText().charAt(i) < '0' || idTxt.getText().charAt(i) > '9')
       		 {
  				 flags[2] = 0;
       		 } 
      	  }
    	}
    	else
    	{
    		flags[2] = 0;
    	}
    	
    	if((subTxt.getText().contentEquals("None") == false) && (subTxt.getText().contentEquals("Annual") == false)
    	   && (subTxt.getText().contentEquals("Monthly") == false))
    	{
    		flags[3] = 0;
    	}
    	
    	if((storeTxt.getText().contentEquals("LilacHaifa") == false) &&
    	   (storeTxt.getText().contentEquals("LilacTelAviv") == false))
    	{
    	    flags[4] = 0;
    	}
    	
    	if(visaNumTxt.getText().length() == 16)
    	{
    	  for(i = 0; i < visaNumTxt.getText().length() && flags[5] == 1; i++)
      	  {
  			 if(visaNumTxt.getText().charAt(i) < '0' || visaNumTxt.getText().charAt(i) > '9')
       		 {
  				 flags[5] = 0;
       		 } 
      	  }
    	}
    	else
    	{
    		flags[5] = 0;
    	}
    	
    	if(expDateTxt.getText().contains("/") == true)
    	{
    		int index = expDateTxt.getText().indexOf('/');
    		int len = expDateTxt.getText().length();
    		String month = expDateTxt.getText().substring(0,index);
    		String year = expDateTxt.getText().substring(index+1,len);
    		
    		if(month.contentEquals("1") == false && month.contentEquals("2") == false &&
    		   month.contentEquals("3") == false && month.contentEquals("4") == false &&
    		   month.contentEquals("5") == false && month.contentEquals("6") == false &&
    		   month.contentEquals("7") == false && month.contentEquals("8") == false &&
    		   month.contentEquals("9") == false && month.contentEquals("10") == false &&
    		   month.contentEquals("11") == false && month.contentEquals("12") == false)
     	    {
    			flags[6] = 0;
     	    }
    		else
    		{
    		  
    		  if(year.length() == 4)
    		  {
    			  for(i = 0; i < 4 && flags[6] == 1; i++)
        	      {
        	  		 if(year.charAt(i) < '0' || year.charAt(i) > '9')
        	       	 {
        	  			flags[6] = 0;
        	       	 } 
        	      }
    		  }
    		  else
    		  {
    			  flags[6] = 0; 
    		  }
    		  
    		}
    	}
    	else
    	{
    		flags[6] = 0;
    	}
    	
    	for(i = 0; i < 7; i++)
    	{
    		if(flags[i] == 0)
    		{
    			f = 0;
    			break;
    		}
    	}
    	
    	if(f == 0)
    	{
    	  registryStatus.setText("Error in registration details!");
    	}
    	else
    	{
    		AnchorPane pane = null;
           
    		LilacApp.client.accept("register customer " + nameTxt.getText() + " " + passwordTxt.getText()
            + " " + idTxt.getText() + " " + subTxt.getText() + " " + storeTxt.getText() + " " 
            + visaNumTxt.getText() + " " + expDateTxt.getText());
            String response = LilacApp.client.getResponse();
            
            
            if(response.contentEquals("Not Successful Customer registration"))
            {
              registryStatus.setText("Invalid registration! Customer already exists");
              return;
            }
            else if(response.contentEquals("Successful Customer registration"))
            {
            	FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomersSection.fxml"));
            	pane = loader.load();
            	CustomerSectionController cs = loader.getController();
            	cs.initializeInfo(nameTxt.getText());
            	Scene scene = new Scene(pane);
                Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
    	}
    }

}
