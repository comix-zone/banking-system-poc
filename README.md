# Project Setup and Microservices Guide

This repository contains a multi-module project with the following components:

- **account**: Handles account-related functionality.
- **common-events**: A shared library for managing events.
- **notification**: Handles notifications and messaging.
- **transaction**: Handles financial transactions.
- **project-setup**: Initial setup required to configure the project.

## Prerequisites

Before starting, ensure you have the following installed on your system:

- [Docker](https://www.docker.com/) and [Docker Compose](https://docs.docker.com/compose/)
- Java (version 21 or higher)
- Maven (for building Java projects 3.9.9)

## Project Setup Instructions

The `project-setup` module contains essential initialization scripts and a `docker-compose.yml` file for setting up the required infrastructure. **Run this first before starting any other microservices.**

### Steps to Set Up the Project prerequisites

1. **Navigate to the `project-setup` directory:**
2. **Execute** ```docker-compose up -d```

Docker will :
1. Pull postgres image and setup Account/Transaction/Notification databases with their respective init.sql scripts
2. Pull images for kafka and zookeeper

After docker completion:
1. ``cd common-events``
2. ``mvnw clean install``
3. ``cd <microservice-directory>``
4. ``mvnw clean install``
5. ``mvnw spring-boot:run``

All of services should be up and running and you should be able to access open api config
1. [account](http://localhost:8080/swagger-ui/index.html)
2. [transaction](http://localhost:8081/swagger-ui/index.html)

At the end there is [postman collection](/postman-collection/) with basic api example calls
and basic [figma diagram](https://www.figma.com/design/jteGsz9LGiju5XXkelsyac/APACHE-PROJECT-LOGOS-%26-ICONS-v.1.0-(Community)?node-id=1304-30533&node-type=canvas&t=oIwUFEsvOsGinAPb-0) of whole system overview 
