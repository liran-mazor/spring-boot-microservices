# Spring Boot Microservices - Book System

Two microservices demonstrating Spring Boot, MongoDB, and REST API communication.

## Architecture

```
┌─────────────┐                            ┌─────────────────┐    ┌───────────┐
│   Client    │──  POST /api/books    ──▶  │   BookService   │───▶│ MongoDB   │
│   Postman   │──  GET /api/books     ──▶  │   Port 8080     │    │           │
│             │──  GET /api/books/{id} ─▶  │                 │    └───────────┘
└─────────────┘                            └────────┬────────┘             
       │                                            │                     
       │                                            │ GET /api/books      
       │                                            │                     
       │                             ┌──────────────────┐            
       └─── GET /api/recommended ───▶│RecommendedService│            
                                     │   Port 8081      │            
                                     └──────────────────┘            

Spring Boot Microservices
```

## Services
- **BookService**: Create and read books with MongoDB (port 8080)
- **RecommendedService**: Returns books sorted by rating via REST calls (port 8081)

## Quick Start

### Prerequisites
- Java 17+
- Maven
- MongoDB (Docker): `docker run -d -p 27017:27017 --name mongodb mongo`

### Deploy Everything
```bash
git clone https://github.com/liran-mazor/spring-boot-microservices.git
cd spring-boot-microservices
chmod +x deploy.sh
./deploy.sh
```

### Start Services Manually
```bash
# Terminal 1: Start BookService
cd bookservice
mvn spring-boot:run

# Terminal 2: Start RecommendedService  
cd recommendedservice
mvn spring-boot:run
```

### Test APIs
```bash
# Create a book
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{"title": "Clean Code", "rating": 9}'

# Get all books
curl http://localhost:8080/api/books

# Get book by ID
curl http://localhost:8080/api/books/1

# Get recommended books (sorted by rating)
curl http://localhost:8081/api/recommended
```

## Commands
```bash
./deploy.sh           # Run tests and build services
./deploy.sh test      # Run tests only (10 unit tests)
./deploy.sh cleanup   # Clean build files
```
