# MySQL Password Reset - Quick Reference for Windows

## Problem
You forgot your MySQL root password and can't login to the CLI.

## Solution: Use MySQL Workbench (Easiest)

### If you have MySQL Workbench installed:

1. Open MySQL Workbench
2. Click on "MySQL Connections" in the left panel
3. Right-click on your localhost connection and select "Edit Connection"
4. Click "Store in Vault" and set a new password
5. Click "Test Connection" to verify it works
6. Click "OK" to save

Then update MySQLDAO.java:
```java
private static final String PASSWORD = "your_new_password";
```

## Solution: Use Command Line (More Direct)

### Step-by-step for Windows Command Prompt:

1. **Open Command Prompt as Administrator**
   - Right-click on Command Prompt → "Run as Administrator"

2. **Navigate to MySQL bin directory:**
   ```
   cd "C:\Program Files\MySQL\MySQL Server 8.0\bin"
   ```
   (Path may vary depending on MySQL version)

3. **Stop MySQL service:**
   ```
   net stop MySQL80
   ```
   (If you have MySQL 5.7, use `MySQL57` instead)

4. **Start MySQL without password protection:**
   ```
   mysqld --skip-grant-tables
   ```
   Leave this window open!

5. **Open another Command Prompt as Administrator** and connect:
   ```
   mysql -u root
   ```

6. **Reset the password:**
   ```sql
   FLUSH PRIVILEGES;
   ALTER USER 'root'@'localhost' IDENTIFIED BY 'newpassword123';
   EXIT;
   ```

7. **Restart MySQL normally:**
   - Close the first mysqld window
   - In Command Prompt:
     ```
     net start MySQL80
     ```

8. **Test your new password:**
   ```
   mysql -u root -p
   newpassword123
   ```

## Solution: Set No Password (Temporary)

If you just want to get it working quickly:

1. Follow the steps above
2. Use empty password:
   ```sql
   ALTER USER 'root'@'localhost' IDENTIFIED BY '';
   ```

3. In MySQLDAO.java:
   ```java
   private static final String PASSWORD = ""; // Empty string
   ```

## After Resetting Password

1. Update `MySQLDAO.java`:
   ```java
   private static final String PASSWORD = "your_new_password"; // Change this
   ```

2. Create the database:
   ```
   mysql -u root -p
   (enter your password)
   source docs/DatabaseSchema.sql
   ```

3. Run the application

## Stuck? Try this:

If MySQL won't start or won't respond:

1. Open Services (Win+R → `services.msc`)
2. Find "MySQL80" (or MySQL57)
3. Right-click → "Restart"
4. Wait 30 seconds
5. Try again

## For MySQL 5.7 users:
Replace `MySQL80` with `MySQL57` in all commands above
