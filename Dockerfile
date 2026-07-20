# Stage 1: Build Frontend (React / Vite)
FROM node:20-alpine AS frontend-builder
WORKDIR /app/frontend
COPY frontend/package*.json ./
RUN npm ci
COPY frontend/ ./
RUN npm run build

# Stage 2: Build Backend com Java 25 (conforme configurado em build.gradle)
FROM eclipse-temurin:25-jdk-alpine AS backend-builder
WORKDIR /app
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./
COPY src src
COPY --from=frontend-builder /app/src/main/resources/static src/main/resources/static
RUN chmod +x gradlew && ./gradlew bootJar --no-daemon -x test

# Stage 3: Runtime com Java 25 JRE
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=backend-builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
