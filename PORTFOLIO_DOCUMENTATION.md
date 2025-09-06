# Portfolio Project Documentation

## Overview

This documentation provides a comprehensive overview of the Portfolio project, which consists of a Spring Boot backend and a Vue.js frontend. The project is designed to showcase professional work, skills, and experience in a modern, responsive web application with secure authentication.

## Project Structure

The project follows a microservices architecture with the following components:

```
Portfolio/
├── portfolio-service/     # Spring Boot backend service
├── portfolio-vue/         # Vue.js frontend application
├── config-server/         # Configuration server
└── gateway/               # API Gateway
```

## API Gateway

The API Gateway serves as the entry point for all client requests, routing them to the appropriate microservices.

### Configuration

```yaml
spring:
  cloud:
    gateway:
      globalcors:
        corsConfigurations:
          '[/**]':
            allowedOrigins: "http://localhost:5173"
            allowedMethods:
              - GET
              - POST
              - PUT
              - DELETE
              - OPTIONS
            allowedHeaders: "*"
            allowCredentials: true
            maxAge: 3600
```

### Features

- **Request Routing**: Routes requests to appropriate microservices
- **CORS Configuration**: Centralized CORS handling for all microservices
- **Load Balancing**: Distributes requests across multiple instances of services
- **Circuit Breaking**: Prevents cascading failures in the microservices architecture

## Backend (Spring Boot)

### Technology Stack

- **Framework**: Spring Boot 3.x
- **Build Tool**: Maven
- **Database**: PostgreSQL
- **Security**: OAuth2 with Keycloak
- **Documentation**: Swagger/OpenAPI

### Key Components

#### Models

1. **UserProfile**
   - Represents a user's professional profile
   - Contains personal information, contact details, and social media links

2. **Project**
   - Represents a portfolio project
   - Includes title, description, technologies, categories, and links

3. **Skill**
   - Represents a professional skill
   - Includes name, category, proficiency level, and years of experience

4. **Experience**
   - Represents work experience
   - Includes company, position, responsibilities, and technologies used

#### Controllers

1. **UserProfileController**
   - Manages user profile operations
   - Endpoints for creating, retrieving, and updating profiles

2. **ProjectController**
   - Manages portfolio projects
   - Endpoints for CRUD operations and filtering by category/technology

3. **SkillController**
   - Manages professional skills
   - Endpoints for CRUD operations and filtering by category/level

4. **ExperienceController**
   - Manages work experience
   - Endpoints for CRUD operations and retrieving current experiences

#### Security

- **SecurityConfig**: Configures OAuth2 resource server with JWT
- **KeycloakRoleConverter**: Converts Keycloak roles to Spring Security authorities

#### API Endpoints

| Endpoint | Method | Description | Access |
|----------|--------|-------------|--------|
| `/api/public/auth/register` | POST | Register a new user with validation and rollback | Public |
| `/api/public/profiles/{username}` | GET | Get public user profile | Public |
| `/api/admin/profiles/{id}` | GET | Get user profile by ID | Admin |
| `/api/admin/profiles` | POST | Create user profile | Admin |
| `/api/admin/profiles/{id}` | PUT | Update user profile | Admin |
| `/api/public/projects` | GET | Get all projects | Public |
| `/api/public/projects/featured` | GET | Get featured projects | Public |
| `/api/public/projects/category/{category}` | GET | Get projects by category | Public |
| `/api/public/projects/technology/{technology}` | GET | Get projects by technology | Public |
| `/api/public/projects/{id}` | GET | Get project by ID | Public |
| `/api/admin/projects` | POST | Create project | Admin |
| `/api/admin/projects/{id}` | PUT | Update project | Admin |
| `/api/admin/projects/{id}` | DELETE | Delete project | Admin |
| `/api/public/skills` | GET | Get all skills | Public |
| `/api/public/skills/category/{category}` | GET | Get skills by category | Public |
| `/api/public/skills/level/{level}` | GET | Get skills by proficiency level | Public |
| `/api/public/skills/{id}` | GET | Get skill by ID | Public |
| `/api/admin/skills` | POST | Create skill | Admin |
| `/api/admin/skills/{id}` | PUT | Update skill | Admin |
| `/api/admin/skills/{id}` | DELETE | Delete skill | Admin |
| `/api/public/experiences` | GET | Get all experiences | Public |
| `/api/public/experiences/current` | GET | Get current experiences | Public |
| `/api/public/experiences/{id}` | GET | Get experience by ID | Public |
| `/api/admin/experiences` | POST | Create experience | Admin |
| `/api/admin/experiences/{id}` | PUT | Update experience | Admin |
| `/api/admin/experiences/{id}` | DELETE | Delete experience | Admin |

### Performance Optimizations

The backend includes several optimizations to prevent N+1 query problems:

1. **Entity Graphs**
   - Named entity graphs for Project and Experience entities
   - Specifies which collections should be eagerly loaded

2. **Fetch Joins**
   - Custom repository methods with fetch joins
   - Eager loading of collections in a single query

3. **Batch Fetching**
   - Configured default_batch_fetch_size in application.yml
   - Allows Hibernate to fetch multiple collections in a single query

4. **Caching**
   - Spring Cache for frequently accessed data
   - Cache eviction on write operations

## Frontend (Vue.js)

### Technology Stack

