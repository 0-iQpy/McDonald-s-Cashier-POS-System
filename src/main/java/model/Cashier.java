package main.java.model;

public class Cashier {
    private final String name;
    private final int id;
    private String password; // This will be null unless explicitly set

    public Cashier(int id, String name) {
        this.name = name;
        this.id = id;
    }

    // Overloaded constructor for authentication purposes
    public Cashier(int id, String name, String password) {
        this.name = name;
        this.id = id;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }
}
