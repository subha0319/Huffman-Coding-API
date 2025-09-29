# Use a specific version of Maven with a JDK to build the application
FROM maven:3.8-openjdk-17 AS build

# Copy the entire backend project into the container
COPY . .

# Run the Maven package command to build the .jar file
RUN mvn clean package -DskipTests

# Use a lightweight Java runtime for the final image
FROM openjdk:17-jdk-slim

# Copy the built .jar file from the 'build' stage
COPY --from=build /target/huffman-api-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the application runs on
EXPOSE 8080

# The command to run when the container starts
ENTRYPOINT ["java","-jar","/app.jar"]