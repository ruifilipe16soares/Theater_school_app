package myspringtest.demo;

import java.util.ArrayList;

public class Student extends Person {
    private ArrayList<Discipline> disciplines;
    private int entryYear;

    public Student(String name, int age, int userType, String email, String password) {
        super(name, age, userType, email, password);
    }
}
