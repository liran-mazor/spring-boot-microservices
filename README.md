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

### Deploy Everything
```bash
git clone https://github.com/liran-mazor/spring-boot-microservices.git
cd spring-boot-microservices
chmod +x deploy.sh
./deploy.sh
```

### Test APIs
```bash
# Create a book
curl -X POST http://books.dev/api/books \
  -H "Content-Type: application/json" \
  -d '{"title": "Clean Code", "rating": 9}'

# Get all books
curl http://books.dev/api/books

# Get recommended books (sorted by rating)
curl http://books.dev/api/recommended
```

## Commands
```bash
./deploy.sh           # Deploy everything
./deploy.sh test      # Run tests only
./deploy.sh cleanup   # Remove everything
```

## Structure
```
├── bookservice/        
├── recommendedservice/ 
├── infra/             # Kubernetes manifests
└── deploy.sh          # Deployment script
```
