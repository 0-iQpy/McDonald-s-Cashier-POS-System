package main.java.db;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionTest {
    public static void main(String[] args) {
        Connection conn = null;
        try {
            // Attempt to get a connection
            conn = DatabaseConnector.getConnection();
            
            // If the line above runs without exception, it worked!
            System.out.println("------------------------------------------");
            System.out.println("✅ SUCCESS: Database connection established!");
            System.out.println("------------------------------------------");
            
            // Test a basic query (Reading the fixed menu)
            System.out.println("Testing Item Count...");
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery("SELECT COUNT(*) AS item_count FROM Items;");
            
            if (rs.next()) {
                int count = rs.getInt("item_count");
                System.out.println("Found " + count + " items in the database.");
            }
            
        } catch (SQLException e) {
            System.err.println("------------------------------------------");
            System.err.println("❌ ERROR: Failed to connect to the database!");
            System.err.println("------------------------------------------");
            System.err.println("Possible causes: Incorrect URL/User/Password in DatabaseConnector.java, or MySQL server is not running.");
            e.printStackTrace();
            
        } finally {
            // Always close the connection
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}