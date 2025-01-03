package patientAccountManagement;

// Import necessary JavaFX classes and main application class
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import startingPoint.Main;

// Controller for managing the Patient Account Management view
public class PatientAccountManagementController {

    private Main mainApp;  // Reference to the main application instance

    @FXML
    private TreeView<String> patientAMTreeView;  // TreeView for navigation options

    @FXML
    private TextField patientAMUserNameOrIdTextField;  // Text field for username or ID input to search

    @FXML
    private DatePicker patientAMDatePicker;  // Date picker to filter records by date

    @FXML
    private ComboBox<String> patientAMRegionComboBox;  // ComboBox to filter records by region

    @FXML
    private TableView<Record> patientAMTableView;  // TableView to display account records

    @FXML
    private TableColumn<Record, String> patientAMRecordIdColumn;  // Column for record ID

    @FXML
    private TableColumn<Record, String> patientAMUserNameColumn;  // Column for username

    @FXML
    private TableColumn<Record, String> patientAMRoleColumn;  // Column for role

    @FXML
    private TableColumn<Record, String> patientAMAccCreatedTimeColumn;  // Column for account creation date

    @FXML
    private TableColumn<Record, String> patientAMAccUpdatedTimeColumn;  // Column for account last updated date

    @FXML
    private TableColumn<Record, String> patientAMStatusColumn;  // Column for account status

    private ObservableList<Record> records = FXCollections.observableArrayList();  // List to hold all records

