# ---------- Stage 1: Build ----------
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy source code and Maven wrapper files
COPY . .

# Build the Spring Boot application (skip tests for speed)
RUN ./mvnw clean package -DskipTests

# ---------- Stage 2: Run ----------
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy only the built JAR from builder stage
COPY --from=builder /app/target/email-writer-sp-0.0.1-SNAPSHOT.jar ./app.jar

# Expose backend port
EXPOSE 8000

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]

