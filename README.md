# 🔥 Java Blogs - Spring Boot Blog Application

A modern blog platform built with **Spring Boot 3.2.0**, **MySQL**, and **Thymeleaf**. This application provides a complete blogging system with user management, role-based access control, and a beautiful responsive UI.

## ✨ Features

- **User Management**: Admin can create, edit, and delete users
- **Role-Based Access**: Admin and User roles with different permissions
- **Blog Management**: Create, edit, and delete blog posts
- **Responsive Design**: Modern UI built with Bootstrap 5
- **Search Functionality**: Search through blog posts
- **Security**: Spring Security integration with password encryption
- **Database**: MySQL with JPA/Hibernate

## 🚀 Technology Stack

- **Backend**: Spring Boot 3.2.0, Spring Security, Spring Data JPA
- **Database**: MySQL 8.0
- **Frontend**: Thymeleaf, Bootstrap 5, Font Awesome
- **Build Tool**: Maven
- **Java Version**: 17+

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- Git

## 🛠️ Local Development Setup

### 1. Clone the Repository
```bash
git clone <your-repo-url>
cd java-blogs
```

### 2. Database Setup
Create a MySQL database:
```sql
CREATE DATABASE javablogs;
CREATE USER 'javablogs_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON javablogs.* TO 'javablogs_user'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Configure Database Connection
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/javablogs?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=javablogs_user
spring.datasource.password=your_password
```

### 4. Build and Run
```bash
# Clean and build
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### 5. Default Users
- **Admin**: `admin` / `admin123`
- **Demo User**: `demo` / `demo123`

## 🌐 AWS Deployment

### EC2 Instance Setup

1. **Launch EC2 Instance**
   - Choose Amazon Linux 2 or Ubuntu
   - Instance Type: t2.micro (free tier) or t3.small
   - Security Group: Allow HTTP (80), HTTPS (443), SSH (22)

2. **Install Java and Maven**
```bash
# Amazon Linux 2
sudo yum update -y
sudo yum install java-17-amazon-corretto -y
sudo yum install maven -y

# Ubuntu
sudo apt update
sudo apt install openjdk-17-jdk -y
sudo apt install maven -y
```

3. **Install MySQL (if not using RDS)**
```bash
# Amazon Linux 2
sudo yum install mysql -y
sudo systemctl start mysqld
sudo systemctl enable mysqld

# Ubuntu
sudo apt install mysql-server -y
sudo systemctl start mysql
sudo systemctl enable mysql
```

### RDS Setup (Recommended)

1. **Create RDS Instance**
   - Engine: MySQL 8.0
   - Instance Class: db.t3.micro (free tier)
   - Storage: 20 GB
   - Multi-AZ: No (for free tier)
   - Publicly Accessible: Yes (for development)

2. **Configure Security Group**
   - Allow inbound MySQL (3306) from EC2 security group

3. **Update Application Properties**
```properties
spring.datasource.url=jdbc:mysql://your-rds-endpoint:3306/javablogs?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=your_rds_username
spring.datasource.password=your_rds_password
```

### Deployment Steps

1. **Upload Application**
```bash
# On your local machine
mvn clean package
scp -i your-key.pem target/java-blogs-0.0.1-SNAPSHOT.jar ec2-user@your-ec2-ip:/home/ec2-user/
```

2. **Run Application**
```bash
# On EC2 instance
java -jar java-blogs-0.0.1-SNAPSHOT.jar
```

3. **Run as Service (Optional)**
```bash
# Create systemd service
sudo nano /etc/systemd/system/javablogs.service
```

```ini
[Unit]
Description=Java Blogs Application
After=network.target

[Service]
Type=simple
User=ec2-user
ExecStart=/usr/bin/java -jar /home/ec2-user/java-blogs-0.0.1-SNAPSHOT.jar
Restart=on-failure

[Install]
WantedBy=multi-user.target
```

```bash
sudo systemctl daemon-reload
sudo systemctl enable javablogs
sudo systemctl start javablogs
```

## 🔧 Configuration

### Environment Variables
```bash
export SPRING_DATASOURCE_URL=jdbc:mysql://your-db-host:3306/javablogs
export SPRING_DATASOURCE_USERNAME=your_username
export SPRING_DATASOURCE_PASSWORD=your_password
export SERVER_PORT=8080
```

### Production Settings
Update `application.properties` for production:
```properties
# Disable debug logging
logging.level.com.example.javablogs=INFO
logging.level.org.springframework.security=INFO

# Enable Thymeleaf cache
spring.thymeleaf.cache=true

# Database connection pooling
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
```

## 📁 Project Structure

```
java-blogs/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/javablogs/
│   │   │       ├── config/          # Configuration classes
│   │   │       ├── controller/      # REST controllers
│   │   │       ├── entity/          # JPA entities
│   │   │       ├── repository/      # Data repositories
│   │   │       └── service/         # Business logic
│   │   └── resources/
│   │       ├── templates/           # Thymeleaf templates
│   │       └── application.properties
├── pom.xml
└── README.md
```

## 🔐 Security

- **Spring Security** with form-based authentication
- **BCrypt** password encryption
- **Role-based access control** (ADMIN, USER)
- **CSRF protection** (can be disabled for development)

## 🧪 Testing

```bash
# Run tests
mvn test

# Run with coverage
mvn jacoco:report
```

## 📊 Monitoring

- **Health checks**: `/actuator/health`
- **Metrics**: `/actuator/metrics`
- **Application info**: `/actuator/info`

## 🚀 Performance Tips

1. **Database Indexing**: Add indexes on frequently queried columns
2. **Connection Pooling**: Configure HikariCP settings
3. **Caching**: Enable Thymeleaf cache in production
4. **CDN**: Use CDN for static assets (Bootstrap, Font Awesome)

## 🐛 Troubleshooting

### Common Issues

1. **Database Connection Failed**
   - Check MySQL service status
   - Verify connection credentials
   - Ensure firewall allows connections

2. **Port Already in Use**
   - Change port in `application.properties`
   - Kill process using the port: `sudo lsof -ti:8080 | xargs kill -9`

3. **Permission Denied**
   - Check file permissions
   - Ensure user has access to application directory

### Logs
```bash
# View application logs
sudo journalctl -u javablogs -f

# View system logs
sudo tail -f /var/log/syslog
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Support

If you encounter any issues:
1. Check the troubleshooting section
2. Review the logs
3. Create an issue with detailed information

## 🔄 Updates

To update the application:
1. Pull latest changes: `git pull origin main`
2. Rebuild: `mvn clean package`
3. Restart service: `sudo systemctl restart javablogs`

---

**Happy Blogging! 🎉**

