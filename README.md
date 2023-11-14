# FamilyDOCS Document Management Application

## Overview

This Spring Boot application provides a RESTful API for managing documents with tagging functionality. It allows users to perform CRUD operations on documents and manage tags associated with them.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

- Java JDK 17 or later
- Gradle 7.x (Note: The project uses the Gradle Wrapper, so having Gradle installed is optional)
- Docker and Docker Compose
- An IDE such as IntelliJ IDEA, Eclipse, or Spring Tool Suite (optional, for development)

### Installing

1. Clone the repository to your local machine:
    ```
    git clone https://github.com/AlexNaron/familydocs
    ```
2. Navigate into the directory where the project is located.
3. Build the project using Gradle. You can use the Gradle Wrapper included in the project:
    ```
    ./gradlew build
    ```
or on Windows:
    ```
    gradlew.bat build
    ```
4. Start the application with Docker Compose:

    ```
    docker-compose up -d
    ```

This command starts all the necessary services including the database, MinIO server, and the application itself.

5. To stop the application and services, you can use:
    ```
    docker-compose down
    ```
   
6. The administrator is created at startup with the credentials admin:admin

## Development Notes

- The application is configured to start in a Docker environment, with all dependencies set up in the `docker-compose.yml` file.
- For development purposes, you can run the application directly from your IDE, but make sure the dependent services (like the database) are running, either in Docker or configured locally.
- Currently, the project is in a prototype/demo state and not finished.