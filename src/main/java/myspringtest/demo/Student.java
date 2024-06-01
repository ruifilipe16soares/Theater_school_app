package myspringtest.demo;

public class Student {
    private int id;
    private int id_user;
    private int entryYear;

    public Student(int id, int id_user, int entryYear) {
        this.id = id;
        this.id_user = id_user;
        this.entryYear = entryYear;
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

    public int getEntryYear() {
        return entryYear;
    }

    public void setEntryYear(int entryYear) {
        this.entryYear = entryYear;
    }

    @Override
    public String toString() {
return "Student [id=" + id + ", id_user=" + id_user + ", entryYear=" + entryYear + "]";
    }
}
