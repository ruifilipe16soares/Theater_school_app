package myspringtest.demo.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public DatabaseConnection() {
        // Database connection settings
        String server = "192.168.100.14";
        String database = "BD_PL4_10";
        String username = "User_BD_PL4_10";
        String password = "diubi:2023!BD!PL4_10";
        String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

        // Create a connection string
        String connectionUrl = String.format(
            "jdbc:sqlserver://%s;databaseName=%s;user=%s;password=%s;",
            server, database, username, password
        );

        Connection connection = null;

        try {
            // Load the JDBC driver
            Class.forName(driver);

            // Establish the connection
            connection = DriverManager.getConnection(connectionUrl);
            System.out.println("Connection established successfully.");

            // Perform database operations

        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Failed to establish connection: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("Connection closed.");
                } catch (SQLException e) {
                    System.err.println("Failed to close connection: " + e.getMessage());
                }
            }
        }
    }
}