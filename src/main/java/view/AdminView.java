package main.java.view;

import main.java.db.IDataAccessObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class AdminView extends JFrame {
    private IDataAccessObject dao;
    private JTable transactionTable;
    private DefaultTableModel tableModel;
    private JButton deleteButton;
    private JButton refreshButton;

    public AdminView(IDataAccessObject dao) {
        this.dao = dao;
        
        setTitle("Admin - Transaction Management");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setupLayout();
        loadTransactions();
    }

    private void setupLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header
        JLabel titleLabel = new JLabel("Transaction Records");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Table
        String[] columnNames = {"Transaction ID", "Cashier ID", "Date", "Subtotal", "VAT", "Discount", "Total", "Action"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Only action column is editable
            }
        };

        transactionTable = new JTable(tableModel);
        transactionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        transactionTable.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Control Panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        deleteButton = new JButton("Delete Selected");
        refreshButton = new JButton("Refresh");

        deleteButton.addActionListener(e -> deleteTransaction());
        refreshButton.addActionListener(e -> loadTransactions());

        controlPanel.add(refreshButton);
        controlPanel.add(deleteButton);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void loadTransactions() {
        tableModel.setRowCount(0); // Clear existing rows
        
        try {
            List<Map<String, Object>> transactions = dao.getAllTransactions();
            
            for (Map<String, Object> transaction : transactions) {
                Object[] row = {
                    transaction.get("transaction_id"),
                    transaction.get("cashier_id"),
                    transaction.get("transaction_date"),
                    transaction.get("subtotal_base"),
                    transaction.get("vat_amount"),
                    transaction.get("discount_amount"),
                    transaction.get("grand_total"),
                    "Delete"
                };
                tableModel.addRow(row);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading transactions: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteTransaction() {
        int selectedRow = transactionTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a transaction to delete.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int transactionId = (int) tableModel.getValueAt(selectedRow, 0);
        
        int response = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this transaction? (ID: " + transactionId + ")",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (response == JOptionPane.YES_OPTION) {
            try {
                dao.deleteTransaction(transactionId);
                JOptionPane.showMessageDialog(this, "Transaction deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadTransactions(); // Refresh the table
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error deleting transaction: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
