# University Management System (UMS)

**ALWAYS follow these instructions first and fallback to search or bash commands only when you encounter unexpected information that does not match the info here.**

The Univer repository contains a University Management System built as a collection of Spring Boot microservices with MySQL databases, Flyway migrations, and Kafka messaging.

## Critical Repository Information

**IMPORTANT**: The main codebase is on the `feat/ums-scaffold` branch, NOT the main branch. The main branch only contains a minimal README.md file.

Always switch to the correct branch before working:
```bash
git fetch --all
git checkout feat/ums-scaffold
```

## Working Effectively

### Prerequisites and Environment Setup
- Install Java 17 or higher: `sudo apt update && sudo apt install openjdk-17-jdk -y`
- Install MySQL Server: `sudo apt install mysql-server -y`
- Install Maven: `sudo apt install maven -y`
- Verify installations:
  - `java -version` -- should show Java 17+
  - `mvn -version` -- should show Maven 3.6+
  - `mysql --version` -- should show MySQL 8.0+

### Database Setup
- Start MySQL service: `sudo systemctl start mysql`
- Set up MySQL root password for development:
  ```bash
  sudo mysql --defaults-file=/etc/mysql/debian.cnf -e "ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'root@1234';"
  sudo mysql --defaults-file=/etc/mysql/debian.cnf -e "FLUSH PRIVILEGES;"
  ```
- Test connection: `mysql -u root -proot@1234 -e "SHOW DATABASES;"`
- Create databases for each service (they are auto-created via connection URLs with `createDatabaseIfNotExist=true`)
- **NEVER CANCEL**: Database initialization can take 5-10 minutes depending on system. Set timeout to 15+ minutes.

### Environment Configuration
- Copy and configure environment variables: `cp .env.local .env` (if .env.local exists)
- Key environment variables in .env.local:
  - `DB_USERNAME=root`
  - `DB_PASSWORD=root@1234`
  - Database URLs for each service (ums_admin, ums_academic, ums_teacher, ums_finance, ums_notification)
  - `JWT_SECRET=dev-secret-change-me`
  - `KAFKA_BOOTSTRAP_SERVERS=localhost:9092` (optional for development)

### Building the Application
Since this is a multi-module Maven project with individual service directories:

1. **Build all services**: 
   ```bash
   cd studentService && mvn clean install -DskipTests
   cd ../adminService && mvn clean install -DskipTests  
   cd ../academicService && mvn clean install -DskipTests
   cd ../teacherService && mvn clean install -DskipTests
   cd ../finanaceService && mvn clean install -DskipTests
   cd ../notificationService && mvn clean install -DskipTests
   cd ../securityServices && mvn clean install -DskipTests
   ```
   **NEVER CANCEL**: Each service build takes 2-5 minutes. Total build time: 15-30 minutes. Set timeout to 45+ minutes.

2. **Alternative individual service build**:
   ```bash
   cd [serviceName] && mvn clean install
   ```

### Running Tests
Run tests for each service individually:
```bash
cd studentService && mvn test
cd ../adminService && mvn test
# ... repeat for other services
```
**NEVER CANCEL**: Test suites can take 3-5 minutes per service. Set timeout to 10+ minutes per service.

### Running the Application

#### Individual Service Startup
Each service runs on a different port:
- studentService: port 8082
- adminService: port 8080 (typically)
- academicService: port 8083 (typically)
- teacherService: port 8084 (typically)
- Other services: check application.yml in each service

**Start each service**:
```bash
# Terminal 1 - Student Service
cd studentService && mvn spring-boot:run

# Terminal 2 - Admin Service  
cd adminService && mvn spring-boot:run

# Terminal 3 - Academic Service
cd academicService && mvn spring-boot:run

# ... continue for other services as needed
```

**NEVER CANCEL**: Service startup takes 30-60 seconds per service. Flyway migrations run on first startup and can take 2-3 minutes. Set timeout to 5+ minutes per service.

