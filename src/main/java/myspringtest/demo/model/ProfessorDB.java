package myspringtest.demo.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import myspringtest.demo.Professor;

public class ProfessorDB {

    private Connection connection;
    private Statement statement;

    public ProfessorDB(Connection connection) throws SQLException {
        this.connection = connection;
        this.statement = connection.createStatement();
    }

    public synchronized List<Professor> getProfessors() throws SQLException {
        List<Professor> professors = new ArrayList<>();
        String query = "SELECT * FROM dbo.Professor";
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            int id_user = resultSet.getInt("id_user");
            float salary = resultSet.getFloat("salary");
            int date = resultSet.getInt("entry_date");
            String education = resultSet.getString("education");
            Professor professor = new Professor(id, id_user, salary, date, education);
            professors.add(professor);
        }

        return professors;
    }

    public synchronized void addProfessorDiscipline(int professorId, int disciplineId) throws SQLException {
        String query = "INSERT INTO dbo.Professor_Discipline (professor_id, discipline_id) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, professorId);
            preparedStatement.setInt(2, disciplineId);
            preparedStatement.executeUpdate();
        }
    }

    public synchronized void addProfessor(int id_user, float salary, int entry_date, String education) throws SQLException {
        String query = "INSERT INTO dbo.Professor (id_user, salary, entry_date, education) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id_user);
            preparedStatement.setFloat(2, salary);
            preparedStatement.setInt(3, entry_date);
            preparedStatement.setString(4, education);
            preparedStatement.executeUpdate();
        }
    }

    public synchronized void updateProfessor(int id, int id_user, float salary, int entry_date, String education) throws SQLException {
        String query = "UPDATE dbo.Professor SET id_user = ?, salary = ?, entry_date = ?, education = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id_user);
            preparedStatement.setFloat(2, salary);
            preparedStatement.setInt(3, entry_date);
            preparedStatement.setString(4, education);
            preparedStatement.setInt(5, id);
            preparedStatement.executeUpdate();
        }
    }

    public synchronized void deleteProfessor(int id) throws SQLException {
        // Delete from Professor_Discipline table
        String deleteProfessorDisciplineQuery = "DELETE FROM dbo.Professor_Discipline WHERE professor_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteProfessorDisciplineQuery)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }

        // Delete from Professor table
        String deleteProfessorQuery = "DELETE FROM dbo.Professor WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteProfessorQuery)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

        public synchronized List<Professor> getProfessorsByDiscipline(int disciplineId) throws SQLException {
        List<Professor> professors = new ArrayList<>();
        String query = "SELECT p.* FROM dbo.Professor p " +
                       "JOIN dbo.Professor_Discipline pd ON p.id = pd.professor_id " +
                       "WHERE pd.discipline_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, disciplineId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int id_user = resultSet.getInt("id_user");
                float salary = resultSet.getFloat("salary");
                int date = resultSet.getInt("entry_date");
                String education = resultSet.getString("education");
                Professor professor = new Professor(id, id_user, salary, date, education);
                professors.add(professor);
            }
        }

        return professors;
    }

    public static void main(String[] args) throws SQLException {
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.getConnection();
        ProfessorDB professorDB = new ProfessorDB(connection);

        // Add a professor
        // professorDB.addProfessor(1, 50000.0f, 20230101, "PhD in Computer Science");

        // Update a professor
        // professorDB.updateProfessor(1, 1, 55000.0f, 20230101, "PhD in Mathematics");

        // Add a professor-discipline association
        // professorDB.addProfessorDiscipline(1, 1); // Example professor ID and discipline ID

        // Delete a professor
        // professorDB.deleteProfessor(1);

        // List all professors
        List<Professor> professors = professorDB.getProfessors();
        for (Professor professor : professors) {
            System.out.println(professor.toString());
        }

        db.closeConnection(connection); // Ensure to close the connection after use
    }
}
