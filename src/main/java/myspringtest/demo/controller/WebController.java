package myspringtest.demo.controller;

import java.sql.SQLException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import myspringtest.demo.Person;
import myspringtest.demo.School;
import myspringtest.demo.model.DatabaseConnection;

@Controller
public class WebController {

    private final School school;

    public WebController() {
        this.school = new School();
        this.school.adduser(new Person("admin", 0, 0, "admin@admin", "admin"));
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
        int total = db.getUsers();
        total++;
        db.addUser(total, name, age, "user", email, password);
        return new ModelAndView("redirect:/register");
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public ModelAndView login(@RequestParam String email, @RequestParam String password) {
        for (Person user : school.getUsers()) {
            System.out.println(school.toString());
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                if (user.getUserType() == 0) {
                    return new ModelAndView("redirect:/admin");
                } else if (user.getUserType() == 1) {
                    return new ModelAndView("redirect:/student");
                }
            }
        }
        return new ModelAndView("login");
    }

    @GetMapping("/student")
    public String student() {
        return "student";
    }

    @GetMapping("/admin")
    public ModelAndView admin() {
        ModelAndView modelAndView = new ModelAndView("admin");
        modelAndView.addObject("users", school.getUsers());
        return modelAndView;
    }

    

}