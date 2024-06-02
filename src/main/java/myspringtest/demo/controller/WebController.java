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
    public ModelAndView home() throws SQLException {
        DatabaseConnection db = new DatabaseConnection();
        db.getConnection();
        CourseDB courseDB = new CourseDB(db.getConnection());
        List<Course> courses = courseDB.getCourses();

        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("courses", courses);
        return modelAndView;
    }
    
    @GetMapping("/professors")
    public ModelAndView professors() throws SQLException {
        DatabaseConnection db = new DatabaseConnection();
        db.getConnection();
        ProfessorDB professorDB = new ProfessorDB(db.getConnection());
        List<User> professors = professorDB.getUserProfessors();

        ModelAndView modelAndView = new ModelAndView("professors");
        modelAndView.addObject("professors", professors);
        return modelAndView;
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
        db.addUser(name, age, "Admin", email, password);

        return new ModelAndView("redirect:/admin");
    }

    @PostMapping("/registerStudent")
    public ModelAndView registerUser(@RequestParam String name, @RequestParam int age,
            @RequestParam String email, @RequestParam String password, @RequestParam int entryYear) throws SQLException {
        DatabaseConnection db = new DatabaseConnection();
        StudentDB studentDB = new StudentDB(db.getConnection());
        Connection connection = db.getConnection();
            int numberid = db.addUser(name, age, "Student", email, password);
            System.out.println("ID: " + numberid);
            studentDB.addStudent(numberid, entryYear);

        return new ModelAndView("redirect:/admin");
    }

    @PostMapping("/registerProfessor")
    public ModelAndView register3(@RequestParam String name, @RequestParam int age, @RequestParam String email, @RequestParam String password, @RequestParam float salary, @RequestParam int entry_date, @RequestParam String education) throws SQLException {
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.getConnection();
        ProfessorDB professorDB = new ProfessorDB(db.getConnection());
        int numberid = db.addUser(name, age, "Professor", email, password);
        System.out.println("ID: " + numberid);
        professorDB.addProfessor(numberid, salary, entry_date, education); 
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
            if (userType.equals("Admin")) {
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
            if (user.getUserType().equals("Student")) {
                usersAlunos.add(user);
            }
        }

        ProfessorDB professorDB = new ProfessorDB(db.getConnection());
        List<Professor> professors = professorDB.getProfessors();

        //FUNCOES DE ESTATISTICA
        //string com numero total de alunos na escola + numero total de professores
        int totalAlunos = 0;
        for (User user : users) {
            if (user.getUserType().equals("Student")) {
                totalAlunos++;
            }
        }
        int totalProfessores = 0;
        for (User user : users) {
            if (user.getUserType().equals("Professor")) {
                totalProfessores++;
            }
        }
        String StotalAlunos = "Total de alunos: " + totalAlunos;
        String StotalProfessores = "Total de professores: " + totalProfessores;

        //curso com mais alunos
        CourseDB courseDB2 = new CourseDB(db.getConnection());
        StudentDB studentDB = new StudentDB(db.getConnection());
        //para cada curso getstudentsbycourse e guardar o id do curso com mais alunos
        int idCourse = 0;
        int maxStudents = 0;
        for (Course course : courses) {
            int students = studentDB.getStudentsByCourse(course.getId()).size();
            if (students > maxStudents) {
                maxStudents = students;
                idCourse = course.getId();
            }
        }
        String nomeCurso = "";
        Course course = courseDB2.getCourse(idCourse);
        if (maxStudents == 0) {
            nomeCurso = "Nenhum aluno inscrito";
        } else {
            nomeCurso = "Curso com mais alunos: " + course.getName() + " com " + maxStudents + " alunos";
        }
        

        //curso mais caro
        float maxPrice = 0;
        int idCourse2 = 0;
        for (Course course2 : courses) {
            if (course2.getPrice() > maxPrice) {
                maxPrice = course2.getPrice();
                idCourse2 = course2.getId();
            }
        }
        Course course2 = courseDB2.getCourse(idCourse2);
        String nomeCurso2 = "Curso mais caro: " + course2.getName() + " com preço de " + course2.getPrice() + " euros";

        //--------------------------

        ModelAndView modelAndView = new ModelAndView("admin");
        modelAndView.addObject("courses", courses);
        modelAndView.addObject("disciplines", disciplines);
        modelAndView.addObject("users", users);
        modelAndView.addObject("admins", admins);
        modelAndView.addObject("professors", professors);
        modelAndView.addObject("usersProfessors", usersProfessors);
        modelAndView.addObject("usersAlunos", usersAlunos);

        //enviar estatistica
        modelAndView.addObject("totalAlunos", StotalAlunos);
        modelAndView.addObject("totalProfessores", StotalProfessores);
        modelAndView.addObject("nomeCurso", nomeCurso);
        modelAndView.addObject("nomeCurso2", nomeCurso2);

        return modelAndView;
    }

    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam int id) throws SQLException {
        System.out.println("Received ID: " + id); // Log do ID recebido
        DatabaseConnection db = new DatabaseConnection();
        db.getConnection();
        StudentDB studentDB = new StudentDB(db.getConnection());
        studentDB.deleteStudent_ProfessorByIdUser(id);
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
    public String editCourse(@RequestParam int id, @RequestParam String name, @RequestParam String description,
            @RequestParam float price, @RequestParam int duration, @RequestParam int normalTime) {
        System.out.println("Received ID: " + id + " Name: " + name + " Description: " + description + " Price: " + price
                + " Duration: " + duration + " NormalTime: " + normalTime); // Log do ID recebido
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
    public String addAlunoToCourse(@RequestParam int studentId, @RequestParam int courseId) {
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

    //add discipline to course
    @PostMapping("/addDisciplineToCourse")
    public String addDisciplineToCourse(@RequestParam int courseId, @RequestParam int disciplineId) {
        System.out.println("Received Discipline ID: " + disciplineId + " Course ID: " + courseId); // Log do ID recebido
        DatabaseConnection db = new DatabaseConnection();
        db.getConnection();
        try {
            DisciplineDB disciplineDB = new DisciplineDB(db.getConnection());
            disciplineDB.addCourseDiscipline(courseId, disciplineId);
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

@PostMapping("/courseDetails")
public ModelAndView courseDetails(@RequestParam int courseId) throws SQLException {
    System.out.println(" Course ID: " + courseId); // Log dos IDs recebidos

    DatabaseConnection db = new DatabaseConnection();
    db.getConnection();

    CourseDB courseDB = new CourseDB(db.getConnection());
    StudentDB studentDB = new StudentDB(db.getConnection());

    Course course = courseDB.getCourse(courseId);
    List<Discipline> disciplines = courseDB.getDisciplinesByCourse(courseId); // Presumindo que você tenha um método para obter disciplinas
    List<User> user = studentDB.getUserStudentsByCourse(courseId);

    ModelAndView mav = new ModelAndView("courseDetails");
    mav.addObject("course", course);
    mav.addObject("disciplines", disciplines);
    mav.addObject("user", user);

    return mav;
}

}
