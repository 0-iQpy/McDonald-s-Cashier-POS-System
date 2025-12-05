package main.java.view;

import main.java.controller.OrderController;
import main.java.db.IDataAccessObject;
import main.java.model.Cashier;
import main.java.model.Item;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CashierView extends JFrame {
    private final LoginView loginView;
    private final IDataAccessObject dao;
    private final Cashier cashier;
    private JButton logoutButton;
    private JButton generateReceiptButton;
    private JCheckBox seniorPwdDiscountCheckBox;
    private JTextField paymentField;
    private JPanel menuPanel;
    private List<Item> menuItems;
    private List<JTextField> quantityFields;

    public CashierView(LoginView loginView, Cashier cashier, List<Item> menuItems, IDataAccessObject dao) {
        this.loginView = loginView;
        this.cashier = cashier;
        this.menuItems = menuItems;
        this.dao = dao;
        setTitle("McDonald's POS - Cashier");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        setupLayout();
        attachListeners();
        new OrderController(this, dao);
    }

    private void initComponents() {
        logoutButton = new JButton("Logout");
        generateReceiptButton = new JButton("Generate Receipt");
        seniorPwdDiscountCheckBox = new JCheckBox("Senior Citizen / PWD Discount (15%)");
        paymentField = new JTextField(10);
        menuPanel = new JPanel();
        quantityFields = new ArrayList<>();
    }

    private void setupLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel welcomeLabel = new JLabel("Welcome, " + cashier.getName(), SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        // Menu Panel
        menuPanel.setLayout(new GridLayout(0, 3, 10, 10)); // Use 0 for rows to allow for dynamic number of rows
        menuPanel.add(new JLabel("Item"));
        menuPanel.add(new JLabel("Price"));
        menuPanel.add(new JLabel("Quantity"));

        for (Item item : menuItems) {
            menuPanel.add(new JLabel(item.getDisplayName()));
            menuPanel.add(new JLabel(String.format("₱%.2f", item.getPriceWithVat())));
            JTextField quantityField = new JTextField("0", 5);
            quantityFields.add(quantityField);
            menuPanel.add(quantityField);
        }
        mainPanel.add(new JScrollPane(menuPanel), BorderLayout.CENTER);


        // South Panel for controls
        JPanel southPanel = new JPanel(new BorderLayout());

        JPanel discountAndPaymentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        discountAndPaymentPanel.add(seniorPwdDiscountCheckBox);
        discountAndPaymentPanel.add(new JLabel("Payment:"));
        discountAndPaymentPanel.add(paymentField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(generateReceiptButton);
        buttonPanel.add(logoutButton);

        southPanel.add(discountAndPaymentPanel, BorderLayout.WEST);
        southPanel.add(buttonPanel, BorderLayout.EAST);

        mainPanel.add(southPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void attachListeners() {
        logoutButton.addActionListener(e -> {
            try {
                java.util.Map<String, Double> summaryMetrics = dao.getSalesSummary(cashier.getId());
                if (!summaryMetrics.get("TotalOrders").equals(0.0)) {
                    new SalesSummaryView(summaryMetrics).setVisible(true);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error fetching sales summary: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
            dispose();
            loginView.setVisible(true);
        });
    }

    // getters for controller
    public List<Item> getMenuItems() {
        return menuItems;
    }

    public List<JTextField> getQuantityFields() {
        return quantityFields;
    }

    public boolean isSeniorPwdDiscountSelected() {
        return seniorPwdDiscountCheckBox.isSelected();
    }

    public String getPaymentAmount() {
        return paymentField.getText();
    }

    public JButton getGenerateReceiptButton() {
        return generateReceiptButton;
    }

    public JButton getLogoutButton() {
        return logoutButton;
    }

    public Cashier getCashier() {
        return cashier;
    }

    public LoginView getLoginView() {
        return loginView;
    }

    public JTextField getPaymentField() {
        return paymentField;
    }

    public JCheckBox getSeniorPwdDiscountCheckBox() {
        return seniorPwdDiscountCheckBox;
    }
}