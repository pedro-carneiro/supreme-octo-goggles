# Recipe example service

This is an example service, that was made using Spring Boot and Kotlin. The build system uses Maven, and the storage is
PostgreSQL. Database changes are applied using liquibase.

## Requirements

- JDK (version 21 or newer)
- Docker desktop

## Build

To build the project, use the provided Maven wrapper. Docker desktop is required to run the tests. 

```bash
./mvnw clean install
```

## Run locally

This sets up a container running PostgreSQL to serve as storage (please enable liquibase in
[application.yml](src/main/resources/application.yml)).

```bash
docker compose up -d && ./mvnw clean package spring-boot:run
```
