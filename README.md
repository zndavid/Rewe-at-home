# Email Kafka microservices

`Email Kafka microservices` are a Spring-based Java applications designed to produce and consume emails from Kafka and forward their statistics for further processing.

## Features

- Produce email messages to a dedicated Kafka topic
- Consume email messages from a dedicated Kafka topic
- Forward email statistics to a specified endpoint for analytics

## Requirements

- Java 21
- Kafka
- Docker
- Maven

## Setting Up

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Installation

1. Clone the repository:
    ```bash
    git clone https://github.com/your_username/email-receiver-service.git
    ```
2. Navigate to docker directory:
    ```bash
    cd docker
    ```
3. Run the following command:
    ```bash
    docker compose up -d
    ```
4. Run each services

## API Documentation
`Swagger` is implemented on each microservice (where it is needed).

- [Email generator](http://localhost:8081/swagger-ui/index.html)
- [Email statistics](http://localhost:8081/swagger-ui/index.html)

<em>(PS: I haven't implemented @ControllerAdvice Global exception handlers)</em>

Postman collection is included in the `postman` folder