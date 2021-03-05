package application;

import java.io.IOException;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ocsf.LilacClient;

public class LilacApp extends Application {
     
	static public LilacClient client;
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		
		//constructing our scene
		URL url = getClass().getResource("LilacScene.fxml");
		AnchorPane pane = FXMLLoader.load(url);	
		Scene scene = new Scene( pane );
		
	   //setting the stage
		primaryStage.setScene(scene);
		primaryStage.setTitle("Lilac");
		primaryStage.show();
	}
	
	public static void main(String[] args) throws Exception {
		client = new LilacClient(args[0],args[1],3306);
		launch(args);
	}
}
