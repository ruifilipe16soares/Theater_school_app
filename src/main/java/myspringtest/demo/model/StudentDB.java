package myspringtest.demo.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import myspringtest.demo.Student;
import myspringtest.demo.User;

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

    public synchronized List<Student> getStudentsByCourse(int courseId) throws SQLException {
        List<Student> students = new ArrayList<>();
        String query = "SELECT s.* FROM dbo.Student s " +
                "JOIN dbo.Student_Course sc ON s.id = sc.student_id " +
                "WHERE sc.course_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, courseId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int id_user = resultSet.getInt("id_user");
                int entryYear = resultSet.getInt("entry_date");
                Student student = new Student(id, id_user, entryYear);
                students.add(student);
            }
        }

        return students;
    }
    public synchronized List<User> getUserStudentsByCourse(int courseId) throws SQLException {
    List<User> users = new ArrayList<>();
    String query = "SELECT u.* FROM dbo.Users u " +
                   "JOIN dbo.Student s ON u.id = s.id_user " +
                   "JOIN dbo.Student_Course sc ON s.id = sc.student_id " +
                   "WHERE sc.course_id = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        preparedStatement.setInt(1, courseId);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            int age = resultSet.getInt("age");
            String userType = resultSet.getString("usertype");
            String email = resultSet.getString("email");
            String password = resultSet.getString("password");

            User user = new User(id, name, age, userType, email, password);
            users.add(user);
        }
    }

    return users;
}
    public synchronized void deleteStudent_ProfessorByIdUser(int id_user) throws SQLException {
    // Obter o ID do professor pelo id_user
    String getProfessorIdQuery = "SELECT id FROM dbo.Professor WHERE id_user = ?";
    int professorId = -1;
    try (PreparedStatement preparedStatement = connection.prepareStatement(getProfessorIdQuery)) {
        preparedStatement.setInt(1, id_user);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            professorId = resultSet.getInt("id");
        }
    }

    // Obter o ID do estudante pelo id_user
    String getStudentIdQuery = "SELECT id FROM dbo.Student WHERE id_user = ?";
    int studentId = -1;
    try (PreparedStatement preparedStatement = connection.prepareStatement(getStudentIdQuery)) {
        preparedStatement.setInt(1, id_user);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            studentId = resultSet.getInt("id");
        }
    }

    // Deletar da tabela Professor_Discipline se professorId foi encontrado
    if (professorId != -1) {
        String deleteProfessorDisciplineQuery = "DELETE FROM dbo.Professor_Discipline WHERE professor_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteProfessorDisciplineQuery)) {
            preparedStatement.setInt(1, professorId);
            preparedStatement.executeUpdate();
        }
    }

    // Deletar da tabela Student_Course se studentId foi encontrado
    if (studentId != -1) {
        String deleteStudentCourseQuery = "DELETE FROM dbo.Student_Course WHERE student_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteStudentCourseQuery)) {
            preparedStatement.setInt(1, studentId);
            preparedStatement.executeUpdate();
        }
    }


    // Deletar da tabela Student se studentId foi encontrado
    if (studentId != -1) {
        String deleteStudentQuery = "DELETE FROM dbo.Student WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteStudentQuery)) {
            preparedStatement.setInt(1, studentId);
            preparedStatement.executeUpdate();
        }
    }

    // Deletar da tabela Professor se professorId foi encontrado
    if (professorId != -1) {
        String deleteProfessorQuery = "DELETE FROM dbo.Professor WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteProfessorQuery)) {
            preparedStatement.setInt(1, professorId);
            preparedStatement.executeUpdate();
        }
    }

    // Finalmente, deletar da tabela Users
    String deleteUserQuery = "DELETE FROM dbo.Users WHERE id = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(deleteUserQuery)) {
        preparedStatement.setInt(1, id_user);
        int affectedRows = preparedStatement.executeUpdate();
        if (affectedRows == 0) {
            System.out.println("No user found with id: " + id_user);
        } else {
            System.out.println("User with id: " + id_user + " was deleted successfully.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("Error occurred while deleting user with id: " + id_user);
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


System.out.println("studentsByCourse id 1");
        // List students by course
        int courseId = 1; // Example course ID
        List<Student> studentsByCourse = studentDB.getStudentsByCourse(courseId);
        for (Student student : studentsByCourse) {
            System.out.println(student.toString());
        }

        db.closeConnection(connection); // Ensure to close the connection after use
    }
}
