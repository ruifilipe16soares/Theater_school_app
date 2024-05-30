package myspringtest.demo;

import java.util.ArrayList;
import java.util.List;

public class School {
    private List<Person> users;

    public School() {
        this.users = new ArrayList<>();
    }

    public void adduser(Person user) {
        this.users.add(user);
    }

    public List<Person> getUsers() {
        return users;
    }

    //toString users
    public String toString() {
        String result = "";
        for (Person user : users) {
            result += user.toString() + "\n";
        }
        return result;
    }
}