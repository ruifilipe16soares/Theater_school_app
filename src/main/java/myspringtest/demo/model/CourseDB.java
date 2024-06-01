package myspringtest.demo.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import myspringtest.demo.Course;

public class CourseDB {

    private Connection connection;
    private Statement statement;

    public CourseDB(Connection connection) throws SQLException {
        this.connection = connection;
        this.statement = connection.createStatement();
    }

    public List<Course> getCourses() throws SQLException {
        List<Course> courses = new ArrayList<>();
        String query = "SELECT * FROM dbo.Courses";
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String description = resultSet.getString("description");
            float price = resultSet.getFloat("price");
            int duration = resultSet.getInt("duration");
            int normalTime = resultSet.getInt("normal_time");
            Course course = new Course(id, name, description, price, duration, normalTime);
            courses.add(course);
        }

        return courses;
    }

    public void addCourse(String name, String description, float price, int duration, int normalTime) throws SQLException {
        String query = "INSERT INTO dbo.Courses (name, description, price, duration, normal_time) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setFloat(3, price);
            preparedStatement.setInt(4, duration);
            preparedStatement.setInt(5, normalTime);
            preparedStatement.executeUpdate();
        }
    }

    public void updateCourse(int id, String name, String description, float price, int duration, int normalTime) throws SQLException {
        String query = "UPDATE dbo.Courses SET name = ?, description = ?, price = ?, duration = ?, normal_time = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setFloat(3, price);
            preparedStatement.setInt(4, duration);
            preparedStatement.setInt(5, normalTime);
            preparedStatement.setInt(6, id);
            preparedStatement.executeUpdate();
        }
    }

    public void deleteCourse(int id) throws SQLException {

    String deleteStudentCourseQuery = "DELETE FROM dbo.Student_Course WHERE course_id = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(deleteStudentCourseQuery)) {
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }

    // Delete from Course_Discipline table
    String deleteCourseDisciplineQuery = "DELETE FROM dbo.Course_Discipline WHERE course_id = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(deleteCourseDisciplineQuery)) {
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }

    // Delete from Courses table
    String deleteCourseQuery = "DELETE FROM dbo.Courses WHERE id = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(deleteCourseQuery)) {
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }
        
    }

    public static void main(String[] args) throws SQLException {
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.getConnection();
        CourseDB courseDB = new CourseDB(connection);

        // Adicionar um curso
        //courseDB.addCourse("Jazz Dance", "Learn the fundamentals of Jazz dance", 120.00f, 30, 60);

        // Atualizar um curso
        courseDB.updateCourse(5, "Ballet", "Ballet", 180.00f, 45, 90);

        // Remover um curso
        courseDB.deleteCourse(2);

        // Listar todos os cursos
        List<Course> courses = courseDB.getCourses();
        for (Course course : courses) {
            System.out.println(course.toString());
        }

        db.closeConnection(connection); // Certifique-se de fechar a conexão após o uso
    }
}
