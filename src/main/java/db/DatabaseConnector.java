package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private static final String URL = "jdbc:mysql://localhost:3306/pos_db";

    private static final String USER = "root";
    private static final String PASSWORD = "Macman021!";

    /**
     * Establishes and returns a connection to the MySQL database.
     * @return A valid Connection object.
     * @throws SQLException if the connection fails.
     */

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
