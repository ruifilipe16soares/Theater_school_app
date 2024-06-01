package myspringtest.demo;

public class Course {
    private int id;
    private String name;
    private String description;
    private float price;
    private int duration;
    private int normalTime;

    public Course(int id, String name, String description, float price, int duration, int normalTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.normalTime = normalTime;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public float getPrice() {
        return price;
    }

    public int getDuration() {
        return duration;
    }

    public int getNormalTime() {
        return normalTime;
    }

    @Override
    public String toString() {
        return "[" + name + "] Descrição: " + description + " | Preço: " + price
                + " | Duração: " + duration + " | normalTime: " + normalTime + " | ID: " + id;
    }
}
