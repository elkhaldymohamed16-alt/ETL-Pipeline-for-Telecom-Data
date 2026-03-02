# Developer Setup Guide

For developers who clone this repository.

## Prerequisites

- Java 21 or higher
- Maven 3.6+
- PostgreSQL 12+ (or another supported database)
- Git
- IDE (IntelliJ IDEA, Eclipse, VS Code, etc.)

---

## 🚀 Quick Start (5 Minutes)

### 1. Clone Repository
```bash
git clone https://github.com/YOUR_USERNAME/ETL-Pipeline-for-Telecom-Data.git
cd ETL-Pipeline-for-Telecom-Data
```

### 2. Configure Database Credentials

**Option A: Using Local Properties File (Recommended for Development)**

```bash
# Copy the example file
cp src/main/resources/application.properties.example src/main/resources/application-local.properties

# Edit with your database details
# Windows
notepad src/main/resources/application-local.properties

# Linux/Mac
nano src/main/resources/application-local.properties
```

Add your credentials:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/telecom_db
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD_HERE
```

**Option B: Using Environment Variables**

Windows (cmd):
```batch
set DB_URL=jdbc:postgresql://localhost:5432/telecom_db
set DB_USERNAME=postgres
set DB_PASSWORD=your_secure_password
```

Windows (PowerShell):
```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/telecom_db"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="your_secure_password"
```

Linux/Mac (bash):
```bash
export DB_URL=jdbc:postgresql://localhost:5432/telecom_db
export DB_USERNAME=postgres
export DB_PASSWORD=your_secure_password
```

### 3. Create Database

```bash
# Connect to PostgreSQL
psql -U postgres

# Create database
CREATE DATABASE telecom_db;

# Exit
\q
```

Or with PostgreSQL GUI tools (pgAdmin, DBeaver, etc.)

### 4. Build Project

```bash
mvn clean install
```

### 5. Run Application

```bash
mvn spring-boot:run
```

The application will:
1. Generate 100,000 test CDR records to `data/cdr_data.csv`
2. Start Spring Boot server
3. Read CSV file
4. Transform and validate data
5. Load into PostgreSQL database

---

## 🔧 Configuration Details

### Application Properties

The application uses Spring's property resolution hierarchy:

1. **Environment Variables** (Highest Priority)
   - `DB_URL`
   - `DB_USERNAME`
   - `DB_PASSWORD`

2. **application-local.properties** (Local Development)
   - Created from `application.properties.example`
   - Excluded from Git by `.gitignore`

3. **application.properties** (Default Fallback)
   - Uses environment variable substitution
   - Checked into Git (safe - no credentials)

### IDE Setup

#### IntelliJ IDEA
```
1. File → Project Structure → Project → SDK → Select Java 21
2. File → Settings → Build, Execution, Deployment → Maven → Select Maven version
3. Run → Edit Configurations → Add "Maven"
   - Name: "Run Application"
   - Command line: "spring-boot:run"
   - Environment variables:
     DB_URL=jdbc:postgresql://localhost:5432/telecom_db
     DB_USERNAME=postgres
     DB_PASSWORD=your_password
```

#### VS Code
```
1. Install Extension Pack for Java
2. Install SQL Tools Extension
3. Create `.vscode/launch.json`:
{
  "version": "0.2.0",
  "configurations": [
    {
      "type": "java",
      "name": "Spring Boot App",
      "request": "launch",
      "mainClass": "com.example.telecometl.TelecomEtlApplication",
      "projectName": "telecom-etl",
      "cwd": "${workspaceFolder}",
      "console": "integratedTerminal",
      "env": {
        "DB_URL": "jdbc:postgresql://localhost:5432/telecom_db",
        "DB_USERNAME": "postgres",
        "DB_PASSWORD": "your_password"
      }
    }
  ]
}
```

#### Eclipse
```
1. File → New → Java Project → Select Java 21
2. Right-click Project → Properties → Java Build Path → Libraries
3. Add JARs from Maven repository
4. Run → Run Configurations → Arguments → VM arguments:
   -DDB_URL=jdbc:postgresql://localhost:5432/telecom_db
   -DDB_USERNAME=postgres
   -DDB_PASSWORD=your_password
```

---

## 🧪 Testing

### Run Tests
```bash
mvn test
```

### Run Integration Tests
```bash
mvn verify
```

### View Test Results
```bash
# After running tests, results in:
target/surefire-reports/
```

---

## 🏗️ Building for Production

### Build JAR
```bash
mvn clean package
```

Output: `target/telecom-etl-0.0.1-SNAPSHOT.jar`

### Run JAR
```bash
# Set environment variables first
set DB_URL=jdbc:postgresql://your-prod-db:5432/telecom_db
set DB_USERNAME=prod_user
set DB_PASSWORD=prod_password

