package main.java.view;

import main.java.model.Cashier;
import main.java.model.Item;
import main.java.model.TransactionModel;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class ReceiptView extends JFrame {
    public ReceiptView(Cashier cashier, Map<Item, Integer> orderedItems, TransactionModel summary, double paymentAmount) {
        setTitle("Receipt");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JTextArea receiptArea = new JTextArea();
        receiptArea.setEditable(false);
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        String receiptText = generateReceiptText(cashier, orderedItems, summary, paymentAmount);
        receiptArea.setText(receiptText);

        add(new JScrollPane(receiptArea));
    }

    private String generateReceiptText(Cashier cashier, Map<Item, Integer> orderedItems, TransactionModel summary, double paymentAmount) {
        StringBuilder sb = new StringBuilder();

        sb.append("****************************************\n");
        sb.append("             Mcdonalds\n");
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

        sb.append("****************************************\n");
        sb.append("       Thank you for your visit!      \n");
        sb.append("****************************************\n");

        return sb.toString();
    }
}