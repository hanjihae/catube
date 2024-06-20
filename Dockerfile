# Use a base image with Java and Spring Boot installed
FROM openjdk:21-jdk

# Set the working directory in the container
WORKDIR /app

# Copy the build output JAR file to the working directory in the container
COPY build/libs/catube-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the application will run on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]