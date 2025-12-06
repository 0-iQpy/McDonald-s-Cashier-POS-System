package main.java.db;

import main.java.model.Item;
import main.java.model.TransactionModel;
import main.java.model.Cashier;

import java.util.List;
import java.util.Map;

public interface IDataAccessObject {
    Cashier authenticateCashier(String name, String password) throws Exception;

    List<Item> loadMenuItems() throws Exception;

    int saveTransaction(Cashier cashier, Map<Item, Integer> orderItems, TransactionModel summary) throws Exception;

    Map<String, Double> getSalesSummary(int cashierId) throws Exception;
}
