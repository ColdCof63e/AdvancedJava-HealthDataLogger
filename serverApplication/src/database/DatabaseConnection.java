package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Database credentials and connection details
    private static final String DB_URL = "jdbc:oracle:thin:@calvin.humber.ca:1521:grok";
    private static final String DB_USER = "N01690273"; //N01625427 N01690273
    private static final String DB_PASSWORD = "oracle"; // wyp123456

    // Method to establish and return a new database connection
    public static Connection getConnection() {
        try {
            // Load Oracle JDBC Driver
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Establish and return the connection
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("Oracle JDBC Driver not found. Ensure ojdbc.jar is added to your classpath.");
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            System.err.println("Failed to establish database connection.");
            e.printStackTrace();
            return null;
        }
    }

    // Method to close the database connection
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed successfully!");
            } catch (SQLException e) {
                System.err.println("Error while closing the database connection.");
                e.printStackTrace();
            }
        }
    }
}