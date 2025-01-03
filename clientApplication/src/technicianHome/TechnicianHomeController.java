package technicianHome;

import java.io.BufferedReader; // Import for reading data from a stream.
import java.io.InputStreamReader; // Import for converting byte streams into character streams.
import java.io.PrintWriter; // Import for writing output to a stream.
import java.net.Socket; // Import for network communication.
import java.util.ArrayList; // Import for ArrayList to store records.
import java.util.List; // Import for List interface.

import javafx.collections.FXCollections; // Import for creating an observable list.
import javafx.collections.ObservableList; // Import for observable data binding.
import javafx.fxml.FXML; // Import for FXML annotations.
// Import for various UI controls like TableView, ComboBox, etc.
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory; // Import for binding data to table columns.
import startingPoint.Main; // Import reference to the main application class.

public class TechnicianHomeController {

    private Main mainApp; // Reference to the main application.

    @FXML
    private MenuBar techHomeMenuBar; // Menu bar for navigation options.

    @FXML
    private TreeView<String> techHomeTreeView; // TreeView for navigation.

    @FXML
    private TextField techHomeUserNameOrIdTextField; // TextField for searching by user name or ID.

    @FXML
    private DatePicker techHomeDatePicker; // DatePicker for filtering by date.

    @FXML
    private ComboBox<String> techHomeGenderComboBox; // ComboBox for selecting gender as a filter.

    @FXML
    private Button techHomeSearchButton; // Button to initiate a search.

    @FXML
    private Button techHomeResetButton; // Button to reset all search filters.

    @FXML
    private Button techHomeLogOutButton; // Button to log out of the application.

    @FXML
    private Button techHomeViewButton; // Button to view selected health data.

    @FXML
    private TableView<Record> techHomeResultsTable; // TableView to display search results.

    @FXML
    private TableColumn<Record, String> techHomeRecordIdColumn; // Column for record ID.

    @FXML
    private TableColumn<Record, String> techHomePatientIdColumn; // Column for patient ID.

    @FXML
    private TableColumn<Record, String> techHomeUserNameColumn; // Column for user name.

    @FXML
    private TableColumn<Record, String> techHomeEntryCreatedTimeColumn; // Column for created time.

    @FXML
    private TableColumn<Record, String> techHomeEntryUpdatedTimeColumn; // Column for updated time.

    private static final String SERVER_HOST = "localhost"; // Host address of the server.
    private static final int SERVER_PORT = 8080; // Port number of the server.

    private ObservableList<Record> records = FXCollections.observableArrayList(); // ObservableList to bind data to the TableView.

    @FXML
    public void initialize() {
        // Initialize table columns with property bindings.
        techHomeRecordIdColumn.setCellValueFactory(new PropertyValueFactory<>("recordId"));
        techHomePatientIdColumn.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        techHomeUserNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        techHomeEntryCreatedTimeColumn.setCellValueFactory(new PropertyValueFactory<>("createdTime"));
        techHomeEntryUpdatedTimeColumn.setCellValueFactory(new PropertyValueFactory<>("updatedTime"));

        // Bind the observable list to the TableView.
        techHomeResultsTable.setItems(records);

        // Setup the TreeView for navigation.
        setupTreeView();

        // Load initial records from the server.
        loadRecordsFromServer();
    }

