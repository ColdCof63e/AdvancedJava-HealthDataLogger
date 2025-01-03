package ControllerFiles;

import java.net.URL;
//import java.util.List;
import java.util.ResourceBundle;
//import java.util.regex.Pattern;

import application.Main;
import utils.BasicInfoData;
//import application.utils.Credentials;
import utils.DBValidation;
import utils.LogGenerator;
//import application.utils.FileOperations;
import utils.SwitchScenes;
import utils.Validators;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AccountCreationController implements Initializable {

	@FXML
	private TextField userMailID, fullName;

	@FXML
	private PasswordField password1, password2;

	@FXML
	private Button nextToBasicInfo;

	@FXML
	private CheckBox isATech;

	@FXML
	private Label promptLabel;
	
	private BasicInfoData bid;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		bid = Main.getBasicInfoData();
		
		promptLabel.setVisible(false);
		nextToBasicInfo.setOnAction(e -> enterBasicInfo());

		Platform.runLater(() -> {
			Stage currentStage = (Stage) nextToBasicInfo.getScene().getWindow();
			currentStage.centerOnScreen();
			currentStage.setMaximized(false);
		});
	}

	// Alert Method
	private void showAlert(String title, String message) {
		Alert alert = new Alert(AlertType.ERROR, message, ButtonType.OK);
		alert.setTitle(title);
		alert.showAndWait();
	}

	public void enterBasicInfo()
	{
		LogGenerator.generateLog(nextToBasicInfo);
		try {
			if(!(Validators.emptyFieldCheck(userMailID) && Validators.emptyFieldCheck(fullName)
					&& Validators.emptyFieldCheck(password1) && Validators.emptyFieldCheck(password2))) {
				if(Validators.isValid(userMailID.getText())) {
					if(!DBValidation.emailExists(userMailID.getText())) {
						if(Validators.passCheck(password1.getText(), password2.getText())) {

							bid.setAccountData(userMailID.getText(), password1.getText(), fullName.getText(), String.valueOf(isATech.isSelected()));
//
							// Switching to Basic Information Screen
							Stage currentStage = (Stage) nextToBasicInfo.getScene().getWindow();
							SwitchScenes.switchScene("/application/DesignFiles/BasicInformation.fxml", "Basic Information", currentStage);

						} else {
							promptLabel.setText("Password Mismatches!");
							promptLabel.setVisible(true);
						}
					} else {
						promptLabel.setText("Email already Exists!");
						promptLabel.setVisible(true);
					}
				} else {
					promptLabel.setText("Invalid Email!");
					promptLabel.setVisible(true);
				}
			} else {
				promptLabel.setText("All Fields Required!");
				promptLabel.setVisible(true);
			}
		} catch(Exception e) {
			showAlert("Error", "Invalid information: "+e.getMessage());
		}
	}
}
