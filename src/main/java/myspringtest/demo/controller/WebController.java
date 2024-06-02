package myspringtest.demo.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import myspringtest.demo.Course;
import myspringtest.demo.Discipline;
import myspringtest.demo.Professor;
import myspringtest.demo.School;
import myspringtest.demo.User;
import myspringtest.demo.model.CourseDB;
import myspringtest.demo.model.DatabaseConnection;
import myspringtest.demo.model.DisciplineDB;
import myspringtest.demo.model.ProfessorDB;
import myspringtest.demo.model.StudentDB;

@Controller
public class WebController {

    private final School school;

    public WebController() {
        this.school = new School();
        //this.school.adduser(new Person("admin", 0, 0, "admin@admin", "admin"));
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/registerDiscipline")
    public String registerDiscipline() {
        return "registerDiscipline";
    }

    @GetMapping("/registerCourse")
    public String registerCourse() {
        return "registerCourse";
    }

    @PostMapping("/registerCourse")
    public ModelAndView registerCourse(@RequestParam String name, @RequestParam String description, @RequestParam float price, @RequestParam int duration, @RequestParam int normalTime) throws SQLException {
        DatabaseConnection db = new DatabaseConnection();
        CourseDB courseDB = new CourseDB(db.getConnection());
        courseDB.addCourse(name, description, price, duration, normalTime);
        return new ModelAndView("redirect:/admin");
    }

    //postmapping discipline
    @PostMapping("/registerDiscipline")
    public ModelAndView registerDiscipline(@RequestParam String name, @RequestParam String description, @RequestParam String schedule) throws SQLException {
        DatabaseConnection db = new DatabaseConnection();
        DisciplineDB disciplineDB = new DisciplineDB(db.getConnection());
        disciplineDB.addDiscipline(name, description, schedule);
        return new ModelAndView("redirect:/admin");
    }

    @PostMapping("/registerAdmin")
    public ModelAndView register(@RequestParam String name, @RequestParam int age,
            @RequestParam String email, @RequestParam String password) throws SQLException {
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.getConnection();
        int total = db.getCountUsers();
        total++;
        db.addUser(1, name, age, "Admin", email, password);

        return new ModelAndView("redirect:/admin");
    }

    @PostMapping("/registerStudent")
    public ModelAndView registerUser(@RequestParam String name, @RequestParam int age,
            @RequestParam String email, @RequestParam String password, @RequestParam int entryYear) throws SQLException {
        DatabaseConnection db = new DatabaseConnection();
        StudentDB studentDB = new StudentDB(db.getConnection());
        Connection connection = db.getConnection();
        int total = db.getCountUsers();
        total++;
        db.addUser(1, name, age, "Aluno", email, password);
        studentDB.addStudent(total, entryYear);

        return new ModelAndView("redirect:/admin");
    }

    @PostMapping("/registerProfessor")
    public ModelAndView register3(@RequestParam String name, @RequestParam int age, @RequestParam String email, @RequestParam String password, @RequestParam float salary, @RequestParam int entry_date, @RequestParam String education) throws SQLException {
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.getConnection();
        ProfessorDB professorDB = new ProfessorDB(db.getConnection());
        int total = db.getCountUsers();
        total++;
        db.addUser(1, name, age, "Professor", email, password);
        professorDB.addProfessor(total, salary, entry_date, education);
        return new ModelAndView("redirect:/admin");
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public ModelAndView login(@RequestParam String email, @RequestParam String password) throws SQLException {
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.getConnection();
        String userType = db.checkType(email, password);
        if (userType != null) {
            if (userType.equals("admin")) {
                return new ModelAndView("redirect:/admin");
            } else if (userType.equals("Professor")) {
                return new ModelAndView("redirect:/student");
            } else if (userType.equals("Aluno")) {
                return new ModelAndView("redirect:/student");
            }
            // Adicione mais condições aqui para outros tipos de usuário
        }
        return new ModelAndView("login");
    }

    @GetMapping("/student")
    public String student() {
        return "student";
    }

    @GetMapping("/admin")
    public ModelAndView admin() throws SQLException {
        DatabaseConnection db = new DatabaseConnection();
        db.getConnection();
        CourseDB courseDB = new CourseDB(db.getConnection());
        List<Course> courses = courseDB.getCourses();
        DisciplineDB disciplineDB = new DisciplineDB(db.getConnection());
        List<Discipline> disciplines = disciplineDB.getDisciplines();

        List<User> users = db.getUsers();
        //if user.userType == "admin" guardar em lista e passar para modelAndView
        List<User> admins = new ArrayList<>();
        for (User user : users) {
            if (user.getUserType().equals("Admin")) {
                admins.add(user);
            }
        }

        //if user.userType == "Professor" guardar em lista e passar para modelAndView
        List<User> usersProfessors = new ArrayList<>();
        for (User user : users) {
            if (user.getUserType().equals("Professor")) {
                usersProfessors.add(user);
            }
        }

        List<User> usersAlunos = new ArrayList<>();
        for (User user : users) {
            if (user.getUserType().equals("Aluno")) {
                usersAlunos.add(user);
            }
        }

        ProfessorDB professorDB = new ProfessorDB(db.getConnection());
        List<Professor> professors = professorDB.getProfessors();

        ModelAndView modelAndView = new ModelAndView("admin");
        modelAndView.addObject("courses", courses);
        modelAndView.addObject("disciplines", disciplines);
        modelAndView.addObject("users", users);
        modelAndView.addObject("admins", admins);
        modelAndView.addObject("professors", professors);
        modelAndView.addObject("usersProfessors", usersProfessors);
        modelAndView.addObject("usersAlunos", usersAlunos);
        return modelAndView;
    }

    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam int id) throws SQLException {
        System.out.println("Received ID: " + id); // Log do ID recebido
        DatabaseConnection db = new DatabaseConnection();
        db.getConnection();
        db.deleteUser(id);
        return "redirect:/admin";
    }

    /* delete curso */
    @PostMapping("/deleteCourse")
    public String deleteCourse(@RequestParam int id) throws SQLException {
        System.out.println("Received ID: " + id); // Log do ID recebido
        DatabaseConnection db = new DatabaseConnection();
        CourseDB courseDB = new CourseDB(db.getConnection());
        courseDB.deleteCourse(id);
        return "redirect:/admin";
    }

    /* delete disciplina */
    @PostMapping("/deleteDiscipline")
    public String deleteDiscipline(@RequestParam int id) throws SQLException {
        System.out.println("Received ID: " + id); // Log do ID recebido
        DatabaseConnection db = new DatabaseConnection();
        DisciplineDB disciplineDB = new DisciplineDB(db.getConnection());
        disciplineDB.deleteDiscipline(id);
        return "redirect:/admin";
    }

    //String name, int age, String usertype, String email, String password
    @PostMapping("/editUser")
    public String postMethodName(@RequestParam int id, @RequestParam String name, @RequestParam int age, @RequestParam String userType, @RequestParam String email, @RequestParam String password) {
        System.out.println("Received ID: " + id + " Name: " + name + " Age: " + age + " UserType: " + userType + " Email: " + email + " Password: " + password); // Log do ID recebido
        DatabaseConnection db = new DatabaseConnection();
        db.getConnection();
        try {
            db.updateUser(id, name, age, userType, email, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "redirect:/admin";
    }

    @PostMapping("/editDiscipline")
    public String editDiscipline(@RequestParam int id, @RequestParam String name, @RequestParam String description, @RequestParam String schedule) {
        System.out.println("Received ID: " + id + " Name: " + name + " Description: " + description + " Schedule: " + schedule); // Log do ID recebido
        DatabaseConnection db = new DatabaseConnection();
        db.getConnection();
        try {
            DisciplineDB disciplineDB = new DisciplineDB(db.getConnection());
            disciplineDB.updateDiscipline(id, name, description, schedule);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "redirect:/admin";
    }

    @PostMapping("/editCourse")
    public String editCourse(@RequestParam int id, @RequestParam String name, @RequestParam String description, @RequestParam float price, @RequestParam int duration, @RequestParam int normalTime) {
        System.out.println("Received ID: " + id + " Name: " + name + " Description: " + description + " Price: " + price + " Duration: " + duration + " NormalTime: " + normalTime); // Log do ID recebido
        DatabaseConnection db = new DatabaseConnection();
        db.getConnection();
        try {
            CourseDB courseDB = new CourseDB(db.getConnection());
            courseDB.updateCourse(id, name, description, price, duration, normalTime);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "redirect:/admin";
    }

    //add aluno to course
    @PostMapping("/addAlunoToCourse")
    public String addAlunoToCourse(@RequestParam int courseId, @RequestParam int studentId) {
        System.out.println("Received Student ID: " + studentId + " Course ID: " + courseId); // Log do ID recebido
        DatabaseConnection db = new DatabaseConnection();
        db.getConnection();
        try {
            CourseDB courseDB = new CourseDB(db.getConnection());
            courseDB.addStudentCourse(studentId, courseId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "redirect:/admin";
    }

    //add professor to discipline
    @PostMapping("/addProfToDiscipline")
    public String addProfToDiscipline(@RequestParam int disciplineId, @RequestParam int professorId) {
        System.out.println("Received Professor ID: " + professorId + " Discipline ID: " + disciplineId); // Log do ID recebido
        DatabaseConnection db = new DatabaseConnection();
        db.getConnection();
        try {
            ProfessorDB professorDB = new ProfessorDB(db.getConnection());
            professorDB.addProfessorDiscipline(professorId, disciplineId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "redirect:/admin";
    }

}
