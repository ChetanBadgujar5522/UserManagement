# User Management

This Spring Boot microservice provides a REST API for user registration, login, and basic user management, featuring JWT-based authentication and role-based authorization.

## Objective

- User registration and login
- JWT-based authentication
- Role-based authorization (ROLE_USER, ROLE_ADMIN)
- Global exception handling

## Tech Stack

- Java 11 (or 8+)
- Spring Boot
- Spring Security
- Spring Data JPA
- JJWT (for JSON Web Tokens)
- H2 (In-memory database) / MySQL (optional)
- Maven
- Lombok
- Springdoc OpenAPI (for API documentation)

## Prerequisites

- Java JDK 11 (or 8+) installed
- Maven installed
- An IDE like IntelliJ IDEA or Eclipse (optional, for development)
- Postman or cURL (for API testing)

## Setup and Running

1.  **Clone the repository:**
    ```bash
    git clone <your-repo-url>
    cd user-management-service
    ```

2.  **Configure `application.properties` (src/main/resources/):**
    *   Ensure `app.jwt.secret` is set to a strong, unique secret key.
    *   If using MySQL, uncomment and configure the MySQL datasource properties.

3.  **Build the project:**
    ```bash
    mvn clean install
    ```

4.  **Run the application:**
    ```bash
    mvn spring-boot:run
    ```
    Or, run the `UserManagementApplication.java` class from your IDE.

    The application will start on `http://localhost:8080`.

5.  **Access H2 Console (if using H2):**
    *   URL: `http://localhost:8080/h2-console`
    *   JDBC URL: `jdbc:h2:mem:userdb`
    *   User Name: `sa`
    *   Password: (leave blank)

6.  **Access Swagger UI (API Documentation):**
    *   URL: `http://localhost:8080/swagger-ui.html`

## API Endpoints

(Refer to Swagger UI for detailed interactive documentation)

### Authentication (`/auth`)

*   **Register a new user:**
    *   `POST /auth/register`
    *   Request Body:
      ```json
      {
          "email": "user@example.com",
          "password": "password123"
      }
      ```
    *   Response (Success 200 OK):
      ```json
      {
          "message": "User registered successfully!"
      }
      ```

*   **Login and get JWT token:**
    *   `POST /auth/login`
    *   Request Body:
      ```json
      {
          "email": "user@example.com",
          "password": "password123"
      }
      ```
    *   Response (Success 200 OK):
      ```json
      {
          "token": "eyJhbGciOiJI...",
          "type": "Bearer",
          "id": 1,
          "email": "user@example.com",
          "roles": ["ROLE_USER"]
      }
      ```

### User Management (`/users` - Secured with JWT)

*All `/users` endpoints require a valid JWT in the `Authorization: Bearer <token>` header.*

*   **Get all users (Admin only):**
    *   `GET /users`
    *   Required Role: `ROLE_ADMIN`

*   **Get user by ID (User or Admin):**
    *   `GET /users/{id}`
    *   Required Role: `ROLE_ADMIN` OR (`ROLE_USER` if `id` matches the authenticated user's ID).

*   **Delete user by ID (Admin only):**
    *   `DELETE /users/{id}`
    *   Required Role: `ROLE_ADMIN`

## Sample Flow (Postman/cURL)

1.  **Register:**
    ```bash
    curl -X POST -H "Content-Type: application/json" -d '{"email":"testuser@example.com","password":"password123"}' http://localhost:8080/auth/register

    # Register an admin (for manual testing, or adjust registration logic)
    # If using data.sql, this admin might already exist
    curl -X POST -H "Content-Type: application/json" -d '{"email":"admin@example.com","password":"adminpassword"}' http://localhost:8080/auth/register
    # (After this, you might need to manually add ROLE_ADMIN via H2 console if your registration doesn't handle it for specific users)
    ```

2.  **Login & Get Token:**
    ```bash
    curl -X POST -H "Content-Type: application/json" -d '{"email":"testuser@example.com","password":"password123"}' http://localhost:8080/auth/login
    # Copy the "token" value (e.g., YOUR_USER_TOKEN)

    curl -X POST -H "Content-Type: application/json" -d '{"email":"admin@example.com","password":"adminpassword"}' http://localhost:8080/auth/login
    # Copy the "token" value (e.g., YOUR_ADMIN_TOKEN)
    ```

3.  **Call Secured Endpoints:**

    *   **Get all users (as Admin):**
        ```bash
        curl -H "Authorization: Bearer YOUR_ADMIN_TOKEN" http://localhost:8080/users
        ```
    *   **Get specific user (as User - own details):**
        Assume user with ID 1 logged in:
        ```bash
        curl -H "Authorization: Bearer YOUR_USER_TOKEN" http://localhost:8080/users/1
        ```
    *   **Get specific user (as Admin):**
        ```bash
        curl -H "Authorization: Bearer YOUR_ADMIN_TOKEN" http://localhost:8080/users/1
        ```
    *   **Attempt to get all users (as User - should be Forbidden):**
        ```bash
        curl -H "Authorization: Bearer YOUR_USER_TOKEN" http://localhost:8080/users
        ```

## Project Structure

(Briefly describe the main packages and their purpose)
- `config`: Spring Security and OpenAPI configurations.
- `controller`: REST API controllers.
- `dto`: Data Transfer Objects for requests and responses.
- `entity`: JPA entities (User).
- `exception`: Global exception handling classes.
- `repository`: Spring Data JPA repositories.
- `security`: JWT utilities, UserDetails service, auth filter, entry point.
- `util`: Utility classes (e.g., custom security checks).

## Further Improvements (Optional)

- Refresh tokens.
- More granular roles and permissions.
- Email verification on registration.
- Password reset functionality.
- Comprehensive unit and integration tests.
- Dockerization.
