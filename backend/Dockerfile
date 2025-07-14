# Multi-stage build để giảm kích thước image
FROM maven:3.9.6-amazoncorretto-17 AS build

# Set working directory
WORKDIR /app

# Copy pom.xml và download dependencies trước (layer caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code và build
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM amazoncorretto:17-alpine-jdk

# Install cần thiết packages
RUN apk add --no-cache tzdata

# Set timezone
ENV TZ=Asia/Ho_Chi_Minh

# Create app user (security best practice)
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# Set working directory
WORKDIR /app

# Copy JAR file từ build stage
COPY --from=build /app/target/*.jar app.jar

# Copy credentials.json nếu có
COPY --chown=appuser:appgroup src/main/resources/credentials.json ./credentials.json

# Tạo thư mục logs
RUN mkdir -p /app/logs && chown appuser:appgroup /app/logs

# Switch to non-root user
USER appuser

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Environment variables
ENV SPRING_PROFILES_ACTIVE=docker
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Run application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
