# Use an official Maven image as the base image
FROM maven:3.8.4-jdk-11 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and the source code to the container
COPY pom.xml .
COPY src/ ./src/

# Build the project using Maven
RUN mvn clean
RUN mvn package


# Use a lightweight JRE image as the base image for running the application
FROM openjdk:11-jre-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file built in the previous stage to the container
COPY --from=build /app/target/rummikub2-1.0-SNAPSHOT-jar-with-dependencies.jar ./app.jar

# Expose port 5000 for the application to listen on
EXPOSE 5000

# Run the application when the container starts
CMD ["java", "-jar", "app.jar"]