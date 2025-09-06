# Micro-service Spring Boot Based Portfolio

A developer portfolio platform built with Spring Boot microservices, Keycloak authentication, and a Vue.js frontend.

## Table of Contents

- [Project Overview](#project-overview)
- [Architecture](#architecture)
- [Technologies Used](#technologies-used)
- [Directory Structure](#directory-structure)
- [Keycloak Authentication Setup](#keycloak-authentication-setup)
- [Getting Started](#getting-started)
- [Running the Project](#running-the-project)
- [API Documentation](#api-documentation)
- [Contributing](#contributing)
- [License](#license)

## Project Overview

This project demonstrates a microservice-based architecture for a developer portfolio. It includes multiple Spring Boot services, a gateway, service discovery, Keycloak authentication, and a Vue.js frontend. The backend exposes REST APIs for managing user profiles, projects, skills, and experiences, while the frontend provides a modern, responsive UI.

## Architecture

- **config-server**: Centralized configuration management.
- **eureka-discovery**: Service registry for microservices.
- **gateway**: API gateway for routing, CORS, and security.
- **contact-service**: Manages contact information.
- **portfolio-service**: Handles portfolio data and business logic.
- **portfolio-vue**: Frontend built with Vue.js.
- **Keycloak**: Authentication and authorization provider.

## Technologies Used
- **Java 17+** (Spring Boot 3.x, Spring Cloud)
- **Maven**
- **PostgreSQL**
- **Keycloak** (OAuth2, JWT)
- **Vue.js 3** (Vite, Pinia, Vue Router)
- **Node.js & npm**

## Directory Structure

```
Portfolio/
├── config-server/
├── contact-service/
├── eureka-discovery/
├── gateway/
├── portfolio-service/
├── portfolio-vue/
├── PORTFOLIO_DOCUMENTATION.md
├── README.md
└── ...
```

## Keycloak Authentication Setup

This project uses Keycloak for authentication and role-based access control. Follow these steps to set up Keycloak:

1. **Install and Run Keycloak**
   - Download Keycloak from [keycloak.org](https://www.keycloak.org/downloads).
   - Start Keycloak:
     ```sh
     cd /path/to/keycloak
     bin/kc.sh start-dev
     ```
   - Access the admin console at `http://localhost:8080/admin`.

2. **Create Realm and Client**
   - Create a new Realm: `PortfolioRealm`.
   - Create a new Client: `portfolio-frontend`.
     - Access Type: `public`
     - Valid Redirect URIs: `http://localhost:5173/*`
     - Web Origins: `http://localhost:5173`
     - Enable Direct Access Grants

3. **Create Roles**
   - Add roles: `manager`, `admin`.

4. **Create Users**
   - Add users and assign roles as needed.

5. **Configure Environment Variables**
   - In `portfolio-vue/.env`:
     ```
     VITE_KEYCLOAK_URL=http://localhost:8080
     VITE_KEYCLOAK_REALM=PortfolioRealm
     VITE_KEYCLOAK_CLIENT_ID=portfolio-frontend
     VITE_API_URL=http://localhost:9090/api
     VITE_KEYCLOAK_ADMIN_USERNAME=admin
     VITE_KEYCLOAK_ADMIN_PASSWORD=admin
     ```

6. **Backend Configuration**
   - In `portfolio-service/src/main/resources/application.yml`:
     ```yaml
     spring:
       security:
         oauth2:
           resourceserver:
             jwt:
               issuer-uri: http://localhost:8080/realms/PortfolioRealm
     ```

7. **Test Authentication**
   - Start backend and frontend services.
   - Register and log in via the frontend.
   - Confirm protected endpoints require authentication.

## Getting Started

### Prerequisites
- Java 17+
- Maven
- Node.js & npm
- Keycloak (see above)

### Clone the Repository

```sh
# Clone the repo
git clone https://github.com/Paul-Ayesiga/Micro-service-Springboot-based-portfolio.git
cd Micro-service-Springboot-based-portfolio
```

### Backend Setup

```sh
cd config-server && mvn clean install
cd ../eureka-discovery && mvn clean install
cd ../gateway && mvn clean install
cd ../contact-service && mvn clean install
cd ../portfolio-service && mvn clean install
```

Start services in order:
- config-server
- eureka-discovery
- gateway
- contact-service
- portfolio-service

Example:
```sh
cd config-server && mvn spring-boot:run
```

### Frontend Setup

```sh
cd portfolio-vue
npm install
npm run dev
```

## Running the Project

- Access the frontend at `http://localhost:5173` (default Vite port).
- Backend services run on their respective ports (see each service's `application.yml` or `application.properties`).
- Keycloak runs at `http://localhost:8080`.

## API Documentation

- Each microservice exposes REST APIs.
- See [`PORTFOLIO_DOCUMENTATION.md`](./PORTFOLIO_DOCUMENTATION.md) for detailed endpoints, models, and technical details.

## Contributing

1. Fork the repository.
2. Create your feature branch (`git checkout -b feature/YourFeature`).
3. Commit your changes.
4. Push to the branch.
5. Open a pull request.

## License

This project is licensed under the MIT License.

---

For more details, see the documentation files in the repository.
