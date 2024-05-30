package myspringtest.demo.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

        String server = "192.168.100.14";
        String database = "BD_PL4_10";
        String username = "User_BD_PL4_10";
        String password = "diubi:2023!BD!PL4_10";
        String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

        Connection connection = null;
        Statement statement = null;
        public DatabaseConnection() {
            // Database connection settings

            // Create a connection string


        }
    
        public Connection getConnection() {
            
            
            String connectionUrl = String.format(
                    "jdbc:sqlserver://%s;databaseName=%s;user=%s;password=%s;encrypt=true;trustServerCertificate=true;",
                    server, database, username, password);
            try {
                // Load the JDBC driver
                Class.forName(driver);

                // Establish the connection
                connection = DriverManager.getConnection(connectionUrl);
                System.out.println("Connection established successfully.");
                return connection;
                // Perform database operations
            } catch (ClassNotFoundException e) {
                System.err.println("JDBC Driver not found: " + e.getMessage());
                return null;
            } catch (SQLException e) {
                System.err.println("Failed to establish connection: " + e.getMessage());
                return null;
            } 
        }

        public void closeConnection(Connection connection) {
            try {
                connection.close();
                System.out.println("Connection closed.");
            } catch (SQLException e) {
                System.err.println("Failed to close connection: " + e.getMessage());
            }
        }
    
        public int getUsers() throws SQLException {
            // Establish connection to the database
            int count = 0;
            // Create a statement to execute SQL query
            statement = connection.createStatement();

            // Execute SQL query to get data from dbo.Estacao
            String query = "SELECT * FROM dbo.Users";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                // Replace 'columnName' with the actual column names from your dbo.Estacao table
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String usertype = resultSet.getString("usertype");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                count++;

                System.out.println("ID: " + id + ", Name: " + name + ", Age: " + age + ", Usertype: " + usertype
                        + ", Email: " + email + ", Password: " + password);
            }
            return count;
        }
        
        public void addUser(int id, String name, int age, String usertype, String email, String password)
                throws SQLException {
            statement = connection.createStatement();
            String query = String.format(
                    "INSERT INTO dbo.Users (id, name, age, usertype, email, password) VALUES (%d, '%s', %d, '%s', '%s', '%s')",
                    id, name, age, usertype, email, password);
            statement.executeUpdate(query);
        }

        public void deleteUser(int id) throws SQLException {
            statement = connection.createStatement();
            String query = String.format("DELETE FROM dbo.Users WHERE id = %d", id);
            statement.executeUpdate(query);
        }

        public void updateUser(int id, String name, int age, String usertype, String email, String password)
                throws SQLException {
            statement = connection.createStatement();
            String query = String.format(
                    "UPDATE dbo.Users SET name = '%s', age = %d, usertype = '%s', email = '%s', password = '%s' WHERE id = %d",
                    name, age, usertype, email, password, id);
            statement.executeUpdate(query);
        }
    
    public boolean  checkUser(String email, String password) throws SQLException {
        statement = connection.createStatement();
        String query = String.format("SELECT * FROM dbo.Users WHERE email = '%s' AND password = '%s'", email, password);
        ResultSet resultSet = statement.executeQuery(query);
        if (resultSet.next()) {
            return true;
        } else {
            return false;
        }
    }
    public static void main(String[] args) throws SQLException {
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.getConnection();
        int contador = db.getUsers();
        db.addUser(5, "Teste", 20, "admin11", "teste5@teste", "teste22");
        //db.deleteUser(3);
        System.out.println("---------------------------------------------------------------------");
        //db.updateUser(4,"Teste2", 80, "user", "alterar@al", "change shit");
        //boolean a = db.checkUser("bob@example.com", "bobpassword");
        //System.out.println("o bolean a " + a);
        //boolean b = db.checkUser("bob1@example.com", "bobpassword");
        //System.out.println("o bolean b " + b);
        //boolean c = db.checkUser("bob@example.com", "bobpasswor");
        //System.out.println("o bolean c " + c);
        db.getUsers();
        db.closeConnection(connection);
    }
}