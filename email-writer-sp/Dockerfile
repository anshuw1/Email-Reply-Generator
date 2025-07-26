# Use an official OpenJDK image as base
FROM eclipse-temurin:21-jdk

# Set working directory
WORKDIR /app

# Copy Maven build files and source code
COPY . .

# Build the Spring Boot app using Maven (skip tests for speed)
RUN ./mvnw clean package -DskipTests

# Expose the backend port (8000)
EXPOSE 8000

# Run the Spring Boot app
CMD ["java", "-jar", "target/email-writer-sp-0.0.1-SNAPSHOT.jar"]

