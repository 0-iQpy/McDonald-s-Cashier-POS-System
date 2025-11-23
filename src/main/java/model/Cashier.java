package main.java.model;

public class Cashier {
    private final String name;
    private final int id;

    public Cashier(int id, String name) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public int getId() {
        return id;
    }
}
