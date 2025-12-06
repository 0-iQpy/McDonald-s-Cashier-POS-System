# McDonald's POS System - Maven Build Script (PowerShell)

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "McDonald's POS System - Build Setup" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Step 1: Find Maven installation
Write-Host "[STEP 1] Finding Maven installation..." -ForegroundColor Yellow

$mavenPath = $null

# Common Maven installation locations
$possiblePaths = @(
    "C:\apache-maven-3.9.6",
    "C:\apache-maven-3.9.5",
    "C:\apache-maven-3.9.4",
    "C:\Maven",
    "$env:USERPROFILE\apache-maven-3.9.6",
    "$env:USERPROFILE\maven",
    "C:\Program Files\maven",
    "C:\Program Files (x86)\maven"
)

foreach ($path in $possiblePaths) {
    if (Test-Path "$path\bin\mvn.cmd") {
        $mavenPath = $path
        Write-Host "[OK] Found Maven at: $mavenPath" -ForegroundColor Green
        break
    }
}

if (-not $mavenPath) {
    Write-Host "[ERROR] Maven not found in common locations!" -ForegroundColor Red
    Write-Host ""
    Write-Host "Please provide Maven location:" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Option 1: Tell me your Maven path (e.g., C:\apache-maven-3.9.6)"
    Write-Host ""
    Write-Host "Option 2: Search for it:"
    Write-Host "  Get-ChildItem -Path 'C:\' -Recurse -Name 'mvn.cmd' -ErrorAction SilentlyContinue | Select-Object -First 5"
    Write-Host ""
    exit 1
}

# Step 2: Add Maven to PATH for this session
Write-Host ""
Write-Host "[STEP 2] Adding Maven to PATH..." -ForegroundColor Yellow
$env:MAVEN_HOME = $mavenPath
$env:Path = "$mavenPath\bin;$env:Path"

# Verify Maven works
Write-Host ""
Write-Host "[STEP 3] Verifying Maven installation..." -ForegroundColor Yellow
$mvnVersion = & mvn --version 2>&1
if ($LASTEXITCODE -eq 0) {
    Write-Host "[OK] Maven is working!" -ForegroundColor Green
    Write-Host $mvnVersion
} else {
    Write-Host "[ERROR] Maven verification failed!" -ForegroundColor Red
    Write-Host $mvnVersion
    exit 1
}

# Step 3: Build project
Write-Host ""
Write-Host "[STEP 4] Building project..." -ForegroundColor Yellow
Write-Host ""

cd "c:\Users\Reymond Navasero\New folder\McDonald-s-Cashier-POS-System"

& mvn clean install -DskipTests

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Green
    Write-Host "[SUCCESS] Build completed!" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "NEXT STEPS:" -ForegroundColor Cyan
    Write-Host "1. Make sure MySQL is running" -ForegroundColor White
    Write-Host "2. Create database (in Command Prompt or PowerShell):" -ForegroundColor White
    Write-Host "   mysql -u root < docs\DatabaseSchema.sql" -ForegroundColor Yellow
    Write-Host "3. Run the application:" -ForegroundColor White
    Write-Host "   mvn exec:java -Dexec.mainClass=`"main.java.MainApplication`"" -ForegroundColor Yellow
    Write-Host ""
} else {
    Write-Host ""
    Write-Host "[ERROR] Build failed! See errors above." -ForegroundColor Red
    exit 1
}
