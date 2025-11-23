-- ----------------------------------------------------------------------
-- Database Initialization
-- ----------------------------------------------------------------------
CREATE DATABASE IF NOT EXISTS pos_db;
USE pos_db;

-- ----------------------------------------------------------------------
-- 1. Cashiers Table (For Login System)
-- Requirement: Cashier must input name to log in
-- Constraint: Cashier's name cannot be empty or blank
-- ----------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS Cashiers (
    cashier_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE, 
    password VARCHAR(255) NOT NULL, -- Added for robust login implementation
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Initial Data: Insert the sample cashier used in the scenarios
INSERT INTO Cashiers (name, password) VALUES 
('Kai', '1234');


-- ----------------------------------------------------------------------
-- 2. Items Table (Fixed Menu - Hybrid Design)
-- Note: Uses fixed prices for calculation but includes a display_name for the UI.
-- Requirement: Fixed menu items with 12% VAT included in prices
-- ----------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS Items (
    item_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,              -- Internal Name (e.g., 'Item 1')
    display_name VARCHAR(100) NOT NULL,     -- UI Name (e.g., 'Big Mac Meal')
    price_with_vat DECIMAL(10, 2) NOT NULL  -- Required price for calculation
);

-- Initial Data: Insert the four fixed menu items with required prices
INSERT INTO Items (name, display_name, price_with_vat) VALUES 
('Item 1', 'Big Mac Meal', 950.00),     --
('Item 2', 'McSpicy Sandwich', 399.00), --
('Item 3', 'Happy Meal', 250.50),       --
('Item 4', 'Medium Coke', 89.90);       --


-- ----------------------------------------------------------------------
-- 3. Transactions Table (Order Summary)
-- Requirement: Store transactions for Sales Summary
-- ----------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS Transactions (
    transaction_id INT PRIMARY KEY AUTO_INCREMENT,
    cashier_id INT NOT NULL, 
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Financial Breakdown columns to generate Sales Summary and Receipt details
    subtotal_base DECIMAL(10, 2) NOT NULL,    -- Total price without VAT/discount
    vat_amount DECIMAL(10, 2) NOT NULL,       
    discount_amount DECIMAL(10, 2) NOT NULL,  
    grand_total DECIMAL(10, 2) NOT NULL,      -- Final amount paid
    
    FOREIGN KEY (cashier_id) REFERENCES Cashiers(cashier_id)
);


-- ----------------------------------------------------------------------
-- 4. Transaction_Details Table (Line Items per Order)
-- ----------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS Transaction_Details (
    detail_id INT PRIMARY KEY AUTO_INCREMENT,
    transaction_id INT NOT NULL, 
    item_id INT NOT NULL,        
    quantity INT NOT NULL,       -- Number of pieces purchased
    price_at_purchase DECIMAL(10, 2) NOT NULL, -- Price with VAT at time of sale
    
    FOREIGN KEY (transaction_id) REFERENCES Transactions(transaction_id),
    FOREIGN KEY (item_id) REFERENCES Items(item_id)
);