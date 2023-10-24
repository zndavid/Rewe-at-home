# Email Kafka Microservices

The `Email Kafka microservices` project consists of Spring-based Java applications that interact with email operations via Kafka messaging. These microservices are tasked with the production, consumption, and forwarding of email messages and their statistics for analytics processing.

## Features

* Producing email messages to a dedicated Kafka topic.
* Consuming email messages from a specific Kafka topic.
* Forwarding email statistics to a particular endpoint for analysis.

## Prerequisites

Before you begin, ensure you have the following software installed:

* Java 21
* Kafka
* Docker
* Maven

## Getting Started

These instructions will guide you in obtaining a copy of the project and running it on your local machine for development and testing purposes.

### Installation

1. Clone the GitHub repository:
    ```shell
    git clone https://github.com/zndavid/Rewe-at-home.git
    ```

2. Change to the docker directory:
    ```shell
    cd Rewe-at-home/docker
    ```

3. Launch the services with Docker Compose:
    ```shell
    docker compose up -d
    ```

4. After setting up the infrastructure, you can start each of the microservices.

5. Update your system's hosts file to resolve the Kafka service's hostname locally. You need to edit the hosts file with superuser privileges:
    ```shell
    sudo nano /etc/hosts
    ```
   And then add this line:
    ```
    127.0.0.1 kafka
    ```

## API Documentation

The project utilizes Swagger for the live documentation of the REST APIs, which is instrumental in comprehending and testing the functionalities provided by the microservices.

* [Email generator Swagger UI](http://localhost:8081/swagger-ui/index.html)
* [Email statistics Swagger UI](http://localhost:8081/swagger-ui/index.html)

> **Note:** The microservices are currently not implementing global exception handling with `@ControllerAdvice`.

### Postman Collection

For easier endpoint testing, a Postman collection is available in the `postman` directory of the project repository.

---

By following these instructions, you prepare your development environment for the Email Kafka microservices, understanding the role and functionality of each component for efficient and hassle-free operations.
