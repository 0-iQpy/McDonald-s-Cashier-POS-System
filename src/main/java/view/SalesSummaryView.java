package main.java.view;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class SalesSummaryView extends JFrame {

    public SalesSummaryView(Map<String, Double> summaryMetrics) {
        setTitle("Sales Summary");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JTextArea summaryArea = new JTextArea();
        summaryArea.setEditable(false);
        summaryArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        String summaryText = generateSummaryText(summaryMetrics);
        summaryArea.setText(summaryText);

        add(new JScrollPane(summaryArea));
    }

    private String generateSummaryText(Map<String, Double> summaryMetrics) {
        StringBuilder sb = new StringBuilder();

        sb.append("****************************************\n");
        sb.append("             SALES SUMMARY\n");
        sb.append("****************************************\n\n");

        sb.append(String.format("Total Orders: %.0f\n", summaryMetrics.get("TotalOrders")));
        sb.append(String.format("Total Earnings: ₱%.2f\n", summaryMetrics.get("TotalEarnings")));
        sb.append(String.format("Total Items Purchased: %.0f\n\n", summaryMetrics.get("TotalItemsPurchased")));

        sb.append("----------------------------------------\n");
        sb.append("           ITEM BREAKDOWN\n");
        sb.append("----------------------------------------\n");

        for (Map.Entry<String, Double> entry : summaryMetrics.entrySet()) {
            if (entry.getKey().endsWith("Qty")) {
                sb.append(String.format("%-20s %.0f\n", entry.getKey(), entry.getValue()));
            }
        }

        sb.append("\n****************************************\n");

        return sb.toString();
    }
}