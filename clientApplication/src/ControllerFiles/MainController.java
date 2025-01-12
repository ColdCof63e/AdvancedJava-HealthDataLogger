package ControllerFiles;

import java.net.URL;
//import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import utils.BasicInfoData;
import utils.DBValidation;
import utils.LogGenerator;
import utils.SwitchScenes;
import utils.Validators;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MainController implements Initializable {

	// Text field for User's Email-ID
	@FXML
	private TextField userID;

	// Password Field for user to enter their Password
	@FXML
	private PasswordField password;

	// Label for Prompting invalid entry and Forgot password
	@FXML
	private Label forgotPassword, promptLabel, accountCreation;

	// Login button
	@FXML
	private Button logIn;

	// Checkbox for remember login credentials
	@FXML
	private CheckBox rememberMe;

	private static BasicInfoData LoginBid;
	
	@Override
	public void initialize (URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		forgotPassword.setCursor(Cursor.HAND);
		accountCreation.setCursor(Cursor.HAND);
		promptLabel.setVisible(false);
		
		userID.setFocusTraversable(false);
		rememberMe.setVisible(false);
		
		LoginBid = Main.getBasicInfoData();

		Platform.runLater(() -> {
			Stage currentStage = (Stage) logIn.getScene().getWindow();
			currentStage.centerOnScreen();
			currentStage.setMaximized(false);
		});

		logIn.setOnAction(event -> login());

		forgotPassword.setOnMouseClicked(event -> forgotPassword());
		accountCreation.setOnMouseClicked(event -> createAccount());
	}
	
	public static BasicInfoData getBasicInfoData() {
        return LoginBid;
    }

    public void login() {
    	LogGenerator.generateLog(logIn);
        String enteredUsername = userID.getText();
        String enteredPassword = password.getText();

        try {
        	if(Validators.isValid(enteredUsername)) {
        		if(DBValidation.emailExists(enteredUsername)) {
        			LogGenerator.generateDatabaseLog("Verified Database for Email!");
        			if(DBValidation.isCredentialsValid(enteredUsername, enteredPassword)) {
        				if(DBValidation.isAdmin(enteredUsername)) {
        					LoginBid.setEmailID(enteredUsername);
        	        		Stage currentStage = (Stage) logIn.getScene().getWindow();
        	            	SwitchScenes.switchScene("/technicianHome/TechnicianHome.fxml", "Home", currentStage);
        	        	} else {
        	        		LoginBid.setEmailID(enteredUsername);
        	        		Stage currentStage = (Stage) logIn.getScene().getWindow();
        	            	SwitchScenes.switchScene("/application/DesignFiles/Home.fxml", "Home", currentStage);
        	        	}
        			} else {
//        				Invalid credentials
        				promptLabel.setText("Invalid Credentials!");
                		promptLabel.setVisible(true);
        			}
        		} else {
//        			Email does not exists
        			LogGenerator.generateDatabaseLog("Verified Database for Email But!");
        			promptLabel.setText("Email-ID does not Exists!");
            		promptLabel.setVisible(true);
        		}
        	} else {
//        		Enter proper email ID
        		promptLabel.setText("Enter Proper Email-ID!");
        		promptLabel.setVisible(true);
        	}
        } catch (Exception e) {
        	System.out.println(e.getMessage());
        }
    }

	public void forgotPassword() {
		LogGenerator.generateLog(forgotPassword);
		Stage currentStage = (Stage) forgotPassword.getScene().getWindow();
		SwitchScenes.switchFromLoginScene("/application/DesignFiles/Forgot_Password.fxml", "Forgot Password", currentStage);
    }

	public void createAccount() {
		LogGenerator.generateLog(accountCreation);
		Stage currentStage = (Stage) accountCreation.getScene().getWindow();
		SwitchScenes.switchFromLoginScene("/application/DesignFiles/AccountCreation.fxml", "Create Account", currentStage);
    }
}
