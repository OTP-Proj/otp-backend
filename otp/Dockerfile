FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY . .
RUN mvn clean package -DskipTests
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
#ARG KEYSTORE_LOCATION
#ENV KEYSTORE_LOCATION ${KEYSTORE_LOCATION}
#COPY keystore.p12 /app/keystore.p12
EXPOSE 8089
CMD ["java", "-jar", "app.jar"]