@echo off
REM Quick Maven Setup and Build Script for McDonald's POS System

echo.
echo ========================================
echo McDonald's POS System - Build Script
echo ========================================
echo.

REM Check if Maven is installed
where mvn >nul 2>nul
if %errorlevel% neq 0 (
    echo [ERROR] Maven is not in your PATH
    echo.
    echo Please do ONE of the following:
    echo.
    echo OPTION 1: Add Maven to PATH manually
    echo 1. Press Win+X and select "System"
    echo 2. Click "Advanced system settings"
    echo 3. Click "Environment Variables"
    echo 4. Under "System variables", click "New"
    echo 5. Variable name: MAVEN_HOME
    echo 6. Variable value: C:\apache-maven-3.9.6 (or your Maven location)
    echo 7. Find "Path" variable, click Edit, and add: C:\apache-maven-3.9.6\bin
    echo 8. Click OK, close all windows, and restart PowerShell
    echo.
    echo OPTION 2: Tell me where you installed Maven
    echo Run this command to find it:
    echo   dir C:\ /s /b *mvn.cmd
    echo.
    pause
    exit /b 1
)

echo [OK] Maven found
mvn --version
echo.

REM Navigate to project
cd /d "%~dp0"
echo [OK] Changed to project directory
echo Working directory: %cd%
echo.

REM Clean and install
echo [BUILDING] Running: mvn clean install -DskipTests
echo.
mvn clean install -DskipTests

if %errorlevel% equ 0 (
    echo.
    echo ========================================
    echo [SUCCESS] Build completed!
    echo ========================================
    echo.
    echo To run the application:
    echo 1. Make sure MySQL is running
    echo 2. Create database: mysql -u root ^< docs\DatabaseSchema.sql
    echo 3. Run: mvn exec:java -Dexec.mainClass="main.java.MainApplication"
    echo.
    pause
) else (
    echo.
    echo [ERROR] Build failed. See errors above.
    echo.
    pause
    exit /b 1
)
