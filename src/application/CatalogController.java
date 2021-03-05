/**
 * Sample Skeleton for 'Catalog.fxml' Controller Class
 */

package application;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import LilacClasses.CatalogProduct;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;

import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;



public class CatalogController  implements Initializable{

	  @FXML
	    private ResourceBundle resources;

	    @FXML
	    private URL location;

	    @FXML
	    private Label titleTxt;

	    @FXML
	    private Button backBtn;

	    @FXML
	    private Label productsLbl;

	   
	    @FXML
	    private TextField idTxt;

	    @FXML
	    private Button backBtn1;


	    @FXML
	    private Label productsLbl1;


	    @FXML
	    private Label checkTxt;

	    @FXML
	    private Button searchTypeBtn;

	    @FXML
	    private TextField typeTxt;

	   // @FXML
	  //  private Label productsLbl11;

	   // @FXML
	   // private Label productsLbl111;

	   @FXML
	    private TextField nameTxt;



	    @FXML
	    private TableColumn<CatalogProduct, ImageView> imagecl;
	    @FXML
	    private TableView<CatalogProduct> catalogtb;
	    @FXML
	    private TableColumn<CatalogProduct, String> idcl;

	    @FXML
	    private TableColumn<CatalogProduct, String> namecl;

	    @FXML
	    private TableColumn<CatalogProduct, String> pricecl;

	    @FXML
	    private TableColumn<CatalogProduct, String> typecl;
	    
	    @FXML
	    private TableColumn<CatalogProduct, String> discountcl;

        private String cartsProducts;
        
        private String[] tokens;

        
        
