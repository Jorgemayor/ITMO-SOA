# Second Service

A Spring Boot REST API application using Jetty as the embedded server instead of the default Tomcat.

## Features

- **Spring MVC REST**: RESTful web services with Spring MVC
- **Jetty Server**: Uses Jetty embedded server instead of Tomcat
- **Two Main Endpoints**: 
  - `DataController`: Handles data retrieval operations
  - `OperationController`: Handles operation submission and processing
- **External API Integration**: Service layer to call external APIs
- **RestTemplate**: Configured for HTTP client operations

## Prerequisites

- Java 17 or higher
- Maven 3.6+

## Project Structure

```
second-service/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/restservice/
│   │   │       ├── Application.java              # Main application class
│   │   │       ├── config/
│   │   │       │   └── RestTemplateConfig.java   # RestTemplate configuration
│   │   │       ├── controller/
│   │   │       │   ├── DataController.java       # First endpoint
│   │   │       │   └── OperationController.java  # Second endpoint
│   │   │       ├── service/
│   │   │       │   └── ExternalApiService.java   # External API calls
│   │   │       └── model/
│   │   │           └── ApiResponse.java          # Response model
│   │   └── resources/
│   │       └── application.properties            # Configuration
│   └── test/
│       └── java/
├── pom.xml
└── README.md
```

## Configuration

Edit `src/main/resources/application.properties` to configure:

```properties
# Server port
server.port=8080

# External API base URL (update with your actual API)
external.api.base-url=http://localhost:9090/api
external.api.timeout=5000
```

## Building the Application

```bash
mvn clean package
```

## Running the Application

```bash
mvn spring-boot:run
```

Or run the JAR directly:

```bash
java -jar target/second-service-1.0.0.jar
```

The application will start on `http://localhost:8080`

## API Endpoints

### DataController Endpoints

#### 1. Fetch Data
```bash
GET http://localhost:8080/api/data/fetch
GET http://localhost:8080/api/data/fetch?id=123
```

#### 2. Get Data Details
```bash
GET http://localhost:8080/api/data/details/{itemId}
```

Example:
```bash
curl -X GET http://localhost:8080/api/data/details/456
```

### OperationController Endpoints

#### 1. Submit Operation
```bash
POST http://localhost:8080/api/operations/submit
Content-Type: application/json

{
  "name": "operation1",
  "value": "test"
}
```

Example:
```bash
curl -X POST http://localhost:8080/api/operations/submit \
  -H "Content-Type: application/json" \
  -d '{"name":"operation1","value":"test"}'
```

#### 2. Process Operation
```bash
POST http://localhost:8080/api/operations/process/{operationType}
Content-Type: application/json

{
  "input1": 10,
  "input2": 20
}
```

Example:
```bash
curl -X POST http://localhost:8080/api/operations/process/calculate \
  -H "Content-Type: application/json" \
  -d '{"input1":10,"input2":20}'
```

#### 3. Get Operation Status
```bash
GET http://localhost:8080/api/operations/status/{operationId}
```

Example:
```bash
curl -X GET http://localhost:8080/api/operations/status/op123
```

## Response Format

All endpoints return responses in the following format:

```json
{
  "message": "Success",
  "data": { ... },
  "status": "OK"
}
```

## Health Check

Spring Boot Actuator is included for health checks:

```bash
GET http://localhost:8080/actuator/health
```

## Customization

### Adding More Endpoints

1. Create a new controller in `src/main/java/com/example/restservice/controller/`
2. Use the `@RestController` and `@RequestMapping` annotations
3. Inject the `ExternalApiService` to call your external API

### Modifying External API Calls

The `ExternalApiService` class provides two main methods:
- `getFromExternalApi(String endpoint)` - for GET requests
- `postToExternalApi(String endpoint, Object requestBody)` - for POST requests

You can extend this service to add more HTTP methods (PUT, DELETE, etc.) as needed.

### Jetty Configuration

Jetty settings can be customized in `application.properties`:

```properties
server.jetty.threads.min=10
server.jetty.threads.max=200
server.jetty.connection-idle-timeout=30000
```

## Dependencies

- Spring Boot 3.1.5
- Spring Web (without Tomcat)
- Spring Boot Jetty Starter
- Spring Boot Actuator
- Lombok (optional)

## License

This project is provided as-is for educational and development purposes.

