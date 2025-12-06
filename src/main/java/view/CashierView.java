package main.java.view;

import main.java.controller.OrderController;
import main.java.db.IDataAccessObject;
import main.java.model.Cashier;
import main.java.model.Item;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CashierView extends JFrame {
    private final LoginView loginView;
    private final IDataAccessObject dao;
    private final Cashier cashier;
    private JButton logoutButton;
    private JButton previewReceiptButton;
    private JButton completeOrderButton;
    private JButton adminButton;
    private JButton applyDiscountButton;
    private JLabel discountStatusLabel;
    private JTextField paymentField;
    private JPanel itemsPanel;
    private List<Item> menuItems;
    private Map<Item, Integer> itemQuantities;
    private List<ItemPanel> itemPanels;
    private boolean discountApplied = false;

    public CashierView(LoginView loginView, Cashier cashier, List<Item> menuItems, IDataAccessObject dao) {
        this.loginView = loginView;
        this.cashier = cashier;
        this.menuItems = menuItems;
        this.dao = dao;
        this.itemQuantities = new HashMap<>();
        this.itemPanels = new ArrayList<>();
        
        setTitle("McDonald's POS - Cashier View");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
        setupLayout();
        attachListeners();
        new OrderController(this, dao);
    }

    private void initComponents() {
        logoutButton = new JButton("Logout");
        previewReceiptButton = new JButton("Preview Receipt");
        completeOrderButton = new JButton("Complete Order");
        adminButton = new JButton("Admin");
        applyDiscountButton = new JButton("Apply Discount");
        discountStatusLabel = new JLabel("No Discount");
        discountStatusLabel.setForeground(Color.RED);
        paymentField = new JTextField(15);
        itemsPanel = new JPanel();
        
        // Initialize quantities
        for (Item item : menuItems) {
            itemQuantities.put(item, 0);
        }
    }

    private void setupLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome, " + cashier.getName(), SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(welcomeLabel, BorderLayout.CENTER);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Items Panel - Clickable items with +/- buttons
        itemsPanel.setLayout(new GridLayout(0, 1, 5, 5));
        JScrollPane itemsScrollPane = new JScrollPane(itemsPanel);
        itemsScrollPane.setBorder(BorderFactory.createTitledBorder("Menu Items - Click to Select"));
        
        for (Item item : menuItems) {
            ItemPanel itemPanel = new ItemPanel(item, this);
            itemPanels.add(itemPanel);
            itemsPanel.add(itemPanel);
        }
        
        mainPanel.add(itemsScrollPane, BorderLayout.CENTER);

        // Control Panel
        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Top control row - Discount and Admin
        JPanel discountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        discountPanel.add(applyDiscountButton);
        discountPanel.add(discountStatusLabel);
        discountPanel.add(new JSeparator(SwingConstants.VERTICAL));
        discountPanel.add(adminButton);
        controlPanel.add(discountPanel, BorderLayout.NORTH);

        // Bottom control row - Payment and Action buttons
        JPanel paymentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        paymentPanel.add(new JLabel("Payment Amount (₱):"));
        paymentPanel.add(paymentField);
        controlPanel.add(paymentPanel, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.add(previewReceiptButton);
        actionPanel.add(completeOrderButton);
        actionPanel.add(logoutButton);
        controlPanel.add(actionPanel, BorderLayout.SOUTH);

        mainPanel.add(controlPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void attachListeners() {
        logoutButton.addActionListener(e -> handleLogout());
        applyDiscountButton.addActionListener(e -> handleApplyDiscount());
        adminButton.addActionListener(e -> openAdminView());
    }

    private void handleLogout() {
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
    }

    private void handleApplyDiscount() {
        AdminAuthDialog authDialog = new AdminAuthDialog(this, dao);
        if (authDialog.isAuthenticated()) {
            discountApplied = true;
            discountStatusLabel.setText("✓ Senior/PWD Discount Applied (15%)");
            discountStatusLabel.setForeground(new Color(0, 150, 0));
            JOptionPane.showMessageDialog(this, "Discount successfully applied!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            discountApplied = false;
            discountStatusLabel.setText("No Discount");
            discountStatusLabel.setForeground(Color.RED);
        }
    }

    private void openAdminView() {
        AdminAuthDialog authDialog = new AdminAuthDialog(this, dao);
        if (authDialog.isAuthenticated()) {
            new AdminView(dao).setVisible(true);
        }
    }

    public void updateItemQuantity(Item item, int quantity) {
        if (quantity < 0) {
            quantity = 0;
        }
        itemQuantities.put(item, quantity);
    }

    // Getters for controller
    public List<Item> getMenuItems() {
        return menuItems;
    }

    public Map<Item, Integer> getItemQuantities() {
        return itemQuantities;
    }

    public boolean isDiscountApplied() {
        return discountApplied;
    }

    public void setDiscountApplied(boolean applied) {
        this.discountApplied = applied;
    }

    public String getPaymentAmount() {
        return paymentField.getText();
    }

    public JButton getPreviewReceiptButton() {
        return previewReceiptButton;
    }

    public JButton getCompleteOrderButton() {
        return completeOrderButton;
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

    public void resetOrder() {
        itemQuantities.clear();
        for (Item item : menuItems) {
            itemQuantities.put(item, 0);
        }
        for (ItemPanel panel : itemPanels) {
            panel.resetQuantity();
        }
        paymentField.setText("");
        discountApplied = false;
        discountStatusLabel.setText("No Discount");
        discountStatusLabel.setForeground(Color.RED);
    }

    // Inner class for clickable item panel
    private static class ItemPanel extends JPanel {
        private Item item;
        private JLabel quantityLabel;
        private int quantity = 0;
        private CashierView parent;

        ItemPanel(Item item, CashierView parent) {
            this.item = item;
            this.parent = parent;
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            setBackground(new Color(240, 240, 240));
            setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

            // Item info panel
            JPanel infoPanel = new JPanel(new BorderLayout());
            infoPanel.setBackground(new Color(240, 240, 240));
            
            JLabel nameLabel = new JLabel(item.getDisplayName());
            nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
            
            JLabel priceLabel = new JLabel(String.format("₱%.2f", item.getPriceWithVat()));
            priceLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            priceLabel.setForeground(new Color(0, 100, 0));
            
            infoPanel.add(nameLabel, BorderLayout.WEST);
            infoPanel.add(priceLabel, BorderLayout.EAST);

            // Quantity control panel
            JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            controlPanel.setBackground(new Color(240, 240, 240));
            
            JButton minusBtn = new JButton("-");
            minusBtn.setPreferredSize(new java.awt.Dimension(40, 30));
            minusBtn.addActionListener(e -> decreaseQuantity());
            
            quantityLabel = new JLabel("0");
            quantityLabel.setFont(new Font("Arial", Font.BOLD, 16));
            quantityLabel.setPreferredSize(new java.awt.Dimension(40, 30));
            quantityLabel.setHorizontalAlignment(SwingConstants.CENTER);
            
            JButton plusBtn = new JButton("+");
            plusBtn.setPreferredSize(new java.awt.Dimension(40, 30));
            plusBtn.addActionListener(e -> increaseQuantity());
            
            controlPanel.add(minusBtn);
            controlPanel.add(quantityLabel);
            controlPanel.add(plusBtn);

            add(infoPanel, BorderLayout.CENTER);
            add(controlPanel, BorderLayout.EAST);

            // Mouse listener for item selection
            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    setBackground(new Color(220, 220, 220));
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    setBackground(new Color(240, 240, 240));
                }
            });
        }

        private void increaseQuantity() {
            quantity++;
            quantityLabel.setText(String.valueOf(quantity));
            parent.updateItemQuantity(item, quantity);
        }

        private void decreaseQuantity() {
            if (quantity > 0) {
                quantity--;
                quantityLabel.setText(String.valueOf(quantity));
                parent.updateItemQuantity(item, quantity);
            }
        }

        public void resetQuantity() {
            quantity = 0;
            quantityLabel.setText("0");
        }
    }
}