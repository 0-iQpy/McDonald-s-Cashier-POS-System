package main.java.view;

import main.java.db.IDataAccessObject;
import main.java.model.Cashier;

import javax.swing.*;

public class AdminAuthDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private boolean authenticated = false;
    private IDataAccessObject dao;

    public AdminAuthDialog(JFrame parent, IDataAccessObject dao) {
        super(parent, "Admin Authentication", true);
        this.dao = dao;
        
        setSize(350, 200);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setupLayout();
        setVisible(true);
    }

    private void setupLayout() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new javax.swing.BoxLayout(mainPanel, javax.swing.BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Username field
        JPanel usernamePanel = new JPanel();
        usernamePanel.setLayout(new javax.swing.BoxLayout(usernamePanel, javax.swing.BoxLayout.X_AXIS));
        usernamePanel.add(new JLabel("Username:"));
        usernamePanel.add(Box.createHorizontalStrut(10));
        usernameField = new JTextField(15);
        usernamePanel.add(usernameField);
        mainPanel.add(usernamePanel);

        mainPanel.add(Box.createVerticalStrut(10));

        // Password field
        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new javax.swing.BoxLayout(passwordPanel, javax.swing.BoxLayout.X_AXIS));
        passwordPanel.add(new JLabel("Password:"));
        passwordPanel.add(Box.createHorizontalStrut(15));
        passwordField = new JPasswordField(15);
        passwordPanel.add(passwordField);
        mainPanel.add(passwordPanel);

        mainPanel.add(Box.createVerticalStrut(20));

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton loginButton = new JButton("Login");
        JButton cancelButton = new JButton("Cancel");

        loginButton.addActionListener(e -> authenticateAdmin());
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel);

        add(mainPanel);
    }

    private void authenticateAdmin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and password cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Cashier admin = dao.authenticateCashier(username, password);
            if (admin != null) {
                authenticated = true;
                JOptionPane.showMessageDialog(this, "Admin authenticated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                authenticated = false;
                JOptionPane.showMessageDialog(this, "Invalid admin credentials.", "Authentication Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isAuthenticated() {
        return authenticated;
    }
}
