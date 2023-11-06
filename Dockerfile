FROM eclipse-temurin:21-jdk-alpine
VOLUME /app
ARG JAR_FILE
COPY target/backend-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8080
