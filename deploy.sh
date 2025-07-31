#!/bin/bash

set -e

GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m'

log_info() {
    echo -e "${BLUE}ℹ️  [INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}✅ [SUCCESS]${NC} $1"
}

log_error() {
    echo -e "${RED}❌ [ERROR]${NC} $1"
}

print_header() {
    echo ""
    echo -e "${BLUE}╔══════════════════════════════════════════════════════════════╗${NC}"
    echo -e "${BLUE}║${NC}  🚀 ${BLUE}Spring Boot Microservices - Book System${NC}           ${BLUE}║${NC}"
    echo -e "${BLUE}╚══════════════════════════════════════════════════════════════╝${NC}"
    echo ""
}

# Check prerequisites
check_prerequisites() {
    log_info "Checking prerequisites..."
    
    # Check Java
    if ! command -v java &> /dev/null; then
        log_error "Java not found. Please install Java 17+"
        exit 1
    fi
    
    # Check Maven
    if ! command -v mvn &> /dev/null; then
        log_error "Maven not found. Please install Maven"
        exit 1
    fi
    
    # Check MongoDB
    if ! docker ps | grep -q mongodb; then
        log_error "MongoDB not running. Start with: docker run -d -p 27017:27017 --name mongodb mongo"
        exit 1
    fi
    
    log_success "All prerequisites met!"
}

# Run tests
run_tests() {
    echo -e "${BLUE}📋 TESTING PHASE${NC}"
    echo -e "${BLUE}─────────────────${NC}"
    echo ""
    
    # Test BookService
    echo -e "${YELLOW}🔍 Testing BookService...${NC}"
    cd bookservice
    if mvn test -q 2>/dev/null; then
        echo -e "   ${GREEN}✓ BookService tests passed${NC}"
    else
        echo -e "   ${RED}✗ BookService tests failed${NC}"
        exit 1
    fi
    cd ..
    echo ""
    
    # Test RecommendedService
    echo -e "${YELLOW}🔍 Testing RecommendedService...${NC}"
    cd recommendedservice
    if mvn test -q 2>/dev/null; then
        echo -e "   ${GREEN}✓ RecommendedService tests passed${NC}"
    else
        echo -e "   ${RED}✗ RecommendedService tests failed${NC}"
        exit 1
    fi
    cd ..
    
    echo ""
    echo -e "${GREEN}🎉 ALL TESTS PASSED!${NC}"
    echo ""
}

# Start services
start_services() {
    echo -e "${BLUE}🔨 BUILD PHASE${NC}"
    echo -e "${BLUE}──────────────${NC}"
    echo ""
    
    # Build both services
    echo -e "${YELLOW}📦 Building BookService...${NC}"
    cd bookservice && mvn clean package -DskipTests -q 2>/dev/null && cd ..
    echo -e "   ${GREEN}✓ BookService built${NC}"
    echo ""
    
    echo -e "${YELLOW}📦 Building RecommendedService...${NC}"
    cd recommendedservice && mvn clean package -DskipTests -q 2>/dev/null && cd ..
    echo -e "   ${GREEN}✓ RecommendedService built${NC}"
    echo ""
    
    echo -e "${GREEN}🎉 BUILD COMPLETED!${NC}"
    echo ""
    echo -e "${BLUE}╔══════════════════════════════════════════════════════════════╗${NC}"
    echo -e "${BLUE}║${NC}  🚀 ${GREEN}READY TO RUN!${NC}                                      ${BLUE}║${NC}"
    echo -e "${BLUE}╠══════════════════════════════════════════════════════════════╣${NC}"
    echo -e "${BLUE}║${NC}  ${YELLOW}Start the services:${NC}                                   ${BLUE}║${NC}"
    echo -e "${BLUE}║${NC}  1. Terminal 1: cd bookservice && mvn spring-boot:run    ${BLUE}║${NC}"
    echo -e "${BLUE}║${NC}  2. Terminal 2: cd recommendedservice && mvn spring-boot:run ${BLUE}║${NC}"
    echo -e "${BLUE}║${NC}                                                            ${BLUE}║${NC}"
    echo -e "${BLUE}║${NC}  ${YELLOW}Test the APIs:${NC}                                        ${BLUE}║${NC}"
    echo -e "${BLUE}║${NC}  • POST http://localhost:8080/api/books                   ${BLUE}║${NC}"
    echo -e "${BLUE}║${NC}  • GET  http://localhost:8080/api/books                   ${BLUE}║${NC}"
    echo -e "${BLUE}║${NC}  • GET  http://localhost:8081/api/recommended             ${BLUE}║${NC}"
    echo -e "${BLUE}╚══════════════════════════════════════════════════════════════╝${NC}"
    echo ""
}

# Cleanup
cleanup() {
    log_info "Cleaning up..."
    cd bookservice && mvn clean -q && cd ..
    cd recommendedservice && mvn clean -q && cd ..
    log_success "Cleanup completed!"
}

# Help
show_help() {
    print_header
    echo -e "${YELLOW}Usage:${NC}"
    echo -e "  ./deploy.sh          - Run tests and build services"
    echo -e "  ./deploy.sh test     - Run tests only"
    echo -e "  ./deploy.sh cleanup  - Clean build files"
    echo ""
    echo -e "${YELLOW}Prerequisites:${NC}"
    echo -e "  • Java 17+"
    echo -e "  • Maven"
    echo -e "  • MongoDB: docker run -d -p 27017:27017 --name mongodb mongo"
    echo ""
}

# Main
main() {
    case "${1:-deploy}" in
        "test")
            print_header
            check_prerequisites
            run_tests
            ;;
        "cleanup")
            print_header
            cleanup
            ;;
        "deploy"|"")
            print_header
            check_prerequisites
            run_tests
            start_services
            ;;
        "help"|"-h"|"--help")
            show_help
            ;;
        *)
            log_error "Unknown command: $1"
            show_help
            exit 1
            ;;
    esac
}

main "$@"