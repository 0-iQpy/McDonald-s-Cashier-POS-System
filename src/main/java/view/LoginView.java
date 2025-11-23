package main.java.view;

import main.java.db.MySQLDAO;
import main.java.model.Cashier;
import main.java.db.IDataAccessObject;

import javax.swing.*;
import java.awt.*;

public class LoginView extends AbstractView {
    private final IDataAccessObject dao = new MySQLDAO();
    private JTextField cashierNameField;
    private JButton loginButton;   

    public LoginView() {
        super("Login", 500, 300);
    }
    @Override
    protected void initComponents(){
        cashierNameField = new JTextField(20);
        loginButton = new JButton("Login");
    }

    @Override
    protected void setupLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        JPanel inputPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        inputPanel.add(new JLabel("Cashier Name:", SwingConstants.RIGHT));
        inputPanel.add(cashierNameField);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(loginButton);
        
        mainPanel.add(new JLabel("CASH REGISTER LOGIN", SwingConstants.CENTER), BorderLayout.NORTH);
        mainPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    @Override
    protected void attachListeners() {
        loginButton.addActionListener(e -> attemptLogin());
    }
    
    protected void attemptLogin() {
        String name = cashierNameField.getText().trim();

        if(name.isEmpty()) {
            displayError("Please enter your name.", "Input Error");
            return;
        }

        try {
            Cashier cashier = dao.getCashierByName(name);
            if(cashier != null) {
                JOptionPane.showMessageDialog(this, "Welcome, " + cashier.getName() + "!", "Login Successful", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                displayError("Cashier not found. Please try again.", "Login Failed");
            }
        } catch (Exception ex) {
            displayError("An error occurred while trying to log in: " + ex.getMessage(), "Login Error");
        }
    }
}
