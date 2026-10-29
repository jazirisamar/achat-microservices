# Image generique : docker build --build-arg MODULE=product-service .
FROM maven:3.8.8-eclipse-temurin-8 AS build
WORKDIR /app
COPY . .
ARG MODULE
RUN mvn -B -q -pl ${MODULE} -am -DskipTests package

FROM eclipse-temurin:8-jre
ARG MODULE
WORKDIR /app
COPY --from=build /app/${MODULE}/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
