package ControllerFiles;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import utils.BasicInfoData;
import utils.SwitchScenes;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MyAccountController implements Initializable {
	
	@FXML
    private Button changeImageButton;

    @FXML
    private TextField myAccAddressBox;

    @FXML
    private ComboBox<String> myAccBloodTypeBox;

    @FXML
    private Hyperlink myAccChangePassword;

    @FXML
    private DatePicker myAccDOBBox;

    @FXML
    private ComboBox<String> myAccGenderBox;

    @FXML
    private Button myAccGoBack;

    @FXML
    private TextField myAccHeightBox;

    @FXML
    private TextField myAccMailBox;

    @FXML
    private TextField myAccNameBox;

    @FXML
    private TextField myAccPhoneNumberBox;

    @FXML
    private Button myAccSaveChanges;

    @FXML
    private TextField myAccWeightBox;
    
    private BasicInfoData bid;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		myAccBloodTypeBox.getItems().addAll("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-", "Rh-null");
		myAccGenderBox.getItems().addAll("Male", "Female", "Others");
		
		bid = Main.getBasicInfoData();
		
		loadExistingData();
		
	}
	
	private void loadExistingData() {
		// TODO Auto-generated method stub
		myAccMailBox.setText(bid.getEmail());
		myAccMailBox.setDisable(true);
		myAccNameBox.setText(bid.getName());
		myAccNameBox.setDisable(true);
		myAccPhoneNumberBox.setText(bid.getPhoneNumber());
		myAccHeightBox.setText(String.valueOf(bid.getHeight()));
		myAccWeightBox.setText(String.valueOf(bid.getWeight()));
		myAccGenderBox.setValue(bid.getGender());
		myAccBloodTypeBox.setValue(bid.getBloodType());
		myAccDOBBox.setValue(bid.getDob());
		myAccAddressBox.setText(bid.getAddress());
		Stage currentStage = (Stage) myAccSaveChanges.getScene().getWindow();
		
		currentStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent arg0) {
				// TODO Auto-generated method stub
				SwitchScenes.switchScene("/application/DesignFiles/Home.fxml", "Home", currentStage);					
			}				
		});	
	}
}
