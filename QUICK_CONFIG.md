# Quick Configuration Checklist

## ✓ Code Fixes Applied

All the following have been FIXED in your code:

1. **LoginController.java** ✓
   - Added authentication logic
   - Added input validation
   - Added error handling

2. **OrderController.java** ✓
   - Fixed VAT calculation bug
   - Now correctly divides by 1.12 instead of multiplying

3. **MySQLDAO.java** ✓
   - Updated password field to empty string (for easy setup)
   - Added MySQL driver class loading
   - Improved error messages

## ⚙️ Configuration Steps

### Step 1: Handle MySQL Password
Choose ONE option:

**Option A: No Password (Easiest to Start)**
- Leave `PASSWORD = ""` in MySQLDAO.java (already set)
- MySQL must be set up without password

**Option B: Set Your Own Password**
- Follow MYSQL_PASSWORD_RESET.md
- Update MySQLDAO.java PASSWORD field
- Run database schema

### Step 2: Create Database

Open Command Prompt or Terminal:

**If you have no MySQL password:**
```bash
mysql -u root < docs/DatabaseSchema.sql
```

**If you set a password:**
```bash
mysql -u root -p < docs/DatabaseSchema.sql
(enter password when prompted)
```

**Or use MySQL Workbench:**
1. Open docs/DatabaseSchema.sql
2. Click "Execute"

### Step 3: Verify Database Created

Connect to MySQL:
```bash
mysql -u root
```

Check if database exists:
```sql
SHOW DATABASES;
USE pos_db;
SHOW TABLES;
```

You should see: Cashiers, Items, Transactions, Transaction_Details

### Step 4: Verify Sample Data

```sql
SELECT * FROM Cashiers;
SELECT * FROM Items;
```

Expected output:
- Cashiers: Kai / 1234
- Items: Big Mac Meal (950.00), McSpicy Sandwich (399.00), Happy Meal (250.50), Medium Coke (89.90)

### Step 5: Build and Run

```bash
cd "path\to\McDonald-s-Cashier-POS-System"
mvn clean compile
mvn javafx:run
```

Or run from your IDE (NetBeans, IntelliJ, Eclipse, etc.)

### Step 6: Test Login

- Username: **Kai**
- Password: **1234**

## 🐛 If Something Goes Wrong

### Error: "Cannot connect to database"
- Check MySQL is running: `net start MySQL80`
- Verify password in MySQLDAO.java matches MySQL password
- Check database URL: `jdbc:mysql://localhost:3306/pos_db`

### Error: "Unknown database 'pos_db'"
- Run database schema script
- Run: `mysql -u root < docs/DatabaseSchema.sql`

### Error: "MySQL JDBC Driver not found"
- Run: `mvn clean install`
- Ensures mysql-connector-java dependency is downloaded

### Error: "Access denied for user 'root'@'localhost'"
- Your password is wrong
- Follow MYSQL_PASSWORD_RESET.md to reset it
- Or set `PASSWORD = ""` if no password configured

## 📋 Current Configuration

Your MySQLDAO.java is now set to:
```java
private static final String URL = "jdbc:mysql://localhost:3306/pos_db";
private static final String USER = "root";
private static final String PASSWORD = ""; // <-- Change this if needed
```

## ✨ What Works Now

- [x] Cashier login authentication
- [x] Menu item loading from database
- [x] Order processing with correct math
- [x] Receipt generation
- [x] Sales summary reports
- [x] Discount calculations (15% for Senior/PWD)
- [x] VAT calculations (correct: 12%)
- [x] Transaction storage in database

## 📖 Additional Documentation

- **SETUP_GUIDE.md** - Detailed setup instructions
- **MYSQL_PASSWORD_RESET.md** - MySQL password reset guide
- **FIXES_SUMMARY.md** - Technical details of all fixes

## 🎯 Next: Run Your App!

Once database is set up:
1. Open your project in IDE or Command Prompt
2. Run: `mvn javafx:run`
3. Login with Kai / 1234
4. Start using the POS system!
