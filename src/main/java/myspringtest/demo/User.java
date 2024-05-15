package myspringtest.demo;

public class User {

    private static int nextId = 1;

    private int id;
    private String name;
    private String email;
    private String password;
    private int age;
    private int userType;

    public User(String name, int age, int userType, String email, String password) {
        this.id = nextId++;
        this.name = name;
        this.age = age;
        this.userType = userType;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //tostring
    @Override
    public String toString() {
        return "User [age=" + age + ", email=" + email + ", id=" + id + ", name=" + name + ", password=" + password
                + ", userType=" + userType + "]";
    }
}