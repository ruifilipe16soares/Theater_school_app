package myspringtest.demo.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import myspringtest.demo.School;
import myspringtest.demo.User;
import myspringtest.demo.model.DatabaseConnection;

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


    @PostMapping("/register")
    public ModelAndView register(@RequestParam String name, @RequestParam int age, String userType, @RequestParam String email, @RequestParam String password) throws SQLException {
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.getConnection();
        int total = db.getCountUsers();
        total++;
        db.addUser(total, name, age, userType, email, password);
        return new ModelAndView("redirect:/");
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
        if(userType != null) {
            if(userType.equals("admin")) {
                return new ModelAndView("redirect:/admin");
            } else if(userType.equals("user")) {
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
        List<User> users = db.getUsers();
        ModelAndView modelAndView = new ModelAndView("admin");
        modelAndView.addObject("users", users);
        return modelAndView;
    }

    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam int id) throws SQLException {
        System.out.println("Received ID: " + id); // Log do ID recebido
        DatabaseConnection db = new DatabaseConnection();
        Connection connection = db.getConnection();
        db.deleteUser(id);
        return "redirect:/admin";
    }
    

}