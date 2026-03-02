@echo off
REM GitHub push script for ETL-Pipeline-for-Telecom-Data

echo ========================================
echo ETL Pipeline - GitHub Push Setup
echo ========================================
echo.

REM Check if git is installed
git --version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Git is not installed or not in PATH
    echo Please install Git from: https://git-scm.com/download/win
    pause
    exit /b 1
)

echo [1/5] Initializing Git repository...
git init

echo [2/5] Adding all files to staging area...
git add .

echo [3/5] Creating initial commit...
git commit -m "Initial commit: ETL Pipeline for Telecom Data"

echo [4/5] Adding remote repository...
REM Replace USERNAME with your GitHub username
git remote add origin https://github.com/USERNAME/ETL-Pipeline-for-Telecom-Data.git

echo [5/5] Pushing to GitHub...
REM Replace main with master if your default branch is master
git branch -M main
git push -u origin main

echo.
echo ========================================
echo SUCCESS! Project pushed to GitHub
echo ========================================
echo Repository: https://github.com/USERNAME/ETL-Pipeline-for-Telecom-Data
echo.
echo NOTE: Remember to:
echo 1. Replace USERNAME with your GitHub username
echo 2. Ensure the GitHub repository exists
echo 3. Set up SSH key or use GitHub token for authentication
echo.
pause
