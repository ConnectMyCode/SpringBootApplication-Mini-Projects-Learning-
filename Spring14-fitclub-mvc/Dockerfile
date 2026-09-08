# ================= STAGE 1: build the jar =================
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# ================= STAGE 2: run the jar =================
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/fitclub-mvc-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