# Run
java -jar target/telecom-etl-0.0.1-SNAPSHOT.jar
```

### Docker Build (Optional)
```bash
# Create Dockerfile in project root
mvn clean package
docker build -t telecom-etl:latest .
docker run -e DB_URL=... -e DB_USERNAME=... -e DB_PASSWORD=... telecom-etl:latest
```

---

## 📝 File Structure

```
telecom-etl/
├── src/
│   ├── main/
│   │   ├── java/com/example/telecometl/
│   │   │   ├── TelecomEtlApplication.java          (Entry point)
│   │   │   ├── config/
│   │   │   │   └── BatchConfig.java                (Batch configuration)
│   │   │   ├── entity/
│   │   │   │   ├── CallRecord.java                 (JPA Entity)
│   │   │   │   └── CallRecordDto.java              (Data Transfer Object)
│   │   │   └── repository/
│   │   │       └── CallRecordRepository.java       (Data Access Layer)
│   │   └── resources/
│   │       ├── application.properties              (Default config)
│   │       └── application.properties.example      (Template)
│   └── test/
│       └── java/...                                (Test files)
├── data/
│   └── cdr_data.csv                                (Generated test data)
├── pom.xml                                         (Maven dependencies)
├── mvnw / mvnw.cmd                                 (Maven wrapper)
├── .gitignore                                      (Git ignore rules)
├── README.md                                       (Project documentation)
├── GIT_PUSH_INSTRUCTIONS.md                        (Git guide)
└── SECURITY_SCAN_REPORT.md                         (Security audit)
```

---

## 🐛 Troubleshooting

### Issue: "Cannot connect to database"

**Solution:**
1. Check PostgreSQL is running:
   - Windows: Services → PostgreSQL
   - Linux: `systemctl status postgresql`
   - Mac: `brew services list`

2. Verify database exists:
   ```bash
   psql -U postgres -l | grep telecom_db
   ```

3. Check credentials:
   ```bash
   psql -U postgres -h localhost -d telecom_db
   ```

4. Check environment variables are set:
   ```bash
   # Windows
   echo %DB_URL%
   echo %DB_USERNAME%
   echo %DB_PASSWORD%
   
   # Linux/Mac
   echo $DB_URL
   echo $DB_USERNAME
   echo $DB_PASSWORD
   ```

### Issue: "Java version not compatible"

**Solution:**
```bash
java -version
# Should show Java 21 or higher

# If not, download from:
# https://www.oracle.com/java/technologies/downloads/#java21
```

### Issue: "Maven not found"

**Solution:**
```bash
# Use Maven wrapper instead (included in project)
./mvnw clean install        # Linux/Mac
mvnw.cmd clean install      # Windows
```

### Issue: "CSV file not found"

**Solution:**
The application auto-generates `data/cdr_data.csv` on first run.
If needed manually:
```bash
mkdir data
# File will be created on next run
```

---

## 📊 Monitoring & Debugging

### View SQL Queries
All SQL queries are logged to console (configured in `application.properties`):
```properties
spring.jpa.show-sql=true
```

### Check Batch Job Status
```bash
# After application starts, check database:
SELECT * FROM BATCH_JOB_EXECUTION;
SELECT * FROM BATCH_STEP_EXECUTION;
```

### Enable Debug Logging
Add to `application.properties`:
```properties
logging.level.org.springframework.batch=DEBUG
logging.level.com.example.telecometl=DEBUG
```

---

## 🔐 Security Notes

### IMPORTANT: Never Commit Credentials
- `.gitignore` excludes `application-local.properties`
- Environment variables are preferred for sensitive data
- Always use secure password managers for local development

### Safe Credential Practices
1. Use different passwords for development and production
2. Use environment variables or secure vaults
3. Never log credentials
4. Rotate passwords periodically

---

## 📚 Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Batch Documentation](https://spring.io/projects/spring-batch)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Maven Documentation](https://maven.apache.org/)

---

## 🤝 Contributing

1. Create a feature branch: `git checkout -b feature/your-feature`
2. Make changes and commit: `git commit -m "Add your feature"`
3. Push to GitHub: `git push origin feature/your-feature`
4. Create a Pull Request on GitHub

---

## 📞 Support

For issues or questions:
1. Check this guide first
2. Review README.md
3. Check SECURITY_SCAN_REPORT.md
4. Create an issue on GitHub

---

**Happy Coding! 🚀**