        @Override
    	public void initialize(URL arg0, ResourceBundle arg1) 
    	{
        	
        	
        	

        	ObservableList<CatalogProduct> data =  catalogtb.getItems();
        	nameTxt.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
        	            if (oldValue != null && (newValue.length() < oldValue.length())) {
        	            	catalogtb.setItems(data);
        	            }
        	            String value = newValue.toLowerCase();
        	            ObservableList<CatalogProduct> subentries = FXCollections.observableArrayList();

        	            long count = catalogtb.getColumns().stream().count();
        	            for (int i = 0; i < catalogtb.getItems().size(); i++) {
        	                for (int j = 0; j < 1; j++) {
        	                    String entry = "" + catalogtb.getColumns().get(1).getCellData(i);
        	                    if (entry.toLowerCase().contains(value)) {
        	                        subentries.add(catalogtb.getItems().get(i));
        	                        break;
        	                    }
        	                }
        	            }
        	            catalogtb.setItems(subentries);
        	        });
        	
        	
        	ObservableList<CatalogProduct> data2 =  catalogtb.getItems();
        	typeTxt.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
        	            if (oldValue != null && (newValue.length() < oldValue.length())) {
        	            	catalogtb.setItems(data2);
        	            }
        	            String value = newValue.toLowerCase();
        	            ObservableList<CatalogProduct> subentries = FXCollections.observableArrayList();

        	            long count = catalogtb.getColumns().stream().count();
        	            for (int i = 0; i < catalogtb.getItems().size(); i++) {
        	                for (int j = 0; j < 1; j++) {
        	                    String entry = "" + catalogtb.getColumns().get(4).getCellData(i);
        	                    if (entry.toLowerCase().contains(value)) {
        	                        subentries.add(catalogtb.getItems().get(i));
        	                        break;
        	                    }
        	                }
        	            }
        	            catalogtb.setItems(subentries);
        	        });
        	
        	idcl.setCellValueFactory(new PropertyValueFactory<>("id"));
        	namecl.setCellValueFactory(new PropertyValueFactory<>("name"));
        	pricecl.setCellValueFactory(new PropertyValueFactory<>("price"));
        	typecl.setCellValueFactory(new PropertyValueFactory<>("type"));
        	imagecl.setCellValueFactory(c-> new SimpleObjectProperty<ImageView>(new ImageView(c.getValue().getImage())));
        	//imagecl.setCellValueFactory(new PropertyValueFactory<>("image"));

        	discountcl.setCellValueFactory(new PropertyValueFactory<>("discount"));
        	 
    		
    			
    	        LilacApp.client.accept("get catalog");
    	        try
    	        {
    				String response = LilacApp.client.getResponse();
    		        
    		        productsLbl.setText("The Format is: ID Name Price Type\n");
    		        
    		        tokens = response.trim().split("\n");
    		        String[] temp;
    		        
    		        for(int i = 0; i < tokens.length;i++)
    		        {
    		          temp = tokens[i].trim().split("\\s++");
    		          String x = productsLbl.getText();
    		           		          
    		          int id=  Integer.parseInt(temp[0]);
    		          String name =temp[1];
         	    	  double price=Double.parseDouble(temp[2]);
		        	  File file = new File("C:\\Users\\Omar\\Desktop\\Catalog" + temp[3]);
		        	 // ImageView image=   new ImageView(new Image(file.toURI().toString()));

			          Image image = new Image(file.toURI().toString(),100, 100, false, false);
			          String type=temp[4];
         	    	  String discount = (temp[5]);

    		          CatalogProduct catalogProduct= new CatalogProduct(id,price,type,name,image,discount);    		    
    		          catalogtb.getItems().add(catalogProduct);

    			} 
    	        }
    	        catch (InterruptedException e)
    	        {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			} // wait till a proper response is available
    			
    		} 
    	
        
        
  
        
        
    @FXML
    void backAction(ActionEvent event) throws Exception
    {
    	if(titleTxt.getText().contentEquals("Guest"))
    	{
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("LilacScene.fxml"));
        	AnchorPane pane = loader.load();
        	Scene scene = new Scene(pane);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
    		return;
    	}
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomersSection.fxml"));
    	AnchorPane pane = loader.load();
    	CustomerSectionController cs = (CustomerSectionController)loader.getController();
    	cs.initializeInfo(titleTxt.getText());
    	cs.setCartProducts(cartsProducts);
    	Scene scene = new Scene(pane);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
    
   

    
    @FXML
    void addAction(ActionEvent event) throws Exception
    {
    	if(titleTxt.getText().contentEquals("Guest"))
    	{
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("LilacScene.fxml"));
        	AnchorPane pane = loader.load();
        	LilacController cs = (LilacController)loader.getController();
        	cs.initializeMessage("Please Login in order buy products");
        	Scene scene = new Scene(pane);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
    		return;
    	}
    	
    	if(tryParsePositiveInt(idTxt.getText()))
    	{
    	   int id = Integer.parseInt(idTxt.getText());
    	   
    	   if(id <= tokens.length)
    	   {
    		   StringBuilder sb = new StringBuilder();
    		   sb.append("Catalog: ");
    		   String[] temp = null;
		        
		        for(int i = 0; i < tokens.length;i++)
		        {
		          temp = tokens[i].trim().split("\\s++");
		          int currentId = Integer.parseInt(temp[0]);
		          if(id == currentId) break;
		        }
		        sb.append(temp[4] + " ");
		        sb.append(temp[1] + " ");
		        sb.append("Price = " + temp[2] + "\n");
		        
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
	    	     
	    	     cs.initializeTitle(titleTxt.getText());
	    	     Scene scene = new Scene(pane);
	             Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
	             window.setScene(scene);
	             window.show();
    		   
    	   }
    	   else
    	   {
    		  checkTxt.setText("Error! Enter a valid product ID");
    	   }
    	}
    	else
    	{
    		checkTxt.setText("Error! Enter a valid product ID");
    	}
    }

	
	
	public void initializeTitle(String str) {
		titleTxt.setText(str);	
	}
	
	public void setCartsProducts(String str) {
		cartsProducts = str;	
	}
	
	private boolean tryParsePositiveInt(String value) {  
	 try
	 {  
	    int x = Integer.parseInt(value); 
	    if(x > 0)
	    {
	      return true;
	    }
	    else
	    {
	    	return false;
	    }
	      
	 }
	 catch (NumberFormatException e)
	 {  
	    return false;  
	 }  
   }
	
}