# ETL Pipeline for Telecom Data

A Spring Boot ETL (Extract, Transform, Load) application for processing telecom CDR (Call Detail Records) data.

## Features

- **Extract**: Reads CSV CDR data from flat files
- **Transform**: Cleans and validates call records, applies business logic for pricing
- **Load**: Bulk inserts processed records into PostgreSQL database
- Fault tolerance with skip limits
- Batch processing with configurable chunk sizes
- Automatic data validation and error handling

## Prerequisites

- Java 21 or higher
- Maven 3.6+
- PostgreSQL 12+

## Setup Instructions

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/ETL-Pipeline-for-Telecom-Data.git
cd ETL-Pipeline-for-Telecom-Data
```

### 2. Configure Database Connection

Create a local configuration file with your database credentials:

```bash
cp src/main/resources/application.properties.example src/main/resources/application-local.properties
```

Edit `application-local.properties` with your database details:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/telecom_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Set Environment Variables (Alternative Method)

Instead of modifying properties files, you can set environment variables:

```bash
# Windows (cmd)
set DB_URL=jdbc:postgresql://localhost:5432/telecom_db
set DB_USERNAME=postgres
set DB_PASSWORD=your_password

# Linux/Mac (bash)
export DB_URL=jdbc:postgresql://localhost:5432/telecom_db
export DB_USERNAME=postgres
export DB_PASSWORD=your_password
```

### 4. Build the Project
```bash
mvn clean install
```

### 5. Run the Application
```bash
mvn spring-boot:run
```

Or build and run the JAR:
```bash
mvn package
java -jar target/telecom-etl-0.0.1-SNAPSHOT.jar
```

## Project Structure

```
src/
├── main/
│   ├── java/com/example/telecometl/
│   │   ├── TelecomEtlApplication.java       # Main application
│   │   ├── config/BatchConfig.java          # Spring Batch configuration
│   │   ├── entity/
│   │   │   ├── CallRecord.java              # JPA Entity
│   │   │   └── CallRecordDto.java           # DTO for CSV mapping
│   │   └── repository/
│   │       └── CallRecordRepository.java    # Data access layer
│   └── resources/
│       ├── application.properties           # Default properties
│       └── application.properties.example   # Example configuration
└── test/
    └── java/com/example/telecometl/
        └── TelecomEtlApplicationTests.java
```

## Data Format

The application expects CSV files in the following format:

```csv
callerNumber,receiverNumber,durationSeconds,callType,callDate
0660001234,0669991234,120,LOCAL,2026-03-02
0660005678,0669995678,300,INTERNATIONAL,2026-03-02
```

## Business Logic

- **Call Pricing**:
  - Local calls: 1 MAD per second
  - International calls: 5 MAD per second
- **Data Validation**:
  - Missed calls (0 duration) are filtered out
  - Invalid records are logged and skipped

## Configuration Options

Key Spring Batch settings in `application.properties`:

```properties
spring.datasource.url          # Database connection URL
spring.datasource.username     # Database username
spring.datasource.password     # Database password
spring.jpa.hibernate.ddl-auto  # Schema generation strategy (update/create/none)
spring.jpa.show-sql            # Log SQL queries
spring.batch.jdbc.initialize-schema # Initialize batch tables
```

## Security Notes

- **Sensitive Information**: Database credentials are NOT committed to the repository
- **Environment Variables**: Use `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` environment variables
- **Local Configuration**: Use `application-local.properties` for local development (not tracked by Git)
- See `.gitignore` for excluded files

## Troubleshooting

### Database Connection Error
- Verify PostgreSQL is running
- Check database credentials in environment variables or configuration file
- Ensure database exists: `CREATE DATABASE telecom_db;`

### Data File Not Found
- Ensure `data/cdr_data.csv` exists in the project root
- The application auto-generates test data on startup

## License

MIT License

## Support

For issues or questions, please create an issue in the repository.
