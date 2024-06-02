package myspringtest.demo;

import java.util.ArrayList;

public class Professor {
    private int id;
    private int id_user;
    private double salary;
    private int entryYear;
    private String education;
    private ArrayList<Discipline> subjects;

    public Professor(int id, int id_user, double salary, int entryYear, String education) {
        this.id = id;
        this.id_user = id_user;
        this.salary = salary;
        this.entryYear = entryYear;
        this.education = education;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public int getEntryYear() {
        return entryYear;
    }

    public void setEntryYear(int date) {
        this.entryYear = date;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String toString() {
        return "Professor [id=" + id + ", id_user=" + id_user + ", salary=" + salary + ", entryYear=" + entryYear
                + ", education=" + education + "]";
    }
}
