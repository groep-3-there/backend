#FROM openjdk:21
#WORKDIR /app
#COPY target/backend-0.0.1-SNAPSHOT.jar .
#EXPOSE 8080
#ENTRYPOINT ["java","-jar","backend-0.0.1-
FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8080
