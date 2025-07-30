#!/bin/bash

# Simple deployment script for Spring Boot microservices
set -e

NAMESPACE="books-system"

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m'

log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Run tests
run_tests() {
    log_info "Running unit tests..."
    
    cd bookservice
    mvn test
    cd ..
    
    cd recommendedservice
    mvn test
    cd ..
    
    log_success "All tests passed!"
}

# Deploy everything
deploy_all() {
    log_info "Building and deploying..."
    
    # Build applications
    log_info "Building BookService..."
    cd bookservice
    mvn clean package -DskipTests
    docker build -t book-service:latest .
    cd ..
    
    log_info "Building RecommendedService..."
    cd recommendedservice
    mvn clean package -DskipTests
    docker build -t recommended-service:latest .
    cd ..
    
    # Deploy to Kubernetes
    log_info "Deploying to Kubernetes..."
    kubectl create namespace ${NAMESPACE} --dry-run=client -o yaml | kubectl apply -f -
    
    kubectl apply -f infra/ -n ${NAMESPACE}
    
    log_success "Deployment completed!"
    
    # Show access info
    echo ""
    log_info "To access services:"
    echo "kubectl port-forward service/books-srv 3000:3000 -n ${NAMESPACE}"
    echo "kubectl port-forward service/recommended-srv 3001:3000 -n ${NAMESPACE}"
}

# Cleanup
cleanup() {
    log_info "Cleaning up..."
    kubectl delete namespace ${NAMESPACE} --ignore-not-found=true
    docker rmi book-service:latest 2>/dev/null || true
    docker rmi recommended-service:latest 2>/dev/null || true
    log_success "Cleanup completed!"
}

# Main logic
case "${1:-deploy}" in
    "test")
        run_tests
        ;;
    "cleanup")
        cleanup
        ;;
    "deploy"|"")
        run_tests
        deploy_all
        ;;
    *)
        echo "Usage: $0 [test|cleanup]"
        echo "  (no args) - Deploy everything"
        echo "  test      - Run tests only"
        echo "  cleanup   - Remove everything"
        exit 1
        ;;
esac