    @FXML
    // Initializes the controller after FXML file is loaded
    public void initialize() {
        // Initialize navigation TreeView with options
        TreeItem<String> rootItem = new TreeItem<>("Options");  // Root item for the TreeView
        rootItem.setExpanded(true);  // Expand root item by default
        TreeItem<String> technicianHomeItem = new TreeItem<>("Technician Home");  // Technician Home option
        TreeItem<String> patientAccountManagementItem = new TreeItem<>("Patient Account Management");  // Patient Account Management option
        TreeItem<String> myAccountItem = new TreeItem<>("My Account");  // My Account option
        rootItem.getChildren().addAll(technicianHomeItem, patientAccountManagementItem, myAccountItem);
        patientAMTreeView.setRoot(rootItem);  // Set root item in TreeView

        // Set double-click listener for TreeView items
        patientAMTreeView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {  // Check if item was double-clicked
                TreeItem<String> selectedItem = patientAMTreeView.getSelectionModel().getSelectedItem();  // Get selected item
                if (selectedItem != null) {
                    // Switch view based on selected item
                    switch (selectedItem.getValue()) {
                        case "Technician Home":
                            mainApp.loadTechnicianHome();  // Load Technician Home view
                            break;
                    }
                }
            }
        });

        // Initialize ComboBox with region options
        patientAMRegionComboBox.setItems(FXCollections.observableArrayList("Ontario", "Quebec", "British Columbia"));

        // Link TableView columns with Record properties
        patientAMRecordIdColumn.setCellValueFactory(cellData -> cellData.getValue().recordIdProperty());
        patientAMUserNameColumn.setCellValueFactory(cellData -> cellData.getValue().userNameProperty());
        patientAMRoleColumn.setCellValueFactory(cellData -> cellData.getValue().roleProperty());
        patientAMAccCreatedTimeColumn.setCellValueFactory(cellData -> cellData.getValue().accCreatedTimeProperty());
        patientAMAccUpdatedTimeColumn.setCellValueFactory(cellData -> cellData.getValue().accUpdatedTimeProperty());
        patientAMStatusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        loadRecords();  // Load sample records into TableView
    }

    // Load initial data into the records list
    private void loadRecords() {
        records.addAll(  // Add sample records to the list
            new Record("00001", "Yipeng Wang", "Patient", "2023-12-10", "2023-12-12", "Active"),
            new Record("00002", "Narendra", "Technician", "2023-11-11", "2023-11-12", "Inactive")
        );
        patientAMTableView.setItems(records);  // Set records as items for TableView
    }

    // Event handler for the "Search" button
    @FXML
    private void onSearch() {
        // Get filter values from input fields
        String userNameOrId = patientAMUserNameOrIdTextField.getText();
        String selectedDate = (patientAMDatePicker.getValue() != null) ? patientAMDatePicker.getValue().toString() : "";
        String selectedRegion = patientAMRegionComboBox.getValue();

        // Filter records based on search criteria
        ObservableList<Record> filteredRecords = FXCollections.observableArrayList();
        for (Record record : records) {
            // Check if each record matches the search filters
            boolean matchesUserNameOrId = userNameOrId.isEmpty() || record.getUserName().contains(userNameOrId) || record.getRecordId().contains(userNameOrId);
            boolean matchesDate = selectedDate.isEmpty() || record.getAccCreatedTime().equals(selectedDate);
            boolean matchesRegion = selectedRegion == null || selectedRegion.isEmpty() || record.getRole().equals(selectedRegion);

            // Add record to filtered list if it matches all criteria
            if (matchesUserNameOrId && matchesDate && matchesRegion) {
                filteredRecords.add(record);
            }
        }

        // Update TableView with filtered records
        patientAMTableView.setItems(filteredRecords);
        System.out.println("Search action triggered with filters - ID/Name: " + userNameOrId + ", Date: " + selectedDate + ", Region: " + selectedRegion);
    }

    // Event handler for the "Reset" button
    @FXML
    private void onReset() {
        // Clear all search input fields and reset TableView to original data
        patientAMUserNameOrIdTextField.clear();
        patientAMDatePicker.setValue(null);
        patientAMRegionComboBox.getSelectionModel().clearSelection();
        patientAMTableView.setItems(records);  // Reset TableView to show all records
    }

    // Event handler for the "View Account" button
    @FXML
    private void onViewAccount() {
        Record selectedRecord = patientAMTableView.getSelectionModel().getSelectedItem();  // Get selected record from TableView
        if (selectedRecord != null) {
            System.out.println("Switching to My Account view for: " + selectedRecord.getUserName());
            // Placeholder for integrating with the "My Account" view created by another team member
        } else {
            System.out.println("Please select a record to view.");  // Message if no record is selected
        }
    }

    // Method to set main application reference
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    // Inner class representing a record in the TableView
    public static class Record {
        private final StringProperty recordId;  // Record ID property
        private final StringProperty userName;  // User name property
        private final StringProperty role;  // Role property
        private final StringProperty accCreatedTime;  // Account creation date property
        private final StringProperty accUpdatedTime;  // Account update date property
        private final StringProperty status;  // Account status property

        // Constructor for Record class
        public Record(String recordId, String userName, String role, String accCreatedTime, String accUpdatedTime, String status) {
            this.recordId = new SimpleStringProperty(recordId);
            this.userName = new SimpleStringProperty(userName);
            this.role = new SimpleStringProperty(role);
            this.accCreatedTime = new SimpleStringProperty(accCreatedTime);
            this.accUpdatedTime = new SimpleStringProperty(accUpdatedTime);
            this.status = new SimpleStringProperty(status);
        }

        // Property getters for TableView columns
        public StringProperty recordIdProperty() { return recordId; }
        public StringProperty userNameProperty() { return userName; }
        public StringProperty roleProperty() { return role; }
        public StringProperty accCreatedTimeProperty() { return accCreatedTime; }
        public StringProperty accUpdatedTimeProperty() { return accUpdatedTime; }
        public StringProperty statusProperty() { return status; }

        // Getters for each property value
        public String getRecordId() { return recordId.get(); }
        public String getUserName() { return userName.get(); }
        public String getRole() { return role.get(); }
        public String getAccCreatedTime() { return accCreatedTime.get(); }
    }
}