- **Framework**: Vue.js 3 with Composition API
- **Build Tool**: Vite
- **State Management**: Pinia
- **Routing**: Vue Router
- **UI Components**: shadcn-vue
- **Authentication**: Keycloak integration

### Key Components

#### Authentication

1. **Keycloak Service**
   - Manages Keycloak instance and authentication state
   - Handles token management and refresh
   - Supports direct login with username/password

2. **Auth Store**
   - Manages authentication state using Pinia
   - Stores user information and tokens
   - Provides methods for login, logout, and token refresh

3. **Login/Register Views**
   - Custom forms for user authentication
   - Integration with Keycloak for secure authentication
   - Form validation and error handling

#### Layout

1. **Navbar**
   - Responsive navigation bar
   - Conditional rendering based on authentication state
   - User profile display and logout functionality

2. **App Layout**
   - Main application layout
   - Handles authentication initialization
   - Loading state during initialization

#### API Integration

1. **API Service**
   - Axios instance for API requests
   - Automatic token handling and refresh
   - Error handling for authentication failures

#### Error Handling & User Feedback

1. **Toast Notifications**
   - Vue-toastification for consistent user feedback
   - Success and error notifications
   - Configurable display duration and styling

2. **Form Validation**
   - Client-side validation with clear error messages
   - Server-side validation with detailed feedback
   - Field-specific error highlighting

3. **Transaction-like Operations**
   - Rollback mechanisms for multi-step processes
   - Consistent error state management
   - Detailed error logging

### Routes

| Route | Component | Description | Access |
|-------|-----------|-------------|--------|
| `/` | HomeView | Landing page | Public |
| `/login` | LoginView | Login form | Guest only |
| `/register` | RegisterView | Registration form | Guest only |

## Authentication Flow

1. **Initialization**
   - App initializes Keycloak on mount
   - Checks for existing session
   - Loads user profile if authenticated

2. **Registration Process**
   - User fills out registration form with personal details
   - Form validation ensures data meets requirements
   - Form data sent to backend registration endpoint
   - Backend communicates with Keycloak Admin API in a transaction-like process:
      1. Create user in Keycloak
      2. Assign the "manager" role to the user
      3. If any step fails, the user is deleted to maintain consistency
   - Toast notifications provide feedback on success or failure
   - User automatically logged in after successful registration

3. **Login Process**
   - User enters credentials in custom form
   - Credentials sent to Keycloak token endpoint
   - Tokens stored and user profile loaded

4. **Token Management**
   - Automatic token refresh before expiration
   - Token included in API requests
   - Redirect to login on authentication failure

5. **Logout Process**
   - Clears local authentication state
   - Redirects to Keycloak logout endpoint
   - Returns to application home page

## Configuration

### Backend Configuration

```yaml
# application.yml
spring:
  application:
    name: portfolio-service

  config:
    import: optional:configserver:http://localhost:8888

  jpa:
    properties:
      hibernate:
        default_batch_fetch_size: 30
```

### Frontend Configuration

```
# .env
VITE_KEYCLOAK_URL=http://localhost:8080
VITE_KEYCLOAK_REALM=PortfolioRealm
VITE_KEYCLOAK_CLIENT_ID=portfolio-frontend
VITE_API_URL=http://localhost:9090/api

# Keycloak admin credentials for user registration
VITE_KEYCLOAK_ADMIN_USERNAME=admin
VITE_KEYCLOAK_ADMIN_PASSWORD=admin
```

## Keycloak Configuration

1. **Realm**: PortfolioRealm
2. **Client**: portfolio-frontend
3. **Access Type**: public
4. **Valid Redirect URIs**: http://localhost:5173/*
5. **Web Origins**: http://localhost:5173
6. **Direct Access Grants**: Enabled (for username/password login)
7. **Roles**:
   - "manager": Required role for portfolio users
   - "admin": Administrative role with elevated permissions

## Next Steps

1. **Portfolio Pages**
   - Implement project showcase
   - Create skills and experience sections
   - Add contact information

2. **Admin Dashboard**
   - Create admin interface for content management
   - Implement CRUD operations for all entities

3. **Deployment**
   - Configure production environment
   - Set up CI/CD pipeline
   - Deploy to cloud provider

## Development Log

### [Date: 2024-04-17] Initial Implementation
- Created Spring Boot backend with models, repositories, services, and controllers
- Implemented security configuration with Keycloak integration
- Added performance optimizations to prevent N+1 query problems
- Created Vue.js frontend with authentication flow
- Implemented custom login and registration forms
- Set up API service with token handling

### [Date: 2024-04-17] User Registration Implementation
- Added backend registration endpoint for user management
- Implemented user registration service in Spring Boot
- Connected registration form to backend API
- Added automatic login after successful registration
- Enhanced login page with registration success message
- Removed CORS configuration from portfolio service
- Centralized CORS configuration at the API gateway level

### [Date: 2024-04-17] Registration Improvements
- Implemented transaction-like user registration with rollback capability
- Added validation for registration form fields
- Integrated vue-toastification for user feedback
- Enhanced error handling in both frontend and backend
- Added user deletion on failed role assignment
- Improved error messages and logging

## Conclusion

The Portfolio project provides a solid foundation for a professional portfolio website with secure authentication and comprehensive API. The backend offers a robust set of endpoints for managing portfolio content, while the frontend provides a clean, modern user interface with secure authentication.

---

*This documentation will be updated as the project progresses.*
