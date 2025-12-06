package main.java.controller;

import main.java.db.IDataAccessObject;
import main.java.model.Cashier;
import main.java.model.Item;
import main.java.model.TransactionModel;
import main.java.view.CashierView;
import main.java.view.ReceiptView;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderController {
    private final CashierView view;
    private final IDataAccessObject dao;
    private static final double VAT_RATE = 0.12;
    private static final double DISCOUNT_RATE = 0.15;

    public OrderController(CashierView view, IDataAccessObject dao) {
        this.view = view;
        this.dao = dao;
        attachListeners();
    }

    private void attachListeners() {
        view.getPreviewReceiptButton().addActionListener(e -> previewOrder());
        view.getCompleteOrderButton().addActionListener(e -> completeOrder());
    }

    private void previewOrder() {
        // 1. Get order details from the view
        Map<Item, Integer> itemQuantities = view.getItemQuantities();
        boolean isDiscounted = view.isDiscountApplied();
        String paymentText = view.getPaymentAmount().trim();

        // 2. Validate inputs
        Map<Item, Integer> orderedItems = new HashMap<>();
        for (Map.Entry<Item, Integer> entry : itemQuantities.entrySet()) {
            if (entry.getValue() > 0) {
                orderedItems.put(entry.getKey(), entry.getValue());
            }
        }

        if (orderedItems.isEmpty()) {
            JOptionPane.showMessageDialog(view, "No items were ordered. Please select at least one item.", "Order Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double paymentAmount;
        try {
            paymentAmount = Double.parseDouble(paymentText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Invalid payment amount. Please enter a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 3. Calculate total
        double totalWithVat = 0;
        for (Map.Entry<Item, Integer> entry : orderedItems.entrySet()) {
            Item item = entry.getKey();
            int quantity = entry.getValue();
            totalWithVat += item.getPriceWithVat() * quantity;
        }

        // Calculate subtotal (base price) by dividing the total by VAT factor (1.12)
        double subtotal = totalWithVat / (1 + VAT_RATE);
        double totalVatAmount = totalWithVat - subtotal;

        double finalVatAmount;
        double discountAmount;
        double total;

        if (isDiscounted) {
            // If discounted, VAT is waived and a discount is applied to the subtotal
            discountAmount = subtotal * DISCOUNT_RATE;
            finalVatAmount = 0;
            total = subtotal - discountAmount;
        } else {
            // If not discounted, the full VAT is applied and there's no discount
            discountAmount = 0;
            finalVatAmount = totalVatAmount;
            total = subtotal + finalVatAmount;
        }

        if (paymentAmount < total) {
            JOptionPane.showMessageDialog(view, "Payment is not enough. Required: ₱" + String.format("%.2f", total), "Payment Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 4. Show receipt preview
        Cashier cashier = view.getCashier();
        TransactionModel transactionModel = new TransactionModel(subtotal, finalVatAmount, discountAmount, total);
        new ReceiptView(cashier, orderedItems, transactionModel, paymentAmount, dao).setVisible(true);
    }

    private void completeOrder() {
        // This is now handled directly in ReceiptView's Complete Order button
        // The ReceiptView directly saves to the database and resets the CashierView
    }
}