package main.java.view;

import main.java.model.Cashier;

import javax.swing.*;
import java.awt.*;

public class CashierView extends JFrame {
    private final LoginView loginView;
    private final Cashier cashier;
    private JButton logoutButton;

    public CashierView(LoginView loginView, Cashier cashier) {
        this.loginView = loginView;
        this.cashier = cashier;
        setTitle("McDonald's POS - Cashier");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        setupLayout();
        attachListeners();
    }

    private void initComponents() {
        logoutButton = new JButton("Logout");
    }

    private void setupLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel welcomeLabel = new JLabel("Welcome, " + cashier.getName(), SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(logoutButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void attachListeners() {
        logoutButton.addActionListener(e -> {
            dispose();
            loginView.setVisible(true);
        });
    }
}