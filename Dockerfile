# Stage 1: Build the JAR
FROM maven:3.10.0-eclipse-temurin-25 AS builder
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the JAR
FROM eclipse-temurin:25-jre
COPY --from=builder /build/target/barshelf-api.jar /app.jar
ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]