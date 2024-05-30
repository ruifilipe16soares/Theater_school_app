package myspringtest.demo;

import java.util.ArrayList;

public class Student extends User {
    private ArrayList<Discipline> disciplines;
    private int entryYear;

    public Student(String name, int age, String userType, String email, String password, int id) {
        super(name, age, userType, email, password, id);
    }
}
