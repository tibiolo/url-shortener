# 1st stage: Build
FROM eclipse-temurin:25-jdk AS build

WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

RUN ./mvnw dependency:go-offline -B

COPY src ./src

RUN ./mvnw clean package -DskipTests

# 2nd stage: Runtime
FROM eclipse-temurin:25-jre

WORKDIR /app

# Add a non-root user and group
RUN groupadd --system spring && useradd --system --gid spring spring

COPY --from=build /app/target/*.jar app.jar

# Run the application as a non-root user
USER spring:spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]