FROM openjdk:11-jdk as builder

RUN apt update && \
    apt install -y maven

WORKDIR /code

COPY . .

RUN mvn -Dmaven.test.skip=true clean package

FROM openjdk:11-jre

# Use the official OpenJDK 11 image as the base image
FROM openjdk:11-jre-slim

# Set the working directory inside the container
WORKDIR /opt/app

# Copy the Spring Boot JAR file from the local build context to the container
COPY --from=builder /code/target/Bom-0.0.1-SNAPSHOT.jar /opt/app/bomapp.jar

# Expose the port that your Spring Boot application listens on
EXPOSE 8093

ENV SPRING_PROFILES_ACTIVE=qa

# Set the entrypoint to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "bomapp.jar"]