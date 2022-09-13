
# Automatic Irrigation System
Automatic irrigation System, providing an interface for IoT device to interact with the sensor and managing plots processes
## Technology
* [Spring Boot 2.7.3](https://projects.spring.io/spring-boot/) - Inversion of Control Framework
* [Lombok](https://projectlombok.org/)
* **Mongodb**
* **Spring webflux**

## How To Use

To clone and run this application, you'll need [Git](https://git-scm.com), [Maven](https://maven.apache.org/), [Java 17](https://www.oracle.com/technetwork/java/javase/downloads/jdk17-downloads-5066655.html). 

```bash
# Clone this repository
$ git clone https://github.com/youssefagagg/automaticirrigationsystem

# Go into the repository, by changing to the directory where you have downloaded the project
$ cd automaticirrigationsystem

# To build the project and run it in a docker container execute the following command:
$  ./mvnw clean package -DskipTests && docker compose build &&  docker compose up -d
```
### the documentation for the rest api using Swagger UI:
- http://localhost:8080/openapi/swagger-ui.html
