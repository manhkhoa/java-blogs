# 🚀 Quick Start Guide - Java Blogs

Get your Spring Boot blog application running in minutes!

## ⚡ Local Development (5 minutes)

### 1. Prerequisites Check
```bash
# Check Java version (requires 17+)
java -version

# Check Maven
mvn -version

# Check MySQL (or use Docker)
mysql --version
```

### 2. Database Setup
```bash
# Option A: Use Docker (recommended for quick start)
docker-compose up -d mysql

# Option B: Local MySQL
mysql -u root -p
CREATE DATABASE javablogs;
CREATE USER 'javablogs_user'@'localhost' IDENTIFIED BY 'javablogs_password';
GRANT ALL PRIVILEGES ON javablogs.* TO 'javablogs_user'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Run Application
```bash
# Build and run
mvn spring-boot:run

# Or build first, then run
mvn clean package
java -jar target/java-blogs-0.0.1-SNAPSHOT.jar
```

### 4. Access Application
- **Homepage**: http://localhost:8080
- **Blog**: http://localhost:8080/blog
- **Login**: http://localhost:8080/login

### 5. Default Users
- **Admin**: `admin` / `admin123`
- **User**: `demo` / `demo123`

## 🐳 Docker Quick Start (3 minutes)

### 1. Start Everything
```bash
docker-compose up -d
```

### 2. Access Application
- **Application**: http://localhost:8080
- **MySQL**: localhost:3306

### 3. Stop Everything
```bash
docker-compose down
```

## ☁️ AWS Deployment (15 minutes)

### 1. Prerequisites
- AWS Account
- EC2 Instance (t2.micro free tier)
- RDS MySQL Instance (db.t3.micro free tier)
- Security Groups configured

### 2. Configure Environment
```bash
# Set your AWS details
export EC2_HOST='your-ec2-public-ip'
export EC2_KEY='path/to/your-key.pem'
export RDS_ENDPOINT='your-rds-endpoint'
export RDS_USERNAME='your-rds-username'
export RDS_PASSWORD='your-rds-password'
```

### 3. Deploy
```bash
# Run deployment script
./deploy-aws.sh

# Or configure first
./deploy-aws.sh configure
```

### 4. Access Your App
- **Application**: http://your-ec2-public-ip:8080

## 🔧 Configuration

### Database Connection
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/javablogs
spring.datasource.username=javablogs_user
spring.datasource.password=javablogs_password
```

### Production Settings
Use `application-prod.properties` for production deployment.

## 📱 Features to Test

### Admin Features
1. **Login** as admin (admin/admin123)
2. **Dashboard** - View statistics
3. **User Management** - Add/edit/delete users
4. **Post Management** - Create/edit/delete posts

### User Features
1. **Login** as user (demo/demo123)
2. **Dashboard** - View your posts
3. **Create Posts** - Write new blog posts
4. **Edit Posts** - Modify your content

### Public Features
1. **Homepage** - Landing page
2. **Blog List** - Browse all posts
3. **Search** - Find specific content
4. **Responsive Design** - Test on mobile

## 🐛 Troubleshooting

### Common Issues

1. **Port 8080 already in use**
   ```bash
   # Kill process using port 8080
   sudo lsof -ti:8080 | xargs kill -9
   
   # Or change port in application.properties
   server.port=8081
   ```

2. **Database connection failed**
   ```bash
   # Check MySQL service
   sudo systemctl status mysql
   
   # Check connection details
   mysql -u javablogs_user -p javablogs
   ```

3. **Build failed**
   ```bash
   # Clean and rebuild
   mvn clean install
   
   # Check Java version
   java -version
   ```

### Logs
```bash
# Application logs
tail -f logs/spring.log

# Docker logs
docker-compose logs -f javablogs
```

## 🚀 Next Steps

1. **Customize Design** - Modify Thymeleaf templates
2. **Add Features** - Comments, categories, tags
3. **Enhance Security** - OAuth2, JWT tokens
4. **Scale** - Load balancer, multiple instances
5. **Monitor** - CloudWatch, application metrics

## 📚 Learning Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security Reference](https://docs.spring.io/spring-security/reference/)
- [Thymeleaf Tutorial](https://www.thymeleaf.org/documentation.html)
- [MySQL Documentation](https://dev.mysql.com/doc/)

---

**Happy Coding! 🎉**

Need help? Check the main README.md for detailed documentation.
