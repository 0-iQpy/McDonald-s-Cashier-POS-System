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
        view.getGenerateReceiptButton().addActionListener(e -> processOrder());
    }

    private void processOrder() {
        // 1. Get order details from the view
        List<Item> menuItems = view.getMenuItems();
        List<JTextField> quantityFields = view.getQuantityFields();
        boolean isDiscounted = view.isSeniorPwdDiscountSelected();
        String paymentText = view.getPaymentAmount().trim();

        // 2. Validate inputs
        Map<Item, Integer> orderedItems = new HashMap<>();
        for (int i = 0; i < menuItems.size(); i++) {
            try {
                int quantity = Integer.parseInt(quantityFields.get(i).getText().trim());
                if (quantity > 0) {
                    orderedItems.put(menuItems.get(i), quantity);
                } else if (quantity < 0) {
                    JOptionPane.showMessageDialog(view, "Quantity cannot be negative.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(view, "Invalid quantity entered.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        if (orderedItems.isEmpty()) {
            JOptionPane.showMessageDialog(view, "No items were ordered.", "Order Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double paymentAmount;
        try {
            paymentAmount = Double.parseDouble(paymentText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Invalid payment amount.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 3. Calculate total based on user-provided logic
        double totalWithVat = 0;
        for (Map.Entry<Item, Integer> entry : orderedItems.entrySet()) {
            Item item = entry.getKey();
            int quantity = entry.getValue();
            totalWithVat += item.getPriceWithVat() * quantity;
        }

        // Calculate subtotal (base price) by subtracting the VAT from the total price
        double totalVatAmount = totalWithVat * VAT_RATE;
        double subtotal = totalWithVat - totalVatAmount;

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
            JOptionPane.showMessageDialog(view, "Payment is not enough.", "Payment Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 4. Save transaction
        Cashier cashier = view.getCashier();
        TransactionModel transactionModel = new TransactionModel(subtotal, finalVatAmount, discountAmount, total);
        try {
            dao.saveTransaction(cashier, orderedItems, transactionModel);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Error saving transaction: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 5. Generate receipt
        new ReceiptView(cashier, orderedItems, transactionModel, paymentAmount).setVisible(true);

        // 6. Reset UI
        for (JTextField quantityField : view.getQuantityFields()) {
            quantityField.setText("0");
        }
        view.getPaymentField().setText("");
        view.getSeniorPwdDiscountCheckBox().setSelected(false);
    }
}