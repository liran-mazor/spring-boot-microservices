# Spring Boot Microservices - Book System

Two microservices demonstrating Spring Boot, MongoDB, Docker, and Kubernetes.

## Services
- **BookService**: Create and read books (port 3000)
- **RecommendedService**: Returns books sorted by rating (port 3000)

## Quick Start

### Prerequisites
- Docker Desktop with Kubernetes enabled
- Java 17+
- Maven

### Deploy
```bash
./deploy.sh
```

### Test APIs
```bash
# Create a book
curl -X POST http://localhost:3000/api/books \
  -H "Content-Type: application/json" \
  -d '{"title": "Clean Code", "rating": 9}'

# Get all books
curl http://localhost:3000/api/books

# Get recommended books (sorted by rating)
curl http://localhost:3001/api/recommended
```

## Commands
```bash
./deploy.sh           # Deploy everything
./deploy.sh test      # Run tests only
./deploy.sh cleanup   # Remove everything
```

## Structure
```
├── bookservice/        # Book CRUD service
├── recommendedservice/ # Recommendation service  
├── infra/             # Kubernetes manifests
└── deploy.sh          # Deployment script
```