# Task For Senior Java Backend Developer

## Description

This is a Jakarta EE & Spring Boot application built with Java 21. It integrates Jakarta imports, Spring Data JPA, Spring MVC, Lombok, and more. The application also provides an embedded H2 database with a web console and an auto-generated API documentation using SpringDoc Swagger.

---

## Getting Started

### Prerequisites

Ensure the following software is installed on your system:

- **Java 21** (JDK)
- **Gradle 7.6+** (or higher)  
  *(If Gradle is not installed, you can also use the Gradle wrapper: `./gradlew` for Linux/Mac or `gradlew.bat` for Windows).*
- Any API testing tool (e.g., Postman, Curl) for testing endpoints.

---

### Steps to Run the Application

1. **Clone the repository:**
   ```bash
   git clone https://github.com/bennyindra/obssolution.git
   cd obssolution
   ```

2. **Build the project using Gradle:**
   ```bash
   ./gradlew build
   ```

3. **Run the application:**
   - Using Gradle:
     ```bash
     ./gradlew bootRun
     ```

4. The application will be available at:
   ```
   http://localhost:8080
   ```

---

### How to Access the SpringDoc Swagger UI

The application uses SpringDoc for generating API documentation. After running the application, you can access the Swagger UI at:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)


The Swagger page provides details about all available APIs, including endpoints, request/response structures, and example payloads.

---

### How to Open the H2 Database Console

1. Ensure the application is running.
2. Open the H2 database console in your browser:
   ```
   http://localhost:8080/h2-console
   ```
3. Use the following credentials to log in:
   - **JDBC URL:** `jdbc:h2:mem:testdb`  
     *(or the URL specified in your `application.properties`)*
   - **Username:** `sa`
   - **Password:** *(leave blank unless specified in `application.properties`)*

   > *Note: Configuration details for the H2 database (like username/password) are defined in the `application.properties` file.*

---

## Additional Information

### Useful Links
- **Swagger API Docs:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **H2 Console:** [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

### Troubleshooting
- If the H2 console does not load, ensure the `spring.h2.console.enabled=true` property is set in `application.properties`.
- For Swagger issues, verify that `springdoc.openapi` settings are correctly configured in `application.properties` if overridden.

---

## Author
P BENNY INDRA PURNAMA 
