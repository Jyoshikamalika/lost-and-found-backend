# --- STAGE 1: The Builder ---
# Use an official Maven image that includes JDK 17
# This is the line we fixed (removed "-jammy")
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Set the working directory
WORKDIR /app

# Copy the pom.xml and download dependencies first
# This is a cache optimization
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of the source code
COPY src ./src

# Build the application and create the .jar file
# We skip tests to speed up the build
RUN mvn clean package -DskipTests

# --- STAGE 2: The Runner ---
# Use a lightweight JRE (Java Runtime Environment) image
FROM eclipse-temurin:17-jre-jammy

# Set the working directory
WORKDIR /app

# Copy the compiled .jar file from the 'builder' stage
# The path is target/lost-and-found-0.0.1-SNAPSHOT.jar
COPY --from=builder /app/target/lost-and-found-0.0.1-SNAPSHOT.jar ./app.jar

# Expose port 8080 (which our app runs on)
EXPOSE 8080

# The command to run when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]

