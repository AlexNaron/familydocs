# FamilyDOCS Document Management Application

## Overview

This Spring Boot application provides a RESTful API for managing documents with tagging functionality. It allows users to perform CRUD operations on documents and manage tags associated with them. Authentication is handled via Spring Security, ensuring that users can only access and modify their documents.

## Features

- **User Authentication:** Utilizes Spring Security for handling user authentication.
- **Document CRUD:** Users can create, retrieve, update, and delete documents.
- **Tag Management:** Users can add and remove tags from their documents.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

- Java JDK 17 or later
- Maven 4.0
- An IDE such as IntelliJ IDEA, Eclipse, or Spring Tool Suite

### Installing

1. Clone the repository to your local machine:
   ```
   git clone https://github.com/your-username/your-repo-name.git
   ```
2. Navigate into the directory where the project is located.
3. Build the project using Maven:
   ```
   mvn clean install
   ```
4. Start docker:
   ```
   docker-compose up -d
   ```
5. Start the application inside an IDE

## Development Notes

- Currently, the project is not in finished state and is rather a prototype/demo.
