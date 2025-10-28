# Use an official OpenJDK 17 image as the base
FROM eclipse-temurin:17-jdk-jammy

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven wrapper and pom.xml (if you had a wrapper, but this is fine)
# Copy the pom.xml file to download dependencies
COPY pom.xml .

# Copy the source code
COPY src ./src

# Build the application using Maven
# We run 'install' to build the .jar file
RUN mvn clean install -DskipTests

# Expose port 8080 (which our app runs on)
EXPOSE 8080

# The command to run when the container starts
# This is our "Start Command"
ENTRYPOINT ["java", "-jar", "target/lost-and-found-0.0.1-SNAPSHOT.jar"]
