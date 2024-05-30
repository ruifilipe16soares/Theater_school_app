package myspringtest.demo.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection2 {

    String server = "192.168.100.14";
    String database = "BD_PL4_10";
    String username = "User_BD_PL4_10";
    String password = "diubi:2023!BD!PL4_10";
    String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    String url = "jdbc:sqlserver://" + server + ";databaseName=" + database;

    public static void main(String[] args) {
        DatabaseConnection2 db = new DatabaseConnection2();

        // Test connection and get data from dbo.Estacao
        db.getData();
    }

    public void getData() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // Load the JDBC driver
            Class.forName(driver);

            // Establish connection to the database
            connection = DriverManager.getConnection(url, username, password);

            // Create a statement to execute SQL query
            statement = connection.createStatement();

            // Execute SQL query to get data from dbo.Estacao
            String query = "SELECT * FROM dbo.Localizacao";
            resultSet = statement.executeQuery(query);

            // Process the result set
            while (resultSet.next()) {
                // Replace 'columnName' with the actual column names from your dbo.Estacao table

                String name = resultSet.getString("Nome");
                String lat = resultSet.getString("Latitude");
                

                // Print the fetched data
                System.out.println("Name: " + name + ", Latitude: " + lat );
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close the resources
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
