package myspringtest.demo.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import myspringtest.demo.Professor;

public class ProfessorDB {

    private Connection connection;
    private Statement statement;

    public ProfessorDB(Connection connection) throws SQLException {
        this.connection = connection;
        this.statement = connection.createStatement();
    }

    public List<Professor> getProfessors() throws SQLException {
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

    public static void main(String[] args) throws SQLException {
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.getConnection();
        ProfessorDB professorDB = new ProfessorDB(connection);
        List<Professor> professors = professorDB.getProfessors();
        for (Professor professor : professors) {
            System.out.println(professor.toString());
        }
        db.closeConnection(connection); // Certifique-se de fechar a conexão após o uso
    }
}
