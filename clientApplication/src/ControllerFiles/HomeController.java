package ControllerFiles;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import utils.BasicInfoData;
import utils.LogGenerator;
import utils.SwitchScenes;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class HomeController implements Initializable {
	
	@FXML
    private Button alertButton;

    @FXML
    private Button createNewEntry;

    @FXML
    private Button generateReportButton;

    @FXML
    private Button modify;

    @FXML
    private Button myAcc;
    
    private BasicInfoData bid;
    
    private String email;

    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		createNewEntry.setOnAction(event -> gotoNewEntryWindow());
		myAcc.setOnAction(e -> openMyAccount());
		
		bid = MainController.getBasicInfoData();
		email = bid.getEmail();

		Platform.runLater(() -> {
			Stage currentStage = (Stage) createNewEntry.getScene().getWindow();
			currentStage.centerOnScreen();
			currentStage.setMaximized(true);
		});
	}

	private void openMyAccount() {
		// TODO Auto-generated method stub
		bid.setEmailID(email);
		LogGenerator.generateLog(myAcc);
		Stage currentStage = (Stage) myAcc.getScene().getWindow();
		SwitchScenes.switchScene("/application/DesignFiles/MyAccount.fxml", "My Account", currentStage);
	}

	public void gotoNewEntryWindow() {
		System.out.println("Create New Entry Clicked");
		Stage currentStage = (Stage) createNewEntry.getScene().getWindow();
    	SwitchScenes.switchScene("/healthDataRecords/HealthDataRecords.fxml", "Health Data Record", currentStage);
	}
}