#### Database Migration Validation
Each service uses Flyway for database migrations. On first startup:
- Check logs for "Flyway migration completed successfully" or similar
- Verify database tables are created: `mysql -u root -p[password] -e "SHOW TABLES;" [database_name]`

## Validation Scenarios

### End-to-End Validation Steps
**ALWAYS run these validation steps after making changes**:

1. **Database Connection Test**:
   ```bash
   mysql -u root -proot@1234 -e "SHOW DATABASES;" | grep ums_
   ```
   Should show: ums_admin, ums_academic, ums_teacher, ums_finance, ums_notification (after services start)

2. **Service Health Check**:
   ```bash
   curl -f http://localhost:8082/actuator/health  # Student Service
   curl -f http://localhost:8080/actuator/health  # Admin Service (typical port)
   ```
   Note: `-f` flag makes curl fail on HTTP error codes

3. **API Functionality Test**:
   ```bash
   # Test student admission endpoint (may require authentication setup)
   curl -X POST http://localhost:8082/api/students/admissions/apply \
     -H "Content-Type: application/json" \
     -d '{"program":"Computer Science"}' \
     -w "\nHTTP Status: %{http_code}\n"
   ```

4. **Database Schema Validation** (after service startup):
   ```bash
   mysql -u root -proot@1234 ums_student -e "DESCRIBE students; DESCRIBE admissions; DESCRIBE enrollments;" 2>/dev/null
   ```

5. **Environment Validation**:
   ```bash
   # Verify Java version
   java -version | head -1
   # Verify Maven version  
   mvn -version | head -1
   # Verify MySQL is running
   sudo systemctl is-active mysql
   ```

### Manual Testing Scenarios
- **Student Registration Flow**: Test creating student, applying for admission, enrollment
- **Admin Operations**: Test administrative functions via admin service endpoints  
- **Cross-Service Communication**: Verify services can communicate if inter-service calls exist
- **Authentication**: Test JWT token validation if security is enabled

## Service Architecture

### Core Services
- **studentService** (port 8082): Student management, admissions, enrollments
- **adminService**: Administrative functions and user management
- **academicService**: Academic records, courses, grades
- **teacherService**: Teacher management and course assignments
- **finanaceService**: Financial operations, billing, payments
- **notificationService**: Notifications and messaging
- **securityServices**: Authentication and authorization

### Database Schema
Each service has its own MySQL database:
- **ums_student**: students, admissions, enrollments tables
- **ums_admin**: administrative data
- **ums_academic**: courses, grades, academic records  
- **ums_teacher**: teacher information and assignments
- **ums_finance**: billing and payment records
- **ums_notification**: notification logs and templates

### Configuration Files
- **application.yml** in each service: Service-specific configuration
- **.env.local**: Shared environment variables for local development
- **Flyway migrations**: Located in `src/main/resources/db/migration/` per service

## Common Development Tasks

### Adding New Features
1. Identify the appropriate service for your feature
2. Add/modify controller in `src/main/java/org/ums/web/`
3. Add service logic in `src/main/java/org/ums/service/`
4. Add data models in `src/main/java/org/ums/model/`
5. Add repository interfaces in `src/main/java/org/ums/repo/`
6. Create Flyway migration if database changes needed: `src/main/resources/db/migration/V[number]__[description].sql`

### Database Changes
1. Create new Flyway migration file: `V[next_number]__[description].sql`
2. Place in `src/main/resources/db/migration/` of appropriate service
3. Restart service to apply migration
4. **NEVER CANCEL**: Migration application can take 30 seconds to 2 minutes

### Debugging
- Check service logs for Spring Boot startup messages
- Verify database connectivity in application.yml
- Check Flyway migration status in database: `SELECT * FROM flyway_schema_history;`
- Use Spring Boot Actuator endpoints for health checks: `/actuator/health`

## Quick Setup Script

For rapid environment setup, use this validated sequence:

