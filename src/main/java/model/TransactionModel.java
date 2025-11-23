package main.java.model;

public class TransactionModel {
    private final double subtotalBase;
    private final double vatAmount;
    private final double discountAmount;
    private final double grandTotal;
    private final boolean isSeniorPwd;

    public TransactionModel(double subtotalBase, double vatAmount, double discountAmount, double grandTotal, boolean isSeniorPwd) {
        this.subtotalBase = subtotalBase;
        this.vatAmount = vatAmount;
        this.discountAmount = discountAmount;
        this.grandTotal = grandTotal;
        this.isSeniorPwd = isSeniorPwd;
    }

    public double getSubtotalBase() {
        return subtotalBase;
    }

    public double getVatAmount() {
        return vatAmount;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }   

    public double getGrandTotal() {
        return grandTotal;
    }   

    public boolean isSeniorPwd() {
        return isSeniorPwd;
    }   
}
