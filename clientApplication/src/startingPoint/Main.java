package startingPoint;

//import java.io.IOException;

import healthDataRecords.HealthDataRecordsController;
//Import necessary classes for JavaFX application and controllers
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import patientAccountManagement.PatientAccountManagementController;
//import login.LoginController;
import technicianHome.TechnicianHomeController;

//Main class that extends JavaFX Application and controls application views
public class Main extends Application {
	// Static reference to primary application stage
    private static Stage primaryStage;
    // Singleton instance of Main class
    private static Main instance;
    // private HealthDataRecordsController healthDataRecordsController;

    @Override
    // Main entry point for the JavaFX application, initializes starting view
    public void start(Stage stage) {
        instance = this; // Set singleton instance to current Main instance
        primaryStage = stage; // Set primaryStage to the provided stage
        loadTechnicianHome(); // Start with Technician Home view(Load Technician Home as the initial view)

    }
    // Method to get singleton instance of Main class
    public static Main getInstance() {
        return instance;
    }
    // Method to get primary application stage
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    //Method to load Login (View) FXML

    // Narendra - Changes - 1: Commented on 21-Nov-2024
//    public void loadLogin() {
//    	try {
//    		// Load the Login FXML file
//    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/login/Login.fxml"));
//    		Parent root = loader.load();
//
//    		 // Get the controller and set the mainApp reference
//    		LoginController controller = loader.getController();
//    		controller.setMainApp(this);
//
//    		// Set the stage title, scene, and show it
//    		primaryStage.setTitle("Login");
//    		primaryStage.setScene(new Scene(root, 700, 400));
//    		primaryStage.show();
//    	} catch(Exception e) {
//    		e.printStackTrace(); // Print error trace if loading fails
//    		System.out.println("Error loading Login.fxml");
//    	}
//    }

    /* public void loadLoginScreen() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("LoginScreen.fxml")); // Update the path as per your project
            Parent loginScreen = loader.load();

            // Set the scene to the primary stage
            Scene scene = new Scene(loginScreen);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading Logout.fxml");
        }
    } */


    // Method to load Technician Home (View) FXML
    public void loadTechnicianHome() {
        try {
        	// Load the TechnicianHome FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/technicianHome/TechnicianHome.fxml"));
            Parent root = loader.load();

            // Get the controller and set the mainApp reference
            TechnicianHomeController controller = loader.getController();
            controller.setMainApp(this);

            // Set the stage title, scene, and show it
            primaryStage.setTitle("Technician Home");
            primaryStage.setScene(new Scene(root, 700, 400));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace(); // Print error trace if loading fails
            System.out.println("Error loading TechnicianHome.fxml");
        }
    }

    // Method to load Patient Account Management (View) FXML
    public void loadPatientAccountManagement() {
        try {
        	// Load the PatientAccountManagement FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/patientAccountManagement/PatientAccountManagement.fxml"));
            Parent root = loader.load();

            // Get the controller and set the mainApp reference
            PatientAccountManagementController controller = loader.getController();
            controller.setMainApp(this);
            // Set the stage title, scene, and show it
            primaryStage.setTitle("Patient Account Management");
            primaryStage.setScene(new Scene(root, 700, 400));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace(); // Print error trace if loading fails
            System.out.println("Error loading PatientAccountManagement.fxml");
        }
    }

    // Method to load Health Data Records (View) FXML
    public void loadHealthDataRecords(String name, String age, String gender, String bloodType, String height, String weight) {
        try {
        	// Load the HealthDataRecords FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/healthDataRecords/HealthDataRecords.fxml"));
            Parent root = loader.load();

            // Get the controller and set the mainApp reference
            HealthDataRecordsController controller = loader.getController();
            controller.setMainApp(this);
            controller.setPatientInfo(name, age, gender, bloodType, height, weight);

            /*// Load data from file at the start of Health Data Records view
            controller.loadDataFromFile();
            // Save reference to use in stop() for data saving
            this.healthDataRecordsController = controller; */

            primaryStage.setTitle("Health Data Records");
            primaryStage.setScene(new Scene(root, 700, 400));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading HealthDataRecords.fxml");
        }
    }

     // @Override
    /*public void stop() {
        // Save data to file if healthDataRecordsController was loaded
        if (healthDataRecordsController != null) {
            healthDataRecordsController.saveDataToFile();
        }
    } */

    // Main method to launch the JavaFX application
    public static void main(String[] args) {
        launch(args); // Calls the start method
    }
}







