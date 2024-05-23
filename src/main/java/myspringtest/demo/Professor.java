package myspringtest.demo;

import java.lang.reflect.Array;
import java.util.ArrayList;

import myspringtest.Discipline;

public class Professor extends Person {

    private double salary;
    private int entryYear;
    private String education;
    private ArrayList<Discipline> subjects;

    public Professor(String name, int age, String email, String password) {
        super(name, age, 1, email, password);
    }
}
