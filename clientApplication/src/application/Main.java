package application;

import utils.BasicInfoData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class Main extends Application {
	
	private static BasicInfoData bid = new BasicInfoData();
	
	@Override
	public void start(Stage primaryStage) {
		try { 
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("/DesignFiles/Main.fxml")); // /healthDataRecords/HealthDataRecords.fxml  DesignFiles/Main.fxml

			Scene scene = new Scene(root,480,360);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			primaryStage.setResizable(false);
			primaryStage.setTitle("Login");
			primaryStage.setScene(scene);
			primaryStage.centerOnScreen();
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static BasicInfoData getBasicInfoData() {
        return bid;
    }

	public static void main(String[] args) {
		launch(args);
	}
}
