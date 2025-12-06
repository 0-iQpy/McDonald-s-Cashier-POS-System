package main.java.view;

import main.java.model.Cashier;
import main.java.model.Item;
import main.java.model.TransactionModel;
import main.java.db.IDataAccessObject;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class ReceiptView extends JFrame {
    private JTextArea receiptArea;
    private JButton completeOrderButton;
    private JButton cancelButton;
    private Cashier cashier;
    private Map<Item, Integer> orderedItems;
    private TransactionModel summary;
    private double paymentAmount;
    private IDataAccessObject dao;
    private CashierView parentView;
    private boolean orderCompleted = false;

    public ReceiptView(Cashier cashier, Map<Item, Integer> orderedItems, TransactionModel summary, double paymentAmount, IDataAccessObject dao) {
        this(cashier, orderedItems, summary, paymentAmount, dao, null);
    }

    public ReceiptView(Cashier cashier, Map<Item, Integer> orderedItems, TransactionModel summary, double paymentAmount, IDataAccessObject dao, CashierView parentView) {
        this.cashier = cashier;
        this.orderedItems = orderedItems;
        this.summary = summary;
        this.paymentAmount = paymentAmount;
        this.dao = dao;
        this.parentView = parentView;

        setTitle("Receipt Preview");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setupLayout();
    }

    private void setupLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Receipt display area
        receiptArea = new JTextArea();
        receiptArea.setEditable(false);
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        String receiptText = generateReceiptText(cashier, orderedItems, summary, paymentAmount);
        receiptArea.setText(receiptText);

        JScrollPane scrollPane = new JScrollPane(receiptArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        completeOrderButton = new JButton("✓ Complete Order");
        completeOrderButton.setFont(new Font("Arial", Font.BOLD, 12));
        completeOrderButton.setBackground(new Color(0, 150, 0));
        completeOrderButton.setForeground(Color.WHITE);
        completeOrderButton.setOpaque(true);
        completeOrderButton.setBorderPainted(false);
        completeOrderButton.addActionListener(e -> handleCompleteOrder());

        cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 12));
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(completeOrderButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void handleCompleteOrder() {
        try {
            // Save the transaction to the database
            dao.saveTransaction(cashier, orderedItems, summary);
            orderCompleted = true;
            
            // Reset parent view if it exists
            if (parentView != null) {
                parentView.resetOrder();
            }
            
            JOptionPane.showMessageDialog(this, "Order completed and saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error completing order: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String generateReceiptText(Cashier cashier, Map<Item, Integer> orderedItems, TransactionModel summary, double paymentAmount) {
        StringBuilder sb = new StringBuilder();

        sb.append("****************************************\n");
        sb.append("             McDonald's\n");
        sb.append("               Calamba\n");
        sb.append("****************************************\n\n");

        LocalDateTime now = LocalDateTime.now();
        sb.append("Date: ").append(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).append("\n");
        sb.append("Time: ").append(now.format(DateTimeFormatter.ofPattern("HH:mm:ss"))).append("\n");
        sb.append("Cashier: ").append(cashier.getName()).append("\n\n");

        sb.append("----------------------------------------\n");
        sb.append(String.format("%-4s %-20s %s\n", "Qty", "Item", "Price"));
        sb.append("----------------------------------------\n");

        for (Map.Entry<Item, Integer> entry : orderedItems.entrySet()) {
            Item item = entry.getKey();
            int quantity = entry.getValue();
            sb.append(String.format("%-4d %-20s ₱%.2f\n", quantity, item.getDisplayName(), item.getPriceWithVat()));
        }

        sb.append("----------------------------------------\n");
        sb.append(String.format("Subtotal:              ₱%.2f\n", summary.getSubtotalBase()));
        sb.append(String.format("VAT (12%%):             ₱%.2f\n", summary.getVatAmount()));
        sb.append(String.format("Discount:              ₱%.2f\n", summary.getDiscountAmount()));
        sb.append(String.format("Total:                 ₱%.2f\n\n", summary.getGrandTotal()));

        sb.append(String.format("Payment:               ₱%.2f\n", paymentAmount));
        sb.append(String.format("Change:                ₱%.2f\n\n", paymentAmount - summary.getGrandTotal()));

        sb.append("----------------------------------------\n");
        sb.append("    Click 'Complete Order' to confirm\n");
        sb.append("   and save this transaction to database\n");
        sb.append("       Thank you for your visit!      \n");
        sb.append("----------------------------------------\n");

        return sb.toString();
    }

    public boolean isOrderCompleted() {
        return orderCompleted;
    }
}