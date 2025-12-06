# System Information & Quick Commands

## Your System Setup

### Maven
- **Location:** `C:\Users\Reymond Navasero\Downloads\apache-maven-3.9.11-bin\apache-maven-3.9.11`
- **Version:** 3.9.11
- **Build Status:** ✅ SUCCESS

### MySQL  
- **Location:** `C:\Program Files\MySQL\MySQL Server 8.0`
- **Service:** MySQL80
- **Status:** ✅ RUNNING

### Java
- **Version:** 17
- **Compiler:** javac (configured in pom.xml)

### Project
- **Name:** McDonald's Cashier POS System
- **Language:** Java
- **Build Tool:** Maven
- **Database:** MySQL

---

## Quick Commands

### PowerShell - Set Maven & Run

```powershell
# Set Maven path
$env:MAVEN_HOME = "C:\Users\Reymond Navasero\Downloads\apache-maven-3.9.11-bin\apache-maven-3.9.11"
$env:Path = "$env:MAVEN_HOME\bin;$env:Path"

# Navigate to project
cd "c:\Users\Reymond Navasero\New folder\McDonald-s-Cashier-POS-System"

# Run the application
mvn exec:java -Dexec.mainClass="main.java.MainApplication"
```

### Command Prompt - Set Maven & Run

```cmd
set MAVEN_HOME=C:\Users\Reymond Navasero\Downloads\apache-maven-3.9.11-bin\apache-maven-3.9.11
set Path=%MAVEN_HOME%\bin;%Path%
cd "c:\Users\Reymond Navasero\New folder\McDonald-s-Cashier-POS-System"
mvn exec:java -Dexec.mainClass="main.java.MainApplication"
```

### Database Setup - Method 1 (Workbench)

1. Open MySQL Workbench
2. File → Open SQL Script
3. Select: `docs/DatabaseSchema.sql`
4. Click Execute (⚡)

### Database Setup - Method 2 (Command Prompt)

```cmd
cd "c:\Users\Reymond Navasero\New folder\McDonald-s-Cashier-POS-System"
"C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -u root -p < docs\DatabaseSchema.sql
(enter password or press Enter if no password)
```

### Check MySQL Status

```powershell
Get-Service MySQL80 | Select-Object Status, Name
```

### Start MySQL if Not Running

```powershell
# In Administrator PowerShell
Start-Service MySQL80
```

### Maven Clean Build

```powershell
# Set path first
$env:MAVEN_HOME = "C:\Users\Reymond Navasero\Downloads\apache-maven-3.9.11-bin\apache-maven-3.9.11"
$env:Path = "$env:MAVEN_HOME\bin;$env:Path"

# Build
cd "c:\Users\Reymond Navasero\New folder\McDonald-s-Cashier-POS-System"
mvn clean install -DskipTests
```

---

## Test Credentials

**Cashier Login:**
- Username: `Kai`
- Password: `1234`

---

## Troubleshooting

### Problem: mvn command not found

**Solution:** Use the full Maven path
```powershell
$env:MAVEN_HOME = "C:\Users\Reymond Navasero\Downloads\apache-maven-3.9.11-bin\apache-maven-3.9.11"
$env:Path = "$env:MAVEN_HOME\bin;$env:Path"
```

### Problem: MySQL connection error

**Solution:** Make sure MySQL is running
```powershell
Get-Service MySQL80
# Should show: Running
```

If not running:
```powershell
Start-Service MySQL80
Start-Sleep -Seconds 3
```

### Problem: "Unknown database 'pos_db'"

**Solution:** Run DatabaseSchema.sql script through MySQL Workbench

### Problem: "Access denied for user 'root'"

**Solution:** 
1. Check MySQL password in `src/main/java/db/MySQLDAO.java`
2. Update if needed:
   ```java
   private static final String PASSWORD = "your_password_here";
   ```

---

## File Locations

| File | Path |
|------|------|
| Main App | `src/main/java/MainApplication.java` |
| Database Setup | `docs/DatabaseSchema.sql` |
| Build Config | `pom.xml` |
| Run Script | `run.bat` |
| Maven Config | `setup-maven.ps1` |

---

## Database Info

**Database Name:** `pos_db`

**Tables:**
1. `Cashiers` - Cashier login credentials
2. `Items` - Menu items (4 fixed items)
3. `Transactions` - Transaction summaries
4. `Transaction_Details` - Order line items

**Sample Data:**
```
Cashier: Kai / 1234
Items:
  - Big Mac Meal (₱950.00)
  - McSpicy Sandwich (₱399.00)
  - Happy Meal (₱250.50)
  - Medium Coke (₱89.90)
```

---

## Status Summary

✅ **Build:** SUCCESS (no errors)
✅ **Compilation:** SUCCESS
✅ **Dependencies:** Downloaded
✅ **MySQL:** Running
⏳ **Database:** Needs setup (one-time)
⏳ **Application:** Ready to run

**You're 99% done! Just need to:**
1. Run DatabaseSchema.sql once
2. Run the application

That's it! 🎉
