package main.java.model;

import java.util.Map;

public class SalesCalculator implements ICalculator{
    private static final double DISCOUNT_RATE = 0.15;
    private static final double VAT_RATE = 0.12;

    private double subtotalBase = 0.0;
    private double vatAmount = 0.0;
    private double discountAmount = 0.0;
    private double grandTotal = 0.0;
    private boolean isDiscounted = false;

    @Override
    public void calculate(Map<Item, Integer> orderItems, boolean isDiscounted) {
        subtotalBase = 0.0;
        for (Map.Entry<Item, Integer> entry : orderItems.entrySet()) {
            subtotalBase += entry.getKey().getBasePrice() * entry.getValue();
        }
        if (isDiscounted) {
            discountAmount = subtotalBase * DISCOUNT_RATE;
            vatAmount = 0.0;
            isDiscounted = true;
            grandTotal = subtotalBase - discountAmount;
        } else {
            discountAmount = 0.0;
            isDiscounted = false;
            vatAmount = subtotalBase * VAT_RATE;
            grandTotal = subtotalBase + vatAmount;
        }
        this.subtotalBase = Math.round(subtotalBase * 100.0) / 100.0;
        this.vatAmount = Math.round(vatAmount * 100.0) / 100.0;
        this.discountAmount = Math.round(discountAmount * 100.0) / 100.0;
        this.grandTotal = Math.round(grandTotal * 100.0) / 100.0;
        this.isDiscounted = isDiscounted;
    }

    @Override
    public TransactionModel getTransactionSummary() {
        return new TransactionModel(subtotalBase, vatAmount, discountAmount, grandTotal);
    }
}
