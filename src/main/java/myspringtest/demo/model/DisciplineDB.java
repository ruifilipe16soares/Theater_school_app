package myspringtest.demo.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import myspringtest.demo.Discipline;

public class DisciplineDB {

    private Connection connection;
    private Statement statement;

    public DisciplineDB(Connection connection) throws SQLException {
        this.connection = connection;
        this.statement = connection.createStatement();
    }

    public synchronized List<Discipline> getDisciplines() throws SQLException {
        List<Discipline> disciplines = new ArrayList<>();
        String query = "SELECT * FROM dbo.Discipline";
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String description = resultSet.getString("description");
            String schedule = resultSet.getString("schedule");
            Discipline discipline = new Discipline(id, name, description, schedule);
            disciplines.add(discipline);
        }

        return disciplines;
    }

    public synchronized void addDiscipline(String name, String description, String schedule) throws SQLException {
        String query = "INSERT INTO dbo.Discipline (name, description, schedule) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, schedule);
            preparedStatement.executeUpdate();
        }
    }

/*     public synchronized void updateDiscipline(int id, String name, String description, String schedule) throws SQLException {
        String query = "UPDATE dbo.Discipline SET name = ?, description = ?, schedule = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, schedule);
            preparedStatement.setInt(4, id);
            preparedStatement.executeUpdate();
        }
    } */

    public synchronized void updateDiscipline(int id, String name, String description, String schedule) throws SQLException {
        StringBuilder query = new StringBuilder("UPDATE dbo.Discipline SET ");
        boolean first = true;
    
        if (name != null && !name.isEmpty()) {
            query.append("name = '").append(name).append("'");
            first = false;
        }
        if (description != null && !description.isEmpty()) {
            if (!first) query.append(", ");
            query.append("description = '").append(description).append("'");
            first = false;
        }
        if (schedule != null && !schedule.isEmpty()) {
            if (!first) query.append(", ");
            query.append("schedule = '").append(schedule).append("'");
        }
        query.append(" WHERE id = ").append(id);
    
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query.toString());
        }
    }
    

    public synchronized void deleteDiscipline(int id) throws SQLException {
        // Delete from Course_Discipline table
        String deleteCourseDisciplineQuery = "DELETE FROM dbo.Course_Discipline WHERE discipline_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteCourseDisciplineQuery)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }

        // Delete from Professor_Discipline table
        String deleteProfessorDisciplineQuery = "DELETE FROM dbo.Professor_Discipline WHERE discipline_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteProfessorDisciplineQuery)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }

        // Delete from Discipline table
        String deleteDisciplineQuery = "DELETE FROM dbo.Discipline WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteDisciplineQuery)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    // New method to add a course-discipline association
    public synchronized void addCourseDiscipline(int courseId, int disciplineId) throws SQLException {
        String query = "INSERT INTO dbo.Course_Discipline (course_id, discipline_id) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, courseId);
            preparedStatement.setInt(2, disciplineId);
            preparedStatement.executeUpdate();
        }
    }

    public static void main(String[] args) throws SQLException {
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.getConnection();
        DisciplineDB disciplineDB = new DisciplineDB(connection);

        // Add a discipline
        // disciplineDB.addDiscipline("Physics", "Introduction to Physics", "MWF 09:00-10:00");

        // Update a discipline
        // disciplineDB.updateDiscipline(1, "Advanced Physics", "Advanced topics in Physics", "MWF 09:00-10:00");

        // Add a course-discipline association
        //disciplineDB.addCourseDiscipline(1, 1); // Example course ID and discipline ID

        // Delete a discipline
        // disciplineDB.deleteDiscipline(1);

        // List all disciplines
        List<Discipline> disciplines = disciplineDB.getDisciplines();
        for (Discipline discipline : disciplines) {
            System.out.println(discipline.toString());
        }

        db.closeConnection(connection); // Ensure to close the connection after use
    }
}
