package myspringtest.demo;

import java.util.ArrayList;
import java.util.List;

public class School {
    private List<User> users;

    public School() {
        this.users = new ArrayList<>();
    }

    public void adduser(User user) {
        this.users.add(user);
    }

    public List<User> getUsers() {
        return users;
    }

    //toString users
    public String toString() {
        String result = "";
        for (User user : users) {
            result += user.toString() + "\n";
        }
        return result;
    }
}