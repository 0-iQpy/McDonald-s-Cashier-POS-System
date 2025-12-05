package main.java.view;

import main.java.db.MySQLDAO;
import main.java.model.Cashier;
import main.java.model.Item;
import main.java.db.IDataAccessObject;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LoginView extends AbstractView {
    private final IDataAccessObject dao = new MySQLDAO();
    private JTextField cashierNameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginView() {
        super("McDonald's POS - Login", 400, 220);
        setResizable(false);
    }

    @Override
    protected void initComponents() {
        cashierNameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        loginButton = new JButton("Login");
    }

    @Override
    protected void setupLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JLabel headerLabel = new JLabel("CASHIER LOGIN", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Input Fields
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Cashier Name:", SwingConstants.RIGHT), gbc);

        gbc.gridx = 1;
        inputPanel.add(cashierNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Password:", SwingConstants.RIGHT), gbc);

        gbc.gridx = 1;
        inputPanel.add(passwordField, gbc);
        mainPanel.add(inputPanel, BorderLayout.CENTER);

        // Button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(loginButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    @Override
    protected void attachListeners() {
        loginButton.addActionListener(e -> attemptLogin());
        passwordField.addActionListener(e -> attemptLogin()); // Allow login on Enter key
    }

    private void attemptLogin() {
        String username = cashierNameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            displayError("Username and password cannot be empty.", "Input Error");
            return;
        }

        try {
            Cashier cashier = dao.authenticateCashier(username, password);
            if (cashier != null) {
                // Load menu items before showing the cashier view
                List<Item> menuItems = dao.loadMenuItems();
                if (menuItems.isEmpty()) {
                    displayError("Menu items could not be loaded. Please check the database.", "Menu Error");
                    return;
                }

                JOptionPane.showMessageDialog(this, "Login successful!", "Welcome", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new CashierView(this, cashier, menuItems, dao).setVisible(true);
            } else {
                displayError("Invalid username or password.", "Login Failed");
            }
        } catch (Exception ex) {
            displayError("An error occurred during login: " + ex.getMessage(), "Login Error");
            ex.printStackTrace();
        }
    }
}
