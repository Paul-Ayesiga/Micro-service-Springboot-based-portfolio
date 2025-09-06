# Micro-service Spring Boot Based Portfolio

A developer portfolio platform built with Spring Boot microservices and a Vue.js frontend.

## Table of Contents

- [Project Overview](#project-overview)
- [Architecture](#architecture)
- [Technologies Used](#technologies-used)
- [Directory Structure](#directory-structure)
- [Getting Started](#getting-started)
- [Running the Project](#running-the-project)
- [API Documentation](#api-documentation)
- [Contributing](#contributing)
- [License](#license)

## Project Overview

This project demonstrates a microservice-based architecture for a developer portfolio. It includes multiple Spring Boot services, a gateway, service discovery, and a Vue.js frontend.

## Architecture

- **config-server**: Centralized configuration management.
- **eureka-discovery**: Service registry for microservices.
- **gateway**: API gateway for routing and security.
- **contact-service**: Manages contact information.
- **portfolio-service**: Handles portfolio data.
- **portfolio-vue**: Frontend built with Vue.js.

## Technologies Used
- **Java (Springboot)
- **Vuejs

## Getting Started

### Prerequisites

- Java 17+
- Maven
- Node.js & npm

### Clone the Repository

```sh
git clone https://github.com/Paul-Ayesiga/Micro-service-Springboot-based-portfolio.git
cd Micro-service-Springboot-based-portfolio

 cd config-server && mvn clean install
 cd ../eureka-discovery && mvn clean install
 cd ../gateway && mvn clean install
 cd ../contact-service && mvn clean install
 cd ../portfolio-service && mvn clean install

Start services in order:
config-server
eureka-discovery
gateway
contact-service
portfolio-service

forexample
 cd config-server && mvn spring-boot:run
```

## Frontend Setup
1. Install dependencies
  ```sh
     cd portfolio-vue
     npm install
     npm run dev
```

### Running the Project
Access the frontend at http://localhost:5173 (default Vite port).
Backend services run on their respective ports (see each service's application.yml or application.properties).

### API Documentation
Each microservice exposes REST APIs.
See PORTFOLIO_DOCUMENTATION.md for detailed endpoints and usage.

### Contributing
Fork the repository.
Create your feature branch (git checkout -b feature/YourFeature).
Commit your changes.
Push to the branch.
Open a pull request.



