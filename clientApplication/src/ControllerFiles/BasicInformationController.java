package ControllerFiles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.ResourceBundle;

import application.Main;
import utils.BasicInfoData;
import utils.DBValidation;
import utils.LogGenerator;
import utils.SwitchScenes;
import utils.Validators;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class BasicInformationController implements Initializable {

    @FXML
    private TextField basicInfoAddress;

    @FXML
    private ComboBox<String> basicInfoBloodType;

    @FXML
    private DatePicker basicInfoDOBPicker;

    @FXML
    private TextField basicInfoEmail;

    @FXML
    private ComboBox<String> basicInfoGender;

    @FXML
    private TextField basicInfoHeight;

    @FXML
    private TextField basicInfoName;

    @FXML
    private TextField basicInfoPhNumber;

    @FXML
    private TextField basicInfoWeight;

    @FXML
    private Button createAccount;

    @FXML
    private Label promptLabel;

    @FXML
    private Label ageLabel;
    
    private BasicInfoData bid;

    // Server details
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8080;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        promptLabel.setVisible(false);
        ageLabel.setVisible(false);

        // Initialize combo boxes with values
        basicInfoBloodType.getItems().addAll("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-", "Rh-null");
        basicInfoGender.getItems().addAll("Male", "Female", "Others");
        
        bid = Main.getBasicInfoData();

        // Set default values for email and name
        basicInfoEmail.setText(bid.getEmail());
        basicInfoEmail.setDisable(true);
        basicInfoName.setText(bid.getName());
        basicInfoName.setDisable(true);

        // Action for the create account button
        createAccount.setOnAction(e -> accountCreation());

        // Calculate age based on DOB selection
        basicInfoDOBPicker.setOnAction(e -> {
            LocalDate dob = basicInfoDOBPicker.getValue();
            if (dob != null) {
                int age = calculateAge(dob);
                ageLabel.setText(age + " years");
                ageLabel.setVisible(true);
            }
        });
    }

	// Account creation logic (sending data to the server)
    public void accountCreation() {
    	
    	
        LogGenerator.generateLog(createAccount);

        // Clear previous errors
        promptLabel.setVisible(false);

        try {
            // Validation checks for empty fields
            if (Validators.emptyFieldCheck(basicInfoPhNumber)) {
                promptLabel.setText("Phone Number Missing!");
                promptLabel.setVisible(true);
                return;
            }

            if (Validators.emptyFieldCheck(basicInfoHeight)) {
                promptLabel.setText("Height Required! - in 'cm'");
                promptLabel.setVisible(true);
                return;
            }

            if (Validators.emptyFieldCheck(basicInfoWeight)) {
                promptLabel.setText("Weight Required! - in 'lb'");
                promptLabel.setVisible(true);
                return;
            }

            if (basicInfoGender.getValue() == null) {
                promptLabel.setText("Select Gender!");
                promptLabel.setVisible(true);
                return;
            }

            if (basicInfoBloodType.getValue() == null) {
                promptLabel.setText("Select Blood Type!");
                promptLabel.setVisible(true);
                return;
            }

            if (basicInfoDOBPicker.getValue() == null) {
                promptLabel.setText("Pick your Date of Birth!");
                promptLabel.setVisible(true);
                return;
            }

            if (Validators.emptyFieldCheck(basicInfoAddress)) {
                promptLabel.setText("Address Required!");
                promptLabel.setVisible(true);
                return;
            }

            // Send account data to the server
            sendAccountDataToServer();

        } catch (Exception e) {
            promptLabel.setText("An unexpected error occurred!");
            promptLabel.setVisible(true);
            e.printStackTrace();
        }
    }

    // Method to send account data to the server
    private void sendAccountDataToServer() {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT); // Connect to the server
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true); // Output stream to send data
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) { // Input stream to read server response

            // Prepare the data to send to the server (can be adjusted as per server requirements)
            String accountData = String.format("ADD_USER:%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
//            		1
            		bid.getEmail(),
//            		2
            		bid.getPassword(),
//            		3
            		bid.getName(),
//            		4
            		bid.getIsTech(),
//            		5
            		basicInfoPhNumber.getText(),
//            		6
                    basicInfoHeight.getText(),
//                  7
                    basicInfoWeight.getText(),
//                  8
                    basicInfoAddress.getText(),
//                  9
                    basicInfoGender.getValue(),
//                  10
                    basicInfoBloodType.getValue(),
//                  11
                    basicInfoDOBPicker.getValue()
            );

            // Send the account creation request
            out.println(accountData);

            // Wait for the server's response
            String response = in.readLine();
            if (response != null && response.equals("SUCCESS")) {
                // Account created successfully
            	Stage currentStage = (Stage) createAccount.getScene().getWindow();
                SwitchScenes.switchScene("/application/DesignFiles/Main.fxml", "Login", currentStage);
            } else {
                // Account creation failed
                promptLabel.setText("Account creation failed!\nPlease try again.");
                promptLabel.setVisible(true);
            }

        } catch (IOException e) {
            // Handle network errors
            promptLabel.setText("Network error: Unable to connect to the server.");
            promptLabel.setVisible(true);
            e.printStackTrace();
        }
    }

    // Method to calculate the age from Date of Birth
    public int calculateAge(LocalDate dob) {
        LocalDate today = LocalDate.now();
        Period period = Period.between(dob, today);
        return period.getYears();
    }
}
