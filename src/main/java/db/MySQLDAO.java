// src/main/java/db/MySQLDAO.java

package main.java.db;

import main.java.model.Item;
import main.java.model.Cashier;
import main.java.model.TransactionModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySQLDAO implements IDataAccessObject {

    // --- Database Configuration (Update These) ---
    private static final String URL = "jdbc:mysql://localhost:3306/pos_db";
    private static final String USER = "root"; // <-- CHANGE THIS
    private static final String PASSWORD = "YOUR_PASSWORD_HERE"; // <-- CHANGE THIS

    /**
     * Helper method to establish a a connection.
     */
    private Connection getConnection() throws SQLException {
        // Uses core Java JDBC library
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // ----------------------------------------------------
    // 1. LOGIN
    // ----------------------------------------------------
    public Cashier authenticateCashier(String name, String password) throws SQLException {
        // Query to check if the cashier exists for login
        String sql = "SELECT cashier_id, name FROM Cashiers WHERE name = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Encapsulation: Return a populated Cashier model
                    return new Cashier(rs.getInt("cashier_id"), rs.getString("name"));
                }
            }
        }
        return null;
    }

    // ----------------------------------------------------
    // 2. ITEM LOADING
    // ----------------------------------------------------
    @Override
    public List<Item> loadMenuItems() throws SQLException {
        // Loads menu items and their fixed prices
        List<Item> items = new ArrayList<>();
        String sql = "SELECT item_id, name, display_name, price_with_vat FROM Items ORDER BY item_id";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Using getDouble() as requested for simpler data type
                items.add(new Item(
                    rs.getInt("item_id"),
                    rs.getString("name"),
                    rs.getDouble("price_with_vat"),
                    rs.getString("display_name")
                ));
            }
        }
        return items;
    }

    // ----------------------------------------------------
    // 3. TRANSACTION SAVING
    // ----------------------------------------------------
    @Override
    public int saveTransaction(Cashier cashier, Map<Item, Integer> orderItems, TransactionModel summary) throws SQLException {
        // This method must use a transaction to ensure both summary and details are saved or neither are.
        Connection conn = null;
        int transactionId = -1;

        // SQL Statements
        String insertTxnSql = "INSERT INTO Transactions (cashier_id, subtotal_base, vat_amount, discount_amount, grand_total) VALUES (?, ?, ?, ?, ?)";
        String insertDetailSql = "INSERT INTO Transaction_Details (transaction_id, item_id, quantity, price_at_purchase) VALUES (?, ?, ?, ?)";

        try {
            conn = getConnection();
            conn.setAutoCommit(false); // Start Transaction

            // STEP 1: Insert Transaction Summary
            try (PreparedStatement pstmtTxn = conn.prepareStatement(insertTxnSql, Statement.RETURN_GENERATED_KEYS)) {
                pstmtTxn.setInt(1, cashier.getId());
                pstmtTxn.setDouble(2, summary.getSubtotalBase());
                pstmtTxn.setDouble(3, summary.getVatAmount());
                pstmtTxn.setDouble(4, summary.getDiscountAmount());
                pstmtTxn.setDouble(5, summary.getGrandTotal());

                pstmtTxn.executeUpdate();

                // Retrieve the auto-generated transaction_id
                try (ResultSet rs = pstmtTxn.getGeneratedKeys()) {
                    if (rs.next()) {
                        transactionId = rs.getInt(1);
                    } else {
                        throw new SQLException("Transaction insert failed, no ID obtained.");
                    }
                }
            }

            // STEP 2: Insert Transaction Details (Batch Insertion)
            try (PreparedStatement pstmtDetail = conn.prepareStatement(insertDetailSql)) {

                for (Map.Entry<Item, Integer> entry : orderItems.entrySet()) {
                    Item item = entry.getKey();
                    int quantity = entry.getValue();

                    pstmtDetail.setInt(1, transactionId);
                    pstmtDetail.setInt(2, item.getId());
                    pstmtDetail.setInt(3, quantity);
                    pstmtDetail.setDouble(4, item.getPriceWithVat());
                    pstmtDetail.addBatch();
                }
                pstmtDetail.executeBatch();
            }

            conn.commit(); // Commit Transaction
            return transactionId;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback on error
                } catch (SQLException ex) {
                    throw new SQLException("Transaction rollback failed: " + ex.getMessage());
                }
            }
            throw new SQLException("Transaction saving failed: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) { /* Ignore */ }
            }
        }
    }

    // ----------------------------------------------------
    // 4. SALES SUMMARY
    // ----------------------------------------------------
    @Override
    public Map<String, Double> getSalesSummary(int cashierId) throws SQLException {
        // Queries database to calculate total orders, earnings, and items sold
        Map<String, Double> summaryMetrics = new HashMap<>();

        try (Connection conn = getConnection()) {

            // 1. Get high-level summary (Total Orders, Total Earnings)
            String summarySql = "SELECT COUNT(transaction_id) AS TotalOrders, SUM(grand_total) AS TotalEarnings " +
                                "FROM Transactions WHERE cashier_id = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(summarySql)) {
                pstmt.setInt(1, cashierId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        summaryMetrics.put("TotalOrders", rs.getDouble("TotalOrders"));
                        summaryMetrics.put("TotalEarnings", rs.getDouble("TotalEarnings"));
                    }
                }
            }

            // 2. Get total items purchased and breakdown
            String breakdownSql = "SELECT SUM(td.quantity) AS TotalItemsPurchased FROM Transaction_Details td " +
                                  "JOIN Transactions t ON td.transaction_id = t.transaction_id " +
                                  "WHERE t.cashier_id = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(breakdownSql)) {
                pstmt.setInt(1, cashierId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        // Store total items purchased
                        summaryMetrics.put("TotalItemsPurchased", rs.getDouble("TotalItemsPurchased"));
                    }
                }
            }

            // 3. Get detailed item breakdown (Optional but highly recommended)
            String itemBreakdownSql = "SELECT i.name, SUM(td.quantity) AS total_quantity " +
                                      "FROM Transaction_Details td " +
                                      "JOIN Transactions t ON td.transaction_id = t.transaction_id " +
                                      "JOIN Items i ON td.item_id = i.item_id " +
                                      "WHERE t.cashier_id = ? GROUP BY i.item_id, i.name ORDER BY i.item_id";

            try (PreparedStatement pstmt = conn.prepareStatement(itemBreakdownSql)) {
                pstmt.setInt(1, cashierId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    int counter = 1;
                    while (rs.next()) {
                        // Store breakdown for display, e.g., "Item 1 Qty": 5.0
                        summaryMetrics.put(rs.getString("name") + " Qty", rs.getDouble("total_quantity"));
                        counter++;
                    }
                }
            }

        }
        return summaryMetrics;
    }
}