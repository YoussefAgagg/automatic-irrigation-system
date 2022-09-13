
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
$ git clone https://github.com/youssefagagg/automatic-irrigation-system

# Go into the repository, by changing to the directory where you have downloaded the project
$ cd automatic-irrigation-system

# To build the project and run it in a docker container execute the following command:
$  ./mvnw clean package -DskipTests && docker compose build &&  docker compose up -d
```
### the documentation for the rest api using Swagger UI:
- http://localhost:8080/openapi/swagger-ui.html

### working flow 
- to start irrigation you need to attach a **sensor** first to the **plot**
- if the **sensor** status is DOWN the system will try 3 times to call the sensor and after that make an alert on
- to simulate the **sensor** status changes a request to update a **sensor** by using th **PUT** method
- after the irrigation started you can end it by calling the endpoint **api/irrigate/end/{plotId}**
- in the root dir a postman collection requests you that tests  the api 
