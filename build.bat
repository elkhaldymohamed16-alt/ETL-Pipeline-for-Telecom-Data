@echo off
cd /d "c:\Users\elkha\IdeaProjects\telecom-etl"
echo ========================================
echo Starting Maven Clean and Build...
echo ========================================
call mvnw.cmd clean install -DskipTests
echo.
echo ========================================
echo Build Complete!
echo ========================================
pause
