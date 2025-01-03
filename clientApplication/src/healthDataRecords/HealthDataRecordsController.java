package healthDataRecords;

import java.io.BufferedReader; // For reading input from server
import java.io.InputStreamReader; // To read input streams
import java.io.PrintWriter; // To write to the server
import java.net.Socket; // For establishing network communication

import utils.BasicInfoData;
import utils.DBValidation;
import utils.SwitchScenes;
// Import necessary JavaFX components
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import startingPoint.Main;

/**
 * Controller for managing the Health Data Records UI.
 * It facilitates interactions between the JavaFX UI and the backend server.
 */
public class HealthDataRecordsController {

    private Main mainApp; // Reference to the main application

    @FXML
    private MenuBar healthDataRecordsMenuBar; // Menu bar for navigation options

    @FXML
    private TreeView<String> healthMetricsTreeView; // TreeView for displaying health metrics

    @FXML
    private TextField patientNameTextField; // TextField for patient name

    @FXML
    private TextField ageTextField; // TextField for patient age

    @FXML
    private TextField genderTextField; // TextField for patient gender

    @FXML
    private TextField bloodTypeTextField; // TextField for patient blood type

    @FXML
    private TextField heightTextField; // TextField for patient height

    @FXML
    private TextField weightTextField; // TextField for patient weight

    @FXML
    private TextArea healthDataDisplayTextArea; // TextArea for displaying selected metric details

    @FXML
    private Button hDRModifyButton; // Button to enable modification of data

    @FXML
    private Button hDRSaveButton; // Button to save modified data

    @FXML
    private Button hDRDeleteButton; // Button to delete selected data

    @FXML
    private Button hDRBackToTechnicianHomeButton; // Button to navigate back to Technician Home

    private static final String SERVER_HOST = "localhost"; // Server host address
    private static final int SERVER_PORT = 8080; // Server port

    private String selectedMetric = null; // The currently selected metric
    private String originalRecordId = null; // The record ID of the selected metric
    private String originalValue = null; // The original value of the selected metric
    private boolean isEditable = false; // Flag to check if the data is editable
    
    private BasicInfoData bid = application.Main.getBasicInfoData();

    /**
     * Initializes the controller.
     * Sets up the TreeView with health metrics and establishes event listeners.
     */
    @FXML
    public void initialize() {
        initializeTreeView(); // Initialize the TreeView structure
        setupTreeViewListener(); // Attach listeners for TreeView events
        healthDataDisplayTextArea.setEditable(false); // Disable editing for TextArea by default

        // Narendra's changes 1
        hDRBackToTechnicianHomeButton.setOnAction(e -> backToHome());
    }

    // Narendra's changes 2
    private void backToHome() {

    	if(DBValidation.isAdmin(bid.getEmail())) {
    		Stage currentStage = (Stage) hDRBackToTechnicianHomeButton.getScene().getWindow();
        	SwitchScenes.switchScene("/application/technicianHome/TechnicianHome.fxml", "Home", currentStage);
    	} else {
    		Stage currentStage = (Stage) hDRBackToTechnicianHomeButton.getScene().getWindow();
        	SwitchScenes.switchScene("/application/DesignFiles/Home.fxml", "Home", currentStage);
    	}
	}

	/**
     * Initializes the TreeView with a hierarchy of health metrics.
     * It creates categories and their respective metrics.
     */
    private void initializeTreeView() {
        TreeItem<String> rootItem = new TreeItem<>("Health Metrics"); // Root node
        rootItem.setExpanded(true); // Expand root node by default

        // Create and add categories to the TreeView
        TreeItem<String> bloodMetrics = new TreeItem<>("Blood Metrics");
        bloodMetrics.getChildren().addAll(
                new TreeItem<>("Blood Glucose"),
                new TreeItem<>("Leucocyte Count"),
                new TreeItem<>("Erythrocyte Count"),
                new TreeItem<>("Blood Oxygen"),
                new TreeItem<>("Blood Pressure"),
                new TreeItem<>("Body Temperature")
        );

        TreeItem<String> heartMetrics = new TreeItem<>("Heart Metrics");
        heartMetrics.getChildren().addAll(
                new TreeItem<>("Heart Rate"),
                new TreeItem<>("Cholesterol Level"),
                new TreeItem<>("Coronary Artery Calcium")
        );

        TreeItem<String> respiratoryMetrics = new TreeItem<>("Respiratory Metrics");
        respiratoryMetrics.getChildren().addAll(
                new TreeItem<>("Respiratory Rate"),
                new TreeItem<>("Vital Capacity")
        );

        // Add categories to the root
        rootItem.getChildren().addAll(bloodMetrics, heartMetrics, respiratoryMetrics);
        healthMetricsTreeView.setRoot(rootItem); // Set the root node
    }

