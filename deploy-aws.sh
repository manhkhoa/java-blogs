#!/bin/bash

# Java Blogs AWS Deployment Script
# This script helps deploy the application to AWS EC2

set -e

echo "🚀 Starting Java Blogs AWS Deployment..."

# Configuration
APP_NAME="java-blogs"
JAR_FILE="target/java-blogs-0.0.1-SNAPSHOT.jar"
EC2_USER="ec2-user"
EC2_HOST=""
EC2_KEY=""
RDS_ENDPOINT=""
RDS_USERNAME=""
RDS_PASSWORD=""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if required tools are installed
check_prerequisites() {
    print_status "Checking prerequisites..."
    
    if ! command -v mvn &> /dev/null; then
        print_error "Maven is not installed. Please install Maven first."
        exit 1
    fi
    
    if ! command -v java &> /dev/null; then
        print_error "Java is not installed. Please install Java 17+ first."
        exit 1
    fi
    
    if ! command -v scp &> /dev/null; then
        print_error "SCP is not available. Please install OpenSSH client."
        exit 1
    fi
    
    print_status "Prerequisites check passed!"
}

# Build the application
build_app() {
    print_status "Building the application..."
    
    if [ ! -f "pom.xml" ]; then
        print_error "pom.xml not found. Please run this script from the project root directory."
        exit 1
    fi
    
    mvn clean package -DskipTests
    
    if [ ! -f "$JAR_FILE" ]; then
        print_error "Build failed. JAR file not found."
        exit 1
    fi
    
    print_status "Application built successfully!"
}

# Deploy to EC2
deploy_to_ec2() {
    if [ -z "$EC2_HOST" ] || [ -z "$EC2_KEY" ]; then
        print_warning "EC2 configuration not set. Please configure EC2_HOST and EC2_KEY variables."
        return 1
    fi
    
    print_status "Deploying to EC2 instance: $EC2_HOST"
    
    # Upload JAR file
    print_status "Uploading JAR file..."
    scp -i "$EC2_KEY" "$JAR_FILE" "$EC2_USER@$EC2_HOST:/home/$EC2_USER/"
    
    # Upload production properties
    if [ -f "src/main/resources/application-prod.properties" ]; then
        print_status "Uploading production configuration..."
        scp -i "$EC2_KEY" "src/main/resources/application-prod.properties" "$EC2_USER@$EC2_HOST:/home/$EC2_USER/"
    fi
    
    # Upload deployment script
    print_status "Uploading deployment script..."
    scp -i "$EC2_KEY" "deploy-ec2.sh" "$EC2_USER@$EC2_HOST:/home/$EC2_USER/"
    
    print_status "Deployment files uploaded successfully!"
}

# Create systemd service file
create_service_file() {
    cat > javablogs.service << EOF
[Unit]
Description=Java Blogs Application
After=network.target

[Service]
Type=simple
User=$EC2_USER
WorkingDirectory=/home/$EC2_USER
ExecStart=/usr/bin/java -jar java-blogs-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
Restart=on-failure
RestartSec=10
Environment="SPRING_DATASOURCE_URL=jdbc:mysql://$RDS_ENDPOINT:3306/javablogs?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"
Environment="SPRING_DATASOURCE_USERNAME=$RDS_USERNAME"
Environment="SPRING_DATASOURCE_PASSWORD=$RDS_PASSWORD"
Environment="SERVER_PORT=8080"

[Install]
WantedBy=multi-user.target
EOF

    print_status "Systemd service file created: javablogs.service"
}

# Main deployment process
main() {
    print_status "Starting deployment process..."
    
    # Check prerequisites
    check_prerequisites
    
    # Build application
    build_app
    
    # Create service file if RDS details are provided
    if [ ! -z "$RDS_ENDPOINT" ] && [ ! -z "$RDS_USERNAME" ] && [ ! -z "$RDS_PASSWORD" ]; then
        create_service_file
        print_status "RDS configuration detected. Service file created with database connection."
    else
        print_warning "RDS configuration not provided. Using default local database settings."
    fi
    
    # Deploy to EC2 if configured
    if deploy_to_ec2; then
        print_status "Deployment completed successfully!"
        print_status "Next steps:"
        echo "1. SSH to your EC2 instance: ssh -i $EC2_KEY $EC2_USER@$EC2_HOST"
        echo "2. Make the deployment script executable: chmod +x deploy-ec2.sh"
        echo "3. Run the deployment script: ./deploy-ec2.sh"
        if [ ! -z "$RDS_ENDPOINT" ]; then
            echo "4. Copy the service file: sudo cp javablogs.service /etc/systemd/system/"
            echo "5. Enable and start the service:"
            echo "   sudo systemctl daemon-reload"
            echo "   sudo systemctl enable javablogs"
            echo "   sudo systemctl start javablogs"
        fi
    else
        print_warning "EC2 deployment skipped. JAR file is ready for manual deployment."
    fi
    
    print_status "Deployment process completed!"
}

# Check if script is run with arguments
if [ "$1" = "configure" ]; then
    echo "🔧 Configuration Mode"
    echo "Please set the following environment variables:"
    echo "export EC2_HOST='your-ec2-public-ip'"
    echo "export EC2_KEY='path/to/your-key.pem'"
    echo "export RDS_ENDPOINT='your-rds-endpoint'"
    echo "export RDS_USERNAME='your-rds-username'"
    echo "export RDS_PASSWORD='your-rds-password'"
    echo ""
    echo "Then run: ./deploy-aws.sh"
    exit 0
fi

# Run main deployment
main
