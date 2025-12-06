@echo off
REM Quick run script for McDonald's POS System

echo.
echo ========================================
echo McDonald's POS System - QUICK RUN
echo ========================================
echo.

REM Set Maven path
set MAVEN_HOME=C:\Users\Reymond Navasero\Downloads\apache-maven-3.9.11-bin\apache-maven-3.9.11
set Path=%MAVEN_HOME%\bin;%Path%

REM Set MySQL path
set MYSQL_PATH=C:\Program Files\MySQL\MySQL Server 8.0\bin

REM Step 1: Check MySQL is running
echo [STEP 1] Checking MySQL...
tasklist | find "mysqld.exe" >nul
if %errorlevel% neq 0 (
    echo [WARNING] MySQL doesn't appear to be running
    echo Starting MySQL80 service...
    net start MySQL80
    timeout /t 3 /nobreak
)
echo [OK] MySQL is running
echo.

REM Step 2: Create database (if not exists)
echo [STEP 2] Setting up database...
cd /d "%~dp0"
echo Running DatabaseSchema.sql...
"%MYSQL_PATH%\mysql.exe" -u root -p < docs\DatabaseSchema.sql
if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Could not create database. MySQL password needed?
    echo.
    echo Try this:
    echo 1. Open MySQL Workbench
    echo 2. Open docs\DatabaseSchema.sql
    echo 3. Click Execute (lightning bolt icon)
    echo.
    echo OR run from Command Prompt:
    echo   cd "%~dp0"
    echo   mysql -u root -p
    echo   source docs\DatabaseSchema.sql;
    echo.
    pause
    exit /b 1
)
echo [OK] Database created
echo.

REM Step 3: Run application
echo [STEP 3] Running application...
echo.
mvn exec:java -Dexec.mainClass="main.java.MainApplication"

pause