    private void setupTreeView() {
        TreeItem<String> rootItem = new TreeItem<>("Options"); // Root item for the TreeView.
        rootItem.setExpanded(true); // Expand the root by default.
        rootItem.getChildren().addAll(
                new TreeItem<>("Technician Home"),
                new TreeItem<>("Patient Account Management"),
                new TreeItem<>("My Account")
        );
        techHomeTreeView.setRoot(rootItem); // Set the root item.

        // Add a click event to handle double-click navigation.
        techHomeTreeView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Check for double-click.
                TreeItem<String> selectedItem = techHomeTreeView.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    // Navigate based on the selected item's value.
                    switch (selectedItem.getValue()) {
                        case "Patient Account Management" -> mainApp.loadPatientAccountManagement();
                        case "Technician Home" -> mainApp.loadTechnicianHome();
                        case "My Account" -> System.out.println("Navigating to My Account (Placeholder for future integration).");
                    }
                }
            }
        });
    }

    private void loadRecordsFromServer() {
        // Fetch all records from the server.
        List<Record> fetchedRecords = fetchRecordsFromServer(null);
        System.out.println("Fetched Records: "); // Debugging log.
        for (Record record : fetchedRecords) {
            System.out.println("Record ID: " + record.getRecordId() + ", Patient ID: " + record.getPatientId());
        }
        records.setAll(fetchedRecords); // Update the observable list.
    }

    private List<Record> fetchRecordsFromServer(String patientId) {
        List<Record> fetchedRecords = new ArrayList<>(); // Temporary list to store fetched records.

        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT); // Connect to the server.
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true); // Send requests to the server.
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) { // Read responses.

            // Send a GET request to the server (all records if patientId is null).
            out.println(patientId == null ? "GET:ALL" : "GET:" + patientId);

            // Read and parse each line of response.
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("Fetched Line: " + line); // Debugging log for each line.
                String[] parts = line.split(", "); // Split the line into parts.
                if (parts.length == 5) {
                    // Create a new Record object and add it to the list.
                    fetchedRecords.add(new Record(parts[0], parts[1], parts[2], parts[3], parts[4]));
                }
            }
        } catch (Exception e) {
            // Handle exceptions and show an error alert.
            e.printStackTrace();
            showAlert("Error", "Failed to fetch records from the server.");
        }

        return fetchedRecords; // Return the fetched records.
    }

    @FXML
    private void onSearch() {
        // Get the patient ID from the search field.
        String patientId = techHomeUserNameOrIdTextField.getText().trim();
        // Fetch records matching the search criteria.
        List<Record> filteredRecords = fetchRecordsFromServer(patientId.isEmpty() ? null : patientId);
        System.out.println("Fetched Records After Search: "); // Debugging log.
        for (Record record : filteredRecords) {
            System.out.println("Record ID: " + record.getRecordId() + ", Patient ID: " + record.getPatientId());
        }
        records.setAll(filteredRecords); // Update the observable list.
    }

    @FXML
    private void onReset() {
        // Clear all search filters.
        techHomeUserNameOrIdTextField.clear();
        techHomeDatePicker.setValue(null);
        techHomeGenderComboBox.getSelectionModel().clearSelection();
        loadRecordsFromServer(); // Reload all records.
    }

    @FXML
    private void onViewHealthData() {
        // Get the selected record from the TableView.
        Record selectedRecord = techHomeResultsTable.getSelectionModel().getSelectedItem();
        if (selectedRecord != null) {
            // Load the Health Data Records view with the selected record's details.
            mainApp.loadHealthDataRecords(
                    selectedRecord.getPatientId(),
                    "30", // Placeholder age.
                    "Male", // Placeholder gender.
                    "O+", // Placeholder blood type.
                    "180 cm", // Placeholder height.
                    "80 kg" // Placeholder weight.
            );
        } else {
            // Show an error alert if no record is selected.
            showAlert("No Selection", "Please select a record to view.");
        }
    }

    @FXML
    private void onLogOut() {
        // Placeholder for logging out (navigate back to login screen).
        System.out.println("Logging out... (Returning to the login screen placeholder).");
    }

    private void showAlert(String title, String message) {
        // Utility method to show an alert dialog.
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp; // Set the reference to the main application.
    }

    // Inner class to represent a record in the TableView.
    public static class Record {
        private final String recordId; // ID of the record.
        private final String patientId; // ID of the patient.
        private final String userName; // Name of the patient/user.
        private final String createdTime; // Time when the record was created.
        private final String updatedTime; // Time when the record was last updated.

        public Record(String recordId, String patientId, String userName, String createdTime, String updatedTime) {
            this.recordId = recordId;
            this.patientId = patientId;
            this.userName = userName;
            this.createdTime = createdTime;
            this.updatedTime = updatedTime;
        }

        public String getRecordId() {
            return recordId;
        }

        public String getPatientId() {
            return patientId;
        }

        public String getUserName() {
            return userName;
        }

        public String getCreatedTime() {
            return createdTime;
        }

        public String getUpdatedTime() {
            return updatedTime;
        }
    }
}
















