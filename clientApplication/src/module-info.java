/**
 * 
 */
/**
 * 
 */
module clientApplication {
	requires javafx.graphics;
	requires javafx.fxml;
	requires javafx.controls;
	requires java.sql;
	
	exports application;
	exports ControllerFiles;
	exports utils;
	
	opens ControllerFiles to javafx.fxml;
	opens technicianHome to javafx.fxml;
    opens patientAccountManagement to javafx.fxml;
    opens healthDataRecords to javafx.fxml;
}