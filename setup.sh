#!/bin/bash

# AegisNav Project Build & Setup Script
# This script prepares the Android project for development and production build

set -e

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$PROJECT_DIR"

echo "╔════════════════════════════════════════════════════════════════╗"
echo "║          AegisNav - GPS-Denied Navigation System              ║"
echo "║              Project Build & Setup Script                     ║"
echo "╚════════════════════════════════════════════════════════════════╝"
echo ""

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Check for required tools
check_requirements() {
    echo -e "${BLUE}[1/5] Checking system requirements...${NC}"
    
    if ! command -v java &> /dev/null; then
        echo -e "${RED}✗ Java not found. Please install Java 17+${NC}"
        exit 1
    fi
    echo -e "${GREEN}✓ Java found: $(java -version 2>&1 | head -1)${NC}"
    
    if ! command -v git &> /dev/null; then
        echo -e "${YELLOW}⚠ Git not found. Some features may not work.${NC}"
    else
        echo -e "${GREEN}✓ Git found${NC}"
    fi
    
    echo ""
}

# Create necessary directories
create_directories() {
    echo -e "${BLUE}[2/5] Creating project directory structure...${NC}"
    
    mkdir -p app/src/main/kotlin/com/aegisnav/{domain/{models,usecases},data/{sensors,services,database/{entities,dao}},ui/{theme,components,screens,viewmodels},di}
    mkdir -p app/src/main/res/{values,xml,layout,drawable,mipmap}
    mkdir -p app/src/test/kotlin/com/aegisnav
    mkdir -p app/src/androidTest/kotlin/com/aegisnav
    
    echo -e "${GREEN}✓ Directory structure created${NC}"
    echo ""
}

# Initialize gradle wrapper
init_gradle() {
    echo -e "${BLUE}[3/5] Initializing Gradle...${NC}"
    
    if [ ! -f "gradlew" ]; then
        echo "Creating Gradle wrapper..."
        gradle wrapper --gradle-version=8.1.4 || true
    fi
    
    chmod +x gradlew 2>/dev/null || true
    echo -e "${GREEN}✓ Gradle initialized${NC}"
    echo ""
}

# Clean and build
build_project() {
    echo -e "${BLUE}[4/5] Building project...${NC}"
    
    ./gradlew clean --no-daemon
    ./gradlew build --no-daemon
    
    echo -e "${GREEN}✓ Project built successfully${NC}"
    echo ""
}

# Summary
print_summary() {
    echo -e "${BLUE}[5/5] Setup complete!${NC}"
    echo ""
    echo -e "${GREEN}╔════════════════════════════════════════════════════════════════╗${NC}"
    echo -e "${GREEN}║                    SETUP SUCCESSFUL!                           ║${NC}"
    echo -e "${GREEN}╚════════════════════════════════════════════════════════════════╝${NC}"
    echo ""
    echo "Next steps:"
    echo "  1. Open Android Studio"
    echo "  2. File → Open → Select $(pwd)/build/outputs"
    echo "  3. Or run: ./gradlew installDebug"
    echo ""
    echo "Build Commands:"
    echo "  • Debug build:   ./gradlew build"
    echo "  • Release build: ./gradlew build -Prelease"
    echo "  • Run tests:     ./gradlew test"
    echo "  • Install APK:   ./gradlew installDebug"
    echo ""
    echo "Project Structure:"
    echo "  • Kotlin sources: app/src/main/kotlin/com/aegisnav/"
    echo "  • Resources:      app/src/main/res/"
    echo "  • Tests:          app/src/test/"
    echo ""
    echo "Documentation: See KOTLIN_DEVELOPMENT_STARTED.md"
    echo ""
}

# Main execution
main() {
    check_requirements
    create_directories
    init_gradle
    # Uncomment below to auto-build
    # build_project
    print_summary
}

# Run main function
main
