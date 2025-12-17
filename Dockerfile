FROM eclipse-temurin:17-jdk-alpine AS builder

COPY ./src src/
COPY ./pom.xml pom.xml

RUN mvn clean install

FROM eclipse-temurin:17-jre-alpine

COPY --from=builder target/*.jar app.jar
EXPOSE 8080

CMD ["java", "-jar", "app.jar"]