```bash
# Quick environment validation and setup
echo "=== University Management System Setup ==="
echo "Step 1: Checking prerequisites..."
java -version | head -1
mvn -version | head -1
mysql --version

echo "Step 2: Starting MySQL..."
sudo systemctl start mysql
sudo systemctl is-active mysql

echo "Step 3: Setting up MySQL root password..."
sudo mysql --defaults-file=/etc/mysql/debian.cnf -e "ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'root@1234';"
sudo mysql --defaults-file=/etc/mysql/debian.cnf -e "FLUSH PRIVILEGES;"

echo "Step 4: Testing database connection..."
mysql -u root -proot@1234 -e "SHOW DATABASES;" | head -5

echo "Step 5: Switch to correct branch..."
git fetch --all 2>/dev/null || echo "Note: fetch failed, continuing..."
git checkout feat/ums-scaffold 2>/dev/null || echo "Already on correct branch or branch not available"

echo "Setup complete! Now you can build and run services."
```

**NEVER CANCEL**: This setup script takes 2-3 minutes. Set timeout to 5+ minutes.

## Performance Measurement Commands

Use these to time operations and validate timeout settings:

```bash
# Time individual service builds
time (cd studentService && mvn clean install -DskipTests)

# Time service startup
time (cd studentService && timeout 300 mvn spring-boot:run) &

# Monitor service startup logs
tail -f studentService/logs/application.log  # if logging to file

# Check memory usage during build
watch -n 5 'free -h && ps aux | grep maven | grep -v grep'
```

### Branch-Related Issues
- **"No such file or directory" when looking for services**: Ensure you're on the `feat/ums-scaffold` branch
  ```bash
  git branch  # Check current branch
  git fetch --all  # Fetch all branches
  git checkout feat/ums-scaffold  # Switch to correct branch
  ```
- **Empty repository appearance**: The main branch only contains README.md. Always work on `feat/ums-scaffold`
- **Git checkout failures**: If remote branch doesn't exist locally:
  ```bash
  git remote -v  # Verify remote is set
  git fetch origin feat/ums-scaffold
  git checkout -b feat/ums-scaffold origin/feat/ums-scaffold
  ```

### Build Failures
- Ensure Java 17+ is installed and in PATH: `java -version | head -1`
- Verify Maven is properly installed: `mvn -version | head -1`
- Check for missing dependencies in pom.xml files
- Clean and rebuild: `mvn clean install`
- **Each service directory must have its own pom.xml file** - verify structure matches instructions

### Database Connection Issues  
- Verify MySQL is running: `sudo systemctl is-active mysql`
- Check database URLs in .env.local match your MySQL setup
- Verify username/password are correct: `mysql -u root -proot@1234 -e "SHOW DATABASES;"`
- Ensure databases exist or can be auto-created
- **Connection refused errors**: Check if MySQL service started properly: `sudo systemctl status mysql`

### Service Startup Issues
- Check port conflicts (each service needs unique port)
- Verify application.yml configuration in each service
- Check for missing environment variables in .env.local
- Review startup logs for specific error messages
- **"Port already in use" errors**: Kill existing processes or use different ports

### Performance Notes
- **Build Time**: 15-30 minutes for all services
- **Test Time**: 20-35 minutes for all services  
- **Startup Time**: 2-5 minutes per service (including migrations)
- **Database Setup**: 5-10 minutes initial setup

**NEVER CANCEL long-running operations** - use appropriate timeouts and wait for completion.

## Frequently Referenced Files

### Repository Structure
```
univer/
├── .env.local                 # Environment variables
├── pom.xml                   # Root Maven configuration
├── studentService/
│   ├── src/main/java/org/ums/
│   │   ├── web/              # REST controllers
│   │   ├── service/          # Business logic
│   │   ├── model/            # JPA entities
│   │   └── repo/             # Repository interfaces
│   └── src/main/resources/
│       ├── application.yml   # Service configuration
│       └── db/migration/     # Flyway migrations
├── adminService/
│   └── [similar structure]
├── academicService/
│   └── [similar structure]
└── [other services with similar structure]
```

Always reference this instruction file first to avoid unnecessary exploration or incorrect commands.