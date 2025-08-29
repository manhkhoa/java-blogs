# Use OpenJDK 17 as base image
FROM openjdk:17-jre-slim

# Set working directory
WORKDIR /app

# Copy the JAR file
COPY target/java-blogs-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080
EXPOSE 8080

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=docker

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

