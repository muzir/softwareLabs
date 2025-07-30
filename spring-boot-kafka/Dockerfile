# Use OpenJDK 21 with Debian
FROM openjdk:21-jdk-slim

# Copy the application JAR file into the container
COPY build/libs/spring-boot-kafka.jar ./service.jar

# Set the entry point to run the application
ENTRYPOINT ["java", "-jar", "/service.jar"]