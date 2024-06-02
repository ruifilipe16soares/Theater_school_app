package myspringtest.demo;

public class User {

    private static int nextId = 1;

    private int id;
    private String name;
    private String email;
    private String password;
    private int age;
    private String userType;

    public User(String name, int age, String userType, String email, String password, int id) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.userType = userType;
        this.email = email;
        this.password = password;
    }

    public User(int id2, String name2, int age2, String userType2, String email2, String password2) {
        this.id = id2;
        this.name = name2;
        this.age = age2;
        this.userType = userType2;
        this.email = email2;
        this.password = password2;
    }

    public int getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getUserType() {
        return userType;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    //tostring
    @Override
    public String toString() {
        //ex: [Admin] - Name - Email: xxx - Age: x - ID: x
        return "[" + userType + "] Nome: " + name + " | Email: " + email + " | Idade: " + age + " | ID: " + id;
    }
}