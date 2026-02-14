# Car Sales Website System

## Overview
This is a Java web application for managing and selling cars online. The system supports multiple user roles (admin, staff, customer), car inventory management, order processing, promotions, authentication (including Google OAuth), and payment integration (VNPay). It uses the MVC pattern, DAO layer, and follows best practices for security and maintainability.

## Features
- User authentication and authorization (admin, staff, customer)
- Car listing, search, and details
- Shopping cart and checkout process
- Order and transaction management
- Promotions and discounts
- Email notifications (JavaMail)
- Google OAuth login
- VNPay payment integration
- Admin dashboard and management tools
- Logging (SLF4J, Logback)
- Rate limiting and security filters

## Technologies Used
- Java 17
- Jakarta Servlet API, JSP, JSTL
- Maven
- SQL Server (JDBC)
- HikariCP (connection pooling)
- BCrypt (password hashing)
- SLF4J & Logback (logging)
- JUnit & Mockito (testing)
- JavaMail

## Project Structure
```
CarSalesWebsiteSystem/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── controller/         # Servlets/controllers (admin, customer, staff)
│   │   │   ├── dao/                # Data access objects
│   │   │   ├── dto/                # Data transfer objects
│   │   │   ├── filter/             # Servlet filters (security, rate limiting, etc.)
│   │   │   ├── model/              # Entity classes
│   │   │   ├── service/            # Business logic
│   │   │   ├── test/               # Test classes
│   │   │   └── util/               # Utilities and configs
│   │   ├── resources/
│   │   │   ├── db.properties       # Database config
│   │   │   ├── google-oauth.properties # OAuth config
│   │   │   ├── logback.xml         # Logging config
│   │   │   └── templates/          # SQL scripts
│   │   └── webapp/
│   │       ├── images/             # Static images
│   │       ├── META-INF/
│   │       └── WEB-INF/            # web.xml, JSPs, etc.
├── pom.xml                         # Maven build file
```

## Setup & Running
1. **Clone the repository**
2. **Configure the database**
   - Edit `src/main/resources/db.properties` with your SQL Server credentials.
   - Run the SQL scripts in `src/main/resources/templates/` to set up the schema and initial data.
3. **Configure Google OAuth** (optional)
   - Edit `src/main/resources/google-oauth.properties` with your credentials.
4. **Build the project**
   - Run `mvnw clean package` (or `mvn clean package` if Maven is installed).
5. **Deploy**
   - Deploy the generated WAR file (`target/CarSalesWebsiteSystem-1.0-SNAPSHOT.war`) to your servlet container (e.g., Tomcat).
6. **Access the application**
   - Open your browser and go to the deployed URL (e.g., `http://localhost:8080/CarSalesWebsiteSystem`)

## Testing
- Unit and integration tests are in `src/main/java/test/`.
- Run tests with `mvnw test`.

## Authors
- Nguyễn Gia Huy

## License
-


