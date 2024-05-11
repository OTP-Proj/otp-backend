# Stage 1: Build the application
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
# Resolve dependencies
RUN mvn dependency:go-offline
# Copy the rest of the application and build
COPY . .
RUN mvn package -DskipTests

# Stage 2: Create the runtime image
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8088
CMD ["java", "-jar", "app.jar"]