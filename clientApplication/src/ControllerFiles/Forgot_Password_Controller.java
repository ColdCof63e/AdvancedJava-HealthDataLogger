package ControllerFiles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import utils.DBValidation;
import utils.SwitchScenes;
import utils.Validators;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Forgot_Password_Controller implements Initializable {

    // Text fields for userID and verification code
    @FXML
    private TextField userID, verifyCode;

    // Password Fields for new password and confirming new password
    @FXML
    private PasswordField newPassword, confirmPassword;

    // Prompt Label for invalid entry
    @FXML
    private Label promptLabel;

    // Single button where text changes upon meeting specific conditions
    @FXML
    private Button uniButton;

    private int code;
    private String activeUserEmail;

    // Server details
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8080;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // Initial UI Setup
        verifyCode.setVisible(false);
        newPassword.setVisible(false);
        confirmPassword.setVisible(false);
        promptLabel.setVisible(false);

        uniButton.setText("Send Code");

        // Button action handler
        uniButton.setOnAction(e -> resetPassword());

        // Center stage
        Platform.runLater(() -> {
            Stage currentStage = (Stage) uniButton.getScene().getWindow();
            currentStage.centerOnScreen();
            currentStage.setMaximized(false);
        });
    }

    // Generate 6-digit verification code
    public int generateVerificationCode() {
        Random random = new Random();
        return 100000 + random.nextInt(900000);
    }

    @FXML
    public void resetPassword() {
        try {
            // Clear previous prompt messages
            promptLabel.setText("");
            promptLabel.setVisible(false);

            // Check if email exists
//            if (!Validators.emailAvailability(userID.getText())) {
            if(!DBValidation.emailExists(userID.getText())) {
                promptLabel.setText("Email does not exist!");
                promptLabel.setVisible(true);
                return;
            }

            // Handle "Send Code" button click
            if (uniButton.getText().equalsIgnoreCase("Send Code")) {
                code = generateVerificationCode();

                // Simulate sending code (in a real-world scenario, an email would be sent)
                System.out.println("Verification Code: " + code);

                // Send code to the server for logging and email dispatch
                sendCodeToServer(userID.getText(), code);

                uniButton.setText("Verify Code");
                verifyCode.setVisible(true);
                return;
            }

            // Handle "Verify Code" button click
            if (uniButton.getText().equalsIgnoreCase("Verify Code")) {
                try {
                    if (verifyCode.getText().isEmpty()) {
                        promptLabel.setText("Please enter the verification code.");
                        promptLabel.setVisible(true);
                        return;
                    }

                    int enteredCode = Integer.parseInt(verifyCode.getText());

                    if (enteredCode == code) {
                        promptLabel.setText("Code Verified Successfully!");
                        promptLabel.setVisible(true);

                        newPassword.setVisible(true);
                        confirmPassword.setVisible(true);
                        uniButton.setText("Reset Password");
                    } else {
                        promptLabel.setText("Incorrect verification code.");
                        promptLabel.setVisible(true);
                        verifyCode.clear();
                    }

                } catch (NumberFormatException n) {
                    promptLabel.setText("Invalid verification code format!");
                    promptLabel.setVisible(true);
                }
                return;
            }

            // Handle "Reset Password" button click
            if (uniButton.getText().equalsIgnoreCase("Reset Password")) {
                // Check if new password fields are filled
                if (Validators.emptyFieldCheck(newPassword)) {
                    promptLabel.setText("Enter a new password.");
                    promptLabel.setVisible(true);
                    return;
                }

                if (Validators.emptyFieldCheck(confirmPassword)) {
                    promptLabel.setText("Please confirm the new password.");
                    promptLabel.setVisible(true);
                    return;
                }

                // Check if passwords match
                if (!Validators.passCheck(newPassword.getText(), confirmPassword.getText())) {
                    promptLabel.setText("Passwords do not match.");
                    promptLabel.setVisible(true);
                    return;
                }

                // Send the new password to the server
                sendPasswordToServer(activeUserEmail, newPassword.getText());

                // Reset code after successful password change
                code = -1;

                // Switch to the main screen after successful password reset
                Stage currentStage = (Stage) uniButton.getScene().getWindow();
                SwitchScenes.switchScene("/application/DesignFiles/Main.fxml", "Login", currentStage);
            }

        } catch (Exception e) {
            // Catch any unexpected errors
            promptLabel.setText("An unexpected error occurred!");
            promptLabel.setVisible(true);
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    // Method to send the verification code to the server
    private void sendCodeToServer(String email, int code) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Send the verification code request to the server
            String message = String.format("SEND_CODE:%s,%d", email, code);
            out.println(message);

            // Read server response (for now, we just print it)
            String response = in.readLine();
            if (response != null && response.equals("SUCCESS")) {
                System.out.println("Code sent successfully!");
            } else {
                System.out.println("Failed to send the code.");
            }

        } catch (IOException e) {
            promptLabel.setText("Network error: Unable to send code.");
            promptLabel.setVisible(true);
            e.printStackTrace();
        }
    }

    // Method to send the new password to the server for updating in the database
    private void sendPasswordToServer(String email, String newPassword) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Send the password reset request to the server
            String message = String.format("UPDATE_PASSWORD:%s,%s", email, newPassword);
            out.println(message);

            // Read server response
            String response = in.readLine();
            if (response != null && response.equals("SUCCESS")) {
                System.out.println("Password reset successfully.");
            } else {
                System.out.println("Password reset failed.");
            }

        } catch (IOException e) {
            promptLabel.setText("Network error: Unable to reset password.");
            promptLabel.setVisible(true);
            e.printStackTrace();
        }
    }
}
