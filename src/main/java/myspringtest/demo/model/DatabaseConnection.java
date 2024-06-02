package myspringtest.demo.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import myspringtest.demo.User;

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
    
        public int getCountUsers() throws SQLException {
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
/*         public int getTotalUsers() throws SQLException {
            // Create a statement to execute SQL query
            statement = connection.createStatement();
        
            // Execute SQL query to get the count of rows in dbo.Users
            String query = "SELECT COUNT(*) FROM dbo.Users";
            ResultSet resultSet = statement.executeQuery(query);
        
            // Get the count from the first row of the result set
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        
            // If the result set is empty, return 0
            return 0;
        } */

        public List<User> getUsers() throws SQLException {
            List<User> users = new ArrayList<>();
            statement = connection.createStatement();
            String query = "SELECT * FROM dbo.Users";
            ResultSet resultSet = statement.executeQuery(query);
        
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String usertype = resultSet.getString("usertype");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
        
                User user = new User(name, age, usertype, email, password, id);
                users.add(user);
            }
            return users;
        }

        public List<String> usersToString() throws SQLException {
            // Establish connection to the database
            List<String> users = new ArrayList<>();
            // Create a statement to execute SQL query
            statement = connection.createStatement();
        
            // Execute SQL query to get data from dbo.Users
            String query = "SELECT * FROM dbo.Users";
            ResultSet resultSet = statement.executeQuery(query);
        
            while (resultSet.next()) {
                // Get user data from the result set
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String usertype = resultSet.getString("usertype");
                String email = resultSet.getString("email");
        
                // Create a string representation of the user
                String user = "[" + usertype.substring(0, 1).toUpperCase() + usertype.substring(1) + "] Nome: " + name 
                        + " | Email: " + email + " | Idade: " + age;
        
                // Add the user string to the list
                users.add(user);
            }
            return users;
        }
        
        public synchronized int addUser(String name, int age, String usertype, String email, String password) throws SQLException {
    String query = "INSERT INTO dbo.Users (name, age, usertype, email, password) VALUES (?, ?, ?, ?, ?)";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
        preparedStatement.setString(1, name);
        preparedStatement.setInt(2, age);
        preparedStatement.setString(3, usertype);
        preparedStatement.setString(4, email);
        preparedStatement.setString(5, password);
        preparedStatement.executeUpdate();

        try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
        }
    }
}

        public void deleteUser(int id) {
            String query = "DELETE FROM dbo.Users WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, id);
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == 0) {
                    System.out.println("No user found with id: " + id);
                } else {
                    System.out.println("User with id: " + id + " was deleted successfully.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error occurred while deleting user with id: " + id);
            }
        }


/*         public void updateUser(int id, String name, int age, String usertype, String email, String password)
                throws SQLException {
            statement = connection.createStatement();
            String query = String.format(
                    "UPDATE dbo.Users SET name = '%s', age = %d, usertype = '%s', email = '%s', password = '%s' WHERE id = %d",
                    name, age, usertype, email, password, id);
            statement.executeUpdate(query);
        } */

        public void updateUser(int id, String name, int age, String userType, String email, String password) throws SQLException {
            StringBuilder query = new StringBuilder("UPDATE dbo.Users SET ");
            boolean first = true;
        
            if (name != null && !name.isEmpty()) {
                query.append("name = '").append(name).append("'");
                first = false;
            }
            if (age > 0) {
                if (!first) query.append(", ");
                query.append("age = ").append(age);
                first = false;
            }
            if (userType != null && !userType.isEmpty()) {
                if (!first) query.append(", ");
                query.append("usertype = '").append(userType).append("'");
                first = false;
            }
            if (email != null && !email.isEmpty()) {
                if (!first) query.append(", ");
                query.append("email = '").append(email).append("'");
                first = false;
            }
            if (password != null && !password.isEmpty()) {
                if (!first) query.append(", ");
                query.append("password = '").append(password).append("'");
            }
            query.append(" WHERE id = ").append(id);
        
            statement = connection.createStatement();
            statement.executeUpdate(query.toString());
        }
        
        
    
    public boolean checkUser(String email, String password) throws SQLException {
        statement = connection.createStatement();
        String query = String.format("SELECT * FROM dbo.Users WHERE email = '%s' AND password = '%s'", email, password);
        ResultSet resultSet = statement.executeQuery(query);
        if (resultSet.next()) {
            return true;
        } else {
            return false;
        }
    }

    public String checkType(String email, String password) throws SQLException {
        statement = connection.createStatement();
        String query = String.format("SELECT usertype FROM dbo.Users WHERE email = '%s' AND password = '%s'", email, password);
        ResultSet resultSet = statement.executeQuery(query);
        if (resultSet.next()) {
            return resultSet.getString("usertype");
        } else {
            return null;
        }
    }

    public int getMaxUserId() throws SQLException {
        String query = "SELECT MAX(id) AS max_id FROM dbo.Users";
        ResultSet rs = statement.executeQuery(query);
        if (rs.next()) {
            return rs.getInt("max_id");
        } else {
            return 0;
        }
    }

    //getUser by id
    public User getUserById(int id) throws SQLException {
        String query = String.format("SELECT * FROM dbo.Users WHERE id = %d", id);
        ResultSet rs = statement.executeQuery(query);
        if (rs.next()) {
            String name = rs.getString("name");
            int age = rs.getInt("age");
            String usertype = rs.getString("usertype");
            String email = rs.getString("email");
            String password = rs.getString("password");
            return new User(name, age, usertype, email, password, id);
        } else {
            return null;
        }
    }

    public static void main(String[] args) throws SQLException {
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.getConnection();
        StudentDB sdb = new StudentDB(db.getConnection());
        //int contador = db.getUsers();
        //db.addUser(5, "Teste", 20, "admin11", "teste5@teste", "teste22");
        //System.out.println(db.checkType("charlie@example.com", "charliepassword"));
        //db.addUser(6, "Admin", 48, "admin", "admin@admin", "admin");
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
        //db.updateUser(5, "bbb", 0, "", "", "");
        db.deleteUser(10);

        //sdb.deleteStudent(8);
        //print ao getusers
        System.out.println(db.usersToString());
        System.out.println("\n");
        System.out.println(db.getUsers());
        db.closeConnection(connection);
    }
}