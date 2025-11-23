package main.java.model;

import java.util.Map;

public interface ICalculator {
    void calculate(Map<Item, Integer> orderItems, boolean isDiscounted);
    TransactionModel getTransactionSummary();
}
