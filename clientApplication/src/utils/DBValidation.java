package utils;

import java.util.List;


public class DBValidation {

    // Check if an email exists in the database
    public static boolean emailExists(String email) {
        List<Credentials> DBCredentials = DatabaseCommunication.readData();
        for (Credentials cred : DBCredentials) {
            if (cred.getEmailID().trim().equalsIgnoreCase(email.trim())) {
                return true;
            }
        }
        return false;
    }

    // Validate if the provided credentials (email and password) match any record in the database
    public static boolean isCredentialsValid(String email, String password) {
        List<Credentials> DBCredentials = DatabaseCommunication.readData(email);
        for (Credentials cred : DBCredentials) {
            if (cred.getEmailID().equalsIgnoreCase(email) && cred.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    // Check if the user is an admin or tech (returns true if the user is a tech/admin, otherwise false)
    public static boolean isAdmin(String email) {
        List<Credentials> creds = DatabaseCommunication.readData(email);  // Read data for the given email
        for (Credentials cred : creds) {
            // Assuming isTech is stored as a "true" or "false" string
            return Boolean.parseBoolean(cred.getIsTech());
        }
        return false;
    }
}