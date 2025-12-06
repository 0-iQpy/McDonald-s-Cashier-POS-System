package main.java.controller;

import main.java.db.IDataAccessObject;
import main.java.model.Cashier;
import main.java.view.LoginView;

import javax.swing.*;

public class LoginController {
    private final LoginView view;
    private final IDataAccessObject dao;

    public LoginController(LoginView view, IDataAccessObject dao) {
        this.view = view;
        this.dao = dao;
    }

    public void handleLogin(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            showError("Username cannot be empty", "Input Error");
            return;
        }
        if (password == null || password.isEmpty()) {
            showError("Password cannot be empty", "Input Error");
            return;
        }

        try {
            Cashier cashier = dao.authenticateCashier(username, password);
            if (cashier != null) {
                showMessage("Login successful! Welcome " + cashier.getName(), "Login Success");
            } else {
                showError("Invalid username or password", "Authentication Failed");
            }
        } catch (Exception ex) {
            showError("Login error: " + ex.getMessage(), "Database Error");
            ex.printStackTrace();
        }
    }

    private void showError(String message, String title) {
        JOptionPane.showMessageDialog(view, message, title, JOptionPane.ERROR_MESSAGE);
    }

    private void showMessage(String message, String title) {
        JOptionPane.showMessageDialog(view, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