    /**
     * Sets up a listener for the TreeView to detect when a metric is selected.
     * When a metric is selected, its details are fetched from the server.
     */
    private void setupTreeViewListener() {
        healthMetricsTreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.isLeaf()) { // Only act on leaf nodes (metrics)
                selectedMetric = newValue.getValue(); // Get the selected metric
                fetchMetricValueFromServer(selectedMetric); // Fetch data for the selected metric
            }
        });
    }

    /**
     * Fetches the value of a selected metric from the server.
     * @param metric The selected metric.
     */
    private void fetchMetricValueFromServer(String metric) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String patientId = patientNameTextField.getText(); // Retrieve the patient ID
            out.println("GET:" + patientId + ":" + metric); // Send GET request to server
            String line = in.readLine(); // Read server response

            if (line != null && !line.equals("No records found.")) { // If record exists
                String[] parts = line.split(", ");
                if (parts.length == 5) {
                    originalRecordId = parts[0]; // Extract record ID
                    originalValue = parts[3]; // Extract value
                    healthDataDisplayTextArea.setText( // Display details in the TextArea
                            "Metric: " + metric + "\n" +
                            "Record ID: " + parts[0] + "\n" +
                            "Patient ID: " + parts[1] + "\n" +
                            "Value: " + parts[3] + "\n" +
                            "Date Recorded: " + parts[4]
                    );
                }
            } else {
                // Handle case where no record exists for the metric
                originalRecordId = null;
                originalValue = null;
                healthDataDisplayTextArea.setText("Metric: " + metric + "\nValue: No records found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to fetch data from the server.");
        }
    }

    /**
     * Enables editing of the selected metric's value.
     */
    @FXML
    private void onModifyData() {
        if (selectedMetric != null) {
            isEditable = true; // Allow editing
            healthDataDisplayTextArea.setEditable(true); // Enable TextArea editing

            if (originalValue == null) {
                // Inform user that a new record will be created
                showAlert("Info", "No existing record found for this metric. You can add a new value.");
            } else {
                showAlert("Info", "You can now modify the value in the TextArea.");
            }
        } else {
            showAlert("Error", "Please select a valid metric to modify.");
        }
    }

    /**
     * Saves the updated or new value of the selected metric to the server.
     */
    @FXML
    private void onSaveData() {
        if (isEditable) {
            String modifiedValue = null;
            String[] lines = healthDataDisplayTextArea.getText().split("\n");

            // Extract the modified value from the TextArea
            for (String line : lines) {
                if (line.startsWith("Value: ")) {
                    modifiedValue = line.replace("Value: ", "").trim();
                }
            }

            if (modifiedValue != null) {
                boolean success = saveToServer(originalRecordId, selectedMetric, modifiedValue);
                if (success) {
                    originalValue = modifiedValue; // Update local copy
                    healthDataDisplayTextArea.setEditable(false); // Disable editing
                    isEditable = false; // Reset editing flag
                    showAlert("Success", "Value successfully updated or added!");
                } else {
                    showAlert("Error", "Failed to save data to the server.");
                }
            } else {
                showAlert("Error", "No valid value found to save.");
            }
        } else {
            showAlert("Error", "Editing is not enabled.");
        }
    }

    /**
     * Sends a request to delete the selected metric from the server.
     */
    @FXML
    private void onDeleteData() {
        if (selectedMetric != null && originalRecordId != null) {
            if (deleteFromServer(originalRecordId)) {
                healthDataDisplayTextArea.clear(); // Clear the display area
                showAlert("Success", "Metric deleted successfully!");
            } else {
                showAlert("Error", "Failed to delete metric.");
            }
        } else {
            showAlert("Error", "Please select a metric to delete.");
        }
    }

    /**
     * Navigates back to the Technician Home view.
     */
    @FXML
    private void onBack() {
        if (mainApp != null) {
            mainApp.loadTechnicianHome(); // Switch to Technician Home
        }
    }

    /**
     * Sends a save or update request to the server.
     * @param recordId The ID of the record (null for new records).
     * @param metric The metric being saved or updated.
     * @param value The value for the metric.
     * @return True if the operation succeeds, false otherwise.
     */
    private boolean saveToServer(String recordId, String metric, String value) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String patientId = patientNameTextField.getText();

            if (metric == null || value == null || patientId.isEmpty()) {
                showAlert("Error", "Invalid data. Please ensure all fields are filled.");
                return false;
            }

            out.println("UPDATE_OR_INSERT:" + patientId + ":" + metric + ":" + value);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to connect to the server.");
            return false;
        }
    }

    /**
     * Sends a delete request to the server for a specific record.
     * @param recordId The ID of the record to delete.
     * @return True if the operation succeeds, false otherwise.
     */
    private boolean deleteFromServer(String recordId) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println("DELETE:" + recordId);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Displays an alert box with a message.
     * @param title The title of the alert box.
     * @param message The message to display.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Sets the reference to the main application.
     * @param mainApp The main application instance.
     */
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Populates the patient information fields with provided data.
     * @param name The patient's name.
     * @param age The patient's age.
     * @param gender The patient's gender.
     * @param bloodType The patient's blood type.
     * @param height The patient's height.
     * @param weight The patient's weight.
     */
    public void setPatientInfo(String name, String age, String gender, String bloodType, String height, String weight) {
        patientNameTextField.setText(name);
        ageTextField.setText(age);
        genderTextField.setText(gender);
        bloodTypeTextField.setText(bloodType);
        heightTextField.setText(height);
        weightTextField.setText(weight);
    }
}















