# Use an official Maven image to build the project
FROM maven:3.9.4-eclipse-temurin-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and source code into the container
COPY pom.xml .
COPY src ./src

# Package the application
RUN mvn clean package -DskipTests

# Use a lightweight Java image to run the application
FROM eclipse-temurin:17-jre-alpine

# Set the working directory for the application
WORKDIR /app

# Copy the built JAR file from the previous stage
COPY --from=build /app/target/ExampleBackend-0.0.1-SNAPSHOT.jar app.jar

# Expose the application's port (9090)
EXPOSE 9090

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
