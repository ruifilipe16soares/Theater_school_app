package myspringtest.demo.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import myspringtest.demo.Student;

public class StudentDB {

    private Connection connection;
    private Statement statement;

    public StudentDB(Connection connection) throws SQLException {
        this.connection = connection;
        this.statement = connection.createStatement();
    }

    public synchronized List<Student> getStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        String query = "SELECT * FROM dbo.Student";
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            int id_user = resultSet.getInt("id_user");
            int entryYear = resultSet.getInt("entry_date");
            Student student = new Student(id, id_user, entryYear);
            students.add(student);
        }

        return students;
    }

    public synchronized void addStudent(int id_user, int entryYear) throws SQLException {
        String query = "INSERT INTO dbo.Student (id_user, entry_date) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id_user);
            preparedStatement.setInt(2, entryYear);
            preparedStatement.executeUpdate();
        }
    }

    public synchronized void updateStudent(int id, int id_user, int entryYear) throws SQLException {
        String query = "UPDATE dbo.Student SET id_user = ?, entry_date = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id_user);
            preparedStatement.setInt(2, entryYear);
            preparedStatement.setInt(3, id);
            preparedStatement.executeUpdate();
        }
    }

    public synchronized void deleteStudent(int id) throws SQLException {
        // Delete from Student_Course table
        String deleteStudentCourseQuery = "DELETE FROM dbo.Student_Course WHERE student_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteStudentCourseQuery)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }

        // Delete from Student table
        String deleteStudentQuery = "DELETE FROM dbo.Student WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteStudentQuery)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    public synchronized void addStudentCourse(int studentId, int courseId) throws SQLException {
        String query = "INSERT INTO dbo.Student_Course (student_id, course_id) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, studentId);
            preparedStatement.setInt(2, courseId);
            preparedStatement.executeUpdate();
        }
    }

    public static void main(String[] args) throws SQLException {
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.getConnection();
        StudentDB studentDB = new StudentDB(connection);

        // Add a student
        //studentDB.addStudent(1, 2023);

        // Update a student
        //studentDB.updateStudent(1, 1, 2024);

        // Add a student-course association
        //studentDB.addStudentCourse(1, 1); // Example student ID and course ID

        // Delete a student
        //studentDB.deleteStudent(1);

        // List all students
        List<Student> students = studentDB.getStudents();
        for (Student student : students) {
            System.out.println(student.toString());
        }

        db.closeConnection(connection); // Ensure to close the connection after use
    }
}
