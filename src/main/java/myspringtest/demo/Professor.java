package myspringtest.demo;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Professor extends User {

    private double salary;
    private int entryYear;
    private String education;
    private ArrayList<Discipline> subjects;

    public Professor(String name, int age, String email, String password, int id) {
        super(name, age, "professor", email, password, id);
    }
}
