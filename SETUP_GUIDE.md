# McDonald's POS System - Setup Guide

## Prerequisites
- Java 8 or higher
- MySQL Server (5.7 or higher)
- Maven 3.6 or higher

## Database Setup

### Step 1: Reset MySQL Root Password (If Forgotten)

If you've forgotten your MySQL root password, follow these steps:

#### On Windows:
1. Open Command Prompt as Administrator
2. Navigate to MySQL bin directory:
   ```cmd
   cd "C:\Program Files\MySQL\MySQL Server 8.0\bin"
   ```
3. Stop MySQL service:
   ```cmd
   net stop MySQL80
   ```
4. Start MySQL without password:
   ```cmd
   mysqld --skip-grant-tables
   ```
5. In another Command Prompt, connect to MySQL:
   ```cmd
   mysql -u root
   ```
6. Flush privileges and reset password:
   ```sql
   FLUSH PRIVILEGES;
   ALTER USER 'root'@'localhost' IDENTIFIED BY 'your_new_password';
   EXIT;
   ```
7. Restart MySQL normally:
   ```cmd
   net start MySQL80
   ```

#### Using MySQL Workbench (Easier):
1. Open MySQL Workbench
2. Go to **Server** → **Users and Privileges**
3. Select `root@localhost`
4. Click **Account Limits** tab
5. Scroll down to find the password field
6. Set a new password or leave empty
7. Click **Apply**

### Step 2: Create Database and Tables

1. Open MySQL Command Line or MySQL Workbench
2. Login with your credentials:
   ```bash
   mysql -u root -p
   ```
3. Run the database schema script:
   ```sql
   source docs/DatabaseSchema.sql
   ```

Or copy and paste the contents of `docs/DatabaseSchema.sql` into MySQL Workbench query editor and execute.

This will:
- Create the `pos_db` database
- Create all required tables (Cashiers, Items, Transactions, Transaction_Details)
- Insert sample data (Cashier: "Kai" / Password: "1234")

### Step 3: Update Application Configuration

1. Open `src/main/java/db/MySQLDAO.java`
2. Update the database credentials:
   ```java
   private static final String USER = "root";
   private static final String PASSWORD = "your_mysql_password"; // Your MySQL root password
   ```
3. If no password is set for MySQL, leave it empty:
   ```java
   private static final String PASSWORD = "";
   ```

## Build and Run

### Compile the project:
```bash
cd "path\to\McDonald-s-Cashier-POS-System"
mvn clean compile
```

### Run the application:
```bash
mvn clean javafx:run
```

Or use your IDE's run configuration.

## Sample Login Credentials

After running the database schema script, use these credentials to log in:
- **Username:** Kai
- **Password:** 1234

## Troubleshooting

### Connection Error: "MySQL JDBC Driver not found"
- Ensure `mysql-connector-java` dependency is in `pom.xml`
- Run `mvn clean install` to download dependencies

### "Access denied for user 'root'@'localhost'"
- Check your MySQL password in `MySQLDAO.java`
- Verify MySQL is running: `mysql -u root -p`
- Reset password following the steps above

### "Unknown database 'pos_db'"
- Run the `docs/DatabaseSchema.sql` script to create the database

## Database Structure

### Cashiers Table
- `cashier_id` (INT, Primary Key, Auto-increment)
- `name` (VARCHAR, Unique)
- `password` (VARCHAR)
- `created_at` (TIMESTAMP)

### Items Table
- `item_id` (INT, Primary Key, Auto-increment)
- `name` (VARCHAR, Internal Name)
- `display_name` (VARCHAR, UI Display Name)
- `price_with_vat` (DECIMAL, Price including 12% VAT)

### Transactions Table
- `transaction_id` (INT, Primary Key, Auto-increment)
- `cashier_id` (INT, Foreign Key)
- `transaction_date` (TIMESTAMP)
- `subtotal_base` (DECIMAL)
- `vat_amount` (DECIMAL)
- `discount_amount` (DECIMAL)
- `grand_total` (DECIMAL)

### Transaction_Details Table
- `detail_id` (INT, Primary Key, Auto-increment)
- `transaction_id` (INT, Foreign Key)
- `item_id` (INT, Foreign Key)
- `quantity` (INT)
- `price_at_purchase` (DECIMAL)

## Features

- **Cashier Login System:** Secure login with cashier authentication
- **Menu Management:** Fixed menu with item display names and pricing
- **Order Processing:** Select items and quantities
- **Discount System:** 15% Senior Citizen/PWD discount with VAT waived
- **Receipt Generation:** Automatic receipt with transaction details
- **Sales Summary:** Daily sales tracking and reporting

## Code Changes Made

1. **LoginController.java:** Implemented authentication logic
2. **OrderController.java:** Fixed VAT calculation (now properly divides by 1.12 instead of multiplying)
3. **MySQLDAO.java:** Updated password field to empty string for easier configuration
4. **Drivers:** Added MySQL JDBC driver loading in getConnection()
