# Use the official Maven image to build the application
# https://hub.docker.com/_/maven
FROM maven:3.9-amazoncorretto-17 AS build

RUN export GOPROXY=""
# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml and the source code to the container
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Use the official OpenJDK image to run the application
# https://hub.docker.com/_/openjdk
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the jar file from the build stage
COPY --from=build /app/target/*.jar /app/build.jar

# Expose the port your application runs on
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "/app/build.jar"]