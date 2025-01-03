package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseCommunication {

    private static final String DB_URL = "jdbc:oracle:thin:@calvin.humber.ca:1521:grok";
    private static final String DB_USERNAME = "n01690273";
    private static final String DB_PASSWORD = "oracle";

    // Method to get the database connection
    public static Connection getConnection() {
        try {
            if (Class.forName("oracle.jdbc.driver.OracleDriver") != null) {
                return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
        return null;
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

    // Insert data into the 'users' table
    public static void insertData(String email, String password, String name, String isTech, String phoneNumber,
                                  int height, int weight, String address, String gender, String bloodType, LocalDate dob) throws SQLException {
        String insertQuery = "INSERT INTO users (email, password, name, isTech, phoneNumber, height, weight, address, gender, bloodType, DOB)"
                           + " VALUES (?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, name);
            preparedStatement.setString(4, isTech);
            preparedStatement.setString(5, phoneNumber);
            preparedStatement.setInt(6, height);
            preparedStatement.setInt(7, weight);
            preparedStatement.setString(8, address);
            preparedStatement.setString(9, gender);
            preparedStatement.setString(10, bloodType);
            preparedStatement.setDate(11, java.sql.Date.valueOf(dob));  // Convert LocalDate to SQL Date

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error inserting data: " + e.getMessage());
        }
    }

    // Read all user data from the 'users' table
    public static List<Credentials> readData() {
        List<Credentials> credentials = new ArrayList<>();
        String selectQuery = "SELECT * FROM users";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(selectQuery)) {

            while (rs.next()) {
                String email = rs.getString("email");
                String password = rs.getString("password");
                String name = rs.getString("name");
                String isTech = rs.getString("isTech");
                String phoneNumber = rs.getString("phoneNumber");
                int height = rs.getInt("height");
                int weight = rs.getInt("weight");
                String address = rs.getString("address");
                String gender = rs.getString("gender");
                String bloodType = rs.getString("bloodType");
                LocalDate dob = rs.getDate("DOB").toLocalDate();  // Convert SQL Date to LocalDate

                credentials.add(new Credentials(email, password, name, isTech, phoneNumber, height, weight, address, gender, bloodType, dob));
            }
        } catch (SQLException e) {
            System.err.println("Error reading data: " + e.getMessage());
        }
        return credentials;
    }

    // Read user data by email from the 'users' table
    public static List<Credentials> readData(String emailID) {
        List<Credentials> credentials = new ArrayList<>();
        String selectQuery = "SELECT * FROM users WHERE email = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(selectQuery)) {

            statement.setString(1, emailID);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    String email = rs.getString("email");
                    String password = rs.getString("password");
                    String name = rs.getString("name");
                    String isTech = rs.getString("isTech");
                    String phoneNumber = rs.getString("phoneNumber");
                    int height = rs.getInt("height");
                    int weight = rs.getInt("weight");
                    String address = rs.getString("address");
                    String gender = rs.getString("gender");
                    String bloodType = rs.getString("bloodType");
                    LocalDate dob = rs.getDate("DOB").toLocalDate();  // Convert SQL Date to LocalDate

                    credentials.add(new Credentials(email, password, name, isTech, phoneNumber, height, weight, address, gender, bloodType, dob));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error reading data by email: " + e.getMessage());
        }
        return credentials;
    }

    // Update password for a user in the 'users' table
    public static void updatePassword(String email, String newPassword) {
        String resetPasswordQuery = "UPDATE users SET password = ? WHERE email = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(resetPasswordQuery)) {

            preparedStatement.setString(1, newPassword);
            preparedStatement.setString(2, email);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Password successfully reset!");
            } else {
                System.out.println("Password failed to reset!");
            }
        } catch (SQLException e) {
            System.err.println("Error updating password: " + e.getMessage());
        }
    }